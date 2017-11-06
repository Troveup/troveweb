package com.troveup.brooklyn.sdk.common.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by tim on 8/29/15.
 */
public class CommonApi
{
    protected Logger logger;

    public CommonApi()
    {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    protected void logError(Exception e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        logger.error("Stack Trace: " + sw.toString());
    }
}
