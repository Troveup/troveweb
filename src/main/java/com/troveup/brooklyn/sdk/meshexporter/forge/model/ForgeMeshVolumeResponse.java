package com.troveup.brooklyn.sdk.meshexporter.forge.model;

/**
 * Created by tim on 8/29/15.
 */
public class ForgeMeshVolumeResponse
{
    private String volume;
    private String units;
    private Boolean success;
    private String message;

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
