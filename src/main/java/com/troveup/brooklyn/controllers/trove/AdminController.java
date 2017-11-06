package com.troveup.brooklyn.controllers.trove;

import com.easypost.exception.EasyPostException;
import com.troveup.brooklyn.model.PricingUpdateResponse;
import com.troveup.brooklyn.model.SamplePriceResponse;
import com.troveup.brooklyn.orm.admin.model.AlertBannerState;
import com.troveup.brooklyn.orm.cart.model.CartItem;
import com.troveup.brooklyn.orm.cart.model.GenericItem;
import com.troveup.brooklyn.orm.cart.model.PromoCode;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.countries.model.Country;
import com.troveup.brooklyn.orm.countries.model.Subdivision;
import com.troveup.brooklyn.orm.item.model.*;
import com.troveup.brooklyn.orm.order.model.EasyPostPostageLabel;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.order.model.PrintOrder;
import com.troveup.brooklyn.orm.simpleitem.initialize.ChristenDominique;
import com.troveup.brooklyn.orm.simpleitem.initialize.JaleesaMoses;
import com.troveup.brooklyn.orm.simpleitem.model.OrderNote;
import com.troveup.brooklyn.orm.simpleitem.model.SimpleItem;
import com.troveup.brooklyn.orm.user.model.Address;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.cdn.gcs.GoogleCloudStorage;
import com.troveup.brooklyn.sdk.cdn.model.CloudParam;
import com.troveup.brooklyn.sdk.mail.model.TroveBoxDimensions;
import com.troveup.brooklyn.spring.social.model.UserDetails;
import com.troveup.brooklyn.util.MoneyUtil;
import com.troveup.brooklyn.util.SizeMapper;
import com.troveup.brooklyn.util.StringUtils;
import com.troveup.brooklyn.util.exception.UsernameAlreadyInUseException;
import org.joda.money.Money;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tim on 10/1/15.
 */
@Controller
@RequestMapping(value = "/admin", method = {RequestMethod.GET, RequestMethod.HEAD})
public class AdminController extends CommonController {

    protected String jsonBucketName;

    protected String cloudStoreUrl;

    protected String imageStoreCdn;

    protected String imageStoreUrl;

    @PostConstruct
    public void init() {
        if (env.getProperty("cloudstore.jsonbucketname") != null)
            jsonBucketName = env.getProperty("cloudstore.jsonbucketname");
        else
            jsonBucketName = GoogleCloudStorage.CDN_JSON_BUCKET_NAME;

        if (env.getProperty("cloudstore.cloudstoreurl") != null)
            cloudStoreUrl = env.getProperty("cloudstore.cloudstoreurl");
        else
            cloudStoreUrl = GoogleCloudStorage.CDN_URL_BASE_PATH;

        if (env.getProperty("cloudstore.itemimagecdn") != null)
            imageStoreCdn = env.getProperty("cloudstore.itemimagecdn");
        else
            imageStoreCdn = "troveup-imagestore";

        imageStoreUrl = cloudStoreUrl + imageStoreCdn + "/assets/img/";

        cloudStoreUrl += jsonBucketName + "/models/";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView adminIndex(HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            rval.setViewName("admin/index");
            rval.addObject("isAuthenticated", isAuthenticated());
            rval.addObject("authUser", user);
        }

        return rval;
    }

    @RequestMapping(value = "/promocodes", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView adminPromoCodes(HttpServletRequest req) {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            rval.setViewName("admin/promocodes");
            rval.addObject("materials", printSupplier.getSupplierMaterials());
            rval.addObject("isAuthenticated", isAuthenticated());
            rval.addObject("authUser", user);
        }

        return rval;
    }

    @RequestMapping(value = "/livephoto", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView adminLivePhoto(HttpServletRequest req) {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            rval.setViewName("admin/livephoto");
            rval.addObject("isAuthenticated", isAuthenticated());
            rval.addObject("authUser", user);
        }

        return rval;
    }

    @RequestMapping(value = "livephotopost", method = {RequestMethod.POST})
    public ModelAndView adminLivePhotoPost(@RequestParam("itemid") final Long itemId,
                                           @RequestParam(value = "applyrelatives", required = false) final Boolean applyToRelatives,
                                           MultipartHttpServletRequest req) {
        Boolean success = false;
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {

            MultiValueMap<String, MultipartFile> imageFile = req.getMultiFileMap();

            Set<String> jsonFileKeys = imageFile.keySet();
            String imageName = (String) jsonFileKeys.toArray()[0];

            List<CloudParam> cloudParams = new ArrayList<>();
            cloudParams.add(new CloudParam(GoogleCloudStorage.KEY_BUCKET_NAME, imageStoreCdn));

            try {
                storage.writeBytes(imageFile.get(imageName).get(0).getBytes(),
                        "assets/img/" + imageName,
                        cloudParams);

                success = true;
            } catch (IOException e) {
                logError(e);
            }

            itemAccessor.updateLivePhotoUrl(itemId, imageStoreUrl + imageName, applyToRelatives == null ? false : applyToRelatives);

            rval.setViewName("admin/livephoto");
            rval.addObject("isAuthenticated", isAuthenticated());
            rval.addObject("authUser", user);
            rval.addObject("success", success);
        }

        return rval;
    }


