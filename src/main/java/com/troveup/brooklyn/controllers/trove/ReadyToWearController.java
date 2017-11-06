package com.troveup.brooklyn.controllers.trove;

import com.troveup.brooklyn.model.*;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.simpleitem.model.SimpleItem;
import com.troveup.brooklyn.orm.storefront.model.StoreFront;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.cdn.gcs.GoogleCloudStorage;
import com.troveup.brooklyn.util.StringUtils;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tim on 6/4/16.
 */
@Controller
@RequestMapping(value = "/readytowear", method = {RequestMethod.GET, RequestMethod.HEAD})
public class ReadyToWearController extends CommonController {
    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView readyToWear(@RequestHeader(required = false, value = "X-PJAX") String pjaxHeader,
                                    HttpServletRequest req) {
        ModelAndView rval = getModelAndView("public/readytowear", req);

        rval.addObject("isAuthenticated", isAuthenticated());
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("developmentServer", isDevelopmentServer());

        return rval;
    }

    @RequestMapping(value = "/ajax/allitems", method = {RequestMethod.GET, RequestMethod.HEAD})
    public
    @ResponseBody
    SimpleItemResponse getAllReadyToWearItems(@RequestParam(value = "pagelimit") final Long pageLimit,
                                              @RequestParam(value = "pagenumber") final Integer pageNumber) {
        SimpleItemResponse rval = new SimpleItemResponse(false, GenericAjaxResponse.standardGenericErrorMessage);

        try {
            rval.setItems(simpleItemAccessor.getPaginatedReadyToWearSimpleItemReferences(pageNumber, pageLimit, IEnums.SEEK_MODE.QUICK));
            rval.setIsSuccess(true);
            rval.setErrorMessage("");

            //Set the item images to something tangible, otherwise everything will appear broken
            //And if there is a local environment out there that hasn't been initialized, give it some sample items.
            // if (isLocalDevelopmentServer()) {
            //     rval.setItems(setAndCheckLocalItems(rval.getItems(), pageLimit, pageNumber));
            // }
        } catch (Exception e) {
            logError(e);
        }

        return rval;
    }

    @RequestMapping(value = "/ajax/storeurlexists", method = {RequestMethod.GET})
    public
    @ResponseBody
    ExistsResponse checkStoreUrlExistence(@RequestParam(value = "url") final String url) {
        ExistsResponse rval = new ExistsResponse(false, GenericAjaxResponse.standardGenericErrorMessage);
        rval.setCheckedValue(url);
        rval.setValid(false);

        try {
            Pattern pattern = Pattern.compile("[~`%@!$#^&*)(:;/\\\\|<>\"'\\[\\]\\+, {}=?]");
            Matcher matcher = pattern.matcher(url);

           if (url.length() > 0 && !matcher.find()) {
                rval.setExists(storeFrontAccessor.doesSellerUrlShortpathExist(url.toLowerCase()));
                rval.setValid(true);
                rval.setIsSuccess(true);
                rval.setErrorMessage("");
            } else {
                rval.setIsSuccess(true);
                rval.setErrorMessage("URL was not valid");
            }
        } catch (Exception e) {
            logError(e);
        }

        return rval;
    }

    @RequestMapping(value = "/ajax/createstore", method = {RequestMethod.POST})
    public
    @ResponseBody
    GenericAjaxResponse createStore(@RequestParam(value = "storesettings") final String storeSettings)
    {
        GenericAjaxResponse rval = new GenericAjaxResponse(false, "Oops!  Something went wrong.  Please try that again.");

        try
        {
            User user = getQuickUserDetailsPII();

            StoreExport export = gson.fromJson(storeSettings, StoreExport.class);

            String imageName = "storefront-" + export.getStoreName().toLowerCase().replace(" ", "-") + "-" + StringUtils.generateRandomUppercaseString(5);
            String[] parsedString = export.getProfileImg().split(",");
            String imgExtension = parseImageType(parsedString[0]);
            imageName += ("." + imgExtension);
            String bucketName = isDevelopmentServer() ? env.getProperty("cloudstore.publicbucketname") : env.getProperty("cloudstore.itemimagecdn");
            String bucketPath = GoogleCloudStorage.CDN_URL_BASE_PATH + bucketName + "/" + imageName;
            sendImageToCloudImageStorage(Base64.decode(parsedString[1].getBytes()), imageName, bucketName, "image/" + imgExtension);

            StoreFront storeFront = new StoreFront();
            storeFront.setStoreOwner(user);
            storeFront.setStoreName(export.getStoreName());
            storeFront.setStoreDescription(export.getStoreDescription());
            storeFront.setStoreShortUrl(export.getStoreUrl().toLowerCase());
            storeFront.setProfileImgUrl(bucketPath);
            storeFront.setStoreFrontItems(new ArrayList<SimpleItem>());

            for (StoreExportItem item : export.getItems())
            {
                SimpleItem simpleItem = new SimpleItem();
                simpleItem.setSimpleItemId(item.getId());
                simpleItem.setItemPrice(item.getPrice());
                simpleItem.setStorefrontShortUrl("/sellers/" + export.getStoreUrl().toLowerCase());
                storeFront.getStoreFrontItems().add(simpleItem);
            }

            if(storeFrontAccessor.persistStoreFront(user.getUserId(), storeFront) != null)
            {
                rval.setIsSuccess(true);
                rval.setErrorMessage("");
            }


        } catch (Exception e)
        {
            logError(e);
        }

        return rval;

    }

    private List<SimpleItem> setAndCheckLocalItems(List<SimpleItem> itemList, Long pageLimit, Integer pageNumber) {
        List<SimpleItem> rval = new ArrayList<>();

        String LOCAL_DISPLAY_IMAGE = "https://storage.googleapis.com/troveup-imagestore/assets/img/placeholder-item.jpg";

        if (itemList == null || itemList.size() == 0) {
            for (int i = 0; i < pageLimit; ++i) {
                long pageValue = i + pageNumber * pageLimit;

                SimpleItem item = new SimpleItem();
                item.setSimpleItemId(pageValue);
                item.setPrimaryDisplayImageUrl(LOCAL_DISPLAY_IMAGE);
                item.setItemPrice(new BigDecimal(10.00 * i));
                item.setItemName("Item " + pageValue);
                item.setItemDescription("This is a description for item number " + pageValue);
                rval.add(item);
            }
        } else {
            for (SimpleItem item : itemList) {
                item.setPrimaryDisplayImageUrl(LOCAL_DISPLAY_IMAGE);
            }

            rval = itemList;
        }

        return rval;
    }
}
