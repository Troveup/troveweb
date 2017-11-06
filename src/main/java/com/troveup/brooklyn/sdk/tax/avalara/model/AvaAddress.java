package com.troveup.brooklyn.sdk.tax.avalara.model;

/**
 * Created by tim on 5/27/15.
 */
public class AvaAddress
{
    //Request
    private String Line1;
    private String Line2;
    private String Line3;

    //Shared
    private String TaxRegionId;
    private String AddressCode;
    private String City;
    private String Region;
    private String Country;
    private String PostalCode;
    private String Latitude;
    private String Longitude;

    //Response
    private String Address;
    private String JurisCode;


    public AvaAddress()
    {

    }

    public void initializeForSimpleTaxRequest(String addressCode, String line1, String city, String region,
                                              String postalCode, String country)
    {
        this.AddressCode = addressCode;
        this.Line1 = line1;
        this.City = city;
        this.Region = region;
        this.PostalCode = postalCode;
        this.Country = country;
    }

    public String getAddressCode() {
        return AddressCode;
    }

    public void setAddressCode(String addressCode) {
        this.AddressCode = addressCode;
    }

    public String getLine1() {
        return Line1;
    }

    public void setLine1(String line1) {
        this.Line1 = line1;
    }

    public String getLine2() {
        return Line2;
    }

    public void setLine2(String line2) {
        this.Line2 = line2;
    }

    public String getLine3() {
        return Line3;
    }

    public void setLine3(String line3) {
        this.Line3 = line3;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        this.City = city;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        this.Region = region;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        this.Country = country;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        this.PostalCode = postalCode;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        this.Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        this.Longitude = longitude;
    }

    public String getTaxRegionId() {
        return TaxRegionId;
    }

    public void setTaxRegionId(String taxRegionId) {
        this.TaxRegionId = taxRegionId;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public String getJurisCode() {
        return JurisCode;
    }

    public void setJurisCode(String jurisCode) {
        this.JurisCode = jurisCode;
    }
}
