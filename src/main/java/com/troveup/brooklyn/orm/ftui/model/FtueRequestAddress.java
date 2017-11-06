package com.troveup.brooklyn.orm.ftui.model;

import com.troveup.brooklyn.sdk.print.voodoo.model.SampleAddress;

import javax.jdo.annotations.*;

/**
 * Created by tim on 6/16/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class FtueRequestAddress
{
    private Long futeRequestAddressId;
    private String city;
    //Appears to be the user's full first and last name
    private String name;
    private String zip;
    private String street1;
    private String street2;
    private String state;
    private String country;
    private String email;

    public FtueRequestAddress()
    {

    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getFuteRequestAddressId() {
        return futeRequestAddressId;
    }

    public void setFuteRequestAddressId(Long futeRequestAddressId) {
        this.futeRequestAddressId = futeRequestAddressId;
    }

    @Persistent
    @Column(name = "city", allowsNull = "true")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Persistent
    @Column(name = "full_user_name", allowsNull = "true")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Persistent
    @Column(name = "zipcode", allowsNull = "true")
    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @Persistent
    @Column(name = "street_one", allowsNull = "true")
    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    @Persistent
    @Column(name = "street_two", allowsNull = "true")
    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    @Persistent
    @Column(name = "state", allowsNull = "true")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Persistent
    @Column(name = "country", allowsNull = "true")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Persistent
    @Column(name = "email", allowsNull = "true")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof FtueRequestAddress)
        {
            if (((FtueRequestAddress) obj).getName().contains(name) &&
                    ((FtueRequestAddress) obj).getStreet1().contains(street1)
                    && ((FtueRequestAddress) obj).getStreet2().contains(street2)
                    && ((FtueRequestAddress) obj).getCity().contains(city)
                    && ((FtueRequestAddress) obj).getState().contains(state)
                    && ((FtueRequestAddress) obj).getZip().contains(zip))
                return true;
            else
                return false;
        }
        else
            return false;
    }

    public SampleAddress toSampleAddress()
    {
        SampleAddress rval = new SampleAddress();
        rval.setName(name);
        rval.setStreet1(street1);
        rval.setStreet2(street2);
        rval.setCity(city);
        rval.setState(state);
        rval.setZip(zip);
        rval.setCountry(country);

        return rval;
    }

    public static FtueRequestAddress getTroveFromAddress()
    {
        FtueRequestAddress fromAddress = new FtueRequestAddress();
        fromAddress.setStreet1("20 Exchange Place");
        fromAddress.setStreet2("Apt 1604");
        fromAddress.setCity("New York");
        fromAddress.setState("NY");
        fromAddress.setZip("10005");
        fromAddress.setName("Trove, Inc.");

        return fromAddress;
    }
}
