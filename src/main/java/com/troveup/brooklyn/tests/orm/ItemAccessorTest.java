package com.troveup.brooklyn.tests.orm;

import com.google.gson.Gson;
import com.troveup.brooklyn.controllers.trove.WorkerController;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.item.interfaces.IItemAccessor;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.item.model.ItemAttribute;
import com.troveup.brooklyn.orm.item.model.ItemImage;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.http.impl.HttpClient;
import com.troveup.brooklyn.sdk.http.interfaces.IHttpClientFactory;
import com.troveup.brooklyn.util.SizeMapper;
import com.troveup.brooklyn.util.WorkerQueuer;
import com.troveup.brooklyn.util.models.UrlResponse;
import com.troveup.config.PersistenceConfig;
import com.troveup.config.SDKConfig;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by tim on 4/24/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, SDKConfig.class})
@PropertySource("classpath:com/troveup/config/application.properties")
public class ItemAccessorTest
{
    public static String ITEM_SPECIFIER = "item";
    public static String ITEM_IMAGE_SPECIFIER = "image";
    public static String ATTRIBUTE_SPECIFIER = "attribute";
    public static String ITEM_NAME_SPECIFIER = "itemName";
    public static String REFRESH_BASE_ITEM_UUID = "71eb7fb4-a3b5-47fc-bbfb-35e376a1d1d3";

    @Autowired
    IItemAccessor itemAccessor;

    @Autowired
    Environment env;

    @Autowired
    IHttpClientFactory httpClientFactory;

    @Autowired
    Gson gson;

    Item item;

    @Before
    public void setUp()
    {
        //item = (Item) persistNewTestItem().get(ITEM_SPECIFIER);
    }

    @Test
    public void testCreateItem()
    {
        Assert.assertTrue(itemAccessor.createItem(item));
    }

    @Test
    public void testGetItemsByName()
    {
        Map<String, Object> createdItemDetails = persistNewTestItem();
        String itemName = (String) createdItemDetails.get(ITEM_NAME_SPECIFIER);

        List<Item> items = itemAccessor.getItems(itemName, Item.SEARCH_BY_TYPE.ITEM_NAME,
                IEnums.SEEK_MODE.QUICK);

        Assert.assertTrue(items != null && items.size() > 0);

        for (Item testItem : items)
        {
            Assert.assertTrue(testItem.getItemName().equals(itemName));
        }
    }

    @Test
    public void testGetItemById()
    {
        Map<String, Object> createdItemDetails = persistNewTestItem();
        Item createdItem = (Item) createdItemDetails.get(ITEM_SPECIFIER);

        Item testItem = itemAccessor.getItemDetached(createdItem.getItemId(), IEnums.SEEK_MODE.QUICK);

        Assert.assertTrue(testItem != null && testItem.getItemId() == createdItem.getItemId());
    }

    @Test
    public void testUpdateItem()
    {
        //Persist a new item
        Map<String, Object> createdItemDetails = persistNewTestItem();

        //Get the persisted item
        Item item = (Item) createdItemDetails.get(ITEM_SPECIFIER);

        //Set up a new attribute
        List<ItemAttribute> attributes = new ArrayList<>();
        ItemAttribute attribute = createTestAttribute();
        attributes.add(attribute);
        item.setItemAttributes(attributes);

        //Set up a new image
        List<ItemImage> images = new ArrayList<>();
        ItemImage image = createTestImage();
        images.add(image);
        item.setImages(images);

        //Change the item name for giggles
        String newItemName = UUID.randomUUID().toString();
        item.setItemName(newItemName);

        //Test appending first
        Assert.assertTrue(itemAccessor.updateItem(item, IEnums.UPDATE_MODE.APPEND));

        //Retrieve the item and test its contents
        item = itemAccessor.getItemDetached(item.getItemId(), IEnums.SEEK_MODE.FULL);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getItemAttributes().size() == 2);
        Assert.assertTrue(item.getImages().size() == 2);
        Assert.assertTrue(item.getItemName().equals(newItemName));

        //Retrieve the original attributes and ensure that they're all the same
        ItemAttribute origAttribute = (ItemAttribute) createdItemDetails.get(ATTRIBUTE_SPECIFIER);
        ItemImage origImage = (ItemImage) createdItemDetails.get(ITEM_IMAGE_SPECIFIER);

        for(ItemAttribute testAtt : item.getItemAttributes())
        {
            Assert.assertTrue(testAtt.getAttributeName().equals(origAttribute.getAttributeName()) ||
            testAtt.getAttributeName().equals(attribute.getAttributeName()));
        }

