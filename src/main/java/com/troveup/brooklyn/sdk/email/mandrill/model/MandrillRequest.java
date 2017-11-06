package com.troveup.brooklyn.sdk.email.mandrill.model;

import com.troveup.brooklyn.sdk.http.impl.HttpClient;
import com.troveup.brooklyn.sdk.http.impl.HttpClientFactory;
import com.troveup.brooklyn.sdk.http.interfaces.IHttpClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author rschreijer
 * @since Mar 16, 2013
 */
public final class MandrillRequest<OUT> implements RequestModel<OUT> {
    private static final Logger log = LoggerFactory.getLogger(MandrillRequest.class);

	private final String url;
	private final Class<OUT> responseContentType;
	private final Map<String,? extends Object> requestParams;

	private final IHttpClientFactory httpClientFactory;

	public MandrillRequest( final String url, 
			final Map<String,? extends Object> params, 
			final Class<OUT> responseType, IHttpClientFactory httpClientFactory) {
		
		if(responseType == null) {
			throw new NullPointerException();
			
		}
		this.url = url;
		this.requestParams = params;
		this.responseContentType = responseType;
		this.httpClientFactory = httpClientFactory;
	}

	public final String getUrl() {
		return url;
	}

	public final HttpClient getRequest() throws IOException {
		final String paramsStr = LutungGsonUtils.getGson().toJson(
				requestParams, requestParams.getClass());

        //TODO:  This is an anti-pattern.  Change it to a factory pattern instead.
        HttpClient client = httpClientFactory.getHttpClientInstance();

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        client.configureForStandardRequest(url, headers, HttpClient.REQUEST_METHOD.POST, paramsStr, null);
		return client;
		
	}

	public final boolean validateResponseStatus(final int httpResponseStatus) {
		return (httpResponseStatus == 200);
	}

	public final OUT handleResponse(final String response)
			throws HandleResponseException {

		try {
            log.debug("raw content from response:\n" +response);
			return LutungGsonUtils.getGson().fromJson(
					response, responseContentType);
			
		} catch(final Throwable t) {
			String msg = "Error handling Mandrill response " +
					((response != null)?": '"+response+"'" : "");
			throw new HandleResponseException(msg, t);
			
		}
	}

}
