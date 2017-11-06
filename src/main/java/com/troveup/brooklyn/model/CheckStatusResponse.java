package com.troveup.brooklyn.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 6/19/15.
 */
public class CheckStatusResponse
{
    private List<CartItemStatus> statusList;

    public CheckStatusResponse()
    {
        statusList = new ArrayList<>();
    }

    public List<CartItemStatus> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<CartItemStatus> statusList) {
        this.statusList = statusList;
    }
}
