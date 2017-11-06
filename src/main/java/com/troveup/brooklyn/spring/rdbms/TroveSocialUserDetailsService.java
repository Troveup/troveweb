package com.troveup.brooklyn.spring.rdbms;

import com.troveup.brooklyn.spring.social.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;

public class TroveSocialUserDetailsService implements org.springframework.social.security.SocialUserDetailsService
{

    private TroveUserDetailsService userDetailsService;

    @Autowired
    public TroveSocialUserDetailsService(TroveUserDetailsService userDetailsService)
    {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public SocialUserDetails loadUserByUserId(String userName) throws UsernameNotFoundException, DataAccessException
    {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

        return userDetails;
    }
}
