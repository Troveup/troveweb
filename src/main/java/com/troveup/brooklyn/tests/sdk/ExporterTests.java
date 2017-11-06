package com.troveup.brooklyn.tests.sdk;

import com.google.gson.Gson;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.ftui.impl.FtueAccessor;
import com.troveup.brooklyn.orm.ftui.model.FtueModelWeights;
import com.troveup.brooklyn.orm.ftui.model.FtueOrderItem;
import com.troveup.brooklyn.orm.ftui.model.FtuePersistedRecord;
import com.troveup.brooklyn.orm.item.interfaces.IItemAccessor;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.item.model.ItemAttribute;
import com.troveup.brooklyn.sdk.http.impl.HttpClient;
import com.troveup.brooklyn.sdk.http.interfaces.IHttpClientFactory;
import com.troveup.brooklyn.sdk.meshexporter.forge.api.ForgeExporter;
import com.troveup.brooklyn.sdk.meshexporter.forge.business.Forge;
import com.troveup.brooklyn.sdk.meshexporter.forge.model.*;
import com.troveup.brooklyn.sdk.meshexporter.java.api.GeometryAnalysis;
import com.troveup.brooklyn.sdk.meshexporter.java.business.MeshExport;
import com.troveup.brooklyn.sdk.meshexporter.java.model.ModelJson;
import com.troveup.brooklyn.sdk.meshexporter.java.model.OperatorWeightJson;
import com.troveup.brooklyn.sdk.meshexporter.java.model.ParameterJson;
import com.troveup.brooklyn.sdk.print.interfaces.IPrintSupplier;
import com.troveup.brooklyn.sdk.print.voodoo.model.SampleCreateOrderResponse;
import com.troveup.brooklyn.sdk.print.voodoo.model.SampleModel;
import com.troveup.brooklyn.sdk.print.voodoo.model.SampleOrderItem;
import com.troveup.brooklyn.util.SizeMapper;
import com.troveup.brooklyn.util.models.UrlResponse;
import com.troveup.brooklyn.util.WorkerQueuer;
import com.troveup.config.PersistenceConfig;
import com.troveup.config.SDKConfig;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by tim on 6/23/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, SDKConfig.class})
public class ExporterTests
{
    @Autowired
    Gson gson;

    @Autowired
    MeshExport exporter;

    @Autowired
    IPrintSupplier printSupplier;

    @Autowired
    Forge forgeExporter;

    @Autowired
    ForgeExporter exporterApi;

    @Autowired
    FtueAccessor ftueAccessor;

    @Autowired
    IItemAccessor itemAccessor;

    @Autowired
    IHttpClientFactory clientFactory;

