<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div data-value="true" id="webglFlag"></div>
<div style="display: none;" id="itemDisplayName"> ${item.itemName} </div>
<div style="display: none;" id="modelFilename">${item.customizerFilename}</div>
<div style="display: none;" id="modelPath">${item.customizerPath}</div>
<div style="display: none;" id="modelParentID">${item.itemId}</div>
<script id="initialParameters" type="application/json"> ${customizerInput} </script>
<script id="materialOptions" type="application/json"> ${forgeMaterials} </script>
<script id="sizeOptions" type="application/json"> ${size} </script>
<c:if test="${item.shouldIncludeChainDropdown}">
<script id="chainOptions" type="application/json"> ${availableChains} </script>
</c:if>
