<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head lang="en">
  <meta charset="UTF-8">
  <title>Collections</title>
  <script src="https://code.jquery.com/jquery-2.1.3.js"></script>
  <script src="/resources/js/vendor/handlebars.js"></script>
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/headjs/1.0.3/head.js"></script>
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=0">
  <link href='https://fonts.googleapis.com/css?family=Quicksand:300,400,700' rel='stylesheet' type='text/css'>
  <link href='https://fonts.googleapis.com/css?family=Raleway:400,200,300,500,600,700' rel='stylesheet' type='text/css'>
  <link rel="icon" href="/resources/img/favicon.png?v=2">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="/resources/stylesheets/main.css">
  <link rel="stylesheet" href="/resources/stylesheets/browse.css">
  <script type="text/javascript" src="/resources/js/namespace.js"></script>
  <script type="text/javascript" src="/resources/js/TROVE.js"></script>
  <c:import url="../fragments/analytics/all.jsp"/>
  <script>
    var jsfiles = [
      "https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.13.1/jquery.validate.js",
      "https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.13.1/additional-methods.js",
      "https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/js/bootstrap.min.js"
    ];
    head.load(jsfiles);
  </script>
  <style>
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
    .card-content {
      position: relative;
      height: auto;
      border: 2px solid #f0f0f0;
      border-radius: 8px;
      float: left;
      overflow: hidden;
      padding: 10px 15px 10px;
      margin-bottom: 30px;
      width: 250px;
      height: 400px;
      background: #FFF;
      -webkit-box-shadow: 0px 3px 6px 0px rgba(0, 0, 0, 0.1);
         -moz-box-shadow: 0px 3px 6px 0px rgba(0, 0, 0, 0.1);
              box-shadow: 0px 3px 6px 0px rgba(0, 0, 0, 0.1);
    }
    .collection_name {
      display: block;
      text-align: center;
      font-size: 20px;
      padding: 10px 0;
      text-transform: capitalize;
      margin-top: 10px;
      white-space: nowrap;
      overflow: hidden;
    }
    .social_counts {
      font-size: 11px;
      padding: 2px 0;
      opacity: .5;
      text-align: center;
      pointer-events: none;
    }
    .social_counts.item {
      font-size: 13px;
      padding: 10px 0;
      opacity: .5;
      text-align: left;
      pointer-events: none;
    }
    .social_counts.item span {
      padding: 0px 4px;
    }
    .icon-small {
      width: 20px;
      height: 10px;
      opacity: .6;
    }
    .overlay {
      display: block;
      position: relative;
      top: -10px;
      left: -15px;
      width: 250px;
      height: auto;
      opacity: 1;
      cursor: pointer;
      -webkit-transition: opacity .2s ease-out 0s;
         -moz-transition: opacity .2s ease-out 0s;
              transition: opacity .2s ease-out 0s;
    }

    .overlay.people {
      top: 0px;
      left: 0px;
    }
    .card_darken {
      position: absolute;
      width: 250px;
      height: 315px;
      background: #000;
      opacity: 0;
      -webkit-transition: opacity .2s ease-out 0s;
         -moz-transition: opacity .2s ease-out 0s;
              transition: opacity .2s ease-out 0s;
    }
    .card-content#collection-new {
      text-align: center;
    }
    .card-content#collection-new .card_darken {
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
      background-color: #b7b0b0;
      color: #fff;
      padding: 5px 20px;
      line-height: 1.4em;
      box-sizing: border-box;
      border: 0px solid transparent;
      text-decoration: none;
      border-radius: 4px;
      text-align: center;
      white-space: nowrap;
      vertical-align: middle;
      opacity: 1;
      -webkit-transition: all 0.30s ease-in-out;
         -moz-transition: all 0.30s ease-in-out;
          -ms-transition: all 0.30s ease-in-out;
           -o-transition: all 0.30s ease-in-out;
    }
    .btn:hover, .btn:focus, .btn.focus {
      text-decoration: none;
      background-color: #8e8b8b;
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

    .btn.btn_unfollow:hover, .btn.btn_unfollow:focus, .btn.btn_unfollow.focus {
      background-color: #8E8B8B;
    }

    .btn.btn_trove, .btn.btn_customize, .btn.btn_buy {
      padding: 5px 12px;
      -webkit-box-shadow: 0px 1px 2px 0px rgba(50, 50, 50, 0.17);
         -moz-box-shadow: 0px 1px 2px 0px rgba(50, 50, 50, 0.17);
              box-shadow: 0px 1px 2px 0px rgba(50, 50, 50, 0.17);
    }

    .btn.btn_buy {
      background-color: #B7B0B0;
    }

    .btn.btn_trove, .btn.btn_customize {
      background-color: #F26868;
    }

    .btn.btn_trove:hover, .btn.btn_trove:focus, .btn.btn_trove.focus, .btn.btn_customize:hover, .btn.btn_customize:focus, .btn.btn_customize.focus {
      background-color: #DD2435;
    }

    .btn.btn_buy:hover, .btn.btn_buy:focus, .btn.btn_buy.focus {
      background-color: #8E8B8B;
    }
    .btn.btn_save {
      background-color: #F26868;
    }
    .btn.btn_follow {
      width: 160px;
      background-color: #F26868;
    }
    .btn.btn_follow:hover, .btn.btn_follow:focus, .btn.btn_follow.focus, .btn.btn_save:hover, .btn.btn_save:focus, .btn.btn_save.focus {
      background-color: #DD2435;
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
    .card_content_img {
      width: 250px;
      height: 250px;
      background: #fff;
      -webkit-border-top-left-radius: 8px;
      -webkit-border-top-right-radius: 8px;
      -webkit-border-bottom-right-radius: 0px;
      -webkit-border-bottom-left-radius: 0px;
      -moz-border-radius-topleft: 8px;
      -moz-border-radius-topright: 8px;
      -moz-border-radius-bottomright: 0px;
      -moz-border-radius-bottomleft: 0px;
      border-top-left-radius: 8px;
      border-top-right-radius: 8px;
      border-bottom-right-radius: 0px;
      border-bottom-left-radius: 0px;
      overflow: hidden;
    }

    .card_image {
      height: 250px;
      display: block;
      margin: 0 auto;
    }
    .grid-cta {
      z-index: 4;
      position: absolute;
      display: inline-block;
      width: 100%;
      top: 10px;
      margin-left: -2px;
      text-align: center;
      opacity: 0;
      -webkit-transition: opacity .1s ease-out 0s;
         -moz-transition: opacity .1s ease-out 0s;
              transition: opacity .1s ease-out 0s;
    }
    .overlay:hover .grid-cta {
      opacity: 1;
    }

    .card_content_a {
      padding: 10px 15px 5px;
      margin: 10px -15px 10px;
      height: auto;
      border-bottom: 1px solid #f0f0f0;
      cursor: pointer;
      -webkit-transition: all 0.30s ease-in-out;
      -moz-transition: all 0.30s ease-in-out;
      -ms-transition: all 0.30s ease-in-out;
      -o-transition: all 0.30s ease-in-out;
    }

    .card_content_a a {
      color: #232323;
    }

    .card_product_name {
      font-size: 18px;
      padding: 0 0 4px;
      color: #232323;
      text-align: left;
    }

    .card_avatar_small {
      height: 30px;
      width: 30px;
      float: left;
      margin-right: 10px;
      margin-top: 2px;
      position: relative;
      border-radius: 50%;
    }

    .card_username {
      display: block;
      font-size: 16px;
      padding-top: 7px;
      margin-bottom: 3px;
    }

    .card_user_info a:hover {
      text-decoration: none;
    }

    .collection-container {
      width: 100%;
      background-color: #f0f0f0;
      padding: 20px 0 10px;
      -webkit-box-shadow: 0px 3px 8px 0px rgba(50, 50, 50, 0.2);
         -moz-box-shadow: 0px 3px 8px 0px rgba(50, 50, 50, 0.2);
              box-shadow: 0px 3px 8px 0px rgba(50, 50, 50, 0.2);
    }

    .collection-user-info {
      display: block;
      position: absolute;
      float: left;
      left: 20px;
      margin-top: 48px;
    }

    .made-by-username, .remade-by-username {
      margin-top: 7px;
      margin-left: 16px;
      display: inline-block;
      font-weight: 400;
      font-size: 18px;
    }

    .topnav a {
      border-top: 3px solid transparent;
    }

    .topnav a.current-page {
      border-top: 3px solid #f26868;
    }

    .card_content_b {
      padding: 0px 0px 0px;
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

    .follow-pic-holder {
      width: 100px;
      height: 100px;
      border-radius: 50%;
      overflow: hidden;
      margin: 30px 74px;
    }

    .profile-pic-img {
      max-height: 100%;
      max-width: 100%;
    }

    .collection_card_content_row1 {
      padding: 5px 0px 0px;
      height: auto;
    }

    .ownerimgholder {
      display: block;
      margin-top: 0px;

      height: 84px;
      max-height: 84px;
      padding: 7px;
      text-align: center;
      margin-bottom: 13px;
      overflow: hidden;
    }

    .ownerimg {
      width: 30%;
      height: 70px;
    }

    .card_darken.person {
      height: 194px;
    }

    #folwbutton {
      width: 212px;
      margin: 10px 18px 10px 18px;
      border-radius: 5px;
    }

    #ufolwbutton {
      width: 212px;
      margin: 10px 18px 10px 18px;
      border-radius: 5px;
    }

    .btn.btn--red {
      background-color: #F26868;
    }

    .btn.btn--red:hover, .btn.btn--red:focus, .btn.btn--red.focus {
      background-color: #DD2435;
    }

    .card_product_name.person {
      font-size: 18px;
      padding: 0px 0px 4px 15px;
    }

  </style>
</head>
<body>
  <c:import url="../fragments/nav/topNavBar.jsp"/>
  <c:import url="../fragments/profileCollectionTop.jsp"/>
  <!-- <input type="hidden" id="userId" value="${authUser.userId}" /> -->

  <div class="container-fluid">
    <div class="row items">
      
    </div>

    <div class="row followers">
      
    </div>
  </div>

  <script id="itemcards" type="text/x-handlebars">
    {{#each this}}
      <div class="card" id="card-{{itemId}}" style="opacity:0;">
        <div class="card-content" id="item-{{itemId}}">
          <div class="overlay">
            <a href="/public/productdescription/{{itemId}}"><div class="card_darken"></div></a>
            <div class="card_content_img"><img class="card_image" src="{{defaultCardImageUrl}}"></div>
            <div class="grid-cta">
              <c:if test="${not empty canFollowUser}"><button id="troveItem-{{itemId}}" onclick="TROVE.troveItem({{itemId}},'userprofile')" class="btn btn--red btn--small btn--card--overlay">Trove</button></c:if>
              <c:if test="${empty canFollowUser}">
                <!--<button id="editItem-{{itemId}}" onclick="editb()" class="btn btn--lightgray btn--small btn--card--overlay" data-toggle="modal" data-target="#modal2-{{itemId}}"><img src="https://storage.googleapis.com/trove-demo-cdn/img/edit-icon-white.svg" class="inline-icon-edit-sm sm">Edit</button>-->
              </c:if>
              <a href="/public/customize/webgl/{{itemId}}" alt="Customize this design"><button onclick="custb()" class="btn btn--red btn--small btn--card--overlay">Customize</button></a>
              <!--<button id="buyItem-{{itemId}}" onclick="buyb()" class="btn btn--lightgray btn--small btn--card--overlay cardbuybutton" data-toggle="modal" data-target="#modal-{{itemId}}">Buy</button>-->
            </div>


            <!--<div class="grid-cta">
              <button id="troveItem-{{itemId}}" onclick="troveItem({{itemId}})" class="btn btn_trove">Trove</button>
              <button id="customizeItem-{{itemId}}" onclick="customizeItem({{itemId}})" class="btn btn_customize">Customize</button>
              <button id="buyItem" data-toggle="modal" data-target="#modal-buyItem-{{itemId}}" class="btn btn_buy">Buy</button>
            </div>-->
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
    {{/each}}
  </script>

  <script id="followerscards" type="text/x-handlebars">
      {{#each this}}
        <div class="block card_mask follower" style="display: none;">
          <div class="card_column_single">
            <div class="overlay people"><a href="/private/user/{{userId}}"><div class="card_darken person" style="position: absolute"></div></a></div>
            <div class="card_content_a">
              <div class="card_product_name person"><a href="/private/user/{{userId}}">{{username}}</a></div>
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

  <script>

    function updateCollection(collectionId) {
      var data = new FormData();
      data.append('collectionId', collectionId)
      var nname = "editCollectionName";
      var ndescrip = "editCollectionDescription";
      var title = "#collection-name";
      var name = document.getElementById(nname).value;
      var descrip = document.getElementById(ndescrip).value;
      if ( name ) {
        data.append('collectionName', name);
      } else {
        data.append('collectionName', $(title).html());
      }

      if ( descrip ) {
        data.append('collectionDescription', descrip);
      } else {
        data.append('collectionDescription', "${collection.collectionDescription}");
      }

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
          alert("Successfully updated collection");
        }
      }).done( function() {
        $(title).html(name);
      });
    }

    // global vars
    var tabContext = 'item';
    var collectionId = ${collectionId};
    var authUserId = ${authUser.userId};
    var cardData;
    var followerData;
    var pageNum = 0;
    var followerPageNum = 0;
    var itemsPerPage = 12;
    var canContinueScrollingItems = true;
    var canContinueScrollingFollowers = true;
    var processing;
    var processingFollowers;

    function getCollectionItems() {
      if (!processing) {
        processing = true;
        var data = Object.create(null);
        data['collectionId'] = collectionId;
        data['pageNumber'] = pageNum;
        // data['objectId-0'] = 0;
        // data['objectId-1'] = 3;
        data['pageLimit'] = itemsPerPage;
        data['duplicateBuffer'] = 3;
        jQuery.ajax({
            url: '/collectionitems',
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
                cardData = data.collectionItems;
                pageNum = pageNum + 1;

              if (data.collectionItems.length < itemsPerPage) {
                canContinueScrollingItems = false;
              }

              processing = false;
            }
        }).done( function() {
          layoutBars();
        });
      }
    }

    function followCollection(collectionId) {
      var f = ".folbtn";
      var fs = f + " span";
      var foc = "followCollection('" + String(collectionId) + "');";
      var u = "ufolbtn";
      var us = u + " span";
      var uoc = "unfollowCollection('" + String(collectionId) + "');";
      var data = new FormData();
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
          $(fs).html('Unfollow Collection');
          $(f).attr( "onclick", uoc );
          $(f).attr( "id", "ufbuttonCollection");
          $(f).addClass('btn--lightgray');
          $(f).addClass('ufolbtn');
          $(f).removeClass('btn--red');
          $(f).removeClass('folbtn');
        }
      });
    }

    function unfollowCollection(collectionId) {
      var f = ".folbtn";
      var fs = f + " span";
      var foc = "followCollection('" + String(collectionId) + "');";
      var u = "ufolbtn";
      var us = u + " span";
      var uoc = "unfollowCollection('" + String(collectionId) + "');";
      var data = new FormData();
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
          $(us).html('Follow Collection');
          $(u).attr( "onclick", foc );
          $(u).attr( "id", "fbuttonCollection");
          $(u).addClass('btn--red');
          $(u).addClass('folbtn');
          $(u).removeClass('btn--lightgray');
          $(u).removeClass('ufolbtn');
        }
      });
    }

    function layoutBars() {
      var t = Handlebars.compile($('#itemcards').html());
      $('.row.items').append(t(cardData));
      setupCards('.card');
    }

    function getCollectionFollowers() {
      if (!processingFollowers) {
        processingFollowers = true;
        var data = Object.create(null);
        data['collectionId'] = collectionId;
        data['pageNumber'] = followerPageNum;
        // data['objectId-0'] = 0;
        data['pageLimit'] = itemsPerPage;
        data['duplicateBuffer'] = 3;
        jQuery.ajax({
            url: '/collectionfollowers',
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
                followerData = data.userList;
                followerPageNum = followerPageNum + 1;

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
      $('.row.followers').append(t(followerData));
      setupCards('.block.card_mask.follower');
    }

    var colCount = 0;
    var colWidth = 250;
    var margin = 30;
    var spaceLeft = 0;
    var windowWidth = 0;
    var blocks = [];
    
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
      $(selector).each(function(i){
        var min = Array.min(blocks);
        var index = $.inArray(min, blocks);
        var leftPos = margin+(index*(colWidth+margin));
        $(this).css({
          'left':(leftPos+spaceLeft)-40+'px',
          'top':min+$(".collection-container").height()+$(".navbar").height()+20+'px',
          'display':'inherit',
          'opacity':1
        });
        blocks[index] = min+$(this).outerHeight()+margin;
      }); 
    }

    Array.min = function(array) {
        return Math.min.apply(Math, array);
    };

    var tabContext = 'item';

    head.ready(document, function () {
      getCollectionItems();

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

      $('#itemstab').on('click', function(e){
        e.preventDefault();
        processing = false;
        tabContext = 'item';
        getCollectionItems();
        $('.row.items').fadeIn();
        $('.row.followers').hide();
        $('.current-page').removeClass('current-page');
        $('#itemstab').addClass('current-page');
        setupCards('.card');
      });

      $('#followerstab').on('click', function(e){
        e.preventDefault();
        processingFollowers = false;
        tabContext = 'followers';
        getCollectionFollowers();
        $('.row.followers').fadeIn();
        $('.row.items').hide();
        $('.current-page').removeClass('current-page');
        $('#followerstab').addClass('current-page');
        setupCards('.block.card_mask.follower');
      });

      $(window).scroll(function(e){
        switch (tabContext) {
            case 'item':
                if (!canContinueScrollingItems)
                  return false;
                if ($(window).scrollTop() >= ($(document).height() - $(window).height())*0.8){
                  getCollectionItems();
                }
                break;
            case 'followers':
                if (!canContinueScrollingFollowers)
                  return false;
                if ($(window).scrollTop() >= ($(document).height() - $(window).height())*0.8) {
                  getCollectionFollowers();
                }
                break;
            default:
                console.log(tabContext);
                break;
        }
        
      });

      $(window).on('resize', function() {
        setupCards('.card');
        setupCards('.block.card_mask.follower');
      });  
    });
  </script>

</body>

</html>

