package com.troveup.brooklyn.sdk.email.mandrill.model;

import com.troveup.brooklyn.sdk.http.impl.HttpClient;

import java.io.IOException;


/**
 * 
 * @author rschreijer
 * @since Jan 7, 2013
 * @param <V> The type that response-data/ response-content is parsed to.
 */
public interface RequestModel<V> {
	
	/**
	 * @return The url for this request, as {@link String}.
	 */
	public String getUrl();

	/**
	 * @return The request object describing the request to 
	 * be made w/ a http client.
	 * @throws IOException
	 * @since Mar 22, 2013
	 */
	public HttpClient getRequest() throws IOException;
	
	/**
	 * <p>Checks weather the response-status is as-expected 
	 * for this request.</p>
	 * @param httpResponseStatus The HTTP response status
	 * @return <code>true</code> if the response status is as expected,
	 * <code>false</code> otherwise.
	 */
	public boolean validateResponseStatus(int httpResponseStatus);
	
	/**
	 * <p>Parses the content/data of this request's response into
	 * a desired format {@link V}.
	 * @param response
	 * @return
	 * @throws HandleResponseException
	 */
	public V handleResponse(String response) throws HandleResponseException;
	
}
