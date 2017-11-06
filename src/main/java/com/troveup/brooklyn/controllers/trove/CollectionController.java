package com.troveup.brooklyn.controllers.trove;

import com.troveup.brooklyn.model.CollectionItemListResponse;
import com.troveup.brooklyn.model.CollectionResponse;
import com.troveup.brooklyn.model.UserResponse;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.item.interfaces.IItemAccessor;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.item.model.ItemImage;
import com.troveup.brooklyn.orm.materials.model.Finish;
import com.troveup.brooklyn.orm.materials.model.Material;
import com.troveup.brooklyn.orm.user.interfaces.IUserAccessor;
import com.troveup.brooklyn.orm.user.model.Collection;
import com.troveup.brooklyn.orm.user.model.Follow;
import com.troveup.brooklyn.orm.user.model.GroupedItem;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.print.interfaces.IPrintSupplier;
import com.troveup.brooklyn.util.WorkerQueuer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tim on 7/30/15.
 */
@Controller
public class CollectionController extends CommonController
{

    @RequestMapping(value = "/collections", method = RequestMethod.GET)
    public @ResponseBody CollectionResponse getCollections(@RequestParam("userId") final Long userId,
                                                           @RequestParam("pageNumber") final Integer pageNumber,
                                                           @RequestParam("pageLimit") final Long pageLimit,
                                                           @RequestParam(value = "duplicateBuffer", required = false)
                                                               final Long duplicateBuffer,
                                                           HttpServletRequest request)
    {
        CollectionResponse rval = new CollectionResponse();

        Map<String, String[]> params = request.getParameterMap();

        //Grab the currently authenticated user
        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

        Boolean shouldIncludePrivateCollections = false;
        if (user != null && user.getUserId().equals(userId))
            shouldIncludePrivateCollections = true;

        List<Collection> userCollections = userAccessor.getPagedCollectionsByUserId(userId, pageNumber.longValue(),
                pageLimit, duplicateBuffer, shouldIncludePrivateCollections, IEnums.SEEK_MODE.COLLECTION_LIGHT);

        List<Long> collectionsAlreadyOnPage = new ArrayList<>();
        for (String pageCollectionIdObject : params.keySet())
        {
            if (pageCollectionIdObject.contains("objectId"))
                collectionsAlreadyOnPage.add(Long.parseLong(params.get(pageCollectionIdObject)[0]));
        }

        for (Long collectionOnPage : collectionsAlreadyOnPage)
        {
            Collection collection = new Collection();
            collection.setCollectionId(collectionOnPage);

            int searchIndex = userCollections.indexOf(collection);

            if (searchIndex > -1)
                userCollections.remove(searchIndex);
        }

        if (userCollections.size() > pageLimit)
        {
            long sizeDelta = userCollections.size() - pageLimit;

            for (int i = 0; i < sizeDelta; ++i)
            {
                userCollections.remove(userCollections.size() - 1);
            }
        }

        for (Collection collection : userCollections)
        {
            //Figure out if the currently authenticated (requesting) user should be able to follow this collection
            if (user != null) {
                for (Follow follow : collection.getFollowed()) {
                    if (follow.getUserFollower().getUserId().equals(user.getUserId()))
                        collection.setIsFollowed(true);
                    else
                        collection.setIsFollowed(false);
                }
            }
            else
            {
                collection.setIsFollowed(false);
            }

            //If the user is the owner of this collection, mark it as such
            if (user != null && collection.getOwner().getUserId().equals(user.getUserId()))
                collection.setIsOwner(true);
            else
                collection.setIsOwner(false);

            collection.setFollowedCount((long) collection.getFollowed().size());
        }

        rval.setCollectionList(userCollections);

        return rval;
    }

    @RequestMapping(value = "/createcollection", method = RequestMethod.POST)
    public @ResponseBody CollectionResponse createCollection(@RequestParam("collectionName") final String collectionName,
                                                             @RequestParam("collectionDescription") final String collectionDescription,
                                                             @RequestParam("isPrivate") final Boolean isPrivate,
                                                           HttpServletRequest request)
    {
        CollectionResponse rval = new CollectionResponse();

        Map<String, String[]> params = request.getParameterMap();

        //Grab the currently authenticated user
        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

        if (user != null)
        {
            Long newCollectionId = userAccessor.addCollectionToUser(user.getUserId(),
                    new Collection(collectionName, collectionDescription, isPrivate));

            if (newCollectionId != null)
            {
                Collection returnCollection = new Collection();
                returnCollection.setCollectionId(newCollectionId);
                returnCollection.setCollectionName(collectionName);
                returnCollection.setCollectionDescription(collectionDescription);
                returnCollection.setIsPrivate(isPrivate);
                returnCollection.setIsOwner(true);
                rval.getCollectionList().add(returnCollection);
            }

        }

        return rval;
    }

