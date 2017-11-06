<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
    <c:import url="../fragments/headers/commonHead.jsp"/>
    <c:import url="../fragments/headers/legalHead.jsp"/>
    <title>Trove Sellers</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=0">
    <link rel="icon" href="/resources/img/favicon.png?v=2">
    <link href='https://fonts.googleapis.com/css?family=Raleway:400,200,300,500,600,700' rel='stylesheet' type='text/css'>
    <link href="https://fonts.googleapis.com/css?family=Vollkorn:400,400italic,700,700italic" rel="stylesheet" type="text/css">
    <c:import url="../fragments/analytics/all.jsp"/>
    <link rel="stylesheet" href="/resources/stylesheets/spinner.css">

    <style>
      body {
        padding-top: 66px;
      }
      header {
        background: #FFF;
        height: 66px;
      }
      h4 {
        padding: 20px;
        font-weight: bold;
        text-align: center;
      }
      nav li {
        list-style: none;
        display: inline-block;
      }
      section {
        min-height: 800px;
        padding-bottom: 80px;
        background: #fff;
      }

      section.clear {
        background: transparent;
      }
      .wrapper {
        width: 100%;
        position: relative;
        padding: 0px;
        margin-top: 48px;
      }
      nav {
        position: absolute;
        left: 0;
        right: 0;
        background: #FFF;
        display: inline-block;
        width: 100%;
        padding: 0px;
        height: 48px;
        z-index: 100;
      }


      nav ul li a {
        font-family: "Raleway", Helvetica, sans-serif;
        text-transform: uppercase;
        color: #000;
        padding: 15px 10px 10px;
        display: block;
        text-decoration: none;
        margin: 0px 20px;
        border-bottom: 3px solid #FFF;
      }

      nav ul {
        text-align: center;
        margin: 0;
        padding: 0;
      }

      nav ul li a:focus {
        color: #000;
        text-decoration: none;
      }

      nav.navigate a:hover,
      nav.navigate a.ractive {
        text-decoration: none;
        border-bottom: 3px solid #000;
        color: #000;
      }
      .fixed {
        position: fixed;
        top: 0;
      } 
      .greey {
        background: #f0f0f0;
      }

      .bgim {
        background-image: url("https://storage.googleapis.com/trove-qa-teststore/how-it-works-background.png");
        background-size: cover;
      }

      #cta-buttons {
        position: absolute;
      }

      .navbar-fixed-top {
        position: absolute;
      }

      #nav-col.navbar-collapse {
        display: none !important;
      }

      nav ul li a#cta {
        background: #dd2435;
        padding: 8px 20px 5px;
        color: #FFF;
        border-bottom: 3px solid #dd2435;
      }

      nav ul li a#cta:hover {
        background: rgb(242, 104, 104);
        border-bottom: 3px solid rgb(242, 104, 104);
      }

      .img-circle {
        background: #e6e6e6;
      }

      .btn.btn-secondary {
        color: #232323;
        text-transform: uppercase;
        background-color: #FFFFFF;
        border: 2px solid #777;
        border-radius: 0px;
        margin-top: 30px;
        padding: 10px 25px;
      }

      .btn.btn-secondary:hover {
        background-color: #777;
        color: #FFFFFF;
      }

      .btn.btn-default {
        text-transform: uppercase;
        padding: 6px 15px;
        font-size: 18px;
        border-radius: 0px;
        border: 1px solid #dd2435;
        height: 40px;
        background: #dd2435;
      }

      .btn.btn-default:hover {
        border: 1px solid rgb(242, 104, 104);
        background: rgb(242, 104, 104);
      }

      .form-control.early-access {
        border-color: #000;
        text-align: left;
      }

      a {
        -webkit-transition: all 0.2s cubic-bezier(0.03, 1.02, 1, 1);
           -moz-transition: all 0.2s cubic-bezier(0.03, 1.02, 1, 1);
            -ms-transition: all 0.2s cubic-bezier(0.03, 1.02, 1, 1);
             -o-transition: all 0.2s cubic-bezier(0.03, 1.02, 1, 1);
                transition: all 0.2s cubic-bezier(0.03, 1.02, 1, 1);
      }

      p {
        margin: 0 auto 10px;
        max-width: 218px;
        line-height: 1.4em;
      }

      

      .container {
        text-align: center;
      }

      h1 {
        text-align: center;
        text-transform: uppercase;
        font-weight: 400;
        padding-top: 350px;
        line-height: 1.2;
        letter-spacing: 2px
      }

      #scrollbutt {
        display: block;
        height: 100px;
        padding-top: 30px;
      }
          

      h2 {
        text-align: center;
        font-size: 18px;
      }

      h3 {
        padding-top: 90px;
        padding-bottom: 90px;
      }

      .input-group {
        max-width: 500px;
        margin: 40 auto;
      }

      @-webkit-keyframes bounce {
        0%, 20%, 60%, 100%  { -webkit-transform: translateY(0); }
          40%  { -webkit-transform: translateY(-20px); }
        80% { -webkit-transform: translateY(-10px); }
      }


      .downarrow {
        text-align: center;
        cursor: pointer;
        display: block;
        margin: 0 auto;
        -webkit-animation: bounce 2s infinite ease-in-out;
      }

      .downarrow span {
        color: #000;
        font-size: 40px;
      }

      .row.spacing {
        margin-bottom: 80px;
      }

      .bg-img {
        position: fixed;
        top: 0;
        right: 0;
        left: 0;
        height: 100vh;
        background-size: cover;
        z-index: -1;
        background-image: url("https://storage.googleapis.com/trove-qa-teststore/bottom-banner-new.jpg");
        opacity: 0.6;
      }

      .hero {
        position: relative;
        background-size: cover;
        background-image: url("https://storage.googleapis.com/troveup-imagestore/assets/img/josephine-banner-5.jpg");
      }

      #holdit {
        content: "";
        padding-top: 21px;
        padding-bottom: 90px;
        margin-top: 0px;
      }

      .create h2 {
        width: 100%;
        margin: 0px auto 5px;
        text-align: left;
      }

      .create p {
        margin: 0 auto 20px;
        max-width: 100%;
        text-align: left;
      }     

      .norisk h2 {
        text-align: left;
        margin-top: 20px;
        margin-bottom: 10px;
      }

      .norisk p {
        text-align: left;
        margin: 0px;
        max-width: 270px;
      }

      .padimg {
        padding-top: 40px;
      }

      .pressrow {
        background: #F0F0F0;
        text-align: center;
        min-height: 210px;
      }

      .pressrow img {
        opacity: 0.4;
        max-width: 65%;
        margin: 2% auto;
      }

      .pressrow span {
        text-align: center;
        padding-top: 20px;
        padding-bottom: 20px;
      }

      .btn.btn-default {
        min-width: 220px;
      }

      .btn .spinner {
        width: 20px;
        height: 20px;
        margin: 0px;
        top: 0px;
        position: relative;
        left: 50%;
        margin-left: -10px;
        border: 3px solid rgba(255, 255, 255, 1.0);
        border-top-color: transparent;
      }

      #btn1-message, #btn2-message {
        color: #FFF;
        font-weight: 500;
      }

      #btn1-spinner, #btn2-spinner {
        display: none;
      }
          
      


      /*.downarrow:hover {
        -webkit-animation: none;
      }*/

      @media (min-width: 767px) {
        body {
          padding-top: 66px;
        }
      }

      @media (max-width: 768px) {

        .hero {
          background-image: url("https://storage.googleapis.com/troveup-imagestore/assets/img/josephine-banner-mobile.jpg");
          background-size: cover;
        }

        .padimg {
          padding: 0px 60px;
        }
        nav.navigate {
          display: none;
        }
        .btn.btn-secondary {
          margin-bottom: 60;
        }

        p {
          margin: 0 0 60px;
          max-width: 100%;
        }

        h1 {
          padding: 100px 20px 10px !important;
          font-size: 25px;
        }

        section {
          min-height: 500px;
        }

        .wrapper {
          margin-top: -90px;
        }
        .input-group {
          display: block;
          padding: 20px;
          margin: 10px auto;
        }
        .btn.btn-default {
          margin: 20px;
          width: 100%;
        }

        .navbar-toggle, 
        .box.cart.special {
          display: none !important; 
        }

        .create p {
          text-align: center !important;
          margin: 0px;
          max-width: 100%;
          padding: 10px 20px;
        }

        .norisk p {
          text-align: center !important;
          margin: 0px;
          max-width: 100%;
          padding: 10px 20px;
        }

        .create h2 {
          text-align: center !important;
          margin-top: 20px;
          margin-bottom: 10px;
        }

        .norisk h2 {
          text-align: center !important;
          margin-top: 20px;
          margin-bottom: 10px;
        }

        .norisk img {
          text-align: center;
          margin: 50px auto;
        }

        h3 {
          padding-top: 200px;
        }
        h2 {
           padding: 0px 20px;
        }
      }

      .error {
        text-align: center;
        color: red;
        font-weight: 600;
        font-size: 20px;
      }

      .tinted-overlay {
        position: absolute;
        background: #FFF;
        opacity: 0.5;
        left: 0px;
        top: 0px;
        width: 100%;
        height: 100%;
      }

      h1, h2, .error {
        position: relative;
      }

      h2 {
        font-family: "Raleway", Helvetica, sans-serif;
      }

      .desktopbr {
        display: block;
      }

      .mobilebr {
        display: none;
      }


      @media (max-width: 500px) {

        h1 {
          font-size: 22px;
        }
        h3 {
            padding-top: 100px;
            padding-bottom: 50px;
            font-size: 18px;
        }

        #about p {
          margin: 0px 0px 20px;
        }

        #holdit {
          padding-top: 0px;
          padding-bottom: 50px;
          margin-top: 0px;
        }

        .padimg {
          padding: 0px 5%;
        }

        .desktopbr {
          display: none;
        }

        .mobilebr {
          display: block;
        }
      }

    </style>
  </head>
  <body class="${bodyClass}">
    <div class="bg-img"></div>
    <c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
    <c:import url="../fragments/nav/topNavBar.jsp"/>

    <nav class="navigate" role="navigation">
      <ul>
        <li><a href="#" data-scroll="top">Top</a></li>
        <li><a href="#" data-scroll="about">about</a></li>
        <li><a href="#" data-scroll="create">create</a></li>
        <li><a href="#" data-scroll="earn">earn</a></li>
        <li><a href="#" data-scroll="norisk">risk free</a></li>
        <li><a id="cta" href="#" data-scroll="signup">Sign Up</a></li>
      </ul>
    </nav>

    <div class="wrapper">
      <section class="hero" id="top" data-anchor="top">
        <div class="tinted-overlay"></div>
        <h1 id="headlineh1">Create your own jewelry line.<br>no design experience needed.</h1>
        <h2>Design jewelry on Trove. We manufacture and ship for you.</h2>
        <div class="error" id="top-error"></div>
        <div class="input-group">
          <input type="text" id="top-email-input" class="form-control early-access" placeholder="Your Email Here" aria-label="...">
          <div class="input-group-btn">
            <button id="btn1" type="button" class="btn btn-default" onclick="handleSignup('top-email-input');">
              <span id="btn1-message">Get Early Access</span>
              <span id="btn1-spinner" class="spinner"></span>
            </button>
          </div>
        </div>
        <div class="downarrow"><a data-scroll="about" id="scrollbutt"><span class="icon ion-chevron-down"></span></a></div>
      </section>
      <div class="pressrow">
        <span style="position: relative;display: block;font-size:18px;letter-spacing: 2px;font-weight: 300;">TROVE IN THE PRESS</span>
        <div class="hidden-xs col-sm-1"></div>
        <div class="col-xs-6 col-sm-2"><a href="http://techcrunch.com/2015/10/08/trove-lets-you-customize-gold-and-silver-jewelry-using-3d-printing/"><img class="img-responsive press" src="https://storage.googleapis.com/troveup-imagestore/assets/img/press-logo-tech-crunch.png" alt="Tech Crunch logo"></a></div>
        <div class="col-xs-6 col-sm-2"><a href="http://www.refinery29.com/2015/10/95370/trove-3d-print-metal-jewelry"><img class="img-responsive press" src="https://storage.googleapis.com/troveup-imagestore/assets/img/refinery29-logo.png" alt="Refinery29 logo"></a></div>
        <div class="col-xs-6 col-sm-2"><a href="http://mashable.com/2015/10/08/trove-3d-print-jewelry/#VxzDzY3k6sqS"><img class="img-responsive press" src="https://storage.googleapis.com/troveup-imagestore/assets/img/mashable-logo.png" alt="Mashable logo"></a></div>
        <div class="col-xs-6 col-sm-2"><a href="https://uk.style.yahoo.com/post/130806134164/will-we-all-soon-be-3d-printing-our-own-clothes"><img class="img-responsive press" src="https://storage.googleapis.com/troveup-imagestore/assets/img/yahoo-style-bw.png" alt="Yahoo Style logo"></a></div>
        <div class="hidden-xs col-sm-2"><a href="http://thenextweb.com/apps/2015/10/08/trove-uses-3d-printing-technology-to-create-customized-jewelry"><img class="img-responsive press" src="https://storage.googleapis.com/troveup-imagestore/assets/img/the_next_web_logo_bw.png" alt="The Next Web logo"></a></div>
        <div class="hidden-xs col-sm-1"></div>
      </div>
      <section id="about" data-anchor="about">
        <div class="container marketing">
          <h3>You design the jewelry. <br class="mobilebr">We handle everything else<br class="desktopbr">from manufacturing to shipping.</h3>
          <div class="row">
            <div class="col-sm-4">
              <img class="img" src="https://storage.googleapis.com/trove-qa-teststore/no-experience.svg" alt="Generic placeholder image" width="140" height="140">
              <h2>No Design Experience Needed</h2>
              <p>Anyone can design on Trove. We provide intuitive and fun tools to make designing jewelry easy. Be empowered to create.</p>
              <p><a data-scroll="create" class="btn btn-secondary scrolllink" href="#" role="button">learn more</a></p>
            </div>
            <div class="col-sm-4">
              <img class="img" src="https://storage.googleapis.com/trove-qa-teststore/shining-pearl-ring.svg" alt="Generic placeholder image" width="140" height="140">
              <h2>Sell High Quality Products</h2>
              <p>All of our jewelry is manufactured in New York's Diamond District alongside the best fashion brands in the world.</p>
              <p><a data-scroll="earn" class="btn btn-secondary scrolllink" href="#" role="button">learn more</a></p>
            </div>
            <div class="col-sm-4">
              <img class="img" src="https://storage.googleapis.com/trove-qa-teststore/pearlring-in-box.svg" alt="Generic placeholder image" width="140" height="140">
              <h2>No Inventory = No Risk</h2>
              <p>We only manufacture and ship once you sell. This means you never have to hold inventory like in most marketplaces.</p>
              <p><a data-scroll="norisk" class="btn btn-secondary scrolllink" href="#" role="button">learn more</a></p>
            </div>
          </div>
        </div>
      </section>
      <section class="bgim" id="create" data-anchor="create">
        <h3 id="holdit"></h3>
        <div class="container create">
          <div class="row">
            <div class="col-sm-7 col-sm-push-5">
              <img class="img-responsive padimg" src="https://storage.googleapis.com/troveup-imagestore/assets/img/customizer-mock-2.gif" alt="">
            </div>
            <div class="col-sm-5 col-sm-pull-7">
              <br><br>
              <h2 style="font-size: 150%">How it Works</h2>
              <br>
              <h2>DESIGN YOUR JEWELRY ON TROVE</h2>
              <p>Use our intuitive and easy design tools to build your jewelry line exactly how you want it.</p>
              <h2>CREATE YOUR STOREFRONT</h2>
              <p>Open your store on Trove, where you'll have access to powerful seller tools to keep track of your sales.</p>
              <h2>FIND CUSTOMERS</h2>
              <p>Find your core consumer who will care about your jewelry line as much as you. We provide resources to help you find your customer.</p>
              <h2>WE HANDLE THE LOGISTICS</h2>
              <p>We take care of manufacturing, shipping, and all of the difficult details in between. Trust us to handle the dirty work.</p>
            </div>
          </div>
        </div>
        <br><br><br>
      </section>
      <section id="earn" data-anchor="earn">
        <div class="container earn">
          <h3>Make money on your own time with flexible hours</h3>
          <div class="col-sm-4">
            <img class="img" src="https://storage.googleapis.com/trove-qa-teststore/set-price.svg" alt="Generic placeholder image" width="140" height="140">
            <h2>Set the price of your jewelry</h2>
            <p>You decide how much you're able to make. The more confident you are in your ability to sell, the higher you can price your jewelry.</p>
          </div>
          <div class="col-sm-4">
            <img class="img" src="https://storage.googleapis.com/trove-qa-teststore/profit-split.svg" alt="Generic placeholder image" width="140" height="140">
            <h2>Receive 50% of all profits</h2>
            <p>You receive half of the profits for making the sales, and Trove receives the other half for manufacturing and shipping your jewelry line.</p>
          </div>
          <div class="col-sm-4">
            <img class="img" src="https://storage.googleapis.com/trove-qa-teststore/free-first-month.svg" alt="Generic placeholder image" width="140" height="140">
            <h2>Costs nothing to start</h2>
            <p>No setup or maintenance fees for selling your jewelry line on Trove. It's free to start!</p>
          </div>
        </div>
      </section>
      <section class="greey" id="norisk" data-anchor="norisk">
        <div class="container norisk">
          <div class="row">
            <div class="col-sm-12">
              <h3>No Risk To You<br>
              <span style="font-size: 15px;"><br><br>We've figured out all the logistics <br class="mobilebr">and pain points of creating <br>your jewelry line so you can focus on designing and selling.</span></h3>
            </div>
          </div>
          <div class="row spacing">
            <div class="col-sm-2">
              <img class="img img-responsive" style="padding: 10px;" src="https://storage.googleapis.com/trove-qa-teststore/manufacturing-icon.svg" alt="Generic placeholder image" width="140" height="140">
            </div>
            <div class="col-sm-4" style="text-align: left;">
              <h2>Top quality manufacturing</h2>
              <p>We create your jewelry from top quality materials in New York's Diamond District alongside some of the top jewelry brands in the world.</p>
            </div>
            <div class="col-sm-2">
              <img class="img img-responsive" style="padding: 30px 10px 0px;" src="https://storage.googleapis.com/trove-qa-teststore/shipping-gift-box-icon.svg" alt="Generic placeholder image" width="140" height="140">
            </div>
            <div class="col-sm-4">
              <h2>We handle all the logistics</h2>
              <p>We handle order fulfillment, packaging, shipping, and everything in between. Build your jewelry brand and we'll take care of the rest.</p>
            </div>
          </div>
          <div class="row">
            <div class="col-sm-2">
              <img class="img img-responsive" style="padding: 10px;" src="https://storage.googleapis.com/trove-qa-teststore/no-inventory-icon.svg" alt="Generic placeholder image" width="140" height="140">
            </div>
            <div class="col-sm-4">
              <h2>Never hold inventory again</h2>
              <p>Each jewelry piece is made to order so you don't have to worry about any up-front costs. There's no inventory risk.</p>
            </div>
            <div class="col-sm-2">
              <img class="img img-responsive" style="padding: 10px;" src="https://storage.googleapis.com/trove-qa-teststore/closed-store-icon-2.svg" alt="Generic placeholder image" width="140" height="140">
            </div>
            <div class="col-sm-4">
              <h2>Stop selling at any time</h2>
              <p>You're entitled to close your shop on Trove at any time. And you're always welcome to come back in the future!</p>
            </div>
          </div>
        </div>
      </section>
      <section class="clear" id="signup" data-anchor="signup">
        <h1>What are you <br class="mobilebr">waiting for?</h1>
        <h2>Sign up to create <br class="mobilebr">your jewelry line!</h2>
        <div class="error" id="bottom-error"></div>
        <div class="input-group">
          <input type="text" id="bottom-email-input" class="form-control early-access" placeholder="Your Email Here" aria-label="...">
          <div class="input-group-btn">
            <button id="btn2" type="button" class="btn btn-default" onclick="handleSignup('bottom-email-input');">
              <span id="btn2-message">Get Early Access</span>
              <span id="btn2-spinner" class="spinner"></span>
            </button>
          </div>
        </div>
      </section>
    </div>

    <script type="text/javascript" src="/resources/js/trove/AJAX.js"></script>
    <script>

      var keypressHelper = new KeypressHelper();
      keypressHelper.addKeyPressDetection(keypressHelper.getEnterKeyCodeNumber(), 'bottom-email-input', handleSignup);
      keypressHelper.addKeyPressDetection(keypressHelper.getEnterKeyCodeNumber(), 'top-email-input', handleSignup);

      var ajaxHelper = new AJAXHelper("${_csrf.token}");

      var topContainerHeight = window.innerHeight - ($('header').height() + $('.navigate').height());
      $('#top').height(topContainerHeight);
      var margtop = (topContainerHeight - 220) / 2;
      $('#headlineh1').css("padding-top", margtop );
      $('.downarrow').css("padding-top", margtop - 200 );


      $('nav.navigate a').on('click', function() {
        var scrollAnchor = $(this).attr('data-scroll');
        var scrollPoint = $('section[data-anchor="' + scrollAnchor + '"]').offset().top - 48;
        $('body,html').animate({
          scrollTop: scrollPoint
        }, 500);
        return false;
      });


      $(window).scroll(function() {
        var windscroll = $(window).scrollTop(); 
        if (windscroll >= 66) {
          $('nav.navigate').addClass('fixed');
          $('.wrapper section').each(function(i) {
            if ($(this).position().top <= windscroll - 20 ) {
              $('nav.navigate a.ractive').removeClass('ractive');
              $('nav.navigate a').eq(i).addClass('ractive');
            }
          });
        } else {
          $('nav.navigate').removeClass('fixed');
          $('nav.navigate a.active').removeClass('ractive');
          $('nav.navigate a:first').addClass('ractive');
        }
      }).scroll();

      $(window).resize(function() {
        var windscroll = $(window).scrollTop(); 
        if (windscroll == 0) {
          topContainerHeight = window.innerHeight - ($('.navbar-inner').height() + $('.navigate').height());
          $('#top').height(topContainerHeight);
          margtop = (topContainerHeight - 220) / 2;
          $('#headlineh1').css("padding-top", margtop );
          $('.downarrow').css("padding-top", margtop - 150 );
        } else {
          
        }
      });

      // function land() {
      //   mixpanel.track("land_landing");
      // };
      // $(document).ready(function() {
      //   land();
      // });

      $(".scrolllink").click(function(e) {
        var scrollAnchor = $(this).attr('data-scroll');
        var scrollPoint = $('section[data-anchor="' + scrollAnchor + '"]').offset().top;
        $('body,html').animate({
          scrollTop: scrollPoint
        }, 500);
        return false;
      });

      // $('body').scrollspy({ target: '#my-nav' });
      $("#scrollbutt").click(function(e) {
        var scrollAnchor = $(this).attr('data-scroll');
        var scrollPoint = $('section[data-anchor="' + scrollAnchor + '"]').offset().top;
        $('body,html').animate({
          scrollTop: scrollPoint
        }, 500);
        return false;
      });

      function handleSignup(inputId) {

        var inputToUse;

        if (inputId == null) {
          if ($('#top-email-input').val().length > 0) {
            inputToUse = "top-email-input";
          } else
          {
            inputToUse = "bottom-email-input";
          }
        } else {
          inputToUse = inputId;
        }

        activateSignupButtonPressBusySpinners();

        var postData = ajaxHelper.createOrAppendPostDataObject("email", $('#' + inputToUse).val());
        postData = ajaxHelper.createOrAppendPostDataObject("type", "SELLER_LAUNCH_PAGE", postData);

        ajaxHelper.performPostRequest(postData, "/genericsignup", getSignupSuccessCallbackFunction(), getSignupFailureCallbackFunction());
      }

      function getSignupSuccessCallbackFunction() {
        return function(data) {
          deactivateSignupButtonPressBusySpinners();
          showSuccessfulSignupConfirmation();
        }
      }

      function getSignupFailureCallbackFunction() {
        return function(data) {
          deactivateSignupButtonPressBusySpinners();
          $('#top-error').html(data.errorMessage);
          $('#bottom-error').html(data.errorMessage);
          $('#top-email-input').val("");
          $('#bottom-email-input').val("");
        }
      }

      function activateSignupButtonPressBusySpinners() {
        $('#btn1-message').html("");
        $('#btn2-message').html("");
        $('#btn2-spinner').fadeIn().css("display","block");
        $('#btn1-spinner').fadeIn().css("display","block");
      }

      function deactivateSignupButtonPressBusySpinners() {
        $('#btn1-spinner').css("display","none");
        $('#btn2-spinner').css("display","none");
        $('#btn2').css("height","auto");
        $('#btn1').css("height","auto");
      }

      function showSuccessfulSignupConfirmation() {
        $('#btn1-spinner').css("display","none");
        $('#btn2-spinner').css("display","none");
        $('#btn1').css("width","100%");
        $('#btn1').css("background","#FFFFFF");
        $('#btn1').css("border-color","#FFFFFF");
        $('#btn1').css("color","#333");
        $('#btn1').css("white-space","normal");
        $('#btn1').css("pointer-events","none");
        $('#btn2').css("width","100%");
        $('#btn2').css("background","#FFFFFF");
        $('#btn2').css("border-color","#FFFFFF");
        $('#btn2').css("color","#333");
        $('#btn2').css("white-space","normal");
        $('#btn2').css("pointer-events","none");
        $('#btn1-message').html("Thanks! Keep an eye on your inbox for more information.");
        $('#btn1-message').css("color","#333");
        $('#btn2-message').html("Thanks! Keep an eye on your inbox for more information.");
        $('#btn1-message').css("text-transform","none");
        $('#btn2-message').css("text-transform","none");
        $('#btn2-message').css("color","#333");
        $('#top-email-input').css("display","none");
        $('#bottom-email-input').css("display","none");
      }

      function land() {
        mixpanel.track("land_sellers");
      };

      $(document).ready(function() {
        land();
      });


      $("#btn1").click(function () {
        mixpanel.track("sellers_email_submit", {
          "which": "top"
        });
      });

      $("#btn2").click(function () {
        mixpanel.track("sellers_email_submit", {
          "which": "bottom"
        });
      });

    </script>


  </body>
</html>
