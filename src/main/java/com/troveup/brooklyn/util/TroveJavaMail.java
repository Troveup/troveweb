package com.troveup.brooklyn.util;

import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Created by tim on 4/20/15.
 */
public class TroveJavaMail
{
    private String messageBody;
    private List<String> recipients;
    private String sender;
    private String subject;
    private boolean messageBodyHtml;
    Logger logger;

    public TroveJavaMail(){

    }

    public TroveJavaMail(String messageBody, List<String> recipients,
                         String sender, String subject, boolean messageBodyHtml)
    {
        this.messageBody = messageBody;
        this.recipients = recipients;
        this.sender = sender;
        this.subject = subject;
        this.messageBodyHtml = messageBodyHtml;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isMessageBodyHtml() {
        return messageBodyHtml;
    }

    public void setMessageBodyHtml(boolean messageBodyHtml) {
        this.messageBodyHtml = messageBodyHtml;
    }

    public boolean send()
    {
        boolean rval = false;

        MailService mailService = MailServiceFactory.getMailService();
        MailService.Message message = new MailService.Message();

        message.setSender(sender);
        message.setSubject(subject);
        message.setTo(recipients);

        if (!messageBodyHtml)
            message.setHtmlBody("<HTML>" + messageBody + "</HTML>");
        else
            message.setHtmlBody(messageBody);

        try
        {
            mailService.send(message);
            rval = true;
        } catch (IOException e)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Stack Trace: " + sw.toString());
        }

        return rval;
    }

    public void queueEmailForTask()
    {
        Queue queue = QueueFactory.getDefaultQueue();
        String commaSeparatedRecipients = StringUtils.join(recipients, ",");

        TaskOptions options = TaskOptions.Builder.withUrl("/worker/sendemail")
                .param("messageBody", messageBody)
                .param("recipients", commaSeparatedRecipients)
                .param("sender", sender)
                .param("subject", subject)
                .param("messageBodyHtml", Boolean.toString(messageBodyHtml));

        queue.add(options);
    }
}
