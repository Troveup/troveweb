package com.troveup.brooklyn.model;

import com.troveup.brooklyn.orm.item.model.Item;

import java.util.List;

/**
 * Created by tim on 8/4/15.
 */
public class ItemResponse
{
    private List<Item> itemList;

    public ItemResponse()
    {

    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}
