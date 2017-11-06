package com.troveup.brooklyn.sdk.realityserver.model;

/**
 * Created by tim on 6/21/15.
 */
public class NimbixStatusRequest
{
    private String username;
    private String api_key;
    private String request;
    private String job_name;

    public NimbixStatusRequest()
    {

    }

    public NimbixStatusRequest(String username, String api_key, String request, String job_name)
    {
        this.username = username;
        this.api_key = api_key;
        this.request = request;
        this.job_name = job_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    /**
     * Nimbix, again, in their infinite wisdom, put a variable in their status request
     * with a DASH IN IT.  So we have to convert from an underscore to a dash, because, you know,
     * one of the most common languages in the world doesn't support dashes in variable names.  What is their
     * format?  I'm glad you asked.
     * {
     *    "username" : "trove_nimbix",
     *    "api-key": "INSERT_YOUR_API_KEY_HERE",
     *    "request" : "status",
     *    "job_name": "20150528091336-14153-nae_12c24-2m2090-trove_nimbix_s1"
     * }
     *
     * This method will put a dash instead of an underscore in the api_key parameter within the JSON string.
     *
     * @param nimbixStatusRequestJsonString The jsonified request string that needs to have an underscore replaced
     *                                      with a dash in the "api_key" parameter.
     * @return The improperly adjusted JSON string for use with a Nimbix status request.
     */
    public static String makeJsonCompatibleWithStupidNimbixFormat(String nimbixStatusRequestJsonString)
    {
        return nimbixStatusRequestJsonString.replace("api_key", "api-key");
    }
}
