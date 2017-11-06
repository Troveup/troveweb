<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <c:import url="../fragments/headers/commonHead.jsp"/>
  <title>${pageTitle}</title>
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=0">
  <script type="text/javascript" src="https://code.jquery.com/jquery-1.11.3.min.js"></script>
  <link href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
  <link rel="icon" href="/resources/img/favicon.png?v=2">
  <link href='https://fonts.googleapis.com/css?family=Raleway:400,200,300,500,600,700' rel='stylesheet' type='text/css'>
  <link href="https://fonts.googleapis.com/css?family=Vollkorn:400,400italic,700,700italic" rel="stylesheet"
        type="text/css">
  <c:import url="../fragments/analytics/all.jsp"/>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>
  <script src="https://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="/resources/stylesheets/authmod.css">
  <link rel="stylesheet" href="/resources/stylesheets/spinner.css">
  <style>

    .error {
      color: red;
    }

    .success {
      color: green;
    }

    .input-text-counter {
      text-align: right;
    }

    .hover-pointer {
      cursor: pointer;
    }

    span.limit-long-text {
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
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

    .item {
      padding: 0px;
      display: block;
      -webkit-transition: all 0.3s ease-in-out;
      -moz-transition: all 0.3s ease-in-out;
      -o-transition: all 0.3s ease-in-out;
      -ms-transition: all 0.3s ease-in-out;
      transition: all 0.3s ease-in-out;
    }

    .item .item-image-container {
      border: 1px solid transparent;
    }

    .item:hover .item-image-container {
      border: 1px solid #DEDEDE;
    }

    .btn.focus, .btn:focus, .btn:hover {
      color: #FFF;
      text-decoration: none;
    }

    .item-container {
      width: Calc(100% - 500px);
      position: fixed;
      left: 150px;
      overflow: scroll;
      -webkit-overflow-scrolling: touch;
      bottom: -15px;
      top: 132px;
      padding-bottom: 80px;
    }

    @media (max-width: 767px) {
      .item-container {
        width: 100%;
      }

      .controls-container {
        display: none;
      }

      .side-contain .content {
        bottom: 80px;
      }

    }

    html {
      height: 100%;
    }

    body {
      background: #FFF;
      max-height: 100%;
    }

    *, *:after, *:before {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
      font-family: "Raleway", Helvetica, Arial, sans-serif;
    }

    input {
      -webkit-box-shadow: none !important;
      box-shadow: none !important;
    }

    .launch-progress-steps {
      margin: 0px auto;
      text-align: center;
      padding-bottom: 13px;
      background: #F6F6F6;
      padding-top: 9px;
      border-top: 1px solid #DEDEDE;
      border-bottom: 1px solid #DEDEDE;
    }

    .launch-progress-steps .step {
      display: inline-block;
      background: transparent;
      width: 30%;
      max-width: 180px;
      height: 26px;
      border-radius: 26px;
      vertical-align: top;
    }

    .launch-progress-steps .step.left {
      padding-left: 25px;
    }

    .launch-progress-steps .step.middle {
      padding-left: 30px;
    }

    .launch-progress-steps .step .label {
      display: inline-block;
      width: 30px;
      height: 30px;
      line-height: 27px;
      border-radius: 30px;
      padding: 0px;
      margin-right: 10px;
      padding-top: 1px;
      color: #8C8C8C;
      font-size: 16px;
      font-weight: 400;
      font-family: helvetica;
      float: left;
      border: 2px solid #8C8C8C;
    }

    .launch-progress-steps .step .title {
      color: #ADABAB;
      font-size: 13px;
      line-height: 20px;
      display: block;
      margin-top: 6px;
      text-align: left;
      letter-spacing: .1em;
      font-weight: 400;
      width: 180px;
    }

    .launch-progress-steps .step .title.desktop {
      display: block;
    }

    .launch-progress-steps .step .title.mobile {
      display: none;
    }

    .launch-progress-steps .step.done {
      background: transparent;
    }

    .launch-progress-steps .step.done .label {
      color: #FFF;
      background: #EE2435;
      border-color: #EE2435;
    }

    .launch-progress-steps .step.done .label .icon:before {
      padding-top: 4px;
    }

    .launch-progress-steps .step.done .title {
      color: #ADABAB;
    }

    .launch-progress-steps .step.active .label {
      color: #EE2435;
      border: 2px solid #EE2435;
      box-shadow: none;
      background: transparent;
    }

    .launch-progress-steps .step.active .title {
      color: #EE2435;
    }

    .nav-center {
      text-align: center;
    }

    .nav-logo {
      width: 120px;
      margin: 20px 40px 25px;
    }

    .btn {
      color: #FFF;
      background: #ee2435;
      border: 0px solid transparent;
      padding: 10px;
      border-radius: 2px;
      outline: 0 !important;
      cursor: pointer;
      transition: all 0.2s cubic-bezier(0.45, 1.01, 1, 1);
      margin: 10px auto 0px;
      font-size: 16px;
      text-transform: uppercase;
      font-weight: 300;
      letter-spacing: 0.1em;
      display: block;
      width: 200px;
      position: absolute;
      bottom: 40px;
      left: 50%;
      margin-left: -100px;
    }

    .btn:hover {
      background: #A51925;
      color: #FFF;
    }

    #back {
      background: #CCC;
      position: fixed;
      bottom: 40px;
      left: 150px;
      width: auto;
      z-index: 999;
      display: none;
    }

    #back:hover {
      background: #444;
      color: #FFF;
    }

    .main-contain, .side-contain {
      content: "";
    }

    .main-contain {
      width: Calc(100% - 350px);
      height: 100%;
      display: inline-block;
      position: fixed;
      left: 0px;
      top: 131px;
      overflow: scroll;
    }

    .side-contain {
      right: 0px;
      width: 350px;
      top: 132px;
      background: #F6F6F6;
      bottom: 0px;
      position: fixed;
      border-left: 1px solid #DEDEDE;
      text-align: center;
    }

    .side-contain .content {
      right: -1px;
      width: 100%;
      top: 0px;
      background: transparent;
      bottom: 115px;
      position: absolute;
      border-bottom: 2px solid #DEDEDE;
      text-align: center;
      padding: 28px 20px 20px;
      overflow: scroll;
    }

    h3 {
      text-align: center;
      padding-top: 20px;
      padding-bottom: 8px;
      font-size: 18px;

    }

    #filter {
      width: 144px;
      padding: 0px;
      position: fixed;
      background: #FFFFFF;
      border: 1px solid #DEDEDE;
      z-index: 999;
      left: 90px;
      top: 235px
    }

    .form-inline .checkbox {
      display: inline-block;
      margin-top: 0;
      margin-bottom: 0;
      vertical-align: middle;
      padding: 1px 5px;
      font-size: 15px;
      letter-spacing: 0.05em;
    }

    .form-inline .checkbox input[type=checkbox] {
      margin-right: 5px;
    }

    #filter-label {

    }

    .item-container {
      display: none;
    }

    .item-container.active {
      display: block;
    }

    .side-contain {
      display: none;
    }

    .side-contain.active {
      display: block;
    }

    .thumbnail {
      display: inline-block;
      padding: 20px;
      margin: 40px 40px 20px;
      line-height: 1.42857143;
      background-color: #fff;
      border: 1px solid #FFFFFF;
      border-radius: 0px;
      cursor: pointer;
    }

    .thumbnail img {
      height: 200px;
      width: 200px;
      display: inline-block;
      float: left;
    }

    .thumbnail .caption {
      padding: 20px;
      padding-left: 30px;
      color: #333;
      display: inline-block;
      float: left;
      width: 40%;
      height: 170px;
      text-align: left;
    }

    .thumbnail:hover, .thumbnail.selected {
      background-color: #F6F6F6;
      border: 1px solid #DEDEDE;
    }

    /* additional auth form stuff */
    .auth-form p {
      font-size: 20px;
      margin-bottom: 30px;
    }

    .auth-content .auth-content-inner .auth-form .stormy {
      border: 0px solid transparent;
      padding: 20px 2.5em;
    }

    nav > li > a:focus, .nav > li > a:hover {
      text-decoration: none;
      background-color: transparent;
    }

    .nav-tabs-justified > li > a {
      border-radius: 0px;
      border: 2px solid transparent;
      border-bottom: 2px solid #DEDEDE;
    }

    .nav-tabs-justified > .active > a,
    .nav-tabs-justified > .active > a:focus,
    .nav-tabs-justified > .active > a:hover {
      border-color: #DEDEDE;
      border-bottom: 2px solid #FFF;
      background: transparent;
      border-top: 2px solid #DEDEDE;
      border-right: 2px solid #DEDEDE;
      border-left: 2px solid #DEDEDE;
    }

    #sutab {
      border-left: 0px !important;
      border-right: 1px solid #DEDEDE !important;
      color: #9E9E9E;
      background: transparent !important;
    }

    #litab {
      border-right: 0px !important;
      border-left: 1px solid #DEDEDE !important;
      color: #9E9E9E;
      background: transparent !important;
    }

    .nav.nav-tabs-justified li a,
    .nav.nav-tabs-justified li a:hover {
      border-bottom: 2px solid #DEDEDE;
    }

    #signup-tab.active #sutab, #signin-tab.active #litab {
      border-bottom: 2px solid #FFFFFF !important;
      border-top: 2px solid #DEDEDE;
    }

    #myTabs {
      height: 44px;
      overflow: hidden;
    }

    .formy {
      display: block;
      position: relative;
    }

    #libut {
      position: relative;
      margin-bottom: 50px;
      margin-top: 10px;
      margin-left: -80px;
      bottom: -15px;
    }

    .formy #create {
      width: 80% !important;
      position: relative;
      margin-top: 70px;
      margin-left: -40%;
    }

    .auth-form .form-control {
      display: block;
      width: Calc(100% - 40px) !important;
      height: 40px;
      padding: 6px 12px;
      font-size: 14px;
      line-height: 1.42857143;
      color: #555;
      background-color: #fff;
      background-image: none;
      border: 1px solid #ccc;
      border-radius: 0px;
      -webkit-box-shadow: none;
      box-shadow: none;
      -webkit-transition: none;
      -o-transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
      transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
      margin: 5px 20px;
    }

    .auth-form br {
      display: none;
    }

    .auth-form br.special {
      display: block;
    }

    .modal-dialog {
      width: 420px;
      margin: 0 auto;
    }

    .auth-content .auth-content-inner .auth-form {
      border: 2px solid #DEDEDE;
      -webkit-box-shadow: none;
      -moz-box-shadow: none;
      box-shadow: none;
    }

    /* additional auth form stuff */

    @media (max-width: 1200px) {
      .item-container {
        width: Calc(100% - 350px);
        position: fixed;
        left: 0px;
        top: 184px;
      }

      #set-goals, #launch-store {
        top: 132px;
      }

      #filter {
        width: Calc(100% - 349px);
        padding: 10px 20px;
        position: fixed;
        background: #F6F6F6;
        border: 1px solid #DEDEDE;
        left: 0px;
        top: 131px;
        z-index: 999;
        text-align: right;
        height: 53px;
      }

      #filter-label {
        color: #8C8C8C;
        font-weight: 100;
        text-transform: uppercase;
        float: left;
        display: inline-block;
        padding-top: 6px;
      }

      .form-inline {
        text-align: center;
      }

      .form-inline .checkbox {
        padding: 5px 5px;
        text-align: right;
      }
    }

    @media (max-width: 850px) {
      #filter {
        padding: 10px;
      }

      #filter-label {
        display: none;
      }
    }

    .hidedesk {
      display: none;
    }

    .hidemob {
      display: block;
    }

    a.deets {
      padding: 6px 12px;
      border: 1px solid #dedede;
      border-radius: 2px;
      text-transform: uppercase;
      color: #999;
      text-align: center;
      text-transform: none !important;
      margin: 0px 15%;
      color: #484848;
      letter-spacing: 0.05em;
      min-width: 94px;
      cursor: pointer;
    }

    a.deets:hover {
      text-decoration: none !important;
      border: 1px solid #484848;
    }

    /*#choose-row {
      margin-right: -15px;
      margin-left: -15px;
      max-width: 890px;
      padding-right: 65px;
      padding-left: 50px;
      margin: 0 auto;
    }*/

    #choose-row, #launch-row {
      margin-right: -15px;
      margin-left: -15px;
      max-width: 890px;
      padding-right: 50px;
      padding-left: 50px;
      margin: 0 auto;
    }

    #goals-row {
      margin-right: -15px;
      margin-left: -15px;
      max-width: 890px;
      padding-right: 10px;
      padding-left: 10px;
      margin: 0 auto;
    }

    @media (max-width: 900px) {
      #choose-row, #launch-row {
        padding-right: 30px;
        padding-left: 30px;
      }
    }

    @media (max-width: 767px) {

      .side-contain .content {
        bottom: 85px;
      }

      #choose-row, #goals-row, #launch-row {
        padding-right: 0px;
        padding-left: 0px;
      }

      .launch-progress-steps .step .title.desktop {
        display: none;
      }

      .launch-progress-steps .step .title.mobile {
        display: block;
        text-transform: uppercase;
      }

      .launch-progress-steps .step.right {
        padding-left: 10px;
      }

      .item-container {
        width: 50%;
        position: fixed;
        left: 0px;
      }

      .side-contain {
        width: 50%;
      }

      .form-inline .checkbox {
        padding: 10px;
        display: block;
        text-align: left;
      }

      #filter-toggle {

        display: block;
        border: 0px solid transparent;
        width: 100%;
        padding: 5px;
        background: transparent;
        font-size: 14px;
        letter-spacing: 0.1em;
        color: #AFADAD;
        outline: 0 !important;
      }

      #filter {
        width: Calc(50% + 1px);
        padding: 10px;
        height: 53px;
        overflow: hidden;
      }

      .form-inline .checkbox {
        display: none;
      }

      .row {
        margin-right: -15px;
        margin-left: -15px;
        padding-right: 15px;
        padding-left: 15px;
      }

    }

    #hidden-login {
      position: fixed;
      top: 26px;
      right: 50px;
      border: 0px solid transparent;
      background: transparent;
      text-decoration: underline;
      font-size: 14px;
      letter-spacing: 0.1em;
    }

    .btn.disabled {
      pointer-events: none;
    }

    .item.chosen .item-image-container {
      border: 1px solid #EE2435;
    }

    .item:hover h4 {
      text-decoration: none !important;
    }

    .item:hover h4 span {
      text-decoration: none !important;
    }

    a.item, a.item:hover, a.item.active, a.item:focus {
      text-decoration: none !important;
      cursor: pointer;
    }

    .marg {
      padding: 20px;
    }

    h4 {
      text-align: center;
      position: relative;
      width: 100%;
      bottom: 0px;
      color: #444;
      font-size: 14px;
      letter-spacing: 0.1em;
      background: #FFF;
      padding: 9px 3px;
      margin: 0px;
      text-decoration: none !important;
    }

    h4 span {
      font-size: 90%;
      padding-top: 4px;
      display: block;
      color: #8C8C8C;
    }

    #empty-choice {
      padding-top: 50%;
      font-size: 19px;
      display: block;
      color: #CCC;
    }

    #not-empty-choice {
      display: none;
    }

    #last-item-name {
      font-size: 16px;
      padding: 1px 10px 0px;
      margin-bottom: 5px;
    }

    #last-item-description {
      padding: 10px 30px;
      text-align: left;
      font-size: 92%;
      margin-top: 20px;
    }

    #last-item-img {
      height: 250px;
      width: 250px;
      display: inline-block;
      border: 1px solid #DEDEDE;
    }

    #last-item-price {
      font-size: 18px;
      letter-spacing: 0.1em;
      margin-bottom: 18px;
      display: block;
      color: #101010;
    }

    .choose-modal-content-inner {
      text-align: center;
    }

    #modal-last-item-name {
      font-size: 16px;
      padding: 1px 10px 0px;
      margin-bottom: 5px;
      text-align: center;
    }

    #modal-last-item-description {
      padding: 10px 30px;
      text-align: left;
      font-size: 92%;
      margin-top: 20px;
      text-align: center;
    }

    #modal-last-item-img {
      height: 250px;
      width: 250px;
      display: inline-block;
      border: 1px solid #DEDEDE;
    }

    #modal-last-item-price {
      font-size: 18px;
      letter-spacing: 0.1em;
      margin-bottom: 18px;
      display: block;
      color: #101010;
    }

    h2 {
      font-size: 18px;
      font-weight: 600;
      padding-top: 20px;
    }

    .form-group {
      padding-top: 60px;
      padding-right: 40px;
    }

    .dollar .input-group-addon {
      padding: 6px 12px;
      font-size: 14px;
      font-weight: 400;
      line-height: 1;
      color: #555;
      text-align: center;
      background-color: #FFF;
      border-right: 0px solid #FFF;
      border-radius: 0px;
    }

    .dollar .form-control {
      display: block;
      width: 100%;
      max-width: 120px;
      height: 43px;
      padding: 6px 12px;
      font-size: 18px;
      font-family: Helvetica;
      line-height: 1.42857143;
      color: #555;
      background-color: #fff;
      background-image: none;
      border: 1px solid #ccc;
      border-left: 0px solid #FFF;
      border-radius: 0px;
      -webkit-box-shadow: none;
      box-shadow: none;
      text-align: right;
      -webkit-transition: border-color ease-in-out .15s, -webkit-box-shadow ease-in-out .15s;
      -o-transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
      transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
    }

    .thumbnail.selected .dollar .input-group-addon,
    .thumbnail.selected .dollar .form-control {
      border-color: #EE2435;
    }

    .item-image-container {
      display: block;
      height: auto;
      overflow: hidden;
      margin: 0 auto;
      max-width: 100%;
    }

    .img-square {
      max-height: 100%;
      max-width: 100%;
    }
        



    @media (max-width: 1200px) {
      .form-group {
        padding-right: 0px;
      }

      .thumbnail {
        margin: 40px 0px 20px;
      }
    }

    .otherHeading {
      display: none;
    }

    .otherPriceRatio {
      display: none;
      padding-top: 10px;
    }

    #dismiss {
      display: none;
    }

    #dismiss.shown {
      display: block;
    }

    input[type=file] {
      width: 100%;
      border-radius: 5em;
    }

    #earn-range {
      font-size: 30px;
      color: #E35965;
    }

    @media (max-width: 900px) {
      .otherHeading {
        display: block;
      }

      .otherPriceRatio {
        display: block;
      }

      .form-group {
        padding-left: 190px;
      }

      #launch-store-side .content form .form-group {
        padding-left: 0px;
        padding-top: 20px;
      }

      .thumbnail .caption {
        display: none;
      }

      .inputGroupLabel {
        position: absolute;
        margin-top: -70px;
      }
    }

    @media (max-width: 767px) {

      #launch-store-side .content {
        overflow: scroll;
        -webkit-overflow-scrolling: touch;

      }

      #set-goals {
        width: 100%;
        position: fixed;
        left: 0px;
        bottom: 225px;
        top: 132px;
      }

      #set-goals-side {
        right: 0px;
        width: 100%;
        top: Calc(100% - 225px);
        background: #F6F6F6;
        bottom: 0px;
        position: fixed;
        border-top: 1px solid #DEDEDE;
        text-align: center;
      }

      h2 {
        padding-top: 0px;
        margin-top: 0px;
      }

      #earn-range {
        float: right;
        margin-top: -15px;
      }

      #set-goals-side .content {
        text-align: left;
        padding: 30px 30px 0px;
        overflow: visible;
      }

      .hidemobile {
        display: none;
      }

      #launch-store {
        width: 100%;
        position: fixed;
        left: 0px;
        top: 0px;
        bottom: 0px;
        background: #FFF;
        z-index: 200;
        display: none !important;
      }

      #launch-store.shown {
        display: block !important;
      }

      #launch-store-side {
        width: 100%;
      }

      #dismiss {
        width: Calc(100% - 40px);
        left: 20px;
        position: fixed;
        padding: 15px;
        bottom: 15px;
        margin: 0px;
        background: #666;
      }

      #finish {
        width: Calc(50% - 30px);
        left: 50%;
        position: fixed;
        padding: 15px;
        bottom: 15px;
        margin: 0px;
      }

      #previewed {
        width: Calc(50% - 30px);
        left: 20px;
        position: fixed;
        padding: 15px;
        bottom: 15px;
        margin: 0px;
        background: #666;
      }

      #earn-range {
        font-size: 20px;
      }

      #range-label {
        position: absolute;
        right: 30px;
        top: 10px;
      }

      #last-item-img {
        height: 200px;
        width: 200px;
      }

      #modal-last-item-img {
        height: 200px;
        width: 200px;
      }

    }

    .thumbnail img.goal-img {
      width: 170px;
      height: 170px;
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
      border: 1px solid #DEDEDE;
      letter-spacing: 0.05em;
      color: #797979;
      text-shadow: none;
      box-shadow: none;
      padding: 13px 40px 10px 10px;
      cursor: pointer;
      text-align: center;
      text-transform: uppercase;
    }

    .styledSelect.active {
      border: 1px solid #EE2435;
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
      border: 1px solid #EE2435;
      background-color: white;
      -webkit-box-shadow: none;
      -moz-box-shadow: none;
      box-shadow: none;
      width: auto;
      max-height: 200px;
      overflow-y: scroll;
      -webkit-overflow-scrolling: touch;
      margin-right: 0px;
      margin-left: 0px;
      margin-top: -1px;
      text-transform: uppercase;
    }

    .options li {
      padding: 0 6px;
      margin: 0 0;
      padding: 10px 10px;
    }

    .select {
      cursor: pointer;
      display: inline-block;
      position: relative;
      font: normal 16px/22px Arial, Sans-Serif;
      color: black;
      height: 44px;
      margin-bottom: 5px;
      margin-left: 30px;
    }

    .options li:hover {
      background-color: #666666;
      color: #fff;
    }

    .s-hidden {
      visibility: hidden;
      padding-right: 10px;
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

    h6 {
      font-size: 18px;
      margin-top: 5px;
      margin-bottom: 10px;
    }

    #goals-description {
      padding: 10px 30px;
      text-align: center;
      font-size: 100%;
      margin-top: 20px;
      color: #8C8C8C;
    }

    #unit-label {
      position: relative;
      top: 10px;
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
    }

    .facepic {
      border-radius: 60px;
      height: 120px;
      width: 120px;
    }

    .lander h1 {
      font-size: 26px;
      letter-spacing: .06em;
      margin-bottom: 4px;
      text-align: center;
    }

    .lander h5 {
      padding-bottom: 15px;
      margin-top: 0px;
      margin-bottom: 0px;
      font-size: 17px;
      color: #8e8b8b;
      padding: 40px;
      text-align: center;
      line-height: 1.3em;
    }

    .soc-links {
      margin-top: 20px;
      display: none;
    }

    .soc-links a {
      display: inline-block;
      color: #CCC;
      font-size: 30px;
      padding: 5px;
      -webkit-transition: .3s all;
      transition: .3s all;
    }

    @media (max-width: 480px) {

      .item.chosen:after {
        content: "ADDED \A TO YOUR \A STOREFRONT";
        width: 50%;
        left: 25%;
        top: 25%;
        position: absolute;
        text-align: center;
        color: #EE2435;
        display: none;
        white-space: pre;
      }

      .item.chosen:after {
          content: "";
          width: 100%;
          height: 100%;
          top: 0;
          left: 0;
          position: absolute;
          text-align: center;
          color: #EE2435;
          display: none;
          white-space: pre;
          background: url("https://storage.googleapis.com/troveup-imagestore/assets/mto-confirm.png") no-repeat;
          background-size: 100px;
          background-position: 50% 25%;
      }


      .item.chosen:after {
        display: block;
      }

      .hidedesk {
        display: block;
      }

      .hidemob {
        display: none;
      }

      .side-contain .content {
        display: none;
      }

      #launch-store-side .content, #set-goals-side .content {
        display: block;
      }

      #set-goals-side .content {
        position: fixed;
        top: 50px;
        background: #F6F6F6;
        bottom: Calc(100% - 162px);
      }

      .form-group {
        padding-left: 40%;
        padding-top: 20px;
      }

      .thumbnail img.goal-img {
        width: 100px;
        height: 100px;
      }

      .modal-backdrop.in {
        filter: alpha(opacity=100);
        opacity: 1.0;
      }

      .modal-header .closeModal {
        position: absolute;
        font-size: 35px;
        right: 38px;
        top: 27px;
      }

      .auth-form p {
        font-size: 13px;
        margin-bottom: 30px;
        text-align: left;
        padding-left: 30px;
        margin-top: 7px;
      }

      .auth-content .auth-content-inner .auth-form {
        width: 100%;
        background: #FFFFFF;
        border: 2px solid #FFFFFF;
      }

      .auth-content .auth-content-inner .auth-form {
        border: 2px solid #FFFFFF;
        -webkit-box-shadow: none;
        -moz-box-shadow: none;
        box-shadow: none;
      }

      #sutab, #litab {
        border: 0px solid #FFF;
        border-bottom: 2px solid #DEDEDE;
      }

      #sutab.active, #litab.active {
        color: 444;
      }

      #filter {
        width: 100%;
        height: 52px;
        overflow: hidden;
        top: 49px;
        background: #8E8B8B;
        border-color: #8E8B8B;
      }

      .form-inline {
        padding: 10px;
      }

      #filter-toggle {
        width: 50%;
        text-align: left;
        padding: 6px 5px 0px 32px;
        color: #FFF;
        display: inline-block;
        position: absolute;
        left: 10px;
      }

      #hidden-login {
        display: inline-block;
        width: 50%;
        text-align: right;
        padding: 5px 15px 5px 5px;
        border: 0px solid transparent;
        background: transparent;
        font-size: 14px;
        letter-spacing: 0.1em;
        color: #AFADAD;
        outline: 0 !important;
        top: 60px;
        z-index: 99;
      }

      #hidden-login.second {
        display: none;
      }

      .nav-center {
        display: none;
      }

      .launch-progress-steps {
        padding-left: 6%;
        overflow: hidden;
        width: 100%;
        position: fixed;
        top: 0px;
        left: 0px;
        right: 0px;
      }

      .launch-progress-steps .step .title.desktop {
        display: none;
      }

      .launch-progress-steps .step .title.mobile {
        display: block;
      }

      .launch-progress-steps .step.left {
        padding-left: 0px;
      }

      .launch-progress-steps .step.middle {
        padding-left: 5px;
      }

      .launch-progress-steps .step.right {
        padding-left: 5px;
      }

      .launch-progress-steps .step .title {
        font-size: 11px;
      }

      .side-contain {
        right: 0px;
        width: 100%;
        top: Calc(100% - 88px);
        background: #F6F6F6;
        bottom: 0px;
        position: fixed;
        border-top: 1px solid #DEDEDE;
        text-align: center;
      }

      .item-container {
        width: 100%;
        position: fixed;
        left: 0px;
        bottom: 62px;
        top: 101px;
      }

      button.side-panel-next-btn {
        width: Calc(100% - 40px);
        left: 20px;
        position: fixed;
        padding: 15px;
        bottom: 15px;
        margin: 0px;
      }

      .side-panel-next-area {
        width: Calc(100% - 40px);
        left: 20px;
        position: fixed;
        padding: 15px;
        bottom: 15px;
        margin: 0px;
      }

      #back {
        display: none;
        bottom: 22px;
        left: 110px;
      }

      #set-goals {
        top: 50px;
        padding: 0px;
      }

      .thumbnail {
        border-radius: 0px;
        margin: 0px;
        padding: 10px 20px;
      }

      #unit-label {
        position: absolute;
        top: 12px;
      }

      .select {
        margin-left: 0px;
        top: 8px;
      }

      #launch-store-side {
        right: 0px;
        width: 100%;
        top: 49px;
        background: #F6F6F6;
        bottom: 0px;
        position: fixed;
        border-top: 1px solid #DEDEDE;
        text-align: center;
      }

      #set-goals-side {
        top: Calc(100% - 85px);
        background: #F6F6F6;
        bottom: 0px;
      }

      #set-goals {
        top: 161px;
        padding: 0px;
        bottom: 85px;
      }

      #hidden-login {
        top: 62px;
        right: 14px;
      }

      #earn-range {
        margin-top: 20px;
        padding-bottom: 0px;
        position: absolute;
        top: 37px;
        right: 10px;
        font-size: 14px;
      }

      #range-label {
        top: 44px;
      }

    }

    #launch-store-side .content .form-control {
      border-radius: 0px;
      box-shadow: none;
      -webkit-box-shadow: none;
      outline: 0 !important;
    }

    #launch-store-side .input-group-addon {
      padding: 6px 12px;
      font-size: 14px;
      font-weight: 400;
      line-height: 1;
      color: #555;
      text-align: center;
      background-color: #CCCCCC;
      border: 1px solid #CCCCCC;
      border-radius: 0px;
    }

    textarea {
      resize: none;
    }

    @media (min-width: 768px) {
      #filter-toggle {
        display: none !important;
      }

      .item-container::-webkit-scrollbar {
        width: 16px;
      }

      .item-container::-webkit-scrollbar-track {
        background: #FFF;
        border-radius: 10px;
      }

      .item-container::-webkit-scrollbar-thumb {
        border-radius: 14px;
        background-color: #E8E8E8;
        border: 5px solid #FFF;
      }

      #launch-store-side .content form .form-group {
        padding-left: 0px;
        padding-top: 20px;
        padding-right: 5px;
      }

    }

    @media (min-width: 1200px) {
      /*#choose-row {
        padding-right: 0px;
        padding-left: 150px;
      }*/
      #choose-row, #launch-row {
        padding-right: 70px;
        padding-left: 70px;
      }

      #goals-row {
        padding-right: 50px;
        padding-left: 50px;
      }

      #filter {
        left: 20px;
      }

      .item-container {
        left: auto;
        width: Calc(100% - 350px);
        right: 350px;
        margin: 0px;
        text-align: center;
        max-width: none;
      }

      .form-inline {
        padding: 22px 0px 22px 11px;
        font-size: 15px;
      }

      #filter-label {
        display: block;
        width: 100%;
        text-align: center;
        padding: 6px 0px;
        background: #F6F6F6;
        border-bottom: 1px solid #dedede;
        font-size: 16px;
      }

    }

    @media (min-width: 1300px) {
      #filter {
        left: 20px;
      }
    }

    @media (min-width: 1400px) {

      #filter {
        width: 144px;
        padding: 0px;
        position: fixed;
        background: #FFFFFF;
        border: 1px solid #DEDEDE;
        z-index: 999;
        left: Calc(50% - 175px);
        top: 235px;
        margin-left: -475px;
      }

    }

    /* No greater than 750px, no less than 480px */
    @media (max-width: 750px) and (min-width: 480px) {
      .col-xs-6 {
        width: 100%;
      }
      .side-panel-next-btn.btn {
        bottom: 20px;
      }
    }

    ​
    form#profile-form .form-group {
      padding-top: 10px;
      padding-right: 10px;
    }

    .btn-select {
      bottom: -80px;
    }

    a.store-item, a.store-item:hover, a.store-item.active, a.store-item:focus {
      text-decoration: none !important;
    }


     @media (max-width: 767px) {
      .modal-dialog {
        width: 100%;
      }

    }

    #store-profile-img {
      background-size: cover !important;
      background-position: center center !important;
    }

    <%--Fixes width issue on step two using Firefox --%>
    @-moz-document url-prefix() {
      .firefox-width {
        width: 100%;
      }
    }

    .profile-img-container {
      display: block;
      background: rgb(140, 140, 140) none repeat scroll 0% 0%;
      padding: 42px 11px;
      text-align: center;
      margin: 0px auto 10px;
      width: 102px;
      height: 102px;
      border-radius: 5em;
      border: 1px solid rgb(140, 140, 140);
    }

  </style>
</head>
<body class="${bodyClass}">
<c:import url="../fragments/modals/authModal.jsp"/>


<div class="spinstuff show">
  <span class="spinner page-fade-spinner"></span>
</div>
<div class="choosestuff">

  <div class="nav-center">
    <a data-role="none" id="topnav_home" href="/welcome">
      <img alt="Trovelogo" class="nav-logo"
           src="https://storage.googleapis.com/troveup-imagestore/assets/img/logo-text.png">
    </a>
  </div>

  <div class="launch-progress-steps">
    <div id="banner-step-1" class="step left active">
      <span class="label">1</span>
      <span class="title desktop">Choose Jewelry</span>
      <span class="title mobile">Choose</span>
    </div>
    <div id="banner-step-2" class="step middle">
      <span class="label">2</span>
      <span class="title desktop">Set Goals</span>
      <span class="title mobile">Goals</span>
    </div>
    <div id="banner-step-3" class="step right">
      <span class="label">3</span>
      <span class="title desktop">Launch Store</span>
      <span class="title mobile">Launch</span>
    </div>
  </div>

  <%-- Not using filters initally, unhide this when filters are ready --%>
  <%--div id="filter" class="">
    <button id="filter-toggle" class=""> Filters</button>
    <span id="filter-label" class="">Filter:</span>
    <form class="form-inline">
      <div class="checkbox">
        <label><input category="necklace" type="checkbox"/> Necklaces </label>
      </div>
      <div class="checkbox">
        <label><input category="bracelet" type="checkbox"/> Bracelets </label>
      </div>
      <div class="checkbox">
        <label><input category="earring" type="checkbox"/> Earrings </label>
      </div>
      <div class="checkbox">
        <label><input category="ring" type="checkbox"/> Rings </label>
      </div>
    </form>

  </div--%>

  <c:if test="${not isAuthenticated}">
    <button id="hidden-login" onClick="triggerAuthModal();" class="">Login</button>
  </c:if>
  

  <!-- step1 -->
  <div id="choose-jewelry" class="container item-container active">
    <h3>Add at least 5 items to your storefront</h3>
    <div id="choose-row" class="row">
    </div>
  </div>

  <div id="choose-jewelry-side" class="side-contain active">
    <div id="choose-content" class="content">
      <span id="empty-choice">Choose an Item<br>to Get Started</span>
          <span id="not-empty-choice">
          <h5 id="last-item-name">Item Name</h5>
          <span id="last-item-price">$19.95</span>
            <img id="last-item-img" data-src="holder.js/100%x200" alt="100%x200"
                 src="https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/rumpus-ring.jpg"
                 data-holder-rendered="true">
            <p id="last-item-description">description lal nsnws bsbqs bws bwdwb bw bwd bwdbjwbbdwj wbwjw .</p>
            <br><br>
          </span>
    </div>
    <button id="next-button-step-1" class="side-panel-next-btn btn disabled">Add <span id="items-left">5</span> more
      items
    </button>
  </div>

  <div class="modal fade chooseModal" id="chooseModal" tabindex="-1" role="dialog" aria-labelledby="chooseModalALabel"
       aria-hidden="true">
    <div class="modal-dialog">
      <div id="chooseModalContent">
        <div class="modal-header">
          <a href="#" data-dismiss="modal" class="closeModal"><i class="ion-android-close"></i></a>
        </div>
        <div class="choose-modal-content">
          <div class="choose-modal-content-inner">
            <h5 id="modal-last-item-name">Item Name</h5>
            <span id="modal-last-item-price">$19.95</span>
            <img id="modal-last-item-img" data-src="holder.js/100%x200" alt="100%x200"
                 src="https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/rumpus-ring.jpg"
                 data-holder-rendered="true">
            <p id="modal-last-item-description">description lal nsnws bsbqs bws bwdwb bw bwd bwdbjwbbdwj wbwjw .</p>
            <br><br>
            <button id="modal-select-item" class="btn-select btn">Select Item</button>
          </div>
        </div>
      </div>
    </div>
  </div>


  <!-- end step1 -->

  <!-- step2 -->
  <div id="set-goals" class="container item-container">
    <h3>Set Selling Prices for Your Items</h3>
    <div id="goals-row" class="row">
    </div>
  </div>

  <div id="set-goals-side" class="side-contain">
    <div class="content">
      <h6 class="hidemobile" id="sales-goals-heading">Your Sales Goals</h6>
      <p class="hidemobile" id="goals-description">Set a sales goal to see how much you'll be able to make for a given number of sales.</p>
      <br class="hidemobile"><br class="hidemobile">
      <span id="unit-label"># of Units</span>
      <select style="min-width: 100px;" id="num-of-units" class="s-hidden">
        <option value="25">25</option>
        <option value="50">50</option>
        <option value="100">100</option>
        <option value="150">150</option>
        <option value="200">200</option>
        <option value="250">250</option>
      </select>
      <br class="hidemobile"><br class="hidemobile">
      <p id="range-label">You can make</p>
      <h3 id="earn-range">$100 - $250+</h3>
    </div>
    <button id="next-button-step-2" class="side-panel-next-btn btn">Next</button>
  </div>

  <!-- end step2 -->


  <!-- step3 -->
  <div id="launch-store" class="container item-container">
    <h3>Edit Your Storefront's Look and Feel</h3>
    <div class="row landingbanner">
      <div class="lander">
        <img id="user-profile-img" class="facepic"
             src="https://storage.googleapis.com/troveup-imagestore/assets/img/placeholder-item.jpg">
        <h1 id="display-store-name">Your Store Name</h1>
        <h5 id="store-display-description">A little something about <br> yourself goes here.</h5>
      </div>
    </div>
    <div id="launch-row" class="row">
    </div>
    <button id="dismiss" class="btn">Dismiss</button>
  </div>

  <div id="launch-store-side" class="side-contain">
    <div class="content">
      <h6 class="hidemobile" id="launch-store-details-heading">Set your storefront details</h6>
      <form id="profile-form">
        <div class="form-group">
          <label for="store-profile-img">Upload a Picture</label>
          <div id="store-profile-img-container" class="profile-img-container">
            <input type="file" id="store-profile-img">
          </div>
          <div class="error" style="display: none" id="upload-picture-error">This field is required.</div>
        </div>
        <div class="form-group">
          <label for="input-store-name">Store Name</label>
          <input type="text" class="form-control" id="input-store-name" placeholder="Your Store Name" maxlength="20">
          <div class="input-text-counter" id="name-input-length-counter"></div>
          <div class="error" style="display: none" id="store-name-error">This field is required.</div>
        </div>
        <div class="form-group">
          <label for="input-store-description">Store Description</label>
          <textarea id="input-store-description" class="form-control" rows="3"
                    placeholder="Write a little something about you or your store." maxlength="50"></textarea>
          <div class="input-text-counter" id="description-input-length-counter"></div>
          <div class="error" style="display: none" id="store-description-error">This field is required.</div>
        </div>
        <div class="form-group">
          <label for="input-url">URL</label>
          <p class="">Where customers can find your jewelry.</p>
          <div class="error" style="display: none" id="store-url-error">This field is required.</div>
          <div class="input-group url">
            <div class="input-group-addon">troveup.com/sellers/</div>
            <input type="text" class="form-control" id="input-url" placeholder="your-name">
          </div>
          <div style="display: none;" id="url-message"></div>
          <div style="padding-top: 10px;"><span id="url-input-spinner" class="button-spinner"></span></div>
        </div>
      </form>
    </div>
    <button id="previewed" class="btn">Preview</button>
    <button id="finish" class="btn">Launch</button>
  </div>

  <!-- end step2 -->


</div>

<!-- <script type="text/javascript" src="/resources/js/trove/Auth.js"></script> -->
<script type="text/javascript" src="/resources/js/trove/AJAX.js"></script>
<script type="text/javascript" src="/resources/js/trove/ImageInput.js"></script>
<script type="text/javascript" src="/resources/js/trove/StringUtil.js"></script>
<script>
  // var authHelper = new AuthHelper("${_csrf.token}", ${isAuthenticated});
  var MAX_STEP_ONE_PAGE_SEEK_SIZE = 10;
  var LAST_PAGE_STEP = 3;
  var FIRST_PAGE_STEP = 1;
  var CHECK_URL_TIMEOUT = 1000;
  var STEP_TWO_NEGATIVE_PROFIT_ERROR = "Oops!  Profits/sale cannot be negative.";
  var INVALID_CHARACTER_MESSAGE = "Invalid Characters: Please use only alpha numeric characters, dashes, and underscores.";
  var URL_ALREADY_TAKEN_MESSAGE = "Oops!  That one is already taken.  Please try another.";
  var URL_AVAILABLE_MESSAGE = "This URL is available!";
  var URL_MESSAGE_TYPE_ERROR = "ERROR";
  var URL_MESSAGE_TYPE_SUCCESS = "SUCCESS";

  var urlInputLength = 0;
  var checkUrlTimeoutId = null;
  var imageInputHelper = new ImageInputHelper();
  var ajaxHelper = new AJAXHelper("${_csrf.token}");
  var stepOnePageNumber = 0;
  var seekingStepOneItems = false;
  var endOfStepOneItems = false;
  var currentPageStep = FIRST_PAGE_STEP;

  var stepOneMainContainer = $("#choose-jewelry");
  var stepOneItemContainer = $("#choose-row");
  var stepOneSideContainer = $("#choose-jewelry-side");
  var stepTwoMainContainer = $("#set-goals");
  var stepTwoItemContainer = $("#goals-row");
  var stepTwoSideContainer = $("#set-goals-side");
  var stepThreeMainContainer = $("#launch-store");
  var stepThreeItemContainer = $("#launch-row");
  var stepThreeSideContainer = $("#launch-store-side");

  var stepTwoHighestItemPrice = 0.00;
  var stepTwoLowestItemPrice = 999999.00;

  function land() {
    mixpanel.track("land_onboard_choose");
  }

  $(document).ready(function () {
    land();

    $('.auth-form p').html("Sign in or sign up to complete<br class='special'>your storefront's launch!");
    setupSelects();

    retrieveAndAppendStepOneItems(stepOnePageNumber, MAX_STEP_ONE_PAGE_SEEK_SIZE, stepOneItemContainer, firstTimeRunCallback);


    $(stepOneMainContainer).scroll(function () {
      if (!endOfStepOneItems && !seekingStepOneItems && ($(stepOneMainContainer).height() - $(stepOneMainContainer).scrollTop() <= ($(stepOneMainContainer).height()) * 0.50)) {
        retrieveAndAppendStepOneItems(stepOnePageNumber, MAX_STEP_ONE_PAGE_SEEK_SIZE, stepOneItemContainer);
      }
    });

    $('#next-button-step-1').on('click', setPageModeStepTwo);
    $('#num-of-units').change(recalculateAndSetSalesGoals);

    setupStepThreeInputs();
    authSuccessCallback = rtwAuthSuccessCallback;
    authHelper.setActionToPerformPostAuth(postAuthSuccessCallback());
  });

  var chosenItems = [];

  $('input:checkbox').change(function (e) {
    if ($("input:checkbox:checked").length > 0) {
      var selects = [];
      $('.item').parent().hide("fade", {direction: "down"}, "fast");
      $('input:checkbox').each(function () {
        if ($(this).is(':checked')) {
          selects.push($(this).attr("category"));
        }
      });
      $.each(selects, function (index, value) {
        $('.item[itemcategory="' + value + '"]').each(function () {
          $(this).parent().show("fade", {direction: "up"}, "fast");
        });
      });
    }
    else {
      $('.item').parent().show("fade", {direction: "up"}, "fast");
    }
  });

  // $("#scrollbutt").click(function(e) {
  //   $("html, body").animate({ scrollTop: 0 }, "slow");
  //   return false;
  // });

  $('#previewed').click(function (e) {
    $('#launch-store').addClass("shown");
    $('#dismiss').addClass("shown");
  });

  $('#dismiss').click(function (e) {
    $('#launch-store').removeClass("shown");
    $('#dismiss').removeClass("shown");
  });

  function rtwAuthSuccessCallback() {
    return function customAuthCallback(data) {
      authHelper.setIsAuthenticated(true);
      authModal.modal('hide');
      if (authHelper.getActionToPerformPostAuth() != null) {
        authHelper.getActionToPerformPostAuth().apply(null, authHelper.getArgsForPostAuthAction());
      }
    }
  }

  function postAuthSuccessCallback() {
    return function() {
       $('#hidden-login').hide();
       $('#finish').click();
    }
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
        //Fires the on change listener manually, since we're manually manipulating the select's value
        $this.change();
      });

      // Hides the unordered list when clicking outside of it
      $(document).click(function () {
        $styledSelect.removeClass('active');
        $list.hide();
      });
    });
  }

  // detail button modal stuff
  var detailModal = $('#chooseModal');
  function centerDetailModal() {
    var wh = Math.min($(window).height(), window.innerHeight);
    var mh = Math.max(( wh - (540) ) / 2, 0);
    $('#chooseModalContent').css("margin-top", String(mh) + "px");
  }
  function showDetailsModal() {
    detailModal.modal('show');
    centerDetailModal();
  }
  $(window).on('resize', function () {
    centerDetailModal()
  });
  // end detail button modal stuff

  function createStepOneItem(simpleItem) {
    var containingDiv =
        $('<div></div>')
            .addClass('marg')
            .addClass('col-md-4')
            .addClass('col-sm-6')
            .addClass('col-xs-6');

    var squareClickActionContainer =
        $('<a></a>')
            .addClass('item')
            .attr('id', 'item-' + simpleItem.simpleItemId)
            .attr('itemid', simpleItem.simpleItemId)
            .attr('itemprice', parseFloat(simpleItem.itemPrice).toFixed(2))
            .attr('itemdescript', simpleItem.itemDescription)
            //TODO: Attribute hard coded to ALL, categories functionality to come later
            .attr('itemcategory', "All")
            .attr('itemname', simpleItem.itemName)
            .click(function () {
              var index = chosenItems.indexOf(simpleItem.simpleItemId);
              if (index > -1) {
                chosenItems.splice(index, 1);
                $("#item-" + simpleItem.simpleItemId).removeClass('chosen');
              } else {
                $("#item-" + simpleItem.simpleItemId).addClass('chosen');
                chosenItems.push(simpleItem.simpleItemId);
              }
              setSidePanelDetails("#item-" + simpleItem.simpleItemId);
              var newItemsLeft = Math.max(0, 5 - chosenItems.length);
              $('#items-left').html(newItemsLeft);
              if (chosenItems.length >= 5) {
                $('#next-button-step-1').removeClass('disabled');
                $('#next-button-step-1').html("Next");
              } else {
                $('#next-button-step-1').addClass('disabled');
                $('#next-button-step-1').html("Add <span id='items-left'>" + Math.max(0, 5 - chosenItems.length) + "</span> more items");
              }
            })
            .hover(function (e) {
              e.preventDefault();
              setSidePanelDetails("#item-" + simpleItem.simpleItemId);
              e.stopPropagation();
            });
    var itemImageContainer =
        $('<div></div>')
            .addClass('item-image-container');

    var itemImage =
        $('<img>')
            .addClass('img-responsive')
            .attr('id', 'img-' + simpleItem.simpleItemId)
            .attr('alt', '160x160')
            .attr('src', simpleItem.primaryDisplayImageUrl);

    var headerItemTextContainer =
        $('<h4></h4>');

    var spanItemName =
        $('<span></span>')
            .addClass('hidemob')
            .addClass('limit-long-text')
            .html(simpleItem.itemName);

    var spanItemPrice =
        $('<span></span>')
            .addClass('limit-long-text')
            .html('$' + parseFloat(simpleItem.itemPrice).toFixed(2));

    var mobileDetailsButton =
        $('<a></a>')
            .addClass('deets')
            .addClass('hidedesk')
            .html('DETAILS')
            .click(function () {
              setModalDetails("#item-" + simpleItem.simpleItemId);
              showDetailsModal();
              $('#modal-select-item').attr("onclick", "selectModalItem('#item-" + simpleItem.simpleItemId + "');");
            })
            .hover(function (e) {
              setModalDetails("#item-" + simpleItem.simpleItemId);
            });

    spanItemName.appendTo(headerItemTextContainer);
    spanItemPrice.appendTo(headerItemTextContainer);

    itemImage.appendTo(itemImageContainer);
    itemImageContainer.appendTo(squareClickActionContainer);
    headerItemTextContainer.appendTo(squareClickActionContainer);

    squareClickActionContainer.appendTo(containingDiv);
    mobileDetailsButton.appendTo(containingDiv);

    return containingDiv;
  }

  function retrieveAndAppendStepOneItems(pageNumber, pageLimit, containerToAppendTo, firstTimeRunCallback) {
    seekingStepOneItems = true;
    var dataObject = ajaxHelper.createOrAppendGetDataObject("pagenumber", pageNumber, null);
    dataObject = ajaxHelper.createOrAppendGetDataObject("pagelimit", pageLimit, dataObject);

    ajaxHelper.performGetRequest(dataObject, "/readytowear/ajax/allitems", createRetrieveItemsSuccessCallback(containerToAppendTo, pageLimit, firstTimeRunCallback), createRetrieveItemsFailureCallback())
  }

  function createRetrieveItemsSuccessCallback(containerToAppendTo, pageLimit, firstTimeRunCallback) {
    return function (data) {
      $.each(data.items, function () {
        createStepOneItem(this)
            .appendTo(containerToAppendTo);
      });

      stepOnePageNumber += 1;
      seekingStepOneItems = false;

      if (data.items.length < pageLimit) {
        endOfStepOneItems = true;
      }

      if (firstTimeRunCallback != null) {
        firstTimeRunCallback();
      }
    }
  }

  function createRetrieveItemsFailureCallback() {
    return function () {
      seekingStepOneItems = false;
    }
  }

  function firstTimeRunCallback() {
    //Hide the page spinner
    $('.spinstuff').toggleClass('show');
    $('.choosestuff').toggleClass('show');
  }

  function setSidePanelDetails(itemContainer) {
    var imgsrc = $(itemContainer).children().first().children().first().attr("src");
    var itemname = $(itemContainer).attr("itemname");
    var itemprice = $(itemContainer).attr("itemprice");
    var itemdescrp = $(itemContainer).attr("itemdescript");
    var itemcat = $(itemContainer).attr("itemcategory");
    $('#empty-choice').css("display", "none");
    $('#last-item-img').attr("src", imgsrc);
    $('#last-item-name').html(itemname);
    $('#last-item-description').html(itemdescrp);
    $('#last-item-price').html('$' + itemprice);
    $('#not-empty-choice').css("display", "block");
  }

  function setModalDetails(itemContainer) {
    var imgsrc = $(itemContainer).children().first().children().first().attr("src");
    var itemname = $(itemContainer).attr("itemname");
    var itemprice = $(itemContainer).attr("itemprice");
    var itemdescrp = $(itemContainer).attr("itemdescript");
    var itemcat = $(itemContainer).attr("itemcategory");
    // modal mimic container changes
    $('#modal-last-item-img').attr("src", imgsrc);
    $('#modal-last-item-name').html(itemname);
    $('#modal-last-item-description').html(itemdescrp);
    $('#modal-last-item-price').html('$' + itemprice);
  }

  function selectModalItem(itemContainer) {
    $('.closeModal').click();
    $(itemContainer).click();
  }


  function setBannerStepActive(stepNumber) {

    //Banner is moving forward
    if (stepNumber > currentPageStep) {
      $('.launch-progress-steps .step:nth-of-type(' + stepNumber + ')').addClass('active');
      $('.launch-progress-steps .step:nth-of-type(' + (stepNumber - 1) + ')').removeClass('active').addClass('done');
      $('.launch-progress-steps .step:nth-of-type(' + (stepNumber - 1) + ') .label').html("<span class='icon ion-checkmark'></span>");
    }
    //Banner is moving backwards
    else {

      for (var nonCurrentStep = LAST_PAGE_STEP; nonCurrentStep > stepNumber; nonCurrentStep--) {
        $('.launch-progress-steps .step:nth-of-type(' + nonCurrentStep + ')').removeClass('active done');
        $('.launch-progress-steps .step:nth-of-type(' + nonCurrentStep + ') .label').html(nonCurrentStep);
      }

      $('.launch-progress-steps .step:nth-of-type(' + stepNumber + ')').addClass('active');
      $('.launch-progress-steps .step:nth-of-type(' + (stepNumber) + ') .label').html(stepNumber);
    }
  }

  function hideAllStepElements() {
    $('#choose-jewelry').hide().removeClass('active');
    $('#choose-jewelry-side').hide().removeClass('active');
    $('#set-goals').hide().removeClass('active');
    $('#set-goals-side').hide().removeClass('active');
    $('#launch-store').hide().removeClass('active');
    $('#launch-store-side').hide().removeClass('active');
  }

  function showStepOneElements(direction) {

    var directionToUse = direction == null ? "right" : direction;
    $('#choose-jewelry').show("slide", {direction: directionToUse}, 'fast').addClass('active');
    $('#choose-jewelry-side').show("slide", {direction: directionToUse}, 'fast').addClass('active');
  }

  function showStepTwoElements(direction) {
    var directionToUse = direction == null ? "right" : direction;
    $('#set-goals').show("slide", {direction: directionToUse}, 'fast').addClass('active');
    $('#set-goals-side').show("slide", {direction: directionToUse}, 'fast').addClass('active');
  }

  function showStepThreeElements(direction) {
    var directionToUse = direction == null ? "right" : direction;
    $('#launch-store').show("slide", {direction: directionToUse}, 'fast').addClass('active');
    $('#launch-store-side').show("slide", {direction: directionToUse}, 'fast').addClass('active');
  }

  function populatePageOneSelectedItemsIntoPageTwo() {
    $.each(chosenItems, function () {
      var itemSelect = $('a[itemid=' + this + ']');
      var itemImgSrc = $(itemSelect.find('.img-responsive')[0]).attr('src');

      var priceCalculationRow = createStepTwoItemPriceCalculationRow(itemSelect.attr('itemid'), itemSelect.attr('itemname'), itemImgSrc, itemSelect.attr('itemprice'));

      stepTwoItemContainer.append(priceCalculationRow);
    });
  }

  function createStepTwoItemPriceCalculationRow(itemId, name, imageUrl, price) {
    var divContainer =
        $('<div></div>')
            .addClass('thumbnail')
            .addClass('firefox-width');

    var errorContainer =
        $('<div></div>')
            .addClass('error')
            .attr('id', 'error-item-' + itemId)
            .css('display', 'none')
            .html(STEP_TWO_NEGATIVE_PROFIT_ERROR);

    var title =
        $('<h2></h2>')
            .addClass('otherHeading')
            .html(name);

    var itemImg =
        $('<img>')
            .addClass('goal-img')
            .attr('src', imageUrl);

    var captionContainer =
        $('<div></div>')
            .addClass('caption');

    var subHeading =
        $('<h2></h2>')
            .html(name);

    var profitPerSalePTag =
        $('<p>$</p>');

    var profitPerSaleSpan =
        $('<span></span>')
            .attr('id', 'primary-profit-display-' + itemId)
            .addClass('profitPerSale')
            .html(parseFloat(price * .25).toFixed(2));

    profitPerSalePTag
        .append(profitPerSaleSpan)
        .append(" Profit/sale");

    var priceInputContainerDiv =
        $('<div></div>')
            .addClass('form-group');

    var priceInputInnerContainer =
        $('<div></div>')
            .addClass('input-group')
            .addClass('dollar');

    var dollarLabelDiv =
        $('<div>$</div>')
            .addClass('input-group-addon');

    var priceInput =
        $('<input>')
            .addClass('form-control')
            .addClass('numberinput')
            .attr('id', 'set-price-' + itemId)
            .attr('type', "number")
            .attr('value', parseFloat(price * 1.25).toFixed(2))
            .attr('min', price)
            .attr('step', '1.00');

    var sellingPriceSpanLabel =
        $('<span></span>')
            .addClass('inputGroupLabel')
            .html('Selling Price');

    var secondProfitPerSalePTag =
        $('<p>$</p>')
            .addClass('otherPriceRatio');

    var secondProfitPerSaleSpan =
        $('<span></span>')
            .attr('id', 'secondary-profit-display-' + itemId)
            .addClass('profitPerSale2')
            .html(parseFloat(price * .25).toFixed(2));

    attachPriceInputCalculation(errorContainer, priceInput, profitPerSaleSpan, secondProfitPerSaleSpan);

    secondProfitPerSalePTag
        .append(secondProfitPerSaleSpan)
        .append(" Profit/sale");

    priceInputInnerContainer
        .append(dollarLabelDiv)
        .append(priceInput);

    priceInputContainerDiv
        .append(priceInputInnerContainer)
        .append(sellingPriceSpanLabel)
        .append(secondProfitPerSalePTag);

    captionContainer
        .append(subHeading)
        .append(profitPerSalePTag);

    divContainer
        .append(errorContainer)
        .append(title)
        .append(itemImg)
        .append(captionContainer)
        .append(priceInputContainerDiv);

    attachPriceCalculationRowListeners(divContainer);

    return divContainer;

  }

  function setPageModeStepOne() {
    var BANNER_STEP_ONE = 1;

    hideAllStepElements();
    showStepOneElements(BANNER_STEP_ONE < currentPageStep ? "left" : "right");
    setBannerStepActive(BANNER_STEP_ONE);

    clearBannerStepOneClickable();
    clearBannerStepTwoClickable();
    $('#next-button-step-1')
        .off('click')
        .on('click', setPageModeStepTwo);

    stepTwoItemContainer.empty();
    stepThreeItemContainer.empty();

    currentPageStep = BANNER_STEP_ONE;
  }

  function setPageModeStepTwo() {
    var BANNER_STEP_TWO = 2;

    setBannerStepActive(BANNER_STEP_TWO);
    hideAllStepElements();
    showStepTwoElements(BANNER_STEP_TWO < currentPageStep ? "left" : "right");

    if (stepTwoItemContainer.children().length == 0) {
      populatePageOneSelectedItemsIntoPageTwo();
    }

    refreshSalesGoalBounds();
    recalculateAndSetSalesGoals();

    setBannerStepOneClickable();
    clearBannerStepTwoClickable();

    $('#next-button-step-2')
        .off('click')
        .on('click', setPageModeStepThree);

    stepThreeItemContainer.empty();

    currentPageStep = BANNER_STEP_TWO;
  }

  function setPageModeStepThree() {
    if (!checkStepTwoProfitsNegative()) {
      $('#step-two-error').hide();
      var BANNER_STEP_THREE = 3;

      hideAllStepElements();
      showStepThreeElements(BANNER_STEP_THREE < currentPageStep ? "left" : "right");
      setBannerStepActive(BANNER_STEP_THREE);
      setBannerStepTwoClickable();
      populatePageTwoItemsIntoPageThree();

      currentPageStep = BANNER_STEP_THREE;
    }
  }

  function populatePageTwoItemsIntoPageThree() {
    $.each(chosenItems, function () {
      var itemStepOneSelect = $('a[itemid=' + this + ']');
      var itemPrice = parseFloat($('#set-price-' + itemStepOneSelect.attr('itemid')).val()).toFixed(2);
      var itemImgSrc = $(itemStepOneSelect.find('.img-responsive')[0]).attr('src');

      var itemSquare = createStepThreeItemSquare(itemStepOneSelect.attr('itemname'), itemPrice, itemImgSrc);

      stepThreeItemContainer.append(itemSquare);
    });
  }

  function createStepThreeItemSquare(itemName, itemPrice, itemImgSrc) {
    var containingDiv =
        $('<div></div>')
            .addClass('marg')
            .addClass('col-md-4')
            .addClass('col-sm-6')
            .addClass('col-xs-6');

    var aImgContainer =
        $('<a></a>')
            .addClass('store-item');

    var itemImg =
        $('<img>')
            .addClass('img-responsive')
            .attr('src', itemImgSrc);

    var header =
        $('<h4></h4>');

    var nameSpan =
        $('<span></span>')
            .addClass('hidemob')
            .addClass('limit-long-text')
            .html(itemName);

    var priceSpan =
        $('<span></span>')
            .html('$' + itemPrice);

    var mobileDetailsButton =
        $('<a></a>')
            .addClass('deets')
            .addClass('hidedesk')
            .html("DETAILS");

    header
        .append(nameSpan)
        .append(priceSpan);

    aImgContainer
        .append(itemImg)
        .append(header);

    containingDiv
        .append(aImgContainer)
        .append(mobileDetailsButton);

    return containingDiv;
  }

  function attachPriceCalculationRowListeners(container) {
    var containerInputElement = container.find('input:first');

    container.on('click', function () {
      //Remove all other selected items
      $('.selected').removeClass('selected');

      //Select the current container and focus on the input
      container
          .addClass('selected');
      containerInputElement.focus();
    });

    containerInputElement.on('focus', function () {
      $('.selected').removeClass('selected');
      container.addClass('selected');
    });
  }

  function attachPriceInputCalculation(errorDiv, priceInput, firstDisplaySpan, secondDisplaySpan) {
    priceInput.on('keyup mouseup', function () {
      var displayedProfit = parseFloat(priceInput.val() - priceInput.attr('min'));

      if (displayedProfit < 0) {
        errorDiv.show(200);
        $('#next-button-step-2').html('Please Fix Errors');
      } else {
        errorDiv.hide(200);

        if (!checkStepTwoProfitsNegative()) {
          $('#next-button-step-2').html('Next');
        }
      }

      firstDisplaySpan.html(displayedProfit.toFixed(2));
      secondDisplaySpan.html(displayedProfit.toFixed(2));

      refreshSalesGoalBounds();
      recalculateAndSetSalesGoals();
    });
  }

  function refreshSalesGoalBounds() {
    var profitLineItems = $('.profitPerSale');
    var highestItemPrice = 0.00;
    var lowestItemPrice = 9999999.00;

    $.each(profitLineItems, function () {
      var thisItemPrice = parseFloat($(this).html());
      if (thisItemPrice < lowestItemPrice) {
        lowestItemPrice = thisItemPrice < 0 ? 0 : thisItemPrice;
      }

      if (thisItemPrice > highestItemPrice) {
        highestItemPrice = thisItemPrice;
      }
    });

    stepTwoHighestItemPrice = highestItemPrice;
    stepTwoLowestItemPrice = lowestItemPrice;
  }

  function recalculateAndSetSalesGoals() {
    var multiplier = $('#num-of-units').val();
    var minVal = stepTwoLowestItemPrice * multiplier;
    var maxVal = stepTwoHighestItemPrice * multiplier;

    $('#earn-range').html('$' + minVal.toFixed(2) + ' - $' + maxVal.toFixed(2) + '+');
  }

  function setBannerStepOneClickable() {
    $('#banner-step-1')
        .off('click')
        .on('click', setPageModeStepOne)
        .addClass('hover-pointer');
  }

  function clearBannerStepOneClickable() {
    $('#banner-step-1')
        .off('click')
        .removeClass('hover-pointer');
  }

  function setBannerStepTwoClickable() {
    $('#banner-step-2')
        .off('click')
        .on('click', setPageModeStepTwo)
        .addClass('hover-pointer');
  }

  function clearBannerStepTwoClickable() {
    $('#banner-step-2')
        .off('click')
        .removeClass('hover-pointer');
  }

  function setupStepThreeInputs() {

    var inputStoreProfileImg = $('#store-profile-img');
    var storeProfileImgContainer = $('#store-profile-img-container');
    var inputStoreName = $('#input-store-name');
    var inputStoreDescription = $('#input-store-description');
    var inputUrl = $('#input-url');

    //Image input auto-population
    inputStoreProfileImg.on('change', function () {
      imageInputHelper
          .getRawSource('store-profile-img')
          .then(function (result) {
            $('#user-profile-img')
                .attr('src', result.result);
            storeProfileImgContainer
                .css('background-image', 'url(' + result.result + ')')
                .css('background-size', '102px 102px');

            $('#upload-picture-error').hide(200);
            if (!validateStepThreeErrors()) {
              $('#finish').html('Launch');
            }
          });
    });

    inputStoreName.on('keyup', function () {
      var storeNameDisplay = $('#display-store-name');
      if (inputStoreName.val().length > 0) {
        storeNameDisplay.html(inputStoreName.val());

        $('#store-name-error').hide(200);
        if (!validateStepThreeErrors()) {
          $('#finish').html('Launch');
        }
      } else {
        storeNameDisplay.html('Your Store Name');
      }

      //Update the counter
      $('#name-input-length-counter').html(inputStoreName.val().length + '/' + inputStoreName.attr('maxlength'));
    });

    //Initialize the counter
    $('#name-input-length-counter').html(inputStoreName.val().length + '/' + inputStoreName.attr('maxlength'));

    inputStoreDescription.on('keyup', function () {
      var storeDescriptionDisplay = $('#store-display-description');

      if (inputStoreDescription.val().length > 0) {
        storeDescriptionDisplay.html(inputStoreDescription.val().replaceAll('\n', '<br>'));

        $('#store-description-error').hide(200);
        if (!validateStepThreeErrors()) {
          $('#finish').html('Launch');
        }
      } else {
        storeDescriptionDisplay.html('A little something about <br> yourself goes here.');
      }

      //Carriage Returns+Line Feeds only count for 1 character in JS, but two characters for the text input element limit.
      //Account for this.
      var crlfCount = 0;
      for (var i = 0; i < inputStoreDescription.val().length; ++i) {
        if (inputStoreDescription.val()[i] == '\n') {
          crlfCount++;
        }
      }

      $('#description-input-length-counter').html((inputStoreDescription.val().length + crlfCount) + '/' + inputStoreDescription.attr('maxlength'));
    });

    $('#description-input-length-counter').html(inputStoreDescription.val().length + '/' + inputStoreDescription.attr('maxlength'));


    inputUrl.on('keyup', function () {
      clearTimeout(checkUrlTimeoutId);
      checkUrlTimeoutId = null;

      if (containsInvalidCharacters(inputUrl.val())) {
        showUrlInputMessage(INVALID_CHARACTER_MESSAGE, URL_MESSAGE_TYPE_ERROR);

      } else if (inputUrl.val().length > 0) {
        showUrlInputSpinner();
        checkUrlTimeoutId = setTimeout(checkUrlAlreadyUsed, CHECK_URL_TIMEOUT);
        $('#store-url-error').hide(200);
        if (!validateStepThreeErrors()) {
          $('#finish').html('Launch');
        }
      } else {
        hideUrlMessageAndSpinner();
      }
    });

    $('#finish').on('click', function() {
      if (authHelper.getIsAuthenticated()) {
        validateStepThreeInputsAndUploadItems();
      } else {
        triggerAuthModal();
        $('#litab').click();
      }
    });
  }

  function containsInvalidCharacters(stringToCheck) {
    return stringToCheck.search(/[~`%@!$#^&*)(:;/\\|<>"'\[\]\+, {}=?]+/g, "") > -1;
  }

  function checkUrlAlreadyUsed() {
    var url = $('#input-url').val();
    var dataObject = ajaxHelper.createOrAppendGetDataObject("url", url);

    ajaxHelper.performGetRequest(dataObject, "/readytowear/ajax/storeurlexists", getCheckUrlSuccessCallback(), getCheckUrlFailureCallback(), url);
  }

  function getCheckUrlSuccessCallback() {
    return function (data) {
      var urlMessageInput = $('#input-url');
      var urlMessageDisplay = $('#url-message');

      //Case where the data that was being checked is not the same as what's currently in the input.
      //Reset everything and send off the request again.
      if (data.checkedValue != urlMessageInput.val()) {
        showUrlInputSpinner();

        if (checkUrlTimeoutId != null) {
          clearTimeout(checkUrlTimeoutId);
          checkUrlTimeoutId = null;
        }

        setTimeout(checkUrlAlreadyUsed, CHECK_URL_TIMEOUT);
      }
      else if (data.valid) {
        if (data.exists != null && !data.exists) {
          showUrlInputMessage(URL_AVAILABLE_MESSAGE, URL_MESSAGE_TYPE_SUCCESS);
        }
        else {
          showUrlInputMessage(URL_ALREADY_TAKEN_MESSAGE, URL_MESSAGE_TYPE_ERROR);
        }
      } else {
        showUrlInputMessage(INVALID_CHARACTER_MESSAGE, URL_MESSAGE_TYPE_ERROR);
      }
    }
  }

  function getCheckUrlFailureCallback() {
    return function (data) {
      var urlMessageDisplay = $('#url-message');
      urlMessageDisplay
          .html(URL_ALREADY_TAKEN_MESSAGE)
          .show(200)
          .removeClass('error')
          .addClass('success');
    }
  }


  function showUrlInputMessage(message, type) {
    var urlMessageDisplay = $('#url-message');
    var displayType = type == null ? URL_MESSAGE_TYPE_ERROR : type;

    $('#url-input-spinner').hide(200);

    urlMessageDisplay
        .html(message)
        .show(200);

    if (displayType == URL_MESSAGE_TYPE_SUCCESS) {
      urlMessageDisplay
          .addClass('success')
          .removeClass('error');
    } else {
      urlMessageDisplay
          .addClass('error')
          .removeClass('success');
    }
  }

  function showUrlInputSpinner() {
    $('#url-message').hide(200);
    $('#url-input-spinner').show(200);
  }

  function hideUrlMessageAndSpinner() {
    $('#url-input-spinner').hide(200);
    $('#url-message').hide(200);
  }

  function checkStepTwoProfitsNegative() {
    var rval = false;

    $.each(chosenItems, function () {
      var itemStepOneSelect = $('a[itemid=' + this + ']');
      var userSetItemPrice = parseFloat($('#set-price-' + itemStepOneSelect.attr('itemid')).val());
      var manufacturingCost = parseFloat(itemStepOneSelect.attr('itemprice'));

      if (userSetItemPrice - manufacturingCost < 0) {
        rval = userSetItemPrice - manufacturingCost < 0;
      }
    });

    return rval;
  }

  function exportStoreSettings() {
    var exportObject = {};
    exportObject.items = [];

    return imageInputHelper.getRawSource('store-profile-img')
        .then(function(result) {
          $.each(chosenItems, function() {
            var item = {};
            item.id = this;
            item.price = getItemPrice(this);
            exportObject.items.push(item);
          });

          exportObject.storeName = $('#input-store-name').val();
          exportObject.storeDescription = $('#input-store-description').val();
          exportObject.storeUrl = $('#input-url').val();
          exportObject.profileImg = result.result;

          return exportObject;
        });
  }

  function validateStepThreeInputsAndUploadItems() {
    var errors = validateAndDisplayStepThreeErrors();
    var isUrlValid = $('#url-message').is(':visible') && $('#url-message').html() == URL_AVAILABLE_MESSAGE;

    if (!errors && isUrlValid) {
      hideAllStepElements();
      $('.spinstuff').toggleClass('show');
      exportStoreSettings().then(function(result) {
        var dataObject = ajaxHelper.createOrAppendPostDataObject('storesettings', JSON.stringify(result));
        ajaxHelper.performPostRequest(dataObject, '/readytowear/ajax/createstore', getCreateStoreSuccessCallback(), getCreateStoreFailureCallback());
      });
    } else {
      $('#finish').html('Please Fix Errors');
    }
  }

  function getCreateStoreSuccessCallback() {
    return function(data) {
      $('.spinstuff').toggleClass('show');
      showStepThreeElements();
      window.location = "/sellers/" + $('#input-url').val();
    }
  }

  function getCreateStoreFailureCallback() {
    return function(data) {
      $('.spinstuff').toggleClass('show');
      showStepThreeElements();
      $('#finish').html("Try Again");
    }
  }

  function validateAndDisplayStepThreeErrors() {
    var errors = false;

    if ($('#store-profile-img').val().length == 0) {
      errors = true;
      $('#upload-picture-error').show(200);
    } else {
      $('#upload-picture-error').hide(200);
    }

    if ($('#input-store-name').val().length == 0) {
      errors = true;
      $('#store-name-error').show(200);
    } else {
      $('#store-name-error').hide(200);
    }

    if ($('#input-store-description').val().length == 0) {
      errors = true;
      $('#store-description-error').show(200);
    } else {
      $('#store-description-error').hide(200);
    }

    if ($('#input-url').val().length == 0) {
      errors = true;
      $('#store-url-error').show(200);
    } else {
      $('#store-url-error').hide(200);
    }

    if (errors) {
      $('#finish').html('Please Fix Errors');
    } else {
      $('#finish').html('Launch');
    }

    return errors;
  }

  function validateStepThreeErrors() {
    var errors;

    if ($('#store-profile-img').val().length == 0) {
      errors = true;
    }

    if ($('#input-store-name').val().length == 0) {
      errors = true;
    }

    if ($('#input-store-description').val().length == 0) {
      errors = true;
    }

    if ($('#input-url').val().length == 0) {
      errors = true;
    }

    return errors;
  }

  function getItemPrice(id) {
    return $('#set-price-' + id).val();
  }

</script>
</body>
</html>
