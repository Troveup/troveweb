<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="trove" uri="http://troveup.com/jsp/tlds/itemimage" %>

<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=0">
  <link rel="icon" href="/resources/img/favicon.png?v=2">
  <link href="/resources/stylesheets/jquery-ui.min.css" rel="stylesheet">
  <link href="/resources/stylesheets/jquery-ui.structure.min.css" rel="stylesheet">
  <link rel="stylesheet" href="/resources/stylesheets/theme-custom.css" rel="stylesheet">
  <script type="text/javascript" src="https://code.jquery.com/jquery-1.11.3.min.js"></script>
  <link href="https://fonts.googleapis.com/css?family=Raleway:400,200,300,500,600,700" rel="stylesheet" type="text/css">
  <script>
    $(document).on("mobileinit", function () {
      $.mobile.ajaxEnabled = false;
      $.mobile.linkBindingEnabled = false;
    });

    $(document).bind("mobileinit", function () {
      $.mobile.page.prototype.options.keepNative = "select.ignore, a, button, input.slideinput, input.form-control, ul, li, select.form-control";
    });
  </script>
  <link href="https://code.jquery.com/mobile/1.4.5/jquery.mobile.structure-1.4.5.css" rel="stylesheet">
  <script type="text/javascript" src="https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
  <link href="/resources/stylesheets/sliders/base.css" rel="stylesheet">
  <link rel="stylesheet" href="/resources/stylesheets/browse.css">
  <script type="text/javascript" src="/resources/js/vendor/head.js"></script>
  <link href="//cdnjs.cloudflare.com/ajax/libs/x-editable/1.5.0/bootstrap3-editable/css/bootstrap-editable.css"
        rel="stylesheet"/>
  <script
      src="//cdnjs.cloudflare.com/ajax/libs/x-editable/1.5.0/bootstrap3-editable/js/bootstrap-editable.min.js"></script>
  <script type="text/javascript" src="/resources/js/namespace.js"></script>
  <script type="text/javascript" src="/resources/js/TROVE.js"></script>
  <script type="text/javascript"
          src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.3/handlebars.min.js"></script>
  <script type="text/javascript"
          src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
  <link href="/resources/vendor/nouislider/nouislider.min.css" rel="stylesheet">
  <script type="text/javascript" src="/resources/vendor/nouislider/nouislider.min.js"></script>
  <link href="/resources/stylesheets/main.css" rel="stylesheet">
  <link rel="stylesheet" href="/resources/stylesheets/cards.css">
  <link href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
  <script type="text/javascript" src="/resources/js/FORGE.js"></script>
  <script type="text/javascript" src="/resources/js/TROVE.js"></script>
  <link href="/resources/stylesheets/fancybox.css" rel="stylesheet">
  <script type="text/javascript" src="/resources/js/vendor/fancybox.js"></script>
  <script type="text/javascript"
          src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.13.1/jquery.validate.js"></script>
  <script type="text/javascript"
          src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.13.1/additional-methods.js"></script>
  <script type="text/javascript"
          src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>
  <c:import url="../fragments/analytics/all.jsp"/>
  <script>TROVE.setToken("${_csrf.token}");</script>
  <%--TODO:  When commonHead can be added to customize, remove these stylesheets--%>
  <link href="/resources/stylesheets/trovecustomizer.css" rel="stylesheet">
  <link rel="stylesheet" href="/resources/stylesheets/spinner.css">
  <style type="text/css">
    #nav-col, #navtog, .free-shipping-notice, .box.profile, .box.cart, #cta-buttons, .holdprice {
      opacity: 0.0 !important;
    }

    .navbar-inner {
      background: #fff;
    }

    .modal-header {
      box-shadow: none;
    }

    #controls {
      max-height: 656px;
      overflow: auto;
    }

    .break {
      background-color: #FFFFFF;
    }

    .modal-body {
      padding-bottom: 70px;
    }

    /*.actionButtonBack {
      z-index: 1000;
      position: fixed;
      top: 10px;
      left: 10px;
      padding: 8px 20px;
      background: #dedede;
      border: 1px solid #FFF;
      color: #FFF;
      letter-spacing: 0.15em;
      font-size: 15px;
      -webkit-font-smoothing: antialiased;
      font-weight: 400;
      cursor: pointer;
      -webkit-transition: .3s all;
              transition: .3s all;
    }

    .actionButtonBack:hover {
      background: #dedede;
      color: #666;
    }*/

    .actionButtonBack {
      z-index: 1000;
      position: fixed;
      top: 10px;
      left: 10px;
      padding: 8px 20px;
      border-radius: 2em;
      background: transparent;
      border: 1px solid #FFF;
      color: #FFF;
      letter-spacing: 0.15em;
      font-size: 15px;
      -webkit-font-smoothing: antialiased;
      font-weight: 400;
      cursor: pointer;
      -webkit-transition: .3s all;
      transition: .3s all;
    }

    .actionButtonBack:hover {
      background: transparent;
      color: #656565;
      border-color: #656565;
    }

    .actionButtonNext:hover {
      background: #F26868;
      border-color: #F26868;
    }

    .actionButtonNext {
      position: relative;
      width: 100%;
      padding: 12px 20px;
      background: #DD2435;
      border: 1px solid #DD2435;
      border-radius: 2px;
      color: #FFF;
      letter-spacing: 0.15em;
      font-size: 15px;
      -webkit-font-smoothing: antialiased;
      text-transform: uppercase;
      font-weight: 600;
      cursor: pointer;
      -webkit-transition: .3s all;
      transition: .3s all;
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
      font-family: Helvetica, Arial, sans-serif;
      display: none;
    }

    .navbar-inner {
      background: #b2b8b8;
    }

    .nav-center {
      border-bottom: 1px solid #b2b8b8;
    }

    .cards_product_wrapper.customizer {
      margin-top: 0px;
    }

    .ui-mobile .ui-page-active {
      display: block;
      overflow: visible;
      overflow-x: visible;
    }

    .ui-page {
      margin: 0px;
      padding: 0px 0px;
      margin-top: 58px;
      margin-bottom: -58px;
    }

    h4.modalHeadline {
      font-size: 17px;
      line-height: 1.25em;
      margin-top: -55px;
      max-width: 500px;
      display: inline-block;
    }

    .product-title-bar {
      padding-top: 8px;
      padding-right: 6px;
      padding-left: 20px;
      margin-bottom: -12px;
    }

    .user_card_content_a {
      padding: 0px;
      height: auto;
      overflow: auto;
    }

    .product-description-image {
      border-top: 2px solid #FFF;
      border-bottom: 2px solid #FFF;
    }

    @media screen and (max-width: 468px) {
      .actionButtonNext {
        position: fixed;
        bottom: 0px;
        right: 0px;
        left: 0px;
        width: 100%;
        padding: 16px 20px;
        background: #DD2435;
        border: 1px solid #DD2435;
        color: #FFF;
        letter-spacing: 0.15em;
        font-size: 20px;
        -webkit-font-smoothing: antialiased;
        font-weight: 600;
        cursor: pointer;
        -webkit-transition: .3s all;
        transition: .3s all;
      }

      .btn.btn--product--description.btnspec.left {
        display: none;
      }

      .btn.btn--product--description.btnspec.right {
        display: none;
      }
    }

    .labyo {
      text-align: left;
      font-size: 12px;
    }

    .ui-input-text, .ui-input-search {
      border: 0px;
    }

    input.form-control {
      border-radius: 4px;
      border: 1px solid #DEDEDE;
      margin-bottom: 6px;
    }

    .ui-select {
      margin-top: .2em;
      margin-bottom: .2em;
      position: relative;
      border: 0px;
    }

    .modal-body {
      padding-bottom: 9px;
    }

    .form-group {
      text-align: left;
    }

    #addressSubmit {
      width: 100%;
      margin-left: -15px;
      margin-top: 10px;
      height: 50px;
      background: #DD2435;
      border: 1px solid #DD2435;
      color: #FFF;
      letter-spacing: 0.15em;
      font-size: 16px;
      -webkit-font-smoothing: antialiased;
      font-weight: 600;
    }

    #addressSubmit:hover {
      background: #F26868;
      border: 1px solid #F26868;
    }

    #addressSubmit.disabled {
      pointer-events: none;
      background: #CCC;
      color: #666;
      border: 1px solid #CCC;
      cursor: not-allowed;
    }

    @-webkit-keyframes spin {
      to {
        -webkit-transform: rotate(360deg);
        transform: rotate(360deg);
      }
    }

    @-moz-keyframes spin {
      to {
        -webkit-transform: rotate(360deg);
        transform: rotate(360deg);
      }
    }

    @-ms-keyframes spin {
      to {
        -webkit-transform: rotate(360deg);
        transform: rotate(360deg);
      }
    }

    @-o-keyframes spin {
      to {
        -webkit-transform: rotate(360deg);
        transform: rotate(360deg);
      }
    }

    @keyframes spin {
      to {
        -webkit-transform: rotate(360deg);
        transform: rotate(360deg);
      }
    }

    .spinner {
      display: block;
      width: 30px;
      height: 30px;
      margin: 20% 50%;
      top: -10px;
      position: absolute;
      left: -15px;
      border: 3px solid rgba(200, 200, 200, 0.8);
      border-top-color: transparent;
      border-radius: 100%;
      -webkit-animation: spin .6s infinite linear;
      -moz-animation: spin .6s infinite linear;
      -ms-animation: spin .6s infinite linear;
      -o-animation: spin .6s infinite linear;
      animation: spin .6s infinite linear;
      margin: 25% 50%;
    }

    .spinstuff {
      opacity: 0.0;
      -webkit-transition: 1s opacity;
      transition: 1s opacity;
      pointer-events: none;
    }

    .formstuff {
      opacity: 0.0;
    }

    .formstuff.show {
      opacity: 1.0;
    }

    .spinstuff.show {
      opacity: 1.0;
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

    .step {
      display: inline-block;
      font-size: 13px;
      letter-spacing: .06em;
      min-width: 180px;
      font-weight: 500;
      color: #656565;
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

    @media screen and (min-width: 768px) {
      .modal-dialog {
        margin: 0px auto 0px auto;
        width: 750px;
      }

      .product-cta-btns {
        display: none;
      }

      .steps {
        display: inline-block;
      }
    }

    @media screen and (max-width: 767px) {
      .noUi-horizontal .noUi-handle {
        width: 50px;
        height: 50px;
        left: -20px;
        top: -22px;
      }

      .currentStep {
        display: inline-block;
      }

      .product-description-image {
        border-top: 2px solid #FFFFFF;
      }
    }

    .errorStuff {
      margin-bottom: 24px;
      margin-right: 10px;
      margin-left: 8px;
      background: #FD8989;
      padding: 10px;
      color: #FFF;
      border-radius: 4px;
      display: none;
    }

    .errorStuff.show {
      display: block;
    }

    .modal-content {
      padding: 35px;
    }

    span.glyphicon {
      margin-top: -50px;
      margin-right: 2px;
      float: right;
      position: relative;
    }

    span.glyphicon.form-control-feedback.select {
      margin-top: -43px;
    }

    .ui-btn-icon-right {
      padding-right: 2.5em;
      padding: 0px;
      height: 41px;
    }

    select.form-control {
      height: 38px;
    }

    input.form-control {
      margin-bottom: 15px;
    }

    .gemIcon {
      width: 30px;
    }

    #customizetext {
      display: none;
    }

    .engraveblock.hidemob {
      display: none;
    }

    .colorchoice {
      position: relative;
      margin-bottom: 40px;
      margin-top: 20px;
    }

    #colorchoice {
      display: inline-block;
      width: 40%;
      float: left;
      padding: 7px;
      font-size: 18px
    }

    #colorChoices {
      display: inline-block;
      width: 60%;
      text-align: right;
    }

    .pricechoice {
      position: relative;
      margin-bottom: 60px;
      margin-top: 20px;
    }

    #pricechoice {
      display: inline-block;
      width: 40%;
      float: left;
      padding: 7px;
      font-size: 18px
    }

    #priceChoices {
      display: inline-block;
      width: 60%;
      text-align: right;
    }

    #priceChoice {
      display: inline-block;
      width: 40%;
      float: left;
      padding: 0px 0px 0px 10px;
      font-size: 14px;
      color: #666666;
    }

    #priceChoice span {
      font-size: 145%;
      text-transform: none;
      color: #505050;
      font-weight: 500;
    }

    .btn.colorBtn {
      height: 34px;
      width: 34px;
      padding: 0px;
      border-radius: 2em;
      margin: 1px;
      overflow: hidden;
      opacity: 0.6;
      -webkit-transition: all .3s ease;
      -moz-transition: all .3s ease;
      transition: all .3s ease;
      background-size: cover;
      font-size: 36px;
      color: transparent;
    }

    .btn.colorBtn:hover {
      opacity: 1.0;
    }

    #colorGold {
      background: url('https://storage.googleapis.com/troveup-imagestore/assets/gold.jpg');
    }

    #colorSilver {
      background: url('https://storage.googleapis.com/troveup-imagestore/assets/silver.jpg');
    }

    #colorRose {
      background: url('https://storage.googleapis.com/troveup-imagestore/assets/rosegold.jpg');
    }

    .btn.colorBtn.picked {
      color: #FFF;
      opacity: 1.0;
    }

    .item-options.hidemob {
      height: 0px !important;
      width: 0px !important;
      padding: 0px !important;
    }

    .material-selector-container {
      opacity: 0.0;
    }

    .chain-container {
      display: none;
    }

    .ui-select .ui-btn > span:not(.ui-li-count) {
      display: block;
      text-overflow: ellipsis;
      overflow: hidden !important;
      white-space: nowrap;
      line-height: 3em;
    }

    .ui-select {
      border: 1px solid #DEDEDE;
    }

    .ui-btn.ui-radio-on.ui-btn-a:after {
      border-color: #F26868;
    }

    .selector-title, .item-size {
      display: none;
    }

    .ui-controlgroup-controls .ui-btn.ui-corner-all {
      border: 1px solid #DEDEDE;
      padding: 10px;
    }

    #colorChoices {
      display: inline-block;
      width: 60%;
      text-align: right;
      padding: 8px 8px 0px 0px;
    }

    #colorChoice {
      display: inline-block;
      width: 40%;
      float: left;
      padding: 0px 0px 0px 10px;
      font-size: 14px;
      color: #666666;
    }

    #colorChoice span {
      font-size: 145%;
      text-transform: none;
      color: #505050;
      font-weight: 500;
    }

    #materialnav, #sizenav {
      display: none;
    }

    .sizechoice {
      position: relative;
      margin-bottom: 50px;
      margin-top: 20px;
      text-align: right;
    }

    .sizehelp {
      color: #f26868;
      position: absolute;
      width: 100%;
      right: 10px;
      top: 50px;
      font-size: 110%;
    }

    #sizeChoices {
      display: inline-block;
      width: 60%;
      text-align: right;
      padding: 8px 8px 0px 0px;
    }

    #sizeChoice {
      display: inline-block;
      width: 40%;
      float: left;
      padding: 0px 0px 0px 10px;
      font-size: 14px;
      color: #666666;
      text-align: left;
    }

    #sizeChoice span {
      font-size: 145%;
      text-transform: none;
      color: #505050;
      font-weight: 500;
    }

    #sizeChoices {
      padding: 0;
      margin: 2px 10px 0px 0px;
      border: 1px solid #FFF;
      width: 140px;
      border-radius: 4px;
      overflow: hidden;
      background-color: #FFF;
      background: #F0F0F0;
      text-align: right;
      opacity: 0.6;
      -webkit-transition: all .3s ease;
      -moz-transition: all .3s ease;
      transition: all .3s ease;
    }

    #sizeChoices select {
      padding: 4px 10px 4px 10px;
      width: 100%;
      border: none;
      box-shadow: none;
      background-color: transparent;
      background-image: none;
      -webkit-appearance: none;
      -moz-appearance: none;
      appearance: none;
      font-size: 16px;
      font-weight: 400;
      font-family: helvetica;
      color: #666666;
      background: transparent;
      border-radius: 0px;
      border: 1px solid #F0F0F0;
      -webkit-transition: all .3s ease;
      -moz-transition: all .3s ease;
      transition: all .3s ease;
    }

    #sizeChoices::before {
      position: absolute;
      color: #4F5049;
      right: 23px;
      top: 12px;
      font-size: 16px;
      pointer-events: none;
      -webkit-transition: all .3s ease;
      -moz-transition: all .3s ease;
      transition: all .3s ease;
    }

    #sizeChoices select:hover {
      opacity: 1.0;
      background: #AFAFAB;
      border: 1px solid #AFAFAB;
      outline: none;
      color: #FFF;
      cursor: pointer;
    }

    #sizeChoices:hover::before {
      color: #000;
    }

    #sizeChoices::before {
      color: #000;
    }

    .controlchoice {
      position: relative;
      margin-bottom: 50px;
      margin-top: 20px;
      text-align: right;
    }

    #controlChoices {
      display: inline-block;
      width: 60%;
      text-align: right;
      padding: 8px 8px 0px 0px;
    }

    #controlChoice {
      display: inline-block;
      width: 40%;
      float: left;
      padding: 0px 0px 0px 10px;
      font-size: 14px;
      color: #666666;
      text-align: left;
    }

    #controlChoice span {
      font-size: 145%;
      text-transform: none;
      color: #505050;
      font-weight: 500;
    }

    #controlChoices {
      padding: 0;
      margin: 2px 10px 0px 0px;
      border: 1px solid #FFF;
      width: 105px;
      border-radius: 4px;
      overflow: hidden;
      background-color: #FFF;
      background: #F0F0F0;
      text-align: right;
      opacity: 0.6;
      -webkit-transition: all .3s ease;
      -moz-transition: all .3s ease;
      transition: all .3s ease;
    }

    #controlChoices select {
      padding: 4px 10px 4px 10px;
      width: 100%;
      border: none;
      box-shadow: none;
      background-color: transparent;
      background-image: none;
      -webkit-appearance: none;
      -moz-appearance: none;
      appearance: none;
      font-size: 16px;
      font-weight: 400;
      font-family: helvetica;
      color: #666666;
      background: transparent;
      border-radius: 0px;
      border: 1px solid #F0F0F0;
      -webkit-transition: all .3s ease;
      -moz-transition: all .3s ease;
      transition: all .3s ease;
    }

    #controlChoices::before {
      position: absolute;
      color: #4F5049;
      right: 23px;
      top: 12px;
      font-size: 16px;
      pointer-events: none;
      -webkit-transition: all .3s ease;
      -moz-transition: all .3s ease;
      transition: all .3s ease;
    }

    #controlChoices select:hover {
      opacity: 1.0;
      background: #AFAFAB;
      border: 1px solid #AFAFAB;
      outline: none;
      color: #FFF;
      cursor: pointer;
    }

    #controlChoices:hover::before {
      color: #000;
    }

    #controlChoices::before {
      color: #000;
    }

    .slideinput {
      float: right;
      width: 50px;
      height: 32px;
      border-radius: 4px;
      border: 1px solid #F6F6F6;
      background: #F6F6F6;
      padding: 5px 5px 5px 5px;
      margin-top: 3px;
      text-align: center;
      font-size: 16px;
      font-weight: 400;
      color: #A3A3A3;
      position: relative;
      right: 0px;
      bottom: 0px;
      font-family: helvetica;
      -webkit-transition: all .3s ease;
      -moz-transition: all .3s ease;
      transition: all .3s ease;
    }

    .groupContainer:hover {
      background: transparent;
    }

    .slideinput:hover {
      opacity: 1.0;
      background: #AFAFAB;
      border: 1px solid #AFAFAB;
      outline: none;
      color: #FFF;
      cursor: pointer;
    }

    .noUi-horizontal {
      height: 3px;
      margin-top: 14px;
      margin-bottom: 5px;
      margin-left: 5px;
      width: 74%;
    }

    .sliderWrapper {
      padding: 5px;
    }

    .noUi-horizontal .noUi-handle {
      width: 34px;
      height: 34px;
      left: -19px;
      top: -15px;
      border-radius: 2em;
      border: 3px solid #FFFFFF;
      box-shadow: none;
      background-color: #FFFFFF;
      cursor: pointer;
      box-shadow: inset 0px 0px 0px 3px #B8B0AE;
    }

    .noUi-horizontal .noUi-handle.noUi-handle-lower.noUi-active {
      border: 3px solid #FFF;
      background-color: #F26968;
      box-shadow: inset 0px 0px 0px 3px #F26968;
    }

    .noUi-target {
      background-color: #F26968;
    }

    .noUi-origin {
      background: #B8B0AE;
    }

    .subHeading {
      margin-left: 5px;
    }

    .sliderchoice {
      margin-top: 33px;
    }

    #sliderChoice {
      margin-left: 5px;
    }

    .renderChannel {
      border-top: 2px solid transparent;
    }

    .small-print {
      font-family: "Raleway", 'Helvetica', sans-serif;
      font-size: 14px;
      font-style: italic;
      padding-top: 15px;
    }

    @media (max-width: 468px) {
      .sizechoice {
        position: fixed;
        bottom: 180px;
        left: 0px;
        width: 50%;
        margin-top: 20px;
        text-align: right;
      }

      .slideChoiceLabel {
        display: none;
      }

      .sizehelp {
        position: absolute;
        width: 107px;
        text-align: left;
        right: 3px;
        top: 0px;
        font-size: 100%;
      }

      #sizeChoice {
        display: inline-block;
        width: 100%;
        padding: 0px 0px 0px 10px;
        font-size: 14px;
        color: #666666;
        text-align: left;
      }

      #sizeChoice span {
        display: none;
      }

      #sizeChoices::before {
        position: absolute;
        color: #B3B3AF;
        right: 12px;
        top: 11px;
        font-size: 16px;
        pointer-events: none;
        -webkit-transition: all .3s ease;
        -moz-transition: all .3s ease;
        transition: all .3s ease;
      }

      #sizeChoices {
        padding: 0;
        position: fixed;
        bottom: 187px;
        margin: 10px 0px 0px 0px;
        left: 10px;
        border: 1px solid #FFF;
        width: 150px;
        border-radius: 4px;
        overflow: hidden;
        background-color: #FFF;
        background: #F0F0F0;
        text-align: right;
        opacity: 0.6;
        display: block;
        text-align: right;
      }

      #sizeChoices select {
        padding: 5px 10px 6px 10px;
        width: 100%;
      }

      #controlChoice {
        display: none;
        width: 100%;
        padding: 0px 0px 0px 10px;
        font-size: 14px;
        color: #666666;
        text-align: left;
      }

      #controlChoice span {
        display: none;
      }

      #controlChoices::before {
        position: absolute;
        color: #B3B3AF;
        right: 12px;
        top: 11px;
        font-size: 16px;
        pointer-events: none;
        -webkit-transition: all .3s ease;
        -moz-transition: all .3s ease;
        transition: all .3s ease;
      }

      #controlChoices {
        padding: 0;
        position: fixed;
        bottom: 190px;
        margin: 10px 0px 0px 0px;
        left: 10px;
        border: 1px solid #FFF;
        width: 105px;
        border-radius: 4px;
        overflow: hidden;
        background-color: #FFF;
        background: #F0F0F0;
        text-align: right;
        opacity: 0.6;
        display: block;
        text-align: right;
      }

      #controlChoices select {
        padding: 5px 10px 6px 10px;
        width: 100%;
      }

      .noUi-horizontal {
        height: 3px;
        margin-top: 13px;
        margin-bottom: 5px;
        margin-left: 20px;
        width: Calc(100% - 20px);
      }

      .noUi-horizontal .noUi-handle {
        width: 50px;
        height: 50px;
        left: -25px;
        top: -25px;
        border-radius: 2em;
        border: 3px solid #E1E4E9;
        box-shadow: none;
        background-color: #DD2435;
        cursor: pointer;
        box-shadow: inset 0px 0px 0px 3px #DD2435;
      }

      .noUi-horizontal .noUi-handle.noUi-handle-lower.noUi-active {
        border: 3px solid #E1E4E9;
        background-color: #DD2435;
        box-shadow: inset 0px 0px 0px 3px #DD2435;
      }

      .slideinput {
        display: none;
        float: right;
        width: 40px;
        height: 30px;
        border-radius: 4px;
        border: 1px solid #F6F6F6;
        background: #F6F6F6;
        padding: 5px 5px 5px 5px;
        margin-top: 0px;
        text-align: center;
        font-size: 12px;
        right: 8px;
      }

      #sizeChoices select {
        opacity: 1.0;
        background: #AFAFAB;
        border: 1px solid #AFAFAB;
        outline: none;
        color: #FFF;
        cursor: pointer;
      }

      #colorChoice {
        display: inline-block;
        width: 50%;
        float: right;
        padding: 0px 10px 0px 0px;
        font-size: 14px;
        color: #666666;
        position: fixed;
        right: 0px;
        bottom: 236px;
        text-align: right;
      }

      #colorChoice span {
        display: none;
      }

      #colorChoices {
        display: inline-block;
        width: 60%;
        text-align: right;
        padding: 8px 8px 0px 0px;
        position: fixed;
        right: 5px;
        bottom: 190px;
      }

      .renderChannel {
        border-top: 2px solid transparent;
      }

      #slideChoice {
        display: none;
      }

      #sizeChoices::before {
        color: #000;
      }

    }

    .ui-page.ui-page-active.ui-page-theme-a {
      padding: 0px;
      margin: 0px;
    }

    .groupContainer {
      padding-bottom: 0px;
    }

    .groupHeading {
      display: none;
    }

    .cards_product_wrapper.customizer {
      margin-top: 0px;
      padding-top: 60px;
    }

    .groupHeading {
      display: none;
    }

    .controlchoice {
      display: none;
    }

    @media screen and (max-width: 468px) {
      #controls {
        padding: 55px 25px 10px 10px;
        height: 116px;
        background: #E1E4E9;
      }

      #groupcustnav {
        display: none;
      }

      #samplecustnav {
        display: none;
      }

      .controlChoiceLabel {
        display: none;
      }

      .controlchoice {
        display: block;
      }

      #controlChoices {
        padding: 0;
        z-index: 999;
        position: fixed;
        bottom: 128px;
        margin: 10px 0px 0px 0px;
        left: 20px;
        border: 1px solid #999;
        width: Calc(100% - 40px);
        border-radius: 4px;
        overflow: hidden;
        background-color: #999;
        background: #FFFFFF;
        text-align: right;
        opacity: 1;
        display: block;
        text-align: right;
      }

      #controlChoices select:hover {
        opacity: 1.0;
        background: #CFCFCC;
        border: 1px solid #CFCFCC;
        outline: none;
        color: #FFF;
        cursor: pointer;
      }

      #controlChoices::before {
        position: absolute;
        color: #666666;
        right: 12px;
        top: 11px;
        font-size: 16px;
        pointer-events: none;
        -webkit-transition: all .3s ease;
        -moz-transition: all .3s ease;
        transition: all .3s ease;
      }

      #controls {
        padding: 58px 25px 10px 10px;
      }

      #addressSubmit {
        font-size: 14px;
      }

    }

    .onoffswitch {
      position: relative;
      width: 156px;
      -webkit-user-select: none;
      -moz-user-select: none;
      -ms-user-select: none;
    }

    .onoffswitch {
      position: absolute;
      width: 156px;
      -webkit-user-select: none;
      -moz-user-select: none;
      -ms-user-select: none;
      right: -29px;
      top: -78px;
    }

    .onoffswitch-checkbox {
      display: none;
    }

    .onoffswitch-label {
      display: block;
      overflow: hidden;
      cursor: pointer;
      border: 1px solid #DEDEDE;
      border-radius: 6px;
    }

    .onoffswitch-inner {
      display: block;
      width: 200%;
      margin-left: -100%;
      transition: margin 0.3s ease-in 0s;
    }

    .onoffswitch-inner:before, .onoffswitch-inner:after {
      display: block;
      float: left;
      width: 50%;
      height: 30px;
      padding: 0;
      line-height: 30px;
      font-size: 12px;
      color: white;
      font-family: Trebuchet, Arial, sans-serif;
      font-weight: bold;
      box-sizing: border-box;
    }

    .onoffswitch-inner:before {
      content: "CLOSE PREVIEW";
      padding-left: 10px;
      background-color: #FFFFFF;
      color: #8E8B8B;
    }

    .onoffswitch-inner:after {
      content: "WEAR PREVIEW";
      margin-left: 10px;
      margin-right: -60px;
      background-color: #FFFFFF;
      color: #8E8B8B;
      text-align: right;
    }

    .onoffswitch-switch {
      display: block;
      width: 17px;
      height: 17px;
      margin: 7.5px;
      background: #F26868;
      position: absolute;
      top: 0;
      bottom: 0;
      right: 122px;
      border: 1px solid #DEDEDE;
      border-radius: 2em;
      transition: all 0.3s ease-in 0s;
    }

    .onoffswitch-checkbox:checked + .onoffswitch-label .onoffswitch-inner {
      margin-left: 0;
    }

    .onoffswitch-checkbox:checked + .onoffswitch-label .onoffswitch-switch {
      right: 0px;
      background-color: #8E8B8B;
    }

    .ui-btn.ui-radio-on.ui-btn-a:after {
      border-color: #F26968;
    }

    .ui-page-theme-a .ui-radio-on:after, html .ui-bar-a .ui-radio-on:after, html .ui-body-a .ui-radio-on:after, html body .ui-group-theme-a .ui-radio-on:after, .ui-btn.ui-radio-on.ui-btn-a:after {
      border-color: #f26868;
    }

    select.form-control {
      height: 40px;
    }

    span.glyphicon.form-control-feedback.select {
      margin-top: -37px;
      margin-right: 15px;
    }

    .step.back:hover {
      cursor: pointer;
    }

    .dragtip span {
      text-align: left;
      margin: 5px auto 0;
      display: block;
      font-weight: 500;
      font-size: 12px;
      bottom: -22px;
      left: 200px;
      position: absolute;
    }

    @media (max-width: 768px) {

      .dragtip span {
        text-align: center;
        margin: 5px auto 0;
        display: block;
        font-weight: 500;
        font-size: 12px;
        position: relative;
        bottom: 0;
        left: 0;
      }

      .pricechoice {
        position: fixed;
        top: 50px;
        left: 0px;
        z-index: 999;
      }
      #priceChoices {
        display: none;
      }

    }

   /* .channelHeader.drop-label {
      display: none;
    }*/

    /*.renderChannel {
      padding-bottom: 0px;
      padding-top: 0px;
    }*/

    #sliderChoice {
      margin-bottom: 10px;
    }

   /* .groupHeading {
      display: none;
    }*/

    .small-print {
      display: none;
    }

    @media screen and (max-width: 468px) {
      .actionButtonNext {
        position: fixed;
        bottom: 0px;
        left: auto;
        right: 0px;
        width: 80%;
        padding: 16px 20px;
        background: #DD2435;
        border: 6px solid #FFFFFF;
        color: #FFF;
        letter-spacing: 0.15em;
        font-size: 14px;
        -webkit-font-smoothing: antialiased;
        font-weight: 600;
        cursor: pointer;
        -webkit-transition: .3s all;
        transition: .3s all;
      }

      .pricechoice {
        position: fixed;
        bottom: -10px;
        left: 0px;
        z-index: 999;
        background: #fff;
        padding: 10px 20px;
        width: 22%;
        margin: 0;
        top: auto;
      }

      #priceChoice {
        padding: 2px 0px 5px 0px;
        margin-left: -5px;
      }
    }

    .product-cta-nav-mobile {
        display: none;
    }

    .ui-mobile label, .ui-controlgroup-label {
      background: #FFF;
    }

  </style>
   
