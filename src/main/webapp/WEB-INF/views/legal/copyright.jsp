<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Trove: Legal - ${pageTitle}</title>
  <c:import url="../fragments/headers/commonHead.jsp"/>
  <c:import url="../fragments/headers/legalHead.jsp"/>
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
        <c:import url="../fragments/nav/legalNav.jsp"/>

        <div class="col-sm-9">
          <div class="cardy privacy"> 
            <h3>Trove Copyright Policy</h3>
            <p>Last Updated: September 30th, 2015</p>
            <br></br>
            <u>Notification of Copyright Infringement:</u>
            <p>Trove, Inc. (“Trove”) respects the intellectual property rights of others and expects its users to do the same.</p>
            <p>It is Trove’s policy, in appropriate circumstances and at its discretion, to disable and/or terminate the accounts of users who repeatedly infringe or are repeatedly charged with infringing the copyrights or other intellectual property rights of others.</p>
            <p>In accordance with the Digital Millennium Copyright Act of 1998, the text of which may be found on the U.S. Copyright Office website at http://www.copyright.gov/legislation/dmca.pdf, Trove will respond expeditiously to claims of copyright infringement committed using the Trove website (the “Site”) that are reported to Trove’s Designated Copyright Agent, identified in the sample notice below. </p>
            <p>If you are a copyright owner, or are authorized to act on behalf of one, or authorized to act under any exclusive right under copyright, please report alleged copyright infringements taking place on or through the Site by completing the following DMCA Notice of Alleged Infringement and delivering it to Trove’s Designated Copyright Agent.  Upon receipt of the Notice as described below, Trove will take whatever action, in its sole discretion, it deems appropriate, including removal of the challenged material from the Site.</p>

            <u>DMCA Notice of Alleged Infringement (“Notice”)</u>
            <ul>
              <li>Identify the copyrighted work that you claim has been infringed, or - if multiple copyrighted works are covered by this Notice - you may provide a representative list of the copyrighted works that you claim have been infringed.</li>
              <li>Identify the material that you claim is infringing (or to be the subject of infringing activity) and that is to be removed or access to which is to be disabled, and information reasonably sufficient to permit us to locate the material, including at a minimum, if applicable, the URL of the link shown on the Site(s) where such material may be found.</li>
              <li>Provide your mailing address, telephone number, and, if available, email address.</li>
              <li>Include both of the following statements in the body of the Notice:</li>
                <ul>
                  <li>o “I hereby state that I have a good faith belief that the disputed use of the copyrighted material is not authorized by the copyright owner, its agent, or the law (e.g., as a fair use).”</li>
                  <li>o “I hereby state that the information in this Notice is accurate and, under penalty of perjury, that I am the owner, or authorized to act on behalf of the owner, of the copyright or of an exclusive right under the copyright that is allegedly infringed.”</li>
                </ul>
              <li>Provide your full legal name and your electronic or physical signature.</li>
            </ul>

            <p>Deliver this Notice, with all items completed, to Trove’s Designated Copyright Agent:</p>
            <p>Copyright Agent<br>
               c/o Trove, Inc.<br>
               20 Exchange Place Apt. 1604<br>
               New York NY, 10005<br>
               email: hello@troveup.com
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