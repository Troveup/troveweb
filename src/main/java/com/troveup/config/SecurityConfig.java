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

import com.troveup.brooklyn.orm.user.interfaces.IUserAccessor;
import com.troveup.brooklyn.spring.rdbms.TroveSocialUserDetailsService;
import com.troveup.brooklyn.spring.rdbms.TroveUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.UserIdSource;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.social.security.SpringSocialConfigurer;


/**
 * Security Configuration used in configuring the Spring Security framework.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{

	@Autowired
	private ApplicationContext context;

	@Autowired
	IUserAccessor accessor;

	/**
	 * Specify authentication management configuration options.  Currently configured to use a custom UserDetailsService
	 * for retrieving user details from a DataNucleus managed repository.  Additionally defines the password encoder
	 * used to encode passwords for password confirmation purposes.
	 *
	 * @param auth Default authentication manager builder that needs to be configured.
	 * @throws Exception
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.userDetailsService(userDetailsService())
				.passwordEncoder(bCryptPasswordEncoder());
	}

	/**
	 * Specify areas that shouldn't be managed by web security, like the resources directory, as this will need to be
	 * accessed from all areas of the app.
	 *
	 * @param web WebSecurity configuration object.
	 * @throws Exception
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web
				.ignoring()
				.antMatchers("/resources/**");
	}

	//TODO:  Allow authentication for all pages, then handle what is shown on the pages on a page-level basis
	/**
	 * Specify authentication control mechanisms.  These include specifying the login page, login processing URL,
	 * where the user is pointed upon success, where the user is pointed upon failure, the logout url, and the
	 * URLs that are accessible without authorization.
	 *
	 * @param http HttpSecurity configuration object
	 * @throws Exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.formLogin()
				.loginPage("/signin")
				.loginProcessingUrl("/signin/authenticate")
				.defaultSuccessUrl("/")
				.failureUrl("/signin?param.error=bad_credentials")
				.and()
				.logout()
				.logoutUrl("/signout")
				.deleteCookies("JSESSIONID")
				.and()
				.authorizeRequests()
				.antMatchers(
						"/blog",
						"/ppfgirl",
						"/genericsignup",
						"/sellers/**",
						"/myprofile",
						"/readytowear/**",
						"/featured/**",
						"/landing",
						"/addsimpleitemtobag",
						"/confirm",
						"/removeitem",
						"/completecartorder",
						"/preparecart",
						"/addgiftcard",
						"/redeemgiftcard",
						"/savenewmodel",
						"/noncustaddtobag",
						"/addpromocodes",
						"removepromocodes",
						"/private/bag",
						"/onboard/**",
						"/ftui/**",
						"/eventconfirm",
						"/error/**",
						"/redeem",
						"/welcome",
						"/try",
						"/giftcard",
						"/giftcard/buy",
						"/freering",
						"/customizerpriceestimate",
						"/cardpriceestimate",
						"/",
						"/public/customize/**",
						"/public/ftuicust/**",
						"/public/cust/**",
						"/public/productdescription/**",
						"/feed",
						"/pagefeed**",
						"/pagecategory**",
						"/featured",
						"/_ah/**",
						"/events/**",
						"/public/end",
						"/public/thankyou",
						"/public/thankyou/**",
						"/public/unsubscribe",
						"/applyftuepromocode",
						"/submitftuecheckout",
						"/initftuecheckout",
						"/admin/**",
						"/public/landing/**",
						"/favicon.ico",
						"/public/ringsizes",
						"/public/welcome/**",
						"/public/welcome",
						"/resources/**",
						"/auth/**",
						"/signin/**",
						"/signup/**",
						"/disconnect/facebook",
						"/worker/**",
						"/password/**",
						"/public/ph**",
						"/public/phcustomize/**",
						"/ftuisubmit",
						"/privacy",
						"/terms",
						"/copyright",
						"/delivery",
						"/public/cust/**",
						"/public/ftuicust/**",
						"/legal/**",
						"/trackclick",
						"/refreshBaseItemImages/*",
						"/about",
						"/team",
						"/mission",
						"/press",
						"/partners",
						"/faq",
						"/privacy",
						"/terms",
						"/copyright",
						"/sales",
						"/submitftueprototypeorder",
						"/s/**",
						"/newcustomizerpriceestimate",
						"/signinasync",
						"/style-guide").permitAll()
				.antMatchers("/**").authenticated()
				.and()
				.rememberMe()
				.and()
				.apply(new SpringSocialConfigurer())
				.and().setSharedObject(ApplicationContext.class, context);
	}



	/**
	 * UserDetailsService declaration bean for providing implemented functionality for retrieving user data.
	 *
	 * @return UserDetailsService object for data retrieval.
	 */
	@Bean
	public UserDetailsService userDetailsService() {
		return new TroveUserDetailsService(accessor);
	}

	@Bean
	public TroveUserDetailsService troveUserDetailsService() {
		return (TroveUserDetailsService) userDetailsService();
	}

	/**
	 * Wrapped UserDetailsService for providing implemented functionality for retrieving social user data.
	 *
	 * @return SocialUserDetailsService object for data retrieval.
	 */
	@Bean
	public SocialUserDetailsService socialUsersDetailService() {
		return new TroveSocialUserDetailsService((TroveUserDetailsService) userDetailsService());
	}

	@Bean
	public UserIdSource userIdSource() {
		return new AuthenticationNameUserIdSource();
	}

	//TODO:  Change this so that it's not noOpText for prod
	@Bean
	public TextEncryptor textEncryptor() {
		return Encryptors.noOpText();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder(10);
	}

	@Bean(name="troveAuthenticationManager")
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
