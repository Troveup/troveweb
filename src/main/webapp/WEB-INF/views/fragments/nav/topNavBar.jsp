<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:import url="../fragments/modals/holidayModal.jsp"/>
<c:import url="../fragments/modals/emailModal.jsp"/>


<div style="display: none;" class="addtobagpop">
    <h2>Added to Your Bag!</h2>
    <a data-role="none" id="addtobagcheckout" href="/private/bag" class="btn btn-checkoutpop">Checkout &nbsp;&nbsp;></a>
</div>

<div class="header">
    <nav class='navbar navbar-trove navbar-fixed-top' role='navigation'>
        <div class="dropdown-container jam">
            <ul class="dropdown-items-hamburger">
                <%--li><a data-role="none" id="about_featured_hamburger" href="/jaleesamoses">Featured</a></li--%>
                <li><a data-role="none" id="about_rings" href="/rings">Rings</a></li>
                <li><a data-role="none" id="about_bracelets" href="/bracelets">Bracelets</a></li>
                <li><a data-role="none" id="about_necklaces" href="/necklaces">Necklaces</a></li>
                <!-- <li><a data-role="none" id="about_giftcards" href="/giftcard">Gift Cards</a></li> -->
                <li><a data-role="none" id="about_contactus" href="/about">About</a></li>
                <li><a data-role="none" id="about_faq" class="hidemob" href="/faq">FAQ</a></li>
                <li><a data-role="none" id="about_legal" class="hidemob" href="/privacy">Legal</a></li>
            </ul>
        </div>

        <%-- ONLY SHOWN TO AUTHENTICATED USER --%>
        <div id="dropdowncontainer" ${not isAuthenticated ? 'style=\"display: none;\"' : ''} class="dropdown-container pro">
            <ul class="dropdown-items">
                <c:if test="${not empty authUser and not empty authUser.influencer and authUser.influencer}">
                <li><a data-role="none" id="topnav_my_sales_dropdown" href="/dashboard/influencer/${authUser.userId}" class="dropdown-item">My
                    Sales</a></li>
                </c:if>
                <li><a data-role="none" id="topnav_trove_dropdown" href="/private/user" class="dropdown-item">My
                    Trove</a></li>
                <li><a data-role="none" id="topnav_account_dropdown" href="/account/settings" class="dropdown-item">Account
                    Settings</a></li>
                <li><a data-role="none" id="topnav_orders_dropdown" href="/private/orders" class="dropdown-item">Orders
                    and Try-On</a></li>
                <!-- <li><a data-role="none" id="topnav_giftcard_dropdown" href="/giftcard" class="dropdown-item">Gift
                    Cards</a></li> -->
                <li><a data-role="none" id="topnav_refer_dropdown" href="/freering" class="dropdown-item">Refer a
                    Friend</a></li>
                <li><a data-role="none" href="javascript:{}"
                       onclick="document.getElementById('so').submit(); return false;">Sign Out</a></li>
                <form id="so" action="${pageContext.request.contextPath}/signout" method="POST"><input type="hidden"
                                                                                                       name="${_csrf.parameterName}"
                                                                                                       value="${_csrf.token}"/>
                </form>
            </ul>
        </div>
        <div id="profilecontainer" ${not isAuthenticated ? 'style=\"display: none;\"' : ''} class="box profile">
            <div class="tallyho-profile" style="display:none;">0</div>
            <a data-role="none" id="topnav_trove_profile_photo_mobile" href="/private/user"
               class="topnav-icon${pageContext.request.requestURI eq '/WEB-INF/views/user/profile.jsp' ? ' active' : ' '}">
                <img id="profilethumbnail" src="${authUser.profileImageThumbnailPath}" alt=" "
                     class="topnav_avatar">
                <span class="topnav-text">You</span>
            </a>
        </div>
        <div id="bagcountercontainer" class="box cart ${not isAuthenticated ? 'special' : ''}">
                <div id="bagcounter" class="tallyho-cart ${bagItemCount > 0 ? 'showitnow' : ''} ${not isAuthenticated ? 'tallyho-cart-unauthed' : 'tallyho-cart-authed'}">${bagItemCount}</div>
            <a data-role="none" id="topnav_bag" href="/private/bag"
               class="topnav-icon${pageContext.request.requestURI eq '/WEB-INF/views/user/bag.jsp' ? ' active' : ' '}">
                <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/shopping-bag-lightweight-2.svg"
                     class="topnav_icon">
                <span class="topnav-text" ${not isAuthenticated ? 'style=\"display: none;\"' : ''}>Bag</span>
            </a>
        </div>
        <a data-role="none" ${not isAuthenticated ? 'style=\"display: none;\"' : ''} id="topnav_trove_profile_photo"
           href="/private/user">
            <div class="protoggle pro"></div>
        </a>
        <%-- END --%>

        <%-- ANONYMOUS USER --%>
        <%--TODO:  Remove auth button styling from cards.css when nav gets refactored--%>
            <div class="btn-group" ${isAuthenticated ? 'style=\"display: none;\"' : ''} role="group" id="cta-buttons">
                <button data-role="none" type="button" onclick="$(location).attr('href','/signin');"
                        class="btn btn_login hidemob">Log In
                </button>
                <button data-role="none" type="button" onclick="$(location).attr('href','/signup');"
                        class="btn btn_signup hidemob">Sign Up
                </button>
            </div>
        <%-- END --%>

        <div class="hamtoggle jam"></div>
        <button id="navtog" class='navbar-toggle' data-role="none" data-target="#nav-col" data-toggle='collapse'
                type='button'>
            <span class='sr-only'></span>
            <span class='icon-bar'></span>
            <span class='icon-bar'></span>
            <span class='icon-bar'></span>
        </button>

        <div class='navbar-inner'>
            <div class='nav-center'>
                <!--<a data-role="none" id="topnav_home" href="/welcome"><img alt="Trovelogo" class="nav-logo" src="https://storage.googleapis.com/troveup-imagestore/assets/img/logo-lt-gray.svg"></a>-->
                <a data-role="none" id="topnav_home" href="/welcome"><img alt="Trovelogo" class="nav-logo"
                                                                          src="https://storage.googleapis.com/troveup-imagestore/assets/img/logo-text.png"></a>
            </div>
        </div>
        <div class='collapse navbar-collapse' id="nav-col">
            <div class='text-center' id='filters'>
                <c:if test="${not empty authUser and not empty authUser.influencer and authUser.influencer}">
                <a data-role="none" href="/dashboard/influencer/${authUser.userId}" class="mobile">My
                    Sales</a>
                </c:if>
                <a data-role="none" id="topnav_featured"
                   class="nomob feat ${pageContext.request.requestURI eq '/WEB-INF/views/featured/kimberlysmith.jsp' ? 'active' : ' '}"
                   href='/ppfgirl'>Featured</a>
                  <div class="dropdown-container feat">
                    <ul class="dropdown-items-featured">
                      <li><a data-role="none" id="kimberly_featured" href="/ppfgirl">KIMBERLY SMITH</a></li>
                      <%--li><a data-role="none" id="jaleesa_featured" href="/jaleesamoses">JALEESA MOSES</a></li--%>
                    </ul>
                  </div>
                <a data-role="none" id="topnav_shop"
                   class="nomob shop"
                   href='/rings'>Shop</a>
                <div class="dropdown-container shop">
                  <ul class="dropdown-items-shop">
                    <li><a data-role="none" id="about_shop_rings" href="/rings">Rings</a></li>
                    <li><a data-role="none" id="about_shop_bracelets" href="/bracelets">Bracelets</a></li>
                    <li><a data-role="none" id="about_shop_necklaces" href="/necklaces">Necklaces</a></li>
                    <%--li><a data-role="none" id="about_shop_jaleese" href="/jaleesamoses">JALEESA MOSES</a></li--%>
                    <li><a data-role="none" id="about_shop_kimberly" href="/ppfgirl">KIMBERLY SMITH</a></li>
                  </ul>
                </div>
                <a data-role="none" id="topnav_kimberly"
                   class="mobile ${pageContext.request.requestURI eq '/WEB-INF/views/featured/kimberlysmith.jsp' ? 'active' : ' '}"
                   href="/ppfgirl">KIMBERLY SMITH</a>
                <%--a data-role="none" id="topnav_jaleesa"
                   class="mobile ${pageContext.request.requestURI eq '/WEB-INF/views/featured/jaleesamoses.jsp' ? 'active' : ' '}"
                   href="/jaleesamoses">JALEESA MOSES</a--%>
                <a data-role="none" id="topnav_rings"
                   class="mobile ${pageContext.request.requestURI eq '/WEB-INF/views/public/rings.jsp' ? 'active' : ' '}"
                   href="/rings">Rings</a>
                <a data-role="none" id="topnav_bracelets"
                   class="mobile ${pageContext.request.requestURI eq '/WEB-INF/views/public/bracelets.jsp' ? 'active' : ' '}"
                   href="/bracelets">Bracelets</a>
                <a data-role="none" id="topnav_necklaces"
                   class="mobile ${pageContext.request.requestURI eq '/WEB-INF/views/public/necklaces.jsp' ? 'active' : ' '}"
                   href="/necklaces">Necklaces</a>
                <a data-role="none" id="topnav_giftcards"
                   class="${pageContext.request.requestURI eq '/WEB-INF/views/public/landing.jsp' ? 'active' : ' '}"
                   href="/sellers">Sell</a>
                <!-- <a data-role="none" id="topnav_giftcards"
                   class="${pageContext.request.requestURI eq '/WEB-INF/views/public/giftcards.jsp' ? 'active' : ' '}"
                   href="/giftcard">Gift Card</a> -->
                <a data-role="none" id="topnav_about" class="mobile" href="/about">About</a>
                <%-- ONLY SHOWN TO AUTHENTICATED USER --%>
                <c:if test="${isAuthenticated}">
                    <a data-role="none" id="topnav_orders" class="mobile" href="/private/orders">Orders</a>
                    <a data-role="none" class="mobile" href="javascript:{}"
                       onclick="document.getElementById('so').submit(); return false;">Sign Out</a>
                    <form id="so" action="${pageContext.request.contextPath}/signout" method="POST"><input
                            type="hidden"
                            name="${_csrf.parameterName}"
                            value="${_csrf.token}"/>
                    </form>
                </c:if>
                <%-- END --%>
                <%-- ANONYMOUS USER --%>
                <c:if test="${not isAuthenticated}">
                    <a data-role="none" id="topnav_faq" class="mobile" href="/faq">FAQ</a>
                    <a data-role="none" id="topnav_legal" class="mobile" href="/legal">Legal</a>
                    <a data-role="none" id="topnav_signin" class="mobile" href="/signin">Log in</a>
                    <!-- <button data-role="none" type="button" onclick="$(location).attr('href','/signup');"
                            class="btn btn_big btn_big_red mobile">Sign Up
                    </button> -->
                </c:if>
                <%-- END --%>
            </div>
        </div>
        <%-- Alert Banner --%>
        <c:if test="${not empty alertBannerState and alertBannerState.bannerState == 'ENABLED'}">
            <div class="special-container">
                <div class="container center">
                    <p class="special-message">${alertBannerState.bannerText}</p>
                    <p class="special-message mobspec">${alertBannerState.mobileBannerText}</p>
                    <c:if test="${alertBannerState.buttonState == 'ENABLED'}">
                        <%--This button is not accessibility compliant.  When accessibility becomes priority, modify it accordingly:
                        http://stackoverflow.com/questions/2906582/how-to-create-an-html-button-that-acts-like-a-link --%>
                        <button type="button" onclick="window.location.href='${alertBannerState.buttonUrl}'"
                                class="btn btn_specialsignup">${alertBannerState.buttonText}</button>
                    </c:if>
                </div>
            </div>
        </c:if>

        <%-- Admin "Show Me!" Banner --%>
        <c:if test="${not empty adminAlertPanel}">
            <div class="special-container" style="display: none;" id="adminbanner">
                <div class="container center">
                    <p class="special-message" id="adminbannermessage"></p>
                    <p class="special-message mobspec" id="adminmobilebannermessage"></p>
                    <button id="adminbannerbutton" type="button" class="btn btn_specialsignup"></button>
                </div>
            </div>
        </c:if>


    </nav>
</div>

<%--c:import url="../fragments/modals/giftModal.jsp"/--%>

<%-- ONLY ANONYMOUS USER --%>
<c:if test="${not isAuthenticated}">
    <script type="text/javascript">
        $("#about_sellers").click(function () {
            mixpanel.track("about_sellers_ANON");
        });
        $("#about_tryon").click(function () {
            mixpanel.track("about_tryon_ANON");
        });
        $("#about_freering").click(function () {
            mixpanel.track("about_freering_ANON");
        });
        $("#about_rings").click(function () {
            mixpanel.track("about_rings_ANON");
        });
        $("#about_bracelets").click(function () {
            mixpanel.track("about_bracelets_ANON");
        });
        $("#about_necklaces").click(function () {
            mixpanel.track("about_necklaces_ANON");
        });
        $("#about_featured").click(function () {
            mixpanel.track("about_featured_ANON");
        });
        $("#about_contactus").click(function () {
            mixpanel.track("about_contactus_ANON");
        });
        $("#about_legal").click(function () {
            mixpanel.track("about_legal_ANON");
        });
        $("#topnav_home").click(function () {
            mixpanel.track("topnav_home_ANON");
        });
        $("#topnav_sellers").click(function () {
            mixpanel.track("topnav_feed_ANON");
        });
        $("#topnav_sellerss").click(function () {
            mixpanel.track("topnav_feed_ANON");
        });
        $("#topnav_rings").click(function () {
            mixpanel.track("topnav_rings_ANON");
        });
        $("#topnav_bracelets").click(function () {
            mixpanel.track("topnav_bracelets_ANON");
        });
        $("#topnav_necklaces").click(function () {
            mixpanel.track("topnav_necklaces_ANON");
        });
        $("#topnav_featured").click(function () {
            mixpanel.track("topnav_featured_ANON");
        });
        $("#topnav_bag").click(function () {
            mixpanel.track("topnav_bag_ANON");
        });
        $("#about_giftcards").click(function () {
            mixpanel.track("about_giftcards_ANON");
        });
        $("#topnav_giftcards").click(function () {
            mixpanel.track("topnav_giftcards_ANON");
        });
        $("#topnav_signin").click(function () {
            mixpanel.track("topnav_signin_ANON");
        });
    </script>
