package com.troveup.brooklyn.orm.cart.datanucleus;

import com.troveup.brooklyn.orm.cart.interfaces.ICartAccessor;
import com.troveup.brooklyn.orm.cart.model.*;
import com.troveup.brooklyn.orm.common.datanucleus.ObjectAccessor;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.countries.interfaces.ICountryAccessor;
import com.troveup.brooklyn.orm.countries.model.Country;
import com.troveup.brooklyn.orm.item.interfaces.IItemAccessor;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.item.model.ItemCustomization;
import com.troveup.brooklyn.orm.order.interfaces.IOrderAccessor;
import com.troveup.brooklyn.orm.order.model.PrintOrder;
import com.troveup.brooklyn.orm.user.interfaces.IUserAccessor;
import com.troveup.brooklyn.orm.user.model.Address;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.util.DateUtils;
import com.troveup.brooklyn.util.MoneyUtil;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jdo.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by tim on 5/15/15.
 */
public class CartAccessor extends ObjectAccessor implements ICartAccessor
{

    @Autowired
    IUserAccessor userAccessor;

    @Autowired
    ICountryAccessor countryAccessor;

    @Autowired
    IItemAccessor itemAccessor;

    @Autowired
    IOrderAccessor orderAccessor;

    public CartAccessor(PersistenceManagerFactory pmfProxy) {
        super(pmfProxy);
    }

