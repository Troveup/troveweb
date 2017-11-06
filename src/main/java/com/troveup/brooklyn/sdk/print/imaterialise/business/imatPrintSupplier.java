package com.troveup.brooklyn.sdk.print.imaterialise.business;

import com.google.appengine.api.memcache.stdimpl.GCacheFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.troveup.brooklyn.orm.cart.interfaces.ICartAccessor;
import com.troveup.brooklyn.orm.cart.model.Cart;
import com.troveup.brooklyn.orm.cart.model.CartAttribute;
import com.troveup.brooklyn.orm.cart.model.CartItem;
import com.troveup.brooklyn.orm.cart.model.CartItemAttribute;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.countries.model.Subdivision;
import com.troveup.brooklyn.orm.item.interfaces.IItemAccessor;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.item.model.ItemAttribute;
import com.troveup.brooklyn.orm.materials.interfaces.IMaterialFilterAccessor;
import com.troveup.brooklyn.orm.materials.model.Finish;
import com.troveup.brooklyn.orm.materials.model.Material;
import com.troveup.brooklyn.orm.order.interfaces.IOrderAccessor;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.user.model.Address;
import com.troveup.brooklyn.sdk.cache.interfaces.ICacheService;
import com.troveup.brooklyn.sdk.print.imaterialise.api.*;
import com.troveup.brooklyn.sdk.print.imaterialise.model.*;
import com.troveup.brooklyn.sdk.print.interfaces.IPrintSupplier;
import com.troveup.brooklyn.sdk.print.model.OrderStatus;
import com.troveup.brooklyn.sdk.print.model.PriceRequest;
import com.troveup.brooklyn.sdk.print.shapeways.model.ShapewaysItem;
import com.troveup.brooklyn.util.MoneyUtil;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by tim on 5/22/15.
 */
public class imatPrintSupplier implements IPrintSupplier
{
    @Autowired
    IItemAccessor itemAccessor;

    @Autowired
    imatUploadModelApi uploadApi;

    @Autowired
    imatPriceCheckApi priceCheckApi;

    @Autowired
    imatOrderApi cartApi;

    @Autowired
    imatInfoApi infoApi;

    @Autowired
    ICartAccessor cartAccessor;

    @Autowired
    IOrderAccessor orderAccessor;

    @Autowired
    imatCancelOrderApi cancelOrderApi;

    @Autowired
    ICacheService cacheService;

    @Autowired
    IMaterialFilterAccessor materialFilterAccessor;

    @Autowired
    Gson gson;



    private String cdnUrlPath;

    public imatPrintSupplier(String cdnUrlPath)
    {
        this.cdnUrlPath = cdnUrlPath;
    }

    /**
     * Instruct iMaterialise to download a model from the CDN with the file name stored within the persistence layer
     * found under the supplied itemId.
     *
     * @param itemId Unique item identifier pointing to the item represented within Trove's persistence layer.
     * @return iMatUploadModelResponse object containing the iMaterialise response.
     * @note If the imatPrintSupplier is not initialized otherwise, it will default to the trove-qa-teststore
     * bucket as the place to direct iMaterialise to pull the path from.
     */
    @Override
    public Object uploadModel(Long itemId)
    {
        Item item = itemAccessor.getItemDetached(itemId, IEnums.SEEK_MODE.QUICK);

        String filePath = cdnUrlPath + item.getItemFileName();

        return uploadApi.uploadModel(filePath);
    }

    /**
     * Instruct iMaterialise to download a model from the CDN with the file name stored within the persistence layer
     * found under the supplied itemId.
     *
     * @param modelCDNFilename String filename identifying the filename of the file on the CDN to instruct iMaterialise
     *                         to download.
     * @return iMatUploadModelResponse object containing the iMaterialise response.
     * @note If the imatPrintSupplier is not initialized otherwise, it will default to the trove-qa-teststore
     * bucket as the place to direct iMaterialise to pull the path from.
     */
    @Override
    public Object uploadModel(String modelCDNFilename)
    {
        String fullFilePath = cdnUrlPath + modelCDNFilename;

        return uploadApi.uploadModel(fullFilePath);
    }

    @Override
    public Object uploadModel(byte[] model, String modelName)
    {
        //TODO:  Implement this once we're not in crunch mode.  Will require modifying the Url util libraries.
        throw new NotImplementedException();
    }

