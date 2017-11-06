package com.troveup.brooklyn.controllers.trove;

import com.troveup.brooklyn.model.*;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.countries.model.Country;
import com.troveup.brooklyn.orm.countries.model.Subdivision;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.item.model.ItemAttribute;
import com.troveup.brooklyn.orm.item.model.ItemCustomization;
import com.troveup.brooklyn.orm.item.model.ItemImage;
import com.troveup.brooklyn.orm.materials.model.Finish;
import com.troveup.brooklyn.orm.materials.model.Material;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.meshexporter.forge.model.CustomizerHash;
import com.troveup.brooklyn.sdk.meshexporter.forge.model.CustomizerOperator;
import com.troveup.brooklyn.util.SizeMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Main controller where all of the public actions take place.  This includes all of the public views the user might
 * access, and serving up all of the possible data that each view might need.
 */
@Controller
@RequestMapping(value = "/public", method = {RequestMethod.GET, RequestMethod.HEAD})
public class PublicController extends CommonController
{

    private String customizerPath;
    private final String FINISH_ID_18K_GOLD_PLATED = "112";
    private final String FINISH_ID_14K_GOLD_PLATED = "110";
    private final String FINISH_ID_14K_ROSE_GOLD_PLATED = "111";
    private final String FINISH_ID_RHODIUM_PLATED = "113";

