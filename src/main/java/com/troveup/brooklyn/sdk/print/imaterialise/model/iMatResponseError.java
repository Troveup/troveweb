package com.troveup.brooklyn.sdk.print.imaterialise.model;

/**
 * Created by tim on 5/23/15.
 */
public class iMatResponseError
{
    private int code;
    private String message;

    public iMatResponseError()
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
