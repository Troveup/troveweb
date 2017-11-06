/**
 * Default constructor, calls the initializer function to set the CSRF token.
 * @param csrfToken
 * @param isAuthenticated
 * @constructor
 */
function AuthHelper(csrfToken, isAuthenticated) {
    init(csrfToken, isAuthenticated == null ? false : isAuthenticated);

    this.getIsAuthenticated = function() {
        return authSettings.isAuthenticated;
    };

    this.setIsAuthenticated = function(isAuthenticated) {
        authSettings.isAuthenticated = isAuthenticated;
    };

    this.setActionToPerformPostAuth = function(postAuthFunction) {
        authSettings.postAuthFunction = postAuthFunction;
    };

    this.getActionToPerformPostAuth = function() {
        return authSettings.postAuthFunction;
    };

    this.setArgsForPostAuthAction = function(postAuthActionArgs) {
        authSettings.postAuthActionArgs = postAuthActionArgs;
    };

    this.getArgsForPostAuthAction = function() {
        return authSettings.postAuthActionArgs;
    }
}

var authSettings = {
    authEndpointUrl: '/signinasync',
    registerEndpointUrl: '/signup'
};

/**
 * Initializes the CSRF token within settings
 * @param isAuthenticated
 * @param csrfToken
 */
function init(csrfToken, isAuthenticated) {
    authSettings.csrfToken = csrfToken;
    authSettings.isAuthenticated = isAuthenticated;
}

/**
 * Performs an AJAX request for authentication
 *
 * @param dataObject FormData object containing the POST data
 * @param url POST URL to send the data to
 * @param callbackSuccess Success callback function reference
 * @param callbackFailure Failure callback function reference
 */
function performAuthRequest(dataObject, url, callbackSuccess, callbackFailure) {
    jQuery.ajax({
        url: url,
        data: dataObject,
        cache: false,
        contentType: false,
        processData: false,
        headers: {
            'Access-Control-Allow-Origin': '*',
            "X-CSRF-TOKEN": authSettings.csrfToken
        },
        type: 'POST',
        success: function(data) {
            if (data.isSuccess) {
                callbackSuccess(data);
            } else {
                callbackFailure(data);
            }
        },
        error: function(data) {
            callbackFailure(data);
        }
    });
}

/**
 * Externally accessible helper for handling packaging and sending user registration requests.
 *
 * @param name User's name
 * @param email User's email
 * @param password User's password
 * @param username Optional username field.  Leave null if not applicable.
 * @param optIn E-mail opt-in setting.  True if opting in, false otherwise.
 * @param callbackSuccess Callback function on successful registration.
 * @param callbackFailure Callback function on failed registration.
 */
AuthHelper.prototype.registerUser = function (name, email, password, username, optIn, callbackSuccess, callbackFailure) {
    var formDataObject = new FormData();
    formDataObject.append('name', name);
    formDataObject.append('email', email);
    formDataObject.append('password', password);

    if (username != null) {
        formDataObject.append('username', username);
    }

    if (optIn != null) {
        formDataObject.append('optIn', optIn);
    }

    performAuthRequest(formDataObject, authSettings.registerEndpointUrl, callbackSuccess, callbackFailure);
};

/**
 * Externally accessible helper for handling packaging and sending user authentication requests.
 *
 * @param usernameOrEmail Either the user's username or their e-mail.  Either will work.
 * @param password The user's password.
 * @param callbackSuccess Callback function on successful authentication.
 * @param callbackFailure Callback function on failed authentication.
 */
AuthHelper.prototype.authenticateUser = function(usernameOrEmail, password, callbackSuccess, callbackFailure) {
    var formDataObject = new FormData();
    formDataObject.append('usernameoremail', usernameOrEmail);
    formDataObject.append('password', password);

    performAuthRequest(formDataObject, authSettings.authEndpointUrl, callbackSuccess, callbackFailure);
};

/**
 * Generates a one-size-fits-all success callback object in the event that it isn't desirable to create one custom.
 *
 * @param redirectUrl The URL to redirect the user to after a successful action has been carried out.
 * @returns {Function} The function that handles the success case.
 */
AuthHelper.prototype.createGenericSuccessRedirectCallback = function(redirectUrl) {
    return function(data) {
        window.location = redirectUrl;
    }
};

/**
 * Generates a one-size-fits-all failure object in the event that it isn't desirable to create one custom.
 *
 * @param errorMessageObjectHandle jQuery div element that is responsible for showing error messages.
 * @returns {Function} The function that handles the fail case.
 */
AuthHelper.prototype.createGenericFailureCallback = function(errorMessageObjectHandle) {
    return function(data) {

        var errorMessageDiv = $(errorMessageObjectHandle);

        if (data.errorMessage != null && data.errorMessage.length > 0) {
            var errorMessage = data.errorMessage;
        }
        else {
            errorMessage = "Oops!  Something went wrong.  Please try that again.";
        }

        errorMessageDiv.html(errorMessage);
        errorMessageDiv.show();
    }
};