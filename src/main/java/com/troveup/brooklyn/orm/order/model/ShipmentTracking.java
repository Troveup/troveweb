package com.troveup.brooklyn.orm.order.model;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 6/11/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class ShipmentTracking
{
    private Long trackingId;
    private String carrier;
    private String shipDate;
    private String trackingNumber;
    private String trackingUrl;
    private String estimatedShipDate;
    private String deliveryDate;

    public ShipmentTracking()
    {

    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(Long trackingId) {
        this.trackingId = trackingId;
    }

    @Persistent
    @Column(name = "carrier", allowsNull = "true")
    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    @Persistent
    @Column(name = "ship_date", allowsNull = "true")
    public String getShipDate() {
        return shipDate;
    }

    public void setShipDate(String shipDate) {
        this.shipDate = shipDate;
    }

    @Persistent
    @Column(name = "tracking_number", allowsNull = "true")
    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    @Persistent
    @Column(name = "tracking_url", allowsNull = "true")
    public String getTrackingUrl() {
        return trackingUrl;
    }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }

    @Persistent
    @Column(name = "estimated_ship_date", allowsNull = "true")
    public String getEstimatedShipDate() {
        return estimatedShipDate;
    }

    public void setEstimatedShipDate(String estimatedShipDate) {
        this.estimatedShipDate = estimatedShipDate;
    }

    @Persistent
    @Column(name = "delivery_date", allowsNull = "true")
    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public static List<String> getTrackingFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("trackingId");
        rval.add("carrier");
        rval.add("shipDate");
        rval.add("trackingNumber");
        rval.add("estimatedShipDate");
        rval.add("deliveryDate");


        return rval;
    }
}
