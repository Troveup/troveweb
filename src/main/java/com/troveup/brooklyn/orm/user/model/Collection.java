package com.troveup.brooklyn.orm.user.model;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 5/7/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class Collection implements Comparable<Collection>, Comparator<Collection>
{
    private long collectionId;
    private String collectionName;
    private String collectionDescription;
    private List<GroupedItem> collectionItems;
    private List<CollectionImage> collectionImages;
    private boolean active;
    private Boolean isPrivate;

    private User owner;
    private List<Follow> followed;
    private Long overallRemakeCount;
    private Long overallTroveCount;
    private Long followedCount;

    private Boolean isOwner;
    private Boolean isFollowed;

    private Date createdDate;

    public Collection()
    {
        createdDate = new Date();
    }

    public Collection(Long collectionId)
    {
        this.collectionId = collectionId;
    }

    public Collection(String collectionName, String collectionDescription, Boolean isPrivate)
    {
        this.collectionName = collectionName;
        this.collectionDescription = collectionDescription;
        this.isPrivate = isPrivate;
    }

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    public long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(long collectionId) {
        this.collectionId = collectionId;
    }

    @Persistent
    @Column(name = "name", allowsNull = "false")
    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    @Persistent
    @Column(name = "description", allowsNull = "false")
    public String getCollectionDescription() {
        return collectionDescription;
    }

    public void setCollectionDescription(String collectionDescription) {
        this.collectionDescription = collectionDescription;
    }

    @Persistent
    public List<GroupedItem> getCollectionItems() {
        return collectionItems;
    }

    public void setCollectionItems(List<GroupedItem> collectionItems) {
        this.collectionItems = collectionItems;
    }

    @Persistent
    @Column(name = "active", allowsNull = "false")
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Persistent
    public List<CollectionImage> getCollectionImages() {
        return collectionImages;
    }

    public void setCollectionImages(List<CollectionImage> collectionImages) {
        this.collectionImages = collectionImages;
    }

    @Persistent
    @Column(name = "collection_owner")
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Persistent
    public List<Follow> getFollowed() {
        return followed;
    }

    public void setFollowed(List<Follow> followed) {
        this.followed = followed;
    }

    @Persistent
    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    @Persistent
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Persistent
    public Long getOverallRemakeCount() {
        return overallRemakeCount;
    }

    public void setOverallRemakeCount(Long overallRemakeCount) {
        this.overallRemakeCount = overallRemakeCount;
    }

    @Persistent
    public Long getOverallTroveCount() {
        return overallTroveCount;
    }

    public void setOverallTroveCount(Long overallTroveCount) {
        this.overallTroveCount = overallTroveCount;
    }

    @NotPersistent
    public Boolean getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(Boolean isOwner) {
        this.isOwner = isOwner;
    }

    @NotPersistent
    public Boolean getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(Boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

    @NotPersistent
    public Long getFollowedCount() {
        return followedCount;
    }

    public void setFollowedCount(Long followedCount) {
        this.followedCount = followedCount;
    }

    public static List<String> getFullCollectionFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("collectionItems");
        rval.add("collectionImages");
        rval.add("owner");
        rval.add("followed");

        return rval;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Collection)
            return ((Collection) obj).collectionId == this.collectionId;
        else
            return false;
    }

    @Override
    public int compareTo(Collection o) {
        return Long.compare(o.getCollectionId(), this.collectionId);
    }

    @Override
    public int compare(Collection o1, Collection o2) {
        return Long.compare(o1.getCollectionId(), o2.getCollectionId());
    }
}
