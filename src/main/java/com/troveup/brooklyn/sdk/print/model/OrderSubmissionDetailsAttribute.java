package com.troveup.brooklyn.sdk.print.model;

import com.troveup.brooklyn.model.GenericAttributeModel;

/**
 * Created by tim on 5/22/15.
 */
public class OrderSubmissionDetailsAttribute extends GenericAttributeModel
{
    public  OrderSubmissionDetailsAttribute()
    {

    }

    public OrderSubmissionDetailsAttribute(String paramName, String paramValue)
    {
        super(paramName, paramValue);
    }

    public OrderSubmissionDetailsAttribute(String paramName, String paramValue, GenericAttributeModel.VALUE_TYPE paramDataType)
    {
        super(paramName, paramValue, paramDataType);
    }
}
