<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<span class="spinner shape"></span>
<div data-value="true" id="webglFlag"></div>
<canvas id="canvas" oncontextmenu="event.preventDefault()" height="800px" width="1100px"></canvas>
<div style="display: none;" id="modelFilename">${item.customizerFilename}</div>
<div style="display: none;" id="modelPath">${item.customizerPath}</div>
<div style="display: none;" id="modelParentID">${item.itemId}</div>
