package com.troveup.brooklyn.orm.simpleitem.model;

/**
 * Created by tim on 4/7/16.
 */
public class ManufacturingStatusOption
{
    public ManufacturingStatusOption()
    {

    }

    public ManufacturingStatusOption(SimpleItem.MANUFACTURING_STATUS stringStatus, String stringValue, Boolean selected)
    {
        this.status = stringStatus;
        this.stringValue = stringValue;
        this.selected = selected;
        this.stringStatusHumanReadable = stringStatus.toString();
    }

    private SimpleItem.MANUFACTURING_STATUS status;
    private String stringStatusHumanReadable;
    private String stringValue;
    private Boolean selected;

    public SimpleItem.MANUFACTURING_STATUS getStatus() {
        return status;
    }

    public void setStatus(SimpleItem.MANUFACTURING_STATUS status) {
        this.status = status;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getStringStatusHumanReadable() {
        return stringStatusHumanReadable;
    }

    public void setStringStatusHumanReadable(String stringStatusHumanReadable) {
        this.stringStatusHumanReadable = stringStatusHumanReadable;
    }
}