    @Test
    public void testParser()
    {
        String json = "{\n" +
                "    \"renderGroups\": [\n" +
                "        {\n" +
                "            \"id\": \"main\",\n" +
                "            \"label\": \"main\",\n" +
                "            \"meshes\": [ \"main.cube\" ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"meshes\": [\n" +
                "        {\n" +
                "            \"id\": \"main.cube\",\n" +
                "            \"metadata\" : {\n" +
                "                \"formatVersion\" : 1,\n" +
                "                \"generatedBy\"   : \"Blender 2.73 Exporter Trove Format\",\n" +
                "                \"vertices\"      : 8,\n" +
                "                \"faces\"         : 12,\n" +
                "                \"normals\"       : 8,\n" +
                "                \"uvs\"           : 0,\n" +
                "                \"morphTargets\"  : 0\n" +
                "            },\n" +
                "            \"scale\" : 1.000000,\n" +
                "            \"vertices\" : [1,1,-1,1,-1,-1,-1,-1,-1,-1,1,-1,1,0.999999,1,0.999999,-1,1,-1,-1,1,-1,1,1],\n" +
                "            \"normals\" : [0.577349,0.577349,-0.577349,0.577349,-0.577349,-0.577349,-0.577349,-0.577349,-0.577349,0.577349,0.577349,0.577349,-0.577349,0.577349,0.577349,-0.577349,-0.577349,0.577349,0.577349,-0.577349,0.577349,-0.577349,0.577349,-0.577349],\n" +
                "            \"faces\" : [32,0,1,2,0,1,2,32,4,7,6,3,4,5,32,0,4,5,0,3,6,32,1,5,6,1,6,5,32,2,6,7,2,5,4,32,4,0,3,3,0,7,32,3,0,2,7,0,2,32,5,4,6,6,3,5,32,1,0,5,1,0,6,32,2,1,6,2,1,5,32,3,2,7,7,2,4,32,7,4,3,4,3,7]\n" +
                "\n" +
                "        }\n" +
                "    ],\n" +
                "    \"controls\": [\n" +
                "\n" +
                "    ],\n" +
                "    \"operators\": [\n" +
                "        {\n" +
                "            \"id\": \"Corner Up\",\n" +
                "            \"type\": \"morph\",\n" +
                "            \"mesh\": \"main.cube\",\n" +
                "            \"parameters\": {\n" +
                "                \"modifiedCount\": 1,\n" +
                "                \"indices\": [4],\n" +
                "                \"displacements\": [0,0,1.0]\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"Smalltop\",\n" +
                "            \"type\": \"morph\",\n" +
                "            \"mesh\": \"main.cube\",\n" +
                "            \"parameters\": {\n" +
                "                \"modifiedCount\": 4,\n" +
                "                \"indices\": [4,5,6,7],\n" +
                "                \"displacements\": [-0.5359284281730652,-0.5359278917312622,0,-0.5359277725219727,0.5359282493591309,0,0.5359281897544861,0.5359277725219727,0,0.535927951335907,-0.5359281301498413,0]\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}\n";

        ModelJson desJson = gson.fromJson(json, ModelJson.class);

        Assert.assertNotNull(desJson.getMeshes());
    }

    @Test
    public void testExporter() throws IOException {

        byte[] encoded = null;

        encoded = Files.readAllBytes(Paths.get("/Users/tim/Downloads/models-open_bracelet-2groups-2keys.json"));

        String jsonFile = new String(encoded, Charset.forName("UTF-8"));

        // Gson deserialization library, courtesy of Google
        Gson gson = new Gson();
        ModelJson jsonObject = gson.fromJson(jsonFile, ModelJson.class);

        ParameterJson parameterJson = new ParameterJson();
        parameterJson.setSize("r4.0");

        List<OperatorWeightJson> weights = new ArrayList<>();
        weights.add(new OperatorWeightJson("Inner", 0.96f));
        weights.add(new OperatorWeightJson("Outer", 0.0f));
        parameterJson.setWeights(weights);

        List<String> visibleMeshes = new ArrayList<>();
        visibleMeshes.add("Holder.Circle");
        visibleMeshes.add("Main.Bracelet");
        parameterJson.setVisible(visibleMeshes);

        byte[] obj = exporter.process(jsonObject, parameterJson);

        Assert.assertTrue(obj.length > 0);

    }

    @Test
    public void testNoParameterJsonItemsExporter() throws IOException {

        byte[] encoded = null;

        //encoded = Files.readAllBytes(Paths.get("/Users/tim/Downloads/models-open_bracelet-2groups-2keys-unittest.json"));
        encoded = Files.readAllBytes(Paths.get("/Users/tim/Downloads/models-cube-sub1-keys1.json"));
        String jsonFile = new String(encoded, Charset.forName("UTF-8"));

        // Gson deserialization library, courtesy of Google
        Gson gson = new Gson();
        ModelJson jsonObject = gson.fromJson(jsonFile, ModelJson.class);

        ParameterJson parameterJson = new ParameterJson();
        parameterJson.setSize("r4.0");

        List<OperatorWeightJson> weights = new ArrayList<>();
        //weights.add(new OperatorWeightJson("Inner", 0.96f));
        //weights.add(new OperatorWeightJson("Outer", 0.0f));
        parameterJson.setWeights(weights);

        List<String> visibleMeshes = new ArrayList<>();
        //visibleMeshes.add("Holder.Circle");
        //visibleMeshes.add("Main.Bracelet");
        parameterJson.setVisible(visibleMeshes);

        byte[] obj = exporter.process(jsonObject, parameterJson);

        Assert.assertTrue(obj.length > 0);

    }

