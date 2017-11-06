/**
 * NavHelper constructor for establishing the various elements that need manipulation within the navbar.
 *
 * @note All element Ids passed to this function should be prefixed with a #
 *
 * @param authedNavBarElementIds Elements that are active during the authenticated state.
 * @param unauthedNavBarElementIds Elements that are active during the unauthenticated state.
 * @param userProfileImageElementId Element that represents the user's profile image
 * @param navMixPanelElementIds Mixpanel-specific elements used for tracking user clicks.
 * @constructor
 */
function NavHelper(authedNavBarElementIds, unauthedNavBarElementIds, userProfileImageElementId, navMixPanelElementIds, otherAuthFunctionsToPerform) {
    init(authedNavBarElementIds, unauthedNavBarElementIds, userProfileImageElementId, navMixPanelElementIds, otherAuthFunctionsToPerform);
}

var navSettings = {
};

/**
 * Navbar settings initializer.  Sets the arguments from the constructor as part of the settings.
 *
 * @param authedNavBarElementIds
 * @param unauthedNavBarElementIds
 * @param userProfileImageElementId
 * @param navMixPanelElementIds
 */
function init(authedNavBarElementIds, unauthedNavBarElementIds, userProfileImageElementId, navMixPanelElementIds, otherAuthFunctionsToPerform) {
    navSettings.navMixPanelElements = navMixPanelElementIds;
    navSettings.authedNavBarElements = authedNavBarElementIds;
    navSettings.unauthedNavBarElements = unauthedNavBarElementIds;
    navSettings.userProfileImageElement = userProfileImageElementId;
    navSettings.otherAuthFunctionsToPerform = otherAuthFunctionsToPerform;
}

/**
 * Configures and updates elements pertaining to the nav in the "authenticated" state to active.
 *
 * @param userId The userId of the authenticated user.  Used with identification for Mixpanel.
 * @param userEmail Email of the authenticated user.  Used with identification for Mixpanel.
 * @param firstName Name field of the authenticated user.  Used with identification for Mixpanel.
 * @param userImageUrl Profile image URL.  Updated on the navbar for the user's profile.
 */
NavHelper.prototype.setNavStateAuthenticated = function(userId, userEmail, firstName, userImageUrl)
{
    setUserProfileImageSource(userImageUrl);
    hideUnauthedNavBarElements();
    showAuthedNavBarElements();
    checkAndShowBagItemCounter();
    setMixPanelAuthenticated(userId, userEmail, firstName);

    if (navSettings.otherAuthFunctionsToPerform != null) {
        navSettings.otherAuthFunctionsToPerform();
    }
};

/**
 * Configures elements pertaining to the nav in the "unauthenticated" state to active.
 */
NavHelper.prototype.setNavUnauthenticated = function() {
    hideAuthedNavBarElements();
    showUnauthedNavBarElements();
};

NavHelper.prototype.incrementBagItemCounter = function() {
    performBagItemIncrement();
    checkAndShowBagItemCounter();
};

/**
 * Configures Mixpanel to be in an authenticated state so that it will track the user by their unique Trove identifiers.
 *
 * @param userId
 * @param userEmail
 * @param firstName
 */
function setMixPanelAuthenticated(userId, userEmail, firstName) {

    //Unbind the previous click settings and rebind to the non-anonymous tracking ID
    for (var i = 0; i < navSettings.navMixPanelElements.length; ++i)
    {
        var element = $(navSettings.navMixPanelElements[i]);
        element.unbind();

        element.click(setMixPanelTrackingId(navSettings.navMixPanelElements[i].replace("#", "")));
    }

    //Identify this user
    mixpanel.identify(userId);
    mixpanel.people.set({
        '$email': userEmail,
        'user_id': userId,
        '$name': firstName
    });
    mixpanel.people.increment('page_views');
}

/**
 * Helper function for setting a mixpanel tracking event by its label
 *
 * @param trackingLabel
 */
function setMixPanelTrackingId(trackingLabel) {
    mixpanel.track(trackingLabel);
}

/**
 * Checks to be sure there is at least one item in the bag, then shows the counter if this is the case.
 */
function checkAndShowBagItemCounter() {
    var count = parseInt($('.tallyho-cart').html());
    if ( count != null && parseInt(count) > 0 ) {
        $('.tallyho-cart').addClass('showitnow');
    }
}

/**
 * Increments the bag item counter if the counter isn't null.
 */
function performBagItemIncrement() {
    var countElement = $('.tallyho-cart');
    var count = countElement.html();

    if (count == null || count == "") {
        count = 0;
    }
    else {
        count = parseInt(count);
    }

    countElement.html(count + 1);
}

/**
 * Helper function for hiding authenticated navbar elements
 */
function hideAuthedNavBarElements() {
    for (var i = 0; i < navSettings.authedNavBarElements.length; ++i) {
        $(navSettings.authedNavBarElements[i]).hide();
    }
}

/**
 * Helper function for showing authenticated navbar elements
 */
function showAuthedNavBarElements() {
    for (var i = 0; i < navSettings.authedNavBarElements.length; ++i) {
        $(navSettings.authedNavBarElements[i]).show();
    }
}

/**
 * Helper function for hiding unauthenticated navbar elements
 */
function hideUnauthedNavBarElements() {
    for (var i = 0; i < navSettings.unauthedNavBarElements.length; ++i) {
        $(navSettings.unauthedNavBarElements[i]).hide();
    }
}

/**
 * Helper function for showing unauthenticated navbar elements
 */
function showUnauthedNavBarElements() {
    for (var i = 0; i < navSettings.unauthedNavBarElements.length; ++i) {
        $(navSettings.unauthedNavBarElements[i]).show();
    }
}

/**
 * Helper function for setting the user profile image source reference
 * @param sourcePath URL path of the user's profile image
 */
function setUserProfileImageSource(sourcePath) {
    $(navSettings.userProfileImageElement).attr('src', sourcePath);
}



