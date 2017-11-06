package com.troveup.brooklyn.model;

/**
 * Created by tim on 5/30/15.
 */
public class PrepareCartResponse
{
    private String subTotal;
    private String tax;
    private String shipping;
    private String storeCredit;
    private String grandTotal;
    private String checkoutReference;
    private Boolean success;
    private Boolean giftwrapIncludedInSubtotal;
    private String message;

    public PrepareCartResponse()
    {
        this.subTotal = "0.00";
        this.tax = "0.00";
        this.shipping = "0.00";
        this.storeCredit = "0.00";
        this.grandTotal = "0.00";
    }

    public PrepareCartResponse(String subTotal, String tax, String shipping, String storeCredit, String grandTotal)
    {
        this.subTotal = subTotal;
        this.tax = tax;
        this.shipping = shipping;
        this.storeCredit = storeCredit;
        this.grandTotal = grandTotal;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getCheckoutReference() {
        return checkoutReference;
    }

    public void setCheckoutReference(String checkoutReference) {
        this.checkoutReference = checkoutReference;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStoreCredit() {
        return storeCredit;
    }

    public void setStoreCredit(String storeCredit) {
        this.storeCredit = storeCredit;
    }

    public Boolean getGiftwrapIncludedInSubtotal() {
        return giftwrapIncludedInSubtotal;
    }

    public void setGiftwrapIncludedInSubtotal(Boolean giftwrapIncludedInSubtotal) {
        this.giftwrapIncludedInSubtotal = giftwrapIncludedInSubtotal;
    }
}
