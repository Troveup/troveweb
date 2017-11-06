<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Trove: RSVP Confirmation</title>
  <c:import url="../fragments/headers/commonHead.jsp"/>
  <c:import url="../fragments/headers/legalHead.jsp"/>
  <c:import url="../fragments/analytics/all.jsp"/>
  <style>

  .thumbnail>img {
    width: 130px;
    padding: 10px;
  }

  .thumbnail {
    text-align: center;
    border: 0px solid #FFF;
  }

  </style>
  
  <c:if test="${isAuthenticated}">
    <style>
      .cards_product_wrapper2 {
          margin-top: 30px;
      }
    </style>
  </c:if>

  <c:if test="${not isAuthenticated}">
    <style>
      .cards_product_wrapper2 {
          margin-top: 75px;
      }
    </style>
  </c:if>
  
</head>

<body>
  <c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
  <c:import url="../fragments/nav/topNavBar.jsp"/>
    <div class="container cards_product_wrapper2">
      <div class="row">
        <div class="col-sm-12">
          <div style="text-align: center;"> 
            <h3>Thank you for RSVPing for Tea with Trove!</h3>
            <div class="breaker"><span></span></div>
            <h1 style="font-family:'vollkorn'; font-style:italic;">We look forward to seeing you soon.</h1>
            <h5 style="padding-top:20px;">Wednesday, November 4<br>7:00-8:00pm EST<br><br><a href="https://join.me/teawithtrove" target="_blank">https://join.me/teawithtrove</a></h5>
            <br></br>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>