package com.troveup.brooklyn.tests.sdk;

import com.braintreegateway.Customer;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.troveup.brooklyn.orm.cart.datanucleus.CartAccessor;
import com.troveup.brooklyn.orm.cart.model.Cart;
import com.troveup.brooklyn.orm.cart.model.CartItem;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.countries.interfaces.ICountryAccessor;
import com.troveup.brooklyn.orm.countries.model.Subdivision;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.user.interfaces.IUserAccessor;
import com.troveup.brooklyn.orm.user.model.Address;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.payment.braintree.api.BraintreeApi;
import com.troveup.brooklyn.spring.social.model.UserDetails;
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
import java.util.List;
import java.util.UUID;

/**
 * Created by tim on 5/28/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SDKConfig.class, PersistenceConfig.class})
public class BraintreeTests
{
    @Autowired
    ICountryAccessor countryAccessor;

    @Autowired
    IUserAccessor userAccessor;

    @Autowired
    BraintreeApi braintreeApi;

    @Autowired
    CartAccessor cartAccessor;

    @Test
    public void testGetClientToken()
    {
        User user = new User();
        user.setFirstName("test");
        user.setLastName("user");
        user.setUsername(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString());
        user.setPassword("testpass");
        user.setRole(UserDetails.Role.ROLE_USER);

        try {
            userAccessor.createUser(user);
        } catch (Exception e)
        {
            //Roll with it
        }

        String response = braintreeApi.getClientToken();

        Assert.assertNotNull(response);
        Assert.assertTrue(response.length() > 0);

    }

    @Test
    public void testPersistNonce()
    {
        Cart cart = new Cart();

        Item item = new Item();
        item.setItemParent(null);
        item.setBaseItem(true);
        item.setBaseItemReference(null);
        item.setItemName(UUID.randomUUID().toString());
        item.setItemDescription("A test new cool item!");
        item.setItemOwner(null);
        item.setBaseItem(true);

        CartItem cartItem = new CartItem.Builder(item, 1, 10f, null, null).build();
        cartItem.setActualPrice(Money.parse("USD 10.00").getAmount());
        List<CartItem> cartItemContainer = new ArrayList<>();
        cartItemContainer.add(cartItem);

        Subdivision subdivision = new Subdivision();
        subdivision.setCode("US-NY");

        Address address = new Address();
        address.setAddressLine1("9 Murray Street");
        address.setCity("New York");
        address.setSubdivision(subdivision);
        address.setPostalCode("10002");

        cart.setShippingAddress(address);
        cart.setCartItems(cartItemContainer);

        User user = new User();
        user.setFirstName("test");
        user.setLastName("user");
        user.setUsername(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString());
        user.setPassword("testpass");
        user.setRole(UserDetails.Role.ROLE_USER);

        try {
            userAccessor.createUser(user);
        }
        catch (Exception e)
        {
            //Do nothing
        }

        userAccessor.addShoppingCartToUser(user.getUserId(), cart);
        String paymentNonce = "ABCD";
        Assert.assertTrue(braintreeApi.persistPaymentNonce(user.getUserId(), paymentNonce));

        user = userAccessor.getUser(user.getUserId(), IEnums.SEEK_MODE.FULL);

        Assert.assertTrue(user.getShoppingCart().getPaymentDetails().getPaymentToken().equals(paymentNonce));
    }

    @Test
    public void testProcessPayment()
    {
        Cart cart = new Cart();

        Item item = new Item();
        item.setItemParent(null);
        item.setBaseItem(true);
        item.setBaseItemReference(null);
        item.setItemName(UUID.randomUUID().toString());
        item.setItemDescription("A test new cool item!");
        item.setItemOwner(null);
        item.setBaseItem(true);

        CartItem cartItem = new CartItem.Builder(item, 1, 10f, null, null).build();
        cartItem.setActualPrice(Money.parse("USD 10.00").getAmount());
        List<CartItem> cartItemContainer = new ArrayList<>();
        cartItemContainer.add(cartItem);

        Subdivision subdivision = new Subdivision();
        subdivision.setCode("US-NY");

        Address address = new Address();
        address.setAddressLine1("9 Murray Street");
        address.setCity("New York");
        address.setSubdivision(subdivision);
        address.setPostalCode("10002");

        cart.setShippingAddress(address);
        cart.setCartItems(cartItemContainer);

        User user = new User();
        user.setFirstName("test");
        user.setLastName("user");
        user.setUsername(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString());
        user.setPassword("testpass");
        user.setRole(UserDetails.Role.ROLE_USER);

        try {
            userAccessor.createUser(user);
        }
        catch (Exception e)
        {
            //Do nothing
        }

        userAccessor.addShoppingCartToUser(user.getUserId(), cart);
        String paymentNonce = "fake-valid-nonce";
        Assert.assertTrue(braintreeApi.persistPaymentNonce(user.getUserId(), paymentNonce));

        user = userAccessor.getUser(user.getUserId(), IEnums.SEEK_MODE.FULL);

        cartAccessor.refreshGrandTotal(user.getShoppingCart().getCartId(), false, false);

        Result<Transaction> result = braintreeApi.processTransaction(user.getUserId());

        Assert.assertNotNull(result);
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void voidTransactionTest()
    {
        Cart cart = new Cart();

        Item item = new Item();
        item.setItemParent(null);
        item.setBaseItem(true);
        item.setBaseItemReference(null);
        item.setItemName(UUID.randomUUID().toString());
        item.setItemDescription("A test new cool item!");
        item.setItemOwner(null);
        item.setBaseItem(true);

        CartItem cartItem = new CartItem.Builder(item, 1, 10f, null, null).build();
        cartItem.setActualPrice(Money.parse("USD 10.00").getAmount());
        List<CartItem> cartItemContainer = new ArrayList<>();
        cartItemContainer.add(cartItem);

        Subdivision subdivision = new Subdivision();
        subdivision.setCode("US-NY");

        Address address = new Address();
        address.setAddressLine1("9 Murray Street");
        address.setCity("New York");
        address.setSubdivision(subdivision);
        address.setPostalCode("10002");

        cart.setShippingAddress(address);
        cart.setCartItems(cartItemContainer);

        User user = new User();
        user.setFirstName("test");
        user.setLastName("user");
        user.setUsername(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString());
        user.setPassword("testpass");
        user.setRole(UserDetails.Role.ROLE_USER);

        try {
            userAccessor.createUser(user);
        }
        catch (Exception e)
        {
            //Do nothing
        }

        userAccessor.addShoppingCartToUser(user.getUserId(), cart);
        String paymentNonce = "fake-valid-nonce";
        Assert.assertTrue(braintreeApi.persistPaymentNonce(user.getUserId(), paymentNonce));

        user = userAccessor.getUser(user.getUserId(), IEnums.SEEK_MODE.FULL);

        cartAccessor.refreshGrandTotal(user.getShoppingCart().getCartId(), false, false);

        Result<Transaction> result = braintreeApi.processTransaction(user.getUserId());

        Assert.assertNotNull(result);
        Assert.assertTrue(result.isSuccess());

        Result<Transaction> refund = braintreeApi.voidTransaction(result.getTarget().getId());

        Assert.assertTrue(refund.isSuccess());
    }

    @Test
    public void addUserToVault()
    {
        User user = userAccessor.getUser(1l, IEnums.SEEK_MODE.USER_CART_FULL);

        Result<Customer> rval = braintreeApi.createVaultCustomer(user);

        Assert.assertTrue(rval.isSuccess());
    }

    @Test
    public void addPaymentMethodToUser()
    {
        User user = userAccessor.getUser(1l, IEnums.SEEK_MODE.USER_CART_FULL);

        Assert.assertTrue((braintreeApi.addPaymentMethodToVaultCustomer(user.getBraintreeUserId(), "fake-valid-nonce")).isSuccess());
    }

    /*@Test
    public void fullIntegrationTest()
    {
        Cart cart = new Cart();

        Item item = new Item();
        item.setItemParent(null);
        item.setBaseItem(true);
        item.setBaseItemReference(null);
        item.setItemName(UUID.randomUUID().toString());
        item.setItemDescription("A test new cool item!");
        item.setItemOwner(null);
        item.setBaseItem(true);

        CartItem cartItem = new CartItem.Builder(item, 1, 10f, "USD").build();
        cartItem.setActualPrice(Money.parse("USD 10.00"));
        List<CartItem> cartItemContainer = new ArrayList<>();
        cartItemContainer.add(cartItem);

        Subdivision subdivision = new Subdivision();
        subdivision.setCode("US-NY");

        Address address = new Address();
        address.setAddressLine1("9 Murray Street");
        address.setCity("New York");
        address.setSubdivision(subdivision);
        address.setPostalCode("10002");

        cart.setShippingAddress(address);
        cart.setCartItems(cartItemContainer);

        User user = new User();
        user.setFirstName("test");
        user.setLastName("user");
        user.setUsername(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString());
        user.setPassword("testpass");
        user.setRole(UserDetails.Role.ROLE_USER);

        try {
            userAccessor.createUser(user);
        }
        catch (Exception e)
        {
            //Do nothing
        }

        userAccessor.addShoppingCartToUser(user.getUserId(), cart);
        String paymentNonce = braintreeApi.getClientToken(user.getUserId());

        braintreeApi.persistPaymentNonce(user.getUserId(), paymentNonce);

        user = userAccessor.getUser(user.getUserId(), IEnums.SEEK_MODE.FULL);

        Result<Transaction> result = braintreeApi.processTransaction(user.getUserId(), "10.00");

        Assert.assertNotNull(result);
        Assert.assertTrue(result.isSuccess());
    }*/
}
