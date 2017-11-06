package com.troveup.brooklyn.sdk.keyvaluestore.interfaces;

/**
 * Created by tim on 6/22/15.
 */
public interface IKeyValueStore
{
    void create(String key, String value);
    String read(String key);
    void update(String key, String value);
    void delete(String key);
}
