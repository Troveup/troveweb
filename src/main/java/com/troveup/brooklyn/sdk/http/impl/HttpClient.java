package com.troveup.brooklyn.sdk.http.impl;

import com.troveup.brooklyn.util.models.ConfigurableFormField;
import com.troveup.brooklyn.util.models.UrlResponse;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tim on 5/22/15.
 */
public class HttpClient
{
    public enum REQUEST_METHOD
    {
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        HEAD("HEAD"),
        DELETE("DELETE");

        private final String text;

        REQUEST_METHOD(String type) {
            this.text = type;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    private String url;
    private String host;
    private String protocol;
    private int port;
    private REQUEST_METHOD requestMethod;
    private HashMap<String, String> headers;
    private HashMap<String, String> formFields;
    private HashMap<String, File> fileParts;
    private List<ConfigurableFormField> configurableFormFields;
    private String jsonBody;
    private String charset;
    private Boolean bypassHostnameVerification;

    public HttpClient()
    {

    }

    public void configureForMultipart(String url, HashMap<String, String> headers, HashMap<String, String> formFields,
                                      HashMap<String, File> fileParts,
                                      List<ConfigurableFormField> configurableFormFields, String charset)
    {
        this.url = url;
        this.headers = headers;
        this.formFields = formFields;
        this.fileParts = fileParts;
        this.configurableFormFields = configurableFormFields;

        if (charset == null)
            this.charset = "UTF-8";
        else
            this.charset = charset;

        bypassHostnameVerification = false;
    }

    public void configureForStandardRequest(String url, HashMap<String, String> headers, REQUEST_METHOD requestMethod,
                                            String jsonBody, String charset)
    {
        this.url = url;
        this.headers = headers;
        this.jsonBody = jsonBody;
        this.requestMethod = requestMethod;

        if (charset == null)
            this.charset = "UTF-8";
        else
            this.charset = charset;

        this.bypassHostnameVerification = false;
    }

    public void configureForStandardHttpsRequest(String url, HashMap<String, String> headers, REQUEST_METHOD requestMethod,
                                            String jsonBody, String charset, Boolean bypassHostnameVerficiation)
    {
        this.url = url;
        this.headers = headers;
        this.jsonBody = jsonBody;
        this.requestMethod = requestMethod;

        if (charset == null)
            this.charset = "UTF-8";
        else
            this.charset = charset;

        if (bypassHostnameVerficiation != null)
            this.bypassHostnameVerification = bypassHostnameVerficiation;
        else
            this.bypassHostnameVerification = false;
    }

    public void configureForStandardHttpsRequest(String protocol, String host, int port,
                                                 HashMap<String, String> headers, REQUEST_METHOD requestMethod,
                                                 String jsonBody, String charset, Boolean bypassHostnameVerification)
    {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.headers = headers;
        this.jsonBody = jsonBody;
        this.requestMethod = requestMethod;

        if (charset == null)
            this.charset = "UTF-8";
        else
            this.charset = charset;

        if (bypassHostnameVerification != null)
            this.bypassHostnameVerification = bypassHostnameVerification;
        else
            this.bypassHostnameVerification = false;
    }

    //Multipart constructor
    public HttpClient(String url, HashMap<String, String> headers, HashMap<String, String> formFields,
                      HashMap<String, File> fileParts, String charset)
    {
        this.url = url;
        this.headers = headers;
        this.formFields = formFields;
        this.fileParts = fileParts;

        if (charset == null)
            this.charset = "UTF-8";
        else
            this.charset = charset;
    }

    //Standard constructor
    public HttpClient(String url, HashMap<String, String> headers, REQUEST_METHOD requestMethod,
                      String jsonBody, String charset)
    {
        this.url = url;
        this.headers = headers;
        this.jsonBody = jsonBody;
        this.requestMethod = requestMethod;

        if (charset == null)
            this.charset = "UTF-8";
        else
            this.charset = charset;
    }

    public UrlResponse sendRequest() throws IOException, NullPointerException {
        UrlResponse rval = new UrlResponse();

        if (url == null || requestMethod == null)
            throw new NullPointerException("url and requestMethod must be provided for a regular request!");

        URL reqUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) reqUrl.openConnection();
        connection.setConnectTimeout(60000);
        connection.setReadTimeout(60000);
        //connection.setRequestMethod(requestMethod.toString());

        if (headers != null)
        {
            for (String header : headers.keySet())
                connection.setRequestProperty(header, headers.get(header));
        }

        if (requestMethod == REQUEST_METHOD.POST || requestMethod == REQUEST_METHOD.PUT)
        {
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            if (jsonBody != null) {
                writer.write(jsonBody);
                writer.flush();
                writer.close();
            }
        }

        int statusCode = connection.getResponseCode();
        if (statusCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                rval.getResponseBody().add(line);
            }
        }
        else
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                rval.getResponseBody().add(line);
            }
        }
        rval.setResponseCode(connection.getResponseCode());
        rval.setResponseMessage(connection.getResponseMessage());

        return rval;

    }

    public UrlResponse sendHttpsRequest() throws IOException, NullPointerException, NoSuchAlgorithmException, KeyManagementException {
        UrlResponse rval = new UrlResponse();

        if (url == null || requestMethod == null)
            throw new NullPointerException("url and requestMethod must be provided for a regular request!");

        //TODO:  This is very very bad.  Modify all users of HttpClient so that they don't need to use this (I'm looking at you, Nimbix).
        if (bypassHostnameVerification) {
            //bypassHostnameVerification(connection);
            //acceptAllSSLCerts(connection);

            TrustManager[] trustAllCerts = new TrustManager[]{ new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }

            } };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });

        }

        URL reqUrl = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) reqUrl.openConnection();

        connection.setConnectTimeout(60000);
        connection.setReadTimeout(60000);
        //connection.setRequestMethod(requestMethod.toString());

        if (headers != null)
        {
            for (String header : headers.keySet())
                connection.setRequestProperty(header, headers.get(header));
        }

        if (requestMethod == REQUEST_METHOD.POST || requestMethod == REQUEST_METHOD.PUT)
        {
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(jsonBody);
            writer.flush();
            writer.close();
        }

        int statusCode = connection.getResponseCode();
        if (statusCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                rval.getResponseBody().add(line);
            }
        }
        rval.setResponseCode(connection.getResponseCode());
        rval.setResponseMessage(connection.getResponseMessage());

        return rval;

    }

    private void acceptAllSSLCerts(HttpsURLConnection connection) throws KeyManagementException, NoSuchAlgorithmException {
        TrustManager[] trustAllCerts = new TrustManager[]{ new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }

        } };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());

        connection.setDefaultSSLSocketFactory(sc.getSocketFactory());

}

    public UrlResponse sendMultipartRequest() throws IOException
    {
        HttpClientMultipartUtility utility = new HttpClientMultipartUtility(url, charset, headers);

        if (formFields != null)
        {
            for (String formField : formFields.keySet())
                utility.addFormField(formField, formFields.get(formField));
        }

        if (configurableFormFields != null)
        {
            for (ConfigurableFormField formField : configurableFormFields)
                utility.addFormField(formField);
        }

        if (fileParts != null)
        {
            for (String filePart : fileParts.keySet())
                utility.addFilePart(filePart, fileParts.get(filePart));
        }

        return utility.finish();
    }

    private void bypassHostnameVerification(HttpsURLConnection connection)
    {
        connection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public REQUEST_METHOD getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(REQUEST_METHOD requestMethod) {
        this.requestMethod = requestMethod;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public String getJsonBody() {
        return jsonBody;
    }

    public void setJsonBody(String jsonBody) {
        this.jsonBody = jsonBody;
    }

    public HashMap<String, String> getFormFields() {
        return formFields;
    }

    public void setFormFields(HashMap<String, String> formFields) {
        this.formFields = formFields;
    }

    public HashMap<String, File> getFileParts() {
        return fileParts;
    }

    public void setFileParts(HashMap<String, File> fileParts) {
        this.fileParts = fileParts;
    }

    public List<ConfigurableFormField> getConfigurableFormFields() {
        return configurableFormFields;
    }

    public void setConfigurableFormFields(List<ConfigurableFormField> configurableFormFields) {
        this.configurableFormFields = configurableFormFields;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
