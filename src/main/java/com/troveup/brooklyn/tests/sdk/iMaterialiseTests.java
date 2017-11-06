package com.troveup.brooklyn.tests.sdk;

import com.troveup.brooklyn.orm.cart.interfaces.ICartAccessor;
import com.troveup.brooklyn.orm.cart.model.Cart;
import com.troveup.brooklyn.orm.cart.model.CartAttribute;
import com.troveup.brooklyn.orm.cart.model.CartItem;
import com.troveup.brooklyn.orm.cart.model.CartItemAttribute;
import com.troveup.brooklyn.orm.countries.model.Country;
import com.troveup.brooklyn.orm.countries.model.Subdivision;
import com.troveup.brooklyn.orm.item.interfaces.IItemAccessor;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.item.model.ItemAttribute;
import com.troveup.brooklyn.orm.user.model.Address;
import com.troveup.brooklyn.sdk.print.imaterialise.api.imatOrderApi;
import com.troveup.brooklyn.sdk.print.imaterialise.api.imatUploadModelApi;
import com.troveup.brooklyn.sdk.print.imaterialise.model.*;
import com.troveup.brooklyn.sdk.print.interfaces.IPrintSupplier;
import com.troveup.config.PersistenceConfig;
import com.troveup.config.SDKConfig;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by tim on 5/23/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SDKConfig.class, PersistenceConfig.class})
public class iMaterialiseTests
{
    @Autowired
    IPrintSupplier iMaterialisePrintSupplierObject;

    @Autowired
    IItemAccessor itemAccessor;

    @Autowired
    ICartAccessor cartAccessor;

    @Autowired
    imatOrderApi cartApi;

    Logger logger;

    @Before
    public void setup()
    {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @Test
    public void uploadModelTest() throws ParseException {
        iMatUploadModelResponse response = (iMatUploadModelResponse) iMaterialisePrintSupplierObject.uploadModel("bar%20necklace.stl");

        Assert.assertTrue(response.getModelStatus().equals(imatUploadModelApi.MODEL_UPLOAD_STATUS.SUCCEEDED.toString()));
    }

    @Test
    public void uploadModelByTroveItem()
    {
        iMatUploadModelResponse response = (iMatUploadModelResponse) iMaterialisePrintSupplierObject.uploadModel((2l));

        Assert.assertTrue(response.getModelStatus().equals(imatUploadModelApi.MODEL_UPLOAD_STATUS.SUCCEEDED.toString()));
    }

    @Test
    public void getInstantPriceTest()
    {
        iMatPriceRequest request = new iMatPriceRequest();
        request.setModels(new ArrayList<iMatModel>());

        iMatModel model = new iMatModel();
        model.setModelReference("Test Model");
        //Silver
        model.setMaterialId("d01a95ab-aaba-44f0-a4b6-8f72b66655b1");
        //Antique
        model.setFinishId("1dc6d8ad-136f-4c84-b1a1-7eefe602f68c");
        model.setQuantity(1);
        model.setxDimMm(34.0f);
        model.setyDimMm(12.0f);
        model.setzDimMm(3.0f);
        model.setVolumeCm3(0.836133f);
        model.setSurfaceCm2(10.1296f);
        request.getModels().add(model);
        request.setCurrency("USD");

        iMatShipmentInfo info = new iMatShipmentInfo();
        info.setCountryCode("US");
        info.setCity("Omaha");
        info.setStateCode("NE");
        info.setZipCode("68102");
        request.setShipmentInfo(info);

        iMatPriceResponse response = (iMatPriceResponse)
                iMaterialisePrintSupplierObject.getInstantPrice(request);

        Assert.assertTrue(response.getRequestError() == null);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getModels());
        Assert.assertTrue(response.getModels().size() > 0);
        Assert.assertTrue(response.getModels().get(0).getTotalPrice() > 0f);
        Assert.assertNotNull(response.getShipmentCost());
    }

