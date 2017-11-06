package com.troveup.brooklyn.sdk.print.shapeways.model;

/**
 * Created by tim on 7/23/15.
 */
public class ShapewaysCancelOrderResponse
{
    private String result;
    private String reason;
    private String orderId;

    public ShapewaysCancelOrderResponse()
    {

    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
