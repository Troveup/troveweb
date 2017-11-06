package com.troveup.brooklyn.sdk.print.shapeways.model;

/**
 * Created by tim on 7/1/15.
 */
public class ShapewaysUploadResponse
{
    private String result;
    private String modelId;
    private String modelVersion;
    private String title;
    private String fileName;
    private String contentLength;
    private String fileMd5Checksum;
    private String description;
    private String isPublic;
    private String isClaimable;
    private String isForSale;
    private String isDownloadable;
    private String printable;

    public ShapewaysUploadResponse()
    {

    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentLength() {
        return contentLength;
    }

    public void setContentLength(String contentLength) {
        this.contentLength = contentLength;
    }

    public String getFileMd5Checksum() {
        return fileMd5Checksum;
    }

    public void setFileMd5Checksum(String fileMd5Checksum) {
        this.fileMd5Checksum = fileMd5Checksum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public String getIsClaimable() {
        return isClaimable;
    }

    public void setIsClaimable(String isClaimable) {
        this.isClaimable = isClaimable;
    }

    public String getIsForSale() {
        return isForSale;
    }

    public void setIsForSale(String isForSale) {
        this.isForSale = isForSale;
    }

    public String getIsDownloadable() {
        return isDownloadable;
    }

    public void setIsDownloadable(String isDownloadable) {
        this.isDownloadable = isDownloadable;
    }

    public String getPrintable() {
        return printable;
    }

    public void setPrintable(String printable) {
        this.printable = printable;
    }
}
