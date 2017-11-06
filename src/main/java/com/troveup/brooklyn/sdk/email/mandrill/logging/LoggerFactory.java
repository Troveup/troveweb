package com.troveup.brooklyn.sdk.email.mandrill.logging;

/**
 * @author aldenquimby@gmail.com
 */
public class LoggerFactory {

    /**
     * Get a named logger instance.
     *
     * @param clazz class from which a log name will be derived
     * @return the logger
     */
    public static org.slf4j.Logger getLogger(Class clazz) {
            return org.slf4j.LoggerFactory.getLogger(clazz);
    }
}
