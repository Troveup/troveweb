package com.troveup.brooklyn.orm.simpleitem.initialize;

import com.troveup.brooklyn.orm.item.model.ItemImage;
import com.troveup.brooklyn.orm.simpleitem.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.troveup.brooklyn.orm.simpleitem.initialize.CommonInitializerMethods.generateDefaultRingSizeCustomization;

/**
 * Created by tim on 5/16/16.
 */
public class ChristenDominique {
    public static List<SimpleItem> generateInitialChristenDominiqueItems()
    {
        List<SimpleItem> rval = new ArrayList<>();

        //Cloud Charm Ring
        SimpleItem simpleItem = new SimpleItem();
        simpleItem.setControlReference(true);
        simpleItem.setPrimaryDisplayImageUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-charm-ring/cloud-charm-static.png");
        simpleItem.setItemName("Cloud Charm Ring");
        simpleItem.setItemPrice(BigDecimal.valueOf(32.99));
        simpleItem.setItemDescription("Something about Christen Dominiques's collection goes here. Something Something else goes here.");
        simpleItem.setUserCreatorName("Christen Dominique");

        List<SimpleItemControl> availableItemCustomizations = new ArrayList<>();
        availableItemCustomizations.add(generateCharmRingBandCustomization());
        availableItemCustomizations.add(generateCharmRingGemstoneCustomization());
        availableItemCustomizations.add(generateDefaultRingSizeCustomization());

        simpleItem.setSimpleItemControls(availableItemCustomizations);

        simpleItem.setTroveDisplayImages(getCloudCharmRingTroveDisplayImages());

        rval.add(simpleItem);

        //Cloud Pendant
        simpleItem = new SimpleItem();
        simpleItem.setControlReference(true);
        simpleItem.setPrimaryDisplayImageUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-pendant/cloud-pendant%20copy.png");
        simpleItem.setItemName("Cloud Pendant");
        simpleItem.setItemPrice(BigDecimal.valueOf(29.99));
        simpleItem.setItemDescription("Something about Christen Dominiques's collection goes here. Something Something else goes here.");
        simpleItem.setUserCreatorName("Christen Dominique");

        availableItemCustomizations = new ArrayList<>();
        availableItemCustomizations.add(generateCloudPendantMaterialCustomization());

        simpleItem.setSimpleItemControls(availableItemCustomizations);

        simpleItem.setTroveDisplayImages(getCloudPendantTroveDisplayImages());

        rval.add(simpleItem);

        //Cloud w Crystal
        simpleItem = new SimpleItem();
        simpleItem.setControlReference(true);
        simpleItem.setPrimaryDisplayImageUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-w-crystal/crystal-cloud-static.png");
        simpleItem.setItemName("Crystal Cloud Pendant");
        simpleItem.setItemPrice(BigDecimal.valueOf(32.99));
        simpleItem.setItemDescription("Something about Christen Dominiques's collection goes here. Something Something else goes here.");
        simpleItem.setUserCreatorName("Christen Dominique");

        availableItemCustomizations = new ArrayList<>();
        availableItemCustomizations.add(generateCrystalCloudPendantMaterialCustomization());
        availableItemCustomizations.add(generateCrystalCloudPendantCrystalCustomization());

        simpleItem.setSimpleItemControls(availableItemCustomizations);

        simpleItem.setTroveDisplayImages(getCrystalCloudPendantTroveDisplayImages());

        rval.add(simpleItem);

        //Heart Pendant
        simpleItem = new SimpleItem();
        simpleItem.setControlReference(true);
        simpleItem.setPrimaryDisplayImageUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/heart-pendant/heart-pendant-static.png");
        simpleItem.setItemName("Heart Pendant");
        simpleItem.setItemPrice(BigDecimal.valueOf(32.99));
        simpleItem.setItemDescription("Something about Christen Dominiques's collection goes here. Something Something else goes here.");
        simpleItem.setUserCreatorName("Christen Dominique");

        availableItemCustomizations = new ArrayList<>();
        availableItemCustomizations.add(generateHeartPendantMaterialCustomization());
        availableItemCustomizations.add(generateHeartPendantCrystalCustomization());

        simpleItem.setSimpleItemControls(availableItemCustomizations);

        simpleItem.setTroveDisplayImages(getHeartPendantTroveDisplayImages());

        rval.add(simpleItem);

        //Open Heart Pendant
        simpleItem = new SimpleItem();
        simpleItem.setControlReference(true);
        simpleItem.setPrimaryDisplayImageUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/open-heart-pendant/open-heart-gold%20copy.png");
        simpleItem.setItemName("Open Heart Pendant");
        simpleItem.setItemPrice(BigDecimal.valueOf(29.99));
        simpleItem.setItemDescription("Something about Christen Dominiques's collection goes here. Something Something else goes here.");
        simpleItem.setUserCreatorName("Christen Dominique");

        availableItemCustomizations = new ArrayList<>();
        availableItemCustomizations.add(generateOpenHeartPendantMaterialCustomization());

        simpleItem.setSimpleItemControls(availableItemCustomizations);

        simpleItem.setTroveDisplayImages(getOpenHeartPendantTroveDisplayImages());

        rval.add(simpleItem);

        //Unicorn Pendant
        simpleItem = new SimpleItem();
        simpleItem.setControlReference(true);
        simpleItem.setPrimaryDisplayImageUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/unicorn-pendant/unicorn-necklace%20copy.png");
        simpleItem.setItemName("Unicorn Pendant");
        simpleItem.setItemPrice(BigDecimal.valueOf(29.99));
        simpleItem.setItemDescription("Something about Christen Dominiques's collection goes here. Something Something else goes here.");
        simpleItem.setUserCreatorName("Christen Dominique");

        availableItemCustomizations = new ArrayList<>();
        availableItemCustomizations.add(generateUnicornPendantMaterialCustomization());

        simpleItem.setSimpleItemControls(availableItemCustomizations);

        simpleItem.setTroveDisplayImages(getUnicornPendantTroveDisplayImages());

        rval.add(simpleItem);

        return rval;
    }

