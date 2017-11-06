package com.troveup.brooklyn.sdk.print.imaterialise.model;

import java.util.List;

/**
 * Created by tim on 5/25/15.
 */
public class iMatCartCreateReq
{
    String myCartReference;
    String currency;
    List<iMatCartItem> cartItems;

    iMatAddress shippingInfo;
    iMatAddress billingInfo;

    public iMatCartCreateReq()
    {

    }

    public String getMyCartReference() {
        return myCartReference;
    }

    public void setMyCartReference(String myCartReference) {
        this.myCartReference = myCartReference;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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
