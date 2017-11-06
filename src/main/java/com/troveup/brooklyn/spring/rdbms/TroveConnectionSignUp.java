package com.troveup.brooklyn.spring.rdbms;

import com.troveup.brooklyn.orm.user.datanucleus.UserAccessor;
import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.user.model.SocialConnection;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.sdk.email.interfaces.IMailProvider;
import com.troveup.brooklyn.spring.social.model.UserDetails;
import com.troveup.brooklyn.util.exception.UsernameAlreadyInUseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

import javax.inject.Inject;
import java.util.*;

/**
 * Created by tim on 4/19/15.
 */
public class TroveConnectionSignUp implements ConnectionSignUp
{
    @Inject
    UserAccessor accessor;

    @Inject
    BCryptPasswordEncoder encoder;

    @Autowired
    IMailProvider mailProvider;

    @Override
    public String execute(Connection<?> connection)
    {
        String rval = null;

        //Make the Trove account difficult to log into by default, as this is natively a social connection.
        String password = encoder.encode(UUID.randomUUID().toString());

        User user = new User();
        user.setFirstName(connection.fetchUserProfile().getFirstName());
        user.setLastName(connection.fetchUserProfile().getLastName());
        user.setPassword(password);
        user.setUsername(connection.fetchUserProfile().getUsername() == null ?
                connection.fetchUserProfile().getEmail() : connection.fetchUserProfile().getUsername());
        user.setEmail(connection.fetchUserProfile().getEmail());
        user.setRole(UserDetails.Role.ROLE_USER);
        user.setEnabled(true);

        SocialConnection userSocialConnection = SocialConnection.getBuilder(connection.createData()).buildSocialConnection();

        List<SocialConnection> socialConnections = new ArrayList<>();
        socialConnections.add(userSocialConnection);

        user.setSocialConnections(socialConnections);

        try {
            if (accessor.createUser(user))
            {
                List<String> identifier = new ArrayList<>();
                identifier.add(connection.fetchUserProfile().getEmail());

                List<User> fetch = accessor.getUser(identifier, User.SEARCH_BY_TYPE.EMAIL, IEnums.SEEK_MODE.USER_SOCIAL_CONNECTIONS);
                if (fetch != null && fetch.size() > 0) {
                    rval = fetch.get(0).getEmail();

                    //Fire off the welcome e-mail
                    Map<String, String> toMap = new HashMap<>();
                    toMap.put(connection.fetchUserProfile().getFirstName(), connection.fetchUserProfile().getEmail());
                    mailProvider.sendWelcomeEmail(connection.fetchUserProfile().getFirstName(), toMap);
                }
            }
        } catch (UsernameAlreadyInUseException e) {
            //Looks like they've already created an account.  Go ahead and associate the social connection.
            List<User> collisionUser = accessor.getUser(user.getEmail(), User.SEARCH_BY_TYPE.EMAIL, IEnums.SEEK_MODE.USER_SOCIAL_CONNECTIONS);
            if (collisionUser != null && collisionUser.size() > 0)
            {
                Long id = collisionUser.get(0).getUserId();

                if (accessor.addUserSocialConnection(connection.createData(), id))
                    rval = collisionUser.get(0).getEmail();
            }
        }

        return rval;
    }
}
