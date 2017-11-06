package com.troveup.brooklyn.sdk.mail.model;

/**
 * Created by tim on 7/4/15.
 */
public class EasyPostResponseError
{
    private EasyPostError error;

    public EasyPostResponseError()
    {

    }

    public EasyPostError getError() {
        return error;
    }

    public void setError(EasyPostError error) {
        this.error = error;
    }
}
