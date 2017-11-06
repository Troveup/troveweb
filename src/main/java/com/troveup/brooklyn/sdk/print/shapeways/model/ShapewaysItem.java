package com.troveup.brooklyn.sdk.print.shapeways.model;

/**
 * Created by tim on 7/3/15.
 */
public class ShapewaysItem
{
    private Integer modelId;
    private Integer materialId;
    private Integer quantity;
    private Integer productDimensionChoiceCombo;

    public ShapewaysItem()

    {
        quantity = 1;
        productDimensionChoiceCombo = 0;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getProductDimensionChoiceCombo() {
        return productDimensionChoiceCombo;
    }

    public void setProductDimensionChoiceCombo(Integer productDimensionChoiceCombo) {
        this.productDimensionChoiceCombo = productDimensionChoiceCombo;
    }
}
