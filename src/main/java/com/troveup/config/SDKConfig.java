package com.troveup.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.troveup.brooklyn.sdk.cache.jcache.JCacheService;
import com.troveup.brooklyn.sdk.email.interfaces.IMailProvider;
import com.troveup.brooklyn.sdk.email.mandrill.api.MandrillApi;
import com.troveup.brooklyn.sdk.cdn.gcs.GoogleCloudStorage;
import com.troveup.brooklyn.sdk.cdn.interfaces.CloudStore;
import com.troveup.brooklyn.sdk.email.mandrill.business.MandrillProvider;
import com.troveup.brooklyn.sdk.http.impl.HttpClientFactory;
import com.troveup.brooklyn.sdk.http.interfaces.IHttpClientFactory;
import com.troveup.brooklyn.sdk.keyvaluestore.datastore.api.DatastoreApi;
import com.troveup.brooklyn.sdk.keyvaluestore.datastore.business.KeyValueDatastore;
import com.troveup.brooklyn.sdk.keyvaluestore.interfaces.IKeyValueStore;
import com.troveup.brooklyn.sdk.image.gae.api.GoogleImages;
import com.troveup.brooklyn.sdk.image.gae.business.GoogleImageManipProvider;
import com.troveup.brooklyn.sdk.image.interfaces.IImageManipProvider;
import com.troveup.brooklyn.sdk.mail.business.EasyPostProvider;
import com.troveup.brooklyn.sdk.meshexporter.forge.api.ForgeExporter;
import com.troveup.brooklyn.sdk.meshexporter.forge.business.Forge;
import com.troveup.brooklyn.sdk.meshexporter.java.business.MeshExport;
import com.troveup.brooklyn.sdk.payment.braintree.api.BraintreeApi;
import com.troveup.brooklyn.sdk.payment.braintree.api.BraintreeCommonApi;
import com.troveup.brooklyn.sdk.payment.braintree.api.BraintreePaymentFactory;
import com.troveup.brooklyn.sdk.print.imaterialise.api.*;
import com.troveup.brooklyn.sdk.print.interfaces.IPrintSupplier;
import com.troveup.brooklyn.sdk.print.interfaces.ISampleSupplier;
import com.troveup.brooklyn.sdk.print.shapeways.api.*;
import com.troveup.brooklyn.sdk.print.shapeways.business.ShapewaysPrintSupplier;
import com.troveup.brooklyn.sdk.print.voodoo.api.VoodooApi;
import com.troveup.brooklyn.sdk.print.voodoo.business.VoodooSampleSupplier;
import com.troveup.brooklyn.sdk.realityserver.api.Nimbix;
import com.troveup.brooklyn.sdk.realityserver.api.RealityServer;
import com.troveup.brooklyn.sdk.realityserver.business.RealityServerManager;
import com.troveup.brooklyn.sdk.realityserver.model.RealityServerPostbackResponse;
import com.troveup.brooklyn.sdk.realityserver.model.RealityServerPostbackResponseDeserializer;
import com.troveup.brooklyn.sdk.tax.avalara.api.AvaCommonApi;
import com.troveup.brooklyn.sdk.tax.avalara.api.AvaTax;
import com.troveup.brooklyn.sdk.tax.avalara.business.AvalaraTaxHandler;
import com.troveup.brooklyn.sdk.tax.interfaces.ITaxHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.Random;

/**
 * Created by tim on 5/23/15.
 */
@Configuration
@PropertySource("classpath:com/troveup/config/application.properties")
public class SDKConfig
{
    @Autowired
    Environment env;

    @Bean
    IPrintSupplier printSupplier()
    {
        //return new imatPrintSupplier(getCdnFullPath());
        return shapewaysPrintSupplier();
    }

    @Bean
    public imatUploadModelApi iMaterialiseUploadApi()
    {
        String fulliMaterialiseUploadApiUrl = (getiMatBaseUrl() + getimatUploadApiUrlFragment()).replace("<toolID>", getToolId());
        return new imatUploadModelApi(fulliMaterialiseUploadApiUrl);
    }

    @Bean
    imatPriceCheckApi priceCheckApi()
    {
        return new imatPriceCheckApi(getApiCode(), getToolId(), getiMatBaseUrl(),
                getimatInstantPriceUrlFragment(), getimatModelPriceUrlFragment());
    }

    @Bean
    CloudStore cloudStore()
    {
        return new GoogleCloudStorage();
    }

    @Bean
    IHttpClientFactory httpClientFactory()
    {
        return new HttpClientFactory();
    }

    @Bean
    imatOrderApi cartApi()
    {
        return new imatOrderApi(getiMatBaseUrl(), getCreateCartUrlFragment(), getCreateCartItemUrlFragment(),
                getAddRemoveItemUrlFragment(), getCheckoutUrlFragment(), getToolId(), getApiCode(),
                getimatOrderStatusUrlFragment());
    }

