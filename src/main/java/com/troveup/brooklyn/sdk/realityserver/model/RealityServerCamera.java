package com.troveup.brooklyn.sdk.realityserver.model;

/**
 * Created by tim on 7/27/15.
 */
public class RealityServerCamera
{
    private String url;
    private Integer errorId;
    private String errorMessage;
    private String filename;

    public RealityServerCamera()
    {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getErrorId() {
        return errorId;
    }

    public void setErrorId(Integer errorId) {
        this.errorId = errorId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
