/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.troveup.brooklyn.controllers.trove;

import com.troveup.brooklyn.model.UserAuthResponse;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.spring.social.model.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SigninController extends CommonController {

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public ModelAndView signin(HttpServletRequest req) {
        //System.out.println("signin");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal().equals("anonymousUser")) {
            ModelAndView mav = getModelAndView(req);
            mav.setViewName("user/signin");
            mav.addObject("developmentServer", isDevelopmentServer());
            return mav;
        } else {
            return getModelAndView("redirect:/feed", req);
        }
    }

    @RequestMapping(value = "/signinasync", method = RequestMethod.POST)
    public
    @ResponseBody
    UserAuthResponse signInAsync(@RequestParam(value = "usernameoremail") final String usernameOrEmail,
                                 @RequestParam(value = "password") final String password,
                                 HttpServletRequest req) {


        UserAuthResponse rval = new UserAuthResponse(false, "Oops!  Something went wrong, please try again.");

        try {

            UserDetails userDetails = userDetailsService.loadUserByUsername(usernameOrEmail);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,
                    password, userDetails.getAuthorities());
            authenticationManager.authenticate(auth);

            if (auth.isAuthenticated()) {

                SecurityContextHolder.getContext().setAuthentication(auth);

                //Details passed to the front end for mixpanel tracking
                UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                User user = populateUserObjectFromSecurityContextPrincipal(details);

                checkMergeAndClearPseudoUser(req, user.getUserId());

                rval.setUser(user);
                rval.setErrorMessage("");
                rval.setIsSuccess(true);
            } else {
                rval.setErrorMessage("Your username/password combination were incorrect.  Please try again.");
            }
        } catch (Exception e) {
            rval.setErrorMessage("Your username/password combination were incorrect.  Please try again.");
        }

        return rval;
    }
}