    @Bean
    imatInfoApi infoApi()
    {
        return new imatInfoApi(getiMatBaseUrl(), getMaterialsApiUrlFragment(), getRegisteredImaterialiseEmail());
    }

    @Bean
    GsonBuilder gsonBuilder()
    {
        return new GsonBuilder();
    }

    @Bean
    RealityServerPostbackResponseDeserializer realityServerPostbackResponseDeserializer()
    {
        return new RealityServerPostbackResponseDeserializer();
    }

    @Bean
    Gson gson() {
        GsonBuilder builder = gsonBuilder();
        builder.registerTypeAdapter(RealityServerPostbackResponse.class, realityServerPostbackResponseDeserializer());
        return builder.create();
    }

    @Bean
    ITaxHandler taxHandler()
    {
        return new AvalaraTaxHandler(getAvalaraCompanyCode());
    }

    @Bean
    AvaTax avaTax()
    {
        //Fix this, only spits out a few characters.
        //byte[] encodedBytes = Base64.encodeBase64((getAvalaraAccountNumber() + ":" + getAlavaraLicenseKey()).getBytes());

        return new AvaTax(getAvalaraBaseTaxUrl(), getAvalaraTaxUrlFragment(), getAvalaraBasicAuthHeader());
    }

    @Bean
    BraintreePaymentFactory braintreePaymentFactory()
    {
        com.braintreegateway.Environment braintreeEnvironment;
        if (getEnvironmentProperty("braintree.environment", BraintreeCommonApi.BT_ENVIRONMENT).equals("PRODUCTION"))
            braintreeEnvironment = com.braintreegateway.Environment.PRODUCTION;
        else
            braintreeEnvironment = com.braintreegateway.Environment.SANDBOX;

        return new BraintreePaymentFactory(braintreeEnvironment, getBraintreeMerchantId(), getBraintreePublicKey(),
                getBraintreePrivateKey());
    }

    @Bean
    BraintreeApi braintreeApi()
    {
        return new BraintreeApi(braintreePaymentFactory());
    }

    @Bean
    MandrillApi mandrillApi() { return new MandrillApi(getMandrillApiKey(), httpClientFactory()); }

    @Bean
    IMailProvider mandrillProvider() { return new MandrillProvider();}

    @Bean
    JCacheService jCacheService() {return new JCacheService();}

    @Bean
    imatCancelOrderApi cancelOrderApi()
    {
        return new imatCancelOrderApi(getiMatBaseUrl(), getimatCancelOrderByNumbersUrlFragment(),
                getApiCode(), getToolId());
    }

    @Bean
    VoodooApi voodooApi()
    {
        return new VoodooApi(getVoodooApiKey(), getVoodooBaseUrl(), getVoodooMaterialsUrlFragment(),
                getVoodooSubmitOrderUrlFragment(), httpClientFactory(), gson());
    }

    @Bean
    ISampleSupplier sampleSupplier()
    {
        return new VoodooSampleSupplier(voodooApi());
    }

    @Bean
    GoogleImages googleImages()
    {
        return new GoogleImages();
    }

    @Bean
    IImageManipProvider imageManipProvider()
    {
        return new GoogleImageManipProvider(googleImages());
    }

    @Bean
    MeshExport meshExport()
    {
        return new MeshExport();
    }

    @Bean
    RealityServer realityServer()
    {
        return new RealityServer(gson(), httpClientFactory());
    }

    @Bean
    Nimbix nimbix()
    {
        return new Nimbix(gson(), httpClientFactory());
    }

    @Bean
    DatastoreApi datastoreApi()
    {
        return new DatastoreApi();
    }

    @Bean
    IKeyValueStore keyValueStore()
    {
        return new KeyValueDatastore(datastoreApi());
    }

    @Bean
    RealityServerManager realityServerManager()
    {
        return new RealityServerManager(nimbix(), realityServer(), getNimbixIpAddress(), getImageStoreCdnName(),
                getRealityServerPostbackUrl());
    }

    @Bean
    ShapewaysRequestFactory shapewaysRequestFactory()
    {
        return new ShapewaysRequestFactory(getShapewaysConsumerKey(), getShapewaysConsumerSecret(), getShapewaysToken(),
                getShapewaysTokenSecret());
    }

    @Bean
    ShapewaysModelApi shapewaysModelApi()
    {
        return new ShapewaysModelApi(shapewaysRequestFactory(), gson());
    }

    @Bean
    ShapewaysPriceApi shapewaysPriceApi()
    {
        return new ShapewaysPriceApi();
    }

