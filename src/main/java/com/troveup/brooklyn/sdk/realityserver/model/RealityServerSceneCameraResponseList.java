package com.troveup.brooklyn.sdk.realityserver.model;

/**
 * Created by tim on 9/15/15.
 */
public class RealityServerSceneCameraResponseList
{
    private String name;
    private String baseCamera;

    public RealityServerSceneCameraResponseList(String name, String baseCamera)
    {
        this.name = name;
        this.baseCamera = baseCamera;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBaseCamera() {
        return baseCamera;
    }

    public void setBaseCamera(String baseCamera) {
        this.baseCamera = baseCamera;
    }
}
