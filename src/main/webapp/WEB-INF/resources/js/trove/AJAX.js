/**
 * Default constructor, calls the initializer function to set the CSRF token.
 * @param csrfToken
 * @constructor
 */
function AJAXHelper(csrfToken) {
    init(csrfToken);
}

var AJAXSettings = {};

/**
 * Initializes the CSRF token within settings
 * @param csrfToken
 */
function init(csrfToken) {
    AJAXSettings.csrfToken = csrfToken;
}

/**
 * Performs an AJAX generic POST request
 *
 * @param dataObject FormData object containing the POST data
 * @param url POST URL to send the data to
 * @param callbackSuccess Success callback function reference
 * @param callbackFailure Failure callback function reference
 * @param extraCallbackData data that can be used in the callback aside from the data passed from the front end
 */
AJAXHelper.prototype.performPostRequest = function (dataObject, url, callbackSuccess, callbackFailure, extraCallbackData) {
    jQuery.ajax({
        url: url,
        data: dataObject,
        cache: false,
        contentType: false,
        processData: false,
        headers: {
            'Access-Control-Allow-Origin': '*',
            "X-CSRF-TOKEN": AJAXSettings.csrfToken
        },
        type: 'POST',
        success: function (data) {
            if (extraCallbackData != null) {
                data.extraCallbackData = extraCallbackData;
            }

            if (data.isSuccess) {
                callbackSuccess(data);
            } else {
                callbackFailure(data);
            }
        },
        error: function (data)
        {
            if (extraCallbackData != null) {
                data.extraCallbackData = extraCallbackData;
            }

            callbackFailure(data);
        }
    });
};

/**
 * Performs an AJAX generic GET request
 *
 * @param dataObject JS Object with key/value pairs assigned to it for params
 * @param url GET URL to send the data to
 * @param callbackSuccess Success callback function reference
 * @param callbackFailure Failure callback function reference
 * @param extraCallbackData data that can be used in the callback aside from the data passed from the front end
 */
AJAXHelper.prototype.performGetRequest = function (dataObject, url, callbackSuccess, callbackFailure, extraCallbackData) {
    jQuery.ajax({
        url: url,
        data: jQuery.param(dataObject),
        cache: false,
        contentType: false,
        processData: false,
        headers: {
            'Access-Control-Allow-Origin': '*',
            "X-CSRF-TOKEN": AJAXSettings.csrfToken
        },
        type: 'GET',
        success: function (data) {
            if (extraCallbackData != null) {
                data.extraCallbackData = extraCallbackData;
            }

            if (data.isSuccess) {
                callbackSuccess(data);
            } else {
                callbackFailure(data);
            }
        },
        error: function (data) {
            if (extraCallbackData != null) {
                data.extraCallbackData = extraCallbackData;
            }

            callbackFailure(data);
        }
    });
};

/**
 * Helper function for creating data objects capable of being used with a POST AJAX request
 *
 * @param paramName The "key" or parameter name expected by the endpoint
 * @param paramValue The value of the parameter expected by the endpoint
 * @param dataObject A previously created data object, if available.  Null otherwise for auto-creation.
 * @returns The resulting FormData object containing the paramName/paramValue combination within
 */
AJAXHelper.prototype.createOrAppendPostDataObject = function (paramName, paramValue, dataObject) {
    var rval = dataObject;

    if (rval == null) {
        rval = new FormData();
    }

    rval.append(paramName, paramValue);

    return rval;
};

/**
 * Helper function for creating data objects capable of being used with a GET AJAX request
 *
 * @param paramName The "key" or parameter name expected by the endpoint
 * @param paramValue paramValue The value of the parameter expected by the endpoint
 * @param dataObject dataObject A previously created data object, if available.  Null otherwise for auto-creation.
 * @returns The resulting Object object containing the paramName/paramValue combination within
 */
AJAXHelper.prototype.createOrAppendGetDataObject = function (paramName, paramValue, dataObject) {
    var rval = dataObject;

    if (rval == null) {
        rval = Object.create(null);
    }

    rval[paramName] = paramValue;

    return rval;
};

/**
 * Generates a one-size-fits-all success callback object in the event that it isn't desirable to create one custom.
 *
 * @param redirectUrl The URL to redirect the user to after a successful action has been carried out.
 * @returns {Function} The function that handles the success case.
 */
AJAXHelper.prototype.createGenericSuccessRedirectCallback = function(redirectUrl) {
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
AJAXHelper.prototype.createGenericFailureCallback = function(errorMessageObjectHandle) {
    return function(data) {

        var errorMessageDiv = $(errorMessageObjectHandle);

        if (data.errorMessage != null && data.errorMessage.length > 0) {
            var errorMessage = data.errorMessage;
        }
        else {
            errorMessage = "Oops!  Something went wrong.  Please try that again.";
        }

        if (!errorMessageDiv.length) {
            console.warn(errorMessage);
            return;
        }

        errorMessageDiv.html(errorMessage);
        errorMessageDiv.show();
    }
};
