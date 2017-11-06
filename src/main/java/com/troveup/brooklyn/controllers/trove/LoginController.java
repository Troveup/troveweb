package com.troveup.brooklyn.controllers.trove;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by tim on 4/14/15.
 */
@Controller
public class LoginController extends CommonController
{
    @RequestMapping(value = "/loginn", method = RequestMethod.GET)
    public String showLoginPage() {
        return "user/login";
    }
}