    @Override
    public Object getInstantPrice(PriceRequest request)
    {
        iMatPriceRequest priceRequest = (iMatPriceRequest) request;

        return priceCheckApi.retrieveInstantPrice(priceRequest);
    }

    @Override
    public Money getInstantPrice(String materialId, String finishId, Float volume) {
        return null;
    }

    /**
     * Gets the price based on the already-uploaded model.  The attributes for the request (finish, material, and model
     * identifier) ar all expected to be a part of the persisted item record.
     *
     * @param troveItemId Persistent Item identifier pointing to the object stored with Trove that contains the
     *                    appropriate model information to use in the model price request.
     * @param quantity Quantity of objects that need pricing.
     * @param scale The model scale.
     * @param currency
     * @param shippingAddress
     * @return iMatPriceResponse Object containing the response.
     */
    @Override
    public Object getModelBasedPrice(Long troveItemId, int quantity, Float scale, String currency, Address shippingAddress)
    {
        Item item = itemAccessor.getItemDetached(troveItemId, IEnums.SEEK_MODE.FULL);

        iMatPriceRequest request = new iMatPriceRequest();

        iMatModel model = new iMatModel();
        model.setFinishId(ItemAttribute.getItemAttribute(ItemAttribute.ATTRIBUTE_TYPE.FINISH_ID, item.getItemAttributes()).getAttributeValue());
        model.setMaterialId(ItemAttribute.getItemAttribute(ItemAttribute.ATTRIBUTE_TYPE.MATERIAL_ID, item.getItemAttributes()).getAttributeValue());
        model.setModelId(ItemAttribute.getItemAttribute(ItemAttribute.ATTRIBUTE_TYPE.IMATERIALISE_MODEL_ID, item.getItemAttributes()).getAttributeValue());
        model.setQuantity(quantity);
        model.setScale(scale);
        model.setCurrency(currency);

        request.setModels(new ArrayList<iMatModel>());
        request.getModels().add(model);

        //TODO:  unhardcode this
        request.setCurrency("USD");

        if (shippingAddress != null)
        {
            iMatShipmentInfo shipmentInfo = new iMatShipmentInfo();
            shipmentInfo.setCountryCode(shippingAddress.getCountry().getIsoAlpha2Code());
            shipmentInfo.setStateCode(Subdivision.getAbbreviatedSubdivisionCode(shippingAddress.getSubdivision().getCode()));
            shipmentInfo.setCity(shippingAddress.getCity());
            shipmentInfo.setZipCode(shippingAddress.getPostalCode());

            request.setShipmentInfo(shipmentInfo);
        }

        return priceCheckApi.retrievePriceByModel(request);
    }

    @Override
    public Object getModelBasedPrice(String imaterialiseModelId,
                                     int quantity, Float scale, String currency,
                                     Address shippingAddress,
                                     String materialId, String finishId)
    {


        iMatPriceRequest request = new iMatPriceRequest();

        iMatModel model = new iMatModel();
        model.setFinishId(finishId);
        model.setMaterialId(materialId);
        model.setModelId(imaterialiseModelId);
        model.setQuantity(quantity);
        model.setScale(scale);
        model.setCurrency(currency);

        request.setModels(new ArrayList<iMatModel>());
        request.getModels().add(model);

        //TODO:  unhardcode this
        request.setCurrency("USD");

        if (shippingAddress != null)
        {
            iMatShipmentInfo shipmentInfo = new iMatShipmentInfo();
            shipmentInfo.setCountryCode(shippingAddress.getCountry().getIsoAlpha2Code());
            shipmentInfo.setStateCode(Subdivision.getAbbreviatedSubdivisionCode(shippingAddress.getSubdivision().getCode()));
            shipmentInfo.setCity(shippingAddress.getCity());
            shipmentInfo.setZipCode(shippingAddress.getPostalCode());

            request.setShipmentInfo(shipmentInfo);
        }

        return priceCheckApi.retrievePriceByModel(request);
    }

    @Override
    public Boolean addModelToOrder(Long troveItemId, Long troveCartId)
    {
        //TODO:  Implement this once we're not in crunch mode.  Will require modifying the Url util libraries.
        throw new NotImplementedException();
    }

    @Override
    public Boolean removeModelFromOrder(Long troveUserId, Long troveCartId) {
        //TODO:  Implement this once we're not in crunch mode.  Will require modifying the Url util libraries.
        throw new NotImplementedException();
    }

