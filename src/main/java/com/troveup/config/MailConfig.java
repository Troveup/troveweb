package com.troveup.config;

import com.troveup.brooklyn.util.TroveJavaMail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by tim on 4/20/15.
 */
@Configuration
//@PropertySource("classpath:com/troveup/config/mandrill.properties")
public class MailConfig
{
    @Bean
    public TroveJavaMail troveJavaMail()
    {
        return new TroveJavaMail();
    }
}
