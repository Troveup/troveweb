package com.troveup.brooklyn.sdk.mail.model;

/**
 * Created by tim on 7/4/15.
 */
public class EasyPostError
{
    private String code;
    private String message;

    public EasyPostError()
    {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
