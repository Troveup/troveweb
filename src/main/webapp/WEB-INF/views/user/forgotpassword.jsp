<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
  <title>Trove: Forgot Password</title>
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
        <h2><spring:message code="auth.fgpassword.title"/></h2>
        <h3><spring:message code="auth.fgpassword.subtitle"/></h3>
        <c:url value="/password/forgotsubmit" var="forgotPasswordUrl" />
        <form:form id="forgotpass" class="forgotpass" action="${forgotPasswordUrl}" method="post" modelAttribute="forgotPasswordForm">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          <input id="email" name="email" type="email" placeholder="Email" class="form-control"/>
          <button type="submit" class="btn btn-si"><spring:message code="auth.fgpassword.submit.button"/></button>
        </form:form>
        <!-- link to sign in -->
        <div class="sib"><spring:message code="auth.signup.signin.prenote"/> <a href="${pageContext.request.contextPath}/signin">Log In</a></div>
        <!-- link to sign up -->
        <div class="sib"><spring:message code="auth.signin.signup.prenote"/> <a href="${pageContext.request.contextPath}/signup">Sign Up</a></div>
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

    $('form.forgotpass').validate({
        rules: {
            email: {
                required: true
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
