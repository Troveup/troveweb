package com.troveup.brooklyn.controllers.trove;

import com.google.common.net.HttpHeaders;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.util.WorkerQueuer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tim on 9/25/15.
 */
@Controller
public class ErrorController extends CommonController
{
    @RequestMapping("/error/{errorCode}")
    public final ModelAndView errorCode(@PathVariable("errorCode") final Integer errorCode, HttpServletRequest req)
    {
        ModelAndView rval = getModelAndView("error/unknown", req);

        switch(errorCode)
        {
            case 404:
                rval.setViewName("error/404");
                break;
            default:
                break;
        }

        try {

            if (errorCode != 404 && !isLocalDevelopmentServer()) {
                User user = null;

                if (isAuthenticated()) {
                    user = getQuickUserDetailsPII();
                }

                String requestURI = null;
                if (req.getAttribute("javax.servlet.forward.request_uri") != null)
                    requestURI = (String) req.getAttribute("javax.servlet.forward.request_uri");

                Map<String, String> workerQueue = new HashMap<>();
                workerQueue.put("userId", user == null ? "anonymous" : user.getUserId().toString());
                workerQueue.put("pathToError", requestURI == null ? "not found" : requestURI);
                workerQueue.put("errorCode", errorCode.toString());
                workerQueue.put("otherDetails", "Referrer: " + req.getHeader(HttpHeaders.REFERER) + " Time Occurred: " + new Date()
                        + " Environment " + getSiteUrl());
                WorkerQueuer.queueWorkForWorker(workerQueue, WorkerController.ERROR_EMAIL_URL);
            }
        }
        catch (Exception e)
        {
            logError(e);
            logger.error("Tried to send exception to Trove Team, but something bad happened in the process.");
        }

        return rval;
    }
}
