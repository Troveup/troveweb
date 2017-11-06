package com.troveup.brooklyn.sdk.print.interfaces;

import com.troveup.brooklyn.sdk.print.voodoo.model.SampleAddress;
import com.troveup.brooklyn.sdk.print.voodoo.model.SampleCreateOrderResponse;
import com.troveup.brooklyn.sdk.print.voodoo.model.SampleMaterial;
import com.troveup.brooklyn.sdk.print.voodoo.model.SampleOrderItem;

import java.util.List;

/**
 * Created by tim on 6/18/15.
 */
public interface ISampleSupplier
{
    List<SampleMaterial> getAvailableMaterials();
    SampleCreateOrderResponse createSampleOrder(List<SampleOrderItem> items, SampleAddress address);
}
