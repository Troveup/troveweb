package com.troveup.brooklyn.tests.sdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.troveup.brooklyn.sdk.realityserver.api.Nimbix;
import com.troveup.brooklyn.sdk.realityserver.api.RealityServer;
import com.troveup.brooklyn.sdk.realityserver.model.*;
import com.troveup.brooklyn.util.StringUtils;
import com.troveup.brooklyn.util.models.UrlResponse;
import com.troveup.config.PersistenceConfig;
import com.troveup.config.SDKConfig;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

/**
 * Created by tim on 6/21/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, SDKConfig.class})
public class RealityServerTests
{
    @Autowired
    RealityServer realityServer;

    @Autowired
    Nimbix nimbix;

    Logger logger;

    @Before
    public void setUp()
    {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @Test
    public void testServerNotRunning()
    {
        Assert.assertTrue(nimbix.isNimbixStarted("abcd").equals(""));
    }

    @Test
    public void testIsServerRunning()
    {
        Assert.assertFalse(nimbix.isNimbixStarted("20150621011504-9353-nae_12c24-2m2090-trove_nimbix_s1").equals(""));
    }

    @Test
    public void testSubmitRender()
    {
        List<String> cameras = new ArrayList<>();
        cameras.add("camera1");
        RealityServerRenderRequest request = new RealityServerRenderRequest(UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), "gold_polished",
                "https://storage.googleapis.com/troveup-dev-cdn/render-45dd997e-b3af-47cb-8f16-34c28b2c35ae.obj",
                "trove_scene_warrior-ring", cameras, "irt", "troveup-imagestore",
                "https://project-troveup-dev.appspot.com/worker/realityserverpostback", "500", "500");

        NimbixStartDetails details = nimbix.startNimbixServer();

        logger.debug("IP Address: " + details.getIpAddress());

        RealityServerInitialSubmitResponse response = realityServer.submitRender(details.getIpAddress(), request);

        nimbix.stopNimbixServer(details.getJobId());

        Assert.assertNotNull(response);
        Assert.assertTrue(response.getErrorId() == 0);
        Assert.assertNotNull(response.getResponse().getJobId());
        Assert.assertNotNull(response.getResponse().getUUID());

    }

    @Test
    public void testStartServer()
    {
        NimbixStartDetails details = nimbix.startNimbixServer();
        Assert.assertNotNull(details.getIpAddress());
        Assert.assertNotNull(details.getJobId());
    }

    @Test
    public void testFullNimbixStartupShutdown()
    {
        NimbixStartDetails details = nimbix.startNimbixServer();

        Assert.assertNotNull(details.getIpAddress());
        Assert.assertNotNull(details.getJobId());
        Assert.assertTrue(nimbix.stopNimbixServer(details.getJobId()));
    }

    @Test
    public void lsTest()
    {

        final String NACC_API_KEY = "f9cd8c3697304e5b9efd76f9edbb4d5c3c32bbea";

        final String NACC_USERNAME = "trove_nimbix";

        final String NACC_SFTP_URL = "drop.nimbix.net";

        String rval = "";
        JSch jsch = new JSch();
        Session session = null;
        Channel channel = null;
        ChannelSftp sftpChannel = null;
        InputStream statusStream = null;
        String returnedFile;
        try {
            session = jsch.getSession(NACC_USERNAME, NACC_SFTP_URL, 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(NACC_API_KEY);
            session.connect();

            channel = session.openChannel("sftp");
            channel.connect();
            sftpChannel = (ChannelSftp) channel;
            statusStream = sftpChannel.get("/data/HelloNACC.input");



            Vector<ChannelSftp.LsEntry> list = sftpChannel.ls("/data");
            sftpChannel.cd("/data/");

            for (ChannelSftp.LsEntry entry : list)
            {
                if (entry.getLongname().contains("HelloNACC"))
                    statusStream = sftpChannel.get(entry.getFilename());
            }

            sftpChannel.exit();
            session.disconnect();

            returnedFile = StringUtils.converStreamToString(statusStream);

            if (returnedFile.contains("NAE Address:"))
            {
                //Result file is assumed to be of this format:
                //NAE Address: 8.39.209.158
                //NAE root/nimbix Password: laoEnlight21

                //Grab the IP
                String resultStringSplit[] = returnedFile.split(" ");
                rval = resultStringSplit[1];
            }

            statusStream.close();

            Assert.assertFalse(rval.equals(""));

        } catch (Exception e)
        {
            Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Stack Trace: " + sw.toString());
        }
        finally
        {
            if (session != null && session.isConnected())
                session.disconnect();

            if (channel != null && channel.isConnected())
                channel.disconnect();

            if (sftpChannel != null && sftpChannel.isConnected())
                sftpChannel.disconnect();

            if (statusStream != null)
                try {
                    statusStream.close();
                } catch (IOException e) {
                    //eat the error, all we're trying to do is close
                }
        }
    }

    @Test
    public void testRenderQueue()
    {
        List<String> cameras = new ArrayList<>();
        cameras.add("camera1");
        RealityServerRenderRequest request = new RealityServerRenderRequest(UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), "gold_polished",
                "https://storage.googleapis.com/troveup-dev-cdn/render-45f2900d-0bd4-498f-86ba-e91adc6db8ad.obj",
                "trove_scene_parted-ring", cameras, "irt", "troveup-imagestore",
                "https://project-troveup-qa.appspot.com/worker/realityserverpostback", "500", "500");

        RealityServerInitialSubmitResponse response = realityServer.submitRender("8.39.209.156", request);

        Assert.assertNotNull(response);

    }

    @Test
    public void realityServerResponseDeserializerTest()
    {
        /*String sampleResponse = "{\"errorId\":0,\"errorMessage\":\"OK\",\"cameras\":{\"camera1\":{\"url\":\"" +
                "http://troveup-imagestore.storage.googleapis.com/render-a4738966-e6cd-45fa-ae84-80166b0bfbba-25aa2d36-" +
                "89ad-4086-a341-d76af12a3ecc-camera1.png\",\"errorId\":0,\"errorMessage\":\"OK\",\"filename\":\"" +
                "render-a4738966-e6cd-45fa-ae84-80166b0bfbba-25aa2d36-89ad-4086-a341-d76af12a3ecc-camera1.png\"}," +
                "\"camera2\":{\"url\":\"http://troveup-imagestore.storage.googleapis.com/render-a4738966-e6cd-45fa-" +
                "ae84-80166b0bfbba-25aa2d36-89ad-4086-a341-d76af12a3ecc-camera2.png\",\"errorId\":0," +
                "\"errorMessage\":\"OK\",\"filename\":\"render-a4738966-e6cd-45fa-ae84-80166b0bfbba-25aa2d36-89ad-4086" +
                "-a341-d76af12a3ecc-camera2.png\"},\"camera3\":{\"url\":\"http://troveup-imagestore.storage." +
                "googleapis.com/render-a4738966-e6cd-45fa-ae84-80166b0bfbba-25aa2d36-89ad-4086-a341-" +
                "d76af12a3ecc-camera3.png\",\"errorId\":0,\"errorMessage\":\"OK\",\"filename\":\"render-a4738966-" +
                "e6cd-45fa-ae84-80166b0bfbba-25aa2d36-89ad-4086-a341-d76af12a3ecc-camera3.png\"}," +
                "\"camera4\":{\"url\":\"\",\"errorId\":-3,\"errorMessage\":\"Rendered image upload failure.\"," +
                "\"errorData\":{\"code\":\"ETIMEDOUT\",\"errno\":\"ETIMEDOUT\",\"syscall\":\"connect\"}}}," +
                "\"jobId\":\"25aa2d36-89ad-4086-a341-d76af12a3ecc\",\"UUID\":\"a4738966-e6cd-45fa-ae84-80166b0bfbba\"," +
                "\"request\":{\"max_samples\":200,\"max_time\":50,\"quality\":1,\"converged_pixel_ratio\":0.95," +
                "\"jobId\":\"25aa2d36-89ad-4086-a341-d76af12a3ecc\",\"UUID\":\"a4738966-e6cd-45fa-ae84-80166b0bfbba\"," +
                "\"material\":\"red_gold_polished\",\"modelUrl\":\"https://storage.googleapis.com/troveup-" +
                "production-cdn/render-a4738966-e6cd-45fa-ae84-80166b0bfbba.obj\",\"scene\":\"trove_scene_bar-ring\"," +
                "\"sceneCameras\":[\"camera1\",\"camera2\",\"camera3\",\"camera4\"],\"renderer\":\"iray\"," +
                "\"cloudStorageBucket\":\"troveup-imagestore\",\"postBackUrl\":\"https://www.troveup.com/worker/" +
                "realityserverpostback\",\"width\":\"400\",\"height\":\"400\"}}";*/

        //Old version
        //String sampleResponse = "{\"errorId\":0,\"errorMessage\":\"OK\",\"cameras\":{\"camera1\":{\"url\":\"http://troveup-imagestore.storage.googleapis.com/render-35ba2832-8bd5-4bfb-9608-bc1c38a545ff-bc5c4355-7879-40cc-bbec-1c226a5c8401-camera1.png\",\"errorId\":0,\"errorMessage\":\"OK\",\"filename\":\"render-35ba2832-8bd5-4bfb-9608-bc1c38a545ff-bc5c4355-7879-40cc-bbec-1c226a5c8401-camera1.png\"},\"camera2\":{\"url\":\"http://troveup-imagestore.storage.googleapis.com/render-35ba2832-8bd5-4bfb-9608-bc1c38a545ff-bc5c4355-7879-40cc-bbec-1c226a5c8401-camera2.png\",\"errorId\":0,\"errorMessage\":\"OK\",\"filename\":\"render-35ba2832-8bd5-4bfb-9608-bc1c38a545ff-bc5c4355-7879-40cc-bbec-1c226a5c8401-camera2.png\"},\"camera3\":{\"url\":\"http://troveup-imagestore.storage.googleapis.com/render-35ba2832-8bd5-4bfb-9608-bc1c38a545ff-bc5c4355-7879-40cc-bbec-1c226a5c8401-camera3.png\",\"errorId\":0,\"errorMessage\":\"OK\",\"filename\":\"render-35ba2832-8bd5-4bfb-9608-bc1c38a545ff-bc5c4355-7879-40cc-bbec-1c226a5c8401-camera3.png\"},\"camera4\":{\"url\":\"\",\"errorId\":-3,\"errorMessage\":\"Rendered image upload failure.\",\"errorData\":{\"code\":\"ETIMEDOUT\",\"errno\":\"ETIMEDOUT\",\"syscall\":\"connect\"}}},\"jobId\":\"bc5c4355-7879-40cc-bbec-1c226a5c8401\",\"UUID\":\"35ba2832-8bd5-4bfb-9608-bc1c38a545ff\",\"request\":{\"max_samples\":200,\"max_time\":50,\"quality\":1,\"converged_pixel_ratio\":0.95,\"jobId\":\"bc5c4355-7879-40cc-bbec-1c226a5c8401\",\"UUID\":\"35ba2832-8bd5-4bfb-9608-bc1c38a545ff\",\"material\":\"white_gold_polished\",\"modelUrl\":\"https://storage.googleapis.com/troveup-dev-cdn/render-35ba2832-8bd5-4bfb-9608-bc1c38a545ff.obj\",\"scene\":\"trove_scene_parted-ring\",\"sceneCameras\":[\"camera1\",\"camera2\",\"camera3\",\"camera4\"],\"renderer\":\"iray\",\"cloudStorageBucket\":\"troveup-imagestore\",\"postBackUrl\":\"https://project-troveup-dev.appspot.com/worker/realityserverpostback\",\"width\":\"400\",\"height\":\"400\"}}";

        //New version
        String sampleResponse = "{\"errorId\":0,\"errorMessage\":\"OK\",\"cameras\":{\"camera1\":{\"url\":\"http://troveup-imagestore.storage.googleapis.com/render-9ddeb59f-2f5d-4f91-90b8-228a0932a499-6F692B7-camera1.png\",\"errorId\":0,\"errorMessage\":\"OK\",\"filename\":\"render-9ddeb59f-2f5d-4f91-90b8-228a0932a499-6F692B7-camera1.png\"},\"camera2\":{\"url\":\"http://troveup-imagestore.storage.googleapis.com/render-9ddeb59f-2f5d-4f91-90b8-228a0932a499-6F692B7-camera2.png\",\"errorId\":0,\"errorMessage\":\"OK\",\"filename\":\"render-9ddeb59f-2f5d-4f91-90b8-228a0932a499-6F692B7-camera2.png\"},\"camera3\":{\"url\":\"http://troveup-imagestore.storage.googleapis.com/render-9ddeb59f-2f5d-4f91-90b8-228a0932a499-6F692B7-camera3.png\",\"errorId\":0,\"errorMessage\":\"OK\",\"filename\":\"render-9ddeb59f-2f5d-4f91-90b8-228a0932a499-6F692B7-camera3.png\"},\"camera4\":{\"url\":\"http://troveup-imagestore.storage.googleapis.com/render-9ddeb59f-2f5d-4f91-90b8-228a0932a499-6F692B7-camera4.png\",\"errorId\":0,\"errorMessage\":\"OK\",\"filename\":\"render-9ddeb59f-2f5d-4f91-90b8-228a0932a499-6F692B7-camera4.png\"}},\"jobId\":\"6F692B7\",\"UUID\":\"9ddeb59f-2f5d-4f91-90b8-228a0932a499\",\"request\":{\"max_samples\":200,\"max_time\":50,\"quality\":1,\"converged_pixel_ratio\":0.95,\"modelTransform\":{\"xx\":1,\"xy\":0,\"xz\":0,\"xw\":0,\"yx\":0,\"yy\":1,\"yz\":0,\"yw\":0,\"zx\":0,\"zy\":0,\"zz\":1,\"zw\":0,\"wx\":0,\"wy\":0,\"wz\":0,\"ww\":1},\"jobId\":\"6F692B7\",\"UUID\":\"9ddeb59f-2f5d-4f91-90b8-228a0932a499\",\"material\":\"gold_polished\",\"modelUrl\":\"https://storage.googleapis.com/trove-qa-teststore/render-9ddeb59f-2f5d-4f91-90b8-228a0932a499.obj\",\"scene\":\"trove_scene_parted-ring\",\"sceneCameras\":[{\"name\":\"camera1\",\"baseCamera\":\"camera1\"},{\"name\":\"camera2\",\"baseCamera\":\"camera2\"},{\"name\":\"camera3\",\"baseCamera\":\"camera3\"},{\"name\":\"camera4\",\"baseCamera\":\"camera4\"}],\"renderer\":\"iray\",\"cloudStorageBucket\":\"troveup-imagestore\",\"postBackUrl\":\"https://project-troveup-qa.appspot.com/worker/realityserverpostback\",\"width\":\"400\",\"height\":\"400\"}}";
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(RealityServerPostbackResponse.class,
                new RealityServerPostbackResponseDeserializer());

        Gson gson = builder.create();

        RealityServerPostbackResponse response = gson.fromJson(sampleResponse, RealityServerPostbackResponse.class);

        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getCameras());
        Assert.assertNotNull(response.getJobId());
        Assert.assertNotNull(response.getUUID());
        Assert.assertTrue(response.getCameras().size() > 0);
    }


}
