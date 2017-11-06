<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Trove: Confirm Email</title>
    <meta name="description" content="Confirm Email Page Post Registration">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href='https://fonts.googleapis.com/css?family=Quicksand:300,400,700' rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Raleway:400,200,300,500,600,700' rel='stylesheet' type='text/css'>
    <link rel="icon" href="/resources/img/favicon.png?v=2">
    <link rel="stylesheet" href="/resources/stylesheets/confirm.css">
    <script src="https://code.jquery.com/jquery-2.1.3.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
    <c:import url="fragments/analytics/all.jsp"/>
    <style>
      #log {
        width: 100%;
        position: absolute;
        text-align: center;
        padding-top: 20px;
      }
      body.confirm h3 {
        text-transform: capitalize;
        margin-bottom: 22px;
      }
      body.confirm .centrafuge {
        position: absolute;
        width: 100%;
        text-align: center;
        top: 50%;
        left: 50%;
        padding: 15px;
        margin-left: -300px;
        width: 600px;
        min-height: 580px;
        padding: 53px 60px;
        border: 10px solid #D4D4D4;
      }

      #biggbutton {
        width: 250px;
        border-radius: 0px;
        text-transform: capitalize;
        padding: 10px;
      }

      .btn.customize img {
        width: 20px;
        margin-right: 20px;
      }

      body.confirm h5 {
        margin-bottom: 0px;
      }

      body.confirm #img1 {
        width: 250px;
        margin-bottom: 10px;
      }

      body.confirm #skipper {
        display: block;
        font-size: 120%;
        color: #666;
        text-decoration: underline;
        margin-top: 20px;
      }

      @media screen and (max-width: 580px) {

        body.confirm .centrafuge {
          position: absolute;
          width: 100%;
          text-align: center;
          top: 35px;
          margin-top: 0px !important;
          left: 0px;
          padding: 15px;
          margin-left: 0px;
          width: 100%;
          padding: 9px 30px;
          border: 10px solid #FFF;
        }

        #log {
          display: none;
        }
      }

      
    </style>
    
  </head>
  <body class="confirm">
    <a id="log" href="/"><img alt="Trovelogo" class="ob-logo" src="/resources/img/troveLogo2.svg"></a>
    <div class="centrafuge"> 
      <!-- <h3>Welcome to Trove!</h3> -->
      <h5>You can find your saved items in your Trove. Revisit and edit designs, or share them with friends.</h5>

      <a id="itemtooltip1" href="/public/phcustomize/10"><img id="img1" class="img-rounded" alt="160x160" src="https://storage.googleapis.com/troveup-imagestore/assets/img/mirror-no-hole-gold.jpg"></a>

      <h5>And thats it! We're glad to have you with us.</h5>
      <a id="biggbutton" href="/feed" class="btn btn--red btn--product--description customize" role="button" data-tooltip="Customize this!" onclick="custb()">Start Exploring</a>

      <!-- <a href="/feed"><button id="button" class="btn btn-lg">Sign In</button></a> -->
      <a id="skipper" href="/feed">Skip</a>
    </div>
    <script>
      $("#biggbutton").click(function () {
        mixpanel.track("onboarding_page4");
      });

      function jqUpdateSize(contain){
        var height = $('.centrafuge').innerHeight();
        $(contain).css("margin-top", String(-1 * (height / 2)) + "px");
      };  
      $(document).ready(function() {
      	jqUpdateSize('.centrafuge');
      });
      $(window).on('resize', function() {
        jqUpdateSize('.centrafuge');
      });
    </script>
  </body>
</html>
