package com.troveup.brooklyn.sdk.print.model;

import com.troveup.brooklyn.model.GenericAttributeModel;

import java.util.List;

/**
 * Created by tim on 5/22/15.
 */
public class OrderStatusAttribute extends GenericAttributeModel
{
    Object extraInformationObject;

    public  OrderStatusAttribute()
    {

    }

    public OrderStatusAttribute(String paramName, String paramValue)
    {
        super(paramName, paramValue);
    }

    public OrderStatusAttribute(String paramName, String paramValue, GenericAttributeModel.VALUE_TYPE paramDataType)
    {
        super(paramName, paramValue, paramDataType);
    }

    public Object getExtraInformationObject() {
        return extraInformationObject;
    }

    public void setExtraInformationObject(Object extraInformationObject) {
        this.extraInformationObject = extraInformationObject;
    }
}
