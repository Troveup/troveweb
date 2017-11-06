package com.troveup.brooklyn.spring.rdbms;

import com.troveup.brooklyn.orm.user.datanucleus.UserAccessor;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tim on 4/16/15.
 */
public class MySQLUsersConnectionRepository implements UsersConnectionRepository
{
    private ConnectionFactoryLocator locator;
    private TextEncryptor encryptor;
    private UserAccessor accessor;
    private ConnectionSignUp connectionSignUp;

    public MySQLUsersConnectionRepository(ConnectionFactoryLocator locator, TextEncryptor encryptor,
                                          UserAccessor accessor, ConnectionSignUp connectionSignUp)
    {
        this.locator = locator;
        this.encryptor = encryptor;
        this.accessor = accessor;
        this.connectionSignUp = connectionSignUp;
    }

    @Override
    public List<String> findUserIdsWithConnection(Connection<?> connection)
    {
        List<String> rval = new ArrayList<>();
        Set<String> userNames = accessor.getEmails(connection.getKey());

        if (userNames != null && userNames.size() > 0) {
            rval.addAll(userNames);
        }
        //Try to create a new user based on the connection
        else if (connectionSignUp != null)
        {
            String newUserId = connectionSignUp.execute(connection);
            rval.add(newUserId);
        }

        return rval;
    }

    @Override
    public Set<String> findUserIdsConnectedTo(String s, Set<String> set)
    {
        Set<String> rval = new HashSet<>();

        List<String> setAsList = new ArrayList<>();
        setAsList.addAll(set);
        Set<Long> userIds = accessor.getUserIds(s, setAsList);

        if (userIds != null && userIds.size() > 0) {
            for (Long userId : userIds) {
                String id = userId.toString();
                rval.add(id);
            }
        }

        return rval;
    }

    @Override
    public ConnectionRepository createConnectionRepository(String userId)
    {
        return new MySQLConnectionRepository(userId, locator, encryptor, accessor);
    }
}
