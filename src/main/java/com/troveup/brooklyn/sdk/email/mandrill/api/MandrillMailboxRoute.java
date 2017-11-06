/**
 * 
 */
package com.troveup.brooklyn.sdk.email.mandrill.api;

/**
 * <p>A mailbox route.</p>
 * @author rschreijer
 * @since Mar 19, 2013
 */
public class MandrillMailboxRoute {
	private String pattern, url;

	/**
	 * @return The search pattern that the mailbox 
	 * name should match.
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @return The webhook URL where inbound messages 
	 * will be published.
	 */
	public String getUrl() {
		return url;
	}

}
