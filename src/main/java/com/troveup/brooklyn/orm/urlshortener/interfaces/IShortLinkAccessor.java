package com.troveup.brooklyn.orm.urlshortener.interfaces;

import com.troveup.brooklyn.orm.urlshortener.model.ShortLink;

import javax.jdo.PersistenceManager;

/**
 * Created by tim on 2/4/16.
 */
public interface IShortLinkAccessor
{
    ShortLink persistShortLink(ShortLink shortLink);
    ShortLink getShortLinkById(Long id);
    ShortLink getShortLinkByTag(String tag);
    ShortLink getShortLinkAttached(Long id, PersistenceManager pm);
}
