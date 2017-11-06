package com.troveup.brooklyn.sdk.mail.model;

/**
 * Created by tim on 8/25/15.
 */
public class EasyPostTrackingDetail
{
    private String object;
    private String message;
    private String status;
    private String datetime;
    private EasyPostTrackingLocation tracking_location;

    public EasyPostTrackingDetail()
    {

    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public EasyPostTrackingLocation getTracking_location() {
        return tracking_location;
    }

    public void setTracking_location(EasyPostTrackingLocation tracking_location) {
        this.tracking_location = tracking_location;
    }
}
