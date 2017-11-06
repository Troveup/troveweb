package com.troveup.brooklyn.orm.clicktracker;

import com.troveup.brooklyn.orm.common.datanucleus.ObjectAccessor;
import org.datanucleus.api.jdo.JDOPersistenceManagerProxy;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

/**
 * Created by tim on 7/7/15.
 */
public class ClickTracker extends ObjectAccessor
{
    public ClickTracker(PersistenceManagerFactory pmfProxy)
    {
        super(pmfProxy);
    }

    public Boolean persistClick(ClickTrackerModel model)
    {
        Boolean rval = false;
        PersistenceManager pm = pmf.getPersistenceManager();

        try
        {
            if (model == null)
                throw new NullPointerException("Argument cannot be null");

            pm.makePersistent(model);

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
}
