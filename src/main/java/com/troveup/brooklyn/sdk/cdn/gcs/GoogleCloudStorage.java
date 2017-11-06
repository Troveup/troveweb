package com.troveup.brooklyn.sdk.cdn.gcs;

import com.google.appengine.tools.cloudstorage.*;
import com.troveup.brooklyn.sdk.cdn.interfaces.CloudStore;
import com.troveup.brooklyn.sdk.cdn.model.CloudParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.List;

/**
 * Created by tim on 5/20/15.
 */
public class GoogleCloudStorage implements CloudStore
{
    Logger logger = LoggerFactory.getLogger(GoogleCloudStorage.class);
    public static final String CDN_URL_BASE_PATH = "https://storage.googleapis.com/";
    public static final String CDN_BUCKET_NAME = "trove-qa-teststore/";
    public static final String CDN_JSON_BUCKET_NAME = "troveup-qa-cdn";
    public static final String KEY_BUCKET_NAME = "KEY_BUCKET_NAME";
    public static final String KEY_MIME_TYPE = "KEY_MIME_TYPE";
    public static final String KEY_CONTENT_ENCODING = "KEY_CONTENT_ENCODING";

    public static final String MIME_TYPE_PNG = "image/png";

    /*@Override
    public Boolean writeFile(Object file, String fileName, List<CloudParam> connectionParams)
    {
        Boolean rval = false;
        ObjectOutputStream outputStream = null;
        try
        {
            if (file == null || fileName == null || fileName.length() == 0 || connectionParams == null || connectionParams.size() == 0)
                throw new IllegalArgumentException("Arguments cannot be null or empty.");

            CloudParam bucketName = getCloudParam(KEY_BUCKET_NAME, connectionParams);
            CloudParam mimeType = getCloudParam(KEY_MIME_TYPE, connectionParams);
            CloudParam contentEncoding = getCloudParam(KEY_CONTENT_ENCODING, connectionParams);

            if (bucketName != null)
            {
                GcsFileOptions options = GcsFileOptions.getDefaultInstance();


                if (mimeType != null || contentEncoding != null)
                {
                    GcsFileOptions.Builder builder = new GcsFileOptions.Builder();

                    if (mimeType != null)
                        builder.mimeType(mimeType.getParamValue());
                    if (contentEncoding != null)
                        builder.contentEncoding(contentEncoding.getParamValue());

                    options = builder.build();
                }

                GcsService gcsService = GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());

                GcsFilename gcsBucketFileInfo = new GcsFilename(bucketName.getParamValue(), fileName);
                GcsOutputChannel outputChannel = gcsService.createOrReplace(gcsBucketFileInfo, options);

                OutputStream stream = Channels.newOutputStream(outputChannel);
                stream.write(file);
                stream.close();

                rval = true;
            }
            else
            {
                logger.warn("Request to download file " + fileName + " failed due to a missing connectionParam " +
                        "specifying the bucket name.");
            }

        } catch (Exception e)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Stack Trace: " + sw.toString());
        }
        finally
        {
            if (outputStream != null)
                try {
                    outputStream.close();
                } catch (Exception e)
                {
                    //at least we tried...
                }
        }

        return rval;
    }*/

