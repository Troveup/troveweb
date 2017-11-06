package com.troveup.brooklyn.model;

import java.util.List;

/**
 * Created by tim on 6/10/16.
 */
public class StoreExport
{
    private List<StoreExportItem> items;
    private String storeName;
    private String storeDescription;
    private String storeUrl;
    private String profileImg;

    public StoreExport()
    {

    }

    public List<StoreExportItem> getItems() {
        return items;
    }

    public void setItems(List<StoreExportItem> items) {
        this.items = items;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreDescription() {
        return storeDescription;
    }

    public void setStoreDescription(String storeDescription) {
        this.storeDescription = storeDescription;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(String storeUrl) {
        this.storeUrl = storeUrl;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
}
