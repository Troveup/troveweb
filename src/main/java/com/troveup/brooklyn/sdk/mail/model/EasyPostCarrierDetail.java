package com.troveup.brooklyn.sdk.mail.model;

/**
 * Created by tim on 8/25/15.
 */
public class EasyPostCarrierDetail
{
    private String object;
    private String service;
    private String container_type;

    public EasyPostCarrierDetail()
    {

    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getContainer_type() {
        return container_type;
    }

    public void setContainer_type(String container_type) {
        this.container_type = container_type;
    }
}
