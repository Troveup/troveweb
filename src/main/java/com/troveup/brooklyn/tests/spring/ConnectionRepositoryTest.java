package com.troveup.brooklyn.tests.spring;

import com.troveup.config.PersistenceConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by tim on 4/18/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceConfig.class)
public class ConnectionRepositoryTest
{
    /*@Autowired
    IUserAccessor accessor;

    @Autowired
    ConnectionFactoryLocator connectionFactoryLocator;

    @Autowired
    TextEncryptor textEncryptor;

    String userEmail;

    User user;

    @Before
    public void setUp()
    {
        user = new User();
        user.setFirstName("Tim");
        user.setLastName("Growney");
        user.setPassword("testing123");
        user.setRole(UserDetails.Role.ROLE_USER);

    }

    public String createNewUser()
    {
        String randomEmail = UUID.randomUUID().toString();
        user.setEmail(userEmail);
        user.setUsername(userEmail);

        accessor.createUser(user);

        List<String> getUserStructure = new ArrayList<>();
        getUserStructure.add(this.userEmail);

        User user = accessor.getUser(getUserStructure, User.SEARCH_BY_TYPE.EMAIL, IUserAccessor.SEEK_MODE.FULL).get(0);

        return user.getUserId().toString();
    }

    @Test
    public void findAllConnections()
    {
        String userId = createNewUser();

        ConnectionRepository repo = new MySQLConnectionRepository(userId, connectionFactoryLocator, textEncryptor);

        String providerUserId = UUID.randomUUID().toString();
        ConnectionData data = new ConnectionData("Facebook", providerUserId, "dood", "http://profileurl", "http://imageurl",
                "randomAccessToken", "shhh", "sofresh", new Long(12345));

        providerUserId = UUID.randomUUID().toString();
        ConnectionData dataOne = new ConnectionData("MySpace", providerUserId, "dood", "http://profileurl", "http://imageurl",
                "randomAccessToken", "shhh", "sofresh", new Long(12345));

        providerUserId = UUID.randomUUID().toString();
        ConnectionData dataTwo = new ConnectionData("MySpace", providerUserId, "dood", "http://profileurl", "http://imageurl",
                "randomAccessToken", "shhh", "sofresh", new Long(12345));


    }

    @Test
    public void findConnections()
    {

    }

    @Test
    public void findConnectionsByProviderId()
    {

    }

    @Test
    public void findConnectionsByClass()
    {

    }

    @Test
    public void findConnectionsToUsers()
    {

    }

    @Test
    public void getConnectionByConnectionKey()
    {

    }

    @Test
    public void getConnectionByClass()
    {

    }

    @Test
    public void getConnectionByClassProviderUserId()
    {

    }

    @Test
    public void getPrimaryConnection()
    {

    }

    @Test
    public void findPrimaryConnection()
    {

    }*/
}
