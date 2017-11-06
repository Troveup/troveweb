package com.troveup.brooklyn.orm.urlshortener.model;

import javax.jdo.annotations.*;
import java.util.Date;

/**
 * Created by tim on 2/4/16.
 */
@PersistenceCapable
@Cacheable(value = "false")
@Unique(name="LINKUNIQUE", members={"shortLinkTag"})
public class ShortLink
{
    enum SHORT_LINK_CATEGORY {
        PRODUCT_DESCRIPTION,
        MISC
    }

    private Long shortLinkId;
    private String shortLinkTag;
    private String shortLinkFullUrl;
    private SHORT_LINK_CATEGORY category;
    private Boolean active;
    private Date createdDate;

    public ShortLink()
    {
        this.category = SHORT_LINK_CATEGORY.MISC;
        this.active = true;
        this.createdDate = new Date();
    }

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    public Long getShortLinkId() {
        return shortLinkId;
    }

    public void setShortLinkId(Long shortLinkId) {
        this.shortLinkId = shortLinkId;
    }

    @Persistent
    public String getShortLinkTag() {
        return shortLinkTag;
    }

    public void setShortLinkTag(String shortLinkTag) {
        this.shortLinkTag = shortLinkTag;
    }

    @Persistent
    public String getShortLinkFullUrl() {
        return shortLinkFullUrl;
    }

    public void setShortLinkFullUrl(String shortLinkFullUrl) {
        this.shortLinkFullUrl = shortLinkFullUrl;
    }

    @Persistent
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Persistent
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Persistent
    public SHORT_LINK_CATEGORY getCategory() {
        return category;
    }

    public void setCategory(SHORT_LINK_CATEGORY category) {
        this.category = category;
    }
}
