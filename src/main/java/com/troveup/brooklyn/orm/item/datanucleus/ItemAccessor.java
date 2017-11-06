package com.troveup.brooklyn.orm.item.datanucleus;

import com.google.appengine.api.memcache.stdimpl.GCacheFactory;
import com.troveup.brooklyn.orm.cart.model.Cart;
import com.troveup.brooklyn.orm.cart.model.GenericItem;
import com.troveup.brooklyn.orm.cart.model.CartItem;
import com.troveup.brooklyn.orm.common.datanucleus.ObjectAccessor;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.feed.interfaces.IFeedAccessor;
import com.troveup.brooklyn.orm.feed.model.FeedItem;
import com.troveup.brooklyn.orm.item.interfaces.IItemAccessor;
import com.troveup.brooklyn.orm.item.model.*;
import com.troveup.brooklyn.orm.materials.interfaces.IMaterialFilterAccessor;
import com.troveup.brooklyn.orm.materials.model.Finish;
import com.troveup.brooklyn.orm.materials.model.Material;
import com.troveup.brooklyn.orm.order.model.EasyPostPostageLabel;
import com.troveup.brooklyn.orm.renderqueue.interfaces.IRenderQueueAccessor;
import com.troveup.brooklyn.orm.urlshortener.interfaces.IShortLinkAccessor;
import com.troveup.brooklyn.orm.urlshortener.model.ShortLink;
import com.troveup.brooklyn.orm.user.interfaces.IUserAccessor;
import com.troveup.brooklyn.orm.user.model.Collection;
import com.troveup.brooklyn.orm.user.model.Follow;
import com.troveup.brooklyn.orm.user.model.GroupedItem;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.cache.interfaces.ICacheService;
import com.troveup.brooklyn.sdk.print.interfaces.IPrintSupplier;
import com.troveup.brooklyn.util.SizeMapper;
import com.troveup.brooklyn.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jdo.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by tim on 4/23/15.
 */
public class ItemAccessor extends ObjectAccessor implements IItemAccessor {

    @Autowired
    IUserAccessor userAccessor;

    @Autowired
    IFeedAccessor feedAccessor;

    @Autowired
    Random random;

    @Autowired
    IMaterialFilterAccessor materialFilterAccessor;

    @Autowired
    IPrintSupplier printSupplier;

    @Autowired
    IShortLinkAccessor shortLinkAccessor;

    public ItemAccessor(PersistenceManagerFactory pmfProxy) {
        super(pmfProxy);
    }

    @Override
    public Boolean createItem(Item item) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (item == null)
                throw new NullPointerException("Item cannot be null!");

            Item parentItem = null;
            Item baseItem = null;
            User owner = null;
            Collection defaultCollection = null;

            if (item.getItemParent() != null && item.getItemParent().getItemId() != null)
                parentItem = getItemAttached(item.getItemParent().getItemId(), pm);

            if (item.getBaseItemReference() != null && item.getBaseItemReference().getItemId() != null)
                baseItem = getItemAttached(item.getBaseItemReference().getItemId(), pm);

            if (item.getItemOwner() != null && item.getItemOwner().getUserId() != null) {
                owner = userAccessor.getUserAttached(item.getItemOwner().getUserId(), pm);
            }

            if (item.getDefaultCollection() != null) {
                Collection collection = userAccessor.getCollectionAttached(item.getDefaultCollection().getCollectionId(), pm);

                if (owner != null && collection.getOwner().getUserId().equals(owner.getUserId()))
                    defaultCollection = collection;

            }

            item.setItemParent(parentItem);
            item.setBaseItemReference(baseItem);
            item.setItemOwner(owner);
            item.setDefaultCollection(defaultCollection);


            if (owner != null)
                owner.getOwnedItems().add(new GroupedItem(owner, item));

            pm.setDetachAllOnCommit(true);
            pm.makePersistent(item);

            rval = true;

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean createItemWithoutDetach(Item item) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (item == null)
                throw new NullPointerException("Item cannot be null!");

            Item parentItem = null;
            Item baseItem = null;
            User owner = null;

            if (item.getItemParent() != null && item.getItemParent().getItemId() != null)
                parentItem = getItemAttached(item.getItemParent().getItemId(), pm);

            if (item.getBaseItemReference() != null && item.getBaseItemReference().getItemId() != null)
                baseItem = getItemAttached(item.getBaseItemReference().getItemId(), pm);

            if (item.getItemOwner() != null && item.getItemOwner().getUserId() != null) {
                owner = userAccessor.getUserAttached(item.getItemOwner().getUserId(), pm);
            }

            item.setItemParent(parentItem);
            item.setBaseItemReference(baseItem);
            item.setItemOwner(owner);


            if (owner != null)
                owner.getOwnedItems().add(new GroupedItem(owner, item));

            pm.makePersistent(item);

            if (item.isBaseItem()) {
                item.setItemParent(item);
                item.setBaseItemReference(item);
            }

