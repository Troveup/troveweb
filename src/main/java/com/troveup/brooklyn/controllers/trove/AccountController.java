package com.troveup.brooklyn.controllers.trove;

import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.user.interfaces.IUserAccessor;
import com.troveup.brooklyn.controllers.event.TroveEventPublisher;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.util.MoneyUtil;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * Created by tim on 4/20/15.
 */
@Controller
@RequestMapping(value = "/account", method = {RequestMethod.GET, RequestMethod.HEAD})
public class AccountController extends CommonController
{
    private final IUserAccessor userAccessor;
    private final TroveEventPublisher publisher;
    private final Environment env;
    private final BCryptPasswordEncoder encoder;

    @Inject
    public AccountController(TroveEventPublisher publisher, Environment env, IUserAccessor userAccessor,
                             BCryptPasswordEncoder encoder)
    {
        this.publisher = publisher;
        this.env = env;
        this.userAccessor = userAccessor;
        this.encoder = encoder;
    }



    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public ModelAndView accountsettings(HttpServletRequest req)
    {
        User user = getQuickUserDetailsPII(IEnums.SEEK_MODE.USER_SETTINGS);

        BigDecimal storeCredit = user.getStoreBalance();

        if (storeCredit == null)
            storeCredit = MoneyUtil.toProperScale(new BigDecimal(0.00), null);
        else
            storeCredit = MoneyUtil.toProperScale(storeCredit, null);

        ModelAndView mav = getModelAndView(null, req);
        mav.addObject("isAuthenticated", isAuthenticated());
        mav.addObject("authUser", user);
        mav.addObject("optIn", !user.getOptOut());
        mav.addObject("storeCredit", storeCredit);
        mav.setViewName("user/accountsettings");

        return mav;
    }
}
