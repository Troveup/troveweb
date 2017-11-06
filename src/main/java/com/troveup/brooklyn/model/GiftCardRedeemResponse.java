package com.troveup.brooklyn.model;

import com.troveup.brooklyn.util.MoneyUtil;

import java.math.BigDecimal;

/**
 * Created by tim on 11/15/15.
 */
public class GiftCardRedeemResponse
{
    private BigDecimal giftCardAmount;
    private BigDecimal storeCreditBalance;
    Boolean success = true;
    String redemptionError;

    public GiftCardRedeemResponse(String redemptionError)
    {
        success = false;
        this.redemptionError = redemptionError;
    }

    public GiftCardRedeemResponse(BigDecimal giftCardAmount, BigDecimal storeCreditBalance)
    {
        this.giftCardAmount = MoneyUtil.toProperScale(giftCardAmount, null);
        this.storeCreditBalance = MoneyUtil.toProperScale(storeCreditBalance, null);
        success = true;
        redemptionError = "";
    }

    public BigDecimal getGiftCardAmount() {
        return giftCardAmount;
    }

    public void setGiftCardAmount(BigDecimal giftCardAmount) {
        this.giftCardAmount = giftCardAmount;
    }

    public BigDecimal getStoreCreditBalance() {
        return storeCreditBalance;
    }

    public void setStoreCreditBalance(BigDecimal storeCreditBalance) {
        this.storeCreditBalance = storeCreditBalance;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getRedemptionError() {
        return redemptionError;
    }

    public void setRedemptionError(String redemptionError) {
        this.redemptionError = redemptionError;
    }
}
