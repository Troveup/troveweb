package com.troveup.brooklyn.tests.sdk;

import com.google.gson.Gson;
import com.troveup.brooklyn.model.ManufacturerUpload;
import com.troveup.brooklyn.orm.cart.interfaces.ICartAccessor;
import com.troveup.brooklyn.orm.cart.model.CartItem;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.ftui.impl.FtueAccessor;
import com.troveup.brooklyn.orm.ftui.model.FtueCheckout;
import com.troveup.brooklyn.orm.ftui.model.FtueModelWeights;
import com.troveup.brooklyn.orm.item.interfaces.IItemAccessor;
import com.troveup.brooklyn.orm.order.interfaces.IOrderAccessor;
import com.troveup.brooklyn.sdk.meshexporter.forge.business.Forge;
import com.troveup.brooklyn.sdk.meshexporter.forge.model.CustomizerOperator;
import com.troveup.brooklyn.sdk.meshexporter.forge.model.ForgeMeshExportResponse;
import com.troveup.brooklyn.sdk.meshexporter.java.business.MeshExport;
import com.troveup.brooklyn.sdk.meshexporter.java.model.ModelJson;
import com.troveup.brooklyn.sdk.meshexporter.java.model.OperatorWeightJson;
import com.troveup.brooklyn.sdk.print.interfaces.IPrintSupplier;
import com.troveup.brooklyn.sdk.print.shapeways.model.ShapewaysUploadResponse;
import com.troveup.brooklyn.util.SizeMapper;
import com.troveup.config.PersistenceConfig;
import com.troveup.config.SDKConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by tim on 8/5/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, SDKConfig.class})
public class ManualLabor
{
    @Autowired
    FtueAccessor ftueAccessor;

    @Autowired
    IItemAccessor itemAccessor;

    @Autowired
    Gson gson;

    @Autowired
    MeshExport exporter;

    @Autowired
    ICartAccessor cartAccessor;

    @Autowired
    IOrderAccessor orderAccessor;

    @Autowired
    Forge forgeExporter;

    @Autowired
    Environment env;

    @Autowired
    IPrintSupplier printSupplier;

    private String cdnBucketName;

    @Before
    public void setUp()
    {
        cdnBucketName = env.getProperty("cloudstore.publicbucketname");
    }

    @Test
    public void generateShapewaysModelJohnLynch() throws IOException {
        ManufacturerUpload johnLynch =
                getFtueCheckoutObject("CBD6653", "/Users/tim/Downloads/triangular-prongs_ring_hires.json");

        //Mirror OBJ
        File mirrorFile = new File("/Users/tim/Workspace/modelexports/john_lynch_order.obj");
        if (!mirrorFile.exists()) {
            mirrorFile.createNewFile();
        }
        FileWriter fw = new FileWriter(mirrorFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(new String(johnLynch.getExportedModel(), "UTF-8"));
        bw.close();


    }

    @Test
    public void generateShapewaysModelSusieKim() throws IOException {
        ManufacturerUpload susieKim =
                getFtueCheckoutObject("4A96972", "/Users/tim/Downloads/warrior_ring.json");

        //Mirror OBJ
        File mirrorFile = new File("/Users/tim/Workspace/modelexports/susie_kim_order.obj");
        if (!mirrorFile.exists()) {
            mirrorFile.createNewFile();
        }
        FileWriter fw = new FileWriter(mirrorFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(new String(susieKim.getExportedModel(), "UTF-8"));
        bw.close();


    }

    @Test
    public void exportModel()
    {
        ManufacturerUpload uploadContainer = new ManufacturerUpload();

        CartItem cartItem = cartAccessor.getCartItem(Long.parseLong("148"), IEnums.SEEK_MODE.CART_ITEM_UPLOAD);

        List<String> visibleMeshes = new ArrayList<>();
        List<CustomizerOperator> weights = new ArrayList<>();

        //Pull customizations from the cart item
        if (cartItem.getCustomizations() != null)
        {
            visibleMeshes = cartItem.getActiveMeshes(cartItem.getCustomizationIteration());
            weights = cartItem.getOperatorsFromCustomizations(cartItem.getCustomizationIteration());
        }

        uploadContainer.setWeights(weights);
        uploadContainer.setVisibleMeshes(visibleMeshes);
        uploadContainer.setSize(SizeMapper.getExporterSize(cartItem.getCartItemReference().getCategory(),
                cartItem.getSize(cartItem.getCustomizationIteration())));

        uploadContainer.setOrderCartItem(cartItem);
        uploadContainer.setItem(cartItem.getCartItemReference());
        uploadContainer.setUploadFileName(orderAccessor.
                getOrderFirstLastNameByCartItemId(cartItem.getCartItemId()) + "_" + UUID.randomUUID().toString() + ".obj");


        ForgeMeshExportResponse export = forgeExporter.exportBlenderMesh(uploadContainer.getVisibleMeshes(),
                uploadContainer.getSize(), uploadContainer.getWeights(), uploadContainer.getItem().getCustomizerPath() +
                        uploadContainer.getItem().getHighResolutionCustomizerFilename(), cdnBucketName,
                uploadContainer.getUploadFileName().replace(" ", "_"), false);

        String exportLocation = export.getExportURL();
    }

    @Test
    public void uploadModel() throws IOException {
        byte[] uploadBytes = Files.
                readAllBytes(
                        Paths.get("/Users/tim/Temp/reorder/Michala_Hirschfeld_45ce9cee-a8f9-40b6-ba20-031177e37423.obj"));

        ShapewaysUploadResponse response = (ShapewaysUploadResponse)
                printSupplier.uploadModel(uploadBytes, "Michala_Hirschfeld_45ce9cee-a8f9-40b6-ba20-031177e37423.obj");

        String responseStatusBreakPoint = response.getResult();
    }

    public ManufacturerUpload getFtueCheckoutObject(String checkoutId, String jsonModelLocation) throws IOException {

        ManufacturerUpload uploadContainer = new ManufacturerUpload();

        /*List<OperatorWeightJson> weights = new ArrayList<>();

        FtueCheckout checkoutObject = ftueAccessor.
                getPersistedFtueCheckoutRecord(checkoutId, IEnums.SEEK_MODE.FTUECHECKOUT_FULL);

        uploadContainer.setItem(itemAccessor.getItemDetached(checkoutObject.getRecordReference().getFtueModelId(),
                IEnums.SEEK_MODE.QUICK));

        for (FtueModelWeights weight : checkoutObject.getRecordReference().getModelWeights())
        {
            weights.add(new OperatorWeightJson(weight.getWeightId(), weight.getWeightValue()));
        }

        uploadContainer.getJsonParams().setSize(SizeMapper.getExporterSize(uploadContainer.getItem().getCategory(),
                checkoutObject.getSize()));
        uploadContainer.getJsonParams().setWeights(weights);

        uploadContainer.setUploadFileName(checkoutObject.getShippingAddress().getFirstName() + "_" +
                checkoutObject.getShippingAddress().getLastName() + "_" + UUID.randomUUID().toString() + ".obj");

        byte[] jsonBytes = Files.readAllBytes(Paths.get(jsonModelLocation));
        String jsonFile = new String(jsonBytes, Charset.forName("UTF-8"));
        ModelJson jsonObject = gson.fromJson(jsonFile, ModelJson.class);
        byte[] mirror = exporter.process(jsonObject, uploadContainer.getJsonParams());

        uploadContainer.setModel(jsonObject);

        uploadContainer.setExportedModel(mirror);*/

        return uploadContainer;
    }
}
