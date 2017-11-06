package com.troveup.brooklyn.model;

import java.util.List;

/**
 * Created by tim on 10/4/15.
 */
public class CustomizerInputFormat
{
    private List<String> visible;
    private List<CustomizerWeight> operators;

    public CustomizerInputFormat()
    {

    }

    public CustomizerInputFormat(List<String> visible, List<CustomizerWeight> operators)
    {
        this.visible = visible;
        this.operators = operators;
    }

    public List<String> getVisible() {
        return visible;
    }

    public void setVisible(List<String> visible) {
        this.visible = visible;
    }

    public List<CustomizerWeight> getOperators() {
        return operators;
    }

    public void setOperators(List<CustomizerWeight> operators) {
        this.operators = operators;
    }
}
