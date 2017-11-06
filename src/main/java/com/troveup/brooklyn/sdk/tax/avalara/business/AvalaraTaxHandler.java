package com.troveup.brooklyn.sdk.tax.avalara.business;

import com.troveup.brooklyn.orm.cart.interfaces.ICartAccessor;
import com.troveup.brooklyn.orm.cart.model.Cart;
import com.troveup.brooklyn.orm.cart.model.CartAttribute;
import com.troveup.brooklyn.orm.cart.model.CartItem;
import com.troveup.brooklyn.orm.cart.model.GenericItem;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.order.interfaces.IOrderAccessor;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.user.interfaces.IUserAccessor;
import com.troveup.brooklyn.orm.user.model.Address;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.tax.avalara.api.AvaTax;
import com.troveup.brooklyn.sdk.tax.avalara.model.AvaAddress;
import com.troveup.brooklyn.sdk.tax.avalara.model.AvaGetTaxRequest;
import com.troveup.brooklyn.sdk.tax.avalara.model.AvaGetTaxResult;
import com.troveup.brooklyn.sdk.tax.avalara.model.AvaLine;
import com.troveup.brooklyn.sdk.tax.interfaces.ITaxHandler;
import com.troveup.brooklyn.util.MoneyUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by tim on 5/27/15.
 */
public class AvalaraTaxHandler implements ITaxHandler
{

    @Autowired
    ICartAccessor cartAccessor;

    @Autowired
    IOrderAccessor orderAccessor;

    @Autowired
    IUserAccessor userAccessor;

    @Autowired
    AvaTax avaTax;

    private String companyCode;

    public AvalaraTaxHandler(String companyCode)
    {
        this.companyCode = companyCode;
    }

    @Override
    public Object getTaxes(Long cartId, Boolean submitTax)
    {
        Cart cart = cartAccessor.getShoppingCart(cartId, IEnums.SEEK_MODE.FULL);
        List<CartItem> orderItems = cart.getCartItems();
        List<GenericItem> genericItems = cart.getGenericItems();
        List<AvaLine> lineItems = new ArrayList<>();
        List<AvaAddress> addresses = new ArrayList<>();

        //TODO:  Put this in a config file.
        //Set up the source address.  This is Trove.
        AvaAddress sourceAddress = new AvaAddress();
        sourceAddress.setLine1("48 Wall Street");
        sourceAddress.setLine2("Floor 5");
        sourceAddress.setRegion("NY");
        sourceAddress.setCity("New York");
        sourceAddress.setPostalCode("10005");
        sourceAddress.setAddressCode("0");
        addresses.add(sourceAddress);
        AvaAddress avaAddress = cart.getShippingAddress().toAvaAddress();
        avaAddress.setAddressCode("1");
        addresses.add(avaAddress);

        //Regular jewelry items
        for (int i = 0; i < orderItems.size(); ++i)
        {
            CartItem cartItem = orderItems.get(i);
            AvaLine line = new AvaLine();
            line.initializeForSimpleTaxRequest(String.valueOf(i), "0", "1",
                    cartItem.getCartItemReference().getItemId().toString(), cartItem.getQuantity(),
                    cartItem.getActualPrice().floatValue());
            lineItems.add(line);
        }

        //TODO:  Fix this so that tax exemption doesn't involve not including line items, but rather marking them as tax exempt in the API
        for (int i = 0; i < genericItems.size(); ++i)
        {
            GenericItem genericItem = genericItems.get(i);

            if (!genericItem.getTaxExempt()) {
                AvaLine line = new AvaLine();
                line.initializeForSimpleTaxRequest(String.valueOf(lineItems.size()), "0", "1",
                        genericItem.getGenericItemId().toString(), genericItem.getQuantity(),
                        genericItem.getPrice().floatValue());
                lineItems.add(line);
            }
        }

        String taxEstimationId = UUID.randomUUID().toString();
        cartAccessor.updateCartAttribute(
                cartId, new CartAttribute(CartAttribute.ATTRIBUTE_TYPE.AVA_TAX_ESTIMATION_ID.toString(),
                        taxEstimationId));

        AvaGetTaxRequest request = new AvaGetTaxRequest();

        AvaGetTaxRequest.DOC_TYPE docType;
        Boolean commit;

        if (submitTax)
        {
            docType = AvaGetTaxRequest.DOC_TYPE.SALES_INVOICE;
            commit = true;

        }
        else
        {
            docType = AvaGetTaxRequest.DOC_TYPE.SALES_ORDER;
            commit = false;
        }

        request.initializeForSimpleTaxRequest(companyCode, docType, taxEstimationId, new Date(),
                cart.getCartOwner().getUserId().toString(), lineItems, addresses, commit);


        return avaTax.getTax(request);
    }

