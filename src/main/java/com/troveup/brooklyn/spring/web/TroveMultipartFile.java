package com.troveup.brooklyn.spring.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Created by tim on 5/20/15.
 */
@SuppressWarnings("serial")
public class TroveMultipartFile implements MultipartFile, Serializable
{

    protected static final Logger logger = LoggerFactory.getLogger(TroveMultipartFile.class);

    private final TroveFileItem troveFileItem;
    private final long size;

    public TroveMultipartFile(TroveFileItem fileItem)
    {
        this.troveFileItem = fileItem;
        this.size = fileItem.getSize();
    }

    @Override
    public String getName() {
        return troveFileItem.getName();
    }

    @Override
    public String getOriginalFilename() {
        return troveFileItem.getName();
    }

    @Override
    public String getContentType() {
        return troveFileItem.getContentType();
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return troveFileItem.get();
    }

    public String getString()
    {
        return troveFileItem.getString();
    }

    public String getString(String encoding) throws UnsupportedEncodingException, IOException
    {
        return troveFileItem.getString(encoding);
    }

    public String getFieldName()
    {
        return troveFileItem.getFieldName();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(troveFileItem.get());
    }

    @Override
    public void transferTo(File file) throws IOException, IllegalStateException {
        //Unimplemented because we're not dealing with physical files on GAE.
    }
}
