/**
 * 
 */
package com.troveup.brooklyn.sdk.email.mandrill.api;

import java.util.Date;

import com.troveup.brooklyn.sdk.email.mandrill.api.StatsBucket.Stats;

/**
 * <p>A time series holds stats for a single hour.</p>
 * @author rschreijer
 * @since Mar 16, 2013
 */
public class MandrillTimeSeries extends Stats {
	private Date time;

	/**
	 * @return The hour for this time series.
	 */
	public Date getTime() {
		return time;
	}

}
