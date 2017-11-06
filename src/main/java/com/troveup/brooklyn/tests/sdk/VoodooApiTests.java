package com.troveup.brooklyn.tests.sdk;

import com.troveup.brooklyn.sdk.print.interfaces.ISampleSupplier;
import com.troveup.brooklyn.sdk.print.voodoo.model.*;
import com.troveup.config.PersistenceConfig;
import com.troveup.config.SDKConfig;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 6/18/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SDKConfig.class, PersistenceConfig.class})
public class VoodooApiTests
{
    @Autowired
    ISampleSupplier sampleSupplier;

    @Test
    public void testGetMaterials()
    {
        List<SampleMaterial> materials = sampleSupplier.getAvailableMaterials();

        Assert.assertNotNull(materials);
        Assert.assertTrue(materials.size() > 0);
    }

    @Test
    public void testSubmitVoodooOrder()
    {
        SampleAddress address = new SampleAddress("New York", "ALI CHASTAIN", "10019", "313 W 54TH street", "Apt 5W", "NY",
                "USA");
        SampleModel model = new SampleModel("https://storage.googleapis.com/troveup-production-cdn/warrior_ring-sample-f21f9940-a44d-4314-8994-ca8349b1ea69.obj", "mm");
        SampleOrderItem item = new SampleOrderItem(model, 8, 1);

        List<SampleOrderItem> container = new ArrayList<>();
        container.add(item);

        SampleCreateOrderResponse response = sampleSupplier.createSampleOrder(container, address);

        Assert.assertNotNull(response);
        Assert.assertNull(response.getErr_type());
        Assert.assertNull(response.getMessage());
    }

    @Test
    public void testSubmitFaultyVoodooOrder()
    {
        SampleAddress address = new SampleAddress("New York", "Tim Growney", "10005", "20 Exchange Pl", null, "NY",
                "USA");
        SampleModel model = new SampleModel("http://storage.googleapis.com/trove-qa-teststore/" +
                "double_block-simplified-ecc80792-482e-4f23-952e-af1c36edfee5.obj", "mm");
        SampleOrderItem item = new SampleOrderItem(model, 8, 1);

        List<SampleOrderItem> container = new ArrayList<>();
        container.add(item);

        SampleCreateOrderResponse response = sampleSupplier.createSampleOrder(container, address);

        Assert.assertNotNull(response.getErr_type());
        Assert.assertNotNull(response.getMessage());
    }
}
