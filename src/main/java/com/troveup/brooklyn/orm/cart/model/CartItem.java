package com.troveup.brooklyn.orm.cart.model;

import com.troveup.brooklyn.orm.item.model.Chain;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.item.model.ItemAttribute;
import com.troveup.brooklyn.orm.item.model.ItemCustomization;
import com.troveup.brooklyn.orm.order.model.*;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.sdk.meshexporter.forge.model.CustomizerOperator;
import com.troveup.brooklyn.sdk.print.imaterialise.model.iMatCartItem;

import javax.jdo.annotations.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by tim on 5/11/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class CartItem implements Serializable {
    public enum CART_ITEM_STATUS {
        ON_HOLD,
        ON_HOLD_PROTOTYPE,
        PROTOTYPE_READY_FOR_UPLOAD,
        PROTOTYPE_ORDERED,
        PROTOTYPE_SHIPPED_TO_USER,
        PENDING_USER_DECISION,
        READY_FOR_UPLOAD,
        ORDERED,
        SHIPPED_TO_USER,
        COMPLETE,
        CANCELLED
    }

    public enum PROTOTYPE_EMAIL_STATE {
        NONE_SENT,
        DAY_TWO_REMINDER_SENT,
        DAY_SEVEN_REMINDER_SENT,
        DAY_FOURTEEN_REMINDER_SENT,
        DAY_TWENTY_ONE_REMINDER_SENT,
        DAY_TWENTY_EIGHT_REMINDER_SENT,
        DAY_THIRTY_CANCELLATION_EMAIL_SENT
    }

    private Long cartItemId;
    private Item cartItemReference;
    private int quantity;
    private Integer customizationIteration;
    private Integer remainingCustomizations;
    private String estimatedPrice;
    private BigDecimal actualPrice;
    private Cart cart;
    private List<CartItemAttribute> attributes;
    private String materialName;
    private String finishName;
    private String materialId;
    private String finishId;
    private PromoCode appliedPromoCode;
    private List<PrintOrder> manufacturerOrders;
    private List<ItemCustomization> customizations;
    private Boolean prototypeRequested;
    private Integer maxCustomizations;
    private Boolean canCustomizeAgain;
    private Date lastStatusChangeDate;
    private Date lastOutstandingPrototypeCheckDate;
    private PROTOTYPE_EMAIL_STATE prototypeEmailState;
    private Boolean cancelable;
    private String size;
    private String engraveText;
    private Chain chain;
    private EasyPostPostageLabel shippingLabel;

    //Used locally within actions to tell whether or not the cart item is prototypeable.  For example,
    //Items that are ordered in the prototype material cannot be prototyped on top of that prototype.
    private Boolean canPrototype;

    //Name of the item at the time of purchase
    private String frozenItemName;

    private CART_ITEM_STATUS cartItemStatus;
    private String userFriendlyStatus;

    public CartItem() {
        customizationIteration = 1;
        maxCustomizations = 2;
        lastStatusChangeDate = new Date();
        prototypeEmailState = PROTOTYPE_EMAIL_STATE.NONE_SENT;
    }

    public CartItem(Long cartItemId) {
        this.cartItemId = cartItemId;
        lastStatusChangeDate = new Date();
        prototypeEmailState = PROTOTYPE_EMAIL_STATE.NONE_SENT;
    }

    //Nonpersistent for Orders page status data transfer
    private TitleAndDescription ordersPageStatusTitleAndDescription;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    @Join(table = "CART_ITEM_REFS")
    @Persistent
    public Item getCartItemReference() {
        return cartItemReference;
    }

    public void setCartItemReference(Item cartItemReference) {
        this.cartItemReference = cartItemReference;
    }

    @Persistent
    @Column(name = "quantity")
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Persistent
    @Column(name = "estimated_price")
    public String getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(String estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    @Persistent
    @Column(name = "actual_price", scale = 4)
    public BigDecimal getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(BigDecimal actualPrice) {
        this.actualPrice = actualPrice;
    }

    @Persistent(mappedBy = "attributeOwner")
    public List<CartItemAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<CartItemAttribute> attributes) {
        this.attributes = attributes;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Persistent
    @Column(name = "material_name")
    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    @Persistent
    @Column(name = "finish_name")
    public String getFinishName() {
        return finishName;
    }

    public void setFinishName(String finishName) {
        this.finishName = finishName;
    }

    public Boolean containsAttribute(CartItemAttribute.ATTRIBUTE_TYPE attributeType, String attributeValue) {
        Boolean rval = false;
        CartItemAttribute attribute = CartItemAttribute.getCartItemAttribute(attributeType.toString(), attributes);

        if (attribute != null && attribute.getAttributeValue().equals(attributeValue))
            rval = true;

        return rval;
    }

    @Persistent
    @Column(name = "applied_promo_code")
    public PromoCode getAppliedPromoCode() {
        return appliedPromoCode;
    }


    public void setAppliedPromoCode(PromoCode appliedPromoCode) {
        this.appliedPromoCode = appliedPromoCode;
    }

    @Persistent
    @Column(name = "material_id")
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    @Persistent
    @Column(name = "finish_id")
    public String getFinishId() {
        return finishId;
    }

    public void setFinishId(String finishId) {
        this.finishId = finishId;
    }

    @Persistent
    public List<ItemCustomization> getCustomizations() {
        return customizations;
    }

    public void setCustomizations(List<ItemCustomization> customizations) {
        this.customizations = customizations;
    }

    @Persistent
    public Integer getCustomizationIteration() {
        return customizationIteration;
    }

    public void setCustomizationIteration(Integer customizationIteration) {
        this.customizationIteration = customizationIteration;
    }

    @Persistent
    public List<PrintOrder> getManufacturerOrders() {
        return manufacturerOrders;
    }

    public void setManufacturerOrders(List<PrintOrder> manufacturerOrders) {
        this.manufacturerOrders = manufacturerOrders;
    }

    @Persistent
    public CART_ITEM_STATUS getCartItemStatus() {
        return cartItemStatus;
    }

    public void setCartItemStatus(CART_ITEM_STATUS cartItemStatus) {
        this.cartItemStatus = cartItemStatus;
    }

    @Persistent
    public Boolean getPrototypeRequested() {
        return prototypeRequested;
    }

    public void setPrototypeRequested(Boolean prototypeRequested) {
        this.prototypeRequested = prototypeRequested;
    }

    @Persistent
    public Integer getMaxCustomizations() {
        return maxCustomizations;
    }

    public void setMaxCustomizations(Integer maxCustomizations) {
        this.maxCustomizations = maxCustomizations;
    }

    @Persistent
    public String getFrozenItemName() {
        return frozenItemName;
    }

    public void setFrozenItemName(String frozenItemName) {
        this.frozenItemName = frozenItemName;
    }

    @Persistent
    public Date getLastStatusChangeDate() {
        return lastStatusChangeDate;
    }

    public void setLastStatusChangeDate(Date lastStatusChangeDate) {
        this.lastStatusChangeDate = lastStatusChangeDate;
    }

    @Persistent
    public Date getLastOutstandingPrototypeCheckDate() {
        return lastOutstandingPrototypeCheckDate;
    }

    public void setLastOutstandingPrototypeCheckDate(Date lastOutstandingPrototypeCheckDate) {
        this.lastOutstandingPrototypeCheckDate = lastOutstandingPrototypeCheckDate;
    }

    @Persistent
    public PROTOTYPE_EMAIL_STATE getPrototypeEmailState() {
        return prototypeEmailState;
    }

    public void setPrototypeEmailState(PROTOTYPE_EMAIL_STATE prototypeEmailState) {
        this.prototypeEmailState = prototypeEmailState;
    }

    @Persistent
    public String getEngraveText() {
        return engraveText;
    }

    public void setEngraveText(String engraveText) {
        this.engraveText = engraveText;
    }

    @Persistent
    public Chain getChain() {
        return chain;
    }

    public void setChain(Chain chain) {
        this.chain = chain;
    }

    @Persistent
    public EasyPostPostageLabel getShippingLabel() { return shippingLabel; }

    public void setShippingLabel(EasyPostPostageLabel shippingLabel) { this.shippingLabel = shippingLabel; }

    @NotPersistent
    public Boolean getCancelable() {
        return cancelable;
    }

    public void setCancelable(Boolean cancelable) {
        this.cancelable = cancelable;
    }

    @NotPersistent
    public Boolean getCanCustomizeAgain() {
        return canCustomizeAgain;
    }

    public void setCanCustomizeAgain(Boolean canCustomizeAgain) {
        this.canCustomizeAgain = canCustomizeAgain;
    }

    //Convenience field for storing the item's size, as it is stored in the ItemCustomizations
    @NotPersistent
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @NotPersistent
    public Integer getRemainingCustomizations() {
        return remainingCustomizations;
    }

    public void setRemainingCustomizations(Integer remainingCustomizations) {
        this.remainingCustomizations = remainingCustomizations;
    }

    @NotPersistent
    public String getUserFriendlyStatus() {
        return userFriendlyStatus;
    }

    public void setUserFriendlyStatus(String userFriendlyStatus) {
        this.userFriendlyStatus = userFriendlyStatus;
    }

    @NotPersistent
    public Boolean getCanPrototype() {
        return canPrototype;
    }

    public void setCanPrototype(Boolean canPrototype) {
        this.canPrototype = canPrototype;
    }

    @NotPersistent
    public TitleAndDescription getOrdersPageStatusTitleAndDescription() {
        return this.ordersPageStatusTitleAndDescription;
    }

    public void setOrdersPageStatusTitleAndDescription(TitleAndDescription ordersPageStatusTitleAndDescription) {
        this.ordersPageStatusTitleAndDescription = ordersPageStatusTitleAndDescription;
    }

    public List<ItemCustomization> getItemCustomizationByIteration(Integer iteration) {
        List<ItemCustomization> rval = new ArrayList<>();

        if (getCustomizations() != null) {
            for (ItemCustomization customization : getCustomizations()) {
                if (customization.getSetNumber().equals(iteration))
                    rval.add(customization);
            }
        }

        return rval;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CartItem)
            return ((CartItem) obj).cartItemId.equals(this.cartItemId);
        else
            return false;
    }

    public void convertCartItemStatusToUserFriendlyStatus() {
        switch (cartItemStatus) {
            case ON_HOLD:
            case ON_HOLD_PROTOTYPE:
            case PROTOTYPE_READY_FOR_UPLOAD:
            case READY_FOR_UPLOAD:
                userFriendlyStatus = "Order Confirmed";
                break;
            case PROTOTYPE_ORDERED:
            case ORDERED:
                userFriendlyStatus = "Order Processing";
                break;
            case PROTOTYPE_SHIPPED_TO_USER:
            case SHIPPED_TO_USER:
                userFriendlyStatus = "Order Shipped";
                break;
            case PENDING_USER_DECISION:
                userFriendlyStatus = "Awaiting User Decision";
                break;
            case COMPLETE:
                userFriendlyStatus = "Delivered";
                break;
            case CANCELLED:
                userFriendlyStatus = "Cancelled";
                break;
            default:
                userFriendlyStatus = "Order Pending";
        }
    }

    public static List<CART_ITEM_STATUS> getStatusesThatCannotBeCancelled() {
        List<CART_ITEM_STATUS> rval = new ArrayList<>();

        rval.add(CART_ITEM_STATUS.ORDERED);
        rval.add(CART_ITEM_STATUS.SHIPPED_TO_USER);
        rval.add(CART_ITEM_STATUS.COMPLETE);
        rval.add(CART_ITEM_STATUS.CANCELLED);

        return rval;
    }

    public List<CustomizerOperator> getOperatorsFromCustomizations(Integer iteration) {
        List<CustomizerOperator> rval = new ArrayList<>();
        if (getCustomizations() != null && getCustomizations().size() > 0) {
            for (ItemCustomization customization : getCustomizations()) {
                if (customization.getId() != null && customization.getSetNumber().equals(iteration))
                    rval.add(new CustomizerOperator(customization.getId(), customization.getValue()));
            }
        }
        return rval;
    }

    public List<String> getActiveMeshes(Integer iteration) {
        List<String> rval = new ArrayList<>();
        if (getCustomizations() != null && getCustomizations().size() > 0) {
            for (ItemCustomization customization : getCustomizations()) {
                if (customization.getVisibleMesh() != null && customization.getSetNumber().equals(iteration))
                    rval.add(customization.getVisibleMesh());
            }
        }
        return rval;
    }

    //Iterates through stored customizations to retrieve the size
    public String getSize(Integer iteration) {
        if (getCustomizations() != null && getCustomizations().size() > 0) {
            for (ItemCustomization customization : getCustomizations()) {
                if (customization.getSize() != null && customization.getSetNumber().equals(iteration))
                    return customization.getSize();
            }
        }

        return null;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static Builder getBuilder(Item item, int quantity, float estimatedPrice, String materialId, String finishId) {
        return new Builder(item, quantity, estimatedPrice, materialId, finishId);
    }

    public static class Builder {
        private Item item;
        private int quantity;
        private String estimatedPrice;
        private String materialId;
        private String finishId;
        private CART_ITEM_STATUS cartItemStatus;

        public Builder() {
            quantity = 1;
            estimatedPrice = null;
        }

        public Builder(Item item, int quantity, float estimatedPrice, String materialId, String finishId) {
            this.item = item;
            this.quantity = quantity;
            //TODO:  Remove this hard coded currencyUnit specifier when the site is internationalized
            this.estimatedPrice = Float.toString(estimatedPrice);
            this.materialId = materialId;
            this.finishId = finishId;
        }

        public Item getItem() {
            return item;
        }

        public void setItem(Item item) {
            this.item = item;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getEstimatedPrice() {
            return estimatedPrice;
        }

        public void setEstimatedPrice(String estimatedPrice) {
            this.estimatedPrice = estimatedPrice;
        }

        public String getMaterialId() {
            return materialId;
        }

        public void setMaterialId(String materialId) {
            this.materialId = materialId;
        }

        public String getFinishId() {
            return finishId;
        }

        public void setFinishId(String finishId) {
            this.finishId = finishId;
        }

        public CART_ITEM_STATUS getCartItemStatus() {
            return cartItemStatus;
        }

        public void setStatus(CART_ITEM_STATUS status) {
            this.cartItemStatus = cartItemStatus;
        }

        public CartItem build() {
            CartItem cartItem = new CartItem();
            cartItem.cartItemReference = this.item;
            cartItem.quantity = this.quantity;
            cartItem.estimatedPrice = estimatedPrice;
            cartItem.setMaterialId(materialId);
            cartItem.setFinishId(finishId);
            cartItem.setCartItemStatus(cartItemStatus);

            return cartItem;
        }
    }

    public static List<String> getCartItemQuickFetchGroupFields() {
        List<String> rval = new ArrayList<>();

        rval.add("estimatedPrice");
        rval.add("actualPrice");
        rval.add("attributes");

        return rval;
    }

    public static List<String> getCartItemMediumFetchGroupFields() {
        List<String> rval = new ArrayList<>();

        rval.add("cartItemReference");
        rval.add("estimatedPrice");
        rval.add("actualPrice");
        rval.add("attributes");
        rval.add("materialId");
        rval.add("finishId");
        rval.add("customizations");

        return rval;
    }

    public static List<String> getCartItemFullFetchGroupFields() {
        List<String> rval = new ArrayList<>();

        rval.add("cartItemReference");
        rval.add("estimatedPrice");
        rval.add("actualPrice");
        rval.add("attributes");
        rval.add("chain");
        rval.add("materialId");
        rval.add("finishId");
        rval.add("customizations");
        rval.add("appliedPromoCode");
        rval.add("manufacturerOrders");
        rval.add("shippingLabel");

        return rval;
    }

    public static List<String> getCartItemFetchGroupFields() {
        List<String> rval = new ArrayList<>();

        rval.add("cartItemReference");
        rval.add("estimatedPrice");
        rval.add("actualPrice");
        rval.add("cart");
        rval.add("attributes");
        rval.add("customizations");
        rval.add("chain");

        return rval;
    }

    public static List<String> getCartItemUploadFetchGroupFields() {
        List<String> rval = new ArrayList<>();

        rval.add("customizations");
        rval.add("cartItemStatus");
        rval.add("customizationIteration");
        rval.add("cartItemReference");

        return rval;
    }

    public static List<String> getCartPromoFetchGroupFields() {
        List<String> rval = new ArrayList<>();

        rval.add("actualPrice");
        rval.add("appliedPromoCode");

        return rval;
    }

    public static List<String> getCartItemOrderSubmitFetchGroupFields() {
        List<String> rval = new ArrayList<>();

        rval.add("cartItemReference");
        rval.add("cartItemStatus");

        return rval;
    }

    public static List<String> getCartItemStatusFetchGroupFields() {
        List<String> rval = new ArrayList<>();

        rval.add("cartItemStatus");

        return rval;
    }

    public static List<String> getCartItemOrderSettlementFetchGroupFields() {
        List<String> rval = new ArrayList<>();

        rval.add("chain");

        return rval;
    }

    //TODO:  Make this more efficient, because it's searching with a for loop every time (use a map?)
    public iMatCartItem toImatCartItemLegacyWorkflow() {
        iMatCartItem rval = new iMatCartItem();

        CartItemAttribute itemSeek = CartItemAttribute.getCartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_CART_ITEM_REFERENCE.toString(), attributes);

        if (itemSeek != null)
            rval.setMyCartItemReference(itemSeek.getAttributeValue());

        itemSeek = CartItemAttribute.getCartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_MODELID.toString(), attributes);

        if (itemSeek != null)
            rval.setModelId(itemSeek.getAttributeValue());

        itemSeek = CartItemAttribute.getCartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_FILESCALEFACTOR.toString(), attributes);

        if (itemSeek != null)
            rval.setFileScaleFactor(Float.parseFloat(itemSeek.getAttributeValue()));

        itemSeek = CartItemAttribute.getCartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_UNITS.toString(), attributes);

        if (itemSeek != null)
            rval.setFileUnits(itemSeek.getAttributeValue());

        //THINGS THAT ARE IN THE ITEM, NOT THE CART ITEM
        ItemAttribute itemAttributeSeek = ItemAttribute.getItemAttribute(
                ItemAttribute.ATTRIBUTE_TYPE.IMAT_MATERIALID, cartItemReference.getItemAttributes());

        if (itemAttributeSeek != null)
            rval.setMaterialId(itemAttributeSeek.getAttributeValue());

        itemAttributeSeek = ItemAttribute.getItemAttribute(
                ItemAttribute.ATTRIBUTE_TYPE.IMAT_FINISHID, cartItemReference.getItemAttributes());

        if (itemAttributeSeek != null)
            rval.setFinishId(itemAttributeSeek.getAttributeValue());

        itemAttributeSeek = ItemAttribute.getItemAttribute(
                ItemAttribute.ATTRIBUTE_TYPE.IMAT_XDIM, cartItemReference.getItemAttributes());

        if (itemAttributeSeek != null)
            rval.setxDimMm(Float.parseFloat(itemAttributeSeek.getAttributeValue()));

        itemAttributeSeek = ItemAttribute.getItemAttribute(
                ItemAttribute.ATTRIBUTE_TYPE.IMAT_YDIM, cartItemReference.getItemAttributes());

        if (itemAttributeSeek != null)
            rval.setyDimMm(Float.parseFloat(itemAttributeSeek.getAttributeValue()));

        itemAttributeSeek = ItemAttribute.getItemAttribute(
                ItemAttribute.ATTRIBUTE_TYPE.IMAT_ZDIM, cartItemReference.getItemAttributes());

        if (itemAttributeSeek != null)
            rval.setzDimMm(Float.parseFloat(itemAttributeSeek.getAttributeValue()));

        itemAttributeSeek = ItemAttribute.getItemAttribute(
                ItemAttribute.ATTRIBUTE_TYPE.IMAT_VOLUME, cartItemReference.getItemAttributes());

        if (itemAttributeSeek != null)
            rval.setVolumeCm3(Float.parseFloat(itemAttributeSeek.getAttributeValue()));

        itemAttributeSeek = ItemAttribute.getItemAttribute(
                ItemAttribute.ATTRIBUTE_TYPE.IMAT_SURFACE, cartItemReference.getItemAttributes());

        if (itemAttributeSeek != null)
            rval.setSurfaceCm2(Float.parseFloat(itemAttributeSeek.getAttributeValue()));

        itemAttributeSeek = ItemAttribute.getItemAttribute(
                ItemAttribute.ATTRIBUTE_TYPE.IMAT_VALIDUNTIL, cartItemReference.getItemAttributes());

        if (itemAttributeSeek != null)
            rval.setValidUntil(itemAttributeSeek.getAttributeValue());

        //END OF THINGS THAT ARE IN THE ITEM, NOT THE CART ITEM

        itemSeek = CartItemAttribute.getCartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_APIPRICE.toString(), attributes);

        if (itemSeek != null)
            rval.setiMatApiPrice(Float.parseFloat(itemSeek.getAttributeValue()));

        itemSeek = CartItemAttribute.getCartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_MYSALESPRICE.toString(), attributes);

        if (itemSeek != null)
            rval.setMySalesPrice(Float.parseFloat(itemSeek.getAttributeValue()));

        itemSeek = CartItemAttribute.getCartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_PRICE.toString(), attributes);

        if (itemSeek != null)
            rval.setiMatApiPrice(Float.parseFloat(itemSeek.getAttributeValue()));

        itemSeek = CartItemAttribute.getCartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_VALIDUNTIL.toString(), attributes);

        if (itemSeek != null)
            rval.setValidUntil(itemSeek.getAttributeValue());

        itemSeek = CartItemAttribute.getCartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_CARTITEMID.toString(), attributes);

        if (itemSeek != null)
            rval.setCartItemID(itemSeek.getAttributeValue());

        ItemAttribute cartItemModelId = ItemAttribute.getItemAttribute(ItemAttribute.ATTRIBUTE_TYPE.IMATERIALISE_MODEL_ID, cartItemReference.getItemAttributes());
        if (cartItemModelId != null)
            rval.setModelId(cartItemModelId.getAttributeValue());

        return rval;
    }

    public iMatCartItem toImatCartItemWorkflow() {
        iMatCartItem rval = new iMatCartItem();

        CartItemAttribute itemSeek = CartItemAttribute.getCartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_CART_ITEM_REFERENCE.toString(), attributes);

        if (itemSeek != null)
            rval.setMyCartItemReference(itemSeek.getAttributeValue());

        itemSeek = CartItemAttribute.getCartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_MODELID.toString(), attributes);

        if (itemSeek != null)
            rval.setModelId(itemSeek.getAttributeValue());

        itemSeek = CartItemAttribute.getCartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_FILESCALEFACTOR.toString(), attributes);

        if (itemSeek != null)
            rval.setFileScaleFactor(Float.parseFloat(itemSeek.getAttributeValue()));

        itemSeek = CartItemAttribute.getCartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_UNITS.toString(), attributes);

        if (itemSeek != null)
            rval.setFileUnits(itemSeek.getAttributeValue());

        /*ItemAttribute itemAttributeSeek = ItemAttribute.getItemAttribute(
                ItemAttribute.ATTRIBUTE_TYPE.IMAT_MATERIALID, cartItemReference.getItemAttributes());*/

        rval.setMaterialId(materialId);

        /*itemAttributeSeek = ItemAttribute.getItemAttribute(
                ItemAttribute.ATTRIBUTE_TYPE.IMAT_FINISHID, cartItemReference.getItemAttributes());*/

        rval.setFinishId(finishId);

        itemSeek = CartItemAttribute.getCartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_XDIM.toString(), attributes);

        if (itemSeek != null)
            rval.setxDimMm(Float.parseFloat(itemSeek.getAttributeValue()));

        itemSeek = CartItemAttribute.getCartItemAttribute(
                ItemAttribute.ATTRIBUTE_TYPE.IMAT_YDIM.toString(), attributes);

        if (itemSeek != null)
            rval.setyDimMm(Float.parseFloat(itemSeek.getAttributeValue()));

        itemSeek = CartItemAttribute.getCartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_ZDIM.toString(), attributes);

        if (itemSeek != null)
            rval.setzDimMm(Float.parseFloat(itemSeek.getAttributeValue()));

        itemSeek = CartItemAttribute.getCartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_VOLUME.toString(), attributes);

        if (itemSeek != null)
            rval.setVolumeCm3(Float.parseFloat(itemSeek.getAttributeValue()));

        itemSeek = CartItemAttribute.getCartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_SURFACE.toString(), attributes);

        if (itemSeek != null)
            rval.setSurfaceCm2(Float.parseFloat(itemSeek.getAttributeValue()));

        itemSeek = CartItemAttribute.getCartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_VALIDUNTIL.toString(), attributes);

        if (itemSeek != null)
            rval.setValidUntil(itemSeek.getAttributeValue());

        itemSeek = CartItemAttribute.getCartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_APIPRICE.toString(), attributes);

        if (itemSeek != null)
            rval.setiMatApiPrice(Float.parseFloat(itemSeek.getAttributeValue()));

        itemSeek = CartItemAttribute.getCartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_MYSALESPRICE.toString(), attributes);

        if (itemSeek != null)
            rval.setMySalesPrice(Float.parseFloat(itemSeek.getAttributeValue()));

        itemSeek = CartItemAttribute.getCartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_PRICE.toString(), attributes);

        if (itemSeek != null)
            rval.setiMatApiPrice(Float.parseFloat(itemSeek.getAttributeValue()));

        itemSeek = CartItemAttribute.getCartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_VALIDUNTIL.toString(), attributes);

        if (itemSeek != null)
            rval.setValidUntil(itemSeek.getAttributeValue());

        itemSeek = CartItemAttribute.getCartItemAttribute(
                CartItemAttribute.ATTRIBUTE_TYPE.IMAT_CARTITEMID.toString(), attributes);

        if (itemSeek != null)
            rval.setCartItemID(itemSeek.getAttributeValue());

        ItemAttribute cartItemModelId = ItemAttribute.getItemAttribute(ItemAttribute.ATTRIBUTE_TYPE.IMATERIALISE_MODEL_ID, cartItemReference.getItemAttributes());
        if (cartItemModelId != null)
            rval.setModelId(cartItemModelId.getAttributeValue());

        return rval;
    }

    public static List<String> getCartItemStatusList() {
        List<String> rval = new ArrayList<>();

        for (CART_ITEM_STATUS status : CART_ITEM_STATUS.values()) {
            rval.add(status.toString());
        }

        return rval;
    }

    public static Map<String, TitleAndDescription> getOrdersPageStatusTitleAndDescriptionMap() {
        Map<String, TitleAndDescription> rval = new HashMap<>();

        for (CartItem.CART_ITEM_STATUS status : CartItem.CART_ITEM_STATUS.values()) {
            switch (status.name()) {
                case "ON_HOLD":
                case "ON_HOLD_PROTOTYPE":
                    rval.put(status.name(), new TitleAndDescription("ORDER PROCESSING", "We're sending your order off to be made!"));
                    break;
                case "PROTOTYPE_READY_FOR_UPLOAD":
                case "PROTOTYPE_ORDERED":
                case "PROTOTYPE_SHIPPED_TO_USER":
                case "PENDING_USER_DECISION":
                    rval.put(status.name(), new TitleAndDescription("SENDING TRY-ON MODEL", "After you receive your try-on model, you can confirm or modify your order."));
                    break;
                case "READY_FOR_UPLOAD":
                case "ORDERED":
                case "SHIPPED_TO_USER":
                    rval.put(status.name(), new TitleAndDescription("FINAL JEWELRY BEING PREPARED", "Sit tight, this item is being prepared and shipped as we speak!"));
                    break;
                case "COMPLETE":
                    rval.put(status.name(), new TitleAndDescription("ORDER COMPLETE", "Thank you for choosing Trove!"));
                    break;
                case "CANCELLED":
                    rval.put(status.name(), new TitleAndDescription("ORDER CANCELLED", "Thank you for choosing Trove!"));
                    break;
                default:
                    rval.put(status.name(), new TitleAndDescription("ORDER COMPLETE", "Thank you for choosing Trove!"));
                    break;
            }
        }

        return rval;
    }

    public static TitleAndDescription getTitleAndDescriptionFromOrderStatus(CartItem.CART_ITEM_STATUS status) {
        Map<String, TitleAndDescription> titlesAndDescriptions = getOrdersPageStatusTitleAndDescriptionMap();

        return titlesAndDescriptions.get(status.name());
    }

    public static class TitleAndDescription {
        public TitleAndDescription(String title, String description) {
            this.title = title;
            this.description = description;
        }

        private String title;
        private String description;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }
}
