package com.troveup.brooklyn.sdk.email.mandrill.model;

/**
 * Created by tim on 9/17/15.
 */
public class OrderConfirmationItem
{
    private String itemName;
    private String itemMaterial;
    private String itemSize;
    private String itemPrice;
    private String itemImageUrl;
    private String engraving;
    private Boolean isPrototype;
    private Boolean isGeneric;

    public OrderConfirmationItem(String itemName, String itemMaterial, String itemPrice, String itemSize, Boolean isPrototype,
                                 String engraving)
    {
        this.itemName = itemName;
        this.itemMaterial = itemMaterial;
        this.itemSize = itemSize;
        this.itemPrice = itemPrice;
        this.isPrototype = isPrototype;
        this.engraving = engraving;
        this.isGeneric = false;
    }

    public OrderConfirmationItem(String itemName, String itemPrice)
    {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.isGeneric = true;
    }

    public OrderConfirmationItem(String itemName, String itemPrice, String itemImageUrl)
    {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemImageUrl = itemImageUrl;
        this.isGeneric = true;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemMaterial() {
        return itemMaterial;
    }

    public void setItemMaterial(String itemMaterial) {
        this.itemMaterial = itemMaterial;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public Boolean getIsPrototype() {
        return isPrototype;
    }

    public void setIsPrototype(Boolean isPrototype) {
        this.isPrototype = isPrototype;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getEngraving() {
        return engraving;
    }

    public void setEngraving(String engraving) {
        this.engraving = engraving;
    }

    public Boolean getIsGeneric() {
        return isGeneric;
    }

    public void setIsGeneric(Boolean isGeneric) {
        this.isGeneric = isGeneric;
    }

    public String getItemImageUrl() {
        return itemImageUrl;
    }

    public void setItemImageUrl(String itemImageUrl) {
        this.itemImageUrl = itemImageUrl;
    }
}