    private static List<ItemImage> getUnicornPendantTroveDisplayImages()
    {
        List<ItemImage> rval = new ArrayList<>();

        ItemImage image = new ItemImage();
        image.setLargeImageUrlPath("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/unicorn-pendant/unicorn-necklace%20copy.png");
        rval.add(image);

        image = new ItemImage();
        image.setLargeImageUrlPath("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/unicorn-pendant/unicorn-necklace-rose-gold%20copy.png");
        rval.add(image);

        image = new ItemImage();
        image.setLargeImageUrlPath("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/unicorn-pendant/unicorn-necklace-silver%20copy.png");
        rval.add(image);

        return rval;
    }

    private static List<ItemImage> getOpenHeartPendantTroveDisplayImages()
    {
        List<ItemImage> rval = new ArrayList<>();

        ItemImage image = new ItemImage();
        image.setLargeImageUrlPath("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/open-heart-pendant/open-heart-gold%20copy.png");
        rval.add(image);

        image = new ItemImage();
        image.setLargeImageUrlPath("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/open-heart-pendant/open-heart-rose-gold%20copy.png");
        rval.add(image);

        image = new ItemImage();
        image.setLargeImageUrlPath("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/open-heart-pendant/open-heart-silver%20copy.png");
        rval.add(image);

        return rval;
    }

    private static List<ItemImage> getHeartPendantTroveDisplayImages()
    {
        List<ItemImage> rval = new ArrayList<>();

        ItemImage image = new ItemImage();
        image.setLargeImageUrlPath("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/heart-pendant/heart-pendant-static.png");
        rval.add(image);

        image = new ItemImage();
        image.setLargeImageUrlPath("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/heart-pendant/heart-pendant-rose-static.png");
        rval.add(image);

        image = new ItemImage();
        image.setLargeImageUrlPath("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/heart-pendant/heart-pendant-silver-static.png");
        rval.add(image);

        return rval;
    }

