package com.troveup.brooklyn.model;

/**
 * Created by tim on 9/30/15.
 */
public class PriceEstimate
{
    private String estimate;
    private String shipping;
    private Boolean giftwrapIncludedInEstimate;

    public PriceEstimate(String estimate)
    {
        this.estimate = estimate;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getEstimate() {
        return estimate;
    }

    public void setEstimate(String estimate) {
        this.estimate = estimate;
    }

    public Boolean getGiftwrapIncludedInEstimate() {
        return giftwrapIncludedInEstimate;
    }

    public void setGiftwrapIncludedInEstimate(Boolean giftwrapIncludedInEstimate) {
        this.giftwrapIncludedInEstimate = giftwrapIncludedInEstimate;
    }
}
