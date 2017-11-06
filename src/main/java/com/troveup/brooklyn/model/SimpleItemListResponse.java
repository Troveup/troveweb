package com.troveup.brooklyn.model;

import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.simpleitem.model.SimpleItem;

import java.util.List;

/**
 * Created by tim on 8/21/15.
 */
public class SimpleItemListResponse extends GenericAjaxResponse
{
    private List<SimpleItem> items;

    public SimpleItemListResponse()
    {

    }

    public List<SimpleItem> getItems() {
        return items;
    }

    public void setItems(List<SimpleItem> items) {
        this.items = items;
    }
}
