package com.troveup.brooklyn.sdk.print.imaterialise.model;

import java.util.List;

/**
 * Created by tim on 5/26/15.
 */
public class iMatMaterial
{
    private String materialID;
    private String name;
    private String defaultFinishID;
    private String productionTimeMinBusinessDays;
    private String productionTimeMaxBusinessDays;

    List<iMatFinish> finishes;

    public iMatMaterial()
    {

    }

    public iMatMaterial(String materialID)
    {
        this.materialID = materialID;
    }

    public String getMaterialID() {
        return materialID;
    }

    public void setMaterialID(String materialID) {
        this.materialID = materialID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultFinishID() {
        return defaultFinishID;
    }

    public void setDefaultFinishID(String defaultFinishID) {
        this.defaultFinishID = defaultFinishID;
    }

    public String getProductionTimeMinBusinessDays() {
        return productionTimeMinBusinessDays;
    }

    public void setProductionTimeMinBusinessDays(String productionTimeMinBusinessDays) {
        this.productionTimeMinBusinessDays = productionTimeMinBusinessDays;
    }

    public String getProductionTimeMaxBusinessDays() {
        return productionTimeMaxBusinessDays;
    }

    public void setProductionTimeMaxBusinessDays(String productionTimeMaxBusinessDays) {
        this.productionTimeMaxBusinessDays = productionTimeMaxBusinessDays;
    }

    public List<iMatFinish> getFinishes() {
        return finishes;
    }

    public void setFinishes(List<iMatFinish> finishes) {
        this.finishes = finishes;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof iMatMaterial)
            return ((iMatMaterial) obj).materialID.equals(materialID);
        else
            return false;
    }
}