    private static List<ItemImage> getCrystalCloudPendantTroveDisplayImages()
    {
        List<ItemImage> rval = new ArrayList<>();

        ItemImage image = new ItemImage();
        image.setLargeImageUrlPath("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-w-crystal/crystal-cloud-static.png");
        rval.add(image);

        image = new ItemImage();
        image.setLargeImageUrlPath("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-w-crystal/crystal-cloud-rose-static.png");
        rval.add(image);

        image = new ItemImage();
        image.setLargeImageUrlPath("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-w-crystal/crystal-cloud-silver-static.png");
        rval.add(image);

        return rval;
    }

    private static List<ItemImage> getCloudPendantTroveDisplayImages()
    {
        List<ItemImage> rval = new ArrayList<>();

        ItemImage image = new ItemImage();
        image.setLargeImageUrlPath("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-pendant/cloud-pendant%20copy.png");
        rval.add(image);

        image = new ItemImage();
        image.setLargeImageUrlPath("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-pendant/cloud-pendant-rose-gold%20copy.png");
        rval.add(image);

        image = new ItemImage();
        image.setLargeImageUrlPath("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-pendant/cloud-pendant-silver%20copy.png");
        rval.add(image);

        return rval;

    }

    private static List<ItemImage> getCloudCharmRingTroveDisplayImages()
    {
        List<ItemImage> rval = new ArrayList<>();

        ItemImage image = new ItemImage();
        image.setLargeImageUrlPath("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-charm-ring/cloud-charm-static.png");
        rval.add(image);

        image = new ItemImage();
        image.setLargeImageUrlPath("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-charm-ring/cloud-charm-rose-static.png");
        rval.add(image);

        image = new ItemImage();
        image.setLargeImageUrlPath("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-charm-ring/cloud-charm-silver-static.png");
        rval.add(image);

        return rval;
    }

    private static SimpleItemControl generateUnicornPendantMaterialCustomization()
    {
        SimpleItemControl rval = new SimpleItemControl();
        rval.setControlName("Select Color");
        rval.setControlSelectedName("Color");
        rval.setInteractionType(SimpleItemControl.INTERACTION_TYPE.ASSET_CHANGE);
        rval.setHidden(false);

        rval.setManufacturerSpecifier(new ManufacturerSpecifier(ManufacturerSpecifier.SPECIFIER_TYPE.MATERIAL));

        List<ControlOption> options = new ArrayList<>();

        ControlOption goldOption = new ControlOption();
        goldOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/gold.png");
        goldOption.setOptionDisplayName("Gold");
        goldOption.setOptionValue("gold");
        goldOption.setControlOptionAssets(generateUnicornPendantGoldOptionAssets());

        ControlOption silverOption = new ControlOption();
        silverOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/silver.png");
        silverOption.setOptionDisplayName("Silver");
        silverOption.setOptionValue("silver");
        silverOption.setControlOptionAssets(generateUnicornPendantSilverOptionAssets());

        ControlOption roseGoldOption = new ControlOption();
        roseGoldOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/rose.png");
        roseGoldOption.setOptionDisplayName("Rose Gold");
        roseGoldOption.setOptionValue("rosegold");
        roseGoldOption.setControlOptionAssets(generateUnicornPendantRoseGoldOptionAssets());

        options.add(goldOption);
        options.add(silverOption);
        options.add(roseGoldOption);

        rval.setControlOptions(options);

        return rval;
    }

