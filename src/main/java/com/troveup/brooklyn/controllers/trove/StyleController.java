package com.troveup.brooklyn.controllers.trove;

import com.revbingo.kss.KssParser;
import com.revbingo.kss.SectionContainer;
import com.revbingo.kss.StyleguideSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tim on 3/24/15.
 */
@Controller
@RequestMapping("/style")
public class StyleController
{
    @Autowired
    ServletContext servletContext;

    @RequestMapping(value = "/index", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView Index()
    {
        ModelAndView mav = new ModelAndView("styleindex");
        KssParser parser = null;
        File resourceDir = new File(servletContext.getRealPath("/WEB-INF/resources/stylesheets"));
        Map<String, StyleguideSection> styleguideSectionMap = null;
        ArrayList<SectionContainer> sectionContainer = new ArrayList<>();

        String resourcePath = resourceDir.getPath();


        try {
            parser = new KssParser(new File(resourcePath));
            styleguideSectionMap = parser.getStyleguideSections();

            for(String section : styleguideSectionMap.keySet())
            {
                StyleguideSection thisObject = styleguideSectionMap.get(section);
                sectionContainer.add(new SectionContainer(thisObject.getDescription(), thisObject.getSectionReference(), thisObject.getMarkup(), thisObject.getModifiers()));
            }

            mav.addObject("kss", sectionContainer);
        }
        catch (Exception e)
        {
            Logger.getLogger("com.troveup.brooklyn").logp(Level.SEVERE, "StyleController", "IndexAction", "Failed to populate sectionContainer with error: " + e.getMessage() + " and Stack Trace: " + e.getStackTrace().toString());
        }

        return mav;
    }
}
