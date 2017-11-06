package com.troveup.brooklyn.orm.item.model;

import javax.jdo.annotations.*;
import java.math.BigDecimal;

/**
 * Created by tim on 10/22/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class Chain
{
    private Long chainId;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean active;

    public Chain()
    {

    }

    public Chain(Long chainId)
    {
        this.chainId = chainId;
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getChainId() {
        return chainId;
    }

    public void setChainId(Long chainId) {
        this.chainId = chainId;
    }

    @Persistent
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Persistent
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Persistent
    @Column(scale = 2)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
