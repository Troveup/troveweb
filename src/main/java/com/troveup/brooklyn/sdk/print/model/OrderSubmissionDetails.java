package com.troveup.brooklyn.sdk.print.model;

import java.util.List;

/**
 * Created by tim on 5/22/15.
 */
public abstract class OrderSubmissionDetails
{
    List<OrderSubmissionDetailsAttribute> attributeList;

    public OrderSubmissionDetails()
    {

    }

    public List<OrderSubmissionDetailsAttribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<OrderSubmissionDetailsAttribute> attributeList) {
        this.attributeList = attributeList;
    }
}
