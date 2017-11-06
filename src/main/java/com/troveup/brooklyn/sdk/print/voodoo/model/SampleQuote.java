package com.troveup.brooklyn.sdk.print.voodoo.model;

import com.troveup.brooklyn.orm.ftui.model.FtueResponseQuote;
import com.troveup.brooklyn.util.MoneyUtil;

/**
 * Created by tim on 6/18/15.
 */
public class SampleQuote
{
    private Float items;
    private Float total;
    private Float tax;
    private Float shipping;

    public SampleQuote()
    {

    }

    public Float getItems() {
        return items;
    }

    public void setItems(Float items) {
        this.items = items;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Float getTax() {
        return tax;
    }

    public void setTax(Float tax) {
        this.tax = tax;
    }

    public Float getShipping() {
        return shipping;
    }

    public void setShipping(Float shipping) {
        this.shipping = shipping;
    }

    public FtueResponseQuote toFtueResponseQuote()
    {
        FtueResponseQuote rval = new FtueResponseQuote();
        rval.setTotal(MoneyUtil.floatToBigDecimal(total, null));
        rval.setTax(MoneyUtil.floatToBigDecimal(tax, null));
        rval.setShipping(MoneyUtil.floatToBigDecimal(shipping, null));
        rval.setItems(MoneyUtil.floatToBigDecimal(items, null));

        return rval;
    }
}