    @Override
    public Boolean writeBytes(byte[] arrayOfBytes, String fileName, List<CloudParam> connectionParams) {
        Boolean rval = false;
        ObjectOutputStream outputStream = null;
        try
        {
            if (arrayOfBytes == null || arrayOfBytes.length == 0 || fileName == null || fileName.length() == 0 ||
                    connectionParams == null || connectionParams.size() == 0)
                throw new IllegalArgumentException("Arguments cannot be null or empty.");

            CloudParam bucketName = getCloudParam(KEY_BUCKET_NAME, connectionParams);
            CloudParam mimeType = getCloudParam(KEY_MIME_TYPE, connectionParams);
            CloudParam contentEncoding = getCloudParam(KEY_CONTENT_ENCODING, connectionParams);

            if (bucketName != null)
            {
                GcsFileOptions options = GcsFileOptions.getDefaultInstance();


                if (mimeType != null || contentEncoding != null)
                {
                    GcsFileOptions.Builder builder = new GcsFileOptions.Builder();

                    if (mimeType != null)
                        builder.mimeType(mimeType.getParamValue());
                    if (contentEncoding != null)
                        builder.contentEncoding(contentEncoding.getParamValue());

                    options = builder.build();
                }

                GcsService gcsService = GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());

                GcsFilename gcsBucketFileInfo = new GcsFilename(bucketName.getParamValue(), fileName);
                GcsOutputChannel outputChannel = gcsService.createOrReplace(gcsBucketFileInfo, options);

                logger.debug("Output channel size in bytes: " + outputChannel.getBufferSizeBytes());

                //Using ObjectOutputStream causes extra padding to be added:
                //See http://docs.oracle.com/javase/7/docs/platform/serialization/spec/protocol.html#8299
                //outputStream = new ObjectOutputStream(Channels.newOutputStream(outputChannel));

                OutputStream stream = Channels.newOutputStream(outputChannel);
                stream.write(arrayOfBytes);
                stream.close();

                rval = true;
            }
            else
            {
                logger.warn("Request to upload file " + fileName + " failed due to a missing connectionParam " +
                        "specifying the bucket name.");
            }

        } catch (Exception e)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Stack Trace: " + sw.toString());
        }
        finally
        {
            if (outputStream != null)
                try {
                    outputStream.close();
                } catch (Exception e)
                {
                    //at least we tried...
                }
        }

        return rval;
    }

    @Override
    public ByteBuffer readFile(String fileName, List<CloudParam> connectionParams)
    {
        ByteBuffer rval = null;
        GcsInputChannel readChannel = null;
        try
        {
            if (fileName == null || fileName == null || fileName.length() == 0 || connectionParams == null || connectionParams.size() == 0)
                throw new IllegalArgumentException("Arguments cannot be null or empty.");

            CloudParam bucketName = getCloudParam(KEY_BUCKET_NAME, connectionParams);

            if (bucketName != null)
            {
                GcsService gcsService = GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());
                GcsFilename gcsBucketFileInfo = new GcsFilename(bucketName.getParamValue(), fileName);

                int fileSize = (int) gcsService.getMetadata(gcsBucketFileInfo).getLength();

                readChannel = gcsService.openReadChannel(gcsBucketFileInfo, 0);

                rval = ByteBuffer.allocate(fileSize);
                readChannel.read(rval);
            }
            else
            {
                logger.warn("Request to upload file " + fileName + " failed due to a missing connectionParam " +
                        "specifying the bucket name.");
            }

        } catch (Exception e)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Stack Trace: " + sw.toString());
        }
        finally
        {
            if (readChannel != null)
                try {
                    readChannel.close();
                } catch (Exception e)
                {
                    //at least we tried...
                }
        }

        return rval;
    }

    @Override
    public GcsFileMetadata readFileMetadata(String fileName, List<CloudParam> connectionParams) {

        final GcsService gcsService =
                GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());

        GcsFileMetadata rval = null;

        try
        {
            if (fileName == null || connectionParams == null || connectionParams.size() == 0)
                throw new IllegalArgumentException("Arguments cannot be null or empty.");

            CloudParam bucketNameParam = getCloudParam(KEY_BUCKET_NAME, connectionParams);
            GcsFilename gcsBucketFileName = new GcsFilename(bucketNameParam.getParamValue(), fileName);

            rval = gcsService.getMetadata(gcsBucketFileName);

        } catch (Exception e)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Stack Trace: " + sw.toString());
        }

        return rval;
    }

    private CloudParam getCloudParam(String paramName, List<CloudParam> params)
    {
        CloudParam rval = null;
        CloudParam searchParam = new CloudParam();
        searchParam.setParamName(paramName);

        int findIndex = params.indexOf(searchParam);

        if (findIndex > -1)
            rval = params.get(findIndex);

        return rval;
    }
}
