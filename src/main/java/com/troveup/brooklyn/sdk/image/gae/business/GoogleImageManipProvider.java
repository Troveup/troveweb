package com.troveup.brooklyn.sdk.image.gae.business;

import com.troveup.brooklyn.sdk.image.gae.api.GoogleImages;
import com.troveup.brooklyn.sdk.image.interfaces.IImageManipProvider;

/**
 * Created by tim on 6/19/15.
 */
public class GoogleImageManipProvider implements IImageManipProvider
{
    GoogleImages googleImages;

    public GoogleImageManipProvider(GoogleImages googleImages)
    {
        this.googleImages = googleImages;
    }

    @Override
    public byte[] resizeImage(byte[] imageData, int width, int height)
    {
        return googleImages.resizeImage(imageData, width, height);
    }
}
