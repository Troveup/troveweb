package com.troveup.brooklyn.sdk.print.shapeways.model;

/**
 * Created by tim on 6/30/15.
 */
public class ShapewaysUploadRequest
{
    private String file;
    private String fileName;
    private Float uploadScale;
    private Boolean hasRightsToModel;
    private Boolean acceptTermsAndConditions;
    private Boolean isPublic;
    private int isForSale;
    private Boolean isDownloadable;

    public ShapewaysUploadRequest(String file, String fileName, Float uploadScale, Boolean hasRightsToModel,
                                  Boolean acceptTermsAndConditions, Boolean isPublic, int isForSale, Boolean isDownloadable)
    {
        this.file = file;
        this.fileName = fileName;
        this.uploadScale = uploadScale;
        this.hasRightsToModel = hasRightsToModel;
        this.acceptTermsAndConditions = acceptTermsAndConditions;
        this.isPublic = isPublic;
        this.isForSale = isForSale;
        this.isDownloadable = isDownloadable;
    }

    public ShapewaysUploadRequest(String file, String fileName)
    {
        this.file = file;
        this.fileName = fileName;
        this.uploadScale = 0.001f;
        this.hasRightsToModel = true;
        this.acceptTermsAndConditions = true;
        this.isPublic = false;
        this.isForSale = 1;
        this.isDownloadable = false;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Float getUploadScale() {
        return uploadScale;
    }

    public void setUploadScale(Float uploadScale) {
        this.uploadScale = uploadScale;
    }

    public Boolean getHasRightsToModel() {
        return hasRightsToModel;
    }

    public void setHasRightsToModel(Boolean hasRightsToModel) {
        this.hasRightsToModel = hasRightsToModel;
    }

    public Boolean getAcceptTermsAndConditions() {
        return acceptTermsAndConditions;
    }

    public void setAcceptTermsAndConditions(Boolean acceptTermsAndConditions) {
        this.acceptTermsAndConditions = acceptTermsAndConditions;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public int getIsForSale() {
        return isForSale;
    }

    public void setIsForSale(int isForSale) {
        this.isForSale = isForSale;
    }

    public Boolean getIsDownloadable() {
        return isDownloadable;
    }

    public void setIsDownloadable(Boolean isDownloadable) {
        this.isDownloadable = isDownloadable;
    }
}
