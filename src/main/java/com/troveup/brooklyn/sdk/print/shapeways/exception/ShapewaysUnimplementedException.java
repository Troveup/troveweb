package com.troveup.brooklyn.sdk.print.shapeways.exception;

/**
 * Created by tim on 7/23/15.
 */
public class ShapewaysUnimplementedException extends RuntimeException
{
    public ShapewaysUnimplementedException()
    {

    }

    public ShapewaysUnimplementedException(String message)
    {
        super("This method is unimplemented.");
    }
}
