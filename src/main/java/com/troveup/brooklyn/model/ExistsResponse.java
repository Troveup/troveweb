package com.troveup.brooklyn.model;

/**
 * Created by tim on 6/8/16.
 */
public class ExistsResponse extends GenericAjaxResponse
{
    private Boolean valid;
    private Boolean exists;
    private String checkedValue;

    public ExistsResponse(Boolean isSuccess, String errorMessage)
    {
        super(isSuccess, errorMessage);
    }

    public Boolean getExists() {
        return exists;
    }

    public void setExists(Boolean exists) {
        this.exists = exists;
    }

    public String getCheckedValue() {
        return checkedValue;
    }

    public void setCheckedValue(String checkedValue) {
        this.checkedValue = checkedValue;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }
}
