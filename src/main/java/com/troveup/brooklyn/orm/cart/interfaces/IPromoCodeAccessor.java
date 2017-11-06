package com.troveup.brooklyn.orm.cart.interfaces;

import com.troveup.brooklyn.orm.cart.model.Cart;
import com.troveup.brooklyn.orm.cart.model.PromoCode;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.user.model.User;

import javax.jdo.PersistenceManager;
import java.util.List;

/**
 * Created by tim on 6/13/15.
 */
public interface IPromoCodeAccessor
{
    //From an earlier implementation
    //Boolean addPromoCode(Long cartId, PromoCode promoCode);
    //Boolean removePromoCode(Long cartId, Long promoCodeId);
    PromoCode getPromoCodeAttached(Long promoCodeId, PersistenceManager pm);
    Boolean updatePromoCode(PromoCode promoCode);

    Boolean addPromoCode(PromoCode promoCode);
    List<String> bulkAddPromoCode(PromoCode promoCode, Integer numberToGenerate);
    Boolean addPromoCodesToUsers(List<Long> userId, List<PromoCode> promoCodes);
    Boolean addPromoCodesToUser(Long userId, List<PromoCode> promoCodes);
    Boolean addPromoCodeToUser(Long userId, PromoCode promoCode);

    List<PromoCode> getGlobalPromoCodes();
    List<PromoCode> getGlobalActivePromoCodes();
    PromoCode getPromoCode(Long promoCodeId);
    PromoCode getPromoCode(String promoCode);

    List<User> getUsersWithAttachedPromoCode(Long promoCodeId);
    List<User> getUsersWithAttachedPromoCode(String promoCode);

    List<Order> getOrdersWithAppliedPromoCode(Long promoCodeId);
    List<Order> getOrdersWithAppliedPromoCode(String promoCode);
    PromoCode validatePromoCode(String promoCode, Long userId);
    PromoCode validatePromoCode(String promoCode);
    List<PromoCode> validatePromoCodes(List<String> promoCodes, Long userId);
    Boolean deactivatePromoCode(Long promoCodeId);
    Boolean consumePromoCode(String promoCodeId);
    Boolean consumePromoCodes(List<Long> promoCodeIds);

    List<String> bulkGenerateUniquePromoCodeNumbers(Integer numberOfPromocodes, Integer lengthOfCode);

    Boolean consumePromoCodes(Long cartId, Long userId);

    Cart applyPromoCodesToCart(List<Long> promoCode, Long cartId);
    Cart removePromoCodesFromCart(List<String> promoCodes, Long cartId);
    Boolean removeAllPromoCodesFromCart(Long cartId);
    Boolean doesPromoCodeExist(String promoCode);
    List<String> bulkCheckPromoCodeExistence(List<String> promoCodesToCheck);
}
