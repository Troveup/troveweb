<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- navBar -->
<div class="header">
  <nav class='navbar navbar-trove navbar-fixed-top' role='navigation'>
    <!-- hamburger dropdown -->
    <div class="dropdown-container jam">
      <ul class="dropdown-items-hamburger">
        <li><a href='/public/phfeed'>Feed</a></li>
        <li><a href="/public/phrings">Rings</a></li>
        <li><a href="/public/phbracelets">Bracelets</a></li>
        <!-- <li><a href="/necklaces">Necklaces</a></li> -->
        <!-- <li><a href="/featured">Featured</a></li> -->
        <!-- <li><a href="/trending">Trending</a></li> -->
        <li><a href="/public/phabout">About</a></li>
        <!-- <li><a href="/privacy">Legal</a></li> -->
      </ul>
    </div>
    <!-- user dropdown -->
    <div class="dropdown-container pro">
      <ul class="dropdown-items">
        <li><a href="#" data-toggle="modal" data-target="#modal-ph" class="dropdown-item">My Trove</a></li>
        <li><a href="#" data-toggle="modal" data-target="#modal-ph" class="dropdown-item">Account Settings</a></li>
        <!-- <li><a href="#" data-toggle="modal" data-target="#modal-ph" class="dropdown-item">Orders</a></li> -->
        <!-- <li><a href="/help" class="dropdown-item">Help</a></li> -->
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
      <a href="#" data-toggle="modal" data-target="#modal-ph" class="topnav-icon${pageContext.request.requestURI eq '/WEB-INF/views/user/profile.jsp' ? ' active' : ' '}">
        <img id="profilethumbnail" src="${authUser.profileImageThumbnailPath}" alt=" " class="topnav_avatar">
        <span class="topnav-text">You</span>
      </a>
    </div>
    <!-- shopping bag -->
    <div class="box cart">
      <c:if test="${authUser.bagItemCount > 0}"><div class="tallyho-cart">${authUser.bagItemCount}</div></c:if>
      
      <a href="#" data-toggle="modal" data-target="#modal-ph" class="topnav-icon${pageContext.request.requestURI eq '/WEB-INF/views/user/bag.jsp' ? ' active' : ' '}">
        <img src="/resources/img/shopping-bag-icon.svg" class="topnav_icon">
        <span class="topnav-text">Bag</span>
      </a>
    </div>
    <!-- triggers for dropdowns hidden on mobile -->
    <a href="#" data-toggle="modal" data-target="#modal-ph"><div class="protoggle pro"></div></a>
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
        <a href="/"><img alt="Trovelogo" class="nav-logo" src="/resources/img/trove-logo-beta.png"></a>
      </div>
    </div>
    <!-- main nav links -->
    <div class='collapse navbar-collapse'>
      <div class='text-center' id='filters'>
        <a class="${pageContext.request.requestURI eq '/WEB-INF/views/user/profile.jsp' ? 'active' : ' '}" href="#" data-toggle="modal" data-target="#modal-ph">My Trove</a>
        <a class="${pageContext.request.requestURI eq '/WEB-INF/views/producthunt/phfeed.jsp' ? 'active' : ' '}" href='/public/phfeed'>Feed</a>
        <a class="${pageContext.request.requestURI eq '/WEB-INF/views/producthunt/phrings.jsp' ? 'active' : ' '}" href="/public/phrings">Rings</a>
        <a class="${pageContext.request.requestURI eq '/WEB-INF/views/producthunt/phbracelets.jsp' ? 'active' : ' '}" href="/public/phbracelets">Bracelets</a>
        <a class="${pageContext.request.requestURI eq '/WEB-INF/views/producthunt/phabout.jsp' ? 'active' : ' '}" href="/public/phabout">About</a>
        <!-- <a class="${pageContext.request.requestURI eq '/WEB-INF/views/public/necklaces.jsp' ? 'active' : ' '}" href="/necklaces">Necklaces</a> -->
        <!-- <a class="${pageContext.request.requestURI eq '/WEB-INF/views/public/featured.jsp' ? 'active' : ' '}" href="/featured">Featured</a> -->
        <!-- <a class="${pageContext.request.requestURI eq '/WEB-INF/views/public/trending.jsp' ? 'active' : ' '}" href="/trending">Trending</a> -->
      </div>
    </div>
  </nav>
</div>
