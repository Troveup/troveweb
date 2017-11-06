<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Trove Custom Jewelry</title>
  <c:import url="../fragments/headers/commonHead.jsp"/>
  <c:import url="../fragments/headers/legalHead.jsp"/>
  <c:import url="../fragments/analytics/all.jsp"/>
  <link rel="stylesheet" href="/resources/stylesheets/buttons.css">

  <style>

  .mobilestuff{
    display:none;
  }
  .mobileStuff .welcome-banner-container p{
    position: absolute;
    width: 100%;
    text-align: center;
    vertical-align: top;
    bottom: 17%;
  }


  .thumbnail>img {
    width: 130px;
    padding: 10px;
  }

  .thumbnail {
    text-align: center;
    border: 0px solid #FFF;
  }

  .text-explanation{
    margin:40px 5% 20px;
    padding: 30px 2% 30px;
    text-align:center;
    /*background-color: #dedede;*/
    border-top:2px solid #dedede;
    border-bottom:2px solid #dedede;

  }
  .how-trove-works.mobile {
    margin:0 0 20px;
  }

  .four-headers {
    font-family:'Vollkorn', serif; 
    font-style: italic;
    font-size: 28px;
    margin-bottom:15px;
  }

  .cta-header{
    font-family:'Vollkorn', serif; 
  }

  .welcome-banner-container{
/*      width: 100%;
      display:inline-block;
      background-size:100%;
      background-repeat: no-repeat;*/
/*      max-width:1600px;
*/      position:relative;
      margin:0 auto;
      text-align: center;
  }

  .welcome-row{
    max-width:120rem;
  }

  .welcome-row.narrow{
    max-width:90rem;
  }

  .welcome-row-mobile{
    margin:5px 0 0;
    padding:0px;
  }

