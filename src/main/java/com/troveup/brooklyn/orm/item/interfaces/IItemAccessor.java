package com.troveup.brooklyn.orm.item.interfaces;

import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.item.model.*;

import javax.jdo.PersistenceManager;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 4/23/15.
 */
public interface IItemAccessor
{
    enum PRICING_STRATEGY
    {
        GLOBAL,
        CATEGORY
    }

    String EMPTY_CACHE = "EMPTY";

    Boolean createItem(Item item);
    Boolean createItemWithoutDetach(Item item);
    List<Item> getItems(List<String> itemIdentifier, Item.SEARCH_BY_TYPE identifierType, IEnums.SEEK_MODE mode);
    List<Item> getItems(List<String> itemIdentifier, Item.SEARCH_BY_TYPE identifierType, PersistenceManager pm);
    List<Item> getItemsAttachedByBaseItemId(Long baseItemId, PersistenceManager pm);
    List<Item> getItems(String itemIdentifier, Item.SEARCH_BY_TYPE identifierType, IEnums.SEEK_MODE mode);
    List<Item> getItems(Date from, Date to, IEnums.SEEK_MODE mode);
    Item getItemAttached(Long item, PersistenceManager pm);
    Item getItemDetached(Long item, IEnums.SEEK_MODE mode);
    List<Item> getAllActiveItems(IEnums.SEEK_MODE mode);
    List<Item> getAllActiveItemsByCategory(String category, IEnums.SEEK_MODE mode);
    List<Item> getItemsByCollectionId(Long collectionId, Long collectionPage, Long collectionPageSize,
                                      Long extraResults, IEnums.SEEK_MODE mode, Boolean includePrivate);
    Boolean updateItem(Item item, IEnums.UPDATE_MODE mode);
    Boolean deactivateItem(Long itemId);
    Boolean activateItem(Long itemId);
    Boolean addItemAttributes(Long itemId, List<ItemAttribute> attributes);
    Boolean addImage(Long itemId, ItemImage image);
    Boolean addImage(Long itemId, List<ItemImage> images);
    Boolean addImage(Long itemId, ItemImage image, int position);
    Boolean removeImage(Long itemId, String imageLocation);
    Boolean removeImage(Long itemId, Long imageId);
    Boolean removeImage(Long itemId, int imagePosition);
    Boolean removeAllImages(Long itemId);
    Boolean removeCustomizerImages(Long itemId);
    Boolean deactivateAttributes(Long itemId, List<Long> attributeId);
    Boolean activateAttributes(Long itemId, List<Long> attributeId);
    Boolean updateItemAttribute(Long itemId, ItemAttribute attribute);
    Boolean updateItemAttributes(Long itemId, List<ItemAttribute> attributes);
    Boolean updateItemCounters(Long itemId);
    Boolean updateItemName(Long itemId, Long userId, String name);
    Boolean updateItemDescription(Long itemId, Long userId, String description);
    Boolean setItemDefaultCollection(Long itemId, Long collectionId, Long userId);
    Boolean clearItemDefaultCollection(Long itemId, Long userId);
    List<Item> getBaseItemsByCategory(String category, Integer collectionPage, Long collectionPageSize, Long extraResults, IEnums.SEEK_MODE mode);
    List<Item> getFeaturedItems(Integer collectionPage, Long collectionPageSize, Long extraResults, IEnums.SEEK_MODE mode);
    List<Item> getPagedTroveItems(Long userId, Integer collectionPage, Long collectionPageSize,
                                  Long extraResults, IEnums.SEEK_MODE mode, Boolean includePrivate);
    List<Item> getBaseItems();
    Long getTrovedItemCount(Long userId, Boolean includePrivate);
    Long getCollectionItemCount(Long collectionId, Boolean includePrivate);
    List<Item> getPagedFeedItems(Long userId, Integer collectionPage, Long collectionPageSize, Long extraResults,
                                 IEnums.SEEK_MODE mode);
    List<Item> getSisterItems(Long itemId, IEnums.SEEK_MODE mode, Long limit);

