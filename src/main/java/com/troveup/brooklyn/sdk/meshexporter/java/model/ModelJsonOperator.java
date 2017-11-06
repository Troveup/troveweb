package com.troveup.brooklyn.sdk.meshexporter.java.model;

/**
 * Created by tim on 6/16/15.
 */
public class ModelJsonOperator
{
    private String id;
    private String type;
    private String mesh;
    private ModelJsonMorphData parameters;

    // make custom handler for deserializing, pick a different data class depening on operator type
    // https://sites.google.com/site/gson/gson-user-guide
    // custom (serialization and) deserialization

    public ModelJsonOperator()
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMesh() {
        return mesh;
    }

    public void setMesh(String mesh) {
        this.mesh = mesh;
    }

    public ModelJsonMorphData getParameters() {
        return parameters;
    }

    public void setParameters(ModelJsonMorphData parameters) {
        this.parameters = parameters;
    }
}
