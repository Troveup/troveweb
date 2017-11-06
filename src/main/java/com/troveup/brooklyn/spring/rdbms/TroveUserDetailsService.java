package com.troveup.brooklyn.spring.rdbms;

import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.user.interfaces.IUserAccessor;
import com.troveup.brooklyn.orm.user.model.User;
import com.troveup.brooklyn.spring.social.model.UserDetails;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


/**
 * Created by tim on 4/13/15.
 */
public class TroveUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    IUserAccessor userAccessor;

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    public TroveUserDetailsService(IUserAccessor userAccessor)
    {
        this.userAccessor = userAccessor;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException
    {
        User user;

        //Email
        if (identifier.contains("@"))
        {
            user = userAccessor.getUserByEmail(identifier, IEnums.SEEK_MODE.USER_SOCIAL_CONNECTIONS);
        }
        //Username
        else
        {
            user = userAccessor.getUserByUsername(identifier, IEnums.SEEK_MODE.USER_SOCIAL_CONNECTIONS);
        }

        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        if (user == null)
            throw new UsernameNotFoundException("No user found with identifier: " + identifier);

        return UserDetails.getBuilder()
                .firstName(user.getFirstName())
                .id(user.getUserId())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .role(user.getRole())
                .socialConnections(user.getSocialConnections())
                .username(user.getUsername())
                .email(user.getEmail())
                .thumbnailProfileImage(user.getProfileImageThumbnailPath())
                .accountNonExpired(accountNonExpired)
                .credentialsNonExpired(credentialsNonExpired)
                .accountNonLocked(accountNonLocked)
                .enabledStatus(user.isEnabled())
                .build();
    }
}
