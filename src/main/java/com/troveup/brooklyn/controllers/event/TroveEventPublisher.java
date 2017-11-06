package com.troveup.brooklyn.controllers.event;

import com.troveup.brooklyn.controllers.event.ForgotPasswordEvent;
import com.troveup.brooklyn.controllers.event.OnRegistrationCompleteEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * Created by tim on 4/20/15.
 */
public class TroveEventPublisher implements ApplicationEventPublisherAware
{

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher)
    {
        this.publisher = publisher;
    }

    public void publishRegistrationEvent(OnRegistrationCompleteEvent event)
    {
        publisher.publishEvent(event);
    }

    public void publishForgotPasswordEvent(ForgotPasswordEvent event)
    {
        publisher.publishEvent(event);
    }
}
