<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
    <c:import url="../fragments/headers/commonHead.jsp"/>
    <c:import url="../fragments/headers/legalHead.jsp"/>
    <title>${pageTitle}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=0">
    <link rel="icon" href="/resources/img/favicon.png?v=2">
    <link href='https://fonts.googleapis.com/css?family=Raleway:400,200,300,500,600,700' rel='stylesheet' type='text/css'>
    <link href="https://fonts.googleapis.com/css?family=Vollkorn:400,400italic,700,700italic" rel="stylesheet" type="text/css">
    <c:import url="../fragments/analytics/all.jsp"/>

    <style>
      .ion-quote:before {
        content: "";
        background-image: url('https://storage.googleapis.com/troveup-imagestore/assets/img/quote.png');
        width: 44px;
        height: 40px;
        background-repeat: no-repeat;
        background-size: contain;
      }

      body {
        padding-bottom: 0px;
        font-family: "Raleway",Helvetica,Arial,sans-serif;
      }
      .container {
        text-align: center;
      }
      .col-sm-4 {
        padding: 20px;
      }
      h1 {
        font-size: 26px;
        letter-spacing: .06em;
        margin-bottom: 4px;
        text-align: center;
      }

      h2 {
        font-family: "Raleway",Helvetica,Arial,sans-serif;
        font-size: 13px;
        letter-spacing: .06em;
        margin-bottom: 10px;
        margin-top: 0px;
        color: #4E4E4E;

      }
      h2 span {
        display: block;
        width: 100%;
        text-align: center;
        font-size: 90%;
        font-weight: 300;
        padding-top: 10px;
        margin-bottom: 15px;
      }
      a:hover {
        color: #4E4E4E;
        text-decoration: none;
      }

      h3 {
        margin-top: 0px;
        color: rgb(129, 130, 130);
        font-size: 17px;
        letter-spacing: 0.08em;
      }

      .navbar-default {
        background-color: #B2B8B8;
        border-color: #B2B8B8;
      }

      button.left {
        border: 1px solid #FFF;
        background: transparent;
        color: #FFF;
        padding: 10px 30px;
        text-transform: uppercase;
        font-size: 16px;
        letter-spacing: 0.08em;
        -webkit-font-smoothing: antialiased;
        margin-top: 15px;
        margin-left: 15px;
        outline: 0;
        cursor: pointer;
        -webkit-transition: all 0.3s ease-in-out;
           -moz-transition: all 0.3s ease-in-out;
            -ms-transition: all 0.3s ease-in-out;
             -o-transition: all 0.3s ease-in-out;
                transition: all 0.3s ease-in-out;
      }
      button.left:hover {
        background: #FFF;
        color: #666;
      }
      button.right {
        border: 1px solid #DD2435;
        background-color: #DD2435;
        color: #FFF;
        padding: 10px 30px;
        text-transform: uppercase;
        font-size: 16px;
        letter-spacing: 0.08em;
        -webkit-font-smoothing: antialiased;
        margin-top: 15px;
        margin-right: 15px;
        outline: 0;
        cursor: pointer;
        -webkit-transition: all 0.3s ease-in-out;
           -moz-transition: all 0.3s ease-in-out;
            -ms-transition: all 0.3s ease-in-out;
             -o-transition: all 0.3s ease-in-out;
                transition: all 0.3s ease-in-out;
      }

      button.right:hover {
        background-color: #F26868;
        border-color: #F26868;
      }

      button.left:hover a {
        background: #FFF;
        color: #666;
      }

      button.right:hover a {
        background-color: #F26868;
        border-color: #F26868;
      }

      @media (max-width: 768px) {
        h3 {
          font-size: 12px;
        }
        .col-xs-0 {
          display: none;
        }
      }

      .uparrow {
        text-align: center;
        padding-bottom: 10px;
        padding-top: 10px;
        cursor: pointer;
        font-weight: 400;
        letter-spacing: 0.06em;
        color: #666666;
      }

      .uparrow span {
        padding-left: 10px;
        color: #666666;
        font-size: 20px;
      }

      h4 {
        margin: 50px 25%;
        font-family: "Raleway",Helvetica,Arial,sans-serif;
        letter-spacing: .06em;
        text-align: center;
        text-transform: uppercase;
        height: auto;
        padding-top: 50px;
        padding-bottom: 50px;
        border: 1px solid #555;
        margin-bottom: 50px;
        font-size: 1.3em;
        font-weight: 300;
        line-height: 1.2em;
      }

      h4 span {
        font-family: 'vollkorn';
        font-size: 1.6em;
        line-height: 1.4em;
      }

      h4 p {
        text-transform: none;
        margin-top: 25px;
        font-size: 90%;
        padding: 0px 10%;
      }
      h5 {
        padding-bottom: 15px;
        margin-top: 0px;
        margin-bottom: 0px;
      }

      @media (min-width: 768px) {
        .col-sm-0, .col-md-0, .col-lg-0 {
          display: none;
        }
      }

      .uparrow a {
        color: #666;
        text-decoration: none;
        border: 0px solid #FFF;
        outline: 0;
        line-height: 30px;
        vertical-align: top;
      }

       a button {
        color: #FFF;
        text-decoration: none;
        -webkit-transition: all 0.3s ease-in-out;
           -moz-transition: all 0.3s ease-in-out;
            -ms-transition: all 0.3s ease-in-out;
             -o-transition: all 0.3s ease-in-out;
                transition: all 0.3s ease-in-out;
      }

      a button:hover, a button:focus, a button:active {
        color: #FFF;
        text-decoration: none;
      }

      a:hover button.left , a:focus button.left , a:active button.left  {
        color: #666;
      }

      .spinstuff {
        opacity: 0.0;
        -webkit-transition: 1s opacity;
                transition: 1s opacity;
        pointer-events: none;
      }
      .choosestuff {
        opacity: 0.0;
        -webkit-transition: 1s opacity;
                transition: 1s opacity;
      }

      .choosestuff.show {
        display: block;
        opacity: 1.0;
      }

      .spinstuff.show {
        display: block;
        opacity: 1.0;
      }

      @media (min-width: 1200px) {
        .container {
            width: 970px;
        }
        h4 {
          margin: 50px;
        }
      }

      @media (max-width: 1200px) {
        h4 {
          margin: 50px;
        }
      }

      .ftui-nav {
        z-index: 999;
        background: rgb(225, 228, 233);
        border-bottom: 1px solid rgb(208, 211, 216);
        height: 60px;
        width: 100%;
        text-align: center;
        position: fixed;
        top: 0px;
        left: 0px;
        right: 0px;
      }

      .steps {
        height: 40px;
        line-height: 16px;
        padding: 0px;
        margin-top: 10px;
        color: #FFF;
        display: none;
        text-align: center;
        width: auto;
      }

      .options-holder {
        width: 100%;
        text-align: center;
        height: 100%;
        margin-top: 60px;
      }

      .options {
        width: 700px;
        height: 1040px;
        margin: 12.5px auto;
      }

      .option {
        width: 200px;
        height: 200px;
        display: inline-block;
        border: 1px solid #FFF;
        margin: 12.5px;
        line-height: 200px;
        font-size: 30px;
        color: #666;
        cursor: pointer;
        -webkit-transition: all .4s ease;
        -moz-transition: all .4s ease;
        transition: all .4s ease;
      }

      .option:hover {
        border: 1px solid #DEDEDE;
      }

      .option.chosen {
        border: 1px solid #DD2435;
      }

      .step {
        display: inline-block;
        font-size: 13px;
        min-width: 180px;
        font-weight: 500;
        color: #656565;
        letter-spacing: 0.06em;
      }

      .step span {
        font-size: 25px;
        color: #FFF;
      }

      .step.active {
        color: #F26968;
      }

      .step.active span {
        color: #F26968;
      }
      .gemIcon {
        width: 30px;
      }

      .currentStep {
        z-index: 1000;
        position: fixed;
        top: 12px;
        right: 10px;
        padding: 8px 20px;
        background: transparent;
        color: #656565;
        letter-spacing: 0.15em;
        font-size: 15px;
        -webkit-font-smoothing: antialiased;
        font-weight: 500;
        -webkit-transition: .3s all;
                transition: .3s all;
        display: none;
        font-family: Helvetica, Arial, sans-serif;
      }



      @media screen and (min-width: 768px) {
        .steps {
          display: inline-block;
        }
      }

      @media screen and (max-width: 767px) {
        .currentStep {
          display: inline-block;
        }
        h4 {
          font-size: 1em;
          margin: 40px 10px;
        }
        #footer-block, #footer-connect {
          display: none;
        }
        #footer-support {
          width: 100%;
          line-height: 2em;
        }
      }

      .row.landingbanner {
        background: #d9edf7;
        padding: 0 20 0 20;
        background-image: url('https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/profile/chrisdombg2.jpg');
        background-size: cover;
        background-repeat: repeat-x;
      }

      .lander {
        background: #FFF;
        margin-top: 40px;
        padding: 20px;
        max-width: 500px;
        margin-left: auto;
        margin-right: auto;
        text-align: center;
        position: relative;
        margin: 50 auto;
      }

      .navbar-toggle {
        position: fixed;
        float: left;
      }

      .row.quotebanner {
        background: #FFF;
        height: 400px;
      }

      .quote {
        position: relative;
        border: 2px solid #F37777;
        background: #FFF;
        padding: 21px;
        width: 600px;
        margin-left: auto;
        margin-right: auto;
        text-align: center;
        margin-top: 100px;
      }

      .quote-before {
        position: absolute;
        height: 59px;
        width: 60px;
        padding: 8px;
        background: #FFF;
        font-size: 45px;
        left: -20px;
        top: -28px;
        color: #ccc;
      }

      .quote-after {
        position: absolute;
        height: 59px;
        width: 60px;
        padding: 8px;
        background: #FFF;
        font-size: 45px;
        right: -20px;
        bottom: -28px;
        color: #ccc;
        -webkit-transform:rotate(-180deg);
           -moz-transform:rotate(-180deg);
             -o-transform:rotate(-180deg);
                transform:rotate(-180deg);
            ms-filter:"progid:DXImageTransform.Microsoft.BasicImage(rotation=2)";
            filter:progid:DXImageTransform.Microsoft.BasicImage(rotation=2);
      }

      .quote p {
        font-size: 20px;
        padding: 10px 20px 0px 20px;
      }

      @media (max-width: 768px) {
        .quote {
          width: Calc(100% - 40px);
          margin-left: 20px;
          margin-right: 20px;
        }

        .lander {
          width: 100%;
        }

        #cta-buttons {
          display: none;
        }

        .img-responsive {
          display: block;
          width: 100%;
          height: auto;
          padding: 10%;
        }

        .center-xs {
          text-align: center;
        }

      }


      @media (max-width: 468px) {
        .img-responsive {
          padding: 0;
        }
      }

      .facepic {
        border-radius: 2em;
        height: 120px;
        width: 120px;
      }

      .soc-links {
        margin-top: 20px
      }

      .soc-links a {
        display: inline-block;
        color: #CCC;
        font-size: 30px;
        padding: 5px;
        -webkit-transition: .3s all;
                transition: .3s all;
      }
      .soc-links a:hover {
        color: #666;
      }
      .caplink {
        text-decoration: underline;
        display: none;
      }

      .hover-img {
        opacity:0;
        margin-top: -100%;
      }
      .main-img {
        opacity:1;
      }

      .item:hover .main-img {
        transition:opacity .4s ease-in-out;
        opacity:0;
      }

      .item:hover .hover-img {
        transition:opacity .4s ease-in-out;
        opacity:1;
      }

      h4.inspiration-block {
        margin: 20px 10px 30px;
        text-transform: none;
      }

      h4.inspiration-block span {
        text-decoration: underline;
      }

      .inspiration-item-spacer {
        content: "";
        height: 40px;
        width: 100%;
      }

      @media (min-width: 768px) {
        .inspiration-block {
          min-height: 610px;
        }
      }
      @media (min-width: 992px) {
       .inspiration-block {
          min-height: 760px;
        }
      }

    </style>
  </head>
  <body class="${bodyClass}">
    <c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
    <c:import url="../fragments/nav/topNavBar.jsp"/>


    <div class="spinstuff show"><span class="spinner page-fade-spinner"></span></div>
    <div class="choosestuff">

      <div class="container-fluid">

        <div class="row landingbanner">
          <div class="lander">
            <img class="facepic" src="https://storage.googleapis.com/troveup-imagestore/assets/img/influencer/christendominique/profile/christdom.jpg">
            <h1>Christen Dominique x Trove</h1>
            <h5>Fashion and Beauty. <br>Mommy. Wife. Youtuber.</h5>
            <a class="caplink" href="#">KEEP READING</a>
            <div class="soc-links">
              <a class="ion-social-instagram" href="#"></a>
              <a class="ion-social-youtube" href="#"></a>
              <a class="ion-social-facebook" href="#"></a>
            </div>
          </div>
        </div>
      </div>

    <div class="container">
      <div class="row">
        <div class="col-sm-12">
          <h4>The<br><span>Professional Dreamer</span><br>Collection<br><p>One-liner about the collection goes here.</p></h4>
        </div>
      </div>

      <c:forEach var="item" items="${items}" varStatus="loop">

      <%-- Quote block after the first row of items --%>
        <c:if test="${loop.index eq 1}">
          <div class="row quotebanner">
            <div class="quote">
              <span class="quote-before ion-quote"></span>
              <p>I wanted to design a jewelry collection that really captured what being a dreamer means to me.</p>
              <span class="quote-after ion-quote"></span>
            </div>
          </div>
        </c:if>

        <%-- Column of items next to Christen's Inspiration box --%>
        <c:choose>
        <c:when test="${loop.index eq 2}">
          <div class="row special">
            <div class="col-sm-4 col-xs-12">
              <c:forEach var="imageContainer" items="${item.troveDisplayImages}" varStatus="imageLoop">
                <c:choose>
                  <c:when test="${imageLoop.index lt 2}">
                    <div class="inspiration-item-spacer"></div><a class="item" href="/featured/item/${item.simpleItemId}"><img class="main-img img-responsive" alt="160x160" src="${imageContainer.largeImageUrlPath}"><img class="hover-img img-responsive" alt="160x160" src="https://storage.googleapis.com/trove-qa-teststore/hover-new.jpg"><h2>${item.itemName}<br><span>$${item.itemPrice}</span></h2></a>
                  </c:when>
                  <c:otherwise>
            </div>
            <div class="col-sm-8 col-xs-12">
              <h4 class="inspiration-block"><span>Christen's Inspiration</span><br><p>One-liner about the collection goes here.</p></h4>
            </div>
          </div>
                    <div class="row">
                      <div class="col-sm-4 col-xs-12">
                        <div class="inspiration-item-spacer"></div><a class="item" href="/featured/item/${item.simpleItemId}"><img class="main-img img-responsive" alt="160x160" src="${imageContainer.largeImageUrlPath}"><img class="hover-img img-responsive" alt="160x160" src="https://storage.googleapis.com/trove-qa-teststore/hover-new.jpg"><h2>${item.itemName}<br><span>$${item.itemPrice}</span></h2></a>
                      </div>
                  </c:otherwise>
                </c:choose>
              </c:forEach>
        </c:when>

        <c:when test="${loop.index eq 3}">
          <c:forEach var="imageContainer" items="${item.troveDisplayImages}" varStatus="imageLoop">
            <c:choose>
              <c:when test="${imageLoop.index lt 2}">
                <div class="col-sm-4 col-xs-12">
                  <div class="inspiration-item-spacer"></div><a class="item" href="/featured/item/${item.simpleItemId}"><img class="main-img img-responsive" alt="160x160" src="${imageContainer.largeImageUrlPath}"><img class="hover-img img-responsive" alt="160x160" src="https://storage.googleapis.com/trove-qa-teststore/hover-new.jpg"><h2>${item.itemName}<br><span>$${item.itemPrice}</span></h2></a>
                </div>
              </c:when>
              <c:otherwise>
                </div>
      <div class="row">
        <%-- Replace below line when we have an actual vertical asset --%>
        <!--div class="col-sm-4 col-xs-12"><img class="main-img img-responsive" alt="160x160" src="https://storage.googleapis.com/trove-qa-teststore/sidepic-christen.jpg"-->
          <div class="col-sm-4 col-xs-12"><img class="main-img img-responsive" alt="Image of Christen" border="solid" style="border-width: 1px; width: 283px; height: 775px;">
        </div>
        <div class="col-sm-4 col-xs-12">
          <div class="inspiration-item-spacer"></div><a class="item" href="/featured/item/${item.simpleItemId}"><img class="main-img img-responsive" alt="160x160" src="${imageContainer.largeImageUrlPath}"><img class="hover-img img-responsive" alt="160x160" src="https://storage.googleapis.com/trove-qa-teststore/hover-new.jpg"><h2>${item.itemName}<br><span>$${item.itemPrice}</span></h2></a>
              </c:otherwise>
            </c:choose>
          </c:forEach>
        </c:when>

        <%-- Vertical banner of Christen, first row of two items, and start of second row with one item --%>
        <c:when test="${loop.index eq 4}">
            <c:forEach var="imageContainer" items="${item.troveDisplayImages}" varStatus="imageLoop">
              <c:choose>
                <c:when test="${imageLoop.index lt 1}">
                  <div class="inspiration-item-spacer"></div><a class="item" href="/featured/item/${item.simpleItemId}"><img class="main-img img-responsive" alt="160x160" src="${imageContainer.largeImageUrlPath}"><img class="hover-img img-responsive" alt="160x160" src="https://storage.googleapis.com/trove-qa-teststore/hover-new.jpg"><h2>${item.itemName}<br><span>$${item.itemPrice}</span></h2></a>
                  </div>
                  <div class="col-sm-4 col-xs-12">
                </c:when>
                <c:when test="${imageLoop.index lt 3}">
                  <div class="inspiration-item-spacer"></div><a class="item" href="/featured/item/${item.simpleItemId}"><img class="main-img img-responsive" alt="160x160" src="${imageContainer.largeImageUrlPath}"><img class="hover-img img-responsive" alt="160x160" src="https://storage.googleapis.com/trove-qa-teststore/hover-new.jpg"><h2>${item.itemName}<br><span>$${item.itemPrice}</span></h2></a>
                  <c:if test="${imageLoop.index eq 2}">
                    </div>
                </div>
                  </c:if>
                </c:when>
              </c:choose>
          </c:forEach>
        </c:when>
        <c:otherwise>
        <div class="row">
          <c:forEach var="imageContainer" items="${item.troveDisplayImages}">
            <div class="col-sm-4 col-xs-12">
              <a class="item" href="/featured/item/${item.simpleItemId}">
                <img class="main-img img-responsive" alt="160x160" src="${imageContainer.largeImageUrlPath}">
                <img class="hover-img img-responsive" alt="160x160" src="https://storage.googleapis.com/trove-qa-teststore/hover-new.jpg">
                <h2>${item.itemName}<br><span>$${item.itemPrice}</span></h2>
              </a>
            </div>
          </c:forEach>
        </div>
        </c:otherwise>
      </c:choose>
      </c:forEach>

      <div class="row">
        <div class="aboutblock col-sm-12">
          <h4><span>About this Collection</span><p>Multiliner-liner about the collection goes here. multiliner-liner about the collection goes here. multiliner-liner about the collection goes here. multiliner-liner about the collection goes here. multiliner-liner about the collection goes here. multiliner-liner about the collection goes here. multiliner-liner about the collection goes here. multiliner-liner about the collection goes here. multiliner-liner about the collection goes here.</p></h4>
        </div>
      </div>
    </div>

    <div class="container-fluid">
      <div class="row backtop" style="border-bottom: 1px solid #DEDEDE;">
        <div class="uparrow"><a id="scrollbutt"> Back to top<span class="icon ion-android-arrow-up"></span></a></div>
      </div>
    </div>

    <c:import url="../fragments/footers/footer.jsp"/>

    <script>

      function land() {
        mixpanel.track("land_onboard_choose");
      };

      $(document).ready(function() {
        land();
        var sStuff = document.querySelector(".spinstuff");
        var cStuff = document.querySelector(".choosestuff");
        var itemLink = document.querySelector("a");
        sStuff.classList.toggle("show");
        cStuff.classList.toggle("show");

        $('.item').click(function (e) {
          e.preventDefault();
          var addressValue = $(this).attr("href");
          sStuff.classList.toggle("show");
          cStuff.classList.toggle("show");
          mixpanel.track("onboard_choose_item", {
            "itemLink": addressValue
          });
          setTimeout(function(){ window.location.href = addressValue; }, 300);
        });
      });


      $("#scrollbutt").click(function(e) {
        $("html, body").animate({ scrollTop: 0 }, "slow");
        return false;
      });

    </script>


  </body>
</html>
