package com.troveup.brooklyn.sdk.print.imaterialise.model;

import java.util.List;

/**
 * Created by tim on 6/10/15.
 */
public class imatStatusOrderResponse
{
    private String orderId;
    private String orderNumber;
    private String orderConfirmationLink;
    private int statusCode;
    private String statusName;
    private Boolean isOnHold;
    private String requestedDate;
    private String estimatedShipDate;
    private String shipmentDate;
    private String deliveryDate;
    private String trackingNumber;
    private String shipmentService;
    private String invoiceNumber;
    private String invoiceLink;
    private String creditNoteNumber;

    private String currency;
    private Float totalPrice;
    private String orderRemarks;

    private iMatAddress shippingInfo;
    private iMatAddress billingInfo;

    private List<imatOrderItem> orderItems;
    private imatStatusResponse orderError;


    public imatStatusOrderResponse()
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

    public String getOrderConfirmationLink() {
        return orderConfirmationLink;
    }

    public void setOrderConfirmationLink(String orderConfirmationLink) {
        this.orderConfirmationLink = orderConfirmationLink;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Boolean getIsOnHold() {
        return isOnHold;
    }

    public void setIsOnHold(Boolean isOnHold) {
        this.isOnHold = isOnHold;
    }

    public String getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(String requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getEstimatedShipDate() {
        return estimatedShipDate;
    }

    public void setEstimatedShipDate(String estimatedShipDate) {
        this.estimatedShipDate = estimatedShipDate;
    }

    public String getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(String shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getShipmentService() {
        return shipmentService;
    }

    public void setShipmentService(String shipmentService) {
        this.shipmentService = shipmentService;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceLink() {
        return invoiceLink;
    }

    public void setInvoiceLink(String invoiceLink) {
        this.invoiceLink = invoiceLink;
    }

    public String getCreditNoteNumber() {
        return creditNoteNumber;
    }

    public void setCreditNoteNumber(String creditNoteNumber) {
        this.creditNoteNumber = creditNoteNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderRemarks() {
        return orderRemarks;
    }

    public void setOrderRemarks(String orderRemarks) {
        this.orderRemarks = orderRemarks;
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

    public List<imatOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<imatOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public imatStatusResponse getOrderError() {
        return orderError;
    }

    public void setOrderError(imatStatusResponse orderError) {
        this.orderError = orderError;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof imatStatusOrderResponse)
            return ((imatStatusOrderResponse) obj).getOrderNumber().equals(orderNumber);
        else
            return false;
    }
}
