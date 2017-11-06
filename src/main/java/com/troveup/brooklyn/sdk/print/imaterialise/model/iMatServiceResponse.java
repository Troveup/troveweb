package com.troveup.brooklyn.sdk.print.imaterialise.model;

import java.util.List;

/**
 * Created by tim on 5/24/15.
 */
public class iMatServiceResponse
{
    List<iMatShipmentResponse> services;

    public iMatServiceResponse()
    {

    }

    public List<iMatShipmentResponse> getServices() {
        return services;
    }

    public void setServices(List<iMatShipmentResponse> services) {
        this.services = services;
    }
}
