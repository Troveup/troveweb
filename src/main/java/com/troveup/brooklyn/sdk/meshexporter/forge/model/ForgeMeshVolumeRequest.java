package com.troveup.brooklyn.sdk.meshexporter.forge.model;

/**
 * Created by tim on 8/29/15.
 */
public class ForgeMeshVolumeRequest
{
    private CustomizerHash hash;

    public ForgeMeshVolumeRequest(CustomizerHash hash)
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
