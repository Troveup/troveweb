<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Trove: Welcome to Trove</title>
  <c:import url="../fragments/headers/commonHead.jsp"/>
  <c:import url="../fragments/headers/legalHead.jsp"/>
  <c:import url="../fragments/analytics/all.jsp"/>
  <style>

    .special-container {
      display: none;
    }

    span.bold {
      font-weight: bold;
    }

    span.dataheading {
      text-decoration: underline;
      float: left;
    }

    span.data {
      float: left;
      padding-left: 5px;
    }

    span.itemdataheading {
      display: inline-block;
      text-decoration: underline;
    }

    span.itemdata {
      display: inline-block;
      padding-left: 5px;
    }

    span.resultheading {
      font-weight: bold;
      text-decoration: underline;
      text-align: center;
      display: block;
      margin-bottom: 20px;
      padding-top: 10px;
    }

    .data {
      word-wrap: break-word;
    }

    .selectactive {
      border-style: solid;
      border-left-style: none;
      border-right-style: none;
      border-top-style: none;
    }

    .selectorcontainer {
      padding-bottom: 10px;
      margin-bottom: 30px;
    }

    .selector {
      display: inline-block;
    }

    span.selectortext {
      display: inline-block;
      vertical-align: middle;

      line-height: normal;
    }

    .checkoutselector {
      margin-right: 10px;
      position: absolute;
    }

    .orderitemselector {
      position: absolute;
      right: 0;
      top: 0;
    }

    img.orderitemimage {
      display: inline-block;
      margin: auto;
      max-width: 150px;
      max-height: 150px;
      vertical-align: top;
    }

    .orderitems {
      padding-bottom: 10px;
      display: inline-block;
    }

    button.labelsbutton {
      border-radius: 10px;
      display: block;
      margin: auto;
      margin-top: 10px;
    }

    button.labelsbutton:hover {
      background-color: lightpink;
      transition: background-color 0.5s ease;
    }

  </style>
</head>

<body>
<c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
<c:import url="../fragments/nav/topNavBar.jsp"/>
<div class="container-fluid" style="margin-top: 50px;">
  <div class="container">
    <div id="checkoutdetailscontainer">
      <div id="error-container"></div>
      <div class="row">
        <div class=".col-xs-6 .col-md-4"></div>
        <div class=".col-xs-6 .col-md-4">
          <div class="well">
            <span class="resultheading">Shipping Address</span>
            <div class="data">
              ${order.shippingAddress.firstName} <c:if
                test="${not empty order.shippingAddress.lastName}">${order.shippingAddress.lastName}</c:if><br>
              <c:if test="${not empty order.shippingAddress.email}">${order.shippingAddress.email}
                <br></c:if>
              <c:if test="${not empty order.shippingAddress.phone}">${order.shippingAddress.phone}
                <br></c:if>
              ${order.shippingAddress.addressLine1}<br>
              <c:if test="${not empty order.shippingAddress.addressLine2}">${order.shippingAddress.addressLine2}
                <br></c:if>
              ${order.shippingAddress.city}, ${order.shippingAddress.subdivision.code} ${order.shippingAddress.postalCode}
              <button id="create-label-button" class="labelsbutton" onclick="createLabel(${order.orderId});">Create New Label</button>
            </div>
          </div>
        </div>
      </div>
      <span class="resultheading">Attached Labels</span>
      <c:forEach items="${labels}" var="label">
        <div class="row">
          <div class="well">
            <span class="dataheading">Label ID:</span><span class="data">${label.easyPostPostageLabelPrimaryKey}</span><br>
            <span class="dataheading">Created Date:</span><span class="data">${label.creationDate}</span><br>
            <span class="dataheading">Last Updated:</span><span class="data">${label.lastUpdated}</span><br>
            <span class="dataheading">Tracking Number:</span><span class="data"><a
              href="" onclick="window.open('https://tools.usps.com/go/TrackConfirmAction_input?qtc_tLabels1=${label.trackingNumber}');">${label.trackingNumber}</a></span><br>
            <span class="dataheading">Status:</span><span class="data">${label.status}</span><br>
            <a href="" onclick="window.open('${label.labelUrl}');">Download Link</a><br>
          </div>
        </div>
      </c:forEach>
    </div>
  </div>
</div>
</body>

<script type="text/javascript" src="/resources/js/trove/AJAX.js"></script>
<script>

  var errorContainer = $('#error-container');
  var ajaxHelper = new AJAXHelper("${_csrf.token}");

  function generateLabelSuccessCallback() {
    return function(data) {
      window.open(data.label.labelUrl);
      location.reload(true);
    }
  }

  function createLabel(orderId) {
    var dataObject = ajaxHelper.createOrAppendPostDataObject('orderId', orderId);

    ajaxHelper.performPostRequest(dataObject, '/admin/ajax/createorderlabel', generateLabelSuccessCallback(),
      ajaxHelper.createGenericFailureCallback(errorContainer));

    $('#create-label-button').text('...Creating...').prop('disabled', true);
  }

</script>
</html>