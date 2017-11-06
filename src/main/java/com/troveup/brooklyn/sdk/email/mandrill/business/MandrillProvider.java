package com.troveup.brooklyn.sdk.email.mandrill.business;

import com.troveup.brooklyn.orm.user.model.Address;
import com.troveup.brooklyn.sdk.email.interfaces.IMailProvider;
import com.troveup.brooklyn.sdk.email.mandrill.api.MandrillApi;
import com.troveup.brooklyn.sdk.email.mandrill.api.MandrillMessage;
import com.troveup.brooklyn.sdk.email.mandrill.api.MandrillMessageStatus;
import com.troveup.brooklyn.sdk.email.mandrill.model.OrderConfirmationItem;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by tim on 6/8/15.
 */
public class MandrillProvider extends CommonMailProvider implements IMailProvider
{
    @Autowired
    MandrillApi mandrillApi;

    @Override
    public Object sendRawEmail(String subject, String from, Map<String, String> to, String htmlContent, Boolean async)
    {
        MandrillMessageStatus[] rval = null;
        MandrillMessage message = new MandrillMessage();
        message.setHtml(htmlContent);
        message.setSubject(subject);
        message.setFromEmail(from);
        message.setAutoHtml(true);
        List<MandrillMessage.Recipient> recipients = new ArrayList<>();

        for (String key : to.keySet())
        {
            MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
            recipient.setType(MandrillMessage.Recipient.Type.TO);
            recipient.setName(key);
            recipient.setEmail(to.get(key));
            recipients.add(recipient);
        }

        message.setTo(recipients);

        try {
            rval = mandrillApi.messages().send(message, async);
        } catch (Exception e) {
            logError(e);
        }

        return rval;
    }

    @Override
    public Object sendTemplateEmail(String subject, String from, Map<String, String> to,
                                    String templateName, Map<String, String> templateVariables, Boolean async)
    {
        MandrillMessageStatus[] rval = null;
        List<MandrillMessage.MergeVar> mergeVars = new ArrayList<>();

        if (templateVariables != null) {
            for (String key : templateVariables.keySet()) {
                mergeVars.add(new MandrillMessage.MergeVar(key, templateVariables.get(key)));
            }
        }

        MandrillMessage message = new MandrillMessage();
        message.setGlobalMergeVars(mergeVars);
        message.setFromEmail(from);
        message.setSubject(subject);
        message.setTrackClicks(true);
        message.setTrackOpens(true);

        List<MandrillMessage.Recipient> recipients = new ArrayList<>();

        for (String key : to.keySet())
        {
            MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
            recipient.setType(MandrillMessage.Recipient.Type.TO);
            recipient.setName(key);
            recipient.setEmail(to.get(key));
            recipients.add(recipient);
        }

        message.setTo(recipients);

        try {
            rval = mandrillApi.messages().sendTemplate(templateName, null, message, async);
        } catch (Exception e)
        {
            logError(e);
        }

        return rval;
    }

    @Override
    public Object sendTemplateEmail(String subject, AbstractMap.SimpleEntry<String, String> from, Map<String, String> To, String templateName, Map<String, String> templateVariables, Boolean async) {
        MandrillMessageStatus[] rval = null;
        List<MandrillMessage.MergeVar> mergeVars = new ArrayList<>();

        if (templateVariables != null) {
            for (String key : templateVariables.keySet()) {
                mergeVars.add(new MandrillMessage.MergeVar(key, templateVariables.get(key)));
            }
        }

        MandrillMessage message = new MandrillMessage();
        message.setGlobalMergeVars(mergeVars);
        message.setFromEmail(from.getValue());
        message.setFromName(from.getKey());
        message.setSubject(subject);
        message.setTrackClicks(true);
        message.setTrackOpens(true);

        List<MandrillMessage.Recipient> recipients = new ArrayList<>();

        for (String key : To.keySet())
        {
            MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
            recipient.setType(MandrillMessage.Recipient.Type.TO);
            recipient.setName(key);
            recipient.setEmail(To.get(key));
            recipients.add(recipient);
        }

        message.setTo(recipients);

        try {
            rval = mandrillApi.messages().sendTemplate(templateName, null, message, async);
        } catch (Exception e)
        {
            logError(e);
        }

        return rval;
    }

    @Override
    public Object sendWelcomeEmail(String userFirstName, Map<String, String> to)
    {
        Map<String, String> templateVariables = new HashMap<>();
        templateVariables.put("FIRST_NAME", userFirstName);

        String[] nameSplit = userFirstName.split(" ");

        AbstractMap.SimpleEntry<String, String> from = new AbstractMap.SimpleEntry<String, String>("Trove Team", "hello@troveup.com");

        return sendTemplateEmail("Welcome to Trove " + nameSplit[0] +"!", from, to, "welcome-email",
                templateVariables, false);

    }

