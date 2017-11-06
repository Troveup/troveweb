package com.troveup.brooklyn.sdk.keyvaluestore.datastore.business;

import com.troveup.brooklyn.sdk.keyvaluestore.datastore.api.DatastoreApi;
import com.troveup.brooklyn.sdk.keyvaluestore.interfaces.IKeyValueStore;

/**
 * Created by tim on 6/22/15.
 */
public class KeyValueDatastore implements IKeyValueStore
{

    private final DatastoreApi datastoreApi;

    public KeyValueDatastore(DatastoreApi datastoreApi)
    {
        this.datastoreApi = datastoreApi;
    }

    @Override
    public void create(String key, String value)
    {
        datastoreApi.createSimpleKeyValueDatastoreEntry(key, value);
    }

    @Override
    public String read(String key) {
        return datastoreApi.readSimpleKeyValueDatastoreEntry(key);
    }

    @Override
    public void update(String key, String value)
    {
        datastoreApi.updateSimpleKeyValueDatastoreEntry(key, value);
    }

    @Override
    public void delete(String key)
    {
        datastoreApi.deleteSimpleKeyValueDatastoreEntry(key);
    }
}
