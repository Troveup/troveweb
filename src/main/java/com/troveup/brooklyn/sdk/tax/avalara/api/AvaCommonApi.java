package com.troveup.brooklyn.sdk.tax.avalara.api;

import com.google.gson.Gson;
import com.troveup.brooklyn.sdk.http.impl.HttpClient;
import com.troveup.brooklyn.sdk.http.interfaces.IHttpClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by tim on 5/27/15.
 */
public abstract class AvaCommonApi
{
    public static final String AVA_ACCOUNT_NUMBER = "1100159810";
    public static final String AVA_LICENSE_KEY = "4BC06CFE40EA155A";
    public static final String AVA_TROVE_COMPANY_CODE = "TROVE";
    public static final String AVA_BASE_TAX_URL = "https://development.avalara.net/";
    public static final String AVA_GET_TAX_URL_FRAGMENT = "1.0/tax/get";
    public static final String AVA_BASIC_AUTH_HEADER = "MTEwMDE1OTgxMDo0QkMwNkNGRTQwRUExNTVB";

    @Autowired
    Gson gson;

    @Autowired
    IHttpClientFactory httpClientFactory;

    Logger logger;

    public AvaCommonApi()
    {
        logger = LoggerFactory.getLogger(AvaCommonApi.class);
    }

    protected void logError(Exception e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        logger.error("Stack Trace: " + sw.toString());
    }
}
