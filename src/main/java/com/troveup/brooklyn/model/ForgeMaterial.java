package com.troveup.brooklyn.model;

import com.troveup.brooklyn.orm.materials.model.Finish;
import com.troveup.brooklyn.orm.materials.model.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 3/16/16.
 */
public class ForgeMaterial
{
    private String id;
    private String forgeMaterialMapping;
    private String label;

    public ForgeMaterial()
    {

    }

    public ForgeMaterial(String finishId, String unityMaterialMapping, String materialName)
    {
        this.id = finishId;
        this.forgeMaterialMapping = unityMaterialMapping;
        this.label = materialName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getForgeMaterialMapping() {
        return forgeMaterialMapping;
    }

    public void setForgeMaterialMapping(String forgeMaterialMapping) {
        this.forgeMaterialMapping = forgeMaterialMapping;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static List<ForgeMaterial> toForgeMaterial(List<Material> materials) {
        List<ForgeMaterial> rval = new ArrayList<>();

        for (Material material : materials) {

            for (Finish finish : material.getFinishList()) {
                rval.add(new ForgeMaterial(finish.getFinishId(),
                        finish.getUnityMaterialMapping(), finish.getName()));
            }

        }

        return rval;
    }
}
