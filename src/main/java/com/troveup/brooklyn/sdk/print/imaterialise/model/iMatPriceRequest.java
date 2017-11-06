package com.troveup.brooklyn.sdk.print.imaterialise.model;

import com.troveup.brooklyn.sdk.print.model.PriceRequest;

import java.util.List;

/**
 * Created by tim on 5/24/15.
 */
public class iMatPriceRequest extends PriceRequest
{

    List<iMatModel> models;
    iMatShipmentInfo shipmentInfo;
    String currency;

    public iMatPriceRequest()
    {

    }

    public List<iMatModel> getModels() {
        return models;
    }

    public void setModels(List<iMatModel> models) {
        this.models = models;
    }

    public iMatShipmentInfo getShipmentInfo() {
        return shipmentInfo;
    }

    public void setShipmentInfo(iMatShipmentInfo shipmentInfo) {
        this.shipmentInfo = shipmentInfo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
