package com.troveup.brooklyn.sdk.print.shapeways.business;

import com.google.appengine.api.memcache.stdimpl.GCacheFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.troveup.brooklyn.orm.cart.interfaces.ICartAccessor;
import com.troveup.brooklyn.orm.materials.interfaces.IMaterialFilterAccessor;
import com.troveup.brooklyn.orm.materials.model.Finish;
import com.troveup.brooklyn.orm.materials.model.FinishPriceMap;
import com.troveup.brooklyn.orm.materials.model.Material;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.user.model.Address;
import com.troveup.brooklyn.sdk.cache.interfaces.ICacheService;
import com.troveup.brooklyn.sdk.print.interfaces.IPrintSupplier;
import com.troveup.brooklyn.sdk.print.model.OrderStatus;
import com.troveup.brooklyn.sdk.print.model.PriceRequest;
import com.troveup.brooklyn.sdk.print.shapeways.api.ShapewaysModelApi;
import com.troveup.brooklyn.sdk.print.shapeways.api.ShapewaysOrdersApi;
import com.troveup.brooklyn.sdk.print.shapeways.api.ShapewaysPriceApi;
import com.troveup.brooklyn.sdk.print.shapeways.exception.ShapewaysUnimplementedException;
import com.troveup.brooklyn.sdk.print.shapeways.model.ShapewaysCancelOrderRequest;
import com.troveup.brooklyn.sdk.print.shapeways.model.ShapewaysGetOrderStatusRequest;
import com.troveup.brooklyn.sdk.print.shapeways.model.ShapewaysItem;
import com.troveup.brooklyn.sdk.print.shapeways.model.ShapewaysOrderRequest;
import com.troveup.brooklyn.util.MoneyUtil;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by tim on 7/3/15.
 */
