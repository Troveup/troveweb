package com.troveup.brooklyn.orm.renderqueue.model;

import com.troveup.brooklyn.orm.ftui.model.FtuePersistedRecord;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.renderqueue.interfaces.IRenderQueueAccessor;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 6/23/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class Render
{
    public enum RENDER_TYPE
    {
        FTUE,
        ITEM
    }

    private Long renderQueueId;
    private Item itemReference;
    private FtuePersistedRecord ftueReference;
    private String renderMaterial;
    private String jobId;
    private String UUID;
    private String modelUrl;
    private String scene;
    private RENDER_TYPE renderType;
    private Integer errorId;
    private String errorString;
    private IRenderQueueAccessor.RENDER_STATUS renderStatus;
    private Date dateQueued;
    private Date dateSubmitted;
    private Date dateCompleted;
    private Integer retryNumber;

    public Render()
    {

    }

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    public Long getRenderQueueId() {
        return renderQueueId;
    }

    public void setRenderQueueId(Long renderQueueId) {
        this.renderQueueId = renderQueueId;
    }

    @Persistent
    @Column(name = "item_reference")
    public Item getItemReference() {
        return itemReference;
    }

    public void setItemReference(Item itemReference) {
        this.itemReference = itemReference;
    }

    @Persistent
    @Column(name = "render_material")
    public String getRenderMaterial() {
        return renderMaterial;
    }

    public void setRenderMaterial(String renderMaterial) {
        this.renderMaterial = renderMaterial;
    }

    @Persistent
    @Column(name = "job_id")
    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    @Persistent
    @Column(name = "render_status")
    public IRenderQueueAccessor.RENDER_STATUS getRenderStatus() {
        return renderStatus;
    }

    public void setRenderStatus(IRenderQueueAccessor.RENDER_STATUS renderStatus) {
        this.renderStatus = renderStatus;
    }

    @Persistent
    @Column(name = "model_url")
    public String getModelUrl() {
        return modelUrl;
    }

    public void setModelUrl(String modelUrl) {
        this.modelUrl = modelUrl;
    }

    @Persistent
    @Column(name = "scene")
    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    @Persistent
    @Column(name = "date_queued")
    public Date getDateQueued() {
        return dateQueued;
    }

    public void setDateQueued(Date dateQueued) {
        this.dateQueued = dateQueued;
    }

    @Persistent
    @Column(name = "date_submitted")
    public Date getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(Date dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    @Persistent
    @Column(name = "date_completed")
    public Date getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    @Persistent
    @Column(name = "error_id")
    public Integer getErrorId() {
        return errorId;
    }

    public void setErrorId(Integer errorId) {
        this.errorId = errorId;
    }

    @Persistent
    @Column(name = "error_string", length = 500)
    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

    @Persistent
    @Column(name = "uuid")
    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    @Persistent
    @Column(name = "render_type")
    public RENDER_TYPE getRenderType() {
        return renderType;
    }

    public void setRenderType(RENDER_TYPE renderType) {
        this.renderType = renderType;
    }

    @Persistent
    @Column(name = "ftue_reference")
    public FtuePersistedRecord getFtueReference() {
        return ftueReference;
    }

    public void setFtueReference(FtuePersistedRecord ftueReference) {
        this.ftueReference = ftueReference;
    }

    @Persistent
    @Column(name = "retry_number")
    public Integer getRetryNumber() {
        return retryNumber;
    }

    public void setRetryNumber(Integer retryNumber) {
        this.retryNumber = retryNumber;
    }

    public static List<String> getFullRenderFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("renderQueueId");
        rval.add("itemReference");
        rval.add("renderMaterial");
        rval.add("jobId");
        rval.add("modelUrl");
        rval.add("scene");
        rval.add("errorId");
        rval.add("errorString");
        rval.add("dateQueued");
        rval.add("dateSubmitted");
        rval.add("dateCompleted");
        rval.add("renderStatus");
        rval.add("ftueReference");

        return rval;
    }

    public static List<String> availableMaterials()
    {
        List<String> rval = new ArrayList<>();
        rval.add("gold_polished");
        rval.add("platinum_polished");
        rval.add("white_gold_polished");
        rval.add("palladium_polished");
        rval.add("red_gold_polished");

        return rval;
    }
}
