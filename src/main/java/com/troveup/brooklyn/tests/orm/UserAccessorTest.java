package com.troveup.brooklyn.tests.orm;

import com.troveup.brooklyn.orm.cart.interfaces.ICartAccessor;
import com.troveup.brooklyn.orm.cart.model.*;
import com.troveup.brooklyn.orm.countries.interfaces.ICountryAccessor;
import com.troveup.brooklyn.orm.countries.model.Country;
import com.troveup.brooklyn.orm.item.interfaces.IItemAccessor;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.item.model.ItemAttribute;
import com.troveup.brooklyn.orm.order.interfaces.IOrderAccessor;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.user.datanucleus.UserAccessor;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.user.model.Address;
import com.troveup.brooklyn.orm.user.model.GroupedItem;
import com.troveup.brooklyn.orm.user.model.SocialConnection;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.print.interfaces.IPrintSupplier;
import com.troveup.brooklyn.spring.social.model.UserDetails;
import com.troveup.brooklyn.util.exception.UsernameAlreadyInUseException;
import com.troveup.config.PersistenceConfig;
import com.troveup.config.SDKConfig;
import com.troveup.config.SecurityConfig;
import junit.framework.Assert;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Security;
import java.util.*;

/**
 * Created by tim on 4/16/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, SDKConfig.class})
public class UserAccessorTest
{

    /**
     * @Note: To set the log4j debug properties file, specify
     * -Dlog4j.configuration=file:/Users/tim/Workspace/Java/Trove/brooklyn/src/main/java/com/troveup/brooklyn/tests/properties/log4j-{level}.properties
     * within the JVM flags before run.  For IntelliJ, this is within the run configuration for the specific JUnit
     * class configuration.
     */

    @Autowired
    UserAccessor accessor;

    @Autowired
    IItemAccessor itemAccessor;

    @Autowired
    ICartAccessor cartAccessor;

    @Autowired
    ICountryAccessor countryAccessor;

    @Autowired
    IPrintSupplier printSupplier;

    @Autowired
    IOrderAccessor orderAccessor;

    String userEmail;

    BCryptPasswordEncoder encoder;

    @Before
    public void setUp() throws UsernameAlreadyInUseException
    {
        encoder = new BCryptPasswordEncoder(10);
    }

    @Test
    public void testCreateUser() throws UsernameAlreadyInUseException {
        User user = new User();
        user.setFirstName("Tim");
        user.setLastName("Growney");
        user.setPassword("testing123");
        user.setRole(UserDetails.Role.ROLE_USER);
        this.userEmail = UUID.randomUUID().toString();
        user.setEmail(userEmail);
        user.setUsername(userEmail);

        Assert.assertTrue(accessor.createUser(user));
    }

    @Test
    public void testCreateUserWithCollision()
    {
        try {
            User user = new User();
            user.setFirstName("Tim");
            user.setLastName("Growney");
            user.setPassword("testing123");
            user.setRole(UserDetails.Role.ROLE_USER);
            this.userEmail = UUID.randomUUID().toString();
            user.setEmail(userEmail);
            user.setUsername(userEmail);

            Assert.assertTrue(accessor.createUser(user));

            user = new User();
            user.setFirstName("Tim");
            user.setLastName("Growney");
            user.setPassword("testing123");
            user.setRole(UserDetails.Role.ROLE_USER);
            user.setEmail(userEmail);
            user.setUsername(userEmail);

            Assert.assertFalse(accessor.createUser(user));
        } catch (UsernameAlreadyInUseException e)
        {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testFetchGroupings()
    {
        //Create the encasing user
        User user = createUser();
        user.setTrovedItems(new ArrayList<GroupedItem>());
        String userEmail = user.getEmail();

        //Create the overall cart
        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<CartItem>());
        cart.setCartAttributes(new ArrayList<CartAttribute>());
        cart.setShipping(Money.parse("USD 10.00").getAmount());
        cart.setGrandTotal(Money.parse("USD 10.00").getAmount());
        cart.setCartOwner(user);

        //Set up a sample payment detail
        PaymentDetails details = new PaymentDetails();
        details.setPaymentToken(UUID.randomUUID().toString());
        details.setPaymentType("Sweet new type");
        cart.setPaymentDetails(details);

        CartAttribute cartAttribute = new CartAttribute();
        cartAttribute.setAttributeName("Test Attribute Name");
        cartAttribute.setAttributeValue("Test Attribute Value");
        cart.getCartAttributes().add(cartAttribute);

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(5);
        cartItem.setEstimatedPrice("20.00");
        cartItem.setAttributes(new ArrayList<CartItemAttribute>());

        CartItemAttribute cartItemAttribute = new CartItemAttribute();
        cartItemAttribute.setAttributeName("Test CIA Name");
        cartItemAttribute.setAttributeValue("Test CIA Value");
        cartItem.getAttributes().add(cartItemAttribute);

        Item item = new Item();
        item.setItemFileName("Test item file name");
        item.setItemName("Test Item Name");
        item.setItemDescription("Test Item Description");
        item.setItemAttributes(new ArrayList<ItemAttribute>());

        ItemAttribute itemAttribute = new ItemAttribute();
        itemAttribute.setAttributeName("Test Item Attribute Name");
        itemAttribute.setAttributeValue("Test Item Attribute Value");
        item.getItemAttributes().add(itemAttribute);
        cartItem.setCartItemReference(item);
        cart.getCartItems().add(cartItem);
        user.setShoppingCart(cart);
        user.getTrovedItems().add(new GroupedItem(user ,item));

        //Set up the connection test data
        SocialConnection connection = new SocialConnection();
        connection.setDisplayName("Test Display Name");
        connection.setAccessToken("Access token!");
        user.setSocialConnections(new ArrayList<SocialConnection>());
        user.getSocialConnections().add(connection);

        try {
            Assert.assertTrue(accessor.createUser(user));
        } catch (Exception e)
        {
            //Eat it, should never get here
        }

        User userTest = accessor.getUserByEmail(userEmail, IEnums.SEEK_MODE.USER_CART_QUICK);

        accessor.addShoppingCartToUser(user.getUserId(), cart);

        Assert.assertNull(userTest.getSocialConnections());
        Assert.assertNull(userTest.getUserCollections());
        Assert.assertNull(userTest.getTrovedItems());
        Assert.assertNotNull(userTest.getShoppingCart());
        Assert.assertNotNull(userTest.getShoppingCart().getCartId());
        Assert.assertNull(userTest.getShoppingCart().getCartItems());

        userTest = accessor.getUserByEmail(userEmail, IEnums.SEEK_MODE.USER_CART_FULL);
        Assert.assertNull(userTest.getSocialConnections());
        Assert.assertNull(userTest.getUserCollections());
        Assert.assertNull(userTest.getTrovedItems());
        Assert.assertNotNull(userTest.getShoppingCart());
        Assert.assertNotNull(userTest.getShoppingCart().getCartId());
        Assert.assertNotNull(userTest.getShoppingCart().getCartItems());
        Assert.assertTrue(userTest.getShoppingCart().getCartItems().size() > 0);
        Assert.assertTrue(userTest.getShoppingCart().getCartItems().get(0).getQuantity() == 5);
        Assert.assertTrue(userTest.getShoppingCart().getCartItems().get(0).getAttributes().size() > 0);
        Assert.assertNotNull(userTest.getShoppingCart().getCartItems().get(0).getCartItemReference());
        Assert.assertTrue(userTest.getShoppingCart().getCartItems().get(0).getCartItemReference().
                getItemAttributes().size() > 0);


        userTest = accessor.getUserByEmail(userEmail, IEnums.SEEK_MODE.USER_CART_PAYMENT_DETAILS);
        Assert.assertNull(userTest.getSocialConnections());
        Assert.assertNull(userTest.getUserCollections());
        Assert.assertNull(userTest.getTrovedItems());
        Assert.assertNull(userTest.getShoppingCart().getCartItems());
        Assert.assertNull(userTest.getShoppingCart().getCartAttributes());
        Assert.assertNotNull(userTest.getShoppingCart().getPaymentDetails());
        Assert.assertNotNull(userTest.getShoppingCart().getGrandTotal());

        userTest = accessor.getUserByEmail(userEmail, IEnums.SEEK_MODE.USER_TROVED);
        Assert.assertNull(userTest.getSocialConnections());
        Assert.assertNull(userTest.getUserCollections());
        Assert.assertNull(userTest.getShoppingCart());
        Assert.assertNotNull(userTest.getTrovedItems());
        Assert.assertTrue(userTest.getTrovedItems().size() > 0);
        Assert.assertNotNull(userTest.getTrovedItems().get(0).getGroupedItem().getItemName());

        userTest = accessor.getUserByEmail(userEmail, IEnums.SEEK_MODE.USER_SOCIAL_CONNECTIONS);
        Assert.assertNull(userTest.getUserCollections());
        Assert.assertNull(userTest.getShoppingCart());
        Assert.assertNull(userTest.getTrovedItems());
        Assert.assertNotNull(userTest.getSocialConnections());
        Assert.assertTrue(userTest.getSocialConnections().size() > 0);
        Assert.assertNotNull(userTest.getSocialConnections().get(0).getDisplayName());
    }

    @Test
    public void addSocialConnection()
    {
        ConnectionData data = new ConnectionData("Facebook", "tim", "dood", "http://profileurl", "http://imageurl",
                "randomAccessToken", "shhh", "sofresh", new Long(12345));


        List<String> userIdentifier = new ArrayList<>();
        userIdentifier.add(this.userEmail);
        List<User> users = accessor.getUser(userIdentifier, User.SEARCH_BY_TYPE.EMAIL, IEnums.SEEK_MODE.FULL);

        Assert.assertNotNull(users);
        Assert.assertTrue(users.size() > 0);

        Assert.assertTrue(accessor.addUserSocialConnection(data, users.get(0).getUserId()));

        users = accessor.getUser(userIdentifier, User.SEARCH_BY_TYPE.EMAIL, IEnums.SEEK_MODE.FULL);

        //.contains on list isn't using the overridden .equals on the SocialConnection objects...
        boolean test = false;
        for (SocialConnection connection : users.get(0).getSocialConnections())
        {
            if (connection.equals(connection))
                test = true;
        }

        Assert.assertTrue(test);
        //Assert.assertTrue(users.get(0).getSocialConnections().contains(data));
    }

    @Test
    public void updateSocialConnection()
    {
        String providerUserId = UUID.randomUUID().toString();

        ConnectionData data = new ConnectionData("Facebook", providerUserId, "dood", "http://profileurl", "http://imageurl",
                "randomAccessToken", "shhh", "sofresh", new Long(12345));


        List<String> userIdentifier = new ArrayList<>();
        userIdentifier.add(this.userEmail);
        List<User> users = accessor.getUser(userIdentifier, User.SEARCH_BY_TYPE.EMAIL, IEnums.SEEK_MODE.FULL);

        ConnectionData newData = new ConnectionData("Facebook", providerUserId, "newDood", "http://newprofileurl", "http://newimageurl",
                "newrandomAccessToken", "newshhh", "newsofresh", new Long(678910));

        Assert.assertTrue(accessor.addUserSocialConnection(data, users.get(0).getUserId()));
        Assert.assertTrue(accessor.updateUserSocialConnection(newData));

        users = accessor.getUser(userIdentifier, User.SEARCH_BY_TYPE.EMAIL, IEnums.SEEK_MODE.FULL);

        boolean test = false;
        for (SocialConnection connection : users.get(0).getSocialConnections())
        {
            if (connection.equals(connection) && newData.getAccessToken().equals(connection.getAccessToken()) &&
                    newData.getDisplayName().equals(connection.getDisplayName()) &&
                    newData.getExpireTime().equals(connection.getExpireTime()))
                test = true;
        }

        Assert.assertTrue(test);
    }

    @Test
    public void removeSocialConnectionByKey()
    {
        String providerUserId = UUID.randomUUID().toString();

        ConnectionData newData = new ConnectionData("Facebook", providerUserId, "otherDood", "http://otherprofileurl", "http://otherimageurl",
                "otherrandomAccessToken", "othershhh", "othersofresh", new Long(11121314));

        List<String> userIdentifier = new ArrayList<>();
        userIdentifier.add(this.userEmail);
        List<User> users = accessor.getUser(userIdentifier, User.SEARCH_BY_TYPE.EMAIL, IEnums.SEEK_MODE.FULL);

        ConnectionKey key = new ConnectionKey("Facebook", providerUserId);

        Assert.assertTrue(accessor.addUserSocialConnection(newData, users.get(0).getUserId()));
        Assert.assertTrue(accessor.removeConnection(key));

        users = accessor.getUser(userIdentifier, User.SEARCH_BY_TYPE.EMAIL, IEnums.SEEK_MODE.FULL);

        boolean test = false;
        for (SocialConnection connection : users.get(0).getSocialConnections())
        {
            if (connection.equals(connection) && newData.getAccessToken().equals(connection.getAccessToken()) &&
                    newData.getDisplayName().equals(connection.getDisplayName()) &&
                    newData.getExpireTime().equals(connection.getExpireTime()))
                test = true;
        }

        Assert.assertFalse(test);
    }

    @Test
    public void removeSocialConnectionWithUserIdSafeguard()
    {
        //Generate a unique connection
        String providerUserId = UUID.randomUUID().toString();
        ConnectionData newData = new ConnectionData("Facebook", providerUserId, "otherDood", "http://otherprofileurl", "http://otherimageurl",
                "otherrandomAccessToken", "othershhh", "othersofresh", new Long(11121314));

        SocialConnection newDataConnection = SocialConnection.getBuilder(newData).buildSocialConnection();

        //Get the user created at the start of this unit test
        List<String> userIdentifier = new ArrayList<>();
        userIdentifier.add(this.userEmail);
        List<User> users = accessor.getUser(userIdentifier, User.SEARCH_BY_TYPE.EMAIL, IEnums.SEEK_MODE.FULL);

        //Specify the connection key to match the unique connection
        ConnectionKey key = new ConnectionKey("Facebook", providerUserId);

        //Add the connection
        Assert.assertTrue(accessor.addUserSocialConnection(newData, users.get(0).getUserId()));

        //Attempt to remove it for the wrong user, make sure it wasn't removed
        Assert.assertFalse(accessor.removeConnection(key, (long) 1));

        //Double check to make sure it wasn't removed
        users = accessor.getUser(userIdentifier, User.SEARCH_BY_TYPE.EMAIL, IEnums.SEEK_MODE.FULL);
        boolean test = false;
        for (SocialConnection connection : users.get(0).getSocialConnections())
        {
            if (connection.equals(newDataConnection) && newData.getAccessToken().equals(connection.getAccessToken()) &&
                    newData.getDisplayName().equals(connection.getDisplayName()) &&
                    newData.getExpireTime().equals(connection.getExpireTime()))
                test = true;
        }
        Assert.assertTrue(test);

        //Now try to remove it for realz
        Assert.assertTrue(accessor.removeConnection(key, users.get(0).getUserId()));

        //Test to make sure it was removed
        users = accessor.getUser(userIdentifier, User.SEARCH_BY_TYPE.EMAIL, IEnums.SEEK_MODE.FULL);
        test = false;
        for (SocialConnection connection : users.get(0).getSocialConnections())
        {
            if (connection.equals(newDataConnection) && newData.getAccessToken().equals(connection.getAccessToken()) &&
                    newData.getDisplayName().equals(connection.getDisplayName()) &&
                    newData.getExpireTime().equals(connection.getExpireTime()))
                test = true;
        }
        Assert.assertFalse(test);
    }

    @Test
    public void removeConnectionByProviderIdUserId() {
        //Generate a unique connection
        String providerUserId = UUID.randomUUID().toString();
        ConnectionData newData = new ConnectionData("Facebook", providerUserId, "otherDood", "http://otherprofileurl", "http://otherimageurl",
                "otherrandomAccessToken", "othershhh", "othersofresh", new Long(11121314));

        String secondProviderUserId = UUID.randomUUID().toString();

        ConnectionData secondNewData = new ConnectionData("Facebook", secondProviderUserId, "secondOtherDood", "http://secotherprofileurl", "http://secotherimageurl",
                "secotherrandomAccessToken", "secothershhh", "secothersofresh", new Long(15161718));

        SocialConnection newDataConnection = SocialConnection.getBuilder(newData).buildSocialConnection();
        SocialConnection secondNewDataConnection = SocialConnection.getBuilder(secondNewData).buildSocialConnection();

        //Get the user created at the start of this unit test
        List<String> userIdentifier = new ArrayList<>();
        userIdentifier.add(this.userEmail);
        List<User> users = accessor.getUser(userIdentifier, User.SEARCH_BY_TYPE.EMAIL, IEnums.SEEK_MODE.FULL);

        //Add the connections
        Assert.assertTrue(accessor.addUserSocialConnection(newData, users.get(0).getUserId()));
        Assert.assertTrue(accessor.addUserSocialConnection(secondNewData, users.get(0).getUserId()));

        //Attempt to remove it for the wrong user, make sure it was removed
        Assert.assertTrue(accessor.removeConnection("Facebook", users.get(0).getUserId()));

        //Double check to make sure it was removed
        users = accessor.getUser(userIdentifier, User.SEARCH_BY_TYPE.EMAIL, IEnums.SEEK_MODE.FULL);
        boolean test = false;
        for (SocialConnection connection : users.get(0).getSocialConnections()) {
            if ((connection.equals(newDataConnection) && newData.getAccessToken().equals(connection.getAccessToken()) &&
                    newData.getDisplayName().equals(connection.getDisplayName()) &&
                    newData.getExpireTime().equals(connection.getExpireTime())) ||
                    (connection.equals(secondNewDataConnection) && secondNewData.getAccessToken().equals(connection.getAccessToken()) &&
                            secondNewData.getDisplayName().equals(connection.getDisplayName()) &&
                            secondNewData.getExpireTime().equals(connection.getExpireTime())))
                test = true;
        }
        Assert.assertFalse(test);

    }

    @Test
    public void getSocialConnectionByKey()
    {
        String providerUserId = UUID.randomUUID().toString();
        ConnectionData data = new ConnectionData("Facebook", providerUserId, "dood", "http://profileurl", "http://imageurl",
                "randomAccessToken", "shhh", "sofresh", new Long(12345));

        ConnectionKey key = new ConnectionKey("Facebook", providerUserId);

        List<String> userIdentifier = new ArrayList<>();
        userIdentifier.add(this.userEmail);
        List<User> users = accessor.getUser(userIdentifier, User.SEARCH_BY_TYPE.EMAIL, IEnums.SEEK_MODE.FULL);

        Assert.assertNotNull(users);
        Assert.assertTrue(users.size() > 0);

        Assert.assertTrue(accessor.addUserSocialConnection(data, users.get(0).getUserId()));

        SocialConnection connection = accessor.getSocialConnection(key, IEnums.SEEK_MODE.FULL);

        Assert.assertTrue(connection.equals(SocialConnection.getBuilder(data).buildSocialConnection()));

    }

    @Test
    public void getSocialConnectionByKeyUserId()
    {
        String providerUserId = UUID.randomUUID().toString();
        ConnectionData data = new ConnectionData("Facebook", providerUserId, "dood", "http://profileurl", "http://imageurl",
                "randomAccessToken", "shhh", "sofresh", new Long(12345));

        ConnectionKey key = new ConnectionKey("Facebook", providerUserId);

        List<String> userIdentifier = new ArrayList<>();
        userIdentifier.add(this.userEmail);
        List<User> users = accessor.getUser(userIdentifier, User.SEARCH_BY_TYPE.EMAIL, IEnums.SEEK_MODE.FULL);

        Assert.assertNotNull(users);
        Assert.assertTrue(users.size() > 0);

        Assert.assertTrue(accessor.addUserSocialConnection(data, users.get(0).getUserId()));

        Assert.assertNull(accessor.getSocialConnection(key, (long) 1, IEnums.SEEK_MODE.FULL));

        SocialConnection connection = accessor.getSocialConnection(key, users.get(0).getUserId(),IEnums.SEEK_MODE.FULL);

        Assert.assertNotNull(connection);
        Assert.assertTrue(connection.equals(SocialConnection.getBuilder(data).buildSocialConnection()));

    }

    @Test
    public void getSortedSocialConnectionsByProviderUserId()
    {
        String providerUserId = UUID.randomUUID().toString();
        ConnectionData data = new ConnectionData("Facebook", providerUserId, "dood", "http://profileurl", "http://imageurl",
                "randomAccessToken", "shhh", "sofresh", new Long(12345));

        ConnectionKey key = new ConnectionKey("Facebook", providerUserId);

        List<String> userIdentifier = new ArrayList<>();
        userIdentifier.add(this.userEmail);
        List<User> users = accessor.getUser(userIdentifier, User.SEARCH_BY_TYPE.EMAIL, IEnums.SEEK_MODE.FULL);

        Assert.assertNotNull(users);
        Assert.assertTrue(users.size() > 0);

        Assert.assertTrue(accessor.addUserSocialConnection(data, users.get(0).getUserId()));
        Assert.assertTrue(accessor.addUserSocialConnection(data, users.get(0).getUserId()));
        Assert.assertTrue(accessor.addUserSocialConnection(data, users.get(0).getUserId()));
        Assert.assertTrue(accessor.addUserSocialConnection(data, users.get(0).getUserId()));

        List<SocialConnection> connections = accessor.getSocialConnections("Facebook", users.get(0).getUserId(), IEnums.SEEK_MODE.FULL);

        boolean test = false;
        for (int i = 0; i < connections.size(); ++i)
        {
            //Test to see if they're out of order, should be descending
            if (i < (connections.size() - 1))
            {
                if ((connections.get(i).getCreated().after(connections.get(i + 1).getCreated())))
                    test = true;
            }
        }

        Assert.assertFalse(test);
    }

    @Test
    public void getSocialConnectionsByUserId()
    {
        String providerUserId = UUID.randomUUID().toString();
        ConnectionData data = new ConnectionData("Facebook", providerUserId, "dood", "http://profileurl", "http://imageurl",
                "randomAccessToken", "shhh", "sofresh", new Long(12345));

        ConnectionKey key = new ConnectionKey("Facebook", providerUserId);

        List<String> userIdentifier = new ArrayList<>();
        userIdentifier.add(this.userEmail);
        List<User> users = accessor.getUser(userIdentifier, User.SEARCH_BY_TYPE.EMAIL, IEnums.SEEK_MODE.FULL);

        Assert.assertNotNull(users);
        Assert.assertTrue(users.size() > 0);

        Assert.assertTrue(accessor.addUserSocialConnection(data, users.get(0).getUserId()));
        Assert.assertTrue(accessor.addUserSocialConnection(data, users.get(0).getUserId()));
        Assert.assertTrue(accessor.addUserSocialConnection(data, users.get(0).getUserId()));
        Assert.assertTrue(accessor.addUserSocialConnection(data, users.get(0).getUserId()));

        List<SocialConnection> connections = accessor.getSocialConnections(users.get(0).getUserId(), IEnums.SEEK_MODE.FULL);

        //Test to make sure all social connections that just returned were for the same userId
        boolean test = false;
        for (SocialConnection connection : connections)
        {
            if (!connection.getUser().getUserId().equals(users.get(0).getUserId()))
                test = true;
        }

        Assert.assertFalse(test);
    }

    @Test
    public void getSocialConnectionsByProviderIdUserId()
    {
        String providerUserId = UUID.randomUUID().toString();
        ConnectionData data = new ConnectionData("Facebook", providerUserId, "dood", "http://profileurl", "http://imageurl",
                "randomAccessToken", "shhh", "sofresh", new Long(12345));

        providerUserId = UUID.randomUUID().toString();
        ConnectionData dataOne = new ConnectionData("Facebook", providerUserId, "dood", "http://profileurl", "http://imageurl",
                "randomAccessToken", "shhh", "sofresh", new Long(12345));

        providerUserId = UUID.randomUUID().toString();
        ConnectionData dataTwo = new ConnectionData("MySpace", providerUserId, "dood", "http://profileurl", "http://imageurl",
                "randomAccessToken", "shhh", "sofresh", new Long(12345));

        List<String> providerUserIds = new ArrayList<>();
        providerUserIds.add(data.getProviderUserId());
        providerUserIds.add(dataOne.getProviderUserId());
        providerUserIds.add(dataTwo.getProviderUserId());

        ConnectionKey key = new ConnectionKey("Facebook", providerUserId);

        List<String> userIdentifier = new ArrayList<>();
        userIdentifier.add(this.userEmail);

        List<User> users = accessor.getUser(userIdentifier, User.SEARCH_BY_TYPE.EMAIL, IEnums.SEEK_MODE.FULL);

        User user = accessor.getUser((long) 1, IEnums.SEEK_MODE.FULL);

        Assert.assertTrue(accessor.addUserSocialConnection(data, users.get(0).getUserId()));
        Assert.assertTrue(accessor.addUserSocialConnection(dataOne, users.get(0).getUserId()));
        Assert.assertTrue(accessor.addUserSocialConnection(dataTwo, users.get(0).getUserId()));

        Assert.assertTrue(accessor.addUserSocialConnection(data, (long) 1));
        Assert.assertTrue(accessor.addUserSocialConnection(dataOne, (long) 1));
        Assert.assertTrue(accessor.addUserSocialConnection(dataTwo, (long) 1));

        List<SocialConnection> connections = accessor.getSocialConnections("Facebook", providerUserIds, users.get(0).getUserId(), IEnums.SEEK_MODE.FULL);

        //Test to make sure all social connections that just returned were for the same userId
        boolean test = false;
        for (SocialConnection connection : connections)
        {
            if ((!connection.getUser().getUserId().equals(users.get(0).getUserId())) || !connection.getProviderId().equals("Facebook"))
                test = true;

            if (!connection.getProviderUserId().equals(data.getProviderUserId()) && !connection.getProviderUserId().equals(dataOne.getProviderUserId()))
                test = true;
        }

        Assert.assertFalse(test);
    }

    @Test
    public void getUserIdsByKey()
    {
        String providerUserId = UUID.randomUUID().toString();
        ConnectionData data = new ConnectionData("Facebook", providerUserId, "dood", "http://profileurl", "http://imageurl",
                "randomAccessToken", "shhh", "sofresh", new Long(12345));

        ConnectionKey key = new ConnectionKey("Facebook", providerUserId);

        List<String> userIdentifier = new ArrayList<>();
        userIdentifier.add(this.userEmail);

        List<User> users = accessor.getUser(userIdentifier, User.SEARCH_BY_TYPE.EMAIL, IEnums.SEEK_MODE.FULL);

        Assert.assertTrue(accessor.addUserSocialConnection(data, (long) 1));
        Assert.assertTrue(accessor.addUserSocialConnection(data, (long) 2));
        Assert.assertTrue(accessor.addUserSocialConnection(data, users.get(0).getUserId()));

        Set<Long> userIdsWithConnection = accessor.getUserIds(key);

        Assert.assertTrue(userIdsWithConnection.size() == 3);

        //Test to make sure all social connections that just returned were for the same userId
        boolean test = false;
        for (Long userId : userIdsWithConnection)
        {
            Assert.assertTrue(userId.equals((long) 1) || userId.equals((long) 2) || userId.equals(users.get(0).getUserId()));
        }

        Assert.assertFalse(test);
    }

    @Test
    public void getUserIdsProviderIdProviderUserIds()
    {
        List<String> providerUserIds = new ArrayList<>();
        String providerUserId = UUID.randomUUID().toString();
        providerUserIds.add(providerUserId);

        ConnectionData data = new ConnectionData("Facebook", providerUserId, "dood", "http://profileurl", "http://imageurl",
                "randomAccessToken", "shhh", "sofresh", new Long(12345));

        providerUserId = UUID.randomUUID().toString();
        providerUserIds.add(providerUserId);

        ConnectionData newData = new ConnectionData("Facebook", providerUserId, "dood", "http://profileurl", "http://imageurl",
                "randomAccessToken", "shhh", "sofresh", new Long(12345));

        providerUserId = UUID.randomUUID().toString();
        providerUserIds.add(providerUserId);

        ConnectionData newDataTwo = new ConnectionData("Facebook", providerUserId, "dood", "http://profileurl", "http://imageurl",
                "randomAccessToken", "shhh", "sofresh", new Long(12345));

        List<String> userIdentifier = new ArrayList<>();
        userIdentifier.add(this.userEmail);

        List<User> users = accessor.getUser(userIdentifier, User.SEARCH_BY_TYPE.EMAIL, IEnums.SEEK_MODE.FULL);

        Assert.assertTrue(accessor.addUserSocialConnection(data, (long) 1));
        Assert.assertTrue(accessor.addUserSocialConnection(data, (long) 2));
        Assert.assertTrue(accessor.addUserSocialConnection(data, users.get(0).getUserId()));
        Assert.assertTrue(accessor.addUserSocialConnection(newData, (long) 3));
        Assert.assertTrue(accessor.addUserSocialConnection(newDataTwo, (long) 4));

        Set<Long> userIdsWithConnection = accessor.getUserIds("Facebook", providerUserIds);

        Assert.assertTrue(userIdsWithConnection.size() == 5);

        //Test to make sure all social connections that just returned were for the same userId
        boolean test = false;
        for (Long userId : userIdsWithConnection)
        {
            Assert.assertTrue(userId.equals((long) 1) || userId.equals((long) 2) || userId.equals(users.get(0).getUserId())
            || userId.equals((long) 3) || userId.equals((long) 4));
        }

        Assert.assertFalse(test);
    }

    @Test
    public void addItemToTroveTest()
    {
        //TODO: Write this so that it doesn't depend on values being in the DB
        Long itemId = itemAccessor.getItemDetached((long) 1, IEnums.SEEK_MODE.FULL).getItemId();

        Assert.assertTrue(accessor.addItemToTrove(userEmail, itemId, User.SEARCH_BY_TYPE.EMAIL));

        User user = accessor.getUserByEmail(userEmail, IEnums.SEEK_MODE.FULL);

        Assert.assertNotNull(user);

        boolean test = false;

        for (GroupedItem item : user.getTrovedItems())
        {
            if (item.getGroupedItem().getItemId() == itemId)
                test = true;
        }

        Assert.assertTrue(test);
    }

    @Test
    public void testAddCollectionToUser()
    {
        com.troveup.brooklyn.orm.user.model.Collection collection = createCollection();

        Assert.assertTrue(accessor.addCollectionToUser(userEmail, collection) != null);

        User user = accessor.getUserByEmail(userEmail, IEnums.SEEK_MODE.FULL);

        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getUserCollections());

        boolean test = false;
        for (com.troveup.brooklyn.orm.user.model.Collection individual : user.getUserCollections())
        {
            if (individual.getCollectionName().equals(collection.getCollectionName()))
                test = true;
        }

        Assert.assertTrue(test);
    }

    @Test
    public void testRemoveCollectionFromUser()
    {
        com.troveup.brooklyn.orm.user.model.Collection collection = createCollection();

        Assert.assertTrue(accessor.addCollectionToUser(userEmail, collection) != null);

        User user = accessor.getUserByEmail(userEmail, IEnums.SEEK_MODE.FULL);

        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getUserCollections());

        boolean test = false;
        for (com.troveup.brooklyn.orm.user.model.Collection individual : user.getUserCollections())
        {
            if (individual.getCollectionName().equals(collection.getCollectionName()))
                test = true;
        }

        Assert.assertTrue(test);

        collection = user.getUserCollections().get(0);

        Assert.assertFalse(accessor.removeCollectionFromUser(userEmail, collection.getCollectionId() + 1));
        Assert.assertTrue(accessor.removeCollectionFromUser(userEmail, collection.getCollectionId()));

        user = accessor.getUserByEmail(userEmail, IEnums.SEEK_MODE.FULL);

        test = false;
        for (com.troveup.brooklyn.orm.user.model.Collection individual : user.getUserCollections())
        {
            if (individual.getCollectionName().equals(collection.getCollectionName()))
                test = true;
        }

        Assert.assertFalse(test);
    }

    @Test
    public void testAddItemToCollection()
    {
        com.troveup.brooklyn.orm.user.model.Collection collection = createCollection();

        Assert.assertTrue(accessor.addCollectionToUser(userEmail, collection) != null);

        User user = accessor.getUserByEmail(userEmail, IEnums.SEEK_MODE.FULL);

        Assert.assertTrue(accessor.addItemToCollection(userEmail, (long) 1, user.getUserCollections().get(0).getCollectionId()));

        user = accessor.getUserByEmail(userEmail, IEnums.SEEK_MODE.FULL);

        boolean test = false;

        for (GroupedItem item : user.getUserCollections().get(0).getCollectionItems())
        {
            if (item.getGroupedItem().getItemId() == 1)
                test = true;
        }

        Assert.assertTrue(test);

    }

    @Test
    public void testRemoveItemFromCollection()
    {
        com.troveup.brooklyn.orm.user.model.Collection collection = createCollection();

        Assert.assertTrue(accessor.addCollectionToUser(userEmail, collection) != null);

        Assert.assertTrue(accessor.addItemToCollection(userEmail, (long) 1, collection.getCollectionId()));

        User user = accessor.getUserByEmail(userEmail, IEnums.SEEK_MODE.FULL);

        boolean test = false;

        int collectionIndex = user.getUserCollections().indexOf(collection);

        //Collection index wasn't found, the test failed
        if (!(collectionIndex > -1))
            Assert.assertTrue(false);

        for (GroupedItem item : user.getUserCollections().get(0).getCollectionItems())
        {
            if (item.getGroupedItem().getItemId() == 1)
                test = true;
        }

        Assert.assertTrue(test);

        Assert.assertTrue(accessor.removeItemFromCollection(userEmail, (long) 1,
                user.getUserCollections().get(collectionIndex).getCollectionId()));

        user = accessor.getUserByEmail(userEmail, IEnums.SEEK_MODE.FULL);

        test = false;

        for (GroupedItem item : user.getUserCollections().get(0).getCollectionItems())
        {
            if (item.getGroupedItem().getItemId() == 1)
                test = true;
        }

        Assert.assertFalse(test);
    }

    @Test
    public void testUpdateCollection()
    {
        com.troveup.brooklyn.orm.user.model.Collection collection = createCollection();

        Assert.assertTrue(accessor.addCollectionToUser(userEmail, collection) != null);

        User user = accessor.getUserByEmail(userEmail, IEnums.SEEK_MODE.FULL);

        collection = user.getUserCollections().get(0);

        collection.setCollectionName(UUID.randomUUID().toString());

        collection.setCollectionDescription(UUID.randomUUID().toString());

        accessor.updateCollection(userEmail, collection);

        user = accessor.getUserByEmail(userEmail, IEnums.SEEK_MODE.FULL);

        Assert.assertTrue(user.getUserCollections().get(0).getCollectionName().equals(collection.getCollectionName()) &&
                user.getUserCollections().get(0).getCollectionDescription().equals(collection.getCollectionDescription()));


    }

    @Test
    public void testAddShoppingCartToUser() throws UsernameAlreadyInUseException {
        User user = createUser();

        Assert.assertTrue(accessor.createUser(user));

        //Gigantic unpersisted cart case
        Item unpersistedItem = ItemAccessorTest.createTestItem();
        Item unpersistedItemTwo = ItemAccessorTest.createTestItem();

        Item persistedItemOne = ItemAccessorTest.createTestItem();
        Item persistedItemTwo = ItemAccessorTest.createTestItem();

        Assert.assertTrue(itemAccessor.createItem(persistedItemOne));
        Assert.assertTrue(itemAccessor.createItem(persistedItemTwo));


        CartItem unpersistedCartItemWithUnpersistedItem = CartItem.getBuilder(unpersistedItem, 1, 1f, null, null).build();
        CartItem unpersistedCartItemWithPersistedItem = CartItem.getBuilder(persistedItemOne, 1, 1f, null, null).build();

        CartItem persistedCartItemWithUnpersistedItem = CartItem.getBuilder(unpersistedItemTwo, 1, 1f, null, null).build();
        CartItem persistedCartItemTwoWithPersistedItem = CartItem.getBuilder(persistedItemTwo, 1, 1f, null, null).build();

        List<CartItem> items = new ArrayList<>();
        items.add(unpersistedCartItemWithUnpersistedItem);
        items.add(unpersistedCartItemWithPersistedItem);
        items.add(persistedCartItemWithUnpersistedItem);
        items.add(persistedCartItemTwoWithPersistedItem);

        Cart cart = new Cart();
        cart.setCartItems(items);

        Assert.assertTrue(accessor.addShoppingCartToUser(user.getUserId(), cart));

        cart = cartAccessor.getShoppingCart(cart.getCartId(), IEnums.SEEK_MODE.FULL);

        //Ensure there aren't any duplicates
        for (CartItem item : cart.getCartItems())
        {
            Assert.assertTrue(itemAccessor.getItems(item.getCartItemReference().getItemName(),
                    Item.SEARCH_BY_TYPE.ITEM_NAME, IEnums.SEEK_MODE.QUICK).size() == 1);
        }

        //Itty bitty already-persisted cart test
        cart = new Cart();
        cart.setCartItems(new ArrayList<CartItem>());
        cart.getCartItems().add(CartItem.getBuilder(ItemAccessorTest.createTestItem(), 1,
                1f, null, null).build());
        cart.getCartItems().add(CartItem.getBuilder(ItemAccessorTest.createTestItem(), 1,
                1f, null, null).build());

        Assert.assertTrue(cartAccessor.persistCart(cart));

        user = createUser();

        Assert.assertTrue(accessor.createUser(user));

        Assert.assertTrue(accessor.addShoppingCartToUser(user.getUserId(), cart));

        cart = cartAccessor.getShoppingCart(cart.getCartId(), IEnums.SEEK_MODE.FULL);

        //Ensure there aren't any duplicates
        for (CartItem item : cart.getCartItems())
        {
            Assert.assertTrue(itemAccessor.getItems(item.getCartItemReference().getItemName(),
                    Item.SEARCH_BY_TYPE.ITEM_NAME, IEnums.SEEK_MODE.QUICK).size() == 1);
        }

    }

    @Test
    public void testPersistNewUserOrder()
    {
        User user = createUser();

        try {
            accessor.createUser(user);
        } catch (Exception e)
        {
            //Eat it
        }

        Item unpersistedItem = ItemAccessorTest.createTestItem();
        Item unpersistedItemTwo = ItemAccessorTest.createTestItem();

        Item persistedItemOne = ItemAccessorTest.createTestItem();
        Item persistedItemTwo = ItemAccessorTest.createTestItem();

        Assert.assertTrue(itemAccessor.createItem(persistedItemOne));
        Assert.assertTrue(itemAccessor.createItem(persistedItemTwo));


        CartItem unpersistedCartItemWithUnpersistedItem = CartItem.getBuilder(unpersistedItem, 1, 1f, null, null).build();
        CartItem unpersistedCartItemWithPersistedItem = CartItem.getBuilder(persistedItemOne, 1, 1f, null, null).build();

        CartItem persistedCartItemWithUnpersistedItem = CartItem.getBuilder(unpersistedItemTwo, 1, 1f, null, null).build();
        CartItem persistedCartItemTwoWithPersistedItem = CartItem.getBuilder(persistedItemTwo, 1, 1f, null, null).build();

        List<CartItem> items = new ArrayList<>();
        items.add(unpersistedCartItemWithUnpersistedItem);
        items.add(unpersistedCartItemWithPersistedItem);
        items.add(persistedCartItemWithUnpersistedItem);
        items.add(persistedCartItemTwoWithPersistedItem);

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

        Cart cart = new Cart();
        cart.setCartItems(items);
        cart.setBillingAddress(address);
        cart.setShippingAddress(address);
        PaymentDetails details = new PaymentDetails();
        details.setPaymentToken(UUID.randomUUID().toString());
        cart.setPaymentDetails(details);

        cart.setGrandTotal(Money.parse("USD 20.00").getAmount());
        cart.setCartOwner(user);

        Assert.assertTrue(accessor.addShoppingCartToUser(user.getUserId(), cart));

        Order order = Order.getBuilder(cart).buildOrder();

        Assert.assertTrue(accessor.persistNewUserOrder(user.getUserId(), order));

        user = accessor.getUser(user.getUserId(), IEnums.SEEK_MODE.USER_ORDERS_FULL);

        Assert.assertNotNull(user.getOrders());
        Assert.assertTrue(user.getOrders().size() > 0);

        order = user.getOrders().get(0);

        Assert.assertNotNull(order.getGrandTotal());
        Assert.assertNotNull(order.getCartReference());
        Assert.assertNotNull(order.getOrderItems());
        Assert.assertNotNull(order.getPaymentDetails());
    }

    @Test
    public void nullifyCartTest()
    {
        User user = createUser();

        try {
            accessor.createUser(user);
        } catch (Exception e)
        {
            //Eat it
        }

        Item unpersistedItem = ItemAccessorTest.createTestItem();
        Item unpersistedItemTwo = ItemAccessorTest.createTestItem();

        Item persistedItemOne = ItemAccessorTest.createTestItem();
        Item persistedItemTwo = ItemAccessorTest.createTestItem();

        Assert.assertTrue(itemAccessor.createItem(persistedItemOne));
        Assert.assertTrue(itemAccessor.createItem(persistedItemTwo));


        CartItem unpersistedCartItemWithUnpersistedItem = CartItem.getBuilder(unpersistedItem, 1, 1f, null, null).build();
        CartItem unpersistedCartItemWithPersistedItem = CartItem.getBuilder(persistedItemOne, 1, 1f, null, null).build();

        CartItem persistedCartItemWithUnpersistedItem = CartItem.getBuilder(unpersistedItemTwo, 1, 1f, null, null).build();
        CartItem persistedCartItemTwoWithPersistedItem = CartItem.getBuilder(persistedItemTwo, 1, 1f, null, null).build();

        List<CartItem> items = new ArrayList<>();
        items.add(unpersistedCartItemWithUnpersistedItem);
        items.add(unpersistedCartItemWithPersistedItem);
        items.add(persistedCartItemWithUnpersistedItem);
        items.add(persistedCartItemTwoWithPersistedItem);

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

        Cart cart = new Cart();
        cart.setCartItems(items);
        cart.setBillingAddress(address);
        cart.setShippingAddress(address);
        PaymentDetails details = new PaymentDetails();
        details.setPaymentToken(UUID.randomUUID().toString());
        cart.setPaymentDetails(details);

        cart.setGrandTotal(Money.parse("USD 20.00").getAmount());
        cart.setCartOwner(user);

        Assert.assertTrue(accessor.addShoppingCartToUser(user.getUserId(), cart));

        Assert.assertTrue(accessor.removeCurrentCart(user.getUserId()));

        user = accessor.getUser(user.getUserId(), IEnums.SEEK_MODE.USER_CART_QUICK);

        Assert.assertTrue(user.getShoppingCart().getCartId() != cart.getCartId());
    }

    @Test
    public void unfollowUserTest() throws UsernameAlreadyInUseException {
        User user = createUser();
        User firstFollowedUser = createUser();
        User secondFollowedUser = createUser();

        accessor.createUser(user);
        accessor.createUser(firstFollowedUser);
        accessor.createUser(secondFollowedUser);

        accessor.followUser(user.getUserId(), firstFollowedUser.getUserId());
        accessor.followUser(user.getUserId(), secondFollowedUser.getUserId());

        List<User> followedUsers = accessor.getFollowedUsers(user.getUserId(), IEnums.SEEK_MODE.QUICK);

        Assert.assertTrue(followedUsers.size() == 2);

        accessor.unfollowUser(user.getUserId(), secondFollowedUser.getUserId());

        followedUsers = accessor.getFollowedUsers(user.getUserId(), IEnums.SEEK_MODE.QUICK);

        Assert.assertTrue(followedUsers.size() == 1);

        Assert.assertFalse(followedUsers.contains(secondFollowedUser));
    }

    @Test
    public void getCountOfUsersThatHaveBeenFollowedTest() throws UsernameAlreadyInUseException {
        User user = createUser();
        User firstFollowedUser = createUser();
        User secondFollowedUser = createUser();

        accessor.createUser(user);
        accessor.createUser(firstFollowedUser);
        accessor.createUser(secondFollowedUser);

        accessor.followUser(user.getUserId(), firstFollowedUser.getUserId());
        accessor.followUser(user.getUserId(), secondFollowedUser.getUserId());

        Assert.assertTrue(accessor.getFollowedUserCount(user.getUserId()) == 2);
    }

    @Test
    public void getCountOfUsersThatAreFollowingTest() throws UsernameAlreadyInUseException {
        User user = createUser();
        User firstFollowedUser = createUser();
        User secondFollowedUser = createUser();

        accessor.createUser(user);
        accessor.createUser(firstFollowedUser);
        accessor.createUser(secondFollowedUser);

        accessor.followUser(firstFollowedUser.getUserId(), user.getUserId());
        accessor.followUser(secondFollowedUser.getUserId(), user.getUserId());

        Assert.assertTrue(accessor.getUsersWhoFollowedUserCount(user.getUserId()) == 2l);

    }

    @Test
    public void getListOfUsersThatAreFollowingTest() throws UsernameAlreadyInUseException {
        User user = createUser();
        User firstFollowedUser = createUser();
        User secondFollowedUser = createUser();

        accessor.createUser(user);
        accessor.createUser(firstFollowedUser);
        accessor.createUser(secondFollowedUser);

        accessor.followUser(firstFollowedUser.getUserId(), user.getUserId());
        accessor.followUser(secondFollowedUser.getUserId(), user.getUserId());

        List<User> followingUsers = accessor.getUsersWhoFollowedUser(user.getUserId(), IEnums.SEEK_MODE.QUICK);

        Assert.assertNotNull(followingUsers);
        Assert.assertTrue(followingUsers.size() == 2);
    }

    @Test
    public void checkUserFollow() throws UsernameAlreadyInUseException {
        User user = createUser();
        User firstFollowedUser = createUser();
        User secondFollowedUser = createUser();
        User thirdNotFollowedUser = createUser();

        accessor.createUser(user);
        accessor.createUser(firstFollowedUser);
        accessor.createUser(secondFollowedUser);
        accessor.createUser(thirdNotFollowedUser);

        accessor.followUser(user.getUserId(), firstFollowedUser.getUserId());
        accessor.followUser(user.getUserId(), secondFollowedUser.getUserId());

        Assert.assertTrue(accessor.checkUserFollow(user.getUserId(), firstFollowedUser.getUserId()));
        Assert.assertTrue(accessor.checkUserFollow(user.getUserId(), secondFollowedUser.getUserId()));
        Assert.assertFalse(accessor.checkUserFollow(user.getUserId(), thirdNotFollowedUser.getUserId()));
    }

    @Test
    public void getCollectionCountTest()
    {
        Assert.assertTrue(accessor.getCollectionCount(2l, true) > 0);
    }

    @Test
    public void getCollectionFollowCount()
    {
        Assert.assertTrue(accessor.getCollectionFollowCount(9l) > 0);
    }

    @Test
    public void getUserOrders()
    {
        User user = accessor.getUser(2l, IEnums.SEEK_MODE.USER_ORDERS_QUICK);

        Assert.assertTrue(user.getOrders().size() > 0);
    }

    @Test
    public void createUsersFromCsv() throws IOException {
        byte[] rawCsvFile = Files.readAllBytes(Paths.get("/Users/tim/Desktop/signup_users_MS_edit.csv"));

        String csvAsString = new String(rawCsvFile, Charset.forName("UTF-8"));

        String[] splitCsv = csvAsString.split("\\n");

        for (int i = 0; i < splitCsv.length; ++i)
        {
            String[] rsplit = splitCsv[i].split("\\r");
            splitCsv[i] = rsplit[0];
        }

        File processedCsvFile = new File("/Users/tim/Desktop/signup_users_processed.csv");
        if (!processedCsvFile.exists()) {
            processedCsvFile.createNewFile();
        }
        FileWriter fw = new FileWriter(processedCsvFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("First Name,Username,Email,Password,Duplicate Error\n");

        //First record contains invalid data
        for (int i = 1; i < splitCsv.length; ++i)
        {
            String[] splitEmail = splitCsv[i].split("@");
            String firstName = splitEmail[0];
            String userName = firstName;
            String email = splitCsv[i];
            Boolean isPasswordValid = false;
            String password = UUID.randomUUID().toString().substring(0, 7).toUpperCase();
            Integer alphaCounter = 0;

            for (Byte individualCharacter : password.getBytes())
            {
                if (!Character.isDigit(individualCharacter))
                    alphaCounter++;
            }

            if (alphaCounter >= 2)
                isPasswordValid = true;

            while(!isPasswordValid)
            {
                alphaCounter = 0;
                password = UUID.randomUUID().toString().substring(0, 7).toUpperCase();

                for (Byte individualCharacter : password.getBytes())
                {
                    if (!Character.isDigit(individualCharacter))
                        alphaCounter++;
                }

                if (alphaCounter >=2)
                    isPasswordValid = true;
            }

            User user = new User();
            user.setRole(UserDetails.Role.ROLE_USER);
            user.setFirstName(firstName);
            user.setLastName("");
            user.setEmail(email);
            user.setUsername(userName);
            user.setPassword(encoder.encode(password));
            user.setFullProfileImagePath("https://storage.googleapis.com/troveup-imagestore/assets/img/default-user-icon.jpg");
            user.setProfileImageThumbnailPath("https://storage.googleapis.com/troveup-imagestore/assets/img/default-user-icon-thumb.jpg");
            user.setCoverPhotoImagePath("https://storage.googleapis.com/troveup-imagestore/assets/default_cover_photo.png");
            user.setHasUsedFtue(false);
            user.setEnabled(true);
            user.setIsAdmin(false);

            user.setOptOut(false);

            String csvWriteLine = firstName + "," + userName + "," + email + "," + password;

            try {
                accessor.createUser(user);
            } catch (UsernameAlreadyInUseException e) {
                e.printStackTrace();
                csvWriteLine += "," + "yes";
            }

            csvWriteLine += "\n";
            bw.write(csvWriteLine);
        }

        bw.close();
    }


    private com.troveup.brooklyn.orm.user.model.Collection createCollection()
    {
        com.troveup.brooklyn.orm.user.model.Collection collection = new com.troveup.brooklyn.orm.user.model.Collection();
        collection.setCollectionName(UUID.randomUUID().toString());
        collection.setCollectionDescription("Test Description");
        collection.setActive(true);

        return collection;
    }

    public static User createUser()
    {
        User user = new User();

        String userEmail;

        user.setFirstName("Tim");
        user.setLastName("Growney");
        user.setPassword("testing123");
        user.setRole(UserDetails.Role.ROLE_USER);
        userEmail = UUID.randomUUID().toString();
        user.setEmail(userEmail);
        user.setUsername(userEmail);

        return user;
    }

}
