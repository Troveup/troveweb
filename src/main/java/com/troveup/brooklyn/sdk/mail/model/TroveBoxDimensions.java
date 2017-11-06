package com.troveup.brooklyn.sdk.mail.model;

/**
 * Created by tim on 8/11/15.
 */
public class TroveBoxDimensions
{
    private Float height;
    private Float width;
    private Float length;
    private Float weight;

    public TroveBoxDimensions()
    {

    }

    public TroveBoxDimensions(Float height, Float width, Float length, Float weight)
    {
        this.height = height;
        this.width = width;
        this.length = length;
        this.weight = weight;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public Float getLength() {
        return length;
    }

    public void setLength(Float length) {
        this.length = length;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public static TroveBoxDimensions getTroveBoxDimensions()
    {
        TroveBoxDimensions rval = new TroveBoxDimensions();

        //Inches and ounces
        rval.setHeight(2f);
        rval.setWidth(5f);
        rval.setLength(8.5f);
        rval.setWeight(7.9f);

        return rval;
    }
}
