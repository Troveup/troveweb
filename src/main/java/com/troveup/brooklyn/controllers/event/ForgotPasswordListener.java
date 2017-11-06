package com.troveup.brooklyn.controllers.event;

import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.user.interfaces.IUserAccessor;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.email.interfaces.IMailProvider;
import com.troveup.brooklyn.sdk.email.mandrill.api.MandrillMessageStatus;
import com.troveup.brooklyn.util.TroveJavaMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

import java.util.*;

/**
 * Created by tim on 4/20/15.
 */
public class ForgotPasswordListener implements ApplicationListener<ForgotPasswordEvent>
{

    @Autowired
    private IUserAccessor accessor;

    @Autowired
    Environment env;

    @Autowired
    private IMailProvider mailProvider;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onApplicationEvent(ForgotPasswordEvent forgotPasswordEvent)
    {
        this.createForgotPasswordScenario(forgotPasswordEvent);
    }

    public void createForgotPasswordScenario(ForgotPasswordEvent event)
    {
        //TODO:  Modify this to use a properties file
        String email = event.getEmail();
        String token = UUID.randomUUID().toString();

        User user = accessor.getUserByEmail(email, IEnums.SEEK_MODE.QUICK);

        if (user != null) {
            accessor.createForgotPasswordLinkage(user.getUserId(), token);

            /*String recipientAddress = user.getEmail();
            List<String> recipientAddresses = new ArrayList<String>();
            recipientAddresses.add(recipientAddress);
            String subject = "Forgot Password";
            String forgotPasswordUrl = event.getAppUrl() + "/forgotpassword/" + token;
            String messageBody = "Please click <a href=\"" + forgotPasswordUrl + "\">here</a> to change your password.";
            String sender = "hello@troveup.com";

            mailer.setSender(sender);
            mailer.setSubject(subject);
            mailer.setMessageBody(messageBody);
            mailer.setMessageBodyHtml(false);
            mailer.setRecipients(recipientAddresses);

            mailer.queueEmailForTask();*/

            String forgotPasswordLink = env.getProperty("environment.baseurl") + "forgotpassword/" + token;

            Map<String, String> toMap = new HashMap<>();
            toMap.put(user.getFirstName(), user.getEmail());

            Map<String, String> variableMap = new HashMap<>();
            variableMap.put("FIRST_NAME_HERE", user.getFirstName());
            variableMap.put("FORGOT_PASSWORD_LINK", forgotPasswordLink);
            AbstractMap.SimpleEntry<String, String> from = new AbstractMap.SimpleEntry<String, String>("Trove Team", "hello@troveup.com");
            MandrillMessageStatus[] status = (MandrillMessageStatus[]) mailProvider.sendTemplateEmail("Trove Password Reset", from, toMap, "password-request-template",
                    variableMap, false);

            if (!status[0].getStatus().equals("sent")) {
                logger.debug("Failed to send e-mail!  Status was " + status[0].getStatus());
                logger.debug("Rejection reason " + status[0].getRejectReason());
            }
        }
    }
}
