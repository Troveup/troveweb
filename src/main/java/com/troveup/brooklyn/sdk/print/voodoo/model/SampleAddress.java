package com.troveup.brooklyn.sdk.print.voodoo.model;

import com.troveup.brooklyn.orm.ftui.model.FtueRequestAddress;

/**
 * Created by tim on 6/18/15.
 */
public class SampleAddress
{
    private String city;
    private String name;
    private String zip;
    private String street1;
    private String street2;
    private String state;
    private String country;

    public SampleAddress()
    {

    }

    public SampleAddress(String city, String fullFirstAndLastName, String zip, String street1, String street2,
                         String state, String country)
    {
        this.city = city;
        this.name = fullFirstAndLastName;
        this.zip = zip;
        this.street1 = street1;
        this.street2 = street2;
        this.state = state;
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
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

    public FtueRequestAddress toFtueAddress()
    {
        FtueRequestAddress address = new FtueRequestAddress();
        address.setName(name);
        address.setStreet1(street1);
        address.setStreet2(street2);
        address.setCity(city);
        address.setState(state);
        address.setZip(zip);
        address.setCountry(country);

        return address;
    }
}
