package com.troveup.config;

import com.google.appengine.api.utils.SystemProperty;
import com.troveup.brooklyn.orm.admin.datanucleus.AdminAccessor;
import com.troveup.brooklyn.orm.admin.interfaces.IAdminAccessor;
import com.troveup.brooklyn.orm.cart.datanucleus.CartAccessor;
import com.troveup.brooklyn.orm.cart.datanucleus.PromoCodeAccessor;
import com.troveup.brooklyn.orm.cart.interfaces.ICartAccessor;
import com.troveup.brooklyn.orm.cart.interfaces.IPromoCodeAccessor;
import com.troveup.brooklyn.orm.clicktracker.ClickTracker;
import com.troveup.brooklyn.orm.countries.datanucleus.CountryAccessor;
import com.troveup.brooklyn.orm.countries.interfaces.ICountryAccessor;
import com.troveup.brooklyn.orm.feed.datanucleus.FeedAccessor;
import com.troveup.brooklyn.orm.feed.interfaces.IFeedAccessor;
import com.troveup.brooklyn.orm.ftui.impl.FtueAccessor;
import com.troveup.brooklyn.orm.item.datanucleus.ItemAccessor;
import com.troveup.brooklyn.orm.item.interfaces.IItemAccessor;
import com.troveup.brooklyn.orm.materials.datanucleus.MaterialFilterAccessor;
import com.troveup.brooklyn.orm.materials.interfaces.IMaterialFilterAccessor;
import com.troveup.brooklyn.orm.order.datanucleus.OrderAccessor;
import com.troveup.brooklyn.orm.order.datanucleus.PrintOrderAccessor;
import com.troveup.brooklyn.orm.order.interfaces.IOrderAccessor;
import com.troveup.brooklyn.orm.order.interfaces.IPrintOrderAccessor;
import com.troveup.brooklyn.orm.renderqueue.datanucleus.RenderQueueAccessor;
import com.troveup.brooklyn.orm.renderqueue.interfaces.IRenderQueueAccessor;
import com.troveup.brooklyn.orm.simpleitem.datanucleus.SimpleItemAccessor;
import com.troveup.brooklyn.orm.simpleitem.interfaces.ISimpleItemAccessor;
import com.troveup.brooklyn.orm.storefront.datanucleus.StoreFrontAccessor;
import com.troveup.brooklyn.orm.storefront.interfaces.IStoreFrontAccessor;
import com.troveup.brooklyn.orm.urlshortener.datanucleus.ShortLinkAccessor;
import com.troveup.brooklyn.orm.urlshortener.interfaces.IShortLinkAccessor;
import com.troveup.brooklyn.orm.user.datanucleus.UserAccessor;
import com.troveup.brooklyn.orm.user.interfaces.IUserAccessor;
import com.troveup.brooklyn.spring.rdbms.TroveUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jdo.LocalPersistenceManagerFactoryBean;
import org.springframework.orm.jdo.TransactionAwarePersistenceManagerFactoryProxy;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;
import java.util.Properties;

/**
 * Created by tim on 4/16/15.
 */
@Configuration
@PropertySource("classpath:com/troveup/config/application.properties")
public class PersistenceConfig {
    @Autowired
    Environment env;

    protected final Boolean useGoogleCloudSqlLocally = false;

    protected final String autoCreateLocalSchema = "true";

    private final String port = "3306";
    // Port that Tim uses because he's silly and likes MAMP
    // protected final String port = "8889";