</head>
<body class="custom">
<div data-role="page" class="ui-page ui-page-active">

  <div class="ftui-nav">
    <div class="steps">
      <div onclick="window.location.href='/onboard/choose'" class="step back"><span><img class="gemIcon"
                                                                                         src="https://storage.googleapis.com/troveup-imagestore/assets/sectioned-gem-white.svg"></span><br>Step
        1: Choose
      </div>
      <div class="step active"><span><img class="gemIcon"
                                          src="https://storage.googleapis.com/troveup-imagestore/assets/sectioned-gem-red.svg"></span><br>Step
        2: Customize
      </div>
      <div class="step"><span><img class="gemIcon"
                                   src="https://storage.googleapis.com/troveup-imagestore/assets/sectioned-gem-white.svg"></span><br>Step
        3: Free Jewelry
      </div>
    </div>
  </div>

  <!-- <button class="actionButtonBack">Go Back</button> -->
  <div class="currentStep">Step 2 of 3</div>
  <c:import url="../fragments/modals/addressModal.jsp"/>

  <input class="ignore" id="csrfToken" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
  <c:import url="../fragments/cust/mobile/modalsAndControls.jsp"/>
  <div class="container cards_product_wrapper customizer">
    <!-- <div class="row"> -->

    <div class="col-sm-7 col-sm-pull-1 col-md-6 col-md-push-1">
      <div class="">

        <div id="bump" class="overlaid">
          <div class="message">
            <h2>Saved to <br></br>Your Trove</h2>
            <img class="add_image" src="/resources/img/plus.png">
          </div>
        </div>

        <div id="bump2" class="overlaid">
          <div class="message">
            <h2>Added to <br></br>Your Bag</h2>
            <img class="add_image" src="/resources/img/plus.png">
          </div>
        </div>

        <!-- <div class="product-title-bar hidemob">
                    <button data-role="none" type="button" class="btn-edit" id="controller"
                            style="display: none; float: right; margin-top: -15px; margin-right: 8px;"><span
                            class="glyphicon glyphicon-edit" aria-hidden="true"></span></button>
                    <a href="#" id="model" data-value="${item.itemName}">
                        <div id="view" style="white-space:nowrap;text-overflow:ellipsis;overflow:hidden;"></div>
                    </a>
                </div> -->

        <div class="product-description-image">
          <div class="dragtip"><span>Click and Drag Item to Rotate</span>
          </div>
          <div class="product-img-container">
            <!-- Customizer Canvas -->
            <c:import url="../fragments/cust/canvas.jsp"/>
          </div>
        </div>

        <div class="item-options hidemob">
          <!-- Customizer Options -->
          <c:import url="../fragments/cust/options.jsp"/>
        </div>


        <div class="product-cta-nav hidemob">
          <ul class="product-cta-btns">
            <%-- ANONYMOUS USER --%>
            <c:if test="${not isAuthenticated}">
              <li>
                <button data-role="none" id="button_addToTrove" onclick="triggerAuthRequest()" type="button"
                        class="btn btn--red  btn--product--description"><img
                    src="https://storage.googleapis.com/trove-demo-cdn/img/trove-icon-white.svg" class="icon-btn"
                    alt="Save to Trove"><span style="color: #fff"> Save</span></button>
              </li>
              <li data-tooltip="You're already customizing!"><a data-role="none" href="/public/customize/webgl/1"
                                                                class="btn btn--red btn--product--description disabled"
                                                                role="button"><img
                  src="https://storage.googleapis.com/trove-demo-cdn/img/remix-icon-white.svg" class="icon-btn"
                  alt="Remakes"> Customize</a></li>
              <li>
                <button data-role="none" id="button_addToBag" onclick="triggerAuthRequest()"
                        class="btn btn--darkgray  btn--product--description"><img
                    src="https://storage.googleapis.com/troveup-imagestore/assets/img/shopping-bag-lightweight-2-white.svg"
                    class="icon-btn" alt="Add to Shopping Bag"><span style="color: #fff"> Buy</span></button>
              </li>
            </c:if>
            <%-- END --%>
            <%-- AUTH USER --%>
            <c:if test="${isAuthenticated}">
              <li>
                <button data-role="none" id="button_addToTrove" onclick="FORGE.Page.updateTrove()" type="button"
                        class="btn btn--red  btn--product--description"><img
                    src="https://storage.googleapis.com/trove-demo-cdn/img/trove-icon-white.svg" class="icon-btn"
                    alt="Save to Trove"><span style="color: #fff"> Save</span></button>
              </li>
              <li data-tooltip="You're already customizing!"><a data-role="none" href="/public/customize/webgl/1"
                                                                class="btn btn--red btn--product--description disabled"
                                                                role="button"><img
                  src="https://storage.googleapis.com/trove-demo-cdn/img/remix-icon-white.svg" class="icon-btn"
                  alt="Remakes"> Customize</a></li>
              <li>
                <button data-role="none" id="button_addToBag" onclick="FORGE.Page.updateCart()"
                        class="btn btn--darkgray  btn--product--description"><img
                    src="https://storage.googleapis.com/troveup-imagestore/assets/img/shopping-bag-lightweight-2-white.svg"
                    class="icon-btn" alt="Add to Shopping Bag"><span style="color: #fff"> Buy</span></button>
              </li>
            </c:if>
            <%-- END --%>
          </ul>
        </div>

        <div class="product-cta-nav product-cta-nav-mobile">
          <ul class="product-cta-btns">
            <%-- ANONYMOUS USER --%>
            <c:if test="${not isAuthenticated}">
              <li>
                <button data-role="none" id="button_addToTroveMob" onclick="triggerAuthRequest()" type="button"
                        class="btn btn--red  btn--product--description btnspec left"><img
                    src="https://storage.googleapis.com/trove-demo-cdn/img/trove-icon-white.svg" class="icon-btn"
                    alt="Save to Trove"><span style="color: #fff;font-size: 14px;"> Save</span></button>
              </li>
              <!-- <li><button id="button_addToBagMob" onclick="triggerAuthRequest()" class="btn btn--darkgray btn--product--description btnspec right"><img src="https://storage.googleapis.com/troveup-imagestore/assets/img/shopping-bag-lightweight-2-white.svg" class="icon-btn" alt="Add to Shopping Bag"><span style="color: #fff; font-size: 14px;"> Buy</span></button></li> -->
              <li>
                <button data-role="none" id="button_addToBagMob" onclick="triggerAuthRequest()"
                        class="btn btn--darkgray  btn--product--description btnspec right">
                  <!-- <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/shopping-bag-lightweight-2-white.svg" class="icon-btn" alt="Add to Shopping Bag"> --><span
                    style="color: #fff; font-size: 14px;"> Buy: </span><span class="item-price-container"><span
                    class="item-price">-</span></span></button>
              </li>
            </c:if>
            <%-- END --%>
            <%-- AUTH USER --%>
            <c:if test="${isAuthenticated}">
              <li>
                <button data-role="none" id="button_addToTroveMob" onclick="FORGE.Page.updateTrove()" type="button"
                        class="btn btn--red  btn--product--description btnspec left"><img
                    src="https://storage.googleapis.com/trove-demo-cdn/img/trove-icon-white.svg" class="icon-btn"
                    alt="Save to Trove"><span style="color: #fff;font-size: 14px;"> Save</span></button>
              </li>
              <!-- <li><button id="button_addToBagMob" onclick="FORGE.Page.updateCart()" class="btn btn--darkgray btn--product--description btnspec right"><img src="https://storage.googleapis.com/troveup-imagestore/assets/img/shopping-bag-lightweight-2-white.svg" class="icon-btn" alt="Add to Shopping Bag"><span style="color: #fff;font-size: 14px;"> Buy</span></button></li> -->
              <li>
                <button data-role="none" id="button_addToBagMob" onclick="FORGE.Page.updateCart()"
                        class="btn btn--darkgray  btn--product--description btnspec right">
                  <!-- <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/shopping-bag-lightweight-2-white.svg" class="icon-btn" alt="Add to Shopping Bag"> --><span
                    style="color: #fff; font-size: 14px;"> Buy: </span><span class="item-price-container"><span
                    class="item-price">-</span></span></button>
              </li>
            </c:if>
            <%-- END --%>
          </ul>
        </div>


      </div>
    </div>


    <div class="col-sm-5 col-sm-pull-1 col-md-4 col-md-push-1">
      <div class="sliderchoice">
        <div class="user_card_content_a">
          <!-- <div class="card_remake_title hidemob custom"> Customizer</div> -->
          <div id="sliderChoice" class="slideChoiceLabel">CUSTOMIZATIONS<br></div>
          <!-- Customizer Controls -->
          <c:import url="../fragments/cust/controls.jsp"/>
        </div>
      </div>

      <c:if test="${item.sizeDropdownAvailable}">
      <div class="sizechoice">
        <div id="sizeChoice" class="sizeChoiceLabel">SIZE:<br><span>0.0</span></div>
        <div id="sizeChoices" class="ion-chevron-down">
          <select class="ignore" name="sizer" id="sizer">
            <c:if test="${not empty size}">
              <c:forEach var="individualsize" items="${size}">
                <option value="${individualsize['key']}">${individualsize['value']}</option>
              </c:forEach>
            </c:if>
          </select>
        </div>
        <a class="sizehelp" href="https://storage.googleapis.com/troveup-imagestore/assets/ring-sizer-final.pdf"
           target="_blank" alt="Jewelry Sizing Chart" style="color:#f26868" class="ui-link">Size Help</a>
      </div>
      </c:if>

      <div class="colorchoice">
        <div id="colorChoice" class="colorChoiceLabel">COLOR:<br><span>Rose Gold</span></div>
        <div id="colorChoices">
          <button id="colorGold" class="btn colorBtn ion-ios-checkmark-empty"></button>
          <button id="colorSilver" class="btn colorBtn ion-ios-checkmark-empty"></button>
          <button id="colorRose" class="btn colorBtn ion-ios-checkmark-empty"></button>
        </div>
      </div>
      <div class="pricechoice">
        <div id="priceChoice" class="priceChoiceLabel">PRICE:<br><span>FREE</span></div>
        <div id="priceChoices">
          <span>White 3D-printed prototype</span>
        </div>
      </div>

      <div class="controlchoice">
        <div id="controlChoice" class="controlChoiceLabel">CONTROL<br><span>None</span></div>
        <div id="controlChoices" class="ion-chevron-down">
          <select class="ignore" name="choosy" id="choosy"></select>
        </div>
      </div>


      <button class="actionButtonNext">Send My Jewelry</button>
      <div class="small-print">Your sample will be 3D-printed in white nylon</div>
    </div>

  </div>