/*  .welcome-banner-container .btn {
    position: absolute;
    top: 260px;
    margin-left: 190px;
    font-size: 170%;
    background-color: #DD2435;
  }

  .welcome-banner-container .btn:hover {
    background-color: #F26868;
  }*/

  .try-text{
    position:absolute;
    padding:20px 20px 40px;
    background-color: rgba(255,255,255,.75);
    margin-left:6%;
  }

  .try-photo-container{
    display:block;
    margin:0 auto;
  }

  .press-header{
    position: relative;
    display: block;
    margin-top: 15px;
    font-size:18px;
    letter-spacing: 1px;
    font-weight: 300;
    text-transform:uppercase;
  }

  .row.mobile{
    padding-left:0;
    padding-right:0;
  }

  .how-header{
    position: absolute;
    background-color: #ffffff;
    font-size: 28px;
    left: 50%;
    margin-left: -153px;
    top: 30px;
    padding:15px;
  }

  .try-container{
    display:block;
    margin:0 auto;
    max-width:1000px;
      }

  .how-body{
    margin:60px 20% 30px;
    padding: 30px 5% 30px;
    text-align:center;
    border: 2px solid #dedede;
  }

  .favorites-img{
    max-width:75%;
    position:relative;
    margin:0 auto 10;

    -webkit-transition: all 0.30s ease-in-out;
    -moz-transition: all 0.30s ease-in-out;
    -ms-transition: all 0.30s ease-in-out;
    -o-transition: all 0.30s ease-in-out;
  }

  .favorites-img:hover{
    opacity:.7;
  }

  .press{
    max-width:55%;
    margin: 2% auto;  
  }
  

  .press li{
    float:left;
    overflow:hidden;
    opacity: .5;
    width:20%;
    padding:10px 2%;
    text-align:center;
  }

  .press .press-logo{
    max-width:150px;
    display:block;
    margin:0 auto;
  }

  .img-text-overlay-wrapper{
    position:absolute;
    width:100%;
  }

    @media screen and (max-width: 768px) { 
    .how-body{
      margin:60px 10% 30px;
    }

    .press{
      max-width:75%;
    }

    .try-container{
      margin:30px 5% 30px;
    }

    .try-text{
      position:relative;
      width: 100%;
      text-align:center;
      top: -40px;
      margin-left:0;
      padding:0px;

    }
  }

    @media screen and (max-width: 440px) { 
    .how-body{
      margin:60px 10px 30px;
    }

    .cta-container{
      left: 0;
      margin-left: 0px;
      width: 100%;
    }
  }
  

  .how-header-text{
    color: #000;
  }

  .btn-open{
    background-color: transparent;
    color:#000;
    border:1px solid #232323;
  }

  .btn-open:hover{
    background-color:#fff;
    border:1px solid #fff;
    color:#000;
  }

  .btn-overlay{
    width:250px;
    display:block;
    padding:10px;
    position:relative;
    margin:15 auto 0;
  }


  .cta-container{
    background-color: rgba(255,255,255,0.75);
    width: 480px;
    z-index:1;
    position:absolute;
    left:50%;
    margin-left:-240px;
    text-align:center;
    padding:50px 0;
  }

  .press {
    padding: 20px 0px;
    opacity: 0.5;
  }

  .pressrow {
    background-color:#f6f6f6;
    text-align:center;
    margin-top:50px;
  }

  .pressrow.mobile{
    margin-top: 4px;
  }

  .fullbanner-image{
    max-width: 1500px;
    width:100%;
    margin-left: auto;
    margin-right: auto;
     -webkit-transition: all 0.30s ease-in-out;
    -moz-transition: all 0.30s ease-in-out;
    -ms-transition: all 0.30s ease-in-out;
    -o-transition: all 0.30s ease-in-out;
  }

 /* .fullbanner-image:hover{
    opacity:.9;
  }*/

  .try-image{
      -webkit-transition: all 0.30s ease-in-out;
    -moz-transition: all 0.30s ease-in-out;
    -ms-transition: all 0.30s ease-in-out;
    -o-transition: all 0.30s ease-in-out;
  }

  .try-image:hover{
    opacity:.7;
  }

  .test-container{
    background-color: #dedede;
    overflow: hidden;

    -webkit-transition: all 0.30s ease-in-out;
    -moz-transition: all 0.30s ease-in-out;
    -ms-transition: all 0.30s ease-in-out;
    -o-transition: all 0.30s ease-in-out;
  }

  /*.test-container:hover{
    opacity:.7;
  }*/

  .img-text-overlay{
    position:absolute;
    top:45%;
    transform:translateY(-50%);
    -moz-transform: translateY(-50%);
    -o-transform: translateY(-50%);
    -ms-transform: translateY(-50%);
    -webkit-transform: translateY(-50%);
  }

  .img-text-overlay.center{
    left:0;
    right:0;
  }

  .img-text-overlay.bottom{
    left:0;
    right:0;
    top:85%;
  }

  .img-text-overlay.hero{
    left:0;
    right:0;
/*    background-color: rgba(255,255,255,.75);
*/    padding: 50 20 50;
    position:absolute;
    text-align:center;
    margin: 0 auto;
    z-index:999;
    color:#000000;
  }

  .img-text-overlay.mobile{
    position:relative;
    padding-top:22%;
    margin-top:0;
    top:0;
  }

  .img-text-overlay.right{
    position:relative;
    text-align:left;
  }

  .bestseller-price{
    font-family:'Vollkorn',Helvetica,serif;
    font-size:14px;
  }

  .hero-overlay{
    position: absolute;
    background: #ffffff;
    opacity: 0.4;
    left: 0px;
    top: 0px;
    width: 100%;
    z-index: 9;
    height: 100%;
  }



  @media(max-width: 768px) {
       .press {
        padding: 20px 25px;
        opacity: 0.5;
      }
  }

  body {
    width: 100%;
    overflow-x: hidden;
  }

  body {
    padding-top: 100px;
  }

  .special-container p {
     margin-bottom: 5px;
  }


  @media (min-width: 767px) {
    body {
        padding-top: 140px;
    }
  }

  @media (max-width: 575px) {
    body {
        padding-top: 130px;
    }


    /*.btn.btn_specialsignup {
      margin-top: 5px;
    }*/
  }

  @media (max-width: 575px) {
     .desktopStuff {
        display: none;
     }

    .mobileStuff{
        display: block;
    }
  }

  a.fullbanner {
      display: none;
  }
  a.mobilebanner {
      display: block;
  }


  @media (min-width: 576px) {
      a.fullbanner {
          display: block;
      }
      a.mobilebanner {
          display: none;
      }
  }

  html, body {
    max-width: 100%;
    overflow-x: hidden;
  }

  .goodbye-text{
    padding:0 0 0;
    font-size: 16px;
    line-height: 1.75;
  }

  .goodbye-container{
    text-align:center;
    margin:150 auto;
    border:8px solid rgba(255,238,238,1);
    padding:50 30 75;
  }

   @media (max-width: 768px) {
      .goodbye-container{
        text-align:center;
        margin:10;
        border:8px solid rgba(255,238,238,1);
        padding:30 5 50;
      }
      .goodbye-text{
      padding:0 0 0;
      font-size: 13px;
      }

      .four-headers{
        font-size: 24px;
      }
   }

  
  </style>
</head>

<body style="padding-bottom:0px;">
  <c:import url="../fragments/analytics/adwordstag.jsp"/>
    <div class="container-fluid" style="padding-right:0px;padding-left:0px;">

      <div class="row welcome-row narrow goodbye-container">
        <img alt="Trovelogo" class="nav-logo" src="https://storage.googleapis.com/troveup-imagestore/assets/img/logo-text.png">
        <div style="margin:20px;">
        </div>
        <div class="col-xs-12 col-md-12">
          <div class="four-points">
               <h2 class="four-headers">Thank You and So Long</h2>
               <p class="goodbye-text">After an exciting and wonderful adventure, Trove is closed as of Friday, September 16th. We have truly enjoyed seeing people come together to create beautiful personalized jewelry for themselves or gifts for others. We thank each of you for joining us in this journey to make everything in this world a little bit more unique.</p>
               <br>
               <p class="goodbye-text"><span style="font-style:italic;">All outstanding orders will still be shipped.</span><br>Questions? Email us at <a href="mailto:hello@troveup.com?Subject=Hello%20there!" style="color: #ee2435;">hello@troveup.com</a>.</p>
            </div>
        </div>
      </div>
    </div>
    
  </body>


  <script>

    function jqUpdateSize(){
      var wh = $('.welcome-banner-container').height();
      var ch = $('.cta-container').innerHeight();
      var newh = Math.max( (wh - ch) / 2 , 0 );
      $('.cta-container').css("margin-top", String(newh) + "px");
    };

    function bodyPadding(){
      var navHeight = $('.navbar').height();
      var bannerHeight = $('.special-container').outerHeight();
      var totalHeight = navHeight + bannerHeight;
      $('body').css("padding-top", totalHeight + "px");
    };

    $(document).ready(function() {
      jqUpdateSize();
      bodyPadding();
      window.addEventListener('resize', jqUpdateSize);
      window.addEventListener('resize', bodyPadding);
    });

    function jqUpdateSize2(){
      var wh = $('.try-photo-container').height();
      var ch = $('.try-text').innerHeight();
      var newh = Math.max( (wh - ch) / 2 , 0 );
      $('.try-text').css("margin-top", String(newh) + "px");
    };

    function getUrlVars() {
      var vars = [], hash;
      var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
      for(var i = 0; i < hashes.length; i++) {
          hash = hashes[i].split('=');
          vars.push(hash[0]);
          vars[hash[0]] = hash[1];
      }
      return vars;
    }

    function superProp(ref) {
      mixpanel.register({
        "refBucket": ref
      });
    };

    function land() {
      mixpanel.track("land_welcome");
    };

    $(document).ready(function() {
      var ref = getUrlVars()["ref"];
      land();
      if (ref) {
        superProp(ref);
      }
      jqUpdateSize2();
      window.addEventListener('resize', jqUpdateSize2);
    });

    // mixpanel
    $("#customizenow").click(function () {
      mixpanel.track("welcome_customizenow");
    });
    $("#shopnow").click(function () {
      mixpanel.track("welcome_shopnow");
    });
    $("#startshop").click(function () {
      mixpanel.track("welcome_startshop");
    });
    $("#startearning").click(function () {
      mixpanel.track("welcome_startearning");
    });
    $("#tryon").click(function () {
      mixpanel.track("welcome_tryonjewelry");
    });
    $("#followus").click(function () {
      mixpanel.track("welcome_followus");
    });
    $("#getstartdesign").click(function () {
      mixpanel.track("welcome_homepage");
    });
    $("#getstarttry").click(function () {
      mixpanel.track("welcome_tryon");
    });
    $("#getstartdiscover-desktop-cta").click(function() {
      mixpanel.track("welcome_desktopstartexploring");
    });
    $("#getstartdiscover-mobile-button").click(function() {
      mixpanel.track("welcome_mobilestartexploring");
    });
    $("#getstartdiscover-mobile-banner").click(function() {
      mixpanel.track("welcome_mobilestartexploring");
    });
    $("#discover-rings").click(function() {
      mixpanel.track("welcome_mobilerings");
    });
    $("#discover-bracelets").click(function() {
      mixpanel.track("welcome_mobilebracelets");
    });

  </script>

</html>