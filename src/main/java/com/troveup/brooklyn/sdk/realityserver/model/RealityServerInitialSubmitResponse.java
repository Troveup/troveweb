package com.troveup.brooklyn.sdk.realityserver.model;

/**
 * Created by tim on 6/21/15.
 */
public class RealityServerInitialSubmitResponse
{
    private int errorId;
    private String errorMessage;
    private RealityServerJobResponse response;

    public RealityServerInitialSubmitResponse()
    {

    }

    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public RealityServerJobResponse getResponse() {
        return response;
    }

    public void setResponse(RealityServerJobResponse response) {
        this.response = response;
    }
}
