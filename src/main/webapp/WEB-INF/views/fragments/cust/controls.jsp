<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<span class="spinner controls"></span>
<%--<div>--%><c:choose><c:when test="${item.isEngravable}"><div id="customizetext" class="engraveblock hidemob"></c:when><c:otherwise><div id="customizetext" class="engraveblock" style="display: none;"></c:otherwise></c:choose>
  <p>Add an Engraved Message</p>
  <input type="text" data-role="none" id="engravetext" maxlength="16" class="form-control engravebox" placeholder="16 characters max">   
  <label class="engravingfineprint">Where does my engraving appear?<div id="engravehint" class="engravehintstyle" data-placement="right" data-content="Text will be engraved in the inside center of our jewelry, or on the back of flat pendants. Try-on Models do not come with engraved text.">?</div></label>
</div>
<div id="controls"></div>