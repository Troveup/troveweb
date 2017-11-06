package com.troveup.brooklyn.sdk.print.model;

/**
 * Created by tim on 5/22/15.
 */
public abstract class OrderStatus
{
    public enum STATUS_CODE
    {
        CREATED,
        CANCELLED,
        INVALID,
        ORDERED,
        PROCESSING,
        IN_PRODUCTION,
        READY_TO_SHIP,
        SHIPPED,
        DELIVERED
    }

    private STATUS_CODE orderStatusCode;
    Float orderTotal;
    Float shippingTotal;

    public OrderStatus()
    {

    }

    public STATUS_CODE getOrderStatusCode() {
        return orderStatusCode;
    }

    public void setOrderStatusCode(STATUS_CODE orderStatusCode) {
        this.orderStatusCode = orderStatusCode;
    }

    public Float getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Float orderTotal) {
        this.orderTotal = orderTotal;
    }

    public Float getShippingTotal() {
        return shippingTotal;
    }

    public void setShippingTotal(Float shippingTotal) {
        this.shippingTotal = shippingTotal;
    }
}
