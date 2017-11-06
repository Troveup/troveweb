package com.troveup.brooklyn.sdk.realityserver.business;

import com.troveup.brooklyn.orm.renderqueue.model.Render;
import com.troveup.brooklyn.sdk.realityserver.api.Nimbix;
import com.troveup.brooklyn.sdk.realityserver.api.RealityServer;
import com.troveup.brooklyn.sdk.realityserver.model.NimbixStartDetails;
import com.troveup.brooklyn.sdk.realityserver.model.RealityServerInitialSubmitResponse;
import com.troveup.brooklyn.sdk.realityserver.model.RealityServerRenderRequest;
import com.troveup.brooklyn.sdk.realityserver.model.RealityServerStatusResponse;

import java.util.*;

/**
 * Created by tim on 6/21/15.
 */
public class RealityServerManager
{
    private final Nimbix nimbix;
    private final RealityServer realityServer;
    private final String postbackUrl;
    private final String ipAddress;
    private final String storageCdn;

    public static final String GOLD_POLISHED_MATERIAL_MAPPING = "gold_polished";
    public static final String PLATINUM_POLISHED_MATERIAL_MAPPING = "platinum_polished";
    public static final String WHITE_GOLD_POLISHED_MATERIAL_MAPPING = "white_gold_polished";
    public static final String PALLADIUM_POLISHED_MATERIAL_MAPPING = "palladium_polished";
    public static final String RED_GOLD_POLISHED_MATERIAL_MAPPING = "red_gold_polished";

    public RealityServerManager(Nimbix nimbix, RealityServer realityServer, String ipAddress, String storageCdn,
                                String postbackUrl)
    {
        this.nimbix = nimbix;
        this.realityServer = realityServer;
        this.ipAddress = ipAddress;
        this.storageCdn = storageCdn;
        this.postbackUrl = postbackUrl;
    }

    public NimbixStartDetails startNimbixServer()
    {
        return nimbix.startNimbixServer();
    }

    public void stopRealityServer(String nimbixJobId)
    {
        nimbix.stopNimbixServer(nimbixJobId);
    }

    public static Map<String, List<String>> getSceneCameraMap()
    {
        Map<String, List<String>> rval = new HashMap<>();

        //available trove_scene1 cameras
        List<String> cameraList = new ArrayList<>();
        //cameraList.add("default_camera");
        cameraList.add("camera1");
        cameraList.add("camera2");
        cameraList.add("camera3");
        cameraList.add("camera4");
        rval.put("default", cameraList);
        rval.put("trove_scene1", cameraList);
        rval.put("trove_scene_bar-ring", cameraList);
        rval.put("trove_scene_mirror-ring", cameraList);
        rval.put("trove_scene_general", cameraList);
        rval.put("trove_scene_parted-ring", cameraList);
        rval.put("trove_scene_mirror-no-hole-ring", cameraList);
        rval.put("trove_scene_warrior-ring", cameraList);
        rval.put("trove_scene_mirror-ring_v1", cameraList);

        return rval;
    }

    public RealityServerInitialSubmitResponse submitModelForRender(Render render)
    {
        Map<String, List<String>> sceneCameraMap = getSceneCameraMap();
        List<String> cameraMap;

        if (sceneCameraMap.get(render.getScene()) == null)
            cameraMap = sceneCameraMap.get("default");
        else
            cameraMap = sceneCameraMap.get(render.getScene());

        RealityServerRenderRequest request = new RealityServerRenderRequest(render.getJobId(), render.getUUID(),
                render.getRenderMaterial(), render.getModelUrl(), render.getScene(), cameraMap,
                "iray", storageCdn, postbackUrl, "400", "400");

        return realityServer.submitRender(ipAddress, request);
    }

    public RealityServer.REALITY_SERVER_STATUS startRealityServer()
    {
        RealityServer.REALITY_SERVER_STATUS rval = RealityServer.REALITY_SERVER_STATUS.RESPONSE_ERROR;
        RealityServerStatusResponse response = realityServer.startRealityServer(ipAddress);

        if (response != null)
            rval = convertStatusResponseToEnum(Integer.parseInt(response.getStatus()));

        return rval;
    }

    public RealityServer.REALITY_SERVER_STATUS stopRealityServer()
    {
        RealityServer.REALITY_SERVER_STATUS rval = RealityServer.REALITY_SERVER_STATUS.RESPONSE_ERROR;
        RealityServerStatusResponse response = realityServer.stopRealityServer(ipAddress);

        if (response != null)
            rval = convertStatusResponseToEnum(Integer.parseInt(response.getStatus()));

        return rval;
    }

    public RealityServer.REALITY_SERVER_STATUS getRealityServerStatus()
    {
        RealityServer.REALITY_SERVER_STATUS rval = RealityServer.REALITY_SERVER_STATUS.RESPONSE_ERROR;
        RealityServerStatusResponse response = realityServer.getRealityServerStatus(ipAddress);

        if (response != null)
            rval = convertStatusResponseToEnum(Integer.parseInt(response.getStatus()));

        return rval;
    }

    public static RealityServer.REALITY_SERVER_STATUS convertStatusResponseToEnum(Integer statusResponse)
    {
        RealityServer.REALITY_SERVER_STATUS[] values = RealityServer.REALITY_SERVER_STATUS.values();
        return values[statusResponse];
    }
}