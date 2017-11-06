package com.troveup.brooklyn.tests.sdk;

import com.troveup.brooklyn.orm.cart.interfaces.ICartAccessor;
import com.troveup.brooklyn.orm.cart.model.Cart;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.order.interfaces.IOrderAccessor;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.sdk.email.interfaces.IMailProvider;
import com.troveup.brooklyn.sdk.email.mandrill.api.MandrillMessageStatus;
import com.troveup.config.PersistenceConfig;
import com.troveup.config.SDKConfig;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tim on 6/8/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, SDKConfig.class})
public class MandrillTest
{
    @Autowired
    IMailProvider provider;

    @Autowired
    ICartAccessor cartAccessor;

    @Test
    public void testSendMessageToMailinator()
    {
        Map<String, String> recipients = new HashMap<>();
        recipients.put("Test User", "tuser001@mailinator.com");

        MandrillMessageStatus[] status = (MandrillMessageStatus[]) provider.sendRawEmail("Test Subject",
                "tuser002@mailinator.com", recipients, "<b>Hello world!</b>", false);

        Assert.assertNotNull(status);
        Assert.assertTrue(status.length > 0);
        Assert.assertTrue(status[0].getStatus().equals("sent"));
        Assert.assertNull(status[0].getRejectReason());
    }

    @Test
    public void testSendTemplateToMailinator()
    {
        Map<String, String> recipients = new HashMap<>();
        recipients.put("Test User", "tim@troveup.com");

        Cart cart = cartAccessor.getShoppingCart(1l, IEnums.SEEK_MODE.CART_ADDRESSES);

        StringBuilder addressBuilder = new StringBuilder();
        addressBuilder.append(cart.getShippingAddress().getAddressLine1());
        addressBuilder.append("<br>");
        if (cart.getShippingAddress().getAddressLine2() != null) {
            addressBuilder.append(cart.getShippingAddress().getAddressLine2());
            addressBuilder.append("<br>");
        }
        addressBuilder.append(cart.getShippingAddress().getCity());
        addressBuilder.append(", ");
        String city = cart.getShippingAddress().getSubdivision().getCode();

        if (city.contains("-"))
            addressBuilder.append(city.replace("US-", ""));
        else
            addressBuilder.append(city);

        addressBuilder.append(" ");
        addressBuilder.append(cart.getShippingAddress().getPostalCode());

        Map<String, String> variables = new HashMap<>();
        variables.put("Name", cart.getBillingAddress().getFirstName());
        variables.put("Shipping_address", addressBuilder.toString());
        variables.put("Tracking_Number", "Tracking <b>number</b>");

        AbstractMap.SimpleEntry<String, String> from = new AbstractMap.SimpleEntry<String, String>("Trove Team", "shipping@troveup.com");

        MandrillMessageStatus[] status = (MandrillMessageStatus[]) provider.sendTemplateEmail("Test Template",
            from, recipients, "shipping-template", variables, false);

        Assert.assertNotNull(status);
        Assert.assertTrue(status.length > 0);
        Assert.assertTrue(status[0].getStatus().equals("sent"));
        Assert.assertNull(status[0].getRejectReason());
    }
}
