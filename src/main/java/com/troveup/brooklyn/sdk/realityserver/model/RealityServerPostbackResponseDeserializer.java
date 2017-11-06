package com.troveup.brooklyn.sdk.realityserver.model;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 7/27/15.
 */
public class RealityServerPostbackResponseDeserializer implements JsonDeserializer<RealityServerPostbackResponse>
{
    @Override
    public RealityServerPostbackResponse deserialize(JsonElement jsonElement,
                                                     Type type, JsonDeserializationContext
                                                                 jsonDeserializationContext) throws JsonParseException
    {
        RealityServerPostbackResponse rval = new RealityServerPostbackResponse();
        rval.setCameras(new ArrayList<RealityServerCamera>());
        List<String> sceneCameras = new ArrayList<>();
        List<RealityServerSceneCameraResponseList> sceneCamerasV2 = new ArrayList<>();

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        if (jsonObject.get("errorId") != null)
            rval.setErrorId(jsonObject.get("errorId").getAsInt());

        if (jsonObject.get("errorMessage") != null)
            rval.setErrorMessage(jsonObject.get("errorMessage").getAsString());

        if (jsonObject.get("UUID") != null)
            rval.setUUID(jsonObject.get("UUID").getAsString());

        if (jsonObject.get("jobId") != null)
            rval.setJobId(jsonObject.get("jobId").getAsString());

        JsonObject request = jsonObject.getAsJsonObject("request");

        if (request != null) {
            RealityServerRenderRequest renderRequest = new RealityServerRenderRequest();

            if (request.get("max_samples") != null)
                renderRequest.setMax_samples(request.get("max_samples").getAsInt());

            if (request.get("max_time") != null)
                renderRequest.setMax_time(request.get("max_time").getAsInt());

            if (request.get("quality") != null)
                renderRequest.setQuality(request.get("quality").getAsInt());

            if (request.get("converged_pixel_ratio") != null)
                renderRequest.setConverged_pixel_ratio(request.get("converged_pixel_ratio").getAsFloat());

            if (request.get("jobId") != null)
                renderRequest.setJobId(request.get("jobId").getAsString());

            if (request.get("UUID") != null)
                renderRequest.setUUID(request.get("UUID").getAsString());

            if (request.get("material") != null)
                renderRequest.setMaterial(request.get("material").getAsString());

            if (request.get("modelUrl") != null)
                renderRequest.setModelUrl(request.get("modelUrl").getAsString());

            if (request.get("scene") != null)
                renderRequest.setScene(request.get("scene").getAsString());

            if (request.get("sceneCameras") != null) //&& request.get("sceneCameras").getAsJsonArray().size() > 0)
            {
                JsonArray cameras = request.get("sceneCameras").getAsJsonArray();

                if (cameras.size() > 0) {
                    //Set up the scene cameras array
                    for (JsonElement camera : cameras) {
                        JsonObject cameraObject = camera.getAsJsonObject();
                        sceneCameras.add(cameraObject.get("name").getAsString());
                        sceneCamerasV2.add(new RealityServerSceneCameraResponseList(cameraObject.get("name").getAsString(), cameraObject.get("baseCamera").getAsString()));
                    }
                }

                renderRequest.setSceneCameras(sceneCameras);
                renderRequest.setSceneCamerasV2(sceneCamerasV2);
            }

            if (request.get("renderer") != null)
                renderRequest.setRenderer(request.get("renderer").getAsString());

            if (request.get("cloudStorageBucket") != null)
                renderRequest.setCloudStorageBucket(request.get("cloudStorageBucket").getAsString());

            if (request.get("postBackUrl") != null)
                renderRequest.setPostBackUrl(request.get("postBackUrl").getAsString());

            if (request.get("width") != null)
                renderRequest.setWidth(request.get("width").getAsString());

            if (request.get("height") != null)
                renderRequest.setHeight(request.get("height").getAsString());

            rval.setRequest(renderRequest);
        }

        if (sceneCameras.size() > 0) {
            JsonObject jsonCameras = jsonObject.get("cameras").getAsJsonObject();
            for (String camera : sceneCameras)
            {
                if (jsonCameras.get(camera) != null)
                {
                    JsonObject cameraObject = jsonCameras.get(camera).getAsJsonObject();

                    String url = null;
                    Integer errorId = null;
                    String errorMessage = null;
                    String filename = null;

                    if (cameraObject.get("url") != null)
                        url = cameraObject.get("url").getAsString();

                    if (cameraObject.get("errorId") != null)
                        errorId = cameraObject.get("errorId").getAsInt();

                    if (cameraObject.get("errorMessage") != null)
                        errorMessage = cameraObject.get("errorMessage").getAsString();

                    if (cameraObject.get("filename") != null)
                        filename = cameraObject.get("filename").getAsString();

                    RealityServerCamera realityServerCamera = new RealityServerCamera();
                    realityServerCamera.setUrl(url);
                    realityServerCamera.setErrorId(errorId);
                    realityServerCamera.setErrorMessage(errorMessage);
                    realityServerCamera.setFilename(filename);

                    rval.getCameras().add(realityServerCamera);
                }

            }
        }

        return rval;
    }
}
