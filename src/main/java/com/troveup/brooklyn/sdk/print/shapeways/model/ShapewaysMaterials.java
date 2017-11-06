package com.troveup.brooklyn.sdk.print.shapeways.model;

import java.util.List;

/**
 * Created by tim on 7/1/15.
 */
public class ShapewaysMaterials
{
    private String result;
    private List<ShapewaysMaterial> materials;

    public ShapewaysMaterials()
    {

    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<ShapewaysMaterial> getMaterials() {
        return materials;
    }

    public void setMaterials(List<ShapewaysMaterial> materials) {
        this.materials = materials;
    }
}
