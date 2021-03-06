/**
 * CSG.java
 *
 * Copyright 2014-2014 Michael Hoffer <info@michaelhoffer.de>. All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY Michael Hoffer <info@michaelhoffer.de> "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL Michael Hoffer <info@michaelhoffer.de> OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of Michael Hoffer
 * <info@michaelhoffer.de>.
 */
package com.troveup.brooklyn.sdk.meshexporter.java.api;

// import javafx.scene.paint.Color;

import java.util.*;

/**
 * Constructive Solid Geometry (CSG).
 *
 * This implementation is a Java port of
 * <a
 * href="https://github.com/evanw/csg.js/">https://github.com/evanw/csg.js/</a>
 * with some additional features like polygon extrude, transformations etc.
 * Thanks to the author for creating the CSG.js library.<br><br>
 *
 * <b>Implementation Details</b>
 *
 * All CSG operations are implemented in terms of two functions,
 * {@link Node#clipTo(eu.mihosoft.vrl.v3d.Node)} and {@link Node#invert()},
 * which remove parts of a BSP tree inside another BSP tree and swap solid and
 * empty space, respectively. To find the union of {@code a} and {@code b}, we
 * want to remove everything in {@code a} inside {@code b} and everything in
 * {@code b} inside {@code a}, then combine polygons from {@code a} and
 * {@code b} into one solid:
 *
 * <blockquote><pre>
 *     a.clipTo(b);
 *     b.clipTo(a);
 *     a.build(b.allPolygons());
 * </pre></blockquote>
 *
 * The only tricky part is handling overlapping coplanar polygons in both trees.
 * The code above keeps both copies, but we need to keep them in one tree and
 * remove them in the other tree. To remove them from {@code b} we can clip the
 * inverse of {@code b} against {@code a}. The code for union now looks like
 * this:
 *
 * <blockquote><pre>
 *     a.clipTo(b);
 *     b.clipTo(a);
 *     b.invert();
 *     b.clipTo(a);
 *     b.invert();
 *     a.build(b.allPolygons());
 * </pre></blockquote>
 *
 * Subtraction and intersection naturally follow from set operations. If union
 * is {@code A | B}, differenceion is {@code A - B = ~(~A | B)} and intersection
 * is {@code A & B =
 * ~(~A | ~B)} where {@code ~} is the complement operator.
 */
public class CSG {

    private List<Polygon> polygons;
    private static OptType defaultOptType = OptType.NONE;
    private OptType optType = null;
    private PropertyStorage storage;

    private CSG() {
        storage = new PropertyStorage();
    }

    /**
     * Expose the empty constructor, added by Ian for convenience
     */
    public static CSG createEmpty() {
        return new CSG();
    }

    /**
     * Constructs a CSG from a list of {@link Polygon} instances.
     *
     * @param polygons polygons
     * @return a CSG instance
     */
    public static CSG fromPolygons(List<Polygon> polygons) {

        CSG csg = new CSG();
        csg.polygons = polygons;

        return csg;
    }

    /**
     * Constructs a CSG from the specified {@link Polygon} instances.
     *
     * @param polygons polygons
     * @return a CSG instance
     */
    public static CSG fromPolygons(Polygon... polygons) {
        return fromPolygons(Arrays.asList(polygons));
    }

    /**
     * Constructs a CSG from a list of {@link Polygon} instances.
     *
     * @param storage shared storage
     * @param polygons polygons
     * @return a CSG instance
     */
    public static CSG fromPolygons(PropertyStorage storage, List<Polygon> polygons) {

        CSG csg = new CSG();
        csg.polygons = polygons;

        csg.storage = storage;

        for (Polygon polygon : polygons) {
            polygon.setStorage(storage);
        }

        return csg;
    }

    /**
     * Constructs a CSG from the specified {@link Polygon} instances.
     *
     * @param storage shared storage
     * @param polygons polygons
     * @return a CSG instance
     */
    public static CSG fromPolygons(PropertyStorage storage, Polygon... polygons) {
        return fromPolygons(storage, Arrays.asList(polygons));
    }

