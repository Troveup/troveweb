<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
    <c:import url="../fragments/headers/commonHead.jsp"/>
    <c:import url="../fragments/headers/legalHead.jsp"/>
    <title>Jaleesa Moses | The Numerology Collection</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=0">
    <link rel="icon" href="/resources/img/favicon.png?v=2">
    <link href='https://fonts.googleapis.com/css?family=Raleway:400,200,300,500,600,700' rel='stylesheet' type='text/css'>
    <link href="https://fonts.googleapis.com/css?family=Vollkorn:400,400italic,700,700italic" rel="stylesheet" type="text/css">
    <c:import url="../fragments/analytics/all.jsp"/>

    <style>
      .ion-quote:before {
        content: "";
        background-image: url('https://storage.googleapis.com/troveup-imagestore/assets/img/quote-lightgray-both.svg');
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
        font-size: 17px;
        letter-spacing: 1px;
        margin-bottom: 4px;
        text-align: center;
        font-weight:400;
      }

      h2 {
        font-family: "Raleway",Helvetica,Arial,sans-serif;
        font-size: 14px;
        line-height: 1.5;
        color: #7666B5;
        margin-top: 15px;
        margin-bottom: 20px;
        font-weight:400;
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
        text-decoration: underline;
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
        font-size: 2.2em;
        font-family: "Raleway", Helvetica, Arial, sans-serif;
        letter-spacing: 1px;
        color: #7666B5;
        font-weight: 300;
        text-transform: uppercase;
        /*margin-top: 60px;*/
        margin-bottom: 10px;
        position: relative;
        /*padding-bottom: 10px;*/
        line-height: 1.2em;
      }