    @Bean
    ShapewaysOrdersApi shapewaysOrdersApi()
    {
        return new ShapewaysOrdersApi(shapewaysRequestFactory(), gson());
    }

    @Bean
    ShapewaysPrintSupplier shapewaysPrintSupplier()
    {
        return new ShapewaysPrintSupplier();
    }

    @Bean
    EasyPostProvider easyPostProvider()
    {
        return new EasyPostProvider(getEasyPostApiKey(), gson());
    }

    @Bean
    Random random() {return new Random();}

    @Bean
    ForgeExporter forgeExporter()
    {
        return new ForgeExporter(gson(), httpClientFactory(), getForgeIpAddress());
    }

    @Bean
    Forge forge()
    {
        return new Forge(forgeExporter(), getcdnBucketName(), null, getForgeLocalDebugSetting());
    }

    private String getApiCode()
    {
        return getEnvironmentProperty("imaterialise.apicode", imatCommonApi.API_CODE);
    }

    private String getToolId()
    {
        return getEnvironmentProperty("imaterialise.toolid", imatCommonApi.TOOL_ID);
    }

    private String getiMatBaseUrl()
    {
        return getEnvironmentProperty("imaterialise.baseurl", imatCommonApi.IMATERIALISE_SANDBOX_BASE_URL);
    }

    private String getimatUploadApiUrlFragment()
    {
        return getEnvironmentProperty("imaterialise.uploadurlfragment", imatCommonApi.IMATERIALISE_UPLOAD_API_URL_FRAGMENT);
    }

    private String getimatInstantPriceUrlFragment()
    {
        return getEnvironmentProperty("imaterialise.instantpriceurlfragment", imatCommonApi.IMATERIALISE_INSTANT_PRICING_URL_FRAGMENT);
    }

    private String getimatModelPriceUrlFragment()
    {
        return getEnvironmentProperty("imaterialise.modelpriceurlfragment", imatCommonApi.IMATERIALISE_MODEL_PRICE_URL_FRAGMENT);
    }

    private String getcdnUrlBasePath()
    {
        return getEnvironmentProperty("cloudstore.cloudstoreurl", GoogleCloudStorage.CDN_URL_BASE_PATH);
    }

    private String getcdnBucketName()
    {
        return getEnvironmentProperty("cloudstore.publicbucketname", GoogleCloudStorage.CDN_BUCKET_NAME);
    }

    private String getCdnFullPath()
    {
        String cdnFullPath;
        cdnFullPath = getcdnUrlBasePath() + getcdnBucketName() + "/";
        return cdnFullPath;
    }

    private String getCreateCartUrlFragment()
    {
        return getEnvironmentProperty("imaterialise.createcarturlfragment", imatCommonApi.IMATERIALISE_CREATE_CART_URL_FRAGMENT);
    }

    private String getCreateCartItemUrlFragment()
    {
        return getEnvironmentProperty("imaterialise.createcartitemurlfragment", imatCommonApi.IMATERIALISE_CREATE_CART_ITEM_URL_FRAGMENT);
    }

    private String getAddRemoveItemUrlFragment()
    {
        return getEnvironmentProperty("imaterialise.addremoveitemurlfragment", imatCommonApi.IMATERIALISE_ADD_REMOVE_ITEM_URL_FRAGMENT);
    }

    private String getCheckoutUrlFragment()
    {
        return getEnvironmentProperty("imaterialise.checkouturlfragment", imatCommonApi.IMATERIALISE_CHECKOUT_URL_FRAGMENT);
    }

    private String getMaterialsApiUrlFragment()
    {
        return getEnvironmentProperty("imaterialise.materialsapifragment", imatCommonApi.IMATERIALISE_MATERIALS_API_URL_FRAGMENT);
    }

    private String getRegisteredImaterialiseEmail()
    {
        return getEnvironmentProperty("imaterialise.registeredemail", imatCommonApi.IMATERIALISE_REGISTERED_EMAIL);
    }

    private String getAvalaraAccountNumber()
    {
        return getEnvironmentProperty("avalara.accountnumber", AvaCommonApi.AVA_ACCOUNT_NUMBER);
    }

    private String getAlavaraLicenseKey()
    {
        return getEnvironmentProperty("avalara.licensekey", AvaCommonApi.AVA_LICENSE_KEY);
    }

    private String getAvalaraCompanyCode()
    {
        return getEnvironmentProperty("avalara.companycode", AvaCommonApi.AVA_TROVE_COMPANY_CODE);
    }

    private String getAvalaraBaseTaxUrl()
    {
        return getEnvironmentProperty("avalara.basetaxurl", AvaCommonApi.AVA_BASE_TAX_URL);
    }

    private String getAvalaraTaxUrlFragment()
    {
        return getEnvironmentProperty("avalara.gettaxurlfragment", AvaCommonApi.AVA_GET_TAX_URL_FRAGMENT);
    }

