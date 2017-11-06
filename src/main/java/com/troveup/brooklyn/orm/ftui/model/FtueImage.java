package com.troveup.brooklyn.orm.ftui.model;

import javax.jdo.annotations.*;

/**
 * Created by tim on 7/13/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class FtueImage
{
    private Long ftueImageId;
    private String material;
    private String ftueImageUrl;

    public FtueImage()
    {

    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getFtueImageId() {
        return ftueImageId;
    }

    public void setFtueImageId(Long ftueImageId) {
        this.ftueImageId = ftueImageId;
    }

    @Persistent
    @Column(name = "material", allowsNull = "true")
    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    @Persistent
    @Column(name = "ftue_url", allowsNull = "true")
    public String getFtueImageUrl() {
        return ftueImageUrl;
    }

    public void setFtueImageUrl(String ftueImageUrl) {
        this.ftueImageUrl = ftueImageUrl;
    }
}