    Boolean setDefaultItemCardImage(Long itemId);

    List<Item> getRenderedItemsWithNoDefaultImage(Long limit, IEnums.SEEK_MODE mode);
    Item getItemBaseItem(Long itemId, IEnums.SEEK_MODE mode);
    Item getItemParentItem(Long itemId, IEnums.SEEK_MODE mode);
    Item getItemGrandParentItem(Long itemId, IEnums.SEEK_MODE mode);
    Boolean updateItemMediumVolume(Long itemId, Float volume);
    Boolean updateItemBaseItemReference(Long itemId, Long baseItemId);
    Boolean updateItemParentItemReference(Long itemId, Long parentItemId);

    Boolean addChain(Chain chain);
    List<Chain> getActiveChains();
    Chain getChain(Long chainId);
    Chain getChainAttached(Long chainId, PersistenceManager pm);

    Integer updateAllPricing(PRICING_STRATEGY strategy, String category, BigDecimal newMarkup, BigDecimal newPackaging, BigDecimal newShipping, BigDecimal newPrototyping);
    Integer updateItemLevelPricing(Long itemId, Boolean applyToRelatives, BigDecimal newMarkup, BigDecimal newPackaging, BigDecimal newShipping, BigDecimal newPrototyping);

    /**
     * Convenience method for getting the latest price filter at the global level.
     *
     * @param materialLevel enum material level of the filter.  Is currently either ALL or MATERIAL.
     * @param materialId Supplier material ID of the material that should be filtered.  Required if materialLevel is set to MATERIAL.
     * @param finishId Supplier finish ID of the finish that should be filtered.  Required if materialLevel is set to MATERIAL.
     * @param mode Seek mode of the PriceFilter.  Primarily applicable if there is a reference item that should be pulled.
     * @return The applicable PriceFilter object.
     */
    PriceFilter getLatestGlobalPriceFilter(PriceFilter.MATERIAL_LEVEL materialLevel, String materialId, String finishId, IEnums.SEEK_MODE mode);

    /**
     * Convenience method for getting the latest price filter at the category level.
     *
     * @param materialLevel enum material level of the filter.  Is currently either ALL or MATERIAL.
     * @param category Item category that this filter applies to.  Required if applicationLevel is CATEGORY.
     * @param materialId Supplier material ID of the material that should be filtered.  Required if materialLevel is set to MATERIAL.
     * @param finishId Supplier finish ID of the finish that should be filtered.  Required if materialLevel is set to MATERIAL.
     * @param mode Seek mode of the PriceFilter.  Primarily applicable if there is a reference item that should be pulled.
     * @return The applicable PriceFilter object.
     */
    PriceFilter getLatestCategoryPriceFilter(PriceFilter.MATERIAL_LEVEL materialLevel, String category, String materialId, String finishId, IEnums.SEEK_MODE mode);

    /**
     * Convenience method for getting the latest price filter at the item level.
     *
     * @param materialLevel enum material level of the filter.  Is currently either ALL or MATERIAL.
     * @param materialId Supplier material ID of the material that should be filtered.  Required if materialLevel is set to MATERIAL.
     * @param finishId Supplier finish ID of the finish that should be filtered.  Required if materialLevel is set to MATERIAL.
     * @param itemId Primary key identifier of an item that has had the filter specifically applied to it.  Required if
     *               applicationLevel is ITEM.
     * @param mode Seek mode of the PriceFilter.  Primarily applicable if there is a reference item that should be pulled.
     * @return The applicable PriceFilter object.
     */
    PriceFilter getLatestItemPriceFilter(PriceFilter.MATERIAL_LEVEL materialLevel, String materialId, String finishId, Long itemId, IEnums.SEEK_MODE mode);

