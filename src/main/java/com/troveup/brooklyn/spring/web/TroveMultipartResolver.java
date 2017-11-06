package com.troveup.brooklyn.spring.web;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.*;
import org.springframework.web.multipart.commons.CommonsFileUploadSupport;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class shamelessly stolen from Spring and modified to work with GAE.  GAE doesn't allow the use of
 * java.io.FileOutputStream, so methods that use it were modified.
 */
public class TroveMultipartResolver extends CommonsFileUploadSupport implements MultipartResolver, ServletContextAware
{

    private boolean resolveLazily = false;


    /**
     * Constructor for use as bean. Determines the servlet container's
     * temporary directory via the ServletContext passed in as through the
     * ServletContextAware interface (typically by a WebApplicationContext).
     * @see #setServletContext
     * @see org.springframework.web.context.ServletContextAware
     * @see org.springframework.web.context.WebApplicationContext
     */
    public TroveMultipartResolver() {
        super();
    }

    /**
     * Constructor for standalone usage. Determines the servlet container's
     * temporary directory via the given ServletContext.
     * @param servletContext the ServletContext to use
     */
    public TroveMultipartResolver(ServletContext servletContext) {
        this();
        setServletContext(servletContext);
    }


    /**
     * Set whether to resolve the multipart request lazily at the time of
     * file or parameter access.
     * <p>Default is "false", resolving the multipart elements immediately, throwing
     * corresponding exceptions at the time of the {@link #resolveMultipart} call.
     * Switch this to "true" for lazy multipart parsing, throwing parse exceptions
     * once the application attempts to obtain multipart files or parameters.
     */
    public void setResolveLazily(boolean resolveLazily) {
        this.resolveLazily = resolveLazily;
    }

    /**
     * Initialize the underlying {@code org.apache.commons.fileupload.servlet.ServletFileUpload}
     * instance. Can be overridden to use a custom subclass, e.g. for testing purposes.
     * @param fileItemFactory the Commons FileItemFactory to use
     * @return the new ServletFileUpload instance
     */
    @Override
    protected FileUpload newFileUpload(FileItemFactory fileItemFactory) {
        return new ServletFileUpload(fileItemFactory);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        if (!isUploadTempDirSpecified()) {
            getFileItemFactory().setRepository(WebUtils.getTempDir(servletContext));
        }
    }


    @Override
    public boolean isMultipart(HttpServletRequest request) {
        return (request != null && ServletFileUpload.isMultipartContent(request));
    }

    @Override
    public MultipartHttpServletRequest resolveMultipart(final HttpServletRequest request) throws MultipartException {
        Assert.notNull(request, "Request must not be null");
        if (this.resolveLazily) {
            return new DefaultMultipartHttpServletRequest(request) {
                @Override
                protected void initializeMultipart() {
                    MultipartParsingResult parsingResult = parseRequest(request);
                    setMultipartFiles(parsingResult.getMultipartFiles());
                    setMultipartParameters(parsingResult.getMultipartParameters());
                    setMultipartParameterContentTypes(parsingResult.getMultipartParameterContentTypes());
                }
            };
        }
        else {
            MultipartParsingResult parsingResult = parseRequest(request);
            return new DefaultMultipartHttpServletRequest(request, parsingResult.getMultipartFiles(),
                    parsingResult.getMultipartParameters(), parsingResult.getMultipartParameterContentTypes());
        }
    }

