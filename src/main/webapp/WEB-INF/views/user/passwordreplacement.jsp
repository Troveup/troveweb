<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:url value="/password/passwordresetsubmit" var="resetUrl" />
<html>
<head>
  <title>Trove: Replace Password</title>
  <c:import url="../fragments/headers/commonHead.jsp"/>
  <c:import url="../fragments/headers/authHead.jsp"/>
  <c:import url="../fragments/analytics/all.jsp"/>
</head>
<body>

  <div class="videoRow">
    <video autoplay="" poster="https://storage.googleapis.com/troveup-imagestore/assets/video/trove-vid.jpg" id="bgvid" loop="" class="desktop-only">
      <source src="https://storage.googleapis.com/troveup-imagestore/assets/video/trove-vid.webm" type="video/webm">
      <source src="https://storage.googleapis.com/troveup-imagestore/assets/video/trove-vid.mp4" type="video/mp4">
    </video>
  </div>
  
  <div class="auth-content">
    <div class="auth-content-inner">
      <div class="auth-form">
        <!-- Logo -->
        <a href="/"><img alt="Trovelogo" class="auth-logo" src="https://storage.googleapis.com/troveup-imagestore/assets/img/logo-lt-gray.svg"></a>
        <h2>New Password</h2>
        <h3>Please choose a new password.</h3>
        <c:url value="forgotsubmit" var="forgotPasswordUrl" />

        <form:form id="passwordreset" class="passwordreset" action="${resetUrl}" method="post" modelAttribute="forgotPasswordReplacementForm">
          <!-- Add CSRF token -->
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          <div class="formInfo">
            <s:bind path="*">
              <c:choose>
                <c:when test="${status.error}">
                  <div class="error">Unable to reset password.  Please fix the problems and try again.</div>
                </c:when>
              </c:choose>
            </s:bind>
          </div>
          <fieldset>
            <form:password path="password" id="password" name="password" placeholder="New Password" class="form-control" />
            <form:password path="confirmPassword" id="confirmPassword" name="confirmPassword" placeholder="Confirm New Password" class="form-control" />
            <form:input path="token" type="hidden"/>
          </fieldset>
          <button type="submit" class="btn btn-si">Save</button>
        </form:form>
       </div>
    </div>
  </div>


  <script>
    function updatePosish(){
      var wh = $(window).height();
      var ch = $('.auth-form').innerHeight();
      var magicheight = Math.max(( wh - (ch + 80) ) / 2, 0);
      $('.auth-content').css("margin-top", String(magicheight) + "px");
    }

    $(document).ready(function() {
      updatePosish();
    });
    
    $(window).on('resize', function() {
      updatePosish();
    });

    $('form.passwordreset').validate({
      rules: {
        password: {
          required: true,
          minlength: 6
        },
        confirmPassword: {
          required: true,
          minlength: 6,
          equalTo: '#password'
        }
      },
      highlight: function(element) {
        var id_attr = "#" + $( element ).attr("id") + "1";
        $(element).closest('.col').removeClass('has-success').addClass('has-error');
        $(id_attr).removeClass('glyphicon-ok').addClass('glyphicon-remove');
      },
      unhighlight: function(element) {
        var id_attr = "#" + $( element ).attr("id") + "1";
        $(element).closest('.col').removeClass('has-error').addClass('has-success');
        $(id_attr).removeClass('glyphicon-remove').addClass('glyphicon-ok');
      },
      errorElement: 'span',
      errorClass: 'help-block',
      errorPlacement: function(error, element) {
        if(element.length) {
          error.insertAfter(element);
        } else {
          error.insertAfter(element);
        }
      }
    });

  </script>

</body>
</html>
