<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
  <title>Trove: Sign Up</title>
  <c:import url="fragments/headers/commonHead.jsp"/>
  <c:import url="fragments/headers/authHead.jsp"/>
  <c:import url="fragments/analytics/all.jsp"/>

  <meta property="fb:app_id" content="420400984811981"/>
  <meta property="og:title" content="Sign up for Trove"/>
  <meta property="og:description" content="Customize jewelry on Trove and have it 3D-printed in precious metals"/>
  <meta property="og:author" content="FB Trove Author"/>
  <meta property="og:image"
        content="https://storage.googleapis.com/troveup-imagestore/assets/img/facebook-share-photo.png"/>

  <style>
    label.signuplabel {
      float: left;
      font-weight: 400;
      color: #232323;
    }
  </style>
</head>
<body>

<div class="videoRow">
  <video autoplay="" poster="https://storage.googleapis.com/troveup-imagestore/assets/video/trove-vid.jpg" id="bgvid"
         loop="" class="desktop-only">
    <source src="https://storage.googleapis.com/troveup-imagestore/assets/video/trove-vid.webm" type="video/webm">
    <source src="https://storage.googleapis.com/troveup-imagestore/assets/video/trove-vid.mp4" type="video/mp4">
  </video>
</div>

<!-- Sign Up Container -->
<div class="auth-content">
  <div class="auth-content-inner">
    <div class="auth-form">
      <a href="/welcome"><img alt="Trovelogo" class="auth-logo"
                              src="https://storage.googleapis.com/troveup-imagestore/assets/img/logo-lt-gray.svg"></a>
      <h2>3D Printed Jewelry Designed Just For You.</h2>
      <h3><spring:message code="auth.signin.subtitle"/></h3>
      <div class="auth-tabs">
        <ul id="myTabs" class="nav nav-tabs nav-justified" role="tablist">
          <li class="active"><a href="/signup">Sign Up</a></li>
          <li><a href="/signin">Log In</a></li>
        </ul>
      </div>
      <form id="registerform" action="/" onsubmit="return false;" method="post">
        <div class="stormy">
          <div class="spinner-display-status">
            <span class="spinner centered"></span>
          </div>
          <div class="spinner-overlay-content show">
            <div id="error" style="display: none;">Email already in use. <br> Please Try again.</div>
            <div class="dormy">
              <div class="has-feedback">
                <input type="email" name="email" id="email" class="form-control"
                       placeholder="Email Address"/>
                <span class="glyphicon form-control-feedback" id="email1"></span>
              </div>
              <div class="has-feedback">
                <input type="text" name="fullName" id="fullName" class="form-control" placeholder="Name"/>
                <span class="glyphicon form-control-feedback" id="firstName1"></span>
              </div>
              <div class="has-feedback">
                <input type="password" name="password" id="password" class="form-control"
                       placeholder="Create a Password"/>
                <span class="glyphicon form-control-feedback" id="password1"></span>
              </div>
            </div>
            <div class="formy">
              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
              <input id="optin" type="hidden" name="optin" value="1"/>
              <button id="create" type="submit" class="btn btn-si">Create an Account</button>
              <h5>By continuing you agree to our <a id="termss" href="/legal/terms" target="_blank">Terms</a>
                <br>
                and <a href="/legal/privacy" target="_blank">Privacy Policy</a></h5>
            </div>
          </div>
        </div>
      </form>
    </div>
  </div>
</div>
<script type="text/javascript" src="/resources/js/trove/Auth.js"></script>
<script>

  var email = $('#email');
  var fullName = $('#fullName');
  var password = $('#password');
  var errorDiv = $('#error');
  var registerForm = $('#registerform');
  var authHelper = new AuthHelper("${_csrf.token}");

  var busySpinnerContent = $('.spinner-display-status');
  var authContent = $('.spinner-overlay-content');

  function updatePosish() {
    var wh = $(window).height();
    var ch = $('.auth-form').innerHeight();
    var magicheight = Math.max(( wh - (ch + 80) ) / 2, 0);
    $('.auth-content').css("margin-top", String(magicheight) + "px");
  }

  $("#sufb").click(function () {
    mixpanel.track("login_signup_FBauth");
  });
  $("#termss").click(function () {
    mixpanel.track("login_termsofservice_privacy");
  });
  $("#create").click(function () {
    mixpanel.track("login_signup_create");
  });

  function land() {
    mixpanel.track("land_signup");
  }

  $(document).ready(function () {
    land();
    updatePosish();

    $('#create').on('click', function () {
      if (registerForm.valid()) {

        busySpinnerContent.toggleClass("show");
        authContent.toggleClass("show");

        authHelper.registerUser(fullName.val(), email.val(), password.val(), null, null,
            authHelper.createGenericSuccessRedirectCallback('/feed'),
            getCustomFailureCallback(errorDiv));
      }
    });

    $('#registerform').validate({
      rules: {
        name: {
          required: true
        },
        email: {
          required: true
        },
        password: {
          required: true,
          minlength: 6
        }
      },
      highlight: function (element) {
        var id_attr = "#" + $(element).attr("id") + "1";
        $(element).closest('.col').removeClass('has-success').addClass('has-error');
        $(id_attr).removeClass('glyphicon-ok').addClass('glyphicon-remove');
      },
      unhighlight: function (element) {
        var id_attr = "#" + $(element).attr("id") + "1";
        $(element).closest('.col').removeClass('has-error').addClass('has-success');
        $(id_attr).removeClass('glyphicon-remove').addClass('glyphicon-ok');
      },
      errorElement: 'span',
      errorClass: 'help-block',
      errorPlacement: function (error, element) {
        if (element.length) {
          error.insertAfter(element);
        } else {
          error.insertAfter(element);
        }
      }
    });

    $(function () {
      $('[data-toggle="tooltip"]').tooltip()
    })
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

</body>
</html>
