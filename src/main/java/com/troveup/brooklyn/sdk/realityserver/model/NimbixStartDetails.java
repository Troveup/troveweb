package com.troveup.brooklyn.sdk.realityserver.model;

/**
 * Created by tim on 6/21/15.
 */
public class NimbixStartDetails
{
    private String ipAddress;
    private String jobId;

    public NimbixStartDetails()
    {

    }

    public NimbixStartDetails(String ipAddress, String jobId)
    {
        this.ipAddress = ipAddress;
        this.jobId = jobId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}