    @Override
    public CSG clone() {
        CSG csg = new CSG();

        csg.setOptType(this.getOptType());

        csg.polygons = new ArrayList<>();
        for (Polygon p: polygons) {
            csg.polygons.add(p.clone());
        }
        //polygons.forEach((polygon) -> {
            //csg.polygons.add(polygon.clone());
        //});

        return csg;
    }

    /**
     *
     * @return the polygons of this CSG
     */
    public List<Polygon> getPolygons() {
        return polygons;
    }

    /**
     * Defines the CSg optimization type.
     *
     * @param type optimization type
     * @return this CSG
     */
    public CSG optimization(OptType type) {
        this.setOptType(type);
        return this;
    }

    /**
     * Return a new CSG solid representing the union of this csg and the
     * specified csg.
     *
     * <b>Note:</b> Neither this csg nor the specified csg are weighted.
     *
     * <blockquote><pre>
     *    A.union(B)
     *
     *    +-------+            +-------+
     *    |       |            |       |
     *    |   A   |            |       |
     *    |    +--+----+   =   |       +----+
     *    +----+--+    |       +----+       |
     *         |   B   |            |       |
     *         |       |            |       |
     *         +-------+            +-------+
     * </pre></blockquote>
     *
     *
     * @param csg other csg
     *
     * @return union of this csg and the specified csg
     */
    public CSG union(CSG csg) {

        switch (getOptType()) {
            case CSG_BOUND:
                return _unionCSGBoundsOpt(csg);
            case POLYGON_BOUND:
                return _unionPolygonBoundsOpt(csg);
            default:
//                return _unionIntersectOpt(csg);
                return _unionNoOpt(csg);
        }
    }
    
    /**
     * Returns a csg consisting of the polygons of this csg and the specified csg.
     * 
     * The purpose of this method is to allow fast union operations for objects
     * that do not intersect. 
     * 
     * <p><b>WARNING:</b> this method does not apply the csg algorithms. Therefore,
     * please ensure that this csg and the specified csg do not intersect.
     * 
     * @param csg csg
     * 
     * @return a csg consisting of the polygons of this csg and the specified csg
     */
    public CSG dumbUnion(CSG csg) {
        
        CSG result = this.clone();
        CSG other = csg.clone();
        
        result.polygons.addAll(other.polygons);
        
        return result;
    }

    /**
     * Return a new CSG solid representing the union of this csg and the
     * specified csgs.
     *
     * <b>Note:</b> Neither this csg nor the specified csg are weighted.
     *
     * <blockquote><pre>
     *    A.union(B)
     *
     *    +-------+            +-------+
     *    |       |            |       |
     *    |   A   |            |       |
     *    |    +--+----+   =   |       +----+
     *    +----+--+    |       +----+       |
     *         |   B   |            |       |
     *         |       |            |       |
     *         +-------+            +-------+
     * </pre></blockquote>
     *
     *
     * @param csgs other csgs
     *
     * @return union of this csg and the specified csgs
     */
    public CSG union(List<CSG> csgs) {

        CSG result = this;

        for (CSG csg : csgs) {
            result = result.union(csg);
        }

        return result;
    }

    /**
     * Return a new CSG solid representing the union of this csg and the
     * specified csgs.
     *
     * <b>Note:</b> Neither this csg nor the specified csg are weighted.
     *
     * <blockquote><pre>
     *    A.union(B)
     *
     *    +-------+            +-------+
     *    |       |            |       |
     *    |   A   |            |       |
     *    |    +--+----+   =   |       +----+
     *    +----+--+    |       +----+       |
     *         |   B   |            |       |
     *         |       |            |       |
     *         +-------+            +-------+
     * </pre></blockquote>
     *
     *
     * @param csgs other csgs
     *
     * @return union of this csg and the specified csgs
     */
    public CSG union(CSG... csgs) {
        return union(Arrays.asList(csgs));
    }

    private CSG _unionCSGBoundsOpt(CSG csg) {
        System.err.println("WARNING: using " + CSG.OptType.NONE
                + " since other optimization types missing for union operation.");
        return _unionIntersectOpt(csg);
    }

    private CSG _unionPolygonBoundsOpt(CSG csg) {
        List<Polygon> inner = new ArrayList<>();
        List<Polygon> outer = new ArrayList<>();

        Bounds bounds = csg.getBounds();

        for (Polygon p: this.polygons) {
            if (bounds.intersects(p.getBounds())) {
                inner.add(p);
            } else {
                outer.add(p);
            }
        }

        List<Polygon> allPolygons = new ArrayList<>();

        if (!inner.isEmpty()) {
            CSG innerCSG = CSG.fromPolygons(inner);

            allPolygons.addAll(outer);
            allPolygons.addAll(innerCSG._unionNoOpt(csg).polygons);
        } else {
            allPolygons.addAll(this.polygons);
            allPolygons.addAll(csg.polygons);
        }

        return CSG.fromPolygons(allPolygons).optimization(getOptType());
    }

    /**
     * Optimizes for intersection. If csgs do not intersect create a new csg
     * that consists of the polygon lists of this csg and the specified csg. In
     * this case no further space partitioning is performed.
     *
     * @param csg csg
     * @return the union of this csg and the specified csg
     */
    private CSG _unionIntersectOpt(CSG csg) {
        boolean intersects = false;

        Bounds bounds = csg.getBounds();

        for (Polygon p : polygons) {
            if (bounds.intersects(p.getBounds())) {
                intersects = true;
                break;
            }
        }

        List<Polygon> allPolygons = new ArrayList<>();

        if (intersects) {
            return _unionNoOpt(csg);
        } else {
            allPolygons.addAll(this.polygons);
            allPolygons.addAll(csg.polygons);
        }

        return CSG.fromPolygons(allPolygons).optimization(getOptType());
    }

    private CSG _unionNoOpt(CSG csg) {
        Node a = new Node(this.clone().polygons);
        Node b = new Node(csg.clone().polygons);
        a.clipTo(b);
        b.clipTo(a);
        b.invert();
        b.clipTo(a);
        b.invert();
        a.build(b.allPolygons());
        return CSG.fromPolygons(a.allPolygons()).optimization(getOptType());
    }

    /**
     * Return a new CSG solid representing the difference of this csg and the
     * specified csgs.
     *
     * <b>Note:</b> Neither this csg nor the specified csgs are weighted.
     *
     * <blockquote><pre>
     * A.difference(B)
     *
     * +-------+            +-------+
     * |       |            |       |
     * |   A   |            |       |
     * |    +--+----+   =   |    +--+
     * +----+--+    |       +----+
     *      |   B   |
     *      |       |
     *      +-------+
     * </pre></blockquote>
     *
     * @param csgs other csgs
     * @return difference of this csg and the specified csgs
     */
    public CSG difference(List<CSG> csgs) {

        if (csgs.isEmpty()) {
            return this.clone();
        }

        CSG csgsUnion = csgs.get(0);

        for (int i = 1; i < csgs.size(); i++) {
            csgsUnion = csgsUnion.union(csgs.get(i));
        }

        return difference(csgsUnion);
    }

    /**
     * Return a new CSG solid representing the difference of this csg and the
     * specified csgs.
     *
     * <b>Note:</b> Neither this csg nor the specified csgs are weighted.
     *
     * <blockquote><pre>
     * A.difference(B)
     *
     * +-------+            +-------+
     * |       |            |       |
     * |   A   |            |       |
     * |    +--+----+   =   |    +--+
     * +----+--+    |       +----+
     *      |   B   |
     *      |       |
     *      +-------+
     * </pre></blockquote>
     *
     * @param csgs other csgs
     * @return difference of this csg and the specified csgs
     */
    public CSG difference(CSG... csgs) {

        return difference(Arrays.asList(csgs));
    }

    /**
     * Return a new CSG solid representing the difference of this csg and the
     * specified csg.
     *
     * <b>Note:</b> Neither this csg nor the specified csg are weighted.
     *
     * <blockquote><pre>
     * A.difference(B)
     *
     * +-------+            +-------+
     * |       |            |       |
     * |   A   |            |       |
     * |    +--+----+   =   |    +--+
     * +----+--+    |       +----+
     *      |   B   |
     *      |       |
     *      +-------+
     * </pre></blockquote>
     *
     * @param csg other csg
     * @return difference of this csg and the specified csg
     */
    public CSG difference(CSG csg) {

        switch (getOptType()) {
            case CSG_BOUND:
                return _differenceCSGBoundsOpt(csg);
            case POLYGON_BOUND:
                return _differencePolygonBoundsOpt(csg);
            default:
                return _differenceNoOpt(csg);
        }
    }

    private CSG _differenceCSGBoundsOpt(CSG csg) {
        CSG b = csg;

        CSG a1 = this._differenceNoOpt(csg.getBounds().toCSG());
        CSG a2 = this.intersect(csg.getBounds().toCSG());

        return a2._differenceNoOpt(b)._unionIntersectOpt(a1).optimization(getOptType());
    }

    private CSG _differencePolygonBoundsOpt(CSG csg) {
        List<Polygon> inner = new ArrayList<>();
        List<Polygon> outer = new ArrayList<>();

        Bounds bounds = csg.getBounds();

        //TODO:  STREAM IS V8
        for (Polygon p: this.polygons) {
            if (bounds.intersects(p.getBounds())) {
                inner.add(p);
            } else {
                outer.add(p);
            }
        }
        CSG innerCSG = CSG.fromPolygons(inner);

        List<Polygon> allPolygons = new ArrayList<>();
        allPolygons.addAll(outer);
        allPolygons.addAll(innerCSG._differenceNoOpt(csg).polygons);

        return CSG.fromPolygons(allPolygons).optimization(getOptType());
    }

    private CSG _differenceNoOpt(CSG csg) {

        Node a = new Node(this.clone().polygons);
        Node b = new Node(csg.clone().polygons);

        a.invert();
        a.clipTo(b);
        b.clipTo(a);
        b.invert();
        b.clipTo(a);
        b.invert();
        a.build(b.allPolygons());
        a.invert();

        CSG csgA = CSG.fromPolygons(a.allPolygons()).optimization(getOptType());
        return csgA;
    }

    /**
     * Return a new CSG solid representing the intersection of this csg and the
     * specified csg.
     *
     * <b>Note:</b> Neither this csg nor the specified csg are weighted.
     *
     * <blockquote><pre>
     *     A.intersect(B)
     *
     *     +-------+
     *     |       |
     *     |   A   |
     *     |    +--+----+   =   +--+
     *     +----+--+    |       +--+
     *          |   B   |
     *          |       |
     *          +-------+
     * }
     * </pre></blockquote>
     *
     * @param csg other csg
     * @return intersection of this csg and the specified csg
     */
    public CSG intersect(CSG csg) {

        Node a = new Node(this.clone().polygons);
        Node b = new Node(csg.clone().polygons);
        a.invert();
        b.clipTo(a);
        b.invert();
        a.clipTo(b);
        b.clipTo(a);
        a.build(b.allPolygons());
        a.invert();
        return CSG.fromPolygons(a.allPolygons()).optimization(getOptType());
    }

    /**
     * Return a new CSG solid representing the intersection of this csg and the
     * specified csgs.
     *
     * <b>Note:</b> Neither this csg nor the specified csgs are weighted.
     *
     * <blockquote><pre>
     *     A.intersect(B)
     *
     *     +-------+
     *     |       |
     *     |   A   |
     *     |    +--+----+   =   +--+
     *     +----+--+    |       +--+
     *          |   B   |
     *          |       |
     *          +-------+
     * }
     * </pre></blockquote>
     *
     * @param csgs other csgs
     * @return intersection of this csg and the specified csgs
     */
    public CSG intersect(List<CSG> csgs) {

        if (csgs.isEmpty()) {
            return this.clone();
        }

        CSG csgsUnion = csgs.get(0);

        for (int i = 1; i < csgs.size(); i++) {
            csgsUnion = csgsUnion.union(csgs.get(i));
        }

        return intersect(csgsUnion);
    }

    /**
     * Return a new CSG solid representing the intersection of this csg and the
     * specified csgs.
     *
     * <b>Note:</b> Neither this csg nor the specified csgs are weighted.
     *
     * <blockquote><pre>
     *     A.intersect(B)
     *
     *     +-------+
     *     |       |
     *     |   A   |
     *     |    +--+----+   =   +--+
     *     +----+--+    |       +--+
     *          |   B   |
     *          |       |
     *          +-------+
     * }
     * </pre></blockquote>
     *
     * @param csgs other csgs
     * @return intersection of this csg and the specified csgs
     */
    public CSG intersect(CSG... csgs) {

        return intersect(Arrays.asList(csgs));
    }

    /**
     * Returns this csg in STL string format.
     *
     * @return this csg in STL string format
     */
    public String toStlString() {
        StringBuilder sb = new StringBuilder();
        toStlString(sb);
        return sb.toString();
    }

    /**
     * Returns this csg in STL string format.
     *
     * @param sb string builder
     *
     * @return the specified string builder
     */
    public StringBuilder toStlString(StringBuilder sb) {
        sb.append("solid v3d.csg\n");

        //TODO:  STREAM IS V8
        for (Polygon p: this.polygons) {
            p.toStlString(sb);
        }
        sb.append("endsolid v3d.csg\n");
        return sb;
    }


    public ObjFile toObj() {

        StringBuilder objSb = new StringBuilder();

        List<Vertex> vertices = new ArrayList<>();
        List<Vector3d> vertexNormals = new ArrayList<>();
        List<PolygonStruct> indices = new ArrayList<>();

        // could use an octree to more efficiently search for new positions
        objSb.append("\n# Vertices\n");
        for (Polygon p : polygons) {
            List<Integer> polyIndices = new ArrayList<>();

            for (Vertex v: p.vertices) {
                if (!vertices.contains(v)) {
                    vertices.add(v);
                    vertexNormals.add(new Vector3d(0,0,0));
                    v.toObjString(objSb);
                    polyIndices.add(vertices.size());
                } else {
                    polyIndices.add(vertices.indexOf(v) + 1);
                }
            }

            indices.add(new PolygonStruct(polyIndices));
        }

        for (PolygonStruct ps : indices) {
            List<Integer> pVerts = ps.indices;
            int index1 = pVerts.get(0);
            for (int i = 0; i < pVerts.size() - 2; i++) {
                int index2 = pVerts.get(i + 1);
                int index3 = pVerts.get(i + 2);

                Vector3d pos1 = vertices.get(index1-1).pos;
                Vector3d pos2 = vertices.get(index2-1).pos;
                Vector3d pos3 = vertices.get(index3-1).pos;

                Vector3d side1 = pos1.minus(pos2);
                Vector3d side2 = pos3.minus(pos2);
                Vector3d norm = side1.cross(side2);

                Vector3d n1 = vertexNormals.get(index1-1);
                Vector3d n2 = vertexNormals.get(index2-1);
                Vector3d n3 = vertexNormals.get(index3-1);
                vertexNormals.set(index1-1, n1.plus(norm));
                vertexNormals.set(index2-1, n2.plus(norm));
                vertexNormals.set(index3-1, n3.plus(norm));
            }
        }

        objSb.append("\n# Normals").append("\n");
        for (Vector3d normVec: vertexNormals) {
            objSb.append("vn ").append(normVec.toObjString()).append("\n");
        }

        objSb.append("\n# Faces").append("\n");
        for (PolygonStruct ps : indices) {
            // we triangulate the polygon to ensure 
            // compatibility with 3d printer software
            List<Integer> pVerts = ps.indices;
            int index1 = pVerts.get(0);
            for (int i = 0; i < pVerts.size() - 2; i++) {
                int index2 = pVerts.get(i + 1);
                int index3 = pVerts.get(i + 2);

                objSb.append("f ").
                        append(index1+"//"+index1).append(" ").
                        append(index2+"//"+index2).append(" ").
                        append(index3+"//"+index3).append("\n");
            }
        }
        StringBuilder mtlSb = new StringBuilder();

        return new ObjFile(objSb.toString(), mtlSb.toString());
    }

