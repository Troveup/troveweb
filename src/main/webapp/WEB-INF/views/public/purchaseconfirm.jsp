<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
  <title>Trove: Your jewelry's on the way!</title>
  <c:import url="../fragments/headers/commonHead.jsp"/>
  <c:import url="../fragments/analytics/all.jsp"/>
  <link rel="stylesheet" href="/resources/stylesheets/main.css">
  <link rel="stylesheet" href="/resources/stylesheets/buttons.css">
  <link rel="stylesheet" href="/resources/stylesheets/authmod.css">
  <link href='https://fonts.googleapis.com/css?family=Raleway:400,200,300,500,600,700' rel='stylesheet' type='text/css'>
  <link href="https://fonts.googleapis.com/css?family=Vollkorn:400,400italic,700,700italic" rel="stylesheet"
        type="text/css">
  <style>

    @media screen and (min-width: 440px) {
      .congrats-body .congrats-body-inner .congrats-form {
        padding: 2.5em 2.5em 2em;
        max-width: none;
        width: auto;
        -webkit-box-shadow: none;
        -moz-box-shadow: none;
        box-shadow: none;
        background: rgba(255, 255, 255, 0.0);
        -webkit-border-radius: 0px;
        border-radius: 0px;
        background-clip: padding-box;
        text-align: center;
        min-width: 468px;
      }
    }

    .congrats-body .congrats-body-inner .congrats-form h2 {
      font-family: "Raleway", 'Helvetica', sans-serif;
      font-size: 24px;
      margin-top: -10px;
      margin-bottom: 10px;
    }

    body {
      width: 100%;
      overflow-x: hidden;
      background: #FBFBFB;
      background-image: url("https://storage.googleapis.com/troveup-imagestore/assets/img/backgroundBlocked.jpg");
      background-size: cover;
    }

    @media screen and (max-width: 441px) {
      .congrats-body .congrats-body-inner .congrats-form h2 {
        display: block;
      }

      .congrats-body .congrats-body-inner .congrats-form h3 {
        display: block;
      }

      .congrats-body .congrats-body-inner .congrats-form {
        margin: -10px auto 30px auto;
      }

      .congrats-body {
        padding-bottom: 50px;
      }
    }

    html, body {
      margin: 0;
      padding: 0;
      width: 100%;
      height: 100%;
      background: #FFF;
      overflow: auto;
    }

    canvas {
      opacity: 0.4;
      position: fixed;
    }

    .congrats-body {
      padding-top: 150px;
      padding-bottom: 40px;
      position: absolute;
      top: 20px;
      left: 0px;
      text-align: center;
    }

    @media screen and (min-width: 440px) {
      .congrats-body {
        padding-top: 150px;
        padding-bottom: 40px;
        position: absolute;
        top: 20px;
        left: 50%;
        margin-left: -248px;
        text-align: center;
      }
    }

    @media screen and (max-width: 440px) {
      .congrats-body .congrats-body-inner .congrats-form {
        background: transparent;
        text-align: center;
      }
      .hidemob {
        display: none;
      }
    }

    .gemIcon {
      width: 30px;
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

      canvas {
        opacity: 0.1;
      }

      .ftui-nav {
        display: none;
      }
    }

    h1 {
      font-size: 30px;
      text-transform: uppercase;
      font-family: vollkorn;
      letter-spacing: 0.05em;
      color: #E26160;
    }

    .congrats-body .congrats-body-inner .congrats-form h2 {
      font-family: "Raleway", 'Helvetica', sans-serif;
      font-size: 19px;
      margin-top: 20px;
      margin-bottom: 40px;
      color: #E26160;
      text-transform: uppercase;
      background: rgba(255, 255, 255, 1.0);
    }

    .congrats-body .congrats-body-inner .congrats-form h3 {
      font-weight: 500;
      margin-top: 10px;
      margin-bottom: 0px;
      padding: 50px;
      font-size: 20px;
    }

    .congrats-body .congrats-body-inner .congrats-form {
      width: auto;
      margin-bottom: 10px;
      letter-spacing: .1em;
      padding: 0px 30px 0px 30px;
      min-width: 240px;
    }

    h4 {
      font-size: 15px;
      text-align: left;
      margin-bottom: 30px;
      line-height: 1.3;
    }

    h4 span {
      text-transform: uppercase;
    }

    button.custom-confirm-button {
      min-width: 240px;
      line-height: 38px;
    }

    .small-padding {
      padding: 10px;
    }

    canvas {
      margin-left: 0px;
    }

  </style>
</head>
<body>
<c:import url="../fragments/modals/authModal.jsp"/>
<c:import url="../fragments/nav/topNavBar.jsp"/>
<canvas id="world"></canvas>
<div class="congrats-body">
  <div class="congrats-body-inner">
    <div class="congrats-form">
      <h1>Get Excited</h1>
      <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/diamond-glow.png">
      <h3>We're getting your jewelry ready<br class="hidemob"> to send to you. Hang tight!</h3>
      <div class="small-padding">
        <button id="keepshoppingbutton" type="submit" class="btn btn--red custom-confirm-button">KEEP SHOPPING</button>
      </div>
      <div class="small-padding">
        <button id="authbutton" type="submit" class="btn btn--darkgray custom-confirm-button">TRACK YOUR ORDER</button>
      </div>
    </div>
  </div>