/*      h6.firsth6:after {
        content: "";
        width: 110px;
        height: 3px;
        position: absolute;
        left: 50%;
        margin-left: -55px;
        bottom: 0px;
        background-color: #7666B5;
      }*/

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
        font-size: 14px;
        line-height: 1.5;
        max-width: 600px;
        margin:25 auto;
      }

      h5 {
        padding-bottom: 15px;
        margin-top: 0px;
        margin-bottom: 0px;
        font-size:14px;
        padding-top:10px;
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
        background-image: url('https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/jaleesamoses/profile/jaleesa-wide-banner.jpg');
        padding: 0 20px 0 20px;
        background-size: cover;
        background-repeat: repeat-x;
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
      }
      .main-img {
        position: absolute;
        /*display: inline-block;*/
        opacity: 1.0;
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
        padding: 30px 5px 30px;
        font-size: 20px;
        background-image: url('https://storage.googleapis.com/troveup-imagestore/assets/img/quote-lightgray-both.svg');
        background-size: 150px;
        background-repeat: no-repeat;
        background-position: center;
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

      @media (max-width: 768px) { 
        
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

    </style>
  </head>
  <body class="${bodyClass}">
    <c:import url="../fragments/analytics/adwordstag.jsp"/>
    <c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
    <c:import url="../fragments/nav/topNavBar.jsp"/>


    <div class="spinstuff show"><span class="spinner page-fade-spinner"></span></div>
    <div class="choosestuff">

      <div class="container-fluid">

        <div class="row landingbanner">
          <div class="lander">
            <img class="facepic" src="${influencer.profileImageThumbnailPath}">
            <h6 class="firsth6">The<br>Numerology Collection<br></h6>
            <h1 style="max-width: 400px; margin: 0 auto;line-height:1.5;">The first jewelry line by fine artist and Youtube star Jaleesa Moses</h1>
            <h5>Jewelry for Beautiful Souls. Made in NYC.</h5>
            <a data-scroll="about" href="#" role="button" id="scrolllink"><h5 style="text-decoration:underline;">Read More</h5></a>
            <div class="soc-links">
              <a class="ion-social-instagram" href="https://www.instagram.com/saythelees/" target="_blank"></a>
              <a class="ion-social-youtube" href="https://www.youtube.com/user/LeesBeans924" target="_blank"></a>
              <a class="ion-social-facebook" href="https://www.facebook.com/saythelees" target="_blank"></a>
            </div>
          </div>
        </div>
      </div>

    <div class="container">
      <div id="control-row" class="row">
        <div class="col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">
          <div class="initial">
            <h6 class="firsth6">Use your Birthday to find the right number for you</h6>
            <h2>Dating back to Ancient Greece, China, and India, Numerology reveals aspects of our lives from numerical patterns. Your Life Path Number is your most important number, as it predicts the types of challenges and opportunities that you will face. Calculate your Life Path Number by entering your birthday, and we'll find the sum of its numbers and reduce it to a single digit.<br><br>Show off your positive traits by keeping your necklace close to your heart.</h2>
            <select style="min-width: 160px;" id="birth-month">
              <option class="hidemobile removemeonload" value="0" selected>Month</option>
              <option class="hidemobile" value="1">January</option>
              <option class="hidemobile" value="2">February</option>
              <option class="hidemobile" value="3">March</option>
              <option class="hidemobile" value="4">April</option>
              <option class="hidemobile" value="5">May</option>
              <option class="hidemobile" value="6">June</option>
              <option class="hidemobile" value="7">July</option>
              <option class="hidemobile" value="8">August</option>
              <option class="hidemobile" value="9">September</option>
              <option class="hidemobile" value="10">October</option>
              <option class="hidemobile" value="11">November</option>
              <option class="hidemobile" value="12">December</option>
              <option class="showmobile" value="1">Jan</option>
              <option class="showmobile" value="2">Feb</option>
              <option class="showmobile" value="3">Mar</option>
              <option class="showmobile" value="4">Apr</option>
              <option class="showmobile" value="5">May</option>
              <option class="showmobile" value="6">June</option>
              <option class="showmobile" value="7">July</option>
              <option class="showmobile" value="8">Aug</option>
              <option class="showmobile" value="9">Sept</option>
              <option class="showmobile" value="10">Oct</option>
              <option class="showmobile" value="11">Nov</option>
              <option class="showmobile" value="12">Dec</option>
            </select>
            <select style="min-width: 100px;" id="birth-day">
              <option class="removemeonload" value="0" selected>Day</option>
              <c:forEach var="i" begin="1" end="31">
                <option value="${i}">${i}</option>
              </c:forEach>
            </select>
            <select style="min-width: 140px;" id="birth-year">
              <option class="removemeonload" value="0" selected>Year</option>
              <c:forEach var="i" begin="0" end="100">
                <option value="${2016-i}">${2016-i}</option>
              </c:forEach>
            </select>
            <button class="findnumber-button disabled" onclick="performNumerologyCalculation();">Calculate your number</button>
          </div>
          <div class="result">
            <h6 class="secondh6">Your life path<br>Number is...<br>
              <span id="numerology-result"></span>
            </h6>
            <h2 id="output"></h2>
            <button class="gotonecklace-button" onclick="goToNecklace();">Go to your necklace</button>
            <a class="try-another" href="#">Try another birthday</a>
          </div>
        </div>
      </div>


      
      <c:forEach var="item" items="${items}" varStatus="loop">
        <c:choose>

          <%-- Special handling for the quotation on the first row --%>
          <c:when test="${loop.index eq 0}">
            <div class="row">
              <div class="col-sm-4 col-xs-12">
                <a class="item" href="/featured/item/${item.simpleItemId}">
                  <div class="img-holder">
                    <img class="main-img img-responsive restricted-img-size" alt="160x160" src="${item.primaryDisplayImageUrl}">
                    <img class="hover-img img-responsive restricted-img-size" alt="160x160" src="${item.hoverImageUrl}">
                  </div>
                  <h2>${item.itemName}<br><span class="itemPrice">$${item.itemPrice}</span></h2>
                </a>
              </div>
              <div class="col-sm-4 col-xs-12">
                <div class="quote-single">
                  <p style="margin-top:70px;">It's important that you share your positive energy with everyone you meet.<br><br>
                    <span>Jaleesa Moses</span></p>
                </div>
              </div>
          </c:when>
          <c:when test="${loop.index eq 1}">
            <div class="col-sm-4 col-xs-12">
              <a class="item" href="/featured/item/${item.simpleItemId}">
                <div class="img-holder">
                  <img class="main-img img-responsive restricted-img-size" alt="160x160" src="${item.primaryDisplayImageUrl}">
                  <img class="hover-img img-responsive restricted-img-size" alt="160x160" src="${item.hoverImageUrl}">
                </div>
                <h2>${item.itemName}<br><span class="itemPrice">$${item.itemPrice}</span></h2>
              </a>
            </div>
            </div>
          </c:when>
          <%-- Because we're no longer counting on multiples of 3, handle the second row specially --%>
          <c:when test="${loop.index gt 1 and loop.index lt 5}">
            <c:if test="${loop.index eq 2}">
              <div class="row">
            </c:if>
              <div class="col-sm-4 col-xs-12">
                <a class="item" href="/featured/item/${item.simpleItemId}">
                  <div class="img-holder">
                    <img class="main-img img-responsive restricted-img-size" alt="160x160" src="${item.primaryDisplayImageUrl}">
                    <img class="hover-img img-responsive restricted-img-size" alt="160x160" src="${item.hoverImageUrl}">
                  </div>
                  <h2>${item.itemName}<br><span class="itemPrice">$${item.itemPrice}</span></h2>
                </a>
              </div>
              <c:if test="${loop.index eq 4}">
                </div>
              </c:if>
          </c:when>
          <%-- Special handling for the banner image on the right side of the page --%>
          <c:when test="${loop.index gt 4 and loop.index lt 9}">
            <c:if test="${loop.index eq 5}">
              <div class="row">
                <div class="col-sm-4 col-xs-12">
            </c:if>
              <div class="inspiration-item-spacer"></div>
              <a class="item" href="/featured/item/${item.simpleItemId}">
                <div class="img-holder">
                  <img class="main-img img-responsive restricted-img-size" alt="160x160" src="${item.primaryDisplayImageUrl}">
                  <img class="hover-img img-responsive restricted-img-size" alt="160x160" src="${item.hoverImageUrl}">
                </div>
                <h2>${item.itemName}<br><span class="itemPrice">$${item.itemPrice}</span></h2>
              </a>
            <c:if test="${loop.index eq 6}">
              </div>
              <div class="col-sm-4 col-xs-12">
            </c:if>
            <c:if test="${loop.index eq 8}">
              </div>
              <div class="col-sm-4 col-xs-12"><img id="vertical-banner" class="main-img img-responsive" src="https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/jaleesamoses/profile/jaleesa-tall-banner.jpg" alt="Image of Jaleesa" border="solid" style="border-width: 1px; width: 283px;"></div>
              </div>
            </c:if>
          </c:when>
          <c:otherwise>
            <c:if test="${((loop.index) % 3 eq 0)}">
              <div class="row">
            </c:if>
              <div class="col-sm-4 col-xs-12">
                <a class="item" href="/featured/item/${item.simpleItemId}">
                  <div class="img-holder">
                    <img class="main-img img-responsive restricted-img-size" alt="160x160" src="${item.primaryDisplayImageUrl}">
                    <img class="hover-img img-responsive restricted-img-size" alt="160x160" src="${item.hoverImageUrl}">
                  </div>
                  <h2>${item.itemName}<br><span class="itemPrice">$${item.itemPrice}</span></h2>
                </a>
              </div>
            <c:if test="${((loop.index + 1) % 3 eq 0)}">
            </div>
            </c:if>
          </c:otherwise>
        </c:choose>
      </c:forEach>

      <div class="row">
        <div class="aboutblock col-sm-12" id="about" data-anchor="about">
          <h4><span>About this Collection</span><p>The Numerology Collection marks the first foray into jewelry by fine artist and YouTube star Jaleesa Moses. Drawing from her deep interest in Numerology and Astrology, each necklace is made to be a proud reminder of who you are at your deepest core, revealed by your Life Path Number.</p><p>The hoop represents the cyclical and continuous nature of life while each and every number reflects a different set of traits and personalities. As each necklace as a hoop, this represents the shared experiences of the 7 billion humans on this earth. By contrast, your individual Life Path Number celebrates your individuality and positive traits. The necklace celebrates the individual as well as the collective nature of people everywhere.</p></h4>
        </div>
      </div>

     

    </div>

    <div class="container-fluid">
      <div class="row backtop" style="border-bottom: 1px solid #DEDEDE;">
        <div class="uparrow"><a id="scrollbutt"> Back to top<span class="icon ion-android-arrow-up"></span></a></div>
      </div>
    </div>

    <c:import url="../fragments/footers/footer.jsp"/>

    <script>

      $("#scrolllink").click(function(e) {
        var os = $('#about').offset().top
        $('body,html').animate({ scrollTop: os }, 500);
      });

      var birthMonthSelect = $('#birth-month');
      var birthDaySelect = $('#birth-day');
      var birthYearSelect = $('#birth-year');
      var numerologyResultContainer = $('#numerology-result');
      var numberItemIdMapping = JSON.parse('${itemIdMapping}');

      var lifepaths = {};
      lifepaths["1"] = "You value determination, independence, ambition, and authenticity. You have conviction in your decisions and express ownership over your thoughts and actions. You are tenacious and motivated. Once you are committed to a goal, nothing and no one can get in your way.";
      lifepaths["2"] = "You possess an artist’s soul. Your sensitivity and perceptiveness allow you to adapt to situations in a diplomatic way. You are conscious of your feelings and the feelings of others, giving you the ability to understand and accommodate the emotional needs of yourself and others.";
      lifepaths["3"] = "Life Path 3 is home to actors, poets, writers, and musicians. Your robust creativity and wonderful communication skills allow you to engage in artistic expression, especially in the realm of speaking, writing, or acting. You require commitment and discipline for your talent to blossom.";
      lifepaths["4"] = "Your organizational and methodical skills allow you to take a balanced approach to solving any problem. You are decisive and rational. You are both idealistic and practical, allowing you to devise magnificent plans and bring them to fruition.";
      lifepaths["5"] = "You are adventurous and curious. You have a hunger for life and an inclination toward freedom. You cannot be tied down as you are in a state of constant evolution. You are compassionate and friendly. Your upbeat, happy-go-lucky personality is inspiring to those that surround you.";
      lifepaths["6"] = "You are a nurturer, healer, and helper. You provide warmth and solace to those in need, often putting others before yourself. Your joy stems from your utility. You are most happy when you are offering support, service, and advice to others.";
      lifepaths["7"] = "You have a strong sense of spirit. You are inquisitive and observant. You value thoroughness, and you often pursue perfection. You are a thinker and an investigator, who aims to understand the unknown and find answers to life’s burning questions.";
      lifepaths["8"] = "You are a leader. You are goal-oriented and possess the ability to organize, govern, and direct. You have a keen eye for the big picture and courage in your convictions, especially with regard to getting ahead and following goals to completion.";
      lifepaths["9"] = "You are compassionate and idealistic. You are motivated by your utopian mentality. You are willing to make large sacrifices in an effort to make the world a better place. You find joy in giving, especially to those who are less fortunate than yourself.";
      lifepaths["11"] = "You are intuitive and energetic. You have the innate ability to understand and inspire others. You live a life of extremes. You possess more potential than you know. While your energy may sometimes be misunderstood, you are naturally sensitive and insightful.";
      lifepaths["22"] = "You possess a great spiritual understanding. Intuition is your natural gift. You have the ability to harness the beauty of an idea, then follow it through to reality. You are hardworking and dedicated. You have great potential for power and success.";
      lifepaths["33"] = "You are altruistic and unselfish. You are a mover and a shaker, using your wisdom and understanding to reach for your humanitarian goals. You are happiest when you are focusing your emotions and spirit on helping others.";

      function land() {
        mixpanel.track("jaleesa_storefront");
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

      function scrollToContainer(selector) {
        var scrollAmount = $(selector).offset().top;
        var wwidth = $(window).width();
        if (wwidth >= 767) {
          scrollAmount = scrollAmount - 40;
        }
        $("html, body").animate({ scrollTop: scrollAmount }, "slow");
      }

      $(document).ready(function() {
        land();
        var sStuff = document.querySelector(".spinstuff");
        var cStuff = document.querySelector(".choosestuff");
        var itemLink = document.querySelector("a");
        sStuff.classList.toggle("show");
        cStuff.classList.toggle("show");

        // scroll for mobile
        var ad = getUrlVars()["ad"];
        var ww = $(window).width();
        if (ad && ww <= 767) {
          if (ad == "Facebook" || ad == "facebook") {
            scrollToContainer(".initial");
          }
        }

        $('.item').click(function (e) {
          e.preventDefault();
          var addressValue = $(this).attr("href");
          sStuff.classList.toggle("show");
          cStuff.classList.toggle("show");
          mixpanel.track("onboard_choose_item", {
            "itemLink": addressValue
          });
          setTimeout(function(){ window.location.href = addressValue; }, 300);
        });



        // mobile hover touch behavior
        $('.item').on('touchstart', function () { 
          $(this).find('.main-img').css('opacity', '0.0'); 
          $(this).find('.hover-img').css('opacity', '1.0'); 
          var that = this;
          setTimeout(function(){
            $(that).find('.main-img').css('opacity', '1.0');
            $(that).find('.hover-img').css('opacity', '0.0'); 
          }, 1000);
        });

        $('.item').on('touchend', function () { 
          // var that = $(this);
          // setTimeout(function(that){
          //   that.find('.main-img').css('opacity', '1.0'); 
          //   that.find('.hover-img').css('opacity', '0.0'); 
          // }, 2000);
        });

        $('.itemPrice').each(function () {
          var currentPriceHtml = $(this).html();
          var newPriceHtml = currentPriceHtml.replace(/\.00$/,'');
          $(this).html(newPriceHtml);
        });

        // Iterate over each select element
        setupSelects();

        $(".removemeonload").remove();
        $('#birth-month').val(0);
        $('#birth-day').val(0);
        $('#birth-year').val(0);
      });

      $('select').on('change', function () {
        alert('Value change');
      });


      $("#scrollbutt").click(function(e) {
        $("html, body").animate({ scrollTop: 0 }, "slow");
        return false;
      });

      $(".try-another").click(function(e) {
        e.preventDefault();
        $(".initial").css("display", "block");
        $(".result").css("display", "none");
      });

      function performNumerologyCalculation() {
        var numerologyValue = calculateNumerologyNumber();
        setNumerologyResult(numerologyValue);
        scrollToContainer(".result");
      }


      function setupSelects() {
        $('select').each(function () {
            // Cache the number of options
            var $this = $(this);
            var numberOfOptions = $(this).children('option').length;
            // Hides the select element
            $this.addClass('s-hidden');
            // Wrap the select element in a div
            $this.wrap('<div class="select"></div>');
            // Insert a styled div to sit over the top of the hidden select element
            $this.after('<div class="styledSelect"></div>');
            // Cache the styled div
            var $styledSelect = $this.next('div.styledSelect');
            // Show the first select option in the styled div
            $styledSelect.text($this.children('option').eq(0).text());
            // Insert an unordered list after the styled div and also cache the list
            var $list = $('<ul />', {
                'class': 'options'
            }).insertAfter($styledSelect);
            // Insert a list item into the unordered list for each select option
            for (var i = 0; i < numberOfOptions; i++) {
                $('<li />', {
                    text: $this.children('option').eq(i).text(),
                    rel: $this.children('option').eq(i).val(),
                    class: $this.children('option').eq(i).attr('class')
                }).appendTo($list);
            }
            // Cache the list items
            var $listItems = $list.children('li');
            // Show the unordered list when the styled div is clicked (also hides it if the div is clicked again)
            $styledSelect.click(function (e) {
                e.stopPropagation();
                $('div.styledSelect.active').each(function () {
                  $(this).removeClass('active').next('ul.options').hide();
                });
                $(this).toggleClass('active').next('ul.options').toggle();
            });

            // Hides the unordered list when a list item is clicked and updates the styled div to show the selected list item
            // Updates the select element to have the value of the equivalent option
            $listItems.click(function (e) {
                e.stopPropagation();
                $styledSelect.text($(this).text()).removeClass('active');
                $this.val($(this).attr('rel'));
                $list.hide();
                

                if ( $('#birth-month').val() != null && $('#birth-day').val() != null && $('#birth-year').val() != null) {
                  $(".findnumber-button").removeClass('disabled');
                } else {
                  
                }
                /* alert($this.val()); Uncomment this for demonstration! */
            });

            // Hides the unordered list when clicking outside of it
            $(document).click(function () {
                $styledSelect.removeClass('active');
                $list.hide();
            });

        });
      }

      function goToNecklace(num) {
        window.location.href="/featured/item/" + num;
      }

      function calculateNumerologyNumber() {
        var birthMonth = parseInt($(birthMonthSelect).val());
        var birthDay = parseInt($(birthDaySelect).val());
        var birthYear = parseInt($(birthYearSelect).val());

        if (birthMonth == 0 || birthDay == 0 || birthYear == 0) {
          numerologyResultContainer.html('Please make sure you have provided your full birthday!');
        } else {

          while(!isValidNumerologyNumber(birthMonth)) {
            birthMonth = numeroligifyNumber(birthMonth);
          }

          while(!isValidNumerologyNumber(birthDay)) {
            birthDay = numeroligifyNumber(birthDay);
          }

          while(!isValidNumerologyNumber(birthYear)) {
            birthYear = numeroligifyNumber(birthYear);
          }

          var concatenatedBirth = birthMonth + birthDay + birthYear;

          //var concatenatedBirth = birthMonth.toString() + birthDay.toString() + birthYear.toString();
          //concatenatedBirth = parseInt(concatenatedBirth);

          while(!isValidNumerologyNumber(concatenatedBirth)) {
            concatenatedBirth = numeroligifyNumber(concatenatedBirth);
          }

          return concatenatedBirth;
        }
      }

      function numeroligifyNumber(number) {
        var rval = number;

        if (!isValidNumerologyNumber(number)) {

          rval = 0;
          var numberStringForm = number.toString();

          for (var i = 0; i < numberStringForm.length; ++i) {
            rval += parseInt(numberStringForm[i]);
          }
        }

        return rval;
      }

      function isValidNumerologyNumber(number) {
        return (number > 0 && number < 10) || number == 11 || number == 22 || number == 33;
      }


      function setNumerologyResult(number) {
        $('#numerology-result').text(number);
        $('#output').text(lifepaths[number.toString()]);
        $(".gotonecklace-button").attr("onClick", "goToNecklace('" + numberItemIdMapping[number] + "');");


        $(".initial").css("display", "none");
        $(".result").css("display", "block");
      }




    </script>


  </body>
</html>