    private static List<ControlOptionAsset> generateUnicornPendantRoseGoldOptionAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/unicorn-pendant/unicorn-necklace-rose-gold%20copy.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/unicorn-pendant/unicorn-necklace-rose-gold-2%20copy.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static List<ControlOptionAsset> generateUnicornPendantSilverOptionAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/unicorn-pendant/unicorn-necklace-silver%20copy.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/unicorn-pendant/unicorn-necklace-silver-2%20copy.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static List<ControlOptionAsset> generateUnicornPendantGoldOptionAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/unicorn-pendant/unicorn-necklace%20copy.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/unicorn-pendant/unicorn-necklace-2%20copy.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static SimpleItemControl generateOpenHeartPendantMaterialCustomization()
    {
        SimpleItemControl rval = new SimpleItemControl();
        rval.setControlName("Select Color");
        rval.setControlSelectedName("Color");
        rval.setInteractionType(SimpleItemControl.INTERACTION_TYPE.ASSET_CHANGE);
        rval.setHidden(false);

        rval.setManufacturerSpecifier(new ManufacturerSpecifier(ManufacturerSpecifier.SPECIFIER_TYPE.MATERIAL));

        List<ControlOption> options = new ArrayList<>();

        ControlOption goldOption = new ControlOption();
        goldOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/gold.png");
        goldOption.setOptionDisplayName("Gold");
        goldOption.setOptionValue("gold");
        goldOption.setControlOptionAssets(generateOpenHeartPendantGoldOptionAssets());

        ControlOption silverOption = new ControlOption();
        silverOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/silver.png");
        silverOption.setOptionDisplayName("Silver");
        silverOption.setOptionValue("silver");
        silverOption.setControlOptionAssets(generateOpenHeartPendantSilverOptionAssets());

        ControlOption roseGoldOption = new ControlOption();
        roseGoldOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/rose.png");
        roseGoldOption.setOptionDisplayName("Rose Gold");
        roseGoldOption.setOptionValue("rosegold");
        roseGoldOption.setControlOptionAssets(generateOpenHeartPendantRoseGoldOptionAssets());

        options.add(goldOption);
        options.add(silverOption);
        options.add(roseGoldOption);

        rval.setControlOptions(options);

        return rval;
    }

    private static List<ControlOptionAsset> generateOpenHeartPendantRoseGoldOptionAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/open-heart-pendant/open-heart-rose-gold%20copy.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/open-heart-pendant/open-heart-rose-gold-2%20copy.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static List<ControlOptionAsset> generateOpenHeartPendantSilverOptionAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/open-heart-pendant/open-heart-silver%20copy.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/open-heart-pendant/open-heart-silver-2%20copy.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static List<ControlOptionAsset> generateOpenHeartPendantGoldOptionAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/open-heart-pendant/open-heart-gold%20copy.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/open-heart-pendant/open-heart-gold-1%20copy.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static SimpleItemControl generateHeartPendantCrystalCustomization()
    {
        SimpleItemControl rval = new SimpleItemControl();
        rval.setControlName("Select Crystal");
        rval.setControlSelectedName("Crystal");
        rval.setInteractionType(SimpleItemControl.INTERACTION_TYPE.ASSET_CHANGE);
        rval.setHidden(false);

        List<ControlOption> options = new ArrayList<>();

        ControlOption clearGemstoneOption = new ControlOption();
        clearGemstoneOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/clear.png");
        clearGemstoneOption.setOptionDisplayName("White");
        clearGemstoneOption.setOptionValue("white");
        clearGemstoneOption.setControlOptionAssets(generateHeartPendantWhiteGemstoneAssets());

        ControlOption greenGemstoneOption = new ControlOption();
        greenGemstoneOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/green.png");
        greenGemstoneOption.setOptionDisplayName("Peridot");
        greenGemstoneOption.setOptionValue("peridot");
        greenGemstoneOption.setControlOptionAssets(generateHeartPendantGreenGemstoneAssets());

        ControlOption redGemstoneOption = new ControlOption();
        redGemstoneOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/red.png");
        redGemstoneOption.setOptionDisplayName("Siam");
        redGemstoneOption.setOptionValue("siam");
        redGemstoneOption.setControlOptionAssets(generateHeartPendantRedGemstoneAssets());

        ControlOption blueGemstoneOption = new ControlOption();
        blueGemstoneOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/blue.png");
        blueGemstoneOption.setOptionDisplayName("Sapphire");
        blueGemstoneOption.setOptionValue("sapphire");
        blueGemstoneOption.setControlOptionAssets(generateHeartPendantBlueGemstoneAssets());

        options.add(clearGemstoneOption);
        options.add(greenGemstoneOption);
        options.add(redGemstoneOption);
        options.add(blueGemstoneOption);

        rval.setControlOptions(options);

        return rval;
    }

