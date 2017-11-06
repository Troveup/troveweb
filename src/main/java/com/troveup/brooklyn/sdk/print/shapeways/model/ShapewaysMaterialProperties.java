package com.troveup.brooklyn.sdk.print.shapeways.model;

import java.util.List;

/**
 * Created by tim on 7/1/15.
 */
public class ShapewaysMaterialProperties
{
    private ShapewaysMaterialProperty title;
    private ShapewaysMaterialProperty supportsColorFiles;
    private ShapewaysMaterialProperty printerId;
    private ShapewaysMaterialProperty swatch;
    private List<ShapewaysRestriction> restrictions;

    public ShapewaysMaterialProperties()
    {

    }

    public ShapewaysMaterialProperty getTitle() {
        return title;
    }

    public void setTitle(ShapewaysMaterialProperty title) {
        this.title = title;
    }

    public ShapewaysMaterialProperty getSupportsColorFiles() {
        return supportsColorFiles;
    }

    public void setSupportsColorFiles(ShapewaysMaterialProperty supportsColorFiles) {
        this.supportsColorFiles = supportsColorFiles;
    }

    public ShapewaysMaterialProperty getPrinterId() {
        return printerId;
    }

    public void setPrinterId(ShapewaysMaterialProperty printerId) {
        this.printerId = printerId;
    }

    public ShapewaysMaterialProperty getSwatch() {
        return swatch;
    }

    public void setSwatch(ShapewaysMaterialProperty swatch) {
        this.swatch = swatch;
    }

    public List<ShapewaysRestriction> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(List<ShapewaysRestriction> restrictions) {
        this.restrictions = restrictions;
    }
}
