package com.troveup.brooklyn.sdk.realityserver.api;

import com.google.gson.Gson;
import com.troveup.brooklyn.sdk.common.api.CommonApi;
import com.troveup.brooklyn.sdk.http.impl.HttpClient;
import com.troveup.brooklyn.sdk.http.interfaces.IHttpClientFactory;
import com.troveup.brooklyn.sdk.realityserver.model.NimbixStartResponse;
import com.troveup.brooklyn.sdk.realityserver.model.RealityServerInitialSubmitResponse;
import com.troveup.brooklyn.sdk.realityserver.model.RealityServerRenderRequest;
import com.troveup.brooklyn.sdk.realityserver.model.RealityServerStatusResponse;
import com.troveup.brooklyn.util.models.UrlResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.util.HashMap;

/**
 * Created by tim on 6/20/15.
 */
public class RealityServer extends CommonApi
{
    private final Gson gson;
    private final IHttpClientFactory clientFactory;
    private Logger logger;

    public enum REALITY_SERVER_STATUS
    {
        STOPPED,
        STARTING,
        STARTED,
        STOPPING,
        UNKNOWN_STATE,
        ERROR_STARTING,
        ERROR_STOPPING,
        TIMEOUT_STARTING,
        UNKNOWN_ERROR,
        STOPPING_FAILED,
        RESPONSE_ERROR
    }

    public RealityServer(Gson gson, IHttpClientFactory clientFactory)
    {
        this.gson = gson;
        this.clientFactory = clientFactory;
        logger = LoggerFactory.getLogger(this.getClass());
    }

    public RealityServerInitialSubmitResponse submitRender(String ipAddress, RealityServerRenderRequest request) {
        RealityServerInitialSubmitResponse rval = null;
        HttpClient client = clientFactory.getHttpClientInstance();
        String requestUrl = "http://" + ipAddress + "/render/submit";
        String jsonRenderRequest = gson.toJson(request);

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

                    rval = gson.fromJson(concatString, RealityServerInitialSubmitResponse.class);
                    retry = false;
                }
                //Something bad happened, trigger a retry
                else
                {
                    retry = true;
                    if (response == null)
                        logger.error("Failure submitting render request " + jsonRenderRequest +
                                ".  Response object was null!  Retrying attempt with request number " + i);
                    else
                        logger.error("Failure submitting render request " + jsonRenderRequest +
                                ".  UrlResponse code was " + response.getResponseCode() + ".");

                    Thread.sleep(5000);
                }
                //IOException, communications failure, sleep 5 seconds and trigger a retry
            } catch (IOException e) {
                retry = true;
                logger.error("Failure submitting render request " + jsonRenderRequest +
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

    public RealityServerStatusResponse getRealityServerStatus(String ipAddress)
    {
        RealityServerStatusResponse rval = null;
        HttpClient client = clientFactory.getHttpClientInstance();
        String requestUrl = "http://" + ipAddress + "/control/status";

        client.configureForStandardRequest(requestUrl, null, HttpClient.REQUEST_METHOD.GET, null, null);

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

                    rval = gson.fromJson(concatString, RealityServerStatusResponse.class);
                    retry = false;
                }
                //Something bad happened, trigger a retry
                else
                {
                    retry = true;
                    if (response == null)
                        logger.error("Failure submitting status request " + requestUrl +
                                ".  Response object was null!  Retrying attempt with request number " + i);
                    else
                        logger.error("Failure submitting render request " + requestUrl +
                                ".  UrlResponse code was " + response.getResponseCode() + ".");

                    Thread.sleep(5000);
                }
                //IOException, communications failure, sleep 5 seconds and trigger a retry
            } catch (IOException e) {
                retry = true;
                logger.error("Failure submitting render request " + requestUrl +
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

    public RealityServerStatusResponse startRealityServer(String ipAddress)
    {
        RealityServerStatusResponse rval = null;
        HttpClient client = clientFactory.getHttpClientInstance();
        String requestUrl = "http://" + ipAddress + "/control/start";

        client.configureForStandardRequest(requestUrl, null, HttpClient.REQUEST_METHOD.GET, null, null);

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

                    rval = gson.fromJson(concatString, RealityServerStatusResponse.class);
                    retry = false;
                }
                //Something bad happened, trigger a retry
                else
                {
                    retry = true;
                    if (response == null)
                        logger.error("Failure submitting status request " + requestUrl +
                                ".  Response object was null!  Retrying attempt with request number " + i);
                    else
                        logger.error("Failure submitting render request " + requestUrl +
                                ".  UrlResponse code was " + response.getResponseCode() + ".");

                    Thread.sleep(5000);
                }
                //IOException, communications failure, sleep 5 seconds and trigger a retry
            } catch (IOException e) {
                retry = true;
                logger.error("Failure submitting render request " + requestUrl +
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

    public RealityServerStatusResponse stopRealityServer(String ipAddress)
    {
        RealityServerStatusResponse rval = null;
        HttpClient client = clientFactory.getHttpClientInstance();
        String requestUrl = "http://" + ipAddress + "/control/stop";

        client.configureForStandardRequest(requestUrl, null, HttpClient.REQUEST_METHOD.GET, null, null);

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

                    rval = gson.fromJson(concatString, RealityServerStatusResponse.class);
                    retry = false;
                }
                //Something bad happened, trigger a retry
                else
                {
                    retry = true;
                    if (response == null)
                        logger.error("Failure submitting status request " + requestUrl +
                                ".  Response object was null!  Retrying attempt with request number " + i);
                    else
                        logger.error("Failure submitting render request " + requestUrl +
                                ".  UrlResponse code was " + response.getResponseCode() + ".");

                    Thread.sleep(5000);
                }
                //IOException, communications failure, sleep 5 seconds and trigger a retry
            } catch (IOException e) {
                retry = true;
                logger.error("Failure submitting render request " + requestUrl +
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
