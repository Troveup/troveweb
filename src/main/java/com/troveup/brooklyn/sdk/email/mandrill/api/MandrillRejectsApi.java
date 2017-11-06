package com.troveup.brooklyn.sdk.email.mandrill.api;

import com.troveup.brooklyn.sdk.email.mandrill.model.MandrillApiError;
import com.troveup.brooklyn.sdk.email.mandrill.model.MandrillHelperClasses;
import com.troveup.brooklyn.sdk.http.interfaces.IHttpClientFactory;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author rschreijer
 * @since Mar 19, 2013
 */
public class MandrillRejectsApi extends MandrillApiCommon{
	private static final String rootUrl = MandrillUtil.rootUrl;
	private final String key;
	
	public MandrillRejectsApi(final String key, IHttpClientFactory httpClientFactory) {
		super(httpClientFactory);
		this.key = key;
	}
	
	public Boolean add(final String email, final String comment, 
			final String subaccount) throws MandrillApiError, IOException {
		
		final HashMap<String,Object> params = MandrillUtil.paramsWithKey(key);
		params.put("email", email);
		params.put("comment", comment);
		params.put("subaccount", subaccount);
		return MandrillUtil.query(rootUrl+ "rejects/add.json", 
				params, MandrillHelperClasses.MandrillRejectsAdded.class, httpClientFactory).getAdded();
		
	}
	
	/**
	 * <p>Retrieve your email rejection blacklist. You can 
	 * provide an email address to limit the results. Returns 
	 * up to 1000 results. By default, entries that have expired 
	 * are excluded from the results; use includeExpired to 
	 * true to include them.</p>
	 * @param email An optional email address to search by.
	 * @param includeExpired Whether to include rejections that 
	 * have already expired.
	 * @return Up to 1000 {@link MandrillRejectsEntry} objects.
	 * @throws MandrillApiError
	 * @throws IOException
	 */
	public MandrillRejectsEntry[] list(final String email, 
			final Boolean includeExpired) throws MandrillApiError, IOException {
	
		return list(email, includeExpired, null);
		
	}
	
	/**
	 * <p>Retrieve your email rejection blacklist. You can 
	 * provide an email address to limit the results. Returns 
	 * up to 1000 results. By default, entries that have expired 
	 * are excluded from the results; use includeExpired to 
	 * true to include them.</p>
	 * @param email An optional email address to search by.
	 * @param includeExpired Whether to include rejections that 
	 * have already expired.
	 * @param subaccount An optional unique identifier for the 
	 * subaccount to limit the blacklist.
	 * @return Up to 1000 {@link MandrillRejectsEntry} objects.
	 * @throws MandrillApiError
	 * @throws IOException
	 */
	public MandrillRejectsEntry[] list(final String email, 
			final Boolean includeExpired, final String subaccount) 
					throws MandrillApiError, IOException {
		
		final HashMap<String,Object> params = MandrillUtil.paramsWithKey(key);
		params.put("email", email);
		params.put("include_expired", includeExpired);
		if(subaccount != null) {
			params.put("subaccount", subaccount);
		}
		return MandrillUtil.query(rootUrl+ "rejects/list.json", 
				params, MandrillRejectsEntry[].class, httpClientFactory);
		
	}
	
	/**
	 * <p>Delete an email rejection. There is no limit to 
	 * how many rejections you can remove from your blacklist, 
	 * but keep in mind that each deletion has an affect on 
	 * your reputation.</p>
	 * @param email The email address that was removed from 
	 * the blacklist.
	 * @return Whether the address was deleted successfully.
	 * @throws MandrillApiError
	 * @throws IOException
	 */
	public Boolean delete(final String email) 
			throws MandrillApiError, IOException {
		
		return delete(email, null);
		
	}
	
	/**
	 * <p>Delete an email rejection. There is no limit to 
	 * how many rejections you can remove from your blacklist, 
	 * but keep in mind that each deletion has an affect on 
	 * your reputation.</p>
	 * @param email The email address that was removed from 
	 * the blacklist.
	 * @param subaccount An optional unique identifier for the 
	 * subaccount to limit the blacklist.
	 * @return Whether the address was deleted successfully.
	 * @throws MandrillApiError
	 * @throws IOException
	 */
	public Boolean delete(final String email, final String subaccount) 
			throws MandrillApiError, IOException {
		
		final HashMap<String,Object> params = MandrillUtil.paramsWithKey(key);
		params.put("email", email);
		if(subaccount != null) {
			params.put("subaccount", subaccount);
		}
		return MandrillUtil.query(rootUrl+ "rejects/delete.json", 
				params, MandrillHelperClasses.MandrillRejectsDeleted.class, httpClientFactory).getDeleted();
		
	}
	
}
