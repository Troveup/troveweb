package com.troveup.brooklyn.model;

/**
 * Created by tim on 9/21/15.
 */
public class CompleteCartOrderResponse
{
    private Boolean success;
    private String message;

    public CompleteCartOrderResponse()
    {

    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
