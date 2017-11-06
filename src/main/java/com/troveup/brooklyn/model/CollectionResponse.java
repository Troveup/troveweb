package com.troveup.brooklyn.model;

import com.troveup.brooklyn.orm.user.model.Collection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 7/30/15.
 */
public class CollectionResponse
{
    private List<Collection> collectionList;

    public CollectionResponse()
    {
        collectionList = new ArrayList<>();
    }

    public List<Collection> getCollectionList() {
        return collectionList;
    }

    public void setCollectionList(List<Collection> collectionList) {
        this.collectionList = collectionList;
    }
}
