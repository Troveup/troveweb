<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Trove: ${pageTitle}</title>
  <c:import url="../fragments/headers/commonHead.jsp"/>
  <c:import url="../fragments/headers/aboutHead.jsp"/>
  <c:import url="../fragments/analytics/all.jsp"/>

  <style>
   .special-container p {
     margin-bottom: 5px;
    }

  </style>
</head>

<body>
  <c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
  <c:import url="../fragments/nav/topNavBar.jsp"/>
    <div class="container cards_product_wrapper2">
      <div class="row">
        <c:import url="../fragments/nav/aboutNav.jsp"/>
        <div class="col-sm-9">
          <div class="cardy about"> 
            <h3>Press</h3>
            <p>
              <br>
              Interested in writing about or learning more about Trove?<br>
              Contact us at <a href="mailto:press@troveup.com?Subject=Press%20Inquiry" target="_top">press@troveup.com</a>
              <br></br>
            </p>
          </div>
          <br></br>
          <br></br>
          <br></br>
          <br></br>
        </div>
      </div>
    </div>

    <script>
      function bodyPadding(){
        var navHeight = $('.navbar').height();
        var bannerHeight = $('.special-container').outerHeight();
        var totalHeight = navHeight + bannerHeight;
        $('body').css("padding-top", totalHeight + "px");
      };

      $(document).ready(function() {
        bodyPadding();
        window.addEventListener('resize', bodyPadding);
      });
    </script>
  </body>
</html>