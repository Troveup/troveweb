package com.troveup.brooklyn.tests.spring;

import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.user.interfaces.IUserAccessor;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.config.PersistenceConfig;
import com.troveup.config.SecurityConfig;
import com.troveup.config.SocialConfig;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by tim on 5/29/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class})
public class UserCredentialsTests {

    @Autowired
    IUserAccessor accessor;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    @Test
    public void testDecryptUser()
    {
        User user = accessor.getUserByEmail("tuser002@mailinator.com", IEnums.SEEK_MODE.FULL);

        Assert.assertTrue(encoder.matches("Gallup99", user.getPassword()));
    }
}
