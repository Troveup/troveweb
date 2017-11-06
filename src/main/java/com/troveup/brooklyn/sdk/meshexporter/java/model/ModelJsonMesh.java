package com.troveup.brooklyn.sdk.meshexporter.java.model;

import java.util.List;

/**
 * Created by tim on 6/16/15.
 */
public class ModelJsonMesh
{
    private String id;
    private ModelJsonMeshMetaData metadata;
    private List<Float> vertices; // a flattened array of the position vectors of vertices
    private List<Float> normals; // a flattened array of the normal vectors in the mesh
    private List<Integer> triangles; // indices of each polygon, 3 per face

    public ModelJsonMesh()
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ModelJsonMeshMetaData getMetadata() {
        return metadata;
    }

    public void setMetadata(ModelJsonMeshMetaData metadata) {
        this.metadata = metadata;
    }

    public List<Float> getVertexData() {
        return vertices;
    }

    public void setVertexData(List<Float> vertices) {
        this.vertices = vertices;
    }

    public List<Float> getNormals() {
        return normals;
    }

    public void setNormals(List<Float> normals) {
        this.normals = normals;
    }

    public List<Integer> getTriangleData() {
        return triangles;
    }

    public void setTriangleData(List<Integer> triangles) {
        this.triangles = triangles;
    }
}
