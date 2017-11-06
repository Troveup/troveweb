package com.troveup.brooklyn.orm.ftui.model;

import javax.jdo.annotations.*;
import java.util.List;

/**
 * Created by tim on 6/16/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class FtueResponse
{
    private Long ftueResponseId;
    private String quote_id;
    private FtueResponseQuote quote;
    private FtueRequestAddress shipping;
    private List<FtueOrderItem> order_items;

    public FtueResponse()
    {

    }

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    public Long getFtueResponseId() {
        return ftueResponseId;
    }

    public void setFtueResponseId(Long ftueResponseId) {
        this.ftueResponseId = ftueResponseId;
    }

    @Persistent
    @Column(name = "quote_id", allowsNull = "true")
    public String getQuote_id() {
        return quote_id;
    }

    public void setQuote_id(String quote_id) {
        this.quote_id = quote_id;
    }

    @Persistent
    @Column(name = "quote", allowsNull = "true")
    public FtueResponseQuote getQuote() {
        return quote;
    }

    public void setQuote(FtueResponseQuote quote) {
        this.quote = quote;
    }

    @Persistent
    @Column(name = "shipping_address", allowsNull = "true")
    public FtueRequestAddress getShipping() {
        return shipping;
    }

    public void setShipping(FtueRequestAddress shipping) {
        this.shipping = shipping;
    }

    @Join(table = "FTUI_RESPONSEITEMS")
    @Persistent
    public List<FtueOrderItem> getOrder_items() {
        return order_items;
    }

    public void setOrder_items(List<FtueOrderItem> order_items) {
        this.order_items = order_items;
    }
}
