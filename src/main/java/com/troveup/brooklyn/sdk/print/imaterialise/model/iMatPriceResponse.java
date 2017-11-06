package com.troveup.brooklyn.sdk.print.imaterialise.model;

import java.util.List;

/**
 * Created by tim on 5/24/15.
 */
public class iMatPriceResponse
{

    List<iMatModel> models;

    String disclaimer;

    iMatServiceResponse shipmentCost;

    String currency;

    String requestError;

    public iMatPriceResponse()
    {

    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public iMatServiceResponse getShipmentCost() {
        return shipmentCost;
    }

    public void setShipmentCost(iMatServiceResponse shipmentCost) {
        this.shipmentCost = shipmentCost;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRequestError() {
        return requestError;
    }

    public void setRequestError(String requestError) {
        this.requestError = requestError;
    }

    public List<iMatModel> getModels() {
        return models;
    }

    public void setModels(List<iMatModel> models) {
        this.models = models;
    }
}
