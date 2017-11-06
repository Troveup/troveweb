package com.troveup.brooklyn.model;


/**
 * Created by tim on 6/24/15.
 */
public class FtueAjaxResponse extends GenericAjaxResponse
{
    private String referralCode;

    public FtueAjaxResponse()
    {

    }

    public FtueAjaxResponse(Boolean isSuccess, String errorMessage)
    {
        this.setIsSuccess(isSuccess);
        this.setErrorMessage(errorMessage);
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }
}
