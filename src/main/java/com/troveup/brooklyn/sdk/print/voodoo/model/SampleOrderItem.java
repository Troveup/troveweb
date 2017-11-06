package com.troveup.brooklyn.sdk.print.voodoo.model;


import com.troveup.brooklyn.orm.ftui.model.FtueOrderItem;

/**
 * Created by tim on 6/18/15.
 */
public class SampleOrderItem
{
    private int id;
    private SampleModel model;
    private int material;
    private int qty;

    public SampleOrderItem()
    {

    }

    public SampleOrderItem(SampleModel model, int material, int qty)
    {
        this.model = model;
        this.material = material;
        this.qty = qty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SampleModel getModel() {
        return model;
    }

    public void setModel(SampleModel model) {
        this.model = model;
    }

    public int getMaterial() {
        return material;
    }

    public void setMaterial(int material) {
        this.material = material;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public FtueOrderItem toFtueOrderItem()
    {
        FtueOrderItem rval;
        if (model != null)
            rval = model.toFtueOrderItem();
        else
            rval = new FtueOrderItem();
        rval.setMaterial(material);
        rval.setQuantity(qty);
        rval.setFtueResponseItemId((long) id);

        return rval;
    }
}
