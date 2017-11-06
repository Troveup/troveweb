package com.troveup.brooklyn.tests.orm;

import com.google.gson.Gson;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.ftui.impl.FtueAccessor;
import com.troveup.brooklyn.orm.ftui.model.*;
import com.troveup.brooklyn.sdk.meshexporter.java.business.MeshExport;
import com.troveup.brooklyn.sdk.meshexporter.java.model.ModelJson;
import com.troveup.brooklyn.sdk.meshexporter.java.model.OperatorWeightJson;
import com.troveup.brooklyn.sdk.meshexporter.java.model.ParameterJson;
import com.troveup.brooklyn.sdk.print.interfaces.ISampleSupplier;
import com.troveup.brooklyn.sdk.print.voodoo.model.*;
import com.troveup.brooklyn.util.SizeMapper;
import com.troveup.config.PersistenceConfig;
import com.troveup.config.SDKConfig;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by tim on 6/24/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, SDKConfig.class})
public class FtueAccessorTest
{
    @Autowired
    FtueAccessor ftueAccessor;

    @Autowired
    MeshExport exporter;

    @Autowired
    ISampleSupplier sampleSupplier;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testGetFtueRecord()
    {
        FtuePersistedRecord record = ftueAccessor.getPersistedRecord(156l, IEnums.SEEK_MODE.FTUE_IMAGE_SUBMIT);

        Assert.assertNotNull(record);
        Assert.assertNotNull(record.getModelWeights());
        Assert.assertTrue(record.getModelWeights().size() > 0);
        Assert.assertNotNull(record.getModelWeights().get(0).getWeightValue());
    }

    @Test
    public void testGetModelWeightStringConversion()
    {
        FtuePersistedRecord record = new FtuePersistedRecord();
        List<FtueModelWeights> weights = new ArrayList<>();

        weights.add(new FtueModelWeights("Test Weight 1", "weight1"));
        weights.add(new FtueModelWeights("Test Weight 2", "weight2"));

        record.setModelWeights(weights);

        ftueAccessor.persistFtueRecord(record);

        FtuePersistedRecord retrievedRecord = ftueAccessor.getPersistedRecord(record.getFtuePersistedRecordId(),
                IEnums.SEEK_MODE.FTUE_IMAGE_SUBMIT);

        Assert.assertNotNull(retrievedRecord);
        Assert.assertNotNull(retrievedRecord.getModelWeights());
        Assert.assertTrue(retrievedRecord.getModelWeights().size() > 0);
        Assert.assertNotNull(retrievedRecord.getModelWeights().get(0).getWeightValue());
    }

    @Test
    public void testDuplicateAddressEntry()
    {
        FtuePersistedRecord record = generatePersistedRecord();
        FtuePersistedRecord unchangedRecord = new FtuePersistedRecord(record);
        Assert.assertNotNull(ftueAccessor.persistFtueRecord(record));

        Assert.assertFalse(ftueAccessor.getAddressCountAddress(unchangedRecord.getRequest().getShipping_info()) < 3);
        Assert.assertTrue(ftueAccessor.getAddressCountAddress(generateAddress()) > 0);

    }

    @Test
    public void testFtueCounter()
    {
        Long count = ftueAccessor.getFtueOrderCount();

        Assert.assertTrue(count > 0);
    }

