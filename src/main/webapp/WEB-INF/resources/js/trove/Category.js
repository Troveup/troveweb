function CategoryHelper() {

    this.landedOnFeedEvent = function (isAuthenticated) {
        this.fireMixPanelEvent(isAuthenticated, "land_feed");
    };

    this.nameButtonClick = function (isAuthenticated, itemid) {
        this.fireMixPanelEvent(isAuthenticated, "feed_username", {
            "item": itemid
        });
    };

    this.collectionButtonClick = function (isAuthenticated, userid) {
        this.fireMixPanelEvent(isAuthenticated, "feed_collection", {
            "owner": userid
        });
    };

    this.imageButtonClick = function (isAuthenticated, itemid) {
        this.fireMixPanelEvent(isAuthenticated, "feed_image", {
            "item": itemid
        });
    };

    this.customizeButtonClick = function(isAuthenticated, itemid) {
        this.fireMixPanelEvent(isAuthenticated, "feed_customize", {
            "item": itemid
        });
    };

    //TODO:  Hard dependency on authHelper existing on page.  If this poses an issue down the road, fix it.
    this.saveButtonClick = function(itemid, location) {

        if (authHelper.getIsAuthenticated()) {
            TROVE.troveItem(itemid, location);
        } else {
            authHelper.setActionToPerformPostAuth(this.saveButtonClick);
            authHelper.setArgsForPostAuthAction([itemid, location]);
            triggerAuthModal();
        }
    }
}

CategoryHelper.prototype.fireMixPanelEvent = function (isAuthenticated, trackingString, objectData) {
    if (isAuthenticated == null || !isAuthenticated) {
        trackingString = trackingString + "_ANON";
    }

    if (objectData != null) {
        mixpanel.track(trackingString, objectData);
    }
    else {
        mixpanel.track(trackingString);
    }
};
