package com.troveup.brooklyn.sdk.realityserver.api;

import com.google.gson.Gson;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.troveup.brooklyn.sdk.http.impl.HttpClient;
import com.troveup.brooklyn.sdk.http.interfaces.IHttpClientFactory;
import com.troveup.brooklyn.sdk.realityserver.model.NimbixStartDetails;
import com.troveup.brooklyn.sdk.realityserver.model.NimbixStartResponse;
import com.troveup.brooklyn.sdk.realityserver.model.NimbixStatusRequest;
import com.troveup.brooklyn.util.StringUtils;
import com.troveup.brooklyn.util.models.UrlResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.util.HashMap;

/**
 * Created by tim on 6/20/15.
 */
public class Nimbix
{
    //Pulled from the console when doing a manual startup.  See also nimbix_api_startup_shutdown.txt in this package.
    //https://nacc.nimbix.net/landing/ -> Select Last Used App (NAE_12C24-2M2090) -> Start -> Continue ->
    //Information in the popup.  This doesn't change, so building an object to deserialize doesn't seem warranted
    //quite yet.  In addition, I'm not sure what empty items are required to show up, so gson would have to be
    //reconfigured to keep empty items around for just this case.  Not worth it at this point.  If it becomes
    //a pain point, it can be changed later.
    public final String NIMBIX_STARTUP_JSON = "{\"files\":[],\"application\":{\"parameters\":{\"USER_NAE\":\"" +
            "TIGS_RealityServer\",\"sub-commands\":{}},\"name\":\"nae_12c24-2m2090\",\"command\":\"start\"}," +
            "\"customer\":{\"username\":\"trove_nimbix\",\"email\":\"brian@troveprint.com\",\"" +
            "api-key\":\"f9cd8c3697304e5b9efd76f9edbb4d5c3c32bbea\",\"notifications\":" +
            "{\"email\":{\"api@troveup.com\":{\"messages\":{\"JobSubmitted\":\"yes\",\"FileDelivered\":\"no\"," +
            "\"JobStarted\":\"yes\",\"JobEnded\":\"yes\",\"TransferStart\":\"yes\"}}}}},\"api-version\":\"2.1\"}";

    //Pulled from https://github.com/nimbix/nacc_cli/blob/master/nacc.cli
    public final String ENDPOINT_BASE_URL = "https://api.nimbix.net:4081/nimbix/";
    public final String ENDPOINT_START_SERVER_URL_FRAGMENT = "nacc_upload21";
    public final String ENDPOINT_JOBSTATUS_SERVER_URL_FRAGMENT = "nacc_jstatus";
    public final String ENDPOINT_DELETEJOB_SERVER_URL_FRAGMENT =
            "nacc_jobDel?user=<USER>&apikey=<APIKEY>&jobnumber=<JOBNUMBER>";

    public final String NACC_API_KEY = "f9cd8c3697304e5b9efd76f9edbb4d5c3c32bbea";

    public final String NACC_USERNAME = "trove_nimbix";

    public final String NACC_SFTP_URL = "drop.nimbix.net";

    public final String TROVE_NIMBIX_USERNAME = "trove_nimbix";

    Logger logger;

    private final Gson gson;

    private final IHttpClientFactory clientFactory;


    public Nimbix(Gson gson, IHttpClientFactory clientFactory)
    {
        logger = LoggerFactory.getLogger(this.getClass());
        this.gson = gson;
        this.clientFactory = clientFactory;
    }

    public NimbixStartDetails startNimbixServer()
    {
        NimbixStartDetails rval = new NimbixStartDetails();

        HttpClient client = clientFactory.getHttpClientInstance();

        String urlString = ENDPOINT_BASE_URL + ENDPOINT_START_SERVER_URL_FRAGMENT;

        HashMap<String, String> fieldHeaders = new HashMap<>();
        fieldHeaders.put("Content-Type", "application/json");

        //TODO:  MASSIVE SSL SECURITY FLAW HOSTNAME BYPASS FOR NIMBIX, CONTACT NIMBIX TO RESOLVE TO MITIGATE SECURITY RISK
        client.configureForStandardHttpsRequest(urlString, fieldHeaders,
                HttpClient.REQUEST_METHOD.POST, NIMBIX_STARTUP_JSON, null, true);

        NimbixStartResponse startResponse;

        try
        {
            UrlResponse response = client.sendHttpsRequest();

            if (response != null && response.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                String concatString = "";

                for (String singleString : response.getResponseBody())
                    concatString += singleString;

                startResponse = gson.fromJson(concatString, NimbixStartResponse.class);

                String ipAddress = "";

                //Limit this to 100 tries, we don't want to sit here forever.  Will wait ~15 minutes before giving up.
                if (startResponse != null && startResponse.getJob_name() != null)
                {
                    for (int i = 0; i < 100; ++i)
                    {
                        ipAddress = isNimbixStarted(startResponse.getJob_name());

                        //Check if we actually found an IP address
                        if (!ipAddress.equals("")) {
                            //Found an IP address, delay another 10 seconds to give the
                            //Node.js server time to spin up.

                            //Going with a more robust ping solution
                            //Thread.sleep(10000);
                            break;
                        }
                        else
                        {
                            Thread.sleep(5000);
                        }
                    }

                    if (ipAddress.length() > 0)
                        rval.setIpAddress(ipAddress);

                    rval.setJobId(startResponse.getJob_name());
                }

                //Now we know Nimbix is running, but there is a delay in
                //reality server starting.  Wait until we're sure it's running, otherwise
                //requests will fail.
                if (!rval.getIpAddress().equals(""))
                {
                    for (int i = 0; i < 100; ++i)
                    {
                        if (isRealityServerStarted(rval.getIpAddress()))
                            break;
                        else
                            Thread.sleep(5000);
                    }
                }
            }

        } catch (Exception e)
        {
            logError(e);
        }

        return rval;
    }

