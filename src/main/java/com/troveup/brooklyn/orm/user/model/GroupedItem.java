package com.troveup.brooklyn.orm.user.model;

import com.troveup.brooklyn.orm.item.model.Item;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 8/6/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class GroupedItem
{
    private Long groupedItemId;
    private Date createdDate;
    private Item groupedItem;
    private User userGroupedItemOwner;
    private Collection collectionGroupedItemOwner;

    public GroupedItem()
    {

    }

    public GroupedItem(User userGroupedItemOwner, Item groupedItem)
    {
        this.userGroupedItemOwner = userGroupedItemOwner;
        createdDate = new Date();
        this.groupedItem = groupedItem;
    }

    public GroupedItem(Collection collectionGroupedItemOwner, Item groupedItem)
    {
        this.collectionGroupedItemOwner = collectionGroupedItemOwner;
        createdDate = new Date();
        this.groupedItem = groupedItem;
    }

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    public Long getGroupedItemId() {
        return groupedItemId;
    }

    public void setGroupedItemId(Long groupedItemId) {
        this.groupedItemId = groupedItemId;
    }

    @Persistent
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Persistent
    public Item getGroupedItem() {
        return groupedItem;
    }

    public void setGroupedItem(Item groupedItem) {
        this.groupedItem = groupedItem;
    }

    @Persistent
    public User getUserGroupedItemOwner() {
        return userGroupedItemOwner;
    }

    public void setUserGroupedItemOwner(User userGroupedItemOwner) {
        this.userGroupedItemOwner = userGroupedItemOwner;
    }

    @Persistent
    public Collection getCollectionGroupedItemOwner() {
        return collectionGroupedItemOwner;
    }

    public void setCollectionGroupedItemOwner(Collection collectionGroupedItemOwner) {
        this.collectionGroupedItemOwner = collectionGroupedItemOwner;
    }

    public static List<Item> groupedItemToItemList(List<GroupedItem> group)
    {
        List<Item> rval = new ArrayList<>();

        for (GroupedItem item : group)
            rval.add(item.getGroupedItem());

        return rval;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof GroupedItem)
        {
            return groupedItemId.equals(((GroupedItem) obj).getGroupedItemId());
        }
        else if (obj instanceof Item)
        {
            return groupedItem.getItemId().equals(((Item) obj).getItemId());
        }
        else
            return false;
    }

    public static List<String> getGroupedItemGroupedItemFetchGroupField()
    {
        List<String> rval = new ArrayList<>();

        rval.add("groupedItem");

        return rval;
    }
}
