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
            <h3>Partners</h3>
            <div class="row">
              <br></br>
              <div class="col-sm-6"><a href="http://www.shapeways.com/"><img style="width:180px;" alt="Shapeways" class="sw-logo" src="/resources/img/sw-logo.svg"></a></div>
              <div class="col-sm-6"><a href="http://i.materialise.com/"><img style="width:180px;" alt="iMaterialise" class="imat-logo" src="/resources/img/imat-logo.gif"></a></div>
            </div>
            <div class="row">
              <br></br>
             <!--  <div class="col-sm-6"><a href="https://twitter.com/voodoomfg"><img style="width: 116px; margin-top: 40px; margin-bottom: 30px;" alt="VooDoo" class="voodoo-logo" src="/resources/img/voodoo-logo.png"></a></div> -->
              <br></br>
            </div>
            </div>            
          </div>
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