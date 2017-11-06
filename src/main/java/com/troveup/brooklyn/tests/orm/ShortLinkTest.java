package com.troveup.brooklyn.tests.orm;

import com.troveup.brooklyn.orm.urlshortener.interfaces.IShortLinkAccessor;
import com.troveup.brooklyn.orm.urlshortener.model.ShortLink;
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
 * Created by tim on 2/4/16.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, SDKConfig.class})
public class ShortLinkTest
{
    @Autowired
    IShortLinkAccessor shortLinkAccessor;

    @Test
    public void generateShortLink()
    {
        ShortLink link = new ShortLink();

        link.setShortLinkFullUrl("/public/productdescription/50");

        Assert.assertNotNull(shortLinkAccessor.persistShortLink(link));
    }
}
