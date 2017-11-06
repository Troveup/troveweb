package com.troveup.brooklyn.sdk.print.imaterialise.model;

/**
 * Created by tim on 5/25/15.
 */
public class iMatCartCheckoutResponse
{
    private iMatResponseError requestError;
    private iMatResponseError warning;
    private String orderNumber;
    private String orderID;
    private String requestNumber;
    private Boolean directMailingAllowed;
    private String cartID;
    private String myOrderReference;
    private String currency;
    private String remarks;
    private String myInvoiceLink;
    private String myDeliveryNoteLink;
    private Float subtotalPrice;
    private Float shipmentCost;
    private Float discount;
    private Float vatTax;
    private Float totalPrice;

    public iMatCartCheckoutResponse()
    {

    }

    public iMatResponseError getRequestError() {
        return requestError;
    }

    public void setRequestError(iMatResponseError requestError) {
        this.requestError = requestError;
    }

    public iMatResponseError getWarning() {
        return warning;
    }

    public void setWarning(iMatResponseError warning) {
        this.warning = warning;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(String requestNumber) {
        this.requestNumber = requestNumber;
    }

    public Boolean getDirectMailingAllowed() {
        return directMailingAllowed;
    }

    public void setDirectMailingAllowed(Boolean directMailingAllowed) {
        this.directMailingAllowed = directMailingAllowed;
    }

    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }

    public String getMyOrderReference() {
        return myOrderReference;
    }

    public void setMyOrderReference(String myOrderReference) {
        this.myOrderReference = myOrderReference;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMyInvoiceLink() {
        return myInvoiceLink;
    }

    public void setMyInvoiceLink(String myInvoiceLink) {
        this.myInvoiceLink = myInvoiceLink;
    }

    public String getMyDeliveryNoteLink() {
        return myDeliveryNoteLink;
    }

    public void setMyDeliveryNoteLink(String myDeliveryNoteLink) {
        this.myDeliveryNoteLink = myDeliveryNoteLink;
    }

    public Float getSubtotalPrice() {
        return subtotalPrice;
    }

    public void setSubtotalPrice(Float subtotalPrice) {
        this.subtotalPrice = subtotalPrice;
    }

    public Float getShipmentCost() {
        return shipmentCost;
    }

    public void setShipmentCost(Float shipmentCost) {
        this.shipmentCost = shipmentCost;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public Float getVatTax() {
        return vatTax;
    }

    public void setVatTax(Float vatTax) {
        this.vatTax = vatTax;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }
}
