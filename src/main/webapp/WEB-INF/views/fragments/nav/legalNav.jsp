<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="col-sm-3">
  <div class="cardy sidemenu">
    <ul class="nav bs-docs-sidenav">
      <li class="sidelink"><a class="${pageContext.request.requestURI eq '/WEB-INF/views/legal/privacy.jsp' ? 'active' : ' '}" href="/privacy">Privacy Policy</a></li>
      <li class="sidelink"><a class="${pageContext.request.requestURI eq '/WEB-INF/views/legal/terms.jsp' ? 'active' : ' '}" href="/terms">Terms of Service</a></li>
      <li class="sidelink"><a class="${pageContext.request.requestURI eq '/WEB-INF/views/legal/copyright.jsp' ? 'active' : ' '}" href="/copyright">Copyright Policy</a></li>
      <li class="sidelink"><a class="${pageContext.request.requestURI eq '/WEB-INF/views/legal/sales.jsp' ? 'active' : ' '}" href="/sales">Sales Policy</a></li>
    </ul>
  </div>
</div>





