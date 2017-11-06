<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
  <title>Trove: Your jewelry's on the way!</title>
  <c:import url="../fragments/headers/commonHead.jsp"/>
  <c:import url="../fragments/headers/authHead.jsp"/>
  <c:import url="../fragments/analytics/all.jsp"/>
  <link href='https://fonts.googleapis.com/css?family=Raleway:400,200,300,500,600,700' rel='stylesheet' type='text/css'>
    <link href="https://fonts.googleapis.com/css?family=Vollkorn:400,400italic,700,700italic" rel="stylesheet" type="text/css">
  <style>

  @media screen and (min-width: 440px) {
    .auth-content .auth-content-inner .auth-form {
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
      }
    }

    .auth-content .auth-content-inner .auth-form h3 {
      font-size: 16px
    }

    .auth-content .auth-content-inner .auth-form h2 {
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
      .auth-content .auth-content-inner .auth-form h2 {
        display: block;
      }

      .auth-content .auth-content-inner .auth-form h3 {
        display: block;
      }

      .auth-content .auth-content-inner .auth-form .auth-logo {
        margin: -10px auto 30px auto;
      }

      .auth-content {
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

    .auth-content {
      padding-top: 10px;
      padding-bottom: 40px;
      position: absolute;
      top: 20px;
      left: 0px;
    }

    @media screen and (min-width: 440px) {
      .auth-content {
        padding-top: 10px;
        padding-bottom: 40px;
        position: absolute;
        top: 20px;
        left: 50%;
        margin-left: -240px;
      }
    }

    @media screen and (max-width: 440px) {
      .auth-content .auth-content-inner .auth-form {
        background: transparent;
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

      .step {
        display: inline-block;
        font-family: "Raleway", Helvetica, Arial, sans-serif;
        font-size: 13px;
        min-width: 180px;
        font-weight: 400;
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

      .auth-content .auth-content-inner .auth-form h2 {
        font-family: "Raleway", 'Helvetica', sans-serif;
        font-size: 19px;
        margin-top: 20px;
        margin-bottom: 40px;
        color: #E26160;
        text-transform: uppercase;
      }

      .auth-content .auth-content-inner .auth-form h3 {
        font-size: 14px;
        font-weight: 500;
        margin-top: 10px;
        margin-bottom: 0px;
      }

      .auth-content .auth-content-inner .auth-form .btn.btn-si.spec {
        width: auto;
        margin-bottom: 80px;
        letter-spacing: .1em;
        padding: 0px 30px 0px 30px;
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

       /*.step.back:hover {
          cursor: pointer;
        }*/

    


  </style>
</head>
<body>
  <div class="ftui-nav">
    <div class="steps">
      <div class="step back"><span><img class="gemIcon" src="https://storage.googleapis.com/troveup-imagestore/assets/sectioned-gem-white.svg"></span><br>Step 1: Choose</div>
      <div class="step back"><span><img class="gemIcon" src="https://storage.googleapis.com/troveup-imagestore/assets/sectioned-gem-white.svg"></span><br>Step 2: Customize</div>
      <div class="step active"><span><img class="gemIcon" src="https://storage.googleapis.com/troveup-imagestore/assets/sectioned-gem-red.svg"></span><br>Step 3: Free Jewelry</div>
    </div>
  </div>



  <canvas id="world"></canvas>
    
    <!-- Login Container -->
    <div class="auth-content">
      <div class="auth-content-inner">
        <div class="auth-form">
          <h1>Get Excited</h1>
          <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/diamond-glow.png">
          <h2>you've just customized your first piece of jewelry on trove</h2>
          <h3>Customize more designs on Trove</h3>
          <!-- <a href="/welcome"><img alt="Trovelogo" class="auth-logo" src="https://storage.googleapis.com/troveup-imagestore/assets/img/logo-lt-gray.svg"></a> -->
          <button id="mainsitebutton" type="submit" class="btn btn-si spec">SHOP NOW</button>
          <!-- <h4><span>track your order</span><br>
          Create an account to view your order history and shop for more jewelry
          </h4> -->
          <!-- <div class="auth-tabs">
            <ul id="myTabs" class="nav nav-tabs nav-justified" role="tablist">
              <li class="active"><a id="sutab" href="#signup">Sign Up</a></li>
              <li><a id="litab" href="#signin">Log In</a></li>
            </ul>
          </div>
          <div id="myTabContent" class="tab-content stormy">
            <div id="error"></div>
            <div role="tabpanel" class="tab-pane fade" id="signin">
              <form action="/" method="POST" onsubmit="return false;" role="form">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input id="user-email" name="username" type="text" placeholder="Email or Username" class="form-control"/>
                <input id="user-password" name="password" type="password" placeholder="Password" class="form-control"/>
                <button id="libut" type="submit" class="btn btn-si">Log In</button>
              </form>
              <a id="fgp" class="fgpassword" href="${pageContext.request.contextPath}/password/forgotpassword">Forgot Password?</a>
            </div>
            <div role="tabpanel" class="tab-pane fade active in" id="signup"> 
              <div class="has-feedback">
                <input type="email" <c:if test="${not empty anonymousEmail}">value="${anonymousEmail}"</c:if> name="email" id="email" class="form-control" placeholder="Email Address"/>
                <span class="glyphicon form-control-feedback" id="email1"></span>
              </div>
              <div class="has-feedback">
                <input type="text" <c:if test="${not empty anonymousName}">value="${anonymousName}"</c:if> name="firstName" id="firstName" class="form-control" placeholder="Name"/>
                <span class="glyphicon form-control-feedback" id="firstName1"></span>
              </div>
              <div class="has-feedback">
                <input type="password" name="password" id="password" class="form-control" placeholder="Create a Password" />
                <span class="glyphicon form-control-feedback" id="password1"></span>
              </div>

              <div class="formy">
                <input id="optin" type="hidden" name="optin" value="1"/>
                <button id="create" type="submit" class="btn btn-si">Create an Account</button>
                <h5>By continuing you agree to our <a id="termss" href="/legal/terms" target="_blank">Terms</a> <br> and <a href="/legal/privacy" target="_blank">Privacy Policy</a></h5>
              </div>

            </div>

          </div> -->
        </div>
      </div>
    </div>
    <!-- JS -->
    <script type="text/javascript" src="/resources/js/trove/Auth.js"></script>
    <script>

      var signInUsernameOrEmail = $('#user-email');
      var signInPassword = $('#user-password');
      var signUpEmail = $('#email');
      var signUpName = $('#firstName');
      var signUpPassword = $('#password');
      var errorDiv = $('#error');
      var authHelper = new AuthHelper("${_csrf.token}");

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

      function updatePosish(){
        var wh = $(window).height();
        var ch = $('.auth-form').innerHeight();
        var magicheight = Math.max(( wh - (ch + 80) ) / 2, 0);
        $('.auth-content').css("margin-top", String(magicheight) + "px");
      }

      function land() {
        mixpanel.track("land_onboard_confirm");
      }

      $('#myTabs a').click(function (e) {
        e.preventDefault()
        $(this).tab('show')
      });

      

      $(document).ready(function() {
        land();
        updatePosish();
        var me = getUrlVars()["param.error"];
        // if (me == "bad_credentials") {
        //   document.getElementById('error').innerHTML = "Invalid Login Credentials";
        // } else {
        //   document.getElementById('error').style.display = 'none';
        // }

        $('#signinbut').on('click', function(e){
          // mixpanel.track("auth_login");
          e.preventDefault();
          $('.formy').fadeIn();
          $('.stormy').hide();
        });

        $('#libut').on('click', function() {
          authHelper.authenticateUser(signInUsernameOrEmail.val(), signInPassword.val(),
                  authHelper.createGenericSuccessRedirectCallback('/feed'),
                  authHelper.createGenericFailureCallback(errorDiv));
        });

        $('#create').on('click', function() {
          authHelper.registerUser(signUpName.val(), signUpEmail.val(), signUpPassword.val(), null, null,
                  authHelper.createGenericSuccessRedirectCallback('/feed'),
                  authHelper.createGenericFailureCallback(errorDiv));
        });

      });

      $("#litab").click(function () {  mixpanel.track("login_login");  });
      $("#sutab").click(function () {  mixpanel.track("login_signuptab");  });
      $("#libut").click(function () {  mixpanel.track("login_nonFBauth");  });
      $("#fgp").click(function () {  mixpanel.track("login_forgot_password");  });
      $("#fbli").click(function () {  mixpanel.track("login_FBauth");  });

      $(window).on('resize', function() {
        updatePosish();
      });

      $("#mainsitebutton").click(function(e) {
        window.location.href = "/welcome";
      });

    </script>



    <script>
      (function() {
        var COLORS, Confetti, NUM_CONFETTI, PI_2, canvas, confetti, context, drawCircle, i, range, resizeWindow, xpos;

        NUM_CONFETTI = 400;

        COLORS = [[255, 71, 106], [255, 61, 99], [255, 56, 83], [244, 92, 68], [255, 182, 70]];

        PI_2 = 2 * Math.PI;

        canvas = document.getElementById("world");

        context = canvas.getContext("2d");

        window.w = 0;

        window.h = 0;

        resizeWindow = function() {
          window.w = canvas.width = window.innerWidth;
          return window.h = canvas.height = window.innerHeight;
        };

        window.addEventListener('resize', resizeWindow, false);

        window.onload = function() {
          return setTimeout(resizeWindow, 0);
        };

        range = function(a, b) {
          return (b - a) * Math.random() + a;
        };

        drawCircle = function(x, y, r, style) {
          context.beginPath();
          // context.arc(x, y, r, 0, PI_2, false);
          context.rect(x, y, r*1.5, r*1.5);
          context.fillStyle = style;
          return context.fill();
        };

        xpos = 0.5;

        document.onmousemove = function(e) {
          return xpos = e.pageX / w;
        };

        window.requestAnimationFrame = (function() {
          return window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.oRequestAnimationFrame || window.msRequestAnimationFrame || function(callback) {
            return window.setTimeout(callback, 1000 / 60);
          };
        })();

        Confetti = (function() {
          function Confetti() {
            this.style = COLORS[~~range(0, 5)];
            this.rgb = "rgba(" + this.style[0] + "," + this.style[1] + "," + this.style[2];
            this.r = ~~range(2, 6);
            this.r2 = 2 * this.r;
            this.replace();
          }

          Confetti.prototype.replace = function() {
            this.opacity = 0;
            this.dop = 0.03 * range(1, 4);
            this.x = range(-this.r2, w - this.r2);
            this.y = range(-20, h - this.r2);
            this.xmax = w - this.r;
            this.ymax = h - this.r;
            this.vx = range(0, 2) + 8 * xpos - 5;
            return this.vy = 0.7 * this.r + range(-1, 1);
          };

          Confetti.prototype.draw = function() {
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

        confetti = (function() {
          var j, ref, results;
          results = [];
          for (i = j = 1, ref = NUM_CONFETTI; 1 <= ref ? j <= ref : j >= ref; i = 1 <= ref ? ++j : --j) {
            results.push(new Confetti);
          }
          return results;
        })();

        window.step = function() {
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
