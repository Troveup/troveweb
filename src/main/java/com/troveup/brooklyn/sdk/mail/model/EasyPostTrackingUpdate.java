package com.troveup.brooklyn.sdk.mail.model;

import com.easypost.model.Tracker;

import java.util.List;

/**
 * Created by tim on 8/25/15.
 */
public class EasyPostTrackingUpdate
{
    private List<String> completed_urls;
    private String created_at;
    private String mode;
    private List<String> pending_urls;
    private EasyPostAttribute previous_attributes;
    private String updated_at;
    private String id;
    private String object;
    private EasyPostTracker result;
    private String description;

    public EasyPostTrackingUpdate()
    {

    }

    public List<String> getCompleted_urls() {
        return completed_urls;
    }

    public void setCompleted_urls(List<String> completed_urls) {
        this.completed_urls = completed_urls;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<String> getPending_urls() {
        return pending_urls;
    }

    public void setPending_urls(List<String> pending_urls) {
        this.pending_urls = pending_urls;
    }

    public EasyPostAttribute getPrevious_attributes() {
        return previous_attributes;
    }

    public void setPrevious_attributes(EasyPostAttribute previous_attributes) {
        this.previous_attributes = previous_attributes;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EasyPostTracker getResult() {
        return result;
    }

    public void setResult(EasyPostTracker result) {
        this.result = result;
    }
}
