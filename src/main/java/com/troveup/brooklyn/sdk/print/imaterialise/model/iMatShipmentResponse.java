package com.troveup.brooklyn.sdk.print.imaterialise.model;

/**
 * Created by tim on 5/24/15.
 */
public class iMatShipmentResponse
{
    private String name;
    private Float value;
    private int daysInTransit;
    private iMatResponseError shipmentError;

    public iMatShipmentResponse()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public int getDaysInTransit() {
        return daysInTransit;
    }

    public void setDaysInTransit(int daysInTransit) {
        this.daysInTransit = daysInTransit;
    }

    public iMatResponseError getShipmentError() {
        return shipmentError;
    }

    public void setShipmentError(iMatResponseError shipmentError) {
        this.shipmentError = shipmentError;
    }
}
