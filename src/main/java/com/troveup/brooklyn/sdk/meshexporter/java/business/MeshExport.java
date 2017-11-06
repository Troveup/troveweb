package com.troveup.brooklyn.sdk.meshexporter.java.business;

import com.google.gson.Gson;
import com.troveup.brooklyn.sdk.meshexporter.java.api.*;
import com.troveup.brooklyn.sdk.meshexporter.java.model.*;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MeshExport {

    Map<String, Double> sizeDiameters  = new HashMap<String, Double>();

    public byte[] process(ModelJson model, ParameterJson params) {
        List<ModelJsonOperator> ops = model.getOperators();

        double originalDiameter = model.getMetadata().getDiameter();

        List<CSG> components = new ArrayList<>();
        if (params.getVisible() == null || params.getVisible().size() == 0)
        {
            // HACK: simulate the visible list with operator weights in case making `visible` available requires dev
            String activeID = "ACTIVEMESH-";
            String visibleMesh = null;
            for (int i = 0, in = params.getWeights().size(); i < in; i++) {
                OperatorWeightJson weight = params.getWeights().get(i);
                if (weight.getId().contains(activeID)) {
                    visibleMesh = weight.getId().substring(activeID.length());
                    System.out.println(visibleMesh);
                }
            }

            if (visibleMesh != null && !visibleMesh.isEmpty()) {

                for (ModelJsonMesh mesh : model.getMeshes()) {
                    if (mesh.getId().equals(visibleMesh)) {

                        CSG componentCSG = toCSG(mesh, ops, originalDiameter, params);
                        components.add(componentCSG);
                        break;
                    }
                }

            } else { // fallback to old method of calculating visible based on deprecated render group
                ModelJsonRenderGroup mainRenderGroup = null;
                for (ModelJsonRenderGroup group : model.getRenderGroups())
                {
                    if (group.getId().toLowerCase().equals("main")) {
                        mainRenderGroup = group;
                        break;
                    }
                }

                if (mainRenderGroup != null) {
                    for (ModelJsonMesh mesh : model.getMeshes()) {
                        if (mesh.getId().toLowerCase().equals(mainRenderGroup.getMeshes().get(0).toLowerCase())) {

                            CSG componentCSG = toCSG(mesh, ops, originalDiameter, params);
                            components.add(componentCSG);
                            break;
                        }
                    }
                }

            }

        }
        else {
            for (ModelJsonMesh mesh : model.getMeshes()) {
                if (!listContains(mesh.getId(), params.getVisible())) {
                    continue;
                }

                CSG componentCSG = toCSG(mesh, ops, originalDiameter, params);
                components.add(componentCSG);
                // toOBJ(componentCSG, "component-"+mesh.getId());
            }
        }

        if (components.size() < 1) {
            return null;
        }

        CSG finalCSG = components.get(0);
        components.remove(0);
        if (components.size() > 0) {
            // FIXME: union algorith is currently broken
            // finalCSG = finalCSG.union(components);
        }

        //dumpCSG(finalCSG);
        return toOBJ(finalCSG);
    }

    public GeometryAnalysis getVolume(ModelJson model, ParameterJson params)
    {
        List<ModelJsonOperator> ops = model.getOperators();

        double originalDiameter = model.getMetadata().getDiameter();

        List<CSG> components = new ArrayList<>();
        if (params.getVisible() == null || params.getVisible().size() == 0)
        {
            ModelJsonRenderGroup mainRenderGroup = null;
            for (ModelJsonRenderGroup group : model.getRenderGroups())
            {
                if (group.getId().toLowerCase().equals("main")) {
                    mainRenderGroup = group;
                    break;
                }
            }

            if (mainRenderGroup != null) {
                for (ModelJsonMesh mesh : model.getMeshes()) {
                    if (mesh.getId().toLowerCase().equals(mainRenderGroup.getMeshes().get(0).toLowerCase())) {

                        CSG componentCSG = toCSG(mesh, ops, originalDiameter, params);
                        components.add(componentCSG);
                        break;
                    }
                }
            }
        }
        else {

            for (ModelJsonMesh mesh : model.getMeshes()) {
                // System.out.print("Examining mesh "+mesh.getId() +"...");
                if (!listContains(mesh.getId(), params.getVisible())) {
                    // System.out.println("not found in list of visible meshes, skipping. ");
                    continue;
                }
                // System.out.println();

                CSG componentCSG = toCSG(mesh, ops, originalDiameter, params);
                components.add(componentCSG);
                // toOBJ(componentCSG, "component-"+mesh.getId());
            }
        }

        if (components.size() < 1) {
            return null;
        }

        CSG finalCSG = components.get(0);
        components.remove(0);
        if (components.size() > 0) {
            // FIXME: union algorith is currently broken
            // finalCSG = finalCSG.union(components);
        }

        return analyze(finalCSG);
    }


    private GeometryAnalysis analyze(CSG csg) {

        GeometryAnalysis ga = new GeometryAnalysis();
        Vector3d min = new Vector3d(0, 0, 0);
        Vector3d max = new Vector3d(0, 0, 0);

        List<Vertex> vertices = new ArrayList<>();
        List<PolygonStruct> indices = new ArrayList<>(); // polygon indices as they would appear in an OBJ face (1-indexed)

        for (Polygon p : csg.getPolygons()) {
            List<Integer> polyIndices = new ArrayList<>();

            for (Vertex v: p.vertices) {
                if (!vertices.contains(v)) {
                    vertices.add(v);
                    
                    // update min and max points while we're at it
                    if (v.pos.x > max.x) max.x = v.pos.x;
                    if (v.pos.y > max.y) max.y = v.pos.y;
                    if (v.pos.z > max.z) max.z = v.pos.z;
                    if (v.pos.x < min.x) min.x = v.pos.x;
                    if (v.pos.y < min.y) min.y = v.pos.y;
                    if (v.pos.z < min.z) min.z = v.pos.z;

                    polyIndices.add(vertices.size());
                } else {
                    polyIndices.add(vertices.indexOf(v) + 1);
                }
            }
            indices.add(new PolygonStruct(polyIndices));
        }

        // float totalVolume = 0;
        for (PolygonStruct ps : indices) {

            // triangulate the polygons
            List<Integer> pVerts = ps.indices;
            int index1 = pVerts.get(0) - 1;
            for (int i = 0; i < pVerts.size() - 2; i++) {
                int index2 = pVerts.get(i + 1) - 1;
                int index3 = pVerts.get(i + 2) - 1;

                Vector3d v1 = vertices.get(index1).pos;
                Vector3d v2 = vertices.get(index2).pos;
                Vector3d v3 = vertices.get(index3).pos;

                // System.out.println("Source vectors: "+v1+" "+v2+" "+v3);
                Vector3d a = v2.minus(v1);
                Vector3d b = v3.minus(v1);
                Vector3d crossProduct =  v1.cross(v2);
                double faceArea = crossProduct.magnitude() / 2.0;
                // System.out.println("face with area: "+faceArea);
                
                // positive sign for positive contribution to volume total
                // sign is flipped if face vertex order is wrong, just take absolute value of summation to cancel it out as long as faces are consistent
                double deltaVolume = (1/6.0)*( -(v3.x*v2.y*v1.z) + (v2.x*v3.y*v1.z) + (v3.x*v1.y*v2.z)
                        - (v1.x*v3.y*v2.z) - (v2.x*v1.y*v3.z) + (v1.x*v2.y*v3.z));

                ga.surface += faceArea;
                ga.volume += deltaVolume;
            }
        }

        ga.extent= new Vector3d(
                Math.abs(max.x - min.x),
                Math.abs(max.y - min.y),
                Math.abs(max.z - min.z));
        return ga;
    }

    private static boolean listContains(String needle, List<String> haystack) {
        for (int i = 0, ni = haystack.size(); i < ni; i++) {
            if (haystack.get(i).equals(needle)) return true;
        }
        return false;
    }

    private CSG toCSG(ModelJsonMesh mesh, List<ModelJsonOperator> ops, double originalDiameter, ParameterJson params) {
        applyOperators(mesh, ops, params);

        List<Polygon> polys = toPolygons(mesh);
        CSG component = CSG.fromPolygons(polys);

        String[] sizeParts = params.getSize().split("_");
        String sizeCategory = sizeParts[0];
        double sizeFloat = 0;
        double outputDiameter = 1; // unprintably small so we know immediately something got messed up (is this a good idea?)

        if (sizeCategory.equals("ring")) {
            sizeFloat = Float.parseFloat(sizeParts[1]);
            outputDiameter = .825 * sizeFloat + 11.6;
        } else if (sizeCategory.equals("bracelet")) {
            if (sizeParts[1].equals("Extra Small")) {
                sizeFloat = 0;
            } else if (sizeParts[1].equals("Small")) {
                sizeFloat = 1;
            } else if (sizeParts[1].equals("Medium")) {
                sizeFloat = 2;
            } else if (sizeParts[1].equals("Large")) {
                sizeFloat = 3;
            } else if (sizeParts[1].equals("Extra Large")) {
                sizeFloat = 4;
            } else {
                sizeFloat = Float.parseFloat(sizeParts[1]);
            }

            outputDiameter = 5 * sizeFloat + 50;
        } else {
            System.out.println("Invalid category for sizing purpose");
        }

        double scaleFactor = outputDiameter / originalDiameter;
        System.out.println("Desired diameter of "+outputDiameter+"; scaling original diameter "+originalDiameter+" up by a factor of "+scaleFactor);
        CSG scaledComponent = component.transformed(Transform.unity().scale(scaleFactor));

        Vector3d groundPlaneOffset = calculateGroundOffset(scaledComponent);
        return scaledComponent.transformed(Transform.unity().translate(groundPlaneOffset));
    }

    private Vector3d calculateGroundOffset(CSG component) {
        // postive Y is up direction, return offset so that object is above xz plane
        Bounds b = component.getBounds();
        double minY = b.getMin().y;
        return new Vector3d(0,-minY,0);
    }

    // updates the data referenced by the mesh
    // returns a boolean signifying success
    private boolean applyOperators(ModelJsonMesh mesh, List<ModelJsonOperator> ops, ParameterJson params) {
        int numVerts = mesh.getMetadata().getVertices();
        int numTris = mesh.getMetadata().getTriangles();

        int vertexDataCount = mesh.getVertexData().size();
        int triangleDataCount = mesh.getTriangleData().size();

        for (int i = 0, in = params.getWeights().size(); i < in; i++) {
            OperatorWeightJson weight = params.getWeights().get(i);

            float operatorValue = weight.getValue();
            if (operatorValue < .001 && operatorValue > -.001) { // ignore noop morphs
                continue;
            }

            // FIXME: have to iterate through operators because they're stored in a list
            for (int j = 0, jn = ops.size(); j < jn; j++) {
                ModelJsonOperator op = ops.get(j);

                if (op.getId().equals(weight.getId())) {
                    ModelJsonMorphData morph = op.getParameters();
                    int numDeformedVerts = morph.getModifiedCount();
                    if (numDeformedVerts > numVerts) {
                        //System.out.println("Can't deform more vertices than exist in original mesh data");
                        return false;
                    }

                    List<Float> deformedVertexData = mesh.getVertexData();
                    for (int k = 0, kn = morph.getModifiedCount(); k < kn; k++) {
                        int offset = k * 3;
                        int originalIndex = morph.getIndices().get(k);

                        List<Float> disps = morph.getDisplacements();
                        float dx = disps.get(offset++) * operatorValue;
                        float dy = disps.get(offset++) * operatorValue;
                        float dz = disps.get(offset++) * operatorValue;

                        // directly update the vertex data
                        offset = originalIndex * 3;
                        dx += deformedVertexData.get(offset);
                        dy += deformedVertexData.get(offset+1);
                        dz += deformedVertexData.get(offset+2);
                        deformedVertexData.set(offset++, dx);
                        deformedVertexData.set(offset++, dy);
                        deformedVertexData.set(offset++, dz);
                    }
                    mesh.setVertexData(deformedVertexData);
                    break;
                }
            }
        }
        return true;
    }

    private static List<Vector3d> toVectors(List<Float> vectorData) {
        List<Vector3d> vectors = new ArrayList<Vector3d>();

        int numVecs = count(vectorData, 3);
        for (int i = 0; i < numVecs; i++) {
            int j = 3 * i;

            Vector3d newVec = new Vector3d(vectorData.get(j), vectorData.get(j+1), vectorData.get(j+2));
            vectors.add(newVec);
        }
        return vectors;
    }

    private static int count(List data, int multiples) {
        if (data.size() % multiples != 0) {
            //System.out.println("Data is not a multiple of "+multiples+", aborting");
            return 0;
        }
        return data.size() / multiples;
    }

    private static void printVectors(List<Vector3d> data) {
        int n = data.size();
        for (int i = 0; i < n; i++) {
            Vector3d vec = data.get(i);
            String vecString = vec.toStlString();
        }
    }

    private static List<Polygon> toPolygons(ModelJsonMesh mesh) {

        List<Vector3d> positions = toVectors(mesh.getVertexData());
        List<Polygon> polys = new ArrayList<Polygon>();

        List<Integer> triangleData = mesh.getTriangleData();
        int numTris = count(triangleData, 3);

        for (int i = 0; i < numTris; i++) {
            int j = 3 * i;
            List<Vertex> triVerts = new ArrayList<Vertex>();

            int a = triangleData.get(j);
            int b = triangleData.get(j+1);
            int c = triangleData.get(j+2);

            triVerts.add(new Vertex(positions.get(a), Vector3d.ZERO));
            triVerts.add(new Vertex(positions.get(b), Vector3d.ZERO));
            triVerts.add(new Vertex(positions.get(c), Vector3d.ZERO));

            polys.add(new Polygon(triVerts));
        }
        return polys;
    }
 
    private void writeToFile(String filename, String content) {
        try {
            File file = new File(filename);
 
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
 
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] toOBJ(CSG csg) {
        String objString = csg.toObj().getObj();
        return objString.getBytes(Charset.forName("UTF-8"));
    }

    private ParameterJson fetchParameterJson(String fsName) {
        byte[] encoded = null;
        try{
            encoded = Files.readAllBytes(Paths.get(fsName));
        }catch(IOException e){
            e.printStackTrace();
        }
        String parameterFile = new String(encoded, Charset.forName("UTF-8"));

        Gson gson = new Gson();
        return gson.fromJson(parameterFile, ParameterJson.class);
    }

    // FIXME: will replace this reference to the filesystem first with passed data, then a GCS interface
    private ModelJson fetchModelJson(String fsName) {
        byte[] encoded = null;
        try{
            encoded = Files.readAllBytes(Paths.get(fsName));
        }catch(IOException e){
            e.printStackTrace();
        }
        String jsonFile = new String(encoded, Charset.forName("UTF-8"));

        // Gson deserialization library, courtesy of Google
        Gson gson = new Gson();
        return gson.fromJson(jsonFile, ModelJson.class);
    }
}