    /**
     * Retrieves the latest price filter based on a set of category constraints.
     *
     * @param applicationLevel enum application level of the filter.  Currently supports GLOBAL, CATEGORY, or ITEM.
     * @param materialLevel enum material level of the filter.  Is currently either ALL or MATERIAL.
     * @param materialId Supplier material ID of the material that should be filtered.  Required if materialLevel is set to MATERIAL.
     * @param finishId Supplier finish ID of the finish that should be filtered.  Required if materialLevel is set to MATERIAL.
     * @param category Item category that this filter applies to.  Required if applicationLevel is CATEGORY.
     * @param itemId Primary key identifier of an item that has had the filter specifically applied to it.  Required if
     *               applicationLevel is ITEM.
     * @param mode Seek mode of the PriceFilter.  Primarily applicable if there is a reference item that should be pulled.
     * @return The applicable PriceFilter object.
     */
    PriceFilter getPersistedPriceFilter(PriceFilter.APPLICATION_LEVEL applicationLevel, PriceFilter.MATERIAL_LEVEL materialLevel, String materialId,
                                        String finishId, String category, Long itemId, IEnums.SEEK_MODE mode);

    /**
     * Gets the most recent, and most applicable price filter for a given item with a given material.
     *
     * @param item Item object that is the focus of this price filter seek.
     * @param materialId Supplier material ID of the material that should be filtered.
     * @param finishId Supplier finish ID of the finish that should be filtered.
     * @return The applicable PriceFilter object.
     */
    PriceFilter getApplicablePricefilter(Item item, String materialId, String finishId, IEnums.SEEK_MODE mode);

    Integer persistPriceFilter(PriceFilter priceFilter, Boolean applyToRelatives);
    String generatePriceFilterCacheKey(PriceFilter filter);
    PriceFilter getCachedPriceFilter(PriceFilter filter);

    /**
     * Checks the cache first, then the persistentce layer for a price filter matching the internal categories of the
     * provided filter.  Matches are based on applicationLevel, materialLevel, category, materialId, and finishId, and itemId.
     * @param filter The search filter
     * @param forceEmptyCacheSeek If the cache is empty, forces the method to go out and attempt to pull the value from
     *                            the persistence layer.  Expensive and not recommended unless absolutely necessary.
     * @return An applicable PriceFilter object
     */
    PriceFilter getPriceFilter(PriceFilter filter, Boolean forceEmptyCacheSeek);
    Integer bulkPersistFilters(List<PriceFilter> filters);
    Integer updateLivePhotoUrl(Long itemId, String livePhotoUrl, Boolean applyToRelatives);

    Boolean updateItemStartingPrice(Long itemId, BigDecimal startingAtPrice);

    AnonymousItem persistedAnonymousItem(AnonymousItem item);
    AnonymousItem getAnonymousItem(Long itemId, IEnums.SEEK_MODE mode);
    AnonymousItem getAnonymousItemAttached(Long itemId, PersistenceManager pm);
    Boolean updateAnonymousAssociatedUser(Long itemId, Long userId);

    Boolean setShortLink(Long itemId, Long shortLinkId);

    Boolean updateBaseAndChildItemsCustomizerFilename(Long itemId, String filename, Boolean sameHighResFile);
    Boolean updateBaseAndChildItemsCustomizerFilename(Long itemId, String filename);
    Boolean updateBaseAndChildItemsCustomizerHighresFilename(Long itemId, String filename);

    ItemImage getItemImageByHash(Integer hash);
    ItemImage getItemImageById(Long itemImageId);
    ItemImage getItemImageAttached(Long itemImageId, PersistenceManager pm);
    Boolean associateLabel(Long orderId, String itemType, String labelId, IEnums.SEEK_MODE mode);
    ItemImage persistItemImage(ItemImage image);
    ItemImage updateItemImageMediumUrlPath(Long itemImageId, String urlPath);
    ItemImage updateItemImageSmallUrlPath(Long itemImageId, String urlPath);
}
