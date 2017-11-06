package com.troveup.brooklyn.orm.feed.model;

import com.troveup.brooklyn.orm.item.model.Item;

import javax.jdo.annotations.*;
import java.util.Date;

/**
 * Created by tim on 8/8/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class FeedItem
{
    private Long feedItemId;
    private Float feedScore;
    private Item itemReference;
    private Long itemOwnerId;
    private Long itemId;
    private Date feedItemCreationDate;
    private Date referenceItemCreationDate;
    private Date admittedDate;
    private Boolean queued;
    private Boolean readyForAdmission;
    private Boolean active;

    public FeedItem()
    {

    }

    public FeedItem(Float feedScore, Item itemReference, Long itemOwnerId, Long itemId, Boolean queued,
                    Boolean readyForAdmission, Date referenceItemCreationDate)
    {
        this.feedScore = feedScore;
        this.itemReference = itemReference;
        this.itemOwnerId = itemOwnerId;
        this.itemId = itemId;
        this.queued = queued;
        this.readyForAdmission = readyForAdmission;
        this.feedItemCreationDate = new Date();
        this.referenceItemCreationDate = referenceItemCreationDate;
        this.active = true;
    }

    public FeedItem(Item itemReference, Long itemOwnerId, Long itemId, Date referenceItemCreationDate)
    {
        this.feedScore = 0f;
        this.itemReference = itemReference;
        this.itemOwnerId = itemOwnerId;
        this.itemId = itemId;
        this.feedItemCreationDate = new Date();
        this.queued = false;
        this.readyForAdmission = false;
        this.admittedDate = new Date();
        this.referenceItemCreationDate = referenceItemCreationDate;
        this.active = true;
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getFeedItemId() {
        return feedItemId;
    }

    public void setFeedItemId(Long feedItemId) {
        this.feedItemId = feedItemId;
    }

    @Persistent
    public Float getFeedScore() {
        return feedScore;
    }

    public void setFeedScore(Float feedScore) {
        this.feedScore = feedScore;
    }

    @Persistent
    public Item getItemReference() {
        return itemReference;
    }

    public void setItemReference(Item itemReference) {
        this.itemReference = itemReference;
    }

    @Persistent
    public Date getFeedItemCreationDate() {
        return feedItemCreationDate;
    }

    public void setFeedItemCreationDate(Date feedItemCreationDate) {
        this.feedItemCreationDate = feedItemCreationDate;
    }

    @Persistent
    public Long getItemOwnerId() {
        return itemOwnerId;
    }

    public void setItemOwnerId(Long itemOwnerId) {
        this.itemOwnerId = itemOwnerId;
    }

    @Persistent
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    @Persistent
    public Boolean getQueued() {
        return queued;
    }

    public void setQueued(Boolean queued) {
        this.queued = queued;
    }

    @Persistent
    public Boolean getReadyForAdmission() {
        return readyForAdmission;
    }

    public void setReadyForAdmission(Boolean readyForAdmission) {
        this.readyForAdmission = readyForAdmission;
    }

    @Persistent
    public Date getReferenceItemCreationDate() {
        return referenceItemCreationDate;
    }

    public void setReferenceItemCreationDate(Date referenceItemCreationDate) {
        this.referenceItemCreationDate = referenceItemCreationDate;
    }

    @Persistent
    public Date getAdmittedDate() {
        return admittedDate;
    }

    public void setAdmittedDate(Date admittedDate) {
        this.admittedDate = admittedDate;
    }

    @Persistent
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
