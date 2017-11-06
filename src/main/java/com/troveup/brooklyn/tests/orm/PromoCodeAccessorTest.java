package com.troveup.brooklyn.tests.orm;

import com.troveup.brooklyn.orm.cart.interfaces.ICartAccessor;
import com.troveup.brooklyn.orm.cart.interfaces.IPromoCodeAccessor;
import com.troveup.brooklyn.orm.cart.model.Cart;
import com.troveup.brooklyn.orm.cart.model.PromoCode;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.util.DateUtils;
import com.troveup.brooklyn.util.MoneyUtil;
import com.troveup.config.PersistenceConfig;
import com.troveup.config.SDKConfig;
import junit.framework.Assert;
import org.joda.money.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tim on 6/14/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, SDKConfig.class})
public class PromoCodeAccessorTest
{
    @Autowired
    IPromoCodeAccessor promoCodeAccessor;

    @Autowired
    ICartAccessor cartAccessor;

    @Test
    public void insertPromoCodeTest()
    {
        PromoCode code = new PromoCode();
        code.setControlReference(true);
        code.setBlanketCategory(PromoCode.DISCOUNT_CATEGORY.SUB_TOTAL.toString());
        code.setPromoCode("abcd");
        code.setActive(true);
        code.setAvailableUses(20);
        code.setExpires(DateUtils.getTomorrowDateObject());
        code.setUnlimited(true);
        code.setGlobal(true);
        code.setDollarDiscount(Money.parse("USD 5.00").getAmount());
        code.setBlanketApplication(true);

        Assert.assertTrue(promoCodeAccessor.addPromoCode(code));
    }

    @Test
    public void insertGlobalPromoCodes()
    {
        Map<String, Double> globalPromos = new HashMap<>();
        globalPromos.put("Launch15", 15d);
        globalPromos.put("Creat3d", 15d);
        globalPromos.put("Xclusive15", 15d);
        globalPromos.put("Kargo20", 20d);



        for (String codeToCreate : globalPromos.keySet()) {

            PromoCode code = new PromoCode();
            //Set this code up as a reference code, the code to which all other entered codes are compared
            code.setControlReference(true);

            //Blanket categories apply to the broader categories of discount, such as shipping, subtotal, grand total, and
            //tax
            code.setBlanketCategory(PromoCode.DISCOUNT_CATEGORY.SUB_TOTAL.toString());

            //Promo code name
            code.setPromoCode(codeToCreate);

            //Whether or not it is active
            code.setActive(true);

            //Whether or not it is unlimited
            code.setUnlimited(true);

            //If it is "global", this means that it doesn't explicitly apply to a single user
            code.setGlobal(true);

            //Percentage discount
            code.setPercentDiscount(globalPromos.get(codeToCreate));

            //Whether or not this is actually a blanket code
            code.setBlanketApplication(true);

            promoCodeAccessor.addPromoCode(code);
        }

    }

    @Test
    public void insertItemPromoCodeTest()
    {
        /*PromoCode code = new PromoCode();
        code.setControlReference(true);
        code.setPromoCode("testPercentCode");
        code.setUnlimited(false);
        code.setGlobal(true);
        code.setBlanketApplication(false);
        code.setMaterialId("3");
        code.setAvailableUses(5);*/

        /*PromoCode code = new PromoCode();
        code.setActive(true);
        code.setControlReference(true);
        code.setPromoCode("MOWSLEY50");
        code.setUnlimited(false);
        code.setGlobal(true);
        code.setDoesExpire(false);
        code.setBlanketApplication(false);
        code.setAvailableUses(5);
        code.setPercentDiscount(50);*/

        PromoCode code = new PromoCode();
        code.setActive(true);
        code.setControlReference(true);
        code.setPromoCode("f1b5307a6d");
        code.setMaterialId("2");
        code.setFinishId("81");
        code.setUnlimited(false);
        code.setGlobal(true);
        code.setDoesExpire(false);
        code.setBlanketApplication(false);
        code.setAvailableUses(1);
        code.setPercentDiscount(100);

        promoCodeAccessor.addPromoCode(code);
    }

    /*@Test
    public void*/

    @Test
    public void addPromoCodeToCartTest()
    {
        String promoCode = "ABCD";
        List<String> promoCodes = new ArrayList<>();
        promoCodes.add(promoCode);
        Long cartId = 4l;

        Cart cart = cartAccessor.getShoppingCart(cartId, IEnums.SEEK_MODE.CART_PROMO);

        List<PromoCode> promoCodeList = promoCodeAccessor.validatePromoCodes(promoCodes, 2l);

        List<Long> promoCodeIds = new ArrayList<>();

        promoCodeIds.add(promoCodeList.get(0).getPromoCodeId());

        Cart appliedPromoCart = promoCodeAccessor.applyPromoCodesToCart(promoCodeIds, cartId);

        Assert.assertNotNull(appliedPromoCart.getGrandTotal());
        Assert.assertFalse(MoneyUtil.bigDecimalToMoney(cart.getCurrencyUnit(), cart.getGrandTotal()).
                isEqual(MoneyUtil.bigDecimalToMoney(appliedPromoCart.getCurrencyUnit(), appliedPromoCart.getGrandTotal())));
        Money difference = MoneyUtil.bigDecimalToMoney(cart.getCurrencyUnit(), cart.getGrandTotal()).minus(appliedPromoCart.getGrandTotal());
        Assert.assertTrue(difference.isEqual(Money.parse(cart.getCurrencyUnit() + " 5.00")));
    }

    @Test
    public void removePromoCodeFromCartTest()
    {
        String promoCode = "ABCD";
        Long cartId = 4l;
        List<String> promoCodes = new ArrayList<>();
        promoCodes.add(promoCode);

        Cart cart = cartAccessor.getShoppingCart(cartId, IEnums.SEEK_MODE.CART_PROMO);

        Cart removedPromoCart = promoCodeAccessor.removePromoCodesFromCart(promoCodes, cartId);

        Assert.assertNotNull(removedPromoCart.getGrandTotal());
        Assert.assertFalse(MoneyUtil.bigDecimalToMoney(cart.getCurrencyUnit(), cart.getGrandTotal())
                .isEqual(MoneyUtil.bigDecimalToMoney(cart.getCurrencyUnit(), removedPromoCart.getGrandTotal())));
        Money subtractValue = Money.parse(cart.getCurrencyUnit() + " 5.00");
        Money checkValue = MoneyUtil.bigDecimalToMoney(cart.getCurrencyUnit(), cart.getGrandTotal()).plus(subtractValue);
        Assert.assertTrue(removedPromoCart.getGrandTotal().equals(checkValue));
    }

    @Test
    public void validatePromoCodeTest()
    {
        String promoCode = "ABCD";
        String promoCode2 = "EFGH";
        List<String> promoCodes = new ArrayList<>();
        promoCodes.add(promoCode);
        promoCodes.add(promoCode2);

        List<PromoCode> result = promoCodeAccessor.validatePromoCodes(promoCodes, 1l);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() == 2);
    }

    @Test
    public void validateCartSubtotal()
    {
        Long cartId = 4l;
        Cart cart = cartAccessor.getShoppingCart(cartId, IEnums.SEEK_MODE.FULL);

        Assert.assertTrue(MoneyUtil.bigDecimalToMoney(cart.getCurrencyUnit(), cart.getSubTotal()).
                isEqual(Money.parse(cart.getCurrencyUnit() + " 12.75")));
    }
}