    @PostConstruct
    public void init()
    {
        customizerPath = env.getProperty("cloudstore.cloudstoreurl") + env.getProperty("cloudstore.jsonbucketname") +
                "/" + env.getProperty("cloudstore.customizerfragmenturl") +
                env.getProperty("cloudstore.customizerversionurlfragment");
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView Index(HttpServletRequest req)
    {
        ModelAndView mav = getModelAndView("index", req);
        return mav;
    }

    @RequestMapping(value = "/browse", method = RequestMethod.GET)
    public ModelAndView Browse(HttpServletRequest req)
    {
        ModelAndView mav = getModelAndView("browse", req);
        return mav;
    }

    // @RequestMapping(value = "/about", method = RequestMethod.GET)
    // public ModelAndView About()
    // {
    //     ModelAndView mav = getModelAndView("about");
    //     return mav;
    // }

    @RequestMapping(value = "/productdescription/{itemId}", method = RequestMethod.GET)
    public ModelAndView ProductDescription(@PathVariable final String itemId,
                                           HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("public/productdescription", req);

        Item item = itemAccessor.getItemDetached(Long.parseLong(itemId), IEnums.SEEK_MODE.ITEM_DESIGN_STORY);
        Item parentItem = itemAccessor.getItemParentItem(Long.parseLong(itemId), IEnums.SEEK_MODE.ITEM_DESIGN_STORY);
        item.setItemParent(parentItem);
        Item baseItem = itemAccessor.getItemBaseItem(Long.parseLong(itemId), IEnums.SEEK_MODE.ITEM_DESIGN_STORY);

        List<Material> materials = (List<Material>) printSupplier.getSupplierMaterials();

        materials = filterHardCodedMaterials(materials);

        List<Item> relatedItems = itemAccessor.getSisterItems(Long.parseLong(itemId), IEnums.SEEK_MODE.ITEM_FEED, 3l);
        //Make sure there aren't any missing renders for items that will be shown to the user
        for (Item relatedItem : relatedItems)
        {
            setDefaultItemImage(relatedItem, materials);
        }

        List<Item> allItems = new ArrayList<>();
        allItems.addAll(relatedItems);
        allItems.add(item);
        allItems.add(parentItem);
        allItems.add(baseItem);

        for (Item thisItem : allItems)
        {
            replaceItemDefaultMaterialWithHardCodedValue(thisItem);
        }

        List<LandingPageMaterialMap> materialImagePriceMap = new ArrayList<>();

        //Go through each material and finish and grab all of the finish images that map between the
        //finish and the image.  Then assign this combination a price.
        if (item.getDefaultCardImageUrl() == null || item.getDefaultCardImageUrl().length() == 0) {
            rval.addObject("stillRendering", true);
            setDefaultItemImage(item, materials);
        }
        else {
            for (Material material : materials) {
                for (Finish finish : material.getFinishList()) {
                    List<String> finishImages = new ArrayList<>();
                    String finishPrice = "";
                    for (ItemImage image : item.getImages()) {
                        if (image.getMaterial().equals(finish.getRealityServerMapping()))
                            finishImages.add(image.getLargeImageUrlPath());
                    }

                    materialImagePriceMap.add(new LandingPageMaterialMap(material, finish, finishImages, finishPrice));

                    //Set the default image map set
                    if (material.getMaterialId().equals(item.getMaterialId()) && finish.getFinishId().equals(item.getFinishId()))
                        rval.addObject("defaultMaterialIndex", materialImagePriceMap.size() - 1);
                }
            }
        }

        item.performCategoryConfigurations();

        rval.addObject("item", item);

        if (isAuthenticated()) {
            User authUser = getQuickUserDetailsPII();
            item.getItemOwner().setFollowed(userAccessor.checkUserFollow(authUser.getUserId(),
                    item.getItemOwner().getUserId()));

            rval.addObject("authUser", authUser);
        }

        setDefaultItemImage(parentItem, materials);

        if (item.getCategory().equals(SizeMapper.CATEGORY_NECKLACE))
            rval.addObject("availableChains", itemAccessor.getActiveChains());

        rval.addObject("itemParent", parentItem);
        rval.addObject("itemBase", baseItem);
        rval.addObject("relatedItems", relatedItems);
        rval.addObject("size", SizeMapper.getAvailableSizesMap(item.getCategory()));
        rval.addObject("materials", materials);
        rval.addObject("developmentServer", isDevelopmentServer());
        rval.addObject("isAuthenticated", isAuthenticated());
        rval.addObject("map", materialImagePriceMap);
        rval.addObject("landingUrl", env.getProperty("environment.baseurl") + "public/productdescription/" + itemId);
        rval.addObject("landingId", itemId);
        return rval;
    }

    @RequestMapping(value = "/ringsizes", method = RequestMethod.GET)
    public ModelAndView Ringsizes(HttpServletRequest req)
    {
        ModelAndView mav = getModelAndView("public/ringsize", req);
        return mav;
    }



    // FTUI ONBOARDING
    // Product Hunt Public Flow ---> phwelcome -> phchooseitem -> phcustomize --> phthankyou --> phfeed
    // OB - Product Hunt Welcome
    @RequestMapping(value = "/phwelcome", method = RequestMethod.GET)
    public ModelAndView PhWelcome(HttpServletRequest req) {
        ModelAndView mav = getModelAndView("redirect:/welcome", req);
        return mav;
    }
    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public ModelAndView PhWelcome2(HttpServletRequest req) {
        ModelAndView mav = getModelAndView("redirect:/welcome", req);
        return mav;
    }

    @RequestMapping(value = "/welcome/{referrerCode}", method = RequestMethod.GET)
    public ModelAndView welcomeReferral(@PathVariable final String referrerCode,
                                        HttpServletRequest req) {
        ModelAndView mav = getModelAndView("redirect:/welcome", req);
        return mav;
    }

    // OB - Product Hunt Choose Item
    @RequestMapping(value = "/phchooseitem", method = RequestMethod.GET)
    public ModelAndView PhChoose(HttpServletRequest req) {
        ModelAndView mav = getModelAndView("redirect:/welcome", req);
        return mav;
    }
     // OB Product Hunter
    @RequestMapping(value = "/phcustomize/{itemId}", method = RequestMethod.GET)
    public ModelAndView CustomizeV2(@PathVariable final String itemId,
                                    HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        return rval;
    }
    // OB - Product Hunt Thankyou
    @RequestMapping(value = "/phthankyou", method = RequestMethod.GET)
    public ModelAndView PhThankyou(HttpServletRequest req) {
        ModelAndView mav = getModelAndView("redirect:/welcome", req);
        return mav;
    }
    // OB - Product Hunt Preview of Feed
    @RequestMapping(value = "/phfeed", method = RequestMethod.GET)
    public ModelAndView PhFeed(HttpServletRequest req) {

        ModelAndView rval = getModelAndView("redirect:/welcome", req);
        return rval;
    }

    // OB - Product Hunt Preview of Feed
    @RequestMapping(value = "/phrings", method = RequestMethod.GET)
    public ModelAndView PhRings(HttpServletRequest req) {
        ModelAndView rval = getModelAndView(req);
        rval.setViewName("redirect:/welcome");
        return rval;
    }

    // OB - Product Hunt Preview of Feed
    @RequestMapping(value = "/phbracelets", method = RequestMethod.GET)
    public ModelAndView PhBracelets(HttpServletRequest req) {
        ModelAndView rval = getModelAndView(req);
        rval.setViewName("redirect:/welcome");
        return rval;
    }

        // Controller for the "/about" GET request URL. 
    @RequestMapping(value = "/phabout", method = RequestMethod.GET)
    public ModelAndView phAbout(HttpServletRequest req) {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);
        return rval;
    }

