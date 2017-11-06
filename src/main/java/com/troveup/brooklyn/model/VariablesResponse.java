package com.troveup.brooklyn.model;

import com.troveup.brooklyn.util.MoneyUtil;

import java.math.BigDecimal;

/**
 * Created by tim on 11/22/15.
 */
public class VariablesResponse
{
    private String markupVal;
    private String packagingVal;
    private String shippingVal;
    private String prototypeVal;

    public VariablesResponse()
    {

    }

    public VariablesResponse(BigDecimal markupVal, BigDecimal packagingVal, BigDecimal shippingVal, BigDecimal prototypeVal)
    {
        if (markupVal == null) {
            this.markupVal = ".00";
        } else {
            this.markupVal = markupVal.toString();
        }

        if (packagingVal == null) {
            this.packagingVal = "0.00";
        }
        else {
            this.packagingVal = MoneyUtil.toProperScale(packagingVal, null).toString();
        }

        if (shippingVal == null) {
            this.shippingVal = "0.00";
        }
        else {
            this.shippingVal = MoneyUtil.toProperScale(shippingVal, null).toString();
        }

        if (prototypeVal == null) {
            this.prototypeVal = "0.00";
        } else {
            this.prototypeVal = MoneyUtil.toProperScale(prototypeVal, null).toString();
        }
    }

    public String getMarkupVal() {
        return markupVal;
    }

    public void setMarkupVal(String markupVal) {
        this.markupVal = markupVal;
    }

    public String getPackagingVal() {
        return packagingVal;
    }

    public void setPackagingVal(String packagingVal) {
        this.packagingVal = packagingVal;
    }

    public String getShippingVal() {
        return shippingVal;
    }

    public void setShippingVal(String shippingVal) {
        this.shippingVal = shippingVal;
    }

    public String getPrototypeVal() {
        return prototypeVal;
    }

    public void setPrototypeVal(String prototypeVal) {
        this.prototypeVal = prototypeVal;
    }
}
