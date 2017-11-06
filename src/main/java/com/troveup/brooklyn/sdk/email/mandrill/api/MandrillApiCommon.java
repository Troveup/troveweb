package com.troveup.brooklyn.sdk.email.mandrill.api;

import com.troveup.brooklyn.sdk.http.interfaces.IHttpClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by tim on 6/10/15.
 */
public class MandrillApiCommon
{
    protected IHttpClientFactory httpClientFactory;

    public MandrillApiCommon(IHttpClientFactory httpClientFactory)
    {
        this.httpClientFactory = httpClientFactory;
    }
}