    @Test
    public void getUploadedModelPriceTest()
    {
        Item item = new Item();

        ItemAttribute finishIdAttribute = new ItemAttribute();
        finishIdAttribute.setAttributeName(ItemAttribute.ATTRIBUTE_TYPE.FINISH_ID.toString());
        finishIdAttribute.setAttributeValue("1dc6d8ad-136f-4c84-b1a1-7eefe602f68c");

        ItemAttribute materialIdAttribute = new ItemAttribute();
        materialIdAttribute.setAttributeName(ItemAttribute.ATTRIBUTE_TYPE.MATERIAL_ID.toString());
        materialIdAttribute.setAttributeValue("d01a95ab-aaba-44f0-a4b6-8f72b66655b1");

        ItemAttribute imaterialiseModelIdAttribute = new ItemAttribute();
        imaterialiseModelIdAttribute.setAttributeName(ItemAttribute.ATTRIBUTE_TYPE.IMATERIALISE_MODEL_ID.toString());
        imaterialiseModelIdAttribute.setAttributeValue("b8bf8cfd-d34e-4be0-a4f6-5cfec2bb8d0d");

        List<ItemAttribute> attributeList = new ArrayList<>();
        attributeList.add(finishIdAttribute);
        attributeList.add(materialIdAttribute);
        attributeList.add(imaterialiseModelIdAttribute);

        item.setItemAttributes(attributeList);

        itemAccessor.createItem(item);

        Address address = new Address();

        Country country = new Country();
        country.setIsoAlpha2Code("US");

        Subdivision subdivision = new Subdivision();
        subdivision.setCode("US-NE");

        address.setCountry(country);
        address.setSubdivision(subdivision);
        address.setCity("Omaha");
        address.setPostalCode("68102");

        iMatPriceResponse response =
                (iMatPriceResponse) iMaterialisePrintSupplierObject.getModelBasedPrice(item.getItemId(), 1, 1f, "USD", address);

        Assert.assertTrue(response.getRequestError() == null);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getModels());
        Assert.assertTrue(response.getModels().size() > 0);
        Assert.assertTrue(response.getModels().get(0).getTotalPrice() > 0f);
        Assert.assertNotNull(response.getShipmentCost());

    }

    @Test
    public void testCreateCart()
    {
        Item item = new Item();

        ItemAttribute finishIdAttribute = new ItemAttribute();
        finishIdAttribute.setAttributeName(ItemAttribute.ATTRIBUTE_TYPE.FINISH_ID.toString());
        finishIdAttribute.setAttributeValue("2c565146-745e-405c-be3e-0eb10be900e3");

        ItemAttribute materialIdAttribute = new ItemAttribute();
        materialIdAttribute.setAttributeName(ItemAttribute.ATTRIBUTE_TYPE.MATERIAL_ID.toString());
        materialIdAttribute.setAttributeValue("c8461d87-2fd6-46de-8aae-f853763e5f9e");

        ItemAttribute imaterialiseModelIdAttribute = new ItemAttribute();
        imaterialiseModelIdAttribute.setAttributeName(ItemAttribute.ATTRIBUTE_TYPE.IMATERIALISE_MODEL_ID.toString());
        imaterialiseModelIdAttribute.setAttributeValue("a68924bf-7e4f-4d7b-9e9c-c6cf7f1f5e7a");

        item.setItemFileName("bar%20necklace.stl");

        List<ItemAttribute> attributeList = new ArrayList<>();
        attributeList.add(finishIdAttribute);
        attributeList.add(materialIdAttribute);
        attributeList.add(imaterialiseModelIdAttribute);

        item.setItemAttributes(attributeList);

        itemAccessor.createItem(item);

        Address address = new Address();

        Country country = new Country();
        country.setIsoAlpha2Code("US");

        Subdivision subdivision = new Subdivision();
        subdivision.setCode("US-NE");

        address.setCountry(country);
        address.setSubdivision(subdivision);
        address.setCity("Omaha");
        address.setPostalCode("68102");
        address.setFirstName("Tim");
        address.setLastName("Growney");
        address.setPhone("1-402-366-1135");
        address.setEmail("tim@troveup.com");
        address.setAddressLine1("1001 Gallup Dr");

        iMatUploadModelResponse response = (iMatUploadModelResponse) iMaterialisePrintSupplierObject.uploadModel(item.getItemId());

        itemAccessor.updateItemAttribute(item.getItemId(),
                new ItemAttribute(ItemAttribute.ATTRIBUTE_TYPE.IMATERIALISE_MODEL_ID.toString(),
                        response.getModelID()));

        CartItem cartItem = new CartItem();
        cartItem.setCartItemReference(item);

        List<CartItemAttribute> cartItemAttributesContainer = new ArrayList<>();
        cartItemAttributesContainer.add(new CartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_CART_ITEM_REFERENCE.toString(), "d00dbrah"));
        cartItemAttributesContainer.add(new CartItemAttribute(CartItemAttribute.ATTRIBUTE_TYPE.IMAT_MODELID.toString(),
                response.getModelID()));
        cartItemAttributesContainer.add(new CartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_FILESCALEFACTOR.toString(), "1"));
        cartItemAttributesContainer.add(new CartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_MATERIALID.toString(), "c8461d87-2fd6-46de-8aae-f853763e5f9e"));
        cartItemAttributesContainer.add(new CartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_FINISHID.toString(), "2c565146-745e-405c-be3e-0eb10be900e3"));
        cartItemAttributesContainer.add(new CartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_FINISHID.toString(), "2c565146-745e-405c-be3e-0eb10be900e3"));
        cartItemAttributesContainer.add(new CartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_VALIDUNTIL.toString(), response.getValidUntil()));
        cartItemAttributesContainer.add(new CartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_XDIM.toString(), response.getxDimMm().toString()));
        cartItemAttributesContainer.add(new CartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_YDIM.toString(), response.getyDimMm().toString()));
        cartItemAttributesContainer.add(new CartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_ZDIM.toString(), response.getzDimMm().toString()));
        cartItemAttributesContainer.add(new CartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_VOLUME.toString(), response.getVolumeCm3().toString()));
        cartItemAttributesContainer.add(new CartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_SURFACE.toString(), response.getSurfaceCm2().toString()));


        cartItem.setAttributes(cartItemAttributesContainer);

        Cart cart = new Cart();
        List<CartItem> cartItemContainerList = new ArrayList();
        cartItemContainerList.add(cartItem);

        cart.setCartItems(cartItemContainerList);
        cart.setShippingAddress(address);
        cart.setBillingAddress(address);

        cart.setCartItems(cartItemContainerList);

        cart.setCurrencyUnit("USD");

        cartAccessor.persistCart(cart);

        iMatCartCreateResponse cartCreateResponse = (iMatCartCreateResponse) iMaterialisePrintSupplierObject.getFinalOrderPrice(cart.getCartId());

        Assert.assertNotNull(cartCreateResponse);
        Assert.assertNull(cartCreateResponse.getRequestError());

    }

    @Test
    public void testSubmitOrder()
    {
        Item item = new Item();

        ItemAttribute finishIdAttribute = new ItemAttribute();
        finishIdAttribute.setAttributeName(ItemAttribute.ATTRIBUTE_TYPE.FINISH_ID.toString());
        finishIdAttribute.setAttributeValue("2c565146-745e-405c-be3e-0eb10be900e3");

        ItemAttribute materialIdAttribute = new ItemAttribute();
        materialIdAttribute.setAttributeName(ItemAttribute.ATTRIBUTE_TYPE.MATERIAL_ID.toString());
        materialIdAttribute.setAttributeValue("c8461d87-2fd6-46de-8aae-f853763e5f9e");

        ItemAttribute imaterialiseModelIdAttribute = new ItemAttribute();
        imaterialiseModelIdAttribute.setAttributeName(ItemAttribute.ATTRIBUTE_TYPE.IMATERIALISE_MODEL_ID.toString());
        imaterialiseModelIdAttribute.setAttributeValue("a68924bf-7e4f-4d7b-9e9c-c6cf7f1f5e7a");

        item.setItemFileName("bar%20necklace.stl");

        List<ItemAttribute> attributeList = new ArrayList<>();
        attributeList.add(finishIdAttribute);
        attributeList.add(materialIdAttribute);
        attributeList.add(imaterialiseModelIdAttribute);

        item.setItemAttributes(attributeList);

        itemAccessor.createItem(item);

        Address address = new Address();

        Country country = new Country();
        country.setIsoAlpha2Code("US");

        Subdivision subdivision = new Subdivision();
        subdivision.setCode("US-NE");

        address.setCountry(country);
        address.setSubdivision(subdivision);
        address.setCity("Omaha");
        address.setPostalCode("68102");
        address.setFirstName("Tim");
        address.setLastName("Growney");
        address.setPhone("1-402-366-1135");
        address.setEmail("tim@troveup.com");
        address.setAddressLine1("1001 Gallup Dr");

        iMatUploadModelResponse response = (iMatUploadModelResponse) iMaterialisePrintSupplierObject.uploadModel(item.getItemId());

        itemAccessor.updateItemAttribute(item.getItemId(),
                new ItemAttribute(ItemAttribute.ATTRIBUTE_TYPE.IMATERIALISE_MODEL_ID.toString(),
                        response.getModelID()));

        CartItem cartItem = new CartItem();
        cartItem.setCartItemReference(item);

        List<CartItemAttribute> cartItemAttributesContainer = new ArrayList<>();
        cartItemAttributesContainer.add(new CartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_CART_ITEM_REFERENCE.toString(), "d00dbrah"));
        cartItemAttributesContainer.add(new CartItemAttribute(CartItemAttribute.ATTRIBUTE_TYPE.IMAT_MODELID.toString(),
                response.getModelID()));
        cartItemAttributesContainer.add(new CartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_FILESCALEFACTOR.toString(), "1"));
        cartItemAttributesContainer.add(new CartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_MATERIALID.toString(), "c8461d87-2fd6-46de-8aae-f853763e5f9e"));
        cartItemAttributesContainer.add(new CartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_FINISHID.toString(), "2c565146-745e-405c-be3e-0eb10be900e3"));
        cartItemAttributesContainer.add(new CartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_FINISHID.toString(), "2c565146-745e-405c-be3e-0eb10be900e3"));
        cartItemAttributesContainer.add(new CartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_VALIDUNTIL.toString(), response.getValidUntil()));
        cartItemAttributesContainer.add(new CartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_XDIM.toString(), response.getxDimMm().toString()));
        cartItemAttributesContainer.add(new CartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_YDIM.toString(), response.getyDimMm().toString()));
        cartItemAttributesContainer.add(new CartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_ZDIM.toString(), response.getzDimMm().toString()));
        cartItemAttributesContainer.add(new CartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_VOLUME.toString(), response.getVolumeCm3().toString()));
        cartItemAttributesContainer.add(new CartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_SURFACE.toString(), response.getSurfaceCm2().toString()));


        cartItem.setAttributes(cartItemAttributesContainer);

        Cart cart = new Cart();
        List<CartItem> cartItemContainerList = new ArrayList();
        cartItemContainerList.add(cartItem);

        cart.setCartItems(cartItemContainerList);
        cart.setShippingAddress(address);
        cart.setBillingAddress(address);

        cart.setCartItems(cartItemContainerList);

        cart.setCurrencyUnit("USD");

        cartAccessor.persistCart(cart);

        iMatCartCreateResponse cartCreateResponse = (iMatCartCreateResponse) iMaterialisePrintSupplierObject.getFinalOrderPrice(cart.getCartId());

        Assert.assertNotNull(cartCreateResponse);
        Assert.assertNull(cartCreateResponse.getRequestError());

        cartAccessor.updateCartAttribute(cart.getCartId(),
                new CartAttribute(CartAttribute.ATTRIBUTE_TYPE.IMAT_SHIPMENT_TYPE.toString(),
                        cartCreateResponse.getShipmentCost().getServices().get(0).getName()));

        cartAccessor.updateCartAttribute(cart.getCartId(), new CartAttribute(CartAttribute.ATTRIBUTE_TYPE.IMAT_CART_ID.toString(), cartCreateResponse.getCartID()));

        iMatCartCheckoutResponse checkoutResponse = (iMatCartCheckoutResponse) iMaterialisePrintSupplierObject.submitOrder(cart.getCartId());
        Assert.assertNotNull(checkoutResponse);
        Assert.assertNull(checkoutResponse.getRequestError());

    }

    @Test
    public void checkoutTest()
    {
        iMatCartCheckoutReq request = new iMatCartCheckoutReq();
        request.setCartId("3918a2c2-cc26-4628-9d3b-34821e3b965a");
        request.setDirectMailingAllowed(false);
        request.setShipmentService("Express");

        iMatCartCheckoutResponse response = cartApi.checkout(request);

        Assert.assertNotNull(response);
    }

    @Test
    public void getMaterialsTest()
    {
        iMatMaterialsResponse response = (iMatMaterialsResponse) iMaterialisePrintSupplierObject.getSupplierMaterials();
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getMaterials().size() > 0);
    }

    @Test
    public void getOrderStatus()
    {
        imatStatusResponse response = (imatStatusResponse) iMaterialisePrintSupplierObject.getOrderStatus("IMC031648");

        Assert.assertTrue(response.getOrders().size() > 0);
    }

    @Test
    public void getOrderStatuses()
    {
        List<String> statuses = new ArrayList<>();
        statuses.add("IMC031648");
        statuses.add("IMC031664");
        statuses.add("IMC031663");

        imatStatusResponse response = (imatStatusResponse) iMaterialisePrintSupplierObject.getOrderStatuses(statuses);

        Assert.assertTrue(response.getOrders().size() == 3);
    }

    @Test
    public void cancelOrderByImatOrderNumber()
    {
        //TODO:  Modify this test to create an order so that it organically passes rather than relying on modification.
        String orderNumber = "IMC031701";

        imatCancelOrderResponse response = (imatCancelOrderResponse) iMaterialisePrintSupplierObject.cancelOrder(orderNumber);

        Assert.assertNotNull(response);

        Assert.assertTrue(response.getIsCanceled());
    }

    @Test
    public void cancelOrderByTroveOrderId()
    {
        //TODO:  Modify this test to create an order so that it organically passes rather than relying on modification.
        Long orderId = 1l;

        imatCancelOrderResponse response = (imatCancelOrderResponse)
                iMaterialisePrintSupplierObject.cancelOrder(orderId);

        Assert.assertNotNull(response);

        Assert.assertTrue(response.getIsCanceled());

    }

}
