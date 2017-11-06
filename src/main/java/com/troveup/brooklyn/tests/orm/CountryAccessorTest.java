package com.troveup.brooklyn.tests.orm;

import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.countries.interfaces.ICountryAccessor;
import com.troveup.brooklyn.orm.countries.model.Country;
import com.troveup.brooklyn.orm.countries.model.Subdivision;
import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.config.PersistenceConfig;
import com.troveup.config.SDKConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tim on 5/13/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, SDKConfig.class})
public class CountryAccessorTest
{
    //TODO:  Build out country accessor tests
    @Autowired
    ICountryAccessor accessor;

    @Before
    public void setUp()
    {
        List<Country> countries = accessor.getAllCountries(IEnums.SEEK_MODE.FULL);

        if (countries == null || countries.size() == 0 || countries.get(0).getSubdivisions().size() == 0);
            accessor.persistCountry(populateUnitedStates());


    }

    @Test
    public void emptyTest()
    {
        //empty test so that the countries will be populated in a one-time run for the ORM
    }

    public static Country populateUnitedStates()
    {
        Country country = new Country();
        country.setZipRequired(false);
        country.setIsoAlpha2Code("US");
        country.setIsoAlpha3Code("USA");
        country.setName("United States of America");
        country.setIsoNumericCode("840");

        List<Subdivision> unitedStatesSubdivisions = new ArrayList<>();
        Map<String, String> unitedStatesSubdivisionMap = getUnitedStatesSubdivisions();

        for (String key : unitedStatesSubdivisionMap.keySet())
        {
            Subdivision division = new Subdivision();
            division.setCategory("state");
            division.setName(unitedStatesSubdivisionMap.get(key));
            division.setCode(key);
            unitedStatesSubdivisions.add(division);
        }

        country.setSubdivisions(unitedStatesSubdivisions);

        return country;
    }

    public static Map<String, String> getUnitedStatesSubdivisions()
    {
        Map<String, String> rval = new HashMap<>();

        rval.put("US-AL", "Alabama");
        rval.put("US-AK", "Alaska");
        rval.put("US-AZ", "Arizona");
        rval.put("US-AR", "Arkansas");
        rval.put("US-CA", "California");
        rval.put("US-CO", "Colorado");
        rval.put("US-CT", "Connecticut");
        rval.put("US-DE", "Delaware");
        rval.put("US-FL", "Florida");
        rval.put("US-GA", "Georgia");
        rval.put("US-HI", "Hawaii");
        rval.put("US-ID", "Idaho");
        rval.put("US-IL", "Illinois");
        rval.put("US-IN", "Indiana");
        rval.put("US-IA", "Iowa");
        rval.put("US-KS", "Kansas");
        rval.put("US-KY", "Kentucky");
        rval.put("US-LA", "Louisiana");
        rval.put("US-ME", "Maine");
        rval.put("US-MD", "Maryland");
        rval.put("US-MA", "Massachusetts");
        rval.put("US-MI", "Michigan");
        rval.put("US-MN", "Minnesota");
        rval.put("US-MS", "Mississippi");
        rval.put("US-MO", "Missouri");
        rval.put("US-MT", "Montana");
        rval.put("US-NE", "Nebraska");
        rval.put("US-NV", "Nevada");
        rval.put("US-NH", "New Hampshire");
        rval.put("US-NJ", "New Jersey");
        rval.put("US-NM", "New Mexico");
        rval.put("US-NY", "New York");
        rval.put("US-NC", "North Carolina");
        rval.put("US-ND", "North Dakota");
        rval.put("US-OH", "Ohio");
        rval.put("US-OK", "Oklahoma");
        rval.put("US-OR", "Oregon");
        rval.put("US-PA", "Pennsylvania");
        rval.put("US-RI", "Rhode Island");
        rval.put("US-SC", "South Carolina");
        rval.put("US-SD", "South Dakota");
        rval.put("US-TN", "Tennessee");
        rval.put("US-TX", "Texas");
        rval.put("US-UT", "Utah");
        rval.put("US-VT", "Vermont");
        rval.put("US-VA", "Virginia");
        rval.put("US-WA", "Washington");
        rval.put("US-WV", "West Virginia");
        rval.put("US-WI", "Wisconsin");
        rval.put("US-WY", "Wyoming");
        rval.put("US-DC", "Dist. of Columbia");

        return rval;
    }

}
