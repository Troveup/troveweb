package com.troveup.brooklyn.sdk.print.imaterialise.model;

/**
 * Created by tim on 6/12/15.
 */
public class imatCancelOrderResponse
{
    private String orderId;
    private String orderNumber;
    private Boolean isCanceled;
    private imatStatusResponse requestError;

    public imatCancelOrderResponse()
    {

    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Boolean getIsCanceled() {
        return isCanceled;
    }

    public void setIsCanceled(Boolean isCanceled) {
        this.isCanceled = isCanceled;
    }

    public imatStatusResponse getRequestError() {
        return requestError;
    }

    public void setRequestError(imatStatusResponse requestError) {
        this.requestError = requestError;
    }
}
