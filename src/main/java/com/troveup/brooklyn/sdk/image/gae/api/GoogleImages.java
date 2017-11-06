package com.troveup.brooklyn.sdk.image.gae.api;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.troveup.brooklyn.controllers.trove.CommonController;
import com.troveup.brooklyn.util.ErrorUtils;

/**
 * Created by tim on 6/19/15.
 */
public class GoogleImages
{
    public byte[] resizeImage(byte[] image, int width, int height)
    {
        byte[] rval = null;
        try {
            ImagesService imageService = ImagesServiceFactory.getImagesService();

            Image oldImage = ImagesServiceFactory.makeImage(image);
            Transform resize = ImagesServiceFactory.makeResize(width, height);

            Image newImage = imageService.applyTransform(resize, oldImage);

            rval = newImage.getImageData();
        }
        catch (Exception e)
        {
            ErrorUtils.logError(e);
        }

        return rval;
    }
}
