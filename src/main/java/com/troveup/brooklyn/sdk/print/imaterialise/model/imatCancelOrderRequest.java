package com.troveup.brooklyn.sdk.print.imaterialise.model;

/**
 * Created by tim on 6/12/15.
 */
public class imatCancelOrderRequest
{
    private String orderNumber;

    public imatCancelOrderRequest()
    {

    }

    public imatCancelOrderRequest(String orderNumber)
    {
        this.orderNumber = orderNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
