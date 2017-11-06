package com.troveup.brooklyn.controllers.trove;

import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.item.model.ItemImage;
import com.troveup.brooklyn.orm.simpleitem.model.SimpleItem;
import com.troveup.brooklyn.orm.user.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by justin
 */
@Controller
@RequestMapping(value = "/featured", method = {RequestMethod.GET, RequestMethod.HEAD})
public class FeaturedController extends CommonController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView Index(HttpServletRequest req) {
        ModelAndView mav = getModelAndView("redirect:/jaleesamoses", req);
        return mav;
    }

    @RequestMapping(value = "/item/{itemId}", method = RequestMethod.GET)
    public ModelAndView featuredItem(@PathVariable final String itemId,
                                     HttpServletRequest req) {
        ModelAndView rval = getModelAndView("", req);
        rval.setViewName("redirect:/welcome");

        Long simpleItemId = Long.parseLong(itemId);

        SimpleItem item = simpleItemAccessor.getSimpleItemByItemId(simpleItemId, IEnums.SEEK_MODE.FULL);

        User influencer = userAccessor.getUser(item.getInfluencerUserAccountId(), IEnums.SEEK_MODE.QUICK);

        List<SimpleItem> relatedItems = simpleItemAccessor.getReferenceSimpleItemsByInfluencerId(item.getInfluencerUserAccountId(), IEnums.SEEK_MODE.ASSETS);

        if (item.getControlReference() && item.getActive())
        {

            for (SimpleItem relatedItem : relatedItems)
            {
                //Bug where item may not have any display images available
                if (relatedItem.getTroveDisplayImages() == null || relatedItem.getTroveDisplayImages().size() == 0)
                {
                    List<ItemImage> images = new java.util.ArrayList<>();
                    ItemImage image = new ItemImage();
                    image.setLargeImageUrlPath(relatedItem.getPrimaryDisplayImageUrl());
                    images.add(image);
                    relatedItem.setTroveDisplayImages(images);
                }
            }

            rval.addObject("itemAttributes", gson.toJson(item));
            rval.addObject("relatedItems", relatedItems);
            rval.addObject("influencer", influencer);
            rval.addObject("developmentServer", isDevelopmentServer());

            if (item.getStorefrontShortUrl() != null && item.getStorefrontShortUrl().length() > 0)
            {
                rval.addObject("storefrontUrl", item.getStorefrontShortUrl());
            }

            if (isAuthenticated()) {
                rval.addObject("authUser", getQuickUserDetailsPII());
                rval.addObject("isAuthenticated", isAuthenticated());
            }

            rval.setViewName("featured/featured-item");
        }

        return rval;
    }
}
