package com.troveup.brooklyn.tests.orm;

import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.feed.interfaces.IFeedAccessor;
import com.troveup.brooklyn.orm.item.interfaces.IItemAccessor;
import com.troveup.config.PersistenceConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by tim on 8/9/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceConfig.class)
public class FeedAccessorTest
{
    @Autowired
    IItemAccessor itemAccessor;

    @Autowired
    IFeedAccessor feedAccessor;

    @Test
    public void testFillFeed()
    {
        Assert.assertTrue(feedAccessor.fillFeed());
    }

    @Test
    public void testCalculateFeedScores()
    {
        Assert.assertTrue(feedAccessor.refreshFeedScores());
    }

    @Test
    public void testGetFeedItems()
    {
        Assert.assertTrue(itemAccessor.getPagedFeedItems(2l, 0, 20l, 0l, IEnums.SEEK_MODE.ITEM_FEED).size() > 0);
    }

    @Test
    public void testAddItemsToFeedQueue()
    {
        Assert.assertTrue(feedAccessor.addItemToFeedQueue(1l));
        Assert.assertTrue(feedAccessor.addItemToFeedQueue(2l));
    }

    @Test
    public void testMarkItemsReadyForAdmission()
    {
        Assert.assertTrue(feedAccessor.setQueuedItemReadyForAdmission(1l));
        Assert.assertTrue(feedAccessor.setQueuedItemReadyForAdmission(2l));
    }

    @Test
    public void testHandleQueuedItems()
    {
        Assert.assertTrue(feedAccessor.handleQueuedItems());
    }
}