    @Test
    public void testForgeExporter()
    {
        List<FtuePersistedRecord> records = ftueAccessor.
                getFtuePersistedRecordsByStatus(
                        FtuePersistedRecord.FTUE_STATUS.QUEUED_FOR_SAMPLE_SUPPLIER, IEnums.SEEK_MODE.FULL, 0l);

        for (FtuePersistedRecord record : records)
        {
            Item item = itemAccessor.getItemDetached(record.getFtueModelId(), IEnums.SEEK_MODE.QUICK);

            //Test get some json for an arrow ring to benchmark how long it takes to export one
            //if (item.getItemId() == 52) {
            if (record.getFtuePersistedRecordId() == 211) {
            //if (true) {

                List<CustomizerOperator> operators = new ArrayList<>();
                List<String> visibleMeshes = new ArrayList<>();

                for (FtueModelWeights weight : record.getModelWeights()) {
                    if (weight.getVisibleMesh() != null)
                        visibleMeshes.add(weight.getVisibleMesh());
                    else
                        operators.add(new CustomizerOperator(weight.getWeightId(), weight.getWeightValue()));
                }

                CustomizerHash hash = new CustomizerHash(visibleMeshes, SizeMapper.getExporterSize(item.getCategory(),
                        record.getSize()),
                        operators, item.getCustomizerPath() + item.getCustomizerFilename(),
                        "troveup-dev-cdn", "testexport/" + record.getRequest().getShipping_info().getName() + "_" + UUID.randomUUID() + ".obj",
                        true);

                ForgeMeshExportResponse response = forgeExporter.exportBlenderMesh(hash.getVisible(), hash.getSize(),
                        hash.getOperators(),
                        hash.getImportUrl(), hash.getExportBucket(), hash.getExportPath(), hash.getEnableRender());

                Assert.assertNotNull(response);
                Assert.assertNull(response.getMessage());
                Assert.assertTrue(response.getSuccess());
            }
        }

    }

    @Test
    public void testBlenderForgeExporter()
    {

    }

    @Test
    public void testExportBrokenWeights()
    {
        Item item = itemAccessor.getItemDetached(31l, IEnums.SEEK_MODE.ITEM_ATTRIBUTES);

        List<CustomizerOperator> operatorWeights = new ArrayList<>();
        List<String> visibleMeshes = new ArrayList<>();
        if (item.getItemAttributes() != null) {
            for (ItemAttribute attribute : item.getItemAttributes()) {
                if (attribute.getAttributeName().contains("modelWeight-")) {
                    operatorWeights.add(new CustomizerOperator(attribute.getAttributeName().split("-")[1], attribute.getAttributeValue()));
                }
            }
        }

        String exportedModelUUID = UUID.randomUUID().toString();
        String exportedModelName = "render" + "-" + exportedModelUUID + ".obj";

        ForgeMeshExportResponse response = forgeExporter.exportMesh(operatorWeights, visibleMeshes,
                SizeMapper.getMediumExportSize(item.getCategory()), true,
                item.getCustomizerPath() + item.getCustomizerFilename(), exportedModelName);

        Assert.assertNotNull(response);
        Assert.assertTrue(response.getSuccess());
    }