</div>

<script>

  $(".colorBtn").click(function () {
    $('.picked').removeClass('picked');
    $(this).addClass('picked');
    var color = $(this).attr('id');
    if (color == "colorGold") {
      $('#webmenu').val("3 110").trigger('change');
      $('#colorChoice span').html('Gold');
    }
    if (color == "colorSilver") {
      $('#webmenu').val("6 6").trigger('change');
      $('#colorChoice span').html('Silver');
    }
    if (color == "colorRose") {
      $('#webmenu').val("3 111").trigger('change');
      $('#colorChoice span').html('Rose Gold');
    }
  });

  $("#webmenu").click(function () {
    mixpanel.track("customizer_material", {
      "itemName": "${item.itemName}"
    });
  });

  $("#size").click(function () {
    mixpanel.track("customizer_size", {
      "itemName": "${item.itemName}",
      "size": this.value
    });
  });

  $("#example1").click(function () {
    mixpanel.track("customizer_livephoto");
  });

</script>


<c:if test="${not isAuthenticated}">
  <script>
    $("#button_addToTrove").click(function () {
      mixpanel.track("customizer_trove_ANON", {
        "itemName": "${item.itemName}"
      });
    });
    $("#button_addToBag").click(function () {
      mixpanel.track("customizer_buy_ANON", {
        "itemName": "${item.itemName}"
      });
    });
    $("#button_addToTroveMob").click(function () {
      mixpanel.track("customizer_trove_ANON", {
        "itemName": "${item.itemName}"
      });
    });
    $("#button_addToBagMob").click(function () {
      mixpanel.track("customizer_buy_ANON", {
        "itemName": "${item.itemName}"
      });
    });
  </script>
