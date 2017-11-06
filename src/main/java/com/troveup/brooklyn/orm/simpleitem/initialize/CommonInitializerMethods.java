package com.troveup.brooklyn.orm.simpleitem.initialize;

import com.troveup.brooklyn.orm.simpleitem.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 5/16/16.
 */
public class CommonInitializerMethods
{
    public static SimpleItem generateSimpleItemSample()
    {
        SimpleItem rval = new SimpleItem();
        rval.setControlReference(true);
        rval.setPrimaryDisplayImageUrl("https://storage.googleapis.com/troveup-imagestore/default-image-icon-1.jpg");
        rval.setItemName("Shiny Band with Gem");
        rval.setItemPrice(BigDecimal.valueOf(50));
        rval.setItemDescription("Something about Christen Dominiques's collection goes here. Something Something else goes here.");
        rval.setUserCreatorName("Christen Dominique");

        List<SimpleItemControl> availableCustomizations = new ArrayList<>();

        SimpleItemControl bandCustomization = generateDefaultMaterialCustomization();
        SimpleItemControl gemstoneCustomization = generateDefaultGemstoneCustomization();
        SimpleItemControl ringSizeCustomization = generateDefaultRingSizeCustomization();

        availableCustomizations.add(bandCustomization);
        availableCustomizations.add(gemstoneCustomization);
        availableCustomizations.add(ringSizeCustomization);

        rval.setSimpleItemControls(availableCustomizations);

        List<StaticAsset> staticAssets = new ArrayList<>();
        staticAssets.add(new StaticAsset("https://storage.googleapis.com/troveup-imagestore/assets/img/IMG_9441_livephoto.jpg"));

        rval.setStaticAssets(staticAssets);

        return rval;
    }

    public static SimpleItemControl generateDefaultRingSizeCustomization()
    {
        SimpleItemControl rval = new SimpleItemControl();
        rval.setControlName("Size");
        rval.setControlSelectedName("Size");
        rval.setInteractionType(SimpleItemControl.INTERACTION_TYPE.NO_INTERACTION);
        rval.setHidden(false);

        List<ControlOption> options = new ArrayList<>();

        for (int i = 4; i < 10; ++i)
        {
            ControlOption option = new ControlOption();
            option.setOptionDisplayName(String.valueOf(i));
            option.setOptionValue("size_" + String.valueOf(i));
            options.add(option);
        }

        rval.setControlOptions(options);
        rval.setManufacturerSpecifier(new ManufacturerSpecifier(ManufacturerSpecifier.SPECIFIER_TYPE.SIZE));

        return rval;
    }

    public static SimpleItemControl generateDefaultGemstoneCustomization()
    {
        SimpleItemControl rval = new SimpleItemControl();
        rval.setControlName("Gemstone");
        rval.setInteractionType(SimpleItemControl.INTERACTION_TYPE.ASSET_CHANGE);
        rval.setHidden(false);

        List<ControlOption> options = new ArrayList<>();

        ControlOption clearGemstoneOption = new ControlOption();
        clearGemstoneOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/clear.png");
        clearGemstoneOption.setOptionDisplayName("White");
        clearGemstoneOption.setOptionValue("white");
        clearGemstoneOption.setControlOptionAssets(getDefaultWhiteGemstoneAssets());

        ControlOption greenGemstoneOption = new ControlOption();
        greenGemstoneOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/green.png");
        greenGemstoneOption.setOptionDisplayName("Peridot");
        greenGemstoneOption.setOptionValue("peridot");
        greenGemstoneOption.setControlOptionAssets(getDefaultGreenGemstoneAssets());

        ControlOption redGemstoneOption = new ControlOption();
        redGemstoneOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/red.png");
        redGemstoneOption.setOptionDisplayName("Siam");
        redGemstoneOption.setOptionValue("siam");
        redGemstoneOption.setControlOptionAssets(getDefaultRedGemstoneAssets());

        ControlOption blueGemstoneOption = new ControlOption();
        blueGemstoneOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/blue.png");
        blueGemstoneOption.setOptionDisplayName("Sapphire");
        blueGemstoneOption.setOptionValue("sapphire");
        blueGemstoneOption.setControlOptionAssets(getDefaultBlueGemstoneAssets());

        options.add(clearGemstoneOption);
        options.add(greenGemstoneOption);
        options.add(redGemstoneOption);
        options.add(blueGemstoneOption);

        rval.setControlOptions(options);

        return rval;
    }

    public static List<ControlOptionAsset> getDefaultWhiteGemstoneAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/test/gemswap/small/cropped/500px/md-white-crystal-1.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/test/gemswap/small/cropped/500px/white-crystal-2.png");