    @RequestMapping(value = "/adminpromocodespost", method = RequestMethod.POST)
    public ModelAndView adminPromoCodesPost(@RequestParam("codetype") final String codeTypeSelect,
                                            @RequestParam("blanketdiscountcategory") final String blanketDiscountCategorySelect,
                                            @RequestParam("itemdiscountcategory") final String itemDiscountCategory,
                                            @RequestParam("valuediscounttype") final String valueDiscountTypeDropdown,
                                            @RequestParam("promocode") final String promocode,
                                            @RequestParam("amount") final Float amount,
                                            @RequestParam("unlimited") final String unlimitedDropdown,
                                            @RequestParam("expires") final String expiresDropdown,
                                            @RequestParam("expiresdate") final String expiresDate,
                                            @RequestParam("numuses") final Integer numberOfUses,
                                            @RequestParam("materialfinish") final String materialFinishDropdown,
                                            @RequestParam("material") final String materialDropdown,
                                            @RequestParam("itemcategory") final String itemCategoryDropdown,
                                            @RequestParam("purposefield") final String promocodePurpose,
                                            @RequestParam("bulkcheck") final String isBulk,
                                            @RequestParam("bulkamount") final Integer bulkAmount,
                                            @RequestParam("bulkcharlength") final Integer characterLength,
                                            HttpServletRequest req) throws IOException, ParseException {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            rval.setViewName("admin/promocodes");
            rval.addObject("materials", printSupplier.getSupplierMaterials());
            rval.addObject("isAuthenticated", isAuthenticated());
            rval.addObject("authUser", user);
            rval.addObject("success", true);

            PromoCode promoCode = new PromoCode();
            promoCode.setActive(true);
            promoCode.setControlReference(true);
            promoCode.setPromoCode(promocode.toUpperCase());
            promoCode.setGlobal(true);

            //Take care of the selection-dependent fields
            if (codeTypeSelect.equals("blanket")) {
                promoCode.setBlanketApplication(true);

                if (blanketDiscountCategorySelect.equals("subtotal")) {
                    promoCode.setBlanketCategory(PromoCode.DISCOUNT_CATEGORY.SUB_TOTAL.toString());
                } else {
                    promoCode.setBlanketCategory(PromoCode.DISCOUNT_CATEGORY.SHIPPING.toString());
                }
            } else {
                promoCode.setBlanketApplication(false);

                if (itemDiscountCategory.equals("material")) {
                    promoCode.setMaterialId(materialDropdown);
                } else if (itemDiscountCategory.equals("materialfinish")) {
                    String[] materialFinishSplit = materialFinishDropdown.split(" ");
                    promoCode.setMaterialId(materialFinishSplit[0]);
                    promoCode.setFinishId(materialFinishSplit[1]);
                } else if (itemDiscountCategory.equals("category")) {
                    promoCode.setItemCategory(itemCategoryDropdown);
                }
            }

            if (unlimitedDropdown.equals("no")) {
                promoCode.setUnlimited(true);
            } else {
                promoCode.setUnlimited(false);
                promoCode.setAvailableUses(numberOfUses);
            }

            if (expiresDropdown.equals("yes")) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                promoCode.setDoesExpire(true);
                promoCode.setExpires(formatter.parse(expiresDate));
            } else {
                promoCode.setDoesExpire(false);
            }

            if (valueDiscountTypeDropdown.equals("percent")) {
                promoCode.setPercentDiscount(amount);
            } else {
                promoCode.setDollarDiscount(new BigDecimal(amount));
            }

            if (promocodePurpose != null && promocodePurpose.length() > 0)
            {
                promoCode.setPurpose(promocodePurpose);
            }

            promoCode.setCharacterLength(characterLength != null && characterLength > 1 ? characterLength + 1 : 7);