    @RequestMapping(value = "/end", method = RequestMethod.GET)
    public ModelAndView end(HttpServletRequest req) {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);
        return rval;
    }

    // Controller for the "/team" GET request URL. 
    @RequestMapping(value = "/phteam", method = RequestMethod.GET)
    public ModelAndView phTeam(HttpServletRequest req) {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);
        return rval;
    }

    // Controller for the "/press" GET request URL. 
    @RequestMapping(value = "/phpress", method = RequestMethod.GET)
    public ModelAndView phPress(HttpServletRequest req) {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);
        return rval;
    }

    // Controller for the "/mission" GET request URL. 
    @RequestMapping(value = "/phmission", method = RequestMethod.GET)
    public ModelAndView phMission(HttpServletRequest req) {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);
        return rval;
    }






    // FTUI ONBOARDING
    // NON Product Hunt Private Flow 
    // OB - Welcome
    @RequestMapping(value = "/obwelcome", method = RequestMethod.GET)
    public ModelAndView ObWelcome(HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);
        return rval;
    }
    // OB - Product Hunt Choose Item
    @RequestMapping(value = "/obchooseitem", method = RequestMethod.GET)
    public ModelAndView ObChoose(HttpServletRequest req) {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);
        return rval;
    }
     // OB Product Hunter
    @RequestMapping(value = "/obcustomize/{itemId}", method = RequestMethod.GET)
    public ModelAndView CustomizeV3(@PathVariable final String itemId,
                                    HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);
        return rval;
    }
    // OB - Product Hunt Thankyou
    @RequestMapping(value = "/obthankyou", method = RequestMethod.GET)
    public ModelAndView ObThankyou(HttpServletRequest req) {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);
        return rval;
    }

    @RequestMapping(value = "/thankyou", method = RequestMethod.GET)
    public ModelAndView thankyou(HttpServletRequest req) {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);
        return rval;
    }

    @RequestMapping(value = "/thankyou/{referralCode}", method = RequestMethod.GET)
    public ModelAndView thankYouReferral(@PathVariable final String referralCode,
                                         HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);
        return rval;
    }

    @RequestMapping(value = "/unsubscribe", method = RequestMethod.GET)
    public ModelAndView seeya(HttpServletRequest req) {
        ModelAndView mav = getModelAndView("public/seeya", req);
        mav.addObject("bodyClass", "onboard");
        mav.addObject("pageTitle", "Trove: See ya");
        mav.addObject("pageDescription", "Description");
        mav.addObject("developmentServer", isDevelopmentServer());
        return mav;
    }

    @RequestMapping(value = "/landing/{landingId}", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView landing(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                                @PathVariable final String landingId,
                                HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);
        return rval;
    }

    private void addCustomizerParameters(ModelAndView rval)
    {
        //Map<String, String> sizes = SizeMapper.getAvailableSizesMap(item.getCategory());
        //rval.addObject("size", gson.toJson(sizes));

        //List<Material> materials = (List<Material>) printSupplier.getSupplierMaterials();
        //rval.addObject("forgeMaterials", gson.toJson(ForgeMaterial.toForgeMaterial(materials)));
    }

    @RequestMapping(value = "/customize/demo-gem", method = RequestMethod.GET)
    public ModelAndView CustomizeDemoGL(HttpServletRequest req)
    {
        //TODO:  Make sure that private items are only shown to the specific user that should have access
        ModelAndView rval = getModelAndView("", req);
        rval.setViewName("redirect:/feed");
        try {
            //TODO:  This has a massive recursive read, if it becomes a speed problem, it needs to be fixed.
            Item item = itemAccessor.getItemDetached(20L, IEnums.SEEK_MODE.ITEM_CUSTOMIZER);

            List<CustomizerWeight> jsonWeights = new ArrayList<>();
            List<CustomizerOperator> operators = new ArrayList<>();

            if (item.getItemCustomizations() != null && item.getItemCustomizations().size() > 0)
            {
                jsonWeights = ItemCustomization.toCustomizerWeights(item.getItemCustomizations());
            }
            else {
                if (item.getItemAttributes() != null) {
                    for (ItemAttribute attribute : item.getItemAttributes()) {
                        if (attribute.getAttributeName().contains("modelWeight-")) {
                            jsonWeights.add(new CustomizerWeight(attribute.getAttributeName().split("-")[1], attribute.getAttributeValue()));
                        }
                    }
                }
            }

            operators = CustomizerOperator.toCustomizerOperators(jsonWeights);

            // begin the canvas specific page parameters
            Map<String, String> sizes = SizeMapper.getAvailableSizesMap(item.getCategory());
            rval.addObject("size", gson.toJson(sizes));

            List<Material> materials = (List<Material>) printSupplier.getSupplierMaterials();
            materials = filterHardCodedMaterials(materials);

            //TODO:  Cache this
            if (item.getCategory().equals(SizeMapper.CATEGORY_NECKLACE))
                rval.addObject("availableChains", gson.toJson(itemAccessor.getActiveChains()));

            if (isAuthenticated()) {
                User authUser = getQuickUserDetailsPII();

                rval.addObject("authUser", authUser);
            }

            replaceItemDefaultMaterialWithHardCodedValue(item);

            CustomizerHash customizerHash = new CustomizerHash(item.getVisibleMeshes(), operators, item.getFinishId());

            item.performCategoryConfigurations();

            item.setCustomizerFilename("demo-ring.json");

            if (isLocalDevelopmentServer()) {
                String CUSTOMIZER_PATH = "http://localhost:8080/resources/json/";
                rval.addObject("customizerPath", CUSTOMIZER_PATH);
                item.setCustomizerPath(CUSTOMIZER_PATH);
            } else {
                String CUSTOMIZER_PATH = "https://www.troveup.com/resources/json/";
                rval.addObject("customizerPath", CUSTOMIZER_PATH);
                item.setCustomizerPath(CUSTOMIZER_PATH);
            }


            rval.addObject("item", item);

            rval.addObject("forgeMaterials", gson.toJson(ForgeMaterial.toForgeMaterial(materials)));
            rval.addObject("customizerInput", gson.toJson(customizerHash));
            rval.addObject("materials", materials);
            rval.addObject("jsonWeights", gson.toJson(jsonWeights));
            rval.addObject("isAuthenticated", isAuthenticated());
            rval.addObject("developmentServer", isDevelopmentServer());
            rval.addObject("customizerHash", customizerHash);
            rval.setViewName("public/customize");

        } catch (Exception e)
        {
            logError(e);
        }

        return rval;
    }

    @RequestMapping(value = "/customize/webgl/{itemId}", method = RequestMethod.GET)
    public ModelAndView CustomizeGL(@PathVariable final String itemId,
                                    HttpServletRequest req)
    {
        //TODO:  Make sure that private items are only shown to the specific user that should have access
        ModelAndView rval = getModelAndView("", req);
        rval.setViewName("redirect:/feed");
        try {
            //TODO:  This has a massive recursive read, if it becomes a speed problem, it needs to be fixed.
            Item item = itemAccessor.getItemDetached(Long.parseLong(itemId), IEnums.SEEK_MODE.ITEM_CUSTOMIZER);

            List<CustomizerWeight> jsonWeights = new ArrayList<>();
            List<CustomizerOperator> operators = new ArrayList<>();

            if (item.getItemCustomizations() != null && item.getItemCustomizations().size() > 0)
            {
                jsonWeights = ItemCustomization.toCustomizerWeights(item.getItemCustomizations());
            }
            else {
                if (item.getItemAttributes() != null) {
                    for (ItemAttribute attribute : item.getItemAttributes()) {
                        if (attribute.getAttributeName().contains("modelWeight-")) {
                            jsonWeights.add(new CustomizerWeight(attribute.getAttributeName().split("-")[1], attribute.getAttributeValue()));
                        }
                    }
                }
            }

            operators = CustomizerOperator.toCustomizerOperators(jsonWeights);

            // begin the canvas specific page parameters
            Map<String, String> sizes = SizeMapper.getAvailableSizesMap(item.getCategory());
            rval.addObject("size", gson.toJson(sizes));

            List<Material> materials = (List<Material>) printSupplier.getSupplierMaterials();
            materials = filterHardCodedMaterials(materials);

            //TODO:  Cache this
            if (item.getCategory().equals(SizeMapper.CATEGORY_NECKLACE))
                rval.addObject("availableChains", gson.toJson(itemAccessor.getActiveChains()));

            if (isAuthenticated()) {
                User authUser = getQuickUserDetailsPII();

                rval.addObject("authUser", authUser);
            }

            replaceItemDefaultMaterialWithHardCodedValue(item);

            CustomizerHash customizerHash = new CustomizerHash(item.getVisibleMeshes(), operators, item.getFinishId());

            item.performCategoryConfigurations();

            rval.addObject("item", item);

            rval.addObject("forgeMaterials", gson.toJson(ForgeMaterial.toForgeMaterial(materials)));
            rval.addObject("customizerInput", gson.toJson(customizerHash));
            rval.addObject("materials", materials);
            rval.addObject("jsonWeights", gson.toJson(jsonWeights));
            rval.addObject("customizerPath", customizerPath);
            rval.addObject("isAuthenticated", isAuthenticated());
            rval.addObject("developmentServer", isDevelopmentServer());
            rval.addObject("customizerHash", customizerHash);
            rval.setViewName("public/customize");

        } catch (Exception e)
        {
            logError(e);
        }

        return rval;
    }


    @RequestMapping(value = "/cust/{itemId}", method = RequestMethod.GET)
    public ModelAndView Cust(@PathVariable final String itemId,
                                    HttpServletRequest request,
                             HttpServletRequest req)
    {
        //TODO:  Make sure that private items are only shown to the specific user that should have access
        ModelAndView rval = getModelAndView("redirect:/feed", req);
        try {
            Item item = itemAccessor.getItemDetached(Long.parseLong(itemId), IEnums.SEEK_MODE.ITEM_CUSTOMIZER);
            Item itemParent = itemAccessor.getItemParentItem(Long.parseLong(itemId), IEnums.SEEK_MODE.ITEM_DESIGN_STORY);
            Item itemGrandParent = itemAccessor.getItemGrandParentItem(Long.parseLong(itemId), IEnums.SEEK_MODE.ITEM_DESIGN_STORY);
            Item itemBase = itemAccessor.getItemBaseItem(Long.parseLong(itemId), IEnums.SEEK_MODE.ITEM_DESIGN_STORY);
            item.setItemParent(itemParent);
            item.setBaseItemReference(itemBase);
            List<CustomizerWeight> jsonWeights = new ArrayList<>();

            if (item.getItemCustomizations() != null && item.getItemCustomizations().size() > 0) {
                jsonWeights = ItemCustomization.toCustomizerWeights(item.getItemCustomizations());
            }
            else {
                if (item.getItemAttributes() != null) {
                    for (ItemAttribute attribute : item.getItemAttributes()) {
                        if (attribute.getAttributeName().contains("modelWeight-")) {
                            jsonWeights.add(new CustomizerWeight(attribute.getAttributeName().split("-")[1], attribute.getAttributeValue()));
                        }
                    }
                }
            }

            rval.addObject("size", SizeMapper.getAvailableSizesMap(item.getCategory()));
            if (item.getCategory().equals(SizeMapper.CATEGORY_NECKLACE)) {
                rval.addObject("availableChains", itemAccessor.getActiveChains());
            }

            List<Material> materials = (List<Material>) printSupplier.getSupplierMaterials();
            int foundItem = -1;
            for (int i = 0; i < item.getItemOwner().getOwnedItems().size(); ++i) {
                if (item.getItemOwner().getOwnedItems().get(i).getGroupedItem().getItemId().equals(Long.parseLong(itemId))) {
                    foundItem = i;
                }
            }

            if (foundItem > -1) {
                item.getItemOwner().getOwnedItems().remove(foundItem);
            }

            if (isAuthenticated()) {
                User authUser = getQuickUserDetailsPII();
                item.getItemOwner().setFollowed(userAccessor.checkUserFollow(authUser.getUserId(), item.getItemOwner().getUserId()));
                rval.addObject("authUser", authUser);
            }
            item.performCategoryConfigurations();
            rval.addObject("item", item);
            List<Item> relatedItems = itemAccessor.getSisterItems(Long.parseLong(itemId), IEnums.SEEK_MODE.ITEM_FEED, 3l);
            for (Item relatedItem : relatedItems) {
                setDefaultItemImage(relatedItem, materials);
            }
            setDefaultItemImage(itemParent, materials);
            setDefaultItemImage(itemGrandParent, materials);
            CustomizerInputFormat customizerInput = new CustomizerInputFormat(item.getVisibleMeshes(), jsonWeights);
            rval.addObject("customizerInput", gson.toJson(customizerInput));
            rval.addObject("itemGrandParent", itemGrandParent);
            rval.addObject("itemBase", itemBase);
            rval.addObject("relatedItems", relatedItems);
            rval.addObject("materials", materials);
            rval.addObject("jsonWeights", gson.toJson(jsonWeights));
            rval.addObject("customizerPath", customizerPath);
            rval.addObject("isAuthenticated", isAuthenticated());
            rval.addObject("developmentServer", isDevelopmentServer());
            rval.setViewName("public/cust");
        } catch (Exception e) {
            logError(e);
        }
        return rval;
    }


    @RequestMapping(value = "/customphone/{itemId}", method = RequestMethod.GET)
    public ModelAndView custphone(@PathVariable final String itemId,
                                  HttpServletRequest req)
    {
        //TODO:  Make sure that private items are only shown to the specific user that should have access
        ModelAndView rval = getModelAndView("redirect:/feed", req);
        try {
            //TODO:  This has a massive recursive read, if it becomes a speed problem, it needs to be fixed.
            Item item = itemAccessor.getItemDetached(Long.parseLong(itemId), IEnums.SEEK_MODE.ITEM_CUSTOMIZER);

            List<CustomizerWeight> jsonWeights = new ArrayList<>();

            if (item.getItemAttributes() != null) {
                for (ItemAttribute attribute : item.getItemAttributes()) {
                    if (attribute.getAttributeName().contains("modelWeight-")) {
                        jsonWeights.add(new CustomizerWeight(attribute.getAttributeName().split("-")[1], attribute.getAttributeValue()));
                    }
                }
            }

            if (item.getCategory().equals("RING"))
                rval.addObject("size", SizeMapper.getRingSizes());
            else if (item.getCategory().equals("BRACELET"))
                rval.addObject("size", SizeMapper.getBraceletSizes());

            List<Material> materials = (List<Material>) printSupplier.getSupplierMaterials();
            int foundItem = -1;

            for (int i = 0; i < item.getItemOwner().getOwnedItems().size(); ++i)
            {
                if (item.getItemOwner().getOwnedItems().get(i).getGroupedItem().getItemId().equals(Long.parseLong(itemId)))
                    foundItem = i;
            }

            if (foundItem > -1)
                item.getItemOwner().getOwnedItems().remove(foundItem);

            User authUser = getQuickUserDetailsPII();

            item.getItemOwner().setFollowed(userAccessor.checkUserFollow(authUser.getUserId(),
                    item.getItemOwner().getUserId()));


            rval.addObject("item", item);

            List<Item> relatedItems = itemAccessor.getSisterItems(Long.parseLong(itemId), IEnums.SEEK_MODE.ITEM_FEED, 3l);
            rval.addObject("relatedItems", relatedItems);
            rval.addObject("materials", materials);
            rval.addObject("jsonWeights", gson.toJson(jsonWeights));
            rval.addObject("customizerPath", customizerPath);
            rval.addObject("authUser", authUser);
            rval.addObject("developmentServer", isDevelopmentServer());
            rval.setViewName("customizephone");

        } catch (Exception e)
        {
            logError(e);
        } finally
        {
            return rval;
        }
    }

    private List<Material> filterHardCodedMaterials(List<Material> fullMaterialList)
    {
        String PLATED_MATERIAL = getPlatedMaterialId();
        List<String> acceptedFinishIds = getAcceptedFinishIds();
        //Hard coding new materials, as simply setting materials as "inactive" on the table breaks a lot of other stuff.
        //If we need to change this again, remove this kludge and update it so that we're using the tables again.
        List<Material> filteredMaterials = new ArrayList<>();
        for (Material material : fullMaterialList)
        {
            if (material.getMaterialId().equals(PLATED_MATERIAL))
            {
                filteredMaterials.add(material);
                List<Finish> filteredFinishes = new ArrayList<>();
                for (Finish finish : material.getFinishList())
                {
                    if (acceptedFinishIds.contains(finish.getFinishId()))
                    {
                        filteredFinishes.add(finish);
                    }
                }
                Collections.reverse(filteredFinishes);
                material.setFinishList(filteredFinishes);
            }
        }

        return filteredMaterials;
    }

    private Item replaceItemDefaultMaterialWithHardCodedValue(Item item)
    {
        if (!item.getMaterialId().equals(getPlatedMaterialId()) || !getAcceptedFinishIds().contains(item.getFinishId()))
        {
            item.setMaterialId(getPlatedMaterialId());
            item.setFinishId(FINISH_ID_18K_GOLD_PLATED);
        }

        return item;
    }

    private List<String> getAcceptedFinishIds() {
        List<String> acceptedFinishIds = new ArrayList<>();
        acceptedFinishIds.add(FINISH_ID_18K_GOLD_PLATED);
        acceptedFinishIds.add(FINISH_ID_14K_ROSE_GOLD_PLATED);
        acceptedFinishIds.add(FINISH_ID_RHODIUM_PLATED);

        return acceptedFinishIds;
    }

    private String getPlatedMaterialId() {
        return "3";
    }
}

