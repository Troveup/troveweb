package com.troveup.brooklyn.model;

import com.troveup.brooklyn.orm.cart.model.Cart;
import com.troveup.brooklyn.orm.cart.model.CartItem;
import com.troveup.brooklyn.orm.cart.model.PromoCode;
import com.troveup.brooklyn.orm.ftui.model.FtueCheckout;
import com.troveup.brooklyn.util.MoneyUtil;
import org.joda.money.Money;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 6/14/15.
 */
public class ChangePromoCodesResponse
{
    private String subTotal;
    private String tax;
    private String shipping;
    private String storeCredit;
    private String grandTotal;
    private Boolean giftwrapIncludedInSubtotal;

    private List<PromoCodeResponse> appliedCodes;
    private List<CartItem> modifiedCartItems;

    public ChangePromoCodesResponse()
    {
        appliedCodes = new ArrayList<>();
        modifiedCartItems = new ArrayList<>();
    }

    public ChangePromoCodesResponse(Money subTotal, Money tax, Money shipping, Money grandTotal,
                                    List<PromoCode> appliedCodes)
    {
        this.subTotal = subTotal.getAmount().toString();
        this.tax = tax.getAmount().toString();
        this.shipping = shipping.getAmount().toString();
        this.grandTotal = grandTotal.getAmount().toString();

        this.appliedCodes = new ArrayList<>();
        this.modifiedCartItems = new ArrayList<>();

        for (PromoCode code : appliedCodes)
        {
            appliedCodes.add(new PromoCode(code));
        }
    }

    public ChangePromoCodesResponse(Cart cart)
    {
        String zeroDollars = "0.00";
        if (cart.getSubTotal() != null)
            this.subTotal = MoneyUtil.toProperScale(cart.getSubTotal(), null).toString();
        else
            this.subTotal = zeroDollars;

        if (cart.getTax() != null)
            this.tax = MoneyUtil.toProperScale(cart.getTax(), null).toString();
        else
            this.tax = zeroDollars;

        if (cart.getShipping() != null)
            this.shipping = MoneyUtil.toProperScale(cart.getShipping(), null).toString();
        else
            this.shipping = zeroDollars;

        if (cart.getGrandTotal() != null)
            this.grandTotal = MoneyUtil.toProperScale(cart.getGrandTotal(), null).toString();
        else
            this.grandTotal = zeroDollars;

        if (cart.getStoreBalanceOffset() != null)
            this.storeCredit = MoneyUtil.toProperScale(cart.getStoreBalanceOffset(), null).toString();
        else
            this.storeCredit = "0.00";

        this.appliedCodes = new ArrayList<>();
        this.modifiedCartItems = new ArrayList<>();

        if (cart.getAppliedPromoCodes() != null) {
            for (PromoCode code : cart.getAppliedPromoCodes()) {
                appliedCodes.add(new PromoCodeResponse(code));
            }
        }

        if (cart.getCartItems() != null) {
            for (CartItem item : cart.getCartItems()) {
                if (item.getAppliedPromoCode() != null) {
                    appliedCodes.add(new PromoCodeResponse(item.getAppliedPromoCode()));
                    modifiedCartItems.add(item);
                }
            }
        }
    }

    public ChangePromoCodesResponse(FtueCheckout checkoutObject)
    {
        if(checkoutObject.getSubtotal() != null)
            this.subTotal = checkoutObject.getSubtotal().toString();

        if (checkoutObject.getTax() != null)
            this.tax = checkoutObject.getTax().toString();

        if (checkoutObject.getShipping() != null)
            this.shipping = checkoutObject.getShipping().toString();

        if (checkoutObject.getGrandtotal() != null)
            this.grandTotal = checkoutObject.getGrandtotal().toString();

        this.appliedCodes = new ArrayList<>();

        appliedCodes.add(new PromoCodeResponse(checkoutObject.getAppliedCode()));
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public List<PromoCodeResponse> getAppliedCodes() {
        return appliedCodes;
    }

    public void setAppliedCodes(List<PromoCodeResponse> appliedCodes) {
        this.appliedCodes = appliedCodes;
    }

    public List<CartItem> getModifiedCartItems() {
        return modifiedCartItems;
    }

    public void setModifiedCartItems(List<CartItem> modifiedCartItems) {
        this.modifiedCartItems = modifiedCartItems;
    }

    public String getStoreCredit() {
        return storeCredit;
    }

    public void setStoreCredit(String storeCredit) {
        this.storeCredit = storeCredit;
    }

    public Boolean getGiftwrapIncludedInSubtotal() {
        return giftwrapIncludedInSubtotal;
    }

    public void setGiftwrapIncludedInSubtotal(Boolean giftwrapIncludedInSubtotal) {
        this.giftwrapIncludedInSubtotal = giftwrapIncludedInSubtotal;
    }
}
