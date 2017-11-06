$(document).on('keypress', function (e) {

    if (keyPressArrayMapping != null) {
        var potentialFunctionsToExecute = keyPressArrayMapping[e.keyCode];
        if (potentialFunctionsToExecute != null && Object.keys(potentialFunctionsToExecute).length > 0) {
            //Sometimes loses focus after pressing a key like enter, check the previous focus if this is null
            var id = $(e.currentTarget.activeElement)[0].id;

            if (potentialFunctionsToExecute[id] != null) {
                potentialFunctionsToExecute[id]();
            }
        }
    }
});

var keyPressArrayMapping = {};

function KeypressHelper() {
}

KeypressHelper.prototype.addKeyPressDetection = function (keyCodeNumber, elementId, functionToExecute) {

    if (keyPressArrayMapping[keyCodeNumber] == null) {
        keyPressArrayMapping[keyCodeNumber] = {};
    }

    keyPressArrayMapping[keyCodeNumber][elementId] = functionToExecute;
};

KeypressHelper.prototype.getEnterKeyCodeNumber = function () {
    return 13;
};