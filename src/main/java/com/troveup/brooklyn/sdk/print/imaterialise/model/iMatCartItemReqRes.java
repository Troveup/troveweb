package com.troveup.brooklyn.sdk.print.imaterialise.model;

import java.util.List;

/**
 * Created by tim on 5/25/15.
 */
public class iMatCartItemReqRes
{
    String currency;
    List<iMatCartItem> cartItems;
    iMatResponseError error;

    public iMatCartItemReqRes()
    {

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

    public iMatResponseError getError() {
        return error;
    }

    public void setError(iMatResponseError error) {
        this.error = error;
    }
}
