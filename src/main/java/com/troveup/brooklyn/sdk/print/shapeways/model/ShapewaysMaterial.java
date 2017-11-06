package com.troveup.brooklyn.sdk.print.shapeways.model;

import java.util.List;

/**
 * Created by tim on 7/1/15.
 */
public class ShapewaysMaterial
{
    private String materialId;
    private String title;
    private String supportsColorFiles;
    private String printerId;
    private String swatch;
    private List<ShapewaysRestriction> restrictions;

    public ShapewaysMaterial()
    {

    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSupportsColorFiles() {
        return supportsColorFiles;
    }

    public void setSupportsColorFiles(String supportsColorFiles) {
        this.supportsColorFiles = supportsColorFiles;
    }

    public String getPrinterId() {
        return printerId;
    }

    public void setPrinterId(String printerId) {
        this.printerId = printerId;
    }

    public String getSwatch() {
        return swatch;
    }

    public void setSwatch(String swatch) {
        this.swatch = swatch;
    }

    public List<ShapewaysRestriction> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(List<ShapewaysRestriction> restrictions) {
        this.restrictions = restrictions;
    }
}
