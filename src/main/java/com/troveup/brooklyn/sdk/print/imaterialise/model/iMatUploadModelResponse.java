package com.troveup.brooklyn.sdk.print.imaterialise.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 5/22/15.
 */
public class iMatUploadModelResponse
{
    String toolID;
    String modelID;
    String modelFileName;
    String fileUnits;
    Float xDimMm;
    Float yDimMm;
    Float zDimMm;
    Float volumeCm3;
    Float surfaceCm2;
    String modelStatus;
    String validUntil;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    List<iMatResponseError> error;

    public iMatUploadModelResponse()
    {
        error = new ArrayList<>();
    }

    public String getToolID() {
        return toolID;
    }

    public void setToolID(String toolID) {
        this.toolID = toolID;
    }

    public String getModelID() {
        return modelID;
    }

    public void setModelID(String modelID) {
        this.modelID = modelID;
    }

    public String getModelFileName() {
        return modelFileName;
    }

    public void setModelFileName(String modelFileName) {
        this.modelFileName = modelFileName;
    }

    public String getFileUnits() {
        return fileUnits;
    }

    public void setFileUnits(String fileUnits) {
        this.fileUnits = fileUnits;
    }

    public Float getxDimMm() {
        return xDimMm;
    }

    public void setxDimMm(Float xDimMm) {
        this.xDimMm = xDimMm;
    }

    public Float getyDimMm() {
        return yDimMm;
    }

    public void setyDimMm(Float yDimMm) {
        this.yDimMm = yDimMm;
    }

    public Float getzDimMm() {
        return zDimMm;
    }

    public void setzDimMm(Float zDimMm) {
        this.zDimMm = zDimMm;
    }

    public Float getVolumeCm3() {
        return volumeCm3;
    }

    public void setVolumeCm3(Float volumeCm3) {
        this.volumeCm3 = volumeCm3;
    }

    public Float getSurfaceCm2() {
        return surfaceCm2;
    }

    public void setSurfaceCm2(Float surfaceCm2) {
        this.surfaceCm2 = surfaceCm2;
    }

    public String getModelStatus() {
        return modelStatus;
    }

    public void setModelStatus(String modelStatus) {
        this.modelStatus = modelStatus;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

    public List<iMatResponseError> getError() {
        return error;
    }

    public void setError(List<iMatResponseError> error) {
        this.error = error;
    }
}
