/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.troveup.brooklyn.sdk.meshexporter.java.api;

/**
 * Weight function.
 * 
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
//TODO: JAVA v8 Annotation
//@FunctionalInterface
public interface WeightFunction {
    /**
     * Evaluates the function at the specified location.
     * @param v location
     * @param csg csg
     * @return the weight of the specified position
     */
    double eval(Vector3d v, CSG csg);
}
