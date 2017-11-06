package com.troveup.brooklyn.orm.cart.model;

/**
 * Created by tim on 11/11/15.
 */

import javax.jdo.annotations.*;

/**
 * Hook attributes that a given generic item hook can use for data manipulation
 */
@PersistenceCapable
@Cacheable(value = "false")
public class GenericItemDisplayAttribute
{

    private Long displayAttributeId;
    private String key;
    private String value;

    public GenericItemDisplayAttribute()
    {

    }

    public GenericItemDisplayAttribute(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getDisplayAttributeId() {
        return displayAttributeId;
    }

    public void setDisplayAttributeId(Long displayAttributeId) {
        this.displayAttributeId = displayAttributeId;
    }

    @Persistent
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Persistent
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
