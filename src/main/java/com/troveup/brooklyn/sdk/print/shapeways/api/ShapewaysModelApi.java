package com.troveup.brooklyn.sdk.print.shapeways.api;

import com.google.gson.Gson;
import com.troveup.brooklyn.sdk.print.shapeways.exception.ShapewaysModelFailureException;
import com.troveup.brooklyn.sdk.print.shapeways.model.ShapewaysGetModelResponse;
import com.troveup.brooklyn.sdk.print.shapeways.model.ShapewaysUploadRequest;
import com.troveup.brooklyn.sdk.print.shapeways.model.ShapewaysUploadResponse;
import com.troveup.brooklyn.util.StringUtils;
import org.scribe.model.Response;
import org.scribe.model.Verb;

import java.io.IOException;

/**
 * Created by tim on 7/1/15.
 */
public class ShapewaysModelApi extends ShapewaysCommonApi
{
    public ShapewaysModelApi(ShapewaysRequestFactory requestFactory, Gson gson)
    {
        super(requestFactory, gson);
    }

    public ShapewaysUploadResponse uploadModel(String base64EncodedModel, String uniqueFileName) throws IOException, ShapewaysModelFailureException {
        ShapewaysUploadRequest data = new ShapewaysUploadRequest(base64EncodedModel, uniqueFileName);
        String requestUrl = ShapewaysRequestFactory.SHAPEWAYS_BASE_API_URL +
                ShapewaysRequestFactory.SHAPEWAYS_UPLOAD_MODEL_URL_FRAGMENT;

        ShapewaysRequest request = requestFactory.getShapewaysRequest(Verb.POST, requestUrl);

        String jsonPayload = gson.toJson(data);

        request.setJsonPayload(jsonPayload);

        Response response = request.sendRequest();

        if (response.isSuccessful()) {
            return gson.fromJson(StringUtils.converStreamToString(response.getStream()),
                    ShapewaysUploadResponse.class);
        }
        else
        {
            throw new ShapewaysModelFailureException("Failed to upload model to Shapeways.  Request JSON body was " +
            jsonPayload + ".  Response message was " + StringUtils.converStreamToString(response.getStream()));
        }
    }

    public ShapewaysGetModelResponse getModelStatus(String modelId) throws IOException, ShapewaysModelFailureException {
        String requestUrl = (ShapewaysRequestFactory.SHAPEWAYS_BASE_API_URL +
                ShapewaysRequestFactory.SHAPEWAYS_GET_MODEL_URL_FRAGMENT).replace("<modelId>", modelId);

        ShapewaysRequest request = requestFactory.getShapewaysRequest(Verb.GET, requestUrl);

        Response response = request.sendRequest();

        if (response.isSuccessful())
            return gson.fromJson(StringUtils.converStreamToString(response.getStream()),
                    ShapewaysGetModelResponse.class);
        else
        {
            throw new ShapewaysModelFailureException("Failed to get model status for model identifier " + modelId + "." +
                    "  Request URL was " + requestUrl + " and response returned with message " +
                    StringUtils.converStreamToString(response.getStream()));
        }
    }
}
