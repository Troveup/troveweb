package com.revbingo.kss;

import java.util.ArrayList;

/**
 * Created by tim on 3/24/15.
 */
public class SectionContainer
{
    private String sectionDescription;
    private String sectionReference;
    private String sectionMarkup;
    private ArrayList<Modifier> sectionModifiers;

    public SectionContainer(String sectionDescription, String sectionReference, String sectionMarkup,ArrayList<Modifier> sectionModifiers)
    {
        //Initialize all the variables
        this.sectionDescription = sectionDescription;
        this.sectionReference = sectionReference;
        this.sectionModifiers = sectionModifiers;
        this.sectionMarkup = sectionMarkup;

        //Parse out and add section markup, if we can
        if (!this.sectionMarkup.isEmpty() && this.sectionMarkup != null)
        {
            for (Modifier modifier : sectionModifiers)
            {
                String replaceClass = "";

                //Replace any : or . that is associated with the css class comment
                /*if (replaceClass.contains(":"))
                {
                    //Colons represent a pseudo-class in CSS
                    replaceClass = replaceClass.replace(":", "pseudo-class-");
                }
                else
                {
                    //Periods represent a subclass, not necessary in the markup
                    replaceClass = replaceClass.replace(".", "");
                }*/

                //TODO:  Fix psuedo classes so that they look like an actual state
                if (modifier.name != null && !modifier.name.isEmpty()) {
                    replaceClass = modifier.name.replaceAll("[:.]", "");
                }

                //Create the markup
                modifier.setMarkup(this.sectionMarkup.replace("@modifierClass@", replaceClass));
            }
        }
    }

    public String getSectionDescription()
    {
        return sectionDescription;
    }

    public String getSectionReference()
    {
        return sectionReference;
    }

    public ArrayList<Modifier> getSectionModifiers()
    {
        return sectionModifiers;
    }

    public String getSectionMarkup()
    {
        return sectionMarkup;
    }

    public void setSectionDescription(String sectionDescription)
    {
        this.sectionDescription = sectionDescription;
    }

    public void setSectionReference(String sectionReference)
    {
        this.sectionReference = sectionReference;
    }

    public void setSectionModifiers(ArrayList<Modifier> sectionModifiers)
    {
        this.sectionModifiers = sectionModifiers;
    }

    public void setSectionMarkup(String sectionMarkup)
    {
        this.sectionMarkup = sectionMarkup;
    }
}