    @Bean
    @Lazy
    public PersistenceManagerFactory pmf() {
        //TODO:  Put these in the application.properties file

        if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
            Properties jdoProperties = new Properties();
            jdoProperties.put("javax.jdo.PersistenceManagerFactoryClass", "org.datanucleus.api.jdo.JDOPersistenceManagerFactory");
            jdoProperties.put("datanucleus.connectionPoolingType", "DBCP");
            jdoProperties.put("datanucleus.datastoreReadTimeout", "0");
            jdoProperties.put("datanucleus.query.jdoql.allowAll", "true");
            jdoProperties.put("javax.jdo.option.ConnectionURL", getCloudSqlConnectionString());
            jdoProperties.put("javax.jdo.option.ConnectionUserName", "root");
            jdoProperties.put("javax.jdo.option.ConnectionDriverName", "com.mysql.jdbc.GoogleDriver");
            jdoProperties.put("javax.jdo.option.Multithreaded", "true");
            jdoProperties.put("datanucleus.autoCreateSchema", "true");

            PersistenceManagerFactory factory = JDOHelper.getPersistenceManagerFactory(jdoProperties);
            return factory;
        } else {
            Properties jdoProperties = new Properties();

            jdoProperties.put("javax.jdo.option.ConnectionDriverName", "com.mysql.jdbc.Driver");
            jdoProperties.put("javax.jdo.PersistenceManagerFactoryClass", "org.datanucleus.api.jdo.JDOPersistenceManagerFactory");
            jdoProperties.put("datanucleus.autoCreateSchema", autoCreateLocalSchema);

            if (useGoogleCloudSqlLocally) {
                jdoProperties.put("javax.jdo.option.ConnectionURL", getLocalCloudSqlConnectionString());
                jdoProperties.put("javax.jdo.option.ConnectionUserName", getLocalCloudSqlUsername());
                jdoProperties.put("javax.jdo.option.ConnectionPassword", getLocalCloudSqlPassword());
            } else {
                jdoProperties.put("javax.jdo.option.ConnectionURL", "jdbc:mysql://127.0.0.1:" + port + "/troveweb");
                jdoProperties.put("javax.jdo.option.ConnectionUserName", "root");
            }

            PersistenceManagerFactory factory = JDOHelper.getPersistenceManagerFactory(jdoProperties);
            return factory;
        }
    }

    @Bean
    public IUserAccessor userAccessor() {
        return new UserAccessor(pmf());
    }

    @Bean
    public TroveUserDetailsService userDetailsService() {
        return new TroveUserDetailsService(userAccessor());
    }

    @Bean
    public IItemAccessor itemAccessor() {
        return new ItemAccessor(pmf());
    }

    @Bean
    public ICountryAccessor countryAccessor() {
        return new CountryAccessor(pmf());
    }

    @Bean
    public IOrderAccessor orderAccessor() {
        return new OrderAccessor(pmf());
    }

    @Bean
    public ICartAccessor cartAccessor() {
        return new CartAccessor(pmf());
    }

    @Bean
    public IPromoCodeAccessor promoCodeAccessor() {
        return new PromoCodeAccessor(pmf());
    }

    @Bean
    public IMaterialFilterAccessor materialFilterAccessor() {
        return new MaterialFilterAccessor(pmf());
    }

    @Bean
    public FtueAccessor ftueAccessor() {
        return new FtueAccessor(pmf());
    }

    @Bean
    public IRenderQueueAccessor renderQueueAccessor() {
        return new RenderQueueAccessor(pmf());
    }

    @Bean
    public IFeedAccessor feedAccessor() {
        return new FeedAccessor(pmf());
    }

    @Bean
    ClickTracker clickTracker() {
        return new ClickTracker(pmf());
    }

    @Bean
    IPrintOrderAccessor printOrderAccessor() {
        return new PrintOrderAccessor(pmf());
    }

    @Bean
    IAdminAccessor adminAccessor() {
        return new AdminAccessor(pmf());
    }

    @Bean
    IShortLinkAccessor shortLinkAccessor() {return new ShortLinkAccessor(pmf());}

    @Bean
    ISimpleItemAccessor simpleItemAccessor() {return new SimpleItemAccessor(pmf());}

    @Bean
    IStoreFrontAccessor storeFrontAccessor() {return new StoreFrontAccessor(pmf());}

    private String getCloudSqlConnectionString() {
        return getEnvironmentProperty("cloudsql.connectionstring", null);
    }

    private String getLocalCloudSqlConnectionString() {
        return getEnvironmentProperty("cloudsql.localconnectionstring", null);
    }

    private String getLocalCloudSqlUsername() {
        return getEnvironmentProperty("cloudsql.localusername", null);
    }

    private String getLocalCloudSqlPassword() {
        return getEnvironmentProperty("cloudsql.localpassword", null);
    }

    private String getEnvironmentProperty(String propertyKey, String backup) {
        String propertyString;
        if (env.getProperty(propertyKey) != null)
            propertyString = env.getProperty(propertyKey);
        else
            propertyString = backup;

        return propertyString;
    }
}
