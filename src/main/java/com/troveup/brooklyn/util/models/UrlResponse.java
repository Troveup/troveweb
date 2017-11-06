package com.troveup.brooklyn.util.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 5/22/15.
 */
public class UrlResponse
{
    private Integer responseCode;
    private List<String> responseBody;
    private String responseMessage;

    public UrlResponse()
    {
        responseBody = new ArrayList<>();
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public List<String> getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(List<String> responseBody) {
        this.responseBody = responseBody;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}

