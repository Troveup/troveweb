package com.troveup.brooklyn.model;

/**
 * Created by tim on 5/22/15.
 */
public abstract class GenericAttributeModel
{
    public enum VALUE_TYPE
    {
        BOOLEAN,
        STRING,
        INT,
        LONG,
        DOUBLE,
        FLOAT
    }

    private String paramName;
    private String paramValue;
    private VALUE_TYPE paramDataType;

    public  GenericAttributeModel()
    {

    }

    public GenericAttributeModel(String paramName, String paramValue)
    {
        this.paramName = paramName;
        this.paramValue = paramValue;
        this.paramDataType = VALUE_TYPE.STRING;
    }

    public GenericAttributeModel(String paramName, String paramValue, VALUE_TYPE paramDataType)
    {
        this.paramName = paramName;
        this.paramValue = paramValue;
        this.paramDataType = paramDataType;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public VALUE_TYPE getParamDataType() {
        return paramDataType;
    }

    public void setParamDataType(VALUE_TYPE paramDataType) {
        this.paramDataType = paramDataType;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof GenericAttributeModel)
            return ((GenericAttributeModel) obj).getParamName().equals(paramName);
        else
            return false;
    }
}
