<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="header">
  <nav class='navbar navbar-trove navbar-fixed-top' role='navigation'>
    <div class="dropdown-container jam">
      <ul class="dropdown-items-hamburger">
        <li><a id="about_feed" href='/'>Feed</a></li>
        <li><a id="about_rings" href="/rings">Rings</a></li>
        <li><a id="about_bracelets" href="/bracelets">Bracelets</a></li>
        <li><a id="about_necklaces" href="/necklaces">Necklaces</a></li>
        <li><a id="about_tryon" href="/tryon">Try-On</a></li>
        <!-- <li><a id="about_featured" href="/featured">Featured</a></li> -->
        <li><a id="about_contactus" href="/about">About</a></li>
        <li><a id="about_legal" href="/privacy">Legal</a></li>
      </ul>
    </div>

    <div class="hamtoggle jam"></div>
    <button class='navbar-toggle' data-target="#nav-col" data-toggle='collapse' type='button'>
      <span class='sr-only'></span>
      <span class='icon-bar'></span>
      <span class='icon-bar'></span>
      <span class='icon-bar'></span>
    </button>
    <div class='navbar-inner'>
      <div class='nav-center'>
        <a data-role="none" id="topnav_home" href="/welcome"><img alt="Trovelogo" class="nav-logo" src="https://storage.googleapis.com/troveup-imagestore/assets/img/logo-lt-gray.svg"></a>
      </div>
    </div>
    <div class='collapse navbar-collapse' id="nav-col">
      <div class='text-center' id='filters'>
        <!-- <a id="topnav_featured" class="${pageContext.request.requestURI eq '/WEB-INF/views/public/featured.jsp' ? 'active' : ' '}" href="/featured">Featured</a> -->
        <!-- <a id="topnav_feed" class="${pageContext.request.requestURI eq '/WEB-INF/views/public/feed.jsp' ? 'active' : ' '}" href='/'>Feed</a> -->
        <!-- <a id="topnav_rings" class="${pageContext.request.requestURI eq '/WEB-INF/views/public/rings.jsp' ? 'active' : ' '}" href="/rings">Rings</a> -->
        <!-- <a id="topnav_bracelets" class="${pageContext.request.requestURI eq '/WEB-INF/views/public/bracelets.jsp' ? 'active' : ' '}" href="/bracelets">Bracelets</a> -->
        <!-- <a id="topnav_necklaces" class="${pageContext.request.requestURI eq '/WEB-INF/views/public/necklaces.jsp' ? 'active' : ' '}" href="/necklaces">Necklaces</a> -->
      </div>
    </div>
  </nav>
</div>

<script type="text/javascript">
  $("#about_feed").click(function () {                         mixpanel.track("about_feed");                        });
  $("#about_rings").click(function () {                        mixpanel.track("about_rings");                       });
  $("#about_bracelets").click(function () {                    mixpanel.track("about_bracelets");                   });
  $("#about_necklaces").click(function () {                    mixpanel.track("about_necklaces");                   });
  $("#about_featured").click(function () {                     mixpanel.track("about_featured");                    });
  $("#about_contactus").click(function () {                    mixpanel.track("about_contactus");                   });
  $("#about_legal").click(function () {                        mixpanel.track("about_legal");                       });
  $("#topnav_home").click(function () {                        mixpanel.track("topnav_home");                       });
  $("#topnav_feed").click(function () {                        mixpanel.track("topnav_feed");                       });
  $("#topnav_rings").click(function () {                       mixpanel.track("topnav_rings");                      });
  $("#topnav_bracelets").click(function () {                   mixpanel.track("topnav_bracelets");                  });
  $("#topnav_necklaces").click(function () {                   mixpanel.track("topnav_necklaces");                  });
  $("#topnav_featured").click(function () {                    mixpanel.track("topnav_featured");                   });
  $("#topnav_bag").click(function () {                         mixpanel.track("topnav_bag");                        });
  $("#topnav_trove_dropdown").click(function () {              mixpanel.track("topnav_trove_dropdown");             });
  $("#topnav_account_dropdown").click(function () {            mixpanel.track("topnav_account_dropdown");           });
  $("#topnav_orders_dropdown").click(function () {             mixpanel.track("topnav_orders_dropdown");            });
  $("#topnav_trove_profile_photo").click(function () {         mixpanel.track("topnav_trove_profile_photo");        });
  $("#topnav_trove_profile_photo_mobile").click(function () {  mixpanel.track("topnav_trove_profile_photo_mobile"); });
  $("#topnav_trove").click(function () {                       mixpanel.track("topnav_trove");                      });

  mixpanel.identify('${authUser.userId}');
  mixpanel.register({
    'Email': '${authUser.email}',
    'Name': '${authUser.firstName}'
  });

  head.ready(document, function () {
    var count = parseInt($('.tallyho-cart').html());
    if ( count >= 1 ) {
      $('.tallyho-cart').addClass('showitnow');
    }
    $('#filters').on( 'click', 'a', function( event ) {
      $('a').removeClass('active');
      $(this).addClass('active');
    });
    $('li').on( 'click', 'a', function( event ) {
      $('a').removeClass('active');
      $(this).addClass('active');
    });
    $('.jam').on('mouseover', function() {
      $('.dropdown-container.jam').addClass('showtime');
    });
    $('.jam').on('mouseleave', function() {
      $('.dropdown-container.jam').removeClass('showtime');
    });
    $('.pro').on('mouseover', function() {
      $('.dropdown-container.pro').addClass('showtime');
    });
    $('.pro').on('mouseleave', function() {
      $('.dropdown-container.pro').removeClass('showtime');
    });

  });
</script>


