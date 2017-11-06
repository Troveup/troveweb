package com.troveup.brooklyn.sdk.http.impl;

import com.troveup.brooklyn.util.models.ConfigurableFormField;
import com.troveup.brooklyn.util.models.UrlResponse;
import org.slf4j.Logger;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * This utility class provides an abstraction layer for sending multipart HTTP
 * POST requests to a web server.
 * @author www.codejava.net
 *
 */

public class HttpClientMultipartUtility {
    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConn;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter writer;
    String debugWriter;
    private Logger logger;

    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     * @param requestURL
     * @param charset
     * @throws IOException
     */
    public HttpClientMultipartUtility(String requestURL, String charset, Map<String, String> headers)
            throws IOException {
        this.charset = charset;

        //TODO:  Make this conditionally work if we're in debug mode
        debugWriter = "";

        logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

        // creates a unique boundary based on time stamp
        boundary = "---" + System.currentTimeMillis() + "---";

        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setConnectTimeout(60000);
        httpConn.setReadTimeout(60000);
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);
        httpConn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);
        httpConn.setRequestProperty("User-Agent", "Trove");

        if (headers != null && headers.containsKey("Accept")) {
            httpConn.setRequestProperty("Accept", headers.get("Accept"));
            headers.remove("Accept");

            for (String header : headers.keySet())
            {
                httpConn.setRequestProperty(header, headers.get(header));
            }
        }
        else
            httpConn.setRequestProperty("Accept", "text/json");

        httpConn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        httpConn.setRequestProperty("Accept-Language", "en-US,en;q=0.8");

        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                true);
    }

    /**
     * Adds a form field to the request
     * @param name field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        appendToWriter("--" + boundary).append(LINE_FEED);
        appendToWriter("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        appendToWriter("Content-Type: text/plain; charset=" + charset).append(
                LINE_FEED);
        appendToWriter(LINE_FEED);
        appendToWriter(value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a form field to the request, and allows for configurable headers.
     * @param data ConfigurableFormField object containing the field name, field data, and optional extra
     *             headers.
     */
    public void addFormField(ConfigurableFormField data)
    {
        appendToWriter("--" + boundary).append(LINE_FEED);
        appendToWriter("Content-Disposition: form-data; name=\"" + data.getName() + "\"")
                .append(LINE_FEED);

        for(String headerName : data.getHeaders().keySet())
        {
            appendToWriter((headerName + ": "));
            appendToWriter(data.getHeaders().get(headerName));

            if (headerName.equals("Content-Type"))
                appendToWriter("; charset=" + charset);

            appendToWriter(LINE_FEED);
        }

        appendToWriter(LINE_FEED);
        appendToWriter(data.getValue()).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a upload file section to the request
     * @param fieldName name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile)
            throws IOException {
        String fileName = uploadFile.getName();
        appendToWriter("--" + boundary).append(LINE_FEED);
        appendToWriter(
                "Content-Disposition: form-data; name=\"" + fieldName
                        + "\"; filename=\"" + fileName + "\"")
                .append(LINE_FEED);
        appendToWriter(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);
        appendToWriter("Content-Transfer-Encoding: binary").append(LINE_FEED);
        appendToWriter(LINE_FEED);
        writer.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();

        appendToWriter(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a header field to the request.
     * @param name - name of the header field
     * @param value - value of the header field
     */
    public void addHeaderField(String name, String value) {
        appendToWriter(name + ": " + value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Completes the request and receives response from the server.
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public UrlResponse finish() throws IOException {
        UrlResponse rval = new UrlResponse();

        appendToWriter(LINE_FEED).flush();
        appendToWriter("--" + boundary + "--").append(LINE_FEED);
        writer.close();
        logger.debug("Writing the following to multipart form: " + debugWriter);

        // checks server's status code first
        int status = httpConn.getResponseCode();
        rval.setResponseCode(status);
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                rval.getResponseBody().add(line);
            }
            reader.close();
            httpConn.disconnect();
        } else {
            rval.getResponseBody().add(httpConn.getResponseMessage());
        }

        return rval;
    }

    private PrintWriter appendToWriter(String string)
    {
        writer.append(string);
        debugWriter += string;

        return writer;
    }
}
