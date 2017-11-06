package com.troveup.brooklyn.controllers.trove;

import com.braintreegateway.Customer;
import com.braintreegateway.PaymentMethod;
import com.braintreegateway.Result;
import com.google.appengine.api.ThreadManager;
import com.google.gson.reflect.TypeToken;
import com.troveup.brooklyn.model.*;
import com.troveup.brooklyn.orm.cart.model.*;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.countries.model.Country;
import com.troveup.brooklyn.orm.countries.model.Subdivision;
import com.troveup.brooklyn.orm.item.model.*;
import com.troveup.brooklyn.orm.materials.model.Finish;
import com.troveup.brooklyn.orm.materials.model.Material;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.simpleitem.model.SimpleItem;
import com.troveup.brooklyn.orm.simpleitem.model.UserControlSelection;
import com.troveup.brooklyn.orm.user.model.Address;
import com.troveup.brooklyn.orm.user.model.Collection;
import com.troveup.brooklyn.orm.user.model.Signup;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.cdn.gcs.GoogleCloudStorage;
import com.troveup.brooklyn.sdk.cdn.model.CloudParam;
import com.troveup.brooklyn.sdk.email.mandrill.api.MandrillMessageStatus;
import com.troveup.brooklyn.sdk.email.mandrill.model.OrderConfirmationItem;
import com.troveup.brooklyn.sdk.mail.business.EasyPostProvider;
import com.troveup.brooklyn.sdk.meshexporter.forge.model.CustomizerHash;
import com.troveup.brooklyn.sdk.meshexporter.forge.model.CustomizerOperator;
import com.troveup.brooklyn.sdk.meshexporter.forge.model.ForgeMeshVolumeResponse;
import com.troveup.brooklyn.sdk.payment.braintree.exception.BraintreeCustomerCreationException;
import com.troveup.brooklyn.sdk.tax.avalara.model.AvaGetTaxResult;
import com.troveup.brooklyn.spring.social.model.UserDetails;
import com.troveup.brooklyn.tests.orm.ItemAccessorTest;
import com.troveup.brooklyn.util.MoneyUtil;
import com.troveup.brooklyn.util.SizeMapper;
import com.troveup.brooklyn.util.StringUtils;
import com.troveup.brooklyn.util.WorkerQueuer;
import com.troveup.brooklyn.util.exception.UsernameAlreadyInUseException;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by tim on 5/5/15.
 */
@Controller
public class AjaxController extends CommonController {
    //TODO: Put this in a config file
    //public static final String BUCKET_NAME = "trove-qa-teststore";
    public static final String WEIGHT_DATA_DESCRIPTION = "Model weight data for customization of the model.  " +
            "Typically used with a slider.";
    public static final String MODEL_WEIGHT = "MODEL_WEIGHT";
    public static final String MODEL_ACTIVE_MESH = "MODEL_ACTIVE_MESH";
    public static final String ACTIVE_MESH_DESCRIPTION = "Active mesh in the scene for this item";
    //public static final String BUCKET_URL = "http://storage.googleapis.com/" + BUCKET_NAME + "/";

    private enum AJAX_ERROR_CODES {
        SKIP,
        ORDER_PERSISTENCE_ERROR,
        PROMOCODE_CONSUMPTION_ERROR,
        UNKNOWN_PREPARE_CART_ERROR,
        UNKNOWN_COMPLETE_CART_ERROR,
    }

    private enum MODEL_RESOLUTION {
        HIGH,
        LOW
    }

    Logger logger = LoggerFactory.getLogger(this.getClass());

    Random random;

    private String cdnBucketName;
    private String cdnBucketUrl;
    private String jsonBucketName;
    private String siteUrl;
    private String imageBucketName;
    private String imageBucketUrl;

    @PostConstruct
    public void init() {
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

        //Random number generator for local environments
        random = new Random();

        siteUrl = env.getProperty("environment.baseurl");
        imageBucketName = env.getProperty("cloudstore.itemimagecdn");
        imageBucketUrl = env.getProperty("cloudstore.cloudstoreurl") + imageBucketName + "/";

    }

    @RequestMapping(value = "/redeemgiftcard", method = RequestMethod.POST)
    public
    @ResponseBody
    GiftCardRedeemResponse redeemGiftCard(@RequestParam("giftCardString") final String giftCardString,
                                          HttpServletRequest req) {
        GiftCardRedeemResponse rval =
                new GiftCardRedeemResponse("Oops!  There was a problem processing your gift card.  Please try again later.");

        User user = getOrCreatePseudoUserOrGetAuthUserPII(req, IEnums.SEEK_MODE.QUICK);


        if (user != null) {
            if (cartAccessor.validateGiftCard(giftCardString.toUpperCase())) {

                BigDecimal giftCardAmount = cartAccessor.redeemGiftCard(giftCardString.toUpperCase(), user.getUserId());
                BigDecimal storeCreditAmount = userAccessor.getUserStoreCredit(user.getUserId());

                rval = new GiftCardRedeemResponse(giftCardAmount, storeCreditAmount);
            } else {
                rval = new GiftCardRedeemResponse("Oops!  That gift card was invalid.  " +
                        "Please check that the gift card number is correct and try again.");
            }
        }

        return rval;
    }

    @RequestMapping(value = "/updateitemdescription", method = RequestMethod.POST)
    public ResponseEntity<String> updateItemDescription(@RequestParam("itemId") final Long itemId,
                                                        @RequestParam("itemDescription") final String itemDescription) {
        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);
        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

        if (itemAccessor.updateItemDescription(itemId, user.getUserId(), itemDescription))
            rval = new ResponseEntity<String>(HttpStatus.OK);