        for(ItemImage testImage : item.getImages())
        {
            Assert.assertTrue(testImage.getImageName().equals(origImage.getImageName()) ||
                    testImage.getImageName().equals(image.getImageName()));
        }

        //Test replacing next, grab the original item so it's not contaminated
        item = (Item) createdItemDetails.get(ITEM_SPECIFIER);
        item.setItemAttributes(attributes);
        item.setImages(images);

        //Double check our work
        Assert.assertTrue(item.getImages().size() == 1);
        Assert.assertTrue(item.getItemAttributes().size() == 1);

        //Run the update
        Assert.assertTrue(itemAccessor.updateItem(item, IEnums.UPDATE_MODE.REPLACE));

        //Retrieve the updated item
        item = itemAccessor.getItemDetached(item.getItemId(), IEnums.SEEK_MODE.FULL);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getItemName().equals(newItemName));
        Assert.assertTrue(item.getImages().size() == 1);
        Assert.assertTrue(item.getItemAttributes().size() == 1);

        //Test to be sure we actually replaced the things that should have been replaced
        for(ItemAttribute testAtt : item.getItemAttributes())
        {
            Assert.assertTrue(testAtt.getAttributeName().equals(attribute.getAttributeName()));
        }

        for(ItemImage testImage : item.getImages())
        {
            Assert.assertTrue(testImage.getImageName().equals(image.getImageName()));
        }
    }

    @Test
    public void testDeactivateItem()
    {
        Map<String, Object> createdItemDetails = persistNewTestItem();
        Item item = (Item) createdItemDetails.get(ITEM_SPECIFIER);

        Assert.assertTrue(itemAccessor.deactivateItem(item.getItemId()));

        item = itemAccessor.getItemDetached(item.getItemId(), IEnums.SEEK_MODE.QUICK);

        Assert.assertFalse(item.isActive());
    }

    @Test
    public void testActivateItem()
    {
        Item item = createTestItem();
        item.setActive(false);
        Assert.assertTrue(itemAccessor.createItem(item));

        Assert.assertTrue(itemAccessor.activateItem(item.getItemId()));

        item = itemAccessor.getItemDetached(item.getItemId(), IEnums.SEEK_MODE.QUICK);

        Assert.assertTrue(item.isActive());
    }

    @Test
    public void testAddItemAttributes()
    {
        Map<String, Object> createdItemDetails = persistNewTestItem();
        Item item = (Item) createdItemDetails.get(ITEM_SPECIFIER);

        List<ItemAttribute> attributes = new ArrayList<>();
        ItemAttribute attribute = createTestAttribute();
        attributes.add(attribute);

        Assert.assertTrue(itemAccessor.addItemAttributes(item.getItemId(), attributes));
        item = itemAccessor.getItemDetached(item.getItemId(), IEnums.SEEK_MODE.FULL);

        Assert.assertNotNull(item);
        Assert.assertTrue(item.getItemAttributes().size() == 2);

        //Retrieve the original attributes and ensure that they're all the same
        ItemAttribute origAttribute = (ItemAttribute) createdItemDetails.get(ATTRIBUTE_SPECIFIER);

        for(ItemAttribute testAtt : item.getItemAttributes())
        {
            Assert.assertTrue(testAtt.getAttributeName().equals(origAttribute.getAttributeName()) ||
                    testAtt.getAttributeName().equals(attribute.getAttributeName()));
        }
    }

    @Test
    public void testGetItemsByDateRange()
    {
        try {
            Date createdDate = new SimpleDateFormat("MM/dd/yyyy").parse("04/24/2015");
            Date from = new SimpleDateFormat("MM/dd/yyyy").parse("04/23/2015");
            Date to = new SimpleDateFormat("MM/dd/yyyy").parse("04/24/2015");

            Date toCompare = new SimpleDateFormat("MM/dd/yyyy").parse("04/25/2015");

            Map<String, Object> createdItemDetails1 = persistNewTestItem(createdDate);
            Map<String, Object> createdItemDetails2 = persistNewTestItem(createdDate);
            Map<String, Object> createdItemDetails3 = persistNewTestItem(createdDate);

            List<Item> testItems = itemAccessor.getItems(from, to, IEnums.SEEK_MODE.QUICK);
            Assert.assertNotNull(testItems);
            Assert.assertTrue(testItems.size() > 0);

            for (Item item : testItems)
            {
                Assert.assertTrue(item.getCreated().after(from) && item.getCreated().before(toCompare));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testGetItemImageOrdered()
    {
        Map<String, Object> createdItemDetails = persistNewTestItem();
        Item item = (Item) createdItemDetails.get(ITEM_SPECIFIER);

        List<ItemImage> images = new ArrayList<>();

        for (int i = 0; i < 3; ++i)
            images.add(createTestImage());

        itemAccessor.addImage(item.getItemId(), images);

        item = itemAccessor.getItemDetached(item.getItemId(), IEnums.SEEK_MODE.FULL);

        for (int i = 0; i < item.getImages().size(); ++i)
            Assert.assertTrue(item.getImages().get(i).getOrder() == i);

    }

    @Test
    public void testFetchGroupings()
    {
        Map<String, Object> persistedItem = persistNewTestItem();

        Item testItem = itemAccessor.getItemDetached(((Item)persistedItem.get(ITEM_SPECIFIER)).getItemId(), IEnums.SEEK_MODE.ITEM_ATTRIBUTES);

        Assert.assertNull(testItem.getImages());
        Assert.assertNotNull(testItem.getItemAttributes());
        Assert.assertTrue(testItem.getItemAttributes().size() > 0);

        testItem = itemAccessor.getItemDetached(((Item)persistedItem.get(ITEM_SPECIFIER)).getItemId(), IEnums.SEEK_MODE.ITEM_FEED);

        Assert.assertNull(testItem.getItemAttributes());
        Assert.assertNotNull(testItem.getImages());
        Assert.assertTrue(testItem.getImages().size() > 0);
    }

    @Test
    public void testUpdateItemCounters()
    {
        List<Item> items = itemAccessor.getAllActiveItems(IEnums.SEEK_MODE.QUICK);

        for (Item item : items)
        {
            itemAccessor.updateItemCounters(item.getItemId());
        }

        Assert.assertNotNull(items);
    }

    @Test
    public void populateNewItems()
    {
        //13 total?
        Map<String, String> bracelets = new HashMap<>();
        //bracelets.put("open-trapezoid_bracelet.json", "open-trapezoid_bracelet_hires.json");

        Map<String, String> rings = new HashMap<>();

        //Already pushed
        //rings.put("bar_ring.json", "bar_ring.json");
        //rings.put("bar_ring.json", "bar_ring_hires.json");

        //Too high res, switching to low
        //rings.put("hexagon-shape_ring.json", "hexagon-shape_ring.json");
        //rings.put("hexagon-shape_ring.json", "hexagon-shape_ring_hires.json");

        //Too high res, switching to low
        //rings.put("mirror-no-hole_ring.json", "mirror-no-hole_ring.json");
        //rings.put("mirror-no-hole_ring.json", "mirror-no-hole_ring_hires.json");

        //Too high res, switching to low
        //rings.put("offset-extruded_ring.json", "offset-extruded_ring.json");
        //rings.put("offset-extruded_ring.json", "offset-extruded_ring_hires.json");

        //Already pushed
        //rings.put("parted_ring.json", "parted_ring.json");
        //rings.put("parted_ring.json", "parted_ring_hires.json");

        //Too high res, switching to low
        //rings.put("square-shape_ring.json", "square-shape_ring.json");
        //rings.put("square-shape_ring.json", "square-shape_ring_hires.json");

        //Too high res, switching to low
        //rings.put("triangle-shape_ring.json", "triangle-shape_ring.json");
        //rings.put("triangle-shape_ring.json", "triangle-shape_ring_hires.json");

        //Already pushed
        //rings.put("triangular-prongs_ring.json", "triangular-prongs_ring_hires.json");

        //rings.put("trapezoid-shape_ring.json", "trapezoid-shape_ring_hires.json");

        //Only low-res available
        //rings.put("warrior_ring.json", "warrior_ring.json");

        //rings.put("flowing_ring.json", "flowing_ring_hires.json");

        //rings.put("hex-2layer_ring.json", "hex-2layer_ring_hires.json");

        //bracelets.put("scrappy_double-t_bracelet.json", "scrappy_double-t_bracelet_hires.json");

        //rings.put("warrior-one-side_ring.json", "warrior-one-side_ring.json");

        rings.put("arrow-ring-loop.json", "arrow-ring-loop.json");

        for (String key : bracelets.keySet())
        {
            Item item = new Item();
            item.setActive(true);
            item.setBaseItem(true);
            item.setCategory("BRACELET");
            item.setCustomizerFilename(key);
            item.setCustomizerPath(getCustomizerFullPath());
            item.setFinishId("98");
            item.setHighResolutionCustomizerFilename(bracelets.get(key));
            item.setItemDescription("A beautiful bracelet.");
            item.setItemName(key.replace("_bracelet.json", ""));
            item.setItemFileBaseName(key.replace(".json", ""));
            item.setItemFileName(key.replace(".json", ".obj"));

            User user = new User();
            user.setUserId(1l);
            item.setItemOwner(user);
            item.setMaterialId("1");
            item.setPrivateItem(false);
            item.setRemadeCount(0l);
            item.setRenderScene("trove_scene1");
            item.setTrovedCount(0);
            item.setSampleSupplier(Item.SAMPLE_SUPPLIER.SHAPEWAYS);
            item.setSampleMaterialId("6");

            itemAccessor.createItemWithoutDetach(item);
        }

        for (String key : rings.keySet())
        {
            Item item = new Item();
            item.setActive(true);
            item.setBaseItem(true);
            item.setCategory("RING");
            item.setCustomizerFilename(key);
            item.setCustomizerPath(getCustomizerFullPath());
            item.setFinishId("98");
            item.setHighResolutionCustomizerFilename(rings.get(key));
            item.setItemDescription("A beautiful ring.");
            item.setItemName(key);
            item.setItemFileBaseName(key.replace(".json", ""));
            item.setItemFileName(key.replace(".json", ".obj"));

            User user = new User();
            user.setUserId(1l);
            item.setItemOwner(user);
            item.setMaterialId("1");
            item.setPrivateItem(false);
            item.setRemadeCount(0l);
            item.setRenderScene("trove_scene1");
            item.setTrovedCount(0);

            itemAccessor.createItemWithoutDetach(item);
        }
    }

    @Test
    public void refreshBaseItemImagesTest() throws IOException {
        HttpClient client = httpClientFactory.getHttpClientInstance();

        client.configureForStandardRequest(getSiteUrl() + "refreshBaseItemImages/" + REFRESH_BASE_ITEM_UUID, null,
                HttpClient.REQUEST_METHOD.GET, null, null);

        UrlResponse response = client.sendRequest();

        Assert.assertTrue(response.getResponseCode() == 200);
    }

    @Test
    public void testGetItemsByCollection()
    {
        List<Item> items = itemAccessor.getItemsByCollectionId(1l, 0l, 12l, 0l, IEnums.SEEK_MODE.ITEM_FEED, false);

        Assert.assertNotNull(items);
    }

    @Test
    public void testGetTrovedItemCount()
    {
        Assert.assertTrue(itemAccessor.getTrovedItemCount(2l, true) > 0);
    }

    @Test
    public void testGetCollectionItemCount()
    {
        Assert.assertTrue(itemAccessor.getCollectionItemCount(9l, true) > 0);
    }

    @Test
    public void testSetDefaultItemCardImage()
    {
        Assert.assertTrue(itemAccessor.setDefaultItemCardImage(19l));
    }

    @Test
    public void testUpdateAllDefaultItemCardImages()
    {
        for (Long i = 15l; i < 51l; ++i)
        {
            Assert.assertTrue(itemAccessor.setDefaultItemCardImage(i));
        }
    }

    @Test
    public void reRenderAllItems()
    {
        List<Item> items = itemAccessor.getAllActiveItems(IEnums.SEEK_MODE.QUICK);

        /*for (Item item : items)
        {
            itemAccessor.removeAllImages(item.getItemId());
        }*/

        for (Item item : items) {
            Map<String, String> queueWorkMap = new HashMap<>();
            queueWorkMap.put("itemId", item.getItemId().toString());
            WorkerQueuer.queueWorkForWorker(queueWorkMap, WorkerController.REALITY_WORKER_SUBMIT_URL);
        }
    }

    @Test
    public void testUpdateGlobalPricing()
    {
        Assert.assertTrue(itemAccessor.updateAllPricing(IItemAccessor.PRICING_STRATEGY.GLOBAL, null, new BigDecimal(0.50), new BigDecimal(1.00), new BigDecimal(1.00), new BigDecimal(1.00)) > 0);
    }

    @Test
    public void testUpdateCategoryPricing()
    {
        Assert.assertTrue(itemAccessor.updateAllPricing(IItemAccessor.PRICING_STRATEGY.CATEGORY, SizeMapper.CATEGORY_NECKLACE, new BigDecimal(0.75), new BigDecimal(2.00), new BigDecimal(2.00), new BigDecimal(2.00)) > 0);
    }

    @Test
    public void testUpdateItemPricing()
    {
        Assert.assertTrue(itemAccessor.updateItemLevelPricing(33l, true, new BigDecimal(0.10), new BigDecimal(5.00), new BigDecimal(5.00), new BigDecimal(5.00)) > 0);
    }

    public Map<String, Object> persistNewTestItem()
    {
        Map<String, Object> rval = new HashMap<>();

        //Set up the item
        Item persistentItem = createTestItem();
        rval.put(ITEM_NAME_SPECIFIER, persistentItem.getItemName());

        //Add a dummy image
        ItemImage image = createTestImage();
        List<ItemImage> images = new ArrayList<>();
        images.add(image);
        persistentItem.setImages(images);

        //Add a dummy Attribute
        ItemAttribute attribute = createTestAttribute();
        List<ItemAttribute> attributeList = new ArrayList<>();
        attributeList.add(attribute);
        persistentItem.setItemAttributes(attributeList);

        //Persist the item
        itemAccessor.createItem(persistentItem);

        //Put all of this in a map for later retrieval
        rval.put(ITEM_SPECIFIER, persistentItem);
        rval.put(ATTRIBUTE_SPECIFIER, attribute);
        rval.put(ITEM_IMAGE_SPECIFIER, image);

        return rval;
    }

    public Map<String, Object> persistNewTestItem(Date date)
    {
        Map<String, Object> rval = new HashMap<>();

        //Set up the item
        Item persistentItem = createTestItem(date);
        rval.put(ITEM_NAME_SPECIFIER, persistentItem.getItemName());

        //Add a dummy image
        ItemImage image = createTestImage();
        List<ItemImage> images = new ArrayList<>();
        images.add(image);
        persistentItem.setImages(images);

        //Add a dummy Attribute
        ItemAttribute attribute = createTestAttribute();
        List<ItemAttribute> attributeList = new ArrayList<>();
        attributeList.add(attribute);
        persistentItem.setItemAttributes(attributeList);

        //Persist the item
        itemAccessor.createItem(persistentItem);

        //Put all of this in a map for later retrieval
        rval.put(ITEM_SPECIFIER, persistentItem);
        rval.put(ATTRIBUTE_SPECIFIER, attribute);
        rval.put(ITEM_IMAGE_SPECIFIER, image);

        return rval;
    }

    public static Item createTestItem()
    {
        Item item = new Item();

        item.setItemParent(null);
        item.setBaseItem(true);
        item.setBaseItemReference(null);
        item.setItemName(UUID.randomUUID().toString());
        item.setItemDescription("A test new cool item!");

        item.setItemOwner(null);
        item.setBaseItem(true);

        return item;
    }

    private Item createTestItem(Date date) {
        Item rval = createTestItem();
        rval.setCreated(date);
        return rval;
    }

    private ItemAttribute createTestAttribute()
    {
        ItemAttribute attribute = new ItemAttribute();
        attribute.setAttributeName(UUID.randomUUID().toString());
        attribute.setAttributeValue("testAttributeVal");
        attribute.setAttributeDescription("testAttributeDescription");
        attribute.setAttributeValueType(ItemAttribute.VALUE_TYPE.STRING);
        attribute.setValid(true);
        attribute.setCreated(new Date());

        return attribute;
    }

    private ItemImage createTestImage()
    {
        ItemImage image = new ItemImage();

        image.setLargeImageUrlPath("http://storage.googleapis.com/trove-qa-teststore/oval-metal-bracelet.jpg");
        image.setImageName(UUID.randomUUID().toString());
        image.setOrder(0);

        return image;
    }

    private String getBaseCustomizerPath()
    {
        return getEnvironmentProperty("cloudstore.cloudstoreurl", "https://storage.googleapis.com/");
    }

    private String getJsonBucketName()
    {
        return getEnvironmentProperty("cloudstore.jsonbucketname", "troveup-dev-private");
    }

    private String getCustomizerFullPath()
    {
        return getBaseCustomizerPath() + getJsonBucketName() + "/models/";
    }

    private String getSiteUrl()
    {
        return getEnvironmentProperty("environment.baseurl", "https://project-troveup-dev.appspot.com/");
    }

    private String getEnvironmentProperty(String propertyKey, String backup)
    {
        String propertyString;
        if (env.getProperty(propertyKey) != null)
            propertyString = env.getProperty(propertyKey);
        else
            propertyString = backup;

        return propertyString;
    }
}
