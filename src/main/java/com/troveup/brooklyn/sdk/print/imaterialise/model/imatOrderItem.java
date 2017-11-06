package com.troveup.brooklyn.sdk.print.imaterialise.model;

/**
 * Created by tim on 6/10/15.
 */
public class imatOrderItem
{
    String cartItemId;
    String cartItemReference;
    String toolId;
    String modelId;
    String orderItemName;
    String previewPictureUrl;
    String materialId;
    String materialName;
    String finishID;
    String finishName;
    Float xDimMm;
    Float yDimMm;
    Float zDimMm;
    Float volumeCm3;
    Float surfaceCm2;
    int quantity;
    Float orderItemPrice;
    Float orderItemUnitPrice;

    public imatOrderItem()
    {

    }

    public String getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(String cartItemId) {
        this.cartItemId = cartItemId;
    }

    public String getCartItemReference() {
        return cartItemReference;
    }

    public void setCartItemReference(String cartItemReference) {
        this.cartItemReference = cartItemReference;
    }

    public String getToolId() {
        return toolId;
    }

    public void setToolId(String toolId) {
        this.toolId = toolId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getOrderItemName() {
        return orderItemName;
    }

    public void setOrderItemName(String orderItemName) {
        this.orderItemName = orderItemName;
    }

    public String getPreviewPictureUrl() {
        return previewPictureUrl;
    }

    public void setPreviewPictureUrl(String previewPictureUrl) {
        this.previewPictureUrl = previewPictureUrl;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getFinishID() {
        return finishID;
    }

    public void setFinishID(String finishID) {
        this.finishID = finishID;
    }

    public String getFinishName() {
        return finishName;
    }

    public void setFinishName(String finishName) {
        this.finishName = finishName;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Float getOrderItemPrice() {
        return orderItemPrice;
    }

    public void setOrderItemPrice(Float orderItemPrice) {
        this.orderItemPrice = orderItemPrice;
    }

    public Float getOrderItemUnitPrice() {
        return orderItemUnitPrice;
    }

    public void setOrderItemUnitPrice(Float orderItemUnitPrice) {
        this.orderItemUnitPrice = orderItemUnitPrice;
    }
}
