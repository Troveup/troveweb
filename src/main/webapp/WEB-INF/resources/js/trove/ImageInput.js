function ImageInputHelper() {

}

/**
 * Helper function for retrieving the source of a file specified by an input
 * @param inputElementId
 * @param args
 * @returns {goog.Promise}
 */
ImageInputHelper.prototype.getRawSource = function(inputElementId, args) {
    return new Promise(function (resolve, reject) {
        var input = $('#' + inputElementId);
        var reader = new FileReader();

        if ($(input)[0].files != null && $(input)[0].files.length > 0) {
            reader.readAsDataURL(input[0].files[0]);

            reader.onload = function () {
                var returnResult = {
                    args: args,
                    result: reader.result
                };
                resolve(returnResult);
            };

            reader.onerror = function (e) {
                reject(e);
            }
        } else {
            var returnResult = {
                args: null,
                result: null
            };
            resolve(returnResult);
        }
    });
};