    private String getAvalaraBasicAuthHeader()
    {
        return getEnvironmentProperty("avalara.basicauth", AvaCommonApi.AVA_BASIC_AUTH_HEADER);
    }

    private String getBraintreeMerchantId()
    {
        return getEnvironmentProperty("braintree.merchantid", BraintreeCommonApi.BT_MERCHANT_ID);
    }

    private String getBraintreePublicKey()
    {
        return getEnvironmentProperty("braintree.publickey", BraintreeCommonApi.BT_PUBLIC_KEY);
    }

    private String getBraintreePrivateKey()
    {
        return getEnvironmentProperty("braintree.privatekey", BraintreeCommonApi.BT_PRIVATE_KEY);
    }

    private String getBraintreeEnvironment()
    {
        return getEnvironmentProperty("braintree.environment", BraintreeCommonApi.BT_ENVIRONMENT);
    }

    private String getMandrillApiKey()
    {
        return getEnvironmentProperty("mandrill.apikey", MandrillApi.API_KEY);
    }

    private String getimatOrderStatusUrlFragment()
    {
        return getEnvironmentProperty("imaterialise.orderstatusurlfragment",
                imatCommonApi.IMATERIALISE_ORDER_STATUS_FRAGMENT);
    }

    private String getimatCancelOrderByNumbersUrlFragment()
    {
        return getEnvironmentProperty("imaterialise.cancelorderbynumberurlfragment",
                imatCommonApi.IMATERIALISE_CANCEL_ORDER_BY_NUMBER_URL_FRAGMENT);
    }

    private String getVoodooApiKey()
    {
        return getEnvironmentProperty("voodoo.apikey",
                VoodooApi.VOODOO_API_KEY);
    }

    private String getVoodooBaseUrl()
    {
        return getEnvironmentProperty("voodoo.baseurl",
                VoodooApi.VOODOO_BASE_URL);
    }

    private String getVoodooMaterialsUrlFragment()
    {
        return getEnvironmentProperty("voodoo.materialsurlfragment",
                VoodooApi.VOODOO_MATERIALS_URL_FRAGMENT);
    }

    private String getVoodooSubmitOrderUrlFragment()
    {
        return getEnvironmentProperty("voodoo.submitorderurlfragment",
                VoodooApi.VOODOO_SUBMIT_ORDER_URL_FRAGMENT);
    }

    private String getNimbixIpAddress()
    {
        //IP address is dynamic, impossible to guess.
        return getEnvironmentProperty("nimbix.ipaddress", null);
    }

    private String getImageStoreCdnName()
    {
        return getEnvironmentProperty("cloudstore.itemimagecdn", null);
    }

    private String getEnvironmentBaseUrl()
    {
        return getEnvironmentProperty("environment.baseurl", null);
    }

    private String getRealityServerPostbackFragmentUrl()
    {
        return getEnvironmentProperty("realityserver.postback", null);
    }

    private String getRealityServerPostbackUrl()
    {
        return getEnvironmentBaseUrl() + getRealityServerPostbackFragmentUrl();
    }

    private String getShapewaysConsumerKey()
    {
        return getEnvironmentProperty("shapeways.consumerkey", ShapewaysRequestFactory.SHAPEWAYS_CONSUMER_KEY);
    }

    private String getShapewaysConsumerSecret()
    {
        return getEnvironmentProperty("shapeways.consumersecret", ShapewaysRequestFactory.SHAPEWAYS_CONSUMER_KEY_SECRET);
    }

    private String getShapewaysToken()
    {
        return getEnvironmentProperty("shapeways.oauthtoken", ShapewaysRequestFactory.SHAPEWAYS_OAUTH_TOKEN);
    }

    private String getShapewaysTokenSecret()
    {
        return getEnvironmentProperty("shapeways.otokensecret", ShapewaysRequestFactory.SHAPEWAYS_OAUTH_TOKEN_SECRET);
    }

    private String getEasyPostApiKey()
    {
        return getEnvironmentProperty("easypost.apikey", EasyPostProvider.API_SANDBOX_KEY);
    }

    private String getForgeIpAddress()
    {
        return getEnvironmentProperty("forge.ipaddress", ForgeExporter.FORGE_DEFAULT_DEV_IP_ADDRESS);
    }

    private Boolean getForgeLocalDebugSetting()
    {
        return Boolean.parseBoolean(getEnvironmentProperty("forge.localdebug", "false"));
    }

    private String getEnvironmentProperty(String propertyKey, String backup)
    {
        String propertyString;
        if (env.getProperty(propertyKey) != null)
            propertyString = env.getProperty(propertyKey);
        else
            propertyString = backup;

        return propertyString;
    }
}
