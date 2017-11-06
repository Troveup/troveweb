package com.troveup.brooklyn.sdk.meshexporter.forge.api;

import com.google.gson.Gson;
import com.troveup.brooklyn.sdk.common.api.CommonApi;
import com.troveup.brooklyn.sdk.http.impl.HttpClient;
import com.troveup.brooklyn.sdk.http.interfaces.IHttpClientFactory;
import com.troveup.brooklyn.sdk.meshexporter.forge.model.ForgeMeshExportRequest;
import com.troveup.brooklyn.sdk.meshexporter.forge.model.ForgeMeshExportResponse;
import com.troveup.brooklyn.sdk.meshexporter.forge.model.ForgeMeshVolumeRequest;
import com.troveup.brooklyn.sdk.meshexporter.forge.model.ForgeMeshVolumeResponse;
import com.troveup.brooklyn.util.models.UrlResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;

/**
 * Created by tim on 8/29/15.
 */
public class ForgeExporter extends CommonApi
{
    public static final String FORGE_DEFAULT_DEV_IP_ADDRESS = "146.148.71.155";
    //Re-enable this when Blender is ready
    private final String EXPORT_URL_ENDPOINT_FRAGMENT = "/api/export";
    //private final String EXPORT_URL_ENDPOINT_FRAGMENT = "/api/oldexport";
    private final String VOLUME_URL_ENDPOINT_FRAGMENT = "/api/volume";
    private final Gson gson;
    private final IHttpClientFactory clientFactory;
    private final String forgeIpAddress;

    public ForgeExporter(Gson gson, IHttpClientFactory clientFactory, String forgeIpAddress)
    {
        this.gson = gson;
        this.clientFactory = clientFactory;
        this.forgeIpAddress = forgeIpAddress;
    }

    public ForgeMeshExportResponse exportMesh(ForgeMeshExportRequest request)
    {
        ForgeMeshExportResponse rval = new ForgeMeshExportResponse();
        HttpClient client = clientFactory.getHttpClientInstance();
        String requestUrl = "http://" + forgeIpAddress + EXPORT_URL_ENDPOINT_FRAGMENT;
        String jsonRenderRequest = gson.toJson(request.getHash());

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        client.configureForStandardRequest(requestUrl, headers, HttpClient.REQUEST_METHOD.POST, jsonRenderRequest, null);

        Boolean retry = false;

        //Build in a redundant backup call in the event of an IOException or other failure
        for (int i = 0; i < 3; ++i)
        {
            try {
                UrlResponse response = client.sendRequest();

                if (response != null && response.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    String concatString = "";

                    for (String singleString : response.getResponseBody())
                        concatString += singleString;

                    rval = gson.fromJson(concatString, ForgeMeshExportResponse.class);
                    retry = false;
                }
                //Something bad happened, trigger a retry
                else
                {
                    retry = true;
                    if (response == null)
                        logger.error("Failure submitting mesh export request " + jsonRenderRequest +
                                ".  Response object was null!  Retrying attempt with request number " + i);
                    else
                        logger.error("Failure submitting mesh export request " + jsonRenderRequest +
                                ".  UrlResponse code was " + response.getResponseCode() + "." +
                        "  Error message was " + response.getResponseMessage());

                    Thread.sleep(5000);
                }
                //IOException, communications failure, sleep 5 seconds and trigger a retry
            } catch (IOException e) {
                retry = true;
                logger.error("Failure submitting mesh export request " + jsonRenderRequest +
                        ". IOException encountered, trying again.  Retry attempt " + i);
                e.printStackTrace();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    //Eat this error
                }
            } catch (Exception e) {
                logError(e);
            }

            //Successful outcome, break out of the for loop to avoid making another request
            if (!retry)
                break;
        }

        return rval;
    }

    public ForgeMeshVolumeResponse getMeshVolume(ForgeMeshVolumeRequest request)
    {
        ForgeMeshVolumeResponse rval = new ForgeMeshVolumeResponse();
        HttpClient client = clientFactory.getHttpClientInstance();
        String requestUrl = "http://" + forgeIpAddress + VOLUME_URL_ENDPOINT_FRAGMENT;
        String jsonRenderRequest = gson.toJson(request.getHash());

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        client.configureForStandardRequest(requestUrl, headers, HttpClient.REQUEST_METHOD.POST, jsonRenderRequest, null);

        Boolean retry = false;

        //Build in a redundant backup call in the event of an IOException or other failure
        for (int i = 0; i < 3; ++i)
        {
            try {
                UrlResponse response = client.sendRequest();

                if (response != null && response.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    String concatString = "";

                    for (String singleString : response.getResponseBody())
                        concatString += singleString;

                    rval = gson.fromJson(concatString, ForgeMeshVolumeResponse.class);
                    retry = false;
                }
                //Something bad happened, trigger a retry
                else
                {
                    retry = true;
                    if (response == null)
                        logger.error("Failure requesting volume using request " + jsonRenderRequest +
                                ".  Response object was null!  Retrying attempt with request number " + i);
                    else
                        logger.error("Failure requesting volume using request " + jsonRenderRequest +
                                ".  UrlResponse code was " + response.getResponseCode() + "." +
                                "  Error message was " + response.getResponseMessage());

                    Thread.sleep(5000);
                }
                //IOException, communications failure, sleep 5 seconds and trigger a retry
            } catch (IOException e) {
                retry = true;
                logger.error("Failure volume request" + jsonRenderRequest +
                        ". IOException encountered, trying again.  Retry attempt " + i);
                e.printStackTrace();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    //Eat this error
                }
            } catch (Exception e) {
                logError(e);
            }

            //Successful outcome, break out of the for loop to avoid making another request
            if (!retry)
                break;
        }

        return rval;
    }


}