    @Test
    public void testGetKargoOrders() throws IOException {
        Gson gson = new Gson();

        List<FtuePersistedRecord> records = ftueAccessor.getAllFtuePersistedRecords(IEnums.SEEK_MODE.FULL);

        List<FtuePersistedRecord> kargoRecords = new ArrayList<>();

        for (FtuePersistedRecord record : records)
        {
            if (!record.getRequest().getShipping_info().getName().toLowerCase().contains("test") &&
                    !record.getRequest().getShipping_info().getName().toLowerCase().contains("brian") &&
                    !record.getRequest().getShipping_info().getName().toLowerCase().contains("kristin") &&
                    !record.getRequest().getShipping_info().getName().toLowerCase().contains("tseting"))
            {
                kargoRecords.add(record);
            }
        }

        byte[] barRingJsonFile =
                Files.readAllBytes(Paths.get("/Users/tim/Downloads/kargo_export/ftue_bar_ring.json"));
        ModelJson barRingJsonObject = gson.fromJson(new String(barRingJsonFile, "UTF-8"), ModelJson.class);

        byte[] partedRingJsonFile =
                Files.readAllBytes(Paths.get("/Users/tim/Downloads/kargo_export/ftue_parted_ring.json"));
        ModelJson partedRingJsonObject = gson.fromJson(new String(partedRingJsonFile, "UTF-8"), ModelJson.class);

        byte[] prongsRingJsonFile =
                Files.readAllBytes(Paths.get("/Users/tim/Downloads/kargo_export/ftue_triangular-prongs_ring.json"));
        ModelJson prongsRingJsonObject = gson.fromJson(new String(prongsRingJsonFile, "UTF-8"), ModelJson.class);

        byte[] teardropJsonFile =
                Files.readAllBytes(Paths.get("/Users/tim/Downloads/kargo_export/ftue_mirror-no-hole.json"));
        ModelJson teardropRingJsonObject = gson.fromJson(new String(teardropJsonFile, "UTF-8"), ModelJson.class);

        for (FtuePersistedRecord kargoRecord : kargoRecords)
        {
            logger.debug("Starting on record for " + kargoRecord.getRequest().getShipping_info().getName());
            ParameterJson parameterJson = new ParameterJson();
            parameterJson.setSize(SizeMapper.getExporterSize("RING", kargoRecord.getSize()));
            parameterJson.setWeights(new ArrayList<OperatorWeightJson>());
            parameterJson.setVisible(new ArrayList<String>());

            for (FtueModelWeights weight : kargoRecord.getModelWeights())
            {
                parameterJson.getWeights().add(new OperatorWeightJson(weight.getWeightId(), weight.getWeightValue()));
            }

            String parameterJsonString = gson.toJson(parameterJson);

            String objName = "bad.obj";
            byte exportedObj[] = null;

            //Parted ring
            if (kargoRecord.getFtueModelId() == 1)
            {
                logger.debug("Request was for a parted ring.  Performing export.");
                objName = kargoRecord.getRequest().getShipping_info().getName().replace(" ", "") + "_part_ring.obj";
                exportedObj = exporter.process(partedRingJsonObject, parameterJson);
            }
            //Bar ring
            else if (kargoRecord.getFtueModelId() == 2)
            {
                logger.debug("Request was for a bar ring.  Performing export.");
                objName = kargoRecord.getRequest().getShipping_info().getName().replace(" ", "") + "_bar_ring.obj";
                exportedObj = exporter.process(barRingJsonObject, parameterJson);
            }
            //Two prong triangular
            else if (kargoRecord.getFtueModelId() == 3)
            {
                logger.debug("Request was for a triangular ring.  Performing export.");
                objName = kargoRecord.getRequest().getShipping_info().getName().replace(" ", "") + "_triangular_ring.obj";
                exportedObj = exporter.process(prongsRingJsonObject, parameterJson);
            }
            //Teardrop Ring
            else if (kargoRecord.getFtueModelId() == 4)
            {
                logger.debug("Request was for a teardrop ring.  Performing export.");
                objName = kargoRecord.getRequest().getShipping_info().getName().replace(" ", "") + "_teardrop_ring.obj";
                exportedObj = exporter.process(teardropRingJsonObject, parameterJson);
            }

            logger.debug("Writing file " + objName);
            //Mirror OBJ
            //File mirrorFile = new File("/Users/tim/Downloads/kargo_export/" + objName);
            File mirrorFile = new File("/Users/tim/Downloads/kargo_export/" + kargoRecord.getRequest().getShipping_info().getName().replace(" ", "") + "_parameterJsonFile.json");
            if (!mirrorFile.exists()) {
                mirrorFile.createNewFile();
            }
            FileWriter fw = new FileWriter(mirrorFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            //bw.write(new String(exportedObj, "UTF-8"));
            bw.write(parameterJsonString);
            bw.close();

        }
    }

    @Test
    public void submitTeardropOrders() throws IOException {
        Gson gson = new Gson();

        byte[] teardropJsonFile =
                Files.readAllBytes(Paths.get("/Users/tim/Downloads/kargo_export/ftue_mirror-no-hole.json"));
        ModelJson teardropRingJsonObject = gson.fromJson(new String(teardropJsonFile, "UTF-8"), ModelJson.class);

        //Samantha Grossman
        //530 E 20th St
        //Apt 2D
        //New York, NY 10009
        //sjayeg@gmail.com
        //Teardrop .32
        List<OperatorWeightJson> jsonWeights = new ArrayList<>();
        jsonWeights.add(new OperatorWeightJson("Arm_1", 0f));
        jsonWeights.add(new OperatorWeightJson("Arm_2", 0f));
        jsonWeights.add(new OperatorWeightJson("Teardrop", .32f));
        jsonWeights.add(new OperatorWeightJson("Width", 0f));
        ParameterJson teardropParams = new ParameterJson();
        teardropParams.setVisible(new ArrayList<String>());
        teardropParams.setSize(SizeMapper.getExporterSize("RING", "7.0"));
        teardropParams.setWeights(jsonWeights);

        byte[] samanthaGrossmanExport = exporter.process(teardropRingJsonObject, teardropParams);

        //File mirrorFile = new File("/Users/tim/Downloads/kargo_export/" + objName);
        File mirrorFile = new File("/Users/tim/Downloads/kargo_export/samanthagrossman_teardrop.obj");
        if (!mirrorFile.exists()) {
            mirrorFile.createNewFile();
        }
        FileWriter fw = new FileWriter(mirrorFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(new String(samanthaGrossmanExport, "UTF-8"));
        bw.close();

        //Deena Jajou
        //441 Lorimer St, Apt 4
        //Brooklyn, NY 11206
        //Arm_1 = .53
        teardropRingJsonObject = gson.fromJson(new String(teardropJsonFile, "UTF-8"), ModelJson.class);
        jsonWeights = new ArrayList<>();
        jsonWeights.add(new OperatorWeightJson("Arm_1", .53f));
        jsonWeights.add(new OperatorWeightJson("Arm_2", 0f));
        jsonWeights.add(new OperatorWeightJson("Teardrop", 0f));
        jsonWeights.add(new OperatorWeightJson("Width", 0f));
        teardropParams = new ParameterJson();
        teardropParams.setVisible(new ArrayList<String>());
        teardropParams.setSize(SizeMapper.getExporterSize("RING", "7.0"));
        teardropParams.setWeights(jsonWeights);

        byte[] deenaJajouExport = exporter.process(teardropRingJsonObject, teardropParams);

        mirrorFile = new File("/Users/tim/Downloads/kargo_export/deenajajou_teardrop.obj");
        if (!mirrorFile.exists()) {
            mirrorFile.createNewFile();
        }
        fw = new FileWriter(mirrorFile.getAbsoluteFile());
        bw = new BufferedWriter(fw);
        bw.write(new String(deenaJajouExport, "UTF-8"));
        bw.close();

    }

    @Test
    public void submitKargoOrderDeena()
    {
        //Put the Voodoo request together for Deena
        List<SampleOrderItem> orderItems = new ArrayList<>();
        SampleOrderItem orderItem = new SampleOrderItem();
        SampleModel model = new SampleModel();
        model.setDimensions("mm");
        model.setFile_url("https://storage.googleapis.com/troveup-dev-private/deenajajou_teardrop.obj");
        orderItem.setMaterial(8);
        orderItem.setQty(1);
        orderItem.setModel(model);
        orderItems.add(orderItem);

        SampleOrderRequest request = new SampleOrderRequest();
        SampleAddress address = new SampleAddress();
        address.setName("Deena Jajou");
        address.setStreet1("441 Lorimer St");
        address.setStreet2("Apt 4");
        address.setCity("Brooklyn");
        address.setState("NY");
        address.setZip("11206");
        address.setCountry("US");
        request.setShipping_info(address);
        request.setOrder_items(orderItems);

        SampleCreateOrderResponse sampleOrderResponse = sampleSupplier.createSampleOrder(orderItems, address);

        FtuePersistedRecord persistedRecord = new FtuePersistedRecord();
        persistedRecord.setRequest(request.toFtueRequest());

        List<FtueOrderItem> persistedOrderItems = new ArrayList<>();

        for (SampleOrderItem sampleOrderItem : sampleOrderResponse.getOrder_items())
        {
            persistedOrderItems.add(sampleOrderItem.toFtueOrderItem());
        }

        FtueResponse ftuePersistedResponse = new FtueResponse();
        ftuePersistedResponse.setOrder_items(persistedOrderItems);
        ftuePersistedResponse.setShipping(sampleOrderResponse.getShipping().toFtueAddress());
        ftuePersistedResponse.setQuote(sampleOrderResponse.getQuote().toFtueResponseQuote());
        ftuePersistedResponse.setQuote_id(String.valueOf(sampleOrderResponse.getOrder_id()));

        persistedRecord.setResponseOrderId(sampleOrderResponse.getOrder_id());
        persistedRecord.setResponse(ftuePersistedResponse);
        persistedRecord.setSize("7.0");

        List<FtueModelWeights> weights = new ArrayList<>();
        weights.add(new FtueModelWeights("Arm_1", .53f));
        weights.add(new FtueModelWeights("Arm_2", 0f));
        weights.add(new FtueModelWeights("Teardrop", 0f));
        weights.add(new FtueModelWeights("Width", 0f));

        persistedRecord.setModelWeights(weights);

        //Current teardrop on prod
        persistedRecord.setFtueModelId(10l);
        persistedRecord.setStatus(FtuePersistedRecord.FTUE_STATUS.SUBMITTED_TO_VOODOO);
        persistedRecord.setSubmitted(true);

        Assert.assertNotNull(ftueAccessor.persistFtueRecord(persistedRecord));
    }

    @Test
    public void submitKargoOrderSamantha()
    {
        //Put the Voodoo request together for Deena
        List<SampleOrderItem> orderItems = new ArrayList<>();
        SampleOrderItem orderItem = new SampleOrderItem();
        SampleModel model = new SampleModel();
        model.setDimensions("mm");
        model.setFile_url("https://storage.googleapis.com/troveup-dev-private/samanthagrossman_teardrop.obj");
        orderItem.setMaterial(8);
        orderItem.setQty(1);
        orderItem.setModel(model);
        orderItems.add(orderItem);

        SampleOrderRequest request = new SampleOrderRequest();
        SampleAddress address = new SampleAddress();
        address.setName("Samantha Grossman");
        address.setStreet1("530 E 20th St");
        address.setStreet2("Apt 2D");
        address.setCity("New York");
        address.setState("NY");
        address.setZip("10009");
        address.setCountry("US");
        request.setShipping_info(address);
        request.setOrder_items(orderItems);

        SampleCreateOrderResponse sampleOrderResponse = sampleSupplier.createSampleOrder(orderItems, address);

        FtuePersistedRecord persistedRecord = new FtuePersistedRecord();
        persistedRecord.setRequest(request.toFtueRequest());

        List<FtueOrderItem> persistedOrderItems = new ArrayList<>();

        for (SampleOrderItem sampleOrderItem : sampleOrderResponse.getOrder_items())
        {
            persistedOrderItems.add(sampleOrderItem.toFtueOrderItem());
        }

        FtueResponse ftuePersistedResponse = new FtueResponse();
        ftuePersistedResponse.setOrder_items(persistedOrderItems);
        ftuePersistedResponse.setShipping(sampleOrderResponse.getShipping().toFtueAddress());
        ftuePersistedResponse.setQuote(sampleOrderResponse.getQuote().toFtueResponseQuote());
        ftuePersistedResponse.setQuote_id(String.valueOf(sampleOrderResponse.getOrder_id()));

        persistedRecord.setResponseOrderId(sampleOrderResponse.getOrder_id());
        persistedRecord.setResponse(ftuePersistedResponse);
        persistedRecord.setSize("7.0");

        List<FtueModelWeights> weights = new ArrayList<>();
        weights.add(new FtueModelWeights("Arm_1", 0f));
        weights.add(new FtueModelWeights("Arm_2", 0f));
        weights.add(new FtueModelWeights("Teardrop", .32f));
        weights.add(new FtueModelWeights("Width", 0f));

        persistedRecord.setModelWeights(weights);

        //Current teardrop on prod
        persistedRecord.setFtueModelId(10l);
        persistedRecord.setStatus(FtuePersistedRecord.FTUE_STATUS.SUBMITTED_TO_VOODOO);
        persistedRecord.setSubmitted(true);

        Assert.assertNotNull(ftueAccessor.persistFtueRecord(persistedRecord));
    }

    @Test
    public void teardropNoTeardropTest() throws IOException {
        Gson gson = new Gson();

        byte[] teardropJsonFile =
                Files.readAllBytes(Paths.get("/Users/tim/Downloads/kargo_export/ftue_mirror-no-hole.json"));
        ModelJson teardropRingJsonObject = gson.fromJson(new String(teardropJsonFile, "UTF-8"), ModelJson.class);

        //Deena Jajou
        //441 Lorimer St, Apt 4
        //Brooklyn, NY 11206
        //Arm_1 = .53
        List<OperatorWeightJson> jsonWeights = new ArrayList<>();
        jsonWeights = new ArrayList<>();
        jsonWeights.add(new OperatorWeightJson("Arm_1", .53f));
        jsonWeights.add(new OperatorWeightJson("Arm_2", 0f));
        jsonWeights.add(new OperatorWeightJson("Teardrop", 0f));
        jsonWeights.add(new OperatorWeightJson("Width", 0f));
        ParameterJson teardropParams = new ParameterJson();
        teardropParams.setVisible(new ArrayList<String>());
        teardropParams.setSize(SizeMapper.getExporterSize("RING", "7.0"));
        teardropParams.setWeights(jsonWeights);

        byte[] deenaJajouExport = exporter.process(teardropRingJsonObject, teardropParams);

        File mirrorFile = new File("/Users/tim/Downloads/kargo_export/deenajajou_teardrop.obj");
        if (!mirrorFile.exists()) {
            mirrorFile.createNewFile();
        }
        FileWriter fw = new FileWriter(mirrorFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(new String(deenaJajouExport, "UTF-8"));
        bw.close();
    }

    //Resubmitted on July 21st at 10:35 PM
    @Test
    public void resubmitOrders()
    {
        List<FtuePersistedRecord> records = ftueAccessor.getAllFtuePersistedRecords(IEnums.SEEK_MODE.FULL);
        List<FtuePersistedRecord> sortedRecords = new ArrayList<>();

        List<String> emailsToResubmit = new ArrayList<>();
        emailsToResubmit.add("ruchij@uber.com");
        emailsToResubmit.add("RuchiJhaveri@gmail.com");
        emailsToResubmit.add("absolutedestiny@gmail.com");
        emailsToResubmit.add("hannah.yung@gmail.com");
        emailsToResubmit.add("albert.sj.hong@gmail.com");
        emailsToResubmit.add("liannegm@gmail.com");
        emailsToResubmit.add("y4smeen@gmail.com");
        emailsToResubmit.add("strawberryLEm0nad3e@gmail.com");
        emailsToResubmit.add("caseymareeskelly@gmail.com");
        emailsToResubmit.add("tarafsiegel@gmail.com");
        emailsToResubmit.add("edzitron@edzitron.com");
        emailsToResubmit.add("rachelmrgls@gmail.com");
        emailsToResubmit.add("simple5336@gmail.com");
        emailsToResubmit.add("brian@wertz.co");
        emailsToResubmit.add("juliayhuang@gmail.com");
        emailsToResubmit.add("natalie.d.simons@gmail.com");
        emailsToResubmit.add("dnprez3@gmail.com");
        emailsToResubmit.add("inbardankner@gmail.com");
        emailsToResubmit.add("carmen.loo@digitas.com");
        emailsToResubmit.add("yao@s23p.com");
        emailsToResubmit.add("taylormcadam@gmail.com");
        emailsToResubmit.add("jameel.khalfan@gmail.com");
        emailsToResubmit.add("Fabiola.Camacho721@gmail.com");
        emailsToResubmit.add("s.capungcol@gmail.com");
        emailsToResubmit.add("JustinArnoldYL@gmail.com");
        emailsToResubmit.add("don.dubose@gmail.com");
        emailsToResubmit.add("cgarv16@gmail.com");
        emailsToResubmit.add("smittal122@gmail.com");
        emailsToResubmit.add("nick.reinig@gmail.com");
        emailsToResubmit.add("nora.j.watson@gmail.com");
        emailsToResubmit.add("sean.mcwhirt@gmail.com");
        emailsToResubmit.add("leeannak@gmail.com");
        emailsToResubmit.add("phima256@yahoo.com");
        emailsToResubmit.add("matthewpbrennan@gmail.com");
        emailsToResubmit.add("kristin.pasating@gmail.com");
        emailsToResubmit.add("aidankmcl@gmail.com");
        emailsToResubmit.add("leximessmer@gmail.com");
        emailsToResubmit.add("ericheflin@gmail.com");
        emailsToResubmit.add("ourythomas@hotmail.com");
        emailsToResubmit.add("Bryan.shoe@gmail.com");
        emailsToResubmit.add("andrewhc@gmail.com");
        emailsToResubmit.add("carlyannpaul@gmail.com");
        emailsToResubmit.add("milyzaa@gmail.com");
        emailsToResubmit.add("kristi_batten@gallup.com");
        emailsToResubmit.add("thesunisrising12@gmail.com");
        emailsToResubmit.add("nick.reinig@gmail.com");

        for (FtuePersistedRecord record : records)
        {
            if (emailsToResubmit.contains(record.getRequest().getShipping_info().getEmail()))
                sortedRecords.add(record);
        }

        Assert.assertTrue(sortedRecords.size() == 48);

        for (FtuePersistedRecord record : sortedRecords)
        {
            List<SampleOrderItem> orderItems = new ArrayList<>();
            SampleModel model = new SampleModel(record.getRequest().getOrder_items().get(0).getFile_url(),
                    record.getRequest().getOrder_items().get(0).getUnits());
            orderItems.add(new SampleOrderItem(model, record.getRequest().getOrder_items().get(0).getMaterial(), 1));
            SampleOrderRequest request = new SampleOrderRequest();
            request.setOrder_items(orderItems);
            SampleAddress address = new SampleAddress(record.getRequest().getShipping_info().getCity(),
                    record.getRequest().getShipping_info().getName(), record.getRequest().getShipping_info().getZip(),
                    record.getRequest().getShipping_info().getStreet1(),
                    record.getRequest().getShipping_info().getStreet2(),
                    record.getRequest().getShipping_info().getState(),
                    record.getRequest().getShipping_info().getCountry());

            SampleCreateOrderResponse response = sampleSupplier.createSampleOrder(orderItems, address);
            Assert.assertNotNull(response);
            Assert.assertNull(response.getMessage());
        }
    }

    @Test
    public void exportOrderModels() throws IOException {
        Gson gson = new Gson();
        byte[] partedRingJsonFile =
                Files.readAllBytes(Paths.get("/Users/tim/Downloads/parted_ring.json.1"));

        ModelJson partedRingJsonObject = gson.fromJson(new String(partedRingJsonFile, "UTF-8"), ModelJson.class);

        FtueCheckout checkoutItem = ftueAccessor.getPersistedFtueCheckoutRecord("0FB8DD3", IEnums.SEEK_MODE.FTUECHECKOUT_FULL);
        List<FtueCheckout> checkoutRecords = new ArrayList<>();
        checkoutRecords.add(checkoutItem);
        checkoutItem = ftueAccessor.getPersistedFtueCheckoutRecord("27B9086", IEnums.SEEK_MODE.FTUECHECKOUT_FULL);
        checkoutRecords.add(checkoutItem);

        for (FtueCheckout checkout : checkoutRecords)
        {
            ParameterJson jsonParams = new ParameterJson();
            FtuePersistedRecord record = checkout.getRecordReference();
            List<OperatorWeightJson> operatorWeights = new ArrayList<>();
            for (FtueModelWeights weight : record.getModelWeights())
            {
                operatorWeights.add(new OperatorWeightJson(weight.getWeightId(), weight.getWeightValue()));
            }

            jsonParams.setWeights(operatorWeights);
            jsonParams.setSize(SizeMapper.getExporterSize("RING", checkout.getSize()));

            byte[] exportedObj = exporter.process(partedRingJsonObject, jsonParams);

            File mirrorFile = new File("/Users/tim/Downloads/kargo_export/" +
                    checkout.getShippingAddress().getFirstName() + "_" +
                    checkout.getShippingAddress().getLastName() + ".obj");
            if (!mirrorFile.exists()) {
                mirrorFile.createNewFile();
            }
            FileWriter fw = new FileWriter(mirrorFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(new String(exportedObj, "UTF-8"));
            bw.close();
        }

    }

    public FtueRequestAddress generateAddress()
    {
        FtueRequestAddress address = new FtueRequestAddress();
        address.setName(UUID.randomUUID().toString().substring(0, 5) + " " + UUID.randomUUID().toString().substring(0, 5));
        address.setStreet1(UUID.randomUUID().toString());
        address.setStreet2(UUID.randomUUID().toString());
        address.setCity(UUID.randomUUID().toString());
        address.setState(UUID.randomUUID().toString());
        address.setCountry(UUID.randomUUID().toString());
        address.setZip(UUID.randomUUID().toString());
        address.setEmail(UUID.randomUUID().toString());

        return address;

    }

    public FtueRequest generateRequest()
    {
        FtueRequest request = new FtueRequest();
        request.setShipping_info(generateAddress());
        List<FtueOrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(new FtueOrderItem(10, 1, "testurl", "mm"));
        request.setOrder_items(orderItemList);

        return request;
    }

    public FtuePersistedRecord generatePersistedRecord()
    {
        FtuePersistedRecord record = new FtuePersistedRecord();
        FtueRequest request = generateRequest();

        record.setRequest(request);
        record.setResponse(generateResponse(request));
        record.setSubmitted(true);

        return record;
    }

    public FtueResponse generateResponse(FtueRequest request)
    {
        FtueResponse response = new FtueResponse();
        response.setShipping(request.getShipping_info());
        response.setOrder_items(request.getOrder_items());
        response.setQuote(generateQuote());
        response.setQuote_id(UUID.randomUUID().toString());

        return response;
    }

    public FtueResponseQuote generateQuote()
    {
        FtueResponseQuote quote = new FtueResponseQuote();
        quote.setItems(new BigDecimal("10.00"));
        quote.setShipping(new BigDecimal("10.00"));
        quote.setTax(new BigDecimal("10.00"));
        quote.setTotal(new BigDecimal("10.00"));

        return quote;
    }


}
