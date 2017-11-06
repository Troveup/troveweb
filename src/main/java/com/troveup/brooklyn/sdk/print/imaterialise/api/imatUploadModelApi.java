package com.troveup.brooklyn.sdk.print.imaterialise.api;

import com.troveup.brooklyn.sdk.http.impl.HttpClient;
import com.troveup.brooklyn.sdk.print.imaterialise.model.iMatUploadModelResponse;
import com.troveup.brooklyn.util.models.UrlResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;

/**
 * Created by tim on 5/22/15.
 */
public class imatUploadModelApi extends imatCommonApi
{

    private String iMaterialiseUploadApiUrl;

    public imatUploadModelApi(String iMaterialiseUploadApiUrl)
    {
        this.iMaterialiseUploadApiUrl = iMaterialiseUploadApiUrl;
    }

    public enum MODEL_UPLOAD_STATUS
    {
        SUCCEEDED("succeeded"),
        NEEDED_FIXING("neededFixing"),
        STILL_NEEDS_FIXING("stillNeedsFixing"),
        BACK_END_EXCEPTION("backEndException");

        private final String text;

        MODEL_UPLOAD_STATUS(String type) {
            this.text = type;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    /**
     * Instructs iMaterialise to retrieve a model from Trove's CDN.
     *
     * @param modelFileFullUrl URL of the file residing on the CDN.
     * @return Boolean success state of the operation.
     */
    public iMatUploadModelResponse uploadModel(String modelFileFullUrl)
    {
        HttpClient client = httpClientFactory.getHttpClientInstance();

        iMatUploadModelResponse rval = null;

        HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put("fileUrl", modelFileFullUrl);
        requestParams.put("fileUnits", "mm");

        client.configureForMultipart(iMaterialiseUploadApiUrl, null, requestParams, null, null, null);
        try {

            UrlResponse response = client.sendMultipartRequest();

            if (response != null && response.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                String concatString = "";

                for (String singleString : response.getResponseBody())
                    concatString += singleString;

                rval = gson.fromJson(concatString, iMatUploadModelResponse.class);
            }

        } catch (IOException e) {
            logError(e);
        }

        return rval;
    }
}