    @Override
    public Object getFinalOrderPrice(Long troveCartId)
    {
        //TODO:  Implement better handling of shopping cart rather than the ditch-and-recreate method
        //TODO:  This will be super slow if the entire shopping cart has expired, or the items within the cart have expired.  Find another time to start uploading models to be sure that this worst case scenario doesn't get hit.
        Cart cart = cartAccessor.getShoppingCart(troveCartId, IEnums.SEEK_MODE.FULL);

        //Make sure we have our own personal reference to this thing, as it's what iMaterialise requires.
        for (CartItem item : cart.getCartItems())
        {
            if (item.getAttributes() != null)
            {
                CartItemAttribute attribute = CartItemAttribute.getCartItemAttribute(CartItemAttribute.ATTRIBUTE_TYPE.IMAT_CART_ITEM_REFERENCE.toString(), item.getAttributes());
                if (attribute == null || attribute.getAttributeValue() == null) {
                    attribute = new CartItemAttribute(CartItemAttribute.ATTRIBUTE_TYPE.IMAT_CART_ITEM_REFERENCE.toString(), UUID.randomUUID().toString());
                    cartAccessor.updateCartItemAttribute(item.getCartItemId(), attribute);
                    cart = cartAccessor.getShoppingCart(troveCartId, IEnums.SEEK_MODE.FULL);
                }
            }
        }

        Map<CartItem, iMatCartItem> imatConvertedCartItems = cart.getCartItemsAsiMatItemsFromItemReference();

        //Make sure that all of our items are valid with iMaterialise
        //TODO:  Unit Test this for loop, as the last time it was tested the results didn't work correctly
        //TODO:  Move this to a cron job.  This is dangerous to do mid-request, as it could take more than 60 seconds.
        /*for (CartItem item : imatConvertedCartItems.keySet())
        {

            Boolean expired = true;
            if (imatConvertedCartItems.get(item).getValidUntil() != null)
            {
                //Make sure the expiration is at least 24 hours ahead.  Otherwise it needs to be reuploaded.
                Date expires = DateUtils.convertIsoDate(imatConvertedCartItems.get(item).getValidUntil());
                if (expires != null)
                    expired = !expires.after(DateUtils.getTomorrowDateObject());
            }

            //TODO:  Make this update the item with the response values in the item's attributes
            if (imatConvertedCartItems.get(item).getModelId() == null ||
                    imatConvertedCartItems.get(item).getValidUntil() == null || expired)
            {
                iMatUploadModelResponse response = (iMatUploadModelResponse) uploadModel(item.getItemId());
                if (response != null && response.getError() == null) {
                    imatConvertedCartItems.get(item).setModelId(response.getModelID());
                    ItemAttribute attribute = new ItemAttribute();

                    //Update the persisted item attribute
                    attribute.setAttributeName(ItemAttribute.ATTRIBUTE_TYPE.IMATERIALISE_MODEL_ID.toString());
                    attribute.setAttributeValue(response.getModelID());
                    itemAccessor.updateItemAttribute(item.getItemId(), attribute);
                }

            }

            imatConvertedCartItems.get(item).setValidUntil(null);
        }*/

        //Create the cart items on the iMaterialise side, now that we are pretty sure they're valid?
        //TODO:  Verify that the cart items really are valid
        iMatCartItemReqRes request = new iMatCartItemReqRes();

        request.setCurrency(cart.getCurrencyUnit());
        request.setCartItems(new ArrayList<>(imatConvertedCartItems.values()));

        //TODO:  Email and phone aren't being populated here.  Subsequent shipping calculation fails.  Doesn't matter for the time being, but it's sloppy.  Fix it.
        iMatCartItemReqRes response = cartApi.createCartItems(request);

        iMatCartCreateReq  cartCreateRequest = new iMatCartCreateReq();

        if (response != null) {
            cartCreateRequest.setCartItems(response.getCartItems());
            cartCreateRequest.setCurrency(response.getCurrency());

            //TODO:  Save this uuid with the persisted cart
            cartCreateRequest.setMyCartReference(UUID.randomUUID().toString());

            cartCreateRequest.setShippingInfo(cart.getShippingAddress().toiMatAddress());
            cartCreateRequest.setBillingInfo(cart.getBillingAddress().toiMatAddress());
        }

        iMatCartCreateResponse createCartResponse = cartApi.createCart(cartCreateRequest);

        //TODO:  O(n^ridiculous)  FIX THIS
        for (iMatCartItem item : createCartResponse.getCartItems())
        {
            for (CartItem cartItem : cart.getCartItems())
            {
                if (cartItem.containsAttribute(CartItemAttribute.ATTRIBUTE_TYPE.IMAT_CART_ITEM_REFERENCE, item.getMyCartItemReference()))
                {
                    CartItem updatedItem = new CartItem();
                    //updatedItem.setActualPrice(cartAccessor.applyMarkup(MoneyUtil.floatToBigDecimal(item.getiMatPrice(), null), null));
                    updatedItem.setCartItemId(cartItem.getCartItemId());
                    updatedItem.setQuantity(cartItem.getQuantity());
                    cartAccessor.updateCartItem(updatedItem);
                }
            }
        }

        //TODO:  Move hardcoded "Express" shipping to properties file
        cartAccessor.updateCartAttribute(cart.getCartId(), new CartAttribute(CartAttribute.ATTRIBUTE_TYPE.IMAT_SHIPMENT_TYPE.toString(), "Express"));

        return createCartResponse;

    }

