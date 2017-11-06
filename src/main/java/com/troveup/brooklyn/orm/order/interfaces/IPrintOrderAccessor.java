package com.troveup.brooklyn.orm.order.interfaces;

import com.troveup.brooklyn.orm.cart.model.Cart;
import com.troveup.brooklyn.orm.cart.model.CartItem;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.order.model.EasyPostPostageLabel;
import com.troveup.brooklyn.orm.order.model.PrintOrder;

import java.util.Date;
import java.util.List;

/**
 * Created by tim on 7/26/15.
 */
public interface IPrintOrderAccessor
{
    Boolean persistPrintOrder(PrintOrder order);
    Boolean updatePrintOrderStatus(Long printOrderId, PrintOrder.ORDER_STATUS status);
    Boolean updatePrintOrderStatus(String manufacturerOrderNumber, PrintOrder.ORDER_STATUS status);
    Boolean updatePrintOrderStatuses(Long cartItemId, PrintOrder.ORDER_STATUS status);
    PrintOrder getPrintOrderByOrderNumber(String orderNumber, PrintOrder.ORDER_SYSTEM orderSystem,
                                          IEnums.SEEK_MODE mode);
    List<PrintOrder> getPrintOrdersByStatus(PrintOrder.ORDER_STATUS status, IEnums.SEEK_MODE mode, Long limit);
    Boolean updatePrintOrderManufacturerStatus(Long printOrderId, String status);
    List<PrintOrder> getPrintOrdersAwaitingSubmission(IEnums.SEEK_MODE mode, Long limit);
    Boolean updatePrintOrderShippingLabel(Long printOrderId, EasyPostPostageLabel shippingLabel);
    List<PrintOrder> getPrintOrdersBeingManufactured(IEnums.SEEK_MODE mode, Long limit);
    Boolean updatePrintOrderManufacturerOrderNumber(Long printOrderPrimaryKeyIdentifier, String orderNumber);
    Boolean updateLastStatusRequestDate(Long printOrderPrimaryKeyIdentifier, Date requestDate, Date nextRequestDate);
    Boolean updatePostageLabelStatus(String trackingNumber, EasyPostPostageLabel.TRACKING_STATUS status);
    String getEmailByTrackingNumber(String trackingNumber);
    PrintOrder.ORDER_STATUS getPrintOrderStatus(Long printOrderId);

    Boolean addPrintOrderToCartItem(Long cartItemId, PrintOrder printOrder);
    Boolean updateCartItemStatusByTrackingNumber(String trackingNumber, CartItem.CART_ITEM_STATUS status);
    List<CartItem> getCartItemsByTrackingNumber(String trackingNumber, IEnums.SEEK_MODE mode);
    Boolean updateOrderSubmitAttemptCount(Long printOrderId, Integer count);

}
