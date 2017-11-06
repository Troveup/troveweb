package com.troveup.brooklyn.sdk.tax.avalara.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 5/27/15.
 */
public class AvaGetTaxRequest
{
    public enum CUSTOMER_USAGE_TYPE
    {
        FEDERAL_GOVERNMENT("A"),
        STATE_LOCAL_GOVERNMENT("B"),
        TRIBAL_GOVERNMENT("C"),
        FOREIGN_DIPLOMAT("D"),
        CHARITABLE_ORGANIZATION("E"),
        RELIGIOUS_EDUCATION("F"),
        RESALE("G"),
        AGRICULTURAL_PRODUCTION("H"),
        INDUSTRIAL_PRODUCTION_MANUFACTURING("I"),
        DIRECT_PAY_PERMIT("J"),
        DIRECT_MAIL("K"),
        OTHER("L"),
        LOCAL_GOVERNMENT("N"),
        COMMERCIAL_AQUACULTURE("P"),
        COMMERCIAL_FISHERY("Q"),
        NON_RESIDENT("R"),
        US_MDET_EXEMPT("MED1"),
        US_MDET_TAXABLE("MED2");

        CUSTOMER_USAGE_TYPE(String value)
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

    public enum DETAIL_LEVEL
    {
        TAX("Tax"),
        LINE("Line"),
        DOCUMENT("Document"),
        SUMMARY("Summary");

        DETAIL_LEVEL(String value)
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
    public enum DOC_TYPE
    {
        SALES_ORDER("SalesOrder"),
        SALES_INVOICE("SalesInvoice"),
        PURCHASE_ORDER("PurchaseOrder"),
        PURCHASE_INVOICE("PurchaseInvoice"),
        RETURN_ORDER("ReturnOrder"),
        RETURN_INVOICE("ReturnInvoice");


        DOC_TYPE(String value)
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

    private String BusinesssIdentificationNo;
    private Boolean Commit;
    private String Client;
    private String CompanyCode;
    private String CustomerCode;
    private String CurrencyCode;
    private String CustomerUsageType;
    private String DetailLevel;
    private Float Discount;
    private String DocCode;
    //Must be converted to yyyy-MM-dd
    private String DocDate;
    private String DocType;
    private String ExemptionNo;
    private String PosLaneCode;
    private String PurchaseOrderNo;
    private String ReferenceCode;
    private AvaTaxOverride AvaTaxOverride;
    private List<AvaAddress> Addresses;
    private List<AvaLine> Lines;

    public AvaGetTaxRequest()
    {

    }

    public void initializeForSimpleTaxRequest(String companyCode, DOC_TYPE docType, String docCode, Date docDate,
                                              String customerCode, List<AvaLine> lines, List<AvaAddress> addresses,
                                              Boolean commit)
    {
        this.CompanyCode = companyCode;
        this.DocType = docType.toString();
        this.DocCode = docCode;
        this.setDocDate(docDate);
        this.CustomerCode = customerCode;
        this.Lines = lines;
        this.Addresses = addresses;
        this.Commit = commit;

    }

    public String getBusinesssIdentificationNo() {
        return BusinesssIdentificationNo;
    }

    public void setBusinesssIdentificationNo(String businesssIdentificationNo) {
        BusinesssIdentificationNo = businesssIdentificationNo;
    }

    public Boolean getCommit() {
        return Commit;
    }

    public void setCommit(Boolean commit) {
        this.Commit = commit;
    }

    public String getClient() {
        return Client;
    }

    public void setClient(String client) {
        this.Client = client;
    }

    public String getCompanyCode() {
        return CompanyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.CompanyCode = companyCode;
    }

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.CustomerCode = customerCode;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.CurrencyCode = currencyCode;
    }

    public String getCustomerUsageType() {
        return CustomerUsageType;
    }

    public void setCustomerUsageType(String customerUsageType) {
        this.CustomerUsageType = customerUsageType;
    }

    public String getDetailLevel() {
        return DetailLevel;
    }

    public void setDetailLevel(String detailLevel) {
        this.DetailLevel = detailLevel;
    }

    public Float getDiscount() {
        return Discount;
    }

    public void setDiscount(Float discount) {
        this.Discount = discount;
    }

    public String getDocCode() {
        return DocCode;
    }

    public void setDocCode(String docCode) {
        this.DocCode = docCode;
    }

    public String getDocDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(DocDate);
    }

    public void setDocDate(Date docDate)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        this.DocDate = df.format(docDate);
    }

    public void setDocDate(String docDate) {
        this.DocDate = docDate;
    }

    public String getDocType() {
        return DocType.toString();
    }

    public void setDocType(String docType) {
        this.DocType = docType;
    }

    public String getExemptionNo() {
        return ExemptionNo;
    }

    public void setExemptionNo(String exemptionNo) {
        this.ExemptionNo = exemptionNo;
    }

    public String getPosLaneCode() {
        return PosLaneCode;
    }

    public void setPosLaneCode(String posLaneCode) {
        this.PosLaneCode = posLaneCode;
    }

    public String getPurchaseOrderNo() {
        return PurchaseOrderNo;
    }

    public void setPurchaseOrderNo(String purchaseOrderNo) {
        this.PurchaseOrderNo = purchaseOrderNo;
    }

    public String getReferenceCode() {
        return ReferenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.ReferenceCode = referenceCode;
    }

    public AvaTaxOverride getAvaTaxOverride() {
        return AvaTaxOverride;
    }

    public void setAvaTaxOverride(AvaTaxOverride avaTaxOverride) {
        this.AvaTaxOverride = avaTaxOverride;
    }

    public List<AvaAddress> getAddresses() {
        return Addresses;
    }

    public void setAddresses(List<AvaAddress> addresses) {
        this.Addresses = addresses;
    }

    public List<AvaLine> getLines() {
        return Lines;
    }

    public void setLines(List<AvaLine> lines) {
        this.Lines = lines;
    }
}
