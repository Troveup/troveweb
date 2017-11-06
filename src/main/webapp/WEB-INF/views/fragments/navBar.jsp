<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- navBar -->
<div class="header">
  <nav class='navbar navbar-trove navbar-fixed-top' role='navigation'>
    <!-- hamburger dropdown -->
    <div class="dropdown-container jam">
      <ul class="dropdown-items-hamburger">
        <li><a id="about_feed" href='/feed'>Feed</a></li>
        <li><a id="about_rings" href="/rings">Rings</a></li>
        <li><a id="about_bracelets" href="/bracelets">Bracelets</a></li>
        <li><a id="about_necklaces" href="/necklaces">Necklaces</a></li>
        <!-- <li><a id="about_featured" href="/featured">Featured</a></li> -->
        <li><a id="about_tryon" href="/try">Try-On</a></li>
        <li><a id="about_contactus" href="/about">About</a></li>
        <li><a id="about_faq" href="/faq">FAQ</a></li>
        <li><a id="about_legal" href="/privacy">Legal</a></li>
        <%--<li><a id="topnav_trending_dropdown" href="/trending">Trending</a></li>--%>
      </ul>
    </div>
    <!-- user dropdown -->
    <div class="dropdown-container pro">
      <ul class="dropdown-items">
        <li><a id="topnav_trove_dropdown" href="/private/user" class="dropdown-item">My Trove</a></li>
        <li><a id="topnav_account_dropdown" href="/account/settings" class="dropdown-item">Account Settings</a></li>
        <li><a id="topnav_orders_dropdown" href="/private/orders" class="dropdown-item">Orders and Try-On</a></li>
        <!-- <li><a href="/help" class="dropdown-item">Help</a></li> -->
        <li><a href="javascript:{}" onclick="document.getElementById('so').submit(); return false;">Sign Out</a></li>
        <form id="so" action="${pageContext.request.contextPath}/signout" method="POST"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
      </ul>
    </div>
    <!-- searchbar -->
    <!-- <div class="box search-bar">
      <div class="container-1">
        <a href=""><img src="/resources/img/search-icon.svg" class="icon-search icon search-btn" style="width: 20px;"></a>
        <input type="search" id="search" placeholder="Search..." /> 
      </div>
    </div> -->
    <!-- search icon only visible on mobile -->
    <!-- <div class="box search">
      <a href=""><img src="/resources/img/search-icon.svg" class="icon-search icon search-btn"></a>
    </div> -->
    <!-- user profile -->
    <div class="box profile">
      <div class="tallyho-profile" style="display:none;">0</div>
      <a id="topnav_trove_profile_photo_mobile" href="/private/user" class="topnav-icon${pageContext.request.requestURI eq '/WEB-INF/views/user/profile.jsp' ? ' active' : ' '}">
        <img id="profilethumbnail" src="${authUser.profileImageThumbnailPath}" alt=" " class="topnav_avatar">
        <span class="topnav-text">You</span>
      </a>
    </div>
    <!-- shopping bag -->
    <div class="box cart">
      <c:if test="${authUser.bagItemCount > 0}"><div class="tallyho-cart">${authUser.bagItemCount}</div></c:if>
      
      <a id="topnav_bag" href="/private/bag" class="topnav-icon${pageContext.request.requestURI eq '/WEB-INF/views/user/bag.jsp' ? ' active' : ' '}">
        <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/shopping-bag-lightweight-2.svg" class="topnav_icon">
        <span class="topnav-text">Bag</span>
      </a>
    </div>
    <!-- triggers for dropdowns hidden on mobile -->
    <a id="topnav_trove_profile_photo" href="/private/user"><div class="protoggle pro"></div></a>
    <div class="hamtoggle jam"></div>
    <!-- hamburger icon and mobile dropdown toggle-->
    <button class='navbar-toggle' data-target='.navbar-collapse' data-toggle='collapse' type='button'>
      <span class='sr-only'></span>
      <span class='icon-bar'></span>
      <span class='icon-bar'></span>
      <span class='icon-bar'></span>
    </button>

    <!-- trove logo -->
    <div class='navbar-inner'>
      <div class='nav-center'>
        <a id="topnav_home" href="/welcome"><img alt="Trovelogo" class="nav-logo" src="https://storage.googleapis.com/troveup-imagestore/assets/img/logo-lt-gray.svg"></a>
      </div>
    </div>
    <!-- main nav links -->
    <div class='collapse navbar-collapse'>
      <div class='text-center' id='filters'>
        <!-- <a id="topnav_trove" class="${pageContext.request.requestURI eq '/WEB-INF/views/user/profile.jsp' ? 'active' : ' '}" href="/private/user">My Trove</a> -->
        <!-- <a id="topnav_featured" class="${pageContext.request.requestURI eq '/WEB-INF/views/public/featured.jsp' ? 'active' : ' '}" href="/featured">Featured</a> -->
        <a id="topnav_feed" class="${pageContext.request.requestURI eq '/WEB-INF/views/public/feed.jsp' ? 'active' : ' '}" href='/feed'>Feed</a>
        <a id="topnav_rings" class="${pageContext.request.requestURI eq '/WEB-INF/views/public/rings.jsp' ? 'active' : ' '}" href="/rings">Rings</a>
        <a id="topnav_bracelets" class="${pageContext.request.requestURI eq '/WEB-INF/views/public/bracelets.jsp' ? 'active' : ' '}" href="/bracelets">Bracelets</a>
        <a id="topnav_necklaces" class="${pageContext.request.requestURI eq '/WEB-INF/views/public/necklaces.jsp' ? 'active' : ' '}" href="/necklaces">Necklaces</a>
        <a id="topnav_tryon" class="${pageContext.request.requestURI eq '/WEB-INF/views/public/tryon.jsp' ? 'active' : ' '}" href="/try">Try-On</a>
        <a class="mobile" href="javascript:{}" onclick="document.getElementById('so').submit(); return false;">Sign Out</a>
        <form id="so" action="${pageContext.request.contextPath}/signout" method="POST"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>  
        <%-- <a class="${pageContext.request.requestURI eq '/WEB-INF/views/public/trending.jsp' ? 'active' : ' '}" href="/trending">Trending</a> --%>
        <%-- <a class="${pageContext.request.requestURI eq '/WEB-INF/views/public/about.jsp' ? 'active' : ' '}" href="/about">About</a> --%>
      </div>
    </div>
  </nav>