    @Override
    public Object submitOrder(Long troveCartId)
    {
        //TODO:  Implement cart expiration checking
        Cart cart = cartAccessor.getShoppingCart(troveCartId, IEnums.SEEK_MODE.FULL);

        iMatCartCheckoutReq request = new iMatCartCheckoutReq();
        request.setCartId(cart.getVendorCartId());
        request.setDirectMailingAllowed(false);

        //TODO:  Clean this up
        CartAttribute myOrderReference =
        CartAttribute.getCartAttribute(CartAttribute.ATTRIBUTE_TYPE.IMAT_MY_ORDER_REFERENCE.toString(),
                cart.getCartAttributes());

        if (myOrderReference != null)
        request.setMyOrderReference(myOrderReference.getAttributeValue());

        CartAttribute myInvoiceReferralLink =
                CartAttribute.getCartAttribute(CartAttribute.ATTRIBUTE_TYPE.IMAT_MY_INVOICE_LINK.toString(),
                        cart.getCartAttributes());

        if (myInvoiceReferralLink != null)
            request.setMyInvoiceLink(myInvoiceReferralLink.getAttributeValue());

        CartAttribute myDeliveryNoteLink =
                CartAttribute.getCartAttribute(CartAttribute.ATTRIBUTE_TYPE.IMAT_MY_DELIVERY_NOTE_LINK.toString(),
                        cart.getCartAttributes());

        if (myDeliveryNoteLink != null)
            request.setMyDeliveryNoteLink(myDeliveryNoteLink.getAttributeValue());

        CartAttribute remarks =
                CartAttribute.getCartAttribute(CartAttribute.ATTRIBUTE_TYPE.IMAT_REMARKS.toString(),
                        cart.getCartAttributes());

        if (remarks != null)
            request.setRemarks(remarks.getAttributeValue());

        //TODO:  Do a little error prevention here, if the shipping type ends up being null we're in trouble
        CartAttribute shippingType =
                CartAttribute.getCartAttribute(CartAttribute.ATTRIBUTE_TYPE.IMAT_SHIPMENT_TYPE.toString(), cart.getCartAttributes());
        request.setShipmentService(shippingType.getAttributeValue());

        CartAttribute cartId = CartAttribute.getCartAttribute(CartAttribute.ATTRIBUTE_TYPE.IMAT_CART_ID.toString(), cart.getCartAttributes());
        request.setCartId(cartId.getAttributeValue());

        return cartApi.checkout(request);
    }

    @Override
    public Object cancelOrder(String supplierOrderId)
    {
        imatCancelOrderRequest request = new imatCancelOrderRequest();
        request.setOrderNumber(supplierOrderId);

        return cancelOrderApi.cancelOrder(request);
    }

    @Override
    public Object cancelOrder(Long troveOrderId)
    {
        Order order = orderAccessor.getOrder(troveOrderId, IEnums.SEEK_MODE.ORDER_QUICK);

        imatCancelOrderRequest request = new imatCancelOrderRequest();
        request.setOrderNumber(order.getVendorOrderNumber());

        return cancelOrderApi.cancelOrder(request);
    }

    @Override
    public Object getOrderStatus(String supplierOrderId)
    {
        List<String> containerObject = new ArrayList<>();
        containerObject.add(supplierOrderId);

        return cartApi.getOrderStatus(containerObject);
    }

