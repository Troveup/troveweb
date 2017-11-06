package com.troveup.brooklyn.orm.ftui.model;

import javax.jdo.annotations.*;

/**
 * Created by tim on 7/1/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class FtueModelWeights
{
    private Long weightPrimaryKey;
    private String weightId;
    private String weightValue;
    private String visibleMesh;

    public FtueModelWeights(String weightId, String weightValue)
    {
        this.weightId = weightId;
        this.weightValue = weightValue;
    }

    public FtueModelWeights(String weightId, Float weightValue)
    {
        this.weightId = weightId;
        this.weightValue = weightValue.toString();
    }

    public FtueModelWeights(String visibleMesh)
    {
        this.visibleMesh = visibleMesh;
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getWeightPrimaryKey() {
        return weightPrimaryKey;
    }

    public void setWeightPrimaryKey(Long weightPrimaryKey) {
        this.weightPrimaryKey = weightPrimaryKey;
    }

    @Persistent
    @Column(name = "weight_identifier")
    public String getWeightId() {
        return weightId;
    }

    public void setWeightId(String weightId) {
        this.weightId = weightId;
    }

    @Persistent
    @Column(name = "weight_value")
    public String getWeightValue() {
        return weightValue;
    }

    @Persistent
    public String getVisibleMesh() {
        return visibleMesh;
    }

    public void setVisibleMesh(String visibleMesh) {
        this.visibleMesh = visibleMesh;
    }

    public void setWeightValue(String weightValue) {
        this.weightValue = weightValue;
    }
}
