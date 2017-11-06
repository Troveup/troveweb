package com.troveup.brooklyn.orm.simpleitem.model;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 3/25/16.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class SimpleItemControl
{
    public enum INTERACTION_TYPE {
        NO_INTERACTION,
        ASSET_CHANGE,
    }

    private Long controlId;

    private String controlName;
    private String controlSelectedName;
    //Selection options for customization
    private List<ControlOption> controlOptions;
    private INTERACTION_TYPE interactionType;
    private Boolean hidden;
    private ManufacturerSpecifier manufacturerSpecifier;

    public SimpleItemControl()
    {
        hidden = false;
        interactionType = INTERACTION_TYPE.NO_INTERACTION;
    }

    public SimpleItemControl(SimpleItemControl control)
    {
        this.controlName = control.getControlName();
        this.controlSelectedName = control.getControlSelectedName();

        this.controlOptions = new ArrayList<>();
        for (ControlOption option : control.getControlOptions())
        {
            controlOptions.add(new ControlOption(option));
        }

        this.interactionType = control.getInteractionType();
        this.hidden = control.getHidden();
        this.manufacturerSpecifier = control.getManufacturerSpecifier();
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getControlId() {
        return controlId;
    }

    public void setControlId(Long controlId) {
        this.controlId = controlId;
    }

    @Persistent
    public String getControlName() {
        return controlName;
    }

    public void setControlName(String controlName) {
        this.controlName = controlName;
    }

    @Persistent
    public List<ControlOption> getControlOptions() {
        return controlOptions;
    }

    public void setControlOptions(List<ControlOption> controlOptions) {
        this.controlOptions = controlOptions;
    }

    @Persistent
    public INTERACTION_TYPE getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(INTERACTION_TYPE interactionType) {
        this.interactionType = interactionType;
    }

    @Persistent
    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    @Persistent(defaultFetchGroup="false")
    public ManufacturerSpecifier getManufacturerSpecifier() {
        return manufacturerSpecifier;
    }

    public void setManufacturerSpecifier(ManufacturerSpecifier manufacturerSpecifier) {
        this.manufacturerSpecifier = manufacturerSpecifier;
    }

    @Persistent
    public String getControlSelectedName() {
        return controlSelectedName;
    }

    public void setControlSelectedName(String controlSelectedName) {
        this.controlSelectedName = controlSelectedName;
    }

    public static List<String> getAllFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("controlOptions");
        rval.add("interactionType");
        rval.add("manufacturerSpecifier");

        return rval;
    }
}