        ControlOptionAsset cameraTwoAsset = new ControlOptionAsset();
        cameraTwoAsset.setCamera(2);
        cameraTwoAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/test/gemswap/small/cropped/500px/white-crystal-3.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);
        rval.add(cameraTwoAsset);

        return rval;
    }

    public static List<ControlOptionAsset> getDefaultGreenGemstoneAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/test/gemswap/small/cropped/500px/md-green-crystal-1.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/test/gemswap/small/cropped/500px/green-crystal-2.png");

        ControlOptionAsset cameraTwoAsset = new ControlOptionAsset();
        cameraTwoAsset.setCamera(2);
        cameraTwoAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/test/gemswap/small/cropped/500px/green-crystal-3.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);
        rval.add(cameraTwoAsset);

        return rval;
    }

    public static List<ControlOptionAsset> getDefaultRedGemstoneAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/test/gemswap/small/cropped/500px/md-red-crystal-1.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/test/gemswap/small/cropped/500px/red-crystal-2.png");

        ControlOptionAsset cameraTwoAsset = new ControlOptionAsset();
        cameraTwoAsset.setCamera(2);
        cameraTwoAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/test/gemswap/small/cropped/500px/red-crystal-3.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);
        rval.add(cameraTwoAsset);

        return rval;
    }

    public static List<ControlOptionAsset> getDefaultBlueGemstoneAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/test/gemswap/small/cropped/500px/md-blue-crystal-1.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/test/gemswap/small/cropped/500px/blue-crystal-2.png");

        ControlOptionAsset cameraTwoAsset = new ControlOptionAsset();
        cameraTwoAsset.setCamera(2);
        cameraTwoAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/test/gemswap/small/cropped/500px/blue-crystal-3.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);
        rval.add(cameraTwoAsset);

        return rval;
    }

    public static SimpleItemControl generateDefaultMaterialCustomization()
    {
        SimpleItemControl rval = new SimpleItemControl();
        rval.setControlName("Select Color");
        rval.setInteractionType(SimpleItemControl.INTERACTION_TYPE.ASSET_CHANGE);
        rval.setHidden(false);

        rval.setManufacturerSpecifier(new ManufacturerSpecifier(ManufacturerSpecifier.SPECIFIER_TYPE.MATERIAL));

        List<ControlOption> options = new ArrayList<>();

        ControlOption goldOption = new ControlOption();
        goldOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/gold.png");
        goldOption.setOptionDisplayName("Gold");
        goldOption.setOptionValue("gold");
        goldOption.setControlOptionAssets(getDefaultGoldMaterialAssets());
        goldOption.setManufacturerSpecifierOption(new ManufacturerSpecifier(ManufacturerSpecifier.SPECIFIER_TYPE.MATERIAL, ManufacturerSpecifier.SPECIFIER_OPTION.GOLD_PLATED));

        ControlOption silverOption = new ControlOption();
        silverOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/silver.png");
        silverOption.setOptionDisplayName("Silver");
        silverOption.setOptionValue("silver");
        silverOption.setControlOptionAssets(getDefaultSilverMaterialAssets());
        goldOption.setControlOptionAssets(getDefaultGoldMaterialAssets());
        goldOption.setManufacturerSpecifierOption(new ManufacturerSpecifier(ManufacturerSpecifier.SPECIFIER_TYPE.MATERIAL, ManufacturerSpecifier.SPECIFIER_OPTION.SILVER));

        ControlOption roseGoldOption = new ControlOption();
        roseGoldOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/rose.png");
        roseGoldOption.setOptionDisplayName("Rose Gold");
        roseGoldOption.setOptionValue("rosegold");
        roseGoldOption.setControlOptionAssets(getDefaultRoseGoldMaterialAssets());
        goldOption.setControlOptionAssets(getDefaultGoldMaterialAssets());
        goldOption.setManufacturerSpecifierOption(new ManufacturerSpecifier(ManufacturerSpecifier.SPECIFIER_TYPE.MATERIAL, ManufacturerSpecifier.SPECIFIER_OPTION.ROSE_GOLD_PLATED));

        options.add(goldOption);
        options.add(silverOption);
        options.add(roseGoldOption);

        rval.setControlOptions(options);

        return rval;
    }

    public static List<ControlOptionAsset> getDefaultGoldMaterialAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/test/gemswap/small/cropped/500px/gold-band-1.jpg");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/test/gemswap/small/cropped/500px/gold-band-2.jpg");

        ControlOptionAsset cameraTwoAsset = new ControlOptionAsset();
        cameraTwoAsset.setCamera(2);
        cameraTwoAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/test/gemswap/small/cropped/500px/gold-band-3.jpg");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);
        rval.add(cameraTwoAsset);

        return rval;
    }

    public static List<ControlOptionAsset> getDefaultSilverMaterialAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/test/gemswap/small/cropped/500px/silver-band-1.jpg");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/test/gemswap/small/cropped/500px/silver-band-2.jpg");

        ControlOptionAsset cameraTwoAsset = new ControlOptionAsset();
        cameraTwoAsset.setCamera(2);
        cameraTwoAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/test/gemswap/small/cropped/500px/silver-band-3.jpg");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);
        rval.add(cameraTwoAsset);

        return rval;
    }

    public static List<ControlOptionAsset> getDefaultRoseGoldMaterialAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/test/gemswap/small/cropped/500px/rose-gold-band-1.jpg");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/test/gemswap/small/cropped/500px/rose-gold-band-2.jpg");

        ControlOptionAsset cameraTwoAsset = new ControlOptionAsset();
        cameraTwoAsset.setCamera(2);
        cameraTwoAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/test/gemswap/small/cropped/500px/rose-gold-band-3.jpg");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);
        rval.add(cameraTwoAsset);

        return rval;
    }
}
