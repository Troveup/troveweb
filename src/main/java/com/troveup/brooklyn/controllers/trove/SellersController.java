package com.troveup.brooklyn.controllers.trove;

import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.storefront.model.StoreFront;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by tim on 6/10/16.
 */
@Controller
@RequestMapping(value = "/sellers", method = {RequestMethod.GET, RequestMethod.HEAD})
public class SellersController extends CommonController
{
    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView landingPage(HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("public/sellers", req);

        rval.addObject("isAuthenticated", isAuthenticated());
        rval.addObject("authUser", getQuickUserDetailsPII());
        rval.addObject("developmentServer", isDevelopmentServer());

        return rval;
    }

    @RequestMapping(value = "/{sellerStoreUrl}", method = RequestMethod.GET)
    public ModelAndView sellerStorefront(@PathVariable String sellerStoreUrl,
                                         HttpServletRequest req)
    {
        ModelAndView rval = new ModelAndView("redirect:/sellers/");

        StoreFront storeFront = storeFrontAccessor.getStoreFront(sellerStoreUrl.toLowerCase(), IEnums.SEEK_MODE.STORE_FRONT_VIEW_DATA);

        if (storeFront != null)
        {
            storeFront.setStoreDescription(storeFront.getStoreDescription().replaceAll("\\n", "<br>"));

            rval = getModelAndView("public/seller", req);
            rval.addObject("storeFront", storeFront);
            rval.addObject("isAuthenticated", isAuthenticated());
            rval.addObject("authUser", getQuickUserDetailsPII());
            rval.addObject("developmentServer", isDevelopmentServer());
        }

        return rval;
    }
}
