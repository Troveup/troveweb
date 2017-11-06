package com.troveup.brooklyn.orm.storefront.datanucleus;

import com.troveup.brooklyn.orm.common.datanucleus.ObjectAccessor;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.simpleitem.interfaces.ISimpleItemAccessor;
import com.troveup.brooklyn.orm.simpleitem.model.SimpleItem;
import com.troveup.brooklyn.orm.storefront.interfaces.IStoreFrontAccessor;
import com.troveup.brooklyn.orm.storefront.model.StoreFront;
import com.troveup.brooklyn.orm.user.interfaces.IUserAccessor;
import com.troveup.brooklyn.orm.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jdo.*;
import javax.mail.Store;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tim on 6/10/16.
 */
public class StoreFrontAccessor extends ObjectAccessor implements IStoreFrontAccessor
{
    @Autowired
    ISimpleItemAccessor simpleItemAccessor;

    @Autowired
    IUserAccessor userAccessor;

    public StoreFrontAccessor(PersistenceManagerFactory pmfProxy) {
        super(pmfProxy);
    }

    @Override
    public Boolean doesSellerUrlShortpathExist(String urlShortPath)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = true;

        try
        {
            if (urlShortPath == null || urlShortPath.length() == 0)
                throw new NullPointerException("Arguments cannot be null or empty");

            Query query = pm.newQuery(StoreFront.class, "storeShortUrl == :urlShortPath");
            query.setResult("count(this)");

            rval = (Long) query.execute(urlShortPath) > 0;

        } catch (Exception e)
        {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public StoreFront persistStoreFront(Long userId, StoreFront storeFront)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        StoreFront rval = null;

        try
        {
            if (userId == null || storeFront == null)
                throw new NullPointerException("Arguments cannot be null");

            User user = userAccessor.getUserAttached(storeFront.getStoreOwner().getUserId(), pm);
            List<SimpleItem> persistenceManagedItems = simpleItemAccessor.getListPersistenceManagedSimpleItems(storeFront.getStoreFrontItems(), pm);
            Map<Long, SimpleItem> itemMap = new HashMap<>();

            for (SimpleItem item : storeFront.getStoreFrontItems())
            {
                itemMap.put(item.getSimpleItemId(), item);
            }

            List<SimpleItem> storeFrontItems = new ArrayList<>();
            for(SimpleItem item : persistenceManagedItems)
            {
                SimpleItem newItem = new SimpleItem(item);
                newItem.setItemPrice(itemMap.get(item.getSimpleItemId()).getItemPrice());
                newItem.setInfluencerUserAccountId(userId);
                newItem.setUserCreatorName(user.getFirstName());
                newItem.setStorefrontShortUrl(itemMap.get(item.getSimpleItemId()).getStorefrontShortUrl());
                storeFrontItems.add(newItem);
            }

            storeFront.setStoreFrontItems(storeFrontItems);
            storeFront.setStoreOwner(user);

            pm.setDetachAllOnCommit(true);
            rval = pm.makePersistent(storeFront);
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
    public StoreFront getStoreFront(String url, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        StoreFront rval = null;

        try
        {
            if (url == null || mode == null)
                throw new NullPointerException("Arguments cannot be null.");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(StoreFront.class, "storeShortUrl == :url");
            List<StoreFront> queryResults = (List<StoreFront>) query.execute(url);

            if (queryResults != null && queryResults.size() > 0)
            {
                rval = queryResults.get(0);
                rval = pm.detachCopy(rval);
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

    private void configureFetchGroups(PersistenceManager pm, IEnums.SEEK_MODE mode) {
        //DataNucleus will only pull surface level data unless explicitly instructed to do so.  If we need to pull
        //the whole item, we need to add extra fetch groups for the collections.
        //Reference:  http://www.datanucleus.org/products/datanucleus/jdo/fetchgroup.html
        if (mode != null && mode != IEnums.SEEK_MODE.QUICK)
        {
            //Very expensive, only use when you need every single detail about the user's profile
            if (mode == IEnums.SEEK_MODE.STORE_FRONT_VIEW_DATA)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(StoreFront.class, "fullFetch");

                //Add the user fetch group fields
                List<String> fetchGroupFields = StoreFront.getAllStoreFrontFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("fullFetch");
                fetchPlan.setMaxFetchDepth(3);
            }
        }
    }
}