</c:if>
<%-- END --%>

<%-- ONLY AUTHENTICATED USER --%>
<c:if test="${isAuthenticated}">
    <script type="text/javascript">
        $("#about_sellers").click(function () {
            mixpanel.track("about_sellers");
        });
        $("#about_tryon").click(function () {
            mixpanel.track("about_tryon");
        });
        $("#about_rings").click(function () {
            mixpanel.track("about_rings");
        });
        $("#about_bracelets").click(function () {
            mixpanel.track("about_bracelets");
        });
        $("#about_necklaces").click(function () {
            mixpanel.track("about_necklaces");
        });
        $("#about_featured").click(function () {
            mixpanel.track("about_featured");
        });
        $("#about_contactus").click(function () {
            mixpanel.track("about_contactus");
        });
        $("#about_legal").click(function () {
            mixpanel.track("about_legal");
        });
        $("#topnav_home").click(function () {
            mixpanel.track("topnav_home");
        });
        $("#topnav_sellers").click(function () {
            mixpanel.track("topnav_feed");
        });
        $("#topnav_sellerss").click(function () {
            mixpanel.track("topnav_feed");
        });
        $("#topnav_rings").click(function () {
            mixpanel.track("topnav_rings");
        });
        $("#topnav_bracelets").click(function () {
            mixpanel.track("topnav_bracelets");
        });
        $("#topnav_necklaces").click(function () {
            mixpanel.track("topnav_necklaces");
        });
        $("#topnav_featured").click(function () {
            mixpanel.track("topnav_featured");
        });
        $("#topnav_bag").click(function () {
            mixpanel.track("topnav_bag");
        });
        $("#topnav_trove_dropdown").click(function () {
            mixpanel.track("topnav_trove_dropdown");
        });
        $("#topnav_account_dropdown").click(function () {
            mixpanel.track("topnav_account_dropdown");
        });
        $("#topnav_orders_dropdown").click(function () {
            mixpanel.track("topnav_orders_dropdown");
        });
        $("#topnav_trove_profile_photo").click(function () {
            mixpanel.track("topnav_trove_profile_photo");
        });
        $("#topnav_trove_profile_photo_mobile").click(function () {
            mixpanel.track("topnav_trove_profile_photo_mobile");
        });
        $("#topnav_trove").click(function () {
            mixpanel.track("topnav_trove");
        });
        $("#topnav_giftcards").click(function () {
            mixpanel.track("topnav_giftcards");
        });
        $("#topnav_giftcard_dropdown").click(function () {
            mixpanel.track("topnav_giftcard_dropdown");
        });

        mixpanel.identify('${authUser.userId}');

        // new people integration

        mixpanel.people.set({
            '$email': '${authUser.email}',
            'user_id': '${authUser.userId}',
            '$name': '${authUser.firstName}'
        });

        mixpanel.people.increment('page_views');

        head.ready(document, function () {
            var count = parseInt($('.tallyho-cart').html());
            if (count >= 1) {
                $('.tallyho-cart').addClass('showitnow');
            }
        });
    </script>
