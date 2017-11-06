package com.troveup.brooklyn.controllers.trove;

import com.troveup.brooklyn.model.GenericAjaxResponse;
import com.troveup.brooklyn.model.SimpleItemListResponse;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.simpleitem.model.SimpleItem;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.util.DateUtils;
import com.troveup.brooklyn.util.MoneyUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by tim on 4/5/16.
 */
@Controller
@RequestMapping(value = "/dashboard/ajax/")
public class DashboardAjaxController extends CommonController {
    @RequestMapping(value = "/ordersall", method = RequestMethod.GET)
    public
    @ResponseBody
    SimpleItemListResponse dashboardQueryOrdersAll(@RequestParam(value = "pagelimit") final Long pageLimit,
                                                   @RequestParam(value = "pagenumber") final Integer pageNumber) {
        SimpleItemListResponse rval = new SimpleItemListResponse();
        rval.setIsSuccess(false);
        rval.setErrorMessage("Oops!  Something went wrong, please try again.");

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (checkIsAdminOrManufacturer(user)) {
            rval.setItems(simpleItemAccessor.getLatestSimpleItemOrders(pageNumber, pageLimit, IEnums.SEEK_MODE.FULL));

            for (SimpleItem item : rval.getItems()) {
                item.setColor(SimpleItem.retrieveColorValueFromSelectedOptions(item));
                item.setQuantity(1);
                item.setSize(SimpleItem.retrieveSizeValueFromSelectedOptions(item));
                item.setEngraving(SimpleItem.retrieveEngravingValueFromSelectedOptions(item));
                item.setAvailableStatuses(SimpleItem.getListOfManufacturingStatuses(item.getStatus()));
                item.getAssociatedOrder().
                        setUserFriendlyOrderDate(DateUtils.formatRawDateOutputToESTString(item.getAssociatedOrder().getOrderDate()));
                item.setRushOrder(item.getRushOrder() == null ? false : item.getRushOrder());
                item.setLastSetManufacturerCompletionDate(DateUtils.convertCommonDateObjectToCommonHtmlCalendarDateString(item.getEstimatedManufacturerCompletionDate()));

                if (item.getOrderNotes() != null && item.getOrderNotes().size() > 0) {
                    item.setLatestNote(item.getOrderNotes().get(item.getOrderNotes().size() - 1));
                    item.setNotesCount(item.getOrderNotes().size());
                }

                if (item.getManufacturingPrice() != null) {
                    item.setManufacturerPriceText(MoneyUtil.toProperScale(item.getManufacturingPrice(), null).toString());
                } else {
                    item.setManufacturerPriceText("Not Specified");
                }
            }

            rval.setIsSuccess(true);
            rval.setErrorMessage("");
        }

        return rval;
    }

    @RequestMapping(value = "/updatecompletiondate", method = RequestMethod.POST)
    public
    @ResponseBody
    GenericAjaxResponse updateCompletionDate(@RequestParam(value = "itemId") final Long itemId,
                                             @RequestParam(value = "completionDate") final String completionDate) {
        GenericAjaxResponse rval = new GenericAjaxResponse(false, "Oops!  Setting the completion date didn't work.  Please try again.");

        User user = null;
        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (checkIsAdminOrManufacturer(user)) {
            try {
                rval.setIsSuccess(simpleItemAccessor.updateSimpleItemManufacturerCompletionDate(itemId, DateUtils.convertCommonHtmlCalendarDateToDateObject(completionDate)));

                if (rval.getIsSuccess()) {
                    rval.setErrorMessage("");
                }
            } catch (Exception e) {
                //Eat any parse exceptions
            }
        }

        return rval;
    }

    @RequestMapping(value = "/updatemanufacturerstatus", method = RequestMethod.POST)
    public
    @ResponseBody
    GenericAjaxResponse updateManufacturerStatus(@RequestParam(value = "itemId") final Long itemId,
                                                 @RequestParam(value = "status") final String status) {
        GenericAjaxResponse rval = new GenericAjaxResponse(false, "Oops!  Setting the status didn't work.  Please try again.");

        User user = null;
        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (checkIsAdminOrManufacturer(user)) {
            SimpleItem.MANUFACTURING_STATUS enumStatus = SimpleItem.getEnumStatusObjectFromString(status);
            rval.setIsSuccess(simpleItemAccessor.updateSimpleItemManufacturerStatus(itemId, enumStatus));

            if (rval.getIsSuccess()) {
                rval.setErrorMessage("");
            }
        }

        return rval;
    }

    @RequestMapping(value = "/updatemanufacturerprice", method = RequestMethod.POST)
    public
    @ResponseBody
    GenericAjaxResponse updateManufacturerPrice(@RequestParam(value = "itemId") final Long itemId,
                                                @RequestParam(value = "price") final Float price) {
        GenericAjaxResponse rval = new GenericAjaxResponse(false, "Oops!  Setting the price didn't work.  Please try again.");

        User user = null;
        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (checkIsAdminOrManufacturer(user) && price != null) {
            rval.setIsSuccess(simpleItemAccessor.updateSimpleItemManufacturerPrice(itemId, price));

            if (rval.getIsSuccess()) {
                rval.setErrorMessage("");
            }
        }

        return rval;
    }

    @RequestMapping(value = "/addnote", method = RequestMethod.POST)
    public
    @ResponseBody
    GenericAjaxResponse addNote(@RequestParam(value = "itemId") final Long itemId,
                                                @RequestParam(value = "noteText") final String noteText) {
        GenericAjaxResponse rval = new GenericAjaxResponse(false, "Oops!  Setting the price didn't work.  Please try again.");

        User user = null;
        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (checkIsAdminOrManufacturer(user) && noteText != null) {
            rval.setIsSuccess(simpleItemAccessor.addSimpleItemNote(itemId, user.getUserId(), noteText));

            if (rval.getIsSuccess()) {
                rval.setErrorMessage("");
            }
        }

        return rval;
    }
}
