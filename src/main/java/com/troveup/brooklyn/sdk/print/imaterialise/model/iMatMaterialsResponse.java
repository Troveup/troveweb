package com.troveup.brooklyn.sdk.print.imaterialise.model;

import java.util.List;

/**
 * Created by tim on 5/26/15.
 */
public class iMatMaterialsResponse
{
    List<iMatMaterial> materials;

    public iMatMaterialsResponse()
    {

    }

    public List<iMatMaterial> getMaterials() {
        return materials;
    }

    public void setMaterials(List<iMatMaterial> materials) {
        this.materials = materials;
    }
}
