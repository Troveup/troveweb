package com.troveup.brooklyn.model;

/**
 * Created by tim on 9/21/15.
 */
public class FtueCheckoutSubmitResult
{
    private Boolean success;
    private String message;

    public FtueCheckoutSubmitResult()
    {

    }

    public FtueCheckoutSubmitResult(Boolean success, String message)
    {
        this.success = success;
        this.message = message;
    }

    public FtueCheckoutSubmitResult(Boolean success)
    {
        this.success = success;
        this.message = "";
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