    @Test
    public void testForgeVolume()
    {
        List<FtuePersistedRecord> records = ftueAccessor.
                getFtuePersistedRecordsByStatus(
                        FtuePersistedRecord.FTUE_STATUS.QUEUED_FOR_SAMPLE_SUPPLIER, IEnums.SEEK_MODE.FULL, 0l);

        Map<String, String> volumeMap = new HashMap<>();
        for (FtuePersistedRecord record : records)
        {
            CustomizerHash hash = new CustomizerHash();
            Item item = itemAccessor.getItemDetached(record.getFtueModelId(), IEnums.SEEK_MODE.QUICK);

            //Test get some json for an arrow ring to benchmark how long it takes to export one
            //if (item.getItemId() == 52) {
            if (true) {

                List<CustomizerOperator> operators = new ArrayList<>();
                List<String> visibleMeshes = new ArrayList<>();

                for (FtueModelWeights weight : record.getModelWeights()) {
                    if (weight.getVisibleMesh() != null)
                        visibleMeshes.add(weight.getVisibleMesh());
                    else
                        operators.add(new CustomizerOperator(weight.getWeightId(), weight.getWeightValue()));
                }

                hash.setSize(SizeMapper.getExporterSize(item.getCategory(), record.getSize()));
                hash.setExportBucket("trove-qa-teststore");
                hash.setModelName(record.getRequest().getShipping_info().getName() + "_" + UUID.randomUUID());
                hash.setExportPath("exports/" + hash.getModelName() + ".obj");
                hash.setJsonUrl(item.getCustomizerPath() + item.getCustomizerFilename());
                hash.setJsonFileName(item.getCustomizerFilename());
                hash.setIncludeNormals(false);
                hash.setIsLocal(true);
                hash.setTempRoot("./var/");
                hash.setOperators(operators);
                hash.setVisible(visibleMeshes);

                ForgeMeshVolumeResponse response = forgeExporter.getVolume(hash.getOperators(),
                        hash.getVisible(), hash.getSize(), hash.getJsonUrl());

                Assert.assertNotNull(response);
                Assert.assertNotNull(response.getVolume());
                Assert.assertTrue(response.getSuccess());

                volumeMap.put(record.getRequest().getShipping_info().getName(), response.getVolume());

            }

        }

        String thisVolume = null;
    }

    @Test
    public void testSpecificExport()
    {
        CustomizerHash hash = new CustomizerHash();
        Item item = itemAccessor.getItemDetached(37l, IEnums.SEEK_MODE.ITEM_ATTRIBUTES);

        //Test get some json for an arrow ring to benchmark how long it takes to export one
        //if (item.getItemId() == 52) {
        if (true) {

            List<CustomizerOperator> operators = new ArrayList<>();
            List<String> visibleMeshes = new ArrayList<>();

            operators = item.getOperatorsFromCustomizations();

            hash.setSize(SizeMapper.getExporterSize(item.getCategory(), "7.5"));
            hash.setExportBucket("trove-dev-cdn");
            hash.setModelName("broken_export_test_" + UUID.randomUUID() + ".obj");
            hash.setExportPath("exports/" + hash.getModelName() + ".obj");
            hash.setJsonUrl(item.getCustomizerPath() + item.getCustomizerFilename());
            hash.setJsonFileName(hash.getModelName());
            hash.setIncludeNormals(false);
            hash.setIsLocal(true);
            hash.setTempRoot("./var/");
            hash.setOperators(operators);
            hash.setVisible(visibleMeshes);

            ForgeMeshExportResponse response = forgeExporter.exportMesh(operators, visibleMeshes, hash.getSize(), false, hash.getJsonUrl(), hash.getModelName());

            Assert.assertNotNull(response);
            Assert.assertNotNull(response.getVolume());
            Assert.assertTrue(response.getSuccess());

        }
    }

