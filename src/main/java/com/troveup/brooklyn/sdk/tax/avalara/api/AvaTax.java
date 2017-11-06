package com.troveup.brooklyn.sdk.tax.avalara.api;

import com.troveup.brooklyn.sdk.tax.avalara.model.AvaGetTaxRequest;
import com.troveup.brooklyn.sdk.tax.avalara.model.AvaGetTaxResult;
import com.troveup.brooklyn.sdk.http.impl.HttpClient;
import com.troveup.brooklyn.util.models.UrlResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;

/**
 * Created by tim on 5/27/15.
 */
public class AvaTax extends AvaCommonApi
{
    private String avaTaxBaseUrl;
    private String getTaxUrlFragment;
    private String authEncodedHeader;


    public AvaTax(String avaTaxBaseUrl, String getTaxUrlFragment, String authEncodedHeader)
    {
        this.avaTaxBaseUrl = avaTaxBaseUrl;
        this.getTaxUrlFragment =getTaxUrlFragment;
        this.authEncodedHeader = authEncodedHeader;
    }

    public AvaGetTaxResult getTax(AvaGetTaxRequest request)
    {
        HttpClient client = httpClientFactory.getHttpClientInstance();
        AvaGetTaxResult rval = null;

        String getTaxUrl = avaTaxBaseUrl + getTaxUrlFragment;

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + authEncodedHeader);
        headers.put("Content-Type", "text/json");
        headers.put("Accept", "*/*");

        String jsonRequest = gson.toJson(request);

        client.configureForStandardRequest(getTaxUrl, headers, HttpClient.REQUEST_METHOD.POST, jsonRequest, null);

        try
        {
            UrlResponse response = client.sendRequest();

            if (response != null && response.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                String concatString = "";

                for (String singleString : response.getResponseBody())
                    concatString += singleString;

                rval = gson.fromJson(concatString, AvaGetTaxResult.class);
            }
        } catch (IOException e)
        {
            logError(e);
        }

        return rval;
    }
}
