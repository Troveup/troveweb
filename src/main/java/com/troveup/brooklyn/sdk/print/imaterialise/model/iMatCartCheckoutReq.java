package com.troveup.brooklyn.sdk.print.imaterialise.model;

/**
 * Created by tim on 5/25/15.
 */
public class iMatCartCheckoutReq
{
    private String cartId;
    private String myOrderReference;
    private Boolean directMailingAllowed;
    private String shipmentService;
    private String myInvoiceLink;
    private String myDeliveryNoteLink;
    private String remarks;

    public iMatCartCheckoutReq()
    {

    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getMyOrderReference() {
        return myOrderReference;
    }

    public void setMyOrderReference(String myOrderReference) {
        this.myOrderReference = myOrderReference;
    }

    public Boolean getDirectMailingAllowed() {
        return directMailingAllowed;
    }

    public void setDirectMailingAllowed(Boolean directMailingAllowed) {
        this.directMailingAllowed = directMailingAllowed;
    }

    public String getShipmentService() {
        return shipmentService;
    }

    public void setShipmentService(String shipmentService) {
        this.shipmentService = shipmentService;
    }

    public String getMyInvoiceLink() {
        return myInvoiceLink;
    }

    public void setMyInvoiceLink(String myInvoiceLink) {
        this.myInvoiceLink = myInvoiceLink;
    }

    public String getMyDeliveryNoteLink() {
        return myDeliveryNoteLink;
    }

    public void setMyDeliveryNoteLink(String myDeliveryNoteLink) {
        this.myDeliveryNoteLink = myDeliveryNoteLink;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
