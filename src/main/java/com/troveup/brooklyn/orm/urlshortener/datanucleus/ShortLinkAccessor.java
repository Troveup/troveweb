package com.troveup.brooklyn.orm.urlshortener.datanucleus;

import com.troveup.brooklyn.orm.common.datanucleus.ObjectAccessor;
import com.troveup.brooklyn.orm.urlshortener.interfaces.IShortLinkAccessor;
import com.troveup.brooklyn.orm.urlshortener.model.ShortLink;
import com.troveup.brooklyn.util.StringUtils;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import java.util.List;

/**
 * Created by tim on 2/4/16.
 */
public class ShortLinkAccessor extends ObjectAccessor implements IShortLinkAccessor
{
    public ShortLinkAccessor(PersistenceManagerFactory pmfProxy) {
        super(pmfProxy);
    }

    @Override
    public ShortLink persistShortLink(ShortLink shortLink)
    {
        PersistenceManager pm = pmf.getPersistenceManager();

        try
        {
            if (shortLink == null)
                throw new NullPointerException("Argument cannot be null");

            if (shortLink.getShortLinkTag() == null)
            {
                shortLink.setShortLinkTag(generateUniqueTag());
            }

            pm.setDetachAllOnCommit(true);
            pm.makePersistent(shortLink);

        } catch (Exception e)
        {
            logError(e);
        } finally
        {
            pm.close();
        }

        return shortLink;
    }

    @Override
    public ShortLink getShortLinkById(Long id) {

        PersistenceManager pm = pmf.getPersistenceManager();
        ShortLink rval = null;

        try
        {
            if (id == null)
                throw new NullPointerException("Argument cannot be null");

            Query query = pm.newQuery(ShortLink.class, "shortLinkId == :id");

            List<ShortLink> queryResults = (List<ShortLink>) query.execute(id);

            if (queryResults != null && queryResults.size() > 0)
                rval = queryResults.get(0);

        } catch (Exception e)
        {
            logError(e);
        } finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public ShortLink getShortLinkByTag(String tag) {

        PersistenceManager pm = pmf.getPersistenceManager();
        ShortLink rval = null;

        try
        {
            if (tag == null)
                throw new NullPointerException("Argument cannot be null");

            Query query = pm.newQuery(ShortLink.class, "shortLinkTag == :tag");

            List<ShortLink> queryResults = (List<ShortLink>) query.execute(tag);

            if (queryResults != null && queryResults.size() > 0)
                rval = queryResults.get(0);


        } catch (Exception e)
        {
            logError(e);
        } finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public ShortLink getShortLinkAttached(Long id, PersistenceManager pm)
    {
        ShortLink rval = null;

        Query query = pm.newQuery(ShortLink.class, "shortLinkId == :id");
        List<ShortLink> queryResults = (List<ShortLink>) query.execute(id);

        if (queryResults != null && queryResults.size() > 0)
            rval = queryResults.get(0);

        return rval;
    }

    public Boolean isTagUnique(String tag)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = true;

        try {
            if (tag == null)
                throw new NullPointerException("Argument cannot be null");

            Query query = pm.newQuery(ShortLink.class, "shortLinkTag == :tag");
            query.setResult("count(this)");

            Long queryResult = (Long) query.execute(tag);

            rval = queryResult < 1;

        } catch (Exception e)
        {
            logError(e);
        } finally {
            pm.close();
        }

        return rval;
    }

    public String generateUniqueTag()
    {
        Integer TAG_LENGTH = 5;
        String potentialTag = StringUtils.generateRandomUppercaseString(TAG_LENGTH);

        while(!isTagUnique(potentialTag))
        {
            potentialTag = StringUtils.generateRandomUppercaseString(TAG_LENGTH);
        }

        return potentialTag;
    }
}
