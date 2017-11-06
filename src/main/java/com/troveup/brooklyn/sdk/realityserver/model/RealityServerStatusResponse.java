package com.troveup.brooklyn.sdk.realityserver.model;

/**
 * Created by tim on 9/15/15.
 */
public class RealityServerStatusResponse
{
    private String status;
    private String message;

    public RealityServerStatusResponse()
    {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
