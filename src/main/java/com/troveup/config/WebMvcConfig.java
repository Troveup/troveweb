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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.troveup.brooklyn.controllers.event.*;
import com.troveup.brooklyn.spring.web.TroveMultipartFile;
import com.troveup.brooklyn.spring.web.TroveMultipartResolver;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Spring MVC Configuration.
 * @author Craig Walls
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	//Appears unused
	/*@Inject
	private ConnectionRepository connectionRepository;*/

	//TODO:  Make a decision about Tiles and whether or not we'll be using them
	/*@Bean
	public ViewResolver viewResolver() {
		UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
		viewResolver.setViewClass(TilesView.class);
		return viewResolver;
	}*/

	@Bean
	public ViewResolver viewResolver()
	{
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

	/*@Bean
	public TilesConfigurer tilesConfigurer() {
		TilesConfigurer configurer = new TilesConfigurer();
		configurer.setDefinitions(new String[] {
				"/WEB-INF/layouts/tiles.xml",
				"/WEB-INF/views/**//*tiles.xml"
		});
		configurer.setCheckRefresh(true);
		return configurer;
	}*/

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/WEB-INF/resources/");
	}

	//TODO:  Make sure our messages text is in the right spot
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("/WEB-INF/resources/messages/messages");
        return messageSource;
    }

	@Bean
	public TroveEventPublisher troveEventPublisher()
	{
		return new TroveEventPublisher();
	}

	@Bean
	public ApplicationListener<OnRegistrationCompleteEvent> registrationApplicationListener()
	{
		return new RegistrationListener();
	}

	@Bean
	public ApplicationListener<ForgotPasswordEvent> forgotPasswordEventApplicationListener()
	{
		return new ForgotPasswordListener();
	}

	@Bean
	public Gson getGson()
	{
		return new GsonBuilder().create();
	}

	@Bean
	public MultipartResolver multipartResolver()
	{
		return new TroveMultipartResolver();
	}

}
