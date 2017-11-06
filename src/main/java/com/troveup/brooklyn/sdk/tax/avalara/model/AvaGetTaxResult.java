package com.troveup.brooklyn.sdk.tax.avalara.model;

import java.util.List;

/**
 * Created by tim on 5/27/15.
 */
public class AvaGetTaxResult
{
    private String DocCode;
    private String Date;
    private String TimeStamp;
    private Float TotalAmount;
    private Float TotalDiscount;
    private Float TotalExemption;
    private Float TotalTaxable;
    private Float TotalTax;
    private Float TotalTaxCalculated;
    private String TaxDate;
    private List<AvaLine> TaxLines;
    private List<AvaTaxDetail> TaxSummary;
    private List<AvaTaxDetail> TaxDetails;
    private List<AvaAddress> TaxAddresses;
    private String ResultCode;
    private List<AvaMessage> Messages;

    public AvaGetTaxResult()
    {

    }

    public String getDocCode() {
        return DocCode;
    }

    public void setDocCode(String docCode) {
        this.DocCode = docCode;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.TimeStamp = timeStamp;
    }

    public Float getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(Float totalAmount) {
        this.TotalAmount = totalAmount;
    }

    public Float getTotalDiscount() {
        return TotalDiscount;
    }

    public void setTotalDiscount(Float totalDiscount) {
        this.TotalDiscount = totalDiscount;
    }

    public Float getTotalExemption() {
        return TotalExemption;
    }

    public void setTotalExemption(Float totalExemption) {
        this.TotalExemption = totalExemption;
    }

    public Float getTotalTaxable() {
        return TotalTaxable;
    }

    public void setTotalTaxable(Float totalTaxable) {
        this.TotalTaxable = totalTaxable;
    }

    public Float getTotalTax() {
        return TotalTax;
    }

    public void setTotalTax(Float totalTax) {
        this.TotalTax = totalTax;
    }

    public Float getTotalTaxCalculated() {
        return TotalTaxCalculated;
    }

    public void setTotalTaxCalculated(Float totalTaxCalculated) {
        this.TotalTaxCalculated = totalTaxCalculated;
    }

    public String getTaxDate() {
        return TaxDate;
    }

    public void setTaxDate(String taxDate) {
        this.TaxDate = taxDate;
    }

    public List<AvaLine> getTaxLines() {
        return TaxLines;
    }

    public void setTaxLines(List<AvaLine> taxLines) {
        this.TaxLines = taxLines;
    }

    public List<AvaTaxDetail> getTaxSummary() {
        return TaxSummary;
    }

    public void setTaxSummary(List<AvaTaxDetail> taxSummary) {
        this.TaxSummary = taxSummary;
    }

    public List<AvaTaxDetail> getTaxDetails() {
        return TaxDetails;
    }

    public void setTaxDetails(List<AvaTaxDetail> taxDetails) {
        this.TaxDetails = taxDetails;
    }

    public List<AvaAddress> getTaxAddresses() {
        return TaxAddresses;
    }

    public void setTaxAddresses(List<AvaAddress> taxAddresses) {
        this.TaxAddresses = taxAddresses;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public void setResultCode(String resultCode) {
        this.ResultCode = resultCode;
    }

    public List<AvaMessage> getMessages() {
        return Messages;
    }

    public void setMessages(List<AvaMessage> messages) {
        this.Messages = messages;
    }

}
