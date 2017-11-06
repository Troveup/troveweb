package com.troveup.brooklyn.sdk.meshexporter.java.model;

/**
 * Created by tim on 6/16/15.
 */
public class ModelJsonMeshMetaData
{
    private Integer vertices;
    private Integer triangles;

    public ModelJsonMeshMetaData()
    {

    }

    public Integer getVertices() {
        return vertices;
    }

    public void setVertices(Integer vertices) {
        this.vertices = vertices;
    }

    public Integer getTriangles() {
        return triangles;
    }

    public void setTriangles(Integer triangles) {
        this.triangles = triangles;
    }
}
