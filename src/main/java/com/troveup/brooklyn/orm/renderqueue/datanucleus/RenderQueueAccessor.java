package com.troveup.brooklyn.orm.renderqueue.datanucleus;

import com.troveup.brooklyn.orm.common.datanucleus.ObjectAccessor;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.ftui.impl.FtueAccessor;
import com.troveup.brooklyn.orm.ftui.model.FtuePersistedRecord;
import com.troveup.brooklyn.orm.item.interfaces.IItemAccessor;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.renderqueue.interfaces.IRenderQueueAccessor;
import com.troveup.brooklyn.orm.renderqueue.model.Render;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jdo.*;
import java.util.*;

/**
 * Created by tim on 6/24/15.
 */
public class RenderQueueAccessor extends ObjectAccessor implements IRenderQueueAccessor
{
    @Autowired
    IItemAccessor itemAccessor;

    @Autowired
    FtueAccessor ftueAccessor;

    public RenderQueueAccessor(PersistenceManagerFactory pmfProxy)
    {
        super(pmfProxy);
    }

    @Override
    public Boolean submitRender(Render render)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (render == null)
                throw new NullPointerException("Arguments cannot be null");

            if (render.getRenderType().equals(Render.RENDER_TYPE.FTUE))
            {
                FtuePersistedRecord ftueRecord = ftueAccessor.
                        getPersistedRecordAttached(render.getFtueReference().getFtuePersistedRecordId(), pm);

                render.setFtueReference(ftueRecord);
            }
            else
            {
                Item item = itemAccessor.getItemAttached(render.getItemReference().getItemId(), pm);
                render.setItemReference(item);
            }

            rval = pm.makePersistent(render) != null;

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
    public Boolean submitRenders(List<Render> renders)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (renders == null)
                throw new NullPointerException("Arguments cannot be null");

            //Assuming it's a bulk render submission, so they should all be associated with the same item
            if (renders.get(0).getRenderType().equals(Render.RENDER_TYPE.FTUE))
            {
                FtuePersistedRecord ftueRecord = ftueAccessor.
                        getPersistedRecordAttached(renders.get(0).getFtueReference().getFtuePersistedRecordId(), pm);

                for (Render render : renders)
                    render.setFtueReference(ftueRecord);
            }
            else
            {
                Item item = itemAccessor.getItemAttached(renders.get(0).getItemReference().getItemId(), pm);

                for (Render render : renders)
                    render.setItemReference(item);
            }

