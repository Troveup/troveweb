package com.troveup.brooklyn.sdk.image.interfaces;

/**
 * Created by tim on 6/19/15.
 */
public interface IImageManipProvider
{
    byte[] resizeImage(byte[] imageData, int width, int height);
}
