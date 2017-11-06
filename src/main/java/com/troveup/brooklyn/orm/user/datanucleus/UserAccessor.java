package com.troveup.brooklyn.orm.user.datanucleus;

import com.troveup.brooklyn.orm.cart.datanucleus.CartAccessor;
import com.troveup.brooklyn.orm.cart.interfaces.ICartAccessor;
import com.troveup.brooklyn.orm.cart.interfaces.IPromoCodeAccessor;
import com.troveup.brooklyn.orm.cart.model.Cart;
import com.troveup.brooklyn.orm.cart.model.CartItem;
import com.troveup.brooklyn.orm.cart.model.GenericItem;
import com.troveup.brooklyn.orm.cart.model.PromoCode;
import com.troveup.brooklyn.orm.common.datanucleus.ObjectAccessor;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.countries.interfaces.ICountryAccessor;
import com.troveup.brooklyn.orm.countries.model.Country;
import com.troveup.brooklyn.orm.countries.model.Subdivision;
import com.troveup.brooklyn.orm.item.interfaces.IItemAccessor;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.order.datanucleus.OrderAccessor;
import com.troveup.brooklyn.orm.order.interfaces.IOrderAccessor;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.user.interfaces.IUserAccessor;
import com.troveup.brooklyn.orm.user.model.*;
import com.troveup.brooklyn.orm.user.model.Collection;
import com.troveup.brooklyn.spring.social.model.UserDetails;
import com.troveup.brooklyn.util.StringUtils;
import com.troveup.brooklyn.util.exception.UsernameAlreadyInUseException;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.*;

import javax.jdo.*;
import javax.jdo.annotations.PersistenceAware;
import javax.jdo.annotations.Persistent;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by tim on 4/16/15.
 */
@PersistenceAware
public class UserAccessor extends ObjectAccessor implements IUserAccessor, com.troveup.brooklyn.orm.user.interfaces.ISocialAccessor {
    /**
     * JDOQL Hints and Tips:
     *
     * :var is an implicit variable, whose name doesn't matter.  Implicit variables do not need to be declared, and
     * are parsed in the order that they are passed in the execute() statement.  For example, if a filter statement has
     * a statement -- (this.id == :var), then the id from the object that is the subject of the search will be compared
     * to the first parameter of the query.execute() method.  There are a maximum of three variables that can be passed.
     *
     * .contains is very useful when searching through a list of objects.  If the reference in and of itself is a list,
     * then it can be accessed as follows -- (itemList.contains(:var)).  This essentially searches a list of primitive
     * items for a given implicit value, :var.
     *
     * declaredVariables are a direct "CROSS JOIN" command in MySQL.  To access a variable that is contained within a
     * given list, and equals a specific value, the filter would look as follows --
     * (itemList.contains(var) & var == :val).  This can be particularly useful if you have to combine searches between
     * lists.  For example -- (itemList.contains(var) & :passedList.contains(var)).  The variable, var, should be
     * declared as a variable with a specific type that matches the templated list.  This essentially says "I want to
     * find all items that are in both the list that I passed and the list that's contained in the item".
     *
     * Objects retrieved by DataNucleus are, by default, attached.  This means that any change that is made within the
     * persistence aware class will be persisted all the way to the database.  If you need the object for uses outside
     * of the Data Access Object, you should detach it.
     *
     * By default, DataNucleus will not retrieve complex members of a given class until they are accessed.  Viewing them
     * or taking them outside of the DAO will leave them null.  A fetch group defines what fields are retrieved by
     * DataNucleus.  These can be defined in such a way that everything is pulled.
     *
     * The dot operator can only access one level down in a given list of items.  After that, a declaredVariable must
     * be used.
     *
     * One PersistenceAware class cannot touch another PersistenceAware class's objects.  It will throw an exception.
     * In addition, adding detached objects to a managed instance will try to create a new version of that object,
     * rather than trying to retrieve the existing version.  There may be some copypasta here because of this
     * technicality.
     *
     * Persistence of an object will modify that object's reference to set all fields to null except the primary key,
     * which it will populate with that key.
     *
     * Persisting an object without a persistence manager attached will create a new object rather than creating a
     * related object.
     */


    /**
     * Default constructor that manages injection of the PersistenceManagerFactory
     *
     * @param pmfProxy Injected proxied PersistenceManagerFactory
     */
    public UserAccessor(PersistenceManagerFactory pmfProxy)
    {
        super(pmfProxy);
    }

    @Autowired
    private IItemAccessor itemAccessor;

    @Autowired
    private ICountryAccessor countryAccessor;

    @Autowired
    private ICartAccessor cartAccessor;

    @Autowired
    private IOrderAccessor orderAccessor;

    @Autowired
    private IPromoCodeAccessor promoCodeAccessor;



