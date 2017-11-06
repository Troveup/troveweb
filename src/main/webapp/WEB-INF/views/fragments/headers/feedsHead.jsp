<script src="/resources/js/vendor/handlebars.js"></script>
<script type="text/javascript" src="/resources/js/vendor/nprogress.js"></script>
<link rel="stylesheet" href="/resources/stylesheets/main.css">
<link rel="stylesheet" href="/resources/stylesheets/browse.css">
<link rel="stylesheet" href="/resources/stylesheets/cards.css">

<script>

	function resizebanner() {
	  var minh = Math.min($(window).width() * (335 / 1100), 335);
	  $('.bannerheight').css({
	    'min-height':minh+'px'
	  });
	}

    $(document).ready(function() {
   	  resizebanner();
    });

    $(window).on('resize', function() {
      resizebanner();
    });

</script>


<link rel="stylesheet" href="/resources/stylesheets/authmod.css">