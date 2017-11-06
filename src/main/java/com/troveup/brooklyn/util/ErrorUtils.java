package com.troveup.brooklyn.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by tim on 3/23/16.
 */
public class ErrorUtils
{
    public static void logError(Exception e, Logger logger)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        logger.error("Stack Trace: " + sw.toString());
    }

    public static void logError(Exception e)
    {
        Logger logger = LoggerFactory.getLogger(ErrorUtils.class);
        logError(e, logger);
    }
}
