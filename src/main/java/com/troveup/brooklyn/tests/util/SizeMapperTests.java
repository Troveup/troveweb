package com.troveup.brooklyn.tests.util;

import com.troveup.brooklyn.util.SizeMapper;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by tim on 11/2/15.
 */
public class SizeMapperTests
{
    @Test
    public void testMediumExportSize()
    {
        //Ensure that all sizes return correctly for medium exporter sizes
        Assert.assertTrue(SizeMapper.getMediumExportSize("bracelet").equals("bracelet_3"));
        Assert.assertTrue(SizeMapper.getMediumExportSize("necklace").equals("necklace_2"));
        Assert.assertTrue(SizeMapper.getMediumExportSize("ring").equals("ring_7.0"));

        Assert.assertTrue(SizeMapper.getMediumExportSize("bracelet".toUpperCase()).equals("bracelet_3"));
        Assert.assertTrue(SizeMapper.getMediumExportSize("necklace".toUpperCase()).equals("necklace_2"));
        Assert.assertTrue(SizeMapper.getMediumExportSize("ring".toUpperCase()).equals("ring_7.0"));
    }

    @Test
    public void testGetStandardizedSizes()
    {
        //First check the new way of converting from a customizer size to a human readable one
        Assert.assertTrue(SizeMapper.getStandardizedSize("ring_7.0").equals("7.0"));
        Assert.assertTrue(SizeMapper.getStandardizedSize("bracelet_2").equals("Small"));
        Assert.assertTrue(SizeMapper.getStandardizedSize("necklace_2").equals("Medium"));

        //Next, check to be sure that non-customizer sizes are just simply passed through
        Assert.assertTrue(SizeMapper.getStandardizedSize("6.0").equals("6.0"));
        Assert.assertTrue(SizeMapper.getStandardizedSize("Medium").equals("Medium"));
        Assert.assertTrue(SizeMapper.getStandardizedSize("small").equals("small"));
        Assert.assertTrue(SizeMapper.getStandardizedSize("extra-small").equals("extra-small"));
    }

    @Test
    public void testGetExporterSizes()
    {
        Assert.assertTrue(SizeMapper.getExporterSize("ring", "ring_7.5").equals("ring_7.5"));
        Assert.assertTrue(SizeMapper.getExporterSize("ring", "8.0").equals("ring_8.0"));
        Assert.assertTrue(SizeMapper.getExporterSize("bracelet", "bracelet_2").equals("bracelet_2"));
        Assert.assertTrue(SizeMapper.getExporterSize("bracelet", "extra-small").equals("bracelet_1"));
        Assert.assertTrue(SizeMapper.getExporterSize("bracelet", "Medium").equals("bracelet_3"));
        Assert.assertTrue(SizeMapper.getExporterSize("necklace", "necklace_2").equals("necklace_2"));
        Assert.assertTrue(SizeMapper.getExporterSize("necklace", null).equals("necklace_0"));

        Assert.assertTrue(SizeMapper.getExporterSize("ring".toUpperCase(), "ring_7.5").equals("ring_7.5"));
        Assert.assertTrue(SizeMapper.getExporterSize("ring".toUpperCase(), "8.0").equals("ring_8.0"));
        Assert.assertTrue(SizeMapper.getExporterSize("bracelet".toUpperCase(), "bracelet_2").equals("bracelet_2"));
        Assert.assertTrue(SizeMapper.getExporterSize("bracelet".toUpperCase(), "extra-small").equals("bracelet_1"));
        Assert.assertTrue(SizeMapper.getExporterSize("bracelet".toUpperCase(), "Medium").equals("bracelet_3"));
        Assert.assertTrue(SizeMapper.getExporterSize("necklace".toUpperCase(), "necklace_2").equals("necklace_2"));
    }
}
