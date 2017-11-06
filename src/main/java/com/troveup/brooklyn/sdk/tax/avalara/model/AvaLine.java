package com.troveup.brooklyn.sdk.tax.avalara.model;

import java.util.List;

/**
 * Created by tim on 5/27/15.
 */
public class AvaLine
{
    //Request fields
    private String LineNo;
    private String DestinationCode;
    private String OriginCode;
    private String ItemCode;
    private String TaxCode;
    private String CustomerUsageType;
    private String BusinessIdentificationNo;
    private String Description;
    private int Qty;
    private Float Amount;
    private Boolean Discounted;
    private Boolean TaxIncluded;
    private String Ref1;
    private String Ref2;
    private AvaTaxOverride TaxOverride;

    //Response fields
    private Boolean Taxability;
    private Float Taxable;
    private Float Rate;
    private Float Tax;
    private Float Discount;
    private Float TaxCalculated;
    private Float Exemption;
    private String BoundaryLevel;
    private List<AvaTaxDetail> AvaTaxDetails;

    public AvaLine()
    {

    }

    public void initializeForSimpleTaxRequest(String lineNo, String originCode, String destinationCode, String itemCode,
                                              int qty, Float amount)
    {
        this.LineNo = lineNo;
        this.OriginCode = originCode;
        this.DestinationCode = destinationCode;
        this.ItemCode = itemCode;
        this.Qty = qty;
        this.Amount = amount;
    }

    public String getLineNo() {
        return LineNo;
    }

    public void setLineNo(String lineNo) {
        this.LineNo = lineNo;
    }

    public String getDestinationCode() {
        return DestinationCode;
    }

    public void setDestinationCode(String destinationCode) {
        this.DestinationCode = destinationCode;
    }

    public String getOriginCode() {
        return OriginCode;
    }

    public void setOriginCode(String originCode) {
        this.OriginCode = originCode;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        this.ItemCode = itemCode;
    }

    public String getTaxCode() {
        return TaxCode;
    }

    public void setTaxCode(String taxCode) {
        this.TaxCode = taxCode;
    }

    public String getCustomerUsageType() {
        return CustomerUsageType;
    }

    public void setCustomerUsageType(String customerUsageType) {
        this.CustomerUsageType = customerUsageType;
    }

    public String getBusinessIdentificationNo() {
        return BusinessIdentificationNo;
    }

    public void setBusinessIdentificationNo(String businessIdentificationNo) {
        this.BusinessIdentificationNo = businessIdentificationNo;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        this.Qty = qty;
    }

    public Float getAmount() {
        return Amount;
    }

    public void setAmount(Float amount) {
        this.Amount = amount;
    }

    public Boolean getDiscounted() {
        return Discounted;
    }

    public void setDiscounted(Boolean discounted) {
        this.Discounted = discounted;
    }

    public Boolean getTaxIncluded() {
        return TaxIncluded;
    }

    public void setTaxIncluded(Boolean taxIncluded) {
        this.TaxIncluded = taxIncluded;
    }

    public String getRef1() {
        return Ref1;
    }

    public void setRef1(String ref1) {
        this.Ref1 = ref1;
    }

    public String getRef2() {
        return Ref2;
    }

    public void setRef2(String ref2) {
        this.Ref2 = ref2;
    }

    public AvaTaxOverride getTaxOverride() {
        return TaxOverride;
    }

    public void setTaxOverride(AvaTaxOverride taxOverride) {
        this.TaxOverride = taxOverride;
    }

    public Boolean getTaxability() {
        return Taxability;
    }

    public void setTaxability(Boolean taxability) {
        this.Taxability = taxability;
    }

    public Float getTaxable() {
        return Taxable;
    }

    public void setTaxable(Float taxable) {
        this.Taxable = taxable;
    }

    public Float getRate() {
        return Rate;
    }

    public void setRate(Float rate) {
        this.Rate = rate;
    }

    public Float getTax() {
        return Tax;
    }

    public void setTax(Float tax) {
        this.Tax = tax;
    }

    public Float getDiscount() {
        return Discount;
    }

    public void setDiscount(Float discount) {
        this.Discount = discount;
    }

    public Float getTaxCalculated() {
        return TaxCalculated;
    }

    public void setTaxCalculated(Float taxCalculated) {
        this.TaxCalculated = taxCalculated;
    }

    public Float getExemption() {
        return Exemption;
    }

    public void setExemption(Float exemption) {
        this.Exemption = exemption;
    }

    public String getBoundaryLevel() {
        return BoundaryLevel;
    }

    public void setBoundaryLevel(String boundaryLevel) {
        this.BoundaryLevel = boundaryLevel;
    }

    public List<AvaTaxDetail> getAvaTaxDetails() {
        return AvaTaxDetails;
    }

    public void setAvaTaxDetails(List<AvaTaxDetail> avaTaxDetails) {
        this.AvaTaxDetails = avaTaxDetails;
    }
}