            if (isBulk != null && isBulk.toLowerCase().equals("yes"))
            {
                rval.addObject("bulkGeneratedListOfPromocodes", promoCodeAccessor.bulkAddPromoCode(promoCode, bulkAmount));
            }
            else {
                promoCodeAccessor.addPromoCode(promoCode);
            }
        }

        return rval;
    }

    @RequestMapping(value = "/modelspost", method = RequestMethod.POST)
    public ModelAndView adminModelsPost(@RequestParam("categoryselect") final String categorySelect,
                                        @RequestParam("itemname") final String itemName,
                                        @RequestParam("itemdescription") final String itemDescription,
                                        @RequestParam("materialmenu") final String materialSelection,
                                        @RequestParam("engravable") final String engravable,
                                        @RequestParam("renderscene") final String renderScene,
                                        @RequestParam("ismobile") final String isMobile,
                                        @RequestParam("highresavailable") final String highResAvailableDropdown,
                                        @RequestParam(value = "livephotoavailable", required = false) final Boolean livePhotoAvailable,
                                        MultipartHttpServletRequest req) throws IOException {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            rval.setViewName("admin/models");
            rval.addObject("materials", printSupplier.getSupplierMaterials());
            rval.addObject("isAuthenticated", isAuthenticated());
            rval.addObject("authUser", user);
            rval.addObject("success", true);
            rval.addObject("categories", SizeMapper.getAvailableCategories());


            Map<String, String[]> params = req.getParameterMap();
            MultiValueMap<String, MultipartFile> jsonFile = req.getMultiFileMap();

            Set<String> jsonFileKeys = jsonFile.keySet();
            String jsonFileName = (String) jsonFileKeys.toArray()[0];

            String[] materialSelectionSplit = materialSelection.split(" ");

            User troveUser = new User();
            troveUser.setUserId(1l);

            //Set up a new base item
            Item item = new Item();
            item.setActive(true);
            item.setBaseItem(true);
            item.setCategory(categorySelect);
            item.setCreated(new Date());
            item.setCustomizerFilename(jsonFileName);
            item.setCustomizerPath(cloudStoreUrl);
            item.setMaterialId(materialSelectionSplit[0]);
            item.setFinishId(materialSelectionSplit[1]);

            if (highResAvailableDropdown.equals("yes"))
                item.setHighResolutionCustomizerFilename((String) jsonFileKeys.toArray()[1]);
            else
                item.setHighResolutionCustomizerFilename(jsonFileName);

            item.setItemDescription(itemDescription);
            item.setItemFileBaseName(jsonFileName.replace(".json", ""));
            item.setItemFileName(jsonFileName.replace(".json", ".obj"));
            item.setItemName(itemName);
            item.setItemOwner(troveUser);
            item.setPrivateItem(false);
            item.setRemadeCount(0l);
            item.setTrovedCount(0l);
            item.setRenderScene(renderScene);
            item.setSampleSupplier(Item.SAMPLE_SUPPLIER.SHAPEWAYS);
            item.setSampleMaterialId("6");
            item.setIsEngravable(engravable.toLowerCase().equals("yes"));
            item.setIsMobileCompatible(isMobile.toLowerCase().equals("yes"));

            //Set the default image while it's being generated, so that we don't get a blank item card
            ItemImage image = new ItemImage();
            image.setMaterial("red_gold_polished");
            image.setOrder(0);
            image.setCustomizerImage(true);
            image.setOwnerItem(item);
            image.setLargeImageUrlPath("https://storage.googleapis.com/troveup-imagestore/default-image-icon-1.jpg");
            item.setImages(new ArrayList<ItemImage>());
            item.getImages().add(image);

            String livePhotoKey = jsonFileKeys.toArray().length > 2 ? (String) jsonFileKeys.toArray()[2] : null;
            Boolean uploadPhoto = false;
            if (livePhotoKey != null &&
                    livePhotoAvailable != null &&
                    livePhotoAvailable && jsonFile.get(livePhotoKey) != null &&
                    jsonFile.get(livePhotoKey).get(0) != null &&
                    jsonFile.get(livePhotoKey).get(0).getBytes().length > 0) {
                item.setLivePhotoUrl(imageStoreUrl + jsonFileKeys.toArray()[2]);
                uploadPhoto = true;
            }

            //Create the item
            itemAccessor.createItem(item);

            userAccessor.addItemToTrove(1l, item.getItemId());

            //Set the base item and parent item to be self referencing
            itemAccessor.updateItemParentItemReference(item.getItemId(), item.getItemId());
            itemAccessor.updateItemBaseItemReference(item.getItemId(), item.getItemId());

            //Upload the json file
            List<CloudParam> cloudParams = new ArrayList<>();
            cloudParams.add(new CloudParam(GoogleCloudStorage.KEY_BUCKET_NAME, jsonBucketName));

            cloudParams.add(new CloudParam(GoogleCloudStorage.KEY_MIME_TYPE, "application/json"));

            if (!isLocalDevelopmentServer()) {

                storage.writeBytes(jsonFile.get(jsonFileName).get(0).getBytes(),
                        "models/" + jsonFileName,
                        cloudParams);

                if (highResAvailableDropdown.equals("yes")) {
                    storage.writeBytes(jsonFile.get((String) jsonFileKeys.toArray()[1]).get(0).getBytes(),
                            "models/" + (String) jsonFileKeys.toArray()[1],
                            cloudParams);
                }

                submitRenderToRealityServer(item);

                if (uploadPhoto)
                {
                    cloudParams = new ArrayList<>();
                    cloudParams.add(new CloudParam(GoogleCloudStorage.KEY_BUCKET_NAME, imageStoreCdn));

                    storage.writeBytes(jsonFile.get(livePhotoKey).get(0).getBytes(),
                            "assets/img/" + livePhotoKey,
                            cloudParams);
                }


            }
        }

        return rval;
    }

    @RequestMapping(value = "/models", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView adminModels(HttpServletRequest req) {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            rval.setViewName("admin/models");
            rval.addObject("materials", printSupplier.getSupplierMaterials());
            rval.addObject("isAuthenticated", isAuthenticated());
            rval.addObject("authUser", user);
            rval.addObject("categories", SizeMapper.getAvailableCategories());
        }

        return rval;
    }

    @RequestMapping(value = "/updatemodel", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView adminModelUpdate(HttpServletRequest req) {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {

            List<Item> baseItems = itemAccessor.getBaseItems();

            rval.setViewName("admin/updatemodel");
            rval.addObject("baseItems", baseItems);
            rval.addObject("materials", printSupplier.getSupplierMaterials());
            rval.addObject("isAuthenticated", isAuthenticated());
            rval.addObject("authUser", user);
            rval.addObject("categories", SizeMapper.getAvailableCategories());
        }

        return rval;
    }

    @RequestMapping(value = "/updatemodelpost", method = RequestMethod.POST)
    public ModelAndView adminModelUpdatePost(@RequestParam("highresavailable") final String highResAvailableDropdown,
                                        @RequestParam("basemodelselect") final Long baseModelIdToUpdate,
                                        MultipartHttpServletRequest req) throws IOException {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {

            try {
                Map<String, String[]> params = req.getParameterMap();
                MultiValueMap<String, MultipartFile> jsonFile = req.getMultiFileMap();

                Set<String> jsonFileKeys = jsonFile.keySet();
                String jsonFileName = (String) jsonFileKeys.toArray()[0];

                String randomString = StringUtils.generateRandomUppercaseString(5) + "-" + StringUtils.generateRandomUppercaseString(5);

                //Upload the json file
                List<CloudParam> cloudParams = new ArrayList<>();
                cloudParams.add(new CloudParam(GoogleCloudStorage.KEY_BUCKET_NAME, jsonBucketName));

                cloudParams.add(new CloudParam(GoogleCloudStorage.KEY_MIME_TYPE, "application/json"));

                if (!isLocalDevelopmentServer()) {

                    String lowResFilename = jsonFileName.replace(".json", "-" + randomString + ".json");
                    String highResFilename = ((String) jsonFileKeys.toArray()[1]).replace(".json", "-" + randomString + ".json");

                    storage.writeBytes(jsonFile.get(jsonFileName).get(0).getBytes(),
                            "models/" + lowResFilename,
                            cloudParams);

                    if (highResAvailableDropdown.equals("yes")) {
                        storage.writeBytes(jsonFile.get((String) jsonFileKeys.toArray()[1]).get(0).getBytes(),
                                "models/" + highResFilename,
                                cloudParams);

                        itemAccessor.updateBaseAndChildItemsCustomizerFilename(baseModelIdToUpdate, lowResFilename, false);
                        itemAccessor.updateBaseAndChildItemsCustomizerHighresFilename(baseModelIdToUpdate, highResFilename);

                    } else
                    {
                        itemAccessor.updateBaseAndChildItemsCustomizerFilename(baseModelIdToUpdate, lowResFilename);
                    }
                }

                List<Item> baseItems = itemAccessor.getBaseItems();

                rval.setViewName("admin/updatemodel");
                rval.addObject("baseItems", baseItems);
                rval.addObject("isAuthenticated", isAuthenticated());
                rval.addObject("authUser", user);
                rval.addObject("success", true);
                rval.addObject("categories", SizeMapper.getAvailableCategories());

            } catch (Exception e)
            {
                logError(e);
            }
        }

        return rval;
    }



    @RequestMapping(value = "/labels", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView labelMaker(HttpServletRequest req) {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {

            List<Country> countries = countryAccessor.getAllCountries(IEnums.SEEK_MODE.FULL);

            List<Subdivision> states = countries.get(0).getSubdivisions();

            Collections.sort(states, new Comparator<Subdivision>() {
                @Override
                public int compare(Subdivision o1, Subdivision o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });

            rval.addObject("states", countries.get(0).getSubdivisions());
            rval.setViewName("admin/label");
            rval.addObject("materials", printSupplier.getSupplierMaterials());
            rval.addObject("isAuthenticated", isAuthenticated());
            rval.addObject("authUser", user);
        }

        return rval;
    }

    @RequestMapping(value = "/labelspost", method = {RequestMethod.POST, RequestMethod.HEAD})
    public ModelAndView labelMakerPost(@RequestParam("toname") final String toName,
                                       @RequestParam("toaddress1") final String toAddress1,
                                       @RequestParam("toaddress2") final String toAddress2,
                                       @RequestParam("tocity") final String toCity,
                                       @RequestParam("tostatemenu") final String toState,
                                       @RequestParam("tozip") final String toZip,
                                       @RequestParam("usedefault") final String useDefaultFrom,
                                       @RequestParam("fromname") final String fromName,
                                       @RequestParam("fromaddress1") final String fromAddress1,
                                       @RequestParam("fromaddress2") final String fromAddress2,
                                       @RequestParam("fromcity") final String fromCity,
                                       @RequestParam("fromstatemenu") final String fromState,
                                       @RequestParam("fromzip") final String fromZip,
                                       HttpServletRequest req) {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {

            EasyPostPostageLabel label = null;
            List<Country> countries = countryAccessor.getAllCountries(IEnums.SEEK_MODE.FULL);

            Address fromAddress = new Address();
            Address toAddress = new Address();
            toAddress.setFirstName(toName.trim());
            toAddress.setAddressLine1(toAddress1.trim());
            if (toAddress2 != null)
                toAddress.setAddressLine2(toAddress2.trim());
            toAddress.setCity(toCity.trim());
            toAddress.setSubdivision(countries.get(0).getSubdivisionByCode(toState.replace("US-", "")));
            toAddress.setPostalCode(toZip.trim());

            if (useDefaultFrom.equals("yes")) {
                fromAddress.setFirstName("Trove, Inc.");
                fromAddress.setAddressLine1("48 Wall St.");
                fromAddress.setAddressLine2("Floor Five");
                fromAddress.setCity("New York");
                fromAddress.setSubdivision(countries.get(0).getSubdivisionByCode("NY"));
                fromAddress.setPostalCode("10005");
            } else {
                fromAddress.setFirstName(fromName.trim());
                fromAddress.setAddressLine1(fromAddress1.trim());
                if (fromAddress2 != null)
                    fromAddress.setAddressLine2(fromAddress2.trim());
                fromAddress.setCity(fromCity.trim());
                fromAddress.setSubdivision(countries.get(0).getSubdivisionByCode(fromState.replace("US-", "")));
                fromAddress.setPostalCode(fromZip.trim());
            }

            try {
                label = easyPostProvider.purchaseLabel(toAddress, fromAddress,
                        TroveBoxDimensions.getTroveBoxDimensions(), 0);
                rval.addObject("success", true);
                rval.addObject("easyPostLabelUrl", label.getLabelUrl());
            } catch (EasyPostException e) {
                e.printStackTrace();
                rval.addObject("error", true);

                if (e.getMessage() != null)
                    rval.addObject("easyPostLabelError", e.getMessage());
                else if (e.getCause() != null && e.getCause().toString() != null)
                    rval.addObject("easyPostLabelError", e.getCause().toString());
                else if (e.getLocalizedMessage() != null)
                    rval.addObject("easyPostLabelError", e.getLocalizedMessage());
                else {
                    rval.addObject("easyPostLabelError", "Check the logs, couldn't print the message.");
                    logError(e);
                }


            }

            List<Subdivision> states = countries.get(0).getSubdivisions();

            Collections.sort(states, new Comparator<Subdivision>() {
                @Override
                public int compare(Subdivision o1, Subdivision o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });

            rval.addObject("states", countries.get(0).getSubdivisions());
            rval.setViewName("admin/label");
            rval.addObject("materials", printSupplier.getSupplierMaterials());
            rval.addObject("isAuthenticated", isAuthenticated());
            rval.addObject("authUser", user);
        }

        return rval;
    }

    @RequestMapping(value = "/chains", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView adminChains(HttpServletRequest req) {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            rval.setViewName("admin/chains");
            rval.addObject("isAuthenticated", isAuthenticated());
            rval.addObject("authUser", user);
        }

        return rval;
    }

    @RequestMapping(value = "/chainspost", method = RequestMethod.POST)
    public ModelAndView adminChainsPost(@RequestParam("chainname") final String chainName,
                                        @RequestParam("description") final String description,
                                        @RequestParam("price") final Float price,
                                        HttpServletRequest req) {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        Chain chain = new Chain();
        chain.setActive(true);
        chain.setName(chainName);
        chain.setDescription(description);
        chain.setPrice(new BigDecimal(price));

        itemAccessor.addChain(chain);

        rval.addObject("success", true);
        rval.setViewName("admin/chains");

        return rval;
    }

    @RequestMapping(value = "/pricing", method = RequestMethod.GET)
    public ModelAndView adminPricing(HttpServletRequest req) {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            rval.setViewName("admin/pricing");
            rval.addObject("materials", printSupplier.getSupplierMaterials());
            rval.addObject("isAuthenticated", isAuthenticated());
            rval.addObject("authUser", user);
            rval.addObject("categories", SizeMapper.getAvailableCategories());
        }

        return rval;
    }

    @RequestMapping(value = "/variables", method = RequestMethod.GET)
    public
    @ResponseBody
    PriceFilter getItemPricingVariables(@RequestParam("appLevel") final String appLevel,
                                        @RequestParam(value = "includeMaterials", required = false) final Boolean includeMaterials,
                                        @RequestParam(value = "materialFinish", required = false) final String materialFinish,
                                        @RequestParam(value = "category", required = false) final String category,
                                        @RequestParam(value = "item", required = false) final Long itemId) {
        PriceFilter rval;

        String[] materialParse = null;
        if (includeMaterials != null && includeMaterials) {
            materialParse = materialFinish.split(" ");
        }

        if (appLevel.equals("global")) {
            if (includeMaterials != null && includeMaterials) {

                rval = itemAccessor.getLatestGlobalPriceFilter(PriceFilter.MATERIAL_LEVEL.MATERIAL, materialParse[0],
                        materialParse[1], IEnums.SEEK_MODE.QUICK);
            } else {
                rval = itemAccessor.getLatestGlobalPriceFilter(PriceFilter.MATERIAL_LEVEL.ALL, null, null, IEnums.SEEK_MODE.QUICK);
            }
        } else if (appLevel.equals("category")) {
            if (includeMaterials != null && includeMaterials) {
                rval = itemAccessor.getLatestCategoryPriceFilter(PriceFilter.MATERIAL_LEVEL.MATERIAL, category,
                        materialParse[0], materialParse[1], IEnums.SEEK_MODE.QUICK);
            } else {
                rval = itemAccessor.getLatestCategoryPriceFilter(PriceFilter.MATERIAL_LEVEL.ALL, category, null, null,
                        IEnums.SEEK_MODE.QUICK);
            }

        }
        //Item level
        else {

            Item item = itemAccessor.getItemDetached(itemId, IEnums.SEEK_MODE.ITEM_PRICING);

            if (includeMaterials != null && includeMaterials) {
                rval = itemAccessor.getApplicablePricefilter(item, materialParse[0], materialParse[1], IEnums.SEEK_MODE.QUICK);
            } else {
                rval = itemAccessor.getApplicablePricefilter(item, null, null, IEnums.SEEK_MODE.QUICK);
            }

        }

        if (rval != null)
            rval.scaleBigDecimals();

        return rval;
    }

    @RequestMapping(value = "/pricingpost", method = RequestMethod.POST)
    public
    @ResponseBody
    PricingUpdateResponse adminPricingPost(@RequestParam(value = "applevel", required = true) final String appLevel,
                                           @RequestParam(value = "category", required = false) final String category,
                                           @RequestParam(value = "itemid", required = false) final Long itemId,
                                           @RequestParam(value = "applyrelatives", required = false) final Boolean applyToRelatives,
                                           @RequestParam(value = "markupnew", required = false) final Float newMarkup,
                                           @RequestParam(value = "packagingnew", required = false) final Float newPackaging,
                                           @RequestParam(value = "shippingnew", required = false) final Float shippingNew,
                                           @RequestParam(value = "prototypingnew", required = false) final Float prototypeNew,
                                           @RequestParam(value = "includeMaterial") final Boolean includeMaterial,
                                           @RequestParam(value = "appliedMaterial", required = false) final String appliedMaterial,
                                           @RequestParam(value = "variableType") final String variableType,
                                           @RequestParam(value = "fixedPrice", required = false) final Float fixedPrice) {
        PricingUpdateResponse rval = new PricingUpdateResponse(false, 0);

        User user = null;

        BigDecimal decimalMarkup = null;
        BigDecimal decimalPackaging = null;
        BigDecimal decimalShipping = null;
        BigDecimal decimalPrototyping = null;
        BigDecimal decimalFixedPrice = null;

        if (newMarkup != null)
            decimalMarkup = new BigDecimal(newMarkup + 1.00f);

        if (newPackaging != null)
            decimalPackaging = new BigDecimal(newPackaging);

        if (shippingNew != null)
            decimalShipping = new BigDecimal(shippingNew);

        if (prototypeNew != null)
            decimalPrototyping = new BigDecimal(prototypeNew);

        if (fixedPrice != null)
            decimalFixedPrice = new BigDecimal(fixedPrice);

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            PriceFilter filter = new PriceFilter();

            if (appLevel.equals("global")) {
                filter.setApplicationLevel(PriceFilter.APPLICATION_LEVEL.GLOBAL);
            } else if (appLevel.equals("category")) {
                filter.setApplicationLevel(PriceFilter.APPLICATION_LEVEL.CATEGORY);
                filter.setCategory(category);
            } else if (appLevel.equals("item")) {
                filter.setApplicationLevel(PriceFilter.APPLICATION_LEVEL.ITEM);
                Item item = new Item();
                item.setItemId(itemId);
                filter.setItemReference(item);
            }

            if (variableType.equals("fixed")) {
                filter.setFlatPrice(decimalFixedPrice);
                filter.setPriceType(PriceFilter.PRICE_TYPE.STATIC);
            } else {
                filter.setMarkupPercentage(decimalMarkup);
                filter.setPackagingMarkup(decimalPackaging);
                filter.setShippingMarkup(decimalShipping);
                filter.setPrototypeMarkup(decimalPrototyping);
                filter.setPriceType(PriceFilter.PRICE_TYPE.FORMULAIC);
            }

            if (includeMaterial) {
                String[] materialParse = appliedMaterial.split(" ");
                filter.setMaterialId(materialParse[0]);
                filter.setFinishId(materialParse[1]);
                filter.setMaterialLevel(PriceFilter.MATERIAL_LEVEL.MATERIAL);
            } else {
                filter.setMaterialLevel(PriceFilter.MATERIAL_LEVEL.ALL);
            }

            rval.setUpdateCount(itemAccessor.persistPriceFilter(filter, applyToRelatives));
            rval.setSuccess(rval.getUpdateCount() > 0);
        }

        return rval;
    }

    @RequestMapping(value = "/sampleprice", method = RequestMethod.GET)
    public
    @ResponseBody
    SamplePriceResponse samplePrice(@RequestParam("itemid") final Long itemId,
                                    @RequestParam("materialfinish") final String materialFinish,
                                    @RequestParam(value = "markupnew", required = false) final Float newMarkup,
                                    @RequestParam(value = "packagingnew", required = false) final Float newPackaging,
                                    @RequestParam(value = "shippingnew", required = false) final Float newShipping,
                                    @RequestParam(value = "prototypingnew", required = false) final Float newPrototype) {
        SamplePriceResponse rval = new SamplePriceResponse("0.00");

        String[] materialFinishSplit = materialFinish.split(" ");

        String material = materialFinishSplit[0];
        String finish = materialFinishSplit[1];

        User user = null;

        BigDecimal decimalMarkup = null;
        BigDecimal decimalPackaging = null;
        BigDecimal decimalShipping = null;
        BigDecimal decimalPrototyping = null;

        if (newMarkup != null)
            decimalMarkup = new BigDecimal(newMarkup + 1.00f);

        if (newPackaging != null)
            decimalPackaging = new BigDecimal(newPackaging);

        if (newShipping != null)
            decimalShipping = new BigDecimal(newShipping);

        if (newPrototype != null)
            decimalPrototyping = new BigDecimal(newPrototype);

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {

            PriceFilter filter = new PriceFilter();
            filter.setMaterialLevel(PriceFilter.MATERIAL_LEVEL.MATERIAL);
            filter.setApplicationLevel(PriceFilter.APPLICATION_LEVEL.ITEM);
            filter.setPriceType(PriceFilter.PRICE_TYPE.FORMULAIC);
            filter.setMarkupPercentage(decimalMarkup);
            filter.setPackagingMarkup(decimalPackaging);
            filter.setShippingMarkup(decimalShipping);
            filter.setPrototypeMarkup(decimalPrototyping);

            Money price = getItemPrice(itemId, material, finish, null, filter);

            rval.setPrice(MoneyUtil.toProperScale(price.getAmount(), null).toString());
        }

        return rval;
    }

    @RequestMapping(value = "/alertbanner", method = RequestMethod.GET)
    public ModelAndView alertBanner(HttpServletRequest req) {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            rval.setViewName("admin/banner");
            rval.addObject("isAuthenticated", isAuthenticated());
            rval.addObject("authUser", user);
            rval.addObject("adminAlertPanel", true);
        }

        return rval;
    }

    @RequestMapping(value = "/alertbannerpost", method = RequestMethod.POST)
    public ModelAndView alertBannerPost(@RequestParam(value = "bannerenabled", required = false) final Boolean enabled,
                                        @RequestParam(value = "bannertext", required = false) final String bannerText,
                                        @RequestParam(value = "mobilebannertext", required = false) final String mobileBannerText,
                                        @RequestParam(value = "buttonenabled", required = false) final Boolean buttonEnabled,
                                        @RequestParam(value = "buttontext", required = false) final String buttonText,
                                        @RequestParam(value = "buttonurl", required = false) final String buttonUrl,
                                        HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);
        Boolean successState = true;

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            AlertBannerState banner;

            if (enabled != null && enabled)
            {
                banner = new AlertBannerState();

                //Regular banner text is required, notify the user if something went wrong
                if (bannerText != null && bannerText.length() > 0) {
                    banner.setBannerText(bannerText);
                    banner.setMobileBannerText(bannerText);
                    banner.setBannerState(AlertBannerState.BANNER_STATE.ENABLED);
                }
                else
                {
                    successState = false;
                }

                if (mobileBannerText != null && mobileBannerText.length() > 0)
                {
                    banner.setMobileBannerText(mobileBannerText);
                }

                if (buttonEnabled != null && buttonEnabled)
                {
                    banner.setButtonState(AlertBannerState.BANNER_STATE.ENABLED);

                    if (buttonText != null && buttonText.length() > 0 && buttonUrl != null && buttonUrl.length() > 0)
                    {
                        banner.setButtonText(buttonText);
                        banner.setButtonUrl(buttonUrl);
                    }
                    else
                    {
                        successState = false;
                    }
                }
                else
                {
                    banner.setButtonState(AlertBannerState.BANNER_STATE.DISABLED);
                }
            }
            else
            {
                banner = AlertBannerState.getDisabledBanner();
            }

            if (successState && banner != null)
            {
                adminAccessor.persistAlertBannerState(banner);
            }

            rval.setViewName("admin/banner");
            rval.addObject("isAuthenticated", isAuthenticated());
            rval.addObject("authUser", user);
            rval.addObject("success", successState);
        }

        return rval;
    }

    @RequestMapping(value = "/orders", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView adminOrders(HttpServletRequest req) {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            List<String> itemCategories = new ArrayList<>();
            itemCategories.addAll(SizeMapper.getAvailableCategories());
            itemCategories.addAll(GenericItem.getGenericItemTypes());

            rval.setViewName("admin/orders");
            rval.addObject("isAuthenticated", isAuthenticated());
            rval.addObject("authUser", user);
            rval.addObject("orderstatustypes", Order.getOrderStatusList());
            rval.addObject("cartitemstatustypes", CartItem.getCartItemStatusList());
            rval.addObject("cartitemcategorytypes", itemCategories);

            if (req.getSession().getAttribute(SESSION_VAR_ADMIN_BACK_BUTTON_PRESERVATION_SETTINGS) != null)
            {
                rval.addObject("state", StringUtils.escapeStringQuotes((String)req.getSession().getAttribute(SESSION_VAR_ADMIN_BACK_BUTTON_PRESERVATION_SETTINGS)));
            }
        }

        return rval;
    }

    @RequestMapping(value = "/order/{orderId}", method = RequestMethod.GET)
    public ModelAndView order(@PathVariable final Long orderId,
                              HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId()))
        {
            Order order = orderAccessor.getOrderWithPurchaserByOrderId(orderId, IEnums.SEEK_MODE.ORDER_FULL);

            if (order.getBillingAddress() != null && order.getBillingAddress().getSubdivision() != null
                    && order.getBillingAddress().getSubdivision().getCode() != null)
            {
                order.getBillingAddress().getSubdivision().setCode(order.getBillingAddress().getSubdivision().getCode().replace("US-", ""));
            }

            if (order.getShippingAddress() != null && order.getShippingAddress().getSubdivision() != null
                    && order.getShippingAddress().getSubdivision().getCode() != null)
            {
                order.getShippingAddress().getSubdivision().setCode(order.getShippingAddress().getSubdivision().getCode().replace("US-", ""));
            }

            order.setSubTotal(MoneyUtil.toProperScale(order.getSubTotal(), null));
            order.setTax(MoneyUtil.toProperScale(order.getTax(), null));
            order.setShipping(MoneyUtil.toProperScale(order.getShipping(), null));
            order.setGrandTotal(MoneyUtil.toProperScale(order.getGrandTotal(), null));

            if(order.getOrderItems() != null && order.getOrderItems().size() > 0)
            {
                for (CartItem item : order.getOrderItems())
                {
                    item.setActualPrice(MoneyUtil.toProperScale(item.getActualPrice(), null));

                    item.setSize(SizeMapper.getStandardizedSize(item.getSize(item.getCustomizationIteration())));
                }
            }

            if (order.getGenericItemsList() != null && order.getGenericItemsList().size() > 0)
            {
                for (GenericItem item : order.getGenericItemsList())
                {
                    item.setPrice(MoneyUtil.toProperScale(item.getPrice(), null));
                }
            }

            List<PromoCode> usedPromoCodes = getPromoCodesFromOrder(order);
            List<OrderNote> orderNotes = order.getOrderNotes();

            for (PromoCode code : usedPromoCodes)
            {
                code.setPriceDelta(MoneyUtil.toProperScale(code.getPriceDelta(), null));
            }

            Integer labelCount = 0;
            if (order.getOrderItems() != null)
            {
                if (order.getOrderItems() != null)
                {
                    for (CartItem item : order.getOrderItems())
                    {
                        if (item.getManufacturerOrders() != null)
                        {
                            for (PrintOrder printOrder : item.getManufacturerOrders())
                            {
                                if (printOrder.getPrintLabels() != null) {
                                    labelCount += printOrder.getPrintLabels().size();
                                }
                            }
                        }
                    }
                }
            }

            if (order.getShippingLabels() != null) {
                labelCount += order.getShippingLabels().size();
            }

            //TODO:  Replace label count with actual logic
            rval.addObject("labelCount", labelCount);
            rval.addObject("baseSiteUrl", getSiteUrl());
            rval.addObject("order", order);
            rval.addObject("usedPromoCodes", usedPromoCodes);
            rval.addObject("orderNotes", orderNotes);
            rval.addObject("authUser", user);

            rval.setViewName("admin/order");
        }

        return rval;
    }

    @RequestMapping(value = "/orderlabel/{orderId}", method = RequestMethod.GET)
    public ModelAndView labelsViewer(@PathVariable final Long orderId,
                              HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId()))
        {
            Order order = orderAccessor.getOrder(orderId, IEnums.SEEK_MODE.ORDER_FULL);

            List<EasyPostPostageLabel> labels = new ArrayList<>();

            if (order.getOrderItems() != null)
            {
                if (order.getOrderItems() != null)
                {
                    for (CartItem item : order.getOrderItems())
                    {
                        if (item.getManufacturerOrders() != null)
                        {
                            for (PrintOrder printOrder : item.getManufacturerOrders())
                            {
                                if (printOrder.getPrintLabels() != null)
                                {
                                    for (EasyPostPostageLabel label : printOrder.getPrintLabels())
                                    {
                                        label.setAssociatedCartItemId(item.getCartItemId());
                                        labels.add(label);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (order.getShippingLabels() != null)
                labels.addAll(order.getShippingLabels());

            Collections.sort(labels, new Comparator<EasyPostPostageLabel>() {
                @Override
                public int compare(EasyPostPostageLabel o1, EasyPostPostageLabel o2) {
                    return Long.compare(o2.getEasyPostPostageLabelPrimaryKey(), o1.getEasyPostPostageLabelPrimaryKey());
                }
            });

            rval.addObject("labels", labels);
            rval.addObject("order", order);
            rval.setViewName("admin/orderlabel");

        }

        return rval;

    }

    @RequestMapping(value = "/printorders/{cartItemId}", method = RequestMethod.GET)
    public ModelAndView printOrdersViewer(@PathVariable final Long cartItemId,
                                          HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            CartItem cartItem = cartAccessor.getCartItem(cartItemId, IEnums.SEEK_MODE.FULL);
            cartItem.setActualPrice(MoneyUtil.toProperScale(cartItem.getActualPrice(), null));
            rval.addObject("cartItem", cartItem);
            rval.setViewName("admin/printorders");
        }

        return rval;
    }

    @RequestMapping(value = "/simpleitem", method = RequestMethod.GET)
    public ModelAndView simpleItem(HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            List<User> influencers = userAccessor.getAllInfluencers(IEnums.SEEK_MODE.QUICK);
            rval.addObject("influencers", influencers);
            rval.setViewName("admin/simpleitem");
        }

        return rval;
    }



    @RequestMapping(value = "/initsimpleitems", method = RequestMethod.GET)
    public ModelAndView simpleItems(HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId())) {
            rval.setViewName("admin/initsimpleitems");
        }

        return rval;
    }

    @RequestMapping(value = "/initsimpleitemspost", method = RequestMethod.POST)
    public ModelAndView initSimpleItemReferences(HttpServletRequest req) throws UsernameAlreadyInUseException {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null && userAccessor.checkUserAdmin(user.getUserId()))
        {
            User christenDominiqueUser = userAccessor.getUserByEmail(IndexController.CHRISTEN_DOMINIQUE_EMAIL, IEnums.SEEK_MODE.QUICK);

            /*if (christenDominiqueUser == null) {
                christenDominiqueUser = new User();
                christenDominiqueUser.setRole(UserDetails.Role.ROLE_USER);
                christenDominiqueUser.setFirstName("Christen");
                christenDominiqueUser.setLastName("Dominique");
                christenDominiqueUser.setEmail(IndexController.CHRISTEN_DOMINIQUE_EMAIL);
                christenDominiqueUser.setUsername("christendominique");
                christenDominiqueUser.setPassword(encoder.encode(UUID.randomUUID().toString()));
                christenDominiqueUser.setFullProfileImagePath("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/profile/christdom.jpg");
                christenDominiqueUser.setProfileImageThumbnailPath("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/profile/christdom.jpg");
                christenDominiqueUser.setCoverPhotoImagePath("https://storage.googleapis.com/troveup-imagestore/assets/default_cover_photo.png");
                christenDominiqueUser.setHasUsedFtue(false);
                christenDominiqueUser.setEnabled(true);
                christenDominiqueUser.setIsAdmin(false);
                christenDominiqueUser.setInfluencer(true);

                userAccessor.createUser(christenDominiqueUser);
            }

            List<SimpleItem> items = ChristenDominique.generateInitialChristenDominiqueItems();

            for (SimpleItem item : items)
            {
                item.setInfluencerUserAccountId(christenDominiqueUser.getUserId());
                simpleItemAccessor.persistSimpleItem(item);
            }*/

            /*User jaleesaMosesUser = userAccessor.getUserByEmail(IndexController.JALEESA_MOSES_EMAIL, IEnums.SEEK_MODE.QUICK);

            if (jaleesaMosesUser == null) {
                jaleesaMosesUser = new User();
                jaleesaMosesUser.setRole(UserDetails.Role.ROLE_USER);
                jaleesaMosesUser.setFirstName("Jaleesa");
                jaleesaMosesUser.setLastName("Moses");
                jaleesaMosesUser.setEmail(IndexController.JALEESA_MOSES_EMAIL);
                jaleesaMosesUser.setUsername("jaleesamoses");
                jaleesaMosesUser.setPassword(encoder.encode(UUID.randomUUID().toString()));
                jaleesaMosesUser.setFullProfileImagePath("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/jaleesamoses/profile/jaleesa-profile-img.jpg");
                jaleesaMosesUser.setProfileImageThumbnailPath("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/jaleesamoses/profile/jaleesa-profile-img.jpg");
                jaleesaMosesUser.setCoverPhotoImagePath("https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/jaleesamoses/profile/jaleesa-banner.png");
                jaleesaMosesUser.setHasUsedFtue(false);
                jaleesaMosesUser.setEnabled(true);
                jaleesaMosesUser.setIsAdmin(false);
                jaleesaMosesUser.setInfluencer(true);

                userAccessor.createUser(jaleesaMosesUser);
            }

            List<SimpleItem> items = JaleesaMoses.generateInitialJaleesaMosesItems();

            for (SimpleItem item : items)
            {
                item.setInfluencerUserAccountId(jaleesaMosesUser.getUserId());
                simpleItemAccessor.persistSimpleItem(item);
            }*/
        }

        return rval;
    }

    private List<PromoCode> getPromoCodesFromOrder(Order order)
    {
        List<PromoCode> rval = new ArrayList<>();

        if (order.getCartReference().getAppliedPromoCodes() != null && order.getCartReference().getAppliedPromoCodes().size() > 0) {
            rval.addAll(order.getCartReference().getAppliedPromoCodes());
        }

        for (CartItem item : order.getOrderItems())
        {
            if (item.getAppliedPromoCode() != null)
                rval.add(item.getAppliedPromoCode());
        }

        return rval;
    }
}
