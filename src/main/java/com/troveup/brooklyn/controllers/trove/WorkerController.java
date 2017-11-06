package com.troveup.brooklyn.controllers.trove;

import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.easypost.exception.EasyPostException;
import com.google.appengine.api.utils.SystemProperty;
import com.troveup.brooklyn.model.ManufacturerUpload;
import com.troveup.brooklyn.orm.cart.model.*;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.ftui.model.*;
import com.troveup.brooklyn.orm.item.model.*;
import com.troveup.brooklyn.orm.materials.model.Finish;
import com.troveup.brooklyn.orm.materials.model.Material;
import com.troveup.brooklyn.orm.order.model.EasyPostPostageLabel;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.order.model.PrintOrder;
import com.troveup.brooklyn.orm.order.model.SupplierModelIdAndName;
import com.troveup.brooklyn.orm.renderqueue.interfaces.IRenderQueueAccessor;
import com.troveup.brooklyn.orm.renderqueue.model.Render;
import com.troveup.brooklyn.orm.simpleitem.model.SimpleItem;
import com.troveup.brooklyn.orm.urlshortener.model.ShortLink;
import com.troveup.brooklyn.orm.user.model.Address;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.cdn.gcs.GoogleCloudStorage;
import com.troveup.brooklyn.sdk.cdn.model.CloudParam;
import com.troveup.brooklyn.sdk.email.mandrill.api.MandrillMessageStatus;
import com.troveup.brooklyn.sdk.email.mandrill.model.OrderConfirmationItem;
import com.troveup.brooklyn.sdk.mail.model.EasyPostTrackingUpdate;
import com.troveup.brooklyn.sdk.mail.model.TroveBoxDimensions;
import com.troveup.brooklyn.sdk.meshexporter.forge.model.CustomizerOperator;
import com.troveup.brooklyn.sdk.meshexporter.forge.model.ForgeMeshExportResponse;
import com.troveup.brooklyn.sdk.meshexporter.java.model.ModelJson;
import com.troveup.brooklyn.sdk.print.shapeways.api.ShapewaysPriceApi;
import com.troveup.brooklyn.sdk.print.shapeways.model.*;
import com.troveup.brooklyn.sdk.realityserver.api.RealityServer;
import com.troveup.brooklyn.sdk.realityserver.business.RealityServerManager;
import com.troveup.brooklyn.sdk.realityserver.model.RealityServerCamera;
import com.troveup.brooklyn.sdk.realityserver.model.RealityServerInitialSubmitResponse;
import com.troveup.brooklyn.sdk.realityserver.model.RealityServerPostbackResponse;
import com.troveup.brooklyn.sdk.tax.avalara.model.AvaGetTaxResult;
import com.troveup.brooklyn.util.*;
import org.apache.commons.io.Charsets;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.*;

/**
 * Created by tim on 4/20/15.
 */
@Controller
@RequestMapping(value = "/worker", method = {RequestMethod.GET, RequestMethod.HEAD})
public class WorkerController extends CommonController
{
    private final String GOOGLE_APP_ENGINE_TASK_QUEUE_IP = "0.1.0.2";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public static final String NIMBIX_STATUS_KEY = "NIMBIX_STATUS";
    public static final String RUNNING_RENDER_JOB = "RUNNING_RENDER";
    public static final String NIMBIX_IP_ADDRESS = "NIMBIX_IP";
    public static final String NIMBIX_SERVER_JOB_ID = "NIMBIX_SERVER_JOB_ID";
    public static final String RUNNING_RENDER_JOB_SUBMIT_TIME = "SUBMIT_TIME";
    public static final String RUNNING_FEED_CURATION = "FEED_CURATING";
    public static final String RUNNING_FEED_CURATION_SUBMISSION_TIME = "FEED_CURATING_TIME";
    public static final String FTUE_WORKER_URL = "/worker/processftuerequest";
    public static final String REALITY_WORKER_SUBMIT_URL = "/worker/submitrendertoqueue";
    public static final String COLLECTION_REMAKE_COUNT_INCREMENT = "/worker/incrementitemcollectionremakecount";
    public static final String COLLECTION_TROVE_COUNT_INCREMENT = "/worker/incrementitemcollectiontrovecount";
    public static final String UPLOAD_MODEL_URL = "/worker/uploadmodel";
    public static final String CREATE_SHIPPING_LABEL_URL = "/worker/createshippinglabel";
    public static final String REFERRAL_EMAIL_WORKER_URL = "/worker/processftuereferral";
    public static final String REFRESH_ORDER_URL = "/worker/refreshorderstatus";
    public static final String ERROR_EMAIL_URL = "/worker/erroremail";
    public static final String PROCESS_REFERRAL_URL = "/worker/processreferral";
    public static final String PROPAGATE_PRICE_FILTER_URL = "/worker/propagatefilters";
    public static final String PROCESS_CART_HOOKS_URL = "/processhooks";
    public static final String GIFT_CARD_HOOK_URL = "/giftcardhook";
    public static final String SIMPLE_ITEM_HOOK_URL = "/simpleitemhook";
    public static final String FTUE_ORDER_AND_PRINT_URL = "/worker/createandsubmitftueorder";
    public static final String GENERATE_USERNAME_URL = "/worker/generateusername";
    public static final String SCALE_IMAGE_URL = "/worker/scaleitemimage";

    public static final String WORKER_QUEUE_OBJECT_UPDATES = "objectupdatequeue";

    public static final String UPLOAD_TYPE_SHAPEWAYS = "SHAPEWAYS";
    public static final String UPLOAD_TYPE_IMATERIALISE = "IMATERIALISE";
    public static final String CHECKOUT_SYSTEM_FTUE = "FTUE";
    public static final String CHECKOUT_SYSTEM_CART = "CART";
    public static final String CHECKOUT_SYSTEM_FTUE_PROTOTYPE = "FTUE_PROTOTYPE";

    private enum MODEL_RESOLUTION
    {
        HIGH,
        LOW
    }

    private String cdnBucketName;
    private String cdnBucketUrl;
    private String jsonBucketName;
    private String imageBucketName;
    private String imageBucketUrl;
    private String environmentType;
    private String siteUrl;

    @PostConstruct
    public void init()
    {
        if (env.getProperty("cloudstore.publicbucketname") != null)
            cdnBucketName = env.getProperty("cloudstore.publicbucketname");
        else
            cdnBucketName = GoogleCloudStorage.CDN_BUCKET_NAME;

        if (env.getProperty("cloudstore.cloudstoreurl") != null)
            cdnBucketUrl = env.getProperty("cloudstore.cloudstoreurl") + cdnBucketName + "/";
        else
            cdnBucketUrl = GoogleCloudStorage.CDN_URL_BASE_PATH + cdnBucketName + "/";

        if (env.getProperty("cloudstore.jsonbucketname") != null)
            jsonBucketName = env.getProperty("cloudstore.jsonbucketname");
        else
            jsonBucketName = GoogleCloudStorage.CDN_JSON_BUCKET_NAME;

        imageBucketName = env.getProperty("cloudstore.itemimagecdn");
        imageBucketUrl = env.getProperty("cloudstore.cloudstoreurl") + imageBucketName + "/";
        environmentType = env.getProperty("environment.type");
        siteUrl = env.getProperty("environment.baseurl");

    }

    @RequestMapping(value = "/propagatefilters", method = RequestMethod.POST)
    public ResponseEntity<String> propagateItemPriceFilters(@RequestParam("parentId") final Long parentId,
                                                            @RequestParam("itemId") final Long itemId,
                                                            HttpServletRequest request)
    {
        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);

        if (parentId != null && itemId != null) {

            try {

                List<PriceFilter> persistableFilters = new ArrayList<>();
                List<Material> materials = printSupplier.getSupplierMaterials();
                Item item = new Item();
                item.setItemId(itemId);

                //Get any sweeping material filter that exists first
                PriceFilter filter = itemAccessor.getPriceFilter(new PriceFilter(PriceFilter.APPLICATION_LEVEL.ITEM, PriceFilter.MATERIAL_LEVEL.ALL, null, null, null, parentId), true);

                if (filter != null) {

                    filter.setItemReference(item);
                    persistableFilters.add(filter);
                }

                for (Material material : materials)
                {
                    for (Finish finish : material.getFinishList())
                    {
                        filter = itemAccessor.getPriceFilter(new PriceFilter(PriceFilter.APPLICATION_LEVEL.ITEM, PriceFilter.MATERIAL_LEVEL.MATERIAL, null, material.getMaterialId(), finish.getFinishId(), parentId), true);

                        if (filter != null) {
                            filter.setItemReference(item);
                            persistableFilters.add(filter);
                        }
                    }
                }

                if (persistableFilters.size() > 0) {
                    if (itemAccessor.bulkPersistFilters(persistableFilters) == persistableFilters.size()) {
                        rval = new ResponseEntity<String>(HttpStatus.OK);
                        logger.info("Persisted " + persistableFilters.size() + " new PriceFilter records.");
                    } else {
                        sendErrorEmailToFounders("1",
                                "/worker/propagatefilters",
                                "Problem encountered trying to propagate pricing from "
                                        + parentId + " to " + itemId + ".  See logs at " + new Date() + " for more details.",
                                "500", request);
                    }
                }
                //Nothing to do here
                else
                {
                    rval = new ResponseEntity<String>(HttpStatus.OK);
                }

            } catch (Exception e)
            {
                logError(e);
                sendErrorEmailToFounders("1",
                        "/worker/propagatefilters",
                        "Exception happened trying to propagate pricing from "
                                + parentId + " to " + itemId + ".  See logs at " + new Date() + " for more details.",
                        "500", request);
            }
        }
        else {
            sendErrorEmailToFounders("1", "/worker/propagatefilters", "Parent or item ID was null", "500", request);
        }

