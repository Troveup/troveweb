package com.troveup.brooklyn.orm.user.model;

import com.troveup.brooklyn.orm.countries.model.Country;
import com.troveup.brooklyn.orm.countries.model.Subdivision;
import com.troveup.brooklyn.sdk.print.imaterialise.model.iMatAddress;
import com.troveup.brooklyn.sdk.tax.avalara.model.AvaAddress;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 5/12/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class Address
{
    public enum ADDRESS_TYPE {
        SHIPPING("Shipping"),
        BILLING("Billing");

        private final String text;

        ADDRESS_TYPE(String type) {
            this.text = type;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    String firstName;
    String lastName;
    String email;
    String phone;
    Long addressId;
    String addressLine1;
    String addressLine2;
    String city;
    Subdivision subdivision;
    String postalCode;
    Country country;
    ADDRESS_TYPE addressType;

    public Address(){

    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    @Persistent
    @Column(name = "address_line_1", allowsNull = "false")
    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    @Persistent
    @Column(name = "address_line_2", allowsNull = "true")
    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    @Persistent
    @Column(name = "city", allowsNull = "true")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Join(table = "ADDRESS_SUBDIVISION")
    @Persistent
    public Subdivision getSubdivision() {
        return subdivision;
    }

    public void setSubdivision(Subdivision subdivision) {
        this.subdivision = subdivision;
    }

    @Persistent
    @Column(name = "postal_code", allowsNull = "true")
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Join(table = "ADDRESS_COUNTRY")
    @Persistent
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Persistent
    @Column(name = "address_type", allowsNull = "true")
    public ADDRESS_TYPE getAddressType() {
        return addressType;
    }

    public void setAddressType(ADDRESS_TYPE addressType) {
        this.addressType = addressType;
    }

    @Persistent
    @Column(name = "first_name", allowsNull = "true")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Persistent
    @Column(name = "last_name", allowsNull = "true")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Persistent
    @Column(name = "email", allowsNull = "true")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Persistent
    @Column(name = "phone", allowsNull = "true")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static List<String> getAddressCartFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("subdivision");
        rval.add("country");
        rval.add("addressType");

        return rval;
    }

    //TODO:  Migrate all of these converters
    public iMatAddress toiMatAddress()
    {
        iMatAddress rval = new iMatAddress();

        rval.setFirstName(firstName);
        rval.setLastName(lastName);
        rval.setEmail(email);
        rval.setPhone(phone);
        rval.setLine1(addressLine1);
        rval.setLine2(addressLine2);
        rval.setCity(city);
        rval.setStateCode(Subdivision.getAbbreviatedSubdivisionCode(subdivision.getCode()));
        rval.setCountryCode(country.getIsoAlpha2Code());
        rval.setZipCode(postalCode);

        return rval;
    }

    public AvaAddress toAvaAddress()
    {
        AvaAddress rval = new AvaAddress();
        rval.setLine1(addressLine1);
        rval.setLine2(addressLine2);
        rval.setCity(city);
        rval.setRegion(Subdivision.getAbbreviatedSubdivisionCode(subdivision.getCode()));
        rval.setPostalCode(postalCode);

        return rval;
    }

    //Tailored for easypost, modify for others if necessary
    public static Address getTroveFromAddress()
    {
        Address rval = new Address();

        rval.setFirstName("Trove, Inc.");
        rval.setAddressLine1("20 Exchange Place");
        rval.setAddressLine2("Apt 1604");
        rval.setCity("New York");
        Subdivision subdivision = new Subdivision();
        subdivision.setCode("US-NY");

        rval.setSubdivision(subdivision);
        rval.setPostalCode("10005");
        rval.setPhone("3108096011");

        return rval;
    }
}
