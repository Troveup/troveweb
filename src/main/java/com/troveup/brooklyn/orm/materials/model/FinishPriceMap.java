package com.troveup.brooklyn.orm.materials.model;

import javax.jdo.annotations.*;
import java.math.BigDecimal;

/**
 * Created by tim on 7/3/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class FinishPriceMap
{
    Long priceMapId;
    private Boolean tiered;
    private Float minVolume;
    private Float maxVolume;
    private BigDecimal flatFee;
    private BigDecimal costPerCm3;

    public FinishPriceMap(Boolean tiered, Float minVolume, Float maxVolume, BigDecimal flatFee, BigDecimal costPerCm3)
    {
        this.tiered = tiered;
        this.minVolume = minVolume;
        this.maxVolume = maxVolume;
        this.flatFee = flatFee;
        this.costPerCm3 = costPerCm3;
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getPriceMapId() {
        return priceMapId;
    }

    public void setPriceMapId(Long priceMapId) {
        this.priceMapId = priceMapId;
    }

    @Persistent
    public Float getMinVolume() {
        return minVolume;
    }

    public void setMinVolume(Float minVolume) {
        this.minVolume = minVolume;
    }

    @Persistent
    public Float getMaxVolume() {
        return maxVolume;
    }

    public void setMaxVolume(Float maxVolume) {
        this.maxVolume = maxVolume;
    }

    @Persistent
    @Column(scale = 2)
    public BigDecimal getFlatFee() {
        return flatFee;
    }

    public void setFlatFee(BigDecimal flatFee) {
        this.flatFee = flatFee;
    }

    @Persistent
    @Column(scale = 2)
    public BigDecimal getCostPerCm3() {
        return costPerCm3;
    }

    public void setCostPerCm3(BigDecimal costPerCm3) {
        this.costPerCm3 = costPerCm3;
    }

    @Persistent
    public Boolean getTiered() {
        return tiered;
    }

    public void setTiered(Boolean tiered) {
        this.tiered = tiered;
    }
}
