package com.troveup.brooklyn.sdk.print.shapeways.api;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by tim on 6/26/15.
 */
public class ShapewaysRequest
{
    private final OAuthRequest request;

    public ShapewaysRequest(OAuthRequest request)
            throws IOException
    {
        this.request = request;
        this.request.setConnectTimeout(60000, TimeUnit.MILLISECONDS);
        this.request.setReadTimeout(60000, TimeUnit.MILLISECONDS);
    }

    public Response sendRequest() throws IOException {
        return request.send();
    }

    public void setJsonPayload(String jsonPayload)
    {
        request.addPayload(jsonPayload);
    }

    public void addHeader(String key, String value)
    {
        request.addHeader(key, value);
    }
}
