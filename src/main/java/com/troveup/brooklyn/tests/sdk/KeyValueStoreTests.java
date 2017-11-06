package com.troveup.brooklyn.tests.sdk;
import com.troveup.config.PersistenceConfig;
import com.troveup.config.SDKConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by tim on 6/22/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SDKConfig.class, PersistenceConfig.class})
public class KeyValueStoreTests
{
    /*@Autowired
    IKeyValueStore keyValueStore;

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp()
    {
        helper.setUp();
    }

    @After
    public void tearDown()
    {
        helper.tearDown();
    }

    @Test
    public void testPersistence()
    {
        keyValueStore.create("key", "value");

        Assert.assertTrue(keyValueStore.read("key").equals("value"));
    }*/
}
