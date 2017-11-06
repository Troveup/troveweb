package com.troveup.brooklyn.sdk.email.mandrill.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.troveup.brooklyn.sdk.email.mandrill.model.MandrillApiError;
import com.troveup.brooklyn.sdk.email.mandrill.model.MandrillRequest;
import com.troveup.brooklyn.sdk.email.mandrill.model.MandrillRequestDispatcher;
import com.troveup.brooklyn.sdk.http.interfaces.IHttpClientFactory;

/**
 * @author rschreijer
 * @since Mar 19, 2013
 */
final class MandrillUtil {
	protected static final String rootUrl = "https://mandrillapp.com/api/1.0/";

	/**
	 * @param key
	 * @return
	 */
	protected static final HashMap<String,Object> paramsWithKey(final String key) {
		final HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("key",key);
		return params;

	}
	
	/**
	 * @param url
	 * @param params
	 * @param responseType
	 * @return
	 * @throws MandrillApiError
	 * @throws IOException
	 */
	protected static final <OUT> OUT query(final String url, 
			final Map<String,Object> params, Class<OUT> responseType, IHttpClientFactory httpClientFactory)
					throws MandrillApiError, IOException {
		
		final MandrillRequest<OUT> requestModel =
				new MandrillRequest<OUT>(url, params, responseType, httpClientFactory);
		return MandrillRequestDispatcher.execute(requestModel);
		
	}
}
