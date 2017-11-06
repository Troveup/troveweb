
(function(){
    // functions that might be useful for the product hunt logging or otherwise
    namespace("PHModule", logClick);

    function logClick(eventString, csrfToken) {
        var logData = new FormData();
        logData.append("timeOnPage", performance.now()); // time since user clicked a link on the previous page
        logData.append("buttonClicked", eventString);

        // FIXME
        //console.log(logData.getAll());

        jQuery.ajax({
            url: "/trackclick",
            data: logData,
            cache: false,
            contentType: false,
            processData: false,
            headers: {
                'Access-Control-Allow-Origin': '*',
                "X-CSRF-TOKEN": csrfToken
            },
            type: 'POST',
            success: function(data){}
        });
    }

})();