            rval = true;

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<Item> getItems(List<String> itemIdentifier, Item.SEARCH_BY_TYPE identifierType, IEnums.SEEK_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Item> rval = null;

        try {
            if (itemIdentifier == null || itemIdentifier.size() == 0 || identifierType == null)
                throw new IllegalArgumentException("Parameter must contain at least one element");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(Item.class, ":p.contains(" + identifierType.toString() + ")");
            query.setResultClass(Item.class);

            rval = (List<Item>) query.execute(itemIdentifier, identifierType);
            populateItemSizes(rval);

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<Item> getItems(List<String> itemIdentifier, Item.SEARCH_BY_TYPE identifierType, PersistenceManager pm) {
        List<Item> rval;
        Query query = pm.newQuery(Item.class, ":p.contains(" + identifierType.toString() + ")");
        query.setResultClass(Item.class);

        rval = (List<Item>) query.execute(itemIdentifier, identifierType);

        populateItemSizes(rval);

        return rval;
    }

    @Override
    public List<Item> getItems(String itemIdentifier, Item.SEARCH_BY_TYPE identifierType, IEnums.SEEK_MODE mode) {
        List<String> itemIdentifierWrapper = new ArrayList<>();

        itemIdentifierWrapper.add(itemIdentifier);

        return getItems(itemIdentifierWrapper, identifierType, mode);
    }

    @Override
    public List<Item> getItems(Date from, Date to, IEnums.SEEK_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Item> rval = null;

        try {
            if (from == null || to == null)
                throw new IllegalArgumentException("Parameter must contain at least one element");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(Item.class, "created >= :from && created <= :to");
            query.setResultClass(Item.class);

            rval = (List<Item>) pm.detachCopyAll((List<Item>) query.execute(from, to));

            populateItemSizes(rval);

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }


    @Override
    public Item getItemAttached(Long item, PersistenceManager pm) {

        Item rval = null;

        try {
            if (item == null)
                throw new NullPointerException("Item cannot be null!");

            Query query = pm.newQuery(Item.class, "itemId == :id");
            query.setResultClass(Item.class);

            List<Item> queryItems = (List<Item>) query.execute(item);

            if (queryItems != null && queryItems.size() > 0)
                rval = queryItems.get(0);

        } catch (Exception e) {
            logError(e);
        }

        return rval;
    }

    @Override
    public Item getItemDetached(Long item, IEnums.SEEK_MODE mode) {

        PersistenceManager pm = pmf.getPersistenceManager();
        Item rval = null;

        try {
            if (item == null)
                throw new NullPointerException("Item cannot be null!");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(Item.class, "itemId == :id");
            query.setResultClass(Item.class);

            List<Item> queryItems = (List<Item>) query.execute(item.longValue());

            if (queryItems != null && queryItems.size() > 0) {
                rval = pm.detachCopy(queryItems.get(0));
                populateItemSizes(rval);
            }

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<Item> getAllActiveItems(IEnums.SEEK_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Item> rval = null;

        try {
            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(Item.class, "active == true");
            query.setResultClass(Item.class);

            rval = (List<Item>) pm.detachCopyAll((List<Item>) query.execute());

            populateItemSizes(rval);

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;

    }

    @Override
    public List<Item> getAllActiveItemsByCategory(String category, IEnums.SEEK_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Item> rval = null;

        try {
            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(Item.class, "active == true && category.equals(:category)");
            query.setResultClass(Item.class);

            rval = (List<Item>) pm.detachCopyAll((List<Item>) query.execute(category));

            populateItemSizes(rval);

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<Item> getItemsByCollectionId(Long collectionId, Long collectionPage, Long collectionPageSize,
                                             Long extraResults, IEnums.SEEK_MODE mode, Boolean includePrivate) {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Item> rval = null;

        try {
            if (collectionId == null || collectionPage == null || collectionPageSize == null || extraResults == null ||
                    mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            Query query;

            if (includePrivate)
                query = pm.newQuery(GroupedItem.class, "collection.collectionItems.contains(this) &&" +
                        "collection.collectionId == :passedId");
            else
                query = pm.newQuery(GroupedItem.class, "collection.collectionItems.contains(this) &&" +
                        "collection.collectionId == :passedId && groupedItem.privateItem == false");

            query.declareImports("import com.troveup.brooklyn.orm.user.model.Collection");
            query.declareVariables("com.troveup.brooklyn.orm.user.model.Collection collection");

            Long beginning = collectionPage * collectionPageSize;
            Long end = (collectionPage + 1) * collectionPageSize + extraResults;
            query.setRange(beginning, end);
            query.setOrdering("createdDate DESC");

            List<GroupedItem> queryItems = (List<GroupedItem>) query.execute(collectionId);

            rval = GroupedItem.groupedItemToItemList(queryItems);

            rval = (List<Item>) pm.detachCopyAll(rval);

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Long getCollectionItemCount(Long collectionId, Boolean includePrivate) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Long rval = -1l;

        try {
            if (collectionId == null || includePrivate == null)
                throw new NullPointerException("Arguments cannot be null");

            Query query;
            if (includePrivate)
                query = pm.newQuery(GroupedItem.class, "collection.collectionItems.contains(this) &&" +
                        "collection.collectionId == :passedId");
            else
                query = pm.newQuery(GroupedItem.class, "collection.collectionItems.contains(this) &&" +
                        "collection.collectionId == :passedId && groupedItem.privateItem == false");

            query.declareImports("import com.troveup.brooklyn.orm.user.model.Collection");
            query.declareVariables("com.troveup.brooklyn.orm.user.model.Collection collection");

            query.setResult("count(this)");

            rval = (Long) query.execute(collectionId);

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<Item> getPagedFeedItems(Long userId, Integer collectionPage, Long collectionPageSize,
                                        Long extraResults, IEnums.SEEK_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Item> rval = new ArrayList<>();

        try {
            if (collectionPage == null || collectionPageSize == null || extraResults == null ||
                    mode == null)
                throw new NullPointerException("Arguments cannot be null");

            Query query;

            List<FeedItem> activeFeedItems = feedAccessor.getCurrentFeedItems(IEnums.SEEK_MODE.QUICK);

            if (activeFeedItems != null && activeFeedItems.size() > 0) {
                if (userId != null && userId > -1) {
                    Map<Long, List<Integer>> feedItemOwners = new HashMap<>();

                    //Create a map of the feed item owners for easy search-and-update of feed scores based on whether or not
                    //this user has followed the user that owns this feed item
                    for (int i = 0; i < activeFeedItems.size(); ++i) {

                        if (!feedItemOwners.containsKey(activeFeedItems.get(i).getItemOwnerId())) {
                            feedItemOwners.put(activeFeedItems.get(i).getItemOwnerId(), new ArrayList<Integer>());
                        }

                        feedItemOwners.get(activeFeedItems.get(i).getItemOwnerId()).add(i);
                    }

                    //Exception with HashMap$KeySet when this gets deployed to the cloud?  Working around that...
                    List<Long> keySet = new ArrayList<>();
                    keySet.addAll(feedItemOwners.keySet());

                    //Find all of the users from the feed item owners list that the user has followed
                    query = pm.newQuery(Follow.class, "user.userId == :userId && user.followedUsers.contains(this) && " +
                            ":feedItemOwners.contains(this.userFollower.userId)");
                    query.declareVariables("com.troveup.brooklyn.orm.user.model.User user");

                    List<Follow> follows = (List<Follow>) query.execute(userId, keySet);

                    //If there were any, add an extra 750 points to their feed scores, as we want them to bubble to the top
                    //if the user has followed them
                    if (follows != null && follows.size() > 0) {
                        List<Long> followUserIds = new ArrayList<>();

                        for (Follow follow : follows) {
                            followUserIds.add(follow.getUserFollower().getUserId());
                        }

                        for (Long followUserId : followUserIds) {
                            List<Integer> feedItemIndices = feedItemOwners.get(followUserId);

                            for (Integer index : feedItemIndices) {
                                FeedItem activeFeedItem = activeFeedItems.get(index);
                                activeFeedItem.setFeedScore(activeFeedItem.getFeedScore() + 750 * .30f);
                            }
                        }
                    }
                }

                //Sort descending order
                Collections.sort(activeFeedItems, new Comparator<FeedItem>() {
                    @Override
                    public int compare(FeedItem o1, FeedItem o2) {
                        return o1.getFeedScore().compareTo(o2.getFeedScore());
                    }
                });

                Collections.reverse(activeFeedItems);

                List<FeedItem> feedItemSubset;

                //Get the beginning of the subset
                Long subsetBeginning = collectionPage * collectionPageSize < activeFeedItems.size() ?
                        collectionPage * collectionPageSize : activeFeedItems.size() - 1;

                //Get the end of the subset
                Long subsetEnd = collectionPage * collectionPageSize + collectionPageSize + extraResults < activeFeedItems.size() ?
                        collectionPage * collectionPageSize + collectionPageSize + extraResults :
                        activeFeedItems.size();

                configureFetchGroups(pm, mode);

                //Grab the smaller subset of the feed items
                feedItemSubset = activeFeedItems.subList(subsetBeginning.intValue(), subsetEnd.intValue());

                List<Long> itemSubset = new ArrayList<>();
                Map<Long, Integer> sortOrder = new HashMap<>();

                for (int i = 0; i < feedItemSubset.size(); ++i) {
                    itemSubset.add(feedItemSubset.get(i).getItemId());
                    sortOrder.put(feedItemSubset.get(i).getItemId(), i);
                }

                //Broken internally within datanucleus for some reason, have to go a different path ;(
                //Grab the items indicated by the feed item subset and return them
            /*Query secondQuery = pm.newQuery(Item.class, "feedItem.itemId == this.itemId && :feedItemSubset.contains(feedItem)");
            secondQuery.declareImports("import com.troveup.brooklyn.orm.feed.model.FeedItem");
            secondQuery.declareVariables("com.troveup.brooklyn.orm.feed.model.FeedItem feedItem");*/

                Query secondQuery = pm.newQuery(Item.class, ":itemSubset.contains(itemId)");

                rval = (List<Item>) secondQuery.execute(itemSubset);
                rval = (List<Item>) pm.detachCopyAll(rval);

                //Sort the returned list of items according to their feed scores, since we can't seem to use
                //feed items to do the sorting
                for (Item item : rval) {
                    item.setSortOrder(sortOrder.get(item.getItemId()));
                }

                Collections.sort(rval, new Comparator<Item>() {
                    @Override
                    public int compare(Item o1, Item o2) {
                        return o1.getSortOrder().compareTo(o2.getSortOrder());
                    }
                });

            }
        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<Item> getSisterItems(Long itemId, IEnums.SEEK_MODE mode, Long limit) {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Item> rval = null;

        try {
            if (itemId == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            Item item = getItemAttached(itemId, pm);

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(Item.class, "itemParent.itemId == :itemParentId && itemId != :itemId");
            query.setOrdering("created DESC, trovedCount DESC, remadeCount DESC");

            if (limit != null && limit > 0)
                query.setRange(0l, limit);

            rval = (List<Item>) query.execute(item.getItemParent().getItemId(), itemId);

            rval = (List<Item>) pm.detachCopyAll(rval);
        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean setDefaultItemCardImage(Long itemId) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (itemId == null)
                throw new NullPointerException("Argument cannot be null");

            Item item = getItemAttached(itemId, pm);
            List<Material> availableMaterials = printSupplier.getSupplierMaterials();
            List<Finish> availableFinishes = new ArrayList<>();

            for (Material material : availableMaterials) {
                availableFinishes.addAll(material.getFinishList());
            }

            Finish defaultFinish = Finish.getFinishByFinishId(item.getFinishId(), availableFinishes);

            List<ItemImage> imagesOfDefaultMaterial = new ArrayList<>();
            for (ItemImage image : item.getImages()) {
                if (image.getMaterial().equals(defaultFinish.getRealityServerMapping()))
                    imagesOfDefaultMaterial.add(image);
            }

            int randomlySelectedImageIndex = random.nextInt(imagesOfDefaultMaterial.size());

            item.setDefaultCardImageUrl(imagesOfDefaultMaterial.get(randomlySelectedImageIndex).getMediumImageUrlPath());

            rval = true;

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<Item> getRenderedItemsWithNoDefaultImage(Long limit, IEnums.SEEK_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Item> rval = null;

        try {
            List<IRenderQueueAccessor.RENDER_STATUS> renderStatuses = new ArrayList<>();
            renderStatuses.add(IRenderQueueAccessor.RENDER_STATUS.QUEUED);
            renderStatuses.add(IRenderQueueAccessor.RENDER_STATUS.SUBMITTED);
            renderStatuses.add(IRenderQueueAccessor.RENDER_STATUS.RUNNING);

            //Get all items who don't have a default card image, and are not queued, submitted, or running a render
            Query query = pm.newQuery(Item.class,
                    "this.defaultCardImageUrl == null && !:statuses.contains(render.renderStatus) && " +
                            "render.itemReference.itemId == this.itemId");
            query.declareVariables("com.troveup.brooklyn.orm.renderqueue.model.Render render");

            if (limit != null && limit > 0) {
                query.setRange(0l, limit);
            }

            if (mode != null) {
                configureFetchGroups(pm, mode);
            }

            rval = (List<Item>) query.execute(renderStatuses);
        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Item getItemBaseItem(Long itemId, IEnums.SEEK_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Item rval = null;

        try {
            if (itemId == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            Item item = getItemAttached(itemId, pm);

            rval = item.getBaseItemReference();

            rval = pm.detachCopy(rval);

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Item getItemParentItem(Long itemId, IEnums.SEEK_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Item rval = null;

        try {
            if (itemId == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            Item item = getItemAttached(itemId, pm);

            rval = item.getItemParent();

            rval = pm.detachCopy(rval);

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Item getItemGrandParentItem(Long itemId, IEnums.SEEK_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Item rval = null;

        try {
            if (itemId == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            Item item = getItemAttached(itemId, pm);

            rval = item.getItemParent().getItemParent();

            rval = pm.detachCopy(rval);

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateItemMediumVolume(Long itemId, Float volume) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (itemId == null || volume == null)
                throw new NullPointerException("Arguments cannot be null");

            Item item = getItemAttached(itemId, pm);
            item.setMediumVolume(volume);

            rval = true;
        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateItemBaseItemReference(Long itemId, Long baseItemId) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (itemId == null || baseItemId == null)
                throw new NullPointerException("Arguments cannot be null");

            Item targetItem = getItemAttached(itemId, pm);
            Item baseItem = getItemAttached(baseItemId, pm);

            targetItem.setBaseItemReference(baseItem);

            rval = true;

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateItemParentItemReference(Long itemId, Long parentItemId) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (itemId == null || parentItemId == null)
                throw new NullPointerException("Arguments cannot be null");

            Item targetItem = getItemAttached(itemId, pm);
            Item parentItem = getItemAttached(parentItemId, pm);

            targetItem.setItemParent(parentItem);

            rval = true;

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateItem(Item item, IEnums.UPDATE_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();

        Boolean rval = false;

        try {
            if (item == null)
                throw new NullPointerException("Item cannot be null!");

            Item seekItem = getItemAttached(item.getItemId(), pm);

            if (item.getItemParent() != null) {
                seekItem.setItemParent(item.getItemParent());
            }
            if (item.getBaseItemReference() != null) {
                seekItem.setBaseItemReference(item.getBaseItemReference());
            }

            if (mode == IEnums.UPDATE_MODE.REPLACE) {
                if (item.getItemAttributes() != null) {

                    List<ItemAttribute> attributeCopies = new ArrayList<>();

                    for (ItemAttribute attribute : item.getItemAttributes()) {
                        attributeCopies.add(new ItemAttribute(attribute));
                    }

                    seekItem.setItemAttributes(attributeCopies);
                }
                if (item.getImages() != null) {
                    List<ItemImage> imageCopies = new ArrayList<>();

                    for (ItemImage image : item.getImages()) {
                        imageCopies.add(new ItemImage(image));
                    }

                    seekItem.setImages(imageCopies);
                }
            } else if (mode == IEnums.UPDATE_MODE.APPEND) {
                if (item.getItemAttributes() != null) {
                    seekItem.getItemAttributes().addAll(item.getItemAttributes());
                }
                if (item.getImages() != null) {
                    seekItem.getImages().addAll(item.getImages());
                }
            }

            if (item.getItemName() != null) {
                seekItem.setItemName(item.getItemName());
            }
            if (item.getItemDescription() != null) {
                seekItem.setItemDescription(item.getItemDescription());
            }
            if (item.getItemOwner() != null) {
                seekItem.setItemOwner(item.getItemOwner());
            }
            if (item.getCustomizerFilename() != null) {
                seekItem.setCustomizerFilename(item.getCustomizerFilename());
            }
            if (item.getCustomizerPath() != null) {
                seekItem.setCustomizerPath(item.getCustomizerPath());
            }

            seekItem.setBaseItem(item.isBaseItem());
            seekItem.setActive(item.isActive());

            rval = true;
        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean deactivateItem(Long itemId) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (itemId == null)
                throw new NullPointerException("Item cannot be null!");

            Item item = getItemAttached(itemId, pm);

            item.setActive(false);

            rval = true;
        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean activateItem(Long itemId) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (itemId == null)
                throw new NullPointerException("Item cannot be null!");

            Item item = getItemAttached(itemId, pm);

            item.setActive(true);

            rval = true;
        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean addItemAttributes(Long itemId, List<ItemAttribute> attributes) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (itemId == null || attributes == null || attributes.size() == 0)
                throw new IllegalArgumentException("Parameter must contain at least one element");

            Item item = getItemAttached(itemId, pm);

            item.getItemAttributes().addAll(attributes);

            rval = true;

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean addImage(Long itemId, ItemImage image) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (itemId == null || image == null)
                throw new IllegalArgumentException("Parameter must contain at least one element");

            Item item = getItemAttached(itemId, pm);

            //Inserts the image at the back of the images list.
            if (item.getImages().size() == 0)
                image.setOrder(0);
            else
                image.setOrder(item.getImages().size());

            item.getImages().add(image);

            rval = true;

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;

    }

    @Override
    public Boolean addImage(Long itemId, List<ItemImage> images) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (itemId == null || images == null || images.size() == 0)
                throw new IllegalArgumentException("Parameter must contain at least one element");

            Item item = getItemAttached(itemId, pm);

            //Set the order to the end
            for (int i = 0; i < images.size(); ++i)
                images.get(i).setOrder(item.getImages().size() + i);


            item.getImages().addAll(images);

            rval = true;

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean addImage(Long itemId, ItemImage image, int position) {

        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (itemId == null || image == null)
                throw new IllegalArgumentException("Parameter must contain at least one element");

            Item item = getItemAttached(itemId, pm);

            if (item.getImages().size() < position)
                throw new IllegalArgumentException("Position must be between 0 and " + item.getImages().size());

            Collections.sort(item.getImages());

            image.setOrder(position);

            for (int i = position; i < item.getImages().size(); ++i) {
                item.getImages().get(i).setOrder(i + 1);
            }

            item.getImages().add(image);

            rval = true;

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean removeImage(Long itemId, String imageLocation) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (itemId == null || imageLocation == null || imageLocation.isEmpty())
                throw new IllegalArgumentException("Parameter must contain at least one element");

            Query query = pm.newQuery(ItemImage.class, "ownerItem.itemId == :itemId && imageLocation == :location");
            Long result = query.deletePersistentAll(itemId.longValue(), imageLocation);

            if (result > 0)
                rval = true;


        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean removeImage(Long itemId, Long imageId) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (itemId == null || imageId == null)
                throw new IllegalArgumentException("Parameter must contain at least one element");

            Query query = pm.newQuery(ItemImage.class, "ownerItem.itemId == :itemId && imageId == :imageId");
            Long result = query.deletePersistentAll(itemId.longValue(), imageId.longValue());

            if (result > 0)
                rval = true;

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean removeImage(Long itemId, int imagePosition) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (itemId == null)
                throw new IllegalArgumentException("Parameter must contain at least one element");

            Query query = pm.newQuery(ItemImage.class, "ownerItem.itemId == :itemId && order == :imagePosition");
            Long result = query.deletePersistentAll(itemId.longValue(), imagePosition);

            if (result > 0)
                rval = true;

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean removeAllImages(Long itemId) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Item item = getItemAttached(itemId, pm);

        item.setImages(new ArrayList<ItemImage>());

        return true;
    }

    @Override
    public Boolean removeCustomizerImages(Long itemId) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (itemId == null)
                throw new NullPointerException("Argument cannot be null");

            Item item = getItemAttached(itemId, pm);

            List<Integer> customizerIndexes = new ArrayList<>();

            List<ItemImage> images = item.getImages();

            for (int i = 0; i < images.size(); ++i) {
                if (images.get(i).isCustomizerImage())
                    customizerIndexes.add(i);
            }

            for (int i = 0; i < customizerIndexes.size(); ++i) {
                int index = customizerIndexes.get(i);
                item.getImages().remove(index);
            }

            rval = true;

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;

    }

    private List<ItemAttribute> getItemAttributes(Long itemId, List<Long> attributeId) {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<ItemAttribute> rval = null;

        try {
            if (itemId == null || attributeId == null || attributeId.size() == 0)
                throw new IllegalArgumentException("Parameter must contain at least one element");

            Query query = pm.newQuery(ItemAttribute.class, ":p.contains(attribute) & " +
                    "attribute.attributeOwner.itemId == :id");
            query.setResultClass(ItemAttribute.class);

            rval = (List<ItemAttribute>) query.execute(attributeId.toArray(), itemId.longValue());

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean deactivateAttributes(Long itemId, List<Long> attributeId) {
        Boolean rval = false;

        try {
            List<ItemAttribute> attributes = getItemAttributes(itemId, attributeId);

            if (attributes != null) {
                for (ItemAttribute attribute : attributes) {
                    attribute.setValid(false);
                }
            }

            rval = true;

        } catch (Exception e) {
            logError(e);
        }

        return rval;
    }

    @Override
    public Boolean activateAttributes(Long itemId, List<Long> attributeId) {
        Boolean rval = false;

        try {
            List<ItemAttribute> attributes = getItemAttributes(itemId, attributeId);

            if (attributes != null) {
                for (ItemAttribute attribute : attributes) {
                    attribute.setValid(true);
                }
            }

            rval = true;

        } catch (Exception e) {
            logError(e);
        }

        return rval;
    }

    @Override
    public Boolean updateItemAttribute(Long itemId, ItemAttribute attribute) {
        Boolean rval = false;
        PersistenceManager pm = pmf.getPersistenceManager();

        try {
            if (itemId == null || attribute == null)
                throw new IllegalArgumentException("Parameter must contain at least one element");

            Item item = getItemAttached(itemId, pm);

            //Remove the old one
            int attributeIndex = item.getItemAttributes().indexOf(attribute);
            if (attributeIndex > -1) {
                item.getItemAttributes().remove(attributeIndex);
            }

            //Add the new one
            rval = item.getItemAttributes().add(attribute);
        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateItemCounters(Long itemId) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            Item item = getItemAttached(itemId, pm);

            Query countQuery = pm.newQuery(User.class, "trovedItems.contains(item) && " +
                    "item.groupedItem.itemId == :itemId");
            countQuery.declareVariables("com.troveup.brooklyn.orm.user.model.GroupedItem item");
            countQuery.setResult("count(this)");
            item.setTrovedCount((Long) countQuery.execute(item));


            countQuery = pm.newQuery(Item.class, "itemParent == :item");
            countQuery.setResult("count(this)");
            item.setRemadeCount((Long) countQuery.execute(item));

            rval = true;

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateItemName(Long itemId, Long userId, String name) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (itemId == null || userId == null || name == null)
                throw new NullPointerException("Arguments cannot be null");

            Item item = getItemAttached(itemId, pm);

            if (item.getItemOwner() != null && item.getItemOwner().getUserId().equals(userId)) {
                item.setItemName(name);
                rval = true;
            }

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateItemDescription(Long itemId, Long userId, String description) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (itemId == null || userId == null || description == null)
                throw new NullPointerException("Arguments cannot be null");

            Item item = getItemAttached(itemId, pm);

            if (item.getItemOwner() != null && item.getItemOwner().getUserId().equals(userId)) {
                item.setItemDescription(description);
                rval = true;
            }

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean setItemDefaultCollection(Long itemId, Long collectionId, Long userId) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (itemId == null || collectionId == null || userId == null)
                throw new NullPointerException("Arguments cannot be null");

            Item item = getItemAttached(itemId, pm);
            Collection collection = userAccessor.getCollectionAttached(collectionId, pm);

            if (item.getItemOwner().getUserId().equals(userId) && collection.getOwner().getUserId().equals(userId)) {
                item.setDefaultCollection(collection);
                rval = true;
            }
        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean clearItemDefaultCollection(Long itemId, Long userId) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (itemId == null || userId == null)
                throw new NullPointerException("Arguments cannot be null");

            Item item = getItemAttached(itemId, pm);

            if (item.getItemOwner().getUserId().equals(userId)) {
                item.setDefaultCollection(null);
                rval = true;
            }
        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<Item> getBaseItemsByCategory(String category, Integer collectionPage, Long collectionPageSize,
                                             Long extraResults, IEnums.SEEK_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Item> rval = null;

        try {
            if (category == null || mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(Item.class, "category == :category && itemOwner.userId == 1 && active == true");

            Long beginning = collectionPage * collectionPageSize;
            Long end = (collectionPage + 1) * collectionPageSize + extraResults;
            query.setRange(beginning, end);
            query.setOrdering("itemId DESC");

            rval = (List<Item>) query.execute(category);

            rval = (List<Item>) pm.detachCopyAll(rval);
        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<Item> getFeaturedItems(Integer collectionPage, Long collectionPageSize, Long extraResults,
                                       IEnums.SEEK_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Item> rval = null;

        try {
            if (mode == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(Item.class, "featured == true");

            Long beginning = collectionPage * collectionPageSize;
            Long end = (collectionPage + 1) * collectionPageSize + extraResults;
            query.setRange(beginning, end);
            query.setOrdering("itemId DESC");

            rval = (List<Item>) query.execute();

            rval = (List<Item>) pm.detachCopyAll(rval);
        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<Item> getPagedTroveItems(Long userId, Integer collectionPage,
                                         Long collectionPageSize, Long extraResults, IEnums.SEEK_MODE mode, Boolean includePrivate) {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Item> rval = null;

        try {
            if (userId == null || collectionPage == null || collectionPageSize == null || extraResults == null ||
                    mode == null || includePrivate == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            Query query;
            if (includePrivate)
                query = pm.newQuery(GroupedItem.class, "user.userId == :userId && user.trovedItems.contains(this) && groupedItem.active == true");
            else
                query = pm.newQuery(GroupedItem.class, "user.userId == :userId && user.trovedItems.contains(this) && " +
                        "groupedItem.privateItem == false && groupedItem.active == true");

            query.declareImports("import com.troveup.brooklyn.orm.user.model.User");
            query.declareVariables("com.troveup.brooklyn.orm.user.model.User user");

            Long beginning = collectionPage * collectionPageSize;
            Long end = (collectionPage + 1) * collectionPageSize + extraResults;
            query.setRange(beginning, end);
            query.setOrdering("createdDate DESC");

            List<GroupedItem> queryList = (List<GroupedItem>) query.execute(userId);

            rval = GroupedItem.groupedItemToItemList(queryList);

            rval = (List<Item>) pm.detachCopyAll(rval);
        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Long getTrovedItemCount(Long userId, Boolean includePrivate) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Long rval = -1l;

        try {
            if (userId == null)
                throw new NullPointerException("Argument cannot be null");

            Query query;
            if (includePrivate)
                query = pm.newQuery(GroupedItem.class, "user.userId == :userId && user.trovedItems.contains(this) && groupedItem.active == true");
            else
                query = pm.newQuery(GroupedItem.class, "user.userId == :userId && user.trovedItems.contains(this) && " +
                        "groupedItem.privateItem == false && groupedItem.active == true");

            query.declareImports("import com.troveup.brooklyn.orm.user.model.User");
            query.declareVariables("com.troveup.brooklyn.orm.user.model.User user");

            query.setResult("count(this)");

            rval = (Long) query.execute(userId);

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<Item> getBaseItems() {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Item> rval = null;

        try {
            Query query = pm.newQuery(Item.class, "baseItem == true");

            rval = (List<Item>) query.execute();
        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateItemAttributes(Long itemId, List<ItemAttribute> attributes) {
        Boolean rval = false;
        PersistenceManager pm = pmf.getPersistenceManager();

        try {
            if (itemId == null || attributes == null)
                throw new IllegalArgumentException("Parameter must contain at least one element");

            Item item = getItemAttached(itemId, pm);

            if (item.getItemAttributes() == null) {
                item.setItemAttributes(new ArrayList<ItemAttribute>());
            }

            for (ItemAttribute attribute : attributes) {
                int attributeIndex = item.getItemAttributes().indexOf(attribute);
                //Remove the old one
                if (attributeIndex > -1) {
                    item.getItemAttributes().remove(attributeIndex);
                }
                //Add a new one
                item.getItemAttributes().add(attribute);
            }

            rval = true;
        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean addChain(Chain chain) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (chain == null)
                throw new NullPointerException("Argument cannot be null");

            rval = pm.makePersistent(chain) != null;
        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<Chain> getActiveChains() {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Chain> rval = null;

        try {
            Query query = pm.newQuery(Chain.class, "active == true");
            rval = (List<Chain>) query.execute();
            rval = (List<Chain>) pm.detachCopyAll(rval);
        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Chain getChain(Long chainId) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Chain rval = null;

        try {
            if (chainId == null)
                throw new NullPointerException("Argument cannot be null or empty");

            Query query = pm.newQuery(Chain.class, "chainId == :chainId");
            List<Chain> queryResults = (List<Chain>) query.execute(chainId);

            if (queryResults != null && queryResults.size() > 0) {
                rval = queryResults.get(0);
                rval = pm.detachCopy(rval);
            }
        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Chain getChainAttached(Long chainId, PersistenceManager pm) {
        Query query = pm.newQuery(Chain.class, "chainId == :chainId");
        Chain rval = null;
        List<Chain> queryResults = (List<Chain>) query.execute(chainId);

        if (queryResults != null && queryResults.size() > 0) {
            rval = queryResults.get(0);
        }

        return rval;
    }

    @Override
    public Integer updateAllPricing(PRICING_STRATEGY strategy, String category, BigDecimal newMarkup, BigDecimal newPackaging, BigDecimal newShipping, BigDecimal newPrototyping) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Integer rval = 0;

        try {
            if (strategy == null || (category == null && strategy == PRICING_STRATEGY.CATEGORY))
                throw new NullPointerException("Arguments cannot be null");

            //Keeping this around, but won't work until extensions are added and the Bulk Update extension is enabled:
            //http://www.datanucleus.org/products/accessplatform_2_2/jdo/jdoql.html
            /*String queryString = "UPDATE Item SET this.percentageMarkup = " + newMarkup.toString() + ", this.packaging = :packaging, this.shippingTotal = :shipping, this.prototypeTotal = :prototype";

            if (strategy == PRICING_STRATEGY.CATEGORY)
                queryString += " WHERE this.category == " + category.toUpperCase();

            Query query = pm.newQuery(queryString);

            rval = (Integer) query.execute(newPackaging, newShipping, newPrototyping);*/

            String queryString;

            if (strategy == PRICING_STRATEGY.GLOBAL)
                queryString = "1==1";
            else
                queryString = "category == :category";

            Query query = pm.newQuery(Item.class, queryString);

            List<Item> queryList = (List<Item>) query.execute(category);

            if (queryList != null) {
                for (Item item : queryList) {
                    item.setPercentageMarkup(newMarkup);
                    item.setPackaging(newPackaging);
                    item.setShippingTotal(newShipping);
                    item.setPrototypeTotal(newPrototyping);
                }

                rval = queryList.size();
            }
        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Integer updateItemLevelPricing(Long itemId, Boolean applyToRelatives, BigDecimal newMarkup, BigDecimal newPackaging, BigDecimal newShipping, BigDecimal newPrototyping) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Integer rval = 0;

        try {
            if (itemId == null || applyToRelatives == null)
                throw new NullPointerException("Arguments cannot be null");

            Item baseItem = getItemAttached(itemId, pm);

            if (applyToRelatives) {
                Item parentItem = getItemAttached(baseItem.getItemParent().getItemId(), pm);

                Query query = pm.newQuery(Item.class, "itemParent == :parent");
                List<Item> queryList = (List<Item>) query.execute(parentItem);

                if (queryList != null) {
                    for (Item item : queryList) {
                        item.setPercentageMarkup(newMarkup);
                        item.setPackaging(newPackaging);
                        item.setShippingTotal(newShipping);
                        item.setPrototypeTotal(newPrototyping);
                    }

                    rval = queryList.size();
                }
            } else {
                baseItem.setPercentageMarkup(newMarkup);
                baseItem.setPackaging(newPackaging);
                baseItem.setShippingTotal(newShipping);
                baseItem.setPrototypeTotal(newPrototyping);

                rval = 1;
            }
        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public PriceFilter getLatestGlobalPriceFilter(PriceFilter.MATERIAL_LEVEL materialLevel, String materialId,
                                                  String finishId, IEnums.SEEK_MODE mode) {
        return getPersistedPriceFilter(PriceFilter.APPLICATION_LEVEL.GLOBAL, materialLevel, materialId, finishId, null, null, mode);
    }

    @Override
    public PriceFilter getLatestCategoryPriceFilter(PriceFilter.MATERIAL_LEVEL materialLevel, String category, String materialId,
                                                    String finishId, IEnums.SEEK_MODE mode) {
        return getPersistedPriceFilter(PriceFilter.APPLICATION_LEVEL.CATEGORY, materialLevel, materialId, finishId, category, null, mode);
    }

    @Override
    public PriceFilter getLatestItemPriceFilter(PriceFilter.MATERIAL_LEVEL materialLevel, String materialId,
                                                String finishId, Long itemId, IEnums.SEEK_MODE mode) {
        return getPersistedPriceFilter(PriceFilter.APPLICATION_LEVEL.ITEM, materialLevel, materialId, finishId, null, itemId, mode);
    }

    @Override
    public PriceFilter getPersistedPriceFilter(PriceFilter.APPLICATION_LEVEL applicationLevel,
                                               PriceFilter.MATERIAL_LEVEL materialLevel, String materialId,
                                               String finishId, String category, Long itemId, IEnums.SEEK_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();
        PriceFilter rval = null;

        try {
            configureFetchGroups(pm, mode);

            Item itemReference = null;

            String queryString = "active == true && applicationLevel == :applevel";

            if (applicationLevel == PriceFilter.APPLICATION_LEVEL.CATEGORY)
                queryString += " && category.equals(\"" + category + "\")";

            queryString += " && materialLevel == :matlevel";

            if (applicationLevel == PriceFilter.APPLICATION_LEVEL.ITEM) {
                queryString += " && itemReference == :reference";
                itemReference = getItemAttached(itemId, pm);
            }

            if (materialLevel == PriceFilter.MATERIAL_LEVEL.MATERIAL)
                queryString += " && materialId.equals(\"" + materialId + "\") && finishId.equals(\"" + finishId + "\")";

            Query query = pm.newQuery(PriceFilter.class, queryString);
            query.setOrdering("creationDate DESC");
            query.setRange(0l, 1l);

            List<PriceFilter> queryList;

            if (itemReference != null)
                queryList = (List<PriceFilter>) query.execute(applicationLevel, materialLevel, itemReference);
            else
                queryList = (List<PriceFilter>) query.execute(applicationLevel, materialLevel);

            if (queryList != null && queryList.size() > 0) {
                rval = queryList.get(0);
                rval = pm.detachCopy(rval);
            }
        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<Item> getItemsAttachedByBaseItemId(Long baseItemId, PersistenceManager pm) {
        Query query = pm.newQuery(Item.class, "baseItemReference.itemId == :baseItemId");

        return (List<Item>) query.execute(baseItemId);
    }

    @Override
    public Integer persistPriceFilter(PriceFilter priceFilter, Boolean applyToRelatives) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Integer rval = 0;

        try {
            if (priceFilter == null)
                throw new NullPointerException("Argument cannot be null");

            List<PriceFilter> siblingFilters = null;

            //If this has an item reference attached, get a persistence managed version of it
            if (priceFilter.getItemReference() != null) {
                Item item = getItemAttached(priceFilter.getItemReference().getItemId(), pm);
                priceFilter.setItemReference(item);

                //If there are relatives to apply this to, get them all, duplicate the price filter, then
                //apply the relatives to the filters
                if (applyToRelatives) {
                    siblingFilters = new ArrayList<>();
                    List<Item> siblings = getItemsAttachedByBaseItemId(item.getBaseItemReference().getItemId(), pm);

                    for (Item sibling : siblings) {
                        PriceFilter siblingFilter = new PriceFilter(priceFilter);
                        siblingFilter.setItemReference(sibling);
                        siblingFilters.add(siblingFilter);

                        populatePriceFilterCache(generatePriceFilterCacheKey(siblingFilter), gson.toJson(siblingFilter));
                    }
                }
            }

            if (siblingFilters != null) {
                pm.makePersistentAll(siblingFilters);
                rval = siblingFilters.size();
            } else {
                populatePriceFilterCache(generatePriceFilterCacheKey(priceFilter), gson.toJson(priceFilter));
                pm.makePersistent(priceFilter);
                rval = 1;
            }

        } catch (Exception e) {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Integer bulkPersistFilters(List<PriceFilter> filters)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Integer rval = 0;

        try {
            if (filters == null || filters.size() == 0)
                throw new NullPointerException("Argument cannot be null or empty");

            Item item = null;

            //Assume all filters are for a single item
            if (filters.get(0).getItemReference() != null)
            {
                item = getItemAttached(filters.get(0).getItemReference().getItemId(), pm);

                for (PriceFilter filter : filters)
                {
                    filter.setItemReference(item);
                }
            }

            pm.makePersistentAll(filters);

            rval = filters.size();

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public PriceFilter getCachedPriceFilter(PriceFilter filter)
    {
        PriceFilter rval = null;

        try {
            String cacheString = cacheService.getCacheValue(generatePriceFilterCacheKey(filter));

            if (cacheString != null && cacheString.length() > 0 && !cacheString.equals(EMPTY_CACHE))
                rval = gson.fromJson(cacheString, PriceFilter.class);
            else if (cacheString != null && cacheString.equals(EMPTY_CACHE))
                rval = new PriceFilter();
        }
        catch (Exception e)
        {
            logError(e);
        }

        return rval;
    }

    @Override
    public PriceFilter getPriceFilter(PriceFilter filter, Boolean forceEmptyCacheSeek)
    {
        PriceFilter rval = getCachedPriceFilter(filter);

        if (rval == null || (rval.getPriceId() == null && forceEmptyCacheSeek))
        {
            Long itemReferenceId = filter.getItemReference() != null ? filter.getItemReference().getItemId() : null;

            rval = getPersistedPriceFilter(filter.getApplicationLevel(), filter.getMaterialLevel(), filter.getMaterialId(), filter.getFinishId(), filter.getCategory(), itemReferenceId, IEnums.SEEK_MODE.QUICK);
        }

        return rval;
    }

    @Override
    public String generatePriceFilterCacheKey(PriceFilter filter)
    {
        String cacheKey;

        if (filter.getApplicationLevel() == PriceFilter.APPLICATION_LEVEL.GLOBAL)
        {
            if (filter.getMaterialLevel() == PriceFilter.MATERIAL_LEVEL.MATERIAL)
            {
                cacheKey = ICacheService.GLOBAL_PRICE_FILTER_MATERIAL_CACHE_KEY.
                        replace("X", filter.getMaterialId()).replace("Y", filter.getFinishId());
            } else {
                cacheKey = ICacheService.GLOBAL_PRICE_FILTER_CACHE_KEY;
            }
        }
        else if (filter.getApplicationLevel() == PriceFilter.APPLICATION_LEVEL.CATEGORY) {
            if (filter.getMaterialLevel() == PriceFilter.MATERIAL_LEVEL.MATERIAL) {
                cacheKey = ICacheService.CATEGORY_PRICE_FILTER_MATERIAL_CACHE_KEY.
                        replace("X", filter.getMaterialId()).replace("Y", filter.getFinishId()).replace("Z", filter.getCategory());
            } else {
                cacheKey = ICacheService.CATEGORY_PRICE_FILTER_CACHE_KEY.replace("X", filter.getCategory());
            }
        }
        else
        {
            if (filter.getMaterialLevel() == PriceFilter.MATERIAL_LEVEL.MATERIAL) {
                cacheKey = ICacheService.ITEM_PRICE_FILTER_MATERIAL_CACHE_KEY
                        .replace("X", filter.getMaterialId())
                        .replace("Y", filter.getFinishId())
                        .replace("Z", filter.getItemReference().getItemId().toString());
            } else {
                cacheKey = ICacheService.ITEM_PRICE_FILTER_CACHE_KEY
                        .replace("X", filter.getItemReference().getItemId().toString());
            }
        }

        return cacheKey;
    }

    @Override
    public PriceFilter getApplicablePricefilter(Item item, String materialId, String finishId, IEnums.SEEK_MODE mode) {
        String cacheKey;

        PriceFilter globalPriceFilter = null;
        PriceFilter globalMaterialPriceFilter = null;
        PriceFilter categoryPriceFilter = null;
        PriceFilter categoryMaterialPriceFilter = null;
        PriceFilter itemPriceFilter = null;
        PriceFilter itemMaterialPriceFilter = null;

        PriceFilter rval = null;

        List<PriceFilter> priceFilterSorter = new ArrayList<>();

        //Make sure that we don't have any null pointer exceptions
        if (item.getCategory() == null)
            item.setCategory("");

        String cacheString = cacheService.getCacheValue(ICacheService.GLOBAL_PRICE_FILTER_CACHE_KEY);

        //Get the global price filter for all materials first, if it exists
        if (cacheString != null && cacheString.length() > 0 && !cacheString.equals(EMPTY_CACHE)) {
            globalPriceFilter = gson.fromJson(cacheString, PriceFilter.class);
        }
        //Possible cache miss, make sure it actually exists, then repopulate the cache
        else if (cacheString == null) {
            globalPriceFilter = getLatestGlobalPriceFilter(PriceFilter.MATERIAL_LEVEL.ALL, null, null,
                    mode);

            //Repopulate the cache
            if (globalPriceFilter != null) {
                populatePriceFilterCache(ICacheService.GLOBAL_PRICE_FILTER_CACHE_KEY, gson.toJson(globalPriceFilter));
            }
            //If this isn't an actual entry, populate the cache with an empty variable so that DB seeks don't continue
            //to happen
            else {
                populatePriceFilterCache(ICacheService.GLOBAL_PRICE_FILTER_CACHE_KEY, EMPTY_CACHE);
            }
        }

        if (materialId != null && finishId != null) {
            cacheKey = ICacheService.GLOBAL_PRICE_FILTER_MATERIAL_CACHE_KEY.
                    replace("X", materialId).replace("Y", finishId);
            cacheString = cacheService.getCacheValue(cacheKey);

            //Get the global price filter for a specific material, if it exists
            if (cacheString != null && cacheString.length() > 0 && !cacheString.equals(EMPTY_CACHE)) {
                globalMaterialPriceFilter = gson.fromJson(cacheString, PriceFilter.class);
            }
            //Possible cache miss, make sure it actually exists, then repopulate the cache.
            else if (cacheString == null) {
                globalMaterialPriceFilter = getLatestGlobalPriceFilter(PriceFilter.MATERIAL_LEVEL.MATERIAL, materialId, finishId, mode);

                //Repopulate the cache if necessary
                if (globalMaterialPriceFilter != null) {
                    populatePriceFilterCache(cacheKey, gson.toJson(globalMaterialPriceFilter));
                }
                //If this isn't an actual entry, populate the cache with an empty variable so that DB seeks don't continue
                //to happen
                else {
                    populatePriceFilterCache(cacheKey, EMPTY_CACHE);
                }
            }

            //Get the material based category price filter
            cacheKey = ICacheService.CATEGORY_PRICE_FILTER_MATERIAL_CACHE_KEY.
                    replace("X", materialId).replace("Y", finishId).replace("Z", item.getCategory());
            cacheString = cacheService.getCacheValue(cacheKey);

            if (cacheString != null && cacheString.length() > 0 && !cacheString.equals(EMPTY_CACHE)) {
                categoryMaterialPriceFilter = gson.fromJson(cacheString, PriceFilter.class);
            }
            //Possible cache miss, make sure it actually exists, then repopulate the cache.
            else if (cacheString == null) {
                categoryMaterialPriceFilter = getLatestCategoryPriceFilter(PriceFilter.MATERIAL_LEVEL.MATERIAL, item.getCategory(), materialId, finishId, mode);

                if (categoryMaterialPriceFilter != null) {
                    populatePriceFilterCache(cacheKey, gson.toJson(categoryMaterialPriceFilter));
                }
                //If this isn't an actual entry, populate the cache with an empty variable so that DB seeks don't continue
                //to happen
                else {
                    populatePriceFilterCache(cacheKey, EMPTY_CACHE);
                }
            }

            cacheKey = ICacheService.ITEM_PRICE_FILTER_MATERIAL_CACHE_KEY
                    .replace("X", materialId).replace("Y", finishId).replace("Z", item.getItemId().toString());
            cacheString = cacheService.getCacheValue(cacheKey);

            if (cacheString != null && cacheString.length() > 0 && !cacheString.equals(EMPTY_CACHE)) {
                itemMaterialPriceFilter = gson.fromJson(cacheString, PriceFilter.class);
            } else if (cacheString == null) {
                itemMaterialPriceFilter = getLatestItemPriceFilter(PriceFilter.MATERIAL_LEVEL.MATERIAL, materialId, finishId, item.getItemId(), mode);

                if (itemMaterialPriceFilter != null) {
                    populatePriceFilterCache(cacheKey, gson.toJson(itemMaterialPriceFilter));
                }
                //If this isn't an actual entry, populate the cache with an empty variable so that DB seeks don't continue
                //to happen
                else {
                    populatePriceFilterCache(cacheKey, EMPTY_CACHE);
                }
            }
        }

        cacheKey = ICacheService.CATEGORY_PRICE_FILTER_CACHE_KEY.replace("X", item.getCategory());
        cacheString = cacheService.getCacheValue(cacheKey);

        //Get the global category price filter for this item's category
        if (cacheString != null && cacheString.length() > 0 && !cacheString.equals(EMPTY_CACHE)) {
            categoryPriceFilter = gson.fromJson(cacheString, PriceFilter.class);
        }
        //Possible cache miss, make sure it actually exists, then repopulate the cache.
        else if (cacheString == null) {
            categoryPriceFilter = getLatestCategoryPriceFilter(PriceFilter.MATERIAL_LEVEL.ALL, item.getCategory(), materialId, finishId, mode);

            if (categoryPriceFilter != null) {
                populatePriceFilterCache(cacheKey, gson.toJson(categoryPriceFilter));
            }
            //If this isn't an actual entry, populate the cache with an empty variable so that DB seeks don't continue
            //to happen
            else {
                populatePriceFilterCache(cacheKey, EMPTY_CACHE);
            }
        }


        cacheKey = ICacheService.ITEM_PRICE_FILTER_CACHE_KEY.replace("X", item.getItemId().toString());
        cacheString = cacheService.getCacheValue(cacheKey);

        if (cacheString != null && cacheString.length() > 0 && !cacheString.equals(EMPTY_CACHE)) {
            itemPriceFilter = gson.fromJson(cacheString, PriceFilter.class);
        } else if (cacheString == null) {
            itemPriceFilter = getLatestItemPriceFilter(PriceFilter.MATERIAL_LEVEL.ALL, materialId, finishId, item.getItemId(), mode);

            if (itemPriceFilter != null) {
                populatePriceFilterCache(cacheKey, gson.toJson(itemPriceFilter));
            }
            //If this isn't an actual entry, populate the cache with an empty variable so that DB seeks don't continue
            //to happen
            else {
                populatePriceFilterCache(cacheKey, EMPTY_CACHE);
            }
        }

        if (globalPriceFilter != null) {
            priceFilterSorter.add(globalPriceFilter);
        }

        if (globalMaterialPriceFilter != null) {
            priceFilterSorter.add(globalMaterialPriceFilter);
        }

        if (categoryPriceFilter != null) {
            priceFilterSorter.add(categoryPriceFilter);
        }

        if (categoryMaterialPriceFilter != null) {
            priceFilterSorter.add(categoryMaterialPriceFilter);
        }

        if (itemPriceFilter != null) {
            priceFilterSorter.add(itemPriceFilter);
        }

        if (itemMaterialPriceFilter != null) {
            priceFilterSorter.add(itemMaterialPriceFilter);
        }

        if (priceFilterSorter.size() > 0) {

            Collections.sort(priceFilterSorter, new Comparator<PriceFilter>() {
                @Override
                public int compare(PriceFilter o1, PriceFilter o2) {
                    return o2.getCreationDate().compareTo(o1.getCreationDate());
                }
            });

            rval = priceFilterSorter.get(0);
        }

        return rval;
    }

    @Override
    public Boolean updateItemStartingPrice(Long itemId, BigDecimal startingPrice)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (itemId == null || startingPrice == null)
                throw new NullPointerException("Arguments cannot be null");

            Item item = getItemAttached(itemId, pm);
            item.setStartingPrice(startingPrice);

            rval = true;
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Integer updateLivePhotoUrl(Long itemId, String livePhotoUrl, Boolean applyToRelatives)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Integer rval = 0;

        try
        {
            if (itemId == null || livePhotoUrl == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            Item item = getItemAttached(itemId, pm);

            if (applyToRelatives) {
                List<Item> items = getItemsAttachedByBaseItemId(item.getBaseItemReference().getItemId(), pm);

                if (items != null && items.size() > 0) {
                    for (Item relative : items) {
                        relative.setLivePhotoUrl(livePhotoUrl);
                    }

                    rval = items.size();
                }
            }
            else {
                item.setLivePhotoUrl(livePhotoUrl);

                rval = 1;
            }

        } catch (Exception e)
        {
            logError(e);
        }
        finally {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateBaseAndChildItemsCustomizerFilename(Long itemId, String filename, Boolean sameHighResFile)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (itemId == null || StringUtils.isNullOrEmpty(filename) || sameHighResFile == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            List<Item> items = getItemsAttachedByBaseItemId(itemId, pm);

            if (items != null && items.size() > 0)
            {
                for (Item item : items)
                {
                    item.setCustomizerFilename(filename);

                    if (sameHighResFile)
                        item.setHighResolutionCustomizerFilename(filename);

                    rval = true;
                }
            }
        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateBaseAndChildItemsCustomizerFilename(Long itemId, String filename)
    {
        return updateBaseAndChildItemsCustomizerFilename(itemId, filename, true);
    }

    @Override
    public Boolean updateBaseAndChildItemsCustomizerHighresFilename(Long itemId, String filename)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (itemId == null || StringUtils.isNullOrEmpty(filename))
                throw new NullPointerException("Arguments cannot be null or empty");

            List<Item> items = getItemsAttachedByBaseItemId(itemId, pm);

            if (items != null && items.size() > 0)
            {
                for (Item item : items)
                {
                    item.setHighResolutionCustomizerFilename(filename);

                    rval = true;
                }
            }
        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public AnonymousItem persistedAnonymousItem(AnonymousItem item)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        AnonymousItem rval = null;

        try
        {
            if (item == null)
                throw new NullPointerException("Argument cannot be null");

            //Get a persistence managed version of the user so that the reference is set properly
            if (item.getAssociatedUser() != null && item.getAssociatedUser().getUserId() != null)
            {
                User user = userAccessor.getUserAttached(item.getAssociatedUser().getUserId(), pm);
                item.setAssociatedUser(user);

                if (item.getBaseItem() != null && item.getBaseItem().getItemId() != null)
                {
                    item.setBaseItem(getItemAttached(item.getBaseItem().getItemId(), pm));
                }

                if (item.getParentItem() != null && item.getParentItem().getItemId() != null)
                {
                    item.setParentItem(getItemAttached(item.getParentItem().getItemId(), pm));
                }
            }

            rval = pm.makePersistent(item);

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public AnonymousItem getAnonymousItem(Long itemId, IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        AnonymousItem rval = null;

        try
        {
            if (itemId == null || mode == null)
                throw new NullPointerException("Arguments cannot be null or empty");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(AnonymousItem.class, "itemId == :itemId");
            List<AnonymousItem> queryResults = (List<AnonymousItem>) query.execute(itemId);

            if (queryResults != null && queryResults.size() > 0)
                rval = queryResults.get(0);
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;

    }

    @Override
    public AnonymousItem getAnonymousItemAttached(Long itemId, PersistenceManager pm)
    {
        AnonymousItem rval = null;

        Query query = pm.newQuery(AnonymousItem.class, "itemId == :itemId");
        List<AnonymousItem> queryResult = (List<AnonymousItem>) query.execute(itemId);

        if (queryResult != null && queryResult.size() > 0)
            rval = queryResult.get(0);

        return rval;
    }

    @Override
    public Boolean updateAnonymousAssociatedUser(Long itemId, Long userId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            AnonymousItem item = getAnonymousItemAttached(itemId, pm);
            User user = userAccessor.getUserAttached(userId, pm);

            item.setAssociatedUser(user);

            rval = true;

        } catch (Exception e)
        {
            logError(e);
        } finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean setShortLink(Long itemId, Long shortLinkId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try {
            if (itemId == null || shortLinkId == null)
                throw new NullPointerException("Arguments cannot be null");

            ShortLink shortLink = shortLinkAccessor.getShortLinkAttached(shortLinkId, pm);
            Item item = getItemAttached(itemId, pm);

            item.setShortLink(shortLink);

            rval = true;

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public ItemImage getItemImageByHash(Integer hash)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        ItemImage rval = null;

        try
        {
            if (hash == null)
                throw new NullPointerException("Argument cannot be null");

            Query query = pm.newQuery(ItemImage.class, "imageHash == :hash");
            List<ItemImage> queryResult = (List<ItemImage>) query.execute(hash);

            if (queryResult != null && queryResult.size() > 0) {
                rval = queryResult.get(0);
            }

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public ItemImage persistItemImage(ItemImage image)
    {
        PersistenceManager pm = pmf.getPersistenceManager();

        try
        {
            if (image == null)
                throw new NullPointerException("Argument cannot be null");

            pm.setDetachAllOnCommit(true);
            pm.makePersistent(image);
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return image;
    }

    @Override
    public ItemImage getItemImageById(Long itemImageId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        ItemImage rval = null;

        try
        {
            if (itemImageId == null)
                throw new NullPointerException("Argument cannot be null");

            Query query = pm.newQuery(ItemImage.class, "imageId == :id");
            List<ItemImage> queryResults = (List<ItemImage>) query.execute(itemImageId);

            if (queryResults != null && queryResults.size() > 0)
                rval = queryResults.get(0);
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public ItemImage updateItemImageMediumUrlPath(Long itemImageId, String urlPath)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        ItemImage rval = null;

        try
        {
            if (itemImageId == null || urlPath == null)
                throw new NullPointerException("Arguments cannot be null");

            rval = getItemImageAttached(itemImageId, pm);

            rval.setMediumImageUrlPath(urlPath);

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public ItemImage updateItemImageSmallUrlPath(Long itemImageId, String urlPath) {
        PersistenceManager pm = pmf.getPersistenceManager();
        ItemImage rval = null;

        try
        {
            if (itemImageId == null || urlPath == null)
                throw new NullPointerException("Arguments cannot be null");

            rval = getItemImageAttached(itemImageId, pm);

            rval.setSmallImageUrlPath(urlPath);

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public ItemImage getItemImageAttached(Long itemImageId, PersistenceManager pm)
    {
        ItemImage rval = null;

        Query query = pm.newQuery(ItemImage.class, "imageId == :id");
        List<ItemImage> queryResult = (List<ItemImage>) query.execute(itemImageId);

        if (queryResult != null && queryResult.size() > 0)
            rval = queryResult.get(0);

        return rval;
    }

    @Override
    public Boolean associateLabel(Long itemId, String itemType, String labelId, IEnums.SEEK_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (itemId == null || labelId == null)
                throw new NullPointerException("Arguments cannot be null");

            configureFetchGroups(pm, mode);

            Query query = pm.newQuery(EasyPostPostageLabel.class, "id == :labelId");
            List<EasyPostPostageLabel> labelList = (List<EasyPostPostageLabel>) query.execute(labelId);
            EasyPostPostageLabel label = labelList.get(0);

            if (itemType.equals("generic")) {
                query = pm.newQuery(GenericItem.class, "genericItemId == :genericItemId");
                List<GenericItem> itemList = (List<GenericItem>) query.execute(itemId);
                GenericItem item = itemList.get(0);
                item.setShippingLabel(label);
            } else if (itemType.equals("cart")) {
                query = pm.newQuery(CartItem.class, "cartItemId == :cartItemId");
                List<CartItem> itemList = (List<CartItem>) query.execute(itemId);
                CartItem item = itemList.get(0);
                item.setShippingLabel(label);
            } else {
                throw new IllegalArgumentException("Item needs to be either a GenericItem (GiftCardItem or SimpleItem) or a CartItem");
            }

            rval = true;
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }


    public void configureFetchGroups(PersistenceManager pm, IEnums.SEEK_MODE mode) {
        if (mode != null && mode != IEnums.SEEK_MODE.QUICK) {
            //Very expensive, only use when you need every single detail about the Item
            if (mode == IEnums.SEEK_MODE.FULL) {
                FetchGroup fetchGroup = pm.getFetchGroup(Item.class, "fullItemFetch");
                List<String> fetchGroupFields = Item.getFullItemFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(GenericItem.class, "fullGenericItemFetch");
                fetchGroupFields = GenericItem.getFullGenericItemFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(CartItem.class, "fullCartItemFetch");
                fetchGroupFields = CartItem.getCartItemFullFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("fullItemFetch");
                fetchPlan.addGroup("fullGenericItemFetch");
                fetchPlan.addGroup("fullCartItemFetch");
                fetchPlan.setMaxFetchDepth(2);

            } else if (mode == IEnums.SEEK_MODE.ITEM_ATTRIBUTES) {
                FetchGroup fetchGroup = pm.getFetchGroup(Item.class, "attributeFetch");
                List<String> fetchGroupFields = Item.getItemWithAttributesFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("attributeFetch");
                fetchPlan.setMaxFetchDepth(2);
            } else if (mode == IEnums.SEEK_MODE.ITEM_FEED) {
                FetchGroup fetchGroup = pm.getFetchGroup(Item.class, "feedFetch");
                List<String> fetchGroupFields = Item.getFeedItemFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("feedFetch");
                fetchPlan.setMaxFetchDepth(2);
            } else if (mode == IEnums.SEEK_MODE.ITEM_CUSTOMIZER) {
                //TODO:  Causes a recursive fetch of parent, might be too much to pull back and slow everything down.
                FetchGroup fetchGroup = pm.getFetchGroup(Item.class, "attributeFetch");
                List<String> fetchGroupFields = Item.getItemWithCustomizerFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("attributeFetch");
                fetchPlan.setMaxFetchDepth(6);
            } else if (mode == IEnums.SEEK_MODE.ITEM_CUSTOMIZER_FTUE)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(Item.class, "attributeFetch");
                List<String> fetchGroupFields = Item.getItemWithAttributesFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("attributeFetch");
                fetchPlan.setMaxFetchDepth(3);
            }
            else if (mode == IEnums.SEEK_MODE.ANONYMOUS_ITEM_FULL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(AnonymousItem.class, "anonymousItemFetch");
                List<String> fetchGroupFields = AnonymousItem.getAnonymousItemFullFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                fetchGroup = pm.getFetchGroup(Item.class, "itemFetch");
                fetchGroupFields = Item.getFullItemFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("anonymousItemFetch");
                fetchPlan.addGroup("itemFetch");
                fetchPlan.setMaxFetchDepth(3);

            }
            else if (mode == IEnums.SEEK_MODE.ITEM_FTUE_REQUEST) {
                FetchGroup fetchGroup = pm.getFetchGroup(Item.class, "prototypeFetch");
                List<String> fetchGroupFields = Item.getItemWithPrototypeSettings();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("prototypeFetch");
                fetchPlan.setMaxFetchDepth(2);
            } else if (mode == IEnums.SEEK_MODE.ITEM_DESIGN_STORY) {
                FetchGroup fetchGroup = pm.getFetchGroup(Item.class, "designStoryFetch");
                List<String> fetchGroupFields = Item.getItemDesignStoryFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("designStoryFetch");
                fetchPlan.setMaxFetchDepth(3);
            } else if (mode == IEnums.SEEK_MODE.ITEM_PRICING) {
                FetchGroup fetchGroup = pm.getFetchGroup(Item.class, "pricingFetch");
                List<String> fetchGroupFields = Item.getItemWithCustomizerAndMarkupFetchGroupFields();
                for (String field : fetchGroupFields) {
                    fetchGroup.addMember(field);
                }

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("pricingFetch");
                fetchPlan.setMaxFetchDepth(3);
            }
        }
    }

    private void populateItemSizes(List<Item> items) {
        if (items != null) {
            for (Item item : items) {
                if (item.getCategory() != null)
                    item.setAvailableSizes(SizeMapper.getAvailableSizesMap(item.getCategory()));
            }
        }
    }

    private void populateItemSizes(Item item) {
        List<Item> container = new ArrayList<>();
        container.add(item);
        populateItemSizes(container);
    }

    private void populatePriceFilterCache(String cacheKey, String json) {
        Map properties = new HashMap<>();
        properties.put(GCacheFactory.EXPIRATION_DELTA, TimeUnit.HOURS.toSeconds(12));
        cacheService.putCache(cacheKey, json, properties);
    }
}
