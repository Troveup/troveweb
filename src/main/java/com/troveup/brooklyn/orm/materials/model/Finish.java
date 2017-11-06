package com.troveup.brooklyn.orm.materials.model;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 6/15/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
@Unique(name="FINUNIQUE", members={"finishId"})
public class Finish
{
    private Long finishPrimaryKey;
    private String finishId;
    private String name;
    private String supplier;
    private String realityServerMapping;
    private String unityMaterialMapping;
    private List<FinishPriceMap> priceMapping;
    private String shortLabel;
    private boolean active;

    public Finish()
    {

    }

    public Finish(String finishId, String name, String supplier, String realityServerMapping,
                  String unityMaterialMapping, List<FinishPriceMap> priceMapping, String shortLabel, boolean active)
    {
        this.finishId = finishId;
        this.name = name;
        this.supplier = supplier;
        this.realityServerMapping = realityServerMapping;
        this.priceMapping = priceMapping;
        this.unityMaterialMapping = unityMaterialMapping;
        this.shortLabel = shortLabel;
        this.active = active;
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getFinishPrimaryKey() {
        return finishPrimaryKey;
    }

    public void setFinishPrimaryKey(Long finishPrimaryKey) {
        this.finishPrimaryKey = finishPrimaryKey;
    }

    @Persistent
    @Column(name = "finish_id", allowsNull = "true")
    public String getFinishId() {
        return finishId;
    }

    public void setFinishId(String finishId) {
        this.finishId = finishId;
    }

    @Persistent
    @Column(name = "finish_name", allowsNull = "true")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    @Column
    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    @Persistent
    @Column
    public String getRealityServerMapping() {
        return realityServerMapping;
    }

    public void setRealityServerMapping(String realityServerMapping) {
        this.realityServerMapping = realityServerMapping;
    }

    @Persistent
    public String getUnityMaterialMapping() {
        return unityMaterialMapping;
    }

    public void setUnityMaterialMapping(String unityMaterialMapping) {
        this.unityMaterialMapping = unityMaterialMapping;
    }

    @Persistent
    public String getShortLabel() {
        return shortLabel;
    }

    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    public static List<String> fullFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("finishPrimaryKey");
        rval.add("finishId");
        rval.add("name");
        rval.add("active");
        rval.add("realityServerMapping");
        rval.add("priceMapping");

        return rval;
    }

    @Persistent
    public List<FinishPriceMap> getPriceMapping() {
        return priceMapping;
    }

    public void setPriceMapping(List<FinishPriceMap> priceMapping) {
        this.priceMapping = priceMapping;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Finish)
            return ((Finish) obj).getFinishId().equals(this.getFinishId());
        else
            return false;
    }

    public static String getFinishNameByFinishId(String finishId, List<Finish> finishes)
    {
        String rval = null;

        Finish searchFinish = new Finish();
        searchFinish.setFinishId(finishId);
        int finishIndex = finishes.indexOf(searchFinish);

        if (finishIndex > -1)
            rval = finishes.get(finishIndex).getName();

        return rval;
    }
 
    public static Finish getFinishByPrimaryKey(String primaryKey, List<Finish> finishes)
    {
        Finish rval = null;

        for (int i = 0; i < finishes.size(); i++) {
            String testKey = finishes.get(i).getFinishPrimaryKey().toString();
            if (testKey.equals(primaryKey)) {
                rval = finishes.get(i);
            }
        }
        return rval;
    }

    public static Finish getFinishByFinishId(String finishId, List<Finish> finishes)
    {
        Finish rval = null;

        Finish searchFinish = new Finish();
        searchFinish.setFinishId(finishId);
        int finishIndex = finishes.indexOf(searchFinish);

        if (finishIndex > -1)
            rval = finishes.get(finishIndex);

        return rval;
    }

    public static List<Finish> getListOfFinishesFromMaterials(List<Material> materials)
    {
        List<Finish> rval = new ArrayList<>();

        for (Material material : materials)
        {
            rval.addAll(material.getFinishList());
        }

        return rval;
    }
}
