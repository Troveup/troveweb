package com.troveup.brooklyn.model;

/**
 * Created by tim on 6/1/15.
 */
public class CustomizerWeight
{
    private String id;
    private String value;

    public CustomizerWeight()
    {

    }

    public CustomizerWeight(String weightKey, String weightValue)
    {
        this.id = weightKey;
        this.value = weightValue;
    }

    public String getWeightKey() {
        return id;
    }

    public void setWeightKey(String weightKey) {
        this.id = weightKey;
    }

    public String getWeightValue() {
        return value;
    }

    public void setWeightValue(String weightValue) {
        this.value = weightValue;
    }
}
