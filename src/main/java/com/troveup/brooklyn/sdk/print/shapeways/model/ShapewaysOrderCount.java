package com.troveup.brooklyn.sdk.print.shapeways.model;

/**
 * Created by tim on 7/22/15.
 */
public class ShapewaysOrderCount
{
    private int total;
    private int placed;
    private int in_production;
    private int cancelled;
    private int unknown;
    private int shipped;

    public ShapewaysOrderCount()
    {

    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPlaced() {
        return placed;
    }

    public void setPlaced(int placed) {
        this.placed = placed;
    }

    public int getIn_production() {
        return in_production;
    }

    public void setIn_production(int in_production) {
        this.in_production = in_production;
    }

    public int getCancelled() {
        return cancelled;
    }

    public void setCancelled(int cancelled) {
        this.cancelled = cancelled;
    }

    public int getUnknown() {
        return unknown;
    }

    public void setUnknown(int unknown) {
        this.unknown = unknown;
    }

    public int getShipped() {
        return shipped;
    }

    public void setShipped(int shipped) {
        this.shipped = shipped;
    }
}
