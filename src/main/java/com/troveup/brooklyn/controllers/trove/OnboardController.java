package com.troveup.brooklyn.controllers.trove;

import com.troveup.brooklyn.model.CustomizerInputFormat;
import com.troveup.brooklyn.model.CustomizerWeight;
import com.troveup.brooklyn.model.ForgeMaterial;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.countries.model.Country;
import com.troveup.brooklyn.orm.countries.model.Subdivision;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.item.model.ItemAttribute;
import com.troveup.brooklyn.orm.item.model.ItemCustomization;
import com.troveup.brooklyn.orm.materials.model.Finish;
import com.troveup.brooklyn.orm.materials.model.Material;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.meshexporter.forge.model.CustomizerHash;
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
 * Created by tim on 2/16/16.
 */
@Controller
@RequestMapping(value = "/onboard", method = {RequestMethod.GET, RequestMethod.HEAD})
public class OnboardController extends CommonController
{

    private String customizerPath;

    @PostConstruct
    public void init()
    {
        customizerPath = env.getProperty("cloudstore.cloudstoreurl") + env.getProperty("cloudstore.jsonbucketname") +
                "/" + env.getProperty("cloudstore.customizerfragmenturl") +
                env.getProperty("cloudstore.customizerversionurlfragment");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView Index(HttpServletRequest req)
    {
        return new ModelAndView("redirect:/welcome");
    }

    @RequestMapping(value = "/welcome", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView stepOne(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                                HttpServletRequest req)
    {
        return new ModelAndView("redirect:/welcome");
    }

    @RequestMapping(value = "/choose", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView stepTwo(@RequestHeader(required = false, value="X-PJAX") String pjaxHeader,
                                HttpServletRequest req)
    {
        return new ModelAndView("redirect:/welcome");
    }

    @RequestMapping(value = "/customize/{itemId}", method = RequestMethod.GET)
    public ModelAndView FtuiCust(@PathVariable final String itemId,
                                 HttpServletRequest req)
    {
        return new ModelAndView("redirect:/welcome");
    }

    @RequestMapping(value = "/confirm", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView stepSignUp(HttpServletRequest req)
    {
        return new ModelAndView("redirect:/welcome");
    }

    private String getSessionVariable(String variableKey, HttpServletRequest req)
    {
        return req.getSession().getAttribute(variableKey) != null &&
                ((String) req.getSession().getAttribute(variableKey)).length() > 0 ? ((String) req.getSession().getAttribute(variableKey)) : null;
    }

}
