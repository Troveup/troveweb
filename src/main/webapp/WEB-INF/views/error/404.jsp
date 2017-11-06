<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Page Not Found</title>
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
            
            <h3 style="padding-top:10%;">Oops!</h3>
            <h5 style="padding-top:0px;padding-bottom:10px;">Let us guess - this isn't the page you're looking for. <br><br><br>Instead, see what others are designing on Trove right now!</h5>
            
          </div>
        </div>
      </div>
      
      <div class="row">
        <div class="col-sm-12">
          <div style="text-align: center;"> 
            <a href="/feed" class="btn btn--red">Go to your Feed</a>
          </div>
        </div>
      </div>

    </div>
  </body>
</html>