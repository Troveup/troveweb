package com.troveup.brooklyn.controllers.trove;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.item.model.Chain;
import com.troveup.brooklyn.orm.materials.model.Material;
import com.troveup.brooklyn.orm.simpleitem.model.SimpleItem;
import com.troveup.brooklyn.orm.urlshortener.model.ShortLink;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.util.SizeMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

// Main PJAX routing controller
@Controller
public class IndexController extends CommonController
{

    public static String CHRISTEN_DOMINIQUE_EMAIL = "info@christendominique.com";
    public static String JALEESA_MOSES_EMAIL = "jaleesa@ipsy.com";

    // ITEMS FEED AND CATEGORY PAGES 
    //
    // "/" 
    // GET request URL acts as root after authentication redirects ~> "/feed"
    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView Index(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                              HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        User user = null;

        if (isAuthenticated()) {
            user = getQuickUserDetailsPII();
        }

        if (user != null)
        {
            if (user.getHasUsedFtue())
            {
                rval.setViewName("redirect:/feed");
            }
            else
            {
                userAccessor.setUserCompletedFtui(user.getUserId(), true);
            }
        }

        return rval;
    }

    /*@RequestMapping(value = "/s/{shortLink}", method = RequestMethod.GET)
    public ModelAndView shortLinkRedirect(@PathVariable String shortLink,
                                          HttpServletRequest req)
    {
        ShortLink link = shortLinkAccessor.getShortLinkByTag(shortLink);

        ModelAndView rval = getModelAndView("redirect:/welcome", req);

        if (link != null && link.getActive())
            rval.setViewName("redirect:" + link.getShortLinkFullUrl());

        return rval;
    }*/

