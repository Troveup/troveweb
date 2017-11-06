<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Trove: Welcome to Trove</title>
  <c:import url="../fragments/headers/commonHead.jsp"/>
  <c:import url="../fragments/headers/legalHead.jsp"/>
  <c:import url="../fragments/analytics/all.jsp"/>
  <c:import url="../fragments/handlebars/dashboardTemplates.jsp"/>
  <link rel="stylesheet" href="/resources/stylesheets/orders.css">
  <script src="/resources/js/vendor/handlebars.js"></script>
</head>

<body>
<c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
<c:import url="../fragments/nav/topNavBar.jsp"/>
<div class="container-fluid" style="margin-top: 50px;">
  <div class="container">
    <div id="error-container"></div>
    <div id="filter-select-container">
    </div>
    <div id="results-container">
      <c:forEach items="${notes}" var="note">
        <div class="well row-well">
          <div class="data-container">
            <div class="row rowspace">
              <div class="col-md-4">
                <span class="bold">Commenter:</span>${note.userCreator.firstName}
              </div>
              <div class="col-md-4">
                <span class="bold">Note Created:</span>${note.creationDate}
              </div>
            </div>
            <div class="row rowspace">
              <div class="col-md-4">
                <span class="bold">Note:</span>
              </div>
            </div>
            <div class="row rowspace notes-container">
              <div class="well">
                ${note.note}
              </div>
            </div>
          </div>
        </div>
      </c:forEach>
    </div>
  </div>
</div>
</body>

<script type="text/javascript" src="/resources/js/trove/AJAX.js"></script>
<script>

</script>

</html>