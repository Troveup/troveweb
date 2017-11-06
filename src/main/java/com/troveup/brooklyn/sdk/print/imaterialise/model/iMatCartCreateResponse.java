package com.troveup.brooklyn.sdk.print.imaterialise.model;

import java.util.List;

/**
 * Created by tim on 5/25/15.
 */
public class iMatCartCreateResponse
{
    String cartID;
    String cartReference;
    String currency;

    Float discount;
    Float subTotalPrice;
    Float vatTaxPercentage;

    String validUntil;
    String expectedShipmentDate;
    String shipmentDateComment;
    String requestNumber;
    iMatResponseError requestError;

    iMatServiceResponse shipmentCost;

    List<iMatCartItem> cartItems;

    iMatAddress shippingInfo;
    iMatAddress billingInfo;

    public iMatCartCreateResponse()
    {

    }

    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }

    public String getCartReference() {
        return cartReference;
    }

    public void setCartReference(String cartReference) {
        this.cartReference = cartReference;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public Float getSubTotalPrice() {
        return subTotalPrice;
    }

    public void setSubTotalPrice(Float subTotalPrice) {
        this.subTotalPrice = subTotalPrice;
    }

    public Float getVatTaxPercentage() {
        return vatTaxPercentage;
    }

    public void setVatTaxPercentage(Float vatTaxPercentage) {
        this.vatTaxPercentage = vatTaxPercentage;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

    public String getExpectedShipmentDate() {
        return expectedShipmentDate;
    }

    public void setExpectedShipmentDate(String expectedShipmentDate) {
        this.expectedShipmentDate = expectedShipmentDate;
    }

    public String getShipmentDateComment() {
        return shipmentDateComment;
    }

    public void setShipmentDateComment(String shipmentDateComment) {
        this.shipmentDateComment = shipmentDateComment;
    }

    public String getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(String requestNumber) {
        this.requestNumber = requestNumber;
    }

    public iMatResponseError getRequestError() {
        return requestError;
    }

    public void setRequestError(iMatResponseError requestError) {
        this.requestError = requestError;
    }

    public iMatServiceResponse getShipmentCost() {
        return shipmentCost;
    }

    public void setShipmentCost(iMatServiceResponse shipmentCost) {
        this.shipmentCost = shipmentCost;
    }

    public List<iMatCartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<iMatCartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public iMatAddress getShippingInfo() {
        return shippingInfo;
    }

    public void setShippingInfo(iMatAddress shippingInfo) {
        this.shippingInfo = shippingInfo;
    }

    public iMatAddress getBillingInfo() {
        return billingInfo;
    }

    public void setBillingInfo(iMatAddress billingInfo) {
        this.billingInfo = billingInfo;
    }
}
