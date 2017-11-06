package com.troveup.brooklyn.orm.storefront.interfaces;

import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.storefront.model.StoreFront;

/**
 * Created by tim on 6/10/16.
 */
public interface IStoreFrontAccessor
{
    Boolean doesSellerUrlShortpathExist(String urlShortPath);
    StoreFront persistStoreFront(Long userId, StoreFront storeFront);
    StoreFront getStoreFront(String url, IEnums.SEEK_MODE mode);
}
