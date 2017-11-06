package com.troveup.brooklyn.sdk.meshexporter.java.model;

/**
 * Created by tim on 6/16/15.
 */
public class ModelJsonMetaData
{
    private Float diameter;
    private Integer formatVersion;
    private String generatedBy;
    private String category;

    public ModelJsonMetaData()
    {

    }

    public Float getDiameter() {
        return diameter;
    }
    public void setDiameter(Float diameter) {
        this.diameter = diameter;
    }

    public Integer getFormatVersion() {
        return formatVersion;
    }
    public void setFormatVersion(Integer formatVersion) {
        this.formatVersion = formatVersion;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }
    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}
