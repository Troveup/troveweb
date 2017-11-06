package com.troveup.brooklyn.orm.simpleitem.datanucleus;

import com.troveup.brooklyn.orm.common.datanucleus.ObjectAccessor;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.order.interfaces.IOrderAccessor;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.simpleitem.interfaces.ISimpleItemAccessor;
import com.troveup.brooklyn.orm.simpleitem.model.*;
import com.troveup.brooklyn.orm.user.interfaces.IUserAccessor;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.util.MoneyUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jdo.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 3/25/16.
 */
public class SimpleItemAccessor extends ObjectAccessor implements ISimpleItemAccessor
{
    @Autowired
    IOrderAccessor orderAccessor;

    @Autowired
    IUserAccessor userAccessor;

    public SimpleItemAccessor(PersistenceManagerFactory pmfProxy) {
        super(pmfProxy);
    }

    @Override
    public SimpleItem getSimpleItemByItemId(Long itemId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        SimpleItem rval = null;

        try {

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(SimpleItem.class, "simpleItemId == :itemId");
            List<SimpleItem> queryResults = (List<SimpleItem>) query.execute(itemId);

            if (queryResults != null && queryResults.size() > 0) {
                rval = queryResults.get(0);
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
    public SimpleItem persistSimpleItem(SimpleItem item)
    {
        PersistenceManager pm = pmf.getPersistenceManager();

        try
        {
            if (item == null)
                throw new NullPointerException("Arguments cannot be null");

            if (item.getParentItem() != null)
            {
                item.setParentItem(getManagedSimpleItem(item.getParentItem().getSimpleItemId(), pm));
            }

            if (item.getAssociatedOrder() != null && item.getAssociatedOrder().getOrderId() != null)
            {
                item.setAssociatedOrder(orderAccessor.getOrderAttached(item.getAssociatedOrder().getOrderId(), pm));
            }

            pm.setDetachAllOnCommit(true);
            pm.makePersistent(item);

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return item;
    }

    @Override
    public SimpleItem getManagedSimpleItem(Long itemId, PersistenceManager pm)
    {
        Query query = pm.newQuery(SimpleItem.class, "simpleItemId == :itemId");
        List<SimpleItem> rval = (List<SimpleItem>) query.execute(itemId);

        return rval.get(0);
    }

    private SimpleItemControl getPersistentItemControl(Long customizationId, PersistenceManager pm)
    {
        SimpleItemControl rval = null;

        Query query = pm.newQuery(SimpleItemControl.class, "controlId == :id");
        List<SimpleItemControl> queryResult = (List<SimpleItemControl>) query.execute(customizationId);

        if (queryResult != null && queryResult.size() > 0)
            rval = queryResult.get(0);

        return rval;
    }

    private List<SimpleItemControl> getPersistentSimpleItemControlList(List<SimpleItemControl> customizations, PersistenceManager pm)
    {
        List<SimpleItemControl> rval = new ArrayList<>();

        if (customizations != null && customizations.size() > 0)
        {
            for (SimpleItemControl customization : customizations)
            {
                if (customization.getControlId() != null)
                {
                    SimpleItemControl persistentCustomization = getPersistentItemControl(customization.getControlId(), pm);

                    if (persistentCustomization != null)
                    {
                        rval.add(persistentCustomization);
                    }
                    else
                    {
                        rval.add(customization);
                    }
                }
                else
                {
                    rval.add(customization);
                }
            }
        }

        return rval;
    }

    @Override
    public String getControlTitleById(Long controlId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        String rval = null;

        try
        {
            if (controlId == null)
                throw new NullPointerException("Argument cannot be null");

            Query query = pm.newQuery(SimpleItemControl.class, "controlId == :controlId");
            List<SimpleItemControl> queryResults = (List<SimpleItemControl>) query.execute(controlId);

            if (queryResults != null && queryResults.size() > 0)
            {
                rval = queryResults.get(0).getControlName();
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
    public String getControlOptionLabelById(Long controlOptionId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        String rval = null;

        try
        {
            if (controlOptionId == null)
                throw new NullPointerException("Argument cannot be null");

            Query query = pm.newQuery(ControlOption.class, "controlOptionId == :controlOptionId");
            List<ControlOption> queryResults = (List<ControlOption>) query.execute(controlOptionId);

            if (queryResults != null && queryResults.size() > 0)
            {
                rval = queryResults.get(0).getOptionDisplayName();
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
    public List<SimpleItem> getReferenceOrderedSimpleItemsWithOrderNumbersByInfluencerId(Long influencerId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<SimpleItem> rval = new ArrayList<>();

        try
        {
            if (influencerId == null)
                throw new NullPointerException("Argument cannot be null");

            Query query = pm.newQuery(SimpleItem.class, "influencerUserAccountId == :influencerId && controlReference == true && active == true");
            List<SimpleItem> queryResult = (List<SimpleItem>) query.execute(influencerId);

            rval = (List<SimpleItem>) pm.detachCopyAll(queryResult);

            for (SimpleItem item : rval)
            {
                query = pm.newQuery(SimpleItem.class, "parentItem.simpleItemId == :itemId && controlReference == false");
                query.setResult("count(this)");

                Long count = (Long) query.execute(item.getSimpleItemId());
                item.setNumberSold(count.intValue());
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
    public List<SimpleItem> getReferenceSimpleItemsByInfluencerId(Long influencerId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<SimpleItem> rval = null;

        try
        {
            configureFetchGroups(pm, mode);

            if (influencerId == null)
                throw new NullPointerException("Argument cannot be null.");

            Query query = pm.newQuery(SimpleItem.class, "influencerUserAccountId == :influencerUserAccountId && active == true && controlReference == true");
            rval = (List<SimpleItem>) query.execute(influencerId);

            rval = (List<SimpleItem>) pm.detachCopyAll(rval);

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
    public List<SimpleItem> getLatestSimpleItemOrders(Integer collectionPage, Long collectionPageSize, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<SimpleItem> rval = null;

        try
        {
            if (collectionPage == null || collectionPageSize == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);
            Query query = pm.newQuery(SimpleItem.class, "active == true && controlReference == false");
            query.setOrdering("this.associatedOrder.orderDate DESC");
            query = constrainQueryByPage(collectionPage, collectionPageSize, query);

            rval = (List<SimpleItem>) query.execute();
            rval = (List<SimpleItem>) pm.detachCopyAll(rval);

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
    public Boolean updateSimpleItemManufacturerCompletionDate(Long itemId, Date date)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;


        try
        {
            if (itemId == null || date == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            SimpleItem item = getManagedSimpleItem(itemId, pm);

            item.setEstimatedManufacturerCompletionDate(date);

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
    public Boolean updateSimpleItemManufacturerStatus(Long itemId, SimpleItem.MANUFACTURING_STATUS status)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (itemId == null || status == null)
                throw new NullPointerException("Arguments cannot be null or empty.");

            SimpleItem item = getManagedSimpleItem(itemId, pm);

            item.setStatus(status);

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
    public Boolean updateSimpleItemManufacturerPrice(Long itemId, Float price)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (itemId == null || price == null)
                throw new NullPointerException("Arguments cannot be null or empty.");

            SimpleItem item = getManagedSimpleItem(itemId, pm);

            item.setManufacturingPrice(MoneyUtil.floatToBigDecimal(price, null));

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
    public Boolean addSimpleItemNote(Long itemId, Long userIdCreator, String noteText)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (itemId == null || userIdCreator == null || noteText == null)
                throw new NullPointerException("Arguments cannot be null.");

            OrderNote note = new OrderNote();
            User user = userAccessor.getUserAttached(userIdCreator, pm);

            note.setUserCreator(user);
            note.setCreationDate(new Date());
            note.setNote(noteText);

            SimpleItem item = getManagedSimpleItem(itemId, pm);

            if (item.getOrderNotes() == null)
                item.setOrderNotes(new ArrayList<OrderNote>());

            item.getOrderNotes().add(note);

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
    public List<OrderNote> getAllActiveNotesBySimpleItemId(Long itemId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<OrderNote> rval = null;

        try
        {
            if (itemId == null || mode == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(OrderNote.class, "item.simpleItemId == :itemId && item.orderNotes.contains(this) && this.active == true");
            query.declareVariables("com.troveup.brooklyn.orm.simpleitem.model.SimpleItem item");

            rval = (List<OrderNote>) query.execute(itemId);

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
    public List<SimpleItem> getOrderedSimpleItemsByInfluencerId(Long influencerId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<SimpleItem> rval = null;

        try
        {
            if (influencerId == null || mode == null)
                throw new NullPointerException("Arguments cannot be null.");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(SimpleItem.class, "influencerUserAccountId == :influencerId && active == true && controlReference == false");
            rval = (List<SimpleItem>) query.execute(influencerId);

            rval = (List<SimpleItem>) pm.detachCopyAll(rval);

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
    public List<SimpleItem> getPaginatedReadyToWearSimpleItemReferences(Integer collectionPage, Long collectionPageSize, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<SimpleItem> rval = null;

        try
        {
            if (collectionPage == null || collectionPageSize == null || mode == null)
                throw new NullPointerException("Arguments cannot be null.");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(SimpleItem.class, "active == true && influencerUserAccountId == null");
            query = constrainQueryByPage(collectionPage, collectionPageSize, query);

            rval = (List<SimpleItem>) query.execute();

            if (rval != null && rval.size() > 0)
            {
                rval = (List<SimpleItem>) pm.detachCopyAll(rval);
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
    public List<SimpleItem> getListPersistenceManagedSimpleItems(List<SimpleItem> items, PersistenceManager pm)
    {
        List<SimpleItem> rval = new ArrayList<>();

        List<Long> itemIds = new ArrayList<>();
        for (SimpleItem item : items)
        {
            itemIds.add(item.getSimpleItemId());
        }

        Query query = pm.newQuery(SimpleItem.class, ":items.contains(simpleItemId)");
        rval = (List<SimpleItem>) query.execute(itemIds);

        return rval;
    }

    public void configureFetchGroups(PersistenceManager pm, IEnums.SEEK_MODE mode)
    {
        if (mode != null && mode != IEnums.SEEK_MODE.QUICK) {
            if (mode == IEnums.SEEK_MODE.FULL) {
                FetchGroup fetchGroup = pm.getFetchGroup(SimpleItem.class, "simpleItemFetch");
                List<String> fetchGroupFields = SimpleItem.getAllSimpleItemFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(SimpleItemControl.class, "itemCustomizationFetch");
                fetchGroupFields = SimpleItemControl.getAllFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(ControlOption.class, "itemCustomizationOptionFetch");
                fetchGroupFields = ControlOption.getAllFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(ManufacturerSpecifier.class, "manufacturerSpecifier");
                fetchGroupFields = ManufacturerSpecifier.getAllManufacturerSpecifierFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(OrderNote.class, "orderNoteFetch");
                fetchGroupFields = OrderNote.getFullFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("simpleItemFetch");
                fetchPlan.addGroup("itemCustomizationFetch");
                fetchPlan.addGroup("itemCustomizationOptionFetch");
                fetchPlan.addGroup("manufacturerSpecifier");
                fetchPlan.addGroup("orderNoteFetch");
                fetchPlan.setMaxFetchDepth(10);
            }
            else if (mode == IEnums.SEEK_MODE.ASSETS)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(SimpleItem.class, "simpleItemFetch");
                List<String> fetchGroupFields = SimpleItem.getSimpleItemAssetFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("simpleItemFetch");
                fetchPlan.setMaxFetchDepth(10);
            }
            else if (mode == IEnums.SEEK_MODE.ORDERNOTE_USER)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(OrderNote.class, "orderNoteFetch");
                List<String> fetchGroupFields = OrderNote.getFullFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("orderNoteFetch");
                fetchPlan.setMaxFetchDepth(10);
            }
            else if (mode == IEnums.SEEK_MODE.SIMPLEITEM_DASHBOARD_ITEM_ORDER)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(SimpleItem.class, "simpleItemFetch");
                List<String> fetchGroupFields = SimpleItem.getSimpleItemWithOrderAndParentItemFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Order.class, "orderFetch");
                fetchGroupFields = Order.getOrderInfluencerDashboardFetchGroups();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("simpleItemFetch");
                fetchPlan.addGroup("orderFetch");
                fetchPlan.setMaxFetchDepth(10);
            }
        }
    }
}
