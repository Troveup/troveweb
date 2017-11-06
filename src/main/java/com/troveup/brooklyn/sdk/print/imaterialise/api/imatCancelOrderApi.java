package com.troveup.brooklyn.sdk.print.imaterialise.api;

import com.troveup.brooklyn.sdk.http.impl.HttpClient;
import com.troveup.brooklyn.sdk.print.imaterialise.model.imatCancelOrderRequest;
import com.troveup.brooklyn.sdk.print.imaterialise.model.imatCancelOrderResponse;
import com.troveup.brooklyn.util.models.UrlResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;

/**
 * Created by tim on 6/12/15.
 */
public class imatCancelOrderApi extends imatCommonApi
{
    private String baseApiUrl;
    private String cancelOrderByNumberFragment;

    public imatCancelOrderApi(String baseApiUrl, String cancelOrderByNumberFragment, String apiCode, String toolId)
    {
        this.baseApiUrl = baseApiUrl;
        this.cancelOrderByNumberFragment = cancelOrderByNumberFragment;
        this.apiCode = apiCode;
        this.toolId = toolId;
    }

    public imatCancelOrderResponse cancelOrder(imatCancelOrderRequest request)
    {
        HttpClient client = httpClientFactory.getHttpClientInstance();

        imatCancelOrderResponse rval = null;

        String createCartUrl = baseApiUrl + cancelOrderByNumberFragment;
        //String createCartUrl = "http://localhost:4444/" + cancelOrderByNumberFragment;

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

                rval = gson.fromJson(concatString, imatCancelOrderResponse.class);
            }
        } catch (IOException e)
        {
            logError(e);
        }

        return rval;
    }
}