    @Override
    public Object getOrderTaxes(Order order, Long userId, Boolean submitTax)
    {
        AvaGetTaxResult rval;

        List<CartItem> orderItems = order.getOrderItems();
        List<GenericItem> genericItems = order.getGenericItemsList();
        List<AvaLine> lineItems = new ArrayList<>();
        List<AvaAddress> addresses = new ArrayList<>();

        //TODO:  Put this in a config file.
        //Set up the source address.  This is Trove.
        AvaAddress sourceAddress = new AvaAddress();
        sourceAddress.setLine1("48 Wall Street");
        sourceAddress.setLine2("Floor 5");
        sourceAddress.setRegion("NY");
        sourceAddress.setCity("New York");
        sourceAddress.setPostalCode("10005");
        sourceAddress.setAddressCode("0");
        addresses.add(sourceAddress);
        AvaAddress avaAddress = order.getShippingAddress().toAvaAddress();
        avaAddress.setAddressCode("1");
        addresses.add(avaAddress);

        //Regular jewelry items
        for (int i = 0; i < orderItems.size(); ++i)
        {
            CartItem cartItem = orderItems.get(i);
            if (cartItem.getCartItemStatus() != CartItem.CART_ITEM_STATUS.CANCELLED) {
                AvaLine line = new AvaLine();
                line.initializeForSimpleTaxRequest(String.valueOf(i), "0", "1",
                        cartItem.getCartItemId().toString(), cartItem.getQuantity(),
                        cartItem.getActualPrice().floatValue());
                lineItems.add(line);
            }
        }

        //TODO:  Fix this so that tax exemption doesn't involve not including line items, but rather marking them as tax exempt in the API
        for (int i = 0; i < genericItems.size(); ++i)
        {
            GenericItem genericItem = genericItems.get(i);

            if (!genericItem.getTaxExempt()) {
                AvaLine line = new AvaLine();
                line.initializeForSimpleTaxRequest(String.valueOf(i), "0", "1",
                        genericItem.getGenericItemId().toString(), genericItem.getQuantity(),
                        genericItem.getPrice().floatValue());
                lineItems.add(line);
            }
        }

        String taxEstimationId = UUID.randomUUID().toString();

        AvaGetTaxRequest request = new AvaGetTaxRequest();

        AvaGetTaxRequest.DOC_TYPE docType;
        Boolean commit;

        if (submitTax)
        {
            docType = AvaGetTaxRequest.DOC_TYPE.SALES_INVOICE;
            commit = true;

        }
        else
        {
            docType = AvaGetTaxRequest.DOC_TYPE.SALES_ORDER;
            commit = false;
        }

        request.initializeForSimpleTaxRequest(companyCode, docType, taxEstimationId, new Date(),
                userId.toString(), lineItems, addresses, commit);

        if (lineItems.size() > 0)
            rval = avaTax.getTax(request);
        else {
            rval = new AvaGetTaxResult();
            rval.setTotalTax(new Float(0.00));
        }

        return rval;
    }

    @Override
    public Object getTaxes(Address shippingAddress, String itemReferenceId, int quantity, Float price,
            String userId, Boolean submitTax)
    {
        List<AvaLine> lineItems = new ArrayList<>();
        List<AvaAddress> addresses = new ArrayList<>();

        //TODO:  Put this in a config file.
        //Set up the source address.  This is Trove.
        AvaAddress sourceAddress = new AvaAddress();
        sourceAddress.setLine1("20 Exchange Pl");
        sourceAddress.setLine2("Apt 1604");
        sourceAddress.setRegion("NY");
        sourceAddress.setCity("New York");
        sourceAddress.setPostalCode("10005");
        sourceAddress.setAddressCode("0");
        addresses.add(sourceAddress);
        AvaAddress avaAddress = shippingAddress.toAvaAddress();
        avaAddress.setAddressCode("1");
        addresses.add(avaAddress);


        AvaLine line = new AvaLine();
        line.initializeForSimpleTaxRequest(String.valueOf(0), "0", "1",
                itemReferenceId, quantity,
                price);
        lineItems.add(line);

        String taxEstimationId = UUID.randomUUID().toString();

        AvaGetTaxRequest request = new AvaGetTaxRequest();

        AvaGetTaxRequest.DOC_TYPE docType;
        Boolean commit;

        if (submitTax)
        {
            docType = AvaGetTaxRequest.DOC_TYPE.SALES_INVOICE;
            commit = true;

        }
        else
        {
            docType = AvaGetTaxRequest.DOC_TYPE.SALES_ORDER;
            commit = false;
        }

        request.initializeForSimpleTaxRequest(companyCode, docType, taxEstimationId, new Date(),
                userId, lineItems, addresses, commit);


        return avaTax.getTax(request);
    }
}
