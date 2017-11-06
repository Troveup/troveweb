<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="trove" uri="http://troveup.com/jsp/tlds/itemimage" %>
<html>
<head>
  <title>Trove: 3D Printed Jewelry Customized for You</title>
  <c:import url="../fragments/headers/commonHead.jsp"/>
  <script src="/resources/js/vendor/handlebars.js"></script>
  <script type="text/javascript" src="/resources/js/vendor/head.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.13.1/jquery.validate.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.13.1/additional-methods.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="/resources/stylesheets/main.css">
  <link rel="stylesheet" href="/resources/stylesheets/browse.css">
  <link rel="stylesheet" href="/resources/stylesheets/cards.css">
  <link rel="stylesheet" href="/resources/stylesheets/spinner.css">
  <script type="text/javascript" src="/resources/js/namespace.js"></script>
  <script type="text/javascript" src="/resources/js/TROVE.js"></script>
  <c:import url="../fragments/analytics/all.jsp"/>
  <style>
    .topnav a {
      border-top: 3px solid #ffffff;
    }
    .topnav a.current-page {
      color: #f26868;
      border-top: 3px solid #f26868;
    }
    .profile-container {
      height: 300px;
    }
    .profile-pic-holder {
      left: 50%;
      position: absolute;
      height: 100px;
      width: 100px;
      border: 2px solid #fff;
      border-radius: 50%;
      overflow: hidden;

    }
    .card_avatar_small {
      margin-right: 10px;
    }
    .btn--edit--profile {
      margin-top: 20px;
    }
    .follow-pic-holder {
      width: 100px;
      height: 100px;
      border-radius: 50%;
      overflow: hidden;
      margin: 30px 74px;
    }  
    body {
      min-height: 1000px;
    }

    #folwbuttonprofile{
      width: 108px;
      margin: 30px 18px 10px 18px;
      border-radius: 2px;
    }
    #folwbutton {
      width: 108px;
      margin: 10px 18px 10px 18px;
      border-radius: 2px;
    }
    #ufolwbutton {
      width: 108px;
      margin: 10px 18px 10px 18px;
      border-radius: 2px;
    }
    
    .card_darken.person {
      height: 194px;
    }
    .card_username {
      display: block;
      font-size: 16px;
      padding-top: 7px;
      margin-bottom: 3px;
    }
    .card_avatar_small {
      margin-right: 10px;
    }
    .card_username {
      display: block;
      font-size: 16px;
      padding-top: 7px;
      margin-bottom: 3px;
    }
    .overlaid {
      position: relative;
      background-color: rgba(242, 241, 242, 0.8);
      pointer-events: none;
      z-index: 6;
      opacity: 0.0;
      -webkit-transition: all 0.5s ease-in-out;
      -moz-transition: all 0.5s ease-in-out;
      -ms-transition: all 0.5s ease-in-out;
      -o-transition: all 0.5s ease-in-out;
      transition: all 0.5s ease-in-out;
    }
    .overlaid .message {
      margin-top: 0;
      margin-left: 0;
      left: 0;
      position: absolute;
      text-align: center;
      padding: 100px 20px;
      border-radius: 2px;
      color: #797979;
      font-size: 18px;
      width: 250px;
      height: 356px;
      background-color: #DEDEDE;
      &.action {
        z-index: 100;
      }
    }
    .btn--small {
      padding: 5px 12px;
    }
    .overlaid .message img {
      opacity: 0.2;
      width: 44px;
    }
    .overlaid .message h2 {
      color: #797979;
      font-size: 23px;
      margin-bottom: 40px;
      line-height: .7em;
    }
    .hideitnow {
      opacity: 0.2;
    }
    .grid-cta {
      z-index: 4;
    }
    .tallyho-profile, .tallyho-cart {
      opacity: 0.0;
      position: absolute;
      z-index: 9999999;
      right: 6px;
      top: -4px;
      background-color: #f26868;
      color: #FFF;
      padding: 0px 5px 0px 6px;
      height: 18px;
      border-radius: 9px;
      font-size: 75%;
      font-weight: 500;
      border: 1px solid #FFF;
    }
    .tallyho-profile.hideitlater, .tallyho-cart.hideitlater {
      opacity: 1.0;
      display: block !important;
    }
    .tallyho-profile.showitnow, .tallyho-cart.showitnow {
      opacity: 1.0;
      display: block !important;
    }
    .buyModal.in ~ .modal-backdrop.in { 
      background-color: #333;   
      opacity: 0.5;
      filter: alpha(opacity=50); 
    }

    .modal-content {
      border: 1px solid #dedede;
      border-radius: 2px;
    }
    .modal-header {
      box-shadow: none;
      -webkit-box-shadow: none;
      margin: 0 0 30px;
      border-bottom: 1px solid #dedede;
    }

    .modal-body {
      padding-bottom: 30px;
      padding-top: 30px;
      text-align: center;
      border-bottom: 1px solid #dedede;
    }
    .material-selector-container{
      margin:0 10px 20px;
    }

    #ufolwbutton.btn--edit--profile {
      margin-top: 15px !important;
    }
    #folwbutton.btn--edit--profile {
      margin-top: 15px !important;
    }
    .card_user_info {
      padding-left: 10px;
    }

    .btn--edit--profile {
      margin-top: 15px;
      margin-right: 15px;
    }

    @media (min-width: 768px) {
      .btn--edit--profile {
        margin-top: 15px;
        margin-right: 15px;
      }
      #covbutton {
        margin-top: 15px;
      }
      .modal-dialog {
        width: 510px;
        margin: 30px auto;
      }
    }

    @media (max-width: 480px){
      .material-selector-container{
      max-width:90%;
      }

      .user-info-count.category{
        font-size:10px;
      }
    }

    .modal-body.photo {
      padding-bottom: 0px;
    }
    .containers {
        position: relative;
        top: 10%; left: 50%; right: 0; bottom: 0;
        margin-left: -200px;
    }
    .action {
        width: 400px;
        height: 30px;
        margin: 5px 0;
    }
    .cropped>img {
        margin-right: 10px;
        border-radius: 50%;
    }
    form.form-horizontal {
      margin-bottom: 20px;
      margin-top: 30px;
    }
    .form-horizontal .form-group {
      margin-right: -15px;
      margin-left: -15px;          padding: 10px;
    }
    input.form-control {
      border-radius: 0px;
    }
    input:-webkit-autofill {
        -webkit-box-shadow: 0 0 0px 1000px white inset;
    }
    input:focus:-webkit-autofill {
        -webkit-box-shadow: 0 0 0px 1000px white inset;
    }
    #profbutton {
        display: inline-block;
        padding: 6px 15px;
        border-radius: 2px;
        background-color: #8e8b8b;
        color: #FFF;
    }
    #profbutton.hover{
      background-color: #b7b0b0;
    }
    .profpic {
      float: left;
      width: 100px;
      height: 100px;
      border-radius: 50%;
      margin-top: -30px;
      overflow: hidden;
    }
    .profpicholder{
      height: 100px;
      width: 100px;
      position: relative;
      margin: 0 auto;
    }

    .account-settings-header.checkout {
      margin: 0px -15px 40px;
    }
    .profpic img { 
      width: 100px;
      height: 100px;
    }

    .imageBox {
      position: relative;
      height: 340px;
      width: 100%;
      border: 1px solid #aaa;
      background: #fff;
      overflow: hidden;
      background-repeat: no-repeat;
      cursor: move;
    }

    .imageBox .thumbBox {
      position: absolute;
      top: 0;
      left: 0;
      width: 101%;
      height: 309px;
      margin-top: 30px;
      margin-left: -1px;
      margin-right: -1px;
      border: 1px solid rgb(102, 102, 102);
      border-radius: 0px;
      box-shadow: 0 0 0 1000px rgba(0, 0, 0, 0.5);
      background: none repeat scroll 0% 0% transparent;
    }

    .imageBox .spinner {
        position: absolute;
        top: 0;
        left: 0;
        bottom: 0;
        right: 0;
        text-align: center;
        line-height: 400px;
        background: rgba(0,0,0,0.7);
    }

    .containers {
      position: fixed;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      margin-left: 0px;
      height: 200px;
    }

    .action {
      display: block;
      width: 260px;
      height: 30px;
      float: right;
      /*margin: 16px 246px 10px 19px;*/
    }

    .modal-body.photo {
      padding-bottom: 0px;
      min-height: 335px;
    }

    #btnZoomIn, #btnZoomOut {
      float: left;
      background-color: #B7B0B0;
      border: 0px solid transparent;
      border-radius: 2px;
      font-size: 18px;
      padding: 1px 8px;
      font-weight: bold; 
      font-family: helvetica;
      color: #FFF;
      margin-right: 3px;
      margin-top: -3px;
    }

    #btnZoomOut {
      padding: 1px 11px;
    }

    label.myLabel input[type="file"] {
      position: fixed;
      top: -1000px;
    }

    /***** Example custom styling *****/
    .myLabel {
      border: 1px solid #F26868;
      border-radius: 2px;
      padding: 4px 16px;
      margin: -5px 0px 0px -10px;
      background: #F26868;
      display: inline-block;
    }

    .myLabel :valid {
      background-color: #b7b0b0;
    }

    .myLabel + span {
      color: #666;
    }

    .myLabel:hover {
      cursor: pointer;
      background: #DD2435;
    }

    .btn.btn--darkgray {
      background-color: rgba(0, 0, 0, 0.7);
      color: #fff;
    }
    .btn.btn--darkgray:hover {
      background-color: rgba(0, 0, 0, 0.8);
      color: #fff;
    }

    .myLabel:active {
    }
    .myLabel :invalid + span {
      color: #F26868;
    }
    .myLabel :valid + span {
      color: #FFF;
    }

    #covimg {
      width: 100%;
      position: absolute;
      z-index: -100;
      margin-top: 0px;
    }

    .profile-container {
      height: 420px;
    }

    .cover-photo-container {
      width: 100%;
      height: 175px;
    }

    @media (min-width: 768px) {
      .modal-dialog.coverphoto {
          width: 100%;
          margin: 50px auto;
      }
    }

    .card_content_a.collect {
      border-bottom: 0px solid #f0f0f0;
      text-align: center;
    }
    .card_product_name.collect {
      font-size: 20px;
      padding: 11px 0 0 4px;
    }

    .overlaid.collectionlaid {
      pointer-events: all;
      position: relative;
      background-color: rgba(255, 255, 255, 1.0);
      z-index: 6;
      opacity: 1.0;
      -webkit-transition: all 0.5s ease-in-out;
      -moz-transition: all 0.5s ease-in-out;
      -ms-transition: all 0.5s ease-in-out;
      -o-transition: all 0.5s ease-in-out;
      transition: all 0.5s ease-in-out;
    }    

    .overlaid.collectionlaid .message {
      cursor: pointer;
      margin-top: 1px;
      margin-left: 0;
      left: 1px;
      position: absolute;
      text-align: center;
      padding: 130px 20px;
      border-radius: 2px;
      color: #797979;
      font-size: 18px;
      width: 247px;
      height: 395px;
      background-color: #FFFFFF;
      -webkit-transition: all 0.5s ease-in-out;
      -moz-transition: all 0.5s ease-in-out;
      -ms-transition: all 0.5s ease-in-out;
      -o-transition: all 0.5s ease-in-out;
      transition: all 0.5s ease-in-out;
    }

    .overlaid.collectionlaid .message img {
      opacity: 0.4;
      width: 44px;
      -webkit-transition: all 0.5s ease-in-out;
      -moz-transition: all 0.5s ease-in-out;
      -ms-transition: all 0.5s ease-in-out;
      -o-transition: all 0.5s ease-in-out;
      transition: all 0.5s ease-in-out;
    }

    .overlaid.collectionlaid .message:hover img {
      opacity: 0.6;
      width: 44px;
    }

    .overlaid.collectionlaid .message h2 {
      color: #797979;
      font-size: 22px;
      margin-bottom: 40px;
      line-height: .8em;
      -webkit-transition: all 0.5s ease-in-out;
      -moz-transition: all 0.5s ease-in-out;
      -ms-transition: all 0.5s ease-in-out;
      -o-transition: all 0.5s ease-in-out;
      transition: all 0.5s ease-in-out;
    }

    .overlaid.collectionlaid .message:hover h2 {
      color: #444444;
      font-size: 22px;
      margin-bottom: 40px;
      line-height: .8em;
    }

    .card_column_single {
      height: 400px;
    }

    .collection_card_content_row1.collect {
      padding: 5px 0px 0px;
      height: auto;
      margin-top: 10px;
    }

    .collection_card_content_row1.collect .ownerimgholder.collect {
      display: block;
      margin-top: 10px;
      padding: 10px;
      text-align: center;
      margin-bottom: 2px;
    }

    .collection_card_content_row2.collect {
      padding: 0px 0px 10px;
      height: auto;
      margin-top: 10px;
    }

    .collection_card_content_row2.collect .ownerimgholder.collect {
      display: block;
      margin-top: 10px;
      padding: 10px;
      text-align: center;
      margin-bottom: 20px;
    }

    .card_darken.person.collect {
      height: 330px;
      z-index: 500;
    }

    .card_darken.person.collect.first {
      height: 400px;
      z-index: 50000;
    }

    .ownerimg.collect {
      width: 68.5px;
      height: 68.5px;
    }

    .card_content_a.collect {
      border-bottom: 0px solid #f0f0f0;
      text-align: center;
      background-color: #FFF !important;
      margin-top: 17px;
      padding: 5px;
    }

    a.darken {
      display: inline-block;
      background: black;
      background-color: rgba(0, 0, 0, 0);
      -webkit-transition: all 0.5s linear;
           -moz-transition: all 0.5s linear;
            -ms-transition: all 0.5s linear;
             -o-transition: all 0.5s linear;
                transition: all 0.5s linear;
    }

    a.darken img {
        display: inline-block;
        -webkit-transition: all 0.5s linear;
           -moz-transition: all 0.5s linear;
            -ms-transition: all 0.5s linear;
             -o-transition: all 0.5s linear;
                transition: all 0.5s linear;
    }

    a.darken:hover {
      background-color: rgba(0, 0, 0, 0.1);
    }

    a.darken:hover img {
        opacity: 0.95;
    }

    .btn--small {
      padding: 6px 10px;
    }

    .user_card_social_count.collect {
      pointer-events: none;
    }

    .modal-backdrop {
      background-color: rgba(106, 106, 106, 0.68);
    }

    .inline-icon-edit-sm {
      width: 11px;
      height: 11px;
      margin-right: 7px;
    }

    * {
      font-family: "Raleway", Helvetica, Arial, sans-serif;
      letter-spacing: 1px;
      font-weight: 300;
      color: #232323;
      outline: 0;
    }
    .card {
      position: absolute;
      -webkit-transition: all .3s ease-in-out;
         -moz-transition: all .3s ease-in-out;
           -o-transition: all .3s ease-in-out;
          -ms-transition: all .3s ease-in-out;
              transition: all .3s ease-in-out;
    }
    .card-content.collections {
      position: relative;
      border-radius: 2px;
      float: left;
      overflow: hidden;
      padding: 10px 15px 10px;
      margin-bottom: 20px;
      width: 250px;
      height: 360px;
      background: #FFF;
      border: 1px solid #DEDEDE;
      -webkit-box-shadow: 0px 2px 10px -2px rgba(0, 0, 0, 0.1);
         -moz-box-shadow: 0px 2px 10px -2px rgba(0, 0, 0, 0.1);
              box-shadow: 0px 2px 10px -2px rgba(0, 0, 0, 0.1);
    }
    .collection_name {
      display: block;
      text-align: center;
      font-size: 20px;
      padding: 10px 0;
      text-transform: capitalize;
      margin-top: 10px;
      white-space: nowrap;
      text-overflow: ellipsis;
      overflow: hidden;
    }
    .social_counts {
      font-size: 1em;
      padding-left: 15px;
      padding-bottom: 10px;
      padding-top: 5px;
      opacity: .5;
      text-align: left;
      pointer-events: none;
    }
    .social_counts span {
      padding: 0px 4px;
    }
    .icon-small {
      width: 20px;
      height: 10px;
      opacity: .6;
    }
    .overlay.collections {
      display: block;
      position: absolute;
      top: 0;
      left: 0;
      width: auto;
      height: auto;
      opacity: 1;
      cursor: pointer;
      -webkit-transition: opacity .2s ease-out 0s;
         -moz-transition: opacity .2s ease-out 0s;
              transition: opacity .2s ease-out 0s;
    }
    .card_darken.collections {
      position: absolute;
      width: 250px;
      height: 270px;
      background: #000;
      opacity: 0;
      -webkit-transition: opacity .2s ease-out 0s;
         -moz-transition: opacity .2s ease-out 0s;
              transition: opacity .2s ease-out 0s;
    }
    .card-content.collections#collection-new {
      text-align: center;
    }
    .card-content.collections#collection-new .card_darken {
      height: 335px;
    }
    .card-content#collection-new img {
      margin-top: 110px;
      height: 30px;
      width: 30px;
      opacity: 0.4;
      pointer-events: none;
    }
    .card-content#collection-new .collection_title {
      font-size: 18px;
      display: block;
      text-align: center;
      padding: 10px 0;
      text-transform: capitalize;
      margin-top: 10px;
    }
    .overlay:hover .card_darken {
      opacity: .05;
    }
    .btn {
      font-size: 14px;
      font-weight: normal;
      cursor: pointer;
      background-color: #8e8b8b;
      color: #fff;
      padding: 5px 20px;
      line-height: 1.4em;
      box-sizing: border-box;
      border: 0px solid transparent;
      text-decoration: none;
      border-radius: 2px;
      text-align: center;
      white-space: nowrap;
      vertical-align: middle;
      opacity: 1;
      -webkit-transition: all 0.30s ease-in-out;
         -moz-transition: all 0.30s ease-in-out;
          -ms-transition: all 0.30s ease-in-out;
           -o-transition: all 0.30s ease-in-out;
    }

    .btn.btn--small {
      padding: 6px 10px;
    }
    .btn:hover, .btn:focus, .btn.focus {
      text-decoration: none;
      background-color: #b7b0b0;
      color: #fff;
      outline: 0;
    }
    .collection_item_photos {
      height: 200px;
      text-align: center;
      line-height: 80px;
      padding-top: 15px;
      color: #C6C6C6;
    }
    .card_actions {
      text-align: center;
    }
    .btn.btn_unfollow {
      width: 160px;
      background-color: #B7B0B0;
    }
    .btn.btn_unfollow:hover, .btn.btn_follow:focus, .btn.btn_follow.focus {
      background-color: #8E8B8B;
    }
    .btn.btn_save {
      background-color: #DD2435;
      float: right;
    }
    .btn.btn_follow {
      width: 160px;
      background-color: #DD2435;
    }
    .btn.btn--red {
      background-color: #DD2435;
    }
    .btn.btn_follow:hover, .btn.btn_follow:focus, .btn.btn_follow.focus, .btn.btn_save:hover, .btn.btn_save:focus, .btn.btn_save.focus {
      background-color: #F26868;
    }

    .btn.btn--red:hover, .btn.btn--red:focus, .btn.btn--red.focus {
      background-color: #F26868;
    }
    .collection_item_photos img {
      width: 66px;
      height: 66px;
    }
    label {
      margin-top: 20px;
    }
    fieldset {
      padding-bottom: 30px;
    }
    textarea {
      resize: vertical;
    }

    .ownerimg {
      width: 30%;
      height: 70px;
    }
    .ownerimgholder {
      display: block;
      margin-top: 25px;
      height: 84px;
      max-height: 84px;
      padding: 7px;
      text-align: center;
      margin-bottom: 13px;
      overflow: hidden;
    }

    #folwbuttonprofile {
      width: 108px;
      margin: 15px 18px 10px 18px;
    }

    .modal-body {
      padding-bottom: 30px;
      padding-top: 0px;
      text-align: center;
    }

    .removeButton {
      width: 87%;
      border: 2px solid #D99393;
      color: #D99393;
      background-color: #FFF;
    }

    .collection_item_photos {
      height: 200px;
      max-height: 200px;
      overflow: hidden;
      text-align: center;
      line-height: 85px;
      padding-top: 15px;
      color: #C6C6C6;
    }

    .card_username {
      display: block;
      font-size: 16px;
      padding-top: 0px;
      margin-bottom: 0px;
      text-transform: capitalize;
      white-space: nowrap;
    }

    .card_collection_name {
      display: block;
      font-size: .9em;
      font-weight: 400;
      margin-bottom: 3px;
      white-space: nowrap;
      text-transform: capitalize;
    }

    @media (max-width: 768px) {
      .grid-cta {
        opacity: 1.0;
      }
    }

     .input-error-text {
      display: block;
      margin-top: 5px;
      margin-bottom: 10px;
      color: #F26868;
      letter-spacing: 0.05em;
      font-size: 15px;
      font-weight: 300;
    }

    .chain-container {
      padding-bottom: 30px;
    }

    .special-container {
      display: none;
    }

    .btn.btn--small {
      padding: 6px 25px;
    }

    .cardbuybutton {
      display: none;
    }

  </style>

</head>
<body>
  <c:import url="../fragments/nav/topNavBar.jsp"/>
  <c:import url="../fragments/profileTop.jsp"/>
  <script>TROVE.setToken("${_csrf.token}");</script>

  <div class="container">

    <script id="collectionform" type="text/x-handlebars">
      {{#each this}}
        <option value="{{collectionId}}">{{collectionName}}</option>
      {{/each}}
    </script>
    
    <!-- row for items -->
    <div class="row items">
    </div>

    <!--

    <div class="card" id="card-{{itemId}}" style="opacity:0;">
          <div id="trovedAlert-{{itemId}}" class="overlaid"><div class="message"><h2>Saved to<br></br>Your Trove</h2><img class="add_image" src="/resources/img/plus.png"></div></div>
          <div id="buyAlert-{{itemId}}" class="overlaid"><div class="message"><h2>Added to<br></br>Your Bag</h2><img class="add_image" src="/resources/img/plus.png"></div></div>
          <div class="card-content" id="item-{{itemId}}">
            <div class="overlay">
              <a href="/public/productdescription/{{itemId}}"><div class="card_darken"></div></a>
              <div class="card_content_img"><img class="card_image" src="{{images.0.largeImageUrlPath}}"></div>
              <div class="grid-cta">
                <button id="troveItem-{{itemId}}" onclick="TROVE.troveItem({{itemId}})" class="btn btn_trove">Trove</button>
                <a href="/public/customize/webgl/{{itemId}}" alt="Customize this design"><button id="customizeItem-{{itemId}}" class="btn btn_customize">Customize</button></a>
                <button id="buyItem-{{itemId}}" data-toggle="modal" data-target="#modalbuyItem-{{itemId}}" class="btn btn_buy cardbuybutton">Buy</button>
              </div>
            </div>
            <div class="modal fade modal-{{itemId}}" id="modalbuyItem-{{itemId}}" tabindex="-1" role="dialog" aria-labelledby="buyModalLabel" aria-hidden="true">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-header"><h4 class="modal-title" id="buyModalLabel">Buy Item</h4></div>
                  <div class="modal-body">
                    <div class="addtocartfromcardstuff-{{itemId}}">
                      <div class="material-selector-container">
                        <span class="selector-title">Material</span>
                        <div class="material-selector">
                          <select name="dismenu" id="dismenu-{{itemId}}" onchange="updateMaterial({{itemId}});">
                            <c:if test="${not empty materials}">
                              <c:forEach var="material" items="${materials}">
                                <c:forEach var="finish" items="${material.finishList}">
                                  <option value="${material.materialId} ${finish.finishId}">${material.name} - ${finish.name}</option>
                                </c:forEach>
                              </c:forEach>
                            </c:if>
                          </select>
                        </div>
                      </div>
                      <div class="material-selector-container">
                        <span class="selector-title">Size</span>
                        <div class="material-selector">
                          <select name="sizee" id="sizee-{{itemId}}" onchange="updateSize({{itemId}});">
                            {{#each availableSizes}}
                              <option value="{{@index}}">{{this}}</option>
                            {{/each}}
                          </select>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn--lightGray" data-dismiss="modal" style="float: left;">Close</button>
                    <button id="addToCart" type="button"  data-dismiss="modal" onclick="TROVE.addFromCard({{itemId}})" class="btn btn--red addToCart">Add to Cart</button>
                  </div>
                </div>
              </div>
            </div>
            <div class="card_content_a">
              <div class="card_product_name"><a href="/public/productdescription/{{itemId}}">{{itemName}}</a></div>
            </div>
            <div class="card_content_b product-collection-name">
              <div class="card_user_info"><a href="/private/user/{{itemOwner.userId}}"><img src="{{itemOwner.profileImageThumbnailPath}}" class="card_avatar_small"><span class="card_username">{{itemOwner.username}}</span></a></div>
            </div>
            <div class="social_counts item">
              <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/trove-icon.svg" class="icon-small" title="Troves" alt="Troves"><span id="trovedCount-{{itemId}}">{{#if trovedCount}} {{trovedCount}} {{else}} 0 {{/if}}</span>
              <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/remix-icon.svg" class="icon-small" style="padding-left: 8px" title="Remixes" alt="Remixes"><span id="remadeCount-{{itemId}}">{{#if remadeCount}} {{remadeCount}} {{else}} 0 {{/if}}</span>
            </div>
          </div>
        </div>

        -->


        <!--

        <script type="text/javascript">
                                    var data = [
                                      { name: 'Recent Activity', value: 'recentActivity:true' },
                                      { name: '1 touch', value: 'touches:1' },
                                      { name: '2 touches', value: 'touches:2' },
                                      { name: '3 touches', value: 'touches:3' },
                                      { name: '4 touches', value: 'touches:4' },
                                      { name: '5+ touches', value: 'touches:5' },
                                      { name: 'Survey Only', value: 'surveyOnly:true' },
                                      { name: 'Access Only', value: 'accessOnly:true' },
                                      { name: 'Has a Year to Date (YTD) Evaluation', value: 'YTDEval:true' },
                                      { name: '1 Login', value: 'logins:1' },
                                      { name: '2+ Login', value: 'logins:2' },
                                      { name: 'No Exchange', value: 'noExchange:true' },
                                      { name: 'Unread Assignment', value: 'unreadAssignments:true' },
                                      { name: 'Verbal/Online PB', value: 'verbalOnLinePiggyBack:true' },
                                      { name: 'Projects', value: 'showProjects:true' },
                                      { name: 'Products Below Minimum', value: 'jIds:1' },
                                      { name: 'Products Below Minimum in 30 Days', value: 'isProductBelowMin30Days:true' },
                                      { name: 'Exchanging Providers', value: 'exchangingProviders:true' },
                                      { name: 'Any Assignments', value: 'hasAssignment:true' },
                                      { name: 'Without Assignments', value: 'hasAssignment:false' },
                                      { name: 'Updates', value: 'isUpdateable:true' },
                                      { name: 'Non-updates', value: 'isUpdateable:false' },
                                      { name: 'Target Commit', value: 'targetCommitments:true' },
                                      { name: 'Commits To Get', value: 'commitsToGet:true' },
                                      { name: 'PM Requests', value: 'pmRequests:true' },
                                      { name: 'Not already completed at IDN', value: 'uniqueAssigments:true' },
                                      { name: 'No PTU', value: 'showNoPtu:true' },
                                      { name: '3 Months Past PTU Date', value: 'show3MonthPastPtuDate:true' },
                                      { name: 'PTU Next 3 months', value: 'ptuNext3Months:true' },
                                      { name: 'Personal Priority', value: 'personalPriority:true' },
                                      { name: 'Project Priority: High', value: 'projectHighPriority:true' },
                                      { name: 'Product Priority: Critical', value: 'priorityIds:1' },
                                      { name: 'Product Priority: High', value: 'priorityIds:2' },
                                      { name: 'Product Priority: Medium', value: 'priorityIds:3' },
                                      { name: 'Product Priority: Low', value: 'priorityIds:4' },
                                      { name: 'Unassigned Contact (No KLAS Owner)', value: 'isUnassigned:true' },
                                      { name: 'Provider Status: Inactive - No Updated Survey', value: 'customerStatusIds:18' },
                                      { name: 'Provider Status: Non-Participant', value: 'customerStatusIds:25' },
                                      { name: 'Provider Status: Gone From Hospital', value: 'customerStatusIds:17' },
                                      { name: 'Provider Status: Unconfirmed', value: 'customerStatusIds:26' },
                                      { name: 'Provider Status: Do Not Contact', value: 'customerStatusIds:28' },
                                      { name: 'Survey Type: Software', value: 'productTypesIds:1' },
                                      { name: 'Survey Type: Medical Equipment (MIME)', value: 'productTypesIds:2' },
                                      { name: 'Survey Type: Professional Services (PSF)', value: 'productTypesIds:3' },
                                      { name: 'Survey Type: Infrastructure', value: 'productTypesIds:4' }
                                    ];

                                    var multiselect = $('#selectedFilters').kendoMultiSelect({
                                      dataSource    : data,
                                      dataTextField : 'name',
                                      dataValueField: 'value',
                                      filter        : 'contains',
                                      placeholder   : "No Collections Selected. (Click Here to Add)",
                                      delay         : 0,
                                      minLength     : 2,
                                      highlightFirst: true,
                                      ignoreCase    : true,
                                      change        : function (){
                                      // alert("selection changed");   
                                      },
                                    }).data("kendoMultiSelect");

                                    $("#magicbutton").on("click", function () {
                                      //multiselect.value([]);
                                      // var res =  [];
                                      var res =  multiselect.value().slice();
                                      res.push('customerStatusIds:25');
                                      res.push('priorityIds:1');
                                      res.push('productTypesIds:4');
                                      res.push('unreadAssignments:true');
                                      res.push('touches:1');
                                      res.push( 'recentActivity:true');
                                      res.push( 'ptuNext3Months:true');
                                      res.push( 'logins:2');
                                      res.push( 'showNoPtu:true' );
                                      multiselect.dataSource.filter({});
                                      multiselect.value(res) 
                                      // multiselect.value(res);
                                      //res.forEach(function (dataItem) {
                                      //var filters = filterMultiSelect.value();
                                      //var temp = $.merge($.merge([], filters), [dataItem]);
                                      //temp = $.unique(temp);
                                      //multiselect.value(temp);
                                    }); 
                                  </script>

                                  <div class="card_content_b"><div class="card_collection_name"><a href="/public/collections-inside">Collection Name</a></div></div> -->


    <script id="itemcards" type="text/x-handlebars">
      {{#each this}}
        {{#if isMobileCompatible}}
        <div class="block card_mask item item-{{itemId}}" id="troveitemcard-{{itemId}}">
        {{else}}
        <div class="block card_mask item item-{{itemId}} hidemobile" id="troveitemcard-{{itemId}}">
        {{/if}}
          <div id="updateAlert-{{itemId}}" class="overlaid"><div class="message"><h2>Updated<br></br>Your Item</h2><img class="add_image" src="/resources/img/plus.png"></div></div>
          <div id="trovedAlert-{{itemId}}" class="overlaid"><div class="message"><h2>Saved to<br></br>Your Trove</h2><img class="add_image" src="/resources/img/plus.png"></div></div>
          <div id="buyAlert-{{itemId}}" class="overlaid"><div class="message"><h2>Added to<br></br>Your Bag</h2><img class="add_image" src="/resources/img/plus.png"></div></div>
          <div class="card_column_single">
            <div class="overlay">
              <a href="/public/productdescription/{{itemId}}"><div class="card_darken" style="position: absolute"></div></a>
              <div class="card_content_img"><img class="card_image" src="{{defaultCardImageUrl}}"></div>
              <div class="grid-cta">
                <c:if test="${not empty canFollowUser}"><button id="troveItem-{{itemId}}" onclick="TROVE.troveItem({{itemId}},'userprofile')" class="btn btn--red btn--small btn--card--overlay">Trove</button></c:if>
                <c:if test="${empty canFollowUser}">
                  <button id="editItem-{{itemId}}" onclick="editb()" class="btn btn--lightgray btn--small btn--card--overlay" data-toggle="modal" data-target="#modal2-{{itemId}}"><img src="https://storage.googleapis.com/trove-demo-cdn/img/edit-icon-white.svg" class="inline-icon-edit-sm sm">Edit</button>
                </c:if>
                <a href="/public/customize/webgl/{{itemId}}" alt="Customize this design"><button onclick="custb()" class="btn btn--red btn--small btn--card--overlay">Customize</button></a>
                <button id="buyItem-{{itemId}}" onclick="buyb({{itemId}})" class="btn btn--lightgray btn--small btn--card--overlay cardbuybutton" data-toggle="modal" data-target="#modal-{{itemId}}">Buy</button>
              </div>
            </div>

            <%-- edit item model--%>
            <div class="modal fade modal2-{{itemId}}" id="modal2-{{itemId}}" tabindex="-1" role="dialog" aria-labelledby="buyModalLabel" aria-hidden="true">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-header"><h4 class="modal-title" id="buyModalLabel">Edit Item</h4></div>
                  <div class="modal-body">
                    <form class="edit-item form-horizontal" action='/' method='post'>
                      <div class="form names">
                        <fieldset>
                          <div class="row">
                            <div class="col-md-12">
                              <div class="editstuff">
                                <div id="error"></div>
                                {{#ifCond itemOwner.userId ${authUser.userId} }}
                                  <div class="form-group">
                                    <div class="col-xs-12">
                                      <span class="labyo">Item Name</span>
                                      <input type="text" id="itemName-{{itemId}}" maxlength="30" name="itemName" class="form-control" placeholder="{{itemName}}" />
                                      <%--Error Class --%>
                                      <div id="nameError-{{itemId}}" class="input-error-text" style="display:none"></div>
                                    </div>
                                  </div>
                                  <%--<div class="form-group">
                                    <div class="col-xs-12 colpadder col has-feedback">
                                      <span class="labyo">Item Description</span>
                                      <input type="textarea" name="itemDescription" row="3" id="itemDescriptioname-{{itemId}}" class="form-control" placeholder="{{itemDescription}}" />
                                    </div>
                                  </div>--%>
                                {{else}}
                                {{/ifCond}}
                                <div class="form-group">
                                  <div class="col-md-12 col has-feedback">
                                   <span class="labyo">Add this item to a Collection</span>
                                   <select class="form-control collectselect" name="collectionmenu-{{itemId}}" id="collectionmenu-{{itemId}}" onchange="addItemToCollection({{itemId}}, 'collectionmenu-{{itemId}}')">
                                      <option value="0">Select One</option>
                                   </select>
                                 </div>
                               </div>
                              </div>
                            </div>
                          </div>
                        </fieldset>
                      </div>
                    </form>
                    <button id="removeItem-{{itemId}}" class="btn btn--lightgray btn--small btn--card--overlay removeButton" data-toggle="modal" data-dismiss="modal" data-target="#removemodal-{{itemId}}" onclick="removeb()">Remove</button>
                  </div>
                  <div class="modal-footer" style="padding:20px 30px 20px;">
                    <button type="button" class="btn btn--lightGray" data-dismiss="modal" style="float: left;">Cancel</button>
                    <button id="editTroveItem" type="button" onclick="editTroveItem({{itemId}})" class="btn btn--red editItem" style="float:right;">Save</button>
                  </div>
                </div>
              </div>
            </div>


            <%-- remove item model--%>
            <div class="modal fade" id="removemodal-{{itemId}}" tabindex="-1" role="dialog" aria-labelledby="removeItem" aria-hidden="true">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-header"><h4 class="modal-title" id="removeItem">Remove {{itemName}}</h4></div>
                  <div class="modal-body">
                    <p class="warningtext">Are you sure you want to remove this item from your trove? </p>
                  </div>
                  <div class="modal-footer" style="padding:20px 30px 20px">
                    <button type="button" class="btn" data-dismiss="modal" style="float: left;">Cancel</button>
                    <button id="removeItemFromTrove-{{itemId}}" onclick="removeItemFromTrove({{itemId}})" data-dismiss="modal" type="button" class="btn btn_save">Remove</button>
                  </div>
                </div>
              </div>
            </div>

            <%-- Buy item model--%>
            <div class="modal fade modal-{{itemId}}" id="modal-{{itemId}}" tabindex="-1" role="dialog" aria-labelledby="buyModalLabel" aria-hidden="true">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-header"><h4 class="modal-title" id="buyModalLabel">Buy Item</h4></div>
                  <div class="modal-body">
                  <div class="card_product_name" style="width:200px;margin:0 auto 20px;"><span style="font-weight:400;">{{itemName}}</span></div>
                  <img class="card_image" src="{{defaultCardImageUrl}}" style="margin:0 auto;">
                    <div class="addtocartfromcardstuff-{{itemId}}">
                      <div class="material-selector-container">
                        <span class="selector-title">Material</span>
                        <div class="material-selector">
                          <select name="dismenu" id="dismenu-{{itemId}}" onchange="updateMaterial({{itemId}});" style="max-width:250px;">
                            <c:if test="${not empty materials}">
                              <c:forEach var="material" items="${materials}">
                                <c:forEach var="finish" items="${material.finishList}">
                                  <option value="${material.materialId} ${finish.finishId}">${material.name} - ${finish.name}</option>
                                </c:forEach>
                              </c:forEach>
                            </c:if>
                          </select>
                        </div>
                      </div>
                      {{#if sizeDropdownAvailable}}
                      <div class="material-selector-container">
                        <span class="selector-title">Size</span>
                        <div class="material-selector">
                          <select name="sizee" id="sizee-{{itemId}}" onchange="updateSize({{itemId}});">
                            {{#each availableSizes}}
                              <option value="{{@key}}">{{this}}</option>
                            {{/each}}
                          </select>
                        </div>
                      </div>
                      {{/if}}
                      {{#if shouldIncludeChainDropdown}}
                      <c:if test="${not empty availableChains}">
                        <div class="chain-container">
                          <span class="selector-title">Chain</span>
                            <div class="material-selector">
                              <select name="chain" id="chain-{{itemId}}" onchange="updateChain({{itemId}});">
                      <c:forEach var="chain" items="${availableChains}">
                                <option value="${chain.chainId}" selected>${chain.name} - ${chain.price}</option>
                      </c:forEach>
                              </select>
                            </div>
                        </div>
                      </c:if>
                      {{/if}}
                      {{#if isEngravable}}
                      <div class="material-selector-container">
                        <span class="selector-title">Engraving</span>
                        <div class="material-selector">
                          <input type="text" id="engravetext-{{itemId}}" maxlength="16" class="form-control engravebox" placeholder="Your Custom Engraving">
                          <label class="engravingfineprint">Maximum 16 Characters</label><div id="engravehint-{{itemId}}" class="engravehintstyle" data-placement="right" data-content="Text will be engraved in the inside center of our jewelry. Try-on Models do not come with engraved text.">?</div>
                        </div>
                      </div>
                      {{/if}}
                    </div>
                  </div>
                  <div class="modal-footer" style="padding: 20px 30px 20px;">
                    <button type="button" class="btn btn--lightGray" data-dismiss="modal" style="float: left;">Cancel</button>
                    <button id="addToCart" type="button"  data-dismiss="modal" onclick="TROVE.addFromCard({{itemId}})" class="btn btn--red addToCart" style="float:right;">Add to Cart</button>
                  </div> 
                </div>
              </div>
            </div>
            <div class="card_content_a"><div class="card_product_name"><a onclick="nameb()" id="troveItemName-{{itemId}}" href="/public/productdescription/{{itemId}}">{{itemName}}</a></div></div>
            {{#unless_blank defaultCollection}}
              <div class="card_content_b product-collection-name"><div class="card_user_info"><a onclick="nameb({{itemId}})" href="/private/user/{{itemOwner.userId}}"><img src="{{itemOwner.profileImageThumbnailPath}}" class="card_avatar_small"><span class="card_username">{{itemOwner.username}}</span></a></div></div>
              <div class="card_content_b"><div class="card_collection_name"><a onclick="collectionb({{itemOwner.userId}})" href="/private/collection/{{defaultCollection.collectionId}}">{{defaultCollection.collectionName}}</a></div></div>
            {{else}}
              <div class="card_content_b product-collection-name"><div class="card_user_info"><a onclick="nameb({{itemId}})" href="/private/user/{{itemOwner.userId}}"><img src="{{itemOwner.profileImageThumbnailPath}}" class="card_avatar_small"><span class="card_username">{{itemOwner.username}}</span></a></div></div>
              <div class="card_content_b"><div class="card_collection_name"><a onclick="collectionb({{itemOwner.userId}})" href="/private/user/{{itemOwner.userId}}">{{itemOwner.username}}'s Trove</a></div></div>
            {{/unless_blank}}
            <div class="social_counts item">
              <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/trove-icon.svg" class="icon-small" title="Troves" alt="Troves"><span id="trovedCount-{{itemId}}">{{#if trovedCount}} {{trovedCount}} {{else}} 0 {{/if}}</span>
              <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/remix-icon.svg" class="icon-small" style="padding-left: 8px" title="Remixes" alt="Remixes"><span id="remadeCount-{{itemId}}">{{#if remadeCount}} {{remadeCount}} {{else}} 0 {{/if}}</span>
            </div>
          </div>
        </div>
      {{/each}}
    </script>
          
    <!-- row for collections -->
    <div class="row collections">
      <c:if test="${user.userId==authUser.userId}">
        <div class="card" style="opacity:0;">
          <div class="card-content collections" id="collection-new">
            <div class="overlay collections"><a href="#" data-toggle="modal" data-target="#modal-newCollection"><div class="card_darken collections"></div></a></div>
            <img src="https://storage.googleapis.com/trove-demo-cdn/img/trove-icon.svg">
            <div class="collection_title">New Collection</div>
          </div>
        </div>
        <div class="newCollectionSpace"></div>
        <!-- new collection modal -->
        <div class="modal fade" id="modal-newCollection" tabindex="-1" role="dialog" aria-labelledby="newCollection" aria-hidden="true">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header"><h4 class="modal-title" id="newCollection">Create New Collection</h4></div>
              <div class="modal-body">
                <form class="new-collection form-horizontal" onsubmit="return false;" action='/' method='post'>
                  <div class="form newCollection">
                    <fieldset>
                      <div class="col-xs-12">
                        <label>Collection Name</label>
                        <input type="text" id="newCollectionName" name="newCollectionName" maxlength="30" class="form-control" placeholder="My Collection" />
                        <div id="createcollerr" class="input-error-text" style="display: none;"></div>
                      </div>
                      <div class="col-xs-12">
                        <label>Collection Description</label>
                        <textarea type="textarea" name="newCollectionDescription" row="3" id="newCollectionDescription" class="form-control" placeholder="Enter a description of the collection."></textarea>
                      </div> 
                    </fieldset>
                  </div>
                </form> 
              </div>
              <div class="modal-footer" style="padding: 20px 30px 20px;">
                <button type="button" class="btn" data-dismiss="modal" style="float: left;">Cancel</button>
                <button id="newCollection" onclick="createCollection()" type="button" class="btn btn_save" style="float:right;">Create</button>
              </div>
            </div>
          </div>
        </div>
      </c:if>
    </div>

    <script id="collectioncards" type="text/x-handlebars">
      {{#each this}}
        <div class="card" id="card-{{collectionId}}" style="opacity:0;">
          <div class="card-content collections" id="collection-{{collectionId}}">
            <div class="overlay collections"><a onclick="collections_click()" href="/private/collection/{{collectionId}}"><div class="card_darken collections"></div></a></div>
            <div id="collectionName-{{collectionId}}" class="collection_name">{{collectionName}}</div>
            <div class="social_counts">
              <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/trove-icon.svg" class="icon-small" title="Troves" alt="Troves"><span id="collectionOverallTroveCount-{{collectionId}}">{{#if overallTroveCount}} {{overallTroveCount}} {{else}} 0 {{/if}}</span>
              <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/remix-icon.svg" class="icon-small" style="padding-left: 8px" title="Remixes" alt="Remixes"><span id="collectionOverallRemakeCount-{{collectionId}}">{{#if overallRemakeCount}} {{overallRemakeCount}} {{else}} 0 {{/if}}</span>
              <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/user-icon.svg" class="icon-small" style="padding-left: 8px" title="Followers" alt="Followers"><span id="collectionOverallFollowedCount-{{collectionId}}">{{#if followedCount}} {{followedCount}} {{else}} 0 {{/if}}</span>
            </div>
            <div class="collection_item_photos">
              {{#each collectionImages}}
                <img src="{{collectionImageUrl}}">
              {{else}}
                <div class="empty">No Items</div>
              {{/each}}
            </div>
            <div class="card_actions">
              {{#if isOwner}}
                <button id="deleteCollection" data-toggle="modal" data-target="#modal-deleteCollection-{{collectionId}}" class="btn">Delete</button>
                <button id="editCollection" data-toggle="modal" data-target="#modal-editCollection-{{collectionId}}" class="btn"><img src="https://storage.googleapis.com/troveup-imagestore/assets/img/edit-icon-white.svg" class="inline-icon-edit-sm sm">Edit</button>
                <div class="modal fade" id="modal-editCollection-{{collectionId}}" tabindex="-1" role="dialog" aria-labelledby="newCollection" aria-hidden="true">
                  <div class="modal-dialog">
                    <div class="modal-content">
                      <div class="modal-header"><h4 class="modal-title" id="newCollection">Edit Collection</h4></div>
                      <div class="modal-body">
                        <form class="edit-collection form-horizontal" onsubmit="return false;" action='/' method='post'>
                          <div class="form editCollection">
                            <fieldset>
                              <div class="col-xs-12">
                                <label>Collection Name</label>
                                <input type="text" id="editCollectionName-{{collectionId}}" maxlength="30" name="editCollectionName-{{collectionId}}" class="form-control" placeholder="{{collectionName}}" />
                                <div id="editcollerr-{{collectionId}}" class="input-error-text" style="display: none;"></div>
                              </div>
                              <div class="col-xs-12">
                                <label>Collection Description</label>
                                <textarea type="textarea" name="editCollectionDescription-{{collectionId}}" row="3" id="editCollectionDescription-{{collectionId}}" class="form-control" placeholder="{{collectionDescription}}"></textarea>
                              </div>
                            </fieldset>
                          </div>
                        </form> 
                      </div>
                      <div class="modal-footer" style="padding: 20px 30px 20px;">
                        <button type="button" class="btn" data-dismiss="modal" style="float: left;">Cancel</button>
                        <button id="editCollection" onclick="updateCollection('{{collectionId}}')" type="button" class="btn btn_save">Save</button>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="modal fade" id="modal-deleteCollection-{{collectionId}}" tabindex="-1" role="dialog" aria-labelledby="deleteCollection" aria-hidden="true">
                  <div class="modal-dialog">
                    <div class="modal-content">
                      <div class="modal-header"><h4 class="modal-title" id="deleteCollection">Delete {{collectionName}}</h4></div>
                      <div class="modal-body">
                        <p class="warningtext">Are you sure you want to delete this collection? </p>
                      </div>
                      <div class="modal-footer" style="padding:20px 30px 20px">
                        <button type="button" class="btn" data-dismiss="modal" style="float: left;">Cancel</button>
                        <button id="deleteCollection" onclick="deleteCollection({{collectionId}})" data-dismiss="modal" type="button" class="btn btn_save" style="float:right;">Delete</button>
                      </div>
                    </div>
                  </div>
                </div>
              {{else}}
                {{#if isFollowed}}
                  <button id="unfollowCollection-{{collectionId}}" class="btn btn_unfollow" onclick="unfollowCollection({{collectionId}})">Unfollow</button>
                {{else}}
                  <button id="followCollection-{{collectionId}}" class="btn btn_follow" onclick="followCollection({{collectionId}})">Follow</button>
                {{/if}}
              {{/if}}
            </div>
          </div>
        </div>
      {{/each}}
    </script>


    <div class="row followers">
    </div>

    <script id="followerscards" type="text/x-handlebars">
      {{#each this}}
        <div class="block card_mask follower">
          <div class="card_column_single">
            <div class="overlay"><a href="/private/user/{{userId}}"><div class="card_darken person" style="position: absolute"></div></a></div>
            <div class="card_content_a">
              <div class="card_product_name"><a href="/private/user/{{userId}}">{{username}}</a></div>
            </div>
            <div class="follow-pic-holder"><img src="{{fullProfileImagePath}}" class="profile-pic-img"></div>
            <div class="collection_card_content_row1">
              <div class="ownerimgholder">
                {{#each trovedItems}}
                  <a href="/public/productdescription/{{groupedItem.itemId}}"><img class="ownerimg" src="{{groupedItem.defaultCardImageUrl}}"></a>
                {{else}}
                  <div class="empty">No Items</div>
                {{/each}}
              </div>
            </div>
            {{#if followed}}
              <button onclick="TROVE.unfollowUser('{{userId}}','trovefollowers')" id="ufolwbutton" class="btn ufolbtn{{userId}} btn--lightgray"><span style="color: #fff">Unfollow</span></button>
            {{else}}
              <button onclick="TROVE.followUser('{{userId}}','trovefollowers')" id="folwbutton" class="btn folbtn{{userId}} btn--red"><span style="color: #fff">Follow</span></button>
            {{/if}}
          </div>
        </div>
      {{/each}}
    </script>

    <!-- followedby blocks -->
    <div class="row followedby">
    </div>

    <script id="followingcards" type="text/x-handlebars">
      {{#each this}}
        <div class="block card_mask followedby">
          <div class="card_column_single">
            <div class="overlay"><a href="/private/user/{{userId}}"><div class="card_darken person" style="position: absolute"></div></a></div>
            <div class="card_content_a">
              <div class="card_product_name"><a href="/private/user/{{userId}}">{{username}}</a></div>
            </div>
            <div class="follow-pic-holder"><img src="{{fullProfileImagePath}}" class="profile-pic-img"></div>
            <div class="collection_card_content_row1">
              <div class="ownerimgholder">
                {{#each trovedItems}}
                  <a href="/public/productdescription/{{groupedItem.itemId}}"><img class="ownerimg" src="{{groupedItem.defaultCardImageUrl}}"></a>
                {{else}}
                  <div class="empty">No Items</div>
                {{/each}}
              </div>
            </div>
            {{#if followed}}
              <button onclick="TROVE.unfollowUser('{{userId}}','trovefollowers')" id="ufolwbutton" class="btn ufolbtn{{userId}} btn--lightgray"><span style="color: #fff">Unfollow</span></button>
            {{else}}
              <button onclick="TROVE.followUser('{{userId}}','trovefollowers')" id="folwbutton" class="btn folbtn{{userId}} btn--red"><span style="color: #fff">Follow</span></button>
            {{/if}}
          </div>
        </div>
      {{/each}}
    </script>

  </div>

  <!-- Modal -->
  <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog coverphoto">
      <div class="modal-content" style="width: 100%;"> 
        <div class="modal-body photo">
          <!-- photo upload -->
          <div class="containers">
            <div class="imageBox">
              <div class="thumbBox"></div>
              <div class="spinner" style="display: none">Loading...</div>
            </div>
           <!--  <div class="action">
              <label class="myLabel">
                <input type="file" id="file">
                <span>Add Cover Photo</span>
              </label>
              <input type="button" id="btnZoomIn" value="+" style="float: left">
              <input type="button" id="btnZoomOut" value="-" style="float: left">
            </div> -->
          </div>
          <script type="text/javascript">
              $(window).load(function() {
                  var options =
                  {
                      thumbBox: '.thumbBox',
                      spinner: '.spinner',
                      imgSrc: 'avatar.png'
                  }
                  var cropper;
                  $('#file').on('change', function(){
                      var reader = new FileReader();
                      reader.onload = function(e) {
                          options.imgSrc = e.target.result;
                          cropper = $('.imageBox').cropbox(options);
                      }
                      reader.readAsDataURL(this.files[0]);
                      this.files = [];
                  })
                  $('#btnCrop').on('click', function(){
                      var img = cropper.getDataURL();
                      var imgblob = cropper.getBlob();
                      var formData = new FormData();
                      formData.append("blob", imgblob);
                      jQuery.ajax({
                          url: '/updatecoverimage',
                          data: formData,
                          cache: false,
                          contentType: false,
                          processData: false,
                          headers: {
                              'Access-Control-Allow-Origin': '*',
                              "X-CSRF-TOKEN": "${_csrf.token}"
                          },
                          type: 'POST',
                          success: function(data) {
                              $('#covimg').attr('src', data.profileCoverFullUrl);
                          }
                      });
                  })
                  $('#btnZoomIn').on('click', function(){
                      cropper.zoomIn();
                  })
                  $('#btnZoomOut').on('click', function(){
                      cropper.zoomOut();
                  })
              });
          </script>
          <!-- end photo upload -->
        </div>
        <div class="modal-footer">
           <div class="action">
              <label class="myLabel">
                <input type="file" id="file">
                <span>Add Cover Photo</span>
              </label>
              <input type="button" id="btnZoomIn" value="+" style="float: left">
              <input type="button" id="btnZoomOut" value="-" style="float: left">
          </div>
          <button type="button" class="btn btn--lightgray" data-dismiss="modal">Close</button>
          <button type="button"  id="btnCrop" class="btn btn--darkgray" data-dismiss="modal">Update Photo</button>
          <!-- <input type="button" id="btnCrop" value="Crop" style="float: right">  -->
        </div>
      </div>
    </div>
  </div>

  <form action="" id="add_to_cart_form" class="form-horizontal">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input id="itemId" type="hidden" name="itemId" value="vada"/>
    <input id="materialId" type="hidden" name="materialId" value="nada"/>
    <input id="finishId" type="hidden" name="finishId" value="nada"/>
    <input id="size" type="hidden" name="size"/>
    <input id="chainId" type="hidden" name="chain" value="0" />
  </form>

  <script src="/resources/js/vendor/cropbox.js"></script>



  <c:choose>
    <c:when test="${not empty canFollowUser}">

      <script>

        function editb() {
          mixpanel.track("trove_edit");
        };

        function custb() {
          mixpanel.track("trove_customize");
        };

        function buyb() {
          mixpanel.track("trove_buy");
        };

        function collectionclick() {
          mixpanel.track("trove_collection");
        };

        function nameb() {
          mixpanel.track("trove_username");
        };
        function imgb() {
          mixpanel.track("trove_image");
        };

      </script>
      

    </c:when>
    <c:when test="${empty canFollowUser}">
      <script>

        function editb() {
          mixpanel.track("mytrove_edit");
        };

        function custb() {
          mixpanel.track("mytrove_customize");
        };

        function buyb() {
          mixpanel.track("mytrove_buy");
        };

        function collectionclick() {
          mixpanel.track("mytrove_collection");
        };

        function nameb() {
          mixpanel.track("mytrove_username");
        };
        function imgb() {
          mixpanel.track("mytrove_image");
        };

      </script>
    

    </c:when>
    <c:otherwise></c:otherwise>
  </c:choose>




  <script>

    function buyb(itemid) {
      document.getElementById("itemId").value = itemid;
      updateSize(itemid);
      var m = document.getElementById("dismenu-"+String(itemid));
      var mater = m.options[m.selectedIndex].value.split(" ");
      document.getElementById("materialId").value = mater[0];
      document.getElementById("finishId").value = mater[1];
      updateChain(itemid);
      mixpanel.track("feed_buy", {
        "item": itemid
      });
    };

    function folitemb(userId,itemId) {
      mixpanel.track("following_item", {
        "item": itemId,
        "user": userId
      });
    };

    function foleritemb(userId, itemId) {
      mixpanel.track("follower_item", {
        "item": itemId,
        "user": userId
      });
    };

    function editcoverphoto() {
      mixpanel.track("trove_editcoverphoto");
    };

    function editprof() {
      mixpanel.track("trove_editprofile");
    };

    function removeb() {
      mixpanel.track("trove_remove");
    };

    

    function submitToCart(itemid) {
      $('.spinner.addtocartfromcard-'+String(itemid)).fadeIn();
      $('.addtocartfromcardstuff-'+String(itemid)).hide();
      var $form = $('#add_to_cart_form');
      var data = $form.serialize();
      $.ajax({
        type:'POST',
        data:data,
        url:'/noncustaddtobag',
        success:function(data) {
          $('.modal-'+String(itemid)).removeClass("in");
          $('.modal-backdrop').removeClass("in");
          $('.spinner.addtocartfromcard-'+String(itemid)).hide();
          $('.addtocartfromcardstuff-'+String(itemid)).fadeIn();
        }
      });
    };

    function updateSize(itemid) {

      var sizeDropdown = $('#sizee-' + String(itemid));

      if (sizeDropdown && sizeDropdown.val()) {
        $('#size').val(sizeDropdown.val());
      }
    }

    function updateChain(itemId) {
      $("#chainId").val($("#chain-" + itemId).val());
    }

    function updateMaterial(itemid) {
      var e = document.getElementById("dismenu-"+String(itemid));
      var mater = e.options[e.selectedIndex].value.split(" ");
      document.getElementById("materialId").value = mater[0];
      document.getElementById("finishId").value = mater[1];
    }; 

  </script>


  <c:if test="${not empty canFollowUser}">
    <script>
      function land() {
        mixpanel.track("land_trove");
      };
    </script>
  </c:if>
  <c:if test="${empty canFollowUser}">
    <script>
      function land() {
        mixpanel.track("land_mytrove");
      };
    </script>          
  </c:if>

  <script>

    var colCount = 0;
    var colWidth = 250;
    var margin = 30;
    var spaceLeft = 0;
    var windowWidth = 0;
    var blocks = [];
    
    function setupBlocksProfile(selector) {
      windowWidth = $(window).width();
      blocks = [];
      colCount = Math.floor(windowWidth/(colWidth+margin*2));
      spaceLeft = (windowWidth - ((colWidth*colCount)+(margin*(colCount-1)))) / 2 + 15;
      console.log(spaceLeft);
      for(var i=0;i<colCount;i++){
        blocks.push(margin);
      }
      positionBlocksProfile(selector);
    }

    function positionBlocksProfile(selector) {
      $(selector).each(function(i){
        var min = Array.min(blocks);
        var index = $.inArray(min, blocks);
        var leftPos = margin+(index*(colWidth+margin));
        $(this).css({
          'left':(leftPos+spaceLeft)-40+'px',
          'top':min+$(".profile-container").height()+$(".navbar").height()+10+'px',
          'display':'inherit'
        });
        blocks[index] = min+$(this).outerHeight()+margin;
      }); 
    }

    Array.min = function(array) {
        return Math.min.apply(Math, array);
    };

    // global vars
    // used for switch in scroll function
    // item, collection, follower, or following
    var tabContext = 'item';

    var userId = ${user.userId};
    var authUserId = ${authUser.userId};
    var itemsPerPage = 12;
    var canContinueScrollingItems = true;
    var canContinueScrollingCollections = true;
    var canContinueScrollingFollowing = true;
    var canContinueScrollingFollowers = true;
    // data holders
    var cardData;
    var itemCardData;
    var followerCardData;
    var followingCardData;

    // latest page holders
    var pageNum = 0;
    var itemPageNum = 0;
    var followerPageNum = 0;
    var followingPageNum = 0;

    var processingItems;
    var processingCollections;
    var processingFollowers;
    var processingFollowing;

    function getTroveItems() {
      if (!processingItems) {
        processingItems = true;
        var data = Object.create(null);
        data['userId'] = userId;
        data['pageNumber'] = itemPageNum;
        // data['objectId-0'] = 0;
        // data['objectId-1'] = 3;
        //Maximum number of items that are allowed to be returned by the server
        data['pageLimit'] = itemsPerPage;
        data['duplicateBuffer'] = 3;
        jQuery.ajax({
            url: '/troveitems',
            data: jQuery.param(data),
            cache: false,
            contentType: false,
            processData: false,
            headers: {
                'Access-Control-Allow-Origin': '*',
                "X-CSRF-TOKEN": "${_csrf.token}"
            },
            type: 'GET',
            success: function(data) {
              if ( data.itemList.length == 0 ) {
                itemCardData = data.itemList;
              } else {
                itemCardData = data.itemList;
                itemPageNum = itemPageNum + 1;
              }

              if (data.itemList.length < itemsPerPage) {
                canContinueScrollingItems = false;
              }

              processingItems = false;
            }
        }).done( function(data) {
          layoutItemBars();

          for (var i = 0; i < data.itemList.length; ++i) {
            $('#engravehint-' + data.itemList[i].itemId).popover({trigger: "hover click"});
          }
        });
      }
    }

    function layoutItemBars() {
      var t = Handlebars.compile($('#itemcards').html());
      Handlebars.registerHelper('ifCond', function(v1, v2, options) {
        if(v1 === v2) {
          return options.fn(this);
        }
        return options.inverse(this);
      });

      Handlebars.registerHelper('unless_blank', function(item, block) {
        return (item && String(item).replace(/\s/g,"").length) ? block.fn(this) : block.inverse(this);
      });

      Handlebars.registerHelper('decimal', function(number) {
        return parseFloat(number).toFixed(0);
      });

      $('.row.items').append(t(itemCardData));
      setupCards('.block.card_mask.item');
      layoutCollectionDropdown();
    }

    // Auth User's Collections Actions
    function createCollection() {

        var name = document.getElementById("newCollectionName").value;

        var regexp = new RegExp(/^[a-zA-Z0-9 ]+$/);

        if (name && name.length > 0) {

          var testResult = regexp.test(name);

          if (testResult) {
              mixpanel.track("collections_addcollection");
              var data = new FormData();

              var descrip = document.getElementById("newCollectionDescription").value;
              data.append('collectionName', name);
              data.append('collectionDescription', descrip);
              data.append('isPrivate', false);
              jQuery.ajax({
                url: '/createcollection',
                data: data,
                cache: false,
                contentType: false,
                processData: false,
                headers: {
                  'Access-Control-Allow-Origin': '*',
                  "X-CSRF-TOKEN": "${_csrf.token}"
                },
                type: 'POST',
                success: function(data){
                  cardData = data.collectionList;
                }
              }).done( function() {
                var t = Handlebars.compile($('#collectioncards').html());
                $('.newCollectionSpace').prepend(t(cardData));
                setupCards('.card');
                var elm = $('#collectCounter');
                var count = parseInt(elm.html());
                elm.html(String(count + 1));

                $('#modal-newCollection').modal('hide');
                $('#createcollerr').html('').hide();
              });
            }
            else {
              $('#createcollerr').html('There are invalid characters present in the collection name.').show();
            }
          }
          else {
            $("#createcollerr").html("Name must be 1 to 30 characters in length.").show();
          }

    }

    function updateCollection(collectionId) {

        var name = document.getElementById("editCollectionName-" + String(collectionId)).value;

        var regexp = new RegExp(/^[a-zA-Z0-9 ]+$/);

        if (name && name.length > 0) {
          var testResult = regexp.test(name);

          if (testResult) {
            var data = new FormData();
            data.append('collectionId', collectionId)

            var ndescrip = "editCollectionDescription-" + String(collectionId);
            var title = "#collectionName-" + String(collectionId);
            var descrip = document.getElementById(ndescrip).value;
            data.append('collectionName', name);
            data.append('collectionDescription', descrip);
            data.append('isPrivate', false);
            jQuery.ajax({
              url: '/updatecollection',
              data: data,
              cache: false,
              contentType: false,
              processData: false,
              headers: {
                'Access-Control-Allow-Origin': '*',
                "X-CSRF-TOKEN": "${_csrf.token}"
              },
              type: 'POST',
              success: function(data){
              }
            }).done( function() {
              $(title).html(name);
              $('#modal-editCollection-' + collectionId).modal('hide');
              $('#editcollerr-' + collectionId).html('').hide();
            });
          }
          else {
            $('#editcollerr-' + collectionId).html('There are invalid characters present in the collection name.').show();
          }
        }
        else {
            $('#editcollerr-' + collectionId).html('Name must be 1 to 30 characters in length.').show();
        }
    }

    function deleteCollection(collectionId) {
        var data = new FormData();
        var cardToDelete = "#card-" + String(collectionId);
        data.append('collectionId', collectionId);
        jQuery.ajax({
            url: '/deletecollection',
            data: data,
            cache: false,
            contentType: false,
            processData: false,
            headers: {
                'Access-Control-Allow-Origin': '*',
                "X-CSRF-TOKEN": "${_csrf.token}"
            },
            type: 'POST',
            success: function(data) {
            }
        }).done( function() {
          $(cardToDelete).detach();
          setupCards('.card');
          var elm = $('#collectCounter');
          var count = parseInt(elm.html());
          elm.html(String(count - 1));
        });
    }

    function removeItemFromTrove(itemId) {
      var data = new FormData();
      var cardToDelete = "#troveitemcard-" + String(itemId);
      data.append('itemId', itemId);
      jQuery.ajax({
        url: '/removefromtrove',
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        headers: {
          'Access-Control-Allow-Origin': '*',
          "X-CSRF-TOKEN": "${_csrf.token}"
        },
        type: 'POST',
        success: function(data) {

        }
      }).done( function() {
        $(cardToDelete).detach();
        setupCards('.block.card_mask.item');
        var elm = $('.item-count');
        var count = parseInt(elm.html());
        elm.html(String(count - 1));
      });
    }

    function editTroveItem(itemId) {
      var name = document.getElementById("itemName-" + String(itemId)).value;
      //var descrip = document.getElementById("itemDescriptioname-" + String(itemId)).value;

      var regexp = new RegExp(/^[a-zA-Z0-9 ]+$/);

      if (name) {
        if (!(regexp.test(name))) {
          var errorString = "There are illegal characters in your item\'s name.";
        }

        if (!(name.length < 31)) {
          errorString = "Please shorten your item\'s name to 30 characters or less.";
        }

        if (!errorString) {
          var titleToEdit = "#troveItemName-" + String(itemId);
          $(titleToEdit).html(name);
          $.ajax({
            url: location.origin + "/updateitemname",
            type: 'post',
            data: {
              itemId: itemId,
              itemDescription: name
            },
            headers: {
              "X-CSRF-TOKEN": "${_csrf.token}"
            },
            dataType: 'json',
            success: TROVE.updateItemCard(itemId, 'updateTrovedItem')
          });

          //Make sure that the error goes away
          document.getElementById("nameError-" + String(itemId)).innerHTML = '';
          $("#nameError-" + String(itemId)).hide();
          $('#modal2-' + String(itemId)).modal('hide');
        }
        else
        {
          document.getElementById("nameError-" + String(itemId)).innerHTML = errorString;
          $("#nameError-" + String(itemId)).show();
        }
      }

      //Otherwise just close the modal
      document.getElementById("nameError-" + String(itemId)).innerHTML = '';
      $("#nameError-" + String(itemId)).hide();
      $('#modal2-' + String(itemId)).modal('hide');

      /*if (descrip) {
        $.ajax({
          url: location.origin + "/updateitemdescription",
          type: 'post',
          data: {
            itemId: itemId,
            itemDescription: descrip
          },
          headers: {
            "X-CSRF-TOKEN": "${_csrf.token}"
          },
          dataType: 'json',
          success: function() {

          }
        });
      }*/

    }

    // Other User's Collections Actions
    function followCollection(collectionId) {
        var data = new FormData();
        var fid = "followCollection-" + String(collectionId);
        var ufid = "unfollowCollection-" + String(collectionId);
        var f = "#" + fid;
        var uf = "#" + ufid;
        var uc = "unfollowCollection(" + String(collectionId) + ")";
        var fcount = "#collectionOverallFollowedCount-" + String(collectionId);
        data.append('collectionId', collectionId);
        jQuery.ajax({
            url: '/followcollection',
            data: data,
            cache: false,
            contentType: false,
            processData: false,
            headers: {
                'Access-Control-Allow-Origin': '*',
                "X-CSRF-TOKEN": "${_csrf.token}"
            },
            type: 'POST',
            success: function(data) {
              $(f).html('Unfollow');
              $(f).attr( "onclick", uc );
              $(f).addClass('btn_unfollow');
              $(f).removeClass('btn_follow');
              $(f).attr( "id", ufid );
            }
        }).done( function() {
          var elm = $(fcount);
          var count = parseInt(elm.html());
          elm.html(String(count + 1));
        });
    }

    function unfollowCollection(collectionId) {
        var data = new FormData();
        var fid = "followCollection-" + String(collectionId);
        var ufid = "unfollowCollection-" + String(collectionId);
        var f = "#" + fid;
        var uf = "#" + ufid;
        var fc = "followCollection(" + String(collectionId) + ")";
        var fcount = "#collectionOverallFollowedCount-" + String(collectionId);
        data.append('collectionId', collectionId);
        jQuery.ajax({
            url: '/unfollowcollection',
            data: data,
            cache: false,
            contentType: false,
            processData: false,
            headers: {
                'Access-Control-Allow-Origin': '*',
                "X-CSRF-TOKEN": "${_csrf.token}"
            },
            type: 'POST',
            success: function(data) {
              $(uf).html('Follow');
              $(uf).attr( "onclick", fc );
              $(uf).addClass('btn_follow');
              $(uf).removeClass('btn_unfollow');
              $(uf).attr( "id", fid );
            }
        }).done( function() {
          var elm = $(fcount);
          var count = parseInt(elm.html());
          elm.html(String(count - 1));
        });
    }

    function getCollections() {
      if (!processingCollections) {
        processingCollections = true;
        var data = Object.create(null);
        data['userId'] = userId;
        data['pageNumber'] = pageNum;
        // data['objectId-0'] = 0;
        // data['objectId-1'] = 3;
        data['pageLimit'] = itemsPerPage;
        data['duplicateBuffer'] = 3;
        jQuery.ajax({
            url: '/collections',
            data: jQuery.param(data),
            cache: false,
            contentType: false,
            processData: false,
            headers: {
                'Access-Control-Allow-Origin': '*',
                "X-CSRF-TOKEN": "${_csrf.token}"
            },
            type: 'GET',
            success: function(data) {
              if ( data.collectionList.length == 0 ) {
                cardData = data.collectionList;
              } else {
                cardData = data.collectionList;
                pageNum = pageNum + 1;
              }
              if (data.collectionList.length < itemsPerPage) {
                canContinueScrollingCollections = false;
              }

              processingCollections = false;
            }
        }).done( function() {
          layoutBars();
        });
      }
    }

    function layoutBars() {
      var t = Handlebars.compile($('#collectioncards').html());
      $('.row.collections').append(t(cardData));
      setupCards('.card');
    }

    function getFollowers() {
      if (!processingFollowers) {
        processingFollowers = true;
        var data = Object.create(null);
        data['userId'] = userId;
        data['pageNumber'] = followerPageNum;
        // data['objectId-0'] = 0;
        data['pageLimit'] = itemsPerPage;
        data['duplicateBuffer'] = 3;
        jQuery.ajax({
            url: '/followers',
            data: jQuery.param(data),
            cache: false,
            contentType: false,
            processData: false,
            headers: {
                'Access-Control-Allow-Origin': '*',
                "X-CSRF-TOKEN": "${_csrf.token}"
            },
            type: 'GET',
            success: function(data) {
              if ( data.userList.length == 0 ) {
                followerCardData = data.userList;
              } else {
                followerCardData = data.userList;
                followerPageNum = followerPageNum + 1;
              }
              if (data.userList.length < itemsPerPage) {
                canContinueScrollingFollowers = false;
              }

              processingFollowers = false;
            }
        }).done( function() {
          layoutFollowerBars();
        });
      }
    }

    function layoutFollowerBars() {
      var t = Handlebars.compile($('#followerscards').html());
      $('.row.followers').append(t(followerCardData));
      setupCards('.block.card_mask.follower');
    }


    function getFollowing() {
      if (!processingFollowing) {
        processingFollowing = true;
        var data = Object.create(null);
        data['userId'] = userId;
        data['pageNumber'] = followingPageNum;
        // data['objectId-0'] = 0;
        data['pageLimit'] = itemsPerPage;
        data['duplicateBuffer'] = 3;
        jQuery.ajax({
            url: '/followed',
            data: jQuery.param(data),
            cache: false,
            contentType: false,
            processData: false,
            headers: {
                'Access-Control-Allow-Origin': '*',
                "X-CSRF-TOKEN": "${_csrf.token}"
            },
            type: 'GET',
            success: function(data) {
              if ( data.userList.length == 0 ) {
                followingCardData = data.userList;
              } else {
                followingCardData = data.userList;
                followingPageNum = followingPageNum + 1;
              }
              if (data.userList.length < itemsPerPage) {
                canContinueScrollingFollowing = false;
              }

              processingFollowing = false;
            }
        }).done( function() {
          layoutFollowingBars();
        });
      }
    }

    function layoutFollowingBars() {
      var t = Handlebars.compile($('#followingcards').html());
      $('.row.followedby').append(t(followingCardData));
      setupCards('.block.card_mask.followedby');
    }

    function setupCards(selector) {
      windowWidth = $(window).width();
      blocks = [];
      colCount = Math.floor(Math.min(windowWidth/(colWidth+margin*2), 4));
      spaceLeft = (windowWidth - ((colWidth*colCount)+(margin*(colCount-1)))) / 2 + 15;
      for(var i=0;i<colCount;i++){
        blocks.push(margin);
      }
      positionCards(selector);
    }

    function positionCards(selector) {
      var itemsProcessed = 0;
      $(selector).each(function(i){
        if ($(window).width() > 468 || ($(window).width() <= 468 && $(this).hasClass('hidemobile') == false)) {
          itemsProcessed += 1;
          var min = Array.min(blocks);
          var index = $.inArray(min, blocks);
          var leftPos = margin + (index * (colWidth + margin));
          $(this).css({
            'left': (leftPos + spaceLeft) - 40 + 'px',
            'top': min + $(".profile-container").height() + $(".navbar").height() + 10 + 'px',
            //'display': 'inherit',
            'opacity': 1
          });
          blocks[index] = min + $(this).outerHeight() + margin;
        }
      }); 

      if ( itemsProcessed <= 3 ) {
          getStuff();
      } 
    }

    Array.min = function(array) {
        return Math.min.apply(Math, array);
    };



    var collector;

    function allCollections() {
      var data = Object.create(null);
      data['userId'] = userId;
      data['pageNumber'] = 0;
      data['pageLimit'] = ${collectionCount};
      data['duplicateBuffer'] = 3;
      jQuery.ajax({
        url: '/collections',
        data: jQuery.param(data),
        cache: false,
        contentType: false,
        processData: false,
        headers: {
          'Access-Control-Allow-Origin': '*',
          "X-CSRF-TOKEN": "${_csrf.token}"
        },
        type: 'GET',
        success: function(data) {
          collector = data.collectionList;
        }
      }).done( function() {
        layoutCollectionDropdown();
      });
    }

    function layoutCollectionDropdown() {
      var t = Handlebars.compile($('#collectionform').html());
      $('.collectselect').each(function(index, value){
        $(this).append(t(collector));
        $(this).removeClass("collectselect");
      });
    }

    function addItemToCollection(itemId, conta) {
      var e = document.getElementById(conta);
      var newCollectForItem = e.options[e.selectedIndex].value;
      var newCollectionNa = e.options[e.selectedIndex].text;
      var data = new FormData();
      data.append('itemId', itemId);
      data.append('collectionId', newCollectForItem );
      jQuery.ajax({
        url: '/addcollectionitem',
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        headers: {
          'Access-Control-Allow-Origin': '*',
          "X-CSRF-TOKEN": "${_csrf.token}"
        },
        type: 'POST',
        success: function(data) {
          setItemDefaultCollection(itemId, newCollectForItem);
        }
      });
    }

    function removeItemFromCollection(itemId, collectionId) {
      var data = new FormData();
      data.append('collectionId', collectionId);
      data.append('itemId', itemId);
      jQuery.ajax({
        url: '/removecollectionitem',
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        headers: {
          'Access-Control-Allow-Origin': '*',
          "X-CSRF-TOKEN": "${_csrf.token}"
        },
        type: 'POST',
        success: function(data) {
        }
      });
    }

    function setItemDefaultCollection(itemId, newCollectForItem) {
      var data = new FormData();
      data.append('itemId', itemId);
      data.append('collectionId', newCollectForItem );
      jQuery.ajax({
        url: '/setitemdefaultcollection',
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        headers: {
          'Access-Control-Allow-Origin': '*',
          "X-CSRF-TOKEN": "${_csrf.token}"
        },
        type: 'POST',
        success: function(data) {
        }
      });
    }

    function clearItemDefaultCollection(itemId, conta)  {
      var data = new FormData();
      data.append('itemId', itemId);
      jQuery.ajax({
        url: '/clearitemdefaultcollection',
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        headers: {
          'Access-Control-Allow-Origin': '*',
          "X-CSRF-TOKEN": "${_csrf.token}"
        },
        type: 'POST',
        success: function(data) {
        }
      });
    }

    function getStuff() {
      switch (tabContext) {
            case 'item':
                if (!canContinueScrollingItems)
                  return false;
                if ($(window).scrollTop() >= ($(document).height() - $(window).height())*0.8) {
                  getTroveItems();
                }
                break;
            case 'collection':
                if (!canContinueScrollingCollections)
                  return false;
                if ($(window).scrollTop() >= ($(document).height() - $(window).height())*0.8) {
                  getCollections();
                }
                break;
            case 'followers':
                if (!canContinueScrollingFollowers)
                  return false;
                if ($(window).scrollTop() >= ($(document).height() - $(window).height())*0.8) {
                  getFollowers();
                }
                break;
            case 'following':
                if (!canContinueScrollingFollowing)
                  return false;
                if ($(window).scrollTop() >= ($(document).height() - $(window).height())*0.8) {
                  getFollowing();
                }
                break;
            default:
                console.log(tabContext);
                break;
        }
    }

    $(document).ready(function() {
      land();
      tabContext = 'item';

      $('[data-toggle="popover"]').popover();
      getTroveItems();
      allCollections();

      $(window).scroll(function(e){
        getStuff();
      });

      var count = parseInt($('.tallyho-cart').html());
      if ( count >= 1 ) {
        $('.tallyho-cart').addClass('showitnow');
      }
      
      $('.spinner').hide();
      $('.row.followers').hide();
      $('.row.followedby').hide();
      $('.row.collections').hide();

      $('#itemstab').on('click', function(e){
        mixpanel.track("trove_items");
        e.preventDefault();
        processingItems = false;
        tabContext = 'item';
        getTroveItems();
        $('.row.items').fadeIn();
        $('.row.followers').hide();
        $('.row.followedby').hide();
        $('.row.collections').hide();
        $('.current-page').removeClass('current-page');
        $('#itemstab').addClass('current-page');
        setupCards('.block.card_mask.item');
      });

      $('#collectionstab').on('click', function(e){
        mixpanel.track("trove_collections");
        e.preventDefault();
        processingCollections = false;
        tabContext = 'collection';
        getCollections();
        $('.row.collections').fadeIn();
        $('.row.items').hide();
        $('.row.followers').hide();
        $('.row.followedby').hide();
        $('.current-page').removeClass('current-page');
        $('#collectionstab').addClass('current-page');
        setupCards('.card');
      });

      $('#followerstab').on('click', function(e){
        mixpanel.track("trove_followers");
        e.preventDefault();
        processingFollowers = false;
        tabContext = 'followers';
        getFollowers();
        $('.row.followers').fadeIn();
        $('.row.items').hide();
        $('.row.followedby').hide();
        $('.row.collections').hide();
        $('.current-page').removeClass('current-page');
        $('#followerstab').addClass('current-page');
        setupCards('.block.card_mask.follower');
      });

      $('#followingtab').on('click', function(e){
        mixpanel.track("trove_following");
        e.preventDefault();
        processingFollowing = false;
        tabContext = 'following';
        getFollowing();
        $('.row.followedby').fadeIn();
        $('.row.items').hide();
        $('.row.followers').hide();
        $('.row.collections').hide();
        $('.current-page').removeClass('current-page');
        $('#followingtab').addClass('current-page');
        setupCards('.block.card_mask.followedby');
      });

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

      $('.cardbuybutton').on( 'click', function( event ) {
        var arr = $(this).attr('id').split("-");
        // var sizes = allsize[arr[1]];
        // var select = $(document.createElement('select'));
        // select.attr('id',"sizee");
        // select.attr('name',"sizee");
        // var i=1;
        // for(;i<=sizes.length;i++){
        //     var option = $('<option>');
        //     option.val(sizes[i-1]).text(sizes[i-1]);
        //     select.append(option);
        // }
        // $(".helpme").append(select);
        // var newcount = parseInt($('.tallyho-cart').html()) + 1;
        // $('.modal-body').html('below will add this item to cart: '+ String(arr[1])); 
        // $('.tallyho-cart').html(String(newcount));
        // $('.tallyho-cart').addClass('showitnow');
        // $('.addToCart').attr('onClick','addItemToCart(arr[1]);');
        document.getElementById("itemId").value = arr[1];
        updateSize(arr[1]);
        updateMaterial(arr[1]);
        updateChain(arr[1]);
      });

    });

    $(window).on('resize', function() {
      setupCards('.card');
      setupCards('.block.card_mask.item');
      setupCards('.block.card_mask.follower');
      setupCards('.block.card_mask.followedby');
    });


  </script>




  </body>
  </html>


