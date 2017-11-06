
package com.troveup.brooklyn.sdk.meshexporter.java.model;

public class OperatorWeightJson {
    private String id;
    private float value;

    public OperatorWeightJson(String id, float value)
    {
        this.id = id;
        this.value = value;
    }

    public OperatorWeightJson(String id, String value)
    {
        this.id = id.replace("modelWeight-", "");
        this.value = Float.parseFloat(value);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    /*public String toString() {
        return id +": "+value;
    }*/
}

