package com.troveup.brooklyn.orm.simpleitem.model;

import javax.jdo.annotations.*;

/**
 * Created by tim on 3/25/16.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class ControlOptionAsset
{
    private Long controlOptionAssetId;
    private String controlOptionAssetUrl;
    private Integer camera;

    public ControlOptionAsset()
    {
        camera = 0;
    }

    public ControlOptionAsset(ControlOptionAsset asset)
    {
        this.controlOptionAssetUrl = asset.getControlOptionAssetUrl();
        this.camera = asset.getCamera();
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getControlOptionAssetId() {
        return controlOptionAssetId;
    }

    public void setControlOptionAssetId(Long controlOptionAssetId) {
        this.controlOptionAssetId = controlOptionAssetId;
    }

    @Persistent
    public String getControlOptionAssetUrl() {
        return controlOptionAssetUrl;
    }

    public void setControlOptionAssetUrl(String controlOptionAssetUrl) {
        this.controlOptionAssetUrl = controlOptionAssetUrl;
    }

    @Persistent
    public Integer getCamera() {
        return camera;
    }

    public void setCamera(Integer camera) {
        this.camera = camera;
    }
}
