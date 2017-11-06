package com.troveup.brooklyn.sdk.print.imaterialise.api;

import com.troveup.brooklyn.sdk.print.imaterialise.model.iMatPriceRequest;
import com.troveup.brooklyn.sdk.print.imaterialise.model.iMatPriceResponse;
import com.troveup.brooklyn.sdk.http.impl.HttpClient;
import com.troveup.brooklyn.util.models.UrlResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;

/**
 * Created by tim on 5/24/15.
 */
public class imatPriceCheckApi extends imatCommonApi
{
    String baseApiUrl;
    String instantPricingUrlFragment;
    String modelPricingUrlFragment;

    public imatPriceCheckApi(String apiCode, String toolId, String baseApiUrl, String instantPricingUrlFragment, String modelPricingUrlFragment)
    {
        super(apiCode, toolId);

        this.baseApiUrl = baseApiUrl;
        this.instantPricingUrlFragment = instantPricingUrlFragment;
        this.modelPricingUrlFragment = modelPricingUrlFragment;
    }

    public iMatPriceResponse retrieveInstantPrice(iMatPriceRequest priceRequest)
    {
        HttpClient client = httpClientFactory.getHttpClientInstance();

        iMatPriceResponse rval = null;

        String instantPricingUrl = baseApiUrl + instantPricingUrlFragment;

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("APICode", apiCode);
        headers.put("Content-Type", "application/json");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept-Language", "en-US,en;q=0.8");

        String jsonRequest = gson.toJson(priceRequest);

        client.configureForStandardRequest(instantPricingUrl, headers, HttpClient.REQUEST_METHOD.POST, jsonRequest, null);

        try
        {
            UrlResponse response = client.sendRequest();

            if (response != null && response.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                String concatString = "";

                for (String singleString : response.getResponseBody())
                    concatString += singleString;

                rval = gson.fromJson(concatString, iMatPriceResponse.class);
            }
        } catch (IOException e)
        {
            logError(e);
        }

        return rval;
    }

    public iMatPriceResponse retrievePriceByModel(iMatPriceRequest request)
    {
        HttpClient client = httpClientFactory.getHttpClientInstance();

        iMatPriceResponse rval = null;

        String modelPricingUrl = baseApiUrl + modelPricingUrlFragment;

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("APICode", apiCode);
        headers.put("Content-Type", "application/json");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept-Language", "en-US,en;q=0.8");

        String jsonRequest = gson.toJson(request);

        //logger.debug("JSON request for price was: " + jsonRequest);

        client.configureForStandardRequest(modelPricingUrl, headers, HttpClient.REQUEST_METHOD.POST, jsonRequest, null);

        try
        {
            UrlResponse response = client.sendRequest();

            //logger.debug("Response code for pricing request: " + response.getResponseCode());

            if (response != null && response.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                String concatString = "";

                for (String singleString : response.getResponseBody())
                    concatString += singleString;

                logger.debug("Response string from price request was: " + concatString);
                rval = gson.fromJson(concatString, iMatPriceResponse.class);
            }
        } catch (IOException e)
        {
            logError(e);
        }

        return rval;
    }
}
