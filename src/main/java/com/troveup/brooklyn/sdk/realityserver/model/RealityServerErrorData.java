package com.troveup.brooklyn.sdk.realityserver.model;

/**
 * Created by tim on 6/24/15.
 */
public class RealityServerErrorData
{
    private int code;
    private String message;

    public RealityServerErrorData()
    {

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
