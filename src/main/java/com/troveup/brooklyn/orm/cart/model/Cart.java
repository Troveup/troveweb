package com.troveup.brooklyn.orm.cart.model;

import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.item.model.ItemAttribute;
import com.troveup.brooklyn.orm.user.model.Address;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.print.imaterialise.model.iMatCartItem;

import javax.jdo.annotations.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tim on 5/11/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class Cart
{
    private Long cartId;
    private String vendorCartId;
    private List<CartItem> cartItems;
    private BigDecimal subTotal;
    private BigDecimal estimatedSubtotal;
    private BigDecimal tax;
    private BigDecimal shipping;
    private BigDecimal grandTotal;
    private BigDecimal additionalDollarDiscount;
    private double additionalPercentDiscount;
    private boolean freeShipping;
    private Address billingAddress;
    private Address shippingAddress;
    private String checkoutEmail;
    private PaymentDetails paymentDetails;
    private List<PromoCode> appliedPromoCodes;
    private List<CartAttribute> cartAttributes;
    private User cartOwner;
    private Boolean shouldBeGiftWrapped;
    private Boolean includeGiftMessageCard;
    private String giftMessageCardText;
    private List<GenericItem> genericItems;
    private Boolean originFtue;

    //Amount of the subtotal that was offset by a store balance from a gift card
    private BigDecimal storeBalanceOffset;

    //Indicates that calculations based on the subtotal will need to be redone, like tax calculations, which involve an
    //external API call.
    private Boolean dirtySubtotal;

    private String currencyUnit;

    public Cart()
    {
        currencyUnit = "USD";
        originFtue = false;
        shipping = new BigDecimal(4.99);
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    @Persistent
    @Column(name = "vendor_cart_id", allowsNull = "true")
    public String getVendorCartId() {
        return vendorCartId;
    }

    public void setVendorCartId(String vendorCartId) {
        this.vendorCartId = vendorCartId;
    }

    @Persistent(mappedBy = "cart")
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @Persistent
    @Column(name = "subtotal", allowsNull = "true", scale = 4)
    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    @Persistent
    @Column(name = "tax", allowsNull = "true", scale = 4)
    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    @Persistent
    @Column(name = "shipping", allowsNull = "true", scale = 4)
    public BigDecimal getShipping() {
        return shipping;
    }

    public void setShipping(BigDecimal shipping) {
        this.shipping = shipping;
    }

    @Persistent
    @Column(name = "grand_total", allowsNull = "true", scale = 4)
    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    @Persistent
    @Column(name = "additionalDollarDiscount", allowsNull = "true", scale = 4)
    public BigDecimal getAdditionalDollarDiscount() {
        return additionalDollarDiscount;
    }

    public void setAdditionalDollarDiscount(BigDecimal additionalDollarDiscount) {
        this.additionalDollarDiscount = additionalDollarDiscount;
    }

    @Persistent
    @Column(name = "percent_discount", allowsNull = "false")
    public double getAdditionalPercentDiscount() {
        return additionalPercentDiscount;
    }

    public void setAdditionalPercentDiscount(double additionalPercentDiscount) {
        this.additionalPercentDiscount = additionalPercentDiscount;
    }

    @Persistent
    @Column(name = "free_shipping", allowsNull = "false")
    public boolean isFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(boolean freeShipping) {
        this.freeShipping = freeShipping;
    }

    @Persistent
    @Column(name = "shipping_address", allowsNull = "true")
    //Bug with datanucleus seems to update a join row before it exists...
    /*@Join(table = "CART_BILLING_ADDRESS")
    @Persistent*/
    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    @Persistent
    @Column(name = "billing_address", allowsNull = "true")
    //Bug with datanucleus seems to update a join row before it exists...
    /*@Join(table = "CART_SHIPPING_ADDRESS")
    @Persistent*/
    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    @Persistent
    @Column(name = "payment_details", allowsNull = "true")
    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    @Join(table = "APPLIED_PROMO_CODES")
    @Persistent
    public List<PromoCode> getAppliedPromoCodes() {
        return appliedPromoCodes;
    }

    public void setAppliedPromoCodes(List<PromoCode> appliedPromoCodes) {
        this.appliedPromoCodes = appliedPromoCodes;
    }

    @Persistent
    @Column(name = "currency_unit")
    public String getCurrencyUnit() {
        return currencyUnit;
    }

    public void setCurrencyUnit(String currencyUnit) {
        this.currencyUnit = currencyUnit;
    }

    @Persistent(mappedBy = "attributeOwner")
    public List<CartAttribute> getCartAttributes() {
        return cartAttributes;
    }

    public void setCartAttributes(List<CartAttribute> cartAttributes) {
        this.cartAttributes = cartAttributes;
    }

    public User getCartOwner() {
        return cartOwner;
    }

    public void setCartOwner(User cartOwner) {
        this.cartOwner = cartOwner;
    }

    @Persistent
    @Column(name = "estimated_subtotal", allowsNull = "true", scale = 4)
    public BigDecimal getEstimatedSubtotal() {
        return estimatedSubtotal;
    }

    public void setEstimatedSubtotal(BigDecimal estimatedSubtotal) {
        this.estimatedSubtotal = estimatedSubtotal;
    }

    @Persistent
    @Column(name = "checkout_email", allowsNull = "true")
    public String getCheckoutEmail() {
        return checkoutEmail;
    }

    public void setCheckoutEmail(String checkoutEmail) {
        this.checkoutEmail = checkoutEmail;
    }

    @Persistent
    public Boolean getShouldBeGiftWrapped() {
        return shouldBeGiftWrapped;
    }

    public void setShouldBeGiftWrapped(Boolean shouldBeGiftWrapped) {
        this.shouldBeGiftWrapped = shouldBeGiftWrapped;
    }

    @Persistent
    public Boolean getIncludeGiftMessageCard() {
        return includeGiftMessageCard;
    }

    public void setIncludeGiftMessageCard(Boolean includeGiftMessageCard) {
        this.includeGiftMessageCard = includeGiftMessageCard;
    }

    @Persistent(mappedBy = "cart")
    public List<GenericItem> getGenericItems() {
        return genericItems;
    }

    public void setGenericItems(List<GenericItem> genericItems) {
        this.genericItems = genericItems;
    }

    @Persistent
    public Boolean getDirtySubtotal() {
        return dirtySubtotal;
    }

    public void setDirtySubtotal(Boolean dirtySubtotal) {
        this.dirtySubtotal = dirtySubtotal;
    }

    @Persistent
    @Column(length = 500)
    public String getGiftMessageCardText() {
        return giftMessageCardText;
    }

    public void setGiftMessageCardText(String giftMessageCardText) {
        this.giftMessageCardText = giftMessageCardText;
    }

    @Persistent
    @Column(scale = 2)
    public BigDecimal getStoreBalanceOffset() {
        return storeBalanceOffset;
    }

    public void setStoreBalanceOffset(BigDecimal storeBalanceOffset) {
        this.storeBalanceOffset = storeBalanceOffset;
    }

    @Persistent
    @Column(defaultValue = "0")
    public Boolean getOriginFtue() {
        return originFtue;
    }

    public void setOriginFtue(Boolean originFtue) {
        this.originFtue = originFtue;
    }

    //New checkout flow, as Item level attributes will no longer contain pertinent iMat details
    public Map<CartItem, iMatCartItem> getCartItemsAsiMatItemsFromItemReference()
    {
        Map<CartItem, iMatCartItem> rval = new HashMap<>();
        for (CartItem item : cartItems)
        {
            if (ItemAttribute.getItemAttribute(ItemAttribute.ATTRIBUTE_TYPE.LEGACY_CHECKOUT, item.getCartItemReference().getItemAttributes()) != null)
                rval.put(item, item.toImatCartItemLegacyWorkflow());
            else
                rval.put(item, item.toImatCartItemWorkflow());
        }

        return rval;
    }

    //Old checkout flow, left for legacy purposes
    public Map<Item, iMatCartItem> getCartItemRefsAsiMatItemsFromImatReference()
    {
        Map<Item, iMatCartItem> rval = new HashMap<>();
        for (CartItem item : cartItems)
        {
            rval.put(item.getCartItemReference(), item.toImatCartItemLegacyWorkflow());
        }

        return rval;
    }

    public static Builder getBuilder()
    {
        return new Builder();
    }

    public static Builder getBuilder(int quantity, List<Item> items, Float estimatedPrice, String currencyUnit)
    {
        return new Builder(quantity, items, estimatedPrice, currencyUnit);
    }

    public static Builder getBuilder(int quantity, Item item, Float estimatedPrice, String currencyUnit)
    {
        return new Builder(quantity, item, estimatedPrice, currencyUnit);
    }

    public static List<String> getCartFullFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("cartItems");
        rval.add("subTotal");
        rval.add("tax");
        rval.add("shipping");
        rval.add("grandTotal");
        rval.add("additionalDollarDiscount");
        rval.add("billingAddress");
        rval.add("shippingAddress");
        rval.add("paymentDetails");
        rval.add("appliedPromoCodes");
        rval.add("cartAttributes");
        rval.add("cartOwner");
        rval.add("genericItems");

        return rval;
    }

    public static List<String> getCartQuickFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("subTotal");
        rval.add("tax");
        rval.add("shipping");
        rval.add("grandTotal");
        rval.add("additionalDollarDiscount");

        return rval;
    }

    public static List<String> getCartPaymentDetailsFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("subTotal");
        rval.add("tax");
        rval.add("shipping");
        rval.add("grandTotal");
        rval.add("paymentDetails");

        return rval;
    }

    public static List<String> getCartPromoFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("subTotal");
        rval.add("tax");
        rval.add("shipping");
        rval.add("grandTotal");
        rval.add("appliedPromoCodes");
        rval.add("cartItems");

        return rval;
    }

    public static List<String> getCartItemsFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("cartItems");

        return rval;
    }
    public static List<String> getCartAddressAndCartItemFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("billingAddress");
        rval.add("shippingAddress");
        rval.add("cartItems");

        return rval;
    }

    public static List<String> getCartAddressFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("billingAddress");
        rval.add("shippingAddress");

        return rval;
    }

    public static class Builder
    {
        int quantity;
        List<Item> items;
        Float estimatedPrice;
        String currencyUnit;

        public Builder()
        {
            items = new ArrayList<>();
        }

        public Builder(int quantity, List<Item> item, Float estimatedPrice, String currencyUnit)
        {
            this.quantity = quantity;
            this.items = new ArrayList<>();
            items.addAll(item);
            this.estimatedPrice = estimatedPrice;
            this.currencyUnit = currencyUnit;
        }

        public Builder(int quantity, Item item, Float estimatedPrice, String currencyUnit)
        {
            this.quantity = quantity;
            this.items = new ArrayList<>();
            items.add(item);
            this.estimatedPrice = estimatedPrice;
            this.currencyUnit = currencyUnit;
        }

        public int getQuantity() {
            return quantity;
        }

        public Builder setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public List<Item> getItems() {
            return items;
        }

        public Builder setItems(List<Item> items) {
            this.items = items;
            return this;
        }

        public Builder addItems(List<Item> items)
        {
            items.addAll(items);
            return this;
        }

        public Float getEstimatedPrice() {
            return estimatedPrice;
        }

        public Builder setEstimatedPrice(Float estimatedPrice) {
            this.estimatedPrice = estimatedPrice;
            return this;
        }

        public String getCurrencyUnit() {
            return currencyUnit;
        }

        public Builder setCurrencyUnit(String currencyUnit) {
            this.currencyUnit = currencyUnit;
            return this;
        }

        public Cart build()
        {
            Cart cart = new Cart();

            if (currencyUnit != null)
                cart.setCurrencyUnit(currencyUnit);

            cart.setCartItems(new ArrayList<CartItem>());

            for (Item item : items)
            {
                cart.getCartItems().add(CartItem.getBuilder(item, 1, estimatedPrice, item.getMaterialId(), item.getFinishId()).build());
            }

            return cart;
        }
    }

    public static BigDecimal getStandardShippingRate()
    {
        return new BigDecimal(4.99);
    }
}
