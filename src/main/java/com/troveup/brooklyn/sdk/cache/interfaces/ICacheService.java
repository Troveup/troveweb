package com.troveup.brooklyn.sdk.cache.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Created by tim on 6/8/15.
 */
public interface ICacheService
{
    String GLOBAL_PRICE_FILTER_CACHE_KEY = "GPRICEFILTER";
    String GLOBAL_PRICE_FILTER_MATERIAL_CACHE_KEY = "GPRICEFILTER_MAT_X_FIN_Y";
    String CATEGORY_PRICE_FILTER_CACHE_KEY = "CPRICEFILTER_CAT_X";
    String CATEGORY_PRICE_FILTER_MATERIAL_CACHE_KEY = "CPRICEFILTER_MAT_X_FIN_Y_CAT_Z";
    String ITEM_PRICE_FILTER_CACHE_KEY = "IPRICEFILTER_ID_X";
    String ITEM_PRICE_FILTER_MATERIAL_CACHE_KEY = "IPRICEFILTER_MAT_X_FIN_Y_ID_Z";

    String ALERT_BANNER_CACHE_KEY = "ALERT_BANNER";

    Boolean putCache(String key, String value);
    Boolean putCache(Map<String, String> keyValues);
    Boolean putCache(String key, String value, Map properties);
    Boolean putCache(Map<String, String> keyValues, Map properties);

    String getCacheValue(String key);

    Map getCacheValues(List<String> keys) throws Exception;

}
