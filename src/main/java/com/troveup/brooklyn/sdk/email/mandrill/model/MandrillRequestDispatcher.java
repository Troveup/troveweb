package com.troveup.brooklyn.sdk.email.mandrill.model;

import java.io.IOException;

import com.troveup.brooklyn.util.models.UrlResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rschreijer
 * @since Feb 21, 2013
 */
public final class MandrillRequestDispatcher {
	private static final Logger logger = LoggerFactory.getLogger(MandrillRequestDispatcher.class);


	/**
	 *
	 * @param requestModel
	 * @param <T>
	 * @return
	 * @throws MandrillApiError
	 * @throws IOException
	 */
	public static final <T> T execute(final RequestModel<T> requestModel) throws MandrillApiError, IOException
	{

	String responseInput = "";


		UrlResponse mandrillResponse = requestModel.getRequest().sendRequest();

		if (requestModel.validateResponseStatus(mandrillResponse.getResponseCode())) {
			try {
				for (String responsePartial : mandrillResponse.getResponseBody()) {
					responseInput += responsePartial;
				}
				return requestModel.handleResponse(responseInput);

			} catch (final HandleResponseException e) {
				throw new IOException(
						"Failed to parse response from request '"
								+ requestModel.getUrl() + "'", e);
			}

		} else {
			// ==> compile mandrill error!
			for (String responsePartial : mandrillResponse.getResponseBody()) {
				responseInput += responsePartial;
			}
			final MandrillApiError.MandrillError error = LutungGsonUtils.getGson()
					.fromJson(responseInput, MandrillApiError.MandrillError.class);
			throw new MandrillApiError(
					"Unexpected http status in response: "
							+ mandrillResponse.getResponseCode());

		}
	}

}
