package com.troveup.brooklyn.controllers.trove;

import com.troveup.brooklyn.orm.cart.model.GenericItem;
import com.troveup.brooklyn.orm.cart.model.PromoCode;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.simpleitem.model.OrderNote;
import com.troveup.brooklyn.orm.simpleitem.model.SimpleItem;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.util.MoneyUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by tim on 3/24/16.
 */
@Controller
@RequestMapping(value = "/dashboard", method = {RequestMethod.GET, RequestMethod.HEAD})
public class DashboardController extends CommonController
{
    @RequestMapping(value = "/influencer/{influencerId}", method = RequestMethod.GET)
    public ModelAndView influencerDashboard(@PathVariable final Long influencerId,
                                 HttpServletRequest req)
    {
        //TODO:  Make this formula dynamic for influencers, rather than hardcoded just for Jaleesa
        //BigDecimal FLAT_AMOUNT_EARNED_FROM_SALES = new BigDecimal(10.00);
        BigDecimal costOfJaleesaGood = new BigDecimal(28.00);
        BigDecimal percentOfSaleJaleesaKeeps = new BigDecimal(0.8);
        ModelAndView rval = getModelAndView("redirect:/feed", req);

        User authUser = getQuickUserDetailsPII();
        User user = userAccessor.getUser(influencerId, IEnums.SEEK_MODE.QUICK);

        if (authUser.getIsAdmin() || authUser.getUserId().equals(influencerId) && user.getInfluencer())
        {
            List <SimpleItem> orderedSimpleItems = simpleItemAccessor.getOrderedSimpleItemsByInfluencerId(influencerId, IEnums.SEEK_MODE.SIMPLEITEM_DASHBOARD_ITEM_ORDER);
            List<SimpleItem> referenceItems = simpleItemAccessor.getReferenceSimpleItemsByInfluencerId(influencerId, IEnums.SEEK_MODE.QUICK);

            for (SimpleItem referenceItem : referenceItems)
            {
                referenceItem.setMoneyEarnedFromThisItem("0.00");
                referenceItem.setNumberSold(0);
            }

            Integer totalItemsSold = 0;
            BigDecimal totalMoneyMade = BigDecimal.ZERO;

            if (orderedSimpleItems != null)
            {
                Map<Order, List<SimpleItem>> orderItemMapping = new HashMap<>();

                //Could be multiple SimpleItems associated with a given order, build a map
                for (SimpleItem item : orderedSimpleItems)
                {
                    if (!orderItemMapping.containsKey(item.getAssociatedOrder()))
                    {
                        List<SimpleItem> mapSimpleItems = new ArrayList<>();
                        mapSimpleItems.add(item);
                        orderItemMapping.put(item.getAssociatedOrder(), mapSimpleItems);
                    } else
                    {
                        orderItemMapping.get(item.getAssociatedOrder()).add(item);
                    }
                }

                for (Order order : orderItemMapping.keySet())
                {
                    BigDecimal promoCodeSum = BigDecimal.ZERO;

                    if (order.getAppliedPromoCodes() != null && order.getAppliedPromoCodes().size() > 0)
                    {
                        for (PromoCode code : order.getAppliedPromoCodes())
                        {
                            promoCodeSum = promoCodeSum.add(code.getPriceDelta());
                        }

                        promoCodeSum = promoCodeSum.divide(new BigDecimal(order.getGenericItemsList().size()), RoundingMode.UP);
                    }

                    for (SimpleItem item : orderItemMapping.get(order))
                    {
                        SimpleItem referenceItem = null;
                        for (SimpleItem potentialReferenceItem : referenceItems)
                        {
                            if (potentialReferenceItem.getSimpleItemId().equals(item.getParentItem().getSimpleItemId()))
                            {
                                referenceItem = potentialReferenceItem;
                            }
                        }

                        //Disregard offlined reference items
                        if (referenceItem != null) {
                            BigDecimal totalMoneyEarnedOnThisItemGroup = new BigDecimal(referenceItem.getMoneyEarnedFromThisItem());
                            BigDecimal moneyEarnedFromThisItem =
                                    item
                                            .getItemPrice()
                                            .subtract(promoCodeSum)
                                            .subtract(costOfJaleesaGood)
                                            .multiply(percentOfSaleJaleesaKeeps);

                            if (moneyEarnedFromThisItem.compareTo(BigDecimal.ZERO) == -1) {
                                moneyEarnedFromThisItem = BigDecimal.ZERO;
                            }

                            totalMoneyEarnedOnThisItemGroup = totalMoneyEarnedOnThisItemGroup.add(moneyEarnedFromThisItem);
                            referenceItem.setMoneyEarnedFromThisItem(MoneyUtil.toProperScale(totalMoneyEarnedOnThisItemGroup, null).toString());
                            totalItemsSold += 1;
                            referenceItem.setNumberSold(referenceItem.getNumberSold() + 1);
                            totalMoneyMade = totalMoneyMade.add(moneyEarnedFromThisItem);
                        }
                    }
                }

            }

            totalMoneyMade = MoneyUtil.toProperScale(totalMoneyMade, null);

            rval = getModelAndView("dashboard/influencer", req);
            rval.addObject("totalItemsSold", totalItemsSold);
            rval.addObject("totalMoneyMade", totalMoneyMade);
            rval.addObject("developmentServer", isDevelopmentServer());
            rval.addObject("isAuthenticated", isAuthenticated());
            rval.addObject("influencer", user);
            rval.addObject("authUser", getQuickUserDetailsPII());
            rval.addObject("simpleItems", referenceItems);

        }

        return rval;
    }

