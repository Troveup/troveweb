package com.troveup.brooklyn.sdk.print.model;

import java.util.List;

/**
 * Created by tim on 5/22/15.
 */
public abstract class PriceResponse
{
    Float itemPrice;
    List<PriceResponseAttribute> priceResponseAttributes;

    public PriceResponse()
    {

    }

    public Float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public List<PriceResponseAttribute> getPriceResponseAttributes() {
        return priceResponseAttributes;
    }

    public void setPriceResponseAttributes(List<PriceResponseAttribute> priceResponseAttributes) {
        this.priceResponseAttributes = priceResponseAttributes;
    }
}
