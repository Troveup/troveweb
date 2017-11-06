package com.troveup.brooklyn.orm.materials.interfaces;

import com.troveup.brooklyn.orm.materials.model.Material;

import java.util.List;

/**
 * Created by tim on 6/15/15.
 */
public interface IMaterialFilterAccessor
{
    Boolean persistMaterials(List<Material> materials);
    Boolean persistMaterial(Material material);
    List<Material> getAllMaterials();
    List<Material> getAllActiveMaterials();

}
