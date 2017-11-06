(function(){
    namespace("1SHOT", setToken );

    // make csrf token available to TROVE
    var csrfToken;
    function setToken(token) {
        csrfToken = token;
    }


})();
