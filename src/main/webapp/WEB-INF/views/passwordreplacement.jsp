<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:url value="/password/passwordresetsubmit" var="resetUrl" />
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

        <form:form id="signup" action="${resetUrl}" method="post" modelAttribute="forgotPasswordReplacementForm">
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
            <form:password path="password" placeholder="New Password" class="form-control" />
            <form:password path="confirmPassword" placeholder="Confirm New Password" class="form-control" />
            <form:input path="token" type="hidden"/>
          </fieldset>
          <button type="submit" class="btn btn-si">Save</button>
        </form:form>
       </div>
    </div>
  </div>
</body>
</html>
