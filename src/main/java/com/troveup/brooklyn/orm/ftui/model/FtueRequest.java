package com.troveup.brooklyn.orm.ftui.model;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 6/16/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class FtueRequest
{
    Long ftueRequestId;

    List<FtueOrderItem> order_items;

    FtueRequestAddress shipping_info;


    public FtueRequest()
    {

    }

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    public Long getFtueRequestId() {
        return ftueRequestId;
    }

    public void setFtueRequestId(Long ftueRequestId) {
        this.ftueRequestId = ftueRequestId;
    }

    @Join(table = "FTUI_REQUESTITEMS")
    @Persistent
    public List<FtueOrderItem> getOrder_items() {
        return order_items;
    }

    public void setOrder_items(List<FtueOrderItem> order_items) {
        this.order_items = order_items;
    }

    @Persistent
    public FtueRequestAddress getShipping_info() {
        return shipping_info;
    }

    public void setShipping_info(FtueRequestAddress shipping_info) {
        this.shipping_info = shipping_info;
    }

    public static List<String> getFtueRequestFullFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("order_items");
        rval.add("shipping_info");

        return rval;
    }

    public static List<String> getFtueRequestShippingFetchGroupField()
    {
        List<String> rval = new ArrayList<>();

        rval.add("shipping_info");

        return rval;
    }
}
