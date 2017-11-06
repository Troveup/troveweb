package com.troveup.brooklyn.sdk.print.voodoo.business;

import com.troveup.brooklyn.sdk.print.interfaces.ISampleSupplier;
import com.troveup.brooklyn.sdk.print.voodoo.api.VoodooApi;
import com.troveup.brooklyn.sdk.print.voodoo.model.*;

import java.util.List;

/**
 * Created by tim on 6/18/15.
 */
public class VoodooSampleSupplier implements ISampleSupplier
{
    private final VoodooApi voodooApi;

    public VoodooSampleSupplier(VoodooApi voodooApi)
    {
        this.voodooApi = voodooApi;
    }

    @Override
    public List<SampleMaterial> getAvailableMaterials()
    {
        return voodooApi.getAvailableMaterials();
    }

    @Override
    public SampleCreateOrderResponse createSampleOrder(List<SampleOrderItem> items, SampleAddress address)
    {
        SampleOrderRequest request = new SampleOrderRequest();
        request.setOrder_items(items);
        request.setShipping_info(address);

        return voodooApi.submitSampleOrder(request);
    }
}
