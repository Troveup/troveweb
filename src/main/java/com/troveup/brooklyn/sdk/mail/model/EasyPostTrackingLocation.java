package com.troveup.brooklyn.sdk.mail.model;

/**
 * Created by tim on 8/25/15.
 */
public class EasyPostTrackingLocation
{
    private String object;
    private String city;
    private String state;
    private String country;
    private String zip;

    public EasyPostTrackingLocation()
    {

    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
