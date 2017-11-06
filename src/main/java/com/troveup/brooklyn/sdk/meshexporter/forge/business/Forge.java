package com.troveup.brooklyn.sdk.meshexporter.forge.business;

import com.troveup.brooklyn.sdk.common.api.CommonBusiness;
import com.troveup.brooklyn.sdk.meshexporter.forge.api.ForgeExporter;
import com.troveup.brooklyn.sdk.meshexporter.forge.model.*;

import java.util.List;

/**
 * Created by tim on 8/29/15.
 */
public class Forge extends CommonBusiness
{
    private final ForgeExporter forgeExporter;
    private String exportBucket;
    private String exportPath;
    private final Boolean isLocalDebug;

    public Forge(ForgeExporter forgeExporter, String exportBucket, String exportPath)
    {
        this.exportBucket = exportBucket;
        this.forgeExporter = forgeExporter;

        if (exportPath != null && exportPath.length() > 0)
            this.exportPath = exportPath + "/";
        else
            this.exportPath = "";

        isLocalDebug = false;
    }

    public Forge(ForgeExporter forgeExporter, String exportBucket, String exportPath, Boolean isLocalDebug)
    {
        this.exportBucket = exportBucket;
        this.forgeExporter = forgeExporter;

        if (exportPath != null && exportPath.length() > 0)
            this.exportPath = exportPath + "/";
        else
            this.exportPath = "";

        this.isLocalDebug = isLocalDebug;
    }

    public ForgeMeshExportResponse exportMesh(List<CustomizerOperator> operatorList, List<String> visibleMeshes,
                                              String size, Boolean isRenderExport, String jsonFileUrl,
                                              String exportFileName)
    {
        CustomizerHash hash = new CustomizerHash();
        hash.setOperators(operatorList);
        hash.setVisible(visibleMeshes);
        hash.setSize(size);
        hash.setIncludeNormals(isRenderExport);
        hash.setIsLocal(false);
        hash.setIsLocalDebug(isLocalDebug);
        hash.setJsonUrl(jsonFileUrl);

        String jsonFileNameSplit[] = jsonFileUrl.split("/");
        hash.setJsonFileName(jsonFileNameSplit[jsonFileNameSplit.length - 1]);
        hash.setModelName(exportFileName);
        hash.setExportPath(exportPath + exportFileName);
        hash.setExportBucket(exportBucket);

        ForgeMeshExportRequest request = new ForgeMeshExportRequest(hash);
        return forgeExporter.exportMesh(request);
    }

    public ForgeMeshExportResponse exportMesh(List<CustomizerOperator> operatorList, List<String> visibleMeshes,
                                              String size, Boolean isRenderExport, String jsonFileUrl,
                                              String exportFileName, Boolean isLocal)
    {
        CustomizerHash hash = new CustomizerHash();
        hash.setOperators(operatorList);
        hash.setVisible(visibleMeshes);
        hash.setSize(size);
        hash.setIncludeNormals(isRenderExport);
        hash.setIsLocal(isLocal);
        hash.setIsLocalDebug(isLocalDebug);
        hash.setJsonUrl(jsonFileUrl);

        String jsonFileNameSplit[] = jsonFileUrl.split("/");
        hash.setJsonFileName(jsonFileNameSplit[jsonFileNameSplit.length - 1]);
        hash.setModelName(exportFileName);
        hash.setExportPath(exportPath + exportFileName);
        hash.setExportBucket(exportBucket);

        ForgeMeshExportRequest request = new ForgeMeshExportRequest(hash);
        return forgeExporter.exportMesh(request);
    }

    public ForgeMeshExportResponse exportBlenderMesh(List<String> visible, String size, List<CustomizerOperator> operators, String importUrl, String exportBucket,
                                                     String exportPath, Boolean enableRender)
    {
        CustomizerHash hash = new CustomizerHash();
        hash.setOperators(operators);
        hash.setVisible(visible);
        hash.setSize(size);
        hash.setImportUrl(importUrl);
        hash.setExportBucket(exportBucket);
        hash.setExportPath(exportPath);
        hash.setEnableRender(enableRender);

        ForgeMeshExportRequest request = new ForgeMeshExportRequest(hash);
        return forgeExporter.exportMesh(request);
    }

    public ForgeMeshExportResponse exportBlenderMesh(List<String> visible, String size, List<CustomizerOperator> operators, String importUrl, String exportBucket, String importBucket,
                                                     String exportPath, Boolean enableRender)
    {
        CustomizerHash hash = new CustomizerHash();
        hash.setOperators(operators);
        hash.setVisible(visible);
        hash.setSize(size);
        hash.setImportUrl(importUrl);
        hash.setExportBucket(exportBucket);
        hash.setExportPath(exportPath);
        hash.setEnableRender(enableRender);
        hash.setImportBucket(importBucket);

        ForgeMeshExportRequest request = new ForgeMeshExportRequest(hash);
        return forgeExporter.exportMesh(request);
    }

    public ForgeMeshVolumeResponse getVolume(List<CustomizerOperator> operatorList, List<String> visibleMeshes,
                                             String size, String jsonFileUrl)
    {
        CustomizerHash hash = new CustomizerHash();
        hash.setOperators(operatorList);
        hash.setVisible(visibleMeshes);
        hash.setSize(size);
        hash.setJsonUrl(jsonFileUrl);

        ForgeMeshVolumeRequest request = new ForgeMeshVolumeRequest(hash);

        return forgeExporter.getMeshVolume(request);
    }
}
