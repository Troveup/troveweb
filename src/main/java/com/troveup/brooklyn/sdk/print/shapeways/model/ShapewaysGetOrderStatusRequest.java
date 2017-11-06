package com.troveup.brooklyn.sdk.print.shapeways.model;

/**
 * Created by tim on 7/22/15.
 */
public class ShapewaysGetOrderStatusRequest
{
    private String orderId;
    private String firstName;
    private String lastName;
    private String country;
    private String state;
    private String city;
    private String address1;
    private String address2;
    private String address3;
    private String zipCode;
    private String phoneNumber;
    private String email;

    public ShapewaysGetOrderStatusRequest()
    {

    }

    public static ShapewaysGetOrderStatusRequest ShapewaysGetOrderStatusRequestByOrderId(String orderId)
    {
        ShapewaysGetOrderStatusRequest rval = new ShapewaysGetOrderStatusRequest();
        rval.orderId = orderId;

        return rval;
    }

    public static ShapewaysGetOrderStatusRequest ShapewaysGetOrderStatusRequestByEmail(String email)
    {
        ShapewaysGetOrderStatusRequest rval = new ShapewaysGetOrderStatusRequest();
        rval.email = email;

        return rval;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
