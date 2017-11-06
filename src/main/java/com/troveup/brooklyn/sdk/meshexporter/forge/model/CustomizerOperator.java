package com.troveup.brooklyn.sdk.meshexporter.forge.model;

import com.troveup.brooklyn.model.CustomizerWeight;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 8/19/15.
 */
public class CustomizerOperator
{
    private String id;
    private String value;

    public CustomizerOperator()
    {

    }

    public CustomizerOperator(String id, String value)
    {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static List<CustomizerOperator> toCustomizerOperators(List<CustomizerWeight> weights)
    {
        List<CustomizerOperator> rval = new ArrayList<>();

        for (CustomizerWeight weight : weights)
        {
            rval.add(new CustomizerOperator(weight.getWeightKey(), weight.getWeightValue()));
        }

        return rval;
    }
}
