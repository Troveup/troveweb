package com.troveup.brooklyn.spring.web;

import org.apache.commons.fileupload.FileItemHeaders;
import org.apache.commons.io.IOUtils;

import java.io.*;


/**
 * Created by tim on 5/20/15.
 */
public class TroveFileItem //implements FileItem
{
    private byte[] byteBuffer;
    private String contentType;
    private String name;
    private String fieldName;
    private boolean formField;
    private FileItemHeaders fileItemHeaders;


    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    public boolean isInMemory() {
        return true;
    }

    public long getSize() {
        return byteBuffer.length;
    }

    public byte[] get() {
        return byteBuffer;
    }

    public String getString(String s) throws UnsupportedEncodingException, IOException {
        return IOUtils.toString(byteBuffer, s);
    }

    public String getString() {
        return new String(byteBuffer);
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String s) {
        this.fieldName = s;
    }

    public boolean isFormField() {
        return formField;
    }

    public void setFormField(boolean b) {
        formField = b;
    }

    public byte[] getByteBuffer() {
        return byteBuffer;
    }

    public void setByteBuffer(byte[] byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileItemHeaders getFileItemHeaders() {
        return fileItemHeaders;
    }

    public void setFileItemHeaders(FileItemHeaders fileItemHeaders) {
        this.fileItemHeaders = fileItemHeaders;
    }
}
