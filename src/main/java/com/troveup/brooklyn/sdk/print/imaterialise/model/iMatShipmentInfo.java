package com.troveup.brooklyn.sdk.print.imaterialise.model;

/**
 * Created by tim on 5/24/15.
 */
public class iMatShipmentInfo
{
    String countryCode;
    String stateCode;
    String city;
    String zipCode;

    public iMatShipmentInfo()
    {

    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
