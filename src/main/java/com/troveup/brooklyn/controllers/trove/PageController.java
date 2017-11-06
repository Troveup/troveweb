package com.troveup.brooklyn.controllers.trove;

import com.troveup.brooklyn.model.ItemResponse;
import com.troveup.brooklyn.model.OrderResponse;
import com.troveup.brooklyn.model.UserResponse;
import com.troveup.brooklyn.orm.cart.model.CartItem;
import com.troveup.brooklyn.orm.cart.model.GenericItem;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.item.interfaces.IItemAccessor;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.item.model.ItemCustomization;
import com.troveup.brooklyn.orm.item.model.ItemImage;
import com.troveup.brooklyn.orm.materials.model.Finish;
import com.troveup.brooklyn.orm.materials.model.Material;
import com.troveup.brooklyn.orm.order.interfaces.IOrderAccessor;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.order.model.Status;
import com.troveup.brooklyn.orm.user.interfaces.IUserAccessor;
import com.troveup.brooklyn.orm.user.model.Follow;
import com.troveup.brooklyn.orm.user.model.GroupedItem;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.print.interfaces.IPrintSupplier;
import com.troveup.brooklyn.util.MoneyUtil;
import com.troveup.brooklyn.util.SizeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tim on 8/4/15.
 */
@Controller
public class PageController extends CommonController {
    private static String CATEGORY_TYPE_FEATURED = "FEATURED";

    @RequestMapping(value = "/pagefeed", method = RequestMethod.GET)
    public
    @ResponseBody
    ItemResponse getPageFeed(@RequestParam("pageNumber") final Integer pageNumber,
                             @RequestParam("pageLimit") final Long pageLimit,
                             @RequestParam(value = "duplicateBuffer", required = false)
                             final Long duplicateBuffer,
                             HttpServletRequest request) {
        List<Material> materials = printSupplier.getSupplierMaterials();
        ItemResponse rval = new ItemResponse();
        Map<String, String[]> params = request.getParameterMap();
        //Grab the currently authenticated user
        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

        Long userId;
        if (user != null)
            userId = user.getUserId();
        else
            userId = -1l;

        List<Item> items = itemAccessor.getPagedFeedItems(userId, pageNumber, pageLimit, duplicateBuffer,
                IEnums.SEEK_MODE.ITEM_FEED);

        List<Long> itemsAlreadyOnPage = new ArrayList<>();
        for (String pageCollectionIdObject : params.keySet()) {
            if (pageCollectionIdObject.contains("objectId"))
                itemsAlreadyOnPage.add(Long.parseLong(params.get(pageCollectionIdObject)[0]));
        }

        for (Long itemOnPage : itemsAlreadyOnPage) {
            Item item = new Item();
            item.setItemId(itemOnPage);

            int searchIndex = items.indexOf(item);

            if (searchIndex > -1)
                items.remove(searchIndex);
        }

        if (items.size() > pageLimit) {
            long sizeDelta = items.size() - pageLimit;

            for (int i = 0; i < sizeDelta; ++i) {
                items.remove(items.size() - 1);
            }
        }

        performItemMaintenance(items, materials);

        rval.setItemList(items);

        return rval;
    }

