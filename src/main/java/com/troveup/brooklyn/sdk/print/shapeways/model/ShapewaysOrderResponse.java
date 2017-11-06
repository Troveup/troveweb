package com.troveup.brooklyn.sdk.print.shapeways.model;

import java.util.List;

/**
 * Created by tim on 7/22/15.
 */
public class ShapewaysOrderResponse
{
    private String result;
    private Long orderId;
    private List<String> productionOrderIds;

    public ShapewaysOrderResponse()
    {

    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<String> getProductionOrderIds() {
        return productionOrderIds;
    }

    public void setProductionOrderIds(List<String> productionOrderIds) {
        this.productionOrderIds = productionOrderIds;
    }
}
