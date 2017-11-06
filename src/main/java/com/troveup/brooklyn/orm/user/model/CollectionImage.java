package com.troveup.brooklyn.orm.user.model;

import javax.jdo.annotations.*;

/**
 * Created by tim on 7/31/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class CollectionImage
{
    private Long collectionImageId;
    private String collectionImageUrl;

    public CollectionImage()
    {

    }

    public CollectionImage(String collectionImageUrl)
    {
        this.collectionImageUrl = collectionImageUrl;
    }

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    public Long getCollectionImageId() {
        return collectionImageId;
    }

    public void setCollectionImageId(Long collectionImageId) {
        this.collectionImageId = collectionImageId;
    }

    @Persistent
    public String getCollectionImageUrl() {
        return collectionImageUrl;
    }

    public void setCollectionImageUrl(String collectionImageUrl) {
        this.collectionImageUrl = collectionImageUrl;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof CollectionImage)
            return ((CollectionImage) obj).collectionImageId.equals(collectionImageId) ||
                    ((CollectionImage) obj).collectionImageUrl.equals(collectionImageUrl);
        else
            return false;
    }
}
