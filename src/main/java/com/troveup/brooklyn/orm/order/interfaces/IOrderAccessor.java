package com.troveup.brooklyn.orm.order.interfaces;

import com.troveup.brooklyn.orm.cart.model.CartItem;
import com.troveup.brooklyn.orm.cart.model.PaymentDetails;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.item.model.ItemCustomization;
import com.troveup.brooklyn.orm.order.model.EasyPostPostageLabel;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.user.model.Address;
import com.troveup.brooklyn.orm.simpleitem.model.OrderNote;

import javax.jdo.PersistenceManager;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 5/12/15.
 */
public interface IOrderAccessor
{
    Boolean persistOrder(Order order);
    Boolean deleteOrder(Long primaryKeyId);
    Order getOrder(Long orderId, IEnums.SEEK_MODE mode);
    List<Order> getOrdersByUser(Long userId);
    Order getOrderByOrderNumber(String orderNumber, IEnums.SEEK_MODE mode);
    List<Order> getOrders(List<String> request, IEnums.ORDER_REQUEST_TYPE type);
    List<Order> getOrders(Date from, Date to);
    List<Order> getOrders(PaymentDetails details);
    List<Order> getOrdersByBillingAddress(Address billingAddress);
    List<Order> getOrdersByShippingAddress(Address shippingAddress);
    Boolean checkIfOrderNumberExists(String orderNumber);

    List<Order> getPagedUserOrders(Long userId, List<Order.ORDER_STATUS> status, Integer collectionPage, Long collectionPageSize,
                                   Long extraResults, IEnums.SEEK_MODE mode);

    String getOrderEmailByCartItemId(Long cartItemId);
    Order getOrderByCartItemId(Long cartItemId, IEnums.SEEK_MODE mode);
    Boolean updateCartItemPrototypeEmailStatus(Long cartItemId, CartItem.PROTOTYPE_EMAIL_STATE state);
    Boolean updateOrderStatus(Long orderId, Order.ORDER_STATUS status);
    String getOrderFirstLastNameByCartItemId(Long cartItemId);
    Order getOrderAttached(Long orderId, PersistenceManager pm);
    Boolean isUserOwnerOfCartItem(Long cartItemId, Long userId);
    Boolean canLineItemBeCancelled(Long cartItemId);

    Boolean addCustomizationsToCartItem(Long cartItemId, List<ItemCustomization> customizations);
    Boolean incrementCartItemCustomizationIterationCounter(Long cartItemId);

    List<Order> getOrdersByStatus(Order.ORDER_STATUS status, Long seekLimit, IEnums.SEEK_MODE mode);
    Boolean updateOrderFinalSettlementAmount(Long orderId, BigDecimal finalSettlementAmount);
    Boolean updateOrderOverallSettlementAmount(Long orderId, BigDecimal finalSettlementAmount, BigDecimal finalSettlementTax, BigDecimal finalSettlementSubtotal, BigDecimal finalSettlementShipping);
    Boolean updateOrderErrorMessage(Long orderId, String errorMessage);
    Boolean incrementOrderSettlementAttempts(Long orderId);

    Boolean addOrderNote(Long orderId, Long userIdCreator, String noteText, Date timeCreated);

    Order getOrderByShippingTrackingNumber(String trackingNumber, IEnums.SEEK_MODE mode);
    Boolean cancelOrderLineItem(Long cartItemId);

    Boolean setOrderAppliedCredit(Long orderId, BigDecimal amount);
    Boolean setOrderCreditRefunded(Long orderId, BigDecimal amount);

    List<Order> getOrdersWithPurchaserAll(Integer collectionPage, Long collectionPageSize, IEnums.SEEK_MODE mode);
    List<Order> getOrdersWithPurchaserByStatus(Order.ORDER_STATUS status, Integer collectionPage, Long collectionPageSize, IEnums.SEEK_MODE mode);
    List<Order> getOrdersWithPurchaserByDateRange(Date beginDate, Date endDate, Integer collectionPage, Long collectionPageSize, IEnums.SEEK_MODE mode);
    List<Order> getOrdersWithPurchaserByAmountRange(BigDecimal lowerBoundAmount, BigDecimal upperBoundAmount, Integer collectionPage, Long collectionPageSize, IEnums.SEEK_MODE mode);
    List<Order> getOrdersWithPurchaserThatContainCartItemStatus(CartItem.CART_ITEM_STATUS status, Integer collectionPage, Long collectionPageSize, IEnums.SEEK_MODE mode);
    List<Order> getOrdersWithPurchaserThatContainCartItemCategory(String category, Integer collectionPage, Long collectionPageSize, IEnums.SEEK_MODE mode);
    List<Order> getOrdersWithPurchaserThatContainGenericItemCategory(String category, Integer collectionPage, Long collectionPageSize, IEnums.SEEK_MODE mode);
    List<Order> getOrdersWithPurchaserByUserId(Long userId, Integer collectionPage, Long collectionPageSize, IEnums.SEEK_MODE mode);
    List<Order> getOrdersWithPurchaserByUsername(String username, Integer collectionPage, Long collectionPageSize, IEnums.SEEK_MODE mode);
    List<Order> getOrdersWithPurchaserByAccountEmail(String email, Integer collectionPage, Long collectionPageSize, IEnums.SEEK_MODE mode);
    List<Order> getOrdersWithPurchaserByName(String name, Integer collectionPage, Long collectionPageSize, IEnums.SEEK_MODE mode);
    List<Order> getOrdersWithPurchaserByPromoCode(String promoCode, Integer collectionPage, Long collectionPageSize, IEnums.SEEK_MODE mode);
    Order getOrderWithPurchaserByOrderNumber(String orderNumber, IEnums.SEEK_MODE mode);
    Order getOrderWithPurchaserByOrderId(Long orderId, IEnums.SEEK_MODE mode);

    List<Order> populateOrderPurchaserFields(List<Order> persistenceManagedOrders, PersistenceManager pm);
    List<Order> getOrdersAttached(List<Long> orderId, PersistenceManager pm);

    Long getFtueOrderCountByAddress(String addressLine1, String addressLine2, String zipCode);
    Long getOrderIdContainingGenericItem(Long genericItemId);

    Boolean associateShippingLabelWithOrder(Long orderId, EasyPostPostageLabel label);
    Order getOrderContainingShippingLabel(String shippingLabelTrackingNumber, IEnums.SEEK_MODE mode);
    List<Order> getOrdersContainingInfluencerItems(Long influencerId, IEnums.SEEK_MODE mode);
}
