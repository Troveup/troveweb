package com.troveup.brooklyn.sdk.email.mandrill.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by tim on 6/8/15.
 */
public class CommonMailProvider
{
    Logger logger;

    public CommonMailProvider()
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
