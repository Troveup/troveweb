package com.troveup.brooklyn.model;

/**
 * Created by tim on 11/23/15.
 */
public class SamplePriceResponse
{
    private String price;

    public SamplePriceResponse()
    {

    }

    public SamplePriceResponse(String price)
    {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
