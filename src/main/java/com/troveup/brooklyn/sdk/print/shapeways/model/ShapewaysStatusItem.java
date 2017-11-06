package com.troveup.brooklyn.sdk.print.shapeways.model;

/**
 * Created by tim on 7/22/15.
 */
public class ShapewaysStatusItem
{
    private Long id;
    private String title;
    private int quantity;
    private ShapewaysItemStatus status;

    public ShapewaysStatusItem()
    {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ShapewaysItemStatus getStatus() {
        return status;
    }

    public void setStatus(ShapewaysItemStatus status) {
        this.status = status;
    }
}
