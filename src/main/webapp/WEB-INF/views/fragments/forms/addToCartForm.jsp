<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<form action="" id="add_to_cart_form" class="form-horizontal">
  <input id="csrf" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
  <input id="itemId" type="hidden" name="itemId" value="nada"/>
  <input id="materialId" type="hidden" name="materialId" value="nada"/>
  <input id="finishId" type="hidden" name="finishId" value="nada"/>
  <input id="size" type="hidden" name="size"/>
  <input id="chainId" type="hidden" name="chain" value="0" />
</form>