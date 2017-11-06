<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
  <title>Trove: Sign In</title>
  <c:import url="../fragments/headers/commonHead.jsp"/>
  <c:import url="../fragments/headers/authHead.jsp"/>
  <c:import url="../fragments/analytics/all.jsp"/>
</head>
<body>

<sec:authorize access="isAnonymous()">
  <!-- Background Video -->
  <div class="videoRow">
    <video autoplay="" poster="https://storage.googleapis.com/trove-qa-teststore/assets/video/trove-vid.jpg" id="bgvid"
           loop="" class="desktop-only">
      <source src="https://storage.googleapis.com/trove-qa-teststore/assets/video/trove-vid.webm" type="video/webm">
      <source src="https://storage.googleapis.com/trove-qa-teststore/assets/video/trove-vid.mp4" type="video/mp4">
    </video>
  </div>
  <!-- Login Container -->
  <div class="auth-content">
    <div class="auth-content-inner">
      <div class="auth-form">
        <a href="/welcome"><img alt="Trovelogo" class="auth-logo"
                                src="https://storage.googleapis.com/troveup-imagestore/assets/img/logo-lt-gray.svg"></a>
        <h2>3D Printed Jewelry Designed Just For You.</h2>
        <h3><spring:message code="auth.signin.subtitle"/></h3>
        <div class="auth-tabs">
          <ul id="myTabs" class="nav nav-tabs nav-justified" role="tablist">
            <li><a id="sutab" href="/signup">Sign Up</a></li>
            <li class="active"><a id="litab" href="/signin">Log In</a></li>
          </ul>
        </div>
        <div class="stormy">
          <div class="spinner-display-status">
            <span class="spinner centered"></span>
          </div>
          <div class="spinner-overlay-content show">
            <div id="error"></div>
            <form action="/" onsubmit="return false;" method="POST" role="form">
              <input id="user-email" name="username" type="text" placeholder="Email or Username" class="form-control"/>
              <input id="user-password" name="password" type="password" placeholder="Password" class="form-control"/>
              <button id="loginbutton" type="submit" class="btn btn-si">Log In</button>
            </form>
            <a id="fgp" class="fgpassword" href="${pageContext.request.contextPath}/password/forgotpassword">Forgot
              Password?</a>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- JS -->
  <script type="text/javascript" src="/resources/js/trove/Auth.js"></script>
  <script>

    var emailUsernameField = $('#user-email');
    var passwordField = $('#user-password');
    var loginButton = $('#loginbutton');
    var errorDiv = $('#error');
    var authHelper = new AuthHelper("${_csrf.token}");

    var busySpinnerContent = $('.spinner-display-status');
    var authContent = $('.spinner-overlay-content');

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

    function updatePosish() {
      var wh = $(window).height();
      var ch = $('.auth-form').innerHeight();
      var magicheight = Math.max(( wh - (ch + 80) ) / 2, 0);
      $('.auth-content').css("margin-top", String(magicheight) + "px");
    }

    function land() {
      mixpanel.track("land_login");
    }

    $(document).ready(function () {
      land();
      updatePosish();

      loginButton.on('click', function () {

        busySpinnerContent.toggleClass("show");
        authContent.toggleClass("show");

        authHelper.authenticateUser(emailUsernameField.val(), passwordField.val(),
            authHelper.createGenericSuccessRedirectCallback('/feed'),
            getCustomFailureCallback(errorDiv));

      });
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

    $(window).on('resize', function () {
      updatePosish();
    });

    function getCustomFailureCallback(errorMessageObjectHandle) {
      return function (data) {

        var errorMessageDiv = $(errorMessageObjectHandle);

        if (data.errorMessage != null && data.errorMessage.length > 0) {
          var errorMessage = data.errorMessage;
        }
        else {
          errorMessage = "Oops!  Something went wrong.  Please try that again.";
        }

        errorMessageDiv.html(errorMessage);
        errorMessageDiv.show();

        busySpinnerContent.toggleClass("show");
        authContent.toggleClass("show");
      }
    }

  </script>
</sec:authorize>

<sec:authorize access="isAuthenticated()">
  <script>
    $(document).ready(function () {
      window.location.replace("/feed");
    });
  </script>
</sec:authorize>

</body>
</html>
