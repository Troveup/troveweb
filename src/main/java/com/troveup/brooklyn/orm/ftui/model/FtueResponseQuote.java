package com.troveup.brooklyn.orm.ftui.model;

import javax.jdo.annotations.*;
import java.math.BigDecimal;

/**
 * Created by tim on 6/16/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class FtueResponseQuote
{
    private Long ftueResponseQuoteId;
    private BigDecimal items;
    private BigDecimal total;
    private BigDecimal tax;
    private BigDecimal shipping;

    public FtueResponseQuote()
    {

    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getFtueResponseQuoteId() {
        return ftueResponseQuoteId;
    }

    public void setFtueResponseQuoteId(Long ftueResponseQuoteId) {
        this.ftueResponseQuoteId = ftueResponseQuoteId;
    }

    @Persistent
    @Column(name = "subtotal", allowsNull = "true", scale = 2)
    public BigDecimal getItems() {
        return items;
    }

    public void setItems(BigDecimal items) {
        this.items = items;
    }

    @Persistent
    @Column(name = "total", allowsNull = "true", scale = 2)
    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Persistent
    @Column(name = "tax", allowsNull = "true", scale = 2)
    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    @Persistent
    @Column(name = "shipping", allowsNull = "true", scale = 2)
    public BigDecimal getShipping() {
        return shipping;
    }

    public void setShipping(BigDecimal shipping) {
        this.shipping = shipping;
    }
}
