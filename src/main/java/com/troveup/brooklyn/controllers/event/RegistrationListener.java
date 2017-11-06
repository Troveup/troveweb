package com.troveup.brooklyn.controllers.event;

import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.user.interfaces.IUserAccessor;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.email.interfaces.IMailProvider;
import com.troveup.brooklyn.sdk.email.mandrill.api.MandrillMessageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

import java.util.*;

/**
 * Created by tim on 4/20/15.
 */
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent>
{
    @Autowired
    private IUserAccessor accessor;

    @Autowired
    private IMailProvider mailProvider;

    @Autowired
    Environment env;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent onRegistrationCompleteEvent)
    {
        this.confirmRegistration(onRegistrationCompleteEvent);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event)
    {
        //TODO:  Modify this to use a properties file
        /*User user = event.getUser();
        String token = UUID.randomUUID().toString();

        accessor.addVerificationLinkage(user.getUserId(), token);

        String recipientAddress = user.getEmail();
        List<String> recipientAddresses = new ArrayList<String>();
        recipientAddresses.add(recipientAddress);
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/signup/" + token;
        String messageBody = "Please click <a href=\"" + confirmationUrl +"\">here</a> to confirm registration.";
        String sender = "hello@troveprint.com";

        mailer.setSender(sender);
        mailer.setSubject(subject);
        mailer.setMessageBody(messageBody);
        mailer.setMessageBodyHtml(false);
        mailer.setRecipients(recipientAddresses);

        mailer.queueEmailForTask();*/

        User user = accessor.getUser(event.getUser().getUserId(), IEnums.SEEK_MODE.USER_SOCIAL_CONNECTIONS);
        String token = UUID.randomUUID().toString();
        Boolean verifyAdd = accessor.addVerificationLinkage(user.getUserId(), token);

        if (verifyAdd)
            logger.debug("Linkage " + token + " added " + " to userId " + user.getUserId());
        else
            logger.debug("Problems adding linkage!");



        String verifyLink = env.getProperty("environment.baseurl") + "signup/" + token;

        Map<String, String> toMap = new HashMap<>();
        toMap.put(user.getFirstName(), user.getEmail());

        Map<String, String> variableMap = new HashMap<>();
        variableMap.put("VERIFY_EMAIL_ADDERSS", verifyLink);
        AbstractMap.SimpleEntry<String, String> from = new AbstractMap.SimpleEntry<String, String>("Trove Team", "hello@troveup.com");
        MandrillMessageStatus[] status = (MandrillMessageStatus[]) mailProvider.sendTemplateEmail("Verify Your Email", from, toMap, "verify-email-template",
            variableMap, false);

        if (!status[0].getStatus().equals("sent")) {
            logger.debug("Failed to send e-mail!  Status was " + status[0].getStatus());
            logger.debug("Rejection reason " + status[0].getRejectReason());
        }
    }
}
