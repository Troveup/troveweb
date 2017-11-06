package com.troveup.brooklyn.sdk.tax.interfaces;

import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.user.model.Address;

import java.util.List;

/**
 * Created by tim on 5/27/15.
 */
public interface ITaxHandler
{
    Object getTaxes(Long cartId, Boolean submitTax);

    Object getOrderTaxes(Order order, Long userId, Boolean submitTax);

    //Simple single-item tax request
    Object getTaxes(Address shippingAddress, String itemReferenceId, int quantity, Float price,
                           String userId, Boolean submitTax);
}
