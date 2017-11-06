package com.troveup.brooklyn.sdk.realityserver.model;

/**
 * Created by tim on 6/21/15.
 */
public class NimbixStartResponse
{
    private String status;
    private String job_name;

    public NimbixStartResponse()
    {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }
}
