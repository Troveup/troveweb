package com.troveup.brooklyn.orm.clicktracker;

import javax.jdo.annotations.*;
import java.util.Date;

/**
 * Created by tim on 7/7/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class ClickTrackerModel
{
    private Long clickTrackerId;
    private String timeOnPage;
    private String buttonClicked;
    private Date submitted;

    public ClickTrackerModel(String timeOnPage, String buttonClicked)
    {
        this.timeOnPage = timeOnPage;
        this.buttonClicked = buttonClicked;
        submitted = new Date();
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getClickTrackerId() {
        return clickTrackerId;
    }

    public void setClickTrackerId(Long clickTrackerId) {
        this.clickTrackerId = clickTrackerId;
    }

    @Persistent
    public String getButtonClicked() {
        return buttonClicked;
    }

    public void setButtonClicked(String buttonClicked) {
        this.buttonClicked = buttonClicked;
    }

    @Persistent
    public Date getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Date submitted) {
        this.submitted = submitted;
    }
}
