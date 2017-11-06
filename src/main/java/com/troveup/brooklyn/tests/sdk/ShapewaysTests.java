package com.troveup.brooklyn.tests.sdk;

import com.google.gson.Gson;
import com.troveup.brooklyn.sdk.meshexporter.java.api.GeometryAnalysis;
import com.troveup.brooklyn.sdk.meshexporter.java.business.MeshExport;
import com.troveup.brooklyn.sdk.meshexporter.java.model.ModelJson;
import com.troveup.brooklyn.sdk.meshexporter.java.model.OperatorWeightJson;
import com.troveup.brooklyn.sdk.meshexporter.java.model.ParameterJson;
import com.troveup.brooklyn.sdk.print.interfaces.IPrintSupplier;
import com.troveup.brooklyn.sdk.print.shapeways.api.ShapewaysModelApi;
import com.troveup.brooklyn.sdk.print.shapeways.exception.ShapewaysModelFailureException;
import com.troveup.brooklyn.sdk.print.shapeways.model.*;
import com.troveup.brooklyn.util.MoneyUtil;
import com.troveup.brooklyn.util.StringUtils;
import com.troveup.config.PersistenceConfig;
import com.troveup.config.SDKConfig;
import junit.framework.Assert;
import org.joda.money.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.*;

/**
 * Created by tim on 6/29/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, SDKConfig.class})
public class ShapewaysTests
{
    @Autowired
    Gson gson;

    @Autowired
    ShapewaysModelApi shapewaysModelApi;

    @Autowired
    IPrintSupplier printSupplier;

    @Autowired
    MeshExport exporter;

    @Test
    public void getTemporaryOauthToken() throws GeneralSecurityException, IOException {

        DefaultApi10a defaultApi10a = new DefaultApi10a() {
            @Override
            public String getRequestTokenEndpoint()
            {
                return "http://api.shapeways.com/oauth1/request_token/v1";
            }

            @Override
            public String getAccessTokenEndpoint()
            {
                return "http://api.shapeways.com/oauth1/access_token/v1";
            }

            @Override
            public String getAuthorizationUrl(Token token) {
                return null;
            }
        };

        OAuthService service = new ServiceBuilder()
                .provider(defaultApi10a)
                .apiKey("b33b4a147fe0daac66605f5bf72c344173de2fd3")
                .apiSecret("4b38484ecd803f43ccbbfe4e10ebe8beb28af8d1")
                .build();


        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.shapeways.com/oauth1/request_token/v1");
        request.setRealm("shapeways.com");
        request.addOAuthParameter("oauth_callback", "https://project-troveup-qa.appspot.com/worker/oauthcallback");
        service.signRequest(Token.empty(), request);

        Response response = request.send();

        Assert.assertNotNull(response);
        Assert.assertTrue(response.getCode() == 200);

        String responseMessage = response.getMessage();
        String body = response.getBody();
    }

    @Test
    public void getPermanentOauthToken()
    {
        DefaultApi10a defaultApi10a = new DefaultApi10a() {
            @Override
            public String getRequestTokenEndpoint()
            {
                return "http://api.shapeways.com/oauth1/request_token/v1";
            }

            @Override
            public String getAccessTokenEndpoint()
            {
                return "http://api.shapeways.com/oauth1/access_token/v1";
            }

            @Override
            public String getAuthorizationUrl(Token token) {
                return null;
            }
        };

        OAuthService service = new ServiceBuilder()
                .provider(defaultApi10a)
                .apiKey("b33b4a147fe0daac66605f5bf72c344173de2fd3")
                .apiSecret("4b38484ecd803f43ccbbfe4e10ebe8beb28af8d1")
                .build();



        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.shapeways.com/oauth1/access_token/v1");

        Token token = new Token("793f7abc826ebe424f13f8b6240eb0a00f766829", "6f1694f4eb2d54e03b707644c09be2815799a115");
        request.addOAuthParameter("oauth_verifier", "fb9530");
        //request.addOAuthParameter("oauth_verifier", "9e2619");
        service.signRequest(token, request);

        Response response = request.send();


        String body = response.getBody();
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getCode() == 200);
    }

    @Test
    public void getShapewaysMaterials()
    {
        DefaultApi10a defaultApi10a = new DefaultApi10a() {
            @Override
            public String getRequestTokenEndpoint()
            {
                return "http://api.shapeways.com/oauth1/request_token/v1";
            }

            @Override
            public String getAccessTokenEndpoint()
            {
                return "http://api.shapeways.com/oauth1/access_token/v1";
            }

            @Override
            public String getAuthorizationUrl(Token token) {
                return null;
            }
        };

        OAuthService service = new ServiceBuilder()
                .provider(defaultApi10a)
                .apiKey("b33b4a147fe0daac66605f5bf72c344173de2fd3")
                .apiSecret("4b38484ecd803f43ccbbfe4e10ebe8beb28af8d1")
                .build();



        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.shapeways.com/materials/v1");

        Token token = new Token("bee76723a27efe115969e152cb93ae3da4ad466f", "9c866b7d8cca786aec15e12f1694fd11892b0d0b");
        service.signRequest(token, request);

        Response response = request.send();

        String materials = response.getBody();

        Gson gson = new Gson();

        //ShapewaysMaterials deserialisedThing = gson.fromJson(materials, ShapewaysMaterials.class);

        Assert.assertNotNull(response);
        Assert.assertTrue(response.getCode() == 200);
    }

    @Test
    public void testUploadShapewaysModel() throws IOException {
        DefaultApi10a defaultApi10a = new DefaultApi10a() {
            @Override
            public String getRequestTokenEndpoint()
            {
                return "http://api.shapeways.com/oauth1/request_token/v1";
            }

            @Override
            public String getAccessTokenEndpoint()
            {
                return "http://api.shapeways.com/oauth1/access_token/v1";
            }

            @Override
            public String getAuthorizationUrl(Token token) {
                return null;
            }
        };

        OAuthService service = new ServiceBuilder()
                .provider(defaultApi10a)
                .apiKey("b33b4a147fe0daac66605f5bf72c344173de2fd3")
                .apiSecret("4b38484ecd803f43ccbbfe4e10ebe8beb28af8d1")
                .build();


        byte[] encoded = Files.readAllBytes(
                Paths.get("/Users/tim/Downloads/render-c378e136-ed73-4dc1-8710-62810f83cf78.obj"));

        encoded = Base64.encode(encoded);

        String model = new String(encoded, "UTF-8");

        ShapewaysUploadRequest requestModel = new ShapewaysUploadRequest(model, "testModel3.obj");

        String jsonRequest = gson.toJson(requestModel);

        OAuthRequest request = new OAuthRequest(Verb.POST, "http://api.shapeways.com/models/v1");
        //OAuthRequest request = new OAuthRequest(Verb.POST, "http://localhost:4444");
        request.addPayload(jsonRequest);
        request.addHeader("Content-Type", "application/json");
        /*request.addBodyParameter("fileName", requestModel.getFileName());
        request.addBodyParameter("file", Base64.encode(requestModel.getFile().getBytes()).toString());
        request.addBodyParameter("hasRightsToModel", requestModel.getHasRightsToModel().toString());
        request.addBodyParameter("acceptTermsAndConditions", requestModel.getAcceptTermsAndConditions().toString());
        request.addBodyParameter("uploadScale", requestModel.getUploadScale().toString());
        request.addBodyParameter("isPublic", requestModel.getIsPublic().toString());
        request.addBodyParameter("isForSale", Integer.toString(requestModel.getIsForSale()));
        request.addBodyParameter("isDownloadable", requestModel.getIsDownloadable().toString());*/
        //request.addOAuthParameter("oauth_verifier", "fb9530");

        //Map<String, String> params =

        Token token = new Token("bee76723a27efe115969e152cb93ae3da4ad466f", "9c866b7d8cca786aec15e12f1694fd11892b0d0b");
        service.signRequest(token, request);

        Response response = request.send();

        ShapewaysUploadResponse uploadResponse = gson.fromJson(response.getBody(), ShapewaysUploadResponse.class);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getCode() == 200);
    }

    @Test
    public void testUploadShapewaysModelWithApiImplementation() throws IOException, ShapewaysModelFailureException {
        byte[] encoded = Files.readAllBytes(
                Paths.get("/Users/tim/Downloads/render-c378e136-ed73-4dc1-8710-62810f83cf78.obj"));

        encoded = Base64.encode(encoded);

        String model = new String(encoded, "UTF-8");

        ShapewaysUploadResponse response = shapewaysModelApi.uploadModel(model, UUID.randomUUID().toString() + ".obj");

        Assert.assertNotNull(response);

        Assert.assertNotNull(response.getModelId());
    }

    @Test
    public void testGetModelById()
    {
        DefaultApi10a defaultApi10a = new DefaultApi10a() {
            @Override
            public String getRequestTokenEndpoint()
            {
                return "http://api.shapeways.com/oauth1/request_token/v1";
            }

            @Override
            public String getAccessTokenEndpoint()
            {
                return "http://api.shapeways.com/oauth1/access_token/v1";
            }

            @Override
            public String getAuthorizationUrl(Token token) {
                return null;
            }
        };

        OAuthService service = new ServiceBuilder()
                .provider(defaultApi10a)
                .apiKey("b33b4a147fe0daac66605f5bf72c344173de2fd3")
                .apiSecret("4b38484ecd803f43ccbbfe4e10ebe8beb28af8d1")
                .build();


        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.shapeways.com/models/3588345/v1");
        //OAuthRequest request = new OAuthRequest(Verb.POST, "http://localhost:4444");
        //request.addHeader("Content-Type", "application/json");

        Token token = new Token("bee76723a27efe115969e152cb93ae3da4ad466f", "9c866b7d8cca786aec15e12f1694fd11892b0d0b");
        service.signRequest(token, request);

        Response response = request.send();

        ShapewaysGetModelResponse getResponse = gson.fromJson(response.getBody(), ShapewaysGetModelResponse.class);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getCode() == 200);
    }

    @Test
    public void testGetShapewaysModelWithApiImplementation() throws IOException, ShapewaysModelFailureException {
        ShapewaysGetModelResponse response = shapewaysModelApi.getModelStatus("3588345");

        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getPrintable());
    }

    @Test
    public void placeOrderTest() throws IOException {
        DefaultApi10a defaultApi10a = new DefaultApi10a() {
            @Override
            public String getRequestTokenEndpoint()
            {
                return "http://api.shapeways.com/oauth1/request_token/v1";
            }

            @Override
            public String getAccessTokenEndpoint()
            {
                return "http://api.shapeways.com/oauth1/access_token/v1";
            }

            @Override
            public String getAuthorizationUrl(Token token) {
                return null;
            }
        };

        OAuthService service = new ServiceBuilder()
                .provider(defaultApi10a)
                .apiKey("b33b4a147fe0daac66605f5bf72c344173de2fd3")
                .apiSecret("4b38484ecd803f43ccbbfe4e10ebe8beb28af8d1")
                .build();

        ShapewaysItem item = new ShapewaysItem();
        //item.setModelId(3589242);
        item.setModelId(3574787);
        //item.setMaterialId(23);
        item.setMaterialId(6);
        item.setProductDimensionChoiceCombo(0);
        item.setQuantity(1);

        List<ShapewaysItem> shapewaysItemList = new ArrayList<>();

        shapewaysItemList.add(item);

        ShapewaysOrderRequest orderRequest = new ShapewaysOrderRequest();
        orderRequest.setFirstName("Tim");
        orderRequest.setLastName("Growney");
        orderRequest.setCountry("US");
        orderRequest.setState("NY");
        orderRequest.setCity("New York");
        orderRequest.setAddress1("20 Exchange Place");
        orderRequest.setAddress2("Apt 1604");
        orderRequest.setZipCode("10005");
        orderRequest.setPhoneNumber("4023661135");
        orderRequest.setItems(shapewaysItemList);

        //Not sure what these values are supposed to be...
        orderRequest.setPaymentVerificationId("TVE-ORDER");
        orderRequest.setPaymentMethod("credit_card");
        orderRequest.setShippingOption("Cheapest");

        String jsonRequest = gson.toJson(orderRequest);

        OAuthRequest request = new OAuthRequest(Verb.POST, "http://api.shapeways.com/orders/v1");
        //OAuthRequest request = new OAuthRequest(Verb.POST, "http://localhost:4444");
        request.addPayload(jsonRequest);
        request.addHeader("Content-Type", "application/json");
        /*request.addBodyParameter("fileName", requestModel.getFileName());
        request.addBodyParameter("file", Base64.encode(requestModel.getFile().getBytes()).toString());
        request.addBodyParameter("hasRightsToModel", requestModel.getHasRightsToModel().toString());
        request.addBodyParameter("acceptTermsAndConditions", requestModel.getAcceptTermsAndConditions().toString());
        request.addBodyParameter("uploadScale", requestModel.getUploadScale().toString());
        request.addBodyParameter("isPublic", requestModel.getIsPublic().toString());
        request.addBodyParameter("isForSale", Integer.toString(requestModel.getIsForSale()));
        request.addBodyParameter("isDownloadable", requestModel.getIsDownloadable().toString());*/
        //request.addOAuthParameter("oauth_verifier", "fb9530");

        //Map<String, String> params =

        Token token = new Token("bee76723a27efe115969e152cb93ae3da4ad466f", "9c866b7d8cca786aec15e12f1694fd11892b0d0b");
        service.signRequest(token, request);

        Response response = request.send();

        String message = response.getMessage();
        String responseParticulars = StringUtils.converStreamToString(response.getStream());
        ShapewaysOrderResponse orderResponse = gson.fromJson(responseParticulars, ShapewaysOrderResponse.class);
        Assert.assertNotNull(orderResponse);
        Assert.assertNotNull(orderResponse.getOrderId());
        Assert.assertNotNull(orderResponse.getProductionOrderIds());
        Assert.assertNotNull(orderResponse.getResult());
        Assert.assertTrue(orderResponse.getProductionOrderIds().size() > 0);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getCode() == 200);
    }

    @Test
    public void placeOrderAndGetStatus() throws IOException {
        DefaultApi10a defaultApi10a = new DefaultApi10a() {
            @Override
            public String getRequestTokenEndpoint()
            {
                return "http://api.shapeways.com/oauth1/request_token/v1";
            }

            @Override
            public String getAccessTokenEndpoint()
            {
                return "http://api.shapeways.com/oauth1/access_token/v1";
            }

            @Override
            public String getAuthorizationUrl(Token token) {
                return null;
            }
        };

        OAuthService service = new ServiceBuilder()
                .provider(defaultApi10a)
                .apiKey("b33b4a147fe0daac66605f5bf72c344173de2fd3")
                .apiSecret("4b38484ecd803f43ccbbfe4e10ebe8beb28af8d1")
                .build();

        ShapewaysItem item = new ShapewaysItem();
        item.setModelId(3574787);
        item.setMaterialId(6);
        item.setProductDimensionChoiceCombo(0);
        item.setQuantity(1);

        ShapewaysItem secondItem = new ShapewaysItem();
        secondItem.setModelId(3590645);
        secondItem.setMaterialId(6);
        secondItem.setProductDimensionChoiceCombo(0);
        secondItem.setQuantity(1);

        List<ShapewaysItem> shapewaysItemList = new ArrayList<>();

        shapewaysItemList.add(item);
        shapewaysItemList.add(secondItem);

        ShapewaysOrderRequest orderRequest = new ShapewaysOrderRequest();
        orderRequest.setFirstName("Tim");
        orderRequest.setLastName("Growney");
        orderRequest.setCountry("US");
        orderRequest.setState("NY");
        orderRequest.setCity("New York");
        orderRequest.setAddress1("20 Exchange Place");
        orderRequest.setAddress2("Apt 1604");
        orderRequest.setZipCode("10005");
        orderRequest.setPhoneNumber("4023661135");
        orderRequest.setItems(shapewaysItemList);

        //Not sure what these values are supposed to be...
        orderRequest.setPaymentVerificationId("TVE-ORDER");
        orderRequest.setPaymentMethod("credit_card");
        orderRequest.setShippingOption("Cheapest");

        String jsonRequest = gson.toJson(orderRequest);

        OAuthRequest request = new OAuthRequest(Verb.POST, "http://api.shapeways.com/orders/v1");
        request.addPayload(jsonRequest);
        request.addHeader("Content-Type", "application/json");

        Token token = new Token("bee76723a27efe115969e152cb93ae3da4ad466f", "9c866b7d8cca786aec15e12f1694fd11892b0d0b");
        service.signRequest(token, request);

        Response response = request.send();

        String message = response.getMessage();
        String responseParticulars = StringUtils.converStreamToString(response.getStream());
        ShapewaysOrderResponse orderResponse = gson.fromJson(responseParticulars, ShapewaysOrderResponse.class);
        Assert.assertNotNull(orderResponse);
        Assert.assertNotNull(orderResponse.getOrderId());
        Assert.assertNotNull(orderResponse.getProductionOrderIds());
        Assert.assertNotNull(orderResponse.getResult());
        Assert.assertTrue(orderResponse.getProductionOrderIds().size() > 0);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getCode() == 200);

        /** Get Request for order **/
        ShapewaysGetOrderStatusRequest getOrderStatusRequest = ShapewaysGetOrderStatusRequest.
                ShapewaysGetOrderStatusRequestByOrderId(orderResponse.getOrderId().toString());

        request = new OAuthRequest(Verb.GET, "http://api.shapeways.com/orders/v1");
        request.addPayload(gson.toJson(getOrderStatusRequest));
        request.addHeader("Content-Type", "application/json");

        token = new Token("bee76723a27efe115969e152cb93ae3da4ad466f", "9c866b7d8cca786aec15e12f1694fd11892b0d0b");
        service.signRequest(token, request);

        response = request.send();

        message = response.getMessage();
        responseParticulars = StringUtils.converStreamToString(response.getStream());
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getCode() == 200);

    }

    @Test
    public void placeOrderAndCancel() throws IOException {
        DefaultApi10a defaultApi10a = new DefaultApi10a() {
            @Override
            public String getRequestTokenEndpoint()
            {
                return "http://api.shapeways.com/oauth1/request_token/v1";
            }

            @Override
            public String getAccessTokenEndpoint()
            {
                return "http://api.shapeways.com/oauth1/access_token/v1";
            }

            @Override
            public String getAuthorizationUrl(Token token) {
                return null;
            }
        };

        OAuthService service = new ServiceBuilder()
                .provider(defaultApi10a)
                .apiKey("b33b4a147fe0daac66605f5bf72c344173de2fd3")
                .apiSecret("4b38484ecd803f43ccbbfe4e10ebe8beb28af8d1")
                .build();

        ShapewaysItem item = new ShapewaysItem();
        item.setModelId(3574787);
        item.setMaterialId(6);
        item.setProductDimensionChoiceCombo(0);
        item.setQuantity(1);

        ShapewaysItem secondItem = new ShapewaysItem();
        secondItem.setModelId(3590645);
        secondItem.setMaterialId(6);
        secondItem.setProductDimensionChoiceCombo(0);
        secondItem.setQuantity(1);

        List<ShapewaysItem> shapewaysItemList = new ArrayList<>();

        shapewaysItemList.add(item);
        shapewaysItemList.add(secondItem);

        ShapewaysOrderRequest orderRequest = new ShapewaysOrderRequest();
        orderRequest.setFirstName("Tim");
        orderRequest.setLastName("Growney");
        orderRequest.setCountry("US");
        orderRequest.setState("NY");
        orderRequest.setCity("New York");
        orderRequest.setAddress1("20 Exchange Place");
        orderRequest.setAddress2("Apt 1604");
        orderRequest.setZipCode("10005");
        orderRequest.setPhoneNumber("4023661135");
        orderRequest.setItems(shapewaysItemList);

        //Not sure what these values are supposed to be...
        orderRequest.setPaymentVerificationId("TVE-ORDER");
        orderRequest.setPaymentMethod("credit_card");
        orderRequest.setShippingOption("Cheapest");

        String jsonRequest = gson.toJson(orderRequest);

        OAuthRequest request = new OAuthRequest(Verb.POST, "http://api.shapeways.com/orders/v1");
        request.addPayload(jsonRequest);
        request.addHeader("Content-Type", "application/json");

        Token token = new Token("bee76723a27efe115969e152cb93ae3da4ad466f", "9c866b7d8cca786aec15e12f1694fd11892b0d0b");
        service.signRequest(token, request);

        Response response = request.send();

        String message = response.getMessage();
        String responseParticulars = StringUtils.converStreamToString(response.getStream());
        ShapewaysOrderResponse orderResponse = gson.fromJson(responseParticulars, ShapewaysOrderResponse.class);
        Assert.assertNotNull(orderResponse);
        Assert.assertNotNull(orderResponse.getOrderId());
        Assert.assertNotNull(orderResponse.getProductionOrderIds());
        Assert.assertNotNull(orderResponse.getResult());
        Assert.assertTrue(orderResponse.getProductionOrderIds().size() > 0);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getCode() == 200);

        /** Cancel Request for order **/
        ShapewaysCancelOrderRequest cancelOrderRequest = new ShapewaysCancelOrderRequest();
        cancelOrderRequest.setOrderId(Integer.parseInt(orderResponse.getOrderId().toString()));
        cancelOrderRequest.setStatus("cancelled");

        request = new OAuthRequest(Verb.PUT, "http://api.shapeways.com/orders/v1");
        request.addPayload(gson.toJson(cancelOrderRequest));
        request.addHeader("Content-Type", "application/json");

        token = new Token("bee76723a27efe115969e152cb93ae3da4ad466f", "9c866b7d8cca786aec15e12f1694fd11892b0d0b");
        service.signRequest(token, request);

        response = request.send();

        message = response.getMessage();
        responseParticulars = StringUtils.converStreamToString(response.getStream());
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getCode() == 200);

    }

    @Test
    public void cancelOrder() throws IOException {
        Integer orderId = 1086154;

        ShapewaysCancelOrderRequest cancelOrderRequest = new ShapewaysCancelOrderRequest();
        cancelOrderRequest.setOrderId(orderId);
        cancelOrderRequest.setStatus("cancelled");

        DefaultApi10a defaultApi10a = new DefaultApi10a() {
            @Override
            public String getRequestTokenEndpoint()
            {
                return "http://api.shapeways.com/oauth1/request_token/v1";
            }

            @Override
            public String getAccessTokenEndpoint()
            {
                return "http://api.shapeways.com/oauth1/access_token/v1";
            }

            @Override
            public String getAuthorizationUrl(Token token) {
                return null;
            }
        };

        OAuthService service = new ServiceBuilder()
                .provider(defaultApi10a)
                .apiKey("b33b4a147fe0daac66605f5bf72c344173de2fd3")
                .apiSecret("4b38484ecd803f43ccbbfe4e10ebe8beb28af8d1")
                .build();

        OAuthRequest request = new OAuthRequest(Verb.PUT, "http://api.shapeways.com/orders/" + orderId + "/v1");
        String jsonPayload = gson.toJson(cancelOrderRequest);
        request.addPayload(jsonPayload);
        request.addHeader("Content-Type", "application/json");

        Token token = new Token("bee76723a27efe115969e152cb93ae3da4ad466f", "9c866b7d8cca786aec15e12f1694fd11892b0d0b");
        service.signRequest(token, request);

        Response response = request.send();

        String message = response.getMessage();
        String responseParticulars = StringUtils.converStreamToString(response.getStream());
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getCode() == 200);

    }

    @Test
    public void parseShapewaysOrderStatus()
    {
        //Single orders with single items
        /*String sampleShapewaysResponse = "{\"result\":\"success\",\"ordersCount\":{\"total\":2,\"placed\":0," +
                "\"in_production\":1,\"cancelled\":1,\"unknown\":0,\"shipped\":0},\"ordersStatus\":{\"1022998\":" +
                "{\"status\":\"cancelled\",\"items\":{\"2137383\":{\"title\":\"Warrior-ring\",\"quantity\":1," +
                "\"status\":{\"processing\":0,\"in_production\":0,\"complete\":0,\"cancelled\":1}}}},\"1023053\":" +
                "{\"status\":\"in_production\",\"items\":{\"2137458\":{\"title\":\"Warrior-ring\",\"quantity\":1," +
                "\"status\":{\"processing\":1,\"in_production\":0,\"complete\":0,\"cancelled\":0}}}}},\"ordersInfo\":" +
                "[{\"orderId\":\"1022998\",\"targetDeliveryDate\":\"2015-08-03 00:00:00\",\"targetShipDate\":" +
                "\"2015-07-30 00:00:00\",\"shipments\":null},{\"orderId\":\"1023053\",\"targetDeliveryDate\":" +
                "\"2015-08-03 00:00:00\",\"targetShipDate\":\"2015-07-30 00:00:00\",\"shipments\":null}]," +
                "\"nextActionSuggestions\":[]}";*/

        //Single orders with variable numbers of items
        String sampleShapewaysResponse = "{\"result\":\"success\",\"ordersCount\":{\"total\":2,\"placed\":0," +
                "\"in_production\":1,\"cancelled\":1,\"unknown\":0,\"shipped\":0},\"ordersStatus\":{\"1023053\":" +
                "{\"status\":\"cancelled\",\"items\":{\"2137458\":{\"title\":\"Warrior-ring\",\"quantity\":1," +
                "\"status\":{\"processing\":0,\"in_production\":0,\"complete\":0,\"cancelled\":1}}}},\"1023248\":" +
                "{\"status\":\"in_production\",\"items\":{\"2137806\":{\"title\":\"Warrior-ring\",\"quantity\":1," +
                "\"status\":{\"processing\":1,\"in_production\":0,\"complete\":0,\"cancelled\":0}},\"2137807\":" +
                "{\"title\":\"VolumeTest\",\"quantity\":1,\"status\":{\"processing\":1,\"in_production\":0," +
                "\"complete\":0,\"cancelled\":0}}}}},\"ordersInfo\":[{\"orderId\":\"1023053\",\"targetDeliveryDate\":" +
                "\"2015-08-03 00:00:00\",\"targetShipDate\":\"2015-07-30 00:00:00\",\"shipments\":null},{\"orderId\":" +
                "\"1023248\",\"targetDeliveryDate\":\"2015-08-04 00:00:00\",\"targetShipDate\":\"2015-07-31 00:00:00\"," +
                "\"shipments\":null}],\"nextActionSuggestions\":[]}";

        int startOfOrderInfo = sampleShapewaysResponse.indexOf("\"ordersStatus\"");
        int endOfOrderInfo = sampleShapewaysResponse.indexOf("\"ordersInfo\"");

        String startString = sampleShapewaysResponse.substring(startOfOrderInfo, endOfOrderInfo);

        Map<Integer, Integer> possibleOrderIdentifiers = new HashMap<>();

        for (int i = startOfOrderInfo; i < endOfOrderInfo; ++i)
        {
            if (sampleShapewaysResponse.charAt(i) == '"')
            {
                for (int j = i + 1; j < endOfOrderInfo; j++)
                {
                    if (sampleShapewaysResponse.charAt(j) == '"')
                    {
                        possibleOrderIdentifiers.put(i + 1, j);
                        i = j;
                        break;
                    }
                }
            }
        }

        Map<Integer, String> possibleOrderStrings = new HashMap<>();

        for (Integer beginningQuote : possibleOrderIdentifiers.keySet())
        {
            possibleOrderStrings.put(beginningQuote, sampleShapewaysResponse.substring(beginningQuote,
                    possibleOrderIdentifiers.get(beginningQuote)));
        }

        Map<Integer, String> orderStrings = new HashMap<>();
        for (Integer possibleOrderString : possibleOrderStrings.keySet())
        {
            if (StringUtils.isNumeric(possibleOrderStrings.get(possibleOrderString)))
                orderStrings.put(possibleOrderString, possibleOrderStrings.get(possibleOrderString));
        }

        StringBuilder builder = new StringBuilder(sampleShapewaysResponse);

        for (Integer startPoint : orderStrings.keySet())
        {
            //First start point for the word, before insertion
            int startPointForWord = builder.indexOf(orderStrings.get(startPoint));
            String newString = "\"id\":" + orderStrings.get(startPoint) + ",";
            builder.insert(startPointForWord + orderStrings.get(startPoint).length() + 3, newString);

            //Need to find the new startpoint, because the string changed
            startPointForWord = builder.indexOf(orderStrings.get(startPoint));
            String deleteValue = builder.substring(startPointForWord - 1,  startPointForWord +
                    orderStrings.get(startPoint).length() + 2);
            builder.delete(startPointForWord - 1,  startPointForWord +
                    orderStrings.get(startPoint).length() + 2);

            int openBracket = builder.indexOf("{{");

            //Replace double curlies with an open bracket
            if (openBracket > -1)
                builder.replace(openBracket, openBracket + 1, "[");

            int closeBracket = builder.indexOf("}}}}");
            if (closeBracket > -1)
                builder.replace(closeBracket + 2, closeBracket + 3, "]");
        }

        int finalCloseBracket = builder.indexOf("}}]}}");
        if (finalCloseBracket > -1)
            builder.replace(finalCloseBracket + 4, finalCloseBracket + 5, "]");

        String outputString = builder.toString();

        Assert.assertTrue(orderStrings.size() == 2);
    }

    @Test
    public void getResponseParserTest()
    {
        String sampleShapewaysOrderGetResponse = "{\"result\":\"success\",\"ordersCount\":{\"total\":2,\"placed\":0," +
                "\"in_production\":1,\"cancelled\":1,\"unknown\":0,\"shipped\":0},\"ordersStatus\":{\"1022998\":" +
                "{\"status\":\"cancelled\",\"items\":{\"2137383\":{\"title\":\"Warrior-ring\",\"quantity\":1," +
                "\"status\":{\"processing\":0,\"in_production\":0,\"complete\":0,\"cancelled\":1}}}},\"1023053\":" +
                "{\"status\":\"in_production\",\"items\":{\"2137458\":{\"title\":\"Warrior-ring\",\"quantity\":1," +
                "\"status\":{\"processing\":1,\"in_production\":0,\"complete\":0,\"cancelled\":0}}}}},\"ordersInfo\":" +
                "[{\"orderId\":\"1022998\",\"targetDeliveryDate\":\"2015-08-03 00:00:00\",\"targetShipDate\":" +
                "\"2015-07-30 00:00:00\",\"shipments\":null},{\"orderId\":\"1023053\",\"targetDeliveryDate\":" +
                "\"2015-08-03 00:00:00\",\"targetShipDate\":\"2015-07-30 00:00:00\",\"shipments\":null}]," +
                "\"nextActionSuggestions\":[]}";

        String sampleShapewaysSecondOrderGetResponse = "{\"result\":\"success\",\"ordersCount\":{\"total\":2,\"placed\":0," +
                "\"in_production\":1,\"cancelled\":1,\"unknown\":0,\"shipped\":0},\"ordersStatus\":{\"1023053\":" +
                "{\"status\":\"cancelled\",\"items\":{\"2137458\":{\"title\":\"Warrior-ring\",\"quantity\":1," +
                "\"status\":{\"processing\":0,\"in_production\":0,\"complete\":0,\"cancelled\":1}}}},\"1023248\":" +
                "{\"status\":\"in_production\",\"items\":{\"2137806\":{\"title\":\"Warrior-ring\",\"quantity\":1," +
                "\"status\":{\"processing\":1,\"in_production\":0,\"complete\":0,\"cancelled\":0}},\"2137807\":" +
                "{\"title\":\"VolumeTest\",\"quantity\":1,\"status\":{\"processing\":1,\"in_production\":0," +
                "\"complete\":0,\"cancelled\":0}}}}},\"ordersInfo\":[{\"orderId\":\"1023053\",\"targetDeliveryDate\":" +
                "\"2015-08-03 00:00:00\",\"targetShipDate\":\"2015-07-30 00:00:00\",\"shipments\":null},{\"orderId\":" +
                "\"1023248\",\"targetDeliveryDate\":\"2015-08-04 00:00:00\",\"targetShipDate\":\"2015-07-31 00:00:00\"," +
                "\"shipments\":null}],\"nextActionSuggestions\":[]}";


        ShapewaysGetOrderStatusResponse response = gson.fromJson(
                ShapewaysGetOrderStatusResponse.fixShapewaysOrderResponse(sampleShapewaysOrderGetResponse),
                ShapewaysGetOrderStatusResponse.class);

        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getOrdersStatus());
        Assert.assertTrue(response.getOrdersStatus().size() > 0);

        response = gson.fromJson(ShapewaysGetOrderStatusResponse.
                        fixShapewaysOrderResponse(sampleShapewaysSecondOrderGetResponse),
                ShapewaysGetOrderStatusResponse.class);

        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getOrdersStatus());
        Assert.assertTrue(response.getOrdersStatus().size() > 0);
    }

    /*@Test
    public void getOrderTest() throws IOException {
        OAuthService service = new ServiceBuilder()
                .provider(defaultApi10a)
                .apiKey("b33b4a147fe0daac66605f5bf72c344173de2fd3")
                .apiSecret("4b38484ecd803f43ccbbfe4e10ebe8beb28af8d1")
                .build();

        ShapewaysGetOrderStatusRequest getOrderStatusRequest = ShapewaysGetOrderStatusRequest.
                ShapewaysGetOrderStatusRequestByOrderId("1023049");

        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.shapeways.com/orders/v1");
        request.addPayload(gson.toJson(getOrderStatusRequest));
        request.addHeader("Content-Type", "application/json");

        Token token = new Token("bee76723a27efe115969e152cb93ae3da4ad466f", "9c866b7d8cca786aec15e12f1694fd11892b0d0b");
        service.signRequest(token, request);

        Response response = request.send();

        String message = response.getMessage();
        String responseParticulars = StringUtils.converStreamToString(response.getStream());
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getCode() == 200);
    }*/

    @Test
    public void getSampleModelPrice()
    {
        Money rval = printSupplier.getInstantPrice("1", "98", 1.26f);

        Assert.assertNotNull(rval);
        Assert.assertTrue(rval.isGreaterThan(Money.zero(MoneyUtil.toCurrencyUnit("USD"))));
    }

    @Test
    public void volumeTest() throws IOException, ShapewaysModelFailureException {
        byte[] encoded = null;

        //encoded = Files.readAllBytes(Paths.get("/Users/tim/Downloads/models-open_bracelet-2groups-2keys-unittest.json"));
        encoded = Files.readAllBytes(Paths.get("/Users/tim/Downloads/bar_ring_hires.json"));
        String jsonFile = new String(encoded, Charset.forName("UTF-8"));

        // Gson deserialization library, courtesy of Google
        Gson gson = new Gson();
        ModelJson jsonObject = gson.fromJson(jsonFile, ModelJson.class);

        ParameterJson parameterJson = new ParameterJson();
        parameterJson.setSize("r7.0");

        List<OperatorWeightJson> weights = new ArrayList<>();
        //weights.add(new OperatorWeightJson("Inner", 0.96f));
        //weights.add(new OperatorWeightJson("Outer", 0.0f));
        parameterJson.setWeights(weights);

        List<String> visibleMeshes = new ArrayList<>();
        //visibleMeshes.add("Holder.Circle");
        //visibleMeshes.add("Main.Bracelet");
        parameterJson.setVisible(visibleMeshes);

        String obj = new String(exporter.process(jsonObject, parameterJson), "UTF-8");
        GeometryAnalysis volObj = exporter.getVolume(jsonObject, parameterJson);

        ShapewaysUploadResponse response = shapewaysModelApi.uploadModel(new String(Base64.encode(obj.getBytes()), "UTF-8"), "barRingVolumeTestHighRes.obj");
    }

    @Test
    public void testUploadModelViaWrapper() throws IOException {
        byte[] encoded = Files.readAllBytes(
                Paths.get("/Users/tim/Downloads/bar_ring-sample-009019cc-6913-4bda-87b3-ead83cda36d5.obj"));

        ShapewaysUploadResponse response = (ShapewaysUploadResponse) printSupplier.uploadModel(encoded, "wrapperTest.obj");

        Assert.assertNotNull(response);
    }

    @Test
    public void testGetUploadStatusViaWrapper()
    {
        ShapewaysGetModelResponse response = (ShapewaysGetModelResponse) printSupplier.checkUploadStatus(3709842);

        Assert.assertNotNull(response);
    }

    @Test
    public void testSubmitOrderViaWrapper()
    {
        ShapewaysItem item = new ShapewaysItem();
        item.setQuantity(1);
        item.setMaterialId(6);
        item.setModelId(3654850);

        List<ShapewaysItem> itemBucket = new ArrayList<>();
        itemBucket.add(item);

        ShapewaysOrderResponse response = (ShapewaysOrderResponse) printSupplier.submitOrder("Tim", "Growney", itemBucket);

        Assert.assertNotNull(response);
    }

    @Test
    public void testGetOrderStatusViaWrapper()
    {
        //ShapewaysGetOrderStatusResponse response = (ShapewaysGetOrderStatusResponse) printSupplier.getOrderStatus("1064281");
        ShapewaysGetOrderStatusResponse response = (ShapewaysGetOrderStatusResponse) printSupplier.getOrderStatus("1086191");

        Assert.assertNotNull(response);
    }

    @Test
    public void testCancelOrderStatusViaWrapper()
    {
        ShapewaysCancelOrderResponse response = (ShapewaysCancelOrderResponse) printSupplier.cancelOrder("1081341");

        Assert.assertNotNull(response);
    }

}
