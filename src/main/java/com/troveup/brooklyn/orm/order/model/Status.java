package com.troveup.brooklyn.orm.order.model;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 5/12/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class Status
{
    public enum ORDER_STATUS
    {
        ACTIVE,
        COMPLETE,
        CANCELLED
    }

    private Long statusId;
    private String statusCode;
    private String statusName;
    private String statusDescription;
    private ORDER_STATUS status;
    private Date lastUpdated;

    public Status()
    {

    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }
    @Persistent
    @Column(name = "status_code", allowsNull = "true")
    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @Persistent
    @Column(name = "status_name", allowsNull = "true")
    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Persistent
    @Column(name = "status_description", allowsNull = "true")
    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    @Persistent
    @Column(name = "last_updated", allowsNull = "true")
    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Persistent
    public ORDER_STATUS getStatus() {
        return status;
    }

    public void setStatus(ORDER_STATUS status) {
        this.status = status;
    }

    public static List<String> getStatusFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("statusId");
        rval.add("statusCode");
        rval.add("statusName");
        rval.add("statusDescription");
        rval.add("lastUpdated");

        return rval;
    }
}
