package com.troveup.brooklyn.controllers.event;

import com.troveup.brooklyn.controllers.forms.ForgotPasswordForm;
import org.springframework.context.ApplicationEvent;

/**
 * Created by tim on 4/20/15.
 */
public class ForgotPasswordEvent extends ApplicationEvent
{
    private final String email;
    private final String appUrl;

    public ForgotPasswordEvent(String email, String appUrl)
    {
        super(email);
        this.email = email;
        this.appUrl = appUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getAppUrl() {
        return appUrl;
    }
}
