package com.troveup.brooklyn.sdk.http.interfaces;

import com.troveup.brooklyn.sdk.http.impl.HttpClient;

/**
 * Created by tim on 6/10/15.
 */
public interface IHttpClientFactory
{
    HttpClient getHttpClientInstance();
}
