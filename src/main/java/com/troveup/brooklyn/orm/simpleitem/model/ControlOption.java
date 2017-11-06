package com.troveup.brooklyn.orm.simpleitem.model;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 3/25/16.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class ControlOption
{
    private Long controlOptionId;
    private String optionDisplayName;
    private String optionValue;

    //For displaying things like gemstones on the dropdown option
    private String optionAssetUrl;
    //Images to make active on the view when a customization is selected
    private List<ControlOptionAsset> controlOptionAssets;

    private ManufacturerSpecifier manufacturerSpecifierOption;

    public ControlOption()
    {

    }

    public ControlOption(ControlOption option)
    {
        this.optionDisplayName = option.getOptionDisplayName();
        this.optionValue = option.getOptionValue();
        this.optionAssetUrl = option.getOptionAssetUrl();

        controlOptionAssets = new ArrayList<>();

        for(ControlOptionAsset asset : option.getControlOptionAssets())
        {
            controlOptionAssets.add(new ControlOptionAsset(asset));
        }

        this.manufacturerSpecifierOption = option.manufacturerSpecifierOption;
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getControlOptionId() {
        return controlOptionId;
    }

    public void setControlOptionId(Long controlOptionId) {
        this.controlOptionId = controlOptionId;
    }

    @Persistent
    public String getOptionDisplayName() {
        return optionDisplayName;
    }

    public void setOptionDisplayName(String optionDisplayName) {
        this.optionDisplayName = optionDisplayName;
    }

    @Persistent
    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    @Persistent
    public String getOptionAssetUrl() {
        return optionAssetUrl;
    }

    public void setOptionAssetUrl(String optionAssetUrl) {
        this.optionAssetUrl = optionAssetUrl;
    }

    @Persistent
    public List<ControlOptionAsset> getControlOptionAssets() {
        return controlOptionAssets;
    }

    public void setControlOptionAssets(List<ControlOptionAsset> controlOptionAssets) {
        this.controlOptionAssets = controlOptionAssets;
    }

    @Persistent(defaultFetchGroup="false")
    public ManufacturerSpecifier getManufacturerSpecifierOption() {
        return manufacturerSpecifierOption;
    }

    public void setManufacturerSpecifierOption(ManufacturerSpecifier manufacturerSpecifierOption) {
        this.manufacturerSpecifierOption = manufacturerSpecifierOption;
    }

    public static List<String> getAllFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("controlOptionAssets");
        rval.add("manufacturerSpecifierOption");

        return rval;
    }
}
