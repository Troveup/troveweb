<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
  <title>Trove: Forgot Password</title>
  <c:import url="../views/fragments/baseHead.jsp"/>
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
        <a href="/"><img alt="Trovelogo" class="auth-logo" src="/resources/img/trove-logo-beta.png"></a>
        <h2><spring:message code="auth.fgpassword.title"/></h2>
        <h3><spring:message code="auth.fgpassword.subtitle"/></h3>
        <c:url value="forgotsubmit" var="forgotPasswordUrl" />
        <form:form id="signup" action="${forgotPasswordUrl}" method="post" modelAttribute="forgotPasswordForm">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          <input id="email" name="email" type="text" placeholder="Email" class="form-control"/>
          <button type="submit" class="btn btn-si"><spring:message code="auth.fgpassword.submit.button"/></button>
        </form:form>
        <!-- link to sign in -->
        <div class="sib"><spring:message code="auth.signup.signin.prenote"/> <a href="${pageContext.request.contextPath}/signin"><spring:message code="auth.signup.signin.link"/></a></div>
        <!-- link to sign up -->
        <div class="sib"><spring:message code="auth.signin.signup.prenote"/> <a href="${pageContext.request.contextPath}/signup"><spring:message code="auth.signin.signup.link"/></a></div>
       </div>
    </div>
  </div>
</body>
</html>
