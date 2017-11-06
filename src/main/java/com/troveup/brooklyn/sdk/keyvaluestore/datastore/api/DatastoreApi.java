package com.troveup.brooklyn.sdk.keyvaluestore.datastore.api;

import com.google.appengine.api.datastore.*;


/**
 * Created by tim on 6/22/15.
 */
public class DatastoreApi
{

    private final String ENTITY_TYPE = "KeyValue";
    private final String PROPERTY_NAME = "value";

    /**
     * Inserts a key/value pair into Google's Datastore
     * @param key Key for the key/value pair.
     * @param value Value of the key/value pair.
     */
    public void createSimpleKeyValueDatastoreEntry(String key, String value)
    {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Entity keyValueEntity = new Entity(ENTITY_TYPE, key);
        keyValueEntity.setProperty(PROPERTY_NAME, value);

        datastore.put(keyValueEntity);
    }

    /**
     * Updates a key/value pair that already exists within Google's Datastore.
     * Convenience method for create, as entities are overwritten in Datastore when a new one is saved.
     * @param key Key for the key/value pair.
     * @param newValue Value for the key/value pair.
     */
    public void updateSimpleKeyValueDatastoreEntry(String key, String newValue)
    {
        createSimpleKeyValueDatastoreEntry(key, newValue);
    }

    /**
     * Reads a key/value pair from within Google's Datastore.
     *
     * @param key Key representing the key/value pair.
     * @return The String value of the key/value pair, or null if it doesn't exist.
     */
    public String readSimpleKeyValueDatastoreEntry(String key)
    {
        String rval = null;
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Key searchKey = KeyFactory.createKey(ENTITY_TYPE, key);

        try {
            Entity keyValueEntity = datastore.get(searchKey);
            rval = (String) keyValueEntity.getProperty(PROPERTY_NAME);
        } catch (Exception e)
        {
            //Eat any exception, we'll just return null if it's not found
        }
        finally
        {
            return rval;
        }
    }

    /**
     * Deletes a key/value pair from the Google's Datastore
     * @param key Key for the key/value pair being removed.
     */
    public void deleteSimpleKeyValueDatastoreEntry(String key)
    {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Key searchKey = KeyFactory.createKey(ENTITY_TYPE, key);

        datastore.delete(searchKey);
    }
}
