package com.troveup.brooklyn.util.models;

import java.util.Map;

/**
 * Created by tim on 5/25/15.
 */
public class ConfigurableFormField
{
    private String name;
    private String value;
    private Map<String, String> headers;

    public ConfigurableFormField()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
