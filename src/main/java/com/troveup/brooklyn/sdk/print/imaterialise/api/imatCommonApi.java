package com.troveup.brooklyn.sdk.print.imaterialise.api;

import com.google.gson.Gson;
import com.troveup.brooklyn.sdk.http.impl.HttpClient;
import com.troveup.brooklyn.sdk.http.impl.HttpClientFactory;
import com.troveup.brooklyn.sdk.http.interfaces.IHttpClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by tim on 5/22/15.
 */
public abstract class imatCommonApi
{
    public static final String API_CODE = "2d732431-0e22-491b-90bb-e27a21a42ce2";
    public static final String TOOL_ID = "a0271645-ba79-44ac-9a2f-b5ebcafbd10c";
    public static final String IMATERIALISE_SANDBOX_BASE_URL = "https://imatsandbox.materialise.net/web-api/";
    public static final String IMATERIALISE_UPLOAD_API_URL_FRAGMENT = "tool/<toolID>/model";
    public static final String IMATERIALISE_INSTANT_PRICING_URL_FRAGMENT = "pricing";
    public static final String IMATERIALISE_MODEL_PRICE_URL_FRAGMENT = "pricing/model";
    public static final String IMATERIALISE_CREATE_CART_URL_FRAGMENT = "cart/post";
    public static final String IMATERIALISE_CREATE_CART_ITEM_URL_FRAGMENT = "cartitems/register";
    public static final String IMATERIALISE_ADD_REMOVE_ITEM_URL_FRAGMENT = "cart/{cartid}/items";
    public static final String IMATERIALISE_CHECKOUT_URL_FRAGMENT = "order/post";
    public static final String IMATERIALISE_MATERIALS_API_URL_FRAGMENT = "materials?user=<your registered email address here>";
    public static final String IMATERIALISE_ORDER_STATUS_FRAGMENT = "order?number=";
    public static final String IMATERIALISE_REGISTERED_EMAIL = "brian@troveprint.com";
    public static final String IMATERIALISE_CANCEL_ORDER_BY_NUMBER_URL_FRAGMENT = "order/CancelOrderByNumber";

    protected Logger logger;

    protected String apiCode;
    protected String toolId;

    @Autowired
    protected IHttpClientFactory httpClientFactory;

    @Autowired
    protected Gson gson;

    public imatCommonApi()
    {
        logger = LoggerFactory.getLogger(this.getClass());
    }
    public imatCommonApi(String apiCode, String toolId)
    {
        logger = LoggerFactory.getLogger(this.getClass());
        this.apiCode = apiCode;
        this.toolId = toolId;
    }

    protected void logError(Exception e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        logger.error("Stack Trace: " + sw.toString());
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
