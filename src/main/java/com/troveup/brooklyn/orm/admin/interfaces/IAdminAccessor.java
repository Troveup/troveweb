package com.troveup.brooklyn.orm.admin.interfaces;

import com.troveup.brooklyn.orm.admin.model.AlertBannerState;

/**
 * Created by tim on 12/9/15.
 */
public interface IAdminAccessor
{
    Boolean persistAlertBannerState(AlertBannerState alertBannerState);
    AlertBannerState getLatestAlertBannerState();
}
