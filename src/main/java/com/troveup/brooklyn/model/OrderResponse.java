package com.troveup.brooklyn.model;

import com.troveup.brooklyn.orm.order.model.Order;

import java.util.List;

/**
 * Created by tim on 8/21/15.
 */
public class OrderResponse extends GenericAjaxResponse
{
    private List<Order> orders;

    public OrderResponse()
    {

    }

    public OrderResponse(Boolean isSuccess, String errorMessage)
    {
        super.isSuccess = isSuccess;
        super.errorMessage = errorMessage;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
