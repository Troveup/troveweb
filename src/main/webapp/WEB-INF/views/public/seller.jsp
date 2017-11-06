<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
    <c:import url="../fragments/headers/commonHead.jsp"/>
    <c:import url="../fragments/headers/legalHead.jsp"/>
    <title>${pageTitle}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=0">
    <link rel="icon" href="/resources/img/favicon.png?v=2">
    <link href='https://fonts.googleapis.com/css?family=Raleway:400,200,300,500,600,700' rel='stylesheet' type='text/css'>
    <link href="https://fonts.googleapis.com/css?family=Vollkorn:400,400italic,700,700italic" rel="stylesheet" type="text/css">
    <c:import url="../fragments/analytics/all.jsp"/>
    <!-- for Facebook -->
    <meta property="fb:app_id"        content="420400984811981"/>
    <meta property="og:type"          content="website" />
    <meta property="og:url"           content="${pageUrl}" />
    <meta property="og:title"         content="${pageTitle}" />
    <meta property="og:description"   content="${pageDescription}" />
    <meta property="og:image"         content="${pageImage}" />
    <!-- for Twitter -->
    <meta name="twitter:card" content="summary"/>
    <meta name="twitter:title" content="${pageTitle}"/>
    <meta name="twitter:description" content="${pageDescription}"/>
    <meta name="twitter:image" content="${pageImage}"/>

    <style>
      .ion-quote:before {
        content: "";
        background-image: url('https://storage.googleapis.com/troveup-imagestore/assets/img/quote.png');
        width: 44px;
        height: 40px;
        background-repeat: no-repeat;
        background-size: contain;
      }

      body {
        padding-bottom: 0px;
        font-family: "Raleway",Helvetica,Arial,sans-serif;
      }
      .container {
        text-align: center;
      }
      .col-sm-4 {
        padding: 20px;
      }
      h1 {
        font-size: 26px;
        letter-spacing: .06em;
        margin-bottom: 4px;
        text-align: center;
      }

      h2 {
        font-family: "Raleway",Helvetica,Arial,sans-serif;
        font-size: 15px;
        color: #4C4B4B;
        margin-top: 15px;
        margin-bottom: 20px;
      }

      a:hover {
        color: #4E4E4E;
        text-decoration: none;
      }

      h3 {
        margin-top: 0px;
        color: rgb(129, 130, 130);
        font-size: 17px;
        letter-spacing: 0.08em;
      }

      .navbar-default {
        background-color: #B2B8B8;
        border-color: #B2B8B8;
      }

      button.left {
        border: 1px solid #FFF;
        background: transparent;
        color: #FFF;
        padding: 10px 30px;
        text-transform: uppercase;
        font-size: 16px;
        letter-spacing: 0.08em;
        -webkit-font-smoothing: antialiased;
        margin-top: 15px;
        margin-left: 15px;
        outline: 0;
        cursor: pointer;
        -webkit-transition: all 0.3s ease-in-out;
           -moz-transition: all 0.3s ease-in-out;
            -ms-transition: all 0.3s ease-in-out;
             -o-transition: all 0.3s ease-in-out;
                transition: all 0.3s ease-in-out;
      }
      button.left:hover {
        background: #FFF;
        color: #666;
      }
      button.right {
        border: 1px solid #DD2435;
        background-color: #DD2435;
        color: #FFF;
        padding: 10px 30px;
        text-transform: uppercase;
        font-size: 16px;
        letter-spacing: 0.08em;
        -webkit-font-smoothing: antialiased;
        margin-top: 15px;
        margin-right: 15px;
        outline: 0;
        cursor: pointer;
        -webkit-transition: all 0.3s ease-in-out;
           -moz-transition: all 0.3s ease-in-out;
            -ms-transition: all 0.3s ease-in-out;
             -o-transition: all 0.3s ease-in-out;
                transition: all 0.3s ease-in-out;
      }

      button.right:hover {
        background-color: #F26868;
        border-color: #F26868;
      }

      button.left:hover a {
        background: #FFF;
        color: #666;
      }

      button.right:hover a {
        background-color: #F26868;
        border-color: #F26868;
      }

      @media (max-width: 768px) {
        h3 {
          font-size: 12px;
        }
        .col-xs-0 {
          display: none;
        }

        #vertical-banner {
          display: none;
        }
      }

      .uparrow {
        text-align: center;
        padding-bottom: 10px;
        padding-top: 10px;
        cursor: pointer;
        font-weight: 400;
        letter-spacing: 0.06em;
        color: #666666;
      }

      .uparrow span {
        padding-left: 10px;
        color: #666666;
        font-size: 20px;
      }

      h4 {
        margin: 50px 25%;
        font-family: "Raleway",Helvetica,Arial,sans-serif;
        letter-spacing: .06em;
        text-align: center;
        text-transform: uppercase;
        height: auto;
        padding-top: 50px;
        padding-bottom: 50px;
        border-top: 2px solid #C1A3FF;
        margin-bottom: 50px;
        font-size: 1.3em;
        font-weight: 300;
        line-height: 1.2em;
        padding-top: 100px;
      }

      h6 {
        font-size: 2.8em;
        font-family: "Raleway", Helvetica, Arial, sans-serif;
        letter-spacing: -.01em;
        color: #7666B5;
        font-weight: 700;
        text-transform: uppercase;
        margin-top: 60px;
        margin-bottom: 10px;
        position: relative;
        padding-bottom: 10px;
        line-height: 1.2em;
      }

      h6.firsth6:after {
        content: "";
        width: 110px;
        height: 3px;
        position: absolute;
        left: 50%;
        margin-left: -55px;
        bottom: 0px;
        background-color: #7666B5;
      }

      h6.secondh6 {
        padding-bottom: 26px;
      }

      h6 span {
        font-size: 1.6em;
        font-family: "Raleway", Helvetica, Arial, sans-serif;
        letter-spacing: -.01em;
        color: #7666B5;
        font-weight: 700;
        text-transform: uppercase;
        margin-top: 60px;
        margin-bottom: 10px;
        position: relative;
        padding-bottom: 10px;
        line-height: 1.2em;
      }



      h4 span {
        font-family: 'vollkorn';
        font-size: 1.6em;
        line-height: 1.4em;
      }

      h4 p {
        text-transform: none;
        margin-top: 25px;
        font-size: 90%;
        padding: 0px 10%;
      }
      h5 {
        padding-bottom: 15px;
        margin-top: 0px;
        margin-bottom: 0px;
      }

      @media (min-width: 768px) {
        .col-sm-0, .col-md-0, .col-lg-0 {
          display: none;
        }
      }

      .uparrow a {
        color: #666;
        text-decoration: none;
        border: 0px solid #FFF;
        outline: 0;
        line-height: 30px;
        vertical-align: top;
      }

       a button {
        color: #FFF;
        text-decoration: none;
        -webkit-transition: all 0.3s ease-in-out;
           -moz-transition: all 0.3s ease-in-out;
            -ms-transition: all 0.3s ease-in-out;
             -o-transition: all 0.3s ease-in-out;
                transition: all 0.3s ease-in-out;
      }

      a button:hover, a button:focus, a button:active {
        color: #FFF;
        text-decoration: none;
      }

      a:hover button.left , a:focus button.left , a:active button.left  {
        color: #666;
      }

      .spinstuff {
        opacity: 0.0;
        -webkit-transition: 1s opacity;
                transition: 1s opacity;
        pointer-events: none;
      }
      .choosestuff {
        opacity: 0.0;
        -webkit-transition: 1s opacity;
                transition: 1s opacity;
      }

      .choosestuff.show {
        display: block;
        opacity: 1.0;
      }

      .spinstuff.show {
        display: block;
        opacity: 1.0;
      }

      @media (min-width: 1200px) {
        .container {
            width: 970px;
        }
        h4 {
          margin: 50px;
        }
      }

      @media (max-width: 1200px) {
        h4 {
          margin: 50px;
        }
      }

      .ftui-nav {
        z-index: 999;
        background: rgb(225, 228, 233);
        border-bottom: 1px solid rgb(208, 211, 216);
        height: 60px;
        width: 100%;
        text-align: center;
        position: fixed;
        top: 0px;
        left: 0px;
        right: 0px;
      }

      .steps {
        height: 40px;
        line-height: 16px;
        padding: 0px;
        margin-top: 10px;
        color: #FFF;
        display: none;
        text-align: center;
        width: auto;
      }

      .options-holder {
        width: 100%;
        text-align: center;
        height: 100%;
        margin-top: 60px;
      }

      .options {
        width: 700px;
        height: 1040px;
        margin: 12.5px auto;
      }

      .option {
        width: 200px;
        height: 200px;
        display: inline-block;
        border: 1px solid #FFF;
        margin: 12.5px;
        line-height: 200px;
        font-size: 30px;
        color: #666;
        cursor: pointer;
        -webkit-transition: all .4s ease;
        -moz-transition: all .4s ease;
        transition: all .4s ease;
      }

      .option:hover {
        border: 1px solid #DEDEDE;
      }

      .option.chosen {
        border: 1px solid #DD2435;
      }

      .findnumber-button {
        font-family: "Raleway",Helvetica,Arial,sans-serif;
        font-size: 15px;
        color: #FFFFFF;
        background: #232323;
        margin-top: 15px;
        margin-bottom: 20px;
        text-transform: uppercase;
        border: 0px solid transparent;
        padding: 15px;
        display: block;
        width: Calc(100% - 80px);
        margin: 20px 40px;

      }

      .gotonecklace-button {
        font-family: "Raleway",Helvetica,Arial,sans-serif;
        font-size: 15px;
        color: #FFFFFF;
        background: #7514D3;
        margin-top: 15px;
        margin-bottom: 20px;
        text-transform: uppercase;
        border: 0px solid transparent;
        padding: 15px;
        display: block;
        width: Calc(100% - 80px);
        margin: 50px 40px 10px;
      }

      .findnumber-button:hover {
        background: #000000;
      }

      .gotonecklace-button:hover {
        background: #000000;
      }

      a.try-another {
        position: block;
        text-transform: uppercase;
        text-decoration: underline;
        font-weight: light;
        color: #232323;
        font-style: italic;
      }

      

      .step {
        display: inline-block;
        font-size: 13px;
        min-width: 180px;
        font-weight: 500;
        color: #656565;
        letter-spacing: 0.06em;
      }

      .step span {
        font-size: 25px;
        color: #FFF;
      }

      .step.active {
        color: #F26968;
      }

      .step.active span {
        color: #F26968;
      }
      .gemIcon {
        width: 30px;
      }

      .currentStep {
        z-index: 1000;
        position: fixed;
        top: 12px;
        right: 10px;
        padding: 8px 20px;
        background: transparent;
        color: #656565;
        letter-spacing: 0.15em;
        font-size: 15px;
        -webkit-font-smoothing: antialiased;
        font-weight: 500;
        -webkit-transition: .3s all;
                transition: .3s all;
        display: none;
        font-family: Helvetica, Arial, sans-serif;
      }



      @media screen and (min-width: 768px) {
        .steps {
          display: inline-block;
        }
      }

      @media screen and (max-width: 767px) {
        .currentStep {
          display: inline-block;
        }
        h4 {
          font-size: 1em;
          margin: 40px 10px;
        }
        #footer-block, #footer-connect {
          display: none;
        }
        #footer-support {
          width: 100%;
          line-height: 2em;
        }
      }

      .row.landingbanner {
        background-image: url('');
        padding: 0 20px 0 20px;
        background-size: cover;
        background-repeat: repeat-x;
        background: #F6F6F6;
        border-bottom: 1px solid #DEDEDE;
        border-top: 1px solid #DEDEDE;
      }

      .lander {
        background: #FFF;
        margin-top: 40px;
        padding: 20px;
        max-width: 500px;
        margin-left: auto;
        margin-right: auto;
        text-align: center;
        position: relative;
        margin: 50 auto;
        border: 1px solid #DEDEDE;
        background: #FFF;
      }

      .navbar-toggle {
        position: fixed;
        float: left;
      }

      .row.quotebanner {
        background: #FFF;
        height: 400px;
      }

      .quote {
        position: relative;
        border: 2px solid #F37777;
        background: #FFF;
        padding: 21px;
        width: 600px;
        margin-left: auto;
        margin-right: auto;
        text-align: center;
        margin-top: 100px;
      }

      .quote-before {
        position: absolute;
        height: 59px;
        width: 60px;
        padding: 8px;
        background: #FFF;
        font-size: 45px;
        left: -20px;
        top: -28px;
        color: #ccc;
      }

      .quote-after {
        position: absolute;
        height: 59px;
        width: 60px;
        padding: 8px;
        background: #FFF;
        font-size: 45px;
        right: -20px;
        bottom: -28px;
        color: #ccc;
        -webkit-transform:rotate(-180deg);
           -moz-transform:rotate(-180deg);
             -o-transform:rotate(-180deg);
                transform:rotate(-180deg);
            ms-filter:"progid:DXImageTransform.Microsoft.BasicImage(rotation=2)";
            filter:progid:DXImageTransform.Microsoft.BasicImage(rotation=2);
      }

      .quote p {
        font-size: 20px;
        padding: 10px 20px 0px 20px;
      }

      @media (max-width: 768px) {
        .quote {
          width: Calc(100% - 40px);
          margin-left: 20px;
          margin-right: 20px;
        }

        .lander {
          width: 100%;
        }

        #cta-buttons {
          display: none;
        }

        .center-xs {
          text-align: center;
        }



      }

      .facepic {
        border-radius: 60px;
        height: 120px;
        width: 120px;
      }

      .soc-links {
        margin-top: 20px
      }

      .soc-links a {
        display: inline-block;
        color: #CCC;
        font-size: 30px;
        padding: 5px;
        -webkit-transition: .3s all;
                transition: .3s all;
      }
      .soc-links a:hover {
        color: #666;
      }
      .caplink {
        text-decoration: underline;
        display: none;
      }

      .item img {
        -webkit-transition: opacity 0.2s ease-in;
           -moz-transition: opacity 0.2s ease-in;
            -ms-transition: opacity 0.2s ease-in;
             -o-transition: opacity 0.2s ease-in;
                transition: opacity 0.2s ease-in;

      }
      .hover-img {
        /*display: none;*/
        position: absolute;
        opacity: 0.0;
        padding: 20px;
      }
      .main-img {
        position: absolute;
        /*display: inline-block;*/
        opacity: 1.0;
        padding: 20px;
      }
      .only-img {
        position: absolute;
        opacity: 1.0;
        padding: 20px;
      }

      .item:hover .main-img, 
      .item:focus .main-img, 
      .item.touch .main-img, 
      .item:active .main-img {
        opacity: 0.0;
      }

      .item:hover .hover-img, 
      .item:focus .hover-img, 
      .item.touch .hover-img, 
      .item:active .hover-img {
        opacity: 1.0;
      }

      button {
        -webkit-transition: all 0.2s ease-in-out;
           -moz-transition: all 0.2s ease-in-out;
            -ms-transition: all 0.2s ease-in-out;
             -o-transition: all 0.2s ease-in-out;
                transition: all 0.2s ease-in-out;
      }

      a.item {
        display: block;
      }

      h4.inspiration-block {
        margin: 20px 10px 30px;
        text-transform: none;
      }

      h4.inspiration-block span {
        text-decoration: underline;
      }

      .inspiration-item-spacer {
        content: "";
        height: 40px;
        width: 100%;
      }

      @media (min-width: 768px) {
        .inspiration-block {
          min-height: 610px;
        }
      }
      @media (min-width: 992px) {
       .inspiration-block {
          min-height: 760px;
        }
      }

      .initial {
        padding: 20px 10px;
      }

      .result {
        display: none;
        padding: 30px 10px;
      }

      .initial:after, .result:after {
        content: "";
        width: 100%;
        height: 8px;
        position: absolute;
        bottom: 0px;
        left: 0px;
        background-color: #C1A3FF;
      }



      .s-hidden {
          visibility:hidden;
          padding-right:10px;
      }
      .select {
        cursor:pointer;
        display:inline-block;
        position:relative;
        font: normal 16px/22px Arial, Sans-Serif;
        color: black;
        border: 2px solid #C1A3FF;
        height: 50px;
        margin-bottom: 5px;
        
      }
      .styledSelect {
        position: absolute;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        background-color: white;
        padding: 0 10px;
        font-weight: bold;
        display: inline-block;
        background-color: #FFFFFF;
        border-radius: 0px;
        font: 500 16px "Raleway", Helvetica, Arial, sans-serif;
        letter-spacing: 0.05em;
        color: #7666B5;
        text-shadow: none;
        box-shadow: none;
        padding: 14px 40px 12px 10px;
        cursor: pointer;
        text-align: center;
        text-transform: uppercase;
      }
      .styledSelect:after {
          content:"";
          width:0;
          height:0;
          border:5px solid transparent;
          border-color:black transparent transparent transparent;
          position:absolute;
          top:9px;
          right:6px;
      }

      .styledSelect:after {
          content: "";
          width: 0;
          height: 0;
          border: 6px solid transparent;
          border-color: #7666B5 transparent transparent transparent;
          position: absolute;
          top: 20px;
          right: 15px;
      }
      .styledSelect:active, .styledSelect.active {
        background-color:#C1A3FF;
        color: #fff;
      }

      .styledSelect:active:after, .styledSelect.active:after {
        border-color: #FFF transparent transparent transparent;
      }
      .options {
        display: none;
        position: absolute;
        top: 100%;
        right: 0;
        left: 0;
        z-index: 999;
        margin: 0 0;
        padding: 0 0;
        list-style: none;
        border: 2px solid #C1A3FF;
        background-color: white;
        -webkit-box-shadow: none;
        -moz-box-shadow: none;
        box-shadow: none;
        width: auto;
        max-height: 200px;
        overflow-y: scroll;
        -webkit-overflow-scrolling: touch;
        margin-right: -2px;
        margin-left: -2px;
        text-transform: uppercase;
      }
      .options li {
          padding:0 6px;
          margin:0 0;
          padding: 10px 10px;
      }
      .options li:hover {
        background-color:#C1A3FF;
        color: #fff;
      }

      .hidemobile {
        display: block;
      }
      .showmobile {
        display: none;
      }

      .findnumber-button.disabled {
        background: #CCC;
        pointer-events: none;
      }


      .quote-single {
        padding: 110px 5px 64px;
        font-size: 25px;
        background-image: url('https://storage.googleapis.com/troveup-imagestore/assets/img/quote.png');
        background-size: 200px;
        background-repeat: no-repeat;
        background-position: center 102px
      }

      .quote-single p {
        font-weight: 400;
      }

      .quote-single p span {
        font-size: 16px;
      }

      @media (max-width: 480px) {
        .styledSelect {
          font: 500 14px "Raleway", Helvetica, Arial, sans-serif;
        }

        #birth-month {
          min-width: 100px !important;
        }

        #birth-day {
          min-width: 70px !important;
        }
        #birth-year {
          min-width: 80px !important;
        }
        .hidemobile {
          display: none;
        }
        .showmobile {
          display: block;
        }
      }
