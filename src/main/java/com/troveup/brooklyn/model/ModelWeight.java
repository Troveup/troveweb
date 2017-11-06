package com.troveup.brooklyn.model;

import com.troveup.brooklyn.orm.item.model.ItemAttribute;

/**
 * Created by tim on 6/16/15.
 */
public class ModelWeight
{
    private String weightLabel;
    private String weightValue;

    public ModelWeight()
    {

    }

    public String getWeightLabel() {
        return weightLabel;
    }

    public void setWeightLabel(String weightLabel) {
        this.weightLabel = weightLabel;
    }

    public String getWeightValue() {
        return weightValue;
    }

    public void setWeightValue(String weightValue) {
        this.weightValue = weightValue;
    }

    public static ModelWeight fromAttribute(ItemAttribute attribute)
    {
        ModelWeight modelWeight = new ModelWeight();
        modelWeight.setWeightLabel(attribute.getAttributeName());
        modelWeight.setWeightValue(attribute.getAttributeValue());

        return modelWeight;
    }
}
