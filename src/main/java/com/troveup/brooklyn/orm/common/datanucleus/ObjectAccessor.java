package com.troveup.brooklyn.orm.common.datanucleus;

import com.google.appengine.api.memcache.stdimpl.GCacheFactory;
import com.google.gson.Gson;
import com.troveup.brooklyn.sdk.cache.interfaces.ICacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by tim on 4/23/15.
 */
public abstract class ObjectAccessor
{
    protected Logger logger;

    protected PersistenceManagerFactory pmf;

    @Autowired
    protected ICacheService cacheService;

    @Autowired
    protected Gson gson;

    //Migrating this to the method level
    //protected PersistenceManager pm;

    public ObjectAccessor(PersistenceManagerFactory pmfProxy)
    {
        this.pmf = pmfProxy;

        //Migrating this to the method level
        //this.pm = pmfProxy.getPersistenceManager();
        logger = LoggerFactory.getLogger(this.getClass());
    }

    public void cacheItem(String cacheKey, String json, Integer hoursToCache)
    {
        Map properties = new HashMap<>();
        properties.put(GCacheFactory.EXPIRATION_DELTA, TimeUnit.HOURS.toSeconds(hoursToCache));
        cacheService.putCache(cacheKey, json, properties);
    }

    protected void logError(Exception e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        logger.error("Stack Trace: " + sw.toString());
    }

    protected Query constrainQueryByPage(Integer collectionPage, Long collectionPageSize, Query query)
    {
        Long beginning = collectionPage * collectionPageSize;
        Long end = (collectionPage + 1) * collectionPageSize;
        query.setRange(beginning, end);

        return query;
    }
}
