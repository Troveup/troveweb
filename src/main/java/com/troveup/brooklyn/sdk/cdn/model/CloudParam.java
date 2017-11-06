package com.troveup.brooklyn.sdk.cdn.model;

import com.troveup.brooklyn.model.GenericAttributeModel;

/**
 * Created by tim on 5/20/15.
 */
public class CloudParam extends GenericAttributeModel
{
    public  CloudParam()
    {

    }

    public CloudParam(String paramName, String paramValue)
    {
        super(paramName, paramValue);
    }

    public CloudParam(String paramName, String paramValue, VALUE_TYPE paramDataType)
    {
        super(paramName, paramValue, paramDataType);
    }
}
