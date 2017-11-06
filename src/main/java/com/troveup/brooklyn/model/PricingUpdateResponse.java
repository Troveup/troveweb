package com.troveup.brooklyn.model;

/**
 * Created by tim on 11/23/15.
 */
public class PricingUpdateResponse
{
    private Boolean success;
    private Integer updateCount;


    public PricingUpdateResponse()
    {

    }

    public PricingUpdateResponse(Boolean success, Integer updateCount)
    {

    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(Integer updateCount) {
        this.updateCount = updateCount;
    }
}
