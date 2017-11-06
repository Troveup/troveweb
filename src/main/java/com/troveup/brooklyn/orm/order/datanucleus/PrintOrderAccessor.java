package com.troveup.brooklyn.orm.order.datanucleus;

import com.troveup.brooklyn.orm.cart.datanucleus.CartAccessor;
import com.troveup.brooklyn.orm.cart.model.Cart;
import com.troveup.brooklyn.orm.cart.model.CartItem;
import com.troveup.brooklyn.orm.common.datanucleus.ObjectAccessor;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.ftui.model.FtueCheckout;
import com.troveup.brooklyn.orm.ftui.model.FtuePersistedRecord;
import com.troveup.brooklyn.orm.ftui.model.FtueRequest;
import com.troveup.brooklyn.orm.order.interfaces.IPrintOrderAccessor;
import com.troveup.brooklyn.orm.order.model.EasyPostPostageLabel;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.order.model.PrintOrder;
import com.troveup.brooklyn.orm.user.model.Address;
import com.troveup.brooklyn.util.DateUtils;

import javax.jdo.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 7/26/15.
 */
public class PrintOrderAccessor extends ObjectAccessor implements IPrintOrderAccessor
{

    public PrintOrderAccessor(PersistenceManagerFactory pmfProxy)
    {
        super(pmfProxy);
    }

    @Override
    public Boolean persistPrintOrder(PrintOrder order)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (order == null)
                throw new NullPointerException("Order cannot be null");

            rval = pm.makePersistent(order) != null;


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
    public Boolean updatePrintOrderStatus(Long printOrderId, PrintOrder.ORDER_STATUS status)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (printOrderId == null || status == null)
                throw new NullPointerException("Arguments cannot be null");

            PrintOrder order = getPrintOrderAttached(printOrderId, pm);

            order.setStatus(status);

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

    public PrintOrder getPrintOrderAttached(Long printOrderId, PersistenceManager pm)
    {
        PrintOrder rval = null;

        Query query = pm.newQuery(PrintOrder.class, "printOrderPrimaryKeyId == :printOrderId");

        List<PrintOrder> queryList = (List<PrintOrder>) query.execute(printOrderId);

        if (queryList != null && queryList.size() > 0)
            rval = queryList.get(0);

        return rval;

    }

