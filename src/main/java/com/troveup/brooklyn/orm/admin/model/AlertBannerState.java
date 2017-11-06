package com.troveup.brooklyn.orm.admin.model;

import javax.jdo.annotations.*;
import java.util.Date;

/**
 * Created by tim on 12/8/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class AlertBannerState
{
    public AlertBannerState()
    {
        this.createdDate = new Date();
    }

    public enum BANNER_STATE {
        ENABLED,
        DISABLED
    }

    public AlertBannerState(String bannerText)
    {
        this.bannerText = bannerText;
        this.mobileBannerText = bannerText;
        this.bannerState = BANNER_STATE.ENABLED;
        this.buttonState = BANNER_STATE.DISABLED;
        this.createdDate = new Date();
    }

    public AlertBannerState(String bannerText, String buttonText, String buttonUrl)
    {
        this.bannerText = bannerText;
        this.mobileBannerText = bannerText;
        this.buttonText = buttonText;
        this.buttonUrl = buttonUrl;
        this.bannerState = BANNER_STATE.ENABLED;
        this.buttonState = BANNER_STATE.ENABLED;
        this.createdDate = new Date();
    }

    public AlertBannerState(String bannerText, String mobileBannerText, String buttonText, String buttonUrl)
    {
        this.bannerText = bannerText;
        this.mobileBannerText = mobileBannerText;
        this.buttonText = buttonText;
        this.buttonUrl = buttonUrl;
        this.bannerState = BANNER_STATE.ENABLED;
        this.buttonState = BANNER_STATE.ENABLED;
        this.createdDate = new Date();
    }

    private Long bannerStateId;
    private String bannerText;
    private String mobileBannerText;
    private BANNER_STATE bannerState;
    private BANNER_STATE buttonState;
    private String buttonText;
    private String buttonUrl;
    private Date createdDate;

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getBannerStateId() {
        return bannerStateId;
    }

    public void setBannerStateId(Long bannerStateId) {
        this.bannerStateId = bannerStateId;
    }

    @Persistent
    public String getBannerText() {
        return bannerText;
    }

    public void setBannerText(String bannerText) {
        this.bannerText = bannerText;
    }

    @Persistent
    public String getMobileBannerText() {
        return mobileBannerText;
    }

    public void setMobileBannerText(String mobileBannerText) {
        this.mobileBannerText = mobileBannerText;
    }

    @Persistent
    public BANNER_STATE getBannerState() {
        return bannerState;
    }

    public void setBannerState(BANNER_STATE bannerState) {
        this.bannerState = bannerState;
    }

    @Persistent
    public BANNER_STATE getButtonState() {
        return buttonState;
    }

    public void setButtonState(BANNER_STATE buttonState) {
        this.buttonState = buttonState;
    }

    @Persistent
    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    @Persistent
    public String getButtonUrl() {
        return buttonUrl;
    }

    public void setButtonUrl(String buttonUrl) {
        this.buttonUrl = buttonUrl;
    }

    @Persistent
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public static AlertBannerState getDisabledBanner()
    {
        AlertBannerState rval = new AlertBannerState();
        rval.setBannerState(BANNER_STATE.DISABLED);
        rval.setButtonState(BANNER_STATE.DISABLED);

        return rval;
    }
}