    @RequestMapping(value = "/pagecategory", method = RequestMethod.GET)
    public
    @ResponseBody
    ItemResponse getCategory(@RequestParam("pageNumber") final Integer pageNumber,
                             @RequestParam("pageLimit") final Long pageLimit,
                             @RequestParam(value = "duplicateBuffer", required = false)
                             final Long duplicateBuffer,
                             @RequestParam("category") final String category,
                             HttpServletRequest request) {
        //TODO:  Get base items by category
        List<Item> items;
        ItemResponse rval = new ItemResponse();

        List<Material> materials = printSupplier.getSupplierMaterials();

        Map<String, String[]> params = request.getParameterMap();

        if (category.equals(CATEGORY_TYPE_FEATURED))
            items = itemAccessor.getFeaturedItems(pageNumber, pageLimit, duplicateBuffer, IEnums.SEEK_MODE.ITEM_FEED);
        else
            items = itemAccessor.getBaseItemsByCategory(category.toUpperCase(), pageNumber, pageLimit,
                    duplicateBuffer, IEnums.SEEK_MODE.ITEM_FEED);

        List<Long> itemsAlreadyOnPage = new ArrayList<>();
        for (String pageCollectionIdObject : params.keySet()) {
            if (pageCollectionIdObject.contains("objectId"))
                itemsAlreadyOnPage.add(Long.parseLong(params.get(pageCollectionIdObject)[0]));
        }

        for (Long itemOnPage : itemsAlreadyOnPage) {
            Item item = new Item();
            item.setItemId(itemOnPage);

            int searchIndex = items.indexOf(item);

            if (searchIndex > -1)
                items.remove(searchIndex);
        }

        if (items.size() > pageLimit) {
            long sizeDelta = items.size() - pageLimit;

            for (int i = 0; i < sizeDelta; ++i) {
                items.remove(items.size() - 1);
            }
        }

        performItemMaintenance(items, materials);

        rval.setItemList(items);

        return rval;
    }

    @RequestMapping(value = "/troveitems", method = RequestMethod.GET)
    public
    @ResponseBody
    ItemResponse getTroveItems(@RequestParam("userId") final Long userId,
                               @RequestParam("pageNumber") final Integer pageNumber,
                               @RequestParam("pageLimit") final Long pageLimit,
                               @RequestParam(value = "duplicateBuffer", required = false)
                               final Long duplicateBuffer,
                               HttpServletRequest request) {
        //TODO:  Get base items by category
        List<Item> items;
        ItemResponse rval = new ItemResponse();
        List<Material> materials = printSupplier.getSupplierMaterials();

        //Grab the currently authenticated user
        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

        Map<String, String[]> params = request.getParameterMap();

        Boolean shouldIncludePrivate = false;
        if (user != null && user.getUserId().equals(userId))
            shouldIncludePrivate = true;

        items = itemAccessor.getPagedTroveItems(userId, pageNumber, pageLimit, duplicateBuffer,
                IEnums.SEEK_MODE.ITEM_FEED, shouldIncludePrivate);

        if (items != null && items.size() > 0) {
            List<Long> itemsAlreadyOnPage = new ArrayList<>();
            for (String pageCollectionIdObject : params.keySet()) {
                if (pageCollectionIdObject.contains("objectId"))
                    itemsAlreadyOnPage.add(Long.parseLong(params.get(pageCollectionIdObject)[0]));
            }

            for (Long itemOnPage : itemsAlreadyOnPage) {
                Item item = new Item();
                item.setItemId(itemOnPage);

                int searchIndex = items.indexOf(item);

                if (searchIndex > -1)
                    items.remove(searchIndex);
            }

            if (items.size() > pageLimit) {
                long sizeDelta = items.size() - pageLimit;

                for (int i = 0; i < sizeDelta; ++i) {
                    items.remove(items.size() - 1);
                }
            }

            performItemMaintenance(items, materials);

            rval.setItemList(items);
        } else {
            rval.setItemList(new ArrayList<Item>());
        }

        return rval;
    }