public class ShapewaysPrintSupplier implements IPrintSupplier
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ICacheService cacheService;

    @Autowired
    IMaterialFilterAccessor materialFilterAccessor;

    @Autowired
    ShapewaysPriceApi priceApi;

    @Autowired
    ICartAccessor cartAccessor;

    @Autowired
    ShapewaysModelApi shapewaysModelApi;

    @Autowired
    ShapewaysOrdersApi shapewaysOrdersApi;

    @Autowired
    Gson gson;

    @Override
    public Object uploadModel(Long troveItemId) {
        throw new ShapewaysUnimplementedException();
    }

    @Override
    public Object uploadModel(String modelCdnFilepath)
    {
        throw new ShapewaysUnimplementedException();
    }

    @Override
    public Object uploadModel(byte[] model, String modelName)
    {
        Object rval = null;
        byte[] encodedByteArray = Base64.encode(model);

        //TODO:  Verify that 3 times is enough with a 5 second wait if this fails
        for (int i = 0; i < 3; ++i) {
            try {
                rval = shapewaysModelApi.uploadModel(new String(encodedByteArray, "UTF-8"), modelName);
            } catch (Exception e) {
                logError(e);
            }

            if (rval != null)
                break;
            else
            {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    //Eat the exception
                }
            }
        }

        return rval;
    }

    @Override
    public Object checkUploadStatus(Integer modelId)
    {
        //TODO:  Verify that 3 times is enough with a 5 second wait if this fails
        Object rval = null;

        for (int i = 0; i < 3; ++i) {
            try {
                rval = shapewaysModelApi.getModelStatus(modelId.toString());
            } catch (Exception e) {
                logError(e);
            }

            if (rval != null)
                break;
            else
            {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    //Eat the exception
                }
            }

        }

        return rval;
    }

    @Override
    public Object getInstantPrice(PriceRequest request) {
        throw new ShapewaysUnimplementedException();
    }

    @Override
    public Money getInstantPrice(String materialId, String finishId, Float volume)
    {
        Money rval = Money.zero(MoneyUtil.toCurrencyUnit("USD"));
        List<Material> materials = getSupplierMaterials();

        Material material = Material.getMaterialByMaterialId(materialId, materials);

        if (material != null) {
            logger.debug("Material ID for object found was " + material.getMaterialId());

            if (material.getFinishList() != null)
                logger.debug("Finish list came back with size of " + material.getFinishList().size());
            else
                logger.debug("Finish list came back as null for the material!");
        }
        else
            logger.debug("Object not found!  MaterialId that came to the instant pricing was " + materialId + " and " +
                    "size of materials after getting was " + materials.size());
        Finish finish = Finish.getFinishByFinishId(finishId, material.getFinishList());

        List<FinishPriceMap> priceMap = finish.getPriceMapping();

        if (priceMap != null)
            logger.debug("priceMap came back with size of " + priceMap.size());
        else
            logger.debug("priceMap was null!");

        //Tiered price
        if (priceMap.size() > 0 && priceMap.get(0).getTiered())
        {
            //Default to lowest in case something goes wrong
            FinishPriceMap mapToApply = priceMap.get(0);

            for (FinishPriceMap map : priceMap)
            {
                //Top level tier, max volume can be null in this case, compare it against something really high
                if (map.getMaxVolume() == null)
                {
                    if (volume >= map.getMinVolume() && volume < 1000f)
                    {
                        mapToApply = map;
                    }
                }
                else
                {
                    if (volume >= map.getMinVolume() && volume < map.getMaxVolume())
                    {
                        mapToApply = map;
                    }
                }
            }

            logger.debug("Tiered price, adding flat fee of " + mapToApply);
            rval = rval.plus(mapToApply.getFlatFee());
            logger.debug("rval after adding the flat fee: " + rval.toString());
            logger.debug("Applying multiplicative result of cost per cm^3: " + mapToApply.getCostPerCm3() + " multiplied by volume of " + volume);
            logger.debug("This calculation ends up being: " + mapToApply.getCostPerCm3().
                    multiply(MoneyUtil.floatToBigDecimal(volume, "USD")).toString());
            rval = rval.plus(mapToApply.getCostPerCm3().
                    multiply(MoneyUtil.floatToBigDecimal(volume, "USD")));
            logger.debug("rval after adding multiplied result.");

        }
        //Non-tiered
        else
        {
            FinishPriceMap mapToApply = priceMap.get(0);

            logger.debug("Tiered price, adding flat fee of " + mapToApply);
            rval = rval.plus(mapToApply.getFlatFee(), RoundingMode.HALF_UP);
            logger.debug("rval after adding the flat fee: " + rval.toString());
            logger.debug("Applying multiplicative result of cost per cm^3: " + mapToApply.getCostPerCm3() + " multiplied by volume of " + volume);
            logger.debug("This calculation ends up being: " + mapToApply.getCostPerCm3().
                    multiply(MoneyUtil.floatToBigDecimal(volume, "USD")).toString());
            rval = rval.plus(mapToApply.getCostPerCm3().
                    multiply(MoneyUtil.floatToBigDecimal(volume, "USD")), RoundingMode.HALF_UP);
            logger.debug("rval after adding multiplied result.");
        }

        return rval;
    }

    @Override
    public Object getModelBasedPrice(Long troveItemId, int quantity, Float scale, String currency, Address shippingAddress) {
        throw new ShapewaysUnimplementedException();
    }

    @Override
    public Object getModelBasedPrice(String imaterialiseModelId, int quantity, Float scale, String currency, Address shippingAddress, String materialId, String finishId) {
        throw new ShapewaysUnimplementedException();
    }

    @Override
    public Boolean addModelToOrder(Long troveItemId, Long troveCartId) {
        throw new ShapewaysUnimplementedException();
    }

    @Override
    public Boolean removeModelFromOrder(Long troveUserId, Long troveCartId) {
        throw new ShapewaysUnimplementedException();
    }

    @Override
    public Object getFinalOrderPrice(Long troveOrderId) {
        return null;
    }

    @Override
    public Object submitOrder(Long troveCartId) {
        return null;
    }

    @Override
    public Object submitOrder(String userFirstName, String userLastName, List<ShapewaysItem> items) {
        Object rval = null;

        ShapewaysOrderRequest request = new ShapewaysOrderRequest();
        request.setFirstName("Brian");
        request.setLastName("Park");
        request.setCountry("US");

/*        if (items.get(0).getMaterialId() == 6) {
            request.setState("NY");
            request.setCity("New York");
            request.setAddress1("20 Exchange Place");
            request.setAddress2("Apt 1604");
            request.setZipCode("10005");
        }
        else {
            request.setState("CA");
            request.setCity("Torrance");
            request.setAddress1("2701 Plaza Del Amo");
            request.setAddress2("Suite 706");
            request.setZipCode("90501");
        }*/

        request.setState("NY");
        request.setCity("New York");
        request.setAddress1("20 Exchange Place");
        request.setAddress2("Apt 1604");
        request.setZipCode("10005");

        request.setPhoneNumber("3108096011");

        request.setPaymentVerificationId("TVE-ORDER");
        request.setPaymentMethod("credit_card");
        request.setShippingOption("Cheapest");
        request.setItems(items);

        //TODO:  Verify that 3 times is enough with a 5 second wait if this fails
        for(int i = 0; i < 3; ++i)
        {
            try {
                rval = shapewaysOrdersApi.submitOrder(request);
            } catch (Exception e) {
                logError(e);
            }

            if (rval != null)
                break;
            else {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    //Eat the exception
                }
            }
        }

        return rval;
    }

    @Override
    public Object cancelOrder(String supplierOrderId)
    {
        Object rval = null;

        ShapewaysCancelOrderRequest request = new ShapewaysCancelOrderRequest();

        request.setOrderId(Integer.parseInt(supplierOrderId));
        request.setStatus("cancelled");

        //TODO:  Verify that 3 times is enough with a 5 second wait if this fails
        for (int i = 0; i < 3; ++i) {
            try {
                rval = shapewaysOrdersApi.cancelOrder(request);
            } catch (Exception e) {
                logError(e);
            }
            if (rval != null)
                break;
            else {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    //Eat the exception
                }
            }
        }

        return rval;
    }

    @Override
    public Object cancelOrder(Long troveCartId) {
        return null;
    }

    @Override
    public Object getOrderStatus(String supplierOrderId)
    {
        Object rval = null;

        ShapewaysGetOrderStatusRequest request = new ShapewaysGetOrderStatusRequest();
        request.setOrderId(supplierOrderId);

        //TODO:  Do error catching and retry here
        for (int i = 0; i < 3; ++i) {
            try {
                rval = shapewaysOrdersApi.getOrderStatus(request);
            } catch (Exception e) {
                logError(e);
            }

            if (rval != null)
                break;
            else {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    //Eat the exception
                }
            }
        }

        return rval;
    }

    @Override
    public OrderStatus getOrderStatus(Long troveOrderId) {
        return null;
    }

    @Override
    public Object getOrderStatuses(List<String> supplierOrderId) {
        return null;
    }

    @Override
    public Object getOrderStatusesByTroveOrder(List<Order> troveUserOrders) {
        return null;
    }

    @Override
    public List<Material> getSupplierMaterials()
    {
        String MATERIAL = "MATERIALS";
        String cachedMaterials = cacheService.getCacheValue(MATERIAL);
        List<Material> rval;

        if (cachedMaterials != null && cachedMaterials.length() > 0)
        {
            Type collectionType = new TypeToken<List<Material>>(){}.getType();
            rval = gson.fromJson(cachedMaterials, collectionType);
        }
        else {
            rval = materialFilterAccessor.getAllActiveMaterials();
            List<Material> hardCodedMaterialSet = priceApi.getActiveShapewaysMaterials();

            //New DB instance, populate the table
            if (rval == null || rval.size() == 0) {
                rval = priceApi.getShapewaysMaterials();

                materialFilterAccessor.persistMaterials(rval);

            }

            //A new material has been detected, add it to the table
            else if (rval.size() != hardCodedMaterialSet.size())
            {
                for (Material material : hardCodedMaterialSet)
                {
                    if (!rval.contains(material)) {
                        materialFilterAccessor.persistMaterial(material);
                        rval.add(material);
                    }
                }
            }

            //Remove any invalid finishes before it's cached
            for (Material material : rval)
            {
                List<Finish> refinedList = new ArrayList<>();
                for (Finish finish : material.getFinishList())
                {
                    if (finish.isActive())
                        refinedList.add(finish);
                }

                material.setFinishList(refinedList);
            }

            Map properties = new HashMap<>();
            properties.put(GCacheFactory.EXPIRATION_DELTA, TimeUnit.HOURS.toSeconds(2));
            cacheService.putCache(MATERIAL, gson.toJson(rval), properties);
        }

        //Invert the list from most expensive to cheapest
        Collections.reverse(rval);

        for (Material material : rval)
        {
            if (material.getFinishList() != null && material.getFinishList().size() > 0)
            {
                Collections.reverse(material.getFinishList());
            }
        }

        return rval;
    }

    protected void logError(Exception e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        logger.error("Stack Trace: " + sw.toString());
    }
}