    /**
     * Inserts a user into a database.
     *
     * @param user The user object that needs to be persisted.
     * @return The success state of the method.
     */
    @Override
    public Boolean createUser(User user) throws UsernameAlreadyInUseException
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;
        try{
            if (user == null) {
                throw new NullPointerException("User cannot be null!");
            }
            if (user.getCreated() == null)
                user.setCreated(new Date());

            if (user.getSocialConnections() != null) {
                for (SocialConnection connection : user.getSocialConnections()) {
                    if (connection.getCreated() == null)
                        connection.setCreated(new Date());
                }
            }

            //Need to ensure that we have a persistence manager attached to the objects that might need to be shared
            //TODO:  Fully test this persistence manager attachment
            if (user.getShippingAddress() != null)
            {
                //If there's an attached address, go get that, everything else should be attached
                if (user.getShippingAddress().getAddressId() != null)
                {
                    Address address = getAddressAttached(user.getShippingAddress().getAddressId(), pm);
                    user.setShippingAddress(address);
                }

                //If there's not an attached address, but the country should be attached, attach it
                else if (user.getShippingAddress().getCountry().getPrimaryKeyId() != null)
                {
                    Country country = countryAccessor.getCountryAttached(user.getShippingAddress().getCountry().getPrimaryKeyId(), pm);
                    user.getShippingAddress().setCountry(country);

                    if (user.getShippingAddress().getSubdivision().getPrimaryKeyId() != null)
                    {
                        Subdivision subdivision = countryAccessor.getSubdivisionAttached(user.getShippingAddress().getSubdivision().getPrimaryKeyId(), pm);
                        user.getShippingAddress().setSubdivision(subdivision);
                    }

                }
            }

            if (user.getBillingAddress() != null && user.getBillingAddress().getAddressId() != null)
            {
                //If there's an attached address, go get that, everything else should be attached
                if (user.getBillingAddress().getAddressId() != null)
                {
                    Address address = getAddressAttached(user.getBillingAddress().getAddressId(), pm);
                    user.setBillingAddress(address);
                }

                //If there's not an attached address, but the country should be attached, attach it
                else if (user.getShippingAddress().getCountry().getPrimaryKeyId() != null)
                {
                    Country country = countryAccessor.getCountryAttached(user.getBillingAddress().getCountry().getPrimaryKeyId(), pm);
                    user.getBillingAddress().setCountry(country);
                }
            }

            if (user.getShoppingCart() != null)
            {
                Address billingAddress = user.getShoppingCart().getBillingAddress();
                Address shippingAddress = user.getShoppingCart().getShippingAddress();

                //Make sure that we properly attach the persistence manager to the billing address
                if(billingAddress != null)
                {
                    //If there's an attached address, go get that, everything else should be attached
                    if (billingAddress.getAddressId() != null)
                    {
                        Address address = getAddressAttached(billingAddress.getAddressId(), pm);
                        user.getShoppingCart().setBillingAddress(address);
                    }

                    //If there's not an attached address, but the country should be attached, attach it
                    else
                    {
                        if (billingAddress.getCountry().getPrimaryKeyId() != null)
                        {
                            Country country = countryAccessor.getCountryAttached(billingAddress.getCountry().getPrimaryKeyId(), pm);
                            user.getShoppingCart().getBillingAddress().setCountry(country);
                        }

                        if (billingAddress.getSubdivision().getPrimaryKeyId() != null)
                        {
                            Subdivision subdivision = countryAccessor.getSubdivisionAttached(billingAddress.getSubdivision().getPrimaryKeyId(), pm);
                            user.getShoppingCart().getBillingAddress().setSubdivision(subdivision);
                        }
                    }
                }

                //Make sure that we properly attach the persistence manager to the shipping address
                if (shippingAddress != null)
                {
                    //If there's an attached address, go get that, everything else should be attached
                    if (shippingAddress.getAddressId() != null)
                    {
                        Address address = getAddressAttached(shippingAddress.getAddressId(), pm);
                        user.getShoppingCart().setShippingAddress(address);
                    }

                    //If there's not an attached address, but the country should be attached, attach it
                    else
                    {
                        if (shippingAddress.getCountry().getPrimaryKeyId() != null)
                        {
                            Country country = countryAccessor.getCountryAttached(shippingAddress.getCountry().getPrimaryKeyId(), pm);
                            user.getShoppingCart().getShippingAddress().setCountry(country);
                        }

                        if (shippingAddress.getSubdivision().getPrimaryKeyId() != null)
                        {
                            Subdivision subdivision = countryAccessor.getSubdivisionAttached(shippingAddress.getSubdivision().getPrimaryKeyId(), pm);
                            user.getShoppingCart().getShippingAddress().setSubdivision(subdivision);
                        }
                    }
                }
            }

            pm.makePersistent(user);

            rval = true;
        }
        catch (JDODataStoreException e)
        {
            if (e.getMessage().contains("UUNIQUE") || e.getMessage().contains("EUNIQUE"))
                throw new UsernameAlreadyInUseException(user.getUsername());
            else {
                logError(e);
            }
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    /**
     * Gets a specific user by their identifying information.
     *
     * @param userIdentifier Identifying information (eg. first name, last name, email, etc.)
     * @param identifierType Type of identifying information
     * @param mode How much of the user data to retrieve.  FULL retrieves all collections included within the
     *             USER object.  Otherwise, collections will return null.
     * @return
     */
    @Override
    public List<User> getUser(List<String> userIdentifier, User.SEARCH_BY_TYPE identifierType, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<User> rval = null;

        try {
            if (userIdentifier == null || userIdentifier.size() == 0)
                throw new IllegalArgumentException("Parameter must contain at least one element");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(User.class, ":p.contains(" + identifierType.toString() + ")");
            query.setResultClass(User.class);

            rval =  (List<User>) query.execute(userIdentifier.toArray());
            Map<Long, Integer> userMap = new HashMap<>();

            //Grab the user bag count for setting it after a detach.
            for (User user : rval)
            {
                Integer itemCount = 0;
                if (user.getShoppingCart() != null)
                {
                    if (user.getShoppingCart().getCartItems() != null) {
                        itemCount += user.getShoppingCart().getCartItems().size();
                    }
                    if (user.getShoppingCart().getGenericItems() != null)
                    {
                        itemCount += user.getShoppingCart().getGenericItems().size();
                    }

                    userMap.put(user.getUserId(), itemCount);
                }
            }

            //Objects initially grabbed by DataNucleus are live, in that any changes are automagically written to
            //the persistence layer (in our case, the database).  If we want that information specifically for
            //manipulation outside of DataNucleus's management, we need to detach them.
            rval = (List<User>) pm.detachCopyAll(rval);

            //Detach happened, now set the bag values.
            for (User user : rval) {
                if (userMap.containsKey(user.getUserId()))
                    user.setBagItemCount(userMap.get(user.getUserId()));
            }

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public List<User> getUserAttached(List<String> userIdentifier, User.SEARCH_BY_TYPE identifierType, PersistenceManager pm)
    {
        List<User> rval = null;

        try {
            if (userIdentifier == null || userIdentifier.size() == 0)
                throw new IllegalArgumentException("Parameter must contain at least one element");

            Query query = pm.newQuery(User.class, ":p.contains(" + identifierType.toString() + ")");
            query.setResultClass(User.class);

            rval =  (List<User>) query.execute(userIdentifier.toArray());

        } catch (Exception e)
        {
            logError(e);
        }

        return rval;
    }

    public User getUserAttached(String userIdentifier, User.SEARCH_BY_TYPE identifierType, PersistenceManager pm)
    {
        User rval = null;
        List<String> encapsulator = new ArrayList<>();
        encapsulator.add(userIdentifier);

        List<User> users = getUserAttached(encapsulator, identifierType, pm);

        if (users != null && users.size() > 0)
            rval = users.get(0);

        return rval;
    }

    /**
     * Retrieves a list of users based on
     *
     * @param userIdentifier
     * @param identifierType
     * @param mode
     * @return
     */
    @Override
    public List<User> getUser(String userIdentifier, User.SEARCH_BY_TYPE identifierType, IEnums.SEEK_MODE mode)
    {
        List<String> encapsulator = new ArrayList<>();
        encapsulator.add(userIdentifier);

        return getUser(encapsulator, identifierType, mode);
    }

    @Override
    public User getUser(Long id, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        User rval = null;
        try
        {
            if(id == null)
                throw new IllegalArgumentException("Parameter must contain at least one element");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(User.class, "userId == :id");
            query.setResultClass(User.class);
            List<User> users = (List<User>) query.execute(id);

            if (users != null && users.size() > 0)
                rval = pm.detachCopy(users.get(0));

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public User getUserAttached(Long id, PersistenceManager pm)
    {
        User rval = null;

        try
        {
            if(id == null)
                throw new IllegalArgumentException("Parameter must contain at least one element");

            Query query = pm.newQuery(User.class, "userId == :id");
            query.setResultClass(User.class);
            List<User> users = (List<User>) query.execute(id);

            if (users != null && users.size() > 0)

            rval = users.get(0);

        } catch (Exception e)
        {
            logError(e);
        }

        return rval;
    }

    private User getUserAttached(String email, PersistenceManager pm)
    {
        User rval = null;

        try
        {
            if (email == null)
                throw new IllegalArgumentException("Parameter must contain at least one element");

            Query query = pm.newQuery(User.class, "email == :email");
            query.setResultClass(User.class);

            List<User> queryResult = (List<User>) query.execute(email);

            if (queryResult != null && queryResult.size() > 0)
                rval = queryResult.get(0);

        } catch (Exception e)
        {
            logError(e);
        }

        return rval;
    }

    @Override
    public User getUserByEmail(String email, IEnums.SEEK_MODE mode)
    {
        User rval = null;

        List<String> emailContainer = new ArrayList<>();
        emailContainer.add(email);

        List<User> users = this.getUser(emailContainer, User.SEARCH_BY_TYPE.EMAIL, mode);

        if (users != null && users.size() > 0)
            rval = users.get(0);

        return rval;
    }

    @Override
    public User getUserByUsername(String username, IEnums.SEEK_MODE mode)
    {
        User rval = null;

        List<String> usernameContainer = new ArrayList<>();
        usernameContainer.add(username);

        List<User> users = this.getUser(usernameContainer, User.SEARCH_BY_TYPE.USER_NAME, mode);

        if (users != null && users.size() > 0)
            rval = users.get(0);

        return rval;
    }

    /**
     * Adds a social connection between a given social provider (Facebook, Twitter, etc) and
     * the Trove record in the RDBMS.
     *
     * @param data Social connection data object from which to add data.
     * @param userId Primary key identifier of a given user.
     * @return Boolean success state of the operation.
     */
    @Override
    public Boolean addUserSocialConnection(ConnectionData data, Long userId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try{
            if (data == null || userId == null)
                throw new IllegalArgumentException("Parameters cannot be null.");

            SocialConnection connection = SocialConnection.getBuilder(data).buildSocialConnection();
            connection.setCreated(new Date());
            User user = getUserAttached(userId, pm);

            if (user == null)
                throw new NullPointerException("No user object retrieved");

            if (user.getSocialConnections() == null)
                user.setSocialConnections(new ArrayList<SocialConnection>());

            user.getSocialConnections().add(connection);

            rval = true;

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateUserSocialConnection(ConnectionData data)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try{
            if (data == null)
                throw new IllegalArgumentException("Parameters cannot be null.");

            List<ConnectionData> dataList = new ArrayList<>();
            dataList.add(data);

            List<SocialConnection> connections = getAttachedSocialConnection(data, pm);

            if (connections.size() == 1) {
                SocialConnection connection = connections.get(0);

                if (data.getAccessToken() != null)
                    connection.setAccessToken(data.getAccessToken());

                if (data.getDisplayName() != null)
                    connection.setDisplayName(data.getDisplayName());

                if (data.getExpireTime() != null)
                    connection.setExpireTime(data.getExpireTime());

                if (data.getImageUrl() != null)
                    connection.setImageUrl(data.getImageUrl());

                if (data.getProfileUrl() != null)
                    connection.setProfileUrl(data.getProfileUrl());

                if (data.getRefreshToken() != null)
                    connection.setRefreshToken(data.getRefreshToken());

                if (data.getSecret() != null)
                    connection.setSecret(data.getSecret());
                rval = true;
            }
        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    private List<SocialConnection> getAttachedSocialConnection(ConnectionData data, PersistenceManager pm)
    {
        List<SocialConnection> rval = null;

        try{
            if (data == null)// || data.size() == 0)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            Query query = pm.newQuery(SocialConnection.class, "providerId == :pId && providerUserId == :pUID");
            query.setResultClass(SocialConnection.class);

            rval = (List<SocialConnection>) query.execute(data.getProviderId(), data.getProviderUserId());

        } catch (Exception e)
        {
            logError(e);
        }

        return rval;
    }

    @Override
    public SocialConnection getSocialConnection(ConnectionKey key, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        SocialConnection rval = null;

        try{
            if (key == null || mode == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            //DataNucleus will only pull surface level data unless explicitly instructed to do so.  If we need to pull
            //the whole user, we need to add extra fetch groups for the collections.
            //Reference:  http://www.datanucleus.org/products/datanucleus/jdo/fetchgroup.html
            if (mode != null && mode == IEnums.SEEK_MODE.FULL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(SocialConnection.class, "fullFetch");
                fetchGroup.addMember("user");

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("fullFetch");
                fetchPlan.setMaxFetchDepth(2);
            }

            Query query = pm.newQuery(SocialConnection.class, "providerId == :pId && providerUserId == :pUID");
            query.setResultClass(SocialConnection.class);

            List<SocialConnection> connections = (List<SocialConnection>) query.execute(key.getProviderId(), key.getProviderUserId());

            if (connections != null && connections.size() > 0)
                rval = connections.get(0);

        } catch (Exception e)
        {
            logError(e);
        }
        finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public SocialConnection getSocialConnection(ConnectionKey key, Long userId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        SocialConnection rval = null;

        try{
            if (key == null || mode == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            //DataNucleus will only pull surface level data unless explicitly instructed to do so.  If we need to pull
            //the whole user, we need to add extra fetch groups for the collections.
            //Reference:  http://www.datanucleus.org/products/datanucleus/jdo/fetchgroup.html
            if (mode != null && mode == IEnums.SEEK_MODE.FULL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(SocialConnection.class, "fullFetch");
                fetchGroup.addMember("user");

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("fullFetch");
                fetchPlan.setMaxFetchDepth(2);
            }

            Query query = pm.newQuery(SocialConnection.class, "providerId == :pId && providerUserId == :pUID && user.userId == :userid");
            query.setResultClass(SocialConnection.class);

            List<SocialConnection> connections = (List<SocialConnection>) query.execute(key.getProviderId(), key.getProviderUserId(), userId.longValue());

            if (connections != null && connections.size() > 0)
                rval = connections.get(0);

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<SocialConnection> getSocialConnections(String providerId, Long userId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<SocialConnection> rval = null;

        try{
            if (providerId == null || providerId.isEmpty() || userId == null || mode == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            //DataNucleus will only pull surface level data unless explicitly instructed to do so.  If we need to pull
            //the whole user, we need to add extra fetch groups for the collections.
            //Reference:  http://www.datanucleus.org/products/datanucleus/jdo/fetchgroup.html
            if (mode != null && mode == IEnums.SEEK_MODE.FULL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(SocialConnection.class, "fullFetch");
                fetchGroup.addMember("user");

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("fullFetch");
                fetchPlan.setMaxFetchDepth(2);
            }

            Query query = pm.newQuery(SocialConnection.class, "providerId == :pId && user.userId == :userid");
            query.setOrdering("created DESC");
            query.setResultClass(SocialConnection.class);

            rval = (List<SocialConnection>) query.execute(providerId, userId.longValue());

        } catch (Exception e)
        {
            logError(e);
        }
        finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<SocialConnection> getSocialConnections(Long userId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<SocialConnection> rval = null;

        try{
            if (userId == null || mode == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            //DataNucleus will only pull surface level data unless explicitly instructed to do so.  If we need to pull
            //the whole user, we need to add extra fetch groups for the collections.
            //Reference:  http://www.datanucleus.org/products/datanucleus/jdo/fetchgroup.html
            if (mode != null && mode == IEnums.SEEK_MODE.FULL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(SocialConnection.class, "fullFetch");
                fetchGroup.addMember("user");

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("fullFetch");
                fetchPlan.setMaxFetchDepth(2);
            }

            Query query = pm.newQuery(SocialConnection.class, "user.userId == :userid");
            query.setOrdering("created DESC");
            query.setResultClass(SocialConnection.class);

            rval = (List<SocialConnection>) query.execute(userId.longValue());

        } catch (Exception e)
        {
            logError(e);
        }
        finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<SocialConnection> getSocialConnections(String providerId, List<String> providerUserId, Long userId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<SocialConnection> rval = null;

        try{
            if (providerUserId == null || providerUserId.size() == 0 || userId == null || mode == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            //DataNucleus will only pull surface level data unless explicitly instructed to do so.  If we need to pull
            //the whole user, we need to add extra fetch groups for the collections.
            //Reference:  http://www.datanucleus.org/products/datanucleus/jdo/fetchgroup.html
            if (mode != null && mode == IEnums.SEEK_MODE.FULL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(SocialConnection.class, "fullFetch");
                fetchGroup.addMember("user");

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("fullFetch");
                fetchPlan.setMaxFetchDepth(2);
            }

            Query query = pm.newQuery(SocialConnection.class, ":ids.contains(providerUserId) && user.userId == :userId && " +
                    "providerId == :providerId");
            query.setOrdering("created DESC");
            query.setResultClass(SocialConnection.class);

            rval = (List<SocialConnection>) query.execute(providerUserId, userId.longValue(), providerId);

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<SocialConnection> getSocialConnections(String providerId, List<String> providerUserId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<SocialConnection> rval = null;

        try{
            if (providerUserId == null || providerUserId.size() == 0 || mode == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            //DataNucleus will only pull surface level data unless explicitly instructed to do so.  If we need to pull
            //the whole user, we need to add extra fetch groups for the collections.
            //Reference:  http://www.datanucleus.org/products/datanucleus/jdo/fetchgroup.html
            if (mode != null && mode == IEnums.SEEK_MODE.FULL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(SocialConnection.class, "fullFetch");
                fetchGroup.addMember("user");

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("fullFetch");
                fetchPlan.setMaxFetchDepth(2);
            }

            Query query = pm.newQuery(SocialConnection.class, ":ids.contains(providerUserId) && " +
                    "providerId == :providerId");
            query.setOrdering("created DESC");
            query.setResultClass(SocialConnection.class);

            rval = (List<SocialConnection>) query.execute(providerUserId, providerId);

        } catch (Exception e)
        {
            logError(e);
        }
        finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean removeConnection(ConnectionKey key)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (key == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            Query query = pm.newQuery(SocialConnection.class, "providerId == :pId && providerUserId == :pUID");
            Long numberDeleted = (Long)query.deletePersistentAll(key.getProviderId(), key.getProviderUserId());

            if (numberDeleted.equals(new Long(1)))
                rval = true;
            else if (numberDeleted > 1)
                throw new Exception("MULTIPLE CONNECTIONS DELETED, providerId was " + key.getProviderId() + " provider" +
                        "UserId was: " + key.getProviderUserId());

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean removeConnection(ConnectionKey key, Long userId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (key == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            Query query = pm.newQuery(SocialConnection.class, "providerId == :pId && providerUserId == :pUID" +
                    "&& user.userId == :userId");
            Long numberDeleted = (Long)query.deletePersistentAll(key.getProviderId(), key.getProviderUserId(), userId.longValue());

            if (numberDeleted.equals(new Long(1)))
                rval = true;
            else if (numberDeleted > 1)
                throw new Exception("MULTIPLE CONNECTIONS DELETED, providerId was " + key.getProviderId() + " provider" +
                        "UserId was: " + key.getProviderUserId());

        } catch (Exception e)
        {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean removeConnection(String providerId, Long userId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (providerId == null || providerId.isEmpty() || userId == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            Query query = pm.newQuery(SocialConnection.class, "providerId == :pId && user.userId == :userId");
            Long numberDeleted = (Long)query.deletePersistentAll(providerId, userId.longValue());

            if (numberDeleted >= 1)
                rval = true;

        } catch (Exception e)
        {
            logError(e);
        }
        finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Set<Long> getUserIds(ConnectionKey key)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Set<Long> rval = null;

        try
        {
            if (key == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            ConnectionData data = new ConnectionData(key.getProviderId(), key.getProviderUserId(),
                    null, null, null, null, null, null, null);

            List<SocialConnection> connections = getAttachedSocialConnection(data, pm);

            if (connections == null || connections.size() == 0)
                throw new NoSuchConnectionException(key);

            HashMap<Long, String> sortMap = new HashMap<>();

            for (SocialConnection connection : connections)
            {
                if (!sortMap.containsKey(connection.getUser().getUserId()))
                    sortMap.put(connection.getUser().getUserId(), key.getProviderUserId());
            }

            rval = sortMap.keySet();


        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Set<String> getUsernames(ConnectionKey key)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Set<String> rval = null;

        try
        {
            if (key == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            ConnectionData data = new ConnectionData(key.getProviderId(), key.getProviderUserId(),
                    null, null, null, null, null, null, null);

            List<SocialConnection> connections = getAttachedSocialConnection(data, pm);

            if (connections == null || connections.size() == 0)
                throw new NoSuchConnectionException(key);

            HashMap<String, String> sortMap = new HashMap<>();

            for (SocialConnection connection : connections)
            {
                if (!sortMap.containsKey(connection.getUser().getUsername()))
                    sortMap.put(connection.getUser().getUsername(), key.getProviderUserId());
            }

            rval = sortMap.keySet();


        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Set<String> getEmails(ConnectionKey key)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Set<String> rval = null;

        try
        {
            if (key == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            ConnectionData data = new ConnectionData(key.getProviderId(), key.getProviderUserId(),
                    null, null, null, null, null, null, null);

            List<SocialConnection> connections = getAttachedSocialConnection(data, pm);

            if (connections != null && connections.size() > 0) {

                HashMap<String, String> sortMap = new HashMap<>();

                for (SocialConnection connection : connections) {
                    if (!sortMap.containsKey(connection.getUser().getUsername()))
                        sortMap.put(connection.getUser().getEmail(), key.getProviderUserId());
                }

                rval = sortMap.keySet();
            }
        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Set<Long> getUserIds(String providerId, List<String> providerUserIds)
    {
        Set<Long> rval = null;

        if (providerId == null || providerId.isEmpty() || providerUserIds == null || providerUserIds.isEmpty())
            throw new IllegalArgumentException("Parameters must have at least one element.");

        try
        {
            List<SocialConnection> connections = getSocialConnections(providerId, providerUserIds, IEnums.SEEK_MODE.FULL);

            if (connections == null || connections.size() == 0)
                throw new NoSuchElementException("ProviderId: " + providerId + " and list of providerUserIds");

            HashMap<Long, String> sortMap = new HashMap<>();

            for (SocialConnection connection : connections)
            {
                if (!sortMap.containsKey(connection.getUser().getUserId()))
                    sortMap.put(connection.getUser().getUserId(), connection.getProviderUserId());
            }

            rval = sortMap.keySet();


        } catch (Exception e)
        {
            logError(e);
        }

        return rval;
    }

    @Override
    public Boolean addVerificationLinkage(Long userId, String token)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (userId == null || token == null || token.isEmpty())
                throw new IllegalArgumentException("Parameters must have at least one element.");

            VerificationToken tokenObject = new VerificationToken(token);

            User user = getUserAttached(userId, pm);

            if (user != null) {
                user.getRegistrationToken().add(tokenObject);
                rval = true;
            }

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public VerificationToken getVerificationToken(String token)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        VerificationToken rval = null;

        try
        {
            if (token == null || token.isEmpty())
                throw new IllegalArgumentException("Parameters must have at least one element.");

            FetchGroup fetchGroup = pm.getFetchGroup(VerificationToken.class, "fullFetch");
            fetchGroup.addMember("user");

            FetchPlan fetchPlan = pm.getFetchPlan();
            fetchPlan.addGroup("fullFetch");
            fetchPlan.setMaxFetchDepth(2);

            Query query = pm.newQuery(VerificationToken.class, "token == :token");
            query.setResultClass(VerificationToken.class);

            List<VerificationToken> result = (List<VerificationToken>) query.execute(token);

            if (result != null && result.size() > 0)
                rval = result.get(0);

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean setVerificationTokenVerified(String token)
    {
        Boolean rval = false;

        try
        {
            if (token == null || token.isEmpty())
                throw new IllegalArgumentException("Parameters must have at least one element.");

            VerificationToken tokenObject = getVerificationToken(token);
            tokenObject.setVerified(true);
            tokenObject.setValid(false);

            rval = true;
        } catch (Exception e)
        {
            logError(e);
        }

        return rval;
    }

    @Override
    public Boolean setUserEnabled(Long userId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (userId == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            User user = getUserAttached(userId, pm);
            user.setEnabled(true);
            rval = true;

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean createForgotPasswordLinkage(Long userId, String token)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (userId == null || token == null || token.isEmpty())
                throw new IllegalArgumentException("Parameters must have at least one element.");

            PasswordResetToken tokenObject = new PasswordResetToken(token);

            User user = getUserAttached(userId, pm);

            if (user != null) {
                if (user.getResetTokens() == null)
                    user.setResetTokens(new ArrayList<PasswordResetToken>());
                user.getResetTokens().add(tokenObject);
                rval = true;
            }

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public PasswordResetToken getResetToken(String token)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        PasswordResetToken rval = null;

        try
        {
            if (token == null || token.isEmpty())
                throw new IllegalArgumentException("Parameters must have at least one element.");

            FetchGroup fetchGroup = pm.getFetchGroup(PasswordResetToken.class, "fullFetch");
            fetchGroup.addMember("user");

            FetchPlan fetchPlan = pm.getFetchPlan();
            fetchPlan.addGroup("fullFetch");
            fetchPlan.setMaxFetchDepth(2);

            Query query = pm.newQuery(PasswordResetToken.class, "token == :token");
            query.setResultClass(PasswordResetToken.class);

            List<PasswordResetToken> result = (List<PasswordResetToken>) query.execute(token);

            if (result != null && result.size() > 0)
                rval = result.get(0);

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean invalidateResetToken(String token)
    {
        Boolean rval = false;

        try
        {
            if (token == null || token.isEmpty())
                throw new IllegalArgumentException("Parameters must have at least one element.");

            PasswordResetToken tokenObject = getResetToken(token);

            if (tokenObject != null) {
                tokenObject.setValid(false);
                rval = true;
            }

        } catch (Exception e)
        {
            logError(e);
        }

        return rval;
    }

    @Override
    public Boolean updateUserPassword(Long userId, String password)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (userId == null || password == null || password.isEmpty())
                throw new IllegalArgumentException("Parameters must have at least one element.");

            User user = getUserAttached(userId, pm);

            user.setPassword(password);

            rval = true;

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean addItemToTrove(String identifier, Long itemId, User.SEARCH_BY_TYPE searchByType)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (identifier == null || itemId == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            User user = getUserAttached(identifier, searchByType, pm);
            Item item = itemAccessor.getItemAttached(itemId, pm);

            if (user != null && item != null)
            {
                user.getTrovedItems().add(new GroupedItem(user, item));
                rval = true;
            }

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean addItemToTrove(Long userId, Long itemId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (userId == null || itemId == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            User user = getUserAttached(userId, pm);
            Item item = itemAccessor.getItemAttached(itemId, pm);

            user.getTrovedItems().add(new GroupedItem(user, item));

            rval = true;

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Long addCollectionToUser(String email, Collection collection)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Long rval = null;

        try
        {
            if (email == null || collection == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            User user = getUserAttached(email, pm);

            if (user != null)
            {
                user.getUserCollections().add(collection);
                collection.setOwner(user);
                rval = collection.getCollectionId();
            }

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Long addCollectionToUser(Long userId, Collection collection)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Long rval = null;

        try
        {
            if (userId == null || collection == null)
                throw new NullPointerException("Arguments cannot be null");

            User user = getUserAttached(userId, pm);

            user.getUserCollections().add(collection);
            collection.setOwner(user);

            rval = collection.getCollectionId();
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean removeCollectionFromUser(String email, Long collectionId) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (email == null || collectionId == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            User user = getUserAttached(email, pm);
            Collection collectionToRemove = new Collection(collectionId);

            if (user != null)
            {
                //Direct removal using the .remove() that takes an Object as a reference doesn't seem to persist,
                //but removal by index does.
                int collectionIndexToRemove = user.getUserCollections().indexOf(collectionToRemove);

                if (collectionIndexToRemove > -1)
                    rval = user.getUserCollections().remove(collectionIndexToRemove) != null ? true : false;
            }

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean removeCollectionFromUser(Long userId, Long collectionId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (userId == null || collectionId == null)
                throw new NullPointerException("Arguments cannot be null");

            User user = getUserAttached(userId, pm);

            Collection searchCollection = new Collection();
            searchCollection.setCollectionId(collectionId);

            int collectionIndex = user.getUserCollections().indexOf(searchCollection);

            if (collectionIndex > -1)
            {
                user.getUserCollections().remove(collectionIndex);
                rval = true;
            }

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean addItemToCollection(String email, Long itemId, Long collectionId) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;
        Collection container = new Collection(collectionId);

        try
        {
            if (email == null || collectionId == null || itemId == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            User user = getUserAttached(email, pm);
            if (user != null)
            {
                //Check to make sure the requested collection that needs to be modified
                //is actually attached to this user
                int collectionToModify = user.getUserCollections().indexOf(container);

                if (collectionToModify > -1) {
                    Item item = itemAccessor.getItemAttached(itemId, pm);

                    if (item != null)
                    {
                        rval = user.getUserCollections().get(collectionToModify).getCollectionItems()
                                .add(new GroupedItem(user.getUserCollections().get(collectionToModify), item));

                        //Put the latest image up on the collection card
                        if (user.getUserCollections().get(collectionToModify).getCollectionImages().size() > 5)
                        {
                            user.getUserCollections().get(collectionToModify).getCollectionImages().remove(0);
                        }

                        user.getUserCollections().get(collectionToModify).
                                getCollectionImages().add(new CollectionImage(item.getImages().get(0).getMediumImageUrlPath()));
                    }
                }
            }

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean addItemToCollection(Long userId, Long itemId, Long collectionId) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;
        Collection container = new Collection(collectionId);

        try
        {
            if (userId == null || collectionId == null || itemId == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            User user = getUserAttached(userId, pm);
            if (user != null)
            {
                //Check to make sure the requested collection that needs to be modified
                //is actually attached to this user
                int collectionToModify = -1;

                if (user.getUserCollections() != null)
                    collectionToModify = user.getUserCollections().indexOf(container);

                if (collectionToModify > -1) {
                    Item item = itemAccessor.getItemAttached(itemId, pm);

                    if (item != null)
                    {
                        Collection focusedCollection = user.getUserCollections().get(collectionToModify);

                        if (focusedCollection.getCollectionImages() == null)
                        {
                            focusedCollection.setCollectionItems(new ArrayList<GroupedItem>());
                        }

                        rval = focusedCollection.getCollectionItems().add(new GroupedItem(focusedCollection, item));

                        //Put the latest image up on the collection card
                        if (focusedCollection.getCollectionImages().size() > 5)
                        {
                            focusedCollection.getCollectionImages().remove(0);
                        }

                        focusedCollection.
                                getCollectionImages().add(new CollectionImage(item.getImages().get(0).getMediumImageUrlPath()));

                        if (focusedCollection.getOverallRemakeCount() == null)
                            focusedCollection.setOverallRemakeCount(item.getRemadeCount());
                        else
                            focusedCollection.setOverallRemakeCount(focusedCollection.getOverallRemakeCount() +
                                    item.getRemadeCount());

                        if (focusedCollection.getOverallTroveCount() == null)
                            focusedCollection.setOverallTroveCount(item.getTrovedCount());
                        else
                            focusedCollection.setOverallTroveCount(focusedCollection.getOverallTroveCount() +
                                    item.getTrovedCount());

                    }
                }
            }

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean removeItemFromCollection(String email, Long itemId, Long collectionId) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;
        Collection container = new Collection(collectionId);

        try
        {
            if (email == null || collectionId == null || itemId == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            User user = getUserAttached(email, pm);
            if (user != null)
            {
                //Check to make sure the requested collection that needs to be modified
                //is actually attached to this user
                int collectionToModify = user.getUserCollections().indexOf(container);

                if (collectionToModify > -1) {
                    Item searchItem = new Item();
                    searchItem.setItemId(itemId);

                    int indexToRemove = user.getUserCollections().get(collectionToModify).getCollectionItems().indexOf(searchItem);

                    if (indexToRemove > -1) {

                        Item item = itemAccessor.getItemAttached(itemId, pm);

                        rval = user.getUserCollections().get(collectionToModify).getCollectionItems().remove(indexToRemove)
                                != null;

                        //If the item that is being removed has an image on the collection, remove that image
                        if (user.getUserCollections().get(collectionToModify).getCollectionImages().
                                contains(new CollectionImage(item.getImages().get(0).getMediumImageUrlPath())))
                        {
                            int deleteIndex = user.getUserCollections().get(collectionToModify).getCollectionImages().
                                    indexOf(new CollectionImage(item.getImages().get(0).getMediumImageUrlPath()));

                            user.getUserCollections().get(collectionToModify).getCollectionImages().remove(deleteIndex);
                        }

                        //If there are more than 5 items in the collection, and the collection is short an image,
                        //add the new image to the collection card face
                        if (user.getUserCollections().get(collectionToModify).getCollectionItems().size() > 5 &&
                                user.getUserCollections().get(collectionToModify).getCollectionImages().size() < 6)
                        {
                            String collectionImage = user.getUserCollections().
                                    get(collectionToModify).getCollectionItems().get(5).getGroupedItem().getImages().get(0).getMediumImageUrlPath();
                            user.getUserCollections().get(collectionToModify).getCollectionImages().
                                    add(new CollectionImage(collectionImage));
                        }

                    }
                }
            }

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean removeItemFromCollection(Long userId, Long itemId, Long collectionId) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;
        Collection container = new Collection(collectionId);

        try
        {
            if (userId == null || collectionId == null || itemId == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            User user = getUserAttached(userId, pm);
            if (user != null)
            {
                //Check to make sure the requested collection that needs to be modified
                //is actually attached to this user
                int collectionToModify = -1;

                if (user.getUserCollections() != null)
                    collectionToModify = user.getUserCollections().indexOf(container);

                if (collectionToModify > -1) {
                    Collection focusedCollection = user.getUserCollections().get(collectionToModify);
                    Item searchItem = new Item();
                    searchItem.setItemId(itemId);

                    int indexToRemove = -1;

                    if (focusedCollection != null &&
                            focusedCollection.getCollectionItems() != null)
                    {

                        for (int i = 0; i < focusedCollection.getCollectionItems().size(); ++i)
                        {
                            if (focusedCollection.getCollectionItems().get(i).getGroupedItem().equals(searchItem)) {
                                indexToRemove = i;
                                break;
                            }
                        }
                    }

                    if (indexToRemove > -1) {

                        Item item = itemAccessor.getItemAttached(itemId, pm);

                        rval = focusedCollection.getCollectionItems().remove(indexToRemove)
                                != null;

                        //If the item that is being removed has an image on the collection, remove that image
                        if (focusedCollection.getCollectionImages().
                                contains(new CollectionImage(item.getImages().get(0).getMediumImageUrlPath())))
                        {
                            int deleteIndex = user.getUserCollections().get(collectionToModify).getCollectionImages().
                                    indexOf(new CollectionImage(item.getImages().get(0).getMediumImageUrlPath()));

                            focusedCollection.getCollectionImages().remove(deleteIndex);
                        }

                        //If there are more than 5 items in the collection, and the collection is short an image,
                        //add the new image to the collection card face
                        if (focusedCollection.getCollectionItems().size() > 5 &&
                                focusedCollection.getCollectionImages().size() < 6)
                        {
                            String collectionImage = focusedCollection.
                                    getCollectionItems().get(5).getGroupedItem().getImages().get(0).getMediumImageUrlPath();
                            focusedCollection.getCollectionImages().add(new CollectionImage(collectionImage));
                        }

                    }
                }
            }

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateCollection(String email, Collection collection)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (email == null || collection == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            User user = getUserAttached(email, pm);
            if (user != null)
            {
                //Check to make sure the requested collection that needs to be modified
                //is actually attached to this user
                int collectionToModify = user.getUserCollections().indexOf(collection);

                if (collectionToModify > -1) {

                    if (collection.getCollectionName() != null)
                        user.getUserCollections().get(collectionToModify).setCollectionName(collection.getCollectionName());

                    if (collection.getCollectionDescription() != null)
                        user.getUserCollections().get(collectionToModify).setCollectionDescription(collection.getCollectionDescription());

                    rval = true;
                }
            }

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateCollection(Long userId, Long collectionId, String collectionName, String collectionDescription,
                                    Boolean privateCollection)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (userId == null || collectionId == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            User user = getUserAttached(userId, pm);
            if (user != null)
            {
                //Check to make sure the requested collection that needs to be modified
                //is actually attached to this user

                int collectionToModify = user.getUserCollections().indexOf(new Collection(collectionId));

                if (collectionToModify > -1) {

                    if (collectionName != null)
                        user.getUserCollections().get(collectionToModify).
                                setCollectionName(collectionName);

                    if (collectionDescription != null)
                        user.getUserCollections().get(collectionToModify).
                                setCollectionDescription(collectionDescription);

                    if (privateCollection != null)
                        user.getUserCollections().get(collectionToModify).setIsPrivate(privateCollection);

                    rval = true;
                }
            }

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean deleteCollection(Long userId, Long collectionId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (userId == null || collectionId == null)
                throw new NullPointerException("Arguments cannot be null");

            User user = getUserAttached(userId, pm);

            int searchIndex = user.getUserCollections().indexOf(new Collection(collectionId));

            if (searchIndex > -1)
            {
                user.getUserCollections().remove(searchIndex);
                rval = true;
            }


        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;

    }

    @Override
    public List<Collection> getPagedCollectionsByUserId(Long userId, Long collectionPage, Long collectionPageSize,
                                                        Long extraResults, Boolean includePrivate, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Collection> rval = null;

        try
        {
            if (userId == null || collectionPage == null || collectionPageSize == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            configureFetchGroups(pm, mode);

            Query query;

            if (includePrivate)
                query = pm.newQuery(Collection.class, "owner.userId == :userId");
            else
                query = pm.newQuery(Collection.class, "owner.userId == :userId && isPrivate == false");

            //Beginning of query range limit should be the current page * the collection size,
            //So if we're on page 2, and we've been showing 12 items per page, we should be on item,
            //36 as the beginning, then the end should be 48 + some extras in the event that there is overlap
            //from the item set having changed
            Long beginning = collectionPage * collectionPageSize;
            Long end = (collectionPage + 1) * collectionPageSize + extraResults;
            query.setRange(beginning, end);
            query.setOrdering("collectionId DESC");

            rval = (List<Collection>) query.execute(userId);

            if (rval != null && rval.size() > 0)
            {

                Map<Long, List<String>> collectionImageSet = new HashMap<>();

                //Grab a list of default item image URLs to set on the front of a collection
                for (Collection collection : rval)
                {
                    ArrayList<String> imageUrls = new ArrayList<>();

                    //Collections cards have 6 item images on their faces.  To get the latest of these images,
                    //Start at the very end of the array and walk forwards
                    int searchLength = collection.getCollectionItems().size() - 1 > 6 ? collection.getCollectionItems().size() - 6 : 0;

                    for (int i = collection.getCollectionItems().size() - 1; i >= searchLength; --i)
                    {
                        imageUrls.add(collection.getCollectionItems().get(i).getGroupedItem().getDefaultCardImageUrl());
                    }

                    collectionImageSet.put(collection.getCollectionId(), imageUrls);
                }

                rval = (List<Collection>) pm.detachCopyAll(rval);

                //Now that the objects are all detached, and further edits aren't going to make changes to the DB,
                //go ahead and update the collection card images
                for (Collection collection : rval)
                {
                    List<CollectionImage> newCollectionImages = new ArrayList<>();
                    if (collectionImageSet.keySet().contains(collection.getCollectionId()))
                    {
                        for (String imageUrl : collectionImageSet.get(collection.getCollectionId()))
                        {
                            newCollectionImages.add(new CollectionImage(imageUrl));
                        }
                    }

                    collection.setCollectionImages(newCollectionImages);
                }
            }


        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Address getAddressAttached(Long addressId, PersistenceManager pm)
    {
        Address rval = null;

        if (addressId == null || pm == null)
            throw new NullPointerException("Arguments cannot be null.");

        Query query = pm.newQuery(Address.class, "addressId == :addressId");
        query.setResultClass(Address.class);

        List<Address> queryResult = (List<Address>) query.execute(addressId);

        if (queryResult != null && queryResult.size() > 0)
            rval = queryResult.get(0);

        return rval;
    }

    @Override
    public Boolean addShoppingCartToUser(Long userId, Cart shoppingCart)
    {
        Boolean rval = false;
        PersistenceManager pm = pmf.getPersistenceManager();

        try
        {
            if (userId == null || shoppingCart == null)
                throw new NullPointerException("Arguments cannot be null.");

            List<CartItem> cartItems = new ArrayList<>();
            Cart persistedCart = null;

            if (shoppingCart.getCartId() != null)
            {
                persistedCart = cartAccessor.getShoppingCartAttached(shoppingCart.getCartId(), pm);
                CartAccessor.getManagedCartAddresses(persistedCart, pm, countryAccessor, this);
            }
            //We're going to need to get managed versions of all CartItems that already exist.  All new CartItems
            //that have already existing Items will need to be managed, then added to existing CartItems.
            else if (shoppingCart.getCartItems() != null)
            {
                List<Item> persistedItems = new ArrayList<>();
                List<Long> unpersistenceManagedCartItem = new ArrayList<>();
                Map<String, CartItem> itemIdsForSeekItems = new HashMap<>();
                for(CartItem cartItem : shoppingCart.getCartItems())
                {
                    //If this is a new cart-item but it has an item that should be managed, we have to get the managed item
                    //to add to a new cart-item so that a new item isn't persisted when the whole thing is created
                    if (cartItem.getCartItemId() == null && cartItem.getCartItemReference() != null && cartItem.getCartItemReference().getItemId() != null)
                        itemIdsForSeekItems.put(cartItem.getCartItemReference().getItemId().toString(), cartItem);

                    //Otherwise add the cart-item to the list of items that needs to grab the managed version
                    else if (cartItem.getCartItemId() != null)
                    {
                        unpersistenceManagedCartItem.add(cartItem.getCartItemId());
                    }
                    //Otherwise it's a new CartItem with new Item
                    else
                    {
                        cartItems.add(cartItem);
                    }
                }

                //Iterate through all non-persisted items within the map and grab persisted versions of them.  Then
                //re-add the persisted items to the cart-items in the map
                List<String> setConversion = new ArrayList<>();
                setConversion.addAll(itemIdsForSeekItems.keySet());
                persistedItems = itemAccessor.getItems(setConversion, Item.SEARCH_BY_TYPE.IDENTITY_COLUMN_NAME, pm);

                for (Item persistedItem : persistedItems)
                {
                    itemIdsForSeekItems.get(persistedItem.getItemId().toString()).setCartItemReference(persistedItem);
                }

                cartItems.addAll(cartAccessor.getCartItemsAttached(unpersistenceManagedCartItem, pm));
                cartItems.addAll(itemIdsForSeekItems.values());

                shoppingCart.setCartItems(cartItems);
            }

            CartAccessor.getManagedCartAddresses(shoppingCart, pm, countryAccessor, this);

            User user = getUserAttached(userId, pm);

            if (persistedCart != null) {
                persistedCart.setCartOwner(user);
                user.setShoppingCart(persistedCart);
            }
            else {
                shoppingCart.setCartOwner(user);
                user.setShoppingCart(shoppingCart);
            }

            rval = true;

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean persistNewUserOrder(Long userId, Order order)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (userId == null || order == null)
                throw new NullPointerException("Arguments cannot be null.");

            //Get a persistence managed version of the cart so that it isn't duplicated
            order.setCartReference(cartAccessor.getShoppingCartAttached(order.getCartReference().getCartId(), pm));

            List<CartItem> managedItems = new ArrayList<>();
            List<GenericItem> managedGenericItems = new ArrayList<>();

            //Get a persistence managed version of each cart item so that they aren't duplicated
            for (int i = 0; i < order.getOrderItems().size(); ++i)
            {
                CartItem item = cartAccessor.getCartItemAttached(order.getCartReference().getCartItems().get(i).getCartItemId(), pm);
                managedItems.add(item);
            }

            //Get a persistence managed version of each generic item so that they aren't duplicated
            for (int i = 0; i < order.getGenericItemsList().size(); ++i)
            {
                GenericItem item = cartAccessor.getGenericItemAttached(order.getGenericItemsList().get(i).getGenericItemId(), pm);
                managedGenericItems.add(item);

            }

            order.setOrderItems(managedItems);
            order.setGenericItemsList(managedGenericItems);
            OrderAccessor.getManagedOrderAddresses(order, pm, countryAccessor, this);

            User user = getUserAttached(userId, pm);

            rval = user.getOrders().add(order);


        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean removeCurrentCart(Long userId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (userId == null)
                throw new NullPointerException("Arguments cannot be null.");

            User user = getUserAttached(userId, pm);
            user.getShoppingCart().setCartOwner(null);
            Cart cart = new Cart();
            cart.setCartOwner(user);
            user.setShoppingCart(cart);

            rval = true;


        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateUserProfileImage(Long userId, String profileImagePath, String profileThumbnailPath)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (userId == null || profileImagePath == null || profileThumbnailPath == null)
                throw new NullPointerException("Arguments cannot be null.");

            User user = getUserAttached(userId, pm);

            user.setFullProfileImagePath(profileImagePath);
            user.setProfileImageThumbnailPath(profileThumbnailPath);

            rval = true;

        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateUserCoverPhotoImage(Long userId, String coverPhotoImagePath)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (userId == null || coverPhotoImagePath == null)
                throw new NullPointerException("Arguments cannot be null.");

            User user = getUserAttached(userId, pm);

            user.setCoverPhotoImagePath(coverPhotoImagePath);

            rval = true;

        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean followCollection(Long userId, Long collectionId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (userId == null || collectionId == null)
                throw new NullPointerException("Arguments cannot be null");

            Collection collection = getCollectionAttached(collectionId, pm);

            User user = getUserAttached(userId, pm);

            rval = collection.getFollowed().add(new Follow(collection, user));

        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean unfollowCollection(Long userId, Long collectionId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (userId == null || collectionId == null)
                throw new NullPointerException("Arguments cannot be null");

            Collection collection = getCollectionAttached(collectionId, pm);

            int searchIndex = -1;
            if (collection.getFollowed() != null)
            {
                for (int i = 0; i < collection.getFollowed().size(); ++i)
                {
                    if (collection.getFollowed().get(i).getUserFollower().getUserId().equals(userId))
                        searchIndex = i;
                    break;
                }
            }

            if (searchIndex > -1)
            {
                rval = collection.getFollowed().remove(searchIndex) != null;
            }
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean incrementItemCollectionTroveCount(Long itemId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (itemId == null)
                throw new NullPointerException("Argument cannot be null");

            Query query = pm.newQuery(Collection.class, "collectionItems.contains(item) && item.groupedItem.itemId == :itemId");

            query.declareVariables("com.troveup.brooklyn.orm.user.model.GroupedItem item");

            List<Collection> queryList = (List<Collection>) query.execute(itemId);

            if (queryList != null)
            {
                for (Collection relatedCollection : queryList)
                {
                    if (relatedCollection.getOverallTroveCount() == null)
                        relatedCollection.setOverallTroveCount(1l);
                    else
                        relatedCollection.setOverallTroveCount(relatedCollection.getOverallTroveCount() + 1);
                    rval = true;
                }
            }

        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean incrementItemCollectionRemakeCount(Long itemId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (itemId == null)
                throw new NullPointerException("Argument cannot be null");

            Query query = pm.newQuery(Collection.class, "collectionItems.contains(item) && item.groupedItem.itemId == :itemId");

            query.declareVariables("com.troveup.brooklyn.orm.user.model.GroupedItem item");

            List<Collection> queryList = (List<Collection>) query.execute(itemId);

            if (queryList != null)
            {
                for (Collection relatedCollection : queryList)
                {
                    if (relatedCollection.getOverallRemakeCount() == null)
                        relatedCollection.setOverallRemakeCount(1l);
                    else
                        relatedCollection.setOverallRemakeCount(relatedCollection.getOverallRemakeCount() + 1);
                    rval = true;
                }
            }

        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Collection getCollectionAttached(Long collectionId, PersistenceManager pm)
    {
        Collection rval = null;

        Query query = pm.newQuery(Collection.class, "collectionId == :collectionId");

        List<Collection> queryList = (List<Collection>) query.execute(collectionId);

        if (queryList != null && queryList.size() > 0)
            rval = queryList.get(0);

        return rval;
    }

    @Override
    public Boolean doesUserOwnCollection(Long collectionId, Long userId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (collectionId == null || userId == null)
                throw new NullPointerException("Arguments cannot be null");

            Collection collection = getCollectionAttached(collectionId, pm);

            if (collection.getOwner().getUserId().equals(userId))
                rval = true;

        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public User getUserByCollectionId(Long collectionId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        User rval = null;

        try
        {
            if (collectionId == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            Collection collection = getCollectionAttached(collectionId, pm);

            rval = collection.getOwner();

            rval = pm.detachCopy(rval);

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<User> getPagedFollowersByUserId(Long userId, Integer collectionPage, Long collectionPageSize, Long extraResults,
                                                IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<User> rval = null;

        try
        {
            if (userId == null || collectionPage == null || collectionPageSize == null || extraResults == null ||
                    mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(Follow.class,"user.followedUsers.contains(this) &&"  +
                    "this.userFollower.userId == :userId");

            query.declareVariables("com.troveup.brooklyn.orm.user.model.User user");

            Long beginning = collectionPage * collectionPageSize;
            Long end = (collectionPage + 1) * collectionPageSize + extraResults;
            query.setRange(beginning, end);
            query.setOrdering("followedDate DESC");

            List<Follow> queryList = (List<Follow>) query.execute(userId);

            rval = Follow.ownerToUserList(queryList);

            rval = (List<User>) pm.detachCopyAll(rval);
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<User> getPagedFollowedByUserId(Long userId, Integer collectionPage, Long collectionPageSize, Long extraResults,
                                               IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<User> rval = null;

        try
        {
            if (userId == null || collectionPage == null || collectionPageSize == null || extraResults == null ||
                    mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(Follow.class,"user.followedUsers.contains(this) &&"  +
                    "user.userId == :userId");

            query.declareVariables("com.troveup.brooklyn.orm.user.model.User user");

            Long beginning = collectionPage * collectionPageSize;
            Long end = (collectionPage + 1) * collectionPageSize + extraResults;
            query.setRange(beginning, end);
            query.setOrdering("followedDate DESC");

            List<Follow> queryList = (List<Follow>) query.execute(userId);

            rval = Follow.followToUserList(queryList);

            rval = (List<User>) pm.detachCopyAll(rval);
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<User> getPagedFollowersOfCollection(Long collectionId, Integer collectionPage,
                                                    Long collectionPageSize, Long extraResults, IEnums.SEEK_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<User> rval = null;

        try {
            if (collectionId == null || collectionPage == null || collectionPageSize == null || extraResults == null ||
                    mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(Follow.class, "collection.collectionId == :collectionId && " +
                    "collection.followed.contains(this)");

            query.declareVariables("com.troveup.brooklyn.orm.user.model.Collection collection");

            Long beginning = collectionPage * collectionPageSize;
            Long end = (collectionPage + 1) * collectionPageSize + extraResults;
            query.setRange(beginning, end);
            query.setOrdering("followedDate DESC");

            List<Follow> queryList = (List<Follow>) query.execute(collectionId);

            rval = Follow.followToUserList(queryList);

            rval = (List<User>) pm.detachCopyAll(rval);

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Long getCollectionCount(Long userId, Boolean includePrivate)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Long rval = -1l;

        try
        {
            if (userId == null || includePrivate == null)
                throw new NullPointerException("Arguments cannot be null");

            Query query;

            if (includePrivate)
                query = pm.newQuery(Collection.class, "owner.userId == :userId");
            else
                query = pm.newQuery(Collection.class, "owner.userId == :userId && isPrivate == false");

            query.setResult("count(this)");

            rval = (Long) query.execute(userId);

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Long getCollectionFollowCount(Long collectionId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Long rval = -1l;

        try
        {
            if (collectionId == null)
                throw new NullPointerException("Argument cannot be null");

            Query query = pm.newQuery(Follow.class, "collection.collectionId == :collectionId && " +
                    "collection.followed.contains(this)");

            query.declareVariables("com.troveup.brooklyn.orm.user.model.Collection collection");

            query.setResult("count(this)");

            rval = (Long) query.execute(collectionId);

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Collection getCollectionByCollectionId(Long collectionId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Collection rval = null;

        try {

            if (collectionId == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(Collection.class, "collectionId == :collectionId");

            List<Collection> queryList = (List<Collection>) query.execute(collectionId);

            if (queryList != null && queryList.size() > 0) {
                rval = queryList.get(0);
                rval = pm.detachCopy(rval);
            }
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateUserOptOutFlag(Long userId, Boolean optOutStatus)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (userId == null)
                throw new NullPointerException("Arguments cannot be null.");

            User user = getUserAttached(userId, pm);
            user.setOptOut(optOutStatus);

            rval = true;


        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateUserFirstLastName(Long userId, String firstName, String lastName)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (userId == null)
                throw new NullPointerException("userId cannot be null.");

            User user = getUserAttached(userId, pm);

            if (firstName != null)
                user.setFirstName(firstName);

            if (lastName != null)
                user.setLastName(lastName);

            rval = true;


        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<User> getFollowedUsers(Long userId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<User> rval = null;

        try
        {
            if (userId == null || mode == null)
                throw new NullPointerException("Arguments cannot be null.");

            configureFetchGroups(pm, mode);

            User user = getUserAttached(userId, pm);

            List<User> convertedFollows = Follow.followToUserList(user.getFollowedUsers());

            rval = (List<User>) pm.detachCopyAll(convertedFollows);


        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<User> getUsersWhoFollowedUser(Long userId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<User> rval = null;

        try
        {
            if (userId == null || mode == null)
                throw new NullPointerException("Arguments cannot be null.");

            configureFetchGroups(pm, mode);

            User searchUser = getUserAttached(userId, pm);

            Query query = pm.newQuery(User.class, "followedUsers.contains(follow) && " +
                    "follow.userFollower.userId == :userId");

            query.declareVariables("com.troveup.brooklyn.orm.user.model.Follow follow");

            query.setResultClass(User.class);

            rval = (List<User>) pm.detachCopyAll((List<User>) query.execute(searchUser));


        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Integer getFollowedUserCount(Long userId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Integer rval = 0;

        try
        {
            if (userId == null)
                throw new NullPointerException("Arguments cannot be null.");

            //TODO:  Not mission critical, but doing the count on the DB is more efficient than pulling in data and counting it.
            //Doesn't seem to work, switching to guns
            /*Query query = pm.newQuery(User.class, "userId == :userId");
            query.setResult("count(this.followedUsers)");

            rval = (Integer) query.execute(userId);*/

            User user = getUserAttached(userId, pm);

            rval = user.getFollowedUsers().size();


        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Long getUsersWhoFollowedUserCount(Long userId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Long rval = null;

        try
        {
            if (userId == null)
                throw new NullPointerException("Arguments cannot be null.");

            Query query = pm.newQuery(User.class, "followedUsers.contains(follow) && " +
                    "follow.userFollower.userId == :userId");
            query.setResult("count(this)");

            query.declareVariables("com.troveup.brooklyn.orm.user.model.Follow follow");

            rval = (Long) query.execute(userId);


        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean followUser(Long followerId, Long followeeId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (followerId == null || followeeId == null)
                throw new NullPointerException("Arguments cannot be null.");

            User followerUser = getUserAttached(followerId, pm);
            User followeeUser = getUserAttached(followeeId, pm);

            if (followerUser.getFollowedUsers() == null)
                followerUser.setFollowedUsers(new ArrayList<Follow>());

            followerUser.getFollowedUsers().add(new Follow(followerUser, followeeUser));

            rval = true;

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean unfollowUser(Long followerId, Long followeeId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (followerId == null || followeeId == null)
                throw new NullPointerException("Arguments cannot be null.");

            User followerUser = getUserAttached(followerId, pm);

            int followeeIndex = -1;
            for (int i = 0; i < followerUser.getFollowedUsers().size(); ++i)
            {
                if (followerUser.getFollowedUsers().get(i).getUserFollower().getUserId().equals(followeeId)) {
                    followeeIndex = i;
                    break;
                }
            }

            if (followeeIndex > -1)
                rval = followerUser.getFollowedUsers().remove(followeeIndex) != null;

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean checkUserFollow(Long followerId, Long followeeId) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = null;

        try
        {
            if (followerId == null || followeeId == null)
                throw new NullPointerException("Arguments cannot be null.");

            Query query = pm.newQuery(User.class, "userId == :followerId && followedUsers.contains(follow) && " +
                    "follow.userFollower.userId == :followeeId");
            query.setResult("count(this)");

            query.declareVariables("com.troveup.brooklyn.orm.user.model.Follow follow");

            Long queryCount = (Long) query.execute(followerId, followeeId);

            rval = queryCount > 0;

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean setUserCompletedFtui(Long userId, Boolean hasCompletedFtue)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            User user = getUserAttached(userId, pm);

            user.setHasUsedFtue(hasCompletedFtue);

            rval = true;
        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean removeItemFromTrove(Long userId, Long itemId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            User user = getUserAttached(userId, pm);

            int searchIndex = -1;
            for (int i = 0; i < user.getTrovedItems().size(); ++i)
            {
                if (user.getTrovedItems().get(i).getGroupedItem().getItemId().equals(itemId))
                {
                    searchIndex = i;
                    break;
                }
            }

            if (searchIndex > -1)
                rval = user.getTrovedItems().remove(searchIndex) != null;


        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean checkUserAdmin(Long userId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (userId == null)
                throw new NullPointerException("Argument cannot be null");

            User user = getUserAttached(userId, pm);

            rval = user.getIsAdmin();

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public String getUserReferralCode(Long userId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        String rval = null;

        try
        {
            if (userId == null)
                throw new NullPointerException("Argument cannot be null");

            User user = getUserAttached(userId, pm);

            //There was no referral code, generate one
            if (user.getReferralCode() == null || user.getReferralCode().length() == 0)
            {
                String potentialReferralCode = UUID.randomUUID().toString().substring(0, 7).toUpperCase();

                Boolean uniquenessCheck = doesReferralCodeExist(potentialReferralCode);

                while(uniquenessCheck)
                {
                    potentialReferralCode = UUID.randomUUID().toString().substring(0, 7).toUpperCase();
                    uniquenessCheck = doesReferralCodeExist(potentialReferralCode);
                }

                user.setReferralCode(potentialReferralCode);
                rval = potentialReferralCode;
            }
            //Otherwise, return the user's current referral code
            else
                rval = user.getReferralCode();

        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean setUserReferrerCode(Long userId, String referrerCode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (userId == null)
                throw new NullPointerException("Argument cannot be null");

            User user = getUserAttached(userId, pm);

            user.setReferrerCode(referrerCode);

            rval = true;
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public String getUserReferrerCode(Long userId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        String rval = null;

        try
        {
            if (userId == null)
                throw new NullPointerException("Argument cannot be null");

            User user = getUserAttached(userId, pm);

            rval = user.getReferrerCode();
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Long getReferralCountByUserId(Long userId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Long rval = 0l;

        try
        {
            if (userId == null)
                throw new NullPointerException("Argument cannot be null");

            User user = getUserAttached(userId, pm);

            if (user.getReferralCode() != null && user.getReferralCode().length() > 0)
            {
                Query query = pm.newQuery(User.class, "referrerCode == :referralCode");
                query.setResult("count(this)");
                rval = (Long) query.execute(user.getReferralCode());
            }
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public User getUserByReferralCode(String referralCode, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        User rval = null;

        try
        {
            if (referralCode == null || referralCode.length() == 0)
                throw new NullPointerException("Argument cannot be null");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(User.class, "referralCode == :referralCode");
            List<User> queryList = (List<User>) query.execute(referralCode);

            if (queryList != null && queryList.size() > 0) {
                rval = queryList.get(0);
                rval = pm.detachCopy(rval);
            }


        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateUserBraintreeVaultId(Long userId, String vaultId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (userId == null || vaultId == null || vaultId.length() == 0)
                throw new NullPointerException("Arguments cannot be null or empty");

            User user = getUserAttached(userId, pm);
            user.setBraintreeUserId(vaultId);

            rval = true;

        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public BigDecimal getUserStoreCredit(Long userId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        BigDecimal rval = Money.zero(CurrencyUnit.USD).getAmount();

        try
        {
            if (userId == null)
                throw new NullPointerException("Argument cannot be null");

            User user = getUserAttached(userId, pm);

            rval = user.getStoreBalance();

        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public User getUserByOrderId(Long orderId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        User rval = null;

        try
        {
            if (orderId == null || mode == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            configureFetchGroups(pm, mode);

            Order order = orderAccessor.getOrderAttached(orderId, pm);
            Query query = pm.newQuery(User.class, "orders.contains(:order)");

            List<User> queryResult = (List<User>) query.execute(order);

            if (queryResult != null && queryResult.size() > 0)
            {
                rval = queryResult.get(0);

                rval = pm.detachCopy(rval);
            }

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public BigDecimal addUserStoreCredit(Long userId, BigDecimal amount)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        BigDecimal rval = BigDecimal.ZERO;

        try
        {
            if (userId == null || amount == null)
                throw new NullPointerException("Arguments cannot be null");

            User user = getUserAttached(userId, pm);

            if (user.getStoreBalance() == null)
                user.setStoreBalance(BigDecimal.ZERO);

            user.setStoreBalance(user.getStoreBalance().add(amount));

            rval = user.getStoreBalance();

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public BigDecimal subtractUserStoreCredit(Long userId, BigDecimal amount) {
        PersistenceManager pm = pmf.getPersistenceManager();
        BigDecimal rval = BigDecimal.ZERO;

        try
        {
            if (userId == null || amount == null)
                throw new NullPointerException("Arguments cannot be null");

            User user = getUserAttached(userId, pm);

            if (user.getStoreBalance() == null || user.getStoreBalance().compareTo(amount) == -1)
                user.setStoreBalance(BigDecimal.ZERO);
            else
                user.setStoreBalance(user.getStoreBalance().subtract(amount));

            rval = user.getStoreBalance();


        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateUserShippingAddress(Long userId, Address shippingAddress)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (userId == null || shippingAddress == null)
                throw new NullPointerException("Arguments cannot be null");

            Address managedAddress = getManagedAddressFields(shippingAddress, pm, countryAccessor, this);

            User user = getUserAttached(userId, pm);
            user.setShippingAddress(managedAddress);
            rval = true;

        } catch (Exception e)
        {
            logError(e);
        } finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateUsername(Long userId, String username)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        if (userId == null || username == null || username.length() == 0)
            throw new NullPointerException("Arguments cannot be null or empty");

        try {
            User user = getUserAttached(userId, pm);
            user.setUsername(username);
            rval = true;
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean generateAndReplaceUsername(Long userId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {

            if (userId == null)
                throw new NullPointerException("Argument cannot be null");

            User user = getUserAttached(userId, pm);
            user.setUsername(generateUsername(user.getFirstName()));

            rval = true;

        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public User createGenericPseudoUser(String randomEncodedPassword)
    {
        String randomString = StringUtils.generateRandomUppercaseString(10);
        User rval = new User();
        rval.setRole(UserDetails.Role.ROLE_USER);
        rval.setFirstName(randomString);
        rval.setLastName("");
        rval.setEmail(generateRandomUniqueEmail());
        rval.setUsername(generateUsername(randomString));
        rval.setPassword(randomEncodedPassword);
        rval.setFullProfileImagePath("https://storage.googleapis.com/troveup-imagestore/assets/img/default-user-icon.jpg");
        rval.setProfileImageThumbnailPath("https://storage.googleapis.com/troveup-imagestore/assets/img/default-user-icon-thumb.jpg");
        rval.setCoverPhotoImagePath("https://storage.googleapis.com/troveup-imagestore/assets/default_cover_photo.png");
        rval.setHasUsedFtue(false);
        rval.setEnabled(false);
        rval.setIsAdmin(false);
        rval.setPseudoUser(true);

        try {
            createUser(rval);
        } catch (Exception e) {
            logError(e);
        }

        return rval;
    }

    private String generateRandomUniqueEmail()
    {
        String MAILINATOR_DOMAIN = "@mailinator.com";

        String potentialEmail = StringUtils.generateRandomUppercaseString(10) + MAILINATOR_DOMAIN;

        while(doesEmailExist(potentialEmail))
        {
            potentialEmail = StringUtils.generateRandomUppercaseString(10) + MAILINATOR_DOMAIN;
        }

        return potentialEmail;
    }

    private Boolean doesEmailExist(String email)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (email == null || email.length() == 0)
                throw new NullPointerException("Argument cannot be null");

            Query query = pm.newQuery(User.class, "email == :email");
            query.setResult("count(this)");

            Long resultCount = (Long) query.execute(email);

            rval = resultCount > 0;

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;

    }

    @Override
    public User mergeUserAccounts(Long sourceUserId, Long destinationUserId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        User destinationUser = null;

        try
        {
            if (sourceUserId == null || destinationUserId == null)
                throw new NullPointerException("Arguments cannot be null");

            User sourceUser = getUserAttached(sourceUserId, pm);
            destinationUser = getUserAttached(destinationUserId, pm);


            if (destinationUser.getOrders() == null)
            {
                destinationUser.setOrders(new ArrayList<Order>());
            }
            if (sourceUser.getOrders().size() > 0)
            {
                for (Order order : sourceUser.getOrders())
                {
                    destinationUser.getOrders().add(order);
                }
            }

            if (destinationUser.getTrovedItems() == null)
            {
                destinationUser.setTrovedItems(new ArrayList<GroupedItem>());
            }
            if (sourceUser.getTrovedItems().size() > 0)
            {
                for (GroupedItem item : sourceUser.getTrovedItems())
                {
                    destinationUser.getTrovedItems().add(item);
                }
            }

            if (destinationUser.getOwnedItems() == null)
            {
                destinationUser.setOwnedItems(new ArrayList<GroupedItem>());
            }
            if (sourceUser.getOwnedItems().size() > 0)
            {
                for (GroupedItem item : sourceUser.getOwnedItems())
                {
                    destinationUser.getOwnedItems().add(item);
                }
            }

            //Merge the shopping carts if the destination user has items in their bag
            if (sourceUser.getShoppingCart() != null && (sourceUser.getShoppingCart().getCartItems().size() > 0 || sourceUser.getShoppingCart().getGenericItems().size() > 0))
            {
                //Cart dancing because datanucleus is struggling to persist an empty cart for some reason
                Boolean newCartCreated = false;
                Cart destinationCart = destinationUser.getShoppingCart();
                if (destinationCart == null)
                {
                    destinationCart = new Cart();
                    newCartCreated = true;
                }

                //Cart items
                if (destinationCart.getCartItems() == null)
                {
                    destinationCart.setCartItems(new ArrayList<CartItem>());
                }
                for (CartItem item : sourceUser.getShoppingCart().getCartItems())
                {
                    destinationCart.getCartItems().add(item);
                }

                //Generic items
                if (destinationCart.getGenericItems() == null)
                {
                    destinationCart.setGenericItems(new ArrayList<GenericItem>());
                }
                for (GenericItem item : sourceUser.getShoppingCart().getGenericItems())
                {
                    destinationCart.getGenericItems().add(item);
                }

                //There are promo codes on the source cart.  They'll need to be merged into the destination cart.
                if (sourceUser.getShoppingCart().getAppliedPromoCodes() !=
                        null && sourceUser.getShoppingCart().getAppliedPromoCodes().size() > 0)
                {

                    //TODO:  Implement destination user promo code checking rules if users figure out that merging doesn't check stacking rules
                    if (destinationCart.getAppliedPromoCodes() == null)
                    {
                        destinationCart.setAppliedPromoCodes(new ArrayList<PromoCode>());

                        for (PromoCode code : sourceUser.getShoppingCart().getAppliedPromoCodes())
                        {
                            destinationCart.getAppliedPromoCodes().add(code);
                        }
                    }
                }

                if (sourceUser.getShoppingCart().getShippingAddress() != null)
                {
                    destinationCart.setShippingAddress(sourceUser.getShippingAddress());
                }

                if (sourceUser.getShoppingCart().getBillingAddress() != null)
                {
                    destinationCart.setBillingAddress(sourceUser.getBillingAddress());
                }

                destinationCart.setShouldBeGiftWrapped(sourceUser.getShoppingCart().getShouldBeGiftWrapped());
                destinationCart.setIncludeGiftMessageCard(sourceUser.getShoppingCart().getIncludeGiftMessageCard());
                destinationCart.setGiftMessageCardText(sourceUser.getShoppingCart().getGiftMessageCardText());
                destinationCart.setPaymentDetails(sourceUser.getShoppingCart().getPaymentDetails());

                if (newCartCreated)
                {
                    addShoppingCartToUser(destinationUser.getUserId(), destinationCart);
                }

                cartAccessor.refreshGrandTotal(destinationCart.getCartId(), false, false);
            }

            if (destinationUser.getUsedPromoCodes() == null)
            {
                destinationUser.setUsedPromoCodes(new ArrayList<PromoCode>());
            }
            if (sourceUser.getUsedPromoCodes() != null && sourceUser.getUsedPromoCodes().size() > 0)
            {
                for (PromoCode code : sourceUser.getUsedPromoCodes())
                {
                    destinationUser.getUsedPromoCodes().add(code);
                }
            }

            if (sourceUser.getStoreBalance() != null) {
                if (destinationUser.getStoreBalance() != null) {
                    BigDecimal summedBalance = destinationUser.getStoreBalance().add(sourceUser.getStoreBalance());
                    destinationUser.setStoreBalance(summedBalance);
                }
                else
                {
                    destinationUser.setStoreBalance(sourceUser.getStoreBalance());
                }
            }


            Query query = pm.newQuery(Item.class, "itemOwner.userId == :sourceUserId");
            List<Item> queryList = (List<Item>) query.execute(sourceUserId);

            if (queryList != null && queryList.size() > 0)
            {
                for (Item item : queryList)
                {
                    item.setItemOwner(destinationUser);
                }
            }


        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return destinationUser;
    }

    @Override
    public Boolean setUserDisabled(Long userId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (userId == null)
                throw new NullPointerException("Argument cannot be null");

            User user = getUserAttached(userId, pm);
            user.setEnabled(false);

            rval = true;
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<User> getAllInfluencers(IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<User> rval = null;

        try
        {
            if (mode == null)
                throw new NullPointerException("Argument cannot be null");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(User.class, "influencer == true");
            rval = (List<User>) query.execute();

            rval = (List<User>) pm.detachCopyAll(rval);

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Signup createSignup(Signup signup)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Signup rval = null;

        try {
            if (signup == null)
                throw new NullPointerException("Argument cannot be null.");

            //Check if one exists already to prevent data duplication
            Query query = pm.newQuery(Signup.class, "email == :email && type == :type");
            List<Signup> queryResult = (List<Signup>) query.execute(signup.getEmail(), signup.getType());

            if (queryResult == null || queryResult.size() < 1)
            {
                rval = pm.makePersistent(signup);
            }
        } catch (Exception e)
        {
            logError(e);
        } finally
        {
            pm.close();
        }

        return rval;
    }

    private String generateUsername(String name)
    {
        String baseName = name.trim().replace(" ", "-").toLowerCase();
        int i = 0;

        while(doesUsernameExist(baseName + "-" + i))
        {
            i += 1;
        }

        return baseName + "-" + i;
    }

    private Boolean doesUsernameExist(String username)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (username == null || username.length() == 0)
                throw new NullPointerException("Argument cannot be null");

            Query query = pm.newQuery(User.class, "username == :username");
            query.setResult("count(this)");

            Long resultCount = (Long) query.execute(username);

            rval = resultCount > 0;

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;

    }

    public Boolean doesReferralCodeExist(String referralCode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = true;

        try
        {
            if (referralCode == null || referralCode.length() == 0)
                throw new NullPointerException("Argument cannot be null or empty");

            Query query = pm.newQuery(User.class, "referralCode == :referralCode");
            query.setResult("count(this)");

            Long result = (Long) query.execute(referralCode);

            rval = result > 0l;
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public static Address getManagedAddressFields(Address address, PersistenceManager pm, ICountryAccessor countryAccessor, IUserAccessor userAccessor)
    {
        Address rval = address;

        if (rval != null)
        {
            if (rval.getAddressId() != null)
            {
                rval = userAccessor.getAddressAttached(rval.getAddressId(), pm);
            }
            else {
                if (rval.getCountry() != null && rval.getCountry().getPrimaryKeyId() != null)
                    rval.setCountry(countryAccessor.getCountryAttached(rval.getCountry().getPrimaryKeyId(), pm));

                //Get a persistent managed object and set that as the subdivision
                if (rval.getSubdivision() != null && rval.getSubdivision().getPrimaryKeyId() != null)
                    rval.setSubdivision(countryAccessor.getSubdivisionAttached(rval.getSubdivision().getPrimaryKeyId(), pm));
            }
        }

        return rval;
    }

    private void configureFetchGroups(PersistenceManager pm, IEnums.SEEK_MODE mode)
    {
        //DataNucleus will only pull surface level data unless explicitly instructed to do so.  If we need to pull
        //the whole user, we need to add extra fetch groups for the collections.
        //Reference:  http://www.datanucleus.org/products/datanucleus/jdo/fetchgroup.html
        if (mode != null && mode != IEnums.SEEK_MODE.QUICK)
        {
            //Very expensive, only use when you need every single detail about the user's profile
            if (mode == IEnums.SEEK_MODE.FULL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(User.class, "fullFetch");

                //Add the user fetch group fields
                List<String> fetchGroupFields = User.getUserFullFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Collection.class, "collectionFetch");

                fetchGroupFields = Collection.getFullCollectionFetchGroupFields();

                //Add the Collection fetch group fields
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Cart.class, "cartFetch");
                fetchGroupFields = Cart.getCartFullFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(CartItem.class, "fullCartItemFetch");
                fetchGroupFields = CartItem.getCartItemFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Item.class, "fullItemFetch");
                fetchGroupFields = Item.getFullItemFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("fullFetch");
                fetchPlan.addGroup("collectionFetch");
                fetchPlan.addGroup("cartFetch");
                fetchPlan.addGroup("fullCartItemFetch");
                fetchPlan.addGroup("fullItemFetch");
                fetchPlan.setMaxFetchDepth(4);
            }
            //Only pulls the direct cart rows, none of its relational linkages
            else if (mode == IEnums.SEEK_MODE.USER_CART_QUICK)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(User.class, "quickUserCartFetch");

                List<String> fetchGroupFields = User.getUserQuickCartFetchGroupFieldsPII();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Cart.class, "quickCartFetch");
                fetchGroupFields = Cart.getCartQuickFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("quickUserCartFetch");
                fetchPlan.addGroup("quickCartFetch");
                fetchPlan.setMaxFetchDepth(2);
            }
            else if (mode == IEnums.SEEK_MODE.USER_CART_PREPARE)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(User.class, "quickUserCartFetch");

                List<String> fetchGroupFields = User.getUserQuickCartFetchGroupFieldsPII();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Cart.class, "quickCartFetch");
                fetchGroupFields = Cart.getCartAddressAndCartItemFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("quickUserCartFetch");
                fetchPlan.addGroup("quickCartFetch");
                fetchPlan.setMaxFetchDepth(4);
            }
            else if (mode == IEnums.SEEK_MODE.USER_CART_CARTITEMS)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(User.class, "quickUserCartFetch");

                List<String> fetchGroupFields = User.getUserQuickCartFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Cart.class, "cartItemFetch");
                fetchGroupFields = Cart.getCartItemsFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("quickUserCartFetch");
                fetchPlan.addGroup("cartItemFetch");
                fetchPlan.setMaxFetchDepth(2);
            }
            //Pulls the full cart, along with its cart items, item references, and all of the item attributes and images
            else if (mode == IEnums.SEEK_MODE.USER_CART_FULL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(User.class, "quickUserCartFetch");

                List<String> fetchGroupFields = User.getUserQuickCartFetchGroupFieldsPII();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Cart.class, "fullCartFetch");
                fetchGroupFields = Cart.getCartFullFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(CartItem.class, "fullCartItemFetch");
                fetchGroupFields = CartItem.getCartItemFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Item.class, "fullItemFetch");
                fetchGroupFields = Item.getFullItemFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Address.class, "addressFetch");
                fetchGroupFields = Address.getAddressCartFetchGroupFields();
                for (String field : fetchGroupFields)
                {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(GenericItem.class, "genericItemFetch");
                fetchGroupFields = GenericItem.getFullGenericItemFetchGroupFields();
                for (String field : fetchGroupFields)
                {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("quickUserCartFetch");
                fetchPlan.addGroup("fullCartFetch");
                fetchPlan.addGroup("fullCartItemFetch");
                fetchPlan.addGroup("fullItemFetch");
                fetchPlan.addGroup("addressFetch");
                fetchPlan.addGroup("genericItemFetch");
                fetchPlan.setMaxFetchDepth(4);
            }
            else if (mode == IEnums.SEEK_MODE.USER_CART_PAYMENT_DETAILS)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(User.class, "quickUserCartFetch");

                List<String> fetchGroupFields = User.getUserQuickCartFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Cart.class, "cartPaymentDetailsFetch");
                fetchGroupFields = Cart.getCartPaymentDetailsFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("quickUserCartFetch");
                fetchPlan.addGroup("cartPaymentDetailsFetch");
                fetchPlan.setMaxFetchDepth(2);
            }
            //Pulls only the user's troved items, along with all details associated with each item
            else if (mode == IEnums.SEEK_MODE.USER_TROVED)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(User.class, "quickTrovedItemsFetch");
                List<String> fetchGroupFields = User.getUserTrovedItemsFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Item.class, "fullItemFetch");
                fetchGroupFields = Item.getFullItemFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("quickTrovedItemsFetch");
                fetchPlan.addGroup("fullItemFetch");
                fetchPlan.setMaxFetchDepth(4);

            }
            else if (mode == IEnums.SEEK_MODE.USER_TROVE)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(User.class, "quickTrovedItemsFetch");
                List<String> fetchGroupFields = User.getUserTroveFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Item.class, "fullItemFetch");
                fetchGroupFields = Item.getFullItemFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("quickTrovedItemsFetch");
                fetchPlan.addGroup("fullItemFetch");
                fetchPlan.setMaxFetchDepth(4);
            }
            else if (mode == IEnums.SEEK_MODE.USER_ADDRESSES)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(User.class, "userAddressFetch");
                List<String> fetchGroupFields = User.getUserAddressFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Address.class, "addressFetch");
                fetchGroupFields = Address.getAddressCartFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("userAddressFetch");
                fetchPlan.addGroup("addressFetch");
                fetchPlan.setMaxFetchDepth(2);
            }
            else if (mode == IEnums.SEEK_MODE.USER_SOCIAL_CONNECTIONS)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(User.class, "quickSocialConnectionFetch");
                List<String> fetchGroupFields = User.getUserSocialConnectionsFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("quickSocialConnectionFetch");
                fetchPlan.setMaxFetchDepth(2);
            }
            //This fetch group exposes PII.  Use with caution.
            else if (mode == IEnums.SEEK_MODE.USER_ORDERS_SLIM)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(User.class, "quickUserOrdersFetch");
                List<String> fetchGroupFields = User.getUserOrdersFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Order.class, "quickOrdersFetch");
                fetchGroupFields = Order.getOrdersSlimFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("quickUserOrdersFetch");
                fetchPlan.addGroup("quickOrdersFetch");
                fetchPlan.setMaxFetchDepth(2);
            }
            //This fetch group exposes PII.  Use with caution.
            else if (mode == IEnums.SEEK_MODE.USER_ORDERS_QUICK)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(User.class, "fullUserOrdersFetch");
                List<String> fetchGroupFields = User.getUserOrdersFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Order.class, "quickOrdersFetch");
                fetchGroupFields = Order.getOrdersQuickFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Address.class, "addressFetch");
                fetchGroupFields = Address.getAddressCartFetchGroupFields();
                for (String field : fetchGroupFields)
                {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("fullUserOrdersFetch");
                fetchPlan.addGroup("quickOrdersFetch");
                fetchPlan.addGroup("addressFetch");
                fetchPlan.setMaxFetchDepth(6);
            }
            //This fetch group exposes PII.  Use with caution.
            else if (mode == IEnums.SEEK_MODE.USER_ORDERS_FULL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(User.class, "fullUserOrdersFetch");
                List<String> fetchGroupFields = User.getUserOrdersFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Order.class, "fullOrdersFetch");
                fetchGroupFields = Order.getOrdersFullFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Cart.class, "fullCartFetch");
                fetchGroupFields = Cart.getCartFullFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(CartItem.class, "fullCartItemFetch");
                fetchGroupFields = CartItem.getCartItemFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Item.class, "fullItemFetch");
                fetchGroupFields = Item.getFullItemFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("fullUserOrdersFetch");
                fetchPlan.addGroup("fullOrdersFetch");
                fetchPlan.addGroup("fullCartFetch");
                fetchPlan.addGroup("fullCartItemFetch");
                fetchPlan.addGroup("fullItemFetch");
                fetchPlan.setMaxFetchDepth(6);
            }
            //This fetch group exposes PII.  Use with caution.
            else if (mode == IEnums.SEEK_MODE.USER_ORDERS_MEDIUM)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(User.class, "fullUserOrdersFetch");
                List<String> fetchGroupFields = User.getUserOrdersFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Order.class, "fullOrdersFetch");
                fetchGroupFields = Order.getOrdersFullFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Cart.class, "quickCartFetch");
                fetchGroupFields = Cart.getCartQuickFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(CartItem.class, "fullCartItemFetch");
                fetchGroupFields = CartItem.getCartItemFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Item.class, "fullItemFetch");
                fetchGroupFields = Item.getFullItemFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("fullUserOrdersFetch");
                fetchPlan.addGroup("fullOrdersFetch");
                fetchPlan.addGroup("quickCartFetch");
                fetchPlan.addGroup("fullCartItemFetch");
                fetchPlan.addGroup("fullItemFetch");
                fetchPlan.setMaxFetchDepth(6);
            }
            else if (mode == IEnums.SEEK_MODE.COLLECTION_LIGHT)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(Collection.class, "lightCollectionFetch");
                List<String> fetchGroupFields = Collection.getFullCollectionFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Follow.class, "followFetch");
                fetchGroupFields = Follow.getFollowFetchGroups();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("lightCollectionFetch");
                fetchPlan.addGroup("followFetch");
                fetchPlan.setMaxFetchDepth(4);
            }
            else if (mode == IEnums.SEEK_MODE.USER_FOLLOWS)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(User.class, "userFollowFetch");
                List<String> fetchGroupFields = User.getUserTrovedItemsFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(GroupedItem.class, "groupedItemFollowFetch");
                fetchGroupFields = GroupedItem.getGroupedItemGroupedItemFetchGroupField();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Item.class, "itemFollowFetch");
                fetchGroupFields = Item.getItemImageFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("userFollowFetch");
                fetchPlan.addGroup("itemFollowFetch");
                fetchPlan.addGroup("groupedItemFollowFetch");
                fetchPlan.setMaxFetchDepth(3);
                fetchPlan.setFetchSize(3);
            }
            else if (mode == IEnums.SEEK_MODE.USER_FOLLOWED_USERS)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(User.class, "userFollowFetch");
                List<String> fetchGroupFields = User.getUsersThisUserHasFollowedFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Follow.class, "followFetch");
                fetchGroupFields = Follow.getFollowFetchGroups();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("userFollowFetch");
                fetchPlan.addGroup("followFetch");
                fetchPlan.setMaxFetchDepth(3);
            }
            else if (mode == IEnums.SEEK_MODE.USER_SETTINGS)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(User.class, "userSettingsFetch");
                List<String> fetchGroupFields = User.getUserSettingsFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("userSettingsFetch");
                fetchPlan.setMaxFetchDepth(2);

            }
            //Contains PII
            else if (mode == IEnums.SEEK_MODE.USER_BAG_COUNT)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(User.class, "userSettingsFetch");
                List<String> fetchGroupFields = User.getUserHasUsedFtueFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("userSettingsFetch");
                fetchPlan.setMaxFetchDepth(2);
            }
            //Contains PII
            else if (mode == IEnums.SEEK_MODE.USER_BAG_COUNT_EMAIL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(User.class, "userSettingsFetch");
                List<String> fetchGroupFields = User.getUserWithEmailAndFtueFlag();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("userSettingsFetch");
                fetchPlan.setMaxFetchDepth(2);
            }
        }
    }
}
