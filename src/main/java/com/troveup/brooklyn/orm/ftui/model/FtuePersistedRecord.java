package com.troveup.brooklyn.orm.ftui.model;

import com.troveup.brooklyn.orm.order.model.PrintOrder;
import com.troveup.brooklyn.sdk.meshexporter.forge.model.CustomizerOperator;
import com.troveup.brooklyn.sdk.meshexporter.java.model.OperatorWeightJson;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 6/16/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class FtuePersistedRecord
{
    public enum FTUE_STATUS
    {
        //Legacy status for pulling purposes
        COMPLETE,
        QUEUED_FOR_VOODOO,
        SUBMITTED_TO_VOODOO,
        //New statuses
        QUEUED_FOR_SAMPLE_SUPPLIER,
        SUBMITTED_TO_SAMPLE_SUPPLIER,
        PROCESSING_FOR_LANDING,
        WAITING_FOR_MANUAL_READY,
        WAITING_TO_BE_SENT,
        EMAIL_SENT
    }

    private Long ftuePersistedRecordId;
    private boolean submitted;

    //Used by Voodoo
    private FtueRequest request;
    private FtueResponse response;

    //Any other print supplier (Initial Implementation: Shapeways)
    private PrintOrder printOrder;

    private int responseOrderId;

    private Date creationDate;
    private Date emailedDate;

    private Long ftueModelId;
    private List<FtueModelWeights> modelWeights;

    private String size;

    private String landingUuid;

    private List<FtueMaterialPrice> materialPrices;
    private List<FtueImage> ftueImages;

    private FTUE_STATUS status;

    private String referralCode;
    private String referrerCode;

    private Float ftueModelVolume;

    private String manufacturerModelUrl;


    public FtuePersistedRecord()
    {
        creationDate = new Date();
    }


    public FtuePersistedRecord(FtuePersistedRecord record)
    {
        this.submitted = record.isSubmitted();
        this.request = record.getRequest();
        this.response = record.getResponse();
        creationDate = new Date();
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getFtuePersistedRecordId() {
        return ftuePersistedRecordId;
    }

    public void setFtuePersistedRecordId(Long ftuePersistedRecordId) {
        this.ftuePersistedRecordId = ftuePersistedRecordId;
    }

    @Persistent
    @Column(name = "order_id", allowsNull = "true")
    public int getResponseOrderId() {
        return responseOrderId;
    }

    public void setResponseOrderId(int responseOrderId) {
        this.responseOrderId = responseOrderId;
    }

    @Persistent
    @Column(name = "submitted", allowsNull = "true")
    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    @Persistent
    @Column(name = "request", allowsNull = "true")
    public FtueRequest getRequest() {
        return request;
    }

    public void setRequest(FtueRequest request) {
        this.request = request;
    }

    @Persistent
    @Column(name = "response", allowsNull = "true")
    public FtueResponse getResponse() {
        return response;
    }

    public void setResponse(FtueResponse response) {
        this.response = response;
    }

    @Persistent
    @Column(name = "creation_date")
    public Date getCreationDate() {
        return creationDate;
    }

    @Persistent
    @Column(name = "parent_model_id")
    public Long getFtueModelId() {
        return ftueModelId;
    }

    public void setFtueModelId(Long ftueModelId) {
        this.ftueModelId = ftueModelId;
    }

    @Persistent
    public List<FtueModelWeights> getModelWeights() {
        return modelWeights;
    }

    public void setModelWeights(List<FtueModelWeights> modelWeights) {
        this.modelWeights = modelWeights;
    }

    @Persistent
    @Column(name = "size")
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Persistent
    @Column(name = "status")
    public FTUE_STATUS getStatus() {
        return status;
    }

    public void setStatus(FTUE_STATUS status) {
        this.status = status;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Persistent
    @Column(name = "landing_uuid", length = 255)
    public String getLandingUuid() {
        return landingUuid;
    }

    public void setLandingUuid(String landingUuid) {
        this.landingUuid = landingUuid;
    }

    @Persistent
    public List<FtueMaterialPrice> getMaterialPrices() {
        return materialPrices;
    }

    public void setMaterialPrices(List<FtueMaterialPrice> materialPrices) {
        this.materialPrices = materialPrices;
    }

    @Persistent
    public List<FtueImage> getFtueImages() {
        return ftueImages;
    }

    public void setFtueImages(List<FtueImage> ftueImages) {
        this.ftueImages = ftueImages;
    }

    @Persistent
    public Date getEmailedDate() {
        return emailedDate;
    }

    public void setEmailedDate(Date emailedDate) {
        this.emailedDate = emailedDate;
    }

    @Persistent
    public PrintOrder getPrintOrder() {
        return printOrder;
    }

    public void setPrintOrder(PrintOrder printOrder) {
        this.printOrder = printOrder;
    }

    @Persistent
    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    @Persistent
    public String getReferrerCode() {
        return referrerCode;
    }

    public void setReferrerCode(String referrerCode) {
        this.referrerCode = referrerCode;
    }

    @Persistent
    public Float getFtueModelVolume() {
        return ftueModelVolume;
    }

    public void setFtueModelVolume(Float ftueModelVolume) {
        this.ftueModelVolume = ftueModelVolume;
    }

    @Persistent
    public String getManufacturerModelUrl() {
        return manufacturerModelUrl;
    }

    public void setManufacturerModelUrl(String manufacturerModelUrl) {
        this.manufacturerModelUrl = manufacturerModelUrl;
    }

    public static List<String> getPersistedRecordFullFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("request");
        rval.add("response");
        rval.add("creationDate");
        rval.add("modelWeights");
        rval.add("status");
        rval.add("landingUuid");
        rval.add("materialPrices");
        rval.add("ftueImages");

        return rval;

    }

    public static List<String> getPersistedRecordPrototypeFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("request");
        rval.add("modelWeights");

        return rval;
    }

    public static List<String> getPersistedRecordRenderFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("ftueModelId");
        rval.add("modelWeights");
        rval.add("status");

        return rval;
    }

    public static List<String> getPersistedRecordAddressFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("request");

        return rval;
    }

    public static List<String> getPersistedRecordLandingFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("materialPrices");
        rval.add("ftueImages");

        return rval;
    }

    public static List<String> getPersistedRecordPricesFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("materialPrices");

        return rval;
    }

    public static List<String> getPersistedRecordAddressImageFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("request");
        rval.add("ftueImages");

        return rval;
    }

    public List<CustomizerOperator> getExporterCompatibleWeights()
    {
        List<CustomizerOperator> rval = new ArrayList<>();

        if (modelWeights != null && modelWeights.size() > 0)
        {
            for (FtueModelWeights weight : modelWeights)
            {
                if (weight.getWeightId() != null)
                    rval.add(new CustomizerOperator(weight.getWeightId(), weight.getWeightValue()));
            }

        }

        return rval;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof FtuePersistedRecord)
        {
            return ((FtuePersistedRecord) obj).getFtuePersistedRecordId().equals(this.getFtuePersistedRecordId());
        }
        else
            return false;
    }
}

