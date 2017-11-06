package com.troveup.brooklyn.sdk.tax.avalara.model;

import java.util.Date;

/**
 * Created by tim on 5/27/15.
 */
public class AvaTaxOverride
{
    public enum TAX_OVERRIDE_TYPE
    {
        NONE("None"),
        TAX_AMOUNT("TaxAmount"),
        DOCUMENTEXEMPTION("Exemption"),
        TAX_DATE("TaxDate");

        TAX_OVERRIDE_TYPE(String value)
        {
            this.value = value;
        }

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString()
        {
            return value;
        }
    }

    private String Reason;
    private String TaxOverrideType;
    //Format (YYYY-MM-DD)
    private Date TaxDate;
    private Float TaxAmount;

    public AvaTaxOverride()
    {

    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getTaxOverrideType() {
        return TaxOverrideType;
    }

    public void setTaxOverrideType(String taxOverrideType) {
        this.TaxOverrideType = taxOverrideType;
    }

    public Date getTaxDate() {
        return TaxDate;
    }

    public void setTaxDate(Date taxDate) {
        this.TaxDate = taxDate;
    }

    public Float getTaxAmount() {
        return TaxAmount;
    }

    public void setTaxAmount(Float taxAmount) {
        TaxAmount = taxAmount;
    }
}
