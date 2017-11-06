package com.troveup.brooklyn.orm.cart.model;

import javax.jdo.annotations.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by tim on 5/12/15.
 */
@Cacheable(value = "false")
@PersistenceCapable
public class PaymentDetails implements Serializable
{
    private Long paymentDetailId;
    private String paymentToken;
    private Date created;
    private Date expires;
    private String paymentType;
    private String lastCCDigits;

    public PaymentDetails()
    {

    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getPaymentDetailId() {
        return paymentDetailId;
    }

    public void setPaymentDetailId(Long paymentDetailId) {
        this.paymentDetailId = paymentDetailId;
    }

    @Persistent
    @Column(name = "payment_token", allowsNull = "true", length = 5000)
    public String getPaymentToken() {
        return paymentToken;
    }

    public void setPaymentToken(String paymentToken) {
        this.paymentToken = paymentToken;
    }

    @Persistent
    @Column(name = "created", allowsNull = "true")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Persistent
    @Column(name = "expires", allowsNull = "true")
    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    @Persistent
    @Column(name = "payment_type", allowsNull = "true")
    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    @Persistent
    @Column(name = "last_cc_digits", allowsNull = "true")
    public String getLastCCDigits() {
        return lastCCDigits;
    }

    public void setLastCCDigits(String lastCCDigits) {
        this.lastCCDigits = lastCCDigits;
    }
}
