package com.troveup.brooklyn.orm.cart.model;


import javax.jdo.annotations.*;
import java.util.*;

/**
 * Created by tim on 5/25/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class CartItemAttribute
{
    public enum ATTRIBUTE_TYPE
    {
        IMAT_CART_ITEM_REFERENCE("imatCartItemReference"),
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
        IMAT_CARTITEMID("cartItemId"),
        IMAT_ESTIMATED_PRICE("estimatedPrice"),
        OBJ_FILE_NAME("objFileName"),
        CARTITEM_UPLOAD_STATUS("uploadStatus"),
        CARTITEM_UPLOAD_STATUS_QUEUED("QUEUED_FOR_VOODOO"),
        CARTITEM_UPLOAD_STATUS_GENERATING("GENERATING"),
        CARTITEM_UPLOAD_STATUS_UPLOADING("UPLOADING"),
        CARTITEM_UPLOAD_STATUS_SENDING("SENDING"),
        CARTITEM_UPLOAD_STATUS_COMPLETE("SUBMITTED_TO_VOODOO"),
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

    public enum ATTRIBUTE_TYPE_VALUE_TYPE_MAP
    {
        IMAT_CART_ITEM_REFERENCE(VALUE_TYPE.STRING),
        IMAT_MODELID(VALUE_TYPE.STRING),
        IMAT_FILESCALEFACTOR(VALUE_TYPE.FLOAT),
        IMAT_UNITS(VALUE_TYPE.STRING),
        IMAT_MATERIALID(VALUE_TYPE.STRING),
        IMAT_FINISHID(VALUE_TYPE.STRING),
        IMAT_XDIM(VALUE_TYPE.FLOAT),
        IMAT_YDIM(VALUE_TYPE.FLOAT),
        IMAT_ZDIM(VALUE_TYPE.FLOAT),
        IMAT_VOLUME(VALUE_TYPE.FLOAT),
        IMAT_SURFACE(VALUE_TYPE.FLOAT),
        IMAT_APIPRICE(VALUE_TYPE.FLOAT),
        IMAT_MYSALESPRICE(VALUE_TYPE.FLOAT),
        IMAT_PRICE(VALUE_TYPE.FLOAT),
        IMAT_VALIDUNTIL(VALUE_TYPE.STRING),
        IMAT_CARTITEMID(VALUE_TYPE.STRING);

        private final VALUE_TYPE valueType;

        ATTRIBUTE_TYPE_VALUE_TYPE_MAP(VALUE_TYPE type) {
            this.valueType = type;
        }

        public VALUE_TYPE getValueType()
        {
            return valueType;
        }

        @Override
        public String toString()
        {
            return valueType.toString();
        }
    }

    public enum VALUE_TYPE
    {
        BOOLEAN("Boolean"),
        STRING("String"),
        INT("Integer"),
        LONG("Long"),
        DOUBLE("Double"),
        FLOAT("Float");

        private final String text;

        VALUE_TYPE(String text)
        {
            this.text = text;
        }

        @Override
        public String toString()
        {
            return text;
        }

    }

    private long attributeId;

    private String attributeName;

    private String attributeValue;

    private String attributeDescription;

    private String attributeCategory;

    private VALUE_TYPE attributeValueType;

    private CartItem attributeOwner;

    private boolean valid;

    private Date created;

    public CartItemAttribute()
    {
        created = new Date();
    }

    public CartItemAttribute(String attributeName, String attributeValue)
    {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    public CartItemAttribute(String attributeName, String attributeValue, String attributeDescription,
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

    public CartItem getAttributeOwner() {
        return attributeOwner;
    }

    public void setAttributeOwner(CartItem attributeOwner) {
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
    public boolean equals(Object obj)
    {
        if (obj instanceof CartItemAttribute)
            return ((CartItemAttribute) obj).getAttributeName().equals(attributeName);
        else
            return false;
    }

    public static CartItemAttribute getCartItemAttribute(String attribute, List<CartItemAttribute> attributes)
    {
        CartItemAttribute rval = null;

        CartItemAttribute attributeToSearch = new CartItemAttribute();
        attributeToSearch.setAttributeName(attribute);
        int attributeIndex = attributes.indexOf(attributeToSearch);

        if (attributeIndex > -1)
            rval = attributes.get(attributeIndex);

        return rval;
    }

    public static List<String> getCartItemAttributeFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("attributeId");
        rval.add("attributeName");
        rval.add("attributeValue");
        rval.add("attributeDescription");
        rval.add("attributeCategory");
        rval.add("attributeValueType");
        rval.add("attributeOwner");
        rval.add("valid");
        rval.add("created");

        return rval;
    }
}
