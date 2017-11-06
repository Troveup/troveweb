package com.troveup.brooklyn.sdk.tax.avalara.model;

/**
 * Created by tim on 5/27/15.
 */
public class AvaMessage
{
    private String Summary;
    private String Details;
    private String RefersTo;
    private String Severity;
    private String Source;

    public AvaMessage()
    {

    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        this.Summary = summary;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        this.Details = details;
    }

    public String getRefersTo() {
        return RefersTo;
    }

    public void setRefersTo(String refersTo) {
        this.RefersTo = refersTo;
    }

    public String getSeverity() {
        return Severity;
    }

    public void setSeverity(String severity) {
        this.Severity = severity;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        this.Source = source;
    }
}
