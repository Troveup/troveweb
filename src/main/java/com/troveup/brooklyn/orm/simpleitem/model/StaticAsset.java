package com.troveup.brooklyn.orm.simpleitem.model;

import javax.jdo.annotations.*;

/**
 * Created by tim on 3/31/16.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class StaticAsset
{
    private Long assetId;
    private String assetUrl;

    public StaticAsset()
    {

    }

    public StaticAsset(String assetUrl)
    {
        this.assetUrl = assetUrl;
    }

    public StaticAsset(StaticAsset asset)
    {
        this.assetUrl = asset.getAssetUrl();
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    @Persistent
    public String getAssetUrl() {
        return assetUrl;
    }

    public void setAssetUrl(String assetUrl) {
        this.assetUrl = assetUrl;
    }
}
