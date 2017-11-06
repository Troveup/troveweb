package com.troveup.brooklyn.sdk.print.model;

import com.troveup.brooklyn.model.GenericAttributeModel;

/**
 * Created by tim on 5/22/15.
 */
public class PriceRequestAttribute extends GenericAttributeModel
{
    public  PriceRequestAttribute()
    {

    }

    public PriceRequestAttribute(String paramName, String paramValue)
    {
        super(paramName, paramValue);
    }

    public PriceRequestAttribute(String paramName, String paramValue, VALUE_TYPE paramDataType)
    {
        super(paramName, paramValue, paramDataType);
    }
}
