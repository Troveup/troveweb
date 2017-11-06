<%--
  Created by IntelliJ IDEA.
  User: tim
  Date: 3/24/15
  Time: 6:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Trove Styleguide</title>
    <h1>Welcome</h1>
    <link href="/resources/stylesheets/styleguide.css" rel="stylesheet">
    <style type="text/css">

    </style>
</head>
<body>
<c:forEach var="section" items="${kss}">
    <p><c:out value="Description: ${section.sectionDescription}"/></p>
    <p><c:out value="Reference: ${section.sectionReference}"/></p>
    <p><c:out value="Markup: ${section.sectionMarkup}"/></p>

    <p>Modifiers: </p>
    <c:forEach var="modifier" items="${section.sectionModifiers}">
        <p><c:out value="Modifier Name: ${modifier.name}"/></p>
        <p><c:out value="Modifier Description: ${modifier.description}"/></p>
        <p><c:out value="${modifier.markup}" escapeXml="false"/></p>
    </c:forEach>
</c:forEach>

    <script src="/resources/scripts/kss.js"></script>
</body>
</html>
