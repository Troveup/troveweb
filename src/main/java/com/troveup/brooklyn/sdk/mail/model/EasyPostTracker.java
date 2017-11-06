package com.troveup.brooklyn.sdk.mail.model;

import java.util.List;

/**
 * Created by tim on 8/25/15.
 */
public class EasyPostTracker
{
    private String id;
    private String object;
    private String mode;
    private String tracking_code;
    private String status;
    private String created_at;
    private String updated_at;
    private String signed_by;
    private String weight;
    private String est_delivery_date;
    private String shipment_id;
    private String carrier;
    private List<EasyPostTrackingDetail> tracking_details;
    private EasyPostCarrierDetail carrier_detail;

    public EasyPostTracker()
    {

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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getTracking_code() {
        return tracking_code;
    }

    public void setTracking_code(String tracking_code) {
        this.tracking_code = tracking_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getSigned_by() {
        return signed_by;
    }

    public void setSigned_by(String signed_by) {
        this.signed_by = signed_by;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getEst_delivery_date() {
        return est_delivery_date;
    }

    public void setEst_delivery_date(String est_delivery_date) {
        this.est_delivery_date = est_delivery_date;
    }

    public String getShipment_id() {
        return shipment_id;
    }

    public void setShipment_id(String shipment_id) {
        this.shipment_id = shipment_id;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public List<EasyPostTrackingDetail> getTracking_details() {
        return tracking_details;
    }

    public void setTracking_details(List<EasyPostTrackingDetail> tracking_details) {
        this.tracking_details = tracking_details;
    }

    public EasyPostCarrierDetail getCarrier_detail() {
        return carrier_detail;
    }

    public void setCarrier_detail(EasyPostCarrierDetail carrier_detail) {
        this.carrier_detail = carrier_detail;
    }
}