    @RequestMapping(value = "/followers", method = RequestMethod.GET)
    public
    @ResponseBody
    UserResponse getFollowers(@RequestParam("userId") final Long userId,
                              @RequestParam("pageNumber") final Integer pageNumber,
                              @RequestParam("pageLimit") final Long pageLimit,
                              @RequestParam(value = "duplicateBuffer", required = false)
                              final Long duplicateBuffer,
                              HttpServletRequest request) {
        UserResponse response = new UserResponse();

        Map<String, String[]> params = request.getParameterMap();

        List<Material> materials = printSupplier.getSupplierMaterials();

        User authUser = getQuickUserDetailsPII(IEnums.SEEK_MODE.USER_FOLLOWED_USERS);

        List<User> users = userAccessor.getPagedFollowersByUserId(userId, pageNumber, pageLimit, duplicateBuffer,
                IEnums.SEEK_MODE.USER_FOLLOWS);

        if (users == null)
            users = new ArrayList<>();

        List<Long> usersAlreadyOnPage = new ArrayList<>();
        for (String pageCollectionIdObject : params.keySet()) {
            if (pageCollectionIdObject.contains("objectId"))
                usersAlreadyOnPage.add(Long.parseLong(params.get(pageCollectionIdObject)[0]));
        }

        for (Long userOnPage : usersAlreadyOnPage) {
            User user = new User();
            user.setUserId(userOnPage);

            int searchIndex = users.indexOf(user);

            if (searchIndex > -1)
                users.remove(searchIndex);
        }

        if (users.size() > pageLimit) {
            long sizeDelta = users.size() - pageLimit;

            for (int i = 0; i < sizeDelta; ++i) {
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

    @RequestMapping(value = "/followed", method = RequestMethod.GET)
    public
    @ResponseBody
    UserResponse getFollowed(@RequestParam("userId") final Long userId,
                             @RequestParam("pageNumber") final Integer pageNumber,
                             @RequestParam("pageLimit") final Long pageLimit,
                             @RequestParam(value = "duplicateBuffer", required = false)
                             final Long duplicateBuffer,
                             HttpServletRequest request) {
        UserResponse response = new UserResponse();

        Map<String, String[]> params = request.getParameterMap();

        List<Material> materials = printSupplier.getSupplierMaterials();

        User authUser = getQuickUserDetailsPII(IEnums.SEEK_MODE.USER_FOLLOWED_USERS);

        List<User> users = userAccessor.getPagedFollowedByUserId(userId, pageNumber, pageLimit, duplicateBuffer,
                IEnums.SEEK_MODE.USER_FOLLOWS);

        if (users == null)
            users = new ArrayList<>();

        List<Long> usersAlreadyOnPage = new ArrayList<>();
        for (String pageCollectionIdObject : params.keySet()) {
            if (pageCollectionIdObject.contains("objectId"))
                usersAlreadyOnPage.add(Long.parseLong(params.get(pageCollectionIdObject)[0]));
        }

        for (Long userOnPage : usersAlreadyOnPage) {
            User user = new User();
            user.setUserId(userOnPage);

            int searchIndex = users.indexOf(user);

            if (searchIndex > -1)
                users.remove(searchIndex);
        }

        if (users.size() > pageLimit) {
            long sizeDelta = users.size() - pageLimit;

            for (int i = 0; i < sizeDelta; ++i) {
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

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public
    @ResponseBody
    OrderResponse getOrders(@RequestParam("orderStatus") final String orderStatus,
                            @RequestParam("pageNumber") final Integer pageNumber,
                            @RequestParam("pageLimit") final Long pageLimit,
                            @RequestParam(value = "duplicateBuffer", required = false)
                            final Long duplicateBuffer,
                            HttpServletRequest request) {
        OrderResponse rval = new OrderResponse(false, "Oops!  Something went wrong.  Please try that again.");

        try {
            Map<String, String[]> params = request.getParameterMap();

            List<Material> materials = printSupplier.getSupplierMaterials();

            //Grab the currently authenticated user
            User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

            List<Order.ORDER_STATUS> orderStatuses = new ArrayList<>();
            //Only get orders for the currently authenticated user
            //Grab all of the closed orders, whether settling or settled
            if (orderStatus.toUpperCase().equals("CLOSED")) {
                orderStatuses.add(Order.ORDER_STATUS.CLOSED);
                orderStatuses.add(Order.ORDER_STATUS.SETTLING);
                orderStatuses.add(Order.ORDER_STATUS.SETTLED);
                orderStatuses.add(Order.ORDER_STATUS.FAILED_TO_SETTLE);
            } else if (orderStatus.toUpperCase().equals("OPEN")) {
                orderStatuses.add(Order.ORDER_STATUS.OPEN);
            } else {
                orderStatuses = Arrays.asList(Order.ORDER_STATUS.values());
            }

            List<Order> orders = orderAccessor.getPagedUserOrders(user.getUserId(), orderStatuses,
                    pageNumber, pageLimit, duplicateBuffer == null ? 0l : duplicateBuffer, IEnums.SEEK_MODE.ORDER_DETAILED);

            if (orders == null)
                orders = new ArrayList<>();

            //Once grabbed, populate the orderItems that are still outstanding

            for (Order order : orders) {
                for (CartItem item : order.getOrderItems()) {
                    item.setCanCustomizeAgain(false);

                    if (item.getPrototypeRequested() && item.getCartItemStatus() ==
                            CartItem.CART_ITEM_STATUS.PENDING_USER_DECISION) {
                        if (item.getCustomizationIteration() == null) {
                            item.setCustomizationIteration(1);
                        }

                        if (item.getCustomizationIteration() < item.getMaxCustomizations()) {
                            item.setCanCustomizeAgain(true);
                        }
                    }

                    //All of the statuses that cannot be cancelled
                    if (CartItem.getStatusesThatCannotBeCancelled().contains(item.getCartItemStatus())) {
                        item.setCancelable(false);
                    } else {
                        item.setCancelable(true);
                    }

                    item.setMaterialName(Material.getMaterialNameByMaterialId(item.getMaterialId(), materials));
                    item.setFinishName(Finish.getFinishNameByFinishId(item.getFinishId(),
                            Material.getMaterialByMaterialId(item.getMaterialId(), materials).getFinishList()));

                    //Sets the size in the convenience field for the front end by using the getSize convenience method
                    //that goes through the item's internal list of customizations and picks out the size based on
                    //iteration
                    item.setSize(SizeMapper.getStandardizedSize(item.getSize(item.getCustomizationIteration())));
                    item.setRemainingCustomizations(item.getMaxCustomizations() - item.getCustomizationIteration());
                    item.convertCartItemStatusToUserFriendlyStatus();

                    item.setOrdersPageStatusTitleAndDescription(CartItem.getTitleAndDescriptionFromOrderStatus(item.getCartItemStatus()));
                    item.setActualPrice(MoneyUtil.toProperScale(item.getActualPrice(), null));
                }

                for (GenericItem item : order.getGenericItemsList())
                {
                    if (item.getItemStatus() != null) {
                        item.setUserFriendlyItemStatus(GenericItem.getUserFriendlyStatusFromEnumGenericItemStatus(item.getItemStatus()));
                    }
                }

                Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("AMERICA/NEW_YORK"));
                calendar.setTime(order.getOrderDate());
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy hh:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("AMERICA/NEW_YORK"));
                order.setUserFriendlyOrderDate(sdf.format(calendar.getTime()));

                order.setGrandTotal(MoneyUtil.toProperScale(order.getGrandTotal(), null));
            }


            List<Long> ordersAlreadyOnPage = new ArrayList<>();
            for (String pageCollectionIdObject : params.keySet()) {
                if (pageCollectionIdObject.contains("objectId"))
                    ordersAlreadyOnPage.add(Long.parseLong(params.get(pageCollectionIdObject)[0]));
            }

            for (Long orderOnPage : ordersAlreadyOnPage) {
                Order order = new Order();
                order.setOrderId(orderOnPage);

                int searchIndex = orders.indexOf(order);

                if (searchIndex > -1)
                    orders.remove(searchIndex);
            }

            if (orders.size() > pageLimit) {
                long sizeDelta = orders.size() - pageLimit;

                for (int i = 0; i < sizeDelta; ++i) {
                    orders.remove(orders.size() - 1);
                }
            }

            rval.setOrders(orders);
            rval.setIsSuccess(true);
            rval.setErrorMessage("");

        } catch (Exception e)
        {
            logError(e);
        }


        return rval;
    }

    private List<Item> performItemMaintenance(List<Item> items, List<Material> materials) {
        for (Item item : items) {
            item.setAvailableSizes(SizeMapper.getAvailableSizesMap(item.getCategory()));

            setDefaultItemImage(item, materials);

            item.performCategoryConfigurations();

            checkAndUpdateItemStartingPrice(item);
            item.setStartingPrice(MoneyUtil.toProperScale(item.getStartingPrice(), null));
        }

        return items;
    }


}
