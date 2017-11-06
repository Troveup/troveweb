package com.troveup.brooklyn.orm.item.model;

import com.troveup.brooklyn.controllers.trove.AjaxController;
import com.troveup.brooklyn.orm.urlshortener.model.ShortLink;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.meshexporter.forge.model.CustomizerOperator;
import com.troveup.brooklyn.util.SizeMapper;

import javax.jdo.annotations.*;


import java.math.BigDecimal;
import java.util.*;

/**
 * Created by tim on 4/1/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class Item implements Comparable<Item>, Comparator<Item> {

    public enum SEARCH_BY_TYPE {
        IDENTITY_COLUMN_NAME("itemId"),
        ITEM_NAME("itemName"),
        ITEM_DESCRIPTION("itemDescription");

        private final String text;

        SEARCH_BY_TYPE(String type) {
            this.text = type;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum SAMPLE_SUPPLIER
    {
        VOODOO,
        SHAPEWAYS
    }

    private Long itemId;

    private String uniqueItemIdentifier;

    //Volume returned by the customizer
    private Float customizedVolume;

    //Volume of the object at a medium size (7.5 for rings, medium for bangles)
    private Float mediumVolume;
    private Item itemParent;
    private Item baseItemReference;
    private String itemName;
    private String itemDescription;
    private String customizerFilename;
    private String customizerPath;
    private String highResolutionCustomizerFilename;
    private String livePhotoUrl;
    private List<ItemAttribute> itemAttributes;
    private List<ItemImage> images;
    private User itemOwner;
    private Date created;
    private boolean baseItem;
    private boolean active;
    private boolean privateItem;
    private BigDecimal estimatedPrice;
    private String category;
    private String renderScene;
    private Long remadeCount;
    private long trovedCount;
    Map<String, String> availableSizes;
    private com.troveup.brooklyn.orm.user.model.Collection defaultCollection;
    private Integer sortOrder;
    private String defaultCardImageUrl;
    private Boolean isEngravable;
    private List<ItemCustomization> itemCustomizations;
    private Boolean isMobileCompatible;

    //Boolean to do the simple indication to Handlebars on the front-end that
    //this is a necklace, and that chain dropdowns should be included
    private Boolean shouldIncludeChainDropdown;

    //Dictates whether or not a size dropdown should be available for the user to pick a size on this item
    private Boolean sizeDropdownAvailable;

    private Boolean featured;

    //Name of the base model file without the unique item identifier or extension added to it.  Concatenating it all
    //should be of the format [itemFileBaseName]-[uniqueItemIdentifier].[extension]
    private String itemFileBaseName;
    private String materialId;
    private String finishId;
    private String itemFileName;
    private SAMPLE_SUPPLIER sampleSupplier;
    private String sampleMaterialId;

    //Used in the markup calculation
    private BigDecimal packaging;
    private BigDecimal shippingTotal;
    private BigDecimal prototypeTotal;
    private BigDecimal percentageMarkup;

    private Boolean originFtue;

    //Displayed on the item cards
    private BigDecimal startingPrice;

    private ShortLink shortLink;

    public Item() {
        this.active = true;
        this.created = new Date();
        this.baseItem = false;
        this.originFtue = false;
    }

    public Item(List<ItemAttribute> itemAttributes, Item itemParent, String itemName, String itemDescription,
                Date created, boolean active) {

        this.itemAttributes = itemAttributes;
        this.itemParent = itemParent;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.created = created;
        this.active = active;
        this.originFtue = false;

    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    @Persistent
    public Item getItemParent() {
        return itemParent;
    }

    public void setItemParent(Item itemParent) {
        this.itemParent = itemParent;
    }

    @Persistent
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Persistent
    @Column(length = 2000)
    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    @Persistent
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Persistent
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean isActive) {
        this.active = isActive;
    }

    @Persistent(mappedBy = "attributeOwner")
    public List<ItemAttribute> getItemAttributes() {
        return itemAttributes;
    }

    public void setItemAttributes(List<ItemAttribute> itemAttributes) {
        this.itemAttributes = itemAttributes;
    }

    public User getItemOwner() {
        return itemOwner;
    }

    public void setItemOwner(User itemOwner) {
        this.itemOwner = itemOwner;
    }

    @Persistent
    public boolean isBaseItem()
    {
        return baseItem;
    }

    public void setBaseItem(boolean baseItem) {
        this.baseItem = baseItem;
    }

    @Persistent
    public Item getBaseItemReference() {
        return baseItemReference;
    }

    public void setBaseItemReference(Item baseItemReference) {
        this.baseItemReference = baseItemReference;
    }

    @Persistent(mappedBy = "ownerItem")
    public List<ItemImage> getImages() {
        return images;
    }

    public void setImages(List<ItemImage> images) {
        this.images = images;
    }

    public ItemImage getImage(int imageOrder)
    {
        Collections.sort(this.images);
        return this.images.get(imageOrder);
    }

    @Persistent
    public String getItemFileBaseName() {
        return itemFileBaseName;
    }

    public void setItemFileBaseName(String itemFileBaseName) {
        this.itemFileBaseName = itemFileBaseName;
    }

    @Persistent
    public String getUniqueItemIdentifier() {
        return uniqueItemIdentifier;
    }

    public void setUniqueItemIdentifier(String uniqueItemIdentifier) {
        this.uniqueItemIdentifier = uniqueItemIdentifier;
    }

    @Persistent
    public boolean isPrivateItem() {
        return privateItem;
    }

    public void setPrivateItem(boolean privateItem) {
        this.privateItem = privateItem;
    }

    @Persistent
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    @Persistent
    public String getItemFileName() {
        return itemFileName;
    }

    public void setItemFileName(String itemFileName) {
        this.itemFileName = itemFileName;
    }

    @Persistent
    public String getCustomizerFilename() {
        return customizerFilename;
    }

    public void setCustomizerFilename(String customizerFilename) {
        this.customizerFilename = customizerFilename;
    }

    @Persistent
    public String getCustomizerPath() {
        return customizerPath;
    }

    public void setCustomizerPath(String customizerPath) {
        this.customizerPath = customizerPath;
    }

    @Persistent
    @Column(scale = 4)
    public BigDecimal getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(BigDecimal estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    @Persistent
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Persistent
    public String getFinishId() {
        return finishId;
    }

    public void setFinishId(String finishId) {
        this.finishId = finishId;
    }

    @Persistent
    public String getRenderScene() {
        return renderScene;
    }

    public void setRenderScene(String renderScene) {
        this.renderScene = renderScene;
    }

    @Persistent
    public Long getRemadeCount() {
        return remadeCount;
    }

    public void setRemadeCount(Long remadeCount) {
        this.remadeCount = remadeCount;
    }

    @Persistent
    public long getTrovedCount() {
        return trovedCount;
    }

    public void setTrovedCount(long trovedCount) {
        this.trovedCount = trovedCount;
    }

    @Persistent
    public String getHighResolutionCustomizerFilename() {
        return highResolutionCustomizerFilename;
    }

    public void setHighResolutionCustomizerFilename(String highResolutionCustomizerFilename) {
        this.highResolutionCustomizerFilename = highResolutionCustomizerFilename;
    }

    @Persistent
    public SAMPLE_SUPPLIER getSampleSupplier() {
        return sampleSupplier;
    }

    public void setSampleSupplier(SAMPLE_SUPPLIER sampleSupplier) {
        this.sampleSupplier = sampleSupplier;
    }

    @Persistent
    public String getSampleMaterialId() {
        return sampleMaterialId;
    }

    public void setSampleMaterialId(String sampleMaterialId) {
        this.sampleMaterialId = sampleMaterialId;
    }

    @Persistent
    public com.troveup.brooklyn.orm.user.model.Collection getDefaultCollection() {
        return defaultCollection;
    }

    public void setDefaultCollection(com.troveup.brooklyn.orm.user.model.Collection defaultCollection) {
        this.defaultCollection = defaultCollection;
    }

    @Persistent
    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    @Persistent
    public String getDefaultCardImageUrl() {
        return defaultCardImageUrl;
    }

    public void setDefaultCardImageUrl(String defaultCardImageUrl) {
        this.defaultCardImageUrl = defaultCardImageUrl;
    }

    @Persistent
    public List<ItemCustomization> getItemCustomizations() {
        return itemCustomizations;
    }

    public void setItemCustomizations(List<ItemCustomization> itemCustomizations) {
        this.itemCustomizations = itemCustomizations;
    }

    @Persistent
    public Float getCustomizedVolume() {
        return customizedVolume;
    }

    public void setCustomizedVolume(Float customizedVolume) {
        this.customizedVolume = customizedVolume;
    }

    @Persistent
    public Float getMediumVolume() {
        return mediumVolume;
    }

    public void setMediumVolume(Float mediumVolume) {
        this.mediumVolume = mediumVolume;
    }

    @Persistent
    @Column(defaultValue = "0")
    public Boolean getIsEngravable() {
        return isEngravable;
    }

    public void setIsEngravable(Boolean isEngravable) {
        this.isEngravable = isEngravable;
    }

    @Persistent
    @Column(defaultValue = "0")
    public Boolean getIsMobileCompatible() {
        return isMobileCompatible;
    }

    public void setIsMobileCompatible(Boolean isMobileCompatible) {
        this.isMobileCompatible = isMobileCompatible;
    }

    @Persistent(defaultFetchGroup = "false")
    @Column(scale = 2)
    public BigDecimal getPackaging() {
        return packaging;
    }

    public void setPackaging(BigDecimal packaging) {
        this.packaging = packaging;
    }

    @Persistent(defaultFetchGroup = "false")
    @Column(scale = 2)
    public BigDecimal getShippingTotal() {
        return shippingTotal;
    }

    public void setShippingTotal(BigDecimal shippingTotal) {
        this.shippingTotal = shippingTotal;
    }

    @Persistent(defaultFetchGroup = "false")
    @Column(scale = 2)
    public BigDecimal getPrototypeTotal() {
        return prototypeTotal;
    }

    public void setPrototypeTotal(BigDecimal prototypeTotal) {
        this.prototypeTotal = prototypeTotal;
    }

    @Persistent(defaultFetchGroup = "false")
    @Column(scale = 2)
    public BigDecimal getPercentageMarkup() {
        return percentageMarkup;
    }

    public void setPercentageMarkup(BigDecimal percentageMarkup) {
        this.percentageMarkup = percentageMarkup;
    }

    @Persistent
    public String getLivePhotoUrl()
    {
        return livePhotoUrl;
    }

    public void setLivePhotoUrl(String livePhotoUrl) {
        this.livePhotoUrl = livePhotoUrl;
    }

    @NotPersistent
    public Boolean getSizeDropdownAvailable() {
        return sizeDropdownAvailable;
    }

    public void setSizeDropdownAvailable(Boolean sizeDropdownAvailable) {
        this.sizeDropdownAvailable = sizeDropdownAvailable;
    }

    public List<ItemCustomization> getItemCustomizationByIteration(Integer iteration)
    {
        List<ItemCustomization> rval = new ArrayList<>();

        if (getItemCustomizations() != null)
        {
            for (ItemCustomization customization : getItemCustomizations())
            {
                if (customization.getSetNumber().equals(iteration))
                    rval.add(customization);
            }
        }

        return rval;
    }

    @NotPersistent
    public Map<String, String> getAvailableSizes() {
        return availableSizes;
    }

    public void setAvailableSizes(Map<String, String> availableSizes) {
        this.availableSizes = availableSizes;
    }

    @NotPersistent
    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @NotPersistent
    public Boolean getShouldIncludeChainDropdown() {
        return shouldIncludeChainDropdown;
    }

    public void setShouldIncludeChainDropdown(Boolean shouldIncludeChainDropdown) {
        this.shouldIncludeChainDropdown = shouldIncludeChainDropdown;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Item)
            return ((Item) obj).getItemId().equals(this.itemId);
        else
            return false;
    }

    @Override
    public int compareTo(Item o)
    {
        return Long.compare(o.getItemId(), this.itemId);
    }

    @Override
    public int compare(Item o1, Item o2)
    {
        return Long.compare(o1.getItemId(), o2.getItemId());
    }

    @Persistent
    @Column(scale = 2)
    public BigDecimal getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(BigDecimal startingPrice) {
        this.startingPrice = startingPrice;
    }

    @Persistent
    public Boolean getOriginFtue() {
        return originFtue;
    }

    public void setOriginFtue(Boolean originFtue) {
        this.originFtue = originFtue;
    }

    @Persistent
    public ShortLink getShortLink() {
        return shortLink;
    }

    public void setShortLink(ShortLink shortLink) {
        this.shortLink = shortLink;
    }

    public void populateItemCustomizationsFromAttributes()
    {
        List<ItemCustomization> operators = new ArrayList<>();

        if (getItemAttributes() != null) {
            for (ItemAttribute attribute : getItemAttributes()) {
                if (attribute.getAttributeName().contains("modelWeight-")) {
                    operators.add(new ItemCustomization(attribute.getAttributeName().split("-")[1], attribute.getAttributeValue()));
                }
            }
        }

        setItemCustomizations(operators);
    }

    public List<CustomizerOperator> getOperatorsFromCustomizations()
    {
        List<CustomizerOperator> rval = new ArrayList<>();
        if (getItemCustomizations() != null && getItemCustomizations().size() > 0) {
            for (ItemCustomization customization : getItemCustomizations()) {
                if (customization.getId() != null)
                    rval.add(new CustomizerOperator(customization.getId(), customization.getValue()));
            }
        }
        return rval;
    }

    public List<String> getVisibleMeshes()
    {
        List<String> rval = new ArrayList<>();
        if (getItemCustomizations() != null && getItemCustomizations().size() > 0)
        {
            for (ItemCustomization customization : getItemCustomizations())
            {
                if (customization.getVisibleMesh() != null)
                    rval.add(customization.getVisibleMesh());
            }
        }

        return rval;
    }

    public static List<String> getFullItemFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("itemParent");
        rval.add("baseItemReference");
        rval.add("itemAttributes");
        rval.add("images");
        rval.add("itemOwner");
        rval.add("created");
        rval.add("packaging");
        rval.add("shippingTotal");
        rval.add("prototypeTotal");
        rval.add("percentageMarkup");

        return rval;
    }

    public static List<String> getFeedItemFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("images");
        rval.add("itemOwner");
        rval.add("created");
        rval.add("defaultCollection");


        return rval;
    }

    public static List<String> getItemWithAttributesFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("itemAttributes");
        rval.add("itemCustomizations");

        return rval;
    }

    public static List<String> getItemAttributesAndImagesFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("itemAttributes");
        rval.add("itemCustomizations");
        rval.add("images");
        rval.add("shortLink");

        return rval;
    }

    public static List<String> getItemWithCustomizerFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("itemCustomizations");

        return rval;
    }

    public static List<String> getItemWithCustomizerAndMarkupFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();
        rval.addAll(getItemWithCustomizerFetchGroupFields());

        rval.add("packaging");
        rval.add("shippingTotal");
        rval.add("prototypeTotal");
        rval.add("percentageMarkup");

        return rval;

    }

    public static List<String> getItemImageFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("images");

        return rval;
    }

    public static List<String> getItemDesignStoryFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("images");
        rval.add("itemOwner");

        return rval;
    }

    public static List<String> getItemWithPrototypeSettings()
    {
        List<String> rval = new ArrayList<>();

        rval.add("sampleSupplier");
        rval.add("sampleMaterialId");

        return rval;
    }

    /**
     * Gets all item categories that should have a size dropdown associated with them in any of the purchase
     * views.
     *
     * @return A List of strings representing the categories to have included size dropdowns.
     */
    public static List<String> getCategoriesWithSizeDropdowns()
    {
        List<String> rval = new ArrayList<>();

        rval.add(SizeMapper.CATEGORY_BRACELET.toUpperCase());
        rval.add(SizeMapper.CATEGORY_RING.toUpperCase());

        return rval;
    }

    /**
     * Method for self initialization of Item objects based on their category.
     */
    public void performCategoryConfigurations()
    {
        if (category.toUpperCase().equals(SizeMapper.CATEGORY_NECKLACE.toUpperCase()))
            setShouldIncludeChainDropdown(true);
        else
            setShouldIncludeChainDropdown(false);

        if (getCategoriesWithSizeDropdowns().contains(category.toUpperCase()))
            setSizeDropdownAvailable(true);
        else
            setSizeDropdownAvailable(false);

    }

}
