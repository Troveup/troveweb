package com.troveup.brooklyn.tests.orm;

import com.troveup.brooklyn.orm.cart.interfaces.ICartAccessor;
import com.troveup.brooklyn.orm.cart.interfaces.IPromoCodeAccessor;
import com.troveup.brooklyn.orm.cart.model.Cart;
import com.troveup.brooklyn.orm.cart.model.CartItem;
import com.troveup.brooklyn.orm.cart.model.PaymentDetails;
import com.troveup.brooklyn.orm.cart.model.PromoCode;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.countries.interfaces.ICountryAccessor;
import com.troveup.brooklyn.orm.countries.model.Country;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.user.interfaces.IUserAccessor;
import com.troveup.brooklyn.orm.user.model.Address;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.util.MoneyUtil;
import com.troveup.config.PersistenceConfig;
import org.joda.money.Money;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by tim on 5/17/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceConfig.class)
public class CartAccessorTest
{
    @Autowired
    ICartAccessor accessor;

    @Autowired
    ICountryAccessor countryAccessor;

    @Autowired
    IUserAccessor userAccessor;

    @Autowired
    IPromoCodeAccessor promoCodeAccessor;

    @Before
    public void setUp()
    {
        List<Country> countries = countryAccessor.getAllCountries(IEnums.SEEK_MODE.FULL);
        Country country = null;

        if (countries == null || countries.size() == 0 || countries.get(0).getSubdivisions().size() == 0) {
            country = CountryAccessorTest.populateUnitedStates();
            countryAccessor.persistCountry(country);
        }

        persistCart();
    }

    @Test
    public void testGetFullShoppingCart()
    {
        Long cartId = persistCart();
        Cart cart = accessor.getShoppingCart(cartId, IEnums.SEEK_MODE.FULL);

        Assert.assertNotNull(cart);
        Assert.assertTrue(cart.getCartItems() != null && cart.getCartItems().size() == 2);
    }

