package com.troveup.brooklyn.orm.user.model;

import com.troveup.brooklyn.orm.item.model.Item;

import javax.jdo.annotations.*;
import java.util.Date;

/**
 * Created by tim on 5/22/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class UserAttribute
{
    public enum VALUE_TYPE
    {
        BOOLEAN,
        STRING,
        INT,
        FLOAT
    }

    private long attributeId;

    private String attributeName;

    private String attributeValue;

    private String attributeDescription;

    private String attributeCategory;

    private VALUE_TYPE attributeValueType;

    private User attributeOwner;

    private boolean valid;

    private Date created;

    public UserAttribute()
    {
        created = new Date();
    }

    public UserAttribute(UserAttribute attribute)
    {
        this.attributeName = attribute.getAttributeName();
        this.attributeValue = attribute.getAttributeValue();
        this.attributeDescription = attribute.getAttributeDescription();
        this.attributeValueType = attribute.getAttributeValueType();
        this.valid = attribute.isValid();
        this.created = attribute.getCreated();
    }

    public UserAttribute(String attributeName, String attributeValue, String attributeDescription,
                         VALUE_TYPE attributeValueType, boolean valid, Date created)
    {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.attributeDescription = attributeDescription;
        this.attributeValueType = attributeValueType;
        this.valid = valid;
        this.created = created;
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public long getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(long attributeId) {
        this.attributeId = attributeId;
    }

    @Persistent
    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    @Persistent
    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    @Persistent
    public String getAttributeDescription() {
        return attributeDescription;
    }

    public void setAttributeDescription(String attributeDescription) {
        this.attributeDescription = attributeDescription;
    }

    @Persistent
    public VALUE_TYPE getAttributeValueType() {
        return attributeValueType;
    }

    public void setAttributeValueType(VALUE_TYPE attributeValueType) {
        this.attributeValueType = attributeValueType;
    }

    public User getAttributeOwner() {
        return attributeOwner;
    }

    public void setAttributeOwner(User attributeOwner) {
        this.attributeOwner = attributeOwner;
    }

    @Persistent
    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Persistent
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Persistent
    public String getAttributeCategory() {
        return attributeCategory;
    }

    public void setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
    }
}
