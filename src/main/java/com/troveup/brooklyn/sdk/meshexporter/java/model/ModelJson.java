package com.troveup.brooklyn.sdk.meshexporter.java.model;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by tim on 6/16/15.
 */
public class ModelJson
{
    private ModelJsonMetaData metadata;
    private List<ModelJsonRenderGroup> renderGroups;
    private List<ModelJsonMesh> meshes;
    private List<ModelJsonControl> controls;
    private List<ModelJsonOperator> operators;

    public ModelJson()
    {

    }
    public ModelJsonMetaData getMetadata() {
        return this.metadata;
    }

    public void setMetadata(ModelJsonMetaData meta) {
        this.metadata = meta;
    }

    public List<ModelJsonRenderGroup> getRenderGroups() {
        return renderGroups;
    }

    public void setRenderGroups(List<ModelJsonRenderGroup> renderGroups) {
        this.renderGroups = renderGroups;
    }

    public List<ModelJsonMesh> getMeshes() {
        if (this.meshes != null) {
            return meshes;
        }
        return new ArrayList<ModelJsonMesh>();
    }

    public void setMeshes(List<ModelJsonMesh> meshes) {
        this.meshes = meshes;
    }

    public List<ModelJsonControl> getControls() {
        return controls;
    }

    public void setControls(List<ModelJsonControl> controls) {
        this.controls = controls;
    }

    public List<ModelJsonOperator> getOperators() {
        return operators;
    }

    public void setOperators(List<ModelJsonOperator> operators) {
        this.operators = operators;
    }
}
