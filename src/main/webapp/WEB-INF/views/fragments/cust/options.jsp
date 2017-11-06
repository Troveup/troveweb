<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="material-selector-container">   
  <span class="selector-title">Material</span>
  <div class="material-selector">
    <select class="ignore" data-mini="true" name="webmenu" id="webmenu">
    <!--Materials-->
      <c:if test="${not empty materials}">
        <c:forEach var="material" items="${materials}">
          <c:forEach var="finish" items="${material.finishList}">
            <option value="${material.materialId} ${finish.finishId}" data-forge="${finish.unityMaterialMapping}">${finish.name}</option>
          </c:forEach>
        </c:forEach>
      </c:if>
    </select>
  </div>
</div>

<div class="item-size-container">
<!-- need this here (temporarily) as a hook for Page module to add the toggle -->
  <c:if test="${item.sizeDropdownAvailable}">
    <span class="selector-title">Size <a href="#sizinghelp"><span style="color: #ee2435;text-decoration:underline;">(Sizing Help)</span></a></span>
    <div data-mini="true"class="item-size">
      <select class="ignore" data-mini="true" name="size" id="size">
        <c:if test="${not empty size}">
          <c:forEach var="individualsize" items="${size}">
            <option value="${individualsize['key']}">${individualsize['value']}</option>
          </c:forEach>
        </c:if>
      </select>
    </div>
  </c:if>
  <c:if test="${not empty availableChains}">
    <div class="chain-container">
      <span class="selector-title">Chain</span>
      <div class="material-selector">
        <select class="ignore" data-mini="true" name="chain" id="chain">
          <c:forEach var="chain" items="${availableChains}">
            <option value="${chain.chainId}" selected>${chain.name} - ${chain.price}</option>
          </c:forEach>
        </select>
      </div>
    </div>
  </c:if>
</div>

<div class="holdprice">
  <div class="item-price-container">
    <span class="selector-title price">Price</span>
    <div class="item-price">-</div>
  </div>
</div>