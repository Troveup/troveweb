package com.troveup.brooklyn.orm.order.model;

import com.easypost.model.PostageLabel;

import javax.jdo.annotations.*;
import java.util.Date;

/**
 * Created by tim on 8/11/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class EasyPostPostageLabel
{
    public enum TRACKING_STATUS
    {
        UNKNOWN,
        PRE_TRANSIT,
        IN_TRANSIT,
        OUT_FOR_DELIVERY,
        RETURN_TO_SENDER,
        AVAILABLE_FOR_PICKUP,
        DELIVERED,
        FAILURE,
        CANCELLED
    }

    private Long easyPostPostageLabelPrimaryKey;
    private String id;
    private int dateAdvance;
    private String integratedForm;
    private int labelResolution;
    private String labelSize;
    private String labelType;
    private String labelUrl;
    private String labelFileType;
    private String labelPdfSize;
    private String labelPdfType;
    private String labelPdfUrl;
    private String labelPdfFileType;
    private String labelEpl2Size;
    private String labelEpl2Type;
    private String labelEpl2Url;
    private String labelEpl2FileType;
    private String labelZplSize;
    private String labelZplType;
    private String labelZplUrl;
    private String labelZplFileType;
    private String trackingNumber;
    private Date lastUpdated;
    private TRACKING_STATUS status;
    private Date creationDate;

    //Non-persistent field for storing a cart item ID association for the admin panel
    private Long associatedCartItemId;

    public EasyPostPostageLabel() {
        this.creationDate = new Date();
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getEasyPostPostageLabelPrimaryKey() {
        return easyPostPostageLabelPrimaryKey;
    }

    public void setEasyPostPostageLabelPrimaryKey(Long easyPostPostageLabelPrimaryKey) {
        this.easyPostPostageLabelPrimaryKey = easyPostPostageLabelPrimaryKey;
    }

    @Persistent
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Persistent
    public int getDateAdvance() {
        return dateAdvance;
    }

    public void setDateAdvance(int dateAdvance) {
        this.dateAdvance = dateAdvance;
    }

    @Persistent
    public String getIntegratedForm() {
        return integratedForm;
    }

    public void setIntegratedForm(String integratedForm) {
        this.integratedForm = integratedForm;
    }

    @Persistent
    public int getLabelResolution() {
        return labelResolution;
    }

    public void setLabelResolution(int labelResolution) {
        this.labelResolution = labelResolution;
    }

    @Persistent
    public String getLabelSize() {
        return labelSize;
    }

    public void setLabelSize(String labelSize) {
        this.labelSize = labelSize;
    }

    @Persistent
    public String getLabelType() {
        return labelType;
    }

    public void setLabelType(String labelType) {
        this.labelType = labelType;
    }

    @Persistent
    public String getLabelUrl() {
        return labelUrl;
    }

    public void setLabelUrl(String labelUrl) {
        this.labelUrl = labelUrl;
    }

    @Persistent
    public String getLabelFileType() {
        return labelFileType;
    }

    public void setLabelFileType(String labelFileType) {
        this.labelFileType = labelFileType;
    }

    @Persistent
    public String getLabelPdfSize() {
        return labelPdfSize;
    }

    public void setLabelPdfSize(String labelPdfSize) {
        this.labelPdfSize = labelPdfSize;
    }

    @Persistent
    public String getLabelPdfType() {
        return labelPdfType;
    }

    public void setLabelPdfType(String labelPdfType) {
        this.labelPdfType = labelPdfType;
    }

    @Persistent
    public String getLabelPdfUrl() {
        return labelPdfUrl;
    }

    public void setLabelPdfUrl(String labelPdfUrl) {
        this.labelPdfUrl = labelPdfUrl;
    }

    @Persistent
    public String getLabelPdfFileType() {
        return labelPdfFileType;
    }

    public void setLabelPdfFileType(String labelPdfFileType) {
        this.labelPdfFileType = labelPdfFileType;
    }

    @Persistent
    public String getLabelEpl2Size() {
        return labelEpl2Size;
    }

    public void setLabelEpl2Size(String labelEpl2Size) {
        this.labelEpl2Size = labelEpl2Size;
    }

    @Persistent
    public String getLabelEpl2Type() {
        return labelEpl2Type;
    }

    public void setLabelEpl2Type(String labelEpl2Type) {
        this.labelEpl2Type = labelEpl2Type;
    }

    @Persistent
    public String getLabelEpl2Url() {
        return labelEpl2Url;
    }

    public void setLabelEpl2Url(String labelEpl2Url) {
        this.labelEpl2Url = labelEpl2Url;
    }

    @Persistent
    public String getLabelEpl2FileType() {
        return labelEpl2FileType;
    }

    public void setLabelEpl2FileType(String labelEpl2FileType) {
        this.labelEpl2FileType = labelEpl2FileType;
    }

    @Persistent
    public String getLabelZplSize() {
        return labelZplSize;
    }

    public void setLabelZplSize(String labelZplSize) {
        this.labelZplSize = labelZplSize;
    }

    @Persistent
    public String getLabelZplType() {
        return labelZplType;
    }

    public void setLabelZplType(String labelZplType) {
        this.labelZplType = labelZplType;
    }

    @Persistent
    public String getLabelZplUrl() {
        return labelZplUrl;
    }

    public void setLabelZplUrl(String labelZplUrl) {
        this.labelZplUrl = labelZplUrl;
    }

    @Persistent
    public String getLabelZplFileType() {
        return labelZplFileType;
    }

    public void setLabelZplFileType(String labelZplFileType) {
        this.labelZplFileType = labelZplFileType;
    }

    @Persistent
    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    @Persistent
    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Persistent
    public TRACKING_STATUS getStatus() {
        return status;
    }

    public void setStatus(TRACKING_STATUS status) {
        this.status = status;
    }

    @Persistent
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @NotPersistent
    public Long getAssociatedCartItemId() {
        return associatedCartItemId;
    }

    public void setAssociatedCartItemId(Long associatedCartItemId) {
        this.associatedCartItemId = associatedCartItemId;
    }

    public static EasyPostPostageLabel fromEasyPostLabel(PostageLabel label, String trackingNumber)
    {
        EasyPostPostageLabel rval = new EasyPostPostageLabel();

        rval.id = label.getId();
        rval.dateAdvance = label.getDateAdvance();
        rval.integratedForm = label.getIntegratedForm();
        rval.labelResolution = label.getLabelResolution();
        rval.labelSize = label.getLabelSize();
        rval.labelType = label.getLabelType();
        rval.labelUrl = label.getLabelUrl();
        rval.labelFileType = label.getLabelFileType();
        rval.labelPdfSize = label.getLabelPdfSize();
        rval.labelPdfType = label.getPdfLabelType();
        rval.labelPdfUrl = label.getLabelPdfUrl();
        rval.labelPdfFileType = label.getLabelPdfFileType();
        rval.labelEpl2Size = label.getLabelEpl2Size();
        rval.labelEpl2Type = label.getEpl2LabelType();
        rval.labelEpl2Url = label.getLabelEpl2Url();
        rval.labelEpl2FileType = label.getLabelEpl2FileType();
        rval.labelZplSize = label.getLabelZplSize();
        rval.labelZplType = label.getZplLabelType();
        rval.labelZplUrl = label.getLabelZplUrl();
        rval.labelZplFileType = label.getLabelZplFileType();
        rval.trackingNumber = trackingNumber;
        rval.creationDate = new Date();

        return rval;
    }

    public static TRACKING_STATUS convertStringStatusToTrackingStatus(String status)
    {
        TRACKING_STATUS rval = TRACKING_STATUS.UNKNOWN;

        if (status.equals("pre_transit"))
            rval = TRACKING_STATUS.PRE_TRANSIT;
        else if (status.equals("in_transit"))
            rval = TRACKING_STATUS.IN_TRANSIT;
        else if (status.equals("out_for_delivery"))
            rval = TRACKING_STATUS.OUT_FOR_DELIVERY;
        else if (status.equals("return_to_sender"))
            rval = TRACKING_STATUS.RETURN_TO_SENDER;
        else if (status.equals("available_for_pickup"))
            rval = TRACKING_STATUS.AVAILABLE_FOR_PICKUP;
        else if (status.equals("delivered"))
            rval = TRACKING_STATUS.DELIVERED;
        else if (status.equals("failure"))
            rval = TRACKING_STATUS.FAILURE;
        else if (status.equals("cancelled"))
            rval = TRACKING_STATUS.CANCELLED;

        return rval;

    }
}