    /**
     * Parse the given servlet request, resolving its multipart elements.
     * @param request the request to parse
     * @return the parsing result
     * @throws MultipartException if multipart resolution failed.
     */
    protected MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
        String encoding = determineEncoding(request);
        FileUpload fileUpload = prepareFileUpload(encoding);
        try {
            //Can't use this with GAE, as it uses java.io.FileOutputStream, which is a restricted class.  Have to
            //implement it manually.
            //List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);

            List<TroveFileItem> fileItems = commonsOverrideParseRequest(request);

            return troveParseFileItems(fileItems, encoding);
        }
        catch (FileUploadBase.SizeLimitExceededException ex) {
            throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
        }
        catch (FileUploadException ex) {
            throw new MultipartException("Could not parse multipart servlet request", ex);
        }
    }

    /**
     * Determine the encoding for the given request.
     * Can be overridden in subclasses.
     * <p>The default implementation checks the request encoding,
     * falling back to the default encoding specified for this resolver.
     * @param request current HTTP request
     * @return the encoding for the request (never {@code null})
     * @see javax.servlet.ServletRequest#getCharacterEncoding
     * @see #setDefaultEncoding
     */
    protected String determineEncoding(HttpServletRequest request) {
        String encoding = request.getCharacterEncoding();
        if (encoding == null) {
            encoding = getDefaultEncoding();
        }
        return encoding;
    }

    @Override
    public void cleanupMultipart(MultipartHttpServletRequest request) {
        if (request != null) {
            try {
                cleanupFileItems(request.getMultiFileMap());
            }
            catch (Throwable ex) {
                logger.warn("Failed to perform multipart cleanup for servlet request", ex);
            }
        }
    }

    private List<TroveFileItem> commonsOverrideParseRequest(HttpServletRequest req)
            throws FileUploadException {
        List<TroveFileItem> items = new ArrayList<TroveFileItem>();

        try {
            ServletFileUpload upload = new ServletFileUpload();
            FileItemIterator iter = upload.getItemIterator(req);
            FileItemFactory fac = getFileItemFactory();
            if (fac == null) {
                throw new NullPointerException("No FileItemFactory has been set.");
            }
            while (iter.hasNext()) {
                final FileItemStream item = iter.next();
                InputStream stream = item.openStream();
                TroveFileItem fileItem = new TroveFileItem();
                fileItem.setByteBuffer(IOUtils.toByteArray(stream));
                fileItem.setContentType(item.getContentType());
                fileItem.setFieldName(item.getFieldName());
                fileItem.setFileItemHeaders(item.getHeaders());
                fileItem.setFormField(item.isFormField());

                String fileName = null;

                try
                {
                    fileName = item.getName();
                } catch (Exception e)
                {
                    //throw away this error, the filename is already null
                }

                fileItem.setName(fileName);

                items.add(fileItem);
            }

            return items;
        } catch (FileUploadBase.FileUploadIOException e) {
            throw (FileUploadException) e.getCause();
        } catch (IOException e) {
            throw new FileUploadException(e.getMessage(), e);
        }
    }

    /**
     * Parse the given List of Commons FileItems into a Spring MultipartParsingResult,
     * containing Spring MultipartFile instances and a Map of multipart parameter.
     * @param fileItems the Commons FileIterms to parse
     * @param encoding the encoding to use for form fields
     * @return the Spring MultipartParsingResult
     * @see CommonsMultipartFile#CommonsMultipartFile(org.apache.commons.fileupload.FileItem)
     */
    protected MultipartParsingResult troveParseFileItems(List<TroveFileItem> fileItems, String encoding) {
        MultiValueMap<String, MultipartFile> multipartFiles = new LinkedMultiValueMap<String, MultipartFile>();
        Map<String, String[]> multipartParameters = new HashMap<String, String[]>();
        Map<String, String> multipartParameterContentTypes = new HashMap<String, String>();

        // Extract multipart files and multipart parameters.
        for (TroveFileItem fileItem : fileItems) {
            if (fileItem.isFormField()) {
                String value;
                String partEncoding = determineEncoding(fileItem.getContentType(), encoding);
                if (partEncoding != null) {
                    try {
                        value = fileItem.getString(partEncoding);
                    }
                    catch (UnsupportedEncodingException ex) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("Could not decode multipart item '" + fileItem.getFieldName() +
                                    "' with encoding '" + partEncoding + "': using platform default");
                        }
                        value = fileItem.getString();
                    } catch (IOException e)
                    {
                        if (logger.isWarnEnabled()) {
                            logger.warn("Could not decode multipart item '" + fileItem.getFieldName() +
                                    "' with encoding '" + partEncoding + "': using platform default");
                        }
                        value = fileItem.getString();
                    }
                }
                else {
                    value = fileItem.getString();
                }
                String[] curParam = multipartParameters.get(fileItem.getFieldName());
                if (curParam == null) {
                    // simple form field
                    multipartParameters.put(fileItem.getFieldName(), new String[] {value});
                }
                else {
                    // array of simple form fields
                    String[] newParam = StringUtils.addStringToArray(curParam, value);
                    multipartParameters.put(fileItem.getFieldName(), newParam);
                }
                multipartParameterContentTypes.put(fileItem.getFieldName(), fileItem.getContentType());
            } else {
                // multipart file field
                TroveMultipartFile file = new TroveMultipartFile(fileItem);
                multipartFiles.add(file.getName(), file);
                if (logger.isDebugEnabled()) {
                    logger.debug("Found multipart file [" + file.getName() + "] of size " + file.getSize() +
                            " bytes");
                }
            }
        }
        return new MultipartParsingResult(multipartFiles, multipartParameters, multipartParameterContentTypes);
    }

    private String determineEncoding(String contentTypeHeader, String defaultEncoding) {
        if (!StringUtils.hasText(contentTypeHeader)) {
            return defaultEncoding;
        }
        MediaType contentType = MediaType.parseMediaType(contentTypeHeader);
        Charset charset = contentType.getCharSet();
        return (charset != null ? charset.name() : defaultEncoding);
    }
}
