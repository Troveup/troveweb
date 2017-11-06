package com.troveup.brooklyn.sdk.print.voodoo.model;

import com.troveup.brooklyn.orm.ftui.model.FtueOrderItem;
import com.troveup.brooklyn.orm.ftui.model.FtueRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 6/18/15.
 */
public class SampleOrderRequest
{
    private List<SampleOrderItem> order_items;
    private SampleAddress shipping_info;

    public SampleOrderRequest()
    {

    }

    public List<SampleOrderItem> getOrder_items() {
        return order_items;
    }

    public void setOrder_items(List<SampleOrderItem> order_items) {
        this.order_items = order_items;
    }

    public SampleAddress getShipping_info() {
        return shipping_info;
    }

    public void setShipping_info(SampleAddress shipping_info) {
        this.shipping_info = shipping_info;
    }

    public FtueRequest toFtueRequest()
    {
        FtueRequest request = new FtueRequest();

        List<FtueOrderItem> orderItems = new ArrayList<>();

        for (SampleOrderItem item : order_items)
        {
            orderItems.add(item.toFtueOrderItem());
        }

        request.setOrder_items(orderItems);
        request.setShipping_info(shipping_info.toFtueAddress());

        return request;
    }
}
