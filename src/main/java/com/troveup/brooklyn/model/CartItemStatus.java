package com.troveup.brooklyn.model;

/**
 * Created by tim on 6/19/15.
 */
public class CartItemStatus
{
    private Long cartItemId;
    private String status;

    public CartItemStatus()
    {

    }

    public CartItemStatus(Long cartItemId, String status)
    {
        this.cartItemId = cartItemId;
        this.status = status;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
