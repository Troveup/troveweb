package com.troveup.brooklyn.sdk.print.shapeways.api;

import com.google.gson.Gson;

/**
 * Created by tim on 7/3/15.
 */
public class ShapewaysCommonApi
{
    protected final ShapewaysRequestFactory requestFactory;
    protected final Gson gson;

    public ShapewaysCommonApi(ShapewaysRequestFactory requestFactory, Gson gson)
    {
        this.requestFactory = requestFactory;
        this.gson = gson;
    }
}
