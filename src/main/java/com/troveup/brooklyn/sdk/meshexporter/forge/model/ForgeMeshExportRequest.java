package com.troveup.brooklyn.sdk.meshexporter.forge.model;

/**
 * Created by tim on 8/29/15.
 */
public class ForgeMeshExportRequest
{
    private CustomizerHash hash;

    public ForgeMeshExportRequest(CustomizerHash hash)
    {
        this.hash = hash;
    }

    public CustomizerHash getHash() {
        return hash;
    }

    public void setHash(CustomizerHash hash) {
        this.hash = hash;
    }
}