</div>


<script type="text/javascript">
  $("#about_feed").click(function () {                         mixpanel.track("about_feed");                        });
  $("#about_tryon").click(function () {                         mixpanel.track("about_tryon");                      });
  $("#about_rings").click(function () {                        mixpanel.track("about_rings");                       });
  $("#about_bracelets").click(function () {                    mixpanel.track("about_bracelets");                   });
  $("#about_necklaces").click(function () {                    mixpanel.track("about_necklaces");                   });
  $("#about_featured").click(function () {                     mixpanel.track("about_featured");                    });
  $("#about_contactus").click(function () {                    mixpanel.track("about_contactus");                   });
  $("#about_legal").click(function () {                        mixpanel.track("about_legal");                       });
  $("#topnav_trove_dropdown").click(function () {              mixpanel.track("topnav_trove_dropdown");             });
  $("#topnav_account_dropdown").click(function () {            mixpanel.track("topnav_account_dropdown");           });
  $("#topnav_orders_dropdown").click(function () {             mixpanel.track("topnav_orders_dropdown");            });
  $("#topnav_trove_profile_photo").click(function () {         mixpanel.track("topnav_trove_profile_photo");        });
  $("#topnav_trove_profile_photo_mobile").click(function () {  mixpanel.track("topnav_trove_profile_photo_mobile"); });
  $("#topnav_bag").click(function () {                         mixpanel.track("topnav_bag");                        });
  $("#topnav_home").click(function () {                        mixpanel.track("topnav_home");                       });
  $("#topnav_trove").click(function () {                       mixpanel.track("topnav_trove");                      });
  $("#topnav_feed").click(function () {                        mixpanel.track("topnav_feed");                       });
  $("#topnav_tryon").click(function () {                       mixpanel.track("topnav_tryon");                      });
  $("#topnav_rings").click(function () {                       mixpanel.track("topnav_rings");                      });
  $("#topnav_bracelets").click(function () {                   mixpanel.track("topnav_bracelets");                  });
  $("#topnav_necklaces").click(function () {                   mixpanel.track("topnav_necklaces");                  });
  $("#topnav_featured").click(function () {                    mixpanel.track("topnav_featured");                   });

  mixpanel.identify('${authUser.userId}');

  mixpanel.register({
    'Email': '${authUser.email}',
    'Name': '${authUser.firstName}'
  });



</script>