    @RequestMapping(value = "/welcome", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView welcomePage(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                                    HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("public/welcome", req);

        rval.addObject("isAuthenticated", isAuthenticated());
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("developmentServer", isDevelopmentServer());

        return rval;
    }

    //
    // "/feed" 
    // GET request URL
    @RequestMapping(value = "/feed", method = RequestMethod.GET)
    public ModelAndView feedController(
            @RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
            HttpServletRequest req)
    {
        ModelAndView rval = new ModelAndView("redirect:/welcome");

        return rval;
    }
    //
    // "/bracelets" 
    // GET request URL
    @RequestMapping(value = "/bracelets", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView bracelets(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                                  HttpServletRequest req) {
        ModelAndView rval = getModelAndView(req);
        List<Material> materials = (List<Material>)printSupplier.getSupplierMaterials();
        rval.addObject("pageTitle", "Bracelets");
        rval.addObject("loco", "productcategory_bracelets");
        rval.addObject("materials", materials);
        rval.addObject("isAuthenticated", isAuthenticated());
        rval.addObject("category", "BRACELET");
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.setViewName("public/bracelets");
        rval.addObject("developmentServer", isDevelopmentServer());
        return rval;
    }

    //
    // "/rings" 
    // GET request URL
    @RequestMapping(value = "/rings", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView rings(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                              HttpServletRequest req) {
        ModelAndView rval = getModelAndView(req);

        User user = getQuickUserDetailsPII();

        if (checkIsAdminOrManufacturer(user)) {

            List<Material> materials = (List<Material>) printSupplier.getSupplierMaterials();
            rval.addObject("pageTitle", "Rings");
            rval.addObject("materials", materials);
            rval.addObject("loco", "productcategory_rings");
            rval.addObject("isAuthenticated", isAuthenticated());
            rval.addObject("category", "RING");
            rval.addObject("authUser", getQuickUserDetailsPII());
            rval.setViewName("public/rings");
            rval.addObject("developmentServer", isDevelopmentServer());
        } else {
            rval = new ModelAndView("redirect:/welcome");
        }

        return rval;
    }

    /**
    * Controller for the "/bracelets" GET request URL.  
    * @param pjaxHeader The value of the request header "X-PJAX", passed by a PJAX request
    * @return ModelAndView object containing all necessary business logic data for view implementation
    */
    @RequestMapping(value = "/necklaces", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView necklaces(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                                  HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("public/necklaces", req);
        List<Material> materials = (List<Material>) printSupplier.getSupplierMaterials();
        List<Chain> chains = itemAccessor.getActiveChains();
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("pageTitle", "Necklaces");
        rval.addObject("isAuthenticated", isAuthenticated());
        rval.addObject("materials", materials);
        rval.addObject("availableChains", chains);
        rval.addObject("category", SizeMapper.CATEGORY_NECKLACE);
        rval.addObject("loco", "productcategory_necklaces");
        rval.addObject("developmentServer", isDevelopmentServer());

        return rval;
    }

    /*/**
    * Controller for the "/bracelets" GET request URL.  
    * @param pjaxHeader The value of the request header "X-PJAX", passed by a PJAX request
    * @return ModelAndView object containing all necessary business logic data for view implementation
    */
    /*@RequestMapping(value = "/featured", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView featured(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                                 HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView(req);
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("pageTitle", "Featured");
        rval.addObject("isAuthenticated", isAuthenticated());
        rval.addObject("category", "FEATURED");
        rval.addObject("loco", "featured");
        rval.addObject("developmentServer", isDevelopmentServer());
        if (pjaxHeader != null)
            rval.setViewName("public/featured");
        else {
            rval.setViewName("public/featured");
        }
        return rval;
    }

    //
    // "/tryon" 
    @RequestMapping(value = "/try", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView tryon(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                              HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("public/tryon", req);
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("pageTitle", "Try-on");
        rval.addObject("isAuthenticated", isAuthenticated());
        rval.addObject("developmentServer", isDevelopmentServer());
        return rval;
    }

    //
    // "/freering" 
    @RequestMapping(value = "/freering", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView freering(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                                 HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("redirect:/onboard", req);

        /** Rerouting the referral URL to /onboarding.  If the referral program is to be removed, this is safe to
         * be deleted.
         */
        /*ModelAndView rval = getModelAndView("public/freering");
        rval.addObject("pageTitle", "Invite Friends Earn Jewelry");

        if (isAuthenticated())
        {
            User user = getQuickUserDetailsPII();
            rval.addObject("authUser", user);
            rval.addObject("isAuthenticated", true);
            rval.addObject("numInvites", userAccessor.getReferralCountByUserId(user.getUserId()));
            rval.addObject("refLink", getSiteUrl() + "signup/" + userAccessor.getUserReferralCode(user.getUserId()));
        }
        else
        {
            rval.addObject("isAuthenticated", false);
        }

        rval.addObject("developmentServer", isDevelopmentServer());*/

        /*return rval;
    }

    // "/giftcards" 
    @RequestMapping(value = "/giftcard/buy", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView giftcards(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                                  HttpServletRequest req) {
        ModelAndView rval = getModelAndView("public/giftcards", req);
        rval.addObject("pageTitle", "Gift Cards");
        if (isAuthenticated()) {
            User user = getQuickUserDetailsPII();
            rval.addObject("authUser", user);
            rval.addObject("isAuthenticated", true);
        }
        else {
            rval.addObject("isAuthenticated", false);
        }
        rval.addObject("developmentServer", isDevelopmentServer());
        return rval;
    }

    // "/redeem" 
    @RequestMapping(value = "/giftcard", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView redeem(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                               HttpServletRequest req) {
        ModelAndView rval = getModelAndView("public/redeem", req);
        rval.addObject("pageTitle", "Gift Cards");
        if (isAuthenticated()) {
            User user = getQuickUserDetailsPII();
            rval.addObject("authUser", user);
            rval.addObject("isAuthenticated", true);
        }
        else {
            rval.addObject("isAuthenticated", false);
        }
        rval.addObject("developmentServer", isDevelopmentServer());
        return rval;
    }



    // LEGAL PAGES 
    //
    // "/privacy" 
    @RequestMapping(value = "/privacy", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView privacy(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                                HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("legal/privacy", req);
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("pageTitle", "Privacy Policy");
        rval.addObject("isAuthenticated", isAuthenticated());
        rval.addObject("developmentServer", isDevelopmentServer());
        return rval;
    }
    //
    // "/terms" 
    @RequestMapping(value = "/terms", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView terms(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                              HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("legal/terms", req);
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("pageTitle", "Terms and Conditions");
        rval.addObject("isAuthenticated", isAuthenticated());
        rval.addObject("developmentServer", isDevelopmentServer());
        return rval;
    }
    //
    // "/copyright" 
    @RequestMapping(value = "/copyright", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView copyright(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                                  HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("legal/copyright", req);
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("pageTitle", "Copyright Information");
        rval.addObject("isAuthenticated", isAuthenticated());
        rval.addObject("developmentServer", isDevelopmentServer());
        return rval;
    }
    //
    // "/sales" 
    @RequestMapping(value = "/sales", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView delivery(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                                 HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("legal/sales", req);
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("pageTitle", "Sales Policy");
        rval.addObject("isAuthenticated", isAuthenticated());
        rval.addObject("developmentServer", isDevelopmentServer());
        return rval;
    }






    // Controller for the "/privacy" GET request URL.  
    @RequestMapping(value = "/legal/privacy", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView lprivacy(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                                 HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView(req);
        rval.setViewName("public/troveprivacy");
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("developmentServer", isDevelopmentServer());
        return rval;
    }
    
    // Controller for the "/terms" GET request URL.  
    @RequestMapping(value = "/legal/terms", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView lterms(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                               HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView(req);
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.setViewName("public/troveterms");
        rval.addObject("developmentServer", isDevelopmentServer());
        return rval;
    }
    
    // Controller for the "/copyright" GET request URL.  
    @RequestMapping(value = "/legal/copyright", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView lcopyright(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                                   HttpServletRequest req) {
        ModelAndView rval = getModelAndView(req);
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.setViewName("public/trovecopyright");
        rval.addObject("developmentServer", isDevelopmentServer());
        return rval;
    }
    
    // Controller for the "/delivery" GET request URL.  
    @RequestMapping(value = "/legal/sales", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView lsales(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                               HttpServletRequest req) {
        ModelAndView rval = getModelAndView(req);
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.setViewName("public/trovedelivery");
        rval.addObject("developmentServer", isDevelopmentServer());
        return rval;
    }





    // ABOUT US VIEWS 

    // Controller for the "/about" GET request URL. 
    @RequestMapping(value = "/about", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView about(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                              HttpServletRequest req) {
        ModelAndView rval = getModelAndView(null, req);
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("pageTitle", "About Us");
        rval.addObject("isAuthenticated", isAuthenticated());
        rval.setViewName("about/about");
        rval.addObject("developmentServer", isDevelopmentServer());
        return rval;
    }

    // Controller for the "/team" GET request URL. 
    @RequestMapping(value = "/team", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView team(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                             HttpServletRequest req) {
        ModelAndView rval = getModelAndView(req);
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("pageTitle", "Our Team");
        rval.addObject("isAuthenticated", isAuthenticated());
        rval.setViewName("about/team");
        rval.addObject("developmentServer", isDevelopmentServer());
        return rval;
    }

    // Controller for the "/mission" GET request URL. 
    @RequestMapping(value = "/mission", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView mission(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                                HttpServletRequest req) {
        ModelAndView rval = getModelAndView(req);
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("pageTitle", "Our Mission");
        rval.addObject("isAuthenticated", isAuthenticated());
        rval.setViewName("about/mission");
        rval.addObject("developmentServer", isDevelopmentServer());
        return rval;
    }

    // Controller for the "/press" GET request URL. 
    @RequestMapping(value = "/press", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView press(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                              HttpServletRequest req) {
        ModelAndView rval = getModelAndView(req);
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("pageTitle", "Press");
        rval.addObject("isAuthenticated", isAuthenticated());
        rval.setViewName("about/press");
        rval.addObject("developmentServer", isDevelopmentServer());
        return rval;
    }

    // Controller for the "/partners" GET request URL. 
    @RequestMapping(value = "/partners", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView partners(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                                 HttpServletRequest req) {
        ModelAndView rval = getModelAndView(req);
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("pageTitle", "Partners");
        rval.addObject("isAuthenticated", isAuthenticated());
        rval.setViewName("about/partners");
        rval.addObject("developmentServer", isDevelopmentServer());
        return rval;
    }

    // Controller for the "/faq" GET request URL. 
    @RequestMapping(value = "/faq", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView faq(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                            HttpServletRequest req) {
        ModelAndView rval = getModelAndView(req);
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("pageTitle", "FAQ");
        rval.addObject("isAuthenticated", isAuthenticated());
        rval.setViewName("about/faq");
        rval.addObject("developmentServer", isDevelopmentServer());
        return rval;
    }






    // Controller for the "/legal" GET request URL.  
    @RequestMapping(value = "/legal", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView legal(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                              HttpServletRequest req) {
        ModelAndView rval = getModelAndView(req);
        rval.addObject("isAuthenticated", isAuthenticated());
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.setViewName("public/legal");
        rval.addObject("developmentServer", isDevelopmentServer());
        return rval;
    }

    @RequestMapping(value = "/eventconfirm", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView eventConfirm(HttpServletRequest req) {
        ModelAndView rval = getModelAndView("public/eventconfirm", req);

        rval.addObject("isAuthenticated", isAuthenticated());
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("developmentServer", isDevelopmentServer());

        return rval;
    }*/

    

    /*@RequestMapping(value = "/testurl", method = RequestMethod.GET)
    public ModelAndView testController(HttpServletRequest req)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user;

        if (name.contains("@"))
        {
            user = userAccessor.getUserByEmail(name, IEnums.SEEK_MODE.USER_CART_FULL);
        }
        else
        {
            user = userAccessor.getUserByUsername(name, IEnums.SEEK_MODE.USER_CART_FULL);
        }

        ModelAndView rval = getModelAndView("testview", req);
        rval.addObject("user", user);

        return rval;
    }*/

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminRedirect()
    {
        return "redirect:/admin/";
    }

    /*@RequestMapping(value = "/onboard", method = RequestMethod.GET)
    public String onboardRedirect()
    {
        return "redirect:/onboard/";
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboardRedirect()
    {
        return "redirect:/dashboard/";
    }

    @RequestMapping(value = "/style-guide", method = RequestMethod.GET)
    public ModelAndView styleGuide(HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("public/styleguide", req);

        rval.addObject("isAuthenticated", isAuthenticated());
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("developmentServer", isDevelopmentServer());

        return rval;
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public ModelAndView confirmPurchase(HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("public/purchaseconfirm", req);

        rval.addObject("isAuthenticated", isAuthenticated());
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("developmentServer", isDevelopmentServer());

        return rval;
    }

    @RequestMapping(value = "/blog", method = RequestMethod.GET)
    public ModelAndView blog(HttpServletRequest req)
    {

        //TODO:  Put tumblr blog link here
        ModelAndView rval = getModelAndView("redirect:https://troveup.tumblr.com", req);

        return rval;
    }*/

    /*@RequestMapping(value = "/christendominique", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView christenDominiquePage(HttpServletRequest req) {

        // return getModelAndView("redirect:/welcome", req);

        // TODO:  Uncomment when Christen goes live.
        User christenDominiqueUser = userAccessor.getUserByEmail(CHRISTEN_DOMINIQUE_EMAIL, IEnums.SEEK_MODE.QUICK);

        List<SimpleItem> items = simpleItemAccessor.getReferenceSimpleItemsByInfluencerId(christenDominiqueUser.getUserId(), IEnums.SEEK_MODE.FULL);

        for (SimpleItem item : items)
        {
            List<String> images = SimpleItem.retrieveColorImages(item);

            item.setItemMaterialImages(images);
        }

        ModelAndView rval = getModelAndView("featured/christendominique", req);
        rval.addObject("items", items);
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("isAuthenticated", isAuthenticated());
        rval.addObject("developmentServer", isDevelopmentServer());
        return rval;
    }*/

    @RequestMapping(value = "/jaleesamoses", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView jaleesaMosesPage(HttpServletRequest req) {

        User jaleesaMosesUser = userAccessor.getUserByEmail(JALEESA_MOSES_EMAIL, IEnums.SEEK_MODE.QUICK);

        List<SimpleItem> items = simpleItemAccessor.getReferenceSimpleItemsByInfluencerId(jaleesaMosesUser.getUserId(), IEnums.SEEK_MODE.QUICK);

        String[] itemIdsAccordingToLifeNumber = new String[34];

        for (int i = 0; i < 34; i++)
        {
            itemIdsAccordingToLifeNumber[i] = "";
        }

        for (int i = 1; i <= items.size(); i++) {
            if (i < 10) {
                itemIdsAccordingToLifeNumber[i] = items.get(i - 1).getSimpleItemId().toString();
            } else if (i == 10) {
                itemIdsAccordingToLifeNumber[11] = items.get(i - 1).getSimpleItemId().toString();
            } else if (i == 11) {
                itemIdsAccordingToLifeNumber[22] = items.get(i - 1).getSimpleItemId().toString();
            } else if (i == 12) {
                itemIdsAccordingToLifeNumber[33] = items.get(i - 1).getSimpleItemId().toString();
            }
        }

        ModelAndView rval = getModelAndView("featured/jaleesamoses", req);
        rval.addObject("influencer", jaleesaMosesUser);
        rval.addObject("items", items);
        rval.addObject("itemIdMapping", gson.toJson(itemIdsAccordingToLifeNumber));
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("isAuthenticated", isAuthenticated());
        rval.addObject("developmentServer", isDevelopmentServer());
        return rval;
    }

    @RequestMapping(value = "/demo", method = RequestMethod.GET)
    public ModelAndView demo2d(HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("public/2ddemo", req);

        return rval;
    }

    /*@RequestMapping(value = "/ppfgirl", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView kimberlySmithPage(HttpServletRequest req) {

        //TODO:  This is temporary, create logic for making Kimberly's account
        User jaleesaMosesUser = userAccessor.getUserByEmail(JALEESA_MOSES_EMAIL, IEnums.SEEK_MODE.QUICK);

        List<SimpleItem> items = simpleItemAccessor.getReferenceSimpleItemsByInfluencerId(jaleesaMosesUser.getUserId(), IEnums.SEEK_MODE.QUICK);

        ModelAndView rval = getModelAndView("featured/kimberlysmith", req);
        rval.addObject("influencer", jaleesaMosesUser);
        rval.addObject("items", items);
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("isAuthenticated", isAuthenticated());
        rval.addObject("developmentServer", isDevelopmentServer());

        return rval;
    }

    @RequestMapping(value = "/readytowear", method = RequestMethod.GET)
    public ModelAndView readyToWearRedirect(HttpServletRequest req) {
        return new ModelAndView("redirect:/readytowear/");
    }

    @RequestMapping(value = "/myprofile", method = RequestMethod.GET)
    public ModelAndView myProfile(HttpServletRequest req) {
        ModelAndView rval = getModelAndView("public/myprofile", req);
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("isAuthenticated", isAuthenticated());
        rval.addObject("developmentServer", isDevelopmentServer());
        return rval;
    }

    @RequestMapping(value = "/sellers", method = RequestMethod.GET)
    public ModelAndView sellers(HttpServletRequest req) {
        return new ModelAndView("redirect:/sellers/");
    }*/
}