</div>
<!-- JS -->
<script type="text/javascript" src="/resources/js/trove/Auth.js"></script>
<script>

  var authHelper = new AuthHelper("${_csrf.token}", ${isAuthenticated});

  function trackYourOrderClick() {
    if (authHelper.getIsAuthenticated()) {
      window.location.href = "/private/orders";
    } else {
      authHelper.setActionToPerformPostAuth(trackYourOrderClick);
      triggerAuthModal();
    }
  }

  function getUrlVars() {
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for (var i = 0; i < hashes.length; i++) {
      hash = hashes[i].split('=');
      vars.push(hash[0]);
      vars[hash[0]] = hash[1];
    }
    return vars;
  }

  function land() {
    mixpanel.track("land_onboard_confirm");
  }

  $('#myTabs a').click(function (e) {
    e.preventDefault()
    $(this).tab('show')
  });


  $(document).ready(function () {
    land();
    //updatePosish();
    var me = getUrlVars()["param.error"];
  });

  $("#litab").click(function () {
    mixpanel.track("login_login");
  });
  $("#sutab").click(function () {
    mixpanel.track("login_signuptab");
  });
  $("#libut").click(function () {
    mixpanel.track("login_nonFBauth");
  });
  $("#fgp").click(function () {
    mixpanel.track("login_forgot_password");
  });
  $("#fbli").click(function () {
    mixpanel.track("login_FBauth");
  });


  $("#keepshoppingbutton").click(function (e) {
    window.location.href = "/feed";
  });

  $("#authbutton").on("click", function (e) {
    trackYourOrderClick();
  });

</script>


<script>
  (function () {
    var COLORS, Confetti, NUM_CONFETTI, PI_2, canvas, confetti, context, drawCircle, i, range, resizeWindow, xpos;

    NUM_CONFETTI = 400;

    COLORS = [[255, 71, 106], [255, 61, 99], [255, 56, 83], [244, 92, 68], [255, 182, 70]];

    PI_2 = 2 * Math.PI;

    canvas = document.getElementById("world");

    context = canvas.getContext("2d");

    window.w = 0;

    window.h = 0;

    resizeWindow = function () {
      window.w = canvas.width = window.innerWidth;
      return window.h = canvas.height = window.innerHeight;
    };

    window.addEventListener('resize', resizeWindow, false);

    window.onload = function () {
      return setTimeout(resizeWindow, 0);
    };

    range = function (a, b) {
      return (b - a) * Math.random() + a;
    };

    drawCircle = function (x, y, r, style) {
      context.beginPath();
      // context.arc(x, y, r, 0, PI_2, false);
      context.rect(x, y, r * 1.5, r * 1.5);
      context.fillStyle = style;
      return context.fill();
    };

    xpos = 0.5;

    document.onmousemove = function (e) {
      return xpos = e.pageX / w;
    };

    window.requestAnimationFrame = (function () {
      return window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.oRequestAnimationFrame || window.msRequestAnimationFrame || function (callback) {
            return window.setTimeout(callback, 1000 / 60);
          };
    })();

    Confetti = (function () {
      function Confetti() {
        this.style = COLORS[~~range(0, 5)];
        this.rgb = "rgba(" + this.style[0] + "," + this.style[1] + "," + this.style[2];
        this.r = ~~range(2, 6);
        this.r2 = 2 * this.r;
        this.replace();
      }

      Confetti.prototype.replace = function () {
        this.opacity = 0;
        this.dop = 0.03 * range(1, 4);
        this.x = range(-this.r2, w - this.r2);
        this.y = range(-20, h - this.r2);
        this.xmax = w - this.r;
        this.ymax = h - this.r;
        this.vx = range(0, 2) + 8 * xpos - 5;
        return this.vy = 0.7 * this.r + range(-1, 1);
      };

      Confetti.prototype.draw = function () {
        var ref;
        this.x += this.vx;
        this.y += this.vy;
        this.opacity += this.dop;
        if (this.opacity > 1) {
          this.opacity = 1;
          this.dop *= -1;
        }
        if (this.opacity < 0 || this.y > this.ymax) {
          this.replace();
        }
        if (!((0 < (ref = this.x) && ref < this.xmax))) {
          this.x = (this.x + this.xmax) % this.xmax;
        }
        return drawCircle(~~this.x, ~~this.y, this.r, this.rgb + "," + this.opacity + ")");
      };

      return Confetti;

    })();

    confetti = (function () {
      var j, ref, results;
      results = [];
      for (i = j = 1, ref = NUM_CONFETTI; 1 <= ref ? j <= ref : j >= ref; i = 1 <= ref ? ++j : --j) {
        results.push(new Confetti);
      }
      return results;
    })();

    window.step = function () {
      var c, j, len, results;
      requestAnimationFrame(step);
      context.clearRect(0, 0, w, h);
      results = [];
      for (j = 0, len = confetti.length; j < len; j++) {
        c = confetti[j];
        results.push(c.draw());
      }
      return results;
    };

    step();

  }).call(this);
</script>


</body>
</html>