    @Override
    public OrderStatus getOrderStatus(Long troveOrderId) {
        return null;
    }

    @Override
    public Object getOrderStatuses(List<String> supplierOrderId)
    {
        return cartApi.getOrderStatus(supplierOrderId);
    }

    @Override
    public Object getOrderStatusesByTroveOrder(List<Order> troveUserOrders)
    {
        List<String> orders = new ArrayList<>();

        for (Order order : troveUserOrders)
        {
            orders.add(order.getVendorOrderNumber());
        }

        return cartApi.getOrderStatus(orders);
    }

    @Override
    public List<Material> getSupplierMaterials()
    {
        String MATERIAL = "MATERIALS";
        String cachedMaterials = cacheService.getCacheValue(MATERIAL);
        List<Material> rval = new ArrayList<>();

        if (cachedMaterials != null && cachedMaterials.length() > 0)
        {
            Type collectionType = new TypeToken<List<Material>>(){}.getType();
            rval = gson.fromJson(cachedMaterials, collectionType);
        }
        else
        {
            rval = materialFilterAccessor.getAllActiveMaterials();

            if (rval == null || rval.size() == 0)
            {
                HashMap<String, List<String>> filter = new HashMap<>();
                Map<String, String> realityMaterialMapping = new HashMap<>();
                Map<String, String> realityFinishMapping = new HashMap<>();
                rval = new ArrayList<>();

                //Set up the pre-defined material filters
                filter.put("Gold", new ArrayList<String>());
                filter.put("Brass", new ArrayList<String>());
                filter.put("Bronze", new ArrayList<String>());
                filter.put("Silver", new ArrayList<String>());

                //Add the filters for the finishes, ALL naturally means all finishes
                filter.get("Gold").add("ALL");
                filter.get("Brass").add("ALL");
                filter.get("Silver").add("High gloss");
                filter.get("Silver").add("Sandblasted");
                filter.get("Silver").add("Antique");
                filter.get("Bronze").add("Natural PU coated");
                filter.get("Bronze").add("Polished PU coated");

                realityMaterialMapping.put("Gold", "gold_polished");
                realityMaterialMapping.put("Brass", "gold_polished");
                realityMaterialMapping.put("Bronze", "red_gold_polished");
                realityMaterialMapping.put("Silver", "platinum_polished");

                realityFinishMapping.put("Red 14kt", "red_gold_polished");
                realityFinishMapping.put("Red 18kt", "red_gold_polished");

                iMatMaterialsResponse response = infoApi.getAvailableMaterials();

                List<Material> materialsToStore = new ArrayList<>();
                for (iMatMaterial material : response.getMaterials())
                {
                    if (filter.containsKey(material.getName()))
                    {
                        Material persistedMaterial = Material.fromiMatMaterial(material, filter.get(material.getName()),
                                realityMaterialMapping, realityFinishMapping);
                        rval.add(persistedMaterial);
                        materialsToStore.add(persistedMaterial);
                    } else
                    {
                        materialsToStore.add(Material.fromiMatMaterial(material));
                    }
                }

                //TODO:  Investigate bug where it tries to double tap this.  Workaround is to prevent duplicate entry at the DB level.
                materialFilterAccessor.persistMaterials(materialsToStore);

                Map properties = new HashMap<>();
                properties.put(GCacheFactory.EXPIRATION_DELTA, TimeUnit.HOURS.toSeconds(2));
                cacheService.putCache(MATERIAL, gson.toJson(rval), properties);

            }
        }

        //TODO:  More O(n)^2 goodness.  Clean this up.  Or figure out why persistence seeking isn't working correctly.
        List<Material> finalReturn = new ArrayList<>();
        for (Material material : rval)
        {
            Material cleanedMaterial = new Material(material);

            for (Finish finish : material.getFinishList())
            {
                if (finish.isActive())
                    cleanedMaterial.getFinishList().add(finish);
            }

            finalReturn.add(cleanedMaterial);
        }

        rval = finalReturn;

        return rval;
    }

    @Override
    public Object checkUploadStatus(Integer modelId)
    {
        return null;
    }

    @Override
    public Object submitOrder(String userFirstName, String userLastName, List<ShapewaysItem> items) {
        return null;
    }

    public String getCdnUrlPath() {
        return cdnUrlPath;
    }

    public void setCdnUrlPath(String cdnUrlPath) {
        this.cdnUrlPath = cdnUrlPath;
    }

}
