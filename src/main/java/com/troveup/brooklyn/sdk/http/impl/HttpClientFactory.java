package com.troveup.brooklyn.sdk.http.impl;

import com.troveup.brooklyn.sdk.http.interfaces.IHttpClientFactory;

/**
 * Created by tim on 6/10/15.
 */
public class HttpClientFactory implements IHttpClientFactory
{
    public HttpClientFactory()
    {

    }

    @Override
    public HttpClient getHttpClientInstance()
    {
        return new HttpClient();
    }
}
