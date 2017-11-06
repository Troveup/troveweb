package com.troveup.brooklyn.controllers.trove;

import com.troveup.brooklyn.model.CustomizerInputFormat;
import com.troveup.brooklyn.model.CustomizerWeight;
import com.troveup.brooklyn.orm.cart.model.*;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.countries.model.Country;
import com.troveup.brooklyn.orm.countries.model.Subdivision;
import com.troveup.brooklyn.orm.item.model.ItemCustomization;
import com.troveup.brooklyn.orm.materials.model.Finish;
import com.troveup.brooklyn.orm.materials.model.Material;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.simpleitem.model.SimpleItem;
import com.troveup.brooklyn.orm.user.model.Collection;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.print.shapeways.api.ShapewaysPriceApi;
import com.troveup.brooklyn.util.DateUtils;
import com.troveup.brooklyn.util.MoneyUtil;
import com.troveup.brooklyn.util.SizeMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by tim on 3/26/15.
 */
@Controller
@RequestMapping(value = "/private", method = {RequestMethod.GET, RequestMethod.HEAD})
public class PrivateController extends CommonController
{

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ModelAndView userTrove(HttpServletRequest req) {
        ModelAndView mav = getModelAndView("user/profile", req);
        try {
            User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.USER_TROVE);

            mav.addObject("isAuthenticated", isAuthenticated());
            mav.addObject("itemCount", itemAccessor.getTrovedItemCount(user.getUserId(), true));
            mav.addObject("user", user);
            mav.addObject("authUser", getQuickUserDetailsPII());
            List<Material> materials = (List<Material>)printSupplier.getSupplierMaterials();
            mav.addObject("materials", materials);
            mav.addObject("availableChains", itemAccessor.getActiveChains());
            mav.addObject("collectionCount", userAccessor.getCollectionCount(user.getUserId(), true));
            mav.addObject("followedUserCount", userAccessor.getFollowedUserCount(user.getUserId()));
            mav.addObject("followersCount", userAccessor.getUsersWhoFollowedUserCount(user.getUserId()));
            mav.addObject("developmentServer", isDevelopmentServer());
        } catch (Exception e) {
            logError(e);
        }
        return mav;
    }

    @RequestMapping(value = "/bag", method = RequestMethod.GET)
    public String userBag(HttpServletRequest req)
    {
        return "redirect:/welcome";
        //ModelAndView mav = getModelAndView("user/bag", req);
        /*try {

            User user = null;

            if (isAuthenticated()) {
                user = getQuickUserDetailsPII(IEnums.SEEK_MODE.USER_CART_FULL);
            }
            else if (getPseudoUserFromSession(req) != null)
            {
                user = userAccessor.getUser(getPseudoUserFromSession(req), IEnums.SEEK_MODE.USER_CART_FULL);
            }
            else
            {
                user = new User();
            }

            //TODO:  Sort this before passing to view
            List<Country> countries = countryAccessor.getAllCountries(IEnums.SEEK_MODE.FULL);

            List<Material> materials = (List<Material>) printSupplier.getSupplierMaterials();
            mav.addObject("materials", materials);

            String braintreeClientToken = braintreeApi.getClientToken();

            Cart cart = user.getShoppingCart();

            if (cart != null)
            {
                promoCodeAccessor.removeAllPromoCodesFromCart(cart.getCartId());

                //Refresh the subtotal now that the cart items have been refreshed from their attributes
                cartAccessor.refreshGrandTotal(cart.getCartId(), false, false);

                cart = cartAccessor.getShoppingCart(cart.getCartId(), IEnums.SEEK_MODE.FULL);

                if (cart.getCartItems() != null) {
                    for (CartItem item : cart.getCartItems()) {

                        if (item.getMaterialName() == null || item.getMaterialName().length() == 0)
                            item.setMaterialName(Material.getMaterialNameByMaterialId(item.getMaterialId(), materials));

                        if (item.getFinishName() == null || item.getFinishName().length() == 0)
                            item.setFinishName(Finish.getFinishNameByFinishId(item.getFinishId(),
                                    Material.getMaterialByMaterialId(item.getMaterialId(), materials).getFinishList()));

                        if (item.getActualPrice() != null)
                            item.setActualPrice(MoneyUtil.toProperScale(item.getActualPrice(), null));
                        else
                            item.setActualPrice(MoneyUtil.toProperScale(new BigDecimal("0.00"), cart.getCurrencyUnit()));

                        if (item.getMaterialId().equals(ShapewaysPriceApi.getTryOnMaterialId()))
                            item.setCanPrototype(false);
                        else
                            item.setCanPrototype(true);

                        if (item.getSize(item.getCustomizationIteration()) != null)
                            item.setSize(SizeMapper.getStandardizedSize(item.getSize(item.getCustomizationIteration())));
                    }

                    //If a subtotal exists, populate it, otherwise in an emergency set it to zero.  Strip trailing zeros
                    //if necessary
                    if (cart.getEstimatedSubtotal() != null)
                        cart.setEstimatedSubtotal(MoneyUtil.toProperScale(cart.getEstimatedSubtotal(), null));
                    else {
                        cart.setEstimatedSubtotal(MoneyUtil.toProperScale(new BigDecimal("0.00"),
                                cart.getCurrencyUnit()));
                    }

                    if (cart.getSubTotal() != null)
                        cart.setSubTotal(MoneyUtil.toProperScale(cart.getSubTotal(), null));
                    else
                        cart.setEstimatedSubtotal(MoneyUtil.toProperScale(new BigDecimal("0.00"), cart.getCurrencyUnit()));

                    if (cart.getShipping() != null)
                        cart.setShipping(MoneyUtil.toProperScale(cart.getShipping(), null));
                    else
                        cart.setShipping(MoneyUtil.toProperScale(new BigDecimal("0.00"), cart.getCurrencyUnit()));

                    if (cart.getTax() != null)
                        cart.setTax(MoneyUtil.toProperScale(cart.getTax(), null));
                    else
                        cart.setTax(MoneyUtil.toProperScale(new BigDecimal("0.00"), null));
                }

                if (cart.getGenericItems() != null)
                {
                    for (GenericItem item : cart.getGenericItems())
                    {
                        item.setPrice(MoneyUtil.toProperScale(item.getPrice(), null));
                    }
                }
            }

            if (cart != null) {
                cart.setSubTotal(MoneyUtil.toProperScale(cart.getSubTotal(), null));
                cart.setGrandTotal(MoneyUtil.toProperScale(cart.getGrandTotal(), null));
            }

            mav.addObject("cart", cart);
            mav.addObject("braintreeClientToken", braintreeClientToken);

            //TODO:  Make this so that it's not US only (bug where subdivisions throw exception on view)

            List<Subdivision> sanitizedSubdivisions = new ArrayList<>();
            for (Subdivision subdivision : countries.get(0).getSubdivisions())
            {
                subdivision.setCode(subdivision.getCode().split("-")[1]);
                sanitizedSubdivisions.add(subdivision);
            }

            Collections.sort(sanitizedSubdivisions, new Comparator<Subdivision>() {
                @Override
                public int compare(Subdivision o1, Subdivision o2) {
                    return o1.getCode().compareTo(o2.getCode());
                }
            });

            //Always keep on top of the credit card expiration years so that we never have to worry about it
            //again
            List<Integer> creditCardExpirationYears = new ArrayList<>();
            creditCardExpirationYears.add(DateUtils.getCurrentYear());

            for (int i = 1; i <= 20; ++i)
            {
                creditCardExpirationYears.add(DateUtils.getCurrentYear() + i);
            }

            if (user.getStoreBalance() != null)
            {
                user.setStoreBalance(MoneyUtil.toProperScale(user.getStoreBalance(), null));
            }

            //For use with Facebook tracking
            if (cart != null && cart.getGenericItems() != null) {
                for (GenericItem item : cart.getGenericItems()) {
                    if (item.getItemType() == GenericItem.ITEM_TYPE.SIMPLE_ITEM) {
                        for (HookAttribute attribute : item.getHookAttributes()) {
                            if (attribute.getKey().equals(SimpleItem.HOOK_ATTRIBUTE_CONTROL_REFERENCE)) {
                                item.setItemReferenceId(Long.parseLong(attribute.getValue()));
                            }
                        }
                    }
                }
            }

            mav.addObject("isAuthenticated", isAuthenticated());
            mav.addObject("ccExpirationList", creditCardExpirationYears);
            mav.addObject("country", countries.get(0));
            mav.addObject("states", sanitizedSubdivisions);
            mav.addObject("user", user);
            mav.addObject("authUser", user);
            mav.addObject("developmentServer", isDevelopmentServer());


        } catch (Exception e)
        {
            logError(e);
        }*/

        //return mav;
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public ModelAndView Index(@PathVariable("userId") final Long userId,
                              HttpServletRequest req)
    {
        ModelAndView mav = getModelAndView("user/profile", req);

        User authUser = getQuickUserDetailsPII(IEnums.SEEK_MODE.USER_BAG_COUNT);

        if (authUser.getUserId().equals(userId))
            return getModelAndView("redirect:/private/user", req);

        User user = userAccessor.getUser(userId, IEnums.SEEK_MODE.QUICK);

        List<Collection> collections = user.getUserCollections();
        mav.addObject("isAuthenticated", isAuthenticated());
        mav.addObject("collections", collections);

        Boolean canFollowUser = !userAccessor.checkUserFollow(authUser.getUserId(), user.getUserId());
        mav.addObject("canFollowUser", canFollowUser);

        mav.addObject("itemCount", itemAccessor.getTrovedItemCount(user.getUserId(), false));
        mav.addObject("developmentServer", isDevelopmentServer());
        mav.addObject("user", user);
        mav.addObject("collectionCount", userAccessor.getCollectionCount(user.getUserId(), false));
        
        //Count of users that this user has followed
        mav.addObject("followedUserCount", userAccessor.getFollowedUserCount(userId));

        //Count of users who have have followed this user
        mav.addObject("followersCount", userAccessor.getUsersWhoFollowedUserCount(userId));

        mav.addObject("authUser", authUser);
        mav.addObject("availableChains", itemAccessor.getActiveChains());


        List<Material> materials = (List<Material>)printSupplier.getSupplierMaterials();
        mav.addObject("materials", materials);

        return mav;
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public ModelAndView Orders(HttpServletRequest req)
    {
        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.USER_ORDERS_QUICK);

        ModelAndView rval = getModelAndView("orders", req);
        rval.setViewName("user/orders");
        rval.addObject("user", user);
        rval.addObject("orderStatusTitleAndDescriptions", gson.toJson(CartItem.getOrdersPageStatusTitleAndDescriptionMap()));
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("developmentServer", isDevelopmentServer());
        rval.addObject("isAuthenticated", isAuthenticated());

        return rval;
    }

    @RequestMapping(value = "/customizesample/{cartItemId}")
    public ModelAndView customizeSample(@PathVariable("cartItemId") final Long cartItemId,
                                        HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("orders", req);

        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.USER_ORDERS_QUICK);

        if (user != null && orderAccessor.isUserOwnerOfCartItem(cartItemId, user.getUserId()))
        {
            CartItem item = cartAccessor.getCartItem(cartItemId, IEnums.SEEK_MODE.CART_ITEM_UPLOAD);

            item.setActualPrice(MoneyUtil.toProperScale(item.getActualPrice(), null));

            List<ItemCustomization> latestIterationCustomizations = item.getItemCustomizationByIteration(item.getCustomizationIteration());

            List<CustomizerWeight> customizerWeights =
                    ItemCustomization.
                            toCustomizerWeights(latestIterationCustomizations);

            List<String> visibleMeshes = ItemCustomization.toVisibleMeshes(latestIterationCustomizations);

            CustomizerInputFormat customizerInput = new CustomizerInputFormat(visibleMeshes, customizerWeights);

            if (item.getCustomizationIteration() < item.getMaxCustomizations())
                rval.addObject("canRequestPrototype", true);
            else
                rval.addObject("canRequestPrototype", false);

            rval.addObject("cartItem", item);
            rval.addObject("item", item.getCartItemReference());
            rval.addObject("customizerInput", gson.toJson(customizerInput));
            //Get user-displayable size
            rval.addObject("size", SizeMapper.
                    getStandardizedSize(SizeMapper.getExporterSize(item.getCartItemReference().getCategory(), item.getSize(item.getCustomizationIteration()))));
            rval.addObject("sizes", SizeMapper.getAvailableSizesMap(item.getCartItemReference().getCategory()));
            rval.addObject("authUser", user);
            rval.addObject("isAuthenticated", isAuthenticated());
            // rval.setViewName("customizesample");
            rval.addObject("developmentServer", isDevelopmentServer());
            // rval.setViewName("customize");
            rval.setViewName("public/prototypecustomizer");
        }

        return rval;
    }

    @RequestMapping(value = "/collection/{collectionId}", method = RequestMethod.GET)
    public ModelAndView collectionItemList(@PathVariable("collectionId") final Long collectionId,
                                           HttpServletRequest req) {
        ModelAndView rval = getModelAndView(req);
        User user = userAccessor.getUserByCollectionId(collectionId, IEnums.SEEK_MODE.QUICK);
        User authUser = getQuickUserDetailsPII();
        Boolean shouldIncludePrivateItems = true;
        Boolean isFollowCollection = true;

        if (user.getUserId().equals(authUser.getUserId()))
            shouldIncludePrivateItems = true;

        if (user.getUserId().equals(authUser.getUserId()))
            isFollowCollection = false;

        Collection collection = userAccessor.getCollectionByCollectionId(collectionId, IEnums.SEEK_MODE.QUICK);
        rval.addObject("isAuthenticated", isAuthenticated());
        rval.addObject("noFollowCollection", shouldIncludePrivateItems);
        rval.addObject("isFollowCollection", isFollowCollection);
        rval.addObject("authUser", authUser);
        rval.addObject("user", user);
        rval.addObject("collectionId", collectionId);
        rval.addObject("collection", collection);
        rval.addObject("collectionItemsCount", itemAccessor.getCollectionItemCount(collectionId, shouldIncludePrivateItems));
        rval.addObject("collectionFollowersCount", userAccessor.getCollectionFollowCount(collectionId));
        rval.addObject("pageTitle", "Collection Items");
        rval.addObject("developmentServer", isDevelopmentServer());
        rval.setViewName("public/collection");
        return rval;
    }
}
