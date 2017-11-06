package com.troveup.brooklyn.model;

import com.troveup.brooklyn.orm.cart.model.CartItem;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.sdk.meshexporter.forge.model.CustomizerOperator;
import com.troveup.brooklyn.sdk.meshexporter.java.model.ModelJson;
import com.troveup.brooklyn.sdk.meshexporter.java.model.ParameterJson;

import java.util.List;

/**
 * Created by tim on 7/26/15.
 */
public class ManufacturerUpload
{
    private Item item;
    private CartItem orderCartItem;
    private List<CustomizerOperator> weights;
    private List<String> visibleMeshes;
    private String size;
    private String uploadFileName;
    private String checkoutSystem;
    private String modelUrl;
    private byte[] exportedModel;

    public ManufacturerUpload()
    {

    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public byte[] getExportedModel() {
        return exportedModel;
    }

    public void setExportedModel(byte[] exportedModel) {
        this.exportedModel = exportedModel;
    }

    public CartItem getOrderCartItem() {
        return orderCartItem;
    }

    public void setOrderCartItem(CartItem orderCartItem) {
        this.orderCartItem = orderCartItem;
    }

    public String getCheckoutSystem() {
        return checkoutSystem;
    }

    public void setCheckoutSystem(String checkoutSystem) {
        this.checkoutSystem = checkoutSystem;
    }

    public List<CustomizerOperator> getWeights() {
        return weights;
    }

    public void setWeights(List<CustomizerOperator> weights) {
        this.weights = weights;
    }

    public List<String> getVisibleMeshes() {
        return visibleMeshes;
    }

    public void setVisibleMeshes(List<String> visibleMeshes) {
        this.visibleMeshes = visibleMeshes;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getModelUrl() {
        return modelUrl;
    }

    public void setModelUrl(String modelUrl) {
        this.modelUrl = modelUrl;
    }
}
