package com.troveup.brooklyn.sdk.print.imaterialise.model;

/**
 * Created by tim on 5/25/15.
 */
public class iMatCartItem
{
    //CartItem Create Request: Required
    String toolId;

    //CartItem Create Request: Required
    String myCartItemReference;

    //CartItem Create Request:  Required if modelFileName null
    String modelId;

    //CartItem Create Request:  Required if modelId null
    String modelFileName;

    //CartItem Create Request: Required
    Float fileScaleFactor;

    //CartItem Create Request: Not Required, defaults to mm
    String fileUnits;

    //CartItem Create Request:  Required
    String materialId;

    String finishName;
    String uploadStatus;
    String materialName;

    //CartItem Create Request:  Required
    String finishId;

    //CartItem Create Request:  Required
    int quantity;

    //CartItem Create Request:  Required, constructor initialized
    Float xDimMm;

    //CartItem Create Request:  Required, constructor initialized
    Float yDimMm;

    //CartItem Create Request:  Required, constructor initialized
    Float zDimMm;

    //CartItem Create Request:  Required, constructor initialized
    Float volumeCm3;

    //CartItem Create Request:  Required, constructor initialized
    Float surfaceCm2;

    //CartItem Create Request:  Required, constructor initialized
    Float iMatApiPrice;

    //CartItem Create Request:  Required, constructor initialized
    Float mySalesPrice;
    Float mySalesUnitPrice;
    Float iMatPrice;
    String validUntil;

    //Cart Create Request:  Required
    String cartItemID;

    iMatResponseError cartItemError;

    public iMatCartItem()
    {
        /**@see http://i.materialise.com/api/docs/cart-item-creation-api/*/
        fileScaleFactor = 1f;
        mySalesPrice = 0f;
        xDimMm = 1f;
        yDimMm = 1f;
        zDimMm = 1f;
        volumeCm3 = 1f;
        surfaceCm2 = 1f;
        iMatApiPrice = 0f;
        quantity = 1;
    }

    public String getToolId() {
        return toolId;
    }

    public void setToolId(String toolId) {
        this.toolId = toolId;
    }

    public String getMyCartItemReference() {
        return myCartItemReference;
    }

    public void setMyCartItemReference(String myCartItemReference) {
        this.myCartItemReference = myCartItemReference;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelFileName() {
        return modelFileName;
    }

    public void setModelFileName(String modelFileName) {
        this.modelFileName = modelFileName;
    }

    public Float getFileScaleFactor() {
        return fileScaleFactor;
    }

    public void setFileScaleFactor(Float fileScaleFactor) {
        this.fileScaleFactor = fileScaleFactor;
    }

    public String getFileUnits() {
        return fileUnits;
    }

    public void setFileUnits(String fileUnits) {
        this.fileUnits = fileUnits;
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

    public Float getiMatApiPrice() {
        return iMatApiPrice;
    }

    public void setiMatApiPrice(Float iMatApiPrice) {
        this.iMatApiPrice = iMatApiPrice;
    }

    public Float getMySalesPrice() {
        return mySalesPrice;
    }

    public void setMySalesPrice(Float mySalesPrice) {
        this.mySalesPrice = mySalesPrice;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
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

    public Float getMySalesUnitPrice() {
        return mySalesUnitPrice;
    }

    public void setMySalesUnitPrice(Float mySalesUnitPrice) {
        this.mySalesUnitPrice = mySalesUnitPrice;
    }

    public Float getiMatPrice() {
        return iMatPrice;
    }

    public void setiMatPrice(Float iMatPrice) {
        this.iMatPrice = iMatPrice;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

    public String getCartItemID() {
        return cartItemID;
    }

    public void setCartItemID(String cartItemID) {
        this.cartItemID = cartItemID;
    }

    public iMatResponseError getCartItemError() {
        return cartItemError;
    }

    public void setCartItemError(iMatResponseError cartItemError) {
        this.cartItemError = cartItemError;
    }

    public iMatModel toImatModel()
    {
        iMatModel model = new iMatModel();
        model.setToolId(toolId);
        model.setModelId(modelId);
        model.setModelReference(myCartItemReference);
        model.setMaterialId(materialId);
        model.setMaterialName(materialName);
        model.setFinishId(finishId);
        model.setFinishName(finishName);
        model.setQuantity(quantity);
        model.setScale(fileScaleFactor);

        return model;
    }
}
