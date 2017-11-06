package com.troveup.brooklyn.sdk.email.interfaces;

import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.user.model.Address;
import com.troveup.brooklyn.sdk.email.mandrill.model.OrderConfirmationItem;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tim on 6/8/15.
 */
public interface IMailProvider
{
    Object sendRawEmail(String subject, String from, Map<String, String> to, String content, Boolean async);
    Object sendTemplateEmail(String subject, String From, Map<String, String> To,
                             String templateName, Map<String, String> templateVariables, Boolean async);
    Object sendTemplateEmail(String subject, AbstractMap.SimpleEntry<String, String> from, Map<String, String> To,
                             String templateName, Map<String, String> templateVariables, Boolean async);

    Object sendWelcomeEmail(String userFirstName, Map<String, String> to);
    Object sendOrderConfirmationEmail(Map<String, String> to, List<OrderConfirmationItem> confirmationItems, String orderNumber, String orderTotal, String orderTax, String orderSubTotal);
    Object sendShippedEmail(Address shippingAddress, String billingFirstName, String billingLastName, String checkoutEmail, String trackingNumber);
    Object sendShippedPrototypeEmail(Address shippingAddress, String billingFirstName, String billingLastName, String checkoutEmail, String trackingNumber);
}
