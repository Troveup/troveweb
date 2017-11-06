package com.troveup.brooklyn.sdk.print.voodoo.model;

import com.troveup.brooklyn.orm.ftui.model.FtueOrderItem;
import com.troveup.brooklyn.orm.ftui.model.FtuePersistedRecord;
import com.troveup.brooklyn.orm.ftui.model.FtueResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 6/18/15.
 */
public class SampleCreateOrderResponse
{
    private int order_id;
    private SampleQuote quote;
    private SampleAddress shipping;
    private List<SampleOrderItem> order_items;
    private boolean purchased;
    private String err_type;
    private String message;

    public SampleCreateOrderResponse()
    {

    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public SampleQuote getQuote() {
        return quote;
    }

    public void setQuote(SampleQuote quote) {
        this.quote = quote;
    }

    public SampleAddress getShipping() {
        return shipping;
    }

    public void setShipping(SampleAddress shipping) {
        this.shipping = shipping;
    }

    public List<SampleOrderItem> getOrder_items() {
        return order_items;
    }

    public void setOrder_items(List<SampleOrderItem> order_items) {
        this.order_items = order_items;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public String getErr_type() {
        return err_type;
    }

    public void setErr_type(String err_type) {
        this.err_type = err_type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FtuePersistedRecord toFtuePersistdRecord()
    {
        FtuePersistedRecord rval = new FtuePersistedRecord();
        rval.setSubmitted(true);
        rval.setResponseOrderId(order_id);

        FtueResponse response = new FtueResponse();

        List<FtueOrderItem> orderItems = new ArrayList<>();

        for (SampleOrderItem item : order_items)
        {
            orderItems.add(item.toFtueOrderItem());
        }

        response.setOrder_items(orderItems);
        response.setQuote(quote.toFtueResponseQuote());
        response.setQuote_id(String.valueOf(order_id));
        response.setShipping(shipping.toFtueAddress());

        rval.setResponse(response);

        return rval;
    }
}