    private static List<ControlOptionAsset> generateHeartPendantBlueGemstoneAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/heart-pendant/heart-pendant-crystal-blue.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/heart-pendant/heart-pendant-crystal-blue-2-cb.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static List<ControlOptionAsset> generateHeartPendantRedGemstoneAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/heart-pendant/heart-pendant-crystal-red.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/heart-pendant/heart-pendant-crystal-red-2.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static List<ControlOptionAsset> generateHeartPendantGreenGemstoneAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/heart-pendant/heart-pendant-crystal-green.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/heart-pendant/heart-pendant-crystal-green-2.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static List<ControlOptionAsset> generateHeartPendantWhiteGemstoneAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/heart-pendant/heart-pendant-crystal-white.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/heart-pendant/heart-pendant-crystal-white-2.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static SimpleItemControl generateHeartPendantMaterialCustomization()
    {
        SimpleItemControl rval = new SimpleItemControl();
        rval.setControlName("Select Color");
        rval.setControlSelectedName("Color");
        rval.setInteractionType(SimpleItemControl.INTERACTION_TYPE.ASSET_CHANGE);
        rval.setHidden(false);

        rval.setManufacturerSpecifier(new ManufacturerSpecifier(ManufacturerSpecifier.SPECIFIER_TYPE.MATERIAL));

        List<ControlOption> options = new ArrayList<>();

        ControlOption goldOption = new ControlOption();
        goldOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/gold.png");
        goldOption.setOptionDisplayName("Gold");
        goldOption.setOptionValue("gold");
        goldOption.setControlOptionAssets(generateHeartPendantGoldOptionAssets());

        ControlOption silverOption = new ControlOption();
        silverOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/silver.png");
        silverOption.setOptionDisplayName("Silver");
        silverOption.setOptionValue("silver");
        silverOption.setControlOptionAssets(generateHeartPendantSilverOptionAssets());

        ControlOption roseGoldOption = new ControlOption();
        roseGoldOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/rose.png");
        roseGoldOption.setOptionDisplayName("Rose Gold");
        roseGoldOption.setOptionValue("rosegold");
        roseGoldOption.setControlOptionAssets(generateHeartPendantRoseGoldOptionAssets());

        options.add(goldOption);
        options.add(silverOption);
        options.add(roseGoldOption);

        rval.setControlOptions(options);

        return rval;
    }

    private static List<ControlOptionAsset> generateHeartPendantRoseGoldOptionAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/heart-pendant/heart-pendant-rose.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/heart-pendant/heart-pendant-rose-2.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static List<ControlOptionAsset> generateHeartPendantSilverOptionAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/heart-pendant/heart-pendant-silver.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/heart-pendant/heart-pendant-silver-2.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static List<ControlOptionAsset> generateHeartPendantGoldOptionAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/heart-pendant/heart-pendant-gold.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/heart-pendant/heart-pendant-gold-2.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static SimpleItemControl generateCrystalCloudPendantCrystalCustomization()
    {
        SimpleItemControl rval = new SimpleItemControl();
        rval.setControlName("Select Crystal");
        rval.setControlSelectedName("Crystal");
        rval.setInteractionType(SimpleItemControl.INTERACTION_TYPE.ASSET_CHANGE);
        rval.setHidden(false);

        List<ControlOption> options = new ArrayList<>();

        ControlOption clearGemstoneOption = new ControlOption();
        clearGemstoneOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/clear.png");
        clearGemstoneOption.setOptionDisplayName("White");
        clearGemstoneOption.setOptionValue("white");
        clearGemstoneOption.setControlOptionAssets(generateCrystalCloudPendantWhiteGemstoneAssets());

        ControlOption greenGemstoneOption = new ControlOption();
        greenGemstoneOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/green.png");
        greenGemstoneOption.setOptionDisplayName("Peridot");
        greenGemstoneOption.setOptionValue("peridot");
        greenGemstoneOption.setControlOptionAssets(generateCrystalCloudPendantGreenGemstoneAssets());

        ControlOption redGemstoneOption = new ControlOption();
        redGemstoneOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/red.png");
        redGemstoneOption.setOptionDisplayName("Siam");
        redGemstoneOption.setOptionValue("siam");
        redGemstoneOption.setControlOptionAssets(generateCrystalCloudPendantRedGemstoneAssets());

        ControlOption blueGemstoneOption = new ControlOption();
        blueGemstoneOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/blue.png");
        blueGemstoneOption.setOptionDisplayName("Sapphire");
        blueGemstoneOption.setOptionValue("sapphire");
        blueGemstoneOption.setControlOptionAssets(generateCrystalCloudPendantBlueGemstoneAssets());

        options.add(clearGemstoneOption);
        options.add(greenGemstoneOption);
        options.add(redGemstoneOption);
        options.add(blueGemstoneOption);

        rval.setControlOptions(options);

        return rval;
    }

