package com.troveup.brooklyn.model;

/**
 * Created by tim on 6/8/16.
 */
public class BooleanResponse extends GenericAjaxResponse
{
    private Boolean value;

    public BooleanResponse(Boolean isSuccess, String errorMessage)
    {
        super(isSuccess, errorMessage);
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }
}
