package com.troveup.brooklyn.sdk.print.imaterialise.model;

/**
 * Created by tim on 5/26/15.
 */
public class iMatFinish
{
    private String finishID;
    private String name;
    private String isApiPriceAvailable;
    private Float minXMm;
    private Float minYMm;
    private Float minZMm;
    private Float maxYMm;
    private Float minDetailMm;
    private Float minWallThicknessMm;

    public iMatFinish()
    {

    }

    public String getFinishID() {
        return finishID;
    }

    public void setFinishID(String finishID) {
        this.finishID = finishID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsApiPriceAvailable() {
        return isApiPriceAvailable;
    }

    public void setIsApiPriceAvailable(String isApiPriceAvailable) {
        this.isApiPriceAvailable = isApiPriceAvailable;
    }

    public Float getMinXMm() {
        return minXMm;
    }

    public void setMinXMm(Float minXMm) {
        this.minXMm = minXMm;
    }

    public Float getMinYMm() {
        return minYMm;
    }

    public void setMinYMm(Float minYMm) {
        this.minYMm = minYMm;
    }

    public Float getMinZMm() {
        return minZMm;
    }

    public void setMinZMm(Float minZMm) {
        this.minZMm = minZMm;
    }

    public Float getMaxYMm() {
        return maxYMm;
    }

    public void setMaxYMm(Float maxYMm) {
        this.maxYMm = maxYMm;
    }

    public Float getMinDetailMm() {
        return minDetailMm;
    }

    public void setMinDetailMm(Float minDetailMm) {
        this.minDetailMm = minDetailMm;
    }

    public Float getMinWallThicknessMm() {
        return minWallThicknessMm;
    }

    public void setMinWallThicknessMm(Float minWallThicknessMm) {
        this.minWallThicknessMm = minWallThicknessMm;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof iMatFinish)
            return ((iMatFinish) obj).getFinishID().equals(finishID);
        else
            return false;
    }
}