    private static List<ControlOptionAsset> generateCrystalCloudPendantBlueGemstoneAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-w-crystal/crystal-cloud-blue-crystal%20copy.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-w-crystal/crystal-cloud-blue-crystal-2%20copy.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static List<ControlOptionAsset> generateCrystalCloudPendantRedGemstoneAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-w-crystal/crystal-cloud-red-crystal%20copy.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-w-crystal/crystal-cloud-red-crystal-2%20copy.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static List<ControlOptionAsset> generateCrystalCloudPendantGreenGemstoneAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-w-crystal/crystal-cloud-green-crystal%20copy.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-w-crystal/crystal-cloud-green-crystal-2%20copy.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static List<ControlOptionAsset> generateCrystalCloudPendantWhiteGemstoneAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-w-crystal/crystal-cloud-white-crystal%20copy.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-w-crystal/crystal-cloud-white-crystal-2%20copy.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static SimpleItemControl generateCrystalCloudPendantMaterialCustomization()
    {
        SimpleItemControl rval = new SimpleItemControl();
        rval.setControlName("Select Color");
        rval.setControlSelectedName("Color");
        rval.setInteractionType(SimpleItemControl.INTERACTION_TYPE.ASSET_CHANGE);
        rval.setHidden(false);

        rval.setManufacturerSpecifier(new ManufacturerSpecifier(ManufacturerSpecifier.SPECIFIER_TYPE.MATERIAL));

        List<ControlOption> options = new ArrayList<>();

        ControlOption goldOption = new ControlOption();
        goldOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/gold.png");
        goldOption.setOptionDisplayName("Gold");
        goldOption.setOptionValue("gold");
        goldOption.setControlOptionAssets(generateCrystalCloudPendantGoldOptionAssets());

        ControlOption silverOption = new ControlOption();
        silverOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/silver.png");
        silverOption.setOptionDisplayName("Silver");
        silverOption.setOptionValue("silver");
        silverOption.setControlOptionAssets(generateCrystalCloudPendantSilverOptionAssets());

        ControlOption roseGoldOption = new ControlOption();
        roseGoldOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/rose.png");
        roseGoldOption.setOptionDisplayName("Rose Gold");
        roseGoldOption.setOptionValue("rosegold");
        roseGoldOption.setControlOptionAssets(generateCrystalCloudPendantRoseGoldOptionAssets());

        options.add(goldOption);
        options.add(silverOption);
        options.add(roseGoldOption);

        rval.setControlOptions(options);

        return rval;
    }

