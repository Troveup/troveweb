package com.troveup.brooklyn.sdk.meshexporter.java.model;

import java.util.List;

/**
 * Created by tim on 6/16/15.
 */
public class ModelJsonMorphData
{
    private Integer modifiedCount;
    private List<Integer> indices;
    private List<Float> displacements;

    public ModelJsonMorphData()
    {

    }

    public Integer getModifiedCount() {
        return modifiedCount;
    }

    public void setModifiedCount(Integer modifiedCount) {
        this.modifiedCount = modifiedCount;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public void setIndices(List<Integer> indices) {
        this.indices = indices;
    }

    public List<Float> getDisplacements() {
        return displacements;
    }

    public void setDisplacements(List<Float> displacements) {
        this.displacements = displacements;
    }

    /*public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("# Modified Vertices: "+modifiedCount+"\n");
        b.append("Indices: [");
        for (int i = 0, in = indices.size(); i < in; i++) {
            b.append(indices.get(i)+",");
        }
        b.append("]\nVertex Data: [");
        for (int i = 0, in = displacements.size(); i < in; i++) {
            b.append(displacements.get(i)+",");
        }
        b.append("]");
        return b.toString();
    }*/
}