        return rval;
    }

    @RequestMapping(value = "/updateitemname", method = RequestMethod.POST)
    public ResponseEntity<String> updateItemName(@RequestParam("itemId") final Long itemId,
                                                 @RequestParam("itemDescription") final String itemName) {
        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);
        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

        if (itemAccessor.updateItemName(itemId, user.getUserId(), itemName))
            rval = new ResponseEntity<String>(HttpStatus.OK);

        return rval;
    }

    /*long startTime = System.nanoTime();
    if (resolution == MODEL_RESOLUTION.HIGH)
    jsonByteBuffer = storage.readFile("models/" + item.getHighResolutionCustomizerFilename(), cloudParams);
    else
    jsonByteBuffer = storage.readFile("models/" + item.getCustomizerFilename(), cloudParams);
    long endTime = System.nanoTime();

    logger.debug("Time it took to download model: " + ((endTime - startTime) / 1000000));*/


    // FIXME: should replace "/customizerpriceestimate" with this functionality once we sunset the old customizer
    @RequestMapping(value = "/newcustomizerpriceestimate", method = RequestMethod.POST)
    public
    @ResponseBody
    PriceEstimate getCustomizerPriceEstimate(@RequestParam(value = "parentItemId", required = true) final Long parentItemId,
                                             @RequestParam(value = "volume", required = true) final Float volume,
                                             @RequestParam("physicalMaterialId") final String physicalMaterialId,
                                             HttpServletRequest req) {

        Finish matchedFinish = null;
        Material matchedMaterial = null;
        List<Material> materials = printSupplier.getSupplierMaterials();

        for (int i = 0; i < materials.size(); i++) {
            List<Finish> materialFinishes = materials.get(i).getFinishList();
            matchedFinish = Finish.getFinishByFinishId(physicalMaterialId, materialFinishes);
            if (matchedFinish != null) {
                matchedMaterial = materials.get(i);
                break;
            }
        }
        
        if (matchedFinish == null || matchedMaterial == null) {
            logger.error("==== FAILED FINDING MATERIAL AND FINISH FROM PHYSICAL ID");
            // error handling for not finding a shapeways finish corresponding to physical material id
        }

        return new PriceEstimate(MoneyUtil.toProperScale(getItemPrice(parentItemId, matchedMaterial.getMaterialId(), matchedFinish.getFinishId(), volume).getAmount(), null).toString());
    }


    @RequestMapping(value = "/customizerpriceestimate", method = RequestMethod.POST)
    public
    @ResponseBody
    PriceEstimate getCustomizerPriceEstimate(@RequestParam(value = "parentItemId", required = true) final Long parentItemId,
                                             @RequestParam(value = "volume", required = true) final Float volume,
                                             @RequestParam("materialId") final String materialId,
                                             @RequestParam("finishId") final String finishId,
                                             HttpServletRequest req) {
        return new PriceEstimate(MoneyUtil.toProperScale(getItemPrice(parentItemId, materialId, finishId, volume).getAmount(), null).toString());
    }

    //Breaks on iOS Chrome, for some reason a String @ResponseBody is rejected and reported as an error with jQuery
    /*@RequestMapping(value = "/cardpriceestimate", method = RequestMethod.POST)
    public @ResponseBody
    String getCardPriceEstimate(@RequestParam("itemId") final Long itemId,
                                @RequestParam("materialId") final String materialId,
                                @RequestParam("finishId") final String finishId)
    {
        return getItemPrice(itemId, materialId, finishId, null).toString();
    }*/

    @RequestMapping(value = "/cardpriceestimate", method = RequestMethod.POST)
    public
    @ResponseBody
    PriceEstimate getCardPriceEstimate(@RequestParam("itemId") final Long itemId,
                                       @RequestParam("materialId") final String materialId,
                                       @RequestParam("finishId") final String finishId) {
        return new PriceEstimate(MoneyUtil.toProperScale(getItemPrice(itemId, materialId, finishId, null).getAmount(), null).toString());
    }


    @RequestMapping(value = "/updateprofileimage", method = RequestMethod.POST)
    public
    @ResponseBody
    UserProfileImageUpdateResponse uploadProfileImage(MultipartHttpServletRequest req) throws IOException {
        UserProfileImageUpdateResponse rval = new UserProfileImageUpdateResponse();

        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

        byte[] image = null;
        byte[] imageThumbnail = null;

        String imageBaseName = "profile_image";
        String profileImageUUID = UUID.randomUUID().toString();
        String profileThumbnailUUID = UUID.randomUUID().toString();
        String imageExtension = ".png";

        String fullProfileImageName = imageBaseName + profileImageUUID + imageExtension;
        String profileImageThumbnailName = imageBaseName + profileThumbnailUUID + imageExtension;

        MultiValueMap<String, MultipartFile> map = req.getMultiFileMap();
        List<MultipartFile> list = map.get("blob");

        for (MultipartFile file : list) {
            // image = Base64.decode(file.getBytes());
            image = file.getBytes();
        }

        // Map<String, String[]> params = req.getParameterMap();

        // for (String param : params.keySet())
        // {
        //     image = Base64.decode(params.get(param)[0].getBytes());
        //     // image = params.get(param)[0].getBytes(); //Base64.decode(params.get(param)[0].getBytes());
        // }

        if (image != null)
            imageThumbnail = imageProvider.resizeImage(image, 100, 100);

        List<CloudParam> cloudParams = new ArrayList<>();
        cloudParams.add(new CloudParam(GoogleCloudStorage.KEY_BUCKET_NAME, cdnBucketName));

        cloudParams.add(new CloudParam(GoogleCloudStorage.KEY_MIME_TYPE, "image/png"));

        storage.writeBytes(image, fullProfileImageName, cloudParams);
        storage.writeBytes(imageThumbnail, profileImageThumbnailName, cloudParams);

        userAccessor.updateUserProfileImage(user.getUserId(), cdnBucketUrl + fullProfileImageName,
                cdnBucketUrl + profileImageThumbnailName);

        rval.setProfileImageFullUrl(cdnBucketUrl + fullProfileImageName);
        rval.setProfileImageThumbnailUrl(cdnBucketUrl + profileImageThumbnailName);

        return rval;
    }

    @RequestMapping(value = "/updatecoverimage", method = RequestMethod.POST)
    public
    @ResponseBody
    UserCoverImageUpdateResponse uploadCoverImage(MultipartHttpServletRequest req) throws IOException {
        UserCoverImageUpdateResponse rval = new UserCoverImageUpdateResponse();

        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

        byte[] image = null;

        String imageBaseName = "profile_image";
        String profileImageUUID = UUID.randomUUID().toString();
        String imageExtension = ".png";

        String fullCoverImageName = imageBaseName + profileImageUUID + imageExtension;

        MultiValueMap<String, MultipartFile> map = req.getMultiFileMap();
        List<MultipartFile> list = map.get("blob");

        for (MultipartFile file : list) {
            image = file.getBytes();
        }

        List<CloudParam> cloudParams = new ArrayList<>();
        cloudParams.add(new CloudParam(GoogleCloudStorage.KEY_BUCKET_NAME, cdnBucketName));

        cloudParams.add(new CloudParam(GoogleCloudStorage.KEY_MIME_TYPE, "image/png"));

        storage.writeBytes(image, fullCoverImageName, cloudParams);

        /*userAccessor.updateUserProfileImage(user.getUserId(), cdnBucketUrl + fullProfileImageName,
                cdnBucketUrl + profileImageThumbnailName);*/

        userAccessor.updateUserCoverPhotoImage(user.getUserId(), cdnBucketUrl + fullCoverImageName);

        rval.setProfileCoverFullUrl(cdnBucketUrl + fullCoverImageName);

        return rval;
    }

    @RequestMapping(value = "/addgiftcard", method = RequestMethod.POST)
    public ResponseEntity<String> addGiftCardToBag(@RequestParam("digital") final Boolean isDigital,
                                                   @RequestParam("amount") final Float amount,
                                                   @RequestParam(value = "date", required = false) final String sendDate,
                                                   @RequestParam(value = "toName", required = false) final String toName,
                                                   @RequestParam(value = "fromName", required = false) final String fromName,
                                                   @RequestParam(value = "toEmail", required = false) final String toEmail,
                                                   @RequestParam(value = "fromEmail", required = false) final String fromEmail,
                                                   HttpServletRequest req) {
        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);

        User user = getOrCreatePseudoUserOrGetAuthUserPII(req, IEnums.SEEK_MODE.USER_CART_QUICK);

        if (user != null) {

            GenericItem genericItem = new GenericItem();

            //Specify this as a gift card
            genericItem.setItemType(GenericItem.ITEM_TYPE.GIFT_CARD);

            //Set the price of the item
            genericItem.setPrice(MoneyUtil.floatToBigDecimal(amount, null));

            //Set the item's bag display name
            genericItem.setCartDisplayName("$" + MoneyUtil.toProperScale(genericItem.getPrice(), null) + " Gift Card");

            List<Hook> hooks = new ArrayList<>();
            hooks.add(new Hook(Hook.HOOK_SPECIFIER.GIFT_CARD));

            //List of displayable attributes that should show up on the bag.  In this case,
            //give the details of the gift card.
            List<GenericItemDisplayAttribute> displayAttributes = new ArrayList<>();

            //Set up the hook attributes for post-purchase-processing on this item
            List<HookAttribute> hookAttributes = new ArrayList<>();
            hookAttributes.add(new HookAttribute(GiftCard.HOOK_ATTRIBUTE_IS_DIGITAL, isDigital.toString()));
            hookAttributes.add(new HookAttribute(GiftCard.HOOK_ATTRIBUTE_CARD_VALUE, amount.toString()));

            if (isDigital) {
                displayAttributes.add(new GenericItemDisplayAttribute("Type", "Digital"));
                genericItem.setShouldChargeShipping(false);
            } else {
                displayAttributes.add(new GenericItemDisplayAttribute("Type", "Physical"));
            }

            if (toName != null && toName.length() > 0) {
                hookAttributes.add(new HookAttribute(GiftCard.HOOK_ATTRIBUTE_TO_NAME, toName));
                displayAttributes.add(new GenericItemDisplayAttribute("Recipient Name", toName));
            }

            if (fromName != null && fromName.length() > 0) {
                hookAttributes.add(new HookAttribute(GiftCard.HOOK_ATTRIBUTE_FROM_NAME, fromName));
                displayAttributes.add(new GenericItemDisplayAttribute("Sender Name", fromName));
            }

            //If it's a digital card, there are extra details, like to and from e-mail, and send date
            if (isDigital) {
                hookAttributes.add(new HookAttribute(GiftCard.HOOK_ATTRIBUTE_SEND_DATE, sendDate));

                if (toEmail != null && toEmail.length() > 0) {
                    hookAttributes.add(new HookAttribute(GiftCard.HOOK_ATTRIBUTE_TO_EMAIL, toEmail));
                    displayAttributes.add(new GenericItemDisplayAttribute("Recipient E-mail", toEmail));
                }
                if (fromEmail != null && fromEmail.length() > 0) {
                    hookAttributes.add(new HookAttribute(GiftCard.HOOK_ATTRIBUTE_FROM_EMAIL, fromEmail));
                    displayAttributes.add(new GenericItemDisplayAttribute("Sender E-mail", fromEmail));
                }
            }

            genericItem.setBagDisplayAttributes(displayAttributes);
            genericItem.setBagLineItemImage("https://storage.googleapis.com/troveup-imagestore/assets/img/gift-card.jpg");
            genericItem.setHookAttributes(hookAttributes);
            genericItem.setHooks(hooks);
            genericItem.setTaxExempt(true);

            //Make sure that the user cart isn't null
            validateUserCart(user);

            cartAccessor.addGenericItemToCart(user.getShoppingCart().getCartId(), genericItem);
            cartAccessor.refreshGrandTotal(user.getShoppingCart().getCartId(), false, false);

            rval = new ResponseEntity<String>(HttpStatus.OK);
        }

        return rval;
    }

    @RequestMapping(value = "/removeitem", method = RequestMethod.POST)
    public
    @ResponseBody
    PriceEstimate removeItemFromCart(@RequestParam("itemId") final String itemId,
                                     @RequestParam("generic") final Boolean isGeneric,
                                     HttpServletRequest req) {

        PriceEstimate rval = new PriceEstimate("0.00");

        User user = getPseudoUserOrAuthUserPII(req, IEnums.SEEK_MODE.USER_CART_QUICK);

        if (itemId != null && user != null) {
            Boolean success;
            if (isGeneric) {
                success = cartAccessor.removeGenericItemFromCart(user.getShoppingCart().getCartId(), Long.parseLong(itemId));
            } else {
                success = cartAccessor.removeItemFromCart(user.getShoppingCart().getCartId(),
                        Long.parseLong(itemId));
            }

            if (success) {
                cartAccessor.refreshGrandTotal(user.getShoppingCart().getCartId(), false, false);
                Cart cart = cartAccessor.getShoppingCart(user.getShoppingCart().getCartId(), IEnums.SEEK_MODE.QUICK);
                rval = new PriceEstimate(MoneyUtil.toProperScale(cart.getSubTotal(), null).toString());
                rval.setShipping(MoneyUtil.toProperScale(cart.getShipping(), null).toString());
                rval.setGiftwrapIncludedInEstimate(cart.getShouldBeGiftWrapped());
            } else {
                rval = new PriceEstimate(MoneyUtil.toProperScale(user.getShoppingCart().getSubTotal(), null).toString());
                rval.setShipping(MoneyUtil.toProperScale(user.getShoppingCart().getSubTotal(), null).toString());
                rval.setGiftwrapIncludedInEstimate(user.getShoppingCart().getShouldBeGiftWrapped());
            }
        }

        return rval;
    }

    @RequestMapping(value = "/noncustaddtobag", method = RequestMethod.POST)
    public ResponseEntity<String> nonCustAddToBag(@RequestParam("itemId") final Long itemId,
                                                  @RequestParam("materialId") final String materialId,
                                                  @RequestParam("finishId") final String finishId,
                                                  @RequestParam(value = "engraveText", required = false) final String engraveText,
                                                  @RequestParam(value = "size", required = false) final String size,
                                                  @RequestParam(value = "chain", required = false) final Integer chain,
                                                  HttpServletRequest req) {
        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);

        Item item = itemAccessor.getItemDetached(itemId, IEnums.SEEK_MODE.ITEM_ATTRIBUTES);

        User user = getOrCreatePseudoUserOrGetAuthUserPII(req, IEnums.SEEK_MODE.USER_CART_QUICK);

        //Populate the customizations from the attributes
        if (item.getItemCustomizations() == null || item.getItemCustomizations().size() == 0) {
            item.populateItemCustomizationsFromAttributes();
        }

        Money price = getItemPrice(itemId, materialId, finishId, null);

        //Dangerous case where size was empty, but not null.  Correct it to be sure that it is
        String passedSize;
        if (size == null || size.length() == 0)
            passedSize = null;
        else
            passedSize = size;

        buildAndSubmitCartItem(item, finishId, materialId, price.getAmount().floatValue(), price, user, passedSize, engraveText,
                CartItem.CART_ITEM_STATUS.ON_HOLD, chain);

        return rval;
    }

    @RequestMapping(value = "/addpromocodes", method = RequestMethod.POST)
    public
    @ResponseBody
    ChangePromoCodesResponse addPromoCodes(MultipartHttpServletRequest req) {
        ChangePromoCodesResponse rval;
        Map<String, String[]> params = req.getParameterMap();
        List<String> codes = new ArrayList<>();

        User user = getOrCreatePseudoUserOrGetAuthUserPII(req, IEnums.SEEK_MODE.USER_CART_QUICK);

        for (String param : params.keySet()) {
            codes.add(params.get(param)[0].toUpperCase());
        }

        user.getShoppingCart().setStoreBalanceOffset(user.getStoreBalance());
        rval = new ChangePromoCodesResponse(user.getShoppingCart());

        List<PromoCode> controlCodes = promoCodeAccessor.validatePromoCodes(codes, user.getUserId());

        if (controlCodes != null && controlCodes.size() > 0) {
            List<Long> controlCodeIds = new ArrayList<>();

            for (PromoCode controlCode : controlCodes) {
                controlCodeIds.add(controlCode.getPromoCodeId());
            }

            Cart cart = promoCodeAccessor.applyPromoCodesToCart(controlCodeIds, user.getShoppingCart().getCartId());

            if (cart != null) {
                if (cart.getSubTotal().compareTo(new BigDecimal("0.00")) == 1) {
                    AvaGetTaxResult taxCalculation = (AvaGetTaxResult) taxHandler.getTaxes(user.getShoppingCart().getCartId(),
                            false);

                    if (taxCalculation != null && taxCalculation.getTotalTax() != null)
                    {
                        cartAccessor.updateCartTax(cart.getCartId(), MoneyUtil.floatToBigDecimal(taxCalculation.getTotalTax(), null));
                    }
                    else
                    {
                        cartAccessor.updateCartTax(cart.getCartId(), new BigDecimal("0.00"));
                    }
                } else
                    cartAccessor.updateCartTax(cart.getCartId(), new BigDecimal("0.00"));

                cartAccessor.refreshGrandTotal(cart.getCartId(), false, false);
                cart = cartAccessor.getShoppingCart(cart.getCartId(), IEnums.SEEK_MODE.CART_PROMO);

                rval = new ChangePromoCodesResponse(cart);

                //Account for store credit
                BigDecimal userStoreCredit = userAccessor.getUserStoreCredit(user.getUserId());
                rval.setStoreCredit(MoneyUtil.toProperScale(userStoreCredit, null).toString());
                rval.setGrandTotal(MoneyUtil.toProperScale(cart.getGrandTotal(), null).toString());
                rval.setGiftwrapIncludedInSubtotal(cart.getShouldBeGiftWrapped());
            }

        }

        return rval;
    }

    @RequestMapping(value = "/removepromocodes", method = RequestMethod.POST)
    public
    @ResponseBody
    ChangePromoCodesResponse removePromoCodes(MultipartHttpServletRequest req) {
        ChangePromoCodesResponse rval;
        Map<String, String[]> params = req.getParameterMap();
        List<String> codes = new ArrayList<>();

        User user = getPseudoUserOrAuthUserPII(req, IEnums.SEEK_MODE.USER_CART_QUICK);

        for (String param : params.keySet()) {
            codes.add(params.get(param)[0].toUpperCase());
        }

        rval = new ChangePromoCodesResponse(user.getShoppingCart());

        Cart cart = promoCodeAccessor.removePromoCodesFromCart(codes, user.getShoppingCart().getCartId());

        if (cart != null)
            rval = new ChangePromoCodesResponse(user.getShoppingCart());

        return rval;
    }

    @RequestMapping(value = "/cancelorderlineitem", method = RequestMethod.POST)
    public @ResponseBody GenericAjaxResponse cancelOrderLineItem(@RequestParam("orderItemId") final String orderItemId) {
        GenericAjaxResponse rval = new GenericAjaxResponse(false, "Oops!  Something went wrong while trying to cancel your item.  Please try that again.");

        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.USER_ORDERS_QUICK);

        try {
            if (user != null && orderAccessor.isUserOwnerOfCartItem(Long.parseLong(orderItemId), user.getUserId()) &&
                    orderAccessor.canLineItemBeCancelled(Long.parseLong(orderItemId))) {

                Order order = orderAccessor.getOrderByCartItemId(Long.parseLong(orderItemId), IEnums.SEEK_MODE.ORDER_ADDRESS);
                CartItem item = cartAccessor.getCartItem(Long.parseLong(orderItemId), IEnums.SEEK_MODE.QUICK);

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

                cartAccessor.updateCartItemStatus(Long.parseLong(orderItemId), CartItem.CART_ITEM_STATUS.CANCELLED);
                orderAccessor.cancelOrderLineItem(Long.parseLong(orderItemId));

                Map<String, String> workerQueue = new HashMap<>();
                workerQueue.put("orderId", orderAccessor.getOrderByCartItemId(Long.parseLong(orderItemId), IEnums.SEEK_MODE.QUICK).getOrderId().toString());
                WorkerQueuer.queueWorkForWorker(workerQueue, WorkerController.REFRESH_ORDER_URL);

                rval.setIsSuccess(true);
                rval.setErrorMessage("");
            }
        } catch (Exception e)
        {
            logError(e);
        }

        return rval;
    }

    @RequestMapping(value = "/completeorderlineitem", method = RequestMethod.POST)
    public @ResponseBody GenericAjaxResponse completeOrderLineItem(@RequestParam("orderItemId") final String orderItemId) {
        GenericAjaxResponse rval = new GenericAjaxResponse(false, "Oops!  Something went wrong with your order item confirmation.  Please try again.");

        try {
            User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.USER_ORDERS_QUICK);

            if (user != null && orderAccessor.isUserOwnerOfCartItem(Long.parseLong(orderItemId), user.getUserId()) &&
                    cartAccessor.getCartItem(Long.parseLong(orderItemId), IEnums.SEEK_MODE.QUICK).getCartItemStatus() == CartItem.CART_ITEM_STATUS.PENDING_USER_DECISION) {
                cartAccessor.updateCartItemStatus(Long.parseLong(orderItemId), CartItem.CART_ITEM_STATUS.READY_FOR_UPLOAD);

                Order order = orderAccessor.getOrderByCartItemId(Long.parseLong(orderItemId), IEnums.SEEK_MODE.ORDER_ADDRESS);
                CartItem item = cartAccessor.getCartItem(Long.parseLong(orderItemId), IEnums.SEEK_MODE.CART_ITEM_UPLOAD);

                AbstractMap.SimpleEntry<String, String> from =
                        new AbstractMap.SimpleEntry<String, String>("Trove Team", "hello@troveup.com");

                Map<String, String> toMap = new HashMap<>();
                toMap.put(order.getBillingAddress().getFirstName() + " " + order.getBillingAddress().getLastName(),
                        order.getCheckoutEmail());

                Map<String, String> templateVariables = new HashMap<>();
                templateVariables.put("ITEM_NAME", item.getFrozenItemName());
                templateVariables.put("SIZE", item.getSize(item.getCustomizationIteration()));
                templateVariables.put("Material", item.getMaterialName() + " " + item.getFinishName());

                MandrillMessageStatus[] status = (MandrillMessageStatus[]) mailProvider.sendTemplateEmail("Trove Design Confirmation", from, toMap, "order-confirmation-prototype",
                        templateVariables, false);

                Map<String, String> workerQueue = new HashMap<>();
                workerQueue.put("uploadType", WorkerController.UPLOAD_TYPE_SHAPEWAYS);
                workerQueue.put("checkoutSystem", WorkerController.CHECKOUT_SYSTEM_CART);
                workerQueue.put("orderNumber", orderItemId);
                WorkerQueuer.queueWorkForWorker(workerQueue, WorkerController.UPLOAD_MODEL_URL);

                rval.setIsSuccess(true);
                rval.setErrorMessage("");
            }
        } catch (Exception e)
        {
            logError(e);
        }

        return rval;
    }

    @RequestMapping(value = "/addtotrove", method = RequestMethod.POST)
    public ResponseEntity<String> addToTrove(@RequestParam("itemId") final String itemId, HttpServletRequest request) {
        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

        User user = getQuickUserDetailsPII();

        try {

            if (userAccessor.addItemToTrove(user.getUserId(), Long.parseLong(itemId))) {
                itemAccessor.updateItemCounters(Long.parseLong(itemId));

                //Update any collections that might have
                Map<String, String> queueWorkMap = new HashMap<>();
                queueWorkMap.put("itemId", itemId);
                WorkerQueuer.queueWorkForWorker(queueWorkMap, WorkerController.COLLECTION_TROVE_COUNT_INCREMENT,
                        WorkerController.WORKER_QUEUE_OBJECT_UPDATES);


                rval = new ResponseEntity<String>(HttpStatus.OK);

            }

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Stack Trace: " + sw.toString());
        }

        return rval;
    }

    @RequestMapping(value = "/removefromtrove", method = RequestMethod.POST)
    public ResponseEntity<String> removeFromTrove(@RequestParam("itemId") final String itemId) {
        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);

        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.USER_CART_QUICK);

        if (userAccessor.removeItemFromTrove(user.getUserId(), Long.parseLong(itemId))) {
            itemAccessor.updateItemCounters(Long.parseLong(itemId));
            rval = new ResponseEntity<String>(HttpStatus.OK);
        }

        return rval;
    }

    @RequestMapping(value = "/completecartorder", method = RequestMethod.POST)
    public
    @ResponseBody
    CompleteCartOrderResponse completeCartOrder(HttpServletRequest req) {

        CompleteCartOrderResponse rval = new CompleteCartOrderResponse();
        rval.setSuccess(false);
        rval.setMessage(userFriendlyErrorMessage(AJAX_ERROR_CODES.UNKNOWN_COMPLETE_CART_ERROR));

        User user = getPseudoUserOrAuthUserPII(req, IEnums.SEEK_MODE.USER_CART_QUICK);

        logger.debug("Retrieving user details.");

        //Apply any user store credit that exists
        cartAccessor.refreshGrandTotal(user.getShoppingCart().getCartId(), true, false);

        //Get fresh version of the cart
        user = userAccessor.getUser(user.getUserId(), IEnums.SEEK_MODE.USER_CART_FULL);

        if (user.getStoreBalance() != null && user.getStoreBalance().compareTo(BigDecimal.ZERO) == 1) {


            //Subtract the amount of store credit used
            userAccessor.subtractUserStoreCredit(user.getUserId(), user.getShoppingCart().getStoreBalanceOffset());
        }

        String cartEmail = user.getShoppingCart().getCheckoutEmail();

        if (cartEmail == null || cartEmail.length() < 2) {
            cartEmail = user.getEmail();
        }

        logger.debug("User details retrieved, checking if we need to send an e-mail about this being > 1.00..");
        //Order was > 200.00, fire off an e-mail to the founders for personal handling
        if (MoneyUtil.bigDecimalToMoney(user.getShoppingCart().getCurrencyUnit(), user.getShoppingCart().getSubTotal()).
                isGreaterThan(Money.parse(user.getShoppingCart().getCurrencyUnit() + " 1.00")) && !isDevelopmentServer()) {
            StringBuilder builder = new StringBuilder();
            builder.append("User with userId ");
            builder.append(user.getUserId());
            builder.append(" and email " + user.getEmail());
            builder.append(" placed an order that was ");
            builder.append(user.getShoppingCart().getGrandTotal().toString());
            builder.append(".  cartId was ");
            builder.append(user.getShoppingCart().getCartId());

            Map<String, String> founders = new HashMap<>();
            founders.put("Tim", "tim@troveup.com");
            founders.put("Brian", "brian@troveup.com");
            founders.put("Andrew", "andrew@troveup.com");

            mailProvider.sendRawEmail("Jewelry purchase for " + user.getUsername() + ".", "hello@troveup.com",
                    founders, builder.toString(), false);
        }

        logger.debug("Email checked, sending the braintree transaction through..");

        Boolean vaultReferencePresent = false;

        //Make sure that this user has a BrainTree Vault reference associated with them
        if (user.getBraintreeUserId() == null || user.getBraintreeUserId().length() == 0) {
            try {
                Result<Customer> createCustomerResult = braintreeApi.createVaultCustomer(user);
                vaultReferencePresent = (createCustomerResult == null || createCustomerResult.isSuccess());
                user.setBraintreeUserId(createCustomerResult.getTarget().getId());

            } catch (BraintreeCustomerCreationException e) {
                logError(e);
            }
        } else {
            vaultReferencePresent = true;
        }

        Result<PaymentMethod> paymentMethodResult = null;

        //Braintree can't process free orders that are discounted to zero.  Handle this case accordingly, otherwise,
        //save the payment method to the user's vault for later processing
        if (vaultReferencePresent && user.getShoppingCart().getGrandTotal().compareTo(new BigDecimal("0.00")) == 1) {
            paymentMethodResult = braintreeApi.addPaymentMethodToVaultCustomer(user.getBraintreeUserId(),
                    user.getShoppingCart().getPaymentDetails().getPaymentToken());
        }

        logger.debug("Braintree payment method saved to vault, checking success");

        //Otherwise, bypass the Braintree piece
        if (vaultReferencePresent && ((paymentMethodResult != null && paymentMethodResult.isSuccess()) ||
                (user.getShoppingCart().getGrandTotal().compareTo(new BigDecimal("0.00")) == 0 &&
                        (user.getShoppingCart().getCartItems().size() > 0 ||
                                user.getShoppingCart().getGenericItems().size() > 0)))) {
            String orderNumber = generateOrderNumber();
            Order.Builder orderBuilder = Order.getBuilder(user.getShoppingCart());

            if (paymentMethodResult != null)
                orderBuilder.setPaymentProcessorTransactionId(paymentMethodResult.getTarget().getToken());
            else
                orderBuilder.setPaymentProcessorTransactionId("FREE");

            orderBuilder.setTroveOrderNumber(orderNumber);

            //Submit for immediate settlement if there aren't any cart items that need
            //settlement to be delayed
            if (orderBuilder.getOrderItems() != null && orderBuilder.getOrderItems().size() > 0)
                orderBuilder.setOrderStatus(Order.ORDER_STATUS.OPEN);
            else
                orderBuilder.setOrderStatus(Order.ORDER_STATUS.SETTLING);

            logger.debug("Order number generated, consuming any promo codes that might exist");
            if (promoCodeAccessor.consumePromoCodes(user.getShoppingCart().getCartId(), user.getUserId())) {

                logger.debug("Promo codes consumed, persisting the new order and removing the current order from the cart");

                if (userAccessor.persistNewUserOrder(user.getUserId(), orderBuilder.buildOrder()) &&
                        userAccessor.removeCurrentCart(user.getUserId())) {
                    logger.debug("Persisted the new order, cart removed, queueing up a worker for uploading the model to Shapeways");

                    //Process any generic item hooks associated with this cart
                    Map<String, String> workerQueue = new HashMap<>();
                    workerQueue.put("cartId", user.getShoppingCart().getCartId().toString());
                    WorkerQueuer.queueWorkForWorker(workerQueue, "/worker" + WorkerController.PROCESS_CART_HOOKS_URL);

                    workerQueue = new HashMap<>();

                    if (!isLocalDevelopmentServer()) {
                        for (CartItem item : user.getShoppingCart().getCartItems()) {
                            workerQueue.put("uploadType", WorkerController.UPLOAD_TYPE_SHAPEWAYS);
                            workerQueue.put("checkoutSystem", WorkerController.CHECKOUT_SYSTEM_CART);
                            workerQueue.put("orderNumber", item.getCartItemId().toString());
                            WorkerQueuer.queueWorkForWorker(workerQueue, WorkerController.UPLOAD_MODEL_URL);
                        }
                    } else {
                        logger.debug("Local development server, holding off on uploading to Shapeways");
                    }


                    List<OrderConfirmationItem> confirmationItems = new ArrayList<>();
                    for (CartItem item : user.getShoppingCart().getCartItems()) {
                        confirmationItems.add(new OrderConfirmationItem(item.getFrozenItemName(),
                                item.getMaterialName() + " " + item.getFinishName(),
                                MoneyUtil.toProperScale(item.getActualPrice(), null).toString(),
                                SizeMapper.getStandardizedSize(item.getSize(item.getCustomizationIteration())), item.getPrototypeRequested(), item.getEngraveText()));
                    }

                    for (GenericItem item : user.getShoppingCart().getGenericItems()) {
                        confirmationItems.add(new OrderConfirmationItem(item.getCartDisplayName(), MoneyUtil.toProperScale(item.getPrice(), null).toString(), item.getBagLineItemImage()));
                    }

                    logger.debug("Worker queued, firing off an e-mail to the user");
                    Map<String, String> toMap = new HashMap<>();
                    toMap.put(user.getFirstName(), cartEmail);

                    MandrillMessageStatus[] status = (MandrillMessageStatus[]) mailProvider.sendOrderConfirmationEmail(toMap, confirmationItems, orderNumber,
                            MoneyUtil.toProperScale(user.getShoppingCart().getGrandTotal(), null).toString(),
                            MoneyUtil.toProperScale(user.getShoppingCart().getTax(), null).toString(),
                            MoneyUtil.toProperScale(user.getShoppingCart().getSubTotal(), null).toString());

                    if (!status[0].getStatus().equals("sent")) {
                        logger.debug("Failed to send e-mail!  Status was " + status[0].getStatus());
                        logger.debug("Rejection reason " + status[0].getRejectReason());
                    }

                    if (user.getPseudoUser())
                    {
                        try {

                            User destinationUserToMerge = checkAndRegisterAnonymousUser(user.getShoppingCart().getCheckoutEmail(),
                                    user.getShoppingCart().getShippingAddress().getFirstName(),
                                            user.getShoppingCart().getShippingAddress().getLastName(),
                                    user.getShoppingCart().getShippingAddress());

                            userAccessor.mergeUserAccounts(user.getUserId(), destinationUserToMerge.getUserId());
                            setSessionUnauthedUser(req, destinationUserToMerge.getUserId());

                        } catch (Exception e)
                        {
                            logError(e);

                            String error = "Error registering anonymous user on checkout with email " + user.getShoppingCart().getCheckoutEmail() != null ? user.getShoppingCart().getCheckoutEmail() : " null.";
                            error += " Pseudo user ID for merge is " + user.getUserId() + ". Please merge manually.";

                            Map<String, String> founders = new HashMap<>();
                            founders.put("Tim", "api@troveup.com");

                            mailProvider.sendRawEmail("Anonymous User Registration Problem", "errors@troveup.com",
                                    founders, error, false);

                        }

                    }

                    logger.debug("Email sent.  Fin!");

                    rval.setMessage("");
                    rval.setSuccess(true);

                } else {
                    logger.error("persistence of the new user order failed for cart id " + user.getShoppingCart().getCartId());
                    rval.setMessage(userFriendlyErrorMessage(AJAX_ERROR_CODES.ORDER_PERSISTENCE_ERROR));
                }
            } else {
                logger.error("Problem Consuming Promo Codes for user " + user.getUserId() + " with cart id " +
                        user.getShoppingCart().getCartId());

                rval.setMessage(userFriendlyErrorMessage(AJAX_ERROR_CODES.PROMOCODE_CONSUMPTION_ERROR));
            }
        } else {
            String error;

            //Walk the entire error tree to make sure we're not going to compound our exception with a null pointer
            if (paymentMethodResult != null && paymentMethodResult.getErrors() != null &&
                    paymentMethodResult.getErrors().getAllValidationErrors() != null &&
                    paymentMethodResult.getErrors().getAllValidationErrors().size() > 0 &&
                    paymentMethodResult.getErrors().getAllValidationErrors().get(0).getMessage() != null)
                error = paymentMethodResult.getErrors().getAllValidationErrors().get(0).getMessage();
            else if (paymentMethodResult != null && paymentMethodResult.getMessage() != null)
                error = paymentMethodResult.getMessage();
            else if (user.getShoppingCart().getGrandTotal().compareTo(new BigDecimal("0.00")) == 0 &&
                    user.getShoppingCart().getCartItems().size() > 0)
                error = "Free transaction failed to make it through the process for user " +
                        user.getUserId() + " and cartId " + user.getShoppingCart().getCartId();
            else
                error = "Unknown error thrown by Braintree for user " + user.getUserId() + " and cartId " + user.getShoppingCart().getCartId();

            logger.error("Problems authorizing braintree payment for cart Id " + user.getShoppingCart().getCartId()
                    + ".  Response was: " + error);

            rval.setMessage("There was a problem verifying your payment information.  Please re-enter your credit card" +
                    " information and try again.");

            if (user.getShoppingCart().getStoreBalanceOffset() != null &&
                    user.getShoppingCart().getStoreBalanceOffset().compareTo(BigDecimal.ZERO) == 1) {
                cartAccessor.setStoreBalanceOffset(user.getShoppingCart().getCartId(), BigDecimal.ZERO);
                userAccessor.addUserStoreCredit(user.getUserId(), user.getShoppingCart().getStoreBalanceOffset());
                logger.info("Error processing credit card, reverting store credit amount " + user.getShoppingCart().getStoreBalanceOffset() + " to user account " + user.getUserId() + ".");
            }
        }

        return rval;
    }

    @RequestMapping(value = "/updateprototypeorder", method = RequestMethod.POST)
    public ResponseEntity<String> updatePrototypeOrder(@RequestParam("exportHash") final String exportHash,
                                                       @RequestParam("cartItemId") final Long cartItemId,
                                                       @RequestParam("nextOrderType") final String nextOrderType,
                                                       @RequestParam("size") final String size) {
        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);

        User user = getQuickUserDetailsPII();

        CartItem item = cartAccessor.getCartItem(cartItemId, IEnums.SEEK_MODE.QUICK);

        if (user != null && orderAccessor.isUserOwnerOfCartItem(cartItemId, user.getUserId()) &&
                item.getCustomizationIteration() < item.getMaxCustomizations()) {

            CustomizerHash hash = gson.fromJson(exportHash, CustomizerHash.class);

            List<ItemCustomization> customizations = hash.getCustomizerOperatorsAsItemCustomizations(item.getCustomizationIteration() + 1);
            ItemCustomization sizeCustomization = new ItemCustomization();
            sizeCustomization.setSize(size);
            sizeCustomization.setSetNumber(item.getCustomizationIteration() + 1);
            customizations.add(sizeCustomization);

            orderAccessor.addCustomizationsToCartItem(cartItemId, customizations);
            orderAccessor.incrementCartItemCustomizationIterationCounter(cartItemId);

            if (nextOrderType.toUpperCase().equals("PROTOTYPE"))
                cartAccessor.updateCartItemStatus(cartItemId, CartItem.CART_ITEM_STATUS.PROTOTYPE_READY_FOR_UPLOAD);
                //"REAL" is the designator for this, but catch everything else as well
            else {
                cartAccessor.updateCartItemStatus(cartItemId, CartItem.CART_ITEM_STATUS.READY_FOR_UPLOAD);
            }

            Map<String, String> workerQueue = new HashMap<>();
            workerQueue.put("uploadType", WorkerController.UPLOAD_TYPE_SHAPEWAYS);
            workerQueue.put("checkoutSystem", WorkerController.CHECKOUT_SYSTEM_CART);
            workerQueue.put("orderNumber", item.getCartItemId().toString());
            WorkerQueuer.queueWorkForWorker(workerQueue, WorkerController.UPLOAD_MODEL_URL);

            rval = new ResponseEntity<String>(HttpStatus.OK);
        }

        return rval;
    }

    @RequestMapping(value = "/preparecart", method = RequestMethod.POST)
    public
    @ResponseBody
    PrepareCartResponse prepareiMatCart(@RequestParam("billingFirstName") final String billingFirstName,
                                        @RequestParam("billingLastName") final String billingLastName,
                                        @RequestParam("billingAddressLine1") final String billingAddressLine1,
                                        @RequestParam(value = "billingAddressLine2", required = false) final String billingAddressLine2,
                                        @RequestParam("billingCity") final String billingCity,
                                        @RequestParam("billingStateId") final Long billingStateId,
                                        @RequestParam("billingCountryId") Long billingCountryId,
                                        @RequestParam("billingZip") final String billingZip,
                                        @RequestParam(value = "billingPhone", required = false) final String billingPhone,
                                        @RequestParam("shippingFirstName") final String shippingFirstName,
                                        @RequestParam("shippingLastName") final String shippingLastName,
                                        @RequestParam("shippingAddressLine1") final String shippingAddressLine1,
                                        @RequestParam(value = "shippingAddressLine2", required = false) final String shippingAddressLine2,
                                        @RequestParam("shippingCity") final String shippingCity,
                                        @RequestParam("shippingStateId") final Long shippingStateId,
                                        @RequestParam("shippingCountryId") Long shippingCountryId,
                                        @RequestParam("shippingZip") final String shippingZip,
                                        @RequestParam(value = "shippingPhone", required = false) final String shippingPhone,
                                        @RequestParam(value = "cartEmail", required = true) final String cartEmail,
                                        @RequestParam("paymentNonce") final String paymentNonce,
                                        @RequestParam(value = "giftWrap", required = false) final Boolean giftWrap,
                                        @RequestParam(value = "giftMessageCard", required = false) final Boolean giftMessageCard,
                                        @RequestParam(value = "giftMessageCardText", required = false) final String giftMessageCardText,
                                        HttpServletRequest req) {
        PrepareCartResponse rval = new PrepareCartResponse();
        rval.setSuccess(false);
        rval.setMessage(userFriendlyErrorMessage(AJAX_ERROR_CODES.UNKNOWN_PREPARE_CART_ERROR));

        try {

            Map<String, String[]> params = req.getParameterMap();

            Country billingCountry = countryAccessor.getCountry(billingCountryId, IEnums.SEEK_MODE.QUICK);
            Country shippingCountry = countryAccessor.getCountry(shippingCountryId, IEnums.SEEK_MODE.QUICK);

            Subdivision billingSubdivision = countryAccessor.getSubdivision(billingStateId);
            Subdivision shippingSubdivision = countryAccessor.getSubdivision(shippingStateId);

            User user = getPseudoUserOrAuthUserPII(req, IEnums.SEEK_MODE.USER_CART_PREPARE);

            if (user != null) {
                Address billingAddress = new Address();
                billingAddress.setFirstName(billingFirstName);
                billingAddress.setLastName(billingLastName);
                billingAddress.setAddressLine1(billingAddressLine1);
                billingAddress.setAddressLine2(billingAddressLine2);
                billingAddress.setCity(billingCity);
                billingAddress.setSubdivision(billingSubdivision);
                billingAddress.setCountry(billingCountry);
                billingAddress.setPostalCode(billingZip);

                //TODO:  Move Brian's phone number to the config
                if (billingPhone == null || billingPhone.length() == 0)
                    billingAddress.setPhone("310-809-6011");
                else
                    billingAddress.setPhone(billingPhone);
                billingAddress.setEmail(cartEmail);

                Address shippingAddress = new Address();
                shippingAddress.setFirstName(shippingFirstName);
                shippingAddress.setLastName(shippingLastName);
                shippingAddress.setAddressLine1(shippingAddressLine1);
                shippingAddress.setAddressLine2(shippingAddressLine2);
                shippingAddress.setCity(shippingCity);
                shippingAddress.setSubdivision(shippingSubdivision);
                shippingAddress.setCountry(shippingCountry);
                shippingAddress.setPostalCode(shippingZip);
                //TODO:  Move Brian's phone number to the config
                if (shippingPhone == null || shippingPhone.length() == 0)
                    shippingAddress.setPhone("310-809-6011");
                else
                    shippingAddress.setPhone(shippingPhone);
                shippingAddress.setEmail(cartEmail);

                //Verify the shipability address
                String addressVerificationCheck = easyPostProvider.verifyAddress(shippingFirstName + " " + shippingLastName, shippingAddressLine1,
                        shippingAddressLine2, shippingCity, shippingSubdivision.getCode().replace("US-", ""), shippingZip, null);

                if (addressVerificationCheck.equals(EasyPostProvider.OK_RESPONSE)) {

                    List<Long> cartItemsMarkedForPrototypeProcess = new ArrayList<>();
                    List<Long> cartItemsForImmediateCheckout = new ArrayList<>();

                    //In case the cart is being re-entered, reset all cart items back to non-prototype status
                    for (CartItem cartItem : user.getShoppingCart().getCartItems()) {
                        cartItemsMarkedForPrototypeProcess.add(cartItem.getCartItemId());
                    }

                    if (cartItemsMarkedForPrototypeProcess.size() > 0) {
                        cartAccessor.setCartItemPrototypeRequestedFlag(cartItemsMarkedForPrototypeProcess, false);
                        for (Long cartItemId : cartItemsMarkedForPrototypeProcess) {
                            cartAccessor.updateCartItemStatus(cartItemId, CartItem.CART_ITEM_STATUS.ON_HOLD);
                        }
                    }

                    cartItemsMarkedForPrototypeProcess = new ArrayList<>();

                    //Discern which items in the cart need to be prototyped first, if gifting wasn't selected
                    if (!giftWrap && !giftMessageCard) {
                        for (String param : params.keySet()) {
                            if (param.contains("prototypeItem"))
                                cartItemsMarkedForPrototypeProcess.add(Long.parseLong(params.get(param)[0]));
                        }
                    }

                    for (CartItem item : user.getShoppingCart().getCartItems()) {
                        if (!cartItemsMarkedForPrototypeProcess.contains(item.getCartItemId()))
                            cartItemsForImmediateCheckout.add(item.getCartItemId());
                    }

                    if (cartItemsMarkedForPrototypeProcess.size() > 0) {
                        cartAccessor.setCartItemPrototypeRequestedFlag(cartItemsMarkedForPrototypeProcess, true);

                        //TODO:  If this ends up taking too much time, integrate it into a single call that isn't a for loop
                        for (Long cartItemId : cartItemsMarkedForPrototypeProcess) {
                            cartAccessor.updateCartItemStatus(cartItemId, CartItem.CART_ITEM_STATUS.ON_HOLD_PROTOTYPE);
                        }
                    }

                    if (cartItemsForImmediateCheckout.size() > 0)
                        cartAccessor.setCartItemPrototypeRequestedFlag(cartItemsForImmediateCheckout, false);

                    Cart cartUpdateObject = new Cart();
                    cartUpdateObject.setCartId(user.getShoppingCart().getCartId());
                    cartUpdateObject.setBillingAddress(billingAddress);
                    cartUpdateObject.setShippingAddress(shippingAddress);
                    cartUpdateObject.setCheckoutEmail(cartEmail);
                    cartUpdateObject.setIncludeGiftMessageCard(giftMessageCard);
                    cartUpdateObject.setShouldBeGiftWrapped(giftWrap);

                    //They may have checked the box for a personalized gift message, written a message, then unchecked the
                    //box.  Don't include the message if the box isn't checked.
                    if (giftMessageCard != null && giftMessageCard && giftMessageCardText != null && giftMessageCardText.length() > 0) {
                        cartUpdateObject.setGiftMessageCardText(giftMessageCardText);
                    } else {
                        cartUpdateObject.setGiftMessageCardText("");
                    }

                    cartAccessor.updateCart(cartUpdateObject);
                    Cart updatedCart = user.getShoppingCart();

                    if (giftWrap) {
                        //Force a refresh of the grand total so that gift wrapping can be included in the subtotal
                        updatedCart = cartAccessor.refreshGrandTotal(user.getShoppingCart().getCartId(), false, false);
                    }

                    //Throw away the old data
                    cartUpdateObject = new Cart();

                    AvaGetTaxResult taxCalculation = (AvaGetTaxResult) taxHandler.getTaxes(user.getShoppingCart().getCartId(),
                            false);

                    PaymentDetails paymentDetails = new PaymentDetails();
                    paymentDetails.setCreated(new Date());
                    paymentDetails.setPaymentToken(paymentNonce);
                    cartUpdateObject.setPaymentDetails(paymentDetails);

                    cartUpdateObject.setCartId(user.getShoppingCart().getCartId());

                    //In case Avalara tax calculation failed
                    if (taxCalculation == null || taxCalculation.getTotalTax() == null || updatedCart.getSubTotal().compareTo(BigDecimal.ZERO) == 0) {
                        cartUpdateObject.setTax(new BigDecimal("0.00"));
                        cartAccessor.updateCart(cartUpdateObject);
                    } else {
                        cartUpdateObject.setTax(
                                MoneyUtil.floatToBigDecimal(taxCalculation.getTotalTax(), null));
                        cartAccessor.updateCart(cartUpdateObject);
                    }


                    //Force a refresh of the grand total
                    cartAccessor.refreshGrandTotal(user.getShoppingCart().getCartId(), false, true);

                    updatedCart = cartAccessor.getShoppingCartByUser(user.getUserId(), IEnums.SEEK_MODE.FULL);

                    rval.setSubTotal(MoneyUtil.toProperScale(updatedCart.getSubTotal(), null).toString());
                    rval.setTax(MoneyUtil.toProperScale(updatedCart.getTax(), null).toString());
                    rval.setShipping(MoneyUtil.toProperScale(updatedCart.getShipping(), null).toString());
                    rval.setGiftwrapIncludedInSubtotal(updatedCart.getShouldBeGiftWrapped());

                    //Give the proper store credit amount
                    BigDecimal userStoreCredit = updatedCart.getStoreBalanceOffset();

                    //This seems silly, but downstream there is a case where the credit card has been
                    //denied, and the user has had the amount deducted from their account and added to the cart.
                    //Order settlement works correctly, but the user gets a 0.00 store credit displayed if we don't
                    //attempt to get the store credit for them from the cart, where it was transferred, first.
                    if (userStoreCredit == null || userStoreCredit.compareTo(BigDecimal.ZERO) == 0) {
                        userStoreCredit = userAccessor.getUserStoreCredit(user.getUserId());

                        if (userStoreCredit == null) {
                            userStoreCredit = BigDecimal.ZERO;
                        }
                    }

                    if (userStoreCredit != null) {
                        rval.setGrandTotal(MoneyUtil.toProperScale(applyStoreCredit(userStoreCredit, updatedCart.getGrandTotal()).getAmount(), null).toString());
                        rval.setStoreCredit(MoneyUtil.toProperScale(userStoreCredit, null).toString());
                    } else {
                        rval.setGrandTotal(MoneyUtil.toProperScale(updatedCart.getGrandTotal(), null).toString());
                    }

                    rval.setSuccess(true);
                    rval.setMessage("");
                } else {
                    rval.setMessage(addressVerificationCheck);
                }
            }
        } catch (Exception e) {
            logError(e);
        }

        return rval;
    }

    @RequestMapping(value = "/addsimpleitemtobag", method = RequestMethod.POST)
    public @ResponseBody GenericAjaxResponse addSimpleItemToBag(@RequestParam(value = "selectedOptions", required = false) final String customizations,
                                                     @RequestParam("referenceItemId") final Long parentItemId,
                                                     MultipartHttpServletRequest req)
    {
        GenericAjaxResponse rval = new GenericAjaxResponse(false, "Oops!  Something went wrong.  Please try again.");

        try {

            User user = getOrCreatePseudoUserOrGetAuthUserPII(req, IEnums.SEEK_MODE.USER_CART_QUICK);

            List<UserControlSelection> itemCustomizations = gson.fromJson(customizations, new TypeToken<List<UserControlSelection>>() {
            }.getType());
            List<HookAttribute> hookAttributes = new ArrayList<>();
            List<GenericItemDisplayAttribute> displayAttributes = new ArrayList<>();

            for (UserControlSelection customization : itemCustomizations) {
                HookAttribute hookAttribute = new HookAttribute();
                hookAttribute.setKey(SimpleItem.HOOK_ATTRIBUTE_USER_CONTROL_CHOICE_ID_OPTION_ID);
                hookAttribute.setValue(customization.getSimpleItemControlId() + "-" + customization.getSimpleItemControlOptionId());
                hookAttributes.add(hookAttribute);

                GenericItemDisplayAttribute displayAttribute = new GenericItemDisplayAttribute();
                displayAttribute.setKey(customization.getSimpleItemControlTitle());
                displayAttribute.setValue(customization.getSimpleItemControlOptionLabel());
                displayAttributes.add(displayAttribute);
            }

            HookAttribute hookAttribute = new HookAttribute();
            hookAttribute.setKey(SimpleItem.HOOK_ATTRIBUTE_CONTROL_REFERENCE);
            hookAttribute.setValue(parentItemId.toString());
            hookAttributes.add(hookAttribute);

            SimpleItem referenceItem = simpleItemAccessor.getSimpleItemByItemId(parentItemId, IEnums.SEEK_MODE.QUICK);

            if (referenceItem.getInfluencerUserAccountId() != null) {
                hookAttribute = new HookAttribute();
                hookAttribute.setKey(SimpleItem.HOOK_ATTRIBUTE_INFLUENCER_OWNER);
                hookAttribute.setValue(referenceItem.getInfluencerUserAccountId().toString());
                hookAttributes.add(hookAttribute);
            }

            byte[] displayImage = null;
            ItemImage itemImage = new ItemImage();

            //Parse the image
            Map<String, String[]> params = req.getParameterMap();
            for (String request : params.keySet()) {
                if (request.contains("displayImage"))
                {
                    if (params.get(request)[0].contains("http"))
                    {
                        itemImage = new ItemImage();
                        itemImage.setLargeImageUrlPath(params.get(request)[0]);
                    }
                    else
                    {
                        String splitString[] = params.get(request)[0].split(",");
                        displayImage = Base64.decode(splitString[1].getBytes());
                        itemImage = retrieveOrPersistItemImage(displayImage, referenceItem.getPrimaryDisplayImageUrl());
                    }
                }
            }

            GenericItem item = new GenericItem();
            item.setHookAttributes(hookAttributes);
            item.setBagDisplayAttributes(displayAttributes);
            item.setCartDisplayName(referenceItem.getItemName());
            item.setBagLineItemImage(itemImage.getLargeImageUrlPath());
            item.setPrice(referenceItem.getItemPrice());
            item.setItemType(GenericItem.ITEM_TYPE.SIMPLE_ITEM);
            item.setTaxExempt(false);

            List<Hook> hookList = new ArrayList<>();
            Hook hook = new Hook();
            hook.setHookSpecifier(Hook.HOOK_SPECIFIER.SIMPLE_ITEM);
            hookList.add(hook);
            item.setHooks(hookList);

            //Make sure that the user cart isn't null
            validateUserCart(user);

            cartAccessor.addGenericItemToCart(user.getShoppingCart().getCartId(), item);
            cartAccessor.refreshGrandTotal(user.getShoppingCart().getCartId(), false, false);

            rval.setIsSuccess(true);
            rval.setErrorMessage("");
        }
        catch (Exception e)
        {
            logError(e);
            sendErrorEmail("Error occurred adding simple item to bag! Error: " + e.getMessage(), "SimpleItem Add To Bag Error");
        }

        return rval;
    }

    @RequestMapping(value = "/savenewmodel", method = RequestMethod.POST)
    @ResponseBody public GenericAjaxResponse saveNewModel(@RequestParam("parentModelId") final Long parentModelId,
                                               @RequestParam("shouldAddToCart") final Boolean shouldAddToCart,
                                               @RequestParam("modelName") final String modelName,
                                               @RequestParam("modelDescription") final String modelDescription,
                                               @RequestParam("isPrivate") final Boolean isPrivate,
                                               @RequestParam("modelExtension") final String modelExtension,
                                               @RequestParam("imageExtension") final String imageExtension,
                                               @RequestParam("modelMaterial") final String modelMaterial,
                                               @RequestParam(value = "addToCollectionId", required = false) final Long addToCollectionId,
                                               @RequestParam(value = "defaultCollectionId", required = false) final Long defaultCollectionId,
                                               @RequestParam(value = "exportHash", required = true) final String exportHash,
                                               @RequestParam(value = "engraveText", required = false) final String engraveText,
                                               @RequestParam(value = "size", required = false) final String size,
                                               @RequestParam(value = "chain", required = false) final Integer chain,
                                               MultipartHttpServletRequest req) {

        GenericAjaxResponse rval = new GenericAjaxResponse(false, "Oops!  Something went wrong, please try that again.");
        try {

            final CustomizerHash hash = gson.fromJson(exportHash, CustomizerHash.class);
            final Item item = itemAccessor.getItemDetached(parentModelId, IEnums.SEEK_MODE.FULL);

            //Get the medium volume of this object in an asynchronous manner so that it doesn't stop the rest of the
            //processing up
            Callable volumeCallable = new Callable() {
                @Override
                public Float call() throws Exception {
                    Float rval = 0f;
                    try {
                        ForgeMeshVolumeResponse response = forgeExporter.getVolume(hash.getOperators(),
                                hash.getVisible(), SizeMapper.getMediumExportSize(item.getCategory()), item.getCustomizerPath() + item.getCustomizerFilename());

                        if (response.getSuccess()) {
                            rval = Float.parseFloat(response.getVolume());
                        } else {
                        }
                    } catch (Exception e) {
                        logError(e);
                    }

                    return rval;
                }
            };

            ThreadFactory threadFactory = ThreadManager.currentRequestThreadFactory();
            ExecutorService threadPool = Executors.newCachedThreadPool(threadFactory);
            Future<Float> mediumVolumeResponse = threadPool.submit(volumeCallable);

            //Input from the requests
            Map<String, String[]> params = req.getParameterMap();
            Map<String, byte[]> images = new HashMap<>();

            //Parse the images
            for (String request : params.keySet()) {
                if (request.contains("image-")) {
                    String splitString[] = params.get(request)[0].split(",");
                    byte[] value = Base64.decode(splitString[1].getBytes());
                    //byte[] value = params.get(request)[0].getBytes();
                    images.put(request, value);

                }
            }

            String uniqueIdentifier = UUID.randomUUID().toString();

            //------------------Image storage----------------
            //Assuming the same MIME type for all images that have been sent, so only checking it once.
            List<CloudParam> cloudParams = new ArrayList<>();
            cloudParams.add(new CloudParam(GoogleCloudStorage.KEY_BUCKET_NAME, cdnBucketName));

            //TODO:  Make this dynamic, eventually.  Not high priority.
            cloudParams.add(new CloudParam(GoogleCloudStorage.KEY_MIME_TYPE, "image/jpeg"));
            //cloudParams.add(new CloudParam(GoogleCloudStorage.KEY_MIME_TYPE, images.get(0).getContentType()));

            for (String image : images.keySet()) {
                if (!isLocalDevelopmentServer()) {
                    storage.writeBytes(images.get(image),
                            image + "-" + uniqueIdentifier + imageExtension,
                            cloudParams);
                }
            }
            //-----------------End Image storage-------------

            //Now we need to persist all of this data.
            Item newItem = new Item();

            //Put the images into the attributes
            newItem.setImages(new ArrayList<ItemImage>());
            for (String image : images.keySet()) {
                ItemImage newImage = new ItemImage();
                String imageName = image + "-" + uniqueIdentifier + imageExtension;

                //Expecting file format of image-[image order specifier]
                String[] parsedStrings = image.split("-");

                newImage.setImageName(imageName);
                newImage.setOrder(Integer.parseInt(parsedStrings[1]));
                newImage.setLargeImageUrlPath(cdnBucketUrl + imageName);
                newImage.setCustomizerImage(true);

                newItem.getImages().add(newImage);
            }

            //Set the parent item ID so that it can be persisted with it
            Item parentItem = new Item();
            parentItem.setItemId(parentModelId);
            Item baseItem = new Item();
            baseItem.setItemId(item.getBaseItemReference().getItemId());

            newItem.setItemParent(parentItem);

            //Set the little item attributes
            newItem.setItemName(modelName);
            //Modify this when we allow item descriptions to be editable.
            newItem.setItemDescription(item.getItemDescription());
            //TODO:  Set this to isPrivate input variable when that functionality is re-enabled on the front end
            newItem.setPrivateItem(false);
            newItem.setItemFileBaseName(item.getItemFileBaseName());
            newItem.setBaseItemReference(baseItem);
            newItem.setItemFileName(item.getItemFileBaseName() + "-" + uniqueIdentifier + modelExtension);
            newItem.setUniqueItemIdentifier(uniqueIdentifier);
            newItem.setCustomizerPath(item.getCustomizerPath());
            newItem.setCustomizerFilename(item.getCustomizerFilename());
            newItem.setCategory(item.getCategory());
            newItem.setMaterialId(modelMaterial);
            newItem.setFinishId(hash.getMaterial());
            newItem.setRenderScene(item.getRenderScene());
            newItem.setRemadeCount(0l);
            newItem.setTrovedCount(0l);
            newItem.setHighResolutionCustomizerFilename(item.getHighResolutionCustomizerFilename());
            newItem.setSampleSupplier(item.getSampleSupplier());
            newItem.setSampleMaterialId(item.getSampleMaterialId());
            newItem.setIsEngravable(item.getIsEngravable());
            newItem.setIsMobileCompatible(item.getIsMobileCompatible());
            newItem.setPercentageMarkup(item.getPercentageMarkup());
            newItem.setShippingTotal(item.getShippingTotal());
            newItem.setPrototypeTotal(item.getPrototypeTotal());
            newItem.setPackaging(item.getPackaging());
            newItem.setLivePhotoUrl(item.getLivePhotoUrl());

            if (defaultCollectionId != null)
                newItem.setDefaultCollection(new Collection(defaultCollectionId));

            newItem.setFeatured(false);

            newItem.setItemAttributes(new ArrayList<ItemAttribute>());

            //Set material in the attributes rather than straight on the model
            newItem.getItemAttributes().add(new ItemAttribute(ItemAttribute.ATTRIBUTE_TYPE.MATERIAL_ID.toString(),
                    modelMaterial));

            //Also set the finish in the attributes rather than straight on the model
            newItem.getItemAttributes().add(new ItemAttribute(ItemAttribute.ATTRIBUTE_TYPE.FINISH_ID.toString(),
                    hash.getMaterial()));

            List<ItemCustomization> customizations = new ArrayList<>();

            customizations = getCustomizationsFromHash(exportHash);

            newItem.setItemCustomizations(customizations);

            User user = getOrCreatePseudoUserOrGetAuthUserPII(req, IEnums.SEEK_MODE.USER_CART_QUICK);

            if (user != null) {
                newItem.setItemOwner(user);

                try {
                    Float mediumVolume = mediumVolumeResponse.get();

                    if (mediumVolume != null && mediumVolume > 0f) {
                        newItem.setMediumVolume(mediumVolume);
                    } else {
                        //newItem.setMediumVolume(Float.parseFloat(hash.getVolume()));
                        logger.error("Error setting medium volume on remade item with parent id " + parentItem.getItemId() +
                                ".  Setting to customizer volume.");
                    }
                } catch (InterruptedException | ExecutionException e)
                {
                    logger.error("Error setting medium volume on remade item with parent id " + parentItem.getItemId() +
                            ".  Setting to customizer volume.");
                    newItem.setMediumVolume(0f);
                } finally {
                    threadPool.shutdown();
                }

                //Persist the item
                Boolean itemCreateTest = itemAccessor.createItem(newItem);

                //Add the item to the user's Trove
                userAccessor.addItemToTrove(user.getUserId(), newItem.getItemId());

                itemAccessor.updateItemCounters(item.getItemId());

                if (addToCollectionId != null)
                    userAccessor.addItemToCollection(user.getUserId(), newItem.getItemId(), addToCollectionId);

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

                //Submit to reality server if we're not on a local machine
                if (!isLocalDevelopmentServer())
                    submitRenderToRealityServer(newItem);

                //TODO:  Update estimated price here once pricing is in place!
                if (shouldAddToCart) {
                    PriceFilter filter = itemAccessor.getApplicablePricefilter(item, newItem.getMaterialId(), newItem.getFinishId(), IEnums.SEEK_MODE.QUICK);
                    Money price = getItemPrice(newItem.getItemId(), null, null, null, filter);
                    buildAndSubmitCartItem(newItem, hash.getMaterial(), modelMaterial, price.getAmount().floatValue(),
                            price, user, size, engraveText, CartItem.CART_ITEM_STATUS.ON_HOLD, chain);
                }

                if (itemCreateTest) {
                    rval.setIsSuccess(true);
                    rval.setErrorMessage("");
                }
            } else
                logger.error("User object not found for attempted model save.  Was user authed correctly?");

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Stack Trace: " + sw.toString());
        }

        return rval;
    }

    @RequestMapping(value = "/optout", method = RequestMethod.POST)
    public ResponseEntity<String> saveNewModel(@RequestParam("optOutStatus") final Boolean optOutStatus) {
        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);

        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

        if (userAccessor.updateUserOptOutFlag(user.getUserId(), optOutStatus))
            rval = new ResponseEntity<String>(HttpStatus.OK);

        return rval;
    }

    @RequestMapping(value = "/updatefirstlastname", method = RequestMethod.POST)
    public ResponseEntity<String> updateName(@RequestParam("firstName") final String firstName,
                                             @RequestParam("lastName") final String lastName) {
        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);

        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

        if (userAccessor.updateUserFirstLastName(user.getUserId(), firstName, lastName))
            rval = new ResponseEntity<String>(HttpStatus.OK);

        return rval;
    }

    @RequestMapping(value = "/updatepassword", method = RequestMethod.POST)
    public ResponseEntity<String> updatePassword(@RequestParam("password") final String password) {
        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);

        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

        if (userAccessor.updateUserPassword(user.getUserId(), encoder.encode(password)))
            rval = new ResponseEntity<String>(HttpStatus.OK);

        return rval;
    }

    @RequestMapping(value = "/followuser", method = RequestMethod.POST)
    public ResponseEntity<String> followUser(@RequestParam("userId") final Long userId) {
        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);

        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

        if (userAccessor.followUser(user.getUserId(), userId))
            rval = new ResponseEntity<String>(HttpStatus.OK);

        return rval;
    }

    @RequestMapping(value = "/unfollowuser", method = RequestMethod.POST)
    public ResponseEntity<String> unfollowUser(@RequestParam("userId") final Long userId) {
        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);

        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

        if (userAccessor.unfollowUser(user.getUserId(), userId))
            rval = new ResponseEntity<String>(HttpStatus.OK);

        return rval;
    }

    @RequestMapping(value = "/usedftui", method = RequestMethod.POST)
    public ResponseEntity<String> usedFtui() {
        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);
        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.QUICK);

        if (userAccessor.setUserCompletedFtui(user.getUserId(), true))
            rval = new ResponseEntity<String>(HttpStatus.OK);

        return rval;
    }

    @RequestMapping(value = "/trackclick", method = RequestMethod.POST)
    public ResponseEntity<String> trackClick(@RequestParam("timeOnPage") final String timeOnPage,
                                             @RequestParam("buttonClicked") final String buttonClicked) {
        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);

        return rval;
    }

    @RequestMapping(value = "/refreshBaseItemImages/{UUID}", method = RequestMethod.GET)
    public ResponseEntity<String> refreshBaseItemImages(@PathVariable("UUID") final String UUID,
                                                        HttpServletRequest request) {

        ResponseEntity<String> rval = new ResponseEntity<String>(HttpStatus.NOT_FOUND);

        if (UUID != null && UUID.equals(ItemAccessorTest.REFRESH_BASE_ITEM_UUID)) {
            List<Item> items = itemAccessor.getBaseItems();

            for (Item item : items)
                submitRenderToRealityServer(item);

            rval = new ResponseEntity<String>(HttpStatus.OK);
        }


        return rval;

    }

    @RequestMapping(value = "/submitftueprototypeorder", method = RequestMethod.POST)
    public
    @ResponseBody
    FtueCheckoutSubmitResult registerFtueUser(@RequestParam("parentModelId") final Long parentModelId,
                                              @RequestParam(value = "exportHash", required = true) final String exportHash,
                                              @RequestParam(value = "size", required = false) final String size,
                                              @RequestParam(value = "chain", required = false) final Long chain,
                                              @RequestParam("firstName") final String firstName,
                                              @RequestParam("lastName") final String lastName,
                                              @RequestParam("email") final String email,
                                              @RequestParam("address1") final String address1,
                                              @RequestParam(value = "address2", required = false) final String address2,
                                              @RequestParam("city") final String city,
                                              @RequestParam("state") final Long stateId,
                                              @RequestParam("zip") final String zip,
                                              HttpServletRequest req) {
        String WHITE_PLASTIC_MATERIAL_ID = "6";
        String WHITE_PLASTIC_FINISH_ID = "6";
        Long MAX_FTUE_ORDERS = 2L;
        Long COUNTRY_PRIMARY_KEY = 1L;
        FtueCheckoutSubmitResult rval = new FtueCheckoutSubmitResult();
        rval.setSuccess(false);
        AnonymousItem item = new AnonymousItem();

        Subdivision subdivision = countryAccessor.getSubdivision(stateId);

        try {

            if (orderAccessor.getFtueOrderCountByAddress(address1, address2, zip) < MAX_FTUE_ORDERS) {

                Address shippingAddress = new Address();
                shippingAddress.setFirstName(firstName);
                shippingAddress.setLastName(lastName);
                shippingAddress.setEmail(email);
                shippingAddress.setAddressLine1(address1);
                if (address2 != null && address2.length() > 0)
                    shippingAddress.setAddressLine2(address2);
                shippingAddress.setCity(city);
                shippingAddress.setPostalCode(zip);
                shippingAddress.setSubdivision(new Subdivision());
                shippingAddress.getSubdivision().setPrimaryKeyId(stateId);
                shippingAddress.setCountry(new Country());
                shippingAddress.getCountry().setPrimaryKeyId(COUNTRY_PRIMARY_KEY);

                User user = checkAndRegisterAnonymousUser(email, firstName, lastName, shippingAddress);

                CustomizerHash hash = gson.fromJson(exportHash, CustomizerHash.class);
                ItemCustomization sizeCustomization = new ItemCustomization();
                sizeCustomization.setSize(hash.getSize());

                Item parentItemReference = new Item();
                parentItemReference.setItemId(parentModelId);

                item.setAnonymousCustomizations(getCustomizationsFromHash(exportHash));
                item.getAnonymousCustomizations().add(sizeCustomization);

                item.setParentItem(parentItemReference);
                item.setMaterialId(WHITE_PLASTIC_MATERIAL_ID);
                item.setFinishId(WHITE_PLASTIC_FINISH_ID);
                item.setAssociatedUser(user);
                item.setOriginFtue(true);

                if (chain != null) {
                    Chain itemChain = new Chain();
                    itemChain.setChainId(chain);
                    item.setChain(itemChain);
                }

                itemAccessor.persistedAnonymousItem(item);

                Map<String, String> workerQueue = new HashMap<>();
                workerQueue.put("anonymousCustomizationId", item.getItemId().toString());
                workerQueue.put("userId", user.getUserId().toString());
                WorkerQueuer.queueWorkForWorker(workerQueue, WorkerController.FTUE_ORDER_AND_PRINT_URL);

                rval.setSuccess(true);

                //Set values in the session so that the confirm page can use them
                req.getSession().setAttribute(SESSION_VAR_ANONYMOUS_NAME, firstName + " " + lastName);
                req.getSession().setAttribute(SESSION_VAR_ANONYMOUS_EMAIL, email);
            }
            //User already has too many FTUE orders
            else {
                rval.setMessage("Sorry!  Free orders are limited to " + MAX_FTUE_ORDERS + " per household.");
            }
        } catch (Exception e) {
            logError(e);

            String error = "Error registering anonymous user with email " + email != null ? email : "null email." +
                    " and anonymous item referenceId " + item.getItemId() != null ? item.getItemId().toString() : "null itemId.";

            Map<String, String> founders = new HashMap<>();
            founders.put("Tim", "api@troveup.com");

            mailProvider.sendRawEmail("Anonymous User Registration Problem", "errors@troveup.com",
                    founders, error, false);
        }

        return rval;
    }

    @RequestMapping(value = "/genericsignup", method = RequestMethod.POST)
    public
    @ResponseBody
    GenericAjaxResponse genericLandingPageSignup(@RequestParam("email") final String email,
                                                 @RequestParam("type") final String type)
    {
        GenericAjaxResponse rval = new GenericAjaxResponse(false, "Oops!  Something went wrong, please try that again.");

        Signup.SIGNUP_TYPE enumType = Signup.getSignupTypeFromStringType(type);

        if (enumType != null)
        {
            Signup signup = new Signup();
            signup.setEmail(email);
            signup.setType(enumType);

            userAccessor.createSignup(signup);

            rval.setIsSuccess(true);
            rval.setErrorMessage("");
        }

        return rval;
    }


    public void buildAndSubmitCartItem(Item item, String finishId, String materialId, Float estimatedPrice,
                                       Money actualPrice, User user, String size, String engraveText, CartItem.CART_ITEM_STATUS status, Integer chain) {
        List<Material> materials = printSupplier.getSupplierMaterials();
        String materialName = Material.getMaterialNameByMaterialId(materialId, materials);
        String finishName = Finish.getFinishNameByFinishId(finishId, Material.getMaterialByMaterialId(materialId, materials).getFinishList());

        //Build the cart item and let any other queries know the state that it's in
        CartItem cartItem = new CartItem();
        cartItem.setCartItemReference(item);
        cartItem.setFinishId(finishId);
        cartItem.setMaterialId(materialId);
        cartItem.setEstimatedPrice(Float.toString(estimatedPrice));
        cartItem.setActualPrice(MoneyUtil.toProperScale(actualPrice.getAmount(), null));
        cartItem.setQuantity(1);
        cartItem.setCartItemStatus(status);
        cartItem.setCustomizations(item.getItemCustomizations());
        cartItem.setFrozenItemName(item.getItemName());
        cartItem.setMaterialName(materialName);
        cartItem.setFinishName(finishName);
        cartItem.setEngraveText(engraveText);
        if (chain != null)
            cartItem.setChain(new Chain(chain.longValue()));

        //Make sure that the user cart isn't null
        validateUserCart(user);

        cartAccessor.addCartItemToCart(user.getShoppingCart().getCartId(), cartItem, size);
        cartAccessor.refreshGrandTotal(user.getShoppingCart().getCartId(), false, false);
    }

    private void sendBraintreeFailureEmailToFounders(String failureReason,
                                                     String userEmail, Long userId, String vendorOrderNumber,
                                                     String braintreeTransactionId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Order for user ");
        stringBuilder.append(userEmail);
        stringBuilder.append(" with Id ");
        stringBuilder.append(userId);
        stringBuilder.append(" was cancelled with imaterialise using imat order number ");
        stringBuilder.append(vendorOrderNumber);
        stringBuilder.append(" but their braintree transaction refund failed with reason ");
        stringBuilder.append(failureReason);
        stringBuilder.append(".  Braintree transaction Id was ");
        stringBuilder.append(braintreeTransactionId);

        Map<String, String> founders = new HashMap<>();
        founders.put("Tim", "tim@troveup.com");
        founders.put("Brian", "brian@troveup.com");
        founders.put("Andrew", "andrew@troveup.com");

        mailProvider.sendRawEmail("Payment Processing Error " + userEmail, "hello@troveup.com", founders,
                stringBuilder.toString(), false);
    }

    private String userFriendlyErrorMessage(AJAX_ERROR_CODES errorCode) {
        return "Error -" + errorCode.ordinal() + ": Oops!  We're experiencing some technical difficulties.  " +
                "Please try submitting again in a few minutes.  Otherwise, contact us at hello@troveup.com.";
    }

    private void validateUserCart(User user) {
        if (user.getShoppingCart() == null || user.getShoppingCart().getCartId() == null) {
            Cart cart = new Cart();
            userAccessor.addShoppingCartToUser(user.getUserId(), cart);
            user.setShoppingCart(cart);
        }
    }

    /**
     * Subtracts store credit from the grand total in a way that doesn't give a negative amount.
     *
     * @param storeCreditAmount Amount of store credit available.
     * @param grandTotal        Amount from which to subtract, zero if negative.
     * @return Money object representing the final grand total, less the store credit, or zero if negative.
     */
    private Money applyStoreCredit(BigDecimal storeCreditAmount, BigDecimal grandTotal) {
        Money rval = MoneyUtil.bigDecimalToMoney(null, grandTotal);

        if (storeCreditAmount != null) {
            Money storeCreditAmountMoney = MoneyUtil.bigDecimalToMoney(null, storeCreditAmount);
            Money grandTotalMoney = MoneyUtil.bigDecimalToMoney(null, grandTotal);

            rval = grandTotalMoney.minus(storeCreditAmountMoney);

            if (rval.isLessThan(Money.zero(CurrencyUnit.USD)))
                rval = Money.zero(CurrencyUnit.USD);
        }

        return rval;
    }

    private User checkAndRegisterAnonymousUser(String email, String firstName, String lastName, Address shippingAddress)
            throws UsernameAlreadyInUseException {
        User user;
        String name = lastName == null ? firstName : (firstName + " " + lastName);

        user = userAccessor.getUserByEmail(email.toLowerCase(), IEnums.SEEK_MODE.QUICK);

        if (user == null) {

            user = new User();
            user.setRole(UserDetails.Role.ROLE_USER);
            user.setFirstName(name);
            user.setLastName("");
            user.setEmail(email.toLowerCase());
            user.setUsername(StringUtils.generateRandomUppercaseString(10));
            user.setPassword(encoder.encode(UUID.randomUUID().toString()));
            user.setFullProfileImagePath("https://storage.googleapis.com/troveup-imagestore/assets/img/default-user-icon.jpg");
            user.setProfileImageThumbnailPath("https://storage.googleapis.com/troveup-imagestore/assets/img/default-user-icon-thumb.jpg");
            user.setCoverPhotoImagePath("https://storage.googleapis.com/troveup-imagestore/assets/default_cover_photo.png");
            user.setHasUsedFtue(false);
            user.setEnabled(false);
            user.setIsAdmin(false);
            user.setOptOut(false);
            user.setOriginFtue(true);
            user.setAnonymousRegistration(true);
            user.setShippingAddress(shippingAddress);

            userAccessor.createUser(user);

            //Generate a more user-friendly username
            Map<String, String> workerQueue = new HashMap<>();
            workerQueue.put("userId", user.getUserId().toString());
            WorkerQueuer.queueWorkForWorker(workerQueue, WorkerController.GENERATE_USERNAME_URL);


        } else {
            userAccessor.updateUserShippingAddress(user.getUserId(), shippingAddress);
        }

        return user;
    }

    private List<ItemCustomization> getCustomizationsFromHash(String hash) {
        List<ItemCustomization> rval = new ArrayList<>();

        final CustomizerHash hashObject = gson.fromJson(hash, CustomizerHash.class);

        for (CustomizerOperator customization : hashObject.getOperators()) {
            rval.add(new ItemCustomization(customization.getId(), customization.getValue()));
        }

        for (String visibleMesh : hashObject.getVisible()) {
            rval.add(new ItemCustomization(visibleMesh));
        }

        return rval;
    }

    private ItemImage retrieveOrPersistItemImage(byte[] imageData, String defaultImageUrl)
    {
        Integer hashCode = Arrays.hashCode(imageData);
        ItemImage rval = itemAccessor.getItemImageByHash(hashCode);

        //New image, needs persisting
        if (rval == null)
        {
            Boolean scaleImages = false;

            String largeImageName = "simpleItem" + "-" + UUID.randomUUID().toString() + ".png";
            largeImageName = sendImageToCloudImageStorage(imageData, largeImageName, imageBucketName, GoogleCloudStorage.MIME_TYPE_PNG);

            rval = new ItemImage();
            rval.setImageHash(hashCode);

            if (largeImageName != null) {
                rval.setLargeImageUrlPath(imageBucketUrl + largeImageName);
                scaleImages = true;
            }
            //An error occurred somewhere along the way, fire off an e-mail and set a default as a backup
            else
            {
                if (defaultImageUrl != null) {
                    rval.setLargeImageUrlPath(defaultImageUrl);
                    rval.setMediumImageUrlPath(defaultImageUrl);
                    rval.setSmallImageUrlPath(defaultImageUrl);
                }
                else
                {
                    rval.setLargeImageUrlPath("https://storage.googleapis.com/troveup-imagestore/default-image-icon-1.jpg");
                }

                rval.setImageHash(null);

                sendErrorEmail("Image Upload Failure!  Please check the logs for more details.", "SimpleItem Image Upload Failure");
            }

            itemAccessor.persistItemImage(rval);

            if (scaleImages && !isLocalDevelopmentServer())
            {
                //Populate the medium and small images asynchronously
                Map<String, String> workerQueue = new HashMap<>();
                workerQueue.put("itemImageId", rval.getImageId().toString());
                WorkerQueuer.queueWorkForWorker(workerQueue, WorkerController.SCALE_IMAGE_URL);
            }
        }

        return rval;
    }
}