    private static List<ControlOptionAsset> generateCrystalCloudPendantRoseGoldOptionAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-w-crystal/crystal-cloud-rose%20copy.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-w-crystal/crystal-cloud-rose-2%20copy.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static List<ControlOptionAsset> generateCrystalCloudPendantSilverOptionAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-w-crystal/crystal-cloud-silver%20copy.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-w-crystal/crystal-cloud-silver-2%20copy.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static List<ControlOptionAsset> generateCrystalCloudPendantGoldOptionAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-w-crystal/crystal-cloud-gold%20copy.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-w-crystal/crystal-cloud-gold-2%20copy.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static SimpleItemControl generateCloudPendantMaterialCustomization()
    {
        SimpleItemControl rval = new SimpleItemControl();
        rval.setControlName("Select Color");
        rval.setControlSelectedName("Color");
        rval.setInteractionType(SimpleItemControl.INTERACTION_TYPE.ASSET_CHANGE);
        rval.setHidden(false);

        rval.setManufacturerSpecifier(new ManufacturerSpecifier(ManufacturerSpecifier.SPECIFIER_TYPE.MATERIAL));

        List<ControlOption> options = new ArrayList<>();

        ControlOption goldOption = new ControlOption();
        goldOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/gold.png");
        goldOption.setOptionDisplayName("Gold");
        goldOption.setOptionValue("gold");
        goldOption.setControlOptionAssets(generateCloudPendantGoldOptionAssets());

        ControlOption silverOption = new ControlOption();
        silverOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/silver.png");
        silverOption.setOptionDisplayName("Silver");
        silverOption.setOptionValue("silver");
        silverOption.setControlOptionAssets(generateCloudPendantSilverOptionAssets());

        ControlOption roseGoldOption = new ControlOption();
        roseGoldOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/rose.png");
        roseGoldOption.setOptionDisplayName("Rose Gold");
        roseGoldOption.setOptionValue("rosegold");
        roseGoldOption.setControlOptionAssets(generateCloudPendantRoseGoldOptionAssets());

        options.add(goldOption);
        options.add(silverOption);
        options.add(roseGoldOption);

        rval.setControlOptions(options);

        return rval;
    }

    private static List<ControlOptionAsset> generateCloudPendantGoldOptionAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-pendant/cloud-pendant%20copy.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-pendant/cloud-pendant-gold-2%20copy.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static List<ControlOptionAsset> generateCloudPendantSilverOptionAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-pendant/cloud-pendant-silver%20copy.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-pendant/cloud-pendant-silver-2%20copy.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static List<ControlOptionAsset> generateCloudPendantRoseGoldOptionAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-pendant/cloud-pendant-rose-gold%20copy.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-pendant/cloud-pendant-rose-gold-2%20copy.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static SimpleItemControl generateCharmRingBandCustomization()
    {
        SimpleItemControl rval = new SimpleItemControl();
        rval.setControlName("Select Color");
        rval.setControlSelectedName("Color");
        rval.setInteractionType(SimpleItemControl.INTERACTION_TYPE.ASSET_CHANGE);
        rval.setHidden(false);

        rval.setManufacturerSpecifier(new ManufacturerSpecifier(ManufacturerSpecifier.SPECIFIER_TYPE.MATERIAL));

        List<ControlOption> options = new ArrayList<>();

        ControlOption goldOption = new ControlOption();
        goldOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/gold.png");
        goldOption.setOptionDisplayName("Gold");
        goldOption.setOptionValue("gold");
        goldOption.setControlOptionAssets(generateCharmRingGoldMaterialBandAssets());

        ControlOption silverOption = new ControlOption();
        silverOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/silver.png");
        silverOption.setOptionDisplayName("Silver");
        silverOption.setOptionValue("silver");
        silverOption.setControlOptionAssets(generateCharmRingSilverMaterialBandAssets());

        ControlOption roseGoldOption = new ControlOption();
        roseGoldOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/rose.png");
        roseGoldOption.setOptionDisplayName("Rose Gold");
        roseGoldOption.setOptionValue("rosegold");
        roseGoldOption.setControlOptionAssets(generateCharmRingRoseGoldMaterialBandAssets());

        options.add(goldOption);
        options.add(silverOption);
        options.add(roseGoldOption);

        rval.setControlOptions(options);

        return rval;
    }

