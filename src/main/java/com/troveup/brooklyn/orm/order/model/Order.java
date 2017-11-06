package com.troveup.brooklyn.orm.order.model;

import com.troveup.brooklyn.orm.cart.model.*;
import com.troveup.brooklyn.orm.user.model.Address;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.orm.simpleitem.model.OrderNote;

import javax.jdo.annotations.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 5/12/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class Order
{

    public enum ORDER_STATUS
    {
        OPEN,
        //Closed, but payment not yet processed
        SETTLING,
        SETTLED,
        FAILED_TO_SETTLE,
        CLOSED
    }

    private Long orderId;
    private Cart cartReference;
    private String vendorCartId;
    private List<CartItem> orderItems;
    private BigDecimal subTotal;
    private BigDecimal tax;
    private BigDecimal shipping;
    private BigDecimal grandTotal;
    private BigDecimal additionalDollarDiscount;
    private BigDecimal finalSettlementAmount;
    private BigDecimal finalSettlementTax;
    private BigDecimal finalSettlementSubtotal;
    private BigDecimal finalSettlementShipping;
    private BigDecimal refundedStoreBalance;
    private double additionalPercentDiscount;
    private boolean freeShipping;
    private Address billingAddress;
    private Address shippingAddress;
    private PaymentDetails paymentDetails;
    private Date orderDate;
    private String troveOrderNumber;
    private String vendorOrderNumber;
    private ORDER_STATUS orderStatus;
    private String paymentProcessorTransactionId;
    private String checkoutEmail;
    private ShipmentTracking trackingInfo;
    private List<PromoCode> appliedPromoCodes;
    private Integer settlementAttempts;
    private String settlementMessage;
    private String userFriendlyOrderDate;
    private List<GenericItem> genericItemsList;
    private List<EasyPostPostageLabel> shippingLabels;
    private List<OrderNote> orderNotes; //latest note first
    private OrderNote latestNote;
    private Boolean hasNote;

    private Boolean shouldBeGiftWrapped;
    private Boolean includeGiftMessageCard;
    private String giftMessageCardText;

    private BigDecimal storeBalanceOffset;

    //Convenience field for storing the purchaser.  Not persisted, as this is kept in a list on the user end.
    private User purchaser;

    private Boolean originFtue;

    public Order()
    {
        this.orderStatus = ORDER_STATUS.OPEN;
        this.originFtue = false;
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Persistent
    @Column(name = "vendor_cart_id", allowsNull = "true")
    public String getVendorCartId() {
        return vendorCartId;
    }

    public void setVendorCartId(String vendorCartId) {
        this.vendorCartId = vendorCartId;
    }

    @Join(table = "ORDER_CARTITEMS")
    @Persistent
    public List<CartItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<CartItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Join(table = "ORDER_CART")
    @Persistent
    public Cart getCartReference() {
        return cartReference;
    }

    public void setCartReference(Cart cartReference) {
        this.cartReference = cartReference;
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
    @Column(name = "additional_dollar_discount", allowsNull = "true", scale = 4)
    public BigDecimal getAdditionalDollarDiscount() {
        return additionalDollarDiscount;
    }

    public void setAdditionalDollarDiscount(BigDecimal additionalDollarDiscount) {
        this.additionalDollarDiscount = additionalDollarDiscount;
    }

    @Persistent
    @Column(name = "additional_dollar_percent", allowsNull = "true")
    public double getAdditionalPercentDiscount() {
        return additionalPercentDiscount;
    }

    public void setAdditionalPercentDiscount(double additionalPercentDiscount) {
        this.additionalPercentDiscount = additionalPercentDiscount;
    }

    @Persistent
    @Column(name = "free_shipping", allowsNull = "true")
    public boolean isFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(boolean freeShipping) {
        this.freeShipping = freeShipping;
    }

    @Join(table = "ORDER_BILLING_ADDRESS")
    @Persistent
    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    @Join(table = "ORDER_SHIPPING_ADDRESS")
    @Persistent
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

    @Persistent
    @Column(name = "order_date", allowsNull = "true")
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Persistent
    @Column(name = "vendor_order_number", allowsNull = "true")
    public String getVendorOrderNumber() {
        return vendorOrderNumber;
    }

    public void setVendorOrderNumber(String vendorOrderNumber) {
        this.vendorOrderNumber = vendorOrderNumber;
    }

    @Persistent
    public ORDER_STATUS getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(ORDER_STATUS orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Persistent(defaultFetchGroup = "false")
    @Column(name = "payment_trans_id", allowsNull = "true")
    public String getPaymentProcessorTransactionId() {
        return paymentProcessorTransactionId;
    }

    public void setPaymentProcessorTransactionId(String paymentProcessorTransactionId) {
        this.paymentProcessorTransactionId = paymentProcessorTransactionId;
    }

    @Persistent
    @Column(name = "tracking_info", allowsNull = "true")
    public ShipmentTracking getTrackingInfo() {
        return trackingInfo;
    }

    public void setTrackingInfo(ShipmentTracking trackingInfo) {
        this.trackingInfo = trackingInfo;
    }

    @Persistent
    @Column(name = "applied_promo_codes", allowsNull = "true")
    public List<PromoCode> getAppliedPromoCodes() {
        return appliedPromoCodes;
    }

    public void setAppliedPromoCodes(List<PromoCode> appliedPromoCodes) {
        this.appliedPromoCodes = appliedPromoCodes;
    }

    @Persistent
    @Column(name = "trove_order_number")
    public String getTroveOrderNumber() {
        return troveOrderNumber;
    }

    public void setTroveOrderNumber(String troveOrderNumber) {
        this.troveOrderNumber = troveOrderNumber;
    }

    @Persistent
    public String getCheckoutEmail() {
        return checkoutEmail;
    }

    public void setCheckoutEmail(String checkoutEmail) {
        this.checkoutEmail = checkoutEmail;
    }

    @Persistent
    @Column(scale = 2)
    public BigDecimal getFinalSettlementAmount() {
        return finalSettlementAmount;
    }

    public void setFinalSettlementAmount(BigDecimal finalSettlementAmount) {
        this.finalSettlementAmount = finalSettlementAmount;
    }

    @Persistent
    public Integer getSettlementAttempts() {
        return settlementAttempts;
    }

    public void setSettlementAttempts(Integer settlementAttempts) {
        this.settlementAttempts = settlementAttempts;
    }

    @Persistent
    public String getSettlementMessage() {
        return settlementMessage;
    }

    public void setSettlementMessage(String settlementMessage) {
        this.settlementMessage = settlementMessage;
    }

    @Persistent
    @Column(scale = 2)
    public BigDecimal getFinalSettlementTax() {
        return finalSettlementTax;
    }

    public void setFinalSettlementTax(BigDecimal finalSettlementTax) {
        this.finalSettlementTax = finalSettlementTax;
    }

    @Persistent
    @Column(scale = 2)
    public BigDecimal getFinalSettlementSubtotal() {
        return finalSettlementSubtotal;
    }

    public void setFinalSettlementSubtotal(BigDecimal finalSettlementSubtotal) {
        this.finalSettlementSubtotal = finalSettlementSubtotal;
    }

    @Persistent
    @Column(scale = 2)
    public BigDecimal getStoreBalanceOffset() {
        return storeBalanceOffset;
    }

    public void setStoreBalanceOffset(BigDecimal storeBalanceOffset) {
        this.storeBalanceOffset = storeBalanceOffset;
    }

    @NotPersistent
    public String getUserFriendlyOrderDate() {
        return userFriendlyOrderDate;
    }

    public void setUserFriendlyOrderDate(String userFriendlyOrderDate) {
        this.userFriendlyOrderDate = userFriendlyOrderDate;
    }

    @Join(table = "ORDER_GENERICITEMS")
    @Persistent
    public List<GenericItem> getGenericItemsList() {
        return genericItemsList;
    }

    public void setGenericItemsList(List<GenericItem> genericItemsList) {
        this.genericItemsList = genericItemsList;
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

    @Persistent
    public String getGiftMessageCardText() {
        return giftMessageCardText;
    }

    public void setGiftMessageCardText(String giftMessageCardText) {
        this.giftMessageCardText = giftMessageCardText;
    }

    @Persistent
    @Column(scale = 2)
    public BigDecimal getRefundedStoreBalance() {
        return refundedStoreBalance;
    }

    public void setRefundedStoreBalance(BigDecimal refundedStoreBalance) {
        this.refundedStoreBalance = refundedStoreBalance;
    }

    @NotPersistent
    public User getPurchaser() {
        return purchaser;
    }

    public void setPurchaser(User purchaser) {
        this.purchaser = purchaser;
    }

    @Persistent
    @Column(defaultValue = "0")
    public Boolean getOriginFtue() {
        return originFtue;
    }

    public void setOriginFtue(Boolean originFtue) {
        this.originFtue = originFtue;
    }

    @Persistent
    @Column(scale = 2)
    public BigDecimal getFinalSettlementShipping() {
        return finalSettlementShipping;
    }

    public void setFinalSettlementShipping(BigDecimal finalSettlementShipping) {
        this.finalSettlementShipping = finalSettlementShipping;
    }

    @Join(table = "ORDER_EASYPOSTLABELS")
    @Persistent
    public List<EasyPostPostageLabel> getShippingLabels() {
        return shippingLabels;
    }

    public void setShippingLabels(List<EasyPostPostageLabel> shippingLabels) {
        this.shippingLabels = shippingLabels;
    }

    @Join(table = "ORDER_NOTES")
    @Persistent
    public List<OrderNote> getOrderNotes() { return orderNotes; }

    public void setOrderNotes(List<OrderNote> orderNotes) {
        this.orderNotes = orderNotes;
    }

    @NotPersistent
    public OrderNote getLatestNote() {
        return latestNote;
    }

    public void setLatestNote(OrderNote latestNote) {
        this.latestNote = latestNote;
    }

    @NotPersistent
    public Boolean getHasNote() {
        return hasNote;
    }

    public void setHasNote(Boolean hasNote) {
        this.hasNote = hasNote;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Order)
            return ((Order) obj).getOrderId() == this.orderId;
        else
            return false;
    }

    public static List<String> getOrdersSlimFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("shippingAddress");
        rval.add("subTotal");
        rval.add("tax");
        rval.add("shipping");
        rval.add("grandTotal");
        rval.add("additionalDollarDiscount");

        return rval;
    }

    public static List<String> getOrdersQuickFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("shippingAddress");
        rval.add("orderDate");
        rval.add("billingAddress");
        rval.add("paymentDetails");
        rval.add("subTotal");
        rval.add("tax");
        rval.add("shipping");
        rval.add("grandTotal");
        rval.add("additionalDollarDiscount");
        rval.add("orderStatus");
        rval.add("trackingInfo");
        rval.add("vendorCartId");

        return rval;
    }

    public static List<String> getOrdersMediumFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("shippingAddress");
        rval.add("orderDate");
        rval.add("orderItems");
        rval.add("billingAddress");
        rval.add("paymentDetails");
        rval.add("subTotal");
        rval.add("tax");
        rval.add("shipping");
        rval.add("grandTotal");
        rval.add("additionalDollarDiscount");
        rval.add("orderStatus");
        rval.add("trackingInfo");
        rval.add("genericItemsList");

        return rval;
    }

    public static List<String> getOrdersFullFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("shippingAddress");
        rval.add("orderDate");
        rval.add("orderItems");
        rval.add("billingAddress");
        rval.add("paymentDetails");
        rval.add("subTotal");
        rval.add("tax");
        rval.add("shipping");
        rval.add("grandTotal");
        rval.add("additionalDollarDiscount");
        rval.add("orderStatus");
        rval.add("trackingInfo");
        rval.add("genericItemsList");
        rval.add("cartReference");
        rval.add("shippingLabels");
        rval.add("orderNotes");

        return rval;
    }

    public static List<String> getOrdersAddressFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("shippingAddress");
        rval.add("billingAddress");

        return rval;
    }

    public static List<String> getOrdersUploadFetchgroups()
    {
        List<String> rval = new ArrayList<>();

        rval.add("orderItems");
        rval.add("shippingAddress");

        return rval;
    }

    public static List<String> getOrdersSettlementFetchGroups()
    {
        List<String> rval = new ArrayList<>();

        rval.add("orderItems");
        rval.add("shippingAddress");
        rval.add("paymentProcessorTransactionId");
        rval.add("genericItemsList");

        return rval;
    }

    public static List<String> getOrdersLineItemFetchGroups()
    {
        List<String> rval = new ArrayList<>();

        rval.add("orderItems");
        rval.add("genericItemsList");
        rval.add("orderNotes");

        return rval;
    }

    public static List<String> getOrderInfluencerDashboardFetchGroups() {
        List<String> rval = new ArrayList<>();

        rval.add("appliedPromoCodes");
        rval.add("genericItemsList");

        return rval;
    }

    public static Builder getBuilder()
    {
        return new Builder();
    }

    public static Builder getBuilder(Cart cart)
    {
        return new Builder(cart);
    }

    public static class Builder
    {
        private String vendorCartId;
        private Cart cartReference;
        private List<CartItem> orderItems;
        private BigDecimal subTotal;
        private BigDecimal tax;
        private BigDecimal shipping;
        private BigDecimal grandTotal;
        private BigDecimal additionalDollarDiscount;
        private double additionalPercentDiscount;
        private boolean freeShipping;
        private Address billingAddress;
        private Address shippingAddress;
        PaymentDetails paymentDetails;
        Date orderDate;
        String vendorOrderNumber;
        ORDER_STATUS orderStatus;
        String trackingNumber;
        String shipper;
        String paymentProcessorTransactionId;
        List<PromoCode> appliedPromoCode;
        private String troveOrderNumber;
        private String checkoutEmail;
        List<GenericItem> genericItemsList;
        private BigDecimal storeBalanceOffset;
        private Boolean shouldBeGiftWrapped;
        private Boolean includeGiftMessageCard;
        private String giftMessageCardText;
        private Boolean originFtue;

        public Builder()
        {

        }

        public Builder(Cart cart)
        {
            this.vendorCartId = cart.getVendorCartId();
            this.cartReference = cart;
            this.orderItems = cart.getCartItems();
            this.subTotal = cart.getSubTotal();
            this.tax = cart.getTax();
            this.shipping = cart.getShipping();
            this.grandTotal = cart.getGrandTotal();
            this.additionalDollarDiscount = cart.getAdditionalDollarDiscount();
            this.additionalPercentDiscount = cart.getAdditionalPercentDiscount();
            this.freeShipping = cart.isFreeShipping();
            this.billingAddress = cart.getBillingAddress();
            this.shippingAddress = cart.getShippingAddress();
            this.paymentDetails = cart.getPaymentDetails();
            this.appliedPromoCode = cart.getAppliedPromoCodes();
            this.orderDate = new Date();
            this.checkoutEmail = cart.getCheckoutEmail();
            this.genericItemsList = cart.getGenericItems();
            this.storeBalanceOffset = cart.getStoreBalanceOffset();
            this.shouldBeGiftWrapped = cart.getShouldBeGiftWrapped();
            this.includeGiftMessageCard = cart.getIncludeGiftMessageCard();
            this.giftMessageCardText = cart.getGiftMessageCardText();
            this.originFtue = cart.getOriginFtue();
        }

        public String getVendorCartId() {
            return vendorCartId;
        }

        public void setVendorCartId(String vendorCartId) {
            this.vendorCartId = vendorCartId;
        }

        public List<CartItem> getOrderItems() {
            return orderItems;
        }

        public void setOrderItems(List<CartItem> orderItems) {
            this.orderItems = orderItems;
        }

        public BigDecimal getSubTotal() {
            return subTotal;
        }

        public void setSubTotal(BigDecimal subTotal) {
            this.subTotal = subTotal;
        }

        public BigDecimal getTax() {
            return tax;
        }

        public void setTax(BigDecimal tax) {
            this.tax = tax;
        }

        public BigDecimal getShipping() {
            return shipping;
        }

        public void setShipping(BigDecimal shipping) {
            this.shipping = shipping;
        }

        public BigDecimal getGrandTotal() {
            return grandTotal;
        }

        public void setGrandTotal(BigDecimal grandTotal) {
            this.grandTotal = grandTotal;
        }

        public BigDecimal getAdditionalDollarDiscount() {
            return additionalDollarDiscount;
        }

        public void setAdditionalDollarDiscount(BigDecimal additionalDollarDiscount) {
            this.additionalDollarDiscount = additionalDollarDiscount;
        }

        public double getAdditionalPercentDiscount() {
            return additionalPercentDiscount;
        }

        public void setAdditionalPercentDiscount(double additionalPercentDiscount) {
            this.additionalPercentDiscount = additionalPercentDiscount;
        }

        public boolean isFreeShipping() {
            return freeShipping;
        }

        public void setFreeShipping(boolean freeShipping) {
            this.freeShipping = freeShipping;
        }

        public Address getBillingAddress() {
            return billingAddress;
        }

        public void setBillingAddress(Address billingAddress) {
            this.billingAddress = billingAddress;
        }

        public Address getShippingAddress() {
            return shippingAddress;
        }

        public void setShippingAddress(Address shippingAddress) {
            this.shippingAddress = shippingAddress;
        }

        public PaymentDetails getPaymentDetails() {
            return paymentDetails;
        }

        public void setPaymentDetails(PaymentDetails paymentDetails) {
            this.paymentDetails = paymentDetails;
        }

        public Date getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(Date orderDate) {
            this.orderDate = orderDate;
        }

        public String getVendorOrderNumber() {
            return vendorOrderNumber;
        }

        public void setVendorOrderNumber(String vendorOrderNumber) {
            this.vendorOrderNumber = vendorOrderNumber;
        }

        public ORDER_STATUS getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(ORDER_STATUS orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getTrackingNumber() {
            return trackingNumber;
        }

        public void setTrackingNumber(String trackingNumber) {
            this.trackingNumber = trackingNumber;
        }

        public String getShipper() {
            return shipper;
        }

        public void setShipper(String shipper) {
            this.shipper = shipper;
        }

        public Cart getCartReference() {
            return cartReference;
        }

        public void setCartReference(Cart cartReference) {
            this.cartReference = cartReference;
        }

        public String getPaymentProcessorTransactionId() {
            return paymentProcessorTransactionId;
        }

        public List<PromoCode> getAppliedPromoCode() {
            return appliedPromoCode;
        }

        public void setAppliedPromoCode(List<PromoCode> appliedPromoCode) {
            this.appliedPromoCode = appliedPromoCode;
        }

        public String getTroveOrderNumber() {
            return troveOrderNumber;
        }

        public void setTroveOrderNumber(String troveOrderNumber) {
            this.troveOrderNumber = troveOrderNumber;
        }

        public void setPaymentProcessorTransactionId(String paymentProcessorTransactionId) {
            this.paymentProcessorTransactionId = paymentProcessorTransactionId;
        }

        public String getCheckoutEmail() {
            return checkoutEmail;
        }

        public void setCheckoutEmail(String checkoutEmail) {
            this.checkoutEmail = checkoutEmail;
        }

        public List<GenericItem> getGenericItems() {
            return genericItemsList;
        }

        public void setGenericItems(List<GenericItem> genericItems) {
            this.genericItemsList = genericItems;
        }

        public List<GenericItem> getGenericItemsList() {
            return genericItemsList;
        }

        public void setGenericItemsList(List<GenericItem> genericItemsList) {
            this.genericItemsList = genericItemsList;
        }

        public BigDecimal getStoreBalanceOffset() {
            return storeBalanceOffset;
        }

        public void setStoreBalanceOffset(BigDecimal storeBalanceOffset) {
            this.storeBalanceOffset = storeBalanceOffset;
        }

        public Boolean getOriginFtue() {
            return originFtue;
        }

        public void setOriginFtue(Boolean originFtue) {
            this.originFtue = originFtue;
        }

        public Order buildOrder()
        {
            Order order = new Order();
            order.cartReference = this.cartReference;
            order.vendorCartId = this.vendorCartId;
            order.orderItems = this.orderItems;
            order.subTotal = this.subTotal;
            order.tax = this.tax;
            order.shipping = this.shipping;
            order.grandTotal = this.grandTotal;
            order.additionalDollarDiscount = this.additionalDollarDiscount;
            order.additionalPercentDiscount = this.additionalPercentDiscount;
            order.freeShipping = this.freeShipping;
            order.billingAddress = this.billingAddress;
            order.shippingAddress = this.shippingAddress;
            order.paymentDetails = this.paymentDetails;
            order.orderDate = this.orderDate;
            order.vendorOrderNumber = this.vendorOrderNumber;
            order.orderStatus = this.orderStatus;
            order.paymentProcessorTransactionId = this.paymentProcessorTransactionId;
            order.appliedPromoCodes = this.appliedPromoCode;
            order.troveOrderNumber = this.troveOrderNumber;
            order.checkoutEmail = this.checkoutEmail;
            order.genericItemsList = this.genericItemsList;
            order.storeBalanceOffset = this.storeBalanceOffset;
            order.shouldBeGiftWrapped = this.shouldBeGiftWrapped;
            order.includeGiftMessageCard = this.shouldBeGiftWrapped;
            order.giftMessageCardText = this.giftMessageCardText;
            order.originFtue = this.originFtue;

            return order;
        }
    }

    public static List<String> getOrderStatusList()
    {
        List<String> orderStatuses = new ArrayList<>();

        for (ORDER_STATUS status : ORDER_STATUS.values())
        {
            orderStatuses.add(status.toString());
        }

        return orderStatuses;
    }

    public static List<Long> getOrderIdsFromList(List<Order> orders)
    {
        List<Long> rval = new ArrayList<>();

        if (orders != null && orders.size() > 0)
        {
            for (Order order : orders)
            {
                rval.add(order.getOrderId());
            }
        }

        return rval;
    }
}
