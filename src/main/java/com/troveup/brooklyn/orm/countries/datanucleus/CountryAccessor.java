package com.troveup.brooklyn.orm.countries.datanucleus;

import com.troveup.brooklyn.orm.common.datanucleus.ObjectAccessor;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.countries.interfaces.ICountryAccessor;
import com.troveup.brooklyn.orm.countries.model.Country;
import com.troveup.brooklyn.orm.countries.model.Subdivision;
import com.troveup.brooklyn.orm.user.model.User;

import javax.jdo.*;
import java.util.List;

/**
 * Created by tim on 5/12/15.
 */
public class CountryAccessor extends ObjectAccessor implements ICountryAccessor
{
    /**
     * Default constructor that manages injection of the PersistenceManagerFactory
     *
     * @param pmfProxy Injected proxied PersistenceManagerFactory
     */
    public CountryAccessor(PersistenceManagerFactory pmfProxy)
    {
        super(pmfProxy);
    }

    @Override
    public Boolean persistCountry(Country country)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;
        try{
            if (country == null) {
                throw new NullPointerException("Country cannot be null!");
            }

            pm.makePersistent(country);

            rval = true;
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean removeCountry(Long countryId)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (countryId == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            Query query = pm.newQuery(User.class, "primaryKeyId == :id");
            query.setResultClass(User.class);
            Long numberRemoved = query.deletePersistentAll(countryId.toString());

            if (numberRemoved > 1)
                rval = true;
            else if (numberRemoved == 0)
                rval = false;
            else
                throw new Exception("More than one Country was removed by delete operation!");
        }
        catch(Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean addSubdivision(Long countryId, Subdivision subdivision) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (countryId == null || subdivision == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            Country country = getCountryAttached(countryId, pm);
            rval = country.getSubdivisions().add(subdivision);

        }
        catch(Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean removeSubdivision(Long countryId, Long subdivisionId) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (countryId == null || subdivisionId == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            Country country = getCountryAttached(countryId, pm);

            if (country != null)
            {
                Subdivision subdivisionToRemove = new Subdivision();
                subdivisionToRemove.setPrimaryKeyId(subdivisionId);

                //Have to look up the index due to a seeming bug in Datanucleus.  Direct removal works in-memory, then
                //doesn't persist.
                int subdivisionToRemoveIndex = country.getSubdivisions().indexOf(subdivisionToRemove);

                if (subdivisionToRemoveIndex > -1)
                    rval = country.getSubdivisions().remove(subdivisionToRemoveIndex) != null;

            }
        }
        catch(Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateCountry(Country country)
    {
        Boolean rval = false;
        PersistenceManager pm = pmf.getPersistenceManager();

        try
        {
            if (country == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            Country persistedCountry = getCountryAttached(country.getPrimaryKeyId(), pm);

            if(persistedCountry != null)
            {
                if (country.getName() != null)
                    persistedCountry.setName(country.getName());
                if (country.getIsoAlpha2Code() != null)
                    persistedCountry.setIsoAlpha2Code(country.getIsoAlpha2Code());
                if (country.getIsoAlpha3Code() != null)
                    persistedCountry.setIsoAlpha3Code(country.getIsoAlpha3Code());

                persistedCountry.setZipRequired(country.isZipRequired());

                rval = true;
            }
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean updateSubdivision(Subdivision subdivision)
    {
        Boolean rval = false;
        PersistenceManager pm = pmf.getPersistenceManager();

        try
        {
            if (subdivision == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            Subdivision persistedSubdivision = getSubdivisionAttached(subdivision.getPrimaryKeyId(), pm);

            if (subdivision.getCode() != null)
                persistedSubdivision.setCode(subdivision.getCode());
            if (subdivision.getName() != null)
                persistedSubdivision.setName(subdivision.getName());
            if (subdivision.getCategory() != null)
                persistedSubdivision.setCategory(subdivision.getCategory());

            rval = true;
        } catch (Exception e)
        {
            logError(e);
        }

        return rval;
    }

    @Override
    public Country getCountry(Long countryId, IEnums.SEEK_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Country rval = null;

        try
        {
            if (countryId == null || mode == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            if (mode == IEnums.SEEK_MODE.FULL)
            {
                //DataNucleus will only pull surface level data unless explicitly instructed to do so.  If we need to pull
                //the whole country, we need to add extra fetch groups for the collections.
                //Reference:  http://www.datanucleus.org/products/datanucleus/jdo/fetchgroup.html
                if (mode != null && mode == IEnums.SEEK_MODE.FULL)
                {
                    FetchGroup fetchGroup = pm.getFetchGroup(Country.class, "fullFetch");
                    fetchGroup.addMember("subdivisions");

                    FetchPlan fetchPlan = pm.getFetchPlan();
                    fetchPlan.addGroup("fullFetch");
                    fetchPlan.setMaxFetchDepth(2);
                }
            }

            Query query = pm.newQuery(Country.class, "primaryKeyId == :countryId");
            query.setResultClass(Country.class);
            List<Country> queryResult = (List<Country>) query.execute(countryId);

            if (queryResult != null && queryResult.size() > 0)
                rval = queryResult.get(0);

            if (rval != null)
                rval = pm.detachCopy(rval);
        }
        catch(Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<Country> getCountry(List<String> searchByType, IEnums.COUNTRY_REQUEST_TYPE type, IEnums.SEEK_MODE mode) {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Country> rval = null;

        try
        {
            if (searchByType == null || type == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            //DataNucleus will only pull surface level data unless explicitly instructed to do so.  If we need to pull
            //the whole country, we need to add extra fetch groups for the collections.
            //Reference:  http://www.datanucleus.org/products/datanucleus/jdo/fetchgroup.html
            if (mode != null && mode == IEnums.SEEK_MODE.FULL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(Country.class, "fullFetch");
                fetchGroup.addMember("subdivisions");

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("fullFetch");
                fetchPlan.setMaxFetchDepth(2);
            }

            Query query = pm.newQuery(Country.class, ":p.contains(" + type.toString() + ")");
            query.setResultClass(Country.class);
            rval = (List<Country>) query.execute(searchByType);
        }
        catch(Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<Country> getAllCountries(IEnums.SEEK_MODE mode)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Country> rval = null;

        try
        {
            if (mode == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            //DataNucleus will only pull surface level data unless explicitly instructed to do so.  If we need to pull
            //the whole country, we need to add extra fetch groups for the collections.
            //Reference:  http://www.datanucleus.org/products/datanucleus/jdo/fetchgroup.html
            if (mode == IEnums.SEEK_MODE.FULL)
            {
                FetchGroup fetchGroup = pm.getFetchGroup(Country.class, "fullFetch");
                fetchGroup.addMember("subdivisions");

                FetchPlan fetchPlan = pm.getFetchPlan();
                fetchPlan.addGroup("fullFetch");
                fetchPlan.setMaxFetchDepth(2);
            }

            Query query = pm.newQuery(Country.class, "1 == 1");
            query.setResultClass(Country.class);
            rval = (List<Country>) query.execute();

        }
        catch(Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Subdivision getSubdivision(Long subdivisionId) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Subdivision rval = null;

        try
        {
            if (subdivisionId == null)
                throw new IllegalArgumentException("Parameters must have at least one element.");

            Query query = pm.newQuery(Subdivision.class, "primaryKeyId == :subdivisionId");
            query.setResultClass(Subdivision.class);
            List<Subdivision> queryResult = (List<Subdivision>) query.execute(subdivisionId);

            if (queryResult != null && queryResult.size() > 0)
                rval = queryResult.get(0);
        }
        catch(Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Country getCountryAttached(Long countryId, PersistenceManager pm)
    {
        Country rval = null;

        if (countryId == null || pm == null)
            throw new IllegalArgumentException("Parameters must have at least one element.");

        Query query = pm.newQuery(Country.class, "primaryKeyId == :countryId");
        query.setResultClass(Country.class);
        List<Country> queryResult = (List<Country>) query.execute(countryId);

        if (queryResult != null && queryResult.size() > 0)
            rval = queryResult.get(0);

        return rval;
    }

    @Override
    public Subdivision getSubdivisionAttached(Long subdivisionId, PersistenceManager pm)
    {
        Subdivision rval = null;

        if (subdivisionId == null || pm == null)
            throw new IllegalArgumentException("Parameters must have at least one element.");

        Query query = pm.newQuery(Subdivision.class, "primaryKeyId == :subdivisionId");
        query.setResultClass(Subdivision.class);
        List<Subdivision> queryResult = (List<Subdivision>) query.execute(subdivisionId);

        if (queryResult != null && queryResult.size() > 0)
            rval = queryResult.get(0);

        return rval;
    }
}