    private static List<ControlOptionAsset> generateCharmRingGoldMaterialBandAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-charm-ring/cloud-charm-ring-gold-1%20copy.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-charm-ring/cloud-charm-ring-gold-2%20copy.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static List<ControlOptionAsset> generateCharmRingSilverMaterialBandAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-charm-ring/cloud-charm-ring-silver-1%20copy.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-charm-ring/cloud-charm-ring-silver-2%20copy.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static List<ControlOptionAsset> generateCharmRingRoseGoldMaterialBandAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-charm-ring/cloud-charm-ring-rose-1%20copy.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-charm-ring/cloud-charm-ring-rose-2%20copy.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static SimpleItemControl generateCharmRingGemstoneCustomization()
    {
        SimpleItemControl rval = new SimpleItemControl();
        rval.setControlName("Select Crystal");
        rval.setControlSelectedName("Crystal");
        rval.setInteractionType(SimpleItemControl.INTERACTION_TYPE.ASSET_CHANGE);
        rval.setHidden(false);

        List<ControlOption> options = new ArrayList<>();

        ControlOption clearGemstoneOption = new ControlOption();
        clearGemstoneOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/clear.png");
        clearGemstoneOption.setOptionDisplayName("White");
        clearGemstoneOption.setOptionValue("white");
        clearGemstoneOption.setControlOptionAssets(generateCharmRingWhiteGemstoneAssets());

        ControlOption greenGemstoneOption = new ControlOption();
        greenGemstoneOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/green.png");
        greenGemstoneOption.setOptionDisplayName("Peridot");
        greenGemstoneOption.setOptionValue("peridot");
        greenGemstoneOption.setControlOptionAssets(generateCharmRingGreenGemstoneAssets());

        ControlOption redGemstoneOption = new ControlOption();
        redGemstoneOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/red.png");
        redGemstoneOption.setOptionDisplayName("Siam");
        redGemstoneOption.setOptionValue("siam");
        redGemstoneOption.setControlOptionAssets(generateCharmRingRedGemstoneAssets());

        ControlOption blueGemstoneOption = new ControlOption();
        blueGemstoneOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/blue.png");
        blueGemstoneOption.setOptionDisplayName("Sapphire");
        blueGemstoneOption.setOptionValue("sapphire");
        blueGemstoneOption.setControlOptionAssets(generateCharmRingBlueGemstoneAssets());

        options.add(clearGemstoneOption);
        options.add(greenGemstoneOption);
        options.add(redGemstoneOption);
        options.add(blueGemstoneOption);

        rval.setControlOptions(options);

        return rval;
    }

    private static List<ControlOptionAsset> generateCharmRingWhiteGemstoneAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-charm-ring/cloud-charm-ring-white-crystal-1%20copy.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-charm-ring/cloud-charm-ring-white-crystal-2.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static List<ControlOptionAsset> generateCharmRingGreenGemstoneAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-charm-ring/cloud-charm-ring-green-crystal-1%20copy.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-charm-ring/cloud-charm-ring-green-crystal-2%20copy.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static List<ControlOptionAsset> generateCharmRingRedGemstoneAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-charm-ring/cloud-charm-ring-red-crystal-1%20copy.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-charm-ring/cloud-charm-ring-red-crystal-2%20copy.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }

    private static List<ControlOptionAsset> generateCharmRingBlueGemstoneAssets()
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        ControlOptionAsset cameraZeroAsset = new ControlOptionAsset();
        cameraZeroAsset.setCamera(0);
        cameraZeroAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-charm-ring/cloud-charm-ring-blue-crystal-1%20copy.png");

        ControlOptionAsset cameraOneAsset = new ControlOptionAsset();
        cameraOneAsset.setCamera(1);
        cameraOneAsset.setControlOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/cloud-charm-ring/cloud-charm-ring-blue-crystal-2%20copy.png");

        rval.add(cameraZeroAsset);
        rval.add(cameraOneAsset);

        return rval;
    }
}
