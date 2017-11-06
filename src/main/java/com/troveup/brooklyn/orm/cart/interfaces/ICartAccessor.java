package com.troveup.brooklyn.orm.cart.interfaces;

import com.troveup.brooklyn.orm.cart.model.*;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.order.model.PrintOrder;
import com.troveup.brooklyn.orm.user.model.User;
import org.joda.money.Money;

import javax.jdo.PersistenceManager;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 5/12/15.
 */
public interface ICartAccessor
{
    Boolean persistCart(Cart cart);
    Cart getShoppingCart(Long cartId, IEnums.SEEK_MODE mode);
    Cart getShoppingCartByUser(Long userId, IEnums.SEEK_MODE mode);
    Cart getShoppingCartAttached(Long cartId, PersistenceManager pm);
    Boolean removeItemFromCart(Long cartId, Long cartItemId);
    Boolean updateCartItem(CartItem cartItem);
    Boolean updateCartItemEstimatedPrice(Long cartItemId, String estimatedPrice);
    Boolean updateCartItemActualPrice(Long cartItemId, BigDecimal actualPrice);
    CartItem getCartItemAttached(Long cartItemId, PersistenceManager pm);
    CartItem getCartItem(Long cartItemId, IEnums.SEEK_MODE mode);
    List<CartItem> getCartItemsAttached(List<Long> cartItemId, PersistenceManager pm);
    Boolean updateCart(Cart cart);
    Boolean updateCartAttribute(Long cartId, CartAttribute attribute);
    Boolean updateCartAttribute(Long cartId, List<CartAttribute> attributes);
    Boolean updateCartItemAttribute(Long cartItemId, CartItemAttribute attribute);
    Boolean addPaymentDetails(Long cartId, PaymentDetails details);
    Boolean addCartItemToCart(Long cartId, CartItem cartItem, String size);
    Boolean updateCartItemAttribute(Long cartItemId, String attributeName, String attributeValue);
    Boolean addCartItemAttributes(Long cartItemId, List<CartItemAttribute> attributes);
    Boolean updateCartItemStatus(Long cartItemId, CartItem.CART_ITEM_STATUS status);



    //TODO:  Build in tax and shipping refresh once the proper APIs have been implemented
    /**
     * Refreshes the grand total.  Will recalculate the sub-total, then recalculate and apply all of the discounts.  This method
     * should be called AFTER the tax and shipping has been refreshed externally, as it depends on those values to be
     * populated without any discounts applied.
     * @param cartId Unique primary key ID of the cart.
     * @param shouldApplyStoreBalance
     * @param shouldClearDirtyFlag
     * @return New Cart object that contains the new values.
     */
    Cart refreshGrandTotal(Long cartId, Boolean shouldApplyStoreBalance, Boolean shouldClearDirtyFlag);
    Cart refreshCartEstimatedSubtotal(Cart cart);
    Cart refreshCartEstimatedSubtotal(Long cartId);
    Cart refreshCartActualSubtotal(Long cartId);
    Boolean getDirtySubtotalStatus(Long cartId);

    Boolean setCartItemPrototypeRequestedFlag(List<Long> cartItemId, Boolean prorotypeRequestedFlagValue);
    Boolean addCartItemPrintOrder(Long cartItemId, PrintOrder order);
    CartItem getCartItemByPrintOrder(Long printOrderPrimaryKeyId, IEnums.SEEK_MODE mode);
    List<CartItem> getOutstandingPrototypeCartItems(Long limit, IEnums.SEEK_MODE mode);
    Boolean updateCartItemLastOutstandingPrototypeCheckDate(Long cartItemId, Date date);
    Boolean updateCartTax(Long cartId, BigDecimal taxValue);
    Boolean addGenericItemToCart(Long cartId, GenericItem genericItem);
    Boolean removeGenericItemFromCart(Long cartId, Long genericItemId);

    GenericItem getGenericItem(Long genericItemId, IEnums.SEEK_MODE mode);
    GenericItem getGenericItemAttached(Long genericItemId, PersistenceManager pm);
    Boolean updateGenericItemReferenceId(Long genericItemId, Long referenceId);
    Boolean persistGiftCard(GiftCard giftCard);
    List<GiftCard> getDigitalGiftCardsToBeMailed(Date date);
    Boolean setDigitalGiftCardStatusSent(Long giftCardId);
    GiftCard getGiftCardAttached(Long giftCardId, PersistenceManager pm);
    GiftCard getGiftCardAttached(String giftCardNumber, PersistenceManager pm);
    Boolean checkIfGiftCardNumberExists(String giftCardNumber);
    Boolean validateGiftCard(String giftCardNumber);
    BigDecimal redeemGiftCard(String giftCardNumber, Long userId);
    Boolean setStoreBalanceOffset(Long cartId, BigDecimal amount);

    GenericItem setGenericItemStatus(Long genericItemId ,GenericItem.GENERIC_ITEM_STATUS status);
}
