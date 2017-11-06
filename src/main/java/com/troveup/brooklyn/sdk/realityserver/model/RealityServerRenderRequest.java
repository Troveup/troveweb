package com.troveup.brooklyn.sdk.realityserver.model;

import java.util.List;

/**
 * Created by tim on 6/21/15.
 */
public class RealityServerRenderRequest
{
    private String jobId;
    private String UUID;
    private String material;
    private String modelUrl;
    private String scene;
    private List<String> sceneCameras;
    private List<RealityServerSceneCameraResponseList> sceneCamerasV2;
    private String renderer;
    private String cloudStorageBucket;
    private String postBackUrl;
    private String width;
    private String height;
    private int max_time;
    private int max_samples;
    private int quality;
    private float converged_pixel_ratio;

    public RealityServerRenderRequest()
    {

    }

    public RealityServerRenderRequest(String jobId, String UUID, String material, String modelUrl, String scene,
                                      List<String> sceneCameras, String renderer, String cloudStorageBucket,
                                      String postBackUrl, String width, String height)
    {
        this.jobId = jobId;
        this.UUID = UUID;
        this.material = material;
        this.modelUrl = modelUrl;
        this.scene = scene;
        this.sceneCameras = sceneCameras;
        this.renderer = renderer;
        this.cloudStorageBucket = cloudStorageBucket;
        this.postBackUrl = postBackUrl;
        this.width = width;
        this.height = height;
        this.max_time = 50;
        this.max_samples = 200;
        this.quality = 1;
        this.converged_pixel_ratio = .95f;
    }

    public RealityServerRenderRequest(String jobId, String UUID, String material, String modelUrl, String scene,
                                      List<String> sceneCameras, String renderer, String cloudStorageBucket,
                                      String postBackUrl, String width, String height, int max_time, int max_samples,
                                      int quality, float converged_pixel_ratio)
    {
        this.jobId = jobId;
        this.UUID = UUID;
        this.material = material;
        this.modelUrl = modelUrl;
        this.scene = scene;
        this.sceneCameras = sceneCameras;
        this.renderer = renderer;
        this.cloudStorageBucket = cloudStorageBucket;
        this.postBackUrl = postBackUrl;
        this.width = width;
        this.height = height;
        this.max_time = max_time;
        this.max_samples = max_samples;
        this.quality = quality;
        this.converged_pixel_ratio = converged_pixel_ratio;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getModelUrl() {
        return modelUrl;
    }

    public void setModelUrl(String modelUrl) {
        this.modelUrl = modelUrl;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public List<String> getSceneCameras() {
        return sceneCameras;
    }

    public void setSceneCameras(List<String> sceneCameras) {
        this.sceneCameras = sceneCameras;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public String getCloudStorageBucket() {
        return cloudStorageBucket;
    }

    public void setCloudStorageBucket(String cloudStorageBucket) {
        this.cloudStorageBucket = cloudStorageBucket;
    }

    public String getPostBackUrl() {
        return postBackUrl;
    }

    public void setPostBackUrl(String postBackUrl) {
        this.postBackUrl = postBackUrl;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public int getMax_time() {
        return max_time;
    }

    public void setMax_time(int max_time) {
        this.max_time = max_time;
    }

    public int getMax_samples() {
        return max_samples;
    }

    public void setMax_samples(int max_samples) {
        this.max_samples = max_samples;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public float getConverged_pixel_ratio() {
        return converged_pixel_ratio;
    }

    public void setConverged_pixel_ratio(float converged_pixel_ratio) {
        this.converged_pixel_ratio = converged_pixel_ratio;
    }

    public List<RealityServerSceneCameraResponseList> getSceneCamerasV2() {
        return sceneCamerasV2;
    }

    public void setSceneCamerasV2(List<RealityServerSceneCameraResponseList> sceneCamerasV2) {
        this.sceneCamerasV2 = sceneCamerasV2;
    }
}
