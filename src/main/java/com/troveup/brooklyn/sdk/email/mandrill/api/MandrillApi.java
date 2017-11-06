package com.troveup.brooklyn.sdk.email.mandrill.api;

import com.troveup.brooklyn.sdk.http.interfaces.IHttpClientFactory;

/**
 * @author rschreijer
 * @since Mar 17, 2013
 */
public class MandrillApi
{
	IHttpClientFactory httpClientFactory;

	public static final String API_KEY = "-Likoi7MhDTa-WC2yHhuCQ";

	private String key;
	private final MandrillUsersApi users;
	private final MandrillMessagesApi messages;
	private final MandrillTagsApi tags;
	private final MandrillRejectsApi rejects;
	private final MandrillWhitelistsApi whitelists;
	private final MandrillSendersApi senders;
	private final MandrillUrlsApi urls;
	private final MandrillTemplatesApi templates;
	private final MandrillWebhooksApi webhooks;
	private final MandrillSubaccountsApi subaccounts;
	private final MandrillInboundApi inbound;
	private final MandrillExportsApi exports;
	private final MandrillIpsApi ips;
	
	public MandrillApi(final String key, IHttpClientFactory httpClientFactory) {
		if(key == null) {
			throw new NullPointerException(
					"'key' is null; please provide Mandrill API key");
		}
		this.key = key;
		this.httpClientFactory = httpClientFactory;
		users = new MandrillUsersApi(key, httpClientFactory);
		messages = new MandrillMessagesApi(key, httpClientFactory);
		tags = new MandrillTagsApi(key, httpClientFactory);
		rejects = new MandrillRejectsApi(key, httpClientFactory);
		whitelists = new MandrillWhitelistsApi(key, httpClientFactory);
		senders = new MandrillSendersApi(key, httpClientFactory);
		urls = new MandrillUrlsApi(key, httpClientFactory);
		templates = new MandrillTemplatesApi(key, httpClientFactory);
		webhooks = new MandrillWebhooksApi(key, httpClientFactory);
		subaccounts = new MandrillSubaccountsApi(key, httpClientFactory);
		inbound = new MandrillInboundApi(key, httpClientFactory);
		exports = new MandrillExportsApi(key, httpClientFactory);
		ips = new MandrillIpsApi(key, httpClientFactory);
	}

	/**
	 * @return Your Mandrill API key.
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * <p>Get access to 'users' calls.</p>
	 * @return An object with access to user calls.
	 */
	public MandrillUsersApi users() {
		return users;
	}
	
	public MandrillMessagesApi messages() {
		return messages;
	}
	
	public MandrillTagsApi tags() {
		return tags;
	}
	
	public MandrillRejectsApi rejects() {
		return rejects;
	}
	
	public MandrillWhitelistsApi whitelists() {
		return whitelists;
	}
	
	public MandrillSendersApi senders() {
		return senders;
	}
	
	public MandrillUrlsApi urls() {
		return urls;
	}
	
	public MandrillTemplatesApi templates() {
		return templates;
	}
	
	public MandrillWebhooksApi webhooks() {
		return webhooks;
	}
	
	public MandrillSubaccountsApi subaccounts() {
		return subaccounts;
	}
	
	public MandrillInboundApi inbound() {
		return inbound;
	}
	
	public MandrillExportsApi exports() {
		return exports;
	}
	
	public MandrillIpsApi ips() {
		return ips;
	}
	
}
