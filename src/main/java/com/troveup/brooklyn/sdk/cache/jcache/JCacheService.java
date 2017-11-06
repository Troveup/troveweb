package com.troveup.brooklyn.sdk.cache.jcache;

import com.troveup.brooklyn.sdk.cache.interfaces.ICacheService;

import javax.cache.Cache;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by tim on 6/8/15.
 */
public class JCacheService extends JCacheCommon implements ICacheService
{
    @Override
    public Boolean putCache(String key, String value)
    {
        return this.putCache(key, value, Collections.emptyMap());
    }

    @Override
    public Boolean putCache(Map<String, String> keyValues)
    {
        return this.putCache(keyValues, Collections.emptyMap());
    }

    @Override
    public Boolean putCache(String key, String value, Map properties)
    {
        Boolean rval = false;

        try
        {
            if (key == null || value == null || properties == null)
                throw new NullPointerException("Can't add to cache if the key, value, or properties are null.");

            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            Cache cache = cacheFactory.createCache(properties);

            cache.put(key, value);

            rval = true;

        } catch (Exception e)
        {
            logError(e);
        }

        return rval;
    }

    @Override
    public Boolean putCache(Map<String, String> keyValues, Map properties)
    {
        Boolean rval = false;

        try
        {
            if (keyValues == null || properties == null)
                throw new NullPointerException("Can't add to cache if the key or value is null!");

            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            Cache cache = cacheFactory.createCache(properties);

            cache.putAll(keyValues);

            rval = true;

        } catch (Exception e)
        {
            logError(e);
        }

        return rval;

    }

    @Override
    public String getCacheValue(String key)
    {
        String rval = "";

        try
        {
            if (key == null)
                throw new NullPointerException("Can't get from cache if the key is null!");

            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            Cache cache = cacheFactory.createCache(Collections.emptyMap());

            rval = (String) cache.get(key);

        } catch (Exception e)
        {
            logError(e);
        }

        return rval;
    }

    /**
     * Doesn't seem to be a functional GAE implementation of JCache.  Always throws NotImplementedException.
     * @param keys
     * @return
     */
    @Override
    public Map getCacheValues(List<String> keys) throws Exception
    {
        throw new Exception("Unimplemented");
        /*Map rval = null;

        try
        {
            if (keys == null)
                throw new NullPointerException("Can't get from cache if the keys are null!");

            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            Cache cache = cacheFactory.createCache(Collections.emptyMap());

            rval = (Map) cache.get(keys);

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            return rval;
        }*/
    }


}
