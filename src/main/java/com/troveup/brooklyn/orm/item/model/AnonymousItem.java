package com.troveup.brooklyn.orm.item.model;

import com.troveup.brooklyn.orm.user.model.User;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 1/22/16.
 *
 * Used for items that aren't necessarily linked directly to a user, yet, but are in the process of doing so.
 *
 */
@PersistenceCapable
@Cacheable(value = "false")
public class AnonymousItem
{
    private Long itemId;
    private List<ItemCustomization> anonymousCustomizations;
    private Item baseItem;
    private Item parentItem;
    private String finishId;
    private String materialId;
    private Chain chain;
    private User associatedUser;
    private Date creationDate;
    private Boolean originFtue;

    public AnonymousItem()
    {
        this.originFtue = false;
        this.creationDate = new Date();
    }

    public AnonymousItem(List<ItemCustomization> customizations)
    {
        this.creationDate = new Date();
        this.anonymousCustomizations = customizations;
        this.originFtue = false;
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    @Join(table = "ANONYMOUSITEM_ITEMCUSTOMIZATION")
    @Persistent
    public List<ItemCustomization> getAnonymousCustomizations() {
        return anonymousCustomizations;
    }

    public void setAnonymousCustomizations(List<ItemCustomization> customizations) {
        this.anonymousCustomizations = customizations;
    }

    @Persistent
    public Item getBaseItem() {
        return baseItem;
    }

    public void setBaseItem(Item baseItem) {
        this.baseItem = baseItem;
    }

    @Persistent
    public User getAssociatedUser() {
        return associatedUser;
    }

    public void setAssociatedUser(User associatedUser) {
        this.associatedUser = associatedUser;
    }

    @Persistent
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Persistent
    public String getFinishId() {
        return finishId;
    }

    public void setFinishId(String finishId) {
        this.finishId = finishId;
    }

    @Persistent
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    @Persistent
    public Chain getChain() {
        return chain;
    }

    public void setChain(Chain chain) {
        this.chain = chain;
    }

    @Persistent
    public Item getParentItem() {
        return parentItem;
    }

    public void setParentItem(Item parentItem) {
        this.parentItem = parentItem;
    }

    @Persistent
    public Boolean getOriginFtue() {
        return originFtue;
    }

    public void setOriginFtue(Boolean originFtue) {
        this.originFtue = originFtue;
    }

    public static List<String> getAnonymousItemFullFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("anonymousCustomizations");
        rval.add("baseItem");
        rval.add("parentItem");
        rval.add("chain");
        rval.add("associatedUser");

        return rval;
    }
}
