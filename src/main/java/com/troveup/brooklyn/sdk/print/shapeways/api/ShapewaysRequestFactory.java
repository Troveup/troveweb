package com.troveup.brooklyn.sdk.print.shapeways.api;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.io.IOException;

/**
 * Created by tim on 6/26/15.
 */
public class ShapewaysRequestFactory {

    public static String SHAPEWAYS_BASE_API_URL = "http://api.shapeways.com/";
    //public static String SHAPEWAYS_BASE_API_URL = "http://localhost:9001/";
    public static String SHAPEWAYS_UPLOAD_MODEL_URL_FRAGMENT = "models/v1";
    public static String SHAPEWAYS_GET_MODEL_URL_FRAGMENT = "models/<modelId>/v1";
    public static String SHAPEWAYS_URL_FRAGMENT = "orders/v1";
    public static String SHAPEWAYS_CANCEL_ORDER_FRAGMENT = "orders/{orderId}/v1";

    public static final String SHAPEWAYS_CONSUMER_KEY = "b33b4a147fe0daac66605f5bf72c344173de2fd3";
    public static final String SHAPEWAYS_CONSUMER_KEY_SECRET = "4b38484ecd803f43ccbbfe4e10ebe8beb28af8d1";
    public static final String SHAPEWAYS_OAUTH_TOKEN = "bee76723a27efe115969e152cb93ae3da4ad466f";
    public static final String SHAPEWAYS_OAUTH_TOKEN_SECRET = "9c866b7d8cca786aec15e12f1694fd11892b0d0b";


    private String consumerKey;
    private String consumerSecret;
    private String token;
    private String tokenSecret;

    public ShapewaysRequestFactory(String consumerKey, String consumerSecret, String token, String tokenSecret)
    {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.token = token;
        this.tokenSecret = tokenSecret;
    }


    public ShapewaysRequest getShapewaysRequest(Verb requestType, String requestUrl) throws IOException
    {
        OAuthService service = new ServiceBuilder()
                .provider(getShapewaysOAuthApi())
                .apiKey(consumerKey)
                .apiSecret(consumerSecret)
                .build();

        Token tokenObject = new Token(token, tokenSecret);
        OAuthRequest oAuthRequest = new OAuthRequest(requestType, requestUrl);
        service.signRequest(tokenObject, oAuthRequest);

        ShapewaysRequest request = new ShapewaysRequest(oAuthRequest);

        return request;
    }

    private Api getShapewaysOAuthApi()
    {
        DefaultApi10a defaultApi10a = new DefaultApi10a() {
            @Override
            public String getRequestTokenEndpoint()
            {
                return "http://api.shapeways.com/oauth1/request_token/v1";
            }

            @Override
            public String getAccessTokenEndpoint()
            {
                return "http://api.shapeways.com/oauth1/access_token/v1";
            }

            @Override
            public String getAuthorizationUrl(Token token) {
                return null;
            }
        };

        return defaultApi10a;
    }
}