/*
      img.restricted-img-size {
        max-width: 283px;
        max-height: 283px;
      }*/


      .img-holder {
        display: block;
        height: 283px;
        max-height: 283px;
        overflow: hidden;
        position: relative;
      }

      @media (max-width: 991px) { 
        .img-holder {
          padding-top: 73px;
        }

        #vertical-banner {
          padding-top: 150px;
        }

      }

      .showbreakonmobile {
        display: none;
      }

      @media (max-width: 768px) { 
        .showbreakonmobile {
          display: block;
        }
        .img-holder {
          height: 360px;
          max-height: 400px;
          max-width: 400px;
          margin: 0px auto;
          padding: 0px;
          margin-top: 50px;
        }
      }

      a.item:hover h2, a.item:focus h2, a.item:visited h2 {
        text-decoration: none !important;
      }

      #control-row {
        margin-bottom: 30px;
      }

      .options::-webkit-scrollbar {
        width: 14px;
      }
      .options::-webkit-scrollbar-thumb {
        border-radius: 14px;
        background-color: #C1A3FF;
        border: 5px solid #fff;
      }
      .options::-webkit-scrollbar-track {
        background: #fff;
        border-radius: 10px;
      }

      .socialbanner {
        padding: 10px;
        line-height: 30px;
        text-align: center;
      }
      .social-label {
        color: #666;
        display: inline-block;
        top: 5px;
        position: relative;
        margin-right: 10px;
      }

      #fbshare {
        margin-top: 10px;
        margin-right: 10px;
        background: #3E579B;
      }

      #fbshare:hover {
        background: #4D6CB2;
      }

      #twshare {
        margin-top: 10px;
        background: #23AAF1;
      }

      #twshare:hover {
        background: #4AC0F4;
      }

      #fbshare span span, #twshare span span {
        color: #FFF;
      }

    </style>
  </head>
  <body class="${bodyClass}">
    <c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
    <c:import url="../fragments/nav/topNavBar.jsp"/>


    <div class="spinstuff show"><span class="spinner page-fade-spinner"></span></div>
    <div class="choosestuff">
      <div class="container-fluid">
        <div class="row socialbanner">
          <div class="social-label">Share your storefront</div>
          <br class="showbreakonmobile">
          <button id="fbshare" onclick="fbShare()" class="btn btn--lightgray btn-md" style="margin-top:10px;"><span style="color: #fff"><span class="ion-social-facebook"></span> FACEBOOK</span></button>
          <button id="twshare" onclick="twitterShare()" class="btn btn--lightgray btn-md" style="margin-top:10px;"><span style="color: #fff"><span class="ion-social-twitter"></span> TWITTER</span></button>
          </div>
        </div>
      </div>
      <div class="container-fluid">
        <div class="row landingbanner">
          <div class="lander">
            <img class="facepic" src="${storeFront.profileImgUrl}">
            <h1>${storeFront.storeName}</h1>
            <h5>${storeFront.storeDescription}</h5>
            <!--button onclick="editprof()" class="btn btn--lightgray btn-md" style="margin-top:10px;"><span style="color: #fff">Edit Profile</span></button-->
            <!-- <div class="soc-links">
              <a class="ion-social-instagram" href="https://www.instagram.com//"></a>
              <a class="ion-social-youtube" href="https://www.youtube.com/user/"></a>
              <a class="ion-social-facebook" href="https://www.facebook.com/"></a>
            </div> -->
          </div>
        </div>
      </div>

      <div class="container">
        <div class="row">
          <c:forEach var="item" items="${storeFront.storeFrontItems}">
            <div class="col-sm-4 col-xs-12">
              <a class="item" href="/featured/item/${item.simpleItemId}">
                <div class="img-holder">
                  <c:if test="${empty item.hoverImageUrl}">
                    <img class="only-img img-responsive restricted-img-size" alt="160x160" src="${item.primaryDisplayImageUrl}">
                  </c:if>
                  <c:if test="${not empty item.hoverImageUrl}">
                    <img class="main-img img-responsive restricted-img-size" alt="160x160" src="${item.primaryDisplayImageUrl}">
                    <img class="hover-img img-responsive restricted-img-size" alt="160x160" src="${item.hoverImageUrl}">
                  </c:if>
                </div>
                <h2>${item.itemName}<br><span class="itemPrice">$${item.itemPrice}</span></h2>
              </a>
            </div>
          </c:forEach>
          </div>
        </div>
      <div class="container-fluid">
        <div class="row backtop" style="border-bottom: 1px solid #DEDEDE;">
          <div class="uparrow"><a id="scrollbutt"> Back to top<span class="icon ion-android-arrow-up"></span></a></div>
        </div>
      </div>

    <c:import url="../fragments/footers/footer.jsp"/>

    <script>

      function fbShare() {
        //window.location.href = "https://www.facebook.com/dialog/share?app_id=145634995501895&display=popup&href=https%3A%2F%2Fdevelopers.facebook.com%2Fdocs%2F&redirect_uri=https%3A%2F%2Fdevelopers.facebook.com%2Ftools%2Fexplorer";
        window.open('http://www.facebook.com/sharer/sharer.php?u=' + window.location.href, 'Share on Facebook', 'width=550,height=300');
      }

      function twitterShare() {
        window.open('http://twitter.com/intent/tweet?status=Trove%20lets%20you%20customize%20jewelry%20and%203D-prints%20it%20for%20you%20in%20precious%20metals+${landingUrl}', 'Share on Twitter', 'width=550,height=300');
      }

      //

      function land() {
        mixpanel.track("jaleesa_storefront");
      };

      $(document).ready(function() {
        land();
        var sStuff = document.querySelector(".spinstuff");
        var cStuff = document.querySelector(".choosestuff");
        sStuff.classList.toggle("show");
        cStuff.classList.toggle("show");
      });


      $("#scrollbutt").click(function(e) {
        $("html, body").animate({ scrollTop: 0 }, "slow");
        return false;
      });

    </script>
    </div>

  </body>
</html>
