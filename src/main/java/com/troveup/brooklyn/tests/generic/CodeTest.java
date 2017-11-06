package com.troveup.brooklyn.tests.generic;

import com.troveup.brooklyn.util.DateUtils;
import com.troveup.config.PersistenceConfig;
import com.troveup.config.SDKConfig;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

/**
 * Created by tim on 4/18/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, SDKConfig.class})
public class CodeTest
{
    @Test
    public void testMultiValueMap()
    {
        List<String> stringList = new ArrayList<>();
        stringList.add("TestKey");
        stringList.add("TestKeyTwo");
        stringList.add("TestKeyThree");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        for (String string : stringList)
            map.put(string, Collections.<String>emptyList());

        map.get("TestKey").add("TestVal");
        map.get("TestKey").add("TestVal2");
        map.get("TestKey").add("TestVal3");

        map.get("TestKeyTwo").add("TestVal4");
        map.get("TestKeyTwo").add("TestVal5");
        map.get("TestKeyTwo").add("TestVal6");

        map.get("TestKeyThree").add("TestVal7");
        map.get("TestKeyThree").add("TestVal8");
        map.get("TestKeyThree").add("TestVal9");

    }

    @Test
    public void getYearTest()
    {
        Calendar calendar = new GregorianCalendar(DateUtils.getTroveTimezone());
        calendar.setTime(new Date());
        Integer year = calendar.get(Calendar.YEAR);

        Assert.assertTrue(year == 2015);
    }

    @Test
    public void stringMatchesTest()
    {
        String regexString = "^.*(?i){{x}}(?i).*$";
        String matchString = regexString.replace("{{x}}", "DEF");

        String firstTestString = "DEFinitely@gmail.com";
        String secondTestString = "Definitely@gmail.com";
        String thirdTestString = "fourthdefinedthing";
        String fourthTestString = "nothinghere";

        Assert.assertTrue(firstTestString.matches(matchString));
        Assert.assertTrue(secondTestString.matches(matchString));
        Assert.assertTrue(thirdTestString.matches(matchString));
        Assert.assertFalse(fourthTestString.matches(matchString));
    }
}
