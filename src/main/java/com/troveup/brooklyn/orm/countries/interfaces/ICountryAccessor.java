package com.troveup.brooklyn.orm.countries.interfaces;

import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.countries.model.Country;
import com.troveup.brooklyn.orm.countries.model.Subdivision;

import javax.jdo.PersistenceManager;
import java.util.List;

/**
 * Created by tim on 5/12/15.
 */
public interface ICountryAccessor
{
    Boolean persistCountry(Country country);
    Boolean removeCountry(Long countryId);
    Boolean addSubdivision(Long countryId, Subdivision subdivision);
    Boolean removeSubdivision(Long countryId, Long subdivisionId);
    Boolean updateCountry(Country country);
    Boolean updateSubdivision(Subdivision subdivision);
    Country getCountry(Long countryId, IEnums.SEEK_MODE mode);
    List<Country> getCountry(List<String> searchByType, IEnums.COUNTRY_REQUEST_TYPE type, IEnums.SEEK_MODE mode);
    List<Country> getAllCountries(IEnums.SEEK_MODE mode);
    Country getCountryAttached(Long countryId, PersistenceManager pm);
    Subdivision getSubdivisionAttached(Long subdivisionId, PersistenceManager pm);
    Subdivision getSubdivision(Long subdivisionId);
}
