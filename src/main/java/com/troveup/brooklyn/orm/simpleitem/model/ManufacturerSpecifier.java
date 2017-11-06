package com.troveup.brooklyn.orm.simpleitem.model;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 4/1/16.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class ManufacturerSpecifier
{
    public enum SPECIFIER_TYPE {
        MATERIAL,
        ENGRAVING,
        SIZE
    }

    public enum SPECIFIER_OPTION {
        GOLD_PLATED,
        SILVER,
        ROSE_GOLD_PLATED
    }

    private Long specifierId;
    private SPECIFIER_TYPE type;
    private SPECIFIER_OPTION option;

    public ManufacturerSpecifier()
    {

    }

    public ManufacturerSpecifier(SPECIFIER_TYPE type)
    {
        this.type = type;
    }

    public ManufacturerSpecifier(SPECIFIER_TYPE type, SPECIFIER_OPTION option)
    {
        this.type = type;
        this.option = option;
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getSpecifierId() {
        return specifierId;
    }

    public void setSpecifierId(Long specifierId) {
        this.specifierId = specifierId;
    }

    @Persistent
    public SPECIFIER_TYPE getType() {
        return type;
    }

    public void setType(SPECIFIER_TYPE type) {
        this.type = type;
    }

    @Persistent
    public SPECIFIER_OPTION getOption() {
        return option;
    }

    public void setOption(SPECIFIER_OPTION option) {
        this.option = option;
    }

    public static List<String> getAllManufacturerSpecifierFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("type");
        rval.add("option");

        return rval;
    }
}