</c:if>
<%-- END --%>
<script type="text/javascript" src="/resources/js/trove/Nav.js"></script>
<script type="text/javascript">

    var authedNavBarElements = [
        "#dropdowncontainer",
        "#profilecontainer",
        "#bagcountercontainer",
        "#topnav_trove_profile_photo",
        ".topnav-text"];

    var unauthedNavBarElements = [
        "#cta-buttons",
        "#unauthedbag"];

    var userProfileImageElement = '#profilethumbnail';

    var navMixPanelElements = [
        "#about_feed",
        "#about_tryon",
        "#about_rings",
        "#about_bracelets",
        "#about_necklaces",
        "#about_featured",
        "#about_contactus",
        "#about_legal",
        "#topnav_home",
        "#topnav_feed",
        "#topnav_rings",
        "#topnav_bracelets",
        "#topnav_necklaces",
        "#topnav_featured",
        "#topnav_tryon",
        "#topnav_bag",
        "#topnav_trove_dropdown",
        "#topnav_account_dropdown",
        "#topnav_orders_dropdown",
        "#topnav_trove_profile_photo",
        "#topnav_trove_profile_photo_mobile",
        "#topnav_trove",
        "#topnav_giftcards",
        "#topnav_giftcard_dropdown"];

    head.ready(document, function () {
        $('#filters').on('click', 'a', function (event) {
            $('a').removeClass('active');
            $(this).addClass('active');
        });
        $('li').on('click', 'a', function (event) {
            $('a').removeClass('active');
            $(this).addClass('active');
        });
        $('.jam').on('mouseover', function () {
            $('.dropdown-container.jam').addClass('showtime');
        });
        $('.jam').on('mouseleave', function () {
            $('.dropdown-container.jam').removeClass('showtime');
        });

        $('.feat').on('mouseover', function () {
            $('.dropdown-container.feat').addClass('showtime');
        });
        $('.feat').on('mouseleave', function () {
            $('.dropdown-container.feat').removeClass('showtime');
        });


        $('.shop').on('mouseover', function () {
            $('.dropdown-container.shop').addClass('showtime');
        });
        $('.shop').on('mouseleave', function () {
            $('.dropdown-container.shop').removeClass('showtime');
        });


        $('.pro').on('mouseover', function () {
            $('.dropdown-container.pro').addClass('showtime');
        });
        $('.pro').on('mouseleave', function () {
            $('.dropdown-container.pro').removeClass('showtime');
        });
    });

    function getOtherAuthenticationActionsToPerform() {
        return function() {
            $('#bagcountercontainer').removeClass('special');
        }
    }

    var navHelper = new NavHelper(authedNavBarElements, unauthedNavBarElements, userProfileImageElement, navMixPanelElements, getOtherAuthenticationActionsToPerform());

    function bagButtonClick() {
        window.location.href='/private/bag';
    }
</script>


