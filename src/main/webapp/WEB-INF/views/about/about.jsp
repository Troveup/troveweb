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
            <h3>About Trove</h3>
            <div class="videoRow" style="overflow: hidden;">
              <video style="width: 100%; margin-top: -10px; margin-bottom: -10px;" autoplay="" poster="https://storage.googleapis.com/troveup-imagestore/assets/video/trove-vid.jpg" id="bgvid" loop="" class="desktop-only">
                <source src="https://storage.googleapis.com/troveup-imagestore/assets/video/trove-vid.webm" type="video/webm">
                <source src="https://storage.googleapis.com/troveup-imagestore/assets/video/trove-vid.mp4" type="video/mp4">
              </video>
            </div>
            <p>
              <br></br>
              Trove makes 3D printing and design accessible to anyone regardless of experience or ability.<br></br>
              Trove is an online platform for discovering, sharing, and customizing 3D printed jewelry designs. Users can browse through a variety of design templates and use our intuitive in-browser customization software to personalize their jewelry to perfectly fit their style.<br></br>
              Once satisfied with their design, users can have their jewelry 3D printed in materials ranging from sterling silver to 18K gold and have their unique jewelry shipped directly to them in a matter of weeks.<br></br>
              Any design created on Trove becomes a part of our social stream, where users can explore and discover designs customized by others on Trove. They can then further customize these designs, allowing products to grow and change over time. Users can also follow each other to keep up-to-date with trending designs and new releases.<br></br>
              <br></br>
              <a href="mailto:hello@troveup.com?Subject=Hello%20there!" target="_blank"><img src="https://storage.googleapis.com/troveup-imagestore/assets/img/email-icon.svg" style="height:15px;margin-right:5px;position:relative;top:-2px;"> hello@troveup.com</a>
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