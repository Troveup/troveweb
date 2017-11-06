
package com.troveup.brooklyn.sdk.meshexporter.java.api;

public class GeometryAnalysis {
    public Vector3d extent; // dimensions of bounding box
    public float surface; // surface area in mm^2
    public float volume; // volume in mm^3

    public GeometryAnalysis() {
        extent = null;
        surface = 0;
        volume = 0;
    }
}
