package com.troveup.brooklyn.sdk.print.voodoo.model;

import com.troveup.brooklyn.orm.ftui.model.FtueOrderItem;

/**
 * Created by tim on 6/18/15.
 */
public class SampleModel
{
    private String file_url;
    private String dimensions;

    public SampleModel()
    {

    }

    public SampleModel(String file_url, String dimensions)
    {
        this.file_url = file_url;
        this.dimensions = dimensions;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public FtueOrderItem toFtueOrderItem()
    {
        FtueOrderItem rval = new FtueOrderItem();

        rval.setFile_url(file_url);
        rval.setUnits(dimensions);

        return rval;
    }
}
