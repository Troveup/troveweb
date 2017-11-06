package com.troveup.brooklyn.orm.cart.model;

/**
 * Created by tim on 11/11/15.
 */

import javax.jdo.annotations.*;
import java.util.List;

/**
 * Hook attributes that a given generic item hook can use for data manipulation
 */
@PersistenceCapable
@Cacheable(value = "false")
public class HookAttribute
{

    private Long hookAttributeId;
    private String key;
    private String value;

    public HookAttribute()
    {

    }

    public HookAttribute(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getHookAttributeId() {
        return hookAttributeId;
    }

    public void setHookAttributeId(Long hookAttributeId) {
        this.hookAttributeId = hookAttributeId;
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

    @Override
    public boolean equals(Object obj)
    {
        Boolean rval = false;

        if (obj != null) {
            //Check if this attribute contains a matching key if a String is provided
            if (obj instanceof String) {
                rval = ((String) obj).equals(this.key);
            }
            //Otherwise check if this attribute contains a matching key if the HookAttribute is provided
            else if (obj instanceof HookAttribute && ((HookAttribute) obj).key != null) {
                rval = ((HookAttribute) obj).getKey().equals(this.key);
            }
        }

        return rval;
    }

    //TODO:  Investigate why this happens when everything isn't on fire.
    //For some reason, list index searching doesn't access the overridden equals.  Work around that..
    public static Integer getIndex(List<HookAttribute> hookAttributes, String key)
    {
        Integer rval = -1;

        for (int i = 0; i < hookAttributes.size(); ++i)
        {
            if (hookAttributes.get(i).key.equals(key))
            {
                rval = i;
            }
        }

        return rval;
    }
}
