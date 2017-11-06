<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Trove: Try-on Models</title>
  <c:import url="../fragments/headers/commonHead.jsp"/>
  <c:import url="../fragments/headers/legalHead.jsp"/>
  <c:import url="../fragments/analytics/all.jsp"/>
  <style>

  .thumbnail>img {
    width: 130px;
    padding: 10px;
  }

  .thumbnail {
    border: 0px solid #FFF;
  }

   .special-container p {
     margin-bottom: 5px;
    }

    .try-section{
      margin-bottom:40px;
    }

 /* .cards_product_wrapper2 {
      margin-top: 75px;
  }*/

  .fullbanner {
      display: none;
  }
  .mobilebanner {
      display: block;
  }

  .try-section.mobile{
    display:none;
  }

@media screen and (max-width: 990px) {
  .try-section.desktop {
    display:none;
  }

  .try-section.mobile{
    display:block;
  }

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
            <h3>TRY-ON MODELS</h3>
            <div class="breaker"><span></span></div>
            <h1 style="font-family:'vollkorn','arial',serif;font-style:italic;">Your Jewelry. Your Way.</h1>
          </div>
        </div>
      </div>

<!--       <div class="row">
        <div style="position:relative;margin:0 auto;">
          <img src="https://console.developers.google.com/m/cloudstorage/b/troveup-imagestore/o/assets/img/try-on-photo-bg.jpg" class="fullbanner">
        </div>
      </div> -->

      <div class="row">
        <div class="col-sm-12">
          <h4 style="margin:20 0 50;text-align:center;">Shop for jewelry and try on a sample before you commit to your customized piece.<br>Change anything you don't like before we send your final jewelry.</h4>
        </div>
      </div>

      <div class="row">
        <div class="col-sm-12">    
          <div style="text-align: center;margin:0 0 50;"> 
            <a href="/feed" class="btn btn_big btn_big_red" style="width:inherit; font-size:16px;">Browse and Shop Jewelry</a>
          </div> 
        </div>
      </div>

      <div class="row  try-section">
        <div class="col-xs-12 col-md-6">
          <div class="thumbnail">
            <img src="https://storage.googleapis.com/troveup-qa-cdn/shopping-bag-try.svg">
          </div>
        </div>
        <div class="col-xs-12 col-md-6">
          <div class="caption">
            <h3 style="text-align: center;">Shop</h3>
            <p>Add a free Try-On model for each of your purchases at checkout. Check the box in your shopping bag and we will send you a 3D-printed plastic model of your jewelry.</p>
          </div>
        </div>
      </div>

      <div class="row try-section desktop">
        <div class="col-xs-12 col-md-6">
          <div class="caption">
            <h3 style="text-align: center;">Try On</h3>
            <p>After wearing your Try-on Model, go to your <span style="font-style:italic;">Orders and Try-On</span> page to confirm or modify your design. Once you confirm the Try-On Model we will handcraft your final piece.</p>
          </div>
        </div>
        <div class="col-xs-12 col-md-6">
          <div class="thumbnail">
            <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/hand-w-ring-outline.svg">
          </div>
        </div>
      </div>

      <div class="row try-section mobile">
        <div class="col-xs-12 col-md-6">
          <div class="thumbnail">
            <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/hand-w-ring-outline.svg">
          </div>
        </div>
        <div class="col-xs-12 col-md-6">
          <div class="caption">
            <h3 style="text-align: center;">Try On</h3>
            <p>After wearing your Try-on Model, go to your <span style="font-style:italic;">Orders and Try-On</span> page to confirm or modify your design. Once you confirm the Try-On Model we will handcraft your final piece.</p>
          </div>
        </div>
      </div>

      <div class="row  try-section">
        <div class="col-xs-12 col-md-6">
          <div class="thumbnail">
            <img src="https://storage.googleapis.com/troveup-qa-cdn/gift-box.svg">
          </div>
        </div>
        <div class="col-xs-12 col-md-6">
          <div class="caption">
            <h3 style="text-align: center;">Love</h3>
            <p>Try-On Models are free with purchase. If you cancel your order after receiving the Try-On Model, you will be refunded for your item minus the cost of the Model ($7.49)</p>
          </div>
        </div>
      </div>



<!--         <div class="col-xs-12 col-md-4">
          <div class="thumbnail">
            <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/hand-w-ring-outline.svg">
            <div class="caption">
              <h3 style="text-align: center;">Try On</h3>
              <p>After wearing your Try-on Model, go to your <span style="font-style:italic;">Orders and Try-On</span> page to confirm or modify your design. Once you confirm the Try-On Model we will handcraft your final piece.</p>
            </div>
          </div>
        </div>
        <div class="col-xs-12 col-md-4">
          <div class="thumbnail">
            <img src="https://storage.googleapis.com/troveup-qa-cdn/gift-box.svg">
            <div class="caption">
              <h3 style="text-align: center;">Love</h3>
              <p>Try-On Models are free with purchase. If you cancel your order after receiving the Try-On Model, you will be refunded for your item minus the cost of the Model ($7.49)</p>
            </div>
          </div>
        </div> -->
      
      
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