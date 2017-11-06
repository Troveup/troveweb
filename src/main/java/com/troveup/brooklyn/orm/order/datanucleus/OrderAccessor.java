package com.troveup.brooklyn.orm.order.datanucleus;

import com.troveup.brooklyn.orm.cart.datanucleus.PromoCodeAccessor;
import com.troveup.brooklyn.orm.cart.interfaces.ICartAccessor;
import com.troveup.brooklyn.orm.cart.interfaces.IPromoCodeAccessor;
import com.troveup.brooklyn.orm.cart.model.*;
import com.troveup.brooklyn.orm.common.datanucleus.ObjectAccessor;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.countries.interfaces.ICountryAccessor;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.item.model.ItemCustomization;
import com.troveup.brooklyn.orm.order.interfaces.IOrderAccessor;
import com.troveup.brooklyn.orm.order.model.*;
import com.troveup.brooklyn.orm.simpleitem.model.OrderNote;
import com.troveup.brooklyn.orm.user.interfaces.IUserAccessor;
import com.troveup.brooklyn.orm.user.model.Address;
import com.troveup.brooklyn.orm.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jdo.*;
import javax.jdo.annotations.Persistent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.troveup.brooklyn.orm.common.interfaces.IEnums.SEEK_MODE.ORDER_INFLUENCER_DASHBOARD;

/**
 * Created by tim on 5/12/15.
 */
public class OrderAccessor extends ObjectAccessor implements IOrderAccessor
{
    @Autowired
    IUserAccessor userAccessor;

    @Autowired
    ICountryAccessor countryAccessor;

    @Autowired
    ICartAccessor cartAccessor;

    @Autowired
    IPromoCodeAccessor promoCodeAccessor;

    public OrderAccessor(PersistenceManagerFactory pmfProxy) {
        super(pmfProxy);
    }

    @Override
    public Boolean persistOrder(Order order)
    {
        Boolean rval = false;
        PersistenceManager pm = pmf.getPersistenceManager();

        try
        {
            if (order == null)
                throw new NullPointerException("Country cannot be null!");

            if (order.getAppliedPromoCodes() != null) {
                //Make sure we have a managed version of the invalidated code
                for (PromoCode promoCode : order.getAppliedPromoCodes()) {
                    if (promoCode.getConsumedCode() != null)
                        promoCode.setConsumedCode(PromoCodeAccessor.getManagedPromoCode(promoCode, pm, promoCodeAccessor));
                }
            }

            rval = pm.makePersistent(order) != null;
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
    public Boolean deleteOrder(Long primaryKeyId) {
        Boolean rval = false;
        PersistenceManager pm = pmf.getPersistenceManager();

        try
        {
            if (primaryKeyId == null)
                throw new NullPointerException("primaryKeyId cannot be null!");

            Query query = pm.newQuery(Order.class, "orderId == :primaryKeyId");
            Long numberRemoved = query.deletePersistentAll(primaryKeyId.toString());

            if (numberRemoved > 1)
                rval = true;
            else if (numberRemoved == 0)
                rval = false;
            else
                throw new Exception("More than one Order was removed by delete operation!");
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
    public Order getOrder(Long orderId, IEnums.SEEK_MODE mode) {
        Order rval = null;
        PersistenceManager pm = pmf.getPersistenceManager();

        try
        {
            if (orderId == null || mode == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(Order.class, "orderId == :orderId");
            query.setResultClass(Order.class);
            List<Order> queryList = (List<Order>) query.execute(orderId);

            if (queryList != null && queryList.size() > 0)
                rval = pm.detachCopy(queryList.get(0));
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
    public List<Order> getOrdersByUser(Long userId) {
        List<Order> rval = null;
        PersistenceManager pm = pmf.getPersistenceManager();

        try
        {
            User user = userAccessor.getUserAttached(userId, pm);

            if (user != null)
            {
                rval = user.getOrders();
            }
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
    public Boolean isUserOwnerOfCartItem(Long cartItemId, Long userId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cartItemId == null)
                throw new NullPointerException("Arguments cannot be null");

            Query query = pm.newQuery(User.class, "userCartItem.cartItemId == :cartItemObject && " +
                    "userOrder.orderItems.contains(userCartItem) &&" +
                    "orders.contains(userOrder) && userId == :userId");
            //query.declareImports("import com.troveup.brooklyn.orm.cart.model.CartItem");
            query.declareVariables("com.troveup.brooklyn.orm.cart.model.CartItem userCartItem; com.troveup.brooklyn.orm.order.model.Order userOrder");
            //query.declareVariables("com.troveup.brooklyn.orm.order.model.Order userOrder");
            query.setResult("count(this)");

            //This is stupid, but DN is throwing an error when I try to load by ID...  Gotta query some other way
            //CartItem item = cartAccessor.getCartItemAttached(cartItemId, pm);

            Long count = (Long) query.execute(cartItemId, userId);

            if (count > 0)
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
    public Boolean canLineItemBeCancelled(Long cartItemId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cartItemId == null)
                throw new NullPointerException("Arguements cannot be null");

            CartItem item = cartAccessor.getCartItemAttached(cartItemId, pm);

            if (!CartItem.getStatusesThatCannotBeCancelled().contains(item.getCartItemStatus()))
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
    public Boolean addCustomizationsToCartItem(Long cartItemId, List<ItemCustomization> customizations)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cartItemId == null || customizations == null || customizations.size() == 0)
                throw new NullPointerException("Arguments cannot be null or empty");

            CartItem item = cartAccessor.getCartItemAttached(cartItemId, pm);

            rval = item.getCustomizations().addAll(customizations);

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
    public Boolean incrementCartItemCustomizationIterationCounter(Long cartItemId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cartItemId == null)
                throw new NullPointerException("Argument cannot be null");

            CartItem item = cartAccessor.getCartItemAttached(cartItemId, pm);

            if (item.getCustomizationIteration() == null)
                item.setCustomizationIteration(1);
            else
                item.setCustomizationIteration(item.getCustomizationIteration() + 1);

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
    public Boolean updateCartItemPrototypeEmailStatus(Long cartItemId, CartItem.PROTOTYPE_EMAIL_STATE state)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cartItemId == null || state == null)
                throw new NullPointerException("Arguments cannot be null");

            CartItem item = cartAccessor.getCartItemAttached(cartItemId, pm);

            item.setPrototypeEmailState(state);
            item.setLastOutstandingPrototypeCheckDate(new Date());

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
    public Order getOrderByOrderNumber(String orderNumber, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Order rval = null;

        try
        {
            if (orderNumber == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(Order.class, "troveOrderNumber == :orderNumber");

            List<Order> queryList = (List<Order>) query.execute(orderNumber);

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
    public List<Order> getOrders(List<String> request, IEnums.ORDER_REQUEST_TYPE type) {
        List<Order> rval = null;
        PersistenceManager pm = pmf.getPersistenceManager();

        try
        {
            if (request == null || type == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            Query query = pm.newQuery(Order.class, ":p.contains(" + type.toString() + ")");
            query.setResultClass(Order.class);
            rval = (List<Order>) query.execute(request);

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
    public List<Order> getOrders(Date from, Date to) {
        List<Order> rval = null;
        PersistenceManager pm = pmf.getPersistenceManager();

        try
        {
            if (from == null || to == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            Query query = pm.newQuery(Order.class, "orderDate >= :from && orderDate <= :to");
            query.setResultClass(Order.class);
            rval = (List<Order>) query.execute(from, to);

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
    public List<Order> getOrders(PaymentDetails details) {
        List<Order> rval = null;
        PersistenceManager pm = pmf.getPersistenceManager();

        try
        {
            Query query = pm.newQuery(Order.class, "paymentDetailId == :detailId");
            query.setResultClass(Order.class);
            rval = (List<Order>) query.execute(details.getPaymentDetailId());
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
    public List<Order> getOrdersByBillingAddress(Address billingAddress) {
        List<Order> rval = null;
        PersistenceManager pm = pmf.getPersistenceManager();

        try
        {
            Query query = pm.newQuery(Order.class, "billingAddress.addressId == :addressId");
            query.setResultClass(Order.class);
            rval = (List<Order>) query.execute(billingAddress.getAddressId());
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
    public List<Order> getOrdersByShippingAddress(Address shippingAddress) {
        List<Order> rval = null;
        PersistenceManager pm = pmf.getPersistenceManager();

        try
        {
            Query query = pm.newQuery(Order.class, "shippingAddress.addressId == :addressId");
            query.setResultClass(Order.class);
            rval = (List<Order>) query.execute(shippingAddress.getAddressId());
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
    public Boolean checkIfOrderNumberExists(String orderNumber)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = true;

        try
        {
            if (orderNumber == null || orderNumber.length() == 0)
                throw new NullPointerException("Argument cannot be null or empty");

            Query query = pm.newQuery(Order.class, "troveOrderNumber.equals(:orderNumber)");
            query.setResult("count(this)");

            Long queryResult = (Long) query.execute(orderNumber);

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
    public List<Order> getPagedUserOrders(Long userId, List<Order.ORDER_STATUS> status,
                                          Integer collectionPage, Long collectionPageSize, Long extraResults,
                                          IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Order> rval = null;

        try {
            if (userId == null || collectionPage == null || collectionPageSize == null || extraResults == null ||
                    mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(Order.class, "user.userId == :userId && " +
                    "user.orders.contains(this) && :status.contains(this.orderStatus)");

            query.declareVariables("com.troveup.brooklyn.orm.user.model.User user");

            Long beginning = collectionPage * collectionPageSize;
            Long end = (collectionPage + 1) * collectionPageSize + extraResults;
            query.setRange(beginning, end);
            query.setOrdering("orderDate DESC");

            rval = (List<Order>) query.execute(userId, status);

            rval = (List<Order>) pm.detachCopyAll(rval);

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateOrderStatus(Long orderId, Order.ORDER_STATUS status)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (orderId == null || status == null)
                throw new NullPointerException("Arguments cannot be null");

            Order order = getOrderAttached(orderId, pm);

            order.setOrderStatus(status);

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
    public String getOrderEmailByCartItemId(Long cartItemId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        String rval = null;

        try
        {
            if (cartItemId == null)
                throw new NullPointerException("Argument cannot be null");

            Query query = pm.newQuery(Order.class, "cartItem.cartItemId == :cartItemId && orderItems.contains(cartItem)");
            query.declareVariables("com.troveup.brooklyn.orm.cart.model.CartItem cartItem");

            query.setResultClass(String.class);
            query.setResult("this.checkoutEmail");

            rval = (String) query.execute(cartItemId);

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
    public Order getOrderByCartItemId(Long cartItemId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Order rval = null;

        try
        {
            if (cartItemId == null)
                throw new NullPointerException("Argument cannot be null");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(Order.class, "cartItem.cartItemId == :cartItemId && orderItems.contains(cartItem)");
            query.declareVariables("com.troveup.brooklyn.orm.cart.model.CartItem cartItem");

            List<Order> queryList = (List<Order>) query.execute(cartItemId);

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
    public List<Order> getOrdersByStatus(Order.ORDER_STATUS status, Long limit, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Order> rval = null;

        try
        {
            if (status == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(Order.class, "orderStatus == :status");

            if (limit == null && limit > 0)
                query.setRange(0l, limit);

            rval = (List<Order>) query.execute(status);
            rval = (List<Order>) pm.detachCopyAll(rval);

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
    public List<Order> getOrdersWithPurchaserByStatus(Order.ORDER_STATUS status, Integer collectionPage, Long collectionPageSize, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Order> rval = null;

        try
        {
            if (status == null || collectionPage == null || collectionPageSize == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);
            Query query = pm.newQuery(Order.class, "orderStatus == :status");
            query = constrainQueryByPage(collectionPage, collectionPageSize, query);
            query.setOrdering("orderDate DESC");

            rval = (List<Order>) query.execute(status);

            if (rval != null && rval.size() > 0) {
                rval = populateOrderPurchaserFields(rval, pm);
                rval = detachCopyWithPurchaser(rval, pm);
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
    public List<Order> getOrdersWithPurchaserByDateRange(Date beginDate, Date endDate, Integer collectionPage,
                                                         Long collectionPageSize, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Order> rval = null;

        try
        {
            if (beginDate == null || endDate == null || collectionPage == null || collectionPageSize == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);
            Query query = pm.newQuery(Order.class, "orderDate >= :beginDate && orderDate <= :endDate");
            query = constrainQueryByPage(collectionPage, collectionPageSize, query);
            query.setOrdering("orderDate DESC");

            rval = (List<Order>) query.execute(beginDate, endDate);

            if (rval != null && rval.size() > 0)
            {
                rval = populateOrderPurchaserFields(rval, pm);
                rval = detachCopyWithPurchaser(rval, pm);
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
    public List<Order> getOrdersWithPurchaserByAmountRange(BigDecimal lowerBoundAmount, BigDecimal upperBoundAmount,
                                                           Integer collectionPage, Long collectionPageSize,
                                                           IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Order> rval = null;

        try
        {
            if (lowerBoundAmount == null || upperBoundAmount == null || collectionPage == null || collectionPageSize == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);
            Query query = pm.newQuery(Order.class, "grandTotal >= :lowerBoundAmount && grandTotal <= :upperBoundAmount");
            query = constrainQueryByPage(collectionPage, collectionPageSize, query);
            query.setOrdering("orderDate DESC");

            rval = (List<Order>) query.execute(lowerBoundAmount, upperBoundAmount);

            if (rval != null && rval.size() > 0)
            {
                rval = populateOrderPurchaserFields(rval, pm);
                rval = detachCopyWithPurchaser(rval, pm);
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
    public List<Order> getOrdersWithPurchaserThatContainCartItemStatus(CartItem.CART_ITEM_STATUS status,
                                                                       Integer collectionPage, Long collectionPageSize,
                                                                       IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Order> rval = null;

        try
        {
            if (status == null || collectionPage == null || collectionPageSize == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);
            Query query = pm.newQuery(Order.class, "orderItems.contains(cartItem) && cartItem.cartItemStatus == :status");
            query.declareVariables("com.troveup.brooklyn.orm.cart.model.CartItem cartItem;");
            query.setOrdering("orderDate DESC");
            query = constrainQueryByPage(collectionPage, collectionPageSize, query);

            rval = (List<Order>) query.execute(status);

            if (rval != null && rval.size() > 0)
            {
                rval = populateOrderPurchaserFields(rval, pm);
                rval = detachCopyWithPurchaser(rval, pm);
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
    public List<Order> getOrdersWithPurchaserThatContainCartItemCategory(String category, Integer collectionPage,
                                                                         Long collectionPageSize, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Order> rval = null;

        try
        {
            if (category == null || category.length() == 0 || collectionPage == null || collectionPageSize == null ||
                    mode == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            configureFetchGroups(pm, mode);

            //Workaround for DataNucleus' inability to reference into a join table
            //Selects a list of order IDs that contain cart items that are of the specified
            //category
            Query query = pm.newQuery("javax.jdo.query.SQL", "SELECT DISTINCT ORDERID FROM `ORDER`" +
                    "  JOIN ORDER_ORDERITEMS OI ON OI.ORDERID_OID = `ORDER`.ORDERID" +
                    "  JOIN CARTITEM_CARTITEMREFERENCE CIR ON CIR.CARTITEMID_OID = OI.CARTITEMID_EID" +
                    "  JOIN ITEM ON CIR.ITEMID_ID = ITEM.ITEMID" +
                    " WHERE ITEM.CATEGORY = ?");

            List<Long> orderIds = (List<Long>) query.execute(category);

            query = pm.newQuery(Order.class, ":orderIds.contains(this.orderId)");
            query.setOrdering("orderDate DESC");
            query = constrainQueryByPage(collectionPage, collectionPageSize, query);
            rval = (List<Order>) query.execute(orderIds);

            if (rval != null && rval.size() > 0)
            {
                rval = populateOrderPurchaserFields(rval, pm);
                rval = detachCopyWithPurchaser(rval, pm);
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
    public List<Order> getOrdersWithPurchaserThatContainGenericItemCategory(String category, Integer collectionPage, Long collectionPageSize, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Order> rval = null;

        try
        {
            if (category == null || category.length() == 0 || collectionPage == null || collectionPageSize == null ||
                    mode == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            configureFetchGroups(pm, mode);

            //Workaround for DataNucleus' inability to reference into a join table
            //Selects a list of order IDs that contain cart items that are of the specified
            //category
            Query query = pm.newQuery("javax.jdo.query.SQL", "SELECT DISTINCT ORDERID FROM `ORDER` " +
                    "JOIN ORDER_GENERICITEMSLIST GIL ON GIL.ORDERID_OID = `ORDER`.ORDERID " +
                    "JOIN GENERICITEM GI ON GI.GENERICITEMID = GIL.GENERICITEMID_EID " +
                    "WHERE GI.ITEMTYPE = ?");

            List<Long> orderIds = (List<Long>) query.execute(category);

            query = pm.newQuery(Order.class, ":orderIds.contains(this.orderId)");
            query.setOrdering("orderDate DESC");
            query = constrainQueryByPage(collectionPage, collectionPageSize, query);
            rval = (List<Order>) query.execute(orderIds);

            if (rval != null && rval.size() > 0)
            {
                rval = populateOrderPurchaserFields(rval, pm);
                rval = detachCopyWithPurchaser(rval, pm);
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
    public List<Order> getOrdersWithPurchaserByUserId(Long userId, Integer collectionPage,
                                                      Long collectionPageSize, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Order> rval = null;

        try {
            if (userId == null || collectionPage == null || collectionPageSize == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);
            Query query = pm.newQuery(Order.class, "user.userId == :userId && user.orders.contains(this)");
            query.declareVariables("com.troveup.brooklyn.orm.user.model.User user");
            query.setOrdering("orderDate DESC");
            query = constrainQueryByPage(collectionPage, collectionPageSize, query);

            rval = (List<Order>) query.execute(userId);

            if (rval != null && rval.size() > 0)
            {
                rval = populateOrderPurchaserFields(rval, pm);
                rval = detachCopyWithPurchaser(rval, pm);
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
    public List<Order> getOrdersWithPurchaserByUsername(String username, Integer collectionPage, Long collectionPageSize, IEnums.SEEK_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Order> rval = null;

        try {
            if (username == null || collectionPage == null || collectionPageSize == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            //String regexString = "^.*(?i){{x}}(?i).*$";
            //String matchString = regexString.replace("{{x}}", username);


            username = username.toLowerCase();
            String matchString = ".*{{x}}.*";
            matchString = matchString.replace("{{x}}", username);

            Query query = pm.newQuery(Order.class, "user.username.toLowerCase().matches(:matchstring) && user.orders.contains(this)");
            query.declareVariables("com.troveup.brooklyn.orm.user.model.User user");
            query.setOrdering("orderDate DESC");
            query = constrainQueryByPage(collectionPage, collectionPageSize, query);

            rval = (List<Order>) query.execute(matchString);

            if (rval != null && rval.size() > 0)
            {
                rval = populateOrderPurchaserFields(rval, pm);
                rval = detachCopyWithPurchaser(rval, pm);
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
    public List<Order> getOrdersWithPurchaserByAccountEmail(String email, Integer collectionPage, Long collectionPageSize, IEnums.SEEK_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Order> rval = null;

        try {
            if (email == null || collectionPage == null || collectionPageSize == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            email = email.toLowerCase();
            String emailRegex = "(" + email + ".*)";
            Query query = pm.newQuery("javax.jdo.query.SQL", "select ORDERID from `ORDER` " +
                                                                " where lower(`ORDER`.CHECKOUTEMAIL) REGEXP ?");
            query = constrainQueryByPage(collectionPage, collectionPageSize, query);
            List<Long> orderIdsCheckoutEmail = (List<Long>) query.execute(emailRegex);

            String matchString = ".*{{x}}.*";
            matchString = matchString.replace("{{x}}", email);
            query = pm.newQuery(Order.class, "user.email.toLowerCase().matches(:matchstring) && user.orders.contains(this)");
            query.declareVariables("com.troveup.brooklyn.orm.user.model.User user");
            query.setOrdering("orderDate DESC");
            query.setResult("this.orderId");
            query = constrainQueryByPage(collectionPage, collectionPageSize, query);
            List<Long> orderIdsUserEmail = (List<Long>) query.execute(matchString);

            List<Long> orderIds = new ArrayList();
            orderIds.addAll(orderIdsCheckoutEmail);
            orderIds.addAll(orderIdsUserEmail);

            query = pm.newQuery(Order.class, ":orderIds.contains(this.orderId)");
            query = constrainQueryByPage(collectionPage, collectionPageSize, query);
            rval = (List<Order>) query.execute(orderIds);

            if (rval != null && rval.size() > 0)
            {
                rval = populateOrderPurchaserFields(rval, pm);
                rval = detachCopyWithPurchaser(rval, pm);
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
    public List<Order> getOrdersWithPurchaserByName(String name, Integer collectionPage, Long collectionPageSize, IEnums.SEEK_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Order> rval = null;

        try {
            if (name == null || collectionPage == null || collectionPageSize == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            name = name.toLowerCase();
            String nameRegex = name.replace(" ", ".*)|(");
            nameRegex = "(" + nameRegex + ".*)";
            Query query = pm.newQuery("javax.jdo.query.SQL", "select distinct ORDERID from `ORDER`" +
                    "join ORDER_BILLINGADDRESS " +
                    "join ORDER_SHIPPINGADDRESS " +
                    "join ADDRESS where (ORDER_SHIPPINGADDRESS.ORDERID_OID = ORDER_BILLINGADDRESS.ORDERID_OID" +
                    "                    and `ORDER`.ORDERID = ORDER_BILLINGADDRESS.ORDERID_OID" +
                    "                    and ADDRESS.ADDRESSID = ORDER_BILLINGADDRESS.ADDRESSID_ID" +
                    "                    and (lower(ADDRESS.FIRST_NAME) REGEXP ?1" +
                    "                         or lower(ADDRESS.LAST_NAME) REGEXP ?1))" +
                    "                or (ORDER_SHIPPINGADDRESS.ORDERID_OID = ORDER_BILLINGADDRESS.ORDERID_OID" +
                    "                    and `ORDER`.ORDERID = ORDER_SHIPPINGADDRESS.ORDERID_OID" +
                    "                    and ADDRESS.ADDRESSID = ORDER_SHIPPINGADDRESS.ADDRESSID_ID" +
                    "                    and (lower(ADDRESS.FIRST_NAME) REGEXP ?1" +
                    "                         or lower(ADDRESS.LAST_NAME) REGEXP ?1))");
            query = constrainQueryByPage(collectionPage, collectionPageSize, query);
            List<Long> orderIdsAddress = (List<Long>) query.execute(nameRegex);

            String matchString = ".*{{x}}.*";
            matchString = matchString.replace("{{x}}", name);
            query = pm.newQuery(Order.class, "user.firstName.toLowerCase().matches(:matchstring) && user.orders.contains(this)");
            query.declareVariables("com.troveup.brooklyn.orm.user.model.User user");
            query.setOrdering("orderDate DESC");
            query.setResult("this.orderId");
            query = constrainQueryByPage(collectionPage, collectionPageSize, query);
            List<Long> orderIdsUserFullName = (List<Long>) query.execute(matchString);

            List<Long> orderIds = new ArrayList();
            orderIds.addAll(orderIdsAddress);
            orderIds.addAll(orderIdsUserFullName);

            query = pm.newQuery(Order.class, ":orderIds.contains(this.orderId)");
            query = constrainQueryByPage(collectionPage, collectionPageSize, query);
            rval = (List<Order>) query.execute(orderIds);

            if (rval != null && rval.size() > 0)
            {
                rval = populateOrderPurchaserFields(rval, pm);
                rval = detachCopyWithPurchaser(rval, pm);
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
    public List<Order> getOrdersWithPurchaserByPromoCode(String promoCode, Integer collectionPage, Long collectionPageSize, IEnums.SEEK_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Order> rval = null;

        try {
            if (promoCode == null || collectionPage == null || collectionPageSize == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

//            String matchString = ".*{{x}}.*";
//            matchString = matchString.replace("{{x}}", promoCode);
            String matchString = promoCode.toLowerCase();

            Query query = pm.newQuery(Order.class, "promoCodeObj.promoCode.toLowerCase().matches(:matchString) && this.appliedPromoCodes.contains(promoCodeObj)");
            query.declareVariables("com.troveup.brooklyn.orm.cart.model.PromoCode promoCodeObj");
            query.setOrdering("orderDate DESC");
            query = constrainQueryByPage(collectionPage, collectionPageSize, query);

            rval = (List<Order>) query.execute(matchString);

            if (rval != null && rval.size() > 0)
            {
                rval = populateOrderPurchaserFields(rval, pm);
                rval = detachCopyWithPurchaser(rval, pm);
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
    public List<Order> getOrdersWithPurchaserAll(Integer collectionPage, Long collectionPageSize, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Order> rval = null;

        try
        {
            if (collectionPage == null || collectionPageSize == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);
            Query query = pm.newQuery(Order.class, "1 == 1");
            query.setOrdering("orderDate DESC");
            query = constrainQueryByPage(collectionPage, collectionPageSize, query);

            rval = (List<Order>) query.execute();

            if (rval != null && rval.size() > 0)
            {
                rval = populateOrderPurchaserFields(rval, pm);
                rval = detachCopyWithPurchaser(rval, pm);
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
    public Order getOrderWithPurchaserByOrderNumber(String orderNumber, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Order rval = null;

        try
        {
            if (orderNumber == null || mode == null || orderNumber.length() == 0)
                throw new NullPointerException("Arguments cannot be null or empty");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(Order.class, "troveOrderNumber == :orderNumber");
            List<Order> queryResult = (List<Order>) query.execute(orderNumber);

            if (queryResult != null && queryResult.size() > 0)
            {
                queryResult = populateOrderPurchaserFields(queryResult, pm);
                queryResult = detachCopyWithPurchaser(queryResult, pm);

                rval = queryResult.get(0);
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
    public Order getOrderWithPurchaserByOrderId(Long orderId, IEnums.SEEK_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Order rval = null;

        try
        {
            if (orderId == null || mode == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(Order.class, "orderId == :orderId");
            List<Order> queryResult = (List<Order>) query.execute(orderId);

            if (queryResult != null && queryResult.size() > 0)
            {
                queryResult = populateOrderPurchaserFields(queryResult, pm);
                queryResult = detachCopyWithPurchaser(queryResult, pm);

                rval = queryResult.get(0);
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
    public List<Order> populateOrderPurchaserFields(List<Order> persistenceManagedOrders, PersistenceManager pm)
    {
        for (Order order : persistenceManagedOrders)
        {
            Query query = pm.newQuery(User.class, "orders.contains(:order)");
            List<User> queryResult = (List<User>) query.execute(order);

            if (queryResult != null && queryResult.size() > 0)
            {
                order.setPurchaser(pm.detachCopy(queryResult.get(0)));
            }
        }

        return persistenceManagedOrders;
    }

    public List<Order> detachCopyWithPurchaser(List<Order> orders, PersistenceManager pm)
    {
        List<Order> detached = new ArrayList<>();

        for (Order order : orders) {
            User user = order.getPurchaser();
            Order detachedCopy = pm.detachCopy(order);
            detachedCopy.setPurchaser(user);
            detached.add(detachedCopy);
        }

        return detached;
    }

    @Override
    public List<Order> getOrdersAttached(List<Long> orderId, PersistenceManager pm)
    {
        Query query = pm.newQuery(Order.class, "orderIdList.contains(orderId)");
        return (List<Order>) query.execute(orderId);
    }

    @Override
    public Boolean updateOrderFinalSettlementAmount(Long orderId, BigDecimal finalSettlementAmount)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (orderId == null || finalSettlementAmount == null)
                throw new NullPointerException("Arguments cannot be null");

            Order order = getOrderAttached(orderId, pm);

            order.setFinalSettlementAmount(finalSettlementAmount);

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
    public Boolean updateOrderOverallSettlementAmount(Long orderId, BigDecimal finalSettlementAmount, BigDecimal finalSettlementTax, BigDecimal finalSettlementSubtotal, BigDecimal finalSettlementShipping) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (orderId == null || finalSettlementAmount == null || finalSettlementTax == null || finalSettlementSubtotal == null)
                throw new NullPointerException("Arguments cannot be null");

            Order order = getOrderAttached(orderId, pm);

            order.setFinalSettlementAmount(finalSettlementAmount);
            order.setFinalSettlementTax(finalSettlementTax);
            order.setFinalSettlementSubtotal(finalSettlementSubtotal);
            order.setFinalSettlementShipping(finalSettlementShipping);

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
    public Boolean addOrderNote(Long orderId, Long userIdCreator, String noteText, Date timeCreated) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (orderId == null || userIdCreator == null || noteText == null)
                throw new NullPointerException("Arguments cannot be null");

            Order order = getOrderAttached(orderId, pm);
            User user = userAccessor.getUserAttached(userIdCreator, pm);

            OrderNote orderNote = new OrderNote();
            orderNote.setUserCreator(user);
            orderNote.setCreationDate(timeCreated);
            orderNote.setNote(noteText);

            if (order.getOrderNotes() == null) {
                order.setOrderNotes(new ArrayList<OrderNote>());
            }
            order.getOrderNotes().add(0, orderNote);

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
    public Boolean setOrderAppliedCredit(Long orderId, BigDecimal amount)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (orderId == null || amount == null)
                throw new NullPointerException("Arguments cannot be null");

            Order order = getOrderAttached(orderId, pm);
            order.setStoreBalanceOffset(amount);

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
    public Boolean setOrderCreditRefunded(Long orderId, BigDecimal amount) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (orderId == null || amount == null)
                throw new NullPointerException("Arguments cannot be null");

            Order order = getOrderAttached(orderId, pm);
            order.setRefundedStoreBalance(amount);

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
    public Boolean updateOrderErrorMessage(Long orderId, String errorMessage) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (orderId == null || errorMessage == null)
                throw new NullPointerException("Arguments cannot be null");

            Order order = getOrderAttached(orderId, pm);

            order.setSettlementMessage(errorMessage);

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

    /**
     * Cancels line item print orders that are of a status that can be cancelled, and are also not prototypes.  Otherwise
     * leaves them alone.
     *
     * @param cartItemId Cart item identifier for the record to be cancelled.
     *
     * @return Whether or not a print order for the cart item record was marked as cancelled.
     */
    @Override
    public Boolean cancelOrderLineItem(Long cartItemId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cartItemId == null)
                throw new NullPointerException("Argument cannot be null");

            CartItem cartItem = cartAccessor.getCartItemAttached(cartItemId, pm);

            for (PrintOrder printOrder : cartItem.getManufacturerOrders())
            {
                if (PrintOrder.getStatusesThatCanBeCancelled().contains(printOrder.getStatus())
                        && !printOrder.getIsPrototype())
                {
                    printOrder.setStatus(PrintOrder.ORDER_STATUS.CANCELLED);

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
    public Boolean incrementOrderSettlementAttempts(Long orderId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (orderId == null)
                throw new NullPointerException("Arguments cannot be null");

            Order order = getOrderAttached(orderId, pm);

            if (order.getSettlementAttempts() == null)
                order.setSettlementAttempts(1);
            else
                order.setSettlementAttempts(order.getSettlementAttempts() + 1);

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
    public Order getOrderByShippingTrackingNumber(String trackingNumber, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Order rval = null;

        try
        {
            if (trackingNumber == null || trackingNumber.length() == 0 || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(Order.class, "label.trackingNumber == :trackingNumber && " +
                    "printOrder.printLabels.contains(label) && cartItem.manufacturerOrders.contains(printOrder) && " +
                    "orderItems.contains(cartItem)");
            query.declareVariables("com.troveup.brooklyn.orm.order.model.EasyPostPostageLabel label; " +
                    "com.troveup.brooklyn.orm.order.model.PrintOrder printOrder; " +
                    "com.troveup.brooklyn.orm.cart.model.CartItem cartItem;");
            query.execute(trackingNumber);

            List<Order> queryList = (List<Order>) query.execute(trackingNumber);

            if (queryList != null && queryList.size() > 0)
                rval = queryList.get(0);

            rval = pm.detachCopy(rval);

        } catch (Exception e)
        {
            logError(e);
        } finally
        {
            pm.close();
        }

        return rval;
    }

    public Order getOrderAttached(Long orderId, PersistenceManager pm)
    {
        Order rval = null;

        if (orderId == null || pm == null)
            throw new IllegalArgumentException("Parameters must have at least one element.");

        Query query = pm.newQuery(Order.class, "orderId == :orderId");
        query.setResultClass(Order.class);

        List<Order> queryList = (List<Order>) query.execute(orderId);

        if (queryList != null && queryList.size() > 0)
            rval = queryList.get(0);

        return rval;

    }

    public Order getOrderAttached(String orderNumber, PersistenceManager pm)
    {
        Order rval = null;

        if (orderNumber == null || pm == null)
            throw new NullPointerException("Arguments cannot be null or empty");

        Query query = pm.newQuery(Order.class, "troveOrderNumber == :orderNumber");

        List<Order> queryList = (List<Order>) query.execute(orderNumber);

        if (queryList != null && queryList.size() > 0)
            rval = queryList.get(0);

        return rval;
    }

    @Override
    public String getOrderFirstLastNameByCartItemId(Long cartItemId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        String rval = null;

        try
        {
            if (cartItemId == null)
                throw new NullPointerException("Argument cannot be null");

            Query query = pm.newQuery(Order.class, "this.orderItems.contains(cartItem) && cartItem.cartItemId == :cartItemId");

            query.declareVariables("com.troveup.brooklyn.orm.cart.model.CartItem cartItem");

            List<Order> queryResult = (List<Order>) query.execute(cartItemId);

            if (queryResult != null && queryResult.size() > 0)
                rval = queryResult.get(0).getCartReference().getShippingAddress().getFirstName() + " " +
                        queryResult.get(0).getCartReference().getShippingAddress().getLastName();

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
    public Long getFtueOrderCountByAddress(String addressLine1, String addressLine2, String zipCode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Long rval = 0L;

        try
        {
            if (addressLine1 == null || addressLine1.length() == 0 || zipCode == null || zipCode.length() == 0)
                throw new NullPointerException("Arguments cannot be null");

            //DataNucleus isn't smart enough to index into join tables with JDOQL.  Have to use raw SQL :(
            /*Query query = pm.newQuery(Order.class, "this.shippingAddress.addressLine1.toLowerCase() == :addressLine1 && " +
                    "this.shippingAddress.postalCode == :zipCode && originFtue == true");
            query.setResult("count(this)");*/

            String queryString = "select COUNT(*) from `ORDER`" +
                " JOIN ORDER_SHIPPINGADDRESS ON `ORDER`.ORDERID = ORDER_SHIPPINGADDRESS.ORDERID_OID" +
                " JOIN ADDRESS ON ORDER_SHIPPINGADDRESS.ADDRESSID_ID = ADDRESS.ADDRESSID" +
                " WHERE `ORDER`.ORIGINFTUE = true AND LOWER(ADDRESS_LINE_1) = LOWER(?) AND ADDRESS.POSTAL_CODE = ?";

            if (addressLine2 != null && addressLine2.length() > 0)
            {
                queryString += " AND LOWER(ADDRESS_LINE_2) = LOWER(?)";
            }

            Query query = pm.newQuery("javax.jdo.query.SQL", queryString);
            List queryResult = null;

            if (addressLine2 != null && addressLine2.length() > 0)
            {
                queryResult = (List) query.execute(addressLine1, zipCode, addressLine2);
            }
            else
            {
                queryResult = (List) query.execute(addressLine1, zipCode);
            }

            rval = (Long) queryResult.get(0);

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
    public Long getOrderIdContainingGenericItem(Long genericItemId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Long rval = null;

        try
        {
            if (genericItemId == null)
                throw new NullPointerException("Argument cannot be null");

            //Workaround for DataNucleus' inability to reference into a join table
            //Selects the orderId of the order that contains a given generic item ID
            Query query = pm.newQuery("javax.jdo.query.SQL", "SELECT DISTINCT ORDERID_OID FROM " +
                    "ORDER_GENERICITEMSLIST WHERE GENERICITEMID_EID = ?");
            List<Long> queryResult = (List<Long>) query.execute(genericItemId);

            if (queryResult != null && queryResult.size() > 0)
            {
                rval = queryResult.get(0);
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
    public Boolean associateShippingLabelWithOrder(Long orderId, EasyPostPostageLabel label)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            pm.setDetachAllOnCommit(true);

            if (orderId == null || label == null)
                throw new NullPointerException("Arguments cannot be null or empty.");

            Order order = getOrderAttached(orderId, pm);
            if (order.getShippingLabels() == null)
                order.setShippingLabels(new ArrayList<EasyPostPostageLabel>());

            order.getShippingLabels().add(label);

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
    public Order getOrderContainingShippingLabel(String shippingLabelTrackingNumber, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Order rval = null;

        try
        {
            if (shippingLabelTrackingNumber == null || mode == null)
                throw new NullPointerException("Arguments cannot be null or empty.");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(Order.class, "label.trackingNumber == :trackingNumber && this.shippingLabels.contains(label)");
            query.declareVariables("com.troveup.brooklyn.orm.order.model.EasyPostPostageLabel label");

            List<Order> queryResults = (List<Order>) query.execute(shippingLabelTrackingNumber);

            if (queryResults != null && queryResults.size() > 0)
                rval = queryResults.get(0);

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
    public List<Order> getOrdersContainingInfluencerItems(Long influencerId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Order> rval = null;

        try
        {
            if (influencerId == null || mode == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(Order.class, "simpleItem.controlReference == false " +
                    "&& simpleItem.active == true " +
                    "&& simpleItem.influencerUserAccountId == :influencerId " +
                    "&& genericItem.itemReferenceId == simpleItem.simpleItemId " +
                    "&& genericItemsList.contains(genericItem)");

            query.declareVariables("com.troveup.brooklyn.orm.simpleitem.model.SimpleItem simpleItem; com.troveup.brooklyn.orm.cart.model.GenericItem genericItem");
            rval = (List<Order>) query.execute(influencerId);

        } catch (Exception e)
        {
            logError(e);
        } finally
        {
            pm.close();
        }

        return rval;
    }

    public ShipmentTracking getShipmentTrackingAttached(Long trackingId, PersistenceManager pm)
    {
        ShipmentTracking rval = null;
        try
        {
            Query query = pm.newQuery(ShipmentTracking.class, "trackingId == :trackingId");
            query.setResultClass(ShipmentTracking.class);

            List<ShipmentTracking> queryList = (List<ShipmentTracking>) query.execute(trackingId);

            if (queryList != null && queryList.size() > 0)
                rval = queryList.get(0);
        }
        catch(Exception e)
        {
            logError(e);
        }

        return rval;
    }

    public Status getStatusAttached(Long statusId, PersistenceManager pm)
    {
        Status rval = null;
        try
        {
            Query query = pm.newQuery(Status.class, "statusId == :statusId");
            query.setResultClass(Status.class);

            List<Status> queryList = (List<Status>) query.execute(statusId);

            if (queryList != null && queryList.size() > 0)
                rval = queryList.get(0);

        }
        catch(Exception e)
        {
            logError(e);
        }

        return rval;
    }

    public static void getManagedOrderAddresses(Order order, PersistenceManager pm, ICountryAccessor countryAccessor, IUserAccessor userAccessor)
    {
        if (order.getBillingAddress() != null)
        {
            if (order.getBillingAddress().getAddressId() != null)
            {
                order.setBillingAddress(userAccessor.getAddressAttached(order.getBillingAddress().getAddressId(), pm));
            }
            else {
                if (order.getBillingAddress().getCountry() != null && order.getBillingAddress().getCountry().getPrimaryKeyId() != null)
                    order.getBillingAddress().setCountry(countryAccessor.getCountryAttached(order.getShippingAddress().getCountry().getPrimaryKeyId(), pm));

                if (order.getBillingAddress().getSubdivision() != null && order.getBillingAddress().getSubdivision().getPrimaryKeyId() != null)
                    order.getBillingAddress().setSubdivision(countryAccessor.getSubdivisionAttached(order.getShippingAddress().getSubdivision().getPrimaryKeyId(), pm));
            }
        }

        if (order.getShippingAddress() != null)
        {
            if (order.getShippingAddress().getAddressId() != null)
            {
                order.setShippingAddress(userAccessor.getAddressAttached(order.getShippingAddress().getAddressId(), pm));
            }
            else {
                if (order.getShippingAddress().getCountry() != null && order.getShippingAddress().getCountry().getPrimaryKeyId() != null)
                    order.getShippingAddress().setCountry(countryAccessor.getCountryAttached(order.getShippingAddress().getCountry().getPrimaryKeyId(), pm));

                //Get a persistent managed object and set that as the subdivision
                if (order.getShippingAddress().getSubdivision() != null && order.getShippingAddress().getSubdivision().getPrimaryKeyId() != null)
                    order.getShippingAddress().setSubdivision(countryAccessor.getSubdivisionAttached(order.getShippingAddress().getSubdivision().getPrimaryKeyId(), pm));
            }
        }
    }

    private void configureFetchGroups(PersistenceManager pm, IEnums.SEEK_MODE mode)
    {
        if (mode != null && mode != IEnums.SEEK_MODE.QUICK)
        {
            if (mode == IEnums.SEEK_MODE.ORDER_DETAILED)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(Order.class, "detailedOrderFetch");

                List<String> fetchGroupFields = Order.getOrdersMediumFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Status.class, "statusFetch");
                fetchGroupFields = Status.getStatusFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(ShipmentTracking.class, "shipmentFetch");
                fetchGroupFields = ShipmentTracking.getTrackingFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Address.class, "addressFetch");
                fetchGroupFields = Address.getAddressCartFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(CartItem.class, "cartItemFetch");
                fetchGroupFields = CartItem.getCartItemMediumFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(GenericItem.class, "genericItemFetch");
                fetchGroupFields = GenericItem.getFullGenericItemFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(CartItemAttribute.class, "cartItemAttributeFetch");
                fetchGroupFields = CartItemAttribute.getCartItemAttributeFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Item.class, "itemAttributeFetch");
                fetchGroupFields = Item.getItemWithAttributesFetchGroupFields();
                for (String field : fetchGroupFields)
                {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("detailedOrderFetch");
                fetchPlan.addGroup("statusFetch");
                fetchPlan.addGroup("shipmentFetch");
                fetchPlan.addGroup("addressFetch");
                fetchPlan.addGroup("cartItemFetch");
                fetchPlan.addGroup("genericItemFetch");
                fetchPlan.addGroup("cartItemAttributeFetch");
                fetchPlan.addGroup("itemAttributeFetch");
                fetchPlan.setMaxFetchDepth(6);
            }
            else if (mode == IEnums.SEEK_MODE.ORDER_FULL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(Order.class, "detailedOrderFetch");

                List<String> fetchGroupFields = Order.getOrdersFullFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Cart.class, "cartFetch");
                fetchGroupFields = Cart.getCartPromoFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Status.class, "statusFetch");
                fetchGroupFields = Status.getStatusFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(ShipmentTracking.class, "shipmentFetch");
                fetchGroupFields = ShipmentTracking.getTrackingFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Address.class, "addressFetch");
                fetchGroupFields = Address.getAddressCartFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(CartItem.class, "cartItemFetch");
                fetchGroupFields = CartItem.getCartItemFullFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(PrintOrder.class, "printOrderFetch");
                fetchGroupFields = PrintOrder.getPrintOrderFullFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(GenericItem.class, "genericItemFetch");
                fetchGroupFields = GenericItem.getFullGenericItemFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(CartItemAttribute.class, "cartItemAttributeFetch");
                fetchGroupFields = CartItemAttribute.getCartItemAttributeFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Item.class, "itemAttributeFetch");
                fetchGroupFields = Item.getItemAttributesAndImagesFetchGroupFields();
                for (String field : fetchGroupFields)
                {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(OrderNote.class, "orderNoteFetch");
                fetchGroupFields = OrderNote.getFullFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("printOrderFetch");
                fetchPlan.addGroup("detailedOrderFetch");
                fetchPlan.addGroup("cartFetch");
                fetchPlan.addGroup("statusFetch");
                fetchPlan.addGroup("shipmentFetch");
                fetchPlan.addGroup("addressFetch");
                fetchPlan.addGroup("cartItemFetch");
                fetchPlan.addGroup("genericItemFetch");
                fetchPlan.addGroup("cartItemAttributeFetch");
                fetchPlan.addGroup("itemAttributeFetch");
                fetchPlan.addGroup("orderNoteFetch");
                fetchPlan.setMaxFetchDepth(20);
            }
            else if (mode == IEnums.SEEK_MODE.ORDER_QUICK)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(Order.class, "quickOrderFetch");

                List<String> fetchGroupFields = Order.getOrdersMediumFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("quickOrderFetch");
                fetchPlan.setMaxFetchDepth(2);
            }
            else if (mode == IEnums.SEEK_MODE.ORDER_UPLOAD)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(Order.class, "quickOrderFetch");

                List<String> fetchGroupFields = Order.getOrdersUploadFetchgroups();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(CartItem.class, "cartItemUploadFetch");
                fetchGroupFields = CartItem.getCartItemUploadFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Item.class, "itemUploadFetch");
                fetchGroupFields = Item.getItemWithAttributesFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("quickOrderFetch");
                fetchPlan.addGroup("cartItemUploadFetch");
                fetchPlan.addGroup("itemUploadFetch");
                fetchPlan.setMaxFetchDepth(5);
            }
            else if (mode == IEnums.SEEK_MODE.ORDER_ADDRESS) {
                FetchGroup fetchGroup = pm.getFetchGroup(Order.class, "orderAddressFetch");

                List<String> fetchGroupFields = Order.getOrdersAddressFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Address.class, "addressFetch");
                fetchGroupFields = Address.getAddressCartFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("orderAddressFetch");
                fetchPlan.addGroup("addressFetch");
                fetchPlan.setMaxFetchDepth(6);
            }
            else if (mode == IEnums.SEEK_MODE.ORDER_SETTLE)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(Order.class, "orderSettleFetchGroup");

                List<String> fetchGroupFields = Order.getOrdersSettlementFetchGroups();
                for (String field : fetchGroupFields)
                {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Address.class, "addressFetch");
                fetchGroupFields = Address.getAddressCartFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(CartItem.class, "cartItemFetch");
                fetchGroupFields = CartItem.getCartItemOrderSettlementFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(GenericItem.class, "genericItemFetch");
                fetchGroupFields = GenericItem.getFullGenericItemFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("orderSettleFetchGroup");
                fetchPlan.addGroup("addressFetch");
                fetchPlan.addGroup("cartItemFetch");
                fetchPlan.addGroup("genericItemFetch");
                fetchPlan.setMaxFetchDepth(4);

            }
            else if (mode == IEnums.SEEK_MODE.PROMO_FULL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(PromoCode.class, "fullPromoCode");

                List<String> fetchGroupFields = PromoCode.getPromoCodeFullFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("fullPromoCode");
                fetchPlan.setMaxFetchDepth(2);
            }
            else if (mode == IEnums.SEEK_MODE.ORDER_LINE_ITEMS)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(Order.class, "orderLineItemFetch");
                List<String> fetchGroupFields = Order.getOrdersLineItemFetchGroups();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(OrderNote.class, "orderNoteFetch");
                fetchGroupFields = OrderNote.getFullFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("orderLineItemFetch");
                fetchPlan.addGroup("orderNoteFetch");

                fetchPlan.setMaxFetchDepth(4);
            }
            else if (mode == IEnums.SEEK_MODE.ORDER_INFLUENCER_DASHBOARD)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(Order.class, "influencerOrderFetch");

                List<String> fetchGroupFields = Order.getOrderInfluencerDashboardFetchGroups();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("influencerOrderFetch");
                fetchPlan.setMaxFetchDepth(4);
            }
        }
    }
}
