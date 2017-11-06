package com.troveup.brooklyn.sdk.cdn.interfaces;

import com.google.appengine.tools.cloudstorage.GcsFileMetadata;
import com.troveup.brooklyn.sdk.cdn.model.CloudParam;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by tim on 5/20/15.
 */
public interface CloudStore
{
    //Not working yet, need to figure out how to write a full on object without using the object stream wrapper,
    //as it pads with extra bytes in the beginning of the file.
    //Boolean writeFile(Object file, String fileName, List<CloudParam> connectionParams);
    Boolean writeBytes(byte[] arrayOfBytes, String fileName, List<CloudParam> connectionParams);
    ByteBuffer readFile(String fileName, List<CloudParam> connectionParams);
    GcsFileMetadata readFileMetadata(String fileName, List<CloudParam> connectionParams);
}
