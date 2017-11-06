package com.troveup.brooklyn.sdk.print.shapeways.model;

/**
 * Created by tim on 7/23/15.
 */
public class ShapewaysCancelOrderRequest
{
    private Integer orderId;
    private String status;

    public ShapewaysCancelOrderRequest()
    {

    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
