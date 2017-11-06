package com.troveup.brooklyn.sdk.meshexporter.forge.model;

/**
 * Created by tim on 8/29/15.
 */
public class ForgeMeshExportResponse
{
    private String error;
    private String message;
    private String sourceModel;
    private String exportURL;
    private String volume;
    private Boolean success;

    public ForgeMeshExportResponse()
    {

    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSourceModel() {
        return sourceModel;
    }

    public void setSourceModel(String sourceModel) {
        this.sourceModel = sourceModel;
    }

    public String getExportURL() {
        return exportURL;
    }

    public void setExportURL(String exportURL) {
        this.exportURL = exportURL;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }
}
