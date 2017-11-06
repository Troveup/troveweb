package com.troveup.brooklyn.orm.ftui.model;

import com.troveup.brooklyn.orm.cart.model.PromoCode;
import com.troveup.brooklyn.orm.order.model.PrintOrder;
import com.troveup.brooklyn.orm.user.model.Address;

import javax.jdo.annotations.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 7/16/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class FtueCheckout
{
    public enum FTUE_CHECKOUT_STATUS
    {
        STARTED,
        SUBMITTING_TO_MANUFACTURER,
        COMPLETE
    }

    private Long ftueCheckoutId;
    private Address billingAddress;
    private Address shippingAddress;

    private String checkoutEmail;
    private String paymentNonce;
    private String checkoutSessionId;
    private String landingUuid;
    private String materialId;
    private String finishId;
    private String size;
    private String modelFileName;

    private PromoCode appliedCode;
    private FtuePersistedRecord recordReference;
    private FTUE_CHECKOUT_STATUS status;
    private Date creationDate;
    private Date checkoutDate;
    private BigDecimal subtotal;
    private BigDecimal shipping;
    private BigDecimal tax;
    private BigDecimal grandtotal;

    private PrintOrder manufacturerOrder;


    public FtueCheckout()
    {
        this.status = FTUE_CHECKOUT_STATUS.STARTED;
        this.creationDate = new Date();
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getFtueCheckoutId() {
        return ftueCheckoutId;
    }

    public void setFtueCheckoutId(Long ftueCheckoutId) {
        this.ftueCheckoutId = ftueCheckoutId;
    }

    @Persistent
    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    @Persistent
    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    @Persistent
    public String getCheckoutEmail() {
        return checkoutEmail;
    }

    public void setCheckoutEmail(String checkoutEmail) {
        this.checkoutEmail = checkoutEmail;
    }

    @Persistent
    @Column(length = 500)
    public String getPaymentNonce() {
        return paymentNonce;
    }

    public void setPaymentNonce(String paymentNonce) {
        this.paymentNonce = paymentNonce;
    }

    @Persistent
    public String getCheckoutSessionId() {
        return checkoutSessionId;
    }

    public void setCheckoutSessionId(String checkoutSessionId) {
        this.checkoutSessionId = checkoutSessionId;
    }

    @Persistent
    public FtuePersistedRecord getRecordReference() {
        return recordReference;
    }

    public void setRecordReference(FtuePersistedRecord recordReference) {
        this.recordReference = recordReference;
    }

    @Persistent
    public PromoCode getAppliedCode() {
        return appliedCode;
    }

    public void setAppliedCode(PromoCode appliedCode) {
        this.appliedCode = appliedCode;
    }

    @Persistent
    public String getLandingUuid() {
        return landingUuid;
    }

    public void setLandingUuid(String landingUuid) {
        this.landingUuid = landingUuid;
    }

    @Persistent
    public FTUE_CHECKOUT_STATUS getStatus() {
        return status;
    }

    public void setStatus(FTUE_CHECKOUT_STATUS status) {
        this.status = status;
    }

    @Persistent
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Persistent
    public Date getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    @Persistent
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    @Persistent
    public String getFinishId() {
        return finishId;
    }

    public void setFinishId(String finishId) {
        this.finishId = finishId;
    }

    @Persistent
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Persistent
    @Column(scale = 2)
    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    @Persistent
    @Column(scale = 2)
    public BigDecimal getShipping() {
        return shipping;
    }

    public void setShipping(BigDecimal shipping) {
        this.shipping = shipping;
    }

    @Persistent
    @Column(scale = 2)
    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    @Persistent
    @Column(scale = 2)
    public BigDecimal getGrandtotal() {
        return grandtotal;
    }

    public void setGrandtotal(BigDecimal grandtotal) {
        this.grandtotal = grandtotal;
    }

    @Persistent
    public String getModelFileName() {
        return modelFileName;
    }

    public void setModelFileName(String modelFileName) {
        this.modelFileName = modelFileName;
    }

    @Persistent
    public PrintOrder getManufacturerOrder() {
        return manufacturerOrder;
    }

    public void setManufacturerOrder(PrintOrder manufacturerOrder) {
        this.manufacturerOrder = manufacturerOrder;
    }

    public static List<String> getFtueCheckoutPromoCodeFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("billingAddress");
        rval.add("shippingAddress");
        rval.add("subtotal");
        rval.add("shipping");
        rval.add("tax");
        rval.add("grandtotal");
        rval.add("appliedCode");

        return rval;
    }

    public static List<String> getFtueCheckoutFullFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("ftueCheckoutId");
        rval.add("grandtotal");
        rval.add("tax");
        rval.add("shipping");
        rval.add("subtotal");
        rval.add("checkoutDate");
        rval.add("creationDate");
        rval.add("status");
        rval.add("recordReference");
        rval.add("appliedCode");
        rval.add("size");
        rval.add("finishId");
        rval.add("materialId");
        rval.add("landingUuid");
        rval.add("checkoutSessionId");
        rval.add("paymentNonce");
        rval.add("checkoutEmail");
        rval.add("shippingAddress");
        rval.add("billingAddress");

        return rval;
    }

}
