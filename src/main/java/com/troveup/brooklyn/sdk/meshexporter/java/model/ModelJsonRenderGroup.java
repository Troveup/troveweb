package com.troveup.brooklyn.sdk.meshexporter.java.model;

import java.util.List;

/**
 * Created by tim on 6/16/15.
 */
public class ModelJsonRenderGroup
{
    private String id;
    private String label;
    private List<String> meshes;

    public ModelJsonRenderGroup()
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getMeshes() {
        return meshes;
    }

    public void setMeshes(List<String> meshes) {
        this.meshes = meshes;
    }
}