</c:if>
<c:if test="${isAuthenticated}">
  <script>
    $("#button_addToTrove").click(function () {
      mixpanel.track("customizer_trove", {
        "itemName": "${item.itemName}"
      });
    });
    $("#button_addToBag").click(function () {
      mixpanel.track("customizer_buy", {
        "itemName": "${item.itemName}"
      });
    });
    $("#button_addToTroveMob").click(function () {
      mixpanel.track("customizer_trove", {
        "itemName": "${item.itemName}"
      });
    });
    $("#button_addToBagMob").click(function () {
      mixpanel.track("customizer_buy", {
        "itemName": "${item.itemName}"
      });
    });
  </script>

</c:if>
<script>

  function moreMobileStuff() {
    var x = document.getElementById("choosy");
    $(".sliderWrapper").each(function () {
      var option = document.createElement("option");
      option.value = $(this).attr('id');
      option.text = $(this).children('.subHeading').html();
      x.add(option, x[0]);
    })
  }
  ;

  function materialStuff() {
    var startColor = $('#webmenu').val();
    switch (startColor) {
      case "6 6":
        $('#colorChoice span').html("Silver");
        $('#colorSilver').addClass('picked');
        break;
      case "5 87":
        $('#colorChoice span').html("Gold");
        $('#colorGold').addClass('picked');
        break;
      case "3 113":
        $('#colorChoice span').html("Silver");
        $('#colorSilver').addClass('picked');
        break;
      case "3 111":
        $('#colorChoice span').html("Rose Gold");
        $('#colorRose').addClass('picked');
        break;
      case "3 110":
        $('#colorChoice span').html("Gold");
        $('#colorGold').addClass('picked');
        break;
      case "3 112":
        $('#colorChoice span').html("Gold");
        $('#colorGold').addClass('picked');
        break;
      case "2 81":
        $('#colorChoice span').html("Silver");
        $('#colorSilver').addClass('picked');
        break;
      case "1 97":
        $('#colorChoice span').html("Silver");
        $('#colorSilver').addClass('picked');
        break;
      case "1 96":
        $('#colorChoice span').html("Rose Gold");
        $('#colorRose').addClass('picked');
        break;
      case "1 91":
        $('#colorChoice span').html("Gold");
        $('#colorGold').addClass('picked');
        break;
      case "1 98":
        $('#colorChoice span').html("Gold");
        $('#colorGold').addClass('picked');
        break;
    }
  }
  ;



  function setUpCanvasEvents() {
    // click and drag to rotate
    // $('#canvas').mousedown(function(e) {
    //   var firstClick = new Date().getTime();
    //   clearTimeout(this.downTimer);
    //   this.downTimer = setTimeout(function() { 
          
    //     }, 100);
    //   }).mouseup(function(e) {
    //   clearTimeout(this.downTimer);
    // });

    var firstClick = new Date().getTime();
    $('#canvas').mousedown(function(e) {
      firstClick = new Date().getTime();
    });

    $('#canvas').mouseup(function(e) {
      var now, duration;
      now = new Date().getTime();
      duration = now - firstClick;
      mixpanel.track("onboard_customize_rotate", {
        "duration": duration
      });
    });


    // engage slider
    $('.sliderWrapper').click(function (e) {
      mixpanel.track("onboard_customize_engage_slider");
    });


  };



  $('#engravehint').popover({trigger: "hover click"});

  function setUpMobileUI() {
    materialStuff();
    moreMobileStuff();
    setUpCanvasEvents();
    $('#controls').trigger('create');

    var first = true;
    var firstt = true;
    $(".groupContainer").each(function () {
      var gro = $(this).attr('id');
      var groptitle = gro.replace(" ", "_").replace(" ", "_").replace(" ", "_").replace(".", "_").replace(".", "_");
      $(this).attr('id', groptitle);
      var grouptitle = groptitle.substring(10, groptitle.length);
      var group = grouptitle.substring(2, grouptitle.length);
      group = group.replace("_", " ").replace("_", " ").replace("_", " ").replace(".", " ").replace(".", " ");
      if (firstt) {
        $("ul.cust-group-options").append('<li><a class="fakeup activee" href="#" id="' + grouptitle + '" onclick="fakeitt(\'' + String(grouptitle) + '\');">' + group + '</a></li>');
      } else {
        $("ul.cust-group-options").append('<li><a class="fakeup" href="#" id="' + grouptitle + '" onclick="fakeitt(\'' + String(grouptitle) + '\');">' + group + '</a></li>');
      }
      $(this).children(".sliderWrapper").each(function () {
        var temptit = $(this).attr('id');
        var temptitle = temptit.replace(" ", "_").replace(" ", "_").replace(" ", "_").replace(".", "_").replace(".", "_");
        $(this).attr('id', temptitle);
        var nametit = $(this).children('.subHeading').html();
        var title = temptitle.substring(12, temptitle.length);
        var tit = title.substring(2, title.length);
        tit = tit.replace("_", " ").replace("_", " ").replace("_", " ").replace(".", " ").replace(".", " ");
        if (firstt) {
          if (first) {
            $(this).addClass('activated');
            $(".navbar-brand.custom").html(nametit);
            $("ul.cust-options").append('<li class="slideopt ' + grouptitle + ' showme"><a class="fakedown activ" href="#" id="' + title + '" onclick="fakeit(\'' + String(title) + '\');">' + nametit + '</a></li>');
            first = false;
          } else {
            $("ul.cust-options").append('<li class="slideopt ' + grouptitle + ' showme"><a class="fakedown" href="#" id="' + title + '" onclick="fakeit(\'' + String(title) + '\');">' + nametit + '</a></li>');
          }
        } else {
          $("ul.cust-options").append('<li class="slideopt ' + grouptitle + '"><a class="fakedown" href="#" id="' + title + '" onclick="fakeit(\'' + String(title) + '\');">' + nametit + '</a></li>');
        }

      });
      firstt = false;

      // handle custom ring size modal case
      var sizenum = $('.sizefor').size();
      if (sizenum <= 10) {
        $('.sizefor').removeClass('sizeformodal');
      }

      $('.sizefor').each(function () {
        var temptit = $(this).attr('id');
        var temptitle = temptit.replace(" ", "_").replace(" ", "_").replace(" ", "_").replace(".", "_").replace(".", "_");
        $(this).attr('id', temptitle);
      });

    });

    var defmat = $('#webmenu').val();
    var defsiz = $('#size').val();
    // matit(defmat);
    // sizeit(defsiz);
    // $(".navbar-toggle.size").click();
    // $(".navbar-toggle.material").click();
    defmat = '#' + defmat.replace(" ", "_");

    if (defsiz) {
      defsiz = '#' + defsiz.replace(" ", "_").replace(" ", "_").replace(" ", "_").replace(".", "_").replace(".", "_");
      $(defsiz).children('a').addClass('activeeee');
    }
    $(defmat).addClass('activeee');


    // $(".sliderWrapper").each(function(){
    //   var temptit = $(this).attr('id');
    //   var temptitle = temptit.replace(" ", "_").replace(" ", "_").replace(" ", "_").replace(".", "_").replace(".", "_");
    //   $(this).attr('id', temptitle);
    //   var title = temptitle.substring(12,temptitle.length);
    //   var tit = title.substring(2,title.length);
    //   tit = tit.replace("_", " ").replace("_", " ").replace("_", " ").replace(".", " ").replace(".", " ");
    //   if (first) {
    //     $(this).addClass('activated');
    //     $(".navbar-brand.custom").html(tit);
    //     $("ul.cust-options").append('<li><a class="fakedown active" href="#" id="' + title +'" onclick="fakeit(\'' + String(title) + '\');">' + tit + '</a></li>');
    //     first = false;
    //   } else {
    //     $("ul.cust-options").append('<li><a class="fakedown" href="#" id="' + title + '" onclick="fakeit(\'' + String(title) + '\');">' + tit + '</a></li>');
    //   }
    // });
  }
  ;

  $('.navbar .navbar-collapse').on('show.bs.collapse', function (e) {
    $('.navbar .navbar-collapse').not(this).collapse('hide');
  });

  function sizeit(ide, val) {
    $('#sRingsize').val(String(ide)).trigger('change');
    $('#size').val(String(ide)).trigger('change');
    $(".navbar-brand.size").html(String(val));
    var sizid = String(ide);

    sizid = '#' + sizid.replace(" ", "_").replace(" ", "_").replace(" ", "_").replace(".", "_").replace(".", "_");
    $('.activeeee').removeClass('activeeee');
    $(sizid).children('a').addClass('activeeee');
    $(".navbar-toggle.size").click();
    mixpanel.track("customizer_size", {
      "itemName": "${item.itemName}",
      "size": String(ide)
    });
  }
  ;

  function matit(ide) {
    $('#webmenu').val(String(ide)).trigger('change');
    var t = '#' + String(ide);
    t = t.replace(" ", "_");
    var text = $(t).html();
    text = text.split(" - ")
    if (text[0] == "Gold" || text[0] == "Precious Plated") {
      $(".navbar-brand.material").html(text[0]);
    } else {
      $(".navbar-brand.material").html(text[0]);
    }
    $('.activeee').removeClass('activeee');
    $(t).addClass('activeee');
    $(".navbar-toggle.material").click();
    var matswitch = text[1] + " " + text[0]
    mixpanel.track("customizer_material", {
      "itemName": "${item.itemName}",
      "material": matswitch

    });
  }
  ;

  function fakeit(ide) {
    var ti = '#' + String(ide);
    var newid = '#sliderWrappe' + String(ide);
    var t = ide.substring(2, ide.length).split("_");
    $('.activ').removeClass('activ');
    $('.activated').removeClass('activated');
    $(newid).addClass('activated');
    $(ti).addClass('activ');
    $(".navbar-brand.custom").html(t[0] + " " + t[1]);
    $(".navbar-toggle.custom.ization").click();
  }
  ;

  function fakeitt(ide) {
    var ti = '.slideopt.' + String(ide);
    var tid = '#' + String(ide);
    $('.showme').removeClass('showme');
    $('.activee').removeClass('activee');
    $(ti).addClass('showme');
    $(tid).addClass('activee');
    $('.activ').removeClass('activ');
    $('.activated').removeClass('activated');
    $(".navbar-brand.custom").html("Choose One");
    $(".navbar-toggle.custom.spec").click();
    $(".navbar-toggle.custom.ization").click();
  }
  ;

  function land() {
    mixpanel.track("land_onboard_customize");
  }
  ;



  $(document).ready(function () {

    land();

    $('#sizer').val('ring_7.0');

    $("#sizeChoices select").bind('change', function () {
      var newval = $(this).find('option:selected').text();
      $('#sizeChoice span').html(newval);
    }).change();


    // $("#sizeChoices select").bind('click', function() {
    //   alert("Clicked" + $(this).find('option:selected').text());
    // });

    // $("#sizeChoices select").bind('focusout', function() {
    //   alert("FocusedOut" + $(this).find('option:selected').text());
    // });

    $("#controlChoices select").bind('change', function () {
      var newwval = "#" + $(this).val();
      $('.activated').removeClass('activated');
      $(newwval).addClass('activated');
      $('#controlChoice span').html($(this).find('option:selected').text());
    }).change();


    $(function () {
      $('[data-toggle="popover"]').popover()
    });

    var count = parseInt($('.tallyho-cart').html());
    if (count >= 1) {
      $('.tallyho-cart').addClass('showitnow');
    }

    $.fn.editable.defaults.mode = 'inline';
    var firstthingsfirst = true;
    $('#model').editable({
      send: 'never',
      title: 'Enter username',
      placement: 'right',
      toggle: 'manual',
      validate: function (value) {
        var regex = new RegExp(/^[a-zA-Z0-9 ]+$/);
        if (!(value.length < 31))
          return "Please shorten your item\'s name to 30 characters or less.";
        if (!(regex.test(value)))
          return "There are illegal characters in your item\'s name.";
        else
          return null;
      },
      display: function (value) {
        $('#view').text(value);
        if (firstthingsfirst) {
          firstthingsfirst = false;
        } else {
          mixpanel.track("customizer_editname", {
            "itemName": "${item.itemName}",
            "newName": value
          });
        }
        // TROVE.renameItem( '${item.itemId}', value );
      }
    });

    $('#controller').click(function (e) {
      e.stopPropagation();
      $('#model').editable('toggle');
    });

    var firstthingsfirstt = true;
    $('#model2').editable({
      send: 'never',
      title: 'Enter username',
      placement: 'right',
      toggle: 'manual',
      display: function (value) {
        $('#view2').text(value);
        // TROVE.updateItemDescription( '${item.itemId}', value );
        if (firstthingsfirstt) {
          firstthingsfirstt = false;
        } else {
          mixpanel.track("customizer_editabout", {
            "itemName": "${item.itemName}",
            "newDescrip": value
          });
        }
      }
    });

    $('#controller2').click(function (e) {
      e.stopPropagation();
      $('#model2').editable('toggle');
    });

    $('.actionButtonNext').click(function (e) {
      triggerAddressRequest();
      mixpanel.track("onboard_customize_click_shipping_modal");
    });

    $('#sizebuton').click(function (e) {
      triggerSizeRequest();
    });


    // $("#button_addToBag").on('click', FORGE.Page.updateCart);
    // $("#button_addToTrove").on('click', FORGE.Page.updateTrove);

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
    $('.pro').on('mouseover', function () {
      $('.dropdown-container.pro').addClass('showtime');
    });
    $('.pro').on('mouseleave', function () {
      $('.dropdown-container.pro').removeClass('showtime');
    });
  });

  $('.fakedown').click(function () {
    return false;
  });

  $('#example1').fancybox({
    openEffect: 'elastic',
    closeEffect: 'elastic',
    helpers: {
      title: {
        type: 'inside'
      }
    }
  });

  $("#walkthroughclose").click(function () {
    $('#walkthroughbanner').slideUp("fast");
  });

  $(".actionButtonBack").click(function () {
    window.location.href = "/steptwo";
  });

  // $("#walkthroughopen").click(function () {
  //   $('#walkthroughbanner').slideDown("fast");
  // });


  TROVE.setToken("${_csrf.token}");
  FORGE.Page.setCSRFToken("${_csrf.token}");

  FORGE.Page.init({
    selectors: {
      materialSelect: "#webmenu",
      modelParentID: ${item.itemId},
      initialParamsBlock: "#initialParameters",
      canvas: $("#canvas")
    },
    admin: true,
    defaultMaterial: "${item.materialId} ${item.finishId}",
    storageRoot: $("#modelPath").html(),
    activeFilename: $("#modelFilename").html(),
    debug: true,
    isFTUE: true
  }, setUpMobileUI);

</script>
<script id="initialParameters" type="application/json">
  ${customizerInput}
</script>
</body>
</html>

