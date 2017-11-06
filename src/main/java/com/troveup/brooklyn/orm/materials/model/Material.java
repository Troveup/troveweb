package com.troveup.brooklyn.orm.materials.model;

import com.troveup.brooklyn.sdk.print.imaterialise.model.iMatFinish;
import com.troveup.brooklyn.sdk.print.imaterialise.model.iMatMaterial;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tim on 6/15/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
@Unique(name="MATUNIQUE", members={"materialId"})
public class Material
{
    private Long materialPrimaryKey;
    private String materialId;
    private String name;
    private String defaultFinish;
    private String supplier;
    private String realityServerMapping;
    private boolean active;

    private List<Finish> finishList;

    public Material()
    {

    }

    public Material(String materialId, String name, List<Finish> finishList, String defaultFinish, String supplier, String realityServerMapping, boolean active)
    {
        this.materialId = materialId;
        this.name = name;
        this.defaultFinish = defaultFinish;
        this.supplier = supplier;
        this.realityServerMapping = realityServerMapping;
        this.active = active;
        this.finishList = finishList;
    }

    public Material(Material material)
    {
        this.materialPrimaryKey = material.getMaterialPrimaryKey();
        this.materialId = material.getMaterialId();
        this.name = material.getName();
        this.defaultFinish = material.getDefaultFinish();
        this.realityServerMapping = material.getRealityServerMapping();
        this.active = material.isActive();

        this.finishList = new ArrayList<>();
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getMaterialPrimaryKey() {
        return materialPrimaryKey;
    }

    public void setMaterialPrimaryKey(Long materialPrimaryKey) {
        this.materialPrimaryKey = materialPrimaryKey;
    }

    @Persistent
    @Column(name = "material_id", allowsNull = "true")
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    @Persistent
    @Column(name = "material_name", allowsNull = "true")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Persistent
    @Column(name = "default_finish", allowsNull = "true")
    public String getDefaultFinish() {
        return defaultFinish;
    }

    public void setDefaultFinish(String defaultFinish) {
        this.defaultFinish = defaultFinish;
    }

    @Persistent
    @Column(name = "material_finish", allowsNull = "true")
    public List<Finish> getFinishList() {
        return finishList;
    }

    public void setFinishList(List<Finish> finishList) {
        this.finishList = finishList;
    }

    @Persistent
    @Column(name = "active", allowsNull = "true")
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Persistent
    @Column(name = "supplier", allowsNull = "true")
    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    @Persistent
    @Column(name = "reality_material_mapping", allowsNull = "true")
    public String getRealityServerMapping() {
        return realityServerMapping;
    }

    public void setRealityServerMapping(String realityServerMapping) {
        this.realityServerMapping = realityServerMapping;
    }

    public static Material fromiMatMaterial(iMatMaterial imatmaterial)
    {
        Material material = new Material();
        material.setName(imatmaterial.getName());
        material.setDefaultFinish(imatmaterial.getDefaultFinishID());
        material.setMaterialId(imatmaterial.getMaterialID());
        material.setFinishList(new ArrayList<Finish>());
        material.setSupplier("iMaterialise");

        for (iMatFinish imatfinish : imatmaterial.getFinishes())
        {
            Finish finish = new Finish();
            finish.setName(imatfinish.getName());
            finish.setFinishId(imatfinish.getFinishID());
            finish.setSupplier("iMaterialise");

            material.getFinishList().add(finish);
        }

        return material;
    }

    public static Material fromiMatMaterial(iMatMaterial imatmaterial, List<String> filter,
                                            Map<String, String> realityMaterialMap, Map<String, String> realityFinishMap)
    {
        Material material = new Material();
        material.setName(imatmaterial.getName());
        material.setDefaultFinish(imatmaterial.getDefaultFinishID());
        material.setMaterialId(imatmaterial.getMaterialID());
        material.setFinishList(new ArrayList<Finish>());
        material.setSupplier("iMaterialise");

        if (realityMaterialMap != null && realityMaterialMap.containsKey(imatmaterial.getName()))
            material.setRealityServerMapping(realityMaterialMap.get(imatmaterial.getName()));

        if (filter != null)
            material.setActive(true);

        for (iMatFinish imatfinish : imatmaterial.getFinishes())
        {
            Finish finish = new Finish();
            finish.setName(imatfinish.getName());
            finish.setFinishId(imatfinish.getFinishID());
            finish.setSupplier("iMaterialise");

            if (realityFinishMap != null && realityFinishMap.containsKey(finish.getName()))
                finish.setRealityServerMapping(realityFinishMap.get(imatfinish.getName()));

            if (filter != null && (filter.contains(imatfinish.getName()) || filter.contains("ALL")))
                finish.setActive(true);

            material.getFinishList().add(finish);
        }

        return material;
    }

    public static List<String> fullMaterialFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("materialPrimaryKey");
        rval.add("materialId");
        rval.add("name");
        rval.add("defaultFinish");
        rval.add("active");
        rval.add("finishList");
        rval.add("realityServerMapping");

        return rval;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Material)
            return ((Material) obj).getMaterialId().equals(this.getMaterialId());
        else
            return false;
    }

    public static String getMaterialNameByMaterialId(String materialId, List<Material> materials)
    {
        String rval = null;

        Material searchMaterial = new Material();
        searchMaterial.setMaterialId(materialId);
        int materialIndex = materials.indexOf(searchMaterial);

        if (materialIndex > -1)
            rval = materials.get(materialIndex).getName();

        return rval;
    }

    public static Material getMaterialByMaterialId(String materialId, List<Material> materials)
    {
        Material rval = null;

        Material searchMaterial = new Material();
        searchMaterial.setMaterialId(materialId);
        int materialIndex = materials.indexOf(searchMaterial);

        if (materialIndex > -1)
            rval = materials.get(materialIndex);

        return rval;
    }

    public static Material getMaterialByName(String materialName, List<Material> materials)
    {
        Material rval = null;

        if (materialName != null && materials != null)
        {
            for (Material material : materials)
            {
                if (materialName.toUpperCase().equals(material.getName().toUpperCase()))
                {
                    rval = material;
                }
            }
        }

        return rval;
    }
}