    @RequestMapping(value = "/influencer", method = RequestMethod.GET)
    public ModelAndView myInfluencerDashboard(HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("redirect:/feed", req);

        User user = getQuickUserDetailsPII();

        if (isAuthenticated())
        {
            rval.setViewName("redirect:/dashboard/influencer/" + user.getUserId());
        }

        return rval;
    }

    /*@RequestMapping(value = "/influencer/christendominique", method = RequestMethod.GET)
    public ModelAndView christenDominiqueDashboard(HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("redirect:/feed", req);

        User user = userAccessor.getUserByEmail(IndexController.CHRISTEN_DOMINIQUE_EMAIL, IEnums.SEEK_MODE.QUICK);

        if (isAuthenticated())
        {
            rval.setViewName("redirect:/dashboard/influencer/" + user.getUserId());
        }

        return rval;
    }*/

    @RequestMapping(value = "/influencer/jaleesamoses", method = RequestMethod.GET)
    public ModelAndView jaleesaMosesDashboard(HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("redirect:/feed", req);

        User user = userAccessor.getUserByEmail(IndexController.JALEESA_MOSES_EMAIL, IEnums.SEEK_MODE.QUICK);

        if (isAuthenticated())
        {
            rval.setViewName("redirect:/dashboard/influencer/" + user.getUserId());
        }

        return rval;
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView dominiqueDashboard(HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("redirect:/feed", req);

        User authUser = getQuickUserDetailsPII();

        if (isAuthenticated() && authUser != null && authUser.getInfluencer())
        {
            rval.setViewName("redirect:/dashboard/influencer/" + authUser.getUserId());
        }

        return rval;
    }

    @RequestMapping(value = "/manufacturer", method = RequestMethod.GET)
    public ModelAndView manufacturerDashboard(HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("redirect:/feed", req);

        User authUser = getQuickUserDetailsPII();

        if (checkIsAdminOrManufacturer(authUser))
        {
            rval.setViewName("dashboard/manufacturer");
            rval.addObject("isAuthenticated", isAuthenticated());
        }

        return rval;
    }

    @RequestMapping(value = "/manufacturer/notes/{simpleItemId}", method = RequestMethod.GET)
    public ModelAndView manufacturerNotes(@PathVariable final Long simpleItemId, HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("redirect:/feed", req);

        User authUser = getQuickUserDetailsPII();

        if (checkIsAdminOrManufacturer(authUser))
        {
            List<OrderNote> notes = simpleItemAccessor.getAllActiveNotesBySimpleItemId(simpleItemId, IEnums.SEEK_MODE.ORDERNOTE_USER);

            rval.addObject("notes", notes);
            rval.setViewName("dashboard/notes");
            rval.addObject("isAuthenticated", isAuthenticated());
        }

        return rval;
    }
}