    @Override
    public Boolean persistCart(Cart cart)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cart == null)
                throw new NullPointerException("cart cannot be null!");

            //Need to attach a persistence manager to the address object country and subdivision so that these are
            //reused rather than created anew.  Also, make sure none of this is null.
            getManagedCartAddresses(cart, pm, countryAccessor, userAccessor);

            //Get persistence managed version of the user owner
            if (cart.getCartOwner() != null && cart.getCartOwner().getUserId() != null)
            {
                User user = userAccessor.getUserAttached(cart.getCartOwner().getUserId(), pm);
                cart.setCartOwner(user);
            }

            rval = pm.makePersistent(cart) != null;

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
    public Boolean persistGiftCard(GiftCard giftCard)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            GenericItem itemReference = getGenericItemAttached(giftCard.getGenericItemReference().getGenericItemId(), pm);

            giftCard.setGenericItemReference(itemReference);

            giftCard.setGiftCardString(generateGiftCardCode());

            rval = pm.makePersistent(giftCard) != null;
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
    public List<GiftCard> getDigitalGiftCardsToBeMailed(Date date)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<GiftCard> rval = new ArrayList<>();

        try
        {
            if (date == null)
                throw new NullPointerException("Argument cannot be null");

            Query query = pm.newQuery(GiftCard.class, "digital == true && emailDate < :date && emailSent == false");

            rval = (List<GiftCard>) query.execute(date);

            if (rval != null) {
                rval = (List<GiftCard>) pm.detachCopyAll(rval);
            } else
            {
                rval = new ArrayList<>();
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
    public Boolean setStoreBalanceOffset(Long cartId, BigDecimal amount)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cartId == null || amount == null)
                throw new NullPointerException("Arguments cannot be null");

            Cart cart = getShoppingCartAttached(cartId, pm);
            cart.setStoreBalanceOffset(amount);

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
    public Boolean validateGiftCard(String giftCardNumber)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {

            if (giftCardNumber == null || giftCardNumber.length() == 0)
                throw new NullPointerException("Argument cannot be null or empty");

            Query query = pm.newQuery(GiftCard.class, "giftCardString == :giftCardNumber");

            List<GiftCard> queryResult = (List<GiftCard>) query.execute(giftCardNumber);

            if (queryResult != null && queryResult.size() > 0)
                rval = queryResult.get(0).getGiftCardString().equals(giftCardNumber) &&
                        !queryResult.get(0).getRedeemed();


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
    public BigDecimal redeemGiftCard(String giftCardNumber, Long userId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        BigDecimal rval = Money.zero(CurrencyUnit.USD).getAmount();

        try
        {
            if (giftCardNumber == null)
                throw new NullPointerException("Argument cannot be null or empty");

            GiftCard giftCard = getGiftCardAttached(giftCardNumber, pm);
            User user = userAccessor.getUserAttached(userId, pm);

            if (user.getStoreBalance() == null)
            {
                user.setStoreBalance(new BigDecimal(0.00));
            }

            if (giftCard != null && !giftCard.getRedeemed())
            {
                user.setStoreBalance(user.getStoreBalance().add(giftCard.getAmount()));
                giftCard.setRedeemed(true);
                giftCard.setRedeemer(user);

                rval = giftCard.getAmount();
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
    public Boolean setDigitalGiftCardStatusSent(Long giftCardId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (giftCardId == null)
                throw new NullPointerException("Argument cannot be null");

            GiftCard giftCard = getGiftCardAttached(giftCardId, pm);

            giftCard.setEmailSent(true);
            giftCard.setEmailSentDate(new Date());

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
    public GiftCard getGiftCardAttached(Long giftCardId, PersistenceManager pm)
    {
        GiftCard rval = null;

        Query query = pm.newQuery(GiftCard.class, "giftCardId == :giftCardId");
        List<GiftCard> queryResults = (List<GiftCard>) query.execute(giftCardId);

        if (queryResults != null && queryResults.size() > 0)
            rval = queryResults.get(0);

        return rval;
    }

    @Override
    public GiftCard getGiftCardAttached(String giftCardString, PersistenceManager pm)
    {
        GiftCard rval = null;

        Query query = pm.newQuery(GiftCard.class, "giftCardString == :giftCardString");
        List<GiftCard> queryResults = (List<GiftCard>) query.execute(giftCardString);

        if (queryResults != null && queryResults.size() > 0)
            rval = queryResults.get(0);

        return rval;
    }

    @Override
    public GenericItem getGenericItemAttached(Long genericItemId, PersistenceManager pm)
    {
        GenericItem rval = null;

        Query query = pm.newQuery(GenericItem.class, "genericItemId == :genericItemId");

        List<GenericItem> queryResult = (List<GenericItem>) query.execute(genericItemId);

        if (queryResult != null && queryResult.size() > 0) {
            rval = queryResult.get(0);
        }

        return rval;
    }

    @Override
    public Cart getShoppingCart(Long cartId, IEnums.SEEK_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Cart rval = null;

        try
        {
            if (cartId == null)
                throw new NullPointerException("cartId cannot be null!");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(Cart.class, "cartId == :cartId");
            query.setResultClass(Cart.class);

            List<Cart> carts = (List<Cart>) query.execute(cartId);

            if (carts != null && carts.size() > 0)
                rval = pm.detachCopy(carts.get(0));

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
    public Cart getShoppingCartByUser(Long userId, IEnums.SEEK_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Cart rval = null;

        try
        {
            if (userId == null)
                throw new NullPointerException("userId cannot be null!");

            if (mode == IEnums.SEEK_MODE.FULL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(Cart.class, "fullFetch");

                //Retrieve the full fetchgroup listing of fields from the Cart class
                //and add them
                List<String> fetchGroupFields = Cart.getCartFullFetchGroupFields();
                for (String field : fetchGroupFields)
                    fetchGroup.addMember(field);

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("fullFetch");
                fetchPlan.setMaxFetchDepth(2);
            }

            User user = userAccessor.getUserAttached(userId, pm);

            if (user != null && user.getShoppingCart() != null)
            {
                rval = pm.detachCopy(user.getShoppingCart());
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

    @Override
    public Cart getShoppingCartAttached(Long cartId, PersistenceManager pm)
    {
        Cart rval = null;

        try {
            if (cartId == null || pm == null)
                throw new IllegalArgumentException("Arguments cannot be null.");

            Query query = pm.newQuery(Cart.class, "cartId == :cartId");
            query.setResultClass(Cart.class);

            List<Cart> carts = (List<Cart>) query.execute(cartId);

            if (carts != null && carts.size() > 0)
                rval = carts.get(0);
        }
        catch (Exception e)
        {
            logError(e);
        }

        return rval;
    }

    @Override
    public Boolean removeItemFromCart(Long cartId, Long cartItemId) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cartId == null || cartItemId == null)
                throw new IllegalArgumentException("Arguments cannot be null.");

            Cart cart = getShoppingCartAttached(cartId, pm);

            if (cart != null)
            {
                CartItem containerItemToRemove = new CartItem(cartItemId);
                int cartItemRemovalIndex = cart.getCartItems().indexOf(containerItemToRemove);

                if (cartItemRemovalIndex > -1)
                    rval = cart.getCartItems().remove(cartItemRemovalIndex) != null;
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

    @Override
    public Boolean removeGenericItemFromCart(Long cartId, Long genericItemId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cartId == null || genericItemId == null)
                throw new IllegalArgumentException("Arguments cannot be null.");

            Cart cart = getShoppingCartAttached(cartId, pm);

            if (cart != null)
            {
                GenericItem containerItemToRemove = new GenericItem();
                containerItemToRemove.setGenericItemId(genericItemId);

                List<GenericItem> genericItems = cart.getGenericItems();
                int cartItemRemovalIndex = genericItems.indexOf(containerItemToRemove);

                if (cartItemRemovalIndex > -1)
                    rval = cart.getGenericItems().remove(cartItemRemovalIndex) != null;
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

    @Override
    public Boolean updateCartItem(CartItem cartItem)
    {
        Boolean rval = false;
        PersistenceManager pm = pmf.getPersistenceManager();

        try
        {
            if (cartItem == null)
                throw new NullPointerException("cartItem cannot be null!");

            if (cartItem.getCartItemId() == null)
                throw new NullPointerException("cartItem's ID cannot be null!");

            CartItem persistedItem = getCartItemAttached(cartItem.getCartItemId(), pm);

            persistedItem.setQuantity(cartItem.getQuantity());
            //persistedItem.setEstimatedPrice(cartItem.getEstimatedPrice());
            persistedItem.setActualPrice(cartItem.getActualPrice());

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
    public Boolean addGenericItemToCart(Long cartId, GenericItem genericItem)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cartId == null || genericItem == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            Cart cart = getShoppingCartAttached(cartId, pm);

            //Workaround for bug in DataNucleus, includes a field twice in the SQL for some reason.
            /*List<GenericItem> itemTransfer = new ArrayList<>();
            itemTransfer.addAll(cart.getGenericItems());
            itemTransfer.add(genericItem);*/

            rval = cart.getGenericItems().add(genericItem);

            //rval = true;
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateCartItemEstimatedPrice(Long cartItemId, String estimatedPrice)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cartItemId == null || estimatedPrice == null)
                throw new IllegalArgumentException("Arguments cannot be null.");

            CartItem cartItem = getCartItemAttached(cartItemId, pm);

            cartItem.setEstimatedPrice(estimatedPrice);

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
    public Boolean updateCartItemActualPrice(Long cartItemId, BigDecimal actualPrice)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cartItemId == null || actualPrice == null)
                throw new IllegalArgumentException("Arguments cannot be null.");

            CartItem cartItem = getCartItemAttached(cartItemId, pm);

            cartItem.setActualPrice(actualPrice);

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
    public CartItem getCartItemAttached(Long cartItemId, PersistenceManager pm)
    {
        CartItem rval = null;

        if (cartItemId == null || pm == null)
            throw new IllegalArgumentException("Arguments cannot be null");

        Query query = pm.newQuery(CartItem.class, "cartItemId == :promoCodeId");
        query.setResultClass(CartItem.class);
        List<CartItem> queryResults = (List<CartItem>) query.execute(cartItemId);

        if (queryResults != null && queryResults.size() > 0)
            rval = queryResults.get(0);

        return rval;
    }

    @Override
    public CartItem getCartItem(Long cartItemId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        CartItem rval = null;

        try
        {
            if (cartItemId == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            rval = getCartItemAttached(cartItemId, pm);

            rval = pm.detachCopy(rval);
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
    public List<CartItem> getCartItemsAttached(List<Long> cartItemId, PersistenceManager pm) {
        List<CartItem> rval;

        if (cartItemId == null || pm == null)
            throw new IllegalArgumentException("Arguments cannot be null");

        Query query = pm.newQuery(CartItem.class, ":p.contains(cartItemId)");
        query.setResultClass(CartItem.class);
        rval = (List<CartItem>) query.execute(cartItemId);

        return rval;
    }

    @Override
    public Boolean updateCart(Cart cart) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cart == null)
                throw new NullPointerException("cart cannot be null!");

            Cart persistedCart = getShoppingCartAttached(cart.getCartId(), pm);

            if (persistedCart != null)
            {
                if (cart.getVendorCartId() != null)
                    persistedCart.setVendorCartId(cart.getVendorCartId());
                if (cart.getSubTotal() != null)
                    persistedCart.setSubTotal(cart.getSubTotal());
                if (cart.getTax() != null)
                    persistedCart.setTax(cart.getTax());
                if (cart.getShipping() != null)
                    persistedCart.setShipping(cart.getShipping());
                if (cart.getGrandTotal() != null)
                    persistedCart.setGrandTotal(cart.getGrandTotal());
                if (cart.getAdditionalDollarDiscount() != null)
                    persistedCart.setGrandTotal(cart.getGrandTotal());
                if (cart.getCheckoutEmail() != null)
                    persistedCart.setCheckoutEmail(cart.getCheckoutEmail());
                if (cart.getShouldBeGiftWrapped() != null)
                    persistedCart.setShouldBeGiftWrapped(cart.getShouldBeGiftWrapped());
                if (cart.getGiftMessageCardText() != null)
                    persistedCart.setGiftMessageCardText(cart.getGiftMessageCardText());
                if (cart.getIncludeGiftMessageCard() != null)
                    persistedCart.setIncludeGiftMessageCard(cart.getIncludeGiftMessageCard());

                persistedCart.setAdditionalPercentDiscount(cart.getAdditionalPercentDiscount());
                persistedCart.setFreeShipping(cart.isFreeShipping());

                getManagedCartAddresses(cart, pm, countryAccessor, userAccessor);

                //TODO:  Does not get a PM version of addresses, just simply persists new ones.  Take that into account.
                if (cart.getBillingAddress() != null)
                {
                    Address address = cart.getBillingAddress();
                    persistedCart.setBillingAddress(address);
                }
                if (cart.getShippingAddress() != null) {
                    Address address = cart.getShippingAddress();
                    persistedCart.setShippingAddress(address);
                }

                if (cart.getPaymentDetails() != null)
                    persistedCart.setPaymentDetails(cart.getPaymentDetails());
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

    @Override
    public Boolean updateCartAttribute(Long cartId, CartAttribute attribute)
    {
        Boolean rval = false;
        PersistenceManager pm = pmf.getPersistenceManager();

        try {
            if (attribute == null || cartId == null)
                throw new IllegalArgumentException("Arguments cannot be null");

            Cart cart = getShoppingCartAttached(cartId, pm);

            if (cart != null)
            {
                CartAttribute searchAttribute = new CartAttribute();
                searchAttribute.setAttributeName(attribute.getAttributeName());
                searchAttribute.setAttributeValue(attribute.getAttributeValue());
                int attributeIndex = cart.getCartAttributes().indexOf(searchAttribute);

                if (attributeIndex > -1)
                    cart.getCartAttributes().remove(attributeIndex);

                rval = cart.getCartAttributes().add(attribute);
            }

        } catch(Exception e)
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
    public Boolean updateCartAttribute(Long cartId, List<CartAttribute> attributes)
    {
        Boolean rval = false;
        PersistenceManager pm = pmf.getPersistenceManager();

        try {
            if (attributes == null || cartId == null)
                throw new IllegalArgumentException("Arguments cannot be null");

            Cart cart = getShoppingCartAttached(cartId, pm);

            if (cart != null)
            {
                for (CartAttribute attribute : attributes) {
                    CartAttribute searchAttribute = new CartAttribute();
                    searchAttribute.setAttributeName(attribute.getAttributeName());
                    searchAttribute.setAttributeValue(attribute.getAttributeValue());
                    int attributeIndex = cart.getCartAttributes().indexOf(searchAttribute);

                    if (attributeIndex > -1)
                        cart.getCartAttributes().remove(attributeIndex);

                    rval = cart.getCartAttributes().add(attribute);
                }

                rval = true;
            }

        } catch(Exception e)
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
    public Boolean updateCartItemAttribute(Long cartItemId, CartItemAttribute attribute)
    {
        Boolean rval = false;
        PersistenceManager pm = pmf.getPersistenceManager();

        try
        {
            if (cartItemId == null || attribute == null)
                throw new IllegalArgumentException("Parameter must contain at least one element");

            CartItem item = getCartItemAttached(cartItemId, pm);

            if (item.getAttributes() == null)
            {
                item.setAttributes(new ArrayList<CartItemAttribute>());
            }

            int attributeIndex = item.getAttributes().indexOf(attribute);
            //Remove the old one
            if (attributeIndex > -1) {
                item.getAttributes().remove(attributeIndex);
            }
            //Add a new one
            item.getAttributes().add(attribute);

            rval = true;
        }
        catch(Exception e)
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
    public Boolean addPaymentDetails(Long cartId, PaymentDetails details)
    {
        Boolean rval = false;
        PersistenceManager pm = pmf.getPersistenceManager();

        try
        {
            if (cartId == null || details == null)
                throw new IllegalArgumentException("Arguments cannot be null");

            Cart cart = getShoppingCartAttached(cartId, pm);

            if (cart != null)
            {
                cart.setPaymentDetails(details);

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
    public Boolean addCartItemToCart(Long cartId, CartItem cartItem, String size)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (cartId == null || cartItem == null)
                throw new IllegalArgumentException("Arguments cannot be null");

            //Get a persisted version of the attached item, if applicable
            if (cartItem.getCartItemReference() != null && cartItem.getCartItemReference().getItemId() != null)
            {
                Item itemReference = itemAccessor.getItemAttached(cartItem.getCartItemReference().getItemId(), pm);
                cartItem.setCartItemReference(itemReference);

                //Create a separate set of item customizations for this cart item
                if (itemReference.getItemCustomizations() != null && itemReference.getItemCustomizations().size() > 0)
                {
                    cartItem.setCustomizations((List<ItemCustomization>) pm.detachCopyAll(itemReference.getItemCustomizations()));
                }
            }

            if (cartItem.getChain() != null && cartItem.getChain().getChainId() != null)
            {
                cartItem.setChain(itemAccessor.getChainAttached(cartItem.getChain().getChainId(), pm));
            }

            Cart cart = getShoppingCartAttached(cartId, pm);
            cartItem.setCart(cart);

            if (size != null && size.length() > 0) {
                ItemCustomization customization = new ItemCustomization();
                customization.setSize(size);
                customization.setSetNumber(1);

                if (cartItem.getCustomizations() == null)
                    cartItem.setCustomizations(new ArrayList<ItemCustomization>());

                cartItem.getCustomizations().add(customization);
            }

            cart.getCartItems().add(cartItem);

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
    public Boolean updateCartItemAttribute(Long cartItemId, String attributeName, String attributeValue)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cartItemId == null || attributeName == null|| attributeValue == null)
                throw new IllegalArgumentException("Arguments cannot be null");

            CartItem cartItem = getCartItemAttached(cartItemId, pm);
            CartItemAttribute searchAttribute = new CartItemAttribute();
            searchAttribute.setAttributeName(attributeName);
            int cartItemIndexToModify = cartItem.getAttributes().indexOf(searchAttribute);

            //Found the attribute
            if (cartItemIndexToModify > -1) {
                cartItem.getAttributes().get(cartItemIndexToModify).setAttributeValue(attributeValue);
                rval = true;
            }
            //Didn't find the attribute, so add it as a new one
            else
            {
                searchAttribute.setAttributeValue(attributeValue);
                cartItem.getAttributes().add(searchAttribute);
                rval = true;
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

    @Override
    public Boolean addCartItemAttributes(Long cartItemId, List<CartItemAttribute> attributes)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cartItemId == null || attributes == null)
                throw new IllegalArgumentException("Arguments cannot be null");

            CartItem cartItem = getCartItemAttached(cartItemId, pm);

            rval = cartItem.getAttributes().addAll(attributes);


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
    public Boolean updateCartItemStatus(Long cartItemId, CartItem.CART_ITEM_STATUS status)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cartItemId == null || status == null)
                throw new IllegalArgumentException("Arguments cannot be null");

            CartItem cartItem = getCartItemAttached(cartItemId, pm);
            cartItem.setCartItemStatus(status);
            cartItem.setLastStatusChangeDate(new Date());

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
    public List<CartItem> getOutstandingPrototypeCartItems(Long limit, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<CartItem> rval = null;

        try
        {
            if (mode != null)
                configureFetchGroups(pm, mode);

            CartItem.CART_ITEM_STATUS seekStatus = CartItem.CART_ITEM_STATUS.PENDING_USER_DECISION;
            Date deliveryTriggerDate = DateUtils.getDateXHoursAgo(new Date(), 48);
            Date updateTriggerDate = DateUtils.getDateXHoursAgo(new Date(), 24);

            Query query = pm.newQuery(CartItem.class, "cartItemStatus == :seekStatus && " +
                    "lastStatusChangeDate < :deliveryTriggerDate && " +
                    "(lastOutstandingPrototypeCheckDate < :updateTriggerDate || lastOutstandingPrototypeCheckDate == null)");

            if (limit != null && limit > 0)
                query.setRange(0l, limit);

            rval = (List<CartItem>) query.execute(seekStatus, deliveryTriggerDate, updateTriggerDate);

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
    public Boolean updateCartItemLastOutstandingPrototypeCheckDate(Long cartItemId, Date date)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cartItemId == null || date == null)
                throw new NullPointerException("Arguments cannot be null");

            CartItem item = getCartItemAttached(cartItemId, pm);

            item.setLastOutstandingPrototypeCheckDate(date);

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
    public Boolean updateCartTax(Long cartId, BigDecimal taxValue)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cartId == null || taxValue == null)
                throw new NullPointerException("Arguments cannot be null");

            Cart cart = getShoppingCartAttached(cartId, pm);

            cart.setTax(taxValue);

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
    public Cart refreshGrandTotal(Long cartId, Boolean shouldApplyStoreBalance, Boolean shouldClearDirtyFlag) {
        PersistenceManager pm = pmf.getPersistenceManager();

        Boolean shouldChargeShipping = true;
        Cart cart = getShoppingCartAttached(cartId, pm);

        Money grandTotal = Money.zero(MoneyUtil.toCurrencyUnit(cart.getCurrencyUnit()));

        refreshCartSubtotalPersistenceManaged(cart);

        //Case where we should potentially not charge shipping.  Narrow it down.
        //Goal here is to not charge shipping if there is only non-shipping items in the bag,
        //So a single shippable item in the bag will trigger shipping
        if ((cart.getCartItems() == null || cart.getCartItems().size() == 0) && cart.getGenericItems() != null && cart.getGenericItems().size() > 0)
        {
            shouldChargeShipping = false;

            for (GenericItem item : cart.getGenericItems())
            {
                if (item.getShouldChargeShipping() != null && item.getShouldChargeShipping())
                {
                    shouldChargeShipping = item.getShouldChargeShipping();
                }
                //Special case prior to the addition of the shouldChargeShipping field
                //Only digital gift cards shouldn't have shipping charged
                else
                {
                    //Pull in the hook attributes from the DB
                    item.getHookAttributes();
                    if (!GiftCard.isGenericItemADigitalGiftCard(item))
                        shouldChargeShipping = true;
                }
            }
        }
        //Special case where bag is empty
        else if ((cart.getCartItems() == null || cart.getCartItems().size() == 0) && (cart.getGenericItems() == null || cart.getGenericItems().size() == 0))
        {
            shouldChargeShipping = false;
        }

        if (cart.getTax() != null)
            grandTotal = grandTotal.plus(cart.getTax());

        Boolean shippingPromoCodeApplied = doesCartHaveShippingPromoCodeApplied(cart);

        //If a promo code was applied, leave shipping alone.  Otherwise, either set it to the standard rate,
        //or 0.
        if (shouldChargeShipping && !shippingPromoCodeApplied) {
            grandTotal = grandTotal.plus(Cart.getStandardShippingRate(), RoundingMode.HALF_UP);
            cart.setShipping(Cart.getStandardShippingRate());
        } else if (!shouldChargeShipping && !shippingPromoCodeApplied)
        {
            cart.setShipping(new BigDecimal(0.00));
        } else if (cart.getShipping() != null)
        {
            grandTotal = grandTotal.plus(cart.getShipping(), RoundingMode.HALF_UP);
        }

        if (cart.getSubTotal() != null)
            grandTotal = grandTotal.plus(cart.getSubTotal());

        if (shouldApplyStoreBalance)
        {
            User user = cart.getCartOwner();

            //If the store balance is greater than zero, apply the store balance to the cart
            if (user.getStoreBalance() != null && user.getStoreBalance().compareTo(new BigDecimal(0.00)) == 1)
            {
                Money storeBalance = MoneyUtil.bigDecimalToMoney(null, user.getStoreBalance());

                if (grandTotal.minus(storeBalance).isLessThan(Money.zero(CurrencyUnit.USD)))
                {
                    cart.setStoreBalanceOffset(grandTotal.getAmount());
                }
                else
                {
                    cart.setStoreBalanceOffset(storeBalance.getAmount());
                }

            }
        }

        cart.setGrandTotal(grandTotal.getAmount());

        if (shouldClearDirtyFlag)
        {
            cart.setDirtySubtotal(false);
        }

        return pm.detachCopy(cart);
    }

    private Boolean doesCartHaveShippingPromoCodeApplied(Cart persistenceManagedCart)
    {
        Boolean rval = false;

        if (persistenceManagedCart.getAppliedPromoCodes() != null && persistenceManagedCart.getAppliedPromoCodes().size() > 0)
        {
            for (PromoCode code : persistenceManagedCart.getAppliedPromoCodes())
            {
                if (code.getBlanketCategory() != null && code.getBlanketCategory().equals(PromoCode.DISCOUNT_CATEGORY.SHIPPING.toString()))
                {
                    rval = true;
                }
            }
        }

        return rval;
    }

    @Override
    public Boolean getDirtySubtotalStatus(Long cartId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = true;

        try
        {
            if (cartId == null)
                throw new NullPointerException("Argument cannot be null");

            Cart cart = getShoppingCartAttached(cartId, pm);

            rval = cart.getDirtySubtotal();
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

    private void refreshCartSubtotalPersistenceManaged(Cart cart)
    {
        Money cartSubtotal = Money.zero(MoneyUtil.toCurrencyUnit(cart.getCurrencyUnit()));

        for (CartItem item : cart.getCartItems())
        {
            cartSubtotal = cartSubtotal.plus(item.getActualPrice());

            //If there is a chain, include it in the calculation
            if (item.getChain() != null && item.getChain().getChainId() != null)
            {
                cartSubtotal = cartSubtotal.plus(item.getChain().getPrice());
            }
        }

        //Add the generic line items
        for (GenericItem item : cart.getGenericItems())
        {
            cartSubtotal = cartSubtotal.plus(item.getPrice());
        }

        //Reapply any promo codes that should have been already applied
        if (cart.getAppliedPromoCodes() != null && cart.getAppliedPromoCodes().size() > 0)
        {
            for (PromoCode code : cart.getAppliedPromoCodes())
            {
                //Don't subtract from the subtotal if it's a shipping promo code
                if (code.getBlanketCategory() != null && !code.getBlanketCategory().equals(PromoCode.DISCOUNT_CATEGORY.SHIPPING.toString())) {
                    cartSubtotal = cartSubtotal.minus(code.getPriceDelta());
                }
            }
        }

        if (cart.getShouldBeGiftWrapped() != null && cart.getShouldBeGiftWrapped())
        {
            cartSubtotal = cartSubtotal.plus(new BigDecimal(4.99), RoundingMode.HALF_DOWN);
        }

        //Indicate that things reliant on the subtotal need to be recalculated, like tax
        if (!MoneyUtil.bigDecimalToMoney(null, cart.getSubTotal()).isEqual(cartSubtotal))
            cart.setDirtySubtotal(true);

        cart.setSubTotal(MoneyUtil.toProperScale(cartSubtotal.getAmount(), cart.getCurrencyUnit()));
    }

    @Override
    public Cart refreshCartEstimatedSubtotal(Cart cart)
    {
        PersistenceManager pm = pmf.getPersistenceManager();

        try {
            Cart persistedCart = null;

            if (cart.getCartId() != null) {
                persistedCart = getShoppingCartAttached(cart.getCartId(), pm);
            }

            Money estimatedSubTotal = Money.zero(MoneyUtil.toCurrencyUnit(cart.getCurrencyUnit()));

            for (CartItem item : cart.getCartItems())
            {
                //Parse out the currency that comes with an iMat price response
                //TODO:  Make this not iMat specific
                if (item.getEstimatedPrice() != null)
                {
                    String estimatedPrice;
                    if (item.getEstimatedPrice().contains(" ")) {
                        String parsedEstimation[] = item.getEstimatedPrice().split(" ");
                        estimatedPrice = parsedEstimation[1];
                    }
                    else
                    {
                        estimatedPrice = item.getEstimatedPrice();
                    }

                    estimatedSubTotal = estimatedSubTotal.plus(Float.parseFloat(estimatedPrice), RoundingMode.HALF_DOWN);
                }
            }

            cart.setEstimatedSubtotal(estimatedSubTotal.getAmount());

            if (persistedCart != null)
                persistedCart.setEstimatedSubtotal(estimatedSubTotal.getAmount());


        } catch (Exception e)
        {
            logError(e);
        } finally
        {
            pm.close();
        }

        return cart;
    }

    @Override
    public Cart refreshCartEstimatedSubtotal(Long cartId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Cart cart = null;

        try {

            if (cartId == null)
                throw new NullPointerException("Arguments cannot be null");

            cart = getShoppingCartAttached(cartId, pm);

            Money estimatedSubTotal = Money.zero(MoneyUtil.toCurrencyUnit(cart.getCurrencyUnit()));

            for (CartItem item : cart.getCartItems())
            {
                //Parse out the currency that comes with an iMat price response
                //TODO:  Make this not iMat specific
                if (item.getEstimatedPrice() != null)
                {
                    String estimatedPrice;
                    if (item.getEstimatedPrice().contains(" ")) {
                        String parsedEstimation[] = item.getEstimatedPrice().split(" ");
                        estimatedPrice = parsedEstimation[1];
                    }
                    else
                    {
                        estimatedPrice = item.getEstimatedPrice();
                    }

                    estimatedSubTotal = estimatedSubTotal.plus(Float.parseFloat(estimatedPrice), RoundingMode.HALF_DOWN);
                }
            }

            cart.setEstimatedSubtotal(estimatedSubTotal.getAmount());


        } catch (Exception e)
        {
            logError(e);
        } finally
        {
            pm.close();
        }

        return cart;
    }

    @Override
    public Cart refreshCartActualSubtotal(Long cartId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Cart cart = null;

        try {

            if (cartId == null)
                throw new NullPointerException("Arguments cannot be null");

            cart = getShoppingCartAttached(cartId, pm);

            Money actualSubtotal = Money.zero(MoneyUtil.toCurrencyUnit(cart.getCurrencyUnit()));

            for (CartItem item : cart.getCartItems())
            {
                //Parse out the currency that comes with an iMat price response
                //TODO:  Make this not iMat specific
                if (item.getEstimatedPrice() != null)
                {
                    actualSubtotal = actualSubtotal.plus(item.getActualPrice(), RoundingMode.HALF_DOWN);
                }
            }

            cart.setEstimatedSubtotal(actualSubtotal.getAmount());


        } catch (Exception e)
        {
            logError(e);
        } finally
        {
            pm.close();
        }

        return cart;
    }

    @Override
    public Boolean setCartItemPrototypeRequestedFlag(List<Long> cartItemIds, Boolean prototypeRequestedFlagValue)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cartItemIds == null || cartItemIds.size() == 0)
                throw new NullPointerException("Argument cannot be null");

            List<CartItem> cartItems = getCartItemsAttached(cartItemIds, pm);

            for (CartItem cartItem : cartItems) {
                cartItem.setPrototypeRequested(prototypeRequestedFlagValue);
            }

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
    public Boolean addCartItemPrintOrder(Long cartItemId, PrintOrder order)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (cartItemId == null || order == null)
                throw new NullPointerException("Arguments cannot be null");

            CartItem cartItem = getCartItemAttached(cartItemId, pm);

            if (order.getOrderReference() != null) {
                order.setOrderReference(orderAccessor.getOrderAttached(order.getOrderReference().getOrderId(), pm));
            }

            order.setCartItemReference(cartItem);

            rval = cartItem.getManufacturerOrders().add(order);
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
    public CartItem getCartItemByPrintOrder(Long printOrderPrimaryKeyId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        CartItem rval = null;

        try
        {
            if (printOrderPrimaryKeyId == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(CartItem.class, "printOrder.printOrderPrimaryKeyId == :primaryKey && " +
                    "this.manufacturerOrders.contains(printOrder)");
            query.declareVariables("com.troveup.brooklyn.orm.order.model.PrintOrder printOrder");

            List<CartItem> queryList = (List<CartItem>) query.execute(printOrderPrimaryKeyId);

            if (queryList != null && queryList.size() > 0)
                rval = queryList.get(0);

            rval = pm.detachCopy(rval);
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
    public GenericItem getGenericItem(Long genericItemId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        GenericItem rval = null;

        try
        {
            if (genericItemId == null || mode == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(GenericItem.class, "genericItemId == :genericItemId");

            List<GenericItem> queryResult = (List<GenericItem>) query.execute(genericItemId);

            if (queryResult != null && queryResult.size() > 0) {
                rval = queryResult.get(0);
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
    public Boolean checkIfGiftCardNumberExists(String giftCardNumber)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = true;

        try
        {
            if (giftCardNumber == null || giftCardNumber.length() == 0)
                throw new NullPointerException("Argument cannot be null or empty");

            Query query = pm.newQuery(GiftCard.class, "giftCardString == :giftCardNumber");
            query.setResult("count(this)");

            Long queryResult = (Long) query.execute(giftCardNumber);

            if (queryResult == 0)
                rval = false;
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
    public Boolean updateGenericItemReferenceId(Long genericItemId, Long referenceId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (genericItemId == null || referenceId == null)
                throw new NullPointerException("Arguments cannot be null");

            GenericItem item = getGenericItemAttached(genericItemId, pm);
            item.setItemReferenceId(referenceId);

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
    public GenericItem setGenericItemStatus(Long genericItemId, GenericItem.GENERIC_ITEM_STATUS status)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        GenericItem rval = null;

        try
        {
            if (genericItemId == null)
                throw new NullPointerException("genericItemId cannot be null.");

            GenericItem item = getGenericItemAttached(genericItemId, pm);
            item.setItemStatus(status);

            rval = pm.detachCopy(item);

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

    public static void configureFetchGroups(PersistenceManager pm, IEnums.SEEK_MODE mode)
    {
        if (mode != IEnums.SEEK_MODE.QUICK)
        {
            if (mode == IEnums.SEEK_MODE.FULL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(Cart.class, "fullFetch");

                //Retrieve the full fetchgroup listing of fields from the Cart class
                //and add them
                List<String> fetchGroupFields = Cart.getCartFullFetchGroupFields();
                for (String field : fetchGroupFields)
                    fetchGroup.addMember(field);

                fetchGroup = pm.getFetchGroup(CartItem.class, "cartItemFetch");
                fetchGroupFields = CartItem.getCartItemFullFetchGroupFields();
                for (String field : fetchGroupFields)
                    fetchGroup.addMember(field);

                fetchGroup = pm.getFetchGroup(Item.class, "itemFetch");
                fetchGroupFields = Item.getFullItemFetchGroupFields();
                for (String field : fetchGroupFields)
                    fetchGroup.addMember(field);

                fetchGroup = pm.getFetchGroup(Country.class, "countryFetch");
                fetchGroupFields = Country.getCountryFetchGroupFields();
                for (String field : fetchGroupFields)
                    fetchGroup.addMember(field);

                fetchGroup = pm.getFetchGroup(Address.class, "addressFetch");
                fetchGroupFields = Address.getAddressCartFetchGroupFields();
                for (String field : fetchGroupFields)
                    fetchGroup.addMember(field);

                fetchGroup = pm.getFetchGroup(CartItemAttribute.class, "cartItemAttributeFetch");
                fetchGroupFields = CartItemAttribute.getCartItemAttributeFetchGroupFields();
                for (String field : fetchGroupFields)
                    fetchGroup.addMember(field);

                fetchGroup = pm.getFetchGroup(CartItemAttribute.class, "cartAttributeFetch");
                fetchGroupFields = CartAttribute.getCartAttributeFetchGroupFields();
                for (String field : fetchGroupFields)
                    fetchGroup.addMember(field);

                fetchGroup = pm.getFetchGroup(GenericItem.class, "genericItemFetch");
                fetchGroupFields = GenericItem.getFullGenericItemFetchGroupFields();
                for (String field : fetchGroupFields)
                    fetchGroup.addMember(field);

                fetchGroup = pm.getFetchGroup(PrintOrder.class, "printOrderFetch");
                fetchGroupFields = PrintOrder.getPrintOrderFullFetchGroupFields();
                for (String field : fetchGroupFields)
                    fetchGroup.addMember(field);

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("fullFetch");
                fetchPlan.addGroup("countryFetch");
                fetchPlan.addGroup("addressFetch");
                fetchPlan.addGroup("cartItemFetch");
                fetchPlan.addGroup("itemFetch");
                fetchPlan.addGroup("cartItemAttributeFetch");
                fetchPlan.addGroup("cartAttributeFetch");
                fetchPlan.addGroup("genericItemFetch");
                fetchPlan.addGroup("printOrderFetch");
                fetchPlan.setMaxFetchDepth(6);
            }
            else if (mode == IEnums.SEEK_MODE.CART_PROMO)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(Cart.class, "promoCartFetch");
                List<String> fetchGroupFields = Cart.getCartPromoFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(CartItem.class, "cartItemPromoFetch");
                fetchGroupFields = CartItem.getCartPromoFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("promoCartFetch");
                fetchPlan.addGroup("cartItemPromoFetch");
                fetchPlan.setMaxFetchDepth(3);
            }
            else if (mode == IEnums.SEEK_MODE.CART_ITEM_UPLOAD)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(CartItem.class, "cartItemUploadFetch");
                List<String> fetchGroupFields = CartItem.getCartItemUploadFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("cartItemUploadFetch");
                fetchPlan.setMaxFetchDepth(5);
            }
            else if (mode == IEnums.SEEK_MODE.CART_ITEM_ORDER_SUBMIT)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(CartItem.class, "cartItemOrderSubmitFetch");
                List<String> fetchGroupFields = CartItem.getCartItemOrderSubmitFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("cartItemOrderSubmitFetch");
                fetchPlan.setMaxFetchDepth(3);
            }
            else if (mode == IEnums.SEEK_MODE.CART_ADDRESSES)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(Cart.class, "cartAddressSubmitFetch");
                List<String> fetchGroupFields = Cart.getCartAddressFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Country.class, "countryFetch");
                fetchGroupFields = Country.getCountryFetchGroupFields();
                for (String field : fetchGroupFields)
                    fetchGroup.addMember(field);

                fetchGroup = pm.getFetchGroup(Address.class, "addressFetch");
                fetchGroupFields = Address.getAddressCartFetchGroupFields();
                for (String field : fetchGroupFields)
                    fetchGroup.addMember(field);


                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("cartAddressSubmitFetch");
                fetchPlan.addGroup("countryFetch");
                fetchPlan.addGroup("addressFetch");

                fetchPlan.setMaxFetchDepth(3);

            }
            else if (mode == IEnums.SEEK_MODE.GENERIC_ITEM_FULL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(GenericItem.class, "genericItemFullFetch");
                List<String> fetchGroupFields = GenericItem.getFullGenericItemFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("genericItemFullFetch");

                fetchPlan.setMaxFetchDepth(3);
            }
        }
    }

    public static void getManagedCartAddresses(Cart cart, PersistenceManager pm, ICountryAccessor countryAccessor, IUserAccessor userAccessor)
    {
        if (cart.getBillingAddress() != null)
        {
            if (cart.getBillingAddress().getAddressId() != null)
            {
                cart.setBillingAddress(userAccessor.getAddressAttached(cart.getBillingAddress().getAddressId(), pm));
            }
            else {
                if (cart.getBillingAddress().getCountry() != null && cart.getBillingAddress().getCountry().getPrimaryKeyId() != null)
                    cart.getBillingAddress().setCountry(countryAccessor.getCountryAttached(cart.getBillingAddress().getCountry().getPrimaryKeyId(), pm));

                if (cart.getBillingAddress().getSubdivision() != null && cart.getBillingAddress().getSubdivision().getPrimaryKeyId() != null)
                    cart.getBillingAddress().setSubdivision(countryAccessor.getSubdivisionAttached(cart.getBillingAddress().getSubdivision().getPrimaryKeyId(), pm));
            }
        }

        if (cart.getShippingAddress() != null)
        {

            if (cart.getShippingAddress().getAddressId() != null)
            {
                cart.setShippingAddress(userAccessor.getAddressAttached(cart.getShippingAddress().getAddressId(), pm));
            }
            else {
                if (cart.getShippingAddress().getCountry() != null && cart.getShippingAddress().getCountry().getPrimaryKeyId() != null)
                    cart.getShippingAddress().setCountry(countryAccessor.getCountryAttached(cart.getShippingAddress().getCountry().getPrimaryKeyId(), pm));

                //Get a persistent managed object and set that as the subdivision
                if (cart.getShippingAddress().getSubdivision() != null && cart.getShippingAddress().getSubdivision().getPrimaryKeyId() != null)
                    cart.getShippingAddress().setSubdivision(countryAccessor.getSubdivisionAttached(cart.getShippingAddress().getSubdivision().getPrimaryKeyId(), pm));
            }
        }
    }

    private String generateGiftCardCode()
    {
        String rval = UUID.randomUUID().toString().substring(0, 7).toUpperCase();

        Boolean uniquenessCheck = checkIfGiftCardNumberExists(rval);

        while(uniquenessCheck)
        {
            rval = UUID.randomUUID().toString().substring(0, 7).toUpperCase();
            uniquenessCheck = checkIfGiftCardNumberExists(rval);
        }

        return rval;
    }
}
