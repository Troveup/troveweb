package com.troveup.brooklyn.sdk.realityserver.model;

/**
 * Created by tim on 6/21/15.
 */
public class RealityServerJobResponse
{
    private String jobId;
    private String UUID;

    public RealityServerJobResponse()
    {

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
}