    @Override
    public Object sendOrderConfirmationEmail(Map<String, String> to, List<OrderConfirmationItem> confirmationItems, String orderNumber, String orderTotal, String orderTax, String orderSubTotal)
    {
        //Table row template to inject into the e-mail
        String orderItemRow = "<tr>\n" +
                "\n" +
                "    <td colspan=\"2\" style=\"border-top:1px solid #e6e6e6;vertical-align:top;border-left:1px solid #e6e6e6; padding-top:30px;padding-bottom:30px;padding-left:20px;\">\n" +
                "        <span style=\"font-family:Arial;font-weight:Bold;font-size:13px;color:#2e2626;line-height:21px;letter-spacing:2px\">{ITEM_NAME}</span><br>\n" +
                "                <span style=\"font-family:Times;font-style:italic;font-size:16px;color:#2e2626;line-height:24px\">{MATERIAL} <br>Size: {ITEM SIZE}<br>{ENGRAVING}{WITH_OR_WITHOUT_PROTOTYPE}<br></span>\n" +
                "    </td>\n" +
                "    <td style=\"border-top:1px solid #e6e6e6;border-right:1px solid #e6e6e6;width:75px;padding-left:10px;padding-right:20px;text-align:right;font-family:Arial;font-weight:bold;font-size:15px;color:black;line-height:18px;letter-spacing:2px;vertical-align:top;padding-top:30px\">{ITEM_PRICE}\n" +
                "\n" +
                "    </td>\n" +
                "</tr>";

        String genericItemRow = "<tr>\n" +
                "\n" +
                "    <td colspan=\"2\" style=\"border-top:1px solid #e6e6e6;vertical-align:top;border-left:1px solid #e6e6e6; padding-top:30px;padding-bottom:30px;padding-left:20px;\">\n" +
                "        <span style=\"font-family:Arial;font-weight:Bold;font-size:13px;color:#2e2626;line-height:21px;letter-spacing:2px\">{ITEM_NAME}</span><br>\n" +
                "    </td>\n" +
                "    <td style=\"border-top:1px solid #e6e6e6;border-right:1px solid #e6e6e6;width:75px;padding-left:10px;padding-right:20px;text-align:right;font-family:Arial;font-weight:bold;font-size:15px;color:black;line-height:18px;letter-spacing:2px;vertical-align:top;padding-top:30px\">{ITEM_PRICE}\n" +
                "\n" +
                "    </td>\n" +
                "</tr>";

        String genericImageItemRow = "<tr>\n" +
                "\n" +
                "    <td colspan=\"1\" style=\"border-top:1px solid #e6e6e6;vertical-align:top;border-left:1px solid #e6e6e6; padding-top:30px;padding-bottom:30px;padding-left:20px;\">\n" +
                "        <img width=\"100px\" src=\"{ITEM_IMAGE}\">\n" +
                "    </td>" +
                "    <td colspan=\"1\" style=\"border-top:1px solid #e6e6e6;vertical-align:top;border-left:1px solid #e6e6e6; padding-top:30px;padding-bottom:30px;padding-left:20px;\">\n" +
                "        <span style=\"font-family:Arial;font-weight:Bold;font-size:13px;color:#2e2626;line-height:21px;letter-spacing:2px\">{ITEM_NAME}</span><br>\n" +
                "    </td>\n" +
                "    <td style=\"border-top:1px solid #e6e6e6;border-right:1px solid #e6e6e6;width:75px;padding-left:10px;padding-right:20px;text-align:right;font-family:Arial;font-weight:bold;font-size:15px;color:black;line-height:18px;letter-spacing:2px;vertical-align:top;padding-top:30px\">{ITEM_PRICE}\n" +
                "\n" +
                "    </td>\n" +
                "</tr>";

        String orderConfirmationRows = "";

        //Fill out table rows and replace the templated information
        for (OrderConfirmationItem confirmationItem : confirmationItems)
        {

            if (!confirmationItem.getIsGeneric()) {
                String prototypeState;
                String engravingText = "";

                if (confirmationItem.getIsPrototype())
                    prototypeState = "Try-On Model Ordered";
                else
                    prototypeState = "Try-On Model Not Ordered";

                if (confirmationItem.getEngraving() != null && confirmationItem.getEngraving().length() > 0)
                    engravingText = "Engraving: " + confirmationItem.getEngraving() + "<br>";

                orderConfirmationRows += orderItemRow.replace("{ITEM_NAME}", confirmationItem.getItemName())
                        .replace("{MATERIAL}", confirmationItem.getItemMaterial())
                        .replace("{ITEM SIZE}", confirmationItem.getItemSize())
                        .replace("{WITH_OR_WITHOUT_PROTOTYPE}", prototypeState)
                        .replace("{ITEM_PRICE}", confirmationItem.getItemPrice())
                        .replace("{ENGRAVING}", engravingText);
            }
            else
            {
                if (confirmationItem.getItemImageUrl() != null)
                {
                    orderConfirmationRows += genericImageItemRow.replace("{ITEM_NAME}", confirmationItem.getItemName())
                            .replace("{ITEM_PRICE}", confirmationItem.getItemPrice()).replace("{ITEM_IMAGE}", confirmationItem.getItemImageUrl());
                }
                else {
                    orderConfirmationRows += genericItemRow.replace("{ITEM_NAME}", confirmationItem.getItemName())
                            .replace("{ITEM_PRICE}", confirmationItem.getItemPrice());
                }
            }
        }

        Map<String, String> templateVariables = new HashMap<>();
        templateVariables.put("UNIQUE_ORDER_NUMBER", orderNumber);
        templateVariables.put("ORDER_DATA", orderConfirmationRows);
        templateVariables.put("TAX", orderTax);
        templateVariables.put("ORDER_SUB_TOTAL", orderSubTotal);
        templateVariables.put("ORDER_TOTAL", orderTotal);


        AbstractMap.SimpleEntry<String, String> from = new AbstractMap.SimpleEntry<String, String>("Trove Team", "hello@troveup.com");

        return sendTemplateEmail("Your Trove order confirmation", from, to, "order-confirmation-final",
                templateVariables, false);
    }

