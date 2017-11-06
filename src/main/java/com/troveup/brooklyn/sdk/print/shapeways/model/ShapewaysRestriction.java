package com.troveup.brooklyn.sdk.print.shapeways.model;

import java.util.List;

/**
 * Created by tim on 7/1/15.
 */
public class ShapewaysRestriction
{
    private String restrictionId;
    private String restrictionName;
    private List<String> restrictionEntityIds;

    public ShapewaysRestriction()
    {

    }

    public String getRestrictionId() {
        return restrictionId;
    }

    public void setRestrictionId(String restrictionId) {
        this.restrictionId = restrictionId;
    }

    public String getRestrictionName() {
        return restrictionName;
    }

    public void setRestrictionName(String restrictionName) {
        this.restrictionName = restrictionName;
    }

    public List<String> getRestrictionEntityIds() {
        return restrictionEntityIds;
    }

    public void setRestrictionEntityIds(List<String> restrictionEntityIds) {
        this.restrictionEntityIds = restrictionEntityIds;
    }
}