    @RequestMapping(value = "/updatecollection", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> updateCollection(@RequestParam("collectionId") final Long collectionId,
                                                             @RequestParam("collectionName") final String collectionName,
                                                             @RequestParam("collectionDescription") final String collectionDescription,
                                                             @RequestParam("isPrivate") final Boolean isPrivate,
                                                             HttpServletRequest request)
    {
        ResponseEntity<String> rval;

        //Grab the currently authenticated user
        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

        if (user != null)
        {
            if (userAccessor.updateCollection(user.getUserId(), collectionId, collectionName, collectionDescription,
                    isPrivate))
                rval = new ResponseEntity<String>(HttpStatus.OK);
            else
                rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);
        }
        else
            rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);

        return rval;
    }

    @RequestMapping(value = "/deletecollection", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> deleteCollection(@RequestParam("collectionId") final Long collectionId,
                                                                 HttpServletRequest request)
    {
        ResponseEntity<String> rval;

        //Grab the currently authenticated user
        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

        if (user != null)
        {
            if (userAccessor.deleteCollection(user.getUserId(), collectionId))
                rval = new ResponseEntity<String>(HttpStatus.OK);
            else
                rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);
        }
        else
            rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);

        return rval;
    }

    @RequestMapping(value = "/addcollectionitem", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> addCollectionItem(@RequestParam("collectionId") final Long collectionId,
                                                                  @RequestParam("itemId") final Long itemId,
                                                                 HttpServletRequest request)
    {
        ResponseEntity<String> rval;

        //Grab the currently authenticated user
        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

        if (user != null)
        {
            if (userAccessor.addItemToCollection(user.getUserId(), itemId, collectionId))
                rval = new ResponseEntity<String>(HttpStatus.OK);
            else
                rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);
        }
        else
            rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);

        return rval;
    }

    @RequestMapping(value = "/removecollectionitem", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> removeCollectionItem(@RequestParam("collectionId") final Long collectionId,
                                                                  @RequestParam("itemId") final Long itemId,
                                                                  HttpServletRequest request)
    {
        ResponseEntity<String> rval;

        //Grab the currently authenticated user
        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

        if (user != null)
        {
            if (userAccessor.removeItemFromCollection(user.getUserId(), itemId, collectionId))
                rval = new ResponseEntity<String>(HttpStatus.OK);
            else
                rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);
        }
        else
            rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);

        return rval;
    }

    @RequestMapping(value = "/collectionitems", method = RequestMethod.GET)
    public @ResponseBody
    CollectionItemListResponse getCollectionItems(@RequestParam(value = "collectionId")
                                                                       final Long collectionId,
                                                                   @RequestParam("pageNumber") final Long pageNumber,
                                                                   @RequestParam("pageLimit") final Long pageLimit,
                                                                   @RequestParam(value = "duplicateBuffer", required = false)
                                                                       final Long duplicateBuffer,
                                                                    HttpServletRequest request)
    {
        CollectionItemListResponse rval = new CollectionItemListResponse();

        Map<String, String[]> params = request.getParameterMap();

        List<Material> materials = printSupplier.getSupplierMaterials();

        //Grab the currently authenticated user
        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

        Boolean includePrivateItems = false;

        if (user != null && userAccessor.doesUserOwnCollection(collectionId, user.getUserId()))
            includePrivateItems = true;

        List<Item> collectionItems = itemAccessor.getItemsByCollectionId(collectionId, pageNumber, pageLimit,
                duplicateBuffer, IEnums.SEEK_MODE.ITEM_FEED, includePrivateItems);

        List<Long> itemsAlreadyOnPage = new ArrayList<>();
        for (String pageCollectionIdObject : params.keySet())
        {
            if (pageCollectionIdObject.contains("objectId"))
                itemsAlreadyOnPage.add(Long.parseLong(params.get(pageCollectionIdObject)[0]));
        }

        for (Long itemOnPage : itemsAlreadyOnPage)
        {
            Item item = new Item();
            item.setItemId(itemOnPage);

            int searchIndex = collectionItems.indexOf(item);

            if (searchIndex > -1)
                collectionItems.remove(searchIndex);
        }

        if (collectionItems.size() > pageLimit)
        {
            long sizeDelta = collectionItems.size() - pageLimit;

            for (int i = 0; i < sizeDelta; ++i)
            {
                collectionItems.remove(collectionItems.size() - 1);
            }
        }

        for (Item item : collectionItems)
        {
            setDefaultItemImage(item, materials);
        }

        rval.setCollectionItems(collectionItems);

        return rval;
    }

    @RequestMapping(value = "/followcollection", method = RequestMethod.POST)
    public
    ResponseEntity<String> followCollection (@RequestParam(value = "collectionId")
                                                  final Long collectionId,
                                                  HttpServletRequest request)
    {
        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);

        //Grab the currently authenticated user
        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

        if (user != null)
        {
            if (userAccessor.followCollection(user.getUserId(), collectionId))
                rval = new ResponseEntity<String>(HttpStatus.OK);
        }

        return rval;
    }

    @RequestMapping(value = "/unfollowcollection", method = RequestMethod.POST)
    public
    ResponseEntity<String> unfollowCollection (@RequestParam(value = "collectionId")
                                             final Long collectionId,
                                             HttpServletRequest request)
    {
        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);

        //Grab the currently authenticated user
        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

        if (user != null)
        {
            if (userAccessor.unfollowCollection(user.getUserId(), collectionId))
                rval = new ResponseEntity<String>(HttpStatus.OK);
        }

        return rval;
    }

    @RequestMapping(value = "/setitemdefaultcollection", method = RequestMethod.POST)
    public
    ResponseEntity<String> setItemDefaultCollection (@RequestParam(value = "collectionId")
                                               final Long collectionId,
                                               @RequestParam(value = "itemId") final Long itemId,
                                               HttpServletRequest request)
    {
        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);

        //Grab the currently authenticated user
        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

        if (user != null)
        {
            if (itemAccessor.setItemDefaultCollection(itemId, collectionId, user.getUserId()))
                rval = new ResponseEntity<String>(HttpStatus.OK);
        }

        return rval;
    }

    @RequestMapping(value = "/clearitemdefaultcollection", method = RequestMethod.POST)
    public
    ResponseEntity<String> clearItemDefaultCollection (@RequestParam(value = "itemId") final Long itemId,
                                                     HttpServletRequest request)
    {
        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);

        //Grab the currently authenticated user
        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

        if (user != null)
        {
            if (itemAccessor.clearItemDefaultCollection(itemId, user.getUserId()))
                rval = new ResponseEntity<String>(HttpStatus.OK);
        }

        return rval;
    }

    @RequestMapping(value = "/collectionfollowers", method = RequestMethod.GET)
    public @ResponseBody
    UserResponse getCollectionFollowers(@RequestParam(value = "collectionId")
                                                  final Long collectionId,
                                                  @RequestParam("pageNumber") final Integer pageNumber,
                                                  @RequestParam("pageLimit") final Long pageLimit,
                                                  @RequestParam(value = "duplicateBuffer", required = false)
                                                  final Long duplicateBuffer,
                                                  HttpServletRequest request)
    {
        UserResponse response = new UserResponse();
        List<Material> materials = printSupplier.getSupplierMaterials();

        Map<String, String[]> params = request.getParameterMap();

        User authUser = getQuickUserDetailsPII(IEnums.SEEK_MODE.USER_FOLLOWED_USERS);

        List<User> users = userAccessor.getPagedFollowersOfCollection(collectionId, pageNumber, pageLimit, duplicateBuffer,
                IEnums.SEEK_MODE.USER_FOLLOWS);

        if (users == null)
            users = new ArrayList<>();

        List<Long> usersAlreadyOnPage = new ArrayList<>();
        for (String pageCollectionIdObject : params.keySet())
        {
            if (pageCollectionIdObject.contains("objectId"))
                usersAlreadyOnPage.add(Long.parseLong(params.get(pageCollectionIdObject)[0]));
        }

        for (Long userOnPage : usersAlreadyOnPage)
        {
            User user = new User();
            user.setUserId(userOnPage);

            int searchIndex = users.indexOf(user);

            if (searchIndex > -1)
                users.remove(searchIndex);
        }

        if (users.size() > pageLimit)
        {
            long sizeDelta = users.size() - pageLimit;

            for (int i = 0; i < sizeDelta; ++i)
            {
                users.remove(users.size() - 1);
            }
        }

        //Mark all of the users that have been followed so that the front end can indicate this appropriately
        if (authUser != null && authUser.getFollowedUsers() != null) {
            for (User user : users) {
                for (Follow userFollow : authUser.getFollowedUsers()) {
                    if (userFollow.getUserFollower().getUserId().equals(user.getUserId()))
                        user.setFollowed(true);
                }
            }
        }

        setDefaultUserItemImages(users, materials);

        response.setUserList(users);

        return response;
    }




    //Test methods for testing counter incrementers
    @RequestMapping(value = "/testfiretroveincrement", method = RequestMethod.POST)
    public
    ResponseEntity<String> testFireTroveIncrement (@RequestParam(value = "itemId")
                                               final Long itemId,
                                               HttpServletRequest request)
    {
        //Update any collections that might have
        Map<String, String> queueWorkMap = new HashMap<>();
        queueWorkMap.put("itemId", itemId.toString());
        WorkerQueuer.queueWorkForWorker(queueWorkMap, WorkerController.COLLECTION_TROVE_COUNT_INCREMENT,
                WorkerController.WORKER_QUEUE_OBJECT_UPDATES);

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/testfireremakeincrement", method = RequestMethod.POST)
    public
    ResponseEntity<String> testFireRemakeIncrement (@RequestParam(value = "itemId")
                                                   final Long itemId,
                                                   HttpServletRequest request)
    {
        //Update any collections that might have
        Map<String, String> queueWorkMap = new HashMap<>();
        queueWorkMap.put("itemId", itemId.toString());
        WorkerQueuer.queueWorkForWorker(queueWorkMap, WorkerController.COLLECTION_REMAKE_COUNT_INCREMENT,
                WorkerController.WORKER_QUEUE_OBJECT_UPDATES);

        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
