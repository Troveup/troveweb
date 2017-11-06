package com.troveup.brooklyn.sdk.meshexporter.java.model;

import java.util.List;

/**
 * Created by tim on 6/16/15.
 */
public class ModelJsonControl
{
    private String id;
    private String group;
    private String label;
    private List<String> maskedControls;

    public ModelJsonControl()
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getMaskedControls() {
        return maskedControls;
    }

    public void setMaskedControls(List<String> maskedControls) {
        this.maskedControls = maskedControls;
    }
}
