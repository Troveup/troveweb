package com.troveup.brooklyn.sdk.print.voodoo.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.troveup.brooklyn.sdk.http.impl.HttpClient;
import com.troveup.brooklyn.sdk.http.interfaces.IHttpClientFactory;
import com.troveup.brooklyn.sdk.print.voodoo.model.SampleCreateOrderResponse;
import com.troveup.brooklyn.sdk.print.voodoo.model.SampleMaterial;
import com.troveup.brooklyn.sdk.print.voodoo.model.SampleOrderRequest;
import com.troveup.brooklyn.util.models.UrlResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tim on 6/18/15.
 */
public class VoodooApi
{
    public static String VOODOO_API_KEY = "14f3f2b21031dcedde1de01945903a23c3fa59ba6f8e7e4b7008e66437914bc0";
    public static String VOODOO_BASE_URL = "https://api.voodoomfg.co/api/0/";
    public static String VOODOO_MATERIALS_URL_FRAGMENT = "materials";
    public static String VOODOO_SUBMIT_ORDER_URL_FRAGMENT = "order/create_and_confirm";

    private String apiKey;
    private String baseUrl;
    private String getMaterialsUrlFragment;
    private String submitOrderUrlFragment;
    private final IHttpClientFactory clientFactory;
    private final Gson gson;
    protected Logger logger;

    public VoodooApi(String apiKey, String baseUrl, String getMaterialsUrlFragment, String submitOrderUrlFragment,
                     IHttpClientFactory clientFactory, Gson gson)
    {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.getMaterialsUrlFragment = getMaterialsUrlFragment;
        this.submitOrderUrlFragment = submitOrderUrlFragment;
        this.clientFactory = clientFactory;
        this.gson = gson;
        logger = LoggerFactory.getLogger(this.getClass());
    }

    public List<SampleMaterial> getAvailableMaterials()
    {
        HttpClient client = clientFactory.getHttpClientInstance();

        List<SampleMaterial> rval = null;

        String materialsRequestUrl = baseUrl + getMaterialsUrlFragment;

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("key", apiKey);

        client.configureForStandardRequest(materialsRequestUrl, headers, HttpClient.REQUEST_METHOD.GET, null, null);

        try
        {
            UrlResponse response = client.sendRequest();

            if (response != null && response.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                String concatString = "";

                for (String singleString : response.getResponseBody())
                    concatString += singleString;

                Type collectionType = new TypeToken<List<SampleMaterial>>(){}.getType();
                rval = gson.fromJson(concatString, collectionType);
            }
        } catch (IOException e)
        {
            logError(e);
        }

        return rval;
    }

    public SampleCreateOrderResponse submitSampleOrder(SampleOrderRequest orderRequest)
    {
        HttpClient client = clientFactory.getHttpClientInstance();

        SampleCreateOrderResponse rval = null;

        String submitSampleOrderUrl = baseUrl + submitOrderUrlFragment;

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("key", apiKey);
        headers.put("Content-Type", "application/json");
        //headers.put("Accept-Encoding", "gzip, deflate");
        //headers.put("Accept-Language", "en-US,en;q=0.8");

        String jsonRequest = gson.toJson(orderRequest);

        client.configureForStandardRequest(submitSampleOrderUrl, headers, HttpClient.REQUEST_METHOD.POST, jsonRequest, null);

        try
        {
            UrlResponse response = client.sendRequest();

            if (response != null)
            {
                String concatString = "";

                for (String singleString : response.getResponseBody())
                    concatString += singleString;

                rval = gson.fromJson(concatString, SampleCreateOrderResponse.class);
            }
        } catch (IOException e)
        {
            logError(e);
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
