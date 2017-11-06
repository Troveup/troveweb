package com.troveup.brooklyn.controllers.event;

import com.troveup.brooklyn.orm.user.model.User;
import org.springframework.context.ApplicationEvent;

/**
 * Created by tim on 4/20/15.
 */
public class OnRegistrationCompleteEvent extends ApplicationEvent
{

    private final String appUrl;
    private final User user;

    public OnRegistrationCompleteEvent(User user, String appUrl) {
        super(user);

        this.user = user;
        this.appUrl = appUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public User getUser() {
        return user;
    }
}
