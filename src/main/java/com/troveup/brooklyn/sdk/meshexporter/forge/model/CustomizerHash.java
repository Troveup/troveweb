package com.troveup.brooklyn.sdk.meshexporter.forge.model;

import com.troveup.brooklyn.orm.ftui.model.FtueModelWeights;
import com.troveup.brooklyn.orm.item.model.ItemCustomization;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 8/19/15.
 */
public class CustomizerHash
{
    //FORGE exporter fields
    private Boolean includeNormals;
    private String tempRoot;
    private Boolean isLocal;
    private String exportBucket;
    private String jsonUrl;
    private String jsonFileName;
    private String material;
    private String volume;
    private String modelName;
    private Boolean isLocalDebug;

    //Blender exporter fields
    private String importUrl;
    private List<String> visible;
    private String size;
    private List<CustomizerOperator> operators;
    private String importBucket;
    private String importPath;
    private String exportPath;
    private Boolean enableRender;



    public CustomizerHash()
    {

    }

    public CustomizerHash(List<String> visible, List<CustomizerOperator> operators, String material)
    {
        this.visible = visible;
        this.operators = operators;
        this.material = material;
    }

    public CustomizerHash(List<String> visible, String size, List<CustomizerOperator> operators, String importBucket,
                          String importPath, String exportBucket, String exportPath, Boolean enableRender)
    {
        this.visible = visible;
        this.size = size;
        this.operators = operators;
        this.importBucket = importBucket;
        this.importPath = importPath;
        this.exportPath = exportPath;
        this.enableRender = enableRender;
        this.exportBucket = exportBucket;
    }

    public CustomizerHash(List<String> visible, String size, List<CustomizerOperator> operators, String importUrl, String exportBucket,
                          String exportPath, Boolean enableRender)
    {
        this.visible = visible;
        this.size = size;
        this.operators = operators;
        this.importUrl = importUrl;
        this.exportPath = exportPath;
        this.enableRender = enableRender;
        this.exportBucket = exportBucket;
    }

    public CustomizerHash(CustomizerHash hash)
    {
        this.includeNormals = hash.getIncludeNormals();
        this.tempRoot = hash.getTempRoot();
        this.isLocal = hash.getIsLocal();
        this.exportBucket = hash.getExportBucket();
        this.jsonUrl = hash.getJsonUrl();
        this.jsonFileName = hash.getJsonFileName();
        this.material = hash.getMaterial();
        this.volume = hash.getVolume();
        this.visible = hash.getVisible();
        this.operators = hash.getOperators();
        this.size = hash.getSize();
        this.modelName = hash.getModelName();
        this.exportPath = hash.getExportPath();
        this.isLocalDebug = hash.getIsLocalDebug();
        this.importBucket = hash.getImportBucket();
        this.importPath = hash.getImportPath();
        this.enableRender = hash.getEnableRender();
    }

    public CustomizerHash(String jsonUrl, String modelName, String exportBucket, String exportPath,
                          List<ItemCustomization> customizations, String finishMaterialPrimaryKey, String defaultSize)
    {
        this.jsonUrl = jsonUrl;
        this.modelName = modelName;
        this.exportBucket = exportBucket;
        this.exportPath = exportPath;

        Integer setNumber = ItemCustomization.getLatestSetNumber(customizations);
        this.visible = ItemCustomization.toVisibleMeshes(customizations, setNumber);
        this.operators = CustomizerOperator.toCustomizerOperators(ItemCustomization.toCustomizerWeights(customizations, setNumber));
        this.size = ItemCustomization.toSize(customizations, setNumber, defaultSize);
        this.material = finishMaterialPrimaryKey;
    }

    public String getExportBucket() {
        return exportBucket;
    }

    public void setExportBucket(String exportBucket) {
        this.exportBucket = exportBucket;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public List<String> getVisible() {
        return visible;
    }

    public void setVisible(List<String> visible) {
        this.visible = visible;
    }

    public List<CustomizerOperator> getOperators() {
        return operators;
    }

    public void setOperators(List<CustomizerOperator> operators) {
        this.operators = operators;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Boolean getIncludeNormals() {
        return includeNormals;
    }

    public void setIncludeNormals(Boolean includeNormals) {
        this.includeNormals = includeNormals;
    }

    public String getTempRoot() {
        return tempRoot;
    }

    public void setTempRoot(String tempRoot) {
        this.tempRoot = tempRoot;
    }

    public Boolean getIsLocal() {
        return isLocal;
    }

    public void setIsLocal(Boolean isLocal) {
        this.isLocal = isLocal;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getExportPath() {
        return exportPath;
    }

    public void setExportPath(String exportPath) {
        this.exportPath = exportPath;
    }

    public String getJsonUrl() {
        return jsonUrl;
    }

    public void setJsonUrl(String jsonUrl) {
        this.jsonUrl = jsonUrl;
    }

    public String getJsonFileName() {
        return jsonFileName;
    }

    public void setJsonFileName(String jsonFileName) {
        this.jsonFileName = jsonFileName;
    }

    public Boolean getIsLocalDebug() {
        return isLocalDebug;
    }

    public void setIsLocalDebug(Boolean isLocalDebug) {
        this.isLocalDebug = isLocalDebug;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getImportBucket() {
        return importBucket;
    }

    public void setImportBucket(String importBucket) {
        this.importBucket = importBucket;
    }

    public String getImportPath() {
        return importPath;
    }

    public void setImportPath(String importPath) {
        this.importPath = importPath;
    }

    public Boolean getEnableRender() {
        return enableRender;
    }

    public void setEnableRender(Boolean enableRender) {
        this.enableRender = enableRender;
    }

    public String getImportUrl() {
        return importUrl;
    }

    public void setImportUrl(String importUrl) {
        this.importUrl = importUrl;
    }

    public List<FtueModelWeights> getCustomizerOperatorsAsFtueModelWeights()
    {
        List<FtueModelWeights> rval = new ArrayList<>();

        if (operators != null) {
            for (CustomizerOperator operator : getOperators()) {
                rval.add(new FtueModelWeights(operator.getId(), operator.getValue()));
            }
        }

        if (visible != null) {
            for (String visibleMesh : visible) {
                rval.add(new FtueModelWeights(visibleMesh));
            }
        }

        return rval;
    }

    public List<ItemCustomization> getCustomizerOperatorsAsItemCustomizations(Integer iteration)
    {
        List<ItemCustomization> rval = new ArrayList<>();

        if (operators != null)
        {
            for (CustomizerOperator operator : getOperators()) {
                ItemCustomization customization = new ItemCustomization(operator.getId(), operator.getValue());
                customization.setSetNumber(iteration);
                rval.add(customization);
            }
        }

        if (visible != null)
        {
            for (String visibleMesh : visible)
            {
                ItemCustomization customization = new ItemCustomization(visibleMesh);
                customization.setSetNumber(iteration);
                rval.add(customization);
            }
        }

        return rval;
    }
}
