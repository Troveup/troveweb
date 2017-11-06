package com.troveup.brooklyn.model;

/**
 * Created by tim on 2/8/16.
 */
public class GenericAjaxResponse
{
    public static String standardGenericErrorMessage = "Oops!  Something went wrong.  Please try that action again.";
    protected Boolean isSuccess;
    protected String errorMessage;

    public GenericAjaxResponse()
    {

    }

    public GenericAjaxResponse(Boolean isSuccess, String errorMessage)
    {
        this.isSuccess = isSuccess;
        this.errorMessage = errorMessage;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
