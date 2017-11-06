package com.troveup.brooklyn.sdk.email.mandrill.api;

import com.troveup.brooklyn.sdk.email.mandrill.model.MandrillApiError;
import com.troveup.brooklyn.sdk.email.mandrill.model.MandrillHelperClasses;
import com.troveup.brooklyn.sdk.http.interfaces.IHttpClientFactory;

import java.io.IOException;
import java.util.HashMap;

public class MandrillWhitelistsApi extends MandrillApiCommon {
	private static final String rootUrl = MandrillUtil.rootUrl;
	private final String key;
	
	public MandrillWhitelistsApi(final String key, IHttpClientFactory httpClientFactory)
	{
		super(httpClientFactory);
		this.key = key;
	}
	
	/**
	 * <p>Adds an email to your email rejection whitelist. If the 
	 * address is currently on your blacklist, that blacklist 
	 * entry will be removed automatically.</p>
	 * @param email An email address to add to the whitelist.
	 * @return If the operation succeeded.
	 * @throws MandrillApiError
	 * @throws IOException
	 */
	public Boolean add(final String email) 
			throws MandrillApiError, IOException {
		
		final HashMap<String,Object> params = MandrillUtil.paramsWithKey(key);
		params.put("email", email);
		return MandrillUtil.query(rootUrl+ "whitelists/add.json", 
				params, WhitelistsAddResponse.class, httpClientFactory).getWhether();
		
	}
	
	/**
	 * <p>Retrieves your email rejection whitelist. You can provide 
	 * an email address or search prefix to limit the results. 
	 * Returns up to 1000 results.</p>
	 * @param email An optional email address or prefix to search by.
	 * @return Information for each whitelist entry.
	 * @throws MandrillApiError
	 * @throws IOException
	 */
	public MandrillWhitelistEntry[] list(final String email) 
			throws MandrillApiError, IOException {
		
		final HashMap<String,Object> params = MandrillUtil.paramsWithKey(key);
		params.put("email", email);
		return MandrillUtil.query(rootUrl+ "whitelists/list.json", 
				params, MandrillWhitelistEntry[].class, httpClientFactory);
		
	}
	
	/**
	 * <p>Removes an email address from the whitelist.</p>
	 * @param email The email address to remove from the whitelist.
	 * @return Whether the address was deleted successfully.
	 * @throws MandrillApiError
	 * @throws IOException
	 */
	public Boolean delete(final String email) 
			throws MandrillApiError, IOException {
		
		final HashMap<String,Object> params = MandrillUtil.paramsWithKey(key);
		params.put("email", email);
		return MandrillUtil.query(rootUrl+ "whitelists/delete.json", 
				params, WhitelistsDeleteResponse.class, httpClientFactory).getDeleted();
		
	}
	
	public static class WhitelistsAddResponse extends MandrillHelperClasses.EmailClass {
		private Boolean whether;
		public Boolean getWhether() {
			return whether;
		}
	}
	
	public static class WhitelistsDeleteResponse {
		private Boolean deleted;
		public Boolean getDeleted() {
			return deleted;
		}
	}

}
