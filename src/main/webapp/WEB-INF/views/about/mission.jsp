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
            <h3>Mission</h3>
            <p>
              <br>
              <span style="font-family:'Vollkorn', serif; font-size:25px; color: #8e8b8b; font-style: italic;">DEMOCRATIZING DESIGN<br><br></span>
              Trove was founded in order to bring the possibilities of 3D printing to a wider audience. While people are becoming increasingly aware of new digital fabrication technologies, widespread adoption of 3D printing is being hampered by the lack of relevance of the technology to people's everyday lives.<br><br>
              Trove seeks to find answers of "what's next" when people are shown 3D printers for the first time
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