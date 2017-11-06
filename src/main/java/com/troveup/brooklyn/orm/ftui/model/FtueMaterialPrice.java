package com.troveup.brooklyn.orm.ftui.model;

import javax.jdo.annotations.*;
import java.util.List;

/**
 * Created by tim on 7/13/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class FtueMaterialPrice
{
    private Long ftueMaterialPriceId;
    private String materialId;
    private String finishId;
    private String materialName;
    private String finishName;
    private String price;

    public FtueMaterialPrice()
    {

    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getFtueMaterialPriceId() {
        return ftueMaterialPriceId;
    }

    public void setFtueMaterialPriceId(Long ftueMaterialPriceId) {
        this.ftueMaterialPriceId = ftueMaterialPriceId;
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
    @Column(name = "finish_id", allowsNull = "true")
    public String getFinishId() {
        return finishId;
    }

    public void setFinishId(String finishId) {
        this.finishId = finishId;
    }

    @Persistent
    @Column(name = "material_name", allowsNull = "true")
    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    @Persistent
    @Column(name = "finish_name", allowsNull = "true")
    public String getFinishName() {
        return finishName;
    }

    public void setFinishName(String finishName) {
        this.finishName = finishName;
    }

    @Persistent
    @Column(name = "price", allowsNull = "true")
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof FtueMaterialPrice)
        {
            //If the materials and finishes exists, run a comparison between them
            if (((FtueMaterialPrice) obj).getMaterialId() != null && ((FtueMaterialPrice) obj).getFinishId() != null)
            {
                return ((((FtueMaterialPrice) obj).getMaterialId().equals(this.getMaterialId()) &&
                        ((FtueMaterialPrice) obj).getFinishId().equals(this.getFinishId())));
            }
            //Otherwise use the ids
            else
                return ((FtueMaterialPrice) obj).getFtueMaterialPriceId().equals(this.getFtueMaterialPriceId());

        }
        return false;
    }

    public static FtueMaterialPrice findPrice(String materialId, String finishId, List<FtueMaterialPrice> materialPriceList)
    {
        FtueMaterialPrice rval = null;

        FtueMaterialPrice searchPrice = new FtueMaterialPrice();
        searchPrice.setMaterialId(materialId);
        searchPrice.setFinishId(finishId);

        int searchIndex = materialPriceList.indexOf(searchPrice);

        if (searchIndex > -1)
            rval = materialPriceList.get(searchIndex);

        return rval;
    }
}
