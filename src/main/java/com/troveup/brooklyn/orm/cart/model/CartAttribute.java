package com.troveup.brooklyn.orm.cart.model;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 5/26/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class CartAttribute
{
    public enum ATTRIBUTE_TYPE
    {
        IMAT_CART_ID("cartId"),
        IMAT_SHIPMENT_TYPE("shipmentType"),
        IMAT_MY_ORDER_REFERENCE("myOrderReference"),
        IMAT_MY_INVOICE_LINK("myInvoiceLink"),
        IMAT_MY_DELIVERY_NOTE_LINK("myDeliveryNoteLink"),
        IMAT_REMARKS("remarks"),
        AVA_TAX_ESTIMATION_ID("avaEstId");


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
        IMAT_SHIPMENT_TYPE(VALUE_TYPE.STRING),
        IMAT_MY_ORDER_REFERENCE(VALUE_TYPE.STRING),
        IMAT_MY_INVOICE_LINK(VALUE_TYPE.STRING),
        IMAT_MY_DELIVERY_NOTE_LINK(VALUE_TYPE.STRING),
        IMAT_REMARKS(VALUE_TYPE.STRING);

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

    private Cart attributeOwner;

    private boolean valid;

    private Date created;

    public CartAttribute()
    {
        created = new Date();
    }

    public CartAttribute(String attributeName, String attributeValue)
    {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    public CartAttribute(String attributeName, String attributeValue, String attributeDescription,
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

    public Cart getAttributeOwner() {
        return attributeOwner;
    }

    public void setAttributeOwner(Cart attributeOwner) {
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
        if (obj instanceof CartAttribute)
            return ((CartAttribute) obj).getAttributeName().equals(attributeName);
        else
            return false;
    }

    public static List<String> getCartAttributeFetchGroupFields()
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

    public static CartAttribute getCartAttribute(String attribute, List<CartAttribute> attributes)
    {
        CartAttribute rval = null;

        CartAttribute attributeToSearch = new CartAttribute();
        attributeToSearch.setAttributeName(attribute);
        int attributeIndex = attributes.indexOf(attributeToSearch);

        if (attributeIndex > -1)
            rval = attributes.get(attributeIndex);

        return rval;
    }
}
