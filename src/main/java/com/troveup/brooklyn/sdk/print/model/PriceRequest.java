package com.troveup.brooklyn.sdk.print.model;

/**
 * Created by tim on 5/22/15.
 */
public abstract class PriceRequest
{
    public enum UNITS
    {
        MILLIMETERS,
        CENTIMETERS,
        METERS
    }

    Float volume;
    UNITS volumeBaseUnits;

    Float area;
    UNITS areaBaseUnits;

    public PriceRequest()
    {

    }

    public Float getVolume() {
        return volume;
    }

    public void setVolume(Float volume) {
        this.volume = volume;
    }

    public UNITS getVolumeBaseUnits() {
        return volumeBaseUnits;
    }

    public void setVolumeBaseUnits(UNITS volumeBaseUnits) {
        this.volumeBaseUnits = volumeBaseUnits;
    }

    public Float getArea() {
        return area;
    }

    public void setArea(Float area) {
        this.area = area;
    }

    public UNITS getAreaBaseUnits() {
        return areaBaseUnits;
    }

    public void setAreaBaseUnits(UNITS areaBaseUnits) {
        this.areaBaseUnits = areaBaseUnits;
    }
}
