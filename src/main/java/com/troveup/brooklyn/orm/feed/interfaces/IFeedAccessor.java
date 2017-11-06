package com.troveup.brooklyn.orm.feed.interfaces;

import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.feed.model.FeedItem;
import java.util.List;

/**
 * Created by tim on 8/8/15.
 */
public interface IFeedAccessor
{
    Boolean fillFeed();
    List<FeedItem> getCurrentFeedItems(IEnums.SEEK_MODE mode);
    Boolean refreshFeedScores();
    Long getFeedItemCount();

    Boolean setQueuedItemReadyForAdmission(Long itemId);

    Boolean handleQueuedItems();

    Boolean addItemToFeedQueue(Long itemId);

    Boolean checkFeedItemExistence(Long itemId);
}
