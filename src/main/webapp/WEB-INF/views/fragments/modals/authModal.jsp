<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="modal fade authModal" id="authModal" tabindex="-1" role="dialog" aria-labelledby="authModalALabel"
     aria-hidden="true">
  <div class="modal-dialog">
    <div id="authModalContent">
      <div class="modal-header">
        <a href="#" data-dismiss="modal" class="closeModal"><i class="ion-android-close"></i></a>
      </div>
      <div class="auth-content">
        <div class="auth-content-inner">
          <div class="auth-form">
            <p>Sign up to customize jewelry and purchase on Trove.</p>
            <br>
            <div class="auth-tabs">
              <ul id="myTabs" class="nav nav-tabs-justified" role="tablist">
                <li id="signup-tab" role="presentation" class="active"><a role="tab" data-toggle="tab"
                                                                          aria-controls="signup"
                                                                          id="sutab" href="#signup">Sign
                  Up</a></li>
                <li id="signin-tab" role="presentation"><a role="tab" data-toggle="tab"
                                                           aria-controls="signin" id="litab"
                                                           href="#signin">Log In</a></li>
              </ul>
            </div>
            <div id="myTabContent" class="tab-content stormy">
              <div class="spinner-display-status">
                <span class="spinner centered"></span>
              </div>
              <div id="error"></div>
              <div class="spinner-overlay-content show">
                <div role="tabpanel" class="tab-pane fade" id="signin" aria-labelledby="litab">
                  <input id="user-email" name="username" type="text" placeholder="Email or Username"
                         class="form-control"/><br>
                  <input id="user-password" name="password" type="password" placeholder="Password"
                         class="form-control"/><br>
                  <button id="libut" type="submit" class="btn btn-si">Log In</button>
                  <br>
                  <a id="fgp" class="fgpassword"
                     href="${pageContext.request.contextPath}/password/forgotpassword">Forgot
                    Password?</a>
                </div>
                <div role="tabpanel" class="tab-pane fade in active" id="signup"
                     aria-labelledby="sutab">
                  <div class="has-feedback">
                    <input type="email" name="email" id="email" class="form-control"
                           placeholder="Email Address"/><br>
                    <span class="glyphicon form-control-feedback" id="email1"></span>
                  </div>
                  <div class="has-feedback">
                    <input type="text" name="firstName" id="firstName" class="form-control"
                           placeholder="Name"/><br>
                    <span class="glyphicon form-control-feedback" id="firstName1"></span>
                  </div>
                  <div class="has-feedback">
                    <input type="password" name="password" id="password" class="form-control"
                           placeholder="Create a Password"/><br>
                    <span class="glyphicon form-control-feedback" id="password1"></span>
                  </div>
                  <div class="formy">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input id="optin" type="hidden" name="optin" value="1"/>
                    <button id="create" type="submit" class="btn btn-si" style="width:200px;">Create
                      an Account
                    </button>
                    <br>
                    <h5>By continuing you agree to our <a id="termss" href="/legal/terms"
                                                          target="_blank">Terms</a> <br> and <a
                        href="/legal/privacy" target="_blank">Privacy Policy</a></h5>
                  </div>
                </div>
              </div>

            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript" src="/resources/js/trove/Auth.js"></script>
<script type="text/javascript">

  var signupTab = $('#signup-tab');
  var signinTab = $('#signin-tab');
  var signinButton = $('#libut');
  var signupButton = $('#create');
  var authModal = $('#authModal');
  var errorDiv = $('#error');
  <c:choose>
  <c:when test="${not empty isAuthenticated}">
  var authHelper = new AuthHelper("${_csrf.token}", ${isAuthenticated});
  </c:when>
  <c:otherwise>
  var authHelper = new AuthHelper("${_csrf.token}");
  </c:otherwise>
  </c:choose>
  var keyPressHelper = new KeypressHelper();

  var authSuccessCallback = getAuthCustomSuccessCallback;

  keyPressHelper.addKeyPressDetection(keyPressHelper.getEnterKeyCodeNumber(), "user-email", authModalClick);
  keyPressHelper.addKeyPressDetection(keyPressHelper.getEnterKeyCodeNumber(), "user-password", authModalClick);
  keyPressHelper.addKeyPressDetection(keyPressHelper.getEnterKeyCodeNumber(), "email", authModalClick);
  keyPressHelper.addKeyPressDetection(keyPressHelper.getEnterKeyCodeNumber(), "firstName", authModalClick);
  keyPressHelper.addKeyPressDetection(keyPressHelper.getEnterKeyCodeNumber(), "password", authModalClick);

  function centerModal() {
    var wh = Math.min($(window).height(), window.innerHeight);
    var mh = Math.max(( wh - (540) ) / 2, 0);
    $('#authModalContent').css("margin-top", String(mh) + "px");
  }

  function triggerAuthModal() {
    authModal.modal('show');
    centerModal();
  }

  $(window).on('resize', function () {
    centerModal();
  });

  var busySpinnerContent = document.querySelector(".spinner-display-status");
  var authModalContent = document.querySelector(".spinner-overlay-content");

  signinButton.on('click', function () {

    if (errorDiv != null) {
      errorDiv.hide();
    }
    busySpinnerContent.classList.toggle("show");
    authModalContent.classList.toggle("show");
    authHelper.authenticateUser($('#user-email').val(), $('#user-password').val(), authSuccessCallback(), getAuthCustomFailureCallback('#error'));
  });

  signupButton.on('click', function () {

    if (errorDiv != null) {
      errorDiv.hide();
    }

    busySpinnerContent.classList.toggle("show");
    authModalContent.classList.toggle("show");
    authHelper.registerUser($('#firstName').val(), $('#email').val(), $('#password').val(), null, null, authSuccessCallback(), getAuthCustomFailureCallback('#error'));
  });

  $('#myTabs a').click(function (e) {
    e.preventDefault();
    $(this).tab('show')
  });

  /*$(document).on('keypress', function (e) {
    if (e.keyCode == 13) {
      e.preventDefault();

      if (authModal != null && authModal.data('bs.modal').isShown) {
        if (signupTab != null && signupTab.hasClass('active')) {
          signupButton.click();
        } else if (signinTab != null && signinTab.hasClass('active')) {
          signinButton.click();
        }
        return false;
      }
    }
  });*/

  function authModalClick() {
    if (authModal != null && authModal.data('bs.modal').isShown) {
      if (signupTab != null && signupTab.hasClass('active')) {
        signupButton.click();
      } else if (signinTab != null && signinTab.hasClass('active')) {
        signinButton.click();
      }
      return false;
    }
  }

  function getAuthCustomSuccessCallback() {
    return function customAuthCallback(data) {
      authHelper.setIsAuthenticated(true);
      navHelper.setNavStateAuthenticated(data.user.userId, data.user.email, data.user.firstName, data.user.profileImageThumbnailPath);
      authModal.modal('hide');
      if (authHelper.getActionToPerformPostAuth() != null) {
        authHelper.getActionToPerformPostAuth().apply(null, authHelper.getArgsForPostAuthAction());
      }
    }
  }

  function getAuthCustomFailureCallback(errorMessageObjectHandle) {
    return function (data) {
      if (data.errorMessage != null && data.errorMessage.length > 0) {
        var errorMessage = data.errorMessage;
      }
      else {
        errorMessage = "Oops!  Something went wrong.  Please try that again.";
      }
      busySpinnerContent.classList.toggle("show");
      authModalContent.classList.toggle("show");
      $(errorMessageObjectHandle).html(errorMessage);
      $(errorMessageObjectHandle).show();
    }
  }


</script>