    /**
     * Returns this csg in OBJ string format.
     *
     * @param sb string builder
     * @return the specified string builder
     */
    public StringBuilder toObjString(StringBuilder sb) {
        sb.append("# Group").append("\n");
        sb.append("g v3d.csg\n");

        class olPolygonStruct {

            PropertyStorage storage;
            List<Integer> indices;
            String materialName;

            public olPolygonStruct(PropertyStorage storage, List<Integer> indices, String materialName) {
                this.storage = storage;
                this.indices = indices;
                this.materialName = materialName;
            }
        }

        List<Vertex> vertices = new ArrayList<>();
        List<olPolygonStruct> indices = new ArrayList<>();

        sb.append("\n# Vertices\n");

        for (Polygon p : polygons) {
            List<Integer> polyIndices = new ArrayList<>();

            //TODO:  STREAM IS V8
            for (Vertex v: p.vertices) {
                if (!vertices.contains(v)) {
                    vertices.add(v);
                    v.toObjString(sb);
                    polyIndices.add(vertices.size());
                } else {
                    polyIndices.add(vertices.indexOf(v) + 1);
                }
            }
        }

        sb.append("\n# Faces").append("\n");

        for (olPolygonStruct ps : indices) {
            // we triangulate the polygon to ensure 
            // compatibility with 3d printer software
            List<Integer> pVerts = ps.indices;
            int index1 = pVerts.get(0);
            for (int i = 0; i < pVerts.size() - 2; i++) {
                int index2 = pVerts.get(i + 1);
                int index3 = pVerts.get(i + 2);

                sb.append("f ").
                        append(index1).append(" ").
                        append(index2).append(" ").
                        append(index3).append("\n");
            }
        }

        sb.append("\n# End Group v3d.csg").append("\n");

        return sb;
    }

    /**
     * Returns this csg in OBJ string format.
     *
     * @return this csg in OBJ string format
     */
    public String toObjString() {
        StringBuilder sb = new StringBuilder();
        return toObjString(sb).toString();
    }

    public CSG weighted(WeightFunction f) {
        return new Modifier(f).modified(this);
    }

    /**
     * Returns a transformed copy of this CSG.
     *
     * @param transform the transform to apply
     *
     * @return a transformed copy of this CSG
     */
    public CSG transformed(Transform transform) {

        if (polygons.isEmpty()) {
            return clone();
        }

        //TODO:  JAVA v8 Type List, make it an ArrayList<>() instead?
        List<Polygon> newpolygons = new ArrayList<>();
        for (Polygon p: this.polygons) {
            newpolygons.add(p.transformed(transform));
        }

        CSG result = CSG.fromPolygons(newpolygons).optimization(getOptType());

        result.storage = storage;

        return result;
    }
 
    /**
     * Returns the bounds of this csg.
     *
     * @return bouds of this csg
     */
    public Bounds getBounds() {

        if (polygons.isEmpty()) {
            return new Bounds(Vector3d.ZERO, Vector3d.ZERO);
        }

        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;

        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;

        for (Polygon p : getPolygons()) {

            for (int i = 0; i < p.vertices.size(); i++) {

                Vertex vert = p.vertices.get(i);

                if (vert.pos.x < minX) {
                    minX = vert.pos.x;
                }
                if (vert.pos.y < minY) {
                    minY = vert.pos.y;
                }
                if (vert.pos.z < minZ) {
                    minZ = vert.pos.z;
                }

                if (vert.pos.x > maxX) {
                    maxX = vert.pos.x;
                }
                if (vert.pos.y > maxY) {
                    maxY = vert.pos.y;
                }
                if (vert.pos.z > maxZ) {
                    maxZ = vert.pos.z;
                }

            } // end for vertices

        } // end for polygon

        return new Bounds(
                new Vector3d(minX, minY, minZ),
                new Vector3d(maxX, maxY, maxZ));
    }

    /**
     * @return the optType
     */
    private OptType getOptType() {
        return optType != null ? optType : defaultOptType;
    }

    /**
     * @param optType the optType to set
     */
    public static void setDefaultOptType(OptType optType) {
        defaultOptType = optType;
    }

    /**
     * @param optType the optType to set
     */
    public void setOptType(OptType optType) {
        this.optType = optType;
    }

    public static enum OptType {

        CSG_BOUND,
        POLYGON_BOUND,
        NONE
    }

}
