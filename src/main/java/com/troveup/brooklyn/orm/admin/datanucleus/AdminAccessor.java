package com.troveup.brooklyn.orm.admin.datanucleus;

import com.troveup.brooklyn.orm.admin.interfaces.IAdminAccessor;
import com.troveup.brooklyn.orm.admin.model.AlertBannerState;
import com.troveup.brooklyn.orm.common.datanucleus.ObjectAccessor;
import com.troveup.brooklyn.sdk.cache.interfaces.ICacheService;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import java.util.List;

/**
 * Created by tim on 12/9/15.
 */
public class AdminAccessor extends ObjectAccessor implements IAdminAccessor
{
    public AdminAccessor(PersistenceManagerFactory pmfProxy) {
        super(pmfProxy);
    }

    @Override
    public Boolean persistAlertBannerState(AlertBannerState alertBannerState)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (alertBannerState == null)
                throw new NullPointerException("Argument cannot be null");

            cacheItem(ICacheService.ALERT_BANNER_CACHE_KEY, gson.toJson(alertBannerState), 12);

            rval = pm.makePersistent(alertBannerState) != null;

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
    public AlertBannerState getLatestAlertBannerState()
    {
        PersistenceManager pm = null;
        AlertBannerState rval = null;

        try
        {
            String cacheString = cacheService.getCacheValue(ICacheService.ALERT_BANNER_CACHE_KEY);

            if (cacheString != null && cacheString.length() > 0)
            {
                rval = gson.fromJson(cacheString, AlertBannerState.class);
            }
            //Possible cache miss, check the DB and repopulate the cache if there is something
            else {
                pm = pmf.getPersistenceManager();
                Query query = pm.newQuery(AlertBannerState.class, "1 == 1");
                query.setRange(0l, 1l);
                query.setOrdering("createdDate DESC");

                List<AlertBannerState> queryResults = (List<AlertBannerState>) query.execute();

                if (queryResults != null && queryResults.size() > 0)
                {
                    rval = queryResults.get(0);
                    cacheItem(ICacheService.ALERT_BANNER_CACHE_KEY, gson.toJson(rval), 12);
                }
            }
        } catch (Exception e)
        {
            logError(e);
        }
        finally {
            if (pm != null) {
                pm.close();
            }
        }

        return rval;
    }
}