    @Test
    public void testGetFullShoppingCartByUser()
    {
        User user = UserAccessorTest.createUser();

        Cart cart = createCart();

        user.setShoppingCart(cart);

        try {
            Assert.assertTrue(userAccessor.createUser(user));
        } catch(Exception e)
        {

        }

        user = userAccessor.getUser(user.getUserId(), IEnums.SEEK_MODE.FULL);

        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getShoppingCart());
        Assert.assertTrue(user.getShoppingCart().getCartItems().size() > 0);
    }

    @Test
    public void persistCartWithJointCountry()
    {
        Cart cart = createCart();

        Address billingAddress = cart.getBillingAddress();
        Address shippingAddress = cart.getShippingAddress();

        Long billingAddressId = billingAddress.getCountry().getPrimaryKeyId();
        Long shippingAddressId = shippingAddress.getCountry().getPrimaryKeyId();

        Assert.assertTrue(accessor.persistCart(cart));

        cart = accessor.getShoppingCart(cart.getCartId(), IEnums.SEEK_MODE.FULL);

        billingAddress = cart.getBillingAddress();
        shippingAddress = cart.getShippingAddress();

        Assert.assertEquals(billingAddressId, billingAddress.getCountry().getPrimaryKeyId());
        Assert.assertEquals(shippingAddressId, shippingAddress.getCountry().getPrimaryKeyId());
    }

    @Test
    public void addItemToCartTest()
    {
        Long cartId = persistCart();
        CartItem cartItem = createCartItem();

        //Assert.assertTrue(accessor.addItemToCart(cartId, cartItem));

        Cart cart = accessor.getShoppingCart(cartId, IEnums.SEEK_MODE.FULL);

        Assert.assertTrue(cart.getCartItems().size() == 3);
    }

    @Test
    public void removeItemFromCartTest()
    {
        Long cartId = persistCart();
        Cart cart = accessor.getShoppingCart(cartId, IEnums.SEEK_MODE.FULL);

        CartItem itemToRemove = cart.getCartItems().get(0);

        Assert.assertTrue(accessor.removeItemFromCart(cartId, itemToRemove.getCartItemId()));

        cart = accessor.getShoppingCart(cartId, IEnums.SEEK_MODE.FULL);

        Assert.assertTrue(cart.getCartItems().size() == 1);
    }

    //Rearchitecturing of how promo codes work made this invalid.
    /*@Test
    public void addPromoCode()
    {
        Long cartId = persistCart();
        Long secondCartId = persistCart();

        //Start off by testing relationships between promocodes.  There isn't a way to individually persist a promo
        //code by design.  However, new ones can be added to carts.  Persist one, change it, then repersist it on a
        //different cart to ensure that it isn't going to be different.
        Cart promoCart = accessor.getShoppingCart(secondCartId, IEnums.SEEK_MODE.FULL);
        PromoCode oldCode = promoCart.getAppliedPromoCodes().get(0);
        PromoCode newCode = oldCode;
        newCode.setPromoCode(UUID.randomUUID().toString());

        Assert.assertTrue(promoCodeAccessor.addPromoCode(cartId, newCode));

        Cart newPromoCart = accessor.getShoppingCart(cartId, IEnums.SEEK_MODE.FULL);

        int indexOfCheckedCode = newPromoCart.getAppliedPromoCodes().indexOf(newCode);
        Assert.assertTrue(indexOfCheckedCode > -1);
        PromoCode checkCode = newPromoCart.getAppliedPromoCodes().get(indexOfCheckedCode);

        Assert.assertTrue(checkCode.getPromoCode().equals(newCode.getPromoCode()));

        //Now test the persistence of a new code
        PromoCode code = createPromoCode();

        Assert.assertTrue(promoCodeAccessor.addPromoCode(cartId, code));

        newPromoCart = accessor.getShoppingCart(cartId, IEnums.SEEK_MODE.FULL);

        Boolean test = false;

        for (PromoCode forPromo : newPromoCart.getAppliedPromoCodes())
        {
            if (forPromo.getPromoCode().equals(code.getPromoCode()))
                test = true;
        }

        Assert.assertTrue(test);
    }*/

    @Test
    public void updatePromoCode()
    {
        PromoCode promoCode = createPromoCode();

        Long cartId = persistCart();

        Cart cart = accessor.getShoppingCart(cartId, IEnums.SEEK_MODE.FULL);

        PromoCode codeToUpdate = cart.getAppliedPromoCodes().get(0);

        promoCode.setPromoCodeId(codeToUpdate.getPromoCodeId());

        promoCodeAccessor.updatePromoCode(promoCode);

        cart = accessor.getShoppingCart(cartId, IEnums.SEEK_MODE.FULL);

        Boolean test = false;

        for (PromoCode code : cart.getAppliedPromoCodes())
        {
            if (code.getPromoCode().equals(promoCode.getPromoCode()))
                test = true;
        }

        Assert.assertTrue(test);
    }

    //Rearchitecturing of how promo codes work made this invalid.
    /*@Test
    public void removePromoCode()
    {
        Long cartId = persistCart();
        Cart cart = accessor.getShoppingCart(cartId, IEnums.SEEK_MODE.FULL);

        PromoCode promoCodeToRemove = cart.getAppliedPromoCodes().get(0);

        Assert.assertTrue(promoCodeAccessor.removePromoCode(cartId, promoCodeToRemove.getPromoCodeId()));

        cart = accessor.getShoppingCart(cartId, IEnums.SEEK_MODE.FULL);

        Assert.assertTrue(cart.getAppliedPromoCodes().size() == 1);

    }*/

    @Test
    public void refreshGrandTotalGeneralUpdateTest()
    {
        Long cartId = persistCart();
        Assert.assertTrue(accessor.refreshGrandTotal(cartId, false, false) != null);

        Cart newCart = accessor.getShoppingCart(cartId, IEnums.SEEK_MODE.FULL);

        Assert.assertTrue(MoneyUtil.bigDecimalToMoney(newCart.getCurrencyUnit(), newCart.getGrandTotal())
                .isGreaterThan(Money.parse("USD 0.00")));
    }

    @Test
    public void refreshGrandTotalSubtotalTest()
    {
        Cart cart = createBareBonesCart();

        Assert.assertTrue(accessor.persistCart(cart));
        Assert.assertTrue(accessor.refreshGrandTotal(cart.getCartId(), false, false) != null);

        cart = accessor.getShoppingCart(cart.getCartId(), IEnums.SEEK_MODE.FULL);

        Assert.assertTrue(MoneyUtil.bigDecimalToMoney(cart.getCurrencyUnit(), cart.getGrandTotal()).
                isEqual(Money.parse("USD 30.00")));
        Assert.assertTrue(MoneyUtil.bigDecimalToMoney(cart.getCurrencyUnit(), cart.getSubTotal()).
                isEqual(Money.parse("USD 30.00")));

    }

    @Test
    public void refreshGrandTotalShippingTest()
    {
        Cart cart = createBareBonesCart();
        cart.setShipping(Money.parse("USD 10.00").getAmount());

        Assert.assertTrue(accessor.persistCart(cart));
        Assert.assertTrue(accessor.refreshGrandTotal(cart.getCartId(), false, false) != null);

        cart = accessor.getShoppingCart(cart.getCartId(), IEnums.SEEK_MODE.FULL);

        Assert.assertTrue(MoneyUtil.bigDecimalToMoney(cart.getCurrencyUnit(), cart.getGrandTotal()).
                isEqual(Money.parse("USD 40.00")));
        Assert.assertTrue(MoneyUtil.bigDecimalToMoney(cart.getCurrencyUnit(), cart.getShipping()).
                isEqual(Money.parse("USD 10.00")));
    }

    @Test
    public void refreshGrandTotalTaxTest()
    {
        Cart cart = createBareBonesCart();
        cart.setTax(Money.parse("USD 10.00").getAmount());

        Assert.assertTrue(accessor.persistCart(cart));
        Assert.assertTrue(accessor.refreshGrandTotal(cart.getCartId(), false, false) != null);

        cart = accessor.getShoppingCart(cart.getCartId(), IEnums.SEEK_MODE.FULL);

        Assert.assertTrue(MoneyUtil.bigDecimalToMoney(cart.getCurrencyUnit(), cart.getGrandTotal()).
                isEqual(Money.parse("USD 40.00")));
        Assert.assertTrue(MoneyUtil.bigDecimalToMoney(cart.getCurrencyUnit(), cart.getTax()).
                isEqual(Money.parse("USD 10.00")));
    }

    @Test
    public void testUpdateCartAddresses()
    {
        Cart cart = createBareBonesCart();
        accessor.persistCart(cart);

        Address address = createAddress();

        cart.setBillingAddress(address);
        cart.setShippingAddress(address);

        accessor.updateCart(cart);

        cart = accessor.getShoppingCart(cart.getCartId(), IEnums.SEEK_MODE.FULL);

        Assert.assertNotNull(cart.getShippingAddress());
        Assert.assertNotNull(cart.getBillingAddress());
    }


    private Long persistCart()
    {
        Cart cart = createCart();
        accessor.persistCart(cart);

        return cart.getCartId();
    }

    private Cart createBareBonesCart()
    {
        Cart rval = new Cart();

        rval.setCartItems(new ArrayList<CartItem>());
        rval.getCartItems().add(createCartItem());
        rval.getCartItems().add(createCartItem());
        rval.getCartItems().add(createCartItem());

        return rval;
    }

    public Cart createCart()
    {
        Cart cart = new Cart();
        cart.setTax(Money.parse("USD 5.00").getAmount());
        cart.setShipping(Money.parse("USD 3.00").getAmount());
        cart.setVendorCartId(UUID.randomUUID().toString());
        cart.setAdditionalDollarDiscount(Money.parse("USD 1.00").getAmount());
        cart.setAdditionalPercentDiscount(1);
        cart.setFreeShipping(false);

        List<PromoCode> promoCodes = new ArrayList<>();
        promoCodes.add(createPromoCode());

        cart.setAppliedPromoCodes(promoCodes);
        cart.setBillingAddress(createAddress());
        cart.setShippingAddress(createAddress());

        List<CartItem> items = new ArrayList<>();
        items.add(createCartItem());
        items.add(createCartItem());

        cart.setCartItems(items);

        cart.setPaymentDetails(createPaymentDetails());

        return cart;
    }

    public PromoCode createPromoCode()
    {
        PromoCode rval = new PromoCode();

        rval.setPromoCode(UUID.randomUUID().toString());
        rval.setDollarDiscount(Money.parse("USD 1.00").getAmount());
        rval.setPercentDiscount(10);
        rval.setBlanketCategory(PromoCode.DISCOUNT_CATEGORY.GRAND_TOTAL.toString());
        rval.setAvailableUses(1);
        rval.setActive(true);

        return rval;
    }

    public CartItem createCartItem()
    {
        Item item = ItemAccessorTest.createTestItem();

        CartItem cartItem = new CartItem();
        cartItem.setActualPrice(Money.parse("USD 10.00").getAmount());
        cartItem.setCartItemReference(item);
        //cartItem.setEstimatedPrice(Money.parse("USD 5.00"));
        cartItem.setQuantity(1);

        return cartItem;
    }


    public Address createAddress()
    {
        Address address = new Address();

        address.setAddressLine1("Test Line 1");
        address.setAddressLine2("Test Line 2");
        address.setAddressType(Address.ADDRESS_TYPE.BILLING);
        address.setCity("New York");
        address.setPostalCode("10005");

        List<String> countrySearchParams = new ArrayList<>();
        countrySearchParams.add("US");

        List<Country> country = countryAccessor.getCountry(countrySearchParams, IEnums.COUNTRY_REQUEST_TYPE.ALPHA_2, IEnums.SEEK_MODE.FULL);

        address.setCountry(country.get(0));
        address.setSubdivision(country.get(0).getSubdivisions().get(0));

        return address;
    }

    public PaymentDetails createPaymentDetails()
    {
        PaymentDetails rval = new PaymentDetails();

        rval.setCreated(new Date());
        rval.setLastCCDigits("1234");
        rval.setPaymentToken(UUID.randomUUID().toString());
        rval.setPaymentType("CC");

        return rval;
    }

    public List<PromoCode> createPromoCodes()
    {
        List<PromoCode> rval = new ArrayList<>();

        PromoCode code = new PromoCode();
        code.setActive(true);
        code.setAvailableUses(5);
        code.setBlanketCategory(PromoCode.DISCOUNT_CATEGORY.GRAND_TOTAL.toString());
        code.setPercentDiscount(5);
        code.setDollarDiscount(Money.parse("USD 5.00").getAmount());
        code.setPromoCode(UUID.randomUUID().toString());

        rval.add(code);

        code = new PromoCode();
        code.setActive(true);
        code.setAvailableUses(1);
        code.setBlanketCategory(PromoCode.DISCOUNT_CATEGORY.GRAND_TOTAL.toString());
        code.setPercentDiscount(1);
        code.setDollarDiscount(Money.parse("USD 2.00").getAmount());
        code.setPromoCode(UUID.randomUUID().toString());

        rval.add(code);

        return rval;

    }

    //Way of force updating the cart in the DB with code.  Shouldn't be an actual test.
    /*@Test
    public void forceUpdateCartSubtotal()
    {
        Cart cart = accessor.getShoppingCart(15l, IEnums.SEEK_MODE.USER_CART_PAYMENT_DETAILS);
        cart.setSubTotal(Money.parse("USD 500.00"));

        accessor.updateCart(cart);
    }*/
}
