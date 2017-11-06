package com.troveup.brooklyn.sdk.print.imaterialise.api;

import com.troveup.brooklyn.sdk.print.imaterialise.model.*;
import com.troveup.brooklyn.sdk.http.impl.HttpClient;
import com.troveup.brooklyn.util.models.ConfigurableFormField;
import com.troveup.brooklyn.util.models.UrlResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tim on 5/25/15.
 */
public class imatOrderApi extends imatCommonApi
{
    String baseApiUrl;
    String createCartUrlFragment;
    String createCartItemUrlFragment;
    String addRemoveCartItemUrlFragment;
    String checkoutCartItemUrlFragment;
    String statusUrlFragment;

    public imatOrderApi(String baseApiUrl, String createCartUrlFragment, String createCartItemUrlFragment,
                        String addRemoveCartItemUrlFragment, String checkoutCartItemUrlFragment,
                        String toolId, String apiCode, String statusUrlFragment)
    {
        this.baseApiUrl = baseApiUrl;
        this.createCartUrlFragment = createCartUrlFragment;
        this.createCartItemUrlFragment = createCartItemUrlFragment;
        this.addRemoveCartItemUrlFragment = addRemoveCartItemUrlFragment;
        this.checkoutCartItemUrlFragment = checkoutCartItemUrlFragment;
        this.statusUrlFragment = statusUrlFragment;
        this.toolId = toolId;
        this.apiCode = apiCode;
    }

    public iMatCartItemReqRes createCartItems(iMatCartItemReqRes request)
    {
        HttpClient client = httpClientFactory.getHttpClientInstance();

        iMatCartItemReqRes rval = null;

        String createCartItemUrl = baseApiUrl + createCartItemUrlFragment;

        for (iMatCartItem item : request.getCartItems())
            item.setToolId(toolId);

        String jsonRequest = gson.toJson(request);

        List<ConfigurableFormField> fieldContainer = new ArrayList<>();


        Map<String, String> fieldHeaders = new HashMap<>();
        fieldHeaders.put("Content-Type", "application/json");

        ConfigurableFormField field = new ConfigurableFormField();
        field.setName("data");
        field.setValue(jsonRequest);
        field.setHeaders(fieldHeaders);
        fieldContainer.add(field);

        client.configureForMultipart(createCartItemUrl, null, null, null, fieldContainer, null);

        try
        {
            UrlResponse response = client.sendMultipartRequest();

            if (response != null && response.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                String concatString = "";

                for (String singleString : response.getResponseBody())
                    concatString += singleString;

                rval = gson.fromJson(concatString, iMatCartItemReqRes.class);
            }
        } catch (IOException e)
        {
            logError(e);
        }

        return rval;
    }

    public iMatCartCreateResponse createCart(iMatCartCreateReq request)
    {
        HttpClient client = httpClientFactory.getHttpClientInstance();

        iMatCartCreateResponse rval = null;

        String createCartUrl = baseApiUrl + createCartUrlFragment;

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("APICode", apiCode);
        headers.put("Content-Type", "application/json");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept-Language", "en-US,en;q=0.8");

        String jsonRequest = gson.toJson(request);

        client.configureForStandardRequest(createCartUrl, headers, HttpClient.REQUEST_METHOD.POST, jsonRequest, null);

        try
        {
            UrlResponse response = client.sendRequest();

            if (response != null && response.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                String concatString = "";

                for (String singleString : response.getResponseBody())
                    concatString += singleString;

                rval = gson.fromJson(concatString, iMatCartCreateResponse.class);
            }
        } catch (IOException e)
        {
            logError(e);
        }

        return rval;
    }

    public iMatCartCheckoutResponse checkout(iMatCartCheckoutReq request)
    {
        HttpClient client = httpClientFactory.getHttpClientInstance();

        iMatCartCheckoutResponse rval = null;

        String checkoutCartUrl = baseApiUrl + checkoutCartItemUrlFragment;

        String jsonRequest = gson.toJson(request);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("APICode", apiCode);
        //headers.put("Accept", "multipart/form-data");
        headers.put("Accept", "application/json");

        List<ConfigurableFormField> fieldContainer = new ArrayList<>();

        Map<String, String> fieldHeaders = new HashMap<>();
        fieldHeaders.put("Content-Type", "application/json");

        ConfigurableFormField field = new ConfigurableFormField();
        field.setName("data");
        field.setValue(jsonRequest);
        field.setHeaders(fieldHeaders);
        fieldContainer.add(field);

        client.configureForMultipart(checkoutCartUrl, headers, null, null, fieldContainer, null);

        try {

            UrlResponse response = client.sendMultipartRequest();

            if (response != null && response.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                String concatString = "";

                for (String singleString : response.getResponseBody())
                    concatString += singleString;

                rval = gson.fromJson(concatString, iMatCartCheckoutResponse.class);
            }

        } catch (IOException e) {
            logError(e);
        }

        return rval;
    }

    public imatStatusResponse getOrderStatus(List<String> imatOrderId)
    {
        HttpClient client = httpClientFactory.getHttpClientInstance();

        imatStatusResponse rval = null;

        String orderStatusUrl = baseApiUrl + statusUrlFragment + imatOrderId.get(0);

        for (int i = 1; i < imatOrderId.size(); ++i)
        {
            orderStatusUrl += "&number=" + imatOrderId.get(i);
        }
        //String orderStatusUrl = "http://localhost:4444/" + statusUrlFragment + imatOrderId;

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("APICode", apiCode);
        headers.put("Content-Type", "application/json");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept-Language", "en-US,en;q=0.8");

        client.configureForStandardRequest(orderStatusUrl, headers, HttpClient.REQUEST_METHOD.GET, null, null);

        try {

            UrlResponse response = client.sendRequest();

            if (response != null && response.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                String concatString = "";

                for (String singleString : response.getResponseBody())
                    concatString += singleString;

                rval = gson.fromJson(concatString, imatStatusResponse.class);
            }

        } catch (IOException e) {
            logError(e);
        }

        return rval;

    }
}
