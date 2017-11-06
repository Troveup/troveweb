package com.troveup.brooklyn.sdk.cache.jcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by tim on 6/8/15.
 */
public class JCacheCommon
{
    Logger logger;

    public JCacheCommon()
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

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
