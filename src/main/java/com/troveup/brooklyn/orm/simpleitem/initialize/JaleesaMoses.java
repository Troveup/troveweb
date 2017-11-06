package com.troveup.brooklyn.orm.simpleitem.initialize;

import com.troveup.brooklyn.orm.item.model.ItemImage;
import com.troveup.brooklyn.orm.simpleitem.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 5/16/16.
 */
public class JaleesaMoses {

    //TODO:  Uncomment when we have the rest of the assets
    //private static String[] itemSpecifiers = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "11", "22", "33"};
    private static String[] itemSpecifiers = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "11", "22", "33"};
    private static String[] itemNames = new String[] {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Eleven", "Twenty-two", "Thirty-three"};
    private static String[] itemMaterial = new String[] {"silver", "silver", "gold", "silver", "silver", "rose", "silver", "silver", "gold", "silver", "silver", "rose"};
    private static String[] itemTopDescription = new String[] {"A necklace for the authentic and committed.", "A necklace for the artistic and emotionally aware soul.", "Artists and creatives - this necklace is just for you.", "A necklace for the problem solvers of the world.", "This necklace symbolizes the adventurous and curious spirit.", "A necklace for the nurturers, healers, and helpers.", "A necklace for the spirited and inquisitive. Also, Jaleesa’s own number!", "A necklace for the goal-oriented leaders of the world.", "Compassionate? Idealistic? This necklace is perfect for you.", "This necklace is perfect for the compassionate and idealistic.", "The number 22 symbolizes great spiritual understanding.", "The perfect number for the unselfish movers and shakers."};
    private static String[] itemBottomDescriptionText = new String[] {"As a Number 1, you value determination, independence, ambition, and authenticity. You have conviction in your decisions and express ownership over your thoughts and actions. You are tenacious and motivated. Once you are committed to a goal, nothing and no one can get in your way.", "As a Number 2, you possess an artist’s soul. Your sensitivity and perceptiveness allow you to adapt to situations in a diplomatic way. You are conscious of your feelings and the feelings of others, giving you the ability to understand and accommodate the emotional needs of yourself and others.", "Life Path 3 is home to actors, poets, writers, and musicians. Your robust creativity and wonderful communication skills allow you to engage in artistic expression, especially in the realm of speaking, writing, or acting. You require commitment and discipline for your talent to blossom.", "As a Number 4, your organizational and methodical skills allow you to take a balanced approach to solving any problem. You are decisive and rational. You are both idealistic and practical, allowing you to devise magnificent plans and bring them to fruition.", "As a Number 5, you are adventurous and curious. You have a hunger for life and an inclination for freedom. You cannot be tied down, as you are in constantly changing and evolving. You are compassionate and friendly. Your upbeat, happy-go-lucky personality is inspiring to those that surround you.", "As a Number 6, you are a nurturer, healer, and helper. You provide warmth and solace to those in need, often putting others before yourself. Your joy stems from your utility. You are most happy when you are offering support, service, and advice to others.", "As a Number 7, you have a strong sense of spirit. You are inquisitive and observant. You value thoroughness, and you often pursue perfection. You are a thinker and an investigator, who aims to understand the unknown and find answers to life’s burning questions.", "As a Number 8, you are a leader. You are goal-oriented and possess the ability to organize, govern, and direct. You have a keen eye for the big picture and courage in your convictions, especially with regard to getting ahead and following goals to completion.", "As a Number 9, you are compassionate and idealistic. You are motivated by your utopian mentality. You are willing to make large sacrifices in an effort to make the world a better place. You find joy in giving, especially to those who are less fortunate than yourself.", "As a Number 11, you are intuitive and energetic. You have the innate ability to understand and inspire others. You live a life of extremes. You possess more potential than you know. While your energy may sometimes be misunderstood, you are naturally sensitive and insightful.", "As a Number 22, you possess a great spiritual understanding. Intuition is your natural gift. You have the ability to harness the beauty of an idea, then follow it through to reality. You are hardworking and dedicated. You have great potential for power and success.", "As a Number 33, you are altruistic and unselfish. You are a mover and a shaker, using your wisdom and understanding to reach for your humanitarian goals. You are happiest when you are focusing your emotions and spirit on helping others."};
    private static String BOTTOM_DESCRIPTION_TEXT_FIRST_PARAGRAPH = "Inspired by her own spirituality and search for positive energy in the world, Jaleesa designed her inaugural jewelry collection to draw from the concepts of Numerology - the study of numbers and how certain numbers can predict or reflect different character tendencies with rippling effects throughout one’s life.\\nYour Life Path Number, calculated from your birthday, is said to tell your traits at birth and can even predict your journey through life. ";

    public static List<SimpleItem> generateInitialJaleesaMosesItems()
    {
        List<SimpleItem> rval = new ArrayList<>();

        for (int i = 0; i < itemSpecifiers.length; ++i)
        {
            SimpleItem simpleItem = new SimpleItem();
            simpleItem.setControlReference(true);
            simpleItem.setPrimaryDisplayImageUrl(getGoogleCloudStorageUrlPath(itemSpecifiers[i]) + itemSpecifiers[i] + "-necklace-" + itemMaterial[i] +"-static.png");
            simpleItem.setHoverImageUrl(getGoogleCloudStorageUrlPath(itemSpecifiers[i]) + "jaleesa_" + itemSpecifiers[i] + "_hover.jpg");
            simpleItem.setItemName("Life Path Necklace - " + itemNames[i]);
            simpleItem.setItemPrice(BigDecimal.valueOf(49.99));
            simpleItem.setItemDescription(itemTopDescription[i]);
            simpleItem.setBottomDescriptionTitle("Numerology Collection");

            String descriptionText = BOTTOM_DESCRIPTION_TEXT_FIRST_PARAGRAPH + itemBottomDescriptionText[i];

            simpleItem.setBottomDescriptionText(descriptionText);
            simpleItem.setUserCreatorName("Jaleesa Moses");
            simpleItem.setUrlFriendlyReferenceItemName("numerology_collection_" + itemSpecifiers[i] + "_necklace");

            List<SimpleItemControl> availableItemCustomizations = new ArrayList<>();
            availableItemCustomizations.add(generateMaterialCustomization(itemSpecifiers[i]));
            availableItemCustomizations.add(generateGemstoneCustomization(itemSpecifiers[i]));

            simpleItem.setSimpleItemControls(availableItemCustomizations);

            simpleItem.setTroveDisplayImages(getDisplayImages(itemSpecifiers[i]));

            simpleItem.setStaticAssets(generateItemStaticAssets(itemSpecifiers[i]));

            rval.add(simpleItem);
        }

        return rval;
    }

    private static List<StaticAsset> generateItemStaticAssets(String item)
    {
        List<StaticAsset> rval = new ArrayList<>();

        StaticAsset asset = new StaticAsset();
        asset.setAssetUrl(getGoogleCloudStorageUrlPath(item) + "jaleesa_" + item + "_hover.jpg");
        rval.add(asset);

        return rval;
    }

    private static SimpleItemControl generateMaterialCustomization(String item)
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
        goldOption.setControlOptionAssets(generateMaterialAssets("gold", item));

        ControlOption silverOption = new ControlOption();
        silverOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/silver.png");
        silverOption.setOptionDisplayName("Silver");
        silverOption.setOptionValue("silver");
        silverOption.setControlOptionAssets(generateMaterialAssets("silver", item));

        ControlOption roseGoldOption = new ControlOption();
        roseGoldOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/rose.png");
        roseGoldOption.setOptionDisplayName("Rose Gold");
        roseGoldOption.setOptionValue("rose");
        roseGoldOption.setControlOptionAssets(generateMaterialAssets("rose", item));

        options.add(goldOption);
        options.add(silverOption);
        options.add(roseGoldOption);

        rval.setControlOptions(options);

        return rval;
    }

    private static List<ControlOptionAsset> generateMaterialAssets(String color, String item)
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        for (int i = 0; i < 2; ++i)
        {
            String endStringSpecifier = i > 0 ? "-angle" + (i + 1) + "-no-gem.png" : "-no-gem.png";

            ControlOptionAsset asset = new ControlOptionAsset();
            asset.setCamera(i);
            asset.setControlOptionAssetUrl(getGoogleCloudStorageUrlPath(item) + item + "-necklace-" + color + endStringSpecifier);

            rval.add(asset);
        }

        return rval;
    }

    private static SimpleItemControl generateGemstoneCustomization(String item)
    {
        SimpleItemControl rval = new SimpleItemControl();
        rval.setControlName("Select Crystal Color");
        rval.setControlSelectedName("Crystal");
        rval.setInteractionType(SimpleItemControl.INTERACTION_TYPE.ASSET_CHANGE);
        rval.setHidden(false);

        List<ControlOption> options = new ArrayList<>();

        ControlOption clearGemstoneOption = new ControlOption();
        clearGemstoneOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/crystal_clear.png");
        clearGemstoneOption.setOptionDisplayName("Clear");
        clearGemstoneOption.setOptionValue("clear");
        clearGemstoneOption.setControlOptionAssets(generateCrystalAssets("clear", item));

        ControlOption amethystGemstoneOption = new ControlOption();
        amethystGemstoneOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/crystal_amethyst.png");
        amethystGemstoneOption.setOptionDisplayName("Amethyst");
        amethystGemstoneOption.setOptionValue("amethyst");
        amethystGemstoneOption.setControlOptionAssets(generateCrystalAssets("amethyst", item));

        ControlOption cobaltGemstoneOption = new ControlOption();
        cobaltGemstoneOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/crystal_cobalt.png");
        cobaltGemstoneOption.setOptionDisplayName("Cobalt");
        cobaltGemstoneOption.setOptionValue("cobalt");
        cobaltGemstoneOption.setControlOptionAssets(generateCrystalAssets("cobalt", item));

        ControlOption roseGemstoneOption = new ControlOption();
        roseGemstoneOption.setOptionAssetUrl("https://storage.googleapis.com/troveup-imagestore/assets/img/swat/crystal_rose.png");
        roseGemstoneOption.setOptionDisplayName("Rose");
        roseGemstoneOption.setOptionValue("rose");
        roseGemstoneOption.setControlOptionAssets(generateCrystalAssets("rose", item));

        options.add(clearGemstoneOption);
        options.add(amethystGemstoneOption);
        options.add(cobaltGemstoneOption);
        options.add(roseGemstoneOption);

        rval.setControlOptions(options);

        return rval;
    }

    private static List<ControlOptionAsset> generateCrystalAssets(String color, String item)
    {
        List<ControlOptionAsset> rval = new ArrayList<>();

        for (int i = 0; i < 2; ++i)
        {
            String endStringSpecifier = i > 0 ? "angle" + (i + 1) + "-" + color + "-crystal.png" : color + "-crystal.png";

            ControlOptionAsset asset = new ControlOptionAsset();
            asset.setCamera(i);
            asset.setControlOptionAssetUrl(getGoogleCloudStorageUrlPath(item) + item + "-necklace-" + endStringSpecifier);

            rval.add(asset);
        }

        return rval;
    }

    private static List<ItemImage> getDisplayImages(String item)
    {
        List<ItemImage> rval = new ArrayList<>();

        ItemImage image = new ItemImage();
        image.setLargeImageUrlPath(getGoogleCloudStorageUrlPath(item) + item + "-necklace-gold-static.png");
        rval.add(image);

        image = new ItemImage();
        image.setLargeImageUrlPath(getGoogleCloudStorageUrlPath(item) + item + "-necklace-silver-static.png");
        rval.add(image);

        image = new ItemImage();
        image.setLargeImageUrlPath(getGoogleCloudStorageUrlPath(item) + item + "-necklace-rose-static.png");
        rval.add(image);

        return rval;
    }

    private static String getGoogleCloudStorageUrlPath(String item)
    {
        return "https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/jaleesamoses/items/" + item + "/";
    }
}
