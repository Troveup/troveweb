package com.troveup.brooklyn.model;

import com.troveup.brooklyn.orm.order.model.EasyPostPostageLabel;

/**
 * Created by tim on 4/27/16.
 */
public class ShippingLabelResponse extends GenericAjaxResponse
{
    private EasyPostPostageLabel label;

    public ShippingLabelResponse() {
        this.setIsSuccess(false);
        this.setErrorMessage("Oops!  There was a problem with your request.  Please try again.");
    }

    public EasyPostPostageLabel getLabel() {
        return label;
    }

    public void setLabel(EasyPostPostageLabel label) {
        this.label = label;
    }
}
