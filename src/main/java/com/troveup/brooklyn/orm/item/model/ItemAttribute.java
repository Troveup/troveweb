package com.troveup.brooklyn.orm.item.model;

import javax.jdo.annotations.*;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 4/13/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class ItemAttribute
{
    public enum ATTRIBUTE_TYPE {
        SLIDER("slider"),
        FINISH_ID("finishId"),
        MATERIAL_ID("materialId"),
        IMATERIALISE_MODEL_ID("iMatModelId"),
        IMATERIALISE_ESTIMATED_PRICE("iMatEstimatedPrice"),
        MODEL_WEIGHT_PREFIX("modelWeight-"),
        IMAT_MODELID("imatModelId"),
        IMAT_FILESCALEFACTOR("fileScaleFactor"),
        IMAT_UNITS("imatUnits"),
        IMAT_MATERIALID("materialId"),
        IMAT_FINISHID("finishId"),
        IMAT_XDIM("xDimMm"),
        IMAT_YDIM("yDimMm"),
        IMAT_ZDIM("zDimMm"),
        IMAT_VOLUME("volumeCm3"),
        IMAT_SURFACE("surfaceCm2"),
        IMAT_APIPRICE("iMatApiPrice"),
        IMAT_MYSALESPRICE("mySalesPrice"),
        IMAT_PRICE("iMatPrice"),
        IMAT_VALIDUNTIL("validUntil"),
        APPLIED_MODEL_WEIGHTS_FILENAME("appliedModelWeightsFileName"),
        LEGACY_CHECKOUT("legacyCheckout");


        private final String text;

        ATTRIBUTE_TYPE(String type) {
            this.text = type;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum VALUE_TYPE
    {
        BOOLEAN,
        STRING,
        INT,
        FLOAT
    }

    private Long attributeId;

    private String attributeName;

    private String attributeValue;

    private String attributeDescription;

    private String attributeCategory;

    private VALUE_TYPE attributeValueType;

    private Item attributeOwner;

    private boolean valid;

    private Date created;

    public ItemAttribute()
    {

    }

    public ItemAttribute(ItemAttribute attribute)
    {
        this.attributeName = attribute.getAttributeName();
        this.attributeValue = attribute.getAttributeValue();
        this.attributeDescription = attribute.getAttributeDescription();
        this.attributeValueType = attribute.getAttributeValueType();
        this.valid = attribute.isValid();
        this.created = attribute.getCreated();
    }

    public ItemAttribute(String attributeName, String attributeValue)
    {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    public ItemAttribute(String attributeName, String attributeValue, String attributeDescription,
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
    public Long getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Long attributeId) {
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

    public Item getAttributeOwner() {
        return attributeOwner;
    }

    public void setAttributeOwner(Item attributeOwner) {
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemAttribute)
        {
            if (((ItemAttribute) obj).getAttributeName() != null)
            {
                return ((ItemAttribute) obj).getAttributeName().equals(attributeName);
            }
            else if (((ItemAttribute) obj).getAttributeId() != null)
            {
                return (((ItemAttribute) obj).getAttributeId() == attributeId);
            }
        }

        return false;
    }

    public static ItemAttribute getItemAttribute(ATTRIBUTE_TYPE nameKey, List<ItemAttribute> attributes)
    {
        ItemAttribute rval = null;
        ItemAttribute searchAttribute = new ItemAttribute();
        searchAttribute.setAttributeName(nameKey.toString());

        int searchIndex = attributes.indexOf(searchAttribute);

        if (searchIndex > -1)
            rval = attributes.get(searchIndex);

        return rval;
    }
}
