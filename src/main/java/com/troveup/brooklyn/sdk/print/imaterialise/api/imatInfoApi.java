package com.troveup.brooklyn.sdk.print.imaterialise.api;

import com.troveup.brooklyn.sdk.print.imaterialise.model.iMatMaterialsResponse;
import com.troveup.brooklyn.sdk.http.impl.HttpClient;
import com.troveup.brooklyn.util.models.UrlResponse;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by tim on 5/26/15.
 */
public class imatInfoApi extends imatCommonApi
{
    String baseApiUrl;
    String materialsApiUrlFragment;
    String registeredEmail;

    public imatInfoApi(String baseApiUrl, String materialsApiUrlFragment, String registeredEmail)
    {
        this.baseApiUrl = baseApiUrl;
        this.materialsApiUrlFragment = materialsApiUrlFragment;
        this.registeredEmail = registeredEmail;
    }

    public iMatMaterialsResponse getAvailableMaterials()
    {
        HttpClient client = httpClientFactory.getHttpClientInstance();

        iMatMaterialsResponse rval = null;

        String materialsRequestUrl = baseApiUrl + materialsApiUrlFragment.replace("<your registered email address here>", registeredEmail);

        client.configureForStandardRequest(materialsRequestUrl, null, HttpClient.REQUEST_METHOD.GET, null, null);

        try
        {
            UrlResponse response = client.sendRequest();

            if (response != null && response.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                String concatString = "";

                for (String singleString : response.getResponseBody())
                    concatString += singleString;

                rval = gson.fromJson(concatString, iMatMaterialsResponse.class);
            }
        } catch (IOException e)
        {
            logError(e);
        }

        return rval;
    }

}
