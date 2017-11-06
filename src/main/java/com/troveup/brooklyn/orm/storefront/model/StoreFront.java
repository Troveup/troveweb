package com.troveup.brooklyn.orm.storefront.model;

import com.troveup.brooklyn.model.StoreExport;
import com.troveup.brooklyn.orm.simpleitem.model.SimpleItem;
import com.troveup.brooklyn.orm.user.model.User;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 6/10/16.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class StoreFront
{
    private Long storeId;
    private User storeOwner;
    private String storeName;
    private String storeDescription;
    private String storeShortUrl;
    private String profileImgUrl;
    private Boolean active;
    private List<SimpleItem> storeFrontItems;

    public StoreFront() {
        active = true;
    }

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    @Persistent
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    @Persistent
    @Column(length = 2000)
    public String getStoreDescription() {
        return storeDescription;
    }

    public void setStoreDescription(String storeDescription) {
        this.storeDescription = storeDescription;
    }

    @Persistent
    public String getStoreShortUrl() {
        return storeShortUrl;
    }

    public void setStoreShortUrl(String storeShortUrl) {
        this.storeShortUrl = storeShortUrl;
    }

    @Persistent
    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    @Persistent
    @Join
    public List<SimpleItem> getStoreFrontItems() {
        return storeFrontItems;
    }

    public void setStoreFrontItems(List<SimpleItem> storeFrontItems) {
        this.storeFrontItems = storeFrontItems;
    }

    @Persistent
    public User getStoreOwner() {
        return storeOwner;
    }

    public void setStoreOwner(User storeOwner) {
        this.storeOwner = storeOwner;
    }

    @Persistent
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public static List<String> getAllStoreFrontFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("storeOwner");
        rval.add("storeFrontItems");

        return rval;
    }
}