            rval = pm.makePersistentAll(renders) != null;

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
    public Render getRender(Long renderId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Render rval = null;

        try
        {
            if (renderId == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(mode, pm);

            Query query = pm.newQuery(Render.class, "renderQueueId == :renderId");
            query.setResultClass(Render.class);

            List<Render> queryResult = (List<Render>) query.execute(renderId);

            if (queryResult != null && queryResult.size() > 0)
                rval = pm.detachCopy(queryResult.get(0));

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
    public Render getRender(String jobId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Render rval = null;

        try
        {
            if (jobId == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(mode, pm);

            Query query = pm.newQuery(Render.class, "jobId.equals(:jobId)");
            query.setResultClass(Render.class);

            List<Render> queryResult = (List<Render>) query.execute(jobId);

            if (queryResult != null && queryResult.size() > 0)
                rval = pm.detachCopy(queryResult.get(0));

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

    public Render getRenderAttached(Long renderId, PersistenceManager pm)
    {
        Render rval = null;

        if (renderId == null)
            throw new NullPointerException("Arguments cannot be null");

        Query query = pm.newQuery(Render.class, "renderQueueId == :renderId");
        query.setResultClass(Render.class);

        List<Render> queryResult = (List<Render>) query.execute(renderId);

        if (queryResult != null && queryResult.size() > 0)
            rval = queryResult.get(0);

        return rval;
    }

    public List<Render> getRendersAttached(List<Long> renderIds, PersistenceManager pm)
    {
        if (renderIds == null)
            throw new NullPointerException("Arguments cannot be null");

        Query query = pm.newQuery(Render.class, ":renderIds.contains(ftuePersistedRecordId)");
        query.setResultClass(Render.class);

        return (List<Render>) query.execute(renderIds);
    }

    public Render getRenderAttached(String jobId, PersistenceManager pm)
    {
        Render rval = null;

        if (jobId == null)
            throw new NullPointerException("Arguments cannot be null");

        Query query = pm.newQuery(Render.class, "jobId == :jobId");
        query.setResultClass(Render.class);

        List<Render> queryResult = (List<Render>) query.execute(jobId);

        if (queryResult != null && queryResult.size() > 0)
            rval = queryResult.get(0);

        return rval;
    }

    @Override
    public List<Render> getRenderByItem(Long itemId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Render> rval = null;

        try
        {
            if (itemId == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(mode, pm);

            Item item = itemAccessor.getItemAttached(itemId, pm);

            Query query = pm.newQuery(Render.class, "itemReference.equals(:item)");
            query.setResultClass(Render.class);

            rval = (List<Render>) query.execute(item);

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
    public Boolean updateRenderStatus(String jobId, RENDER_STATUS status, String errorMessage, int errorId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (jobId == null || status == null)
                throw new NullPointerException("Arguments cannot be null");

            Render render = getRenderAttached(jobId, pm);

            if (render != null)
            {
                render.setRenderStatus(status);

                if (status == RENDER_STATUS.RUNNING)
                {
                    render.setDateSubmitted(new Date());
                }
                if (status == RENDER_STATUS.QUEUED)
                {
                    render.setDateSubmitted(null);
                }

                if (errorMessage != null) {
                    render.setErrorString(errorMessage);
                    render.setErrorId(errorId);
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
    public List<Render> getRendersByStatus(RENDER_STATUS status)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Render> rval = null;

        try
        {
            if (status == null)
                throw new NullPointerException("Argument cannot be null");

            Query query = pm.newQuery(Render.class, "renderStatus == :status");
            rval = (List<Render>) query.execute(status);

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
    public List<Render> getQueuedRenders()
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Render> rval = null;

        try
        {
            RENDER_STATUS compareStatus = RENDER_STATUS.QUEUED;

            Query query = pm.newQuery(Render.class, "renderStatus == :compareStatus");
            query.setResultClass(Render.class);

            rval = (List<Render>) query.execute(compareStatus);

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
    public Render getRunningRender()
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Render rval = null;

        try
        {
            RENDER_STATUS compareStatus = RENDER_STATUS.RUNNING;

            configureFetchGroups(IEnums.SEEK_MODE.RENDER_FULL, pm);

            Query query = pm.newQuery(Render.class, "renderStatus == :compareStatus");
            query.setResultClass(Render.class);

            List<Render> queryList = (List<Render>) query.execute(compareStatus);

            if (queryList != null && queryList.size() > 0)
                rval = pm.detachCopy(queryList.get(0));

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
    public List<Render> getFtueBasedRenders(List<Long> ftuePersistedRecordIdentifiers, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Render> rval = new ArrayList<>();

        try
        {
            if (ftuePersistedRecordIdentifiers == null || ftuePersistedRecordIdentifiers.size() == 0)
                throw new NullPointerException("Arguments cannot be null or empty");

            configureFetchGroups(mode, pm);

            rval = getRendersAttached(ftuePersistedRecordIdentifiers, pm);

            rval = (List<Render>) pm.detachCopyAll(rval);

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
     * Checks the status of a Ftue image render.  If it contains an entry stating that the render isn't complete,
     * it is indicated in the return map with a false for the boolean.
     * @param ftueIdentifiers List of FtuePersistedRecord identifiers to check.
     * @return A hashmap of the persisted record identifiers that indicate with a boolean whether or not that particular
     * render has completed.
     */
    @Override
    public Map<Long, Boolean> checkFtueRenderStatuses(List<Long> ftueIdentifiers)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Map<Long, Boolean> rval = new HashMap<>();

        try
        {
            if (ftueIdentifiers == null || ftueIdentifiers.size() == 0)
                throw new NullPointerException("Argument cannot be null or empty");

            //Grab all renders that match an item from the ftue identifiers list and are not of the COMPLETE status
            Query query = pm.newQuery(Render.class,
                    ":ftueIdentifiers.contains(ftueReference.ftuePersistedRecordId) && renderStatus != :completeStatus");

            //Pass the list and the complete status param
            List<Render> queryList = (List<Render>) query.execute(ftueIdentifiers, RENDER_STATUS.COMPLETE);

            //If there is anything in this list, make it more searchable by getting all of the identifiers
            if (queryList != null && queryList.size() > 0)
            {
                List<Long> rendersStillBeingExecuted = new ArrayList<>();
                for (Render render : queryList)
                {
                    rendersStillBeingExecuted.add(render.getFtueReference().getFtuePersistedRecordId());
                }

                //Indicate its completeness in the return object, true for complete, false for not
                for (Long identifier : ftueIdentifiers)
                {
                    if (rendersStillBeingExecuted.contains(identifier))
                        rval.put(identifier, false);
                    else
                        rval.put(identifier, true);
                }
            }
            //Got an empty or null list back, they are all complete, mark the list done
            else
            {
                for (Long identifier : ftueIdentifiers)
                    rval.put(identifier, true);
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
    public Boolean incrementRenderRetryCount(String renderJobId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (renderJobId == null || renderJobId.length() == 0)
                throw new NullPointerException("Arguments cannot be null or empty");

            Render render = getRenderAttached(renderJobId, pm);

            Integer incrementedRetry = 0;

            if (render.getRetryNumber() != null)
                incrementedRetry = render.getRetryNumber() + 1;

            render.setRetryNumber(incrementedRetry);

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


    private void configureFetchGroups(IEnums.SEEK_MODE mode, PersistenceManager pm)
    {
        if (mode != IEnums.SEEK_MODE.QUICK)
        {
            if (mode == IEnums.SEEK_MODE.RENDER_FULL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(Render.class, "fullFetch");

                //Add the user fetch group fields
                List<String> fetchGroupFields = Render.getFullRenderFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("fullFetch");
                fetchPlan.setMaxFetchDepth(2);
            }
        }
    }
}
