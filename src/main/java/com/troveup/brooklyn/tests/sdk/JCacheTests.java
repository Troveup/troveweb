package com.troveup.brooklyn.tests.sdk;

import com.troveup.brooklyn.sdk.cache.jcache.JCacheService;
import com.troveup.config.PersistenceConfig;
import com.troveup.config.SDKConfig;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by tim on 6/8/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SDKConfig.class, PersistenceConfig.class})
public class JCacheTests
{
    @Autowired
    JCacheService cacheService;

    @Test
    public void testInsertRemoveSimpleCache()
    {
        Assert.assertTrue(cacheService.putCache("TestKey", "TestVal"));
        Assert.assertTrue(cacheService.getCacheValue("TestKey").equals("TestVal"));
    }
}
