package com.troveup.brooklyn.model;

import com.troveup.brooklyn.orm.simpleitem.model.SimpleItem;

import java.util.List;

/**
 * Created by tim on 6/4/16.
 */
public class SimpleItemResponse extends GenericAjaxResponse
{
    private List<SimpleItem> items;

    public SimpleItemResponse(Boolean isSuccess, String errorMessage)
    {
        super.setIsSuccess(isSuccess);
        super.setErrorMessage(errorMessage);
    }

    public List<SimpleItem> getItems() {
        return items;
    }

    public void setItems(List<SimpleItem> items) {
        this.items = items;
    }
}
