package com.troveup.brooklyn.sdk.print.imaterialise.model;

/**
 * Created by tim on 5/24/15.
 */
public class iMatModel
{
    private String toolId;
    private String modelId;
    private String modelReference;
    private String materialId;
    private String materialName;
    private String finishId;
    private String finishName;
    private int quantity;
    private Float totalPrice;
    private Float xDimMm;
    private Float yDimMm;
    private Float zDimMm;
    private Float volumeCm3;
    private Float surfaceCm2;
    private Float scale;
    private String currency;
    private String validUntil;

    public iMatModel()
    {

    }

    public String getModelReference() {
        return modelReference;
    }

    public void setModelReference(String modelReference) {
        this.modelReference = modelReference;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getFinishId() {
        return finishId;
    }

    public void setFinishId(String finishId) {
        this.finishId = finishId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public String getToolId() {
        return toolId;
    }

    public void setToolId(String toolId) {
        this.toolId = toolId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getFinishName() {
        return finishName;
    }

    public void setFinishName(String finishName) {
        this.finishName = finishName;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public Float getScale() {
        return scale;
    }

    public void setScale(Float scale) {
        this.scale = scale;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }
}
