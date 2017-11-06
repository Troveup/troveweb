<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>${pageTitle}</title>
    <meta name="description" content="${pageDescription}">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=0">
    <link href='https://fonts.googleapis.com/css?family=Quicksand:300,400,700' rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Raleway:400,200,300,500,600,700' rel='stylesheet' type='text/css'>
    <link rel="icon" href="/resources/img/favicon.png?v=2">
    <script src="https://code.jquery.com/jquery-2.1.3.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.13.1/jquery.validate.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.13.1/additional-methods.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/resources/stylesheets/onboard.css">
    <c:import url="../fragments/analytics/all.jsp"/>
    <style>
      body.onboard .social-icon {
        display: inline-block;
        margin: 3px;
        margin-top: 15px;
      }

      body.onboard .centrafuge {
        position: absolute;
        width: 100%;
        text-align: center;
        top: 0%;
      }

      body.onboard .social-icon img {
        width: 40px;
      }

      body.onboard, body.thankyoureferral {
        padding-top: 0px;
        margin-bottom: 0px;
      }

      body.onboard h1 {
        font-size: 26px;
        letter-spacing: 4px;
        font-weight: 300;
        font-family: "Raleway", sans-serif;
        background-color: #FFF;
        color: #f56866;
        padding: 15px;
      }

      body.onboard h5 {
        margin-bottom: 0px;
        font-size: 14px;
      }

      body.onboard h3 {
        margin-top: 30px;
        text-transform: none;
        background-color: #fcd2d1;
        padding: 15px 20px;
        font-size: 16px;
        border: 2px solid #f56866;
        display: inline-block;
        max-width: 100%;
        white-space: nowrap;
        overflow-x: scroll;
      }

      body.onboard .socialhold {
        margin-bottom: 15px;
      }

      body.onboard h4 {
        font-size: 16px;
        margin-top: 15px;
        font-weight: 300;
        letter-spacing: 1.5px;
      }

      .logo {
        margin-top: 5px;
      }

      body.onboard h1 {
        font-size: 27px;
        padding: 20px;
      }

      body.onboard h5 {
        padding: 0px 20px 0 20px;
        font-size: 16px;
      }

      #copyme {
        position: relative;
        top: -63px;
        height: 53px;
        left: 50%;
        margin-left: -100px;
        width: 100px;
        font-size: 15px;
        font-weight: bold;
        color: #FFF;
        background-color: rgb(183, 176, 176);
        border: 2px solid #F56866;
      }

      body.onboard h6 {
        padding: 0px 70px 0 70px;
        font-size: 12px;
        position: relative;
        right: 0;
        top: 10px;
        font-weight: 300;
      }

      #myRow {
        margin-right: 0px;
        margin-left: 0px;
      }

      body.onboard h3 {
        min-width: auto;
        text-align: left;
        padding: 15px 190px 15px 20px;
      }

      @media (min-width: 650px) { 
        body.onboard .centrafuge {
          max-width: 650px;
          border: 10px solid #dedede;
          left: 50%;
          margin-left: -325px;
          padding: 10px 20px 40px 20px;
          top: 0%;
        }

        body.onboard h1 {
          font-size: 46px;
          padding: 25px;
        }
        body.onboard h3 {
          min-width: 590px;
          margin-top: 50px;
          overflow-x: hidden;
        }
        body.onboard h5 {
          padding: 0px 70px 0 70px;
          font-size: 16px;
        }
        body.onboard h4 {
          margin-top: 50px;
        }

        body.onboard .socialhold {
          margin-bottom: 40px;
        }
        .logo {
          margin-top: 35px;
        }
        body.onboard .social-icon img {
          width: 60px;
        }
      }

    </style>
  </head>
  <body class="${bodyClass}">
    <div class="centrafuge"> 
      <img class="logo" width="200" alt="Trove" src="/resources/img/trove-logo-beta.png" />
      <h4>Your free 3D-printed ring is on its way for you to try on!</h4>
      <h1>Like Your Ring? <br>Get it Free in Silver.</h1>
      <h5>Share your unique referral code with 10 friends and get your custom ring in sterling silver - on us.</h5>
      <div id="myRow" class="row">
        <h3 id="selectable">${referralUrl}</h3>
        <button id="copyme" onclick="copyToClipboard('#selectable')">Copy Link</button>
      </div>
      <div class="socialhold">
        <div class="social-icon"><a href="http://www.facebook.com/sharer/sharer.php?u=${referralUrl}" target="_blank"><img src="https://storage.googleapis.com/troveup-imagestore/assets/facebook-icon.png" alt="Share on Facebook" class="inline-icon"></a></div>
        <div class="social-icon"><a href="http://twitter.com/intent/tweet?status=Customize%20your%20own%20%233Dprinted%20jewelry%20on%20%40troveup%20and%20get%20a%20try-on%20model%20sent%20to%20you%20for%20free!%20Check%20it%20out%20here%20${referralUrl}" target="_blank"><img src="https://storage.googleapis.com/troveup-imagestore/assets/twitter-icon.png" alt="Share on Twitter" class="inline-icon"></a></div>
      </div>
      <h5>Full site Launching soon.</h5>
      <h6>*One ring per person.</h6>
      
    </div>
    <script>
      var isWelcome = false;

      copyToClipboard = function (element) {
        var $temp = $("<input />");
        $("body").append($temp);
        $temp.val($(element).text()).select();
        var result = false;
        try {
          result = document.execCommand("copy");
        } catch (err) {
          console.log("Copy error: " + err);
        }
        $temp.remove();
        $('#copyme').html('Copied');
        $('#copyme').css("background-color", "#F56866");
        return result;
      }

      function getUrlVars() {
        var vars = [], hash;
        var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
        for(var i = 0; i < hashes.length; i++) {
          hash = hashes[i].split('=');
          vars.push(hash[0]);
          vars[hash[0]] = hash[1];
        }
        return vars;
      }

      function landed(testversion) {
        if (testversion) {
          var eventname = testversion + "-landthankyou";
          mixpanel.track(eventname);
        } else {
          mixpanel.track("landthankyou");
        }
      };

    </script>

    <script>
      $(document).ready(function() {
        var testversion = getUrlVars()["testversion"];
        landed(testversion);
      });

    </script>
    <c:import url="../fragments/phonboardJS.jsp"/>
  </body>
</html>