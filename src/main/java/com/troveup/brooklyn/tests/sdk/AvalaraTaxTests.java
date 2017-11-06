package com.troveup.brooklyn.tests.sdk;

import com.troveup.brooklyn.orm.cart.model.Cart;
import com.troveup.brooklyn.orm.cart.model.CartItem;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.countries.interfaces.ICountryAccessor;
import com.troveup.brooklyn.orm.countries.model.Country;
import com.troveup.brooklyn.orm.countries.model.Subdivision;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.user.interfaces.IUserAccessor;
import com.troveup.brooklyn.orm.user.model.Address;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.tax.avalara.model.AvaGetTaxRequest;
import com.troveup.brooklyn.sdk.tax.avalara.model.AvaGetTaxResult;
import com.troveup.brooklyn.sdk.tax.interfaces.ITaxHandler;
import com.troveup.brooklyn.spring.social.model.UserDetails;
import com.troveup.brooklyn.tests.orm.CountryAccessorTest;
import com.troveup.brooklyn.util.exception.UsernameAlreadyInUseException;
import com.troveup.config.PersistenceConfig;
import com.troveup.config.SDKConfig;
import junit.framework.Assert;
import org.joda.money.Money;
import org.junit.Before;
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
 * Created by tim on 5/27/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SDKConfig.class, PersistenceConfig.class})
public class AvalaraTaxTests
{
    @Autowired
    ICountryAccessor countryAccessor;

    @Autowired
    IUserAccessor userAccessor;

    @Autowired
    ITaxHandler avaTaxHandler;

    @Before
    public void setup()
    {
        List<Country> countries = countryAccessor.getAllCountries(IEnums.SEEK_MODE.FULL);
        Country country = null;

        if (countries == null || countries.size() == 0 || countries.get(0).getSubdivisions().size() == 0) {
            country = CountryAccessorTest.populateUnitedStates();
            countryAccessor.persistCountry(country);
        }
    }

    @Test
    public void getTaxTest()
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
        } catch (UsernameAlreadyInUseException e)
        {
            //do nothing
        }

        userAccessor.addShoppingCartToUser(user.getUserId(), cart);

        AvaGetTaxResult result = (AvaGetTaxResult) avaTaxHandler.getTaxes(cart.getCartId(), false);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getTotalTax() > 0);
    }
}
