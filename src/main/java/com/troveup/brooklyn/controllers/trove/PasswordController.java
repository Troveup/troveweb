package com.troveup.brooklyn.controllers.trove;

import com.troveup.brooklyn.controllers.forms.ForgotPasswordForm;
import com.troveup.brooklyn.controllers.forms.ForgotPasswordReplacementForm;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.user.model.PasswordResetToken;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.email.interfaces.IMailProvider;
import com.troveup.brooklyn.sdk.email.mandrill.api.MandrillMessageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by tim on 7/6/15.
 */
@Controller
@RequestMapping(value = "/password", method = {RequestMethod.GET, RequestMethod.HEAD})
public class PasswordController extends CommonController
{

    @Autowired
    private IMailProvider mailProvider;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(value = "/forgotpassword", method = RequestMethod.GET)
    public ModelAndView forgotPasswordGet(HttpServletRequest req)
    {

        ModelAndView mav = getModelAndView(req);
        mav.addObject("forgotPasswordForm", new ForgotPasswordForm());
        mav.setViewName("user/forgotpassword");

        return mav;
    }

    @RequestMapping(value = "/forgotsubmit", method = RequestMethod.POST)
    public String forgotPasswordPost(@Valid ForgotPasswordForm form, BindingResult formBinding, WebRequest request)
    {
        if (formBinding.hasErrors())
            return null;

        String submittedEmail = form.getEmail();

        User user = userAccessor.getUserByEmail(submittedEmail, IEnums.SEEK_MODE.USER_SOCIAL_CONNECTIONS);

        if (user != null)
        {
            String token = UUID.randomUUID().toString();

            userAccessor.createForgotPasswordLinkage(user.getUserId(), token);

            String forgotPasswordLink = env.getProperty("environment.baseurl") + "password/forgotreset/" + token;

            Map<String, String> toMap = new HashMap<>();
            toMap.put(user.getFirstName(), user.getEmail());

            Map<String, String> variableMap = new HashMap<>();
            variableMap.put("FIRST_NAME_HERE", user.getFirstName());
            variableMap.put("FORGOT_PASSWORD_LINK", forgotPasswordLink);
            AbstractMap.SimpleEntry<String, String> from = new AbstractMap.SimpleEntry<String, String>("Trove Team", "hello@troveup.com");
            MandrillMessageStatus[] status = (MandrillMessageStatus[]) mailProvider.sendTemplateEmail("Trove Password Reset", from, toMap, "password-request-template",
                    variableMap, false);

            if (!status[0].getStatus().equals("sent")) {
                logger.debug("Failed to send e-mail!  Status was " + status[0].getStatus());
                logger.debug("Rejection reason " + status[0].getRejectReason());
            }
        }

        return "forgotpasswordemail";
    }

    @RequestMapping(value = "/forgotreset/{id}", method = RequestMethod.GET)
    public ModelAndView forgotPasswordReset(@PathVariable final String id,
                                            HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("badpasswordlink", req);

        if (id != null)
        {
            PasswordResetToken token = userAccessor.getResetToken(id);
            if (token != null && token.isValid()) {
                ForgotPasswordReplacementForm form = new ForgotPasswordReplacementForm(id);
                rval = getModelAndView(req);
                rval.addObject("forgotPasswordReplacementForm", form);
                rval.setViewName("user/passwordreplacement");
            }
        }

        return rval;
    }

    @RequestMapping(value = "/passwordresetsubmit", method = RequestMethod.POST)
    public String forgotPasswordPost(@Valid ForgotPasswordReplacementForm form, BindingResult formBinding, WebRequest request)
    {
        if (formBinding.hasErrors())
            return null;

        PasswordResetToken token = userAccessor.getResetToken(form.getToken());

        if (token != null && token.isValid() == true)
        {
            if (userAccessor.updateUserPassword(token.getUser().getUserId(), encoder.encode(form.getPassword())))
            {
                userAccessor.invalidateResetToken(token.getToken());
                return "resetpasswordsuccess";
            }
            else
                return "resetpasswordproblem";
        }
        else
            return "resetpasswordbadtoken";
    }
}