    @Test
    public void exportAllTheArrowRings()
    {
        List<FtuePersistedRecord> records = ftueAccessor.
                getFtuePersistedRecordsByStatus(
                        FtuePersistedRecord.FTUE_STATUS.QUEUED_FOR_SAMPLE_SUPPLIER, IEnums.SEEK_MODE.FULL, 0l);

        for (FtuePersistedRecord record : records)
        {
            CustomizerHash hash = new CustomizerHash();
            Item item = itemAccessor.getItemDetached(record.getFtueModelId(), IEnums.SEEK_MODE.QUICK);

            //Test get some json for an arrow ring to benchmark how long it takes to export one
            if (item.getItemId() == 52) {
            //if (true) {

                List<CustomizerOperator> operators = new ArrayList<>();
                List<String> visibleMeshes = new ArrayList<>();

                for (FtueModelWeights weight : record.getModelWeights()) {
                    if (weight.getVisibleMesh() != null)
                        visibleMeshes.add(weight.getVisibleMesh());
                    else
                        operators.add(new CustomizerOperator(weight.getWeightId(), weight.getWeightValue()));
                }

                hash.setSize(SizeMapper.getExporterSize(item.getCategory(), record.getSize()));
                hash.setExportBucket("trove-qa-teststore");
                hash.setModelName(record.getRequest().getShipping_info().getName() + "_" + UUID.randomUUID());
                hash.setExportPath("exports/" + hash.getModelName() + ".obj");
                hash.setJsonUrl(item.getCustomizerPath() + item.getCustomizerFilename());
                hash.setJsonFileName(item.getCustomizerFilename());
                hash.setIncludeNormals(false);
                hash.setIsLocal(true);
                hash.setTempRoot("./var/");
                hash.setOperators(operators);
                hash.setVisible(visibleMeshes);

                for (String size : SizeMapper.getRingSizes())
                {
                    hash.setSize(SizeMapper.getExporterSize(item.getCategory(), size));
                    hash.setModelName(record.getRequest().getShipping_info().getName() + "_" + hash.getSize());

                    ForgeMeshExportRequest request = new ForgeMeshExportRequest(hash);

                    ForgeMeshExportResponse response = exporterApi.exportMesh(request);

                    Assert.assertNotNull(response);
                }
                break;
            }
        }
    }

    @Test
    public void testBrokenBarRingExport()
    {
        Item item = itemAccessor.getItemDetached(31l, IEnums.SEEK_MODE.ITEM_ATTRIBUTES);
        List<CustomizerOperator> operatorWeights = new ArrayList<>();
        List<String> visibleMeshes = new ArrayList<>();
        if (item.getItemAttributes() != null) {
            for (ItemAttribute attribute : item.getItemAttributes()) {
                if (attribute.getAttributeName().contains("modelWeight-")) {
                    operatorWeights.add(new CustomizerOperator(attribute.getAttributeName().split("-")[1], attribute.getAttributeValue()));
                }
            }
        }

        String exportedModelUUID = UUID.randomUUID().toString();
        String exportedModelName = "render" + "-" + exportedModelUUID + ".obj";

        ForgeMeshExportResponse response = forgeExporter.exportMesh(operatorWeights, visibleMeshes,
                SizeMapper.getMediumExportSize(item.getCategory()), true,
                item.getCustomizerPath() + item.getCustomizerFilename(), exportedModelName);

        Assert.assertNotNull(response);
        Assert.assertTrue(response.getSuccess());
    }