    public Boolean stopNimbixServer(String jobId)
    {
        Boolean rval = false;

        HttpClient client = clientFactory.getHttpClientInstance();

        NimbixStatusRequest request = new NimbixStatusRequest(TROVE_NIMBIX_USERNAME, NACC_API_KEY, "status", jobId);

        String jsonRequest = gson.toJson(request);
        jsonRequest = NimbixStatusRequest.makeJsonCompatibleWithStupidNimbixFormat(jsonRequest);

        String urlString = ENDPOINT_BASE_URL + ENDPOINT_JOBSTATUS_SERVER_URL_FRAGMENT;

        HashMap<String, String> fieldHeaders = new HashMap<>();
        fieldHeaders.put("Content-Type", "application/json");

        //TODO:  MASSIVE SSL SECURITY FLAW HOSTNAME BYPASS FOR NIMBIX, CONTACT NIMBIX TO RESOLVE TO MITIGATE SECURITY RISK
        client.configureForStandardHttpsRequest(urlString, fieldHeaders,
                HttpClient.REQUEST_METHOD.POST, jsonRequest, null, true);

        try
        {
            UrlResponse response = client.sendHttpsRequest();

            String jobNumber = "";
            if (response != null && response.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                //Example part of the reply we're trying to find and parse:
                //Job Number     26359
                for (String responseLine : response.getResponseBody())
                {
                    if (responseLine.contains("Job Number")) {
                        jobNumber = responseLine;
                        break;
                    }
                }

                if (jobNumber.length() > 0)
                {
                    String jobNumberParse[] = jobNumber.split("\\s+");
                    jobNumber = jobNumberParse[2];

                    //Just to be different, the stop job request is a GET.....
                    urlString = ENDPOINT_BASE_URL + ENDPOINT_DELETEJOB_SERVER_URL_FRAGMENT;
                    urlString = urlString.replace("<USER>", TROVE_NIMBIX_USERNAME);
                    urlString = urlString.replace("<APIKEY>", NACC_API_KEY);
                    urlString = urlString.replace("<JOBNUMBER>", jobNumber);

                    //TODO:  MASSIVE SSL SECURITY FLAW HOSTNAME BYPASS FOR NIMBIX, CONTACT NIMBIX TO RESOLVE TO MITIGATE SECURITY RISK
                    client.configureForStandardHttpsRequest(urlString, fieldHeaders,
                            HttpClient.REQUEST_METHOD.GET, null, null, true);

                    //Format for this response should look like this:
                    //Requesting delete for Job # 27843 for userID trove_nimbix
                    response = client.sendHttpsRequest();

                    //TODO:  If shutdown starts failing, implement shutdown validation.  For now, assuming it'll just work.
                    for(String responseString : response.getResponseBody())
                    {
                        if (responseString.contains("Requesting delete") && responseString.contains(jobNumber)) {
                            rval = true;
                            break;
                        }
                    }
                }
            }

        } catch (Exception e)
        {
            logError(e);
        }

        return rval;

    }

    /**
     * This deserves an explanation.  Nimbix, in its infinite wisdom, did not create an API endpoint to check and see
     * when a server is up and running.  Instead, it drops a status file on an SFTP site that we have access to.
     * Therefore, we must query this file until the server is up and running.  Once the server is up and running,
     * the file will reflect an IP address that we may connect to in order to run RealityServer commands.
     *
     * @param jobName Name of the "job" instance that is started.  This is part of status text file's name.
     * @return The IP address of the started Nimbix instance, if started.  Otherwise, an empty string.
     */
    public String isNimbixStarted(String jobName)
    {
        //Courtesy http://stackoverflow.com/a/2690861/537454
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
            statusStream = sftpChannel.get("/data/JARVICE-BUILD-NAE-" + jobName + ".txt");
            returnedFile = StringUtils.converStreamToString(statusStream);
            sftpChannel.exit();
            session.disconnect();


            //If this isn't the case, the results file will indicate that it's still starting up.
            if (returnedFile.contains("NAE Address:"))
            {
                //Result file is assumed to be of this format:
                //NAE Address: 8.39.209.158
                //NAE root/nimbix Password: laoEnlight21

                //Grab the IP
                String resultStringSplit[] = returnedFile.split("\\s+");
                rval = resultStringSplit[2];

                //NAE doesn't get stripped off properly with the split.  So strip it off manually.
                rval = rval.replace("NAE", "");
            }

            statusStream.close();

        } catch (Exception e)
        {
            logError(e);
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

        return rval;
    }

    public Boolean isRealityServerStarted(String ipAddress)
    {
        Boolean rval = false;

        HttpClient client = clientFactory.getHttpClientInstance();

        String urlString = "http://" + ipAddress + "/render/ping";

        client.configureForStandardRequest(urlString, null, HttpClient.REQUEST_METHOD.GET, null, null);

        try
        {
            UrlResponse response = client.sendRequest();

            if (response.getResponseCode() == HttpURLConnection.HTTP_OK)
                rval = true;

        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            return rval;
        }
    }

    private void logError(Exception e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        logger.error("Stack Trace: " + sw.toString());
    }
}