        return rval;
    }

    @RequestMapping(value = "/curategiftcardemails", method = RequestMethod.GET)
    public ResponseEntity<String> giftCardEmailCurator()
    {
        //New York Time
        List<GiftCard> digitalCardsToBeSent = cartAccessor.getDigitalGiftCardsToBeMailed(DateUtils.getDateXHoursAgo(new Date(), 5));

        if (digitalCardsToBeSent != null && digitalCardsToBeSent.size() > 0)
        {

            logger.info("Card to be sent count: " + digitalCardsToBeSent.size());

            for (GiftCard card : digitalCardsToBeSent)
            {
                AbstractMap.SimpleEntry<String, String> from = new AbstractMap.SimpleEntry<String, String>("Trove Team",
                        "hello@troveup.com");

                Map<String, String> toMap = new HashMap<>();
                toMap.put(card.getToName(), card.getToEmail());

                Map<String, String> templateVariables = new HashMap<>();
                templateVariables.put("SENDER_NAME", card.getFromName());
                templateVariables.put("RECIPIENT_NAME", card.getToName());
                templateVariables.put("GIFT_CARD_NUMBER", card.getGiftCardString());
                templateVariables.put("GIFT_CARD_AMOUNT", MoneyUtil.toProperScale(card.getAmount(), null).toString());

                MandrillMessageStatus[] status = (MandrillMessageStatus[])
                        mailProvider.sendTemplateEmail("A Gift from " + card.getFromName(), from, toMap,
                                "digital-gift-card-delivery",
                                templateVariables, false);

                cartAccessor.setDigitalGiftCardStatusSent(card.getGiftCardId());
            }
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = GIFT_CARD_HOOK_URL, method = RequestMethod.POST)
    public ResponseEntity<String> giftCardHook(@RequestParam("genericItemId") final Long genericItemId)
    {
        GenericItem genericItem = cartAccessor.getGenericItem(genericItemId, IEnums.SEEK_MODE.GENERIC_ITEM_FULL);

        try {
            GiftCard giftCard = new GiftCard(genericItem);

            //Make sure to throw out an alert if this persistence fails and keep trying
            if (!cartAccessor.persistGiftCard(giftCard))
                throw new RuntimeException("Gift card persistence failed, please check the logs");

        } catch (ParseException e) {
            throw new RuntimeException("Date format provided to endpoint didn't match the attempted parse format!");
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = SIMPLE_ITEM_HOOK_URL, method = RequestMethod.POST)
    public ResponseEntity<String> simpleItemHook(@RequestParam("genericItemId") final Long genericItemId)
    {
        GenericItem genericItem = cartAccessor.getGenericItem(genericItemId, IEnums.SEEK_MODE.GENERIC_ITEM_FULL);

        //Set the order number on the simpleItem so that the ORM layer can attach a persistence managed version
        //of the actual order
        Long containingOrderId = orderAccessor.getOrderIdContainingGenericItem(genericItem.getGenericItemId());
        Order tempOrder = new Order();
        tempOrder.setOrderId(containingOrderId);

        SimpleItem simpleItem = new SimpleItem(genericItem, simpleItemAccessor);
        simpleItem.setAssociatedOrder(tempOrder);

        simpleItemAccessor.persistSimpleItem(simpleItem);
        cartAccessor.updateGenericItemReferenceId(genericItemId, simpleItem.getSimpleItemId());

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = PROCESS_CART_HOOKS_URL, method = RequestMethod.POST)
    public ResponseEntity<String> processCartHooks(@RequestParam("cartId") final Long cartId)
    {
        Cart cart = cartAccessor.getShoppingCart(cartId, IEnums.SEEK_MODE.FULL);

        if (cart.getGenericItems() != null && cart.getGenericItems().size() > 0)
        {
            for (GenericItem item : cart.getGenericItems())
            {
                for (Hook hook : item.getHooks())
                {
                    if (hook.getHookSpecifier() == Hook.HOOK_SPECIFIER.GIFT_CARD)
                    {
                        Map<String, String> workerQueue = new HashMap<>();
                        workerQueue.put("genericItemId", item.getGenericItemId().toString());
                        WorkerQueuer.queueWorkForWorker(workerQueue, "/worker" + WorkerController.GIFT_CARD_HOOK_URL);
                    }
                    else if (hook.getHookSpecifier() == Hook.HOOK_SPECIFIER.SIMPLE_ITEM)
                    {
                        Map<String, String> workerQueue = new HashMap<>();
                        workerQueue.put("genericItemId", item.getGenericItemId().toString());
                        WorkerQueuer.queueWorkForWorker(workerQueue, "/worker" + WorkerController.SIMPLE_ITEM_HOOK_URL);
                    }
                }
            }
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/incrementitemcollectiontrovecount", method = RequestMethod.POST)
    public ResponseEntity<String> incrementItemCollectionTroveCount(@RequestParam("itemId") final Long itemId)
    {
        userAccessor.incrementItemCollectionTroveCount(itemId);

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/incrementitemcollectionremakecount", method = RequestMethod.POST)
    public ResponseEntity<String> incrementItemCollectionRemakeCount(@RequestParam("itemId") final Long itemId)
    {
        userAccessor.incrementItemCollectionRemakeCount(itemId);

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/curateprintorders", method = RequestMethod.GET)
    public ResponseEntity<String> curatePrintOrders()
    {
        List<PrintOrder> printOrdersPendingWithManufacturer =
                printOrderAccessor.getPrintOrdersBeingManufactured(IEnums.SEEK_MODE.FULL, 20l);

        if (printOrdersPendingWithManufacturer != null && printOrdersPendingWithManufacturer.size() > 0)
        {
            for (PrintOrder order : printOrdersPendingWithManufacturer)
            {
                ShapewaysGetOrderStatusResponse response = (ShapewaysGetOrderStatusResponse)
                        printSupplier.getOrderStatus(order.getManufacturerOrderId());

                if (response != null && response.getOrdersStatus() != null)
                {
                    //Shapeways returns a whole bunch of orders, even though I only specify one.  Find the one
                    //that I'm ACTUALLY tring to get...
                    for (ShapewaysOrderStatus status : response.getOrdersStatus())
                    {
                        //Found it.  Shapeways doesn't give a single string for statuses.  Instead, it gives several
                        //fields as integers that stand for booleans as to whether or not the process is complete.
                        //Compare them field-by-field to be sure that we update our status accordingly..
                        if (status.getId().equals(Long.parseLong(order.getManufacturerOrderId())))
                        {
                            PrintOrder.ORDER_STATUS newPrintOrderStatus = null;
                            //Order is complete
                            if (status.getItems().get(0).getStatus().getComplete() == -1)
                            {
                                newPrintOrderStatus = PrintOrder.ORDER_STATUS.COMPLETE;
                            }
                            //Order is being produced
                            else if (status.getItems().get(0).getStatus().getIn_production() == 1)
                            {
                                newPrintOrderStatus = PrintOrder.ORDER_STATUS.IN_PRODUCTION;
                            }
                            //Order is still in processing
                            else if (status.getItems().get(0).getStatus().getProcessing() == 1)
                            {
                                newPrintOrderStatus = PrintOrder.ORDER_STATUS.PROCESSING;
                            }
                            else if (status.getItems().get(0).getStatus().getCancelled() == 1)
                            {
                                newPrintOrderStatus = PrintOrder.ORDER_STATUS.CANCELLED;

                                //Possible that Shapeways cancelled it from their end without notifying us.  Make sure
                                //that we're notified from TroveWeb.
                                if (order.getManufacturerOrderId() != null && !isDevelopmentServer())
                                {
                                    Map<String, String> founders = new HashMap<>();
                                    String errorText = "Trove has detected that Shapeways order " + order.getManufacturerOrderId() +
                                            " was cancelled from the Shapeways side.  The Trove PrintOrder status has been updated to reflect "
                                            + "this change.  Please verify that this cancellation was intentional, and that it was not caused by a forced "
                                            + "cancellation due to the model being unprintable.  Environment: " + getSiteUrl();
                                    founders.put("Tim", "api@troveup.com");
                                    founders.put("Tim", "tim@troveup.com");
                                    founders.put("Brian", "brian@troveup.com");
                                    founders.put("Andrew", "andrew@troveup.com");

                                    mailProvider.sendRawEmail("Shapeways Forced Cancellation Notification", "errors@troveup.com",
                                            founders, errorText, false);
                                }
                            }
                            //else if (status.getItems().get(0).getStatus().get)

                            //If we have a status update
                            if (newPrintOrderStatus != null)
                            {
                                //Set the update for the item
                                printOrderAccessor.updatePrintOrderStatus(order.getPrintOrderPrimaryKeyId(),
                                        newPrintOrderStatus);

                                //Generate a shipping label to be sent from Trove HQ
                                if (newPrintOrderStatus == PrintOrder.ORDER_STATUS.COMPLETE)
                                {
                                    if (order.getOrderSystem() == PrintOrder.ORDER_SYSTEM.CART) {
                                        //Generate a shipping label for this item
                                        Map<String, String> workerQueue = new HashMap<>();
                                        workerQueue.put("checkoutSystem", WorkerController.CHECKOUT_SYSTEM_CART);
                                        workerQueue.put("orderNumber", String.valueOf(order.getPrintOrderPrimaryKeyId()));
                                        WorkerQueuer.queueWorkForWorker(workerQueue, WorkerController.CREATE_SHIPPING_LABEL_URL);
                                    }
                                }
                                else
                                {
                                    printOrderAccessor.updateLastStatusRequestDate(order.getPrintOrderPrimaryKeyId(),
                                            new Date(), DateUtils.getDateXHoursFrom(new Date(),
                                                    PrintOrder.UPDATE_HOUR_DELAY));
                                }
                            }
                            else
                            {
                                //Something went wrong, notify the API e-mail address
                                StringBuilder builder = new StringBuilder();
                                builder.append("Couldn't get a status update for print order ");
                                builder.append(order.getPrintOrderPrimaryKeyId());
                                builder.append(".  Manufacturer order ID is ");
                                builder.append(order.getManufacturerOrderId());

                                Map<String, String> founders = new HashMap<>();
                                founders.put("Tim", "api@troveup.com");

                                mailProvider.sendRawEmail("Shapeways Status Update Failure Notification", "errors@troveup.com",
                                        founders, builder.toString(), false);
                            }
                        }
                    }
                }
            }
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    /**
     * Referral handling mechanism, ensures that e-mails are sent out and promocodes are properly allotted.
     * @param referrerCode Referral code used by the person that was referred, used in pulling the referrer's user account
     * @param request Servlet request for contextual use
     * @return HttpStatus response
     */
    @RequestMapping(value = "/processreferral", method = RequestMethod.POST)
    public ResponseEntity<String> processFtueReferral(@RequestParam("referrerCode") final String referrerCode,
                                                     HttpServletRequest request)
    {
        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.OK);

        List<Long> referralCountEmailTiers = new ArrayList<>();
        referralCountEmailTiers.add(5l);
        referralCountEmailTiers.add(10l);
        referralCountEmailTiers.add(25l);

        //Use this SEEK_MODE with caution if copy/pasting this line.  Passing this user object via a serialized
        //AJAX call to the front end will expose sensitive user data, like password hash and email address.
        //This is okay here, as that data is discarded and stays localized to the back-end.
        User user = userAccessor.getUserByReferralCode(referrerCode, IEnums.SEEK_MODE.USER_SOCIAL_CONNECTIONS);

        Long referralCount = userAccessor.getReferralCountByUserId(user.getUserId());

        //Handle the referral count tiers, if they qualify.  Otherwise, do nothing.
        if (referralCountEmailTiers.contains(referralCount))
        {

            AbstractMap.SimpleEntry<String, String> from = new AbstractMap.SimpleEntry<String, String>("Trove Team",
                    "hello@troveup.com");

            Map<String, String> toMap = new HashMap<>();
            toMap.put(user.getFirstName(), user.getEmail());

            PromoCode promoCode = PromoCode.buildItemPromoCode(null, PromoCode.DISCOUNT_ITEM_CATEGORY.MATERIAL, false, 1,
                    false, null, 100d, true, ShapewaysPriceApi.getTryOnMaterialId(), null, null, user);

            String[] userSplitName = user.getFirstName().split(" ");

            String name = userSplitName.length > 0 ? userSplitName[0] : user.getFirstName();

            String shareLink = getSiteUrl() + "signup/" + user.getReferralCode();

            Map<String, String> templateVariables = new HashMap<>();
            templateVariables.put("UNIQUE_TW_SHARE", "https://twitter.com/home?status=Customize,%20Try-On,%20" +
                    "and%20Buy%20jewelry%20in%20precious%20metals%20-%203D-printed%20just%20for%20you." +
                    "%0ASign%20up%20here%3A" + shareLink);
            templateVariables.put("UNIQUE_FB_SHARE", "http://www.facebook.com/sharer/sharer.php?u=" + shareLink);
            templateVariables.put("UNIQUE_URL_SHARE", shareLink);
            templateVariables.put("NAME", name);

            Integer referralTier = 1;

            String promocodeAlertText = "User " + user.getUsername() + " with userId " + user.getUserId() +
                    " has reached tier ";


            if (referralCount.equals(5l))
            {
                promoCodeAccessor.addPromoCode(promoCode);
                templateVariables.put("PROMO_CODE_TRYON", promoCode.getPromoCode());

                MandrillMessageStatus[] status = (MandrillMessageStatus[])
                        mailProvider.sendTemplateEmail("You have reached your first reward!", from, toMap,
                                "referral-program-5-referrals",
                                templateVariables, false);

                promocodeAlertText += " 1, and they\'ve been given promocode " + promoCode.getPromoCode();

            }
            else if (referralCount.equals(10l))
            {
                PromoCode secondPromoCode = PromoCode.buildItemPromoCode(null, PromoCode.DISCOUNT_ITEM_CATEGORY.MATERIAL, false, 1,
                        false, null, 100d, true, ShapewaysPriceApi.getTryOnMaterialId(), null, null, user);

                PromoCode thirdPromoCode = PromoCode.buildItemPromoCode(null, PromoCode.DISCOUNT_ITEM_CATEGORY.MATERIAL, false, 1,
                        false, null, 100d, true, ShapewaysPriceApi.getTryOnMaterialId(), null, null, user);

                promoCodeAccessor.addPromoCode(promoCode);
                promoCodeAccessor.addPromoCode(secondPromoCode);
                promoCodeAccessor.addPromoCode(thirdPromoCode);

                templateVariables.put("PROMO_CODE_TRYON_1", promoCode.getPromoCode());
                templateVariables.put("PROMO_CODE_TRYON_2", secondPromoCode.getPromoCode());
                templateVariables.put("PROMO_CODE_TRYON_3", thirdPromoCode.getPromoCode());

                MandrillMessageStatus[] status = (MandrillMessageStatus[])
                        mailProvider.sendTemplateEmail("You have reached your second reward!", from, toMap,
                                "referral-program-10-referrals",
                                templateVariables, false);

                referralTier = 2;

                promocodeAlertText += " 2, and they\'ve been given promocodes " + promoCode.getPromoCode() + ", " +
                        secondPromoCode.getPromoCode() + ", " + thirdPromoCode.getPromoCode();
            }
            else if (referralCount.equals(25l))
            {
                PromoCode bronzePromoCode = PromoCode.buildItemPromoCode(null,
                        PromoCode.DISCOUNT_ITEM_CATEGORY.MATERIAL_AND_FINISH, false, 1, false, null, 100d, true,
                        ShapewaysPriceApi.getPolishedBronzeMaterialFinish().getKey(),
                        ShapewaysPriceApi.getPolishedBronzeMaterialFinish().getValue(), null, user);

                promoCodeAccessor.addPromoCode(bronzePromoCode);
                templateVariables.put("PROMO_CODE_BRZ_RING", bronzePromoCode.getPromoCode());

                MandrillMessageStatus[] status = (MandrillMessageStatus[])
                        mailProvider.sendTemplateEmail("You have reached your third reward!", from, toMap,
                                "referral-program-25-referrals",
                                templateVariables, false);

                referralTier = 3;

                promocodeAlertText += " 3, and they\'ve been given promocode " + bronzePromoCode.getPromoCode();
            }

            Map<String, String> founders = new HashMap<>();
            founders.put("Tim", "api@troveup.com");
            founders.put("Kristin", "kristin@troveup.com");
            founders.put("Brian", "brian@troveup.com");

            if (!isLocalDevelopmentServer()) {
                mailProvider.sendRawEmail("User " + user.getUsername() + " Reached Referral Tier " + referralTier, "hello@troveup.com",
                        founders, promocodeAlertText, false);
            }
        }

        return rval;
    }

    @RequestMapping(value = "/easypostwebhook", method = RequestMethod.POST)
    public ResponseEntity<String> easyPostWebhook(HttpServletRequest request) throws IOException
    {

        String input = request.getReader().readLine();

        logger.debug("Webhook request: " + input);

        EasyPostTrackingUpdate trackingUpdate = gson.fromJson(input, EasyPostTrackingUpdate.class);

        //Old Item based system tracking, still needs to be active until we offline them
        if (trackingUpdate != null && trackingUpdate.getResult() != null &&
                trackingUpdate.getResult().getStatus() != null)
        {
            EasyPostPostageLabel.TRACKING_STATUS status =
                    EasyPostPostageLabel.convertStringStatusToTrackingStatus(trackingUpdate.getResult().getStatus());

            List<CartItem> cartItemsInShipment =
                    printOrderAccessor.getCartItemsByTrackingNumber(trackingUpdate.getResult().getTracking_code(),
                            IEnums.SEEK_MODE.CART_ITEM_STATUS);

            if (cartItemsInShipment != null && cartItemsInShipment.size() > 0 &&
                    printOrderAccessor.updatePostageLabelStatus(trackingUpdate.getResult().getTracking_code(), status))
            {
                if (status == EasyPostPostageLabel.TRACKING_STATUS.IN_TRANSIT) {
                    Order order = orderAccessor.getOrderByShippingTrackingNumber(trackingUpdate.getResult().getTracking_code(),
                            IEnums.SEEK_MODE.ORDER_ADDRESS);

                    if (cartItemsInShipment != null && cartItemsInShipment.size() > 0)
                    {
                        for (CartItem item : cartItemsInShipment)
                        {
                            if (item.getCartItemStatus() == CartItem.CART_ITEM_STATUS.PROTOTYPE_ORDERED)
                            {
                                cartAccessor.updateCartItemStatus(item.getCartItemId(),
                                        CartItem.CART_ITEM_STATUS.PROTOTYPE_SHIPPED_TO_USER);

                                mailProvider.sendShippedPrototypeEmail(order.getShippingAddress(), order.getBillingAddress().getFirstName(), order.getBillingAddress().getLastName(), order.getCheckoutEmail(), trackingUpdate.getResult().getTracking_code());
                            }
                            else if (item.getCartItemStatus() == CartItem.CART_ITEM_STATUS.ORDERED)
                            {
                                cartAccessor.updateCartItemStatus(item.getCartItemId(),
                                        CartItem.CART_ITEM_STATUS.SHIPPED_TO_USER);

                                mailProvider.sendShippedEmail(order.getShippingAddress(), order.getBillingAddress().getFirstName(), order.getBillingAddress().getLastName(), order.getCheckoutEmail(), trackingUpdate.getResult().getTracking_code());

                                Map<String, String> workerQueue = new HashMap<>();
                                workerQueue.put("orderId", orderAccessor.getOrderByCartItemId(item.getCartItemId(), IEnums.SEEK_MODE.QUICK).getOrderId().toString());
                                WorkerQueuer.queueWorkForWorker(workerQueue, WorkerController.REFRESH_ORDER_URL);
                            }
                        }
                    }
                }
                //Item was reported delivered, set the status as pending the user's decision
                else if (status == EasyPostPostageLabel.TRACKING_STATUS.DELIVERED)
                {
                    Order order = orderAccessor.getOrderByShippingTrackingNumber(trackingUpdate.getResult().getTracking_code(),
                            IEnums.SEEK_MODE.ORDER_FULL);

                    if (cartItemsInShipment != null && cartItemsInShipment.size() > 0)
                    {
                        for (CartItem item : cartItemsInShipment)
                        {
                            if (item.getCartItemStatus() == CartItem.CART_ITEM_STATUS.PROTOTYPE_SHIPPED_TO_USER)
                            {
                                cartAccessor.updateCartItemStatus(item.getCartItemId(),
                                        CartItem.CART_ITEM_STATUS.PENDING_USER_DECISION);

                                //New item sent, reset the clock on the reminder e-mails
                                orderAccessor.updateCartItemPrototypeEmailStatus(item.getCartItemId(),
                                        CartItem.PROTOTYPE_EMAIL_STATE.NONE_SENT);
                            }
                            else if (item.getCartItemStatus() == CartItem.CART_ITEM_STATUS.SHIPPED_TO_USER)
                            {
                                cartAccessor.updateCartItemStatus(item.getCartItemId(),
                                        CartItem.CART_ITEM_STATUS.COMPLETE);

                                Map<String, String> workerQueue = new HashMap<>();
                                workerQueue.put("orderId", orderAccessor.getOrderByCartItemId(item.getCartItemId(), IEnums.SEEK_MODE.QUICK).getOrderId().toString());
                                WorkerQueuer.queueWorkForWorker(workerQueue, WorkerController.REFRESH_ORDER_URL);
                            }
                        }

                        //FTUE Upsell e-mail on delivery
                        if (order != null && order.getOriginFtue())
                        {
                            String goldImageUrl = ItemImage.getItemImageListSubsetByMaterial(order.getOrderItems().get(0).getCartItemReference().getImages(),
                                    RealityServerManager.GOLD_POLISHED_MATERIAL_MAPPING).get(0).getMediumImageUrlPath();

                            String url = siteUrl + "public/productdescription/" + order.getOrderItems().get(0).getCartItemReference().getItemId();
                            Map<String, String> toMap = new HashMap<>();
                            toMap.put(order.getShippingAddress().getFirstName(), order.getCheckoutEmail());
                            AbstractMap.SimpleEntry<String, String> from = new AbstractMap.SimpleEntry<String, String>("Trove Team", "hello@troveup.com");
                            Map<String, String> vars = new HashMap<>();
                            vars.put("IMAGEURLGOLD", goldImageUrl);
                            vars.put("IMAGEGOLDLINK", url);
                            vars.put("FTUE_URL", url);
                            mailProvider.sendTemplateEmail("Give Your Custom Ring an Upgrade", from, toMap, "ftue-upsell", vars, false);
                        }
                    }
                }
            }
            //New unattached shipping label system, will update GenericItem types to status SHIPPED if they're included in an order attached to this shipping label
            else
            {
                Order order = orderAccessor.getOrderContainingShippingLabel(trackingUpdate.getResult().getTracking_code(), IEnums.SEEK_MODE.ORDER_FULL);

                if (order != null)
                {
                    if (status == EasyPostPostageLabel.TRACKING_STATUS.IN_TRANSIT)
                    {
                        //Order has been shipped, send the user an e-mail update
                        if (order.getCheckoutEmail() != null) {
                            mailProvider.sendShippedEmail(order.getShippingAddress(), order.getBillingAddress().getFirstName(), order.getBillingAddress().getLastName(), order.getCheckoutEmail(), trackingUpdate.getResult().getTracking_code());

                            if (order.getGenericItemsList() != null && order.getGenericItemsList().size() > 0)
                            {
                                for (GenericItem item : order.getGenericItemsList())
                                {
                                    cartAccessor.setGenericItemStatus(item.getGenericItemId(), GenericItem.GENERIC_ITEM_STATUS.SHIPPED);
                                }
                            }
                        }
                    }

                    printOrderAccessor.updatePostageLabelStatus(trackingUpdate.getResult().getTracking_code(), status);
                }
                else
                {
                    logger.info("Easypost label created without a found CartItem or Order reference!  Disregarding, as there is no action to be taken.");
                }
            }
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    /**
     * Cron endpoint for checking outstanding prototypes
     *
     * Rules:
     *
     * First and second prototype: 2 (email 1), 7 (email 2), 14 (email 2), 21 (email 2), 28 (email 2), cancel on day 30 (cancellation email)
     * Charge for first prototype
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/curateoutstandingprototypes", method = RequestMethod.GET)
    public ResponseEntity<String> curateOutstandingPrototypes()
    {
        List<CartItem> outstandingPrototypeCartItems = cartAccessor.
                getOutstandingPrototypeCartItems(20l, IEnums.SEEK_MODE.QUICK);

        for (CartItem item : outstandingPrototypeCartItems)
        {
            if (new Date().after(DateUtils.getDateXDaysFrom(item.getLastStatusChangeDate(), 1)) && item.getPrototypeEmailState() == CartItem.PROTOTYPE_EMAIL_STATE.NONE_SENT)
            {
                Order order = orderAccessor.getOrderByCartItemId(item.getCartItemId(), IEnums.SEEK_MODE.ORDER_ADDRESS);

                AbstractMap.SimpleEntry<String, String> from =
                        new AbstractMap.SimpleEntry<String, String>("Trove Team", "hello@troveup.com");

                Map<String, String> toMap = new HashMap<>();
                toMap.put(order.getBillingAddress().getFirstName() + " " + order.getBillingAddress().getLastName(),
                        order.getCheckoutEmail());

                //Not necessary at this point
                /*Map<String, String> templateVariables = new HashMap<>();
                templateVariables.put("Item_Name", item.getFrozenItemName());
                templateVariables.put("First_name", order.getBillingAddress().getFirstName());
                templateVariables.put("link_to_orders", getSiteUrl() + "private/orders");*/

                MandrillMessageStatus[] status = (MandrillMessageStatus[]) mailProvider.sendTemplateEmail("What To Do With Your Try-On Model", from, toMap, "prototype-instructions",
                        null, false);

                orderAccessor.updateCartItemPrototypeEmailStatus(item.getCartItemId(), CartItem.PROTOTYPE_EMAIL_STATE.DAY_TWO_REMINDER_SENT);

            }

            else if (new Date().after(DateUtils.getDateXDaysFrom(item.getLastStatusChangeDate(), 7)) && item.getPrototypeEmailState() == CartItem.PROTOTYPE_EMAIL_STATE.DAY_TWO_REMINDER_SENT)
            {
                Order order = orderAccessor.getOrderByCartItemId(item.getCartItemId(), IEnums.SEEK_MODE.ORDER_ADDRESS);

                AbstractMap.SimpleEntry<String, String> from =
                        new AbstractMap.SimpleEntry<String, String>("Trove Team", "hello@troveup.com");

                Map<String, String> toMap = new HashMap<>();
                toMap.put(order.getBillingAddress().getFirstName() + " " + order.getBillingAddress().getLastName(),
                        order.getCheckoutEmail());

                Map<String, String> templateVariables = new HashMap<>();
                templateVariables.put("ITEM_NAME", item.getFrozenItemName());
                templateVariables.put("NAME", order.getBillingAddress().getFirstName());

                MandrillMessageStatus[] status = (MandrillMessageStatus[]) mailProvider.sendTemplateEmail("Don't Forget to Confirm Your Trove Design!", from, toMap, "prototype-reminders",
                        templateVariables, false);

                orderAccessor.updateCartItemPrototypeEmailStatus(item.getCartItemId(), CartItem.PROTOTYPE_EMAIL_STATE.DAY_SEVEN_REMINDER_SENT);
            }

            else if (new Date().after(DateUtils.getDateXDaysFrom(item.getLastStatusChangeDate(), 14)) && item.getPrototypeEmailState() == CartItem.PROTOTYPE_EMAIL_STATE.DAY_SEVEN_REMINDER_SENT)
            {
                Order order = orderAccessor.getOrderByCartItemId(item.getCartItemId(), IEnums.SEEK_MODE.ORDER_ADDRESS);

                AbstractMap.SimpleEntry<String, String> from =
                        new AbstractMap.SimpleEntry<String, String>("Trove Team", "hello@troveup.com");

                Map<String, String> toMap = new HashMap<>();
                toMap.put(order.getBillingAddress().getFirstName() + " " + order.getBillingAddress().getLastName(),
                        order.getCheckoutEmail());

                Map<String, String> templateVariables = new HashMap<>();
                templateVariables.put("ITEM_NAME", item.getFrozenItemName());
                templateVariables.put("NAME", order.getBillingAddress().getFirstName());

                MandrillMessageStatus[] status = (MandrillMessageStatus[]) mailProvider.sendTemplateEmail("Don't Forget to Confirm Your Trove Design!", from, toMap, "prototype-reminders",
                        templateVariables, false);

                orderAccessor.updateCartItemPrototypeEmailStatus(item.getCartItemId(), CartItem.PROTOTYPE_EMAIL_STATE.DAY_FOURTEEN_REMINDER_SENT);
            }

            else if (new Date().after(DateUtils.getDateXDaysFrom(item.getLastStatusChangeDate(), 21)) && item.getPrototypeEmailState() == CartItem.PROTOTYPE_EMAIL_STATE.DAY_FOURTEEN_REMINDER_SENT)
            {
                Order order = orderAccessor.getOrderByCartItemId(item.getCartItemId(), IEnums.SEEK_MODE.ORDER_ADDRESS);

                AbstractMap.SimpleEntry<String, String> from =
                        new AbstractMap.SimpleEntry<String, String>("Trove Team", "hello@troveup.com");

                Map<String, String> toMap = new HashMap<>();
                toMap.put(order.getBillingAddress().getFirstName() + " " + order.getBillingAddress().getLastName(),
                        order.getCheckoutEmail());

                Map<String, String> templateVariables = new HashMap<>();
                templateVariables.put("ITEM_NAME", item.getFrozenItemName());
                templateVariables.put("NAME", order.getBillingAddress().getFirstName());

                MandrillMessageStatus[] status = (MandrillMessageStatus[]) mailProvider.sendTemplateEmail("Don't Forget to Confirm Your Trove Design!", from, toMap, "prototype-reminders",
                        templateVariables, false);

                orderAccessor.updateCartItemPrototypeEmailStatus(item.getCartItemId(), CartItem.PROTOTYPE_EMAIL_STATE.DAY_TWENTY_ONE_REMINDER_SENT);
            }

            else if (new Date().after(DateUtils.getDateXDaysFrom(item.getLastStatusChangeDate(), 28)) && item.getPrototypeEmailState() == CartItem.PROTOTYPE_EMAIL_STATE.DAY_TWENTY_ONE_REMINDER_SENT)
            {
                Order order = orderAccessor.getOrderByCartItemId(item.getCartItemId(), IEnums.SEEK_MODE.ORDER_ADDRESS);

                AbstractMap.SimpleEntry<String, String> from =
                        new AbstractMap.SimpleEntry<String, String>("Trove Team", "hello@troveup.com");

                Map<String, String> toMap = new HashMap<>();
                toMap.put(order.getBillingAddress().getFirstName() + " " + order.getBillingAddress().getLastName(),
                        order.getCheckoutEmail());

                Map<String, String> templateVariables = new HashMap<>();
                templateVariables.put("ITEM_NAME", item.getFrozenItemName());

                MandrillMessageStatus[] status = (MandrillMessageStatus[]) mailProvider.sendTemplateEmail("Last Chance! Confirm Your Trove Design Today", from, toMap, "prototype-remidners-final",
                        templateVariables, false);

                orderAccessor.updateCartItemPrototypeEmailStatus(item.getCartItemId(), CartItem.PROTOTYPE_EMAIL_STATE.DAY_TWENTY_EIGHT_REMINDER_SENT);
            }

            else if (new Date().after(DateUtils.getDateXDaysFrom(item.getLastStatusChangeDate(), 45)) && item.getPrototypeEmailState() == CartItem.PROTOTYPE_EMAIL_STATE.DAY_TWENTY_EIGHT_REMINDER_SENT)
            {
                Order order = orderAccessor.getOrderByCartItemId(item.getCartItemId(), IEnums.SEEK_MODE.ORDER_ADDRESS);

                AbstractMap.SimpleEntry<String, String> from =
                        new AbstractMap.SimpleEntry<String, String>("Trove Team", "hello@troveup.com");

                Map<String, String> toMap = new HashMap<>();
                toMap.put(order.getBillingAddress().getFirstName() + " " + order.getBillingAddress().getLastName(),
                        order.getCheckoutEmail());

                Map<String, String> templateVariables = new HashMap<>();
                templateVariables.put("ITEM_NAME", item.getFrozenItemName());
                templateVariables.put("Name", order.getBillingAddress().getFirstName());
                templateVariables.put("Price", MoneyUtil.toProperScale(item.getActualPrice(), null).toString());
                templateVariables.put("Material", item.getMaterialName() + " " + item.getFinishName());

                MandrillMessageStatus[] status = (MandrillMessageStatus[]) mailProvider.sendTemplateEmail("Your order has been successfully cancelled", from, toMap, "order-cancellation",
                        templateVariables, false);

                orderAccessor.updateCartItemPrototypeEmailStatus(item.getCartItemId(), CartItem.PROTOTYPE_EMAIL_STATE.DAY_THIRTY_CANCELLATION_EMAIL_SENT);

                //Cancel the order
                cartAccessor.updateCartItemStatus(item.getCartItemId(), CartItem.CART_ITEM_STATUS.CANCELLED);
                Map<String, String> workerQueue = new HashMap<>();
                workerQueue.put("orderId", orderAccessor.getOrderByCartItemId(item.getCartItemId(), IEnums.SEEK_MODE.QUICK).getOrderId().toString());
                WorkerQueuer.queueWorkForWorker(workerQueue, WorkerController.REFRESH_ORDER_URL);

            }
            else {

                //Update the record so that it doesn't pull again for another 12 hours
                cartAccessor.updateCartItemLastOutstandingPrototypeCheckDate(item.getCartItemId(), new Date());
            }
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/refreshorderstatus", method = RequestMethod.POST)
    public ResponseEntity<String> refreshOrderStatus(@RequestParam("orderId") final String orderId,
                                                                   HttpServletRequest request)
    {
        Order order = orderAccessor.getOrder(Long.parseLong(orderId), IEnums.SEEK_MODE.ORDER_DETAILED);

        Boolean canSettleOrder = true;

        List<CartItem.CART_ITEM_STATUS> statusesThatCanBeClosed = new ArrayList<>();
        statusesThatCanBeClosed.add(CartItem.CART_ITEM_STATUS.COMPLETE);
        statusesThatCanBeClosed.add(CartItem.CART_ITEM_STATUS.CANCELLED);
        statusesThatCanBeClosed.add(CartItem.CART_ITEM_STATUS.SHIPPED_TO_USER);
        statusesThatCanBeClosed.add(CartItem.CART_ITEM_STATUS.ORDERED);

        for (CartItem item : order.getOrderItems())
        {
            if (!statusesThatCanBeClosed.contains(item.getCartItemStatus()))
            {
                canSettleOrder = false;
            }
        }

        //If this order is open and can be settled, start the settlement process
        if (canSettleOrder && order.getOrderStatus().equals(Order.ORDER_STATUS.OPEN))
        {
            orderAccessor.updateOrderStatus(Long.parseLong(orderId), Order.ORDER_STATUS.SETTLING);
        }
        //If the order has already been settled, go ahead and close it
        else if (canSettleOrder && order.getOrderStatus().equals(Order.ORDER_STATUS.SETTLED))
        {
            orderAccessor.updateOrderStatus(Long.parseLong(orderId), Order.ORDER_STATUS.CLOSED);
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/submitrendertoqueue", method = RequestMethod.POST)
    public ResponseEntity<String> submitRealityServerRenderToQueue(@RequestParam("itemId") final String itemId,
                                                                   @RequestParam("renderType") final Render.RENDER_TYPE renderType,
                                                                   HttpServletRequest request)
    {


        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        if(request.getRemoteAddr().equals(GOOGLE_APP_ENGINE_TASK_QUEUE_IP) ||
                SystemProperty.environment.value() == SystemProperty.Environment.Value.Development)
        {

            FtuePersistedRecord record = null;
            Item item;
            List<CustomizerOperator> operatorWeights = new ArrayList<>();
            List<String> visibleMeshes = new ArrayList<>();

            if (renderType.equals(Render.RENDER_TYPE.FTUE))
            {
                record = ftueAccessor.getPersistedRecord(Long.parseLong(itemId),
                        IEnums.SEEK_MODE.FTUE_IMAGE_SUBMIT);
                item = itemAccessor.getItemDetached(record.getFtueModelId(), IEnums.SEEK_MODE.ITEM_ATTRIBUTES);
                operatorWeights = record.getExporterCompatibleWeights();

                for (FtueModelWeights weight : record.getModelWeights()) {
                    if (weight.getWeightId() != null)
                        operatorWeights.add(new CustomizerOperator(weight.getWeightId(), weight.getWeightValue()));
                    else if (weight.getVisibleMesh() != null)
                        visibleMeshes.add(weight.getVisibleMesh());
                }
            }
            else
            {
                item = itemAccessor.getItemDetached(Long.parseLong(itemId), IEnums.SEEK_MODE.ITEM_ATTRIBUTES);

                visibleMeshes = item.getVisibleMeshes();

                //New hotness
                if (item.getItemCustomizations() == null && item.getItemAttributes() != null)
                {
                    item.populateItemCustomizationsFromAttributes();
                }

                operatorWeights = item.getOperatorsFromCustomizations();
            }

            String exportedModelUUID = UUID.randomUUID().toString();
            String exportedModelName = "render" + "-" + exportedModelUUID + ".obj";

            ForgeMeshExportResponse response = forgeExporter.exportBlenderMesh(visibleMeshes,
                    SizeMapper.getMediumExportSize(item.getCategory()),
                    operatorWeights, item.getCustomizerPath() + item.getCustomizerFilename(),
                    cdnBucketName, jsonBucketName, exportedModelName.replace(" ", "_"), true);

            //We had a bad export request, send it back through the queue
            if (response == null || !response.getSuccess())
                return rval;

            List<Render> materialRenders = new ArrayList<>();

            for (String material : Render.availableMaterials()) {
                Render render = new Render();
                render.setRenderStatus(IRenderQueueAccessor.RENDER_STATUS.QUEUED);

                if (renderType.equals(Render.RENDER_TYPE.FTUE))
                {
                    render.setFtueReference(record);
                }
                else
                {
                    render.setItemReference(item);
                }

                render.setRenderType(renderType);
                render.setJobId(UUID.randomUUID().toString().substring(0, 7).toUpperCase());
                render.setUUID(exportedModelUUID);
                render.setScene(item.getRenderScene());
                render.setRenderMaterial(material);
                render.setDateQueued(new Date());
                render.setModelUrl(cdnBucketUrl + exportedModelName);

                materialRenders.add(render);
            }

            if (renderQueueAccessor.submitRenders(materialRenders)) {
                feedAccessor.addItemToFeedQueue(Long.parseLong(itemId));
                rval = new ResponseEntity<String>(HttpStatus.OK);
            }
        }

        return rval;
    }

    /**
     * Task worker endpoint that uploads models to manufacturers
     *
     * @param uploadType Manufacturer to be uploaded to, should be SHAPEWAYS or IMATERIALISE
     * @param checkoutSystem Type of checkout system used, FTUE or CART
     * @param orderNumber Unique order identifier from the order to upload
     * @param request
     * @return
     */
    @RequestMapping(value = "/uploadmodel", method = RequestMethod.POST)
    public ResponseEntity<String> uploadModelToManufacturer(@RequestParam("uploadType") final String uploadType,
                                                            @RequestParam("checkoutSystem") final String checkoutSystem,
                                                            @RequestParam("orderNumber") final String orderNumber,
                                                            HttpServletRequest request) {

        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

        if(request.getRemoteAddr().equals(GOOGLE_APP_ENGINE_TASK_QUEUE_IP) ||
                SystemProperty.environment.value() == SystemProperty.Environment.Value.Development) {
            List<ManufacturerUpload> modelsToUpload = new ArrayList<>();

            //Uploading from a FTUE based order
            if (checkoutSystem.equals(CHECKOUT_SYSTEM_FTUE)) {
                ManufacturerUpload uploadContainer = new ManufacturerUpload();

                List<CustomizerOperator> weights = new ArrayList<>();
                List<String> visibleMeshes = new ArrayList<>();

                FtueCheckout checkoutObject = ftueAccessor.
                        getPersistedFtueCheckoutRecord(orderNumber, IEnums.SEEK_MODE.FTUECHECKOUT_FULL);

                uploadContainer.setItem(itemAccessor.getItemDetached(checkoutObject.getRecordReference().getFtueModelId(),
                        IEnums.SEEK_MODE.QUICK));

                for (FtueModelWeights weight : checkoutObject.getRecordReference().getModelWeights()) {
                    if (weight.getWeightId() != null)
                        weights.add(new CustomizerOperator(weight.getWeightId(), weight.getWeightValue()));
                    else if (weight.getVisibleMesh() != null)
                        visibleMeshes.add(weight.getVisibleMesh());
                }

                uploadContainer.setWeights(weights);
                uploadContainer.setVisibleMeshes(visibleMeshes);
                uploadContainer.setSize(SizeMapper.getExporterSize(uploadContainer.getItem().getCategory(),
                        checkoutObject.getSize()));

                uploadContainer.setUploadFileName(checkoutObject.getShippingAddress().getFirstName() + "_" +
                        checkoutObject.getShippingAddress().getLastName() + "_" + UUID.randomUUID().toString() + ".obj");

                uploadContainer.setCheckoutSystem(CHECKOUT_SYSTEM_FTUE);

                modelsToUpload.add(uploadContainer);


            }
            //Upload from a prototype order
            else if (checkoutSystem.equals(CHECKOUT_SYSTEM_FTUE_PROTOTYPE)) {
                ManufacturerUpload uploadContainer = new ManufacturerUpload();

                //We have this one for free, as it was already exported.  Go grab the file and include it in the
                //upload container.

                FtuePersistedRecord persistedRecord = ftueAccessor.
                        getPersistedRecord(Long.parseLong(orderNumber), IEnums.SEEK_MODE.FTUE_PROTOTYPE);

                uploadContainer.setItem(itemAccessor.getItemDetached(persistedRecord.getFtueModelId(),
                        IEnums.SEEK_MODE.QUICK));

                List<CustomizerOperator> weights = new ArrayList<>();
                List<String> visibleMeshes = new ArrayList<>();

                for (FtueModelWeights weight : persistedRecord.getModelWeights()) {
                    if (weight.getWeightId() != null)
                        weights.add(new CustomizerOperator(weight.getWeightId(), weight.getWeightValue()));
                    else if (weight.getVisibleMesh() != null)
                        visibleMeshes.add(weight.getVisibleMesh());
                }

                uploadContainer.setWeights(weights);
                uploadContainer.setVisibleMeshes(visibleMeshes);
                uploadContainer.setSize(SizeMapper.getExporterSize(uploadContainer.getItem().getCategory(),
                        persistedRecord.getSize()));

                uploadContainer.setUploadFileName(persistedRecord.getRequest().getShipping_info().getName().replace(" ", "_") + "_" +
                        UUID.randomUUID().toString() + ".obj");

                uploadContainer.setCheckoutSystem(CHECKOUT_SYSTEM_FTUE_PROTOTYPE);

                modelsToUpload.add(uploadContainer);
            }
            //Uploading from cart based order
            else
            {
                ManufacturerUpload uploadContainer = new ManufacturerUpload();

                CartItem cartItem = cartAccessor.getCartItem(Long.parseLong(orderNumber), IEnums.SEEK_MODE.CART_ITEM_UPLOAD);

                List<String> visibleMeshes = new ArrayList<>();
                List<CustomizerOperator> weights = new ArrayList<>();

                //Pull customizations from the cart item
                if (cartItem.getCustomizations() != null)
                {
                    visibleMeshes = cartItem.getActiveMeshes(cartItem.getCustomizationIteration());
                    weights = cartItem.getOperatorsFromCustomizations(cartItem.getCustomizationIteration());
                }

                uploadContainer.setWeights(weights);
                uploadContainer.setVisibleMeshes(visibleMeshes);
                uploadContainer.setSize(SizeMapper.getExporterSize(cartItem.getCartItemReference().getCategory(),
                        cartItem.getSize(cartItem.getCustomizationIteration())));

                uploadContainer.setOrderCartItem(cartItem);
                uploadContainer.setItem(cartItem.getCartItemReference());
                uploadContainer.setUploadFileName(orderAccessor.
                        getOrderFirstLastNameByCartItemId(cartItem.getCartItemId()) + "_" + UUID.randomUUID().toString() + ".obj");

                modelsToUpload.add(uploadContainer);

            }

            for (ManufacturerUpload upload : modelsToUpload)
            {
                if (upload.getExportedModel() == null || upload.getExportedModel().length == 0)
                {
                    ForgeMeshExportResponse export = forgeExporter.exportBlenderMesh(upload.getVisibleMeshes(),
                            upload.getSize(), upload.getWeights(), upload.getItem().getCustomizerPath() +
                                    upload.getItem().getHighResolutionCustomizerFilename(), cdnBucketName, jsonBucketName,
                            upload.getUploadFileName().replace(" ", "_"), false);

                    logger.debug("Upload size was: " + upload.getSize() + " " + "for mesh at " + upload.getItem().getCustomizerFilename());

                    String modelData = getModelFile(export.getExportURL());

                    upload.setExportedModel(modelData.getBytes());
                    upload.setModelUrl(export.getExportURL());

                    //Export failed,
                    if (!export.getSuccess())
                        throw new RuntimeException("Export failed on upload attempt for orderNumber " +
                                orderNumber + "of checkoutSystem " + checkoutSystem + ", message was " +
                                export.getMessage() + ".  Error was " + export.getError());
                }
            }

            //Uploading to Shapeways
            if (uploadType.equals(UPLOAD_TYPE_SHAPEWAYS)) {
                List<SupplierModelIdAndName> manufacturerOrderItemMap = new ArrayList<>();
                for (ManufacturerUpload upload : modelsToUpload) {
                    ShapewaysUploadResponse response =
                            (ShapewaysUploadResponse) printSupplier.uploadModel(upload.getExportedModel(),
                                    upload.getUploadFileName());

                    //Throw an exception so that it retries the upload later
                    if (response == null || response.getResult() == null || !response.getResult().equals("success"))
                    {
                        String exceptionString = "Shapeways upload failure!  Order number: " + orderNumber;

                        if (response != null && response.getResult() != null)
                            exceptionString += ". Response was " + response.getResult();
                        else
                            exceptionString += ". Response was null!";

                        throw new RuntimeException(exceptionString);


                    } else {

                        SupplierModelIdAndName modelIdAndName = new SupplierModelIdAndName();
                        modelIdAndName.setSupplierModelId(response.getModelId());
                        modelIdAndName.setSupplierModelName(upload.getUploadFileName());

                        manufacturerOrderItemMap.add(modelIdAndName);

                        PrintOrder order = new PrintOrder();
                        order.setStatus(PrintOrder.ORDER_STATUS.UPLOADING);
                        order.setPrintSupplier(PrintOrder.PRINT_SUPPLIER.SHAPEWAYS);
                        order.setSupplierModelIdAndNames(manufacturerOrderItemMap);
                        order.setModelUrl(upload.getModelUrl());

                        logger.debug("Checkout system variable value: " + checkoutSystem);

                        //Assuming there's only one record for FTUE, for either case.

                        //Associate this with a FTUE record
                        if (checkoutSystem.equals(CHECKOUT_SYSTEM_FTUE)) {
                            order.setOrderSystem(PrintOrder.ORDER_SYSTEM.FTUE);
                            ftueAccessor.setCheckoutPrintOrder(orderNumber, order);
                        } else if (checkoutSystem.equals(CHECKOUT_SYSTEM_FTUE_PROTOTYPE)) {
                            order.setOrderSystem(PrintOrder.ORDER_SYSTEM.FTUE_PROTOTYPE);
                            ftueAccessor.setPersistedRecordSampleSupplierOrder(Long.parseLong(orderNumber), order);
                        }
                        //Associate this with an order record
                        else {

                            if (upload.getOrderCartItem().getCartItemStatus() == CartItem.CART_ITEM_STATUS.ON_HOLD_PROTOTYPE) {
                                cartAccessor.updateCartItemStatus(upload.getOrderCartItem().getCartItemId(), CartItem.CART_ITEM_STATUS.PROTOTYPE_READY_FOR_UPLOAD);
                                order.setIsPrototype(true);
                            }
                            else {
                                cartAccessor.updateCartItemStatus(upload.getOrderCartItem().getCartItemId(), CartItem.CART_ITEM_STATUS.READY_FOR_UPLOAD);
                                order.setIsPrototype(false);
                            }

                            order.setOrderSystem(PrintOrder.ORDER_SYSTEM.CART);
                            order.setOrderReference(orderAccessor.getOrderByCartItemId(upload.getOrderCartItem().getCartItemId(), IEnums.SEEK_MODE.QUICK));
                            cartAccessor.addCartItemPrintOrder(upload.getOrderCartItem().getCartItemId(), order);

                        }
                    }
                }

                rval = new ResponseEntity<String>(HttpStatus.OK);

            }
            //TODO:  Implement iMaterialise upload procedures here
            else
            {

            }
        }

        return rval;
    }

    /**
     * Cron endpoint that checks the status of models that have been uploaded to a given manufacturer.  Currently only
     * implemented for Shapeways.
     *
     * @return Http response code of OK if everything went well.
     */
    @RequestMapping(value = "/curatemodeluploads", method = RequestMethod.GET)
    public ResponseEntity<String> curateModelUploads()
    {
        List<PrintOrder> printOrderList = printOrderAccessor.getPrintOrdersByStatus(PrintOrder.ORDER_STATUS.UPLOADING,
                IEnums.SEEK_MODE.FULL, 5l);

        logger.debug("Size of print order list that came back: " + printOrderList.size());
        //TODO:  Adapt this to iMaterialise when the time comes.
        if (printOrderList != null && printOrderList.size() > 0)
        {
            for (PrintOrder order : printOrderList)
            {
                Boolean successUpload = true;
                Boolean wasError = false;
                for (SupplierModelIdAndName modelIdAndName : order.getSupplierModelIdAndNames())
                {
                    logger.debug("Inside orderlist loop, this model's ID and name was " +
                            modelIdAndName.getSupplierModelId() + " " + modelIdAndName.getSupplierModelName());

                    ShapewaysGetModelResponse response = (ShapewaysGetModelResponse)
                            printSupplier.checkUploadStatus(Integer.parseInt(modelIdAndName.getSupplierModelId()));

                    logger.debug("Response came back with printable as " + response.getPrintable());

                    if (!response.getPrintable().equals("yes"))
                    {
                        successUpload = false;
                    }

                    if (response.getPrintable().equals("no"))
                    {
                        wasError = true;

                        printOrderAccessor.updatePrintOrderStatus(order.getPrintOrderPrimaryKeyId(),
                                PrintOrder.ORDER_STATUS.ERROR);

                        printOrderAccessor.updatePrintOrderManufacturerStatus(order.getPrintOrderPrimaryKeyId(),
                                "One or more models associated with this order are unprintable.");

                        StringBuilder builder = new StringBuilder();
                        builder.append("Unprintable model uploaded to Shapeways!  Shapeways model Id: " +
                                modelIdAndName.getSupplierModelId() +
                                ".  " + "Print order ID: " + order.getPrintOrderPrimaryKeyId());


                        Map<String, String> founders = new HashMap<>();
                        founders.put("Tim", "tim@troveup.com");

                        if (!isDevelopmentServer()) {
                            founders.put("Brian", "brian@troveup.com");
                            founders.put("Andrew", "andrew@troveup.com");
                        }

                        mailProvider.sendRawEmail("Shapeways Model Upload Failure Notification", "errors@troveup.com",
                                founders, builder.toString(), false);

                    }

                }

                if (successUpload && !wasError &&
                        printOrderAccessor.
                                getPrintOrderStatus(order.getPrintOrderPrimaryKeyId()) != PrintOrder.ORDER_STATUS.CANCELLED)
                {
                    printOrderAccessor.updatePrintOrderStatus(order.getPrintOrderPrimaryKeyId(),
                            PrintOrder.ORDER_STATUS.UPLOADED);
                }
            }
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/curatependingordersubmissions", method = RequestMethod.GET)
    public ResponseEntity<String> curatePendingOrderSubmissions()
    {
        List<PrintOrder> orders = printOrderAccessor.getPrintOrdersAwaitingSubmission(IEnums.SEEK_MODE.FULL, 5l);

        if (orders != null && orders.size() > 0)
        {
            for (PrintOrder order : orders)
            {
                List<ShapewaysItem> orderItems = new ArrayList<>();
                CartItem cartItem = null;
                String firstName;
                String lastName;
                String orderNumber;

                if (order.getOrderSystem() == PrintOrder.ORDER_SYSTEM.FTUE)
                {
                    for (SupplierModelIdAndName modelIdAndName : order.getSupplierModelIdAndNames())
                    {
                        ShapewaysItem item = new ShapewaysItem();
                        item.setModelId(Integer.parseInt(modelIdAndName.getSupplierModelId()));
                        item.setMaterialId(Integer.parseInt(order.getCheckoutReference().getFinishId()));
                        orderItems.add(item);
                    }

                    firstName = order.getCheckoutReference().getShippingAddress().getFirstName();
                    lastName = order.getCheckoutReference().getShippingAddress().getLastName();
                    orderNumber = order.getCheckoutReference().getCheckoutSessionId();

                }
                else if (order.getOrderSystem() == PrintOrder.ORDER_SYSTEM.FTUE_PROTOTYPE)
                {
                    Item baseItem = itemAccessor.getItemDetached(order.getPersistedRecordReference().getFtueModelId(),
                            IEnums.SEEK_MODE.QUICK);

                    for (SupplierModelIdAndName modelIdAndName : order.getSupplierModelIdAndNames())
                    {
                        ShapewaysItem item = new ShapewaysItem();
                        item.setModelId(Integer.parseInt(modelIdAndName.getSupplierModelId()));
                        item.setMaterialId(Integer.parseInt(baseItem.getSampleMaterialId()));
                        orderItems.add(item);
                    }

                    String name = order.getPersistedRecordReference().getRequest().getShipping_info().getName();

                    if (name.contains(" "))
                    {
                        String[] nameSplit = name.split(" ");
                        firstName = nameSplit[0];
                        lastName = nameSplit[1];
                    }
                    else
                    {
                        firstName = name;
                        lastName = "nolast";
                    }

                    orderNumber = order.getPersistedRecordReference().getFtuePersistedRecordId().toString();
                }
                else
                {
                    Order troveOrder = order.getOrderReference();

                    cartItem =
                            cartAccessor.getCartItemByPrintOrder(order.getPrintOrderPrimaryKeyId(),
                                    IEnums.SEEK_MODE.CART_ITEM_ORDER_SUBMIT);


                    ShapewaysItem shapewaysItem = new ShapewaysItem();

                    //Get the last item that was uploaded, as it should be the latest
                    shapewaysItem.setModelId(Integer.parseInt(order.getSupplierModelIdAndNames().
                            get(order.getSupplierModelIdAndNames().size() - 1).getSupplierModelId()));

                    firstName = troveOrder.getShippingAddress().getFirstName();
                    lastName = troveOrder.getShippingAddress().getLastName();
                    orderNumber = troveOrder.getTroveOrderNumber();

                    //Prototype, order it in plastic
                    if (cartItem.getCartItemStatus() == CartItem.CART_ITEM_STATUS.PROTOTYPE_READY_FOR_UPLOAD)
                    {
                        shapewaysItem.setMaterialId(Integer.parseInt(cartItem.getCartItemReference().getSampleMaterialId()));
                    }
                    else
                    {
                        shapewaysItem.setMaterialId(Integer.parseInt(cartItem.getFinishId()));
                    }

                    orderItems.add(shapewaysItem);
                }

                ShapewaysOrderResponse response = (ShapewaysOrderResponse) printSupplier.submitOrder(firstName, lastName, orderItems);

                if (response != null && response.getResult() != null && response.getResult().equals("success"))
                {
                    printOrderAccessor.updatePrintOrderManufacturerOrderNumber(order.getPrintOrderPrimaryKeyId(),
                            response.getOrderId().toString());

                    //Print orders will probably never be cancelled, needs to be based on cart item status instead
                    //if (printOrderAccessor.getPrintOrderStatus(order.getPrintOrderPrimaryKeyId()) !=
                            //PrintOrder.ORDER_STATUS.CANCELLED)
                    if (cartAccessor.getCartItemByPrintOrder(order.getPrintOrderPrimaryKeyId(),
                            IEnums.SEEK_MODE.QUICK).getCartItemStatus() != CartItem.CART_ITEM_STATUS.CANCELLED)
                    {

                        printOrderAccessor.updatePrintOrderStatus(order.getPrintOrderPrimaryKeyId(),
                                PrintOrder.ORDER_STATUS.SUBMITTED);

                        if (order.getOrderSystem() == PrintOrder.ORDER_SYSTEM.FTUE)
                        {
                            //Ftue checkout order
                            ftueAccessor.updateFtueCheckoutStatus(orderNumber, FtueCheckout.FTUE_CHECKOUT_STATUS.COMPLETE);

                        }
                        else if (order.getOrderSystem() == PrintOrder.ORDER_SYSTEM.FTUE_PROTOTYPE)
                        {
                            //Shapeways prototype order
                            ftueAccessor.updateFtueStatus(order.getPersistedRecordReference().getFtuePersistedRecordId(),
                                    FtuePersistedRecord.FTUE_STATUS.SUBMITTED_TO_SAMPLE_SUPPLIER);
                        }
                        else
                        {
                            //Cart checkout order
                            if (cartItem != null)
                            {
                                if (cartItem.getCartItemStatus() == CartItem.CART_ITEM_STATUS.PROTOTYPE_READY_FOR_UPLOAD)
                                {
                                    cartAccessor.updateCartItemStatus(cartItem.getCartItemId(),
                                            CartItem.CART_ITEM_STATUS.PROTOTYPE_ORDERED);
                                }
                                else
                                {
                                    cartAccessor.updateCartItemStatus(cartItem.getCartItemId(),
                                            CartItem.CART_ITEM_STATUS.ORDERED);

                                    Map<String, String> workerQueue = new HashMap<>();
                                    workerQueue.put("orderId", orderAccessor.getOrderByCartItemId(cartItem.getCartItemId(), IEnums.SEEK_MODE.QUICK).getOrderId().toString());
                                    WorkerQueuer.queueWorkForWorker(workerQueue, WorkerController.REFRESH_ORDER_URL);
                                }
                            }
                        }
                    }
                    //Edge case race condition wherein the user cancelled their order just after it was submitted,
                    //and the submission made it.  Wouldn't be caught until after it was printed, costing Trove money.
                    //Account for this case.
                    else
                    {
                        ShapewaysCancelOrderResponse cancelOrderResponse =
                                (ShapewaysCancelOrderResponse)
                                        printSupplier.cancelOrder(response.getOrderId().toString());

                        if (cancelOrderResponse != null && cancelOrderResponse.getResult() != null && cancelOrderResponse.getResult().equals("success"))
                        {
                            printOrderAccessor.updatePrintOrderStatus(response.getOrderId().toString(), PrintOrder.ORDER_STATUS.CANCELLED);
                        }
                        else
                        {
                            String errorString = "Shapeways edge case cancellation for Manufacturer order id " + response.getOrderId() + " failed with return status ";

                            if (cancelOrderResponse == null)
                                errorString += "null";
                            else
                                errorString += cancelOrderResponse.getReason();

                            logger.error(errorString);


                            StringBuilder builder = new StringBuilder();
                            builder.append("Order cancellation failure for ");
                            builder.append(orderNumber);
                            builder.append(".  Print order ID: ");
                            builder.append(order.getPrintOrderPrimaryKeyId());
                            builder.append("  Edge case where user cancelled order just before cron submission and ");
                            builder.append("it wasn't caught.  Automatic attempt at cancellation seems to have failed.");
                            builder.append("  Please check this order manually to ensure that order cancellation happened properly");

                            Map<String, String> founders = new HashMap<>();
                            founders.put("Tim", "tim@troveup.com");
                            founders.put("Brian", "brian@troveup.com");
                            founders.put("Andrew", "andrew@troveup.com");

                            mailProvider.sendRawEmail("Shapeways Order Cancellation Failure Notification", "errors@troveup.com",
                                    founders, builder.toString(), false);

                            printOrderAccessor.updatePrintOrderStatus(response.getOrderId().toString(), PrintOrder.ORDER_STATUS.CANCELLATION_ERROR);
                        }
                    }

                }
                else
                {

                    String statusString = "Shapeways order submission failure!  Order number: " + orderNumber;

                    //If Shapeways was down for some reason, attempt 3 more times.  Otherwise, e-mail out for manual
                    //intervention
                    if (order.getOrderSubmitAttemptCount() == null || order.getOrderSubmitAttemptCount() < 3)
                    {

                        //Null check, as this is a new feature and prior records may not have this field filled
                        if (order.getOrderSubmitAttemptCount() == null)
                            order.setOrderSubmitAttemptCount(0);

                        //Update the order status to the error, but keep it at the "UPLOADED" status, so that it'll try again
                        printOrderAccessor.updatePrintOrderManufacturerStatus(order.getPrintOrderPrimaryKeyId(),
                                statusString);

                        //Increment the counter so that this doesn't keep trying into perpetuity
                        printOrderAccessor.updateOrderSubmitAttemptCount(order.getPrintOrderPrimaryKeyId(),
                                order.getOrderSubmitAttemptCount() + 1);

                        StringBuilder builder = new StringBuilder();
                        builder.append("Order submission failure to Shapeways!  Order number was ");
                        builder.append(orderNumber);
                        builder.append(".  Print order ID: ");
                        builder.append(order.getPrintOrderPrimaryKeyId());
                        builder.append(". This order is on attempt ");
                        builder.append(order.getOrderSubmitAttemptCount());
                        builder.append(" of 3.  Environment: ");
                        builder.append(getSiteUrl());

                        Map<String, String> founders = new HashMap<>();
                        founders.put("Tim", "api@troveup.com");

                        mailProvider.sendRawEmail("Shapeways Order Submission Failure Notification", "errors@troveup.com",
                                founders, builder.toString(), false);
                    }
                    else
                    {
                        printOrderAccessor.updateOrderSubmitAttemptCount(order.getPrintOrderPrimaryKeyId(),
                                order.getOrderSubmitAttemptCount() + 1);

                        if (response != null && response.getResult() != null)
                            statusString += ". Response was " + response.getResult();
                        else
                            statusString += ". Response was null!";

                        printOrderAccessor.updatePrintOrderStatus(order.getPrintOrderPrimaryKeyId(),
                                PrintOrder.ORDER_STATUS.ERROR);
                        printOrderAccessor.updatePrintOrderManufacturerStatus(order.getPrintOrderPrimaryKeyId(),
                                statusString);

                        String errorString = "Order submission failure to Shapeways!  Order number was " + orderNumber +
                                ".  " + "Print order ID: " + order.getPrintOrderPrimaryKeyId() + ". This was the final attempt." +
                                "Please submit this order manually.  Environment: " + getSiteUrl();

                        Map<String, String> founders = new HashMap<>();
                        founders.put("Tim", "tim@troveup.com");
                        founders.put("Brian", "brian@troveup.com");
                        founders.put("Andrew", "andrew@troveup.com");

                        mailProvider.sendRawEmail("Shapeways Order Submission Failure Notification", "errors@troveup.com",
                                founders, errorString, false);
                    }
                }
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/settleorders", method = RequestMethod.GET)
    public ResponseEntity<String> settleOutstandingOrders()
    {
        List<Order> ordersReadyForSettlement = orderAccessor.getOrdersByStatus(Order.ORDER_STATUS.SETTLING, 20l,
                IEnums.SEEK_MODE.ORDER_SETTLE);

        if (ordersReadyForSettlement != null && ordersReadyForSettlement.size() > 0)
        {
            //There are three different cases, one where there are no prototypes or cancellations,
            //One where there are no prototypes and all cancellations, and one where there is a mixed bag of
            //prototypes, regular orders, and cancellations.

            for (Order order : ordersReadyForSettlement)
            {
                logger.info("Processing settlement on order " + order.getOrderId());
                try {
                    //Make sure this was an order that the user actually paid for.  If they didn't (case where promo code made the order free),
                    //bypass this otherwise braintree would explode.
                    if (order.getPaymentProcessorTransactionId() != null && !order.getPaymentProcessorTransactionId().equals("FREE")) {
                        BigDecimal finalSettlementSubtotal = order.getSubTotal();
                        Result<Transaction> transactionResult = null;
                        BigDecimal finalSettlementAmount = order.getGrandTotal();
                        BigDecimal finalSettlementTax = order.getTax();
                        BigDecimal finalSettlementShipping = order.getShipping();
                        BigDecimal storeCreditToRefund = BigDecimal.ZERO;
                        Boolean recalculateTotals = false;
                        User user = userAccessor.getUserByOrderId(order.getOrderId(), IEnums.SEEK_MODE.USER_CART_QUICK);

                        List<CartItem> cancelledItems = new ArrayList<>();

                        if (order.getOrderItems() != null && order.getOrderItems().size() > 0) {
                            //Iterate through all of the items and first decide which of the above three cases this ends up being
                            for (CartItem item : order.getOrderItems()) {
                                if (item.getCartItemStatus() == CartItem.CART_ITEM_STATUS.CANCELLED)
                                {
                                    cancelledItems.add(item);
                                }
                            }

                            Money orderSubtotal = MoneyUtil.bigDecimalToMoney(null, order.getSubTotal());
                            Money totalToSubtract = Money.zero(CurrencyUnit.USD);

                            if (cancelledItems.size() > 0) {
                                recalculateTotals = true;
                                for (CartItem item : cancelledItems) {
                                    if (item.getPrototypeRequested()) {
                                        //If there were prototypes requested, refund the amount less $7.49 per prototype
                                        Money lessPrototypeCost = MoneyUtil.bigDecimalToMoney(null, item.getActualPrice());
                                        lessPrototypeCost = lessPrototypeCost.minus(ShapewaysPriceApi.getFlatPrototypePrice().multiply(new BigDecimal(item.getCustomizationIteration())), RoundingMode.HALF_UP);

                                        if (lessPrototypeCost.isPositive())
                                            totalToSubtract = totalToSubtract.plus(lessPrototypeCost);
                                    } else {
                                        totalToSubtract = totalToSubtract.plus(MoneyUtil.bigDecimalToMoney(null, item.getActualPrice()));
                                    }

                                    //Account for refund on the chain
                                    if (item.getChain() != null) {
                                        totalToSubtract = totalToSubtract.plus(MoneyUtil.bigDecimalToMoney(null, item.getChain().getPrice()));
                                    }
                                }
                            }

                            //If there isn't anymore items left, and there aren't any generic items, refund giftwrapping if necessary
                            //TODO:  Differentiate here between generic items and Gift Card specific
                            if (order.getOrderItems().size() == cancelledItems.size() &&
                                    (order.getGenericItemsList() == null || order.getGenericItemsList().size() == 0) &&
                                    order.getShouldBeGiftWrapped()) {
                                totalToSubtract = totalToSubtract.plus(new BigDecimal(3.99), RoundingMode.UP);
                            }

                            if (orderSubtotal.isGreaterThan(totalToSubtract)) {
                                logger.debug("Order subtotal wasn't zero, subtracting " + totalToSubtract.getAmount().toString() + " from " + orderSubtotal.getAmount().toString());
                                orderSubtotal = orderSubtotal.minus(totalToSubtract);
                            } else {
                                orderSubtotal = Money.zero(CurrencyUnit.USD);
                                logger.debug("Order subtotal wasn't greater than the amount being subtracted, setting it to zero.");
                            }

                            finalSettlementSubtotal = orderSubtotal.getAmount();

                            if (orderSubtotal.isGreaterThan(Money.zero(CurrencyUnit.USD))) {
                                logger.debug("Order subtotal was greater than zero, final settlement amount calculated to be " + finalSettlementAmount.toString());
                            } else {
                                logger.debug("Final settlement amount was zero.");
                            }
                        }

                        //There was a cancellation of some sort, need to recalculate tax and the final settlement amount
                        //based on whatever else is in the order outside of jewelry
                        if (recalculateTotals)
                        {
                            AvaGetTaxResult taxResult = (AvaGetTaxResult) taxHandler.getOrderTaxes(order, user.getUserId(), false);
                            finalSettlementTax = MoneyUtil.floatToBigDecimal(taxResult.getTotalTax(), null);
                            finalSettlementAmount = finalSettlementSubtotal
                                    .add(finalSettlementTax)
                                    .add(finalSettlementShipping);
                        }


                        //If there was store credit used for this order
                        if (order.getStoreBalanceOffset() != null && order.getStoreBalanceOffset().compareTo(BigDecimal.ZERO) == 1)
                        {
                            //Subtract from the final settlement amount the used store credit, then queue up whatever remains for a
                            //store credit refund to the user's account
                            if (finalSettlementAmount.subtract(order.getStoreBalanceOffset()).compareTo(BigDecimal.ZERO) == -1)
                            {
                                storeCreditToRefund = order.getStoreBalanceOffset().subtract(finalSettlementAmount);
                                finalSettlementAmount = BigDecimal.ZERO;
                            }
                            else
                            {
                                finalSettlementAmount = finalSettlementAmount.subtract(order.getStoreBalanceOffset());
                            }
                        }

                        if (finalSettlementAmount.compareTo(BigDecimal.ZERO) == 1) {
                            transactionResult = braintreeApi.settleTransactionFromVault(order.getPaymentProcessorTransactionId(), finalSettlementAmount);
                        }

                        String settlementStatusString = "";

                        if (transactionResult == null && finalSettlementAmount.compareTo(BigDecimal.ZERO) == 1)
                        {
                            settlementStatusString = "transactionResult was null, reporting error.";
                        } else if (transactionResult != null && !transactionResult.isSuccess())
                        {
                            settlementStatusString = "transactionResult was populated, but the result from Braintree was a failure.";
                        }
                        else
                        {
                            settlementStatusString = "Successful settlement, updating status.";
                        }

                        logger.debug(settlementStatusString);

                        //Successful settlement, update the status and the order's final settlement amount accordingly
                        if ((transactionResult != null && transactionResult.isSuccess()) || finalSettlementAmount.compareTo(BigDecimal.ZERO) == 0) {

                            orderAccessor.updateOrderOverallSettlementAmount(order.getOrderId(), finalSettlementAmount, finalSettlementTax, finalSettlementSubtotal, finalSettlementShipping);

                            //Refund any leftover store credit if there was a cancellation, and keep a record as part of the order
                            if (storeCreditToRefund.compareTo(BigDecimal.ZERO) == 1)
                            {
                                userAccessor.addUserStoreCredit(user.getUserId(), storeCreditToRefund);
                                orderAccessor.setOrderCreditRefunded(order.getOrderId(), storeCreditToRefund);
                                orderAccessor.setOrderAppliedCredit(order.getOrderId(), order.getStoreBalanceOffset().subtract(storeCreditToRefund));
                            }

                            orderAccessor.updateOrderStatus(order.getOrderId(), Order.ORDER_STATUS.SETTLED);
                        }
                        //Failure to settle, attempt a few times before e-mailing out and giving up
                        else {

                            String error;
                            if (transactionResult != null && transactionResult.getMessage() != null && transactionResult.getMessage().length() > 0)
                                error = transactionResult.getMessage();
                            else if (transactionResult != null && transactionResult.getErrors() != null &&
                                    transactionResult.getErrors().getAllValidationErrors() != null &&
                                    transactionResult.getErrors().getAllValidationErrors().size() > 0 &&
                                    transactionResult.getErrors().getAllValidationErrors().get(0).getMessage() != null)
                                error = transactionResult.getErrors().getAllValidationErrors().get(0).getMessage();
                            else
                                error = "Couldn't find error in object, please investigate.";



                            orderAccessor.updateOrderErrorMessage(order.getOrderId(),
                                    error);

                            orderAccessor.incrementOrderSettlementAttempts(order.getOrderId());

                            logger.error("Failed to settle order with return message " +
                                    error +
                                    " for Order Id " + order.getOrderId());

                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Failure to settle Order with orderId ");
                            stringBuilder.append(order.getOrderId());
                            stringBuilder.append(" with Braintree response message ");
                            stringBuilder.append(error);
                            stringBuilder.append(".  Attempt ");
                            stringBuilder.append(order.getSettlementAttempts());

                            Map<String, String> emailTo = new HashMap<>();

                            //Try, try again
                            if (order.getSettlementAttempts() < 3) {
                                emailTo.put("Tim", "api@troveup.com");
                            }
                            //Email out for manual intervention and keep this order from surfacing again
                            else {
                                orderAccessor.updateOrderStatus(order.getOrderId(), Order.ORDER_STATUS.FAILED_TO_SETTLE);

                                stringBuilder.append(". ");
                                stringBuilder.append("This was the last attempt.  Please manually intervene.");

                                emailTo.put("Tim", "tim@troveup.com");
                                emailTo.put("Brian", "brian@troveup.com");
                                emailTo.put("Andrew", "andrew@troveup.com");
                                emailTo.put("Tim", "api@troveup.com");

                                logger.error("Final failed attempt to settle order with return message " +
                                        error +
                                        " for Order Id " + order.getOrderId());

                            }

                            mailProvider.sendRawEmail("Payment Settlement Failure for Order " + order.getOrderId(), "error@troveup.com", emailTo,
                                    stringBuilder.toString(), false);
                        }
                    }
                    //Free order, go ahead and mark it as complete
                    else if (order.getPaymentProcessorTransactionId() != null && order.getPaymentProcessorTransactionId().equals("FREE"))
                    {
                        orderAccessor.updateOrderFinalSettlementAmount(order.getOrderId(), new BigDecimal("0.00"));
                        orderAccessor.updateOrderStatus(order.getOrderId(), Order.ORDER_STATUS.SETTLED);
                    }
                    else
                        throw new RuntimeException("Order " + order.getOrderId() + " failed to settle properly.  Throwing" +
                                "an exception to trigger a retry.  If this persists, please handle manually.");
                }
                catch (Exception e)
                {
                    if (order.getSettlementAttempts() == null ||  order.getSettlementAttempts() < 3)
                    {
                        orderAccessor.incrementOrderSettlementAttempts(order.getOrderId());
                    }
                    else
                    {
                        orderAccessor.updateOrderStatus(order.getOrderId(), Order.ORDER_STATUS.FAILED_TO_SETTLE);
                    }

                    orderAccessor.updateOrderErrorMessage(order.getOrderId(), e.toString());

                    Map<String, String> emailTo = new HashMap<>();
                    emailTo.put("Tim", "api@troveup.com");

                    StringWriter writer = new StringWriter();
                    PrintWriter pwriter = new PrintWriter(writer);
                    e.printStackTrace(pwriter);

                    String errorMessage = "Exception encountered while trying to settle order " + order.getOrderId() + ".  Message was " + writer.toString();

                    mailProvider.sendRawEmail("Payment Settlement Failure for Order " + order.getOrderId(), "error@troveup.com", emailTo,
                            errorMessage, false);
                }
            }
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/createshippinglabel", method = RequestMethod.POST)
    public ResponseEntity<String> uploadModelToManufacturer(@RequestParam("checkoutSystem") final String checkoutSystem,
                                                            @RequestParam("orderNumber") final String orderNumber,
                                                            HttpServletRequest request)
    {
        PrintOrder.ORDER_SYSTEM orderSystem;
        EasyPostPostageLabel label = null;
        if (checkoutSystem.equals(CHECKOUT_SYSTEM_FTUE_PROTOTYPE))
        {
            orderSystem = PrintOrder.ORDER_SYSTEM.FTUE_PROTOTYPE;
        }
        else if (checkoutSystem.equals(CHECKOUT_SYSTEM_FTUE))
        {
            orderSystem = PrintOrder.ORDER_SYSTEM.FTUE;
        }
        else
        {
            orderSystem = PrintOrder.ORDER_SYSTEM.CART;
        }

        PrintOrder printOrder = printOrderAccessor.getPrintOrderByOrderNumber(orderNumber, orderSystem,
                IEnums.SEEK_MODE.FULL);

        try
        {
            if (checkoutSystem.equals(CHECKOUT_SYSTEM_FTUE_PROTOTYPE))
            {
                label = easyPostProvider.
                        purchaseLabel(printOrder.getPersistedRecordReference().getRequest().getShipping_info(),
                                FtueRequestAddress.getTroveFromAddress(),
                                "3108096011", "3108096011", TroveBoxDimensions.getTroveBoxDimensions(), 0);

            }
            else if (checkoutSystem.equals(CHECKOUT_SYSTEM_FTUE))
            {
                label = easyPostProvider.
                        purchaseLabel(printOrder.getCheckoutReference().getShippingAddress(), Address.getTroveFromAddress(),
                                TroveBoxDimensions.getTroveBoxDimensions(), 0);

            }
            else
            {
                label = easyPostProvider.purchaseLabel(printOrder.getOrderReference().getShippingAddress(),
                        Address.getTroveFromAddress(), TroveBoxDimensions.getTroveBoxDimensions(), 0);
            }
        }
        catch (EasyPostException e)
        {
            Map<String, String> founders = new HashMap<>();
            founders.put("Tim", "tim@troveup.com");
            founders.put("Brian", "brian@troveup.com");
            founders.put("Andrew", "andrew@troveup.com");

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("EasyPost Label Creation Failure for orderNumber ");
            stringBuilder.append(orderNumber);
            stringBuilder.append(" under checkout system ");
            stringBuilder.append(checkoutSystem);
            stringBuilder.append(".  Reason message was ");
            stringBuilder.append(e.getMessage());

            mailProvider.sendRawEmail("EasyPost Label Failure", "error@troveup.com", founders,
                    stringBuilder.toString(), false);

            logError(e);
        }

        if (label != null)
            printOrderAccessor.updatePrintOrderShippingLabel(printOrder.getPrintOrderPrimaryKeyId(), label);

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    /**
     * Cron endpoint that checks all free First Time User Experience requests that are in the process of being rendered
     * to see if they are complete.
     *
     * @return Response entity based on the outcome of the request.
     */
    @RequestMapping(value = "/curatecompletedfreeftuerequests", method = RequestMethod.GET)
    public ResponseEntity<String> curateCompletedFreeFtueRequests() {

        List<FtuePersistedRecord> ftueRecordsBeingRendered = ftueAccessor.getFtuePersistedRecordsByStatus
                (FtuePersistedRecord.FTUE_STATUS.PROCESSING_FOR_LANDING, IEnums.SEEK_MODE.QUICK, 20l);

        if (ftueRecordsBeingRendered != null && ftueRecordsBeingRendered.size() > 0)
        {
            List<Long> identifiersOfRecordsBeingRendered = new ArrayList<>();
            for (FtuePersistedRecord record : ftueRecordsBeingRendered)
            {
                identifiersOfRecordsBeingRendered.add(record.getFtuePersistedRecordId());
            }

            Map<Long, Boolean> renderStatusesByFtueId =
                    renderQueueAccessor.checkFtueRenderStatuses(identifiersOfRecordsBeingRendered);

            identifiersOfRecordsBeingRendered = new ArrayList<>();
            for (Long uniqueRecord : renderStatusesByFtueId.keySet())
            {
                //Completed renders, update the status
                if (renderStatusesByFtueId.get(uniqueRecord)) {
                    identifiersOfRecordsBeingRendered.add(uniqueRecord);
                }
            }

            if (identifiersOfRecordsBeingRendered.size() > 0)
                //ftueAccessor.bulkUpdateFtueStatusesById(identifiersOfRecordsBeingRendered, FtuePersistedRecord.FTUE_STATUS.WAITING_TO_BE_SENT);
                //TODO:  Swap these lines when we're ready for the system to process automagically
                ftueAccessor.bulkUpdateFtueStatusesById(identifiersOfRecordsBeingRendered, FtuePersistedRecord.FTUE_STATUS.WAITING_FOR_MANUAL_READY);
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/curaterenderqueue", method = RequestMethod.GET)
    public ResponseEntity<String> curateRenderQueue() throws Exception
    {
        //If there isn't a job running, check the queue and restart processing
        if(!isRenderJobRunning())
        {
            logger.debug("There is no job running.");
            //Check render queue, if there's nothing there, submit a new job
            List<Render> renderQueue = renderQueueAccessor.getQueuedRenders();
            if (renderQueue != null && renderQueue.size() > 0)
            {
                logger.debug("There were " + renderQueue.size() + " renders in the queue.  Submitting the first" +
                        " with jobId " + renderQueue.get(0).getJobId());

                logger.debug("Starting RealityServer..");
                RealityServer.REALITY_SERVER_STATUS status = realityServerManager.startRealityServer();
                if (status == RealityServer.REALITY_SERVER_STATUS.STARTED)
                    submitRender(renderQueue.get(0));
                else
                {
                    String error = "Unable to start RealityServer, returned with status of " + status.toString() + ".  " +
                            "No render jobs were started.  Manual intervention may be required.";

                    Map<String, String> founders = new HashMap<>();
                    founders.put("Tim", "api@troveup.com");

                    mailProvider.sendRawEmail("RealityServer State Problem", "errors@troveup.com",
                            founders, error, false);
                }
            }
            else
            {
                logger.debug("Nothing in the queue, doing nothing.");
            }
        }
        else
        {
            logger.debug("Render job is running, doing nothing.");
        }

        List<Item> itemsWithNoDefaultImage = itemAccessor.getRenderedItemsWithNoDefaultImage(20l, IEnums.SEEK_MODE.QUICK);

        if (itemsWithNoDefaultImage != null && itemsWithNoDefaultImage.size() > 0)
        {
            for (Item itemWithNoDefaultImage : itemsWithNoDefaultImage) {
                itemAccessor.setDefaultItemCardImage(itemWithNoDefaultImage.getItemId());
                feedAccessor.setQueuedItemReadyForAdmission(itemWithNoDefaultImage.getItemId());
            }
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/realityserverpostback", method = RequestMethod.POST)
    public ResponseEntity<String> realityServerPostback(HttpServletRequest req) throws Exception {
        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.OK);

        String requestBody = StringUtils.converStreamToString(req.getInputStream());

        logger.debug("Response back from RealityServer was: " + requestBody);

        RealityServerPostbackResponse response = gson.fromJson(requestBody, RealityServerPostbackResponse.class);
        String baseImageName = "render-" +
                response.getUUID() + "-" + response.getJobId();

        Render render = renderQueueAccessor.getRender(getRenderJobId(), IEnums.SEEK_MODE.RENDER_FULL);

        Boolean hasError = false;
        Boolean realityServerOffline = false;
        String errorMessage = "";
        Integer errorCode = 0;

        for (RealityServerCamera camera : response.getCameras())
        {
            if (camera.getErrorId() != 0) {
                hasError = true;
                errorMessage = camera.getErrorMessage();
                errorCode = camera.getErrorId();
            }
            //Detect if reality server shut down during any of the renders so that it can be started back up again
            if (camera.getErrorId() == -2)
                realityServerOffline = true;
        }

        if (response.getErrorId() != 0 || hasError) {
            if (render != null) {
                //Let this thing retry itself 10 times before we give up and e-mail somebody for help
                if (render.getRetryNumber() == null || render.getRetryNumber() <= 10) {
                    String focusedErrorMessage;

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failure to render job: ");
                    stringBuilder.append(response.getJobId());
                    stringBuilder.append(" with UUID ");
                    stringBuilder.append(response.getUUID());
                    stringBuilder.append(".  Reason message was ");

                    if (response.getErrorId() != 0) {
                        stringBuilder.append(response.getErrorMessage());
                        focusedErrorMessage = response.getErrorMessage();
                    } else {
                        stringBuilder.append(errorMessage);
                        focusedErrorMessage = errorMessage;
                    }

                    Map<String, String> founders = new HashMap<>();
                    founders.put("Tim", "api@troveup.com");

                    mailProvider.sendRawEmail("Render Failure " + render.getJobId(), "error@troveup.com", founders,
                            stringBuilder.toString(), false);

                    renderQueueAccessor.updateRenderStatus(render.getJobId(), IRenderQueueAccessor.RENDER_STATUS.QUEUED,
                            null, 0);

                    renderQueueAccessor.incrementRenderRetryCount(getRenderJobId());

                /*deleteRenderJob();

                throw new Exception("Reality server postback for jobId " + render.getJobId() + " had error message " +
                        response.getErrorMessage() + ". Embedded error message was " + focusedErrorMessage);*/

                    logger.error("Reality server postback for jobId " + render.getJobId() + " had error message " +
                            response.getErrorMessage() + ". Embedded error message was " + focusedErrorMessage + ".  Retrying...");

                    rval = new ResponseEntity<String>(HttpStatus.FAILED_DEPENDENCY);

                } else {

                    Integer focusedErrorId;
                    String focusedErrorMessage;

                    if (response.getErrorId() == 0) {
                        focusedErrorId = errorCode;
                        focusedErrorMessage = errorMessage;
                    } else {
                        focusedErrorId = render.getErrorId();
                        focusedErrorMessage = render.getErrorString();
                    }

                    Map<String, String> founders = new HashMap<>();
                    founders.put("Tim", "tim@troveup.com");
                    founders.put("Brian", "brian@troveup.com");
                    founders.put("Andrew", "andrew@troveup.com");

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failure to render job: ");
                    stringBuilder.append(response.getJobId());
                    stringBuilder.append(" with UUID ");
                    stringBuilder.append(response.getUUID());
                    stringBuilder.append(".  Reason message was ");
                    stringBuilder.append(focusedErrorMessage);
                    stringBuilder.append(". This was the 10th attempt.  Aborting render, please manually submit.");

                    mailProvider.sendRawEmail("Render Failure " + response.getJobId(), "error@troveup.com", founders,
                            stringBuilder.toString(), false);

                    renderQueueAccessor.updateRenderStatus(getRenderJobId(), IRenderQueueAccessor.RENDER_STATUS.ERROR,
                            focusedErrorMessage, focusedErrorId);

                /*deleteRenderJob();

                throw new Exception("Reality server postback for jobId " + response.getJobId() + " had error message " +
                response.getErrorMessage() + ". Embedded error message was " + focusedErrorMessage);*/

                    logger.error("Reality server postback for jobId " + render.getJobId() + " had error message " +
                            response.getErrorMessage() + ". Embedded error message was " + focusedErrorMessage + ".  Retrying...");

                    rval = new ResponseEntity<String>(HttpStatus.FAILED_DEPENDENCY);
                }
            }
            //Reality server was offlined by a different instance, restart it for the next round.
            if(response.getErrorId() == -2 || realityServerOffline)
            {
                logger.debug("RealityServer was offline for this render, firing it back up for the retry..");
                RealityServer.REALITY_SERVER_STATUS statusResponse = realityServerManager.startRealityServer();

                if (statusResponse != RealityServer.REALITY_SERVER_STATUS.STARTED) {
                    String error = "Unable to start RealityServer after render failure due to it not being online, returned with status of " +
                            statusResponse.toString() + ".  Manual intervention may be required.";

                    HashMap<String, String> founders = new HashMap<>();
                    founders.put("Tim", "api@troveup.com");

                    mailProvider.sendRawEmail("RealityServer State Problem", "errors@troveup.com",
                            founders, error, false);
                }
            }
        }
        else
        {
            logger.debug("Response came back with UUID: " + response.getUUID() + " and jobId: " + response.getJobId());

            Map<String, List<String>> cameraMap = RealityServerManager.getSceneCameraMap();

            //Process a regular item render
            if (render.getRenderType().equals(Render.RENDER_TYPE.ITEM)) {

                //TODO:  Optimize this out.  We don't need to try to remove the customizer images every time.
                itemAccessor.removeCustomizerImages(render.getItemReference().getItemId());

                List<CloudParam> readCloudParams = new ArrayList<>();
                readCloudParams.add(new CloudParam(GoogleCloudStorage.KEY_BUCKET_NAME, imageBucketName));

                List<CloudParam> writeCloudParams = new ArrayList<>();
                writeCloudParams.add(new CloudParam(GoogleCloudStorage.KEY_BUCKET_NAME, imageBucketName));
                writeCloudParams.add(new CloudParam(GoogleCloudStorage.KEY_MIME_TYPE, "application/octet-stream"));

                List<ItemImage> itemImageReferences = new ArrayList<>();

                //TODO:  If this is slow, bump the image resizing to the front-end.  Seems pretty overkill.
                for (String camera : cameraMap.get(render.getScene())) {
                    //First things first, we need to have multiple sizes of this image.  Resize it so that we have a "card"
                    //sized image, and a "thumbnail" image
                    String normalImageName = baseImageName + "-" + camera + ".png";
                    String cardImageName = baseImageName + "-" + camera + "-card" + ".png";
                    String thumbnailImageName = baseImageName + "-" + camera + "-thumbnail" + ".png";

                    logger.debug("Attempting to read image " + normalImageName + " from the bucket");
                    //Get the normal image
                    ByteBuffer imageByteBuffer = storage.readFile(normalImageName, readCloudParams);

                    //Save the Card sized image to the CDN
                    byte[] resizedImage = imageProvider.resizeImage(imageByteBuffer.array(), 260, 260);
                    storage.writeBytes(resizedImage, cardImageName, writeCloudParams);

                    //Save the Thumbnail sized image to the CDN
                    resizedImage = imageProvider.resizeImage(imageByteBuffer.array(), 100, 100);
                    storage.writeBytes(resizedImage, thumbnailImageName, writeCloudParams);

                    ItemImage itemImage = new ItemImage();
                    itemImage.setCustomizerImage(false);
                    itemImage.setLargeImageUrlPath(imageBucketUrl + normalImageName);
                    itemImage.setMediumImageUrlPath(imageBucketUrl + cardImageName);
                    itemImage.setSmallImageUrlPath(imageBucketUrl + thumbnailImageName);
                    itemImage.setMaterial(render.getRenderMaterial());
                    itemImageReferences.add(itemImage);
                }

                //Add the images we just processed
                itemAccessor.addImage(render.getItemReference().getItemId(), itemImageReferences);
            }
            //We're doing a FTUE render, process it
            else
            {
                List<FtueImage> ftueImages = new ArrayList<>();
                for (String camera : cameraMap.get(render.getScene()))
                {
                    FtueImage image = new FtueImage();
                    image.setMaterial(render.getRenderMaterial());
                    image.setFtueImageUrl(imageBucketUrl + baseImageName + "-" + camera + ".png");
                    ftueImages.add(image);
                }

                ftueAccessor.addFtueImages(render.getFtueReference().getFtuePersistedRecordId(), ftueImages);
            }

            renderQueueAccessor.updateRenderStatus(render.getJobId(),
                    IRenderQueueAccessor.RENDER_STATUS.COMPLETE, null, 0);
        }

        //Send the next render
        List<Render> renderQueue = renderQueueAccessor.getQueuedRenders();
        if (renderQueue != null && renderQueue.size() > 0)
        {
            submitRender(renderQueue.get(0));
            setRenderJobId(renderQueue.get(0).getJobId());
        }
        else
        {
            deleteRenderJob();
            RealityServer.REALITY_SERVER_STATUS stopResponse = realityServerManager.stopRealityServer();
            logger.debug("Shutting down RealityServer, no more renders to process...");

            if (stopResponse != RealityServer.REALITY_SERVER_STATUS.STOPPED)
            {
                String error = "Unable to stop RealityServer after render chain completion, returned with status of " +
                        stopResponse.toString() + ".  Manual intervention may be required.";

                Map<String, String> founders = new HashMap<>();
                founders.put("Tim", "api@troveup.com");

                mailProvider.sendRawEmail("RealityServer State Problem", "errors@troveup.com",
                        founders, error, false);
            }
        }

        return rval;
    }

    @RequestMapping(value = "/renderqueuewatchdog", method = RequestMethod.GET)
    public ResponseEntity<String> renderQueueWatchDog() throws Exception
    {
        if (isRenderJobRunning())
        {
            Date startedRenderTime = getRunningRenderJobSubmitTime();

            if (new Date().after(DateUtils.getDateFiveMinutesFrom(startedRenderTime)))
            {
                Render render = renderQueueAccessor.getRunningRender();

                if (render != null) {
                    logger.error("Watchdog triggered reset of job " + render.getJobId());

                    renderQueueAccessor.updateRenderStatus(render.getJobId(), IRenderQueueAccessor.RENDER_STATUS.QUEUED,
                            "Watchdog Reset", -1);
                }

                deleteRenderJob();

                RealityServer.REALITY_SERVER_STATUS stopResponse = realityServerManager.stopRealityServer();

                if (stopResponse != RealityServer.REALITY_SERVER_STATUS.STOPPED)
                {
                    String error = "Unable to stop RealityServer after watchdog reset, returned with status of " +
                            stopResponse.toString() + ".  Manual intervention may be required.";

                    Map<String, String> founders = new HashMap<>();
                    founders.put("Tim", "api@troveup.com");

                    mailProvider.sendRawEmail("RealityServer State Problem", "errors@troveup.com",
                            founders, error, false);
                }
            }
        }

        List<Render> rendersByRunningStatus = renderQueueAccessor.getRendersByStatus(IRenderQueueAccessor.RENDER_STATUS.RUNNING);

        if (rendersByRunningStatus != null && rendersByRunningStatus.size() > 0)
        {
            for (Render render : rendersByRunningStatus)
            {
                if (render.getDateSubmitted() != null && new Date().after(DateUtils.getDateXMinutesFrom(render.getDateSubmitted(), 5)))
                {
                    logger.error("Render with JobId " + render.getJobId() + " has been running for 5 minutes, restarting it as something more than likely failed.");
                    renderQueueAccessor.updateRenderStatus(render.getJobId(), IRenderQueueAccessor.RENDER_STATUS.QUEUED, "Render reset by watchdog.", -1);
                }
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/curatefeed", method = RequestMethod.GET)
    public ResponseEntity<String> curateFeed()
    {
        if (!isFeedCurationRunning()) {

            setRunningFeedCuration();

            Boolean shouldCheckItemQueue = !feedAccessor.fillFeed();

            feedAccessor.refreshFeedScores();

            if (shouldCheckItemQueue)
                feedAccessor.handleQueuedItems();

            deleteRunningFeedCuration();
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/feedcurationwatchdog", method = RequestMethod.GET)
    public ResponseEntity<String> feedCurationWatchdog()
    {
        if (isFeedCurationRunning())
        {
            Date startedCurationTime = getFeedCurationJobSubmitTime();

            if (startedCurationTime != null && new Date().after(DateUtils.getDateXMinutesFrom(startedCurationTime, 15)))
            {
                deleteRunningFeedCuration();
                logger.error("Watchdog triggered reset of feed curation!");
            }
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/generateusername", method = RequestMethod.POST)
    public ResponseEntity<String> generateUsername(@RequestParam("userId") final String userId)
    {
        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

        if (userAccessor.generateAndReplaceUsername(Long.parseLong(userId)))
            rval = new ResponseEntity<String>(HttpStatus.OK);

        return rval;
    }

    private Boolean isNimbixRunning()
    {
        Boolean rval;
        String nimbixStatus = getNimbixIpAddress();
        if (nimbixStatus != null)
            rval = true;
        else
            rval = false;

        return rval;
    }

    private Boolean isRenderJobRunning()
    {
        Boolean rval;
        String runningJob = getRenderJobId();
        if (runningJob != null)
            rval = true;
        else
            rval = false;

        return rval;
    }

    private Date getRunningRenderJobSubmitTime()
    {
        return new Date(Long.parseLong(keyValueStore.read(RUNNING_RENDER_JOB_SUBMIT_TIME)));
    }

    private Date getFeedCurationJobSubmitTime()
    {
        String submissionTime = keyValueStore.read(RUNNING_FEED_CURATION_SUBMISSION_TIME);
        Date rval = null;
        if (submissionTime != null)
        {
            rval = new Date(Long.parseLong(submissionTime));
        }

        return rval;
    }

    private String getRenderJobId()
    {
        return keyValueStore.read(RUNNING_RENDER_JOB);
    }

    private void deleteRenderJob()
    {
        keyValueStore.delete(RUNNING_RENDER_JOB);
        keyValueStore.delete(RUNNING_RENDER_JOB_SUBMIT_TIME);
    }

    private void setRenderJobId(String renderJobId)
    {
        keyValueStore.create(RUNNING_RENDER_JOB, renderJobId);
        keyValueStore.create(RUNNING_RENDER_JOB_SUBMIT_TIME, Long.toString(new Date().getTime()));
    }

    private void setRunningFeedCuration()
    {
        keyValueStore.create(RUNNING_FEED_CURATION, "true");
        keyValueStore.create(RUNNING_FEED_CURATION_SUBMISSION_TIME, Long.toString(new Date().getTime()));
    }

    private void deleteRunningFeedCuration()
    {
        keyValueStore.delete(RUNNING_FEED_CURATION);
        keyValueStore.delete(RUNNING_FEED_CURATION_SUBMISSION_TIME);
    }

    private String getRunningFeedCuration()
    {
        return keyValueStore.read(RUNNING_FEED_CURATION);
    }

    private Boolean isFeedCurationRunning()
    {
        Boolean rval;
        String runningCuration = getRunningFeedCuration();

        if (runningCuration != null)
            rval = true;
        else
            rval = false;

        return rval;
    }

    private String getNimbixIpAddress()
    {
        return keyValueStore.read(NIMBIX_STATUS_KEY);
    }

    private void setNimbixIpAddress(String ipAddress)
    {
        keyValueStore.create(NIMBIX_STATUS_KEY, ipAddress);
    }

    private void deleteNimbixIpAddress()
    {
        keyValueStore.delete(NIMBIX_STATUS_KEY);
    }

    private String getNimbixServerJobId()
    {
        return keyValueStore.read(NIMBIX_SERVER_JOB_ID);
    }

    private void deleteNimbixServerJobId()
    {
        keyValueStore.delete(NIMBIX_SERVER_JOB_ID);
    }

    private void setNimbixServerJobId(String serverJobId)
    {
        keyValueStore.create(NIMBIX_SERVER_JOB_ID, serverJobId);
    }

    public Boolean submitRender(Render render) throws Exception {
        Boolean rval;

        RealityServerInitialSubmitResponse response =
                realityServerManager.submitModelForRender(render);

        if (response.getErrorId() != 0)
        {
            renderQueueAccessor.updateRenderStatus(render.getJobId(),
                    IRenderQueueAccessor.RENDER_STATUS.ERROR, response.getErrorMessage(), response.getErrorId());

            deleteRenderJob();

            throw new Exception("Error submitting render for renderId " + render.getRenderQueueId() +
                    " Error message: " + response.getErrorMessage());

        }
        else
        {
            setRenderJobId(render.getJobId());
            renderQueueAccessor.updateRenderStatus(render.getJobId(),
                    IRenderQueueAccessor.RENDER_STATUS.RUNNING, null, 0);

            rval = true;
        }

        return rval;
    }

    @RequestMapping(value = "/oauthcallback", method = RequestMethod.GET)
    public ResponseEntity<String> oAuthCallbackGet(HttpServletRequest req) {
        logger.debug("Request response URI: " + req.getRequestURI());
        try {
            logger.debug("Request response body: " + StringUtils.converStreamToString(req.getInputStream()));
        } catch (IOException e) {
            logger.debug("Just kidding, there was no response body, throwing away the exception.");
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/oauthcallback", method = RequestMethod.POST)
    public ResponseEntity<String> oAuthCallbackPost(HttpServletRequest req) {
        logger.debug("Request response URI: " + req.getRequestURI());
        try {
            logger.debug("Request response body: " + StringUtils.converStreamToString(req.getInputStream()));
        } catch (IOException e) {
            logger.debug("Just kidding, there was no response body, throwing away the exception.");
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }


    @RequestMapping(value = "/sendemailtest", method = RequestMethod.GET)
    public String testAccess()
    {
        return "testcomplete";
    }

    private ModelJson getJsonModelFile(Item item, MODEL_RESOLUTION resolution)
    {
        //Get the json base model file
        List<CloudParam> cloudParams = new ArrayList<>();
        cloudParams.add(new CloudParam(GoogleCloudStorage.KEY_BUCKET_NAME, jsonBucketName));

        ByteBuffer jsonByteBuffer;
        long startTime = System.nanoTime();
        if (resolution == MODEL_RESOLUTION.HIGH)
            jsonByteBuffer = storage.readFile("models/" + item.getHighResolutionCustomizerFilename(), cloudParams);
        else
            jsonByteBuffer = storage.readFile("models/" + item.getCustomizerFilename(), cloudParams);
        long endTime = System.nanoTime();

        logger.debug("Time it took to download model: " + ((endTime - startTime) / 1000000));

        String jsonFile = new String(jsonByteBuffer.array(), Charsets.UTF_8);

        return gson.fromJson(jsonFile, ModelJson.class);
    }

    @RequestMapping(value = "/erroremail", method = RequestMethod.POST)
    public ResponseEntity<String> sendErrorEmailToFounders(@RequestParam("userId") final String userId,
                                                            @RequestParam("pathToError") final String pathToError,
                                                            @RequestParam("otherDetails") final String otherDetails,
                                                           @RequestParam("errorCode") final String errorCode,
                                                            HttpServletRequest request)
    {
        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        if(request.getRemoteAddr().equals(GOOGLE_APP_ENGINE_TASK_QUEUE_IP) ||
                SystemProperty.environment.value() == SystemProperty.Environment.Value.Development) {
            //Something went wrong, notify the API e-mail address
            String errorEmail = "An error occurred for userId " + userId + " with error path " + pathToError + ".";

            if (otherDetails != null && otherDetails.length() > 0)
                errorEmail += "Additional Details: " + otherDetails;

            Map<String, String> founders = new HashMap<>();
            founders.put("Tim", "api@troveup.com");

            mailProvider.sendRawEmail(errorCode + " Exception Encountered", "errors@troveup.com",
                    founders, errorEmail, false);

            rval = new ResponseEntity<String>(HttpStatus.OK);
        }

        return rval;
    }

    @RequestMapping(value = "/createandsubmitftueorder", method = RequestMethod.POST)
    public ResponseEntity<String> createAndPrintFtueOrder(@RequestParam("anonymousCustomizationId") final String customizationId,
                                                            @RequestParam("userId") final String userId,
                                                            HttpServletRequest request)
    {
        String MATERIAL_GOLD_TONED_BRONZE = "5";
        String FINISH_GOLD_TONED_BRONZE = "87";

        String MATERIAL_TRY_ON = "6";
        String FINISH_TRY_ON = "6";

        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

        try {


            User user = userAccessor.getUser(Long.parseLong(userId), IEnums.SEEK_MODE.USER_ADDRESSES);
            AnonymousItem anonymousItem = itemAccessor.getAnonymousItem(Long.parseLong(customizationId), IEnums.SEEK_MODE.ANONYMOUS_ITEM_FULL);

            //Set this to gold-toned bronze so that the items in the product description show up
            //with this material as default
            anonymousItem.setMaterialId(MATERIAL_GOLD_TONED_BRONZE);
            anonymousItem.setFinishId(FINISH_GOLD_TONED_BRONZE);

            Item newItem = createAndPersistItemFromAnonymousCustomization(anonymousItem, user);

            //Reset these values to Try-On, now that we're in the process of ordering the items
            newItem.setMaterialId(MATERIAL_TRY_ON);
            newItem.setFinishId(FINISH_TRY_ON);

            generateShortLinkAndAttachToItem(newItem.getItemId());
            CartItem cartItem = createFreeFtueCartItem(newItem, anonymousItem.getChain());
            Cart cart = createAndPersistFreeFtueCart(cartItem, user.getShippingAddress(), user);
            processFreeOrder(cart);

            rval = new ResponseEntity<String>(HttpStatus.OK);

        } catch (Exception e)
        {
            logError(e);
        }

        return rval;
    }

    @RequestMapping(value = "/scaleitemimage", method = RequestMethod.POST)
    public ResponseEntity<String> scaleItemImage(@RequestParam("itemImageId") final String itemImageId,
                                                 HttpServletRequest request)
    {

        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

        try {

            logger.debug("Scaling image with itemImageId: " + itemImageId);

            ItemImage image = itemAccessor.getItemImageById(Long.parseLong(itemImageId));
            String[] imageNameSplit = image.getLargeImageUrlPath().split("/");

            String imageName = imageNameSplit[imageNameSplit.length - 1];

            logger.debug("Item name was: " + imageName);

            List<CloudParam> readCloudParams = new ArrayList<>();
            readCloudParams.add(new CloudParam(GoogleCloudStorage.KEY_BUCKET_NAME, imageBucketName));
            ByteBuffer imageByteBuffer = storage.readFile(imageName, readCloudParams);

            byte[] mediumImage = imageProvider.resizeImage(imageByteBuffer.array(), 260, 260);
            String mediumImageName = "simpleItem" + "-" + UUID.randomUUID().toString() + ".png";
            mediumImageName = sendImageToCloudImageStorage(mediumImage, mediumImageName, imageBucketName, GoogleCloudStorage.MIME_TYPE_PNG);

            byte[] smallImage = imageProvider.resizeImage(imageByteBuffer.array(), 100, 100);
            String smallImageName = "simpleItem" + "-" + UUID.randomUUID().toString() + ".png";
            smallImageName = sendImageToCloudImageStorage(smallImage, smallImageName, imageBucketName, GoogleCloudStorage.MIME_TYPE_PNG);

            if (mediumImageName != null) {
                itemAccessor.updateItemImageMediumUrlPath(image.getImageId(), imageBucketUrl + mediumImageName);
            }

            if (smallImageName != null) {
                itemAccessor.updateItemImageSmallUrlPath(image.getImageId(), imageBucketUrl + smallImageName);
            }

            rval = new ResponseEntity<String>(HttpStatus.OK);

        } catch (Exception e)
        {
            logError(e);
        }

        return rval;
    }

    private String getModelFile(String downloadUrl)
    {
        List<CloudParam> cloudParams = new ArrayList<>();
        cloudParams.add(new CloudParam(GoogleCloudStorage.KEY_BUCKET_NAME, cdnBucketName));

        String modelName[] = downloadUrl.split("/");

        ByteBuffer modelByteBuffer = storage.readFile(modelName[modelName.length - 1], cloudParams);

        return new String(modelByteBuffer.array(), Charsets.UTF_8);
    }

    private String generateFtueUniqueUuid()
    {
        String rval = UUID.randomUUID().toString().substring(0, 7).toUpperCase();

        Boolean uniquenessCheck = ftueAccessor.checkIfLandingIdExists(rval);

        while(uniquenessCheck)
        {
            rval = UUID.randomUUID().toString().substring(0, 7).toUpperCase();
            uniquenessCheck = ftueAccessor.checkIfLandingIdExists(rval);
        }

        return rval;
    }

    private Item createAndPersistItemFromAnonymousCustomization(AnonymousItem anonymousItem, User user)
    {

        Item newItem = new Item();
        newItem.setItemParent(anonymousItem.getParentItem());
        newItem.setBaseItemReference(anonymousItem.getParentItem().getBaseItemReference());
        newItem.setItemCustomizations(anonymousItem.getAnonymousCustomizations());
        newItem.setItemName(anonymousItem.getParentItem().getItemName());
        newItem.setItemDescription(anonymousItem.getParentItem().getItemDescription());
        newItem.setPrivateItem(false);
        newItem.setItemFileBaseName(anonymousItem.getParentItem().getItemFileBaseName());
        newItem.setCustomizerPath(anonymousItem.getParentItem().getCustomizerPath());
        newItem.setCustomizerFilename(anonymousItem.getParentItem().getCustomizerFilename());
        newItem.setCategory(anonymousItem.getParentItem().getCategory());
        newItem.setMaterialId(anonymousItem.getMaterialId());
        newItem.setFinishId(anonymousItem.getFinishId());
        newItem.setRenderScene(anonymousItem.getParentItem().getRenderScene());
        newItem.setRemadeCount(0L);
        newItem.setTrovedCount(0L);
        newItem.setHighResolutionCustomizerFilename(anonymousItem.getParentItem().getHighResolutionCustomizerFilename());
        newItem.setSampleSupplier(anonymousItem.getParentItem().getSampleSupplier());
        newItem.setSampleMaterialId(anonymousItem.getParentItem().getSampleMaterialId());
        newItem.setIsEngravable(anonymousItem.getParentItem().getIsEngravable());
        newItem.setIsMobileCompatible(anonymousItem.getParentItem().getIsMobileCompatible());
        newItem.setPercentageMarkup(anonymousItem.getParentItem().getPercentageMarkup());
        newItem.setShippingTotal(anonymousItem.getParentItem().getShippingTotal());
        newItem.setPrototypeTotal(anonymousItem.getParentItem().getPrototypeTotal());
        newItem.setPackaging(anonymousItem.getParentItem().getPackaging());
        newItem.setLivePhotoUrl(anonymousItem.getParentItem().getLivePhotoUrl());
        newItem.setFeatured(false);
        newItem.setItemCustomizations(anonymousItem.getAnonymousCustomizations());
        newItem.setItemOwner(user);
        newItem.setOriginFtue(anonymousItem.getOriginFtue());

        if (!itemAccessor.createItem(newItem))
            throw new RuntimeException("Error creating item for anonymous FTUE customizationId " + anonymousItem.getItemId() + " and userId " + user.getUserId());

        userAccessor.addItemToTrove(user.getUserId(), newItem.getItemId());
        itemAccessor.updateItemCounters(anonymousItem.getItemId());

        //Update any collections that might have
        Map<String, String> queueWorkMap = new HashMap<>();
        queueWorkMap.put("itemId", newItem.getItemParent().getItemId().toString());
        WorkerQueuer.queueWorkForWorker(queueWorkMap, WorkerController.COLLECTION_REMAKE_COUNT_INCREMENT,
                WorkerController.WORKER_QUEUE_OBJECT_UPDATES);

        //Propagate any applicable price filters
        queueWorkMap = new HashMap<>();
        queueWorkMap.put("parentId", newItem.getItemParent().getItemId().toString());
        queueWorkMap.put("itemId", newItem.getItemId().toString());
        WorkerQueuer.queueWorkForWorker(queueWorkMap, WorkerController.PROPAGATE_PRICE_FILTER_URL,
                WorkerController.WORKER_QUEUE_OBJECT_UPDATES);

        if (!isLocalDevelopmentServer())
            submitRenderToRealityServer(newItem);

        return newItem;
    }

    private Cart createAndPersistFreeFtueCart(CartItem cartItem, Address address, User user)
    {
        List<CartItem> cartItemListWrapper = new ArrayList<>();
        cartItemListWrapper.add(cartItem);

        return createAndPersistFreeFtueCart(cartItemListWrapper, address, user);
    }

    private Cart createAndPersistFreeFtueCart(List<CartItem> cartItems, Address address, User owner)
    {
        Cart rval = new Cart();

        rval.setOriginFtue(true);
        rval.setCartOwner(owner);
        rval.setShippingAddress(address);
        rval.setBillingAddress(address);
        rval.setIncludeGiftMessageCard(false);
        rval.setShouldBeGiftWrapped(false);
        rval.setCheckoutEmail(address.getEmail());
        rval.setSubTotal(MoneyUtil.toProperScale(new BigDecimal("0.00"), null));
        rval.setTax(MoneyUtil.toProperScale(new BigDecimal("0.00"), null));
        rval.setShipping(MoneyUtil.toProperScale(new BigDecimal("0.00"), null));
        rval.setGrandTotal(MoneyUtil.toProperScale(new BigDecimal("0.00"), null));
        rval.setOriginFtue(true);

        cartAccessor.persistCart(rval);

        for (CartItem cartItem : cartItems) {
            cartAccessor.addCartItemToCart(rval.getCartId(), cartItem, null);
        }

        rval = cartAccessor.getShoppingCart(rval.getCartId(), IEnums.SEEK_MODE.FULL);

        return rval;
    }

    private CartItem createFreeFtueCartItem(Item item, Chain chain)
    {
        CartItem rval = new CartItem();

        List<Material> materials = printSupplier.getSupplierMaterials();
        String materialName = Material.getMaterialNameByMaterialId(item.getMaterialId(), materials);
        String finishName = Finish.getFinishNameByFinishId(item.getFinishId(), Material.getMaterialByMaterialId(item.getMaterialId(), materials).getFinishList());

        rval.setCartItemReference(item);
        rval.setFinishId(item.getFinishId());
        rval.setMaterialId(item.getMaterialId());
        rval.setEstimatedPrice("0.00");
        rval.setActualPrice(new BigDecimal("0.00"));
        rval.setQuantity(1);
        rval.setCartItemStatus(CartItem.CART_ITEM_STATUS.ON_HOLD);
        rval.setCustomizations(item.getItemCustomizations());
        rval.setFrozenItemName(item.getItemName());
        rval.setMaterialName(materialName);
        rval.setFinishName(finishName);
        rval.setPrototypeRequested(false);

        if (chain != null)
            rval.setChain(chain);

        return rval;
    }

    private ShortLink generateShortLinkAndAttachToItem(Long itemId)
    {
        ShortLink shortLink = new ShortLink();
        shortLink.setShortLinkFullUrl("/public/productdescription/" + itemId);

        shortLinkAccessor.persistShortLink(shortLink);
        itemAccessor.setShortLink(itemId, shortLink.getShortLinkId());

        return shortLink;
    }

    private Order processFreeOrder(Cart cart)
    {
        String orderNumber = generateOrderNumber();
        Order.Builder orderBuilder = Order.getBuilder(cart);
        orderBuilder.setPaymentProcessorTransactionId("FREE");
        orderBuilder.setTroveOrderNumber(orderNumber);
        orderBuilder.setOrderStatus(Order.ORDER_STATUS.OPEN);
        Order order = orderBuilder.buildOrder();

        userAccessor.persistNewUserOrder(cart.getCartOwner().getUserId(), order);

        List<OrderConfirmationItem> confirmationItems = new ArrayList<>();

        for (CartItem item : cart.getCartItems())
        {
            if (!isLocalDevelopmentServer()) {
                Map<String, String> workerQueue = new HashMap<>();
                workerQueue.put("uploadType", WorkerController.UPLOAD_TYPE_SHAPEWAYS);
                workerQueue.put("checkoutSystem", WorkerController.CHECKOUT_SYSTEM_CART);
                workerQueue.put("orderNumber", item.getCartItemId().toString());
                WorkerQueuer.queueWorkForWorker(workerQueue, WorkerController.UPLOAD_MODEL_URL);
            }

            confirmationItems.add(new OrderConfirmationItem(item.getFrozenItemName(),
                    item.getMaterialName() + " " + item.getFinishName(),
                    MoneyUtil.toProperScale(item.getActualPrice(), null).toString(),
                    SizeMapper.getStandardizedSize(item.getSize(item.getCustomizationIteration())), true, item.getEngraveText()));

        }

        userAccessor.removeCurrentCart(cart.getCartOwner().getUserId());

        Map<String, String> toMap = new HashMap<>();
        toMap.put(order.getShippingAddress().getFirstName(), order.getCheckoutEmail());

        MandrillMessageStatus[] status = (MandrillMessageStatus[]) mailProvider.sendOrderConfirmationEmail(toMap, confirmationItems, orderNumber,
                MoneyUtil.toProperScale(order.getGrandTotal(), null).toString(),
                MoneyUtil.toProperScale(order.getTax(), null).toString(),
                MoneyUtil.toProperScale(order.getSubTotal(), null).toString());

        if (!status[0].getStatus().equals("sent")) {
            logger.debug("Failed to send e-mail!  Status was " + status[0].getStatus());
            logger.debug("Rejection reason " + status[0].getRejectReason());
        }

        return order;
    }
}
