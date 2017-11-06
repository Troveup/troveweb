package com.troveup.brooklyn.controllers.trove;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by tim on 7/27/15.
 */
@Controller
@RequestMapping(value = "/events", method = {RequestMethod.GET, RequestMethod.HEAD})
public class EventsController
{
    @RequestMapping(value = "/tea", method = RequestMethod.GET)
    public String Ringsizes()
    {
        return "redirect:https://calendly.com/troveup/tea-with-trove";
    }
}