    @Override
    public PrintOrder getPrintOrderByOrderNumber(String orderNumber,
                                                 PrintOrder.ORDER_SYSTEM orderSystem, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        PrintOrder rval = null;

        try
        {
            if (orderNumber == null || orderSystem == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            String queryString;
            Long orderNumberLongVal = null;

            configureFetchGroups(pm, mode);

            if (orderSystem == PrintOrder.ORDER_SYSTEM.FTUE)
                queryString = "orderSystem == :orderSystem && checkoutReference.checkoutSessionId == :orderNumber";
            else if (orderSystem == PrintOrder.ORDER_SYSTEM.CART) {
                queryString = "orderSystem == :orderSystem && printOrderPrimaryKeyId == :orderNumber";
                orderNumberLongVal = Long.parseLong(orderNumber);
            }
            else {
                queryString = "orderSystem == :orderSystem && persistedRecordReference.ftuePersistedRecordId == :orderNumber";
                orderNumberLongVal = Long.parseLong(orderNumber);
            }

            Query query = pm.newQuery(PrintOrder.class, queryString);

            List<PrintOrder> queryList;

            if (orderNumberLongVal == null)
                queryList = (List<PrintOrder>) query.execute(orderSystem, orderNumber);
            else
                queryList = (List<PrintOrder>) query.execute(orderSystem, orderNumberLongVal);

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
    public List<PrintOrder> getPrintOrdersByStatus(PrintOrder.ORDER_STATUS status, IEnums.SEEK_MODE mode, Long limit)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<PrintOrder> rval = null;

        try
        {
            if (status == null || mode == null || limit == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(PrintOrder.class, "status == :status");

            if (limit > 0l)
            {
                query.setRange(0l, limit);
            }

            rval = (List<PrintOrder>) query.execute(status);

            rval = (List<PrintOrder>) pm.detachCopyAll(rval);
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
    public Boolean updatePrintOrderStatus(String manufacturerOrderNumber, PrintOrder.ORDER_STATUS status)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (manufacturerOrderNumber == null || manufacturerOrderNumber.length() == 0 || status == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            Query query = pm.newQuery(PrintOrder.class, "manufacturerOrderId == :manufacturerOrderNumber");

            List<PrintOrder> printOrders = (List<PrintOrder>) query.execute(manufacturerOrderNumber);

            if (printOrders != null && printOrders.size() > 0)
            {

                for (PrintOrder printOrder : printOrders) {
                    printOrder.setStatus(status);
                }

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
    public Boolean updatePrintOrderStatuses(Long cartItemId, PrintOrder.ORDER_STATUS status)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cartItemId == null || status == null)
                throw new NullPointerException("Arguments cannot be null");

            Query query = pm.newQuery(CartItem.class, "cartItemId == :cartItemId");

            List<CartItem> cartItems = (List<CartItem>) query.execute(cartItemId);

            if (cartItems != null && cartItems.size() > 0)
            {
                for (CartItem cartItem : cartItems)
                {
                    for (PrintOrder printOrder : cartItem.getManufacturerOrders())
                    {
                        printOrder.setStatus(status);
                    }
                }

                rval = true;
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
    public List<PrintOrder> getPrintOrdersAwaitingSubmission(IEnums.SEEK_MODE mode, Long limit)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<PrintOrder> rval = new ArrayList<>();

        try
        {
            if (mode == null || limit == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            //Ugh, three param limit, have to do a workaround...
            PrintOrder.ORDER_STATUS searchStatus = PrintOrder.ORDER_STATUS.UPLOADED;
            //PrintOrder.ORDER_SYSTEM ftueSystem = PrintOrder.ORDER_SYSTEM.FTUE;
            //PrintOrder.ORDER_SYSTEM cartSystem = PrintOrder.ORDER_SYSTEM.CART;
            //Date thirtySixHoursAgo = DateUtils.getThirtySixHoursAgo(new Date());

            Query query = pm.newQuery(PrintOrder.class, "status == :searchStatus");

            List<PrintOrder> rawList = new ArrayList<>();

            rawList = (List<PrintOrder>) query.execute(searchStatus);

            for (PrintOrder order : rawList)
            {
                //If it's either a FTUE order, a cart sample order, or a regular cart order that was ordered more than 12 hours ago
                //add it to rval
                if ((order.getOrderSystem().equals(PrintOrder.ORDER_SYSTEM.FTUE) || (order.getOrderSystem().equals(PrintOrder.ORDER_SYSTEM.CART) &&
                        order.getOrderDate().before(DateUtils.getDateXHoursAgo(new Date(), 12))))
                        || order.getOrderSystem().equals(PrintOrder.ORDER_SYSTEM.FTUE_PROTOTYPE) ||
                        (order.getOrderSystem().equals(PrintOrder.ORDER_SYSTEM.CART) &&
                                order.getCartItemReference() != null && order.getCartItemReference().getPrototypeRequested()))
                    rval.add(order);
            }

            if (limit.intValue() < rval.size())
                rval = rval.subList(0, limit.intValue());

            rval = (List<PrintOrder>) pm.detachCopyAll(rval);

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
    public Boolean updatePrintOrderShippingLabel(Long printOrderId, EasyPostPostageLabel shippingLabel)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (printOrderId == null || shippingLabel == null)
                throw new NullPointerException("Arguments cannot be null");

            PrintOrder order = getPrintOrderAttached(printOrderId, pm);

            order.getPrintLabels().add(shippingLabel);

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
    public PrintOrder.ORDER_STATUS getPrintOrderStatus(Long printOrderId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        PrintOrder.ORDER_STATUS rval = null;

        try
        {
            if (printOrderId == null)
                throw new NullPointerException("Arguments cannot be null");

            PrintOrder order = getPrintOrderAttached(printOrderId, pm);

            rval = order.getStatus();

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
    public Boolean addPrintOrderToCartItem(Long cartItemId, PrintOrder printOrder)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (cartItemId == null || printOrder == null)
                throw new NullPointerException("Arguments cannot be null");

            Query query = pm.newQuery(CartItem.class, "cartItemId == :cartItemId");
            query.setRange(0l, 1l);
            CartItem cartItem = (CartItem) query.execute(cartItemId);

            rval = cartItem.getManufacturerOrders().add(printOrder);

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
    public List<PrintOrder> getPrintOrdersBeingManufactured(IEnums.SEEK_MODE mode, Long limit)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<PrintOrder> rval = new ArrayList<>();

        try
        {
            if (mode == null || limit == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            List<PrintOrder.ORDER_STATUS> manufacturerPrintOrderStatuses = new ArrayList<>();
            manufacturerPrintOrderStatuses.add(PrintOrder.ORDER_STATUS.SUBMITTED);
            manufacturerPrintOrderStatuses.add(PrintOrder.ORDER_STATUS.PROCESSING);
            manufacturerPrintOrderStatuses.add(PrintOrder.ORDER_STATUS.IN_PRODUCTION);

            Date date = new Date();

            Query query = pm.newQuery(PrintOrder.class, ":printOrderStatuses.contains(this.status) && " +
                    "this.nextStatusUpdateRequest < :currentDate");
            query.setRange(0l, limit);

            rval = (List<PrintOrder>) query.execute(manufacturerPrintOrderStatuses, date);

            rval = (List<PrintOrder>) pm.detachCopyAll(rval);
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
    public Boolean updatePrintOrderManufacturerOrderNumber(Long printOrderPrimaryKeyIdentifier, String orderNumber)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (printOrderPrimaryKeyIdentifier == null || orderNumber == null || orderNumber.length() == 0)
                throw new NullPointerException("Arguments cannot be null or empty");

            PrintOrder order = getPrintOrderAttached(printOrderPrimaryKeyIdentifier, pm);

            order.setManufacturerOrderId(orderNumber);

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
    public Boolean updateLastStatusRequestDate(Long printOrderPrimaryKeyIdentifier, Date requestDate, Date nextRequestDate)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (printOrderPrimaryKeyIdentifier == null || requestDate == null || nextRequestDate == null)
                throw new NullPointerException("Arguments cannot be null");

            PrintOrder printOrder = getPrintOrderAttached(printOrderPrimaryKeyIdentifier, pm);

            printOrder.setLastStatusUpdateRequest(requestDate);
            printOrder.setNextStatusUpdateRequest(nextRequestDate);

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
    public Boolean updatePostageLabelStatus(String trackingNumber, EasyPostPostageLabel.TRACKING_STATUS status)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (trackingNumber == null || trackingNumber.length() == 0 || status == null)
                throw new NullPointerException("Arguments cannot be null");

            Query query = pm.newQuery(EasyPostPostageLabel.class, "trackingNumber == :trackingNumber");
            List<EasyPostPostageLabel> queryList = (List<EasyPostPostageLabel>) query.execute(trackingNumber);

            if (queryList != null)
            {
                for (EasyPostPostageLabel label : queryList) {
                    label.setStatus(status);
                    label.setLastUpdated(new Date());
                }

                rval = true;
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
    public String getEmailByTrackingNumber(String trackingNumber)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        String rval = null;

        try
        {
            if (trackingNumber == null || trackingNumber.length() == 0)
                throw new NullPointerException("Argument cannot be null or empty");

            Query query = pm.newQuery(CartItem.class, "manufacturerOrders.contains(printOrder) && " +
                    "printOrder.printLabel.trackingNumber == :trackingNumber");

            query.declareVariables("import com.troveup.brooklyn.orm.order.model.PrintOrder printOrder");

            query.setResult("this.cart.checkoutEmail");

            List<String> queryList = (List<String>) query.execute(trackingNumber);

            if (queryList != null && queryList.size() > 0)
                rval = queryList.get(0);
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
    public Boolean updateCartItemStatusByTrackingNumber(String trackingNumber, CartItem.CART_ITEM_STATUS status)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (trackingNumber == null || trackingNumber.length() == 0 || status == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            List<CartItem> items = getCartItemsByTrackingNumberAttached(trackingNumber, pm);

            if (items != null && items.size() > 0)
            {
                for (CartItem item : items)
                {
                    item.setCartItemStatus(status);
                    item.setLastStatusChangeDate(new Date());
                }

                rval = true;
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
    public List<CartItem> getCartItemsByTrackingNumber(String trackingNumber, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<CartItem> rval = null;

        try
        {
            if (trackingNumber == null || trackingNumber.length() == 0 || mode == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            configureFetchGroups(pm, mode);

            rval = getCartItemsByTrackingNumberAttached(trackingNumber, pm);

            rval = (List<CartItem>) pm.detachCopyAll(rval);
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
    public Boolean updateOrderSubmitAttemptCount(Long printOrderId, Integer count)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (printOrderId == null || count == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            PrintOrder printOrder = getPrintOrderAttached(printOrderId, pm);

            printOrder.setOrderSubmitAttemptCount(count);

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

    public List<CartItem> getCartItemsByTrackingNumberAttached(String trackingNumber, PersistenceManager pm)
    {
        Query query = pm.newQuery(CartItem.class, "label.trackingNumber == :trackingNumber && " +
                "printOrder.printLabels.contains(label) && this.manufacturerOrders.contains(printOrder)");

        query.declareVariables("com.troveup.brooklyn.orm.order.model.EasyPostPostageLabel label; " +
                "com.troveup.brooklyn.orm.order.model.PrintOrder printOrder");

        return (List<CartItem>) query.execute(trackingNumber);
    }

    @Override
    public Boolean updatePrintOrderManufacturerStatus(Long printOrderId, String status)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (printOrderId == null || status == null)
                throw new NullPointerException("Arguments cannot be null");

            PrintOrder order = getPrintOrderAttached(printOrderId, pm);

            order.setManufacturerStatus(status);

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

    public void configureFetchGroups(PersistenceManager pm, IEnums.SEEK_MODE mode)
    {
        if (mode != IEnums.SEEK_MODE.QUICK)
        {
            if (mode == IEnums.SEEK_MODE.FULL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(PrintOrder.class, "fullFetch");

                //Add the user fetch group fields
                List<String> fetchGroupFields = PrintOrder.getPrintOrderFullFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(FtuePersistedRecord.class, "ftuePersistedFetch");
                fetchGroupFields = FtuePersistedRecord.getPersistedRecordAddressFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(FtueRequest.class, "ftueRequestFetch");
                fetchGroupFields = FtueRequest.getFtueRequestShippingFetchGroupField();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(FtueCheckout.class, "ftueFetch");
                fetchGroupFields = FtueCheckout.getFtueCheckoutFullFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Address.class, "addressFetch");
                fetchGroupFields = Address.getAddressCartFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Order.class, "orderFetch");
                fetchGroupFields = Order.getOrdersMediumFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(CartItem.class, "cartItemFetch");
                fetchGroupFields = CartItem.getCartItemQuickFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("fullFetch");
                fetchPlan.addGroup("ftueFetch");
                fetchPlan.addGroup("ftuePersistedFetch");
                fetchPlan.addGroup("ftueRequestFetch");
                fetchPlan.addGroup("addressFetch");
                fetchPlan.addGroup("orderFetch");
                fetchPlan.addGroup("cartItemFetch");

                fetchPlan.setMaxFetchDepth(3);
            }
            else if (mode == IEnums.SEEK_MODE.CART_ITEM_STATUS)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(CartItem.class, "cartItemStatusFetch");
                List<String> fetchGroupFields = CartItem.getCartItemStatusFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("cartItemStatusFetch");
                fetchPlan.setMaxFetchDepth(3);
            }
        }
    }
}
