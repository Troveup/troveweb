<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="trove" uri="http://troveup.com/jsp/tlds/itemimage" %>
<html>
<head>
  <title>Trove: 3D Printed Jewelry Customized for You</title>
  <%--<meta name="viewport" content="width=device-width, initial-scale=1">--%>
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=0">
  <link href='https://fonts.googleapis.com/css?family=Quicksand:300,400,700' rel='stylesheet' type='text/css'>
  <link href='https://fonts.googleapis.com/css?family=Raleway:400,200,300,500,600,700' rel='stylesheet' type='text/css'>
  <link rel="icon" href="/resources/img/favicon.png?v=2">
  <script src="https://code.jquery.com/jquery-2.1.3.js"></script>
  <script src="/resources/js/vendor/handlebars.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.13.1/jquery.validate.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.13.1/additional-methods.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="/resources/stylesheets/main.css">
  <link rel="stylesheet" href="/resources/stylesheets/browse.css">
  <link rel="stylesheet" href="/resources/stylesheets/spinner.css">
  <script type="text/javascript" src="/resources/js/vendor/head.js"></script>
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

    body {
      min-height: 1000px;
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

    * {
      font-family: "Raleway", Helvetica, Arial, sans-serif;
      letter-spacing: 1px;
      font-weight: 300;
      color: #232323;
      outline: 0;
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

    .row.orderContainer {
      background: #DEDEDE;
      margin-right: 0px;
      margin-left: 0px;
      margin-top: 30px;
      margin-bottom: 30px;
      padding-bottom: 20px;
    }

    .orderHeader {
      padding: 15px 20px 0px;
    }

    .orderDetails, .orderNum, .orderDate, .orderTotal {
      display: inline-block;
    }

    .orderTotal {
      margin-left: 50px;
    }

    .orderLinks {
      margin-top: 20px;
      float: right;
      display: inline-block;
    }

    .orderLinks li {
      text-decoration: underline;
      display: inline-block;
    }

    .orderItemLeft {
      min-height: 200px;
      background: #FFF;
      padding: 15px;
      width: 58%;
      border-right: 1px solid #DEDEDE;
      margin-left: 20px;
      float: left;
      margin-top: 10px;
      margin-bottom: 10px;
    }

    .orderItemRight {
      min-height: 200px;
      width: 38%;
      background: #FFF;
      padding: 15px;
      border-left: 1px solid #DEDEDE;
      margin-right: -40px;
      float: left;
      margin-top: 10px;
      margin-bottom: 10px;
    }

    img.orderItemPhoto {
      width: 120px;
      height: 120px;
      float: left;
      margin-right: 15px;
      margin-top: 15px;
      background: #DEDEDE;
    }

    .orderItemName {
      margin-top: 20px;
      display: inline-block;

    }

    .orderItemPrice, .orderItemSize, .orderItemMaterial {
      display: inline-block;
      font-style: italic;
    }

    .orderItemCancel {
      /*      float: right;
      */
      margin-top: 6px;
      margin-right: 9px;
      padding: 5px 10px 5px;
      border: 1px solid #b7b0b0;
    }

    .topnav a.current-page .user-info-count.category {
      color: #f26868;
    }

    .topnav a.current-page:hover .user-info-count.category {
      color: #DD2435;
    }

    .tryOnStatus {
      text-align: center;
      text-decoration: underline;
      margin-bottom: 20px;
    }

    .longButton {
      width: 100%;
      border-radius: 0px;
      margin-bottom: 20px;
      padding-top: 8px;
      padding-bottom: 8px;
    }

    .longButton span {
      color: #fff;
    }

    .halfButton {
      width: 48%;
      border-radius: 0px;
      padding-top: 7px;
      padding-bottom: 7px;
    }

    .halfButton.right {
      float: right;
    }

    .orderItemStatus {
      font-size: 18px;
      color: #DD2435;
    }

    .btn.longButton.purch {
      background-color: #F26868;
    }

    .btn.longButton.two {
      margin-bottom: 5px;
    }

    .btn.longButton.purch:hover, .btn.longButton.purch:focus, .btn.longButton.purch.focus {
      background-color: #DD2435;
    }

    .confirmNote {
      text-align: center;
      padding-bottom: 20px;
    }

    .topnav .user-info-count:hover span {
      color: #f26868;
    }

    .topnav .user-info-count:hover span {
      color: #f26868;
    }

    .topnav a.current-page .user-info-count span {
      color: #f26868;
    }

    .topnav a.current-page .user-info-count:hover span {
      color: #f26868;
    }

    .tryOnNum {
      text-align: center;
      padding-top: 10px;
    }

    .tryOnNum span {
      font-size: 80%;
      text-align: center;
      position: relative;
      display: block;
      margin-top: 15px;
      margin-right: 30px;
      margin-left: 30px;
    }

    @media (max-width: 768px) {

      .orderItemLeft, .orderItemRight {
        min-height: none;
        margin: 0px;
        padding: 15px;
        width: 100%;
        border: 0px solid transparent;
        border-bottom: 1px solid #DEDEDE;
        border-top: 1px solid #DEDEDE;
      }

      .orderItemLeft {
        margin-top: 20px;
      }

      .orderHeader {
        padding: 5px;
      }

      .btn.halfButton {
        width: 100%;
        margin-top: 15px;
        padding-top: 12px;
        padding-bottom: 12px;
      }

      .btn.longButton {
        width: 100%;
        margin-bottom: 10px !important;
        padding-top: 12px;
        padding-bottom: 12px;
      }

      .btn.halfButton.right {
        margin-top: 15px;
      }

      .row.orderContainer {
        padding: 15px;
        margin-left: 10px;
        margin-right: 10px;
        margin-top: 20px;
        margin-bottom: 40px;
      }

      .orderTotal {
        margin-left: 0px;
        display: block;
      }

      .img.orderItemPhoto {
        margin-right: 20px;
      }

    }

    body {
      padding-top: 100px;
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
    }

    .title {
      padding: 10px 0 10px;
      min-width: 200px;
      text-align: center;
      font-size: 24px;
      line-height: 24px;
      margin: 0 auto;
    }

    <%-- Used in userOrdersTemplate --%>
    .cancel-button-container {
      text-align: center;
      width: 87px;
      height: 28px;
      display: inline-block;
      border: 1px solid #b7b0b0;
      margin: auto;
      padding-top: 2px;
    }

    .cancel-text {
      color: black;
    }

    .cancel-text:hover {
      color: #DD2435;
      cursor: pointer; cursor: hand;
    }

  </style>
</head>
<body>
<c:import url="../fragments/nav/topNavBar.jsp"/>
<c:import url="../fragments/handlebars/userOrdersTemplates.jsp"/>

<div class="title">
    ORDERS
</div>

<div class="container">

  <!-- row for items -->
  <div class="row items"></div>
</div>

<script type="text/javascript" src="/resources/js/trove/AJAX.js"></script>
<script>
  var ajaxHelper = new AJAXHelper("${_csrf.token}");
  var itemsPerPage = 4;
  var pageNumber = 0;
  var processingItems = false;
  var endOfOrders = false;
  var orderStatusTitleAndDescriptions = JSON.parse('${orderStatusTitleAndDescriptions}');

  Handlebars.registerHelper('if_str', function(a, b, opts) {
    if (a == b) {
      return opts.fn(this);
    } else {
      return opts.inverse(this);
    }
  });

  Handlebars.registerHelper('if_not_null', function(a, opts) {
    if (a != null) {
      return opts.fn(this);
    } else {
      return opts.inverse(this);
    }
  });

  Handlebars.registerHelper('moneydecimalfix', function(arg, options) {
    return parseFloat(arg).toFixed(2);
  });

  var handlebarsTemplate = Handlebars.compile($('#itemcards').html());

  function getOrders() {
    processingItems = true;
    var dataObject = ajaxHelper.createOrAppendGetDataObject("orderStatus", "ALL", null);
    dataObject = ajaxHelper.createOrAppendGetDataObject("pageNumber", pageNumber, dataObject);
    dataObject = ajaxHelper.createOrAppendGetDataObject("pageLimit", itemsPerPage, dataObject);


    ajaxHelper.performGetRequest(dataObject, "/orders", createGetOrderItemsSuccessCallback(), createGetOrderItemsFailedCallback());
  }

  function createGetOrderItemsSuccessCallback() {
    return function(data) {
      pageNumber = pageNumber + 1;
      processingItems = false;
      layoutItemBars(data.orders);

      if (data.orders.length < itemsPerPage) {
        endOfOrders = true;
      }
    };
  }

  function createGetOrderItemsFailedCallback() {
    return function(data) {
      location.reload();
    }
  }

  function layoutItemBars(itemData) {
    $('.row.items').append(handlebarsTemplate(itemData));
  }

  $(document).ready(function () {
    tabContext = 'item';
    getOrders();

    $(window).scroll(function (e) {
      if (!processingItems && !endOfOrders && ($(window).height() - $(window).scrollTop() <= ($(document).height()) * 0.70)) {
        processingItems = true;
        getOrders();
      }
    });
  });

  function cancelOrderLineItem(orderItemId) {
    showCancelButtonSpinner(orderItemId);

    mixpanel.track("orders_currentorders_cancel");
    var dataObject = ajaxHelper.createOrAppendPostDataObject("orderItemId", orderItemId, null);

    ajaxHelper.performPostRequest(dataObject, "/cancelorderlineitem", createCancelOrderLineItemSuccessCallback(orderItemId), createCancelOrderLineItemFailedCallback(orderItemId));
  }

  function createCancelOrderLineItemSuccessCallback(orderItemId) {
    return function(data) {
      var orderItemRowSelector = $('#order-item-row-' + orderItemId);
      orderItemRowSelector.find('.orderItemStatus').text('Status: Cancelled');

      var rightSideTitle = $('<div class="tryOnStatus"></div>');
      rightSideTitle.text(orderStatusTitleAndDescriptions["ORDERED"].title);

      var rightSideDescription = $('<div class="tryOnNum"></div>');
      rightSideDescription.text(orderStatusTitleAndDescriptions["ORDERED"].description);

      orderItemRowSelector.find('.order-line-item-right-side')
          .empty()
          .append(rightSideTitle)
          .append(rightSideDescription)
          .show(200);

      orderItemRowSelector.find('.cancel-button-container').hide(200);
    }
  }

  function createCancelOrderLineItemFailedCallback(orderItemId) {
    return function(data) {
      $('#cancel-order-btn-text-' + orderItemId).show(200);
      $('#cancel-button-spinner-' + orderItemId).hide(200);

      if (data != null && data.errorMessage != null) {
        alert(data.errorMessage);
      } else {
        alert("Oops!  Something went wrong with your item cancellation.  Please try again.");
      }
    }
  }

  function showCancelButtonSpinner(orderItemId) {
    $('#cancel-order-btn-text-' + orderItemId).hide(200);
    $('#cancel-button-spinner-' + orderItemId).show(200);
  }

  function setRightSideLineItemSpinner(orderItemId) {
    var orderItemRow = $('#order-item-row-' + orderItemId);
    orderItemRow.find('.spinner').show(200);
    orderItemRow.find('.order-line-item-right-side').hide(200);
  }

  function hideRightSideLineItemSpinner(orderItemId) {
    var orderItemRow = $('#order-item-row-' + orderItemId);
    orderItemRow.find('.spinner').hide(200);
    orderItemRow.find('.order-line-item-right-side').show(200);
  }

  function completeOrderLineItem(orderItemId){
    mixpanel.track("orders_currentorders_sendmyjewelry");

    setRightSideLineItemSpinner(orderItemId);

    var dataObject = ajaxHelper.createOrAppendPostDataObject("orderItemId", orderItemId, null);
    ajaxHelper.performPostRequest(dataObject, "/completeorderlineitem", createCompleteOrderLineItemSuccessCallback(orderItemId), createCompleteOrderLineItemFailureCallback(orderItemId));
  }

  function createCompleteOrderLineItemSuccessCallback(orderItemId) {
    return function(data) {
      var orderItemRow = $('#order-item-row-' + orderItemId);

      var rightSideTitle = $('<div class="tryOnStatus"></div>');
      rightSideTitle.text(orderStatusTitleAndDescriptions["ORDERED"].title);

      var rightSideDescription = $('<div class="tryOnNum"></div>');
      rightSideDescription.text(orderStatusTitleAndDescriptions["ORDERED"].description);

      orderItemRow.find('.order-line-item-right-side')
          .empty()
          .append(rightSideTitle)
          .append(rightSideDescription)
          .show(200);

      orderItemRow.find('.spinner').hide(200);
      orderItemRow.find('.orderItemStatus').text('Status: Ordered');
    }
  }

  function createCompleteOrderLineItemFailureCallback(orderItemId) {
    return function(data) {
      hideRightSideLineItemSpinner(orderItemId);

      if (data != null && data.errorMessage != null) {
        alert(data.errorMessage);
      } else {
        alert("Oops!  Something went wrong with your order item confirmation.  Please try again.");
      }
    }
  }

</script>


</body>
</html>


