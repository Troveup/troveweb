package com.troveup.brooklyn.orm.item.model;

import com.troveup.brooklyn.util.MoneyUtil;

import javax.jdo.annotations.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@PersistenceCapable
@Cacheable(value = "false")
public class PriceFilter
{
    public enum APPLICATION_LEVEL {
        GLOBAL,
        CATEGORY,
        ITEM
    }

    public enum MATERIAL_LEVEL {
        ALL,
        MATERIAL
    }

    public enum PRICE_TYPE {
        STATIC,
        FORMULAIC
    }

    private Long priceId;
    private APPLICATION_LEVEL applicationLevel;
    private MATERIAL_LEVEL materialLevel;
    private String category;
    private String materialId;
    private String finishId;
    private PRICE_TYPE priceType;
    private BigDecimal flatPrice;
    private BigDecimal markupPercentage;
    private BigDecimal shippingMarkup;
    private BigDecimal prototypeMarkup;
    private BigDecimal packagingMarkup;
    private Date creationDate;
    private Boolean active;
    private Item itemReference;

    public PriceFilter()
    {
        this.active = true;
        this.creationDate = new Date();
    }

    public PriceFilter(PriceFilter filter)
    {
        this.applicationLevel = filter.applicationLevel;
        this.materialLevel = filter.materialLevel;
        this.category = filter.category;
        this.materialId = filter.materialId;
        this.finishId = filter.finishId;
        this.priceType = filter.priceType;
        this.flatPrice = filter.flatPrice;
        this.markupPercentage = filter.markupPercentage;
        this.shippingMarkup = filter.shippingMarkup;
        this.prototypeMarkup = filter.prototypeMarkup;
        this.packagingMarkup = filter.packagingMarkup;

        this.creationDate = new Date();
        this.active = true;
    }

    public PriceFilter(APPLICATION_LEVEL applicationLevel, MATERIAL_LEVEL materialLevel, String category, String materialId, String finishId, Long itemId)
    {
        this.applicationLevel = applicationLevel;
        this.materialLevel = materialLevel;
        this.category = category;
        this.materialId = materialId;
        this.finishId = finishId;

        if (itemId != null) {
            this.itemReference = new Item();
            this.itemReference.setItemId(itemId);
        }
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getPriceId() {
        return priceId;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }

    @Persistent
    public APPLICATION_LEVEL getApplicationLevel() {
        return applicationLevel;
    }

    public void setApplicationLevel(APPLICATION_LEVEL applicationLevel) {
        this.applicationLevel = applicationLevel;
    }

    @Persistent
    public MATERIAL_LEVEL getMaterialLevel() {
        return materialLevel;
    }

    public void setMaterialLevel(MATERIAL_LEVEL materialLevel) {
        this.materialLevel = materialLevel;
    }

    @Persistent
    public PRICE_TYPE getPriceType() {
        return priceType;
    }

    public void setPriceType(PRICE_TYPE priceType) {
        this.priceType = priceType;
    }

    @Persistent
    @Column(scale = 2)
    public BigDecimal getFlatPrice() {
        return flatPrice;
    }

    public void setFlatPrice(BigDecimal flatPrice) {
        this.flatPrice = flatPrice;
    }

    @Persistent
    @Column(scale = 2)
    public BigDecimal getMarkupPercentage() {
        return markupPercentage;
    }

    public void setMarkupPercentage(BigDecimal markupPercentage) {
        this.markupPercentage = markupPercentage;
    }

    @Persistent
    @Column(scale = 2)
    public BigDecimal getShippingMarkup() {
        return shippingMarkup;
    }

    public void setShippingMarkup(BigDecimal shippingMarkup) {
        this.shippingMarkup = shippingMarkup;
    }

    @Persistent
    @Column(scale = 2)
    public BigDecimal getPrototypeMarkup() {
        return prototypeMarkup;
    }

    public void setPrototypeMarkup(BigDecimal prototypeMarkup) {
        this.prototypeMarkup = prototypeMarkup;
    }

    @Persistent
    @Column(scale = 2)
    public BigDecimal getPackagingMarkup() {
        return packagingMarkup;
    }

    public void setPackagingMarkup(BigDecimal packagingMarkup) {
        this.packagingMarkup = packagingMarkup;
    }

    @Persistent
    public Item getItemReference() {
        return itemReference;
    }

    public void setItemReference(Item itemReference) {
        this.itemReference = itemReference;
    }

    @Persistent
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    @Persistent
    public String getFinishId() {
        return finishId;
    }

    public void setFinishId(String finishId) {
        this.finishId = finishId;
    }

    @Persistent
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Persistent
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Persistent
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void scaleBigDecimals()
    {
        if (this.flatPrice != null)
            this.flatPrice = MoneyUtil.toProperScale(this.flatPrice, null);

        if (this.markupPercentage != null) {
            this.markupPercentage = this.markupPercentage.subtract(BigDecimal.ONE);
            this.markupPercentage = MoneyUtil.toProperScale(this.markupPercentage, null);
        }

        if (this.packagingMarkup != null)
            this.packagingMarkup = MoneyUtil.toProperScale(this.packagingMarkup, null);

        if (this.prototypeMarkup != null)
            this.prototypeMarkup = MoneyUtil.toProperScale(this.prototypeMarkup, null);

        if (this.shippingMarkup != null)
            this.shippingMarkup = MoneyUtil.toProperScale(this.shippingMarkup, null);
    }

}
