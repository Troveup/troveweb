<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <c:import url="../fragments/headers/commonHead.jsp"/>
    <c:import url="../fragments/headers/legalHead.jsp"/>
    <title>${pageTitle}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=0">
    <script type="text/javascript" src="https://code.jquery.com/jquery-1.11.3.min.js"></script>
    <link href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link rel="icon" href="/resources/img/favicon.png?v=2">
    <link href='https://fonts.googleapis.com/css?family=Raleway:400,200,300,500,600,700' rel='stylesheet' type='text/css'>
    <link href="https://fonts.googleapis.com/css?family=Vollkorn:400,400italic,700,700italic" rel="stylesheet" type="text/css">
    <c:import url="../fragments/analytics/all.jsp"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
    <style>
      body {
        font-family: "Raleway",Helvetica,Arial,sans-serif;
        margin-top: 17px;
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
        font-size: 17px;
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

      .navbar {
        min-height: 80px;
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
        padding-bottom: 60px;
        padding-top: 60px;
        cursor: pointer;
        font-weight: 400;
        letter-spacing: 0.06em;
        color: #666666;
      }

      .uparrow span {
        padding-left: 10px;
        color: #666666;
      }

      h4 {
        margin: 50px 25%;
        font-family: "Raleway",Helvetica,Arial,sans-serif;
        letter-spacing: .06em;
        text-align: center;
        text-transform: uppercase;
        height: 220px;
        padding-top: 50px;
        border: 1px solid #555;
        margin-bottom: 50px;
        font-size: 1.3em;
        font-weight: 300;
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
      }

      .row.landingbanner {
        background: #d9edf7;
        padding: 10 20 40 20;
        background-image: url('https://storage.googleapis.com/trove-qa-teststore/chrisdombg2.jpg');
        background-position: left bottom;
        background-repeat: repeat-x;   
      }

      .lander {
        background: #FFF;
        margin-top: 40px;
        padding: 20px;
        width: 500px;
        margin-left: auto;
        margin-right: auto;
        text-align: center;
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
        width: 55px;
        padding: 8px;
        background: #FFF;
        font-size: 45px;
        left: -18px;
        top: -28px;
        color: #ccc;
      }

      .quote-after {
        position: absolute;
        height: 59px;
        width: 55px;
        padding: 8px;
        background: #FFF;
        font-size: 45px;
        right: -18px;
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
      }

      .facepic {
        border-radius: 2em;
        height: 120px;
        width: 120px;
      }

      .soc-links {
        margin-top: 30px
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
          <img class="facepic" src="https://storage.googleapis.com/trove-qa-teststore/christdom.jpg">
          <h1>Christen Domique's Trove</h1>
          <h5>Fashion and Beauty. <br>Mommy. Wife. Youtuber.</h5>
          <a class="caplink" href="#">KEEP READING</a>
          <div class="soc-links">
            <a class="ion-social-instagram" href="#"></a>
            <a class="ion-social-youtube" href="#"></a>
            <a class="ion-social-facebook" href="#"></a>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-sm-12">
          <h4>The<br><span>Professional Dreamer</span><br>Collection<br><p>One-liner about the collection goes here.</p></h4>
        </div>
      </div>
    </div>
    <div class="container">
      <div class="row">
        <div class="col-sm-4 col-xs-12"><a class="item" id="itemtooltip9a"  href="/featured/item/1"><img id="img9a" class="img-responsive" alt="160x160" src="https://storage.googleapis.com/trove-qa-teststore/cloud-pendant-rose-gold.jpg"><h2>Cloud Pendant<br><span>$56</span></h2></a></div>
        <div class="col-sm-4 col-xs-12"><a class="item" id="itemtooltip9b"  href="/featured/item/1"><img id="img9b" class="img-responsive" alt="160x160" src="https://storage.googleapis.com/trove-qa-teststore/cloud-pendant.jpg"><h2>Cloud Pendant<br><span>$37</span></h2></a></div>
        <div class="col-sm-4 col-xs-12"><a class="item" id="itemtooltip9c"  href="/featured/item/1"><img id="img9c" class="img-responsive" alt="160x160" src="https://storage.googleapis.com/trove-qa-teststore/cloud-pendant-silver.jpg"><h2>Cloud Pendant<br><span>$42</span></h2></a></div>
      </div>
      <div class="row quotebanner">
        <div class="quote">
          <span class="quote-before ion-quote"></span>
          <p>I wanted to design a jewelry collection that really captured what being a dreamer means to me.</p>
          <span class="quote-after ion-quote"></span>
        </div>
      </div>
      <div class="row">
        <div class="col-sm-4 col-xs-12"><a class="item" id="itemtooltip1"  href="/featured/item/1"><img id="img1" class="img-responsive" alt="160x160" src="https://storage.googleapis.com/trove-qa-teststore/crystal-cloud-rose-gold.jpg"><h2>Cloud w/ Crystal</h2><span>$25</span></a></div>
        <div class="col-sm-4 col-xs-12"><a class="item" id="itemtooltip2"  href="/featured/item/1"><img id="img2" class="img-responsive" alt="160x160" src="https://storage.googleapis.com/trove-qa-teststore/crystal-cloud-gold.jpg"><h2>Cloud w/ Crystal</h2><span>$70</span></a></div>
        <div class="col-sm-4 col-xs-12"><a class="item" id="itemtooltip3"  href="/featured/item/1"><img id="img3" class="img-responsive" alt="160x160" src="https://storage.googleapis.com/trove-qa-teststore/crystal-cloud-silver.jpg"><h2>Cloud w/ Crystal</h2><span>$54</span></a></div>
      </div>
      <div class="row">
        <div class="col-sm-4 col-xs-12"><a class="item" id="itemtooltip1"  href="/featured/item/1"><img id="img1" class="img-responsive" alt="160x160" src="https://storage.googleapis.com/trove-qa-teststore/open-heart-gold-1.jpg"><h2>Open Heart</h2><span>$25</span></a></div>
        <div class="col-sm-4 col-xs-12"><a class="item" id="itemtooltip2"  href="/featured/item/1"><img id="img2" class="img-responsive" alt="160x160" src="https://storage.googleapis.com/trove-qa-teststore/open-heart-rose-gold-2.jpg"><h2>Open Heart</h2><span>$26</span></a></div>
        <div class="col-sm-4 col-xs-12"><a class="item" id="itemtooltip3"  href="/featured/item/1"><img id="img3" class="img-responsive" alt="160x160" src="https://storage.googleapis.com/trove-qa-teststore/open-heart-silver-2.jpg"><h2>Open Heart</h2><span>$27</span></a></div>
      </div>
      <div class="row">
        <div class="col-sm-4 col-xs-12"><a class="item" id="itemtooltip1"  href="/featured/item/1"><img id="img1" class="img-responsive" alt="160x160" src="https://storage.googleapis.com/trove-qa-teststore/unicorn-necklace-2.jpg"><h2>Unicorn Pendant</h2><span>$26</span></a></div>
        <div class="col-sm-4 col-xs-12"><a class="item" id="itemtooltip2"  href="/featured/item/1"><img id="img2" class="img-responsive" alt="160x160" src="https://storage.googleapis.com/trove-qa-teststore/unicorn-necklace-rose-gold-2.jpg"><h2>Unicorn Pendant</h2><span>$29</span></a></div>
        <div class="col-sm-4 col-xs-12"><a class="item" id="itemtooltip3"  href="/featured/item/1"><img id="img3" class="img-responsive" alt="160x160" src="https://storage.googleapis.com/trove-qa-teststore/unicorn-necklace-silver-2.jpg"><h2>Unicorn Pendant</h2><span>$27</span></a></div>
      </div>
      <div class="row">
        <div class="col-sm-4 col-xs-12"><a class="item" id="itemtooltip1"  href="/featured/item/1"><img id="img1" class="img-responsive" alt="160x160" src="https://storage.googleapis.com/trove-qa-teststore/heart-pendant-gold.jpg"><h2>Heart Pendant</h2><span>$25</span></a></div>
        <div class="col-sm-4 col-xs-12"><a class="item" id="itemtooltip2"  href="/featured/item/1"><img id="img2" class="img-responsive" alt="160x160" src="https://storage.googleapis.com/trove-qa-teststore/heart-pendant-rose-gold.jpg"><h2>Heart Pendant</h2><span>$26</span></a></div>
        <div class="col-sm-4 col-xs-12"><a class="item" id="itemtooltip3"  href="/featured/item/1"><img id="img3" class="img-responsive" alt="160x160" src="https://storage.googleapis.com/trove-qa-teststore/heart-pendant-silver.jpg"><h2>Heart Pendant</h2><span>$27</span></a></div>
      </div>
    </div>
    <footer class="footer">
      <div class="container-fluid">
        <div class="row">
          <div class="col-sm-12 buttonholder">
            <div class="uparrow"><a id="scrollbutt"> Back to top<span class="glyphicon glyphicon-chevron-up"></div>
          </div>
        </div>
      </div>
    </footer>
  </span>

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
