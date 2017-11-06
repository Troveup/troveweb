package com.troveup.brooklyn.model;

import com.troveup.brooklyn.orm.cart.model.PromoCode;

/**
 * Created by tim on 6/14/15.
 */
public class PromoCodeResponse
{
    private String promoCode;
    private String description;

    public PromoCodeResponse()
    {

    }

    public PromoCodeResponse(PromoCode code)
    {
        this.promoCode = code.getPromoCode();
        this.description = code.getPromoCodeDescription();
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
