package com.troveup.brooklyn.sdk.realityserver.model;

import java.util.List;

/**
 * Created by tim on 6/24/15.
 */
public class RealityServerPostbackResponse
{
    private int errorId;
    private String errorMessage;
    private List<RealityServerCamera> cameras;

    private String jobId;
    private String UUID;
    private RealityServerRenderRequest request;

    public RealityServerPostbackResponse()
    {

    }

    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public RealityServerRenderRequest getRequest() {
        return request;
    }

    public void setRequest(RealityServerRenderRequest request) {
        this.request = request;
    }

    public List<RealityServerCamera> getCameras() {
        return cameras;
    }

    public void setCameras(List<RealityServerCamera> cameras) {
        this.cameras = cameras;
    }
}
