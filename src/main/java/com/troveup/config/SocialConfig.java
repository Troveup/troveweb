/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.troveup.config;

import com.troveup.brooklyn.orm.user.datanucleus.UserAccessor;
import com.troveup.brooklyn.spring.rdbms.MySQLUsersConnectionRepository;
import com.troveup.brooklyn.spring.rdbms.TroveConnectionSignUp;
import org.springframework.social.connect.*;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.facebook.web.DisconnectController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.twitter.api.Twitter;

import javax.inject.Inject;

/**
 * Spring Social Configuration used to configure Spring Social Framework.
 */
@Configuration
@EnableSocial
public class SocialConfig implements SocialConfigurer {

    @Inject
    private TextEncryptor textEncryptor;

    @Inject
    private UserAccessor accessor;

    private UsersConnectionRepository usersConnectionRepository;
    private UserIdSource userIdSource;

    public SocialConfig()
    {

    }

    /**
     * Define specifically implemented connection factories, like that of Facebook.
     *
     * @param cfConfig Injected Spring ConnectionFactoryConfigurer
     * @param env Environment variable for seeking environment properties
     */
    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
        cfConfig.addConnectionFactory(new FacebookConnectionFactory(env.getProperty("facebook.clientId"), env.getProperty("facebook.clientSecret")));

        //Enable these if we ever start using them.
        //cfConfig.addConnectionFactory(new TwitterConnectionFactory(env.getProperty("twitter.consumerKey"), env.getProperty("twitter.consumerSecret")));
        //cfConfig.addConnectionFactory(new LinkedInConnectionFactory(env.getProperty("linkedin.consumerKey"), env.getProperty("linkedin.consumerSecret")));
    }

    /**
     * User connection repository specifier.  Used in defining RDBMS functionality for manipulating a given user's social
     * connections (e.g. authentication linkage between their Trove account and their social account, like Facebook).
     *
     * @param connectionFactoryLocator Injected Spring ConnectionFactoryLocator
     * @return Custom implemented UsersConnectionRepository.
     */
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        if ( usersConnectionRepository == null ) {
            usersConnectionRepository = new MySQLUsersConnectionRepository(connectionFactoryLocator, textEncryptor,
                    accessor, connectionSignUp());
        }
        return usersConnectionRepository;
    }

    /**
     * Define default AuthenticationNameUserIdSource as the User ID Source to be used.  Makes calls to configured
     * id sources like TroveSocialUserDetailsService
     *
     * @return Default UserIdSource
     */
    @Override
    public UserIdSource getUserIdSource() {
        if ( userIdSource == null ) {
            userIdSource = new AuthenticationNameUserIdSource();
        }
        return userIdSource;
    }

    /**
     * Define the default ConnectController.  Left dormant, as this is where to define interceptors for posting to
     * the user's Facebook wall after connection.
     *
     * @param connectionFactoryLocator Injected default ConnectionFactoryLocator
     * @param connectionRepository Injected default connectionRepository
     * @return Default ConnectController with added interceptors, if there are any.
     */
    @Bean
    public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
        ConnectController connectController = new ConnectController(connectionFactoryLocator, connectionRepository);
        //connectController.addInterceptor(new PostToWallAfterConnectInterceptor());
        //connectController.addInterceptor(new TweetAfterConnectInterceptor());
        return connectController;
    }

    /**
     * Define default DisconnectController.  Left dormant for now, as there isn't a case that necessitates social
     * disconnection from a Trove user account.
     *
     * @param usersConnectionRepository Custom UsersConnectionRepository for connection removal functionality.
     * @param environment Environment variable for retrieving environment properties.
     * @return Default configured DisconnectController
     */
    @Bean
    public DisconnectController disconnectController(UsersConnectionRepository usersConnectionRepository, Environment environment) {
        return new DisconnectController(usersConnectionRepository, environment.getProperty("facebook.clientSecret"));
    }

    /**
     * Generates a Facebook object from the specified ConnectionRepository.  Currently not used, but necessary for use
     * with the ConnectController, whose code is being left in place for later use.
     *
     * @param repository ConnectionRepository for accessing user connections.
     * @return A valid Facebook object.
     */
    @Bean
    @Scope(value="request", proxyMode= ScopedProxyMode.INTERFACES)
    public Facebook facebook(ConnectionRepository repository) {
        Connection<Facebook> connection = repository.findPrimaryConnection(Facebook.class);
        return connection != null ? connection.getApi() : null;
    }

    /**
     * Generates a Twitter object from the specified ConnectionRepository.  Currently not used, but necessary for use
     * with the ConnectController, whose code is being left in place for later use.
     *
     * @param repository ConnectionRepository for accessing user connections.
     * @return A valid Twitter object.
     */
    @Bean
    @Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
    public Twitter twitter(ConnectionRepository repository) {
        Connection<Twitter> connection = repository.findPrimaryConnection(Twitter.class);
        return connection != null ? connection.getApi() : null;
    }

    /**
     * Generates a LinkedIn object from the specified ConnectionRepository.  Currently not used, but necessary for use
     * with the ConnectController, whose code is being left in place for later use.
     *
     * @param repository ConnectionRepository for accessing user connections.
     * @return A valid LinkedIn object.
     */
    @Bean
    @Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
    public LinkedIn linkedin(ConnectionRepository repository) {
        Connection<LinkedIn> connection = repository.findPrimaryConnection(LinkedIn.class);
        return connection != null ? connection.getApi() : null;
    }

    /**
     * Custom ConnectionSignUp object bean for use with the UsersConnectionRepository and auto registration
     * capabilities using a social provider.
     *
     * @return ConnectionSignUp object for handling
     * <a href="http://docs.spring.io/spring-social/docs/1.0.3.RELEASE/reference/html/signin.html#signin_signup_implicit">implicit signups</a>.
     */
    @Bean
    public ConnectionSignUp connectionSignUp()
    {
        return new TroveConnectionSignUp();
    }

    /**
     * Sign in utilities for assisting with signing in using a given social provider.
     *
     * @return ProviderSignInUtils object for interacting with utils.
     */
    @Bean
    public ProviderSignInUtils providerSignInUtils()
    {
        return new ProviderSignInUtils();
    }

}
