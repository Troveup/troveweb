package com.troveup.brooklyn.orm.cart.model;

/**
 * Created by tim on 11/11/15.
 */

import com.troveup.brooklyn.orm.order.model.EasyPostPostageLabel;
import com.troveup.brooklyn.sdk.print.model.OrderStatus;

import javax.jdo.annotations.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Genericized bag item that allows for a simple name/value to be added to the bag that can be relied upon to be
 * calculated and handled properly for checkout.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class GenericItem
{
    public enum ITEM_TYPE
    {
        GIFT_CARD,
        SIMPLE_ITEM,
        GENERIC
    }

    public enum GENERIC_ITEM_STATUS
    {
        PROCESSING,
        SHIPPED
    }

    private Long genericItemId;
    private List<HookAttribute> hookAttributes;
    private BigDecimal price;
    private String cartDisplayName;
    private Cart cart;

    //Datanucleus doesn't handle lists of ENUMs well.  Using a persistable class instead.
    private List<Hook> hooks;
    private String bagLineItemImage;
    private List<GenericItemDisplayAttribute> bagDisplayAttributes;
    private ITEM_TYPE itemType;
    private Long itemReferenceId;
    private EasyPostPostageLabel shippingLabel;

    //Some items are tax exempt, like gift cards.  Take this into account when calculating tax.
    private Boolean taxExempt;

    //TODO:  Bag doesn't take this into account yet
    private Integer quantity;

    private GENERIC_ITEM_STATUS itemStatus;

    //Non-persistent field for the user's orders page
    private String userFriendlyItemStatus;


    private Boolean shouldChargeShipping;

    public GenericItem()
    {
        this.quantity = 1;
        this.taxExempt = false;
        itemStatus = GENERIC_ITEM_STATUS.PROCESSING;
        shouldChargeShipping = true;
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getGenericItemId() {
        return genericItemId;
    }

    public void setGenericItemId(Long genericItemId) {
        this.genericItemId = genericItemId;
    }

    @Persistent
    public List<HookAttribute> getHookAttributes() {
        return hookAttributes;
    }

    public void setHookAttributes(List<HookAttribute> hookAttributes) {
        this.hookAttributes = hookAttributes;
    }

    @Persistent
    @Column(scale = 2)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Persistent
    public String getCartDisplayName() {
        return cartDisplayName;
    }

    public void setCartDisplayName(String cartDisplayName) {
        this.cartDisplayName = cartDisplayName;
    }

    @Persistent
    public List<Hook> getHooks() {
        return hooks;
    }

    public void setHooks(List<Hook> hooks) {
        this.hooks = hooks;
    }

    @Persistent
    public String getBagLineItemImage() {
        return bagLineItemImage;
    }

    public void setBagLineItemImage(String bagLineItemImage) {
        this.bagLineItemImage = bagLineItemImage;
    }

    @Persistent
    public List<GenericItemDisplayAttribute> getBagDisplayAttributes() {
        return bagDisplayAttributes;
    }

    public void setBagDisplayAttributes(List<GenericItemDisplayAttribute> bagDisplayAttributes) {
        this.bagDisplayAttributes = bagDisplayAttributes;
    }

    @Persistent
    public ITEM_TYPE getItemType() {
        return itemType;
    }

    public void setItemType(ITEM_TYPE itemType) {
        this.itemType = itemType;
    }

    @Persistent
    public Long getItemReferenceId() {
        return itemReferenceId;
    }

    public void setItemReferenceId(Long itemReferenceId) {
        this.itemReferenceId = itemReferenceId;
    }

    @Persistent
    public EasyPostPostageLabel getShippingLabel() { return shippingLabel; }

    public void setShippingLabel(EasyPostPostageLabel shippingLabel) { this.shippingLabel = shippingLabel; }

    @Persistent
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Persistent
    public Boolean getTaxExempt() {
        return taxExempt;
    }

    public void setTaxExempt(Boolean taxExempt) {
        this.taxExempt = taxExempt;
    }

    @Persistent
    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Persistent
    public GENERIC_ITEM_STATUS getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(GENERIC_ITEM_STATUS itemStatus) {
        this.itemStatus = itemStatus;
    }

    @Persistent
    public Boolean getShouldChargeShipping() {
        return shouldChargeShipping;
    }

    public void setShouldChargeShipping(Boolean shouldChargeShipping) {
        this.shouldChargeShipping = shouldChargeShipping;
    }

    @NotPersistent
    public String getUserFriendlyItemStatus() {
        return userFriendlyItemStatus;
    }

    public void setUserFriendlyItemStatus(String userFriendlyItemStatus) {
        this.userFriendlyItemStatus = userFriendlyItemStatus;
    }

    public static List<String> getFullGenericItemFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("hookAttributes");
        rval.add("hooks");
        rval.add("bagDisplayAttributes");
        rval.add("itemType");
        rval.add("shippingLabel");

        return rval;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GenericItem && ((GenericItem) obj).getGenericItemId().equals(genericItemId);
    }

    public static List<String> getGenericItemTypes()
    {
        List<String> rval = new ArrayList<>();

        for (ITEM_TYPE itemType : ITEM_TYPE.values())
        {
            rval.add(itemType.toString());
        }

        return rval;
    }

    public static String getUserFriendlyStatusFromEnumGenericItemStatus(GENERIC_ITEM_STATUS status)
    {
        String rval;

        switch(status)
        {
            case PROCESSING:
                rval = "Processing";
                break;
            case SHIPPED:
                rval = "Shipped";
                break;
            default:
                rval = "Processing";
                break;
        }

        return rval;
    }
}
