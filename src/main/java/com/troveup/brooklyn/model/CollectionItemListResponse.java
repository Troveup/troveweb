package com.troveup.brooklyn.model;

import com.troveup.brooklyn.orm.item.model.Item;

import java.util.List;

/**
 * Created by tim on 8/2/15.
 */
public class CollectionItemListResponse
{
    private List<Item> collectionItems;

    public CollectionItemListResponse()
    {

    }

    public List<Item> getCollectionItems() {
        return collectionItems;
    }

    public void setCollectionItems(List<Item> collectionItems) {
        this.collectionItems = collectionItems;
    }
}
