/**
 * 
 */
package com.troveup.brooklyn.sdk.email.mandrill.api;

import com.troveup.brooklyn.sdk.email.mandrill.api.StatsBucket.Stats;

/**
 * <p>A URL and its statistics.</p>
 * @author rschreijer
 * @since Mar 18, 2013
 */
public class MandrillUrl extends Stats {
	private String url;

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

}
