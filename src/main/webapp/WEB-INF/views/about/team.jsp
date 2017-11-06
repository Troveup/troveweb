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
            <h3>Team</h3>
            <p>
              <br>
              We believe in surounding ourselves with people who care as deeply about solving difficult problems as we do. We love what we do and the team that we've built to make Trove a reality.
              <br></br>
            </p>
            <div class="row">
              <div class="col-sm-4">
                <img class="img-circle" alt="140x140" src="https://storage.googleapis.com/troveup-imagestore/assets/img/brian-hs2.jpg" style="width: 140px; height: 140px;">
                <br>
                <span>Brian Park<br>CEO</span>
                <br><br>
              </div>
              <div class="col-sm-4">
                <img class="img-circle" alt="140x140" src="https://storage.googleapis.com/troveup-imagestore/assets/img/tim-hs2.jpg" style="width: 140px; height: 140px;">
                <br>
                <span>Tim Growney<br>Engineering</span>
                <br><br>
              </div>
              <div class="col-sm-4">
               <img class="img-circle" alt="140x140" src="https://storage.googleapis.com/troveup-imagestore/assets/img/andrew-hs2.jpg" style="width: 140px; height: 140px;">
                <br>
                <span>Andrew Hong<br>Design</span>
                <br><br>
              </div>
            </div>
            </br>
            </br>
            <div class="row">
              <div class="col-sm-4">
                <img class="img-circle" alt="140x140" src="https://storage.googleapis.com/troveup-imagestore/assets/img/ian-hs2.jpg" style="width: 140px; height: 140px;">
                <br>
                <span>Ian McLaughlin<br>Engineering</span>
                <br><br>
              </div>
              <div class="col-sm-4">
                <img class="img-circle" alt="140x140" src="https://storage.googleapis.com/troveup-imagestore/assets/img/justin-hs2.jpg" style="width: 140px; height: 140px;">
                <br>
                <span>Justin Lynch<br>Engineering</span>
                <br><br>
              </div>
              <div class="col-sm-4">
                <img class="img-circle" alt="140x140" src="https://storage.googleapis.com/troveup-imagestore/assets/img/kristin-headshot.png" style="width: 140px; height: 140px;">
                <br>
                <span>Kristin Sword<br>Community</span>
                <br><br>
              </div>
            </div>
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