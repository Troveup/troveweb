package com.troveup.brooklyn.sdk.print.shapeways.model;

/**
 * Created by tim on 7/22/15.
 */
public class ShapewaysItemStatus
{
    private int processing;
    private int in_production;
    private int complete;
    private int cancelled;

    public ShapewaysItemStatus()
    {

    }

    public int getProcessing() {
        return processing;
    }

    public void setProcessing(int processing) {
        this.processing = processing;
    }

    public int getIn_production() {
        return in_production;
    }

    public void setIn_production(int in_production) {
        this.in_production = in_production;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public int getCancelled() {
        return cancelled;
    }

    public void setCancelled(int cancelled) {
        this.cancelled = cancelled;
    }
}
