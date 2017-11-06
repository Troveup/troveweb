package com.troveup.brooklyn.sdk.tax.avalara.model;

/**
 * Created by tim on 5/27/15.
 */
public class AvaTaxDetail
{
    private String Country;
    private String JurisCode;
    private String JurisName;
    private String JurisType;
    private Float Rate;
    private String Region;
    private Float Tax;
    private Float Taxable;
    private String TaxName;

    public AvaTaxDetail()
    {

    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        this.Country = country;
    }

    public String getJurisCode() {
        return JurisCode;
    }

    public void setJurisCode(String jurisCode) {
        this.JurisCode = jurisCode;
    }

    public String getJurisName() {
        return JurisName;
    }

    public void setJurisName(String jurisName) {
        this.JurisName = jurisName;
    }

    public String getJurisType() {
        return JurisType;
    }

    public void setJurisType(String jurisType) {
        this.JurisType = jurisType;
    }

    public Float getRate() {
        return Rate;
    }

    public void setRate(Float rate) {
        this.Rate = rate;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        this.Region = region;
    }

    public Float getTax() {
        return Tax;
    }

    public void setTax(Float tax) {
        this.Tax = tax;
    }

    public Float getTaxable() {
        return Taxable;
    }

    public void setTaxable(Float taxable) {
        this.Taxable = taxable;
    }

    public String getTaxName() {
        return TaxName;
    }

    public void setTaxName(String taxName) {
        this.TaxName = taxName;
    }
}
