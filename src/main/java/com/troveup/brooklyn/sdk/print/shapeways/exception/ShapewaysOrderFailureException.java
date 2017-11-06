package com.troveup.brooklyn.sdk.print.shapeways.exception;

import com.troveup.brooklyn.sdk.print.shapeways.api.ShapewaysCommonApi;

/**
 * Created by tim on 7/23/15.
 */
public class ShapewaysOrderFailureException extends Exception
{
    public ShapewaysOrderFailureException()
    {

    }

    public ShapewaysOrderFailureException(String message)
    {
        super(message);
    }
}
