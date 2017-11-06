<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: tim
  Date: 4/19/15
  Time: 2:51 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home</title>
</head>
<body>
Congrats!  You've signed in!
<sec:authorize access="isAuthenticated()">
To sign out, <form action="${pageContext.request.contextPath}/signout" method="POST">
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
  <button type="submit">Click Here!</button>
</sec:authorize>
<sec:authorize>
  It looks like something went wrong with the auth!
</sec:authorize>
</body>
</html>
