package com.troveup.brooklyn.orm.cart.model;

import com.troveup.brooklyn.orm.user.model.User;

import javax.jdo.annotations.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 5/12/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class PromoCode
{
    public enum DISCOUNT_CATEGORY {
        TAX("tax"),
        SHIPPING("shipping"),
        SUB_TOTAL("subTotal"),
        GRAND_TOTAL("grandTotal");

        private final String text;

        DISCOUNT_CATEGORY(String type) {
            this.text = type;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum DISCOUNT_ITEM_CATEGORY {
        MATERIAL("material"),
        MATERIAL_AND_FINISH("materialfinish"),
        CATEGORY("category");

        private final String text;

        DISCOUNT_ITEM_CATEGORY(String type) {
            this.text = type;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    Long promoCodeId;
    String promoCode;
    int availableUses;
    private double percentDiscount;

    //Designates this code as a control code reference (one that contains all of the rules of the code, like whether
    //it's global or user-gated, number of uses remaining, etc) and not a code associated with an order that
    //was applied with a reference to the control reference
    private Boolean controlReference;

    //Whether or not this is a global promo code or a user-attached one
    private Boolean global;
    private BigDecimal dollarDiscount;
    private String blanketCategory;
    private String itemCategory;
    private String materialId;
    private String finishId;

    //True if this should be directly applied to a high-level category (i.e. shipping, tax, subtotal, or grand total),
    // or a specific item category, material id, and finish id
    private Boolean blanketApplication;
    private Date expires;

    //Whether this promo code is unlimited or not
    private Boolean unlimited;
    private boolean active;
    private String promoCodeDescription;

    //Reference code object that was used in the consumption of the applied code
    //(e.g. this code was invalidated on the user side because a code was applied on the order)
    private PromoCode consumedCode;

    //User reference for user level promo codes
    private User userLevelAttachment;

    private BigDecimal priceDelta;

    private Boolean doesExpire;

    //Used to associate a user with this code.  Currently relates to the referral system.
    private User associatedUser;

    private String purpose;

    private Integer characterLength;

    public PromoCode()
    {

    }

    public PromoCode(PromoCode promoCode)
    {
        this.promoCode = promoCode.getPromoCode();
        this.availableUses = promoCode.getAvailableUses();
        this.percentDiscount = promoCode.getPercentDiscount();
        this.global = promoCode.getGlobal();
        this.dollarDiscount = promoCode.getDollarDiscount();
        this.blanketApplication = promoCode.getBlanketApplication();
        this.expires = promoCode.getExpires();
        this.unlimited = promoCode.getUnlimited();
        this.active = true;
        this.controlReference = true;
        this.global = true;
        this.promoCodeDescription = promoCode.getPromoCodeDescription();
        this.consumedCode = promoCode.getConsumedCode();
        this.userLevelAttachment = promoCode.getUserLevelAttachment();
        this.priceDelta = promoCode.getPriceDelta();
        this.blanketCategory = promoCode.getBlanketCategory();
        this.doesExpire = promoCode.getDoesExpire();
        this.purpose = promoCode.getPurpose();
    }

    public PromoCode(Long promoCodeId)
    {
        this.promoCodeId = promoCodeId;
    }

    public PromoCode(String promoCode, int availableUses, double percentDiscount, BigDecimal dollarDiscount,
                     String blanketCategory, Boolean blanketApplication, String itemCategory,
                     String materialId, String finishId)
    {
        this.promoCode = promoCode;
        this.availableUses = availableUses;
        this.percentDiscount = percentDiscount;
        this.dollarDiscount = dollarDiscount;
        this.blanketCategory = blanketCategory;
        this.blanketApplication = blanketApplication;
        this.itemCategory = itemCategory;
        this.materialId = materialId;
        this.finishId = finishId;
        this.blanketApplication = blanketApplication;
    }

    public static PromoCode getAssociativeCode(PromoCode code)
    {
        PromoCode rval = new PromoCode();
        rval.promoCode = code.getPromoCode();
        rval.promoCodeDescription = code.getPromoCodeDescription();
        rval.consumedCode = code;
        rval.controlReference = false;
        rval.active = false;
        rval.blanketApplication = false;
        rval.blanketCategory = code.getBlanketCategory();

        return rval;
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getPromoCodeId() {
        return promoCodeId;
    }

    public void setPromoCodeId(Long promoCodeId) {
        this.promoCodeId = promoCodeId;
    }

    @Persistent
    @Column(name = "promo_code", allowsNull = "false")
    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    @Persistent
    @Column(name = "available_uses", allowsNull = "true")
    public int getAvailableUses() {
        return availableUses;
    }

    public void setAvailableUses(int availableUses) {
        this.availableUses = availableUses;
    }

    @Persistent
    @Column(name = "percent_discount", allowsNull = "true")
    public double getPercentDiscount() {
        return percentDiscount;
    }

    public void setPercentDiscount(double percentDiscount) {
        this.percentDiscount = percentDiscount;
    }

    @Persistent
    @Column(name = "dollar_discount", allowsNull = "true", scale = 4)
    public BigDecimal getDollarDiscount() {
        return dollarDiscount;
    }

    public void setDollarDiscount(BigDecimal dollarDiscount) {
        this.dollarDiscount = dollarDiscount;
    }

    @Persistent
    @Column(name = "blanket_category", allowsNull = "true")
    public String getBlanketCategory()
    {
        return blanketCategory;
    }

    public void setBlanketCategory(String blanketCategory)
    {
        this.blanketCategory = blanketCategory;
    }

    @Persistent
    @Column(name = "active", allowsNull = "true")
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Persistent
    @Column(name = "item_category", allowsNull = "true")
    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    @Persistent
    @Column(name = "material_id", allowsNull = "true")
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    @Persistent
    @Column(name = "finish_id", allowsNull = "true")
    public String getFinishId() {
        return finishId;
    }

    public void setFinishId(String finishId) {
        this.finishId = finishId;
    }

    @Persistent
    @Column(name = "blanket_application", allowsNull = "true")
    public Boolean getBlanketApplication() {
        return blanketApplication;
    }

    public void setBlanketApplication(Boolean blanketApplication) {
        this.blanketApplication = blanketApplication;
    }

    @Persistent
    @Column(name = "expires", allowsNull = "true")
    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    @Persistent
    @Column(name = "unlimited", allowsNull = "true")
    public Boolean getUnlimited() {
        return unlimited;
    }

    public void setUnlimited(Boolean unlimited) {
        this.unlimited = unlimited;
    }

    @Persistent
    @Column(name = "description", allowsNull = "true", length = 5000)
    public String getPromoCodeDescription() {
        return promoCodeDescription;
    }

    public void setPromoCodeDescription(String promoCodeDescription) {
        this.promoCodeDescription = promoCodeDescription;
    }

    @Persistent
    @Column(name = "global", allowsNull = "true")
    public Boolean getGlobal() {
        return global;
    }

    public void setGlobal(Boolean global) {
        this.global = global;
    }

    @Persistent
    @Column(name = "invalidated_code", allowsNull = "true")
    public PromoCode getConsumedCode() {
        return consumedCode;
    }

    public void setConsumedCode(PromoCode consumedCode) {
        this.consumedCode = consumedCode;
    }

    @Persistent
    @Column(name = "user_owner", allowsNull = "true")
    public User getUserLevelAttachment() {
        return userLevelAttachment;
    }

    public void setUserLevelAttachment(User userLevelAttachment) {
        this.userLevelAttachment = userLevelAttachment;
    }

    @Persistent
    @Column(name = "control_reference", allowsNull = "true")
    public Boolean getControlReference() {
        return controlReference;
    }

    @Persistent
    @Column(name = "price_delta", allowsNull = "true", scale = 4)
    public BigDecimal getPriceDelta() {
        return priceDelta;
    }

    public void setPriceDelta(BigDecimal priceDelta) {
        this.priceDelta = priceDelta;
    }

    public void setControlReference(Boolean controlReference) {
        this.controlReference = controlReference;
    }

    @Persistent
    public Boolean getDoesExpire() {
        return doesExpire;
    }

    public void setDoesExpire(Boolean doesExpire) {
        this.doesExpire = doesExpire;
    }

    @Persistent
    public User getAssociatedUser() {
        return associatedUser;
    }

    public void setAssociatedUser(User associatedUser) {
        this.associatedUser = associatedUser;
    }

    @Persistent
    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @NotPersistent
    public Integer getCharacterLength() {
        return characterLength;
    }

    public void setCharacterLength(Integer characterLength) {
        this.characterLength = characterLength;
    }

    /**
     * Helper method for generating a blanket promocode based on the required fields for blanket promocodes.
     *
     * @param promoCode The actual promotional code string
     * @param category The discount category for the blanket promo code.  Currently only shipping and subtotal are supported.
     * @param isUnlimited Boolean indicator as to whether or not this code has an unlimited number of uses.
     * @param numberOfUses Required only if isUnlimited is false.  Limits the number of uses for this code to the value
     *                     specified by this parameter.
     * @param doesExpire Boolean indicator as to whether or not this code expires after a certain date.
     * @param expireDate Required only if doesExpire is true.  Limits the usage of this code only up to the specified
     *                   date object.
     * @param amountDiscounted Double value indicating the amount discounted.
     * @param isPercentDiscount Boolean indicating whether or not this is a percent or flat dollar discount.  The
     *                          amount from the amountDiscounted parameter is the amount applied to either choice.
     * @param associatedUser Not required.  The user that was issued this promocode.
     * @return The preconfigured PromoCode object.
     */
    public static PromoCode buildBlanketPromoCode(String promoCode, DISCOUNT_CATEGORY category, Boolean isUnlimited,
                                                  Integer numberOfUses, Boolean doesExpire, Date expireDate,
                                                  Double amountDiscounted, Boolean isPercentDiscount, User associatedUser)
    {
        PromoCode rval = buildCommonPromoCode(promoCode, isUnlimited, numberOfUses, doesExpire, expireDate,
                amountDiscounted, isPercentDiscount, associatedUser);

        rval.setBlanketApplication(true);
        rval.setBlanketCategory(category.toString());

        return rval;
    }

    /**
     * Helper method for generating an item level promo code based on the required fields for an item level code.
     *
     * @param promoCode The actual promotional code string
     * @param category The discount category for the blanket promo code.
     * @param isUnlimited Boolean indicator as to whether or not this code has an unlimited number of uses.
     * @param numberOfUses Required only if isUnlimited is false.  Limits the number of uses for this code to the value
     *                     specified by this parameter.
     * @param doesExpire Boolean indicator as to whether or not this code expires after a certain date.
     * @param expireDate Required only if doesExpire is true.  Limits the usage of this code only up to the specified
     *                   date object.
     * @param amountDiscounted Double value indicating the amount discounted.
     * @param isPercentDiscount Boolean indicating whether or not this is a percent or flat dollar discount.  The amount
     *                          from the amountDiscounted parameter is the amount applied to either choice.
     * @param materialId Required only if the category param is MATERIAL or MATERIAL_AND_FINISH.  Will filter the promocode
     *                   to only work with a specific material.
     * @param finishId Required only if the category param is MATERIAL_AND_FINISH.  Will filter the promocode to only work
     *                 with a specific finish.
     * @param itemCategory Required only if the category param is CATEGORY.  String filter identifier that is attached
     *                     to ITEM objects to identify them by a base level category.  Currently BRACELET and RING exist,
     *                     but more may be added later.
     * @param associatedUser Not required.  The user that was issued this promocode.
     * @return The preconfigured PromoCode object.
     */
    public static PromoCode buildItemPromoCode(String promoCode, DISCOUNT_ITEM_CATEGORY category, Boolean isUnlimited,
                                               Integer numberOfUses, Boolean doesExpire, Date expireDate,
                                               Double amountDiscounted, Boolean isPercentDiscount, String materialId,
                                               String finishId, String itemCategory, User associatedUser)
    {
        PromoCode rval = buildCommonPromoCode(promoCode, isUnlimited, numberOfUses, doesExpire, expireDate,
                amountDiscounted, isPercentDiscount, associatedUser);

        rval.setBlanketApplication(false);

        if (category == DISCOUNT_ITEM_CATEGORY.MATERIAL)
        {
            rval.setMaterialId(materialId);
        }
        else if (category == DISCOUNT_ITEM_CATEGORY.MATERIAL_AND_FINISH)
        {
            rval.setFinishId(finishId);
        }
        else if (category == DISCOUNT_ITEM_CATEGORY.CATEGORY)
        {
            rval.setItemCategory(itemCategory);
        }

        return rval;
    }

    /**
     * Method for creating a promocode object with all of the variables that are common between ITEM and BLANKET level
     * promocodes.  Used as a helper method to create base promocodes with one call that may be later modified to be
     * either ITEM or BLANKET level.
     *
     * @param promoCode The actual promotional code string
     * @param isUnlimited Boolean indicator as to whether or not this code has an unlimited number of uses.
     * @param numberOfUses Required only if isUnlimited is false.  Limits the number of uses for this code to the value
     *                     specified by this parameter.
     * @param doesExpire Boolean indicator as to whether or not this code expires after a certain date.
     * @param expireDate Required only if doesExpire is true.  Limits the usage of this code only up to the specified
     *                   date object.
     * @param amountDiscounted Double value indicating the amount discounted.
     * @param isPercentDiscount Boolean indicating whether or not this is a percent or flat dollar discount.  The amount
     *                          from the amountDiscounted parameter is the amount applied to either choice.
     * @param associatedUser Not required.  The user that was issued this promocode.
     * @return The preconfigured PromoCode object.
     */
    public static PromoCode buildCommonPromoCode(String promoCode, Boolean isUnlimited, Integer numberOfUses,
                                                 Boolean doesExpire, Date expireDate, Double amountDiscounted,
                                                 Boolean isPercentDiscount, User associatedUser)
    {
        PromoCode rval = new PromoCode();

        rval.setActive(true);
        rval.setControlReference(true);
        if (promoCode != null)
            rval.setPromoCode(promoCode.toUpperCase());
        rval.setGlobal(true);

        rval.setUnlimited(isUnlimited);

        if (!isUnlimited)
        {
            rval.setAvailableUses(numberOfUses);
        }

        rval.setDoesExpire(doesExpire);

        if (doesExpire)
        {
            rval.setExpires(expireDate);
        }

        if (isPercentDiscount)
        {
            rval.setPercentDiscount(amountDiscounted);
        }
        else
        {
            rval.setDollarDiscount(new BigDecimal(amountDiscounted));
        }

        rval.setAssociatedUser(associatedUser);

        return rval;
    }

    public PromoCode updatePromoCode(PromoCode promoCode)
    {
        this.active = promoCode.active;
        this.promoCode = promoCode.promoCode;
        this.availableUses = promoCode.availableUses;
        this.percentDiscount = promoCode.percentDiscount;
        this.dollarDiscount = promoCode.dollarDiscount;
        this.blanketCategory = promoCode.blanketCategory;

        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PromoCode)
        {
            if (((PromoCode) obj).getPromoCode() != null)
                return ((PromoCode) obj).getPromoCode().equals(promoCode);
            else if (((PromoCode) obj).getPromoCodeId() != null)
                return ((PromoCode) obj).getPromoCodeId().equals(promoCodeId);
        }

        return false;
    }

    public static List<String> getPromoCodeFullFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("promoCode");
        rval.add("availableUses");
        rval.add("percentDiscount");
        rval.add("global");
        rval.add("dollarDiscount");
        rval.add("blanketCategory");
        rval.add("itemCategory");
        rval.add("materialId");
        rval.add("finishId");
        rval.add("blanketApplication");
        rval.add("expires");
        rval.add("unlimited");
        rval.add("active");
        rval.add("promoCodeDescription");


        return rval;
    }
}
