package com.troveup.brooklyn.orm.cart.model;

import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.util.MoneyUtil;

import javax.jdo.annotations.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 11/11/15.
 */

@PersistenceCapable
@Cacheable(value = "false")
public class GiftCard
{
    public static String HOOK_ATTRIBUTE_IS_DIGITAL = "IS_DIGITAL";
    public static String HOOK_ATTRIBUTE_CARD_VALUE = "CARD_VALUE";
    public static String HOOK_ATTRIBUTE_SEND_DATE = "SEND_DATE";
    public static String HOOK_ATTRIBUTE_TO_NAME = "TO_NAME";
    public static String HOOK_ATTRIBUTE_FROM_NAME = "FROM_NAME";
    public static String HOOK_ATTRIBUTE_TO_EMAIL = "TO_EMAIL";
    public static String HOOK_ATTRIBUTE_FROM_EMAIL = "FROM_EMAIL";

    private Long giftCardId;
    private String giftCardString;
    private Boolean digital;
    private Date emailDate;
    private BigDecimal amount;
    private String toName;
    private String toEmail;
    private String fromName;
    private String fromEmail;
    private GenericItem genericItemReference;
    private Boolean emailSent;
    private Date emailSentDate;
    private Boolean redeemed;
    private User redeemer;

    public GiftCard()
    {
        this.emailSent = false;
        this.redeemed = false;
        this.redeemer = null;
    }

    public GiftCard(GenericItem genericItem) throws ParseException {
        List<HookAttribute> attributeList = genericItem.getHookAttributes();

        int index = HookAttribute.getIndex(attributeList, HOOK_ATTRIBUTE_IS_DIGITAL);
        if (index > -1)
            digital = Boolean.parseBoolean(attributeList.get(index).getValue());

        index = HookAttribute.getIndex(attributeList, HOOK_ATTRIBUTE_CARD_VALUE);
        if (index > -1)
            amount = MoneyUtil.floatToBigDecimal(Float.parseFloat(attributeList.get(index).getValue()), null);

        index = HookAttribute.getIndex(attributeList, HOOK_ATTRIBUTE_SEND_DATE);
        if (index > -1)
        {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            emailDate = formatter.parse(attributeList.get(index).getValue());
        }

        index = HookAttribute.getIndex(attributeList, HOOK_ATTRIBUTE_TO_NAME);
        if (index > -1)
            toName = attributeList.get(index).getValue();

        index = HookAttribute.getIndex(attributeList, HOOK_ATTRIBUTE_FROM_NAME);
        if (index > -1)
            fromName = attributeList.get(index).getValue();

        index = HookAttribute.getIndex(attributeList, HOOK_ATTRIBUTE_TO_EMAIL);
        if (index > -1)
            toEmail = attributeList.get(index).getValue();

        index = HookAttribute.getIndex(attributeList, HOOK_ATTRIBUTE_FROM_EMAIL);
        if (index > -1)
            fromEmail = attributeList.get(index).getValue();

        this.genericItemReference = genericItem;

        this.emailSent = false;
        this.redeemed = false;
        this.redeemer = null;

    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getGiftCardId() {
        return giftCardId;
    }

    public void setGiftCardId(Long giftCardId) {
        this.giftCardId = giftCardId;
    }

    @Persistent
    public String getGiftCardString() {
        return giftCardString;
    }

    public void setGiftCardString(String giftCardString) {
        this.giftCardString = giftCardString;
    }

    @Persistent
    public Boolean getDigital() {
        return digital;
    }

    public void setDigital(Boolean digital) {
        this.digital = digital;
    }

    @Persistent
    public Date getEmailDate() {
        return emailDate;
    }

    public void setEmailDate(Date emailDate) {
        this.emailDate = emailDate;
    }

    @Persistent
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Persistent
    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    @Persistent
    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    @Persistent
    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    @Persistent
    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    @Persistent
    public GenericItem getGenericItemReference() {
        return genericItemReference;
    }

    public void setGenericItemReference(GenericItem genericItemReference) {
        this.genericItemReference = genericItemReference;
    }

    @Persistent
    public Boolean getEmailSent() {
        return emailSent;
    }

    public void setEmailSent(Boolean emailSent) {
        this.emailSent = emailSent;
    }

    @Persistent
    public Date getEmailSentDate() {
        return emailSentDate;
    }

    public void setEmailSentDate(Date emailSentDate) {
        this.emailSentDate = emailSentDate;
    }

    @Persistent
    public Boolean getRedeemed() {
        return redeemed;
    }

    public void setRedeemed(Boolean redeemed) {
        this.redeemed = redeemed;
    }

    @Persistent
    public User getRedeemer() {
        return redeemer;
    }

    public void setRedeemer(User redeemer) {
        this.redeemer = redeemer;
    }

    public static Boolean isGenericItemADigitalGiftCard(GenericItem item)
    {
        return item.getItemType().equals(GenericItem.ITEM_TYPE.GIFT_CARD) && HookAttribute.getIndex(item.getHookAttributes(), HOOK_ATTRIBUTE_IS_DIGITAL) > -1;
    }
}
