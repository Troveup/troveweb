package com.troveup.brooklyn.controllers.trove;

import com.troveup.brooklyn.model.GenericAjaxResponse;
import com.troveup.brooklyn.model.OrderResponse;
import com.troveup.brooklyn.model.ShippingLabelResponse;
import com.troveup.brooklyn.orm.cart.model.CartItem;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.order.model.EasyPostPostageLabel;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.simpleitem.model.*;
import com.troveup.brooklyn.orm.user.model.Address;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.cdn.gcs.GoogleCloudStorage;
import com.troveup.brooklyn.sdk.mail.model.TroveBoxDimensions;
import com.troveup.brooklyn.util.DateUtils;
import com.troveup.brooklyn.util.MoneyUtil;
import com.troveup.brooklyn.util.SizeMapper;
import com.troveup.brooklyn.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 12/14/15.
 */
@Controller
@RequestMapping(value = "/admin/ajax/")
public class AdminAjaxController extends CommonController {

    @RequestMapping(value = "/ordersall", method = RequestMethod.GET)
    public
    @ResponseBody
    OrderResponse adminQueryOrdersAll(@RequestParam(value = "pagelimit") final Long pageLimit,
                                      @RequestParam(value = "pagenumber") final Integer pageNumber) {
        OrderResponse rval = new OrderResponse();

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            rval.setOrders(orderAccessor.getOrdersWithPurchaserAll(pageNumber, pageLimit, IEnums.SEEK_MODE.ORDER_LINE_ITEMS));

            rval.setOrders(preProcessOrders(rval.getOrders()));
        }

