package com.troveup.brooklyn.model;

import com.troveup.brooklyn.orm.materials.model.Finish;
import com.troveup.brooklyn.orm.materials.model.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 3/14/16.
 */
public class CustomizerMaterial
{
    private Long primaryKey;
    private String forgeMaterialMapping;
    private String longLabel;
    private String shortLabel;

    public CustomizerMaterial()
    {

    }

    public CustomizerMaterial(Finish finish)
    {
        this.primaryKey = finish.getFinishPrimaryKey();
        this.forgeMaterialMapping = finish.getUnityMaterialMapping();
        this.longLabel = finish.getName();
        this.shortLabel = finish.getShortLabel();
    }

    public Long getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Long primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getForgeMaterialMapping() {
        return forgeMaterialMapping;
    }

    public void setForgeMaterialMapping(String forgeMaterialMapping) {
        this.forgeMaterialMapping = forgeMaterialMapping;
    }

    public String getLongLabel() {
        return longLabel;
    }

    public void setLongLabel(String longLabel) {
        this.longLabel = longLabel;
    }

    public String getShortLabel() {
        return shortLabel;
    }

    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    public static List<CustomizerMaterial> toCustomizerMaterials(List<Material> materials)
    {
        List<CustomizerMaterial> rval = new ArrayList<>();

        for (Material material : materials)
        {
            for (Finish finish : material.getFinishList()) {
                CustomizerMaterial convertedMaterial = new CustomizerMaterial(finish);
                rval.add(convertedMaterial);
            }
        }

        return rval;
    }
}
