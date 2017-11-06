package com.troveup.brooklyn.sdk.meshexporter.java.model;
import java.util.List;

public class ParameterJson {
    private String size;
    private String material;
    private List<String> visible;
    private List<OperatorWeightJson> weights;

    public ParameterJson() {
    }

    public String getSize() {
        if (size == null) {
            size = "ring_7.5";
        }
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public List<String> getVisible() {
        return visible;
    }

    public void setVisible(List<String> visible) {
        this.visible = visible;
    }

    public List<OperatorWeightJson> getWeights() {
        return weights;
    }

    public void setWeights(List<OperatorWeightJson> weights) {
        this.weights = weights;
    }

    /*public String toString() {
        String output = "Size: " + size + "\n"
            + "Material: " + material + "\n"
            + "Visible: ";
        for (int i = 0; i < visible.size(); i++) {
            output += visible.get(i) +", ";
        }

        output += "\nWeight Data:\n";
        for (int i = 0; i < weights.size(); i++) {
            output += weights.get(i) +"\n";
        }
        return output;
    }*/
}