        return rval;
    }

    @RequestMapping(value = "/ordersbystatus", method = {RequestMethod.GET})
    public
    @ResponseBody
    OrderResponse adminQueryOrdersByStatus(@RequestParam(value = "status") final String status,
                                           @RequestParam(value = "pagelimit") final Long pageLimit,
                                           @RequestParam(value = "pagenumber") final Integer pageNumber) {
        OrderResponse rval = new OrderResponse();

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {

            rval.setOrders(orderAccessor.getOrdersWithPurchaserByStatus(Order.ORDER_STATUS.valueOf(status), pageNumber,
                    pageLimit, IEnums.SEEK_MODE.ORDER_LINE_ITEMS));

            rval.setOrders(preProcessOrders(rval.getOrders()));
        }

        return rval;
    }

    @RequestMapping(value = "/ordersbydaterange", method = {RequestMethod.GET})
    public
    @ResponseBody
    OrderResponse adminQueryOrdersByDateRange(@RequestParam(value = "begindate") final String beginDate,
                                              @RequestParam(value = "enddate") final String endDate,
                                              @RequestParam(value = "pagelimit") final Long pageLimit,
                                              @RequestParam(value = "pagenumber") final Integer pageNumber) {
        OrderResponse rval = new OrderResponse();

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            try {
                Date queryEndDate = DateUtils.convertCommonHtmlCalendarDateToDateObject(endDate);
                Date queryBeginDate = DateUtils.convertCommonHtmlCalendarDateToDateObject(beginDate);
                rval.setOrders(orderAccessor.getOrdersWithPurchaserByDateRange(queryBeginDate, queryEndDate, pageNumber,
                        pageLimit, IEnums.SEEK_MODE.ORDER_LINE_ITEMS));

                rval.setOrders(preProcessOrders(rval.getOrders()));

            } catch (ParseException e) {
                logError(e);
            }
        }

        return rval;
    }

    @RequestMapping(value = "/ordersbyamountrange", method = {RequestMethod.GET})
    public
    @ResponseBody
    OrderResponse adminQueryOrdersByAmountRange(@RequestParam(value = "beginamount") final Float beginAmount,
                                                @RequestParam(value = "endamount") final Float endAmount,
                                                @RequestParam(value = "pagelimit") final Long pageLimit,
                                                @RequestParam(value = "pagenumber") final Integer pageNumber) {
        OrderResponse rval = new OrderResponse();

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            rval.setOrders(orderAccessor.getOrdersWithPurchaserByAmountRange(MoneyUtil.floatToBigDecimal(beginAmount, null),
                    MoneyUtil.floatToBigDecimal(endAmount, null), pageNumber, pageLimit, IEnums.SEEK_MODE.ORDER_LINE_ITEMS));

            rval.setOrders(preProcessOrders(rval.getOrders()));
        }

        return rval;
    }

    @RequestMapping(value = "ordersbycartitemstatus", method = {RequestMethod.GET})
    public
    @ResponseBody
    OrderResponse adminQueryOrderByCartItemStatus(@RequestParam(value = "status") final String status,
                                                  @RequestParam(value = "pagelimit") final Long pageLimit,
                                                  @RequestParam(value = "pagenumber") final Integer pageNumber) {
        OrderResponse rval = new OrderResponse();

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            rval.setOrders(orderAccessor.getOrdersWithPurchaserThatContainCartItemStatus(
                    CartItem.CART_ITEM_STATUS.valueOf(status), pageNumber, pageLimit, IEnums.SEEK_MODE.ORDER_LINE_ITEMS));

            rval.setOrders(preProcessOrders(rval.getOrders()));
        }

        return rval;
    }

    @RequestMapping(value = "ordersbycartitemcategory", method = {RequestMethod.GET})
    public
    @ResponseBody
    OrderResponse adminQueryOrderByCartItemCategory(@RequestParam(value = "category") final String category,
                                                    @RequestParam(value = "pagelimit") final Long pageLimit,
                                                    @RequestParam(value = "pagenumber") final Integer pageNumber) {
        OrderResponse rval = new OrderResponse();

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {

            if (SizeMapper.getAvailableCategories().contains(category)) {
                rval.setOrders(orderAccessor.getOrdersWithPurchaserThatContainCartItemCategory(category, pageNumber, pageLimit, IEnums.SEEK_MODE.ORDER_LINE_ITEMS));
            } else {
                rval.setOrders(orderAccessor.getOrdersWithPurchaserThatContainGenericItemCategory(category, pageNumber, pageLimit, IEnums.SEEK_MODE.ORDER_LINE_ITEMS));
            }

            rval.setOrders(preProcessOrders(rval.getOrders()));
        }

        return rval;
    }

    @RequestMapping(value = "ordersearchbyordernumber", method = {RequestMethod.GET})
    public
    @ResponseBody
    OrderResponse adminQueryOrderByTroveOrderNumber(@RequestParam(value = "ordernumber") final String ordernumber) {
        OrderResponse rval = new OrderResponse();

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            rval.setOrders(new ArrayList<Order>());
            rval.getOrders().add(orderAccessor.getOrderWithPurchaserByOrderNumber(ordernumber, IEnums.SEEK_MODE.ORDER_LINE_ITEMS));

            rval.setOrders(preProcessOrders(rval.getOrders()));
        }

        return rval;
    }

    @RequestMapping(value = "ordersearchbyorderid", method = RequestMethod.GET)
    public
    @ResponseBody
    OrderResponse adminQueryOrderByOrderId(@RequestParam(value = "orderid") final Long orderId) {
        OrderResponse rval = new OrderResponse();

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            rval.setOrders(new ArrayList<Order>());
            rval.getOrders().add(orderAccessor.getOrderWithPurchaserByOrderId(orderId, IEnums.SEEK_MODE.ORDER_LINE_ITEMS));

            rval.setOrders(preProcessOrders(rval.getOrders()));

        }

        return rval;
    }

    @RequestMapping(value = "ordersearchbyuserid", method = RequestMethod.GET)
    public
    @ResponseBody
    OrderResponse adminQueryOrderByUserId(@RequestParam(value = "userid") final Long userId,
                                          @RequestParam(value = "pagelimit") final Long pageLimit,
                                          @RequestParam(value = "pagenumber") final Integer pageNumber) {
        OrderResponse rval = new OrderResponse();

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            rval.setOrders(orderAccessor.getOrdersWithPurchaserByUserId(userId, pageNumber, pageLimit, IEnums.SEEK_MODE.ORDER_LINE_ITEMS));

            rval.setOrders(preProcessOrders(rval.getOrders()));
        }

        return rval;
    }

    @RequestMapping(value = "ordersearchbyusername", method = RequestMethod.GET)
    public
    @ResponseBody
    OrderResponse adminQueryOrdersByUsername(@RequestParam(value = "username") final String username,
                                             @RequestParam(value = "pagelimit") final Long pageLimit,
                                             @RequestParam(value = "pagenumber") final Integer pageNumber) {
        OrderResponse rval = new OrderResponse();

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {

            rval.setOrders(orderAccessor.getOrdersWithPurchaserByUsername(username, pageNumber, pageLimit, IEnums.SEEK_MODE.ORDER_LINE_ITEMS));

            rval.setOrders(preProcessOrders(rval.getOrders()));

        }

        return rval;
    }

    @RequestMapping(value = "ordersearchbyemail", method = RequestMethod.GET)
    public
    @ResponseBody
    OrderResponse adminQueryOrdersByEmail(@RequestParam(value = "email") final String email,
                                          @RequestParam(value = "pagelimit") final Long pageLimit,
                                          @RequestParam(value = "pagenumber") final Integer pageNumber) {
        OrderResponse rval = new OrderResponse();

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {

            rval.setOrders(orderAccessor.getOrdersWithPurchaserByAccountEmail(email, pageNumber, pageLimit, IEnums.SEEK_MODE.ORDER_LINE_ITEMS));

            rval.setOrders(preProcessOrders(rval.getOrders()));
        }

        return rval;
    }


    @RequestMapping(value = "ordersearchbyname", method = RequestMethod.GET)
    public @ResponseBody OrderResponse adminQueryOrdersByName(@RequestParam(value = "name") final String name,
                                                               @RequestParam(value = "pagelimit") final Long pageLimit,
                                                               @RequestParam(value = "pagenumber") final Integer pageNumber)
    {
        OrderResponse rval = new OrderResponse();

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {

            rval.setOrders(orderAccessor.getOrdersWithPurchaserByName(name, pageNumber, pageLimit, IEnums.SEEK_MODE.ORDER_LINE_ITEMS));

            rval.setOrders(preProcessOrders(rval.getOrders()));
        }

        return rval;
    }


    @RequestMapping(value = "ordersearchbypromocode", method = RequestMethod.GET)
    public @ResponseBody OrderResponse adminQueryOrdersByPromoCode(@RequestParam(value = "promocode") final String promoCode,
                                                               @RequestParam(value = "pagelimit") final Long pageLimit,
                                                               @RequestParam(value = "pagenumber") final Integer pageNumber)
    {
        OrderResponse rval = new OrderResponse();

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            rval.setOrders(orderAccessor.getOrdersWithPurchaserByPromoCode(promoCode, pageNumber, pageLimit, IEnums.SEEK_MODE.ORDER_LINE_ITEMS));

            rval.setOrders(preProcessOrders(rval.getOrders()));
        }

        return rval;
    }

    @RequestMapping(value = "createorderlabel", method = RequestMethod.POST)
    public
    @ResponseBody
    ShippingLabelResponse createOrderShippingLabel(@RequestParam(value = "orderId") final Long orderId,
                                                   HttpServletRequest req) {
        ShippingLabelResponse rval = new ShippingLabelResponse();

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            try {
                Order order = orderAccessor.getOrder(orderId, IEnums.SEEK_MODE.ORDER_FULL);
                EasyPostPostageLabel label = easyPostProvider.purchaseLabel(order.getShippingAddress(), Address.getTroveFromAddress(), TroveBoxDimensions.getTroveBoxDimensions(), 0);
                orderAccessor.associateShippingLabelWithOrder(orderId, label);
                rval.setLabel(label);
                rval.setIsSuccess(true);
                rval.setErrorMessage("");

            } catch (Exception e) {
                logError(e);
            }
        }

        return rval;
    }

    @RequestMapping(value = "/simpleitem", method = RequestMethod.POST)
    public
    @ResponseBody
    GenericAjaxResponse createSimpleItem(@RequestParam(value = "simpleItemData") final String data) {
        GenericAjaxResponse rval = new GenericAjaxResponse(false, "Oops!  Something went wrong.  Please try that again.");

        try {
            SimpleItem newItem = gson.fromJson(data, SimpleItem.class);

            String imagePrefix = newItem.getItemName().replace(" ", "-").toLowerCase() + "-" + StringUtils.generateRandomUppercaseString(5);
            newItem.setPrimaryDisplayImageUrl(uploadItemImage(newItem.getPrimaryDisplayImageUrl(), imagePrefix + "-mainitemimage"));

            if (newItem.getStaticAssets() != null && newItem.getStaticAssets().size() > 0) {
                for (int i = 0; i < newItem.getStaticAssets().size(); ++i) {
                    StaticAsset asset = newItem.getStaticAssets().get(i);
                    asset.setAssetUrl(uploadItemImage(asset.getAssetUrl(), imagePrefix + "-asset-" + i));
                }
            }

            if (newItem.getSimpleItemControls() != null && newItem.getSimpleItemControls().size() > 0) {
                for (SimpleItemControl control : newItem.getSimpleItemControls()) {

                    for (int i = 0; i < control.getControlOptions().size(); ++i) {
                        ControlOption currentOption = control.getControlOptions().get(i);
                        String optionPrefix = currentOption.getOptionDisplayName().replace(" ", "-").toLowerCase() + "-";

                        if (currentOption.getOptionAssetUrl() != null && currentOption.getOptionAssetUrl().length() > 0) {
                            currentOption.setOptionAssetUrl(uploadItemImage(currentOption.getOptionAssetUrl(), imagePrefix + "-swatch-" + i));
                        }

                        if (control.getInteractionType().equals(SimpleItemControl.INTERACTION_TYPE.ASSET_CHANGE)) {
                            for (ControlOptionAsset asset : currentOption.getControlOptionAssets()) {
                                asset.setControlOptionAssetUrl(uploadItemImage(asset.getControlOptionAssetUrl(), imagePrefix + optionPrefix + "-camera-" + asset.getCamera()));
                            }
                        }
                    }
                }
            }

            if (newItem.getHoverImageUrl() != null) {
                newItem.setHoverImageUrl(uploadItemImage(newItem.getHoverImageUrl(), imagePrefix + "-hoverimg"));
            }

            newItem.setControlReference(true);

            newItem.setBottomDescriptionText(newItem.getBottomDescriptionText().replaceAll("(\\r|\\n)", "\\\\n"));

            simpleItemAccessor.persistSimpleItem(newItem);

            rval.setErrorMessage("");
            rval.setIsSuccess(true);
        } catch (Exception e) {
            logError(e);
        }

        return rval;
    }

    private List<Order> preProcessOrders(List<Order> orders) {
        if (orders != null) {
            for (Order order : orders) {
                if (order.getOriginFtue() == null) {
                    order.setOriginFtue(false);
                }
                if (order.getOrderNotes() != null && order.getOrderNotes().size() > 0) {
                    order.setLatestNote(order.getOrderNotes().get(0));
                    order.setHasNote(true);
                    for (OrderNote note : order.getOrderNotes()) {
                        note.setUserFriendlyOrderNoteDate(DateUtils.formatRawDateOutputToESTString(note.getCreationDate()));
                    }
                }

                order.setUserFriendlyOrderDate(DateUtils.formatRawDateOutputToESTString(order.getOrderDate()));
            }
        }

        return orders;
    }

    @RequestMapping(value = "/addnote", method = RequestMethod.POST)
    public
    @ResponseBody
    GenericAjaxResponse addNote(@RequestParam(value = "orderId") final Long orderId,
                                @RequestParam(value = "noteText") final String noteText,
                                @RequestParam(value = "timeCreated") final Date timeCreated,
                                @RequestParam(value = "creatorUserId") final Long userId
                                ) {
        GenericAjaxResponse rval = new GenericAjaxResponse(false, "Oops!  Adding a new note didn't work. Try again.");

        User user = null;
        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (checkIsAdminOrManufacturer(user) && noteText != null) {
            rval.setIsSuccess(orderAccessor.addOrderNote(orderId, userId, noteText, timeCreated));

            if (rval.getIsSuccess()) {
                rval.setErrorMessage("");
            }
        }

        return rval;
    }

    @RequestMapping(value = "/associatelabel", method = RequestMethod.POST)
    public
    @ResponseBody
    GenericAjaxResponse associateLabelWithItem(@RequestParam(value = "labelId") final String labelId,
                                               @RequestParam(value = "itemType") final String itemType,
                                                @RequestParam(value = "itemId") final Long itemId) {
        GenericAjaxResponse rval = new GenericAjaxResponse(false, "Oops!  Adding a label didn't work. Try again.");

        User user = null;
        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (checkIsAdminOrManufacturer(user)) {
            rval.setIsSuccess(itemAccessor.associateLabel(itemId, itemType, labelId, IEnums.SEEK_MODE.FULL));

            if (rval.getIsSuccess()) {
                rval.setErrorMessage("");
            }
        }

        return rval;
    }

    private String uploadItemImage(String encodedData, String imageName) {
        String bucketName = isDevelopmentServer() ? env.getProperty("cloudstore.publicbucketname") : env.getProperty("cloudstore.itemimagecdn");
        String bucketPath = GoogleCloudStorage.CDN_URL_BASE_PATH + bucketName + "/" + imageName;
        String rval = null;

        if (encodedData.contains("http://") || encodedData.contains("https://")) {
            rval = encodedData;
        } else {
            try {
                String[] parsedString = encodedData.split(",");
                String imgExtension = parseImageType(parsedString[0]);
                rval = sendImageToCloudImageStorage(Base64.decode(parsedString[1].getBytes()), imageName + "." + imgExtension, bucketName, "image/" + imgExtension);

                if (rval != null) {
                    rval = bucketPath + "." + imgExtension;
                }
            } catch (Exception e) {
                logError(e);
            }
        }

        return rval;
    }
}
