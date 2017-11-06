<link rel="stylesheet" href="/resources/stylesheets/theme-custom.css" rel="stylesheet">
<script type="text/javascript" src="https://code.jquery.com/jquery-1.11.3.min.js"></script>
<script>
    $(document).on("mobileinit", function () {
        $.mobile.ajaxEnabled = false;
        $.mobile.linkBindingEnabled = false;
    });

    $(document).bind("mobileinit", function(){
      $.mobile.page.prototype.options.keepNative = "a, button, input.slideinput, ul, li";
    });
</script>
<link href="https://code.jquery.com/mobile/1.4.5/jquery.mobile.structure-1.4.5.css" rel="stylesheet">
<script type="text/javascript" src="https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.js"></script>