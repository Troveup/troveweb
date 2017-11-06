<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Server Temporarily Unavailable</title>
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
</head>

<body>
  <c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
  <c:import url="../fragments/nav/topNavBar.jsp"/>
    <div class="container cards_product_wrapper2">
      <div class="row">
        <div class="col-sm-12">
          <div style="text-align: center;"> 
            <div class="breaker"><span></span></div>
            
            <h3 style="padding-top:10%;">Looks like you've found an error.</h3>
            <h5 style="padding-top:0px;">We're working on getting Trove back up and running.<br><br><br>Give us a few minutes and you'll be right back to designing beautiful jewelry!</h5>
            
          </div>
        </div>
      </div>
      
      <!-- <div class="row">
        <div class="col-sm-12">
          <div style="text-align: center;"> 
            <a href="/feed" class="btn btn--red">Go to your Feed</a>
          </div>
        </div>
      </div> -->

    </div>
  </body>
</html>