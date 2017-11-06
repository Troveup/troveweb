package com.troveup.brooklyn.orm.simpleitem.model;

import com.troveup.brooklyn.orm.cart.model.GenericItem;
import com.troveup.brooklyn.orm.cart.model.HookAttribute;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.item.model.ItemImage;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.simpleitem.interfaces.ISimpleItemAccessor;

import javax.jdo.annotations.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 3/22/16.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class SimpleItem
{
    public static String HOOK_ATTRIBUTE_CONTROL_REFERENCE = "HOOK_ATTRIBUTE_CONTROL_REFERENCE";
    public static String HOOK_ATTRIBUTE_USER_CONTROL_CHOICE_ID_OPTION_ID = "HOOK_ATTRIBUTE_USER_CONTROL_CHOICE_ID-OPTION_ID";
    public static String HOOK_ATTRIBUTE_INFLUENCER_OWNER = "HOOK_ATTRIBUTE_INFLUENCER_OWNER";

    public enum MANUFACTURING_STATUS
    {
        NEW("New"),
        ORDER_RECEIVED("Order Received by Manufacturer"),
        WITH_CASTER("With Caster"),
        PLATING_OR_POLISHING("Being Polished or Plated"),
        ENGRAVING("Being Engraved"),
        COMPLETE("Order Finalized");

        private String text;

        MANUFACTURING_STATUS(String status)
        {
            this.text = status;
        }

        @Override
        public String toString()
        {
            return text;
        }
    }

    private Long simpleItemId;
    private Long influencerUserAccountId;
    private String itemName;
    private String itemDescription;
    private String userCreatorName;
    private BigDecimal itemPrice;
    private Boolean controlReference;
    private String primaryDisplayImageUrl;
    private String hoverImageUrl;
    private List<ItemImage> troveDisplayImages;
    private Boolean active;
    private List<SimpleItemControl> simpleItemControls;
    private List<UserControlSelection> userControlSelections;
    private List<StaticAsset> staticAssets;
    private String moldSerialNumber;
    private String stlUrl;
    private List<OrderNote> orderNotes;
    private com.troveup.brooklyn.orm.order.model.Order associatedOrder;
    private MANUFACTURING_STATUS status;
    private Boolean rushOrder;
    private BigDecimal manufacturingPrice;
    private SimpleItem parentItem;
    private Date estimatedManufacturerCompletionDate;
    private String bottomDescriptionTitle;
    private String bottomDescriptionText;
    private String urlFriendlyReferenceItemName;
    private String storefrontShortUrl;

    //Not persistent, convenience fields for manufacturer dashboard
    private String color;
    private Integer quantity;
    private String size;
    private String engraving;
    private List<ManufacturingStatusOption> availableStatuses;
    private OrderNote latestNote;
    private Integer notesCount;
    private String manufacturerPriceText;
    private String lastSetManufacturerCompletionDate;

    //For the influencer's page
    private List<String> itemMaterialImages;

    //For the influencer's dashboard
    private Integer numberSold;
    private String moneyEarnedFromThisItem;

    public SimpleItem()
    {
        active = true;
    }

    public SimpleItem(GenericItem item, ISimpleItemAccessor simpleItemAccessor)
    {
        userControlSelections = new ArrayList<>();
        Long controlReferenceId = null;

        for (HookAttribute attribute : item.getHookAttributes())
        {
            if (attribute.getKey().equals(HOOK_ATTRIBUTE_CONTROL_REFERENCE))
            {
                controlReferenceId = Long.parseLong(attribute.getValue());
            }
            else if (attribute.getKey().equals(HOOK_ATTRIBUTE_INFLUENCER_OWNER))
            {
                influencerUserAccountId = Long.parseLong(attribute.getValue());
            }
            else {

                String[] parsedControlChoiceOptionIds = attribute.getValue().split("-");

                UserControlSelection selection = new UserControlSelection();

                Long controlId = Long.parseLong(parsedControlChoiceOptionIds[0]);
                Long optionId = Long.parseLong(parsedControlChoiceOptionIds[1]);

                //IDs of the actual control and option
                selection.setSimpleItemControlId(controlId);
                selection.setSimpleItemControlOptionId(optionId);

                //Labels of the control and option
                selection.setSimpleItemControlTitle(simpleItemAccessor.getControlTitleById(controlId));
                selection.setSimpleItemControlOptionLabel(simpleItemAccessor.getControlOptionLabelById(optionId));

                userControlSelections.add(selection);
            }
        }

        SimpleItem parentItem = simpleItemAccessor.getSimpleItemByItemId(controlReferenceId, IEnums.SEEK_MODE.FULL);

        this.parentItem = parentItem;
        simpleItemControls = parentItem.getSimpleItemControls();
        itemName = item.getCartDisplayName();
        itemPrice = item.getPrice();
        controlReference = false;
        primaryDisplayImageUrl = item.getBagLineItemImage();
        active = true;
        status = MANUFACTURING_STATUS.NEW;
        moldSerialNumber = parentItem.getMoldSerialNumber();
        stlUrl = parentItem.getStlUrl();
        rushOrder = false;
    }

    public SimpleItem(SimpleItem item)
    {
        this.influencerUserAccountId = item.getInfluencerUserAccountId();
        this.itemName = item.getItemName();
        this.itemDescription = item.getItemDescription();
        this.userCreatorName = item.getUserCreatorName();
        this.itemPrice = item.getItemPrice();
        this.controlReference = item.getControlReference();
        this.primaryDisplayImageUrl = item.getPrimaryDisplayImageUrl();
        this.hoverImageUrl = item.getHoverImageUrl();
        this.storefrontShortUrl = item.getStorefrontShortUrl();

        if (item.getTroveDisplayImages() != null) {
            troveDisplayImages = new ArrayList<>();
            for (ItemImage image : item.getTroveDisplayImages()) {
                troveDisplayImages.add(new ItemImage(image));
            }
        }

        this.active = item.getActive();

        if (item.getSimpleItemControls() != null) {
            simpleItemControls = new ArrayList<>();
            for (SimpleItemControl control : item.getSimpleItemControls()) {
                simpleItemControls.add(new SimpleItemControl(control));
            }
        }

        if (item.getUserControlSelections() != null) {
            this.userControlSelections = new ArrayList<>();
            for (UserControlSelection selection : this.getUserControlSelections())
            {
                userControlSelections.add(new UserControlSelection(selection));
            }
        }

        if (item.getStaticAssets() != null) {
            this.staticAssets = new ArrayList<>();
            for (StaticAsset asset : item.getStaticAssets()) {
                staticAssets.add(new StaticAsset(asset));
            }
        }

        this.moldSerialNumber = item.getMoldSerialNumber();
        this.stlUrl = item.getStlUrl();

        //Disregarding contextual things like orderNotes, associatedOrder, and status

        this.manufacturingPrice = item.getManufacturingPrice();
        this.parentItem = item.getParentItem();
        this.bottomDescriptionTitle = item.getBottomDescriptionTitle();
        this.bottomDescriptionText = item.getBottomDescriptionText();
        this.urlFriendlyReferenceItemName = item.getUrlFriendlyReferenceItemName();
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getSimpleItemId() {
        return simpleItemId;
    }

    public void setSimpleItemId(Long simpleItemId) {
        this.simpleItemId = simpleItemId;
    }

    @Persistent
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Persistent
    @Column(scale = 2)
    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    @Persistent
    public Boolean getControlReference() {
        return controlReference;
    }

    public void setControlReference(Boolean controlReference) {
        this.controlReference = controlReference;
    }

    @Persistent
    public String getPrimaryDisplayImageUrl() {
        return primaryDisplayImageUrl;
    }

    public void setPrimaryDisplayImageUrl(String primaryDisplayImageUrl) {
        this.primaryDisplayImageUrl = primaryDisplayImageUrl;
    }

    @Persistent
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Persistent
    public List<SimpleItemControl> getSimpleItemControls() {
        return simpleItemControls;
    }

    public void setSimpleItemControls(List<SimpleItemControl> simpleItemControls) {
        this.simpleItemControls = simpleItemControls;
    }

    @Persistent
    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    @Persistent
    public String getUserCreatorName() {
        return userCreatorName;
    }

    public void setUserCreatorName(String userCreatorName) {
        this.userCreatorName = userCreatorName;
    }

    @Persistent
    public Long getInfluencerUserAccountId() {
        return influencerUserAccountId;
    }

    public void setInfluencerUserAccountId(Long influencerUserAccountId) {
        this.influencerUserAccountId = influencerUserAccountId;
    }

    @Persistent
    public List<UserControlSelection> getUserControlSelections() {
        return userControlSelections;
    }

    public void setUserControlSelections(List<UserControlSelection> userControlSelections) {
        this.userControlSelections = userControlSelections;
    }

    @Persistent
    public List<StaticAsset> getStaticAssets() {
        return staticAssets;
    }

    public void setStaticAssets(List<StaticAsset> staticAssets) {
        this.staticAssets = staticAssets;
    }

    @NotPersistent
    public Integer getNumberSold() {
        return numberSold;
    }

    public void setNumberSold(Integer numberSold) {
        this.numberSold = numberSold;
    }

    @Persistent
    public String getMoldSerialNumber() {
        return moldSerialNumber;
    }

    public void setMoldSerialNumber(String moldSerialNumber) {
        this.moldSerialNumber = moldSerialNumber;
    }

    @Persistent
    public String getStlUrl() {
        return stlUrl;
    }

    public void setStlUrl(String stlUrl) {
        this.stlUrl = stlUrl;
    }

    @Persistent
    public List<OrderNote> getOrderNotes() {
        return orderNotes;
    }

    public void setOrderNotes(List<OrderNote> orderNotes) {
        this.orderNotes = orderNotes;
    }

    @Persistent
    public com.troveup.brooklyn.orm.order.model.Order getAssociatedOrder() {
        return associatedOrder;
    }

    public void setAssociatedOrder(Order associatedOrder) {
        this.associatedOrder = associatedOrder;
    }

    @Persistent
    public MANUFACTURING_STATUS getStatus() {
        return status;
    }

    public void setStatus(MANUFACTURING_STATUS status) {
        this.status = status;
    }

    @NotPersistent
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Persistent
    public Boolean getRushOrder() {
        return rushOrder;
    }

    public void setRushOrder(Boolean rushOrder) {
        this.rushOrder = rushOrder;
    }

    @NotPersistent
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @NotPersistent
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @NotPersistent
    public String getEngraving() {
        return engraving;
    }

    public void setEngraving(String engraving) {
        this.engraving = engraving;
    }

    @NotPersistent
    public List<ManufacturingStatusOption> getAvailableStatuses() {
        return availableStatuses;
    }

    public void setAvailableStatuses(List<ManufacturingStatusOption> availableStatuses) {
        this.availableStatuses = availableStatuses;
    }

    @Persistent
    @Column(scale = 2)
    public BigDecimal getManufacturingPrice() {
        return manufacturingPrice;
    }

    public void setManufacturingPrice(BigDecimal manufacturingPrice) {
        this.manufacturingPrice = manufacturingPrice;
    }

    @NotPersistent
    public OrderNote getLatestNote() {
        return latestNote;
    }

    public void setLatestNote(OrderNote latestNote) {
        this.latestNote = latestNote;
    }

    @NotPersistent
    public Integer getNotesCount() {
        return notesCount;
    }

    public void setNotesCount(Integer notesCount) {
        this.notesCount = notesCount;
    }

    @NotPersistent
    public String getManufacturerPriceText() {
        return manufacturerPriceText;
    }

    public void setManufacturerPriceText(String manufacturerPriceText) {
        this.manufacturerPriceText = manufacturerPriceText;
    }

    @NotPersistent
    public List<String> getItemMaterialImages() {
        return itemMaterialImages;
    }

    public void setItemMaterialImages(List<String> itemMaterialImages) {
        this.itemMaterialImages = itemMaterialImages;
    }

    @Join(table = "SIMPLEITEM_DISPLAYIMAGES")
    @Persistent
    public List<ItemImage> getTroveDisplayImages() {
        return troveDisplayImages;
    }

    public void setTroveDisplayImages(List<ItemImage> troveDisplayImages) {
        this.troveDisplayImages = troveDisplayImages;
    }

    @NotPersistent
    public String getMoneyEarnedFromThisItem() {
        return moneyEarnedFromThisItem;
    }

    public void setMoneyEarnedFromThisItem(String moneyEarnedFromThisItem) {
        this.moneyEarnedFromThisItem = moneyEarnedFromThisItem;
    }

    @Persistent
    public SimpleItem getParentItem() {
        return parentItem;
    }

    public void setParentItem(SimpleItem parentItem) {
        this.parentItem = parentItem;
    }

    @Persistent
    public Date getEstimatedManufacturerCompletionDate() {
        return estimatedManufacturerCompletionDate;
    }

    public void setEstimatedManufacturerCompletionDate(Date estimatedManufacturerCompletionDate) {
        this.estimatedManufacturerCompletionDate = estimatedManufacturerCompletionDate;
    }

    @NotPersistent
    public String getLastSetManufacturerCompletionDate() {
        return lastSetManufacturerCompletionDate;
    }

    public void setLastSetManufacturerCompletionDate(String lastSetManufacturerCompletionDate) {
        this.lastSetManufacturerCompletionDate = lastSetManufacturerCompletionDate;
    }

    @Persistent
    @Column(length = 2000)
    public String getBottomDescriptionTitle() {
        return bottomDescriptionTitle;
    }

    public void setBottomDescriptionTitle(String bottomDescriptionTitle) {
        this.bottomDescriptionTitle = bottomDescriptionTitle;
    }

    @Persistent
    @Column(length = 2000)
    public String getBottomDescriptionText() {
        return bottomDescriptionText;
    }

    public void setBottomDescriptionText(String bottomDescriptionText) {
        this.bottomDescriptionText = bottomDescriptionText;
    }

    @Persistent
    public String getUrlFriendlyReferenceItemName() {
        return urlFriendlyReferenceItemName;
    }

    public void setUrlFriendlyReferenceItemName(String urlFriendlyReferenceItemName) {
        this.urlFriendlyReferenceItemName = urlFriendlyReferenceItemName;
    }

    @Persistent
    public String getHoverImageUrl() {
        return hoverImageUrl;
    }

    public void setHoverImageUrl(String hoverImageUrl) {
        this.hoverImageUrl = hoverImageUrl;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Long && this.getSimpleItemId().equals((Long)obj)) || (obj instanceof SimpleItem && this.getSimpleItemId().equals(((SimpleItem) obj).getSimpleItemId()));
    }

    @Persistent
    public String getStorefrontShortUrl() {
        return storefrontShortUrl;
    }

    public void setStorefrontShortUrl(String storefrontShortUrl) {
        this.storefrontShortUrl = storefrontShortUrl;
    }

    public static List<String> getAllSimpleItemFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("userControlSelections");
        rval.add("simpleItemControls");
        rval.add("staticAssets");
        rval.add("troveDisplayImages");
        rval.add("parentItem");
        rval.add("associatedOrder");
        rval.add("orderNotes");

        return rval;
    }

    public static List<String> getSimpleItemWithOrderAndParentItemFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("associatedOrder");
        rval.add("parentItem");

        return rval;
    }

    public static List<String> getSimpleItemAssetFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("staticAssets");
        rval.add("troveDisplayImages");

        return rval;
    }

    public static String retrieveColorValueFromSelectedOptions(SimpleItem item)
    {
        SimpleItemControl controlRepresentingColor = retrieveControl(item.getParentItem().getSimpleItemControls(), ManufacturerSpecifier.SPECIFIER_TYPE.MATERIAL);

        return retrieveControlOptionValue(controlRepresentingColor, item.getUserControlSelections());
    }

    public static List<String> retrieveColorImages(SimpleItem item)
    {
        List<String> rval = new ArrayList<>();

        SimpleItemControl control = retrieveControl(item.getSimpleItemControls(), ManufacturerSpecifier.SPECIFIER_TYPE.MATERIAL);

        for (ControlOption option : control.getControlOptions())
        {
            rval.add(option.getControlOptionAssets().get(0).getControlOptionAssetUrl());
        }

        return rval;
    }

    public static String retrieveSizeValueFromSelectedOptions(SimpleItem item)
    {
        SimpleItemControl controlRepresentingSize = retrieveControl(item.getParentItem().getSimpleItemControls(), ManufacturerSpecifier.SPECIFIER_TYPE.SIZE);

        return retrieveControlOptionValue(controlRepresentingSize, item.getUserControlSelections());
    }

    public static String retrieveEngravingValueFromSelectedOptions(SimpleItem item)
    {
        SimpleItemControl controlRepresentingEngraving = retrieveControl(item.getParentItem().getSimpleItemControls(), ManufacturerSpecifier.SPECIFIER_TYPE.ENGRAVING);

        return retrieveControlOptionValue(controlRepresentingEngraving, item.getUserControlSelections());
    }

    private static SimpleItemControl retrieveControl(List<SimpleItemControl> controls, ManufacturerSpecifier.SPECIFIER_TYPE controlType)
    {
        SimpleItemControl rval = null;

        for (SimpleItemControl control : controls)
        {
            if (control.getManufacturerSpecifier() != null &&
                    control.getManufacturerSpecifier().getType() == controlType)
            {
                rval = control;
            }
        }

        return rval;
    }

    private static String retrieveControlOptionValue(SimpleItemControl control, List<UserControlSelection> userSelections)
    {
        String rval = null;

        if (userSelections != null && control != null && control.getControlOptions() != null)
        {
            for (UserControlSelection selection : userSelections)
            {
                //Given a control, find the user-selection that matches
                if (selection.getSimpleItemControlId().equals(control.getControlId()))
                {
                    //Now find the user selection that matches the option
                    for (ControlOption option : control.getControlOptions())
                    {
                        if (selection.getSimpleItemControlOptionId().equals(option.getControlOptionId()))
                        {
                            rval = option.getOptionDisplayName();
                        }
                    }
                }
            }
        }

        return rval;
    }

    public static List<ManufacturingStatusOption> getListOfManufacturingStatuses(MANUFACTURING_STATUS selectedStatus)
    {
        List<ManufacturingStatusOption> rval = new ArrayList<>();

        for (MANUFACTURING_STATUS status : MANUFACTURING_STATUS.values())
        {
            ManufacturingStatusOption option = new ManufacturingStatusOption(status, status.name(), status.equals(selectedStatus));
            rval.add(option);
        }

        return rval;
    }

    public static MANUFACTURING_STATUS getEnumStatusObjectFromString(String status)
    {
        MANUFACTURING_STATUS rval = null;

        for (MANUFACTURING_STATUS potentialStatus : MANUFACTURING_STATUS.values())
        {
            if (potentialStatus.name().equals(status))
                rval = potentialStatus;
        }

        return rval;
    }
}
