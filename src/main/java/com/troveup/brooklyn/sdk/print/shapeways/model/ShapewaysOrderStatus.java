package com.troveup.brooklyn.sdk.print.shapeways.model;

import java.util.List;

/**
 * Created by tim on 7/22/15.
 */
public class ShapewaysOrderStatus
{
    private Long id;
    private String status;
    private List<ShapewaysStatusItem> items;

    public ShapewaysOrderStatus()
    {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ShapewaysStatusItem> getItems() {
        return items;
    }

    public void setItems(List<ShapewaysStatusItem> items) {
        this.items = items;
    }
}