    @Override
    public Object sendShippedEmail(Address shippingAddress, String billingFirstName, String billingLastName, String checkoutEmail, String trackingNumber)
    {
        if (checkoutEmail != null) {
            StringBuilder addressBuilder = buildShippedEmailShippingAddress(shippingAddress);
            Map<String, String> variables = buildShippingEmailVariablesMap(billingFirstName, addressBuilder.toString(), trackingNumber);
            AbstractMap.SimpleEntry<String, String> from = new AbstractMap.SimpleEntry<String, String>("Trove Team", "shipping@troveup.com");

            Map<String, String> recipients = new HashMap<>();
            recipients.put(billingFirstName + " " +
                    billingLastName, checkoutEmail);

            return sendTemplateEmail("Your Trove order has shipped",
                    from, recipients, "shipping-notification-final", variables, false);
        }

        return null;
    }

    @Override
    public Object sendShippedPrototypeEmail(Address shippingAddress, String billingFirstName, String billingLastName, String checkoutEmail, String trackingNumber) {

        if (checkoutEmail != null) {
            StringBuilder addressBuilder = buildShippedEmailShippingAddress(shippingAddress);
            Map<String, String> variables = buildShippingEmailVariablesMap(billingFirstName, addressBuilder.toString(), trackingNumber);
            AbstractMap.SimpleEntry<String, String> from = new AbstractMap.SimpleEntry<String, String>("Trove Team", "shipping@troveup.com");

            Map<String, String> recipients = new HashMap<>();
            recipients.put(billingFirstName + " " +
                    billingLastName, checkoutEmail);

            return sendTemplateEmail("Your Try-On Model Has Shipped",
                    from, recipients, "shipping-notification-prototype", variables, false);
        }

        return null;
    }

    private Map<String, String> buildShippingEmailVariablesMap(String userFirstName, String addressString, String trackingNumber)
    {
        Map<String, String> rval = new HashMap<>();
        rval.put("Name", userFirstName);
        rval.put("Shipping_address", addressString);
        rval.put("Tracking_Number", trackingNumber);
        rval.put("Tracking_Link", "http://wwwapps.ups.com/WebTracking/track?track=yes&trackNums=" +
                trackingNumber);

        return rval;
    }

    private StringBuilder buildShippedEmailShippingAddress(Address address)
    {
        StringBuilder rval = null;

        if (address != null)
        {
            rval = new StringBuilder();
            rval.append(address.getAddressLine1());
            rval.append("<br>");
            if (address.getAddressLine2() != null) {
                rval.append(address.getAddressLine2());
                rval.append("<br>");
            }
            rval.append(address.getCity());
            rval.append(", ");
            String state = address.getSubdivision().getCode();

            if (state.contains("-"))
                rval.append(state.replace("US-", ""));
            else
                rval.append(state);

            rval.append(" ");
            rval.append(address.getPostalCode());
        }

        return rval;
    }
}
