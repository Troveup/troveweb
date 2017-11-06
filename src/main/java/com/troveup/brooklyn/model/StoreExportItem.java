package com.troveup.brooklyn.model;

import java.math.BigDecimal;

/**
 * Created by tim on 6/10/16.
 */
public class StoreExportItem
{
    private Long id;
    private BigDecimal price;

    public StoreExportItem()
    {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
