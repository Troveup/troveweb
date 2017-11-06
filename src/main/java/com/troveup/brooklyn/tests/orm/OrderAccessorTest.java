package com.troveup.brooklyn.tests.orm;

import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.countries.interfaces.ICountryAccessor;
import com.troveup.brooklyn.orm.countries.model.Country;
import com.troveup.brooklyn.orm.order.interfaces.IOrderAccessor;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.config.PersistenceConfig;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by tim on 5/17/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceConfig.class)
public class OrderAccessorTest
{
    //TODO:  Build tests out for orders accessor
    @Autowired
    IOrderAccessor accessor;

    @Before
    public void setUp()
    {

    }

    @Test
    public void testOrderNumberCollision()
    {
        Order order = new Order();
        order.setTroveOrderNumber("12345");

        accessor.persistOrder(order);

        Assert.assertTrue(accessor.checkIfOrderNumberExists("12345"));
    }

}
