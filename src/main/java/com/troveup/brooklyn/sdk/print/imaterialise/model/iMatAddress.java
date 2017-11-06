package com.troveup.brooklyn.sdk.print.imaterialise.model;

/**
 * Created by tim on 5/25/15.
 */
public class iMatAddress
{
    //Shipping and Billing Required
    private String firstName;

    //Shipping and Billing Required
    private String lastName;

    //Shipping and Billing Required
    private String email;

    //Shipping and Billing Required
    private String phone;

    //Shipping and Billing not required,
    //for shipping, should be specified in
    //conjunction with vatNumber
    private String company;

    //Shipping and Billing not required,
    //for shipping, should be specified in
    //conjunction with company
    private String vatNumber;

    //Shipping and Billing Required
    private String line1;

    //Shipping and Billing Required
    private String line2;

    //Shipping and Billing Required
    private String countryCode;

    //Shipping and Billing Required
    private String stateCode;

    //Shipping and Billing Required
    private String zipCode;

    //Shipping and Billing Required
    private String city;

    private iMatResponseError shippingInfoError;

    private iMatResponseError billingInfoError;

    public iMatAddress()
    {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public iMatResponseError getShippingInfoError() {
        return shippingInfoError;
    }

    public void setShippingInfoError(iMatResponseError shippingInfoError) {
        this.shippingInfoError = shippingInfoError;
    }

    public iMatResponseError getBillingInfoError() {
        return billingInfoError;
    }

    public void setBillingInfoError(iMatResponseError billingInfoError) {
        this.billingInfoError = billingInfoError;
    }
}
