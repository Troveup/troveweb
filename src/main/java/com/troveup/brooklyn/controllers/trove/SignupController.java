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

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.troveup.brooklyn.controllers.event.TroveEventPublisher;
import com.troveup.brooklyn.controllers.forms.SignupForm;
import com.troveup.brooklyn.model.UserAuthResponse;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.spring.social.model.UserDetails;
import com.troveup.brooklyn.util.StringUtils;
import com.troveup.brooklyn.util.WorkerQueuer;
import com.troveup.brooklyn.util.exception.UsernameAlreadyInUseException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class SignupController extends CommonController {

	private final TroveEventPublisher publisher;

	@Inject
	public SignupController(TroveEventPublisher publisher, UserDetailsService userDetailsService) {
		this.publisher = publisher;
	}

	@RequestMapping(value="/signup/{referrerCode}", method=RequestMethod.GET)
	public ModelAndView signupForm(@PathVariable final String referrerCode, HttpServletRequest req) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (referrerCode != null && referrerCode.length() > 0)
			req.getSession().setAttribute(REFERRER_SESSION_INDEX, referrerCode);

		if (!auth.getPrincipal().equals("anonymousUser"))
			return getModelAndView("redirect:/feed", req);

		ModelAndView mav = getModelAndView(req);
		mav.addObject("signupForm", new SignupForm());
		mav.addObject("developmentServer", isDevelopmentServer());
		mav.setViewName("signup");

		return mav;

	}

	@RequestMapping(value="/signup", method=RequestMethod.GET)
	public ModelAndView signupNonReferral(HttpServletRequest req) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!auth.getPrincipal().equals("anonymousUser"))
			return getModelAndView("redirect:/feed", req);

		ModelAndView mav = getModelAndView(req);
		mav.addObject("signupForm", new SignupForm());
		mav.addObject("developmentServer", isDevelopmentServer());
		mav.setViewName("signup");

		return mav;

	}

	@RequestMapping(value="/signup", method=RequestMethod.POST)
	public @ResponseBody
	UserAuthResponse signupAjax(@RequestParam("email") final String email,
								@RequestParam("name") final String name,
								@RequestParam(value = "username", required = false) final String username,
								@RequestParam("password") final String password,
								@RequestParam(value = "referrerCode", required = false) final String referrerCode,
								@RequestParam(value = "optIn", required = false) final Boolean optin,
								HttpServletRequest req)
	{
		UserAuthResponse rval = new UserAuthResponse(false, "Oops!  We had a problem registering you.  Please try again.");
		Boolean shouldGenerateUserFriendlyUsername = false;
		String usernameSetting;

		if (email.length() > 0 && name.length() > 0 && password.length() > 0) {
			if (username == null) {
				usernameSetting = StringUtils.generateRandomUppercaseString(6);
				shouldGenerateUserFriendlyUsername = true;
			} else {
				usernameSetting = username;
			}

			User user = createAccount(email, name, usernameSetting, password, referrerCode, optin);

			if (user != null) {

				//Set the referral
				if (req.getSession().getAttribute(REFERRER_SESSION_INDEX) != null && ((String) req.getSession().getAttribute(REFERRER_SESSION_INDEX)).length() > 0) {
					Map<String, String> workerQueue = new HashMap<>();
					workerQueue.put("referrerCode", ((String) req.getSession().getAttribute(REFERRER_SESSION_INDEX)));
					WorkerQueuer.queueWorkForWorker(workerQueue, WorkerController.PROCESS_REFERRAL_URL);
				}

				//Decision was made to reduce the number of fields on registration and auto-generate the username.
				//This shouldn't happen on the request thread, as it has the potential to be long-running.  Punt it
				//to a worker and place a temporary username in its place instead
				if (shouldGenerateUserFriendlyUsername) {
					Map<String, String> workerQueue = new HashMap<>();
					workerQueue.put("userId", user.getUserId().toString());
					WorkerQueuer.queueWorkForWorker(workerQueue, WorkerController.GENERATE_USERNAME_URL);
				}

				try {
					UserDetails userDetails = userDetailsService.loadUserByUsername(email);
					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,
							password, userDetails.getAuthorities());
					authenticationManager.authenticate(auth);

					if (auth.isAuthenticated()) {
						SecurityContextHolder.getContext().setAuthentication(auth);

						checkMergeAndClearPseudoUser(req, user.getUserId());

						//Details passed to the front end for mixpanel tracking
						UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
						User newUserDetails = populateUserObjectFromSecurityContextPrincipal(details);

						//Send a welcome e-mail
						String nameSplit[] = name.split(" ");

						String nameToUse;

						if (nameSplit.length > 1)
							nameToUse = nameSplit[0];
						else
							nameToUse = name;

						//Fire off the welcome e-mail
						Map<String, String> toMap = new HashMap<>();
						toMap.put(nameToUse, email);
						mailProvider.sendWelcomeEmail(nameToUse, toMap);

						rval.setIsSuccess(true);
						rval.setErrorMessage("");
						rval.setUser(newUserDetails);
					}

				} catch (Exception e) {
					logger.debug("Problem authenticating user on registration with username " + username, e);
				}
			} else {
				rval.setErrorMessage("Oops!  That e-mail is already registered with us.  Please try logging in instead.");
			}
		}
		else {
			rval.setErrorMessage("Oops!  Some required fields were missing.  Please make sure you've provided an e-mail, name, and password for registration.");
		}

		return rval;

	}

	private User createAccount(String email, String name, String username, String password, String referrerCode, Boolean optIn)
	{
		User rval = userAccessor.getUserByEmail(email, IEnums.SEEK_MODE.USER_SOCIAL_CONNECTIONS);

		if (rval == null) {

			try {
				rval = new User();
				rval.setRole(UserDetails.Role.ROLE_USER);
				rval.setFirstName(name);
				rval.setLastName("");
				rval.setEmail(email);
				rval.setUsername(username);
				rval.setPassword(encoder.encode(password));
				rval.setFullProfileImagePath("https://storage.googleapis.com/troveup-imagestore/assets/img/default-user-icon.jpg");
				rval.setProfileImageThumbnailPath("https://storage.googleapis.com/troveup-imagestore/assets/img/default-user-icon-thumb.jpg");
				rval.setCoverPhotoImagePath("https://storage.googleapis.com/troveup-imagestore/assets/default_cover_photo.png");
				rval.setHasUsedFtue(false);
				rval.setEnabled(true);
				rval.setIsAdmin(false);

				if (referrerCode != null && referrerCode.length() > 0)
					rval.setReferrerCode(referrerCode);

				//Check box not being checked is a null value.  Otherwise the user opted in, which means they're not opting out.
				//I'm so sorry.
				if (optIn != null && !optIn)
					rval.setOptOut(true);
				else
					rval.setOptOut(false);

				userAccessor.createUser(rval);
				rval.setUsername(username);

			} catch (UsernameAlreadyInUseException e)
			{
				rval = null;
			}
		}
		//Handle case where user was anonymously registered through a different process, like the FTUE
		else if (!rval.isEnabled() && rval.getAnonymousRegistration())
		{
			userAccessor.updateUserPassword(rval.getUserId(), encoder.encode(password));
			userAccessor.setUserEnabled(rval.getUserId());

			//May fail, gracefully, if the username already exists, so it'll default to whatever it was originally
			//set to
			userAccessor.updateUsername(rval.getUserId(), username);
		}
		else
		{
			rval = null;
		}

		return rval;
	}
}
