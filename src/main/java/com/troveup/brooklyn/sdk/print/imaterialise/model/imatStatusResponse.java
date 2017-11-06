package com.troveup.brooklyn.sdk.print.imaterialise.model;

import java.util.List;

/**
 * Created by tim on 6/10/15.
 */
public class imatStatusResponse
{
    List<imatStatusOrderResponse> orders;

    private imatStatusResponse requestError;

    public imatStatusResponse()
    {

    }

    public List<imatStatusOrderResponse> getOrders() {
        return orders;
    }

    public void setOrders(List<imatStatusOrderResponse> orders) {
        this.orders = orders;
    }

    public imatStatusResponse getRequestError() {
        return requestError;
    }

    public void setRequestError(imatStatusResponse requestError) {
        this.requestError = requestError;
    }
}
