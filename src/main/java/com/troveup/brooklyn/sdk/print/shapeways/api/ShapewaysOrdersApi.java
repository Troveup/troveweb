package com.troveup.brooklyn.sdk.print.shapeways.api;

import com.google.gson.Gson;
import com.troveup.brooklyn.sdk.print.shapeways.exception.ShapewaysOrderFailureException;
import com.troveup.brooklyn.sdk.print.shapeways.model.*;
import com.troveup.brooklyn.util.StringUtils;
import org.scribe.model.Response;
import org.scribe.model.Verb;

import java.io.IOException;

/**
 * Created by tim on 6/26/15.
 */
public class ShapewaysOrdersApi extends ShapewaysCommonApi {
    public ShapewaysOrdersApi(ShapewaysRequestFactory requestFactory, Gson gson) {
        super(requestFactory, gson);
    }

    public ShapewaysOrderResponse submitOrder(ShapewaysOrderRequest request) throws IOException,
            ShapewaysOrderFailureException {
        ShapewaysRequest httpClient = requestFactory.getShapewaysRequest(Verb.POST,
                ShapewaysRequestFactory.SHAPEWAYS_BASE_API_URL + ShapewaysRequestFactory.SHAPEWAYS_URL_FRAGMENT);

        String jsonRequest = gson.toJson(request);
        httpClient.setJsonPayload(jsonRequest);
        httpClient.addHeader("Content-Type", "application/json");

        Response response = httpClient.sendRequest();

        if (response.isSuccessful())
            return gson.fromJson(StringUtils.converStreamToString(response.getStream()), ShapewaysOrderResponse.class);
        else
        {
            throw new ShapewaysOrderFailureException("Order request to Shapeways failed.  Request JSON payload: " +
                    jsonRequest + ".  Response returned with message " +
                    StringUtils.converStreamToString(response.getStream()));
        }
    }

    public ShapewaysGetOrderStatusResponse getOrderStatus(ShapewaysGetOrderStatusRequest request) throws IOException,
            ShapewaysOrderFailureException {
        ShapewaysRequest httpClient = requestFactory.getShapewaysRequest(Verb.GET,
                ShapewaysRequestFactory.SHAPEWAYS_BASE_API_URL + ShapewaysRequestFactory.SHAPEWAYS_CANCEL_ORDER_FRAGMENT.replace("{orderId}",
                        request.getOrderId()));

        //String jsonRequest = gson.toJson(request);
        //httpClient.setJsonPayload(jsonRequest);
        //httpClient.addHeader("Content-Type", "application/json");

        Response response = httpClient.sendRequest();

        if (response.isSuccessful())
            return gson.fromJson(ShapewaysGetOrderStatusResponse.fixShapewaysOrderResponse(StringUtils.converStreamToString(response.getStream())),
                    ShapewaysGetOrderStatusResponse.class);
        else
        {
            throw new ShapewaysOrderFailureException("Order status request to Shapeways failed.  Request querystring: " +
                     ShapewaysRequestFactory.SHAPEWAYS_BASE_API_URL + ShapewaysRequestFactory.SHAPEWAYS_CANCEL_ORDER_FRAGMENT.replace("{orderId}",
                    request.getOrderId()) + ".  Response returned with message " +
                    StringUtils.converStreamToString(response.getStream()));
        }
    }

    public ShapewaysCancelOrderResponse cancelOrder(ShapewaysCancelOrderRequest request) throws IOException, ShapewaysOrderFailureException {
        ShapewaysRequest httpClient = requestFactory.getShapewaysRequest(Verb.PUT,
                ShapewaysRequestFactory.SHAPEWAYS_BASE_API_URL +
                        ShapewaysRequestFactory.SHAPEWAYS_CANCEL_ORDER_FRAGMENT.replace("{orderId}",
                                request.getOrderId().toString()));

        String jsonRequest = gson.toJson(request);
        httpClient.setJsonPayload(jsonRequest);
        httpClient.addHeader("Content-Type", "application/json");

        Response response = httpClient.sendRequest();

        if (response.isSuccessful() || response.getCode() == 400)
            return gson.fromJson(StringUtils.converStreamToString(response.getStream()),
                    ShapewaysCancelOrderResponse.class);
        else
        {
            throw new ShapewaysOrderFailureException("Cancel order request to Shapeways failed.  Request JSON payload: " +
                    jsonRequest + ".  Response returned with message " +
                    StringUtils.converStreamToString(response.getStream()));
        }
    }
}