    @Test
    public void testGetVolumeTime() throws IOException {

        //General setup
        byte[] encoded;
        Gson gson = new Gson();
        ParameterJson parameterJson = new ParameterJson();
        parameterJson.setSize(SizeMapper.getRingSizeExporterValue("7.5"));
        List<OperatorWeightJson> weights = new ArrayList<>();
        parameterJson.setWeights(weights);
        List<String> visibleMeshes = new ArrayList<>();
        parameterJson.setVisible(visibleMeshes);

        //Mirror ring
        encoded = Files.readAllBytes(Paths.get("/Users/tim/Downloads/obj_tests/mirror-no-hole_ring.json"));
        String jsonFile = new String(encoded, Charset.forName("UTF-8"));
        ModelJson jsonObject = gson.fromJson(jsonFile, ModelJson.class);
        //byte[] mirror = exporter.process(jsonObject, parameterJson);
        GeometryAnalysis mirrorVolume = exporter.getVolume(jsonObject, parameterJson);

        //Bar ring
        encoded = Files.readAllBytes(Paths.get("/Users/tim/Downloads/obj_tests/bar_ring.json"));
        jsonFile = new String(encoded, Charset.forName("UTF-8"));
        jsonObject = gson.fromJson(jsonFile, ModelJson.class);
        //byte[] bar = exporter.process(jsonObject, parameterJson);
        GeometryAnalysis barVolume = exporter.getVolume(jsonObject, parameterJson);

        //Parted ring
        encoded = Files.readAllBytes(Paths.get("/Users/tim/Downloads/obj_tests/parted_ring.json"));
        jsonFile = new String(encoded, Charset.forName("UTF-8"));
        jsonObject = gson.fromJson(jsonFile, ModelJson.class);
        //byte[] parted = exporter.process(jsonObject, parameterJson);
        GeometryAnalysis partedVolume = exporter.getVolume(jsonObject, parameterJson);

        //Mirror OBJ
        File mirrorFile = new File("/Users/tim/Downloads/obj_tests/mirror-no-hole_ring.obj");
        if (!mirrorFile.exists()) {
  //          mirrorFile.createNewFile();
        }
        FileWriter fw = new FileWriter(mirrorFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        //bw.write(new String(mirror, "UTF-8"));
        bw.close();

        //Bar OBJ
        File barFile = new File("/Users/tim/Downloads/obj_tests/bar_ring.obj");
        if (!barFile.exists()) {
//            barFile.createNewFile();
        }
        fw = new FileWriter(barFile.getAbsoluteFile());
        bw = new BufferedWriter(fw);
        //bw.write(new String(bar, "UTF-8"));
        bw.close();

        //Parted OBJ
        File partedFile = new File("/Users/tim/Downloads/obj_tests/parted_ring.obj");
        if (!partedFile.exists()) {
//            partedFile.createNewFile();
        }
        fw = new FileWriter(partedFile.getAbsoluteFile());
        bw = new BufferedWriter(fw);
        //bw.write(new String(parted, "UTF-8"));
        bw.close();
    }

    @Test
    public void exportLocalTest() throws IOException {
        List<FtuePersistedRecord> records = ftueAccessor.
                getFtuePersistedRecordsByStatus(
                        FtuePersistedRecord.FTUE_STATUS.QUEUED_FOR_SAMPLE_SUPPLIER, IEnums.SEEK_MODE.FULL, 0l);

        for (FtuePersistedRecord record : records)
        {
            CustomizerHash hash = new CustomizerHash();
            Item item = itemAccessor.getItemDetached(record.getFtueModelId(), IEnums.SEEK_MODE.QUICK);

            //Test get some json for an arrow ring to benchmark how long it takes to export one
            //if (item.getItemId() == 52) {
            if (true) {

                List<CustomizerOperator> operators = new ArrayList<>();
                List<String> visibleMeshes = new ArrayList<>();

                for (FtueModelWeights weight : record.getModelWeights()) {
                    if (weight.getVisibleMesh() != null)
                        visibleMeshes.add(weight.getVisibleMesh());
                    else
                        operators.add(new CustomizerOperator(weight.getWeightId(), weight.getWeightValue()));
                }

                hash.setSize(SizeMapper.getExporterSize(item.getCategory(), record.getSize()));
                hash.setExportBucket("trove-qa-teststore");
                hash.setModelName(record.getRequest().getShipping_info().getName() + "_" + UUID.randomUUID());
                hash.setExportPath("exports/" + hash.getModelName() + ".obj");
                hash.setJsonUrl(item.getCustomizerPath() + item.getCustomizerFilename());
                hash.setJsonFileName(item.getCustomizerFilename());
                hash.setIncludeNormals(false);
                hash.setIsLocal(false);
                hash.setTempRoot("./var/");
                hash.setOperators(operators);
                hash.setVisible(visibleMeshes);

                String returnedResult = sendExportRequest(hash);

                Assert.assertNotNull(returnedResult);
                Assert.assertTrue(returnedResult.length() > 0);
            }
        }


    }

    private String sendExportRequest(CustomizerHash requestHash) throws IOException {
        HttpClient client = clientFactory.getHttpClientInstance();

        String rval = null;

        //String submitSampleOrderUrl = "http://localhost:9001/api/export";
        String submitSampleOrderUrl = "http://104.197.71.84/api/export";

        String jsonRequest = gson.toJson(requestHash);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        //headers.put("Accept-Encoding", "gzip, deflate");
        //headers.put("Accept-Language", "en-US,en;q=0.8");

        client.configureForStandardRequest(submitSampleOrderUrl, headers, HttpClient.REQUEST_METHOD.POST, jsonRequest, null);

        UrlResponse response = client.sendRequest();

        if (response != null)
        {
            String concatString = "";

            for (String singleString : response.getResponseBody())
                concatString += singleString;

            rval = concatString;
        }

        return rval;
    }
}
