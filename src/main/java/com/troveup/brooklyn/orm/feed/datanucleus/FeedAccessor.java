package com.troveup.brooklyn.orm.feed.datanucleus;

import com.troveup.brooklyn.orm.common.datanucleus.ObjectAccessor;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.feed.interfaces.IFeedAccessor;
import com.troveup.brooklyn.orm.feed.model.FeedItem;
import com.troveup.brooklyn.orm.item.interfaces.IItemAccessor;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import java.util.*;

/**
 * Created by tim on 8/8/15.
 */
public class FeedAccessor extends ObjectAccessor implements IFeedAccessor
{

    protected final Long MAX_FEED_LENGTH = 1000l;

    @Autowired
    IItemAccessor itemAccessor;

    public FeedAccessor(PersistenceManagerFactory pmfProxy)
    {
        super(pmfProxy);
    }

    @Override
    public Boolean fillFeed()
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Long feedSize = getFeedItemCount();
        Boolean rval = false;

        try {
            //If the feed has any room, go get queued items and include them in the feed
            if (feedSize < MAX_FEED_LENGTH) {

                List<FeedItem> queuedItems = getQueuedFeedItems(MAX_FEED_LENGTH - feedSize, pm);
                for (FeedItem item : queuedItems)
                {
                    item.setQueued(false);
                    item.setAdmittedDate(new Date());
                }

                //If there is still room for more items, grab some from the global item list
                if (feedSize + queuedItems.size() < MAX_FEED_LENGTH)
                {
                    List<Item> feedEligibleItems = getFeedEligibleItems(MAX_FEED_LENGTH - feedSize - queuedItems.size(),
                            pm);

                    if (feedEligibleItems != null && feedEligibleItems.size() > 0) {
                        List<FeedItem> listToBeAdded = new ArrayList<>();
                        for (Item item : feedEligibleItems) {
                            FeedItem itemToAdd =
                                    new FeedItem(item, item.getItemOwner().getUserId(), item.getItemId(),
                                            item.getCreated());
                            listToBeAdded.add(itemToAdd);
                        }

                        pm.makePersistentAll(listToBeAdded);

                    }
                }
                rval = getFeedItemCount().equals(MAX_FEED_LENGTH);
            }
            else
            {
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
    public List<FeedItem> getCurrentFeedItems(IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<FeedItem> rval = null;

        try
        {
            configureFetchGroups(pm, mode);
            Query query = pm.newQuery(FeedItem.class, "queued == false && active == true");

            rval = (List<FeedItem>) query.execute();
            rval = (List<FeedItem>) pm.detachCopyAll(rval);
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

    private List<FeedItem> getCurrentFeedItemsAttached(PersistenceManager pm)
    {
        Query query = pm.newQuery(FeedItem.class, "queued == false && active == true");

        return (List<FeedItem>) query.execute();
    }

    @Override
    public Boolean refreshFeedScores()
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        List<FeedItem> feedItems = getCurrentFeedItemsAttached(pm);

        if (feedItems != null && feedItems.size() > 0)
        {
            for (FeedItem item : feedItems)
            {
                item.setFeedScore(calculateFeedScore(item));
            }

            rval = true;
        }

        return rval;
    }

    @Override
    public Long getFeedItemCount()
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Long rval = -1l;

        try
        {
            Query query = pm.newQuery(FeedItem.class, "queued == false && active == true");
            query.setResult("count(this)");

            rval = (Long) query.execute();

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
    public Boolean setQueuedItemReadyForAdmission(Long itemId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (itemId == null)
                throw new NullPointerException("Argument cannot be null");

            Query query = pm.newQuery(FeedItem.class, "itemReference.itemId == :itemId");

            List<FeedItem> queryList = (List<FeedItem>) query.execute(itemId);

            if (queryList != null && queryList.size() > 0)
            {
                queryList.get(0).setReadyForAdmission(true);
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
    public Boolean handleQueuedItems()
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        //Grab all queued items
        List<FeedItem> queuedItems = getQueuedFeedItems(-1l, pm);

        //If there is are items in the queue, process them
        if (queuedItems != null && queuedItems.size() > 0)
        {
            //Grab all feed items
            Map<Long, Integer> originalListItemLocationMap = new HashMap<>();
            List<FeedItem> feedItems = getCurrentFeedItemsAttached(pm);

            for (int i = 0; i < feedItems.size(); ++i)
            {
                originalListItemLocationMap.put(feedItems.get(i).getItemId(), i);
            }

            List<FeedItem> detachedFeedItems = (List<FeedItem>) pm.detachCopyAll(feedItems);

            //Check all of the queued items to see if their score beats out any other items in the list of feed items
            for (FeedItem item : queuedItems)
            {
                //Sort the feed items so that we can tell if this item belongs in the feed
                Collections.sort(detachedFeedItems, new Comparator<FeedItem>()
                {
                    @Override
                    public int compare(FeedItem o1, FeedItem o2)
                    {
                        return o1.getFeedScore().compareTo(o2.getFeedScore());
                    }
                });

                Float itemFeedScore = calculateFeedScore(item);

                //If the item belongs in the feed, eject the least item from the feed, and add this one
                if (itemFeedScore > feedItems.get(feedItems.size() - 1).getFeedScore()) {
                    feedItems.get(originalListItemLocationMap.get(feedItems.get(feedItems.size() - 1).getItemId())).setActive(false);
                    item.setQueued(false);
                    item.setAdmittedDate(new Date());
                }
                //Otherwise, remove the item from the queue to be lost forever in the void
                else
                {
                    item.setActive(false);
                }
            }

            rval = true;
        }

        return rval;
    }

    @Override
    public Boolean checkFeedItemExistence(Long itemId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = true;

        try
        {
            if (itemId == null)
                throw new NullPointerException("Argument cannot be null");

            Query query = pm.newQuery(FeedItem.class, "this.itemReference.itemId == :argumentItemId");
            query.setResult("count(this)");

            Long result = (Long) query.execute(itemId);

            if (result > 0l)
                rval = true;
            else
                rval = false;

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
    public Boolean addItemToFeedQueue(Long itemId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (itemId == null)
                throw new NullPointerException("Argument cannot be null");

            if (!checkFeedItemExistence(itemId)) {
                Item attachedItem = itemAccessor.getItemAttached(itemId, pm);

                FeedItem feedItem = new FeedItem(0f, attachedItem, attachedItem.getItemOwner().getUserId(), itemId,
                        true, false, attachedItem.getCreated());


                rval = pm.makePersistent(feedItem) != null;
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

    private List<Item> getFeedEligibleItems(Long itemCount, PersistenceManager pm)
    {
        //First, grab a listing of all feed item identifiers
        Query feedItemQuery = pm.newQuery(FeedItem.class, "1 == 1");
        feedItemQuery.setResult("this.itemReference.itemId");

        List<Long> feedItemReferenceIdentifiers = (List<Long>) feedItemQuery.execute();

        //Grab a limited set of items that are not in the current list of feed items
        Query query = pm.newQuery(Item.class, "!:feedItemReferenceIdentifiers.contains(this.itemId) && " +
                "this.images.size() > 1 && this.images.get(0).customizerImage == false && this.defaultCardImageUrl != null && this.itemReference.originFtue == false && this.itemReference.itemOwner.pseudoUser != true");
        query.setOrdering("created DESC, trovedCount DESC, remadeCount DESC");
        query.setRange(0l, itemCount);

        return (List<Item>) query.execute(feedItemReferenceIdentifiers);
    }

    private List<FeedItem> getQueuedFeedItems(Long itemCount, PersistenceManager pm)
    {
        Query query = pm.newQuery(FeedItem.class, "queued == true && readyForAdmission == true && active == true && this.itemReference.originFtue == false && this.itemReference.itemOwner.pseudoUser != true");
        query.setOrdering("feedItemCreationDate ASC");

        if (itemCount > -1)
            query.setRange(0l, itemCount);

        return (List<FeedItem>) query.execute();
    }

    private Float calculateFeedScore(FeedItem item)
    {
        Float minutesElapsed = 0f;
        Float daysElapsed = 0f;

        //Not sure what to do with this yet
        Float weekElapsed = 0f;

        Float recencyWeight = 0.5f;
        Float frequencyTrovedWeight = 0.2f;
        Float frequencyRemadeWeight = 0.2f;

        Float feedScore;

        Long minutesDelta = DateUtils.getMinutesDelta(item.getReferenceItemCreationDate(), new Date());

        if (24 * 60 - minutesDelta > 0)
        {
            minutesElapsed = (float) 24 * 60 - minutesDelta;
        }

        if ((600 - minutesDelta)/(24*60)*100 > 0)
        {
            daysElapsed = (float) (600 - minutesDelta)/(24*60)*100;
        }

        feedScore = (minutesElapsed + daysElapsed + weekElapsed) * recencyWeight +
                item.getItemReference().getTrovedCount() * frequencyTrovedWeight +
                item.getItemReference().getRemadeCount() * frequencyRemadeWeight;

        //Bump new items to the top for two minutes
        if (item.getAdmittedDate() != null && DateUtils.getMinutesDelta(item.getAdmittedDate(), new Date()) <= 2)
        {
            feedScore += 5000f;
        }

        return feedScore;
    }

    private void configureFetchGroups(PersistenceManager pm, IEnums.SEEK_MODE mode)
    {

    }
}
