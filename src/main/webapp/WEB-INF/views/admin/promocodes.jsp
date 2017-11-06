<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Trove: Welcome to Trove</title>
    <c:import url="../fragments/headers/commonHead.jsp"/>
    <c:import url="../fragments/headers/legalHead.jsp"/>
    <c:import url="../fragments/analytics/all.jsp"/>
    <style>

        option.blanket {
            display: none;
        }

        .special-container {
            display: none;
        }

    </style>
</head>

<body>
<c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
<c:import url="../fragments/nav/topNavBar.jsp"/>
<div class="container-fluid">
    <form class="new-collection form-horizontal"
          action="/admin/adminpromocodespost?${_csrf.parameterName}=${_csrf.token}" method='POST'>
        <div class="form newCollection">
            <fieldset>
                <div class="col-xs-12" id="codetypecontainer">
                    <label for="codetype">Promo Code Type</label>
                    <select name="codetype" id="codetype">
                        <option value="blanket">Blanket</option>
                        <option value="item" selected>Item</option>
                    </select>
                </div>
                <div class="col-xs-12" id="blanketdiscountcategorycontainer" style="display: none;">
                    <label for="blanketdiscountcategory">Discount Category</label>
                    <select name="blanketdiscountcategory" id="blanketdiscountcategory">
                        <option value="subtotal" selected>Subtotal</option>
                        <option value="shipping">Shipping</option>
                    </select>
                </div>
                <div class="col-xs-12" id="itemdiscountcategorycontainer">
                    <label for="itemdiscountcategory">Discount Category</label>
                    <select name="itemdiscountcategory" id="itemdiscountcategory">
                        <option value="overall" selected>Overall</option>
                        <option value="material">Material</option>
                        <option value="materialfinish">Material & Finish</option>
                        <option value="category">Category</option>
                    </select>
                </div>
                <div class="col-xs-12">
                    <label for="valuediscounttype">Discount Type</label>
                    <select name="valuediscounttype" id="valuediscounttype">
                        <option value="percent">Percent</option>
                        <option value="dollar">Flat Dollar Amount</option>
                    </select>
                </div>
                <div>
                    <label for="promocode">Promocode's Code (Not Applicable for Bulk)</label>
                    <input type="text" id="promocode" name="promocode" maxlength="30" class="form-control"
                           placeholder="Promocode"/>
                </div>
                <div>
                    <label for="amount">Discount Amount</label>
                    <input type="number" name="amount" id="amount" step="0.01" min="0" class="form-control"
                           placeholder="25.25"/>
                </div>
                <div>
                    <label for="unlimited">Does this code have a limited number of uses?</label>
                    <select name="unlimited" id="unlimited">
                        <option value="yes" selected>Yes</option>
                        <option value="no">No</option>
                    </select>
                </div>
                <div>
                    <label for="expires">Does this code have an expiration?</label>
                    <select name="expires" id="expires">
                        <option value="yes" selected>Yes</option>
                        <option value="no">No</option>
                    </select>
                </div>
                <div id="expiresdatecontainer">
                    <label for="expiresdate">Expiration Date</label>
                    <input type="date" id="expiresdate" name="expiresdate" class="form-control"/>
                </div>
                <div id="numusescontainer">
                    <label for="numuses">Number of Uses</label>
                    <input type="number" name="numuses" id="numuses" step="1" min="1" class="form-control"
                           placeholder="1"/>
                </div>
                <div id="purposecontainer">
                    <label for="purposefield">Promocode Purpose</label>
                    <input id="purposefield" name="purposefield" type="text" maxlength="255" class="form-control"/>
                </div>
                <div id="materialfinishcontainer" style="display: none;">
                    <label for="materialfinish">Discount Item Material & Finish</label>
                    <select name="materialfinish" id="materialfinish">
                        <c:forEach var="material" items="${materials}">
                            <c:forEach var="finish" items="${material.finishList}">
                                <option value="${material.materialId} ${finish.finishId}">${material.name}
                                    - ${finish.name}</option>
                            </c:forEach>
                        </c:forEach>
                    </select>
                </div>
                <div id="materialcontainer" style="display: none">
                    <label for="material">Discount Item Material</label>
                    <select name="material" id="material">
                        <c:forEach var="material" items="${materials}">
                            <option value="${material.materialId}">${material.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div id="itemcategorycontainer" style="display: none">
                    <label for="itemcategory">Item Category</label>
                    <select name="itemcategory" id="itemcategory">
                        <option value="RING">Ring</option>
                        <option value="BRACELET">Bracelet</option>
                    </select>
                </div>
                <div>
                    <label for="bulkcheck">Bulk Create</label>
                    <select name="bulkcheck" id="bulkcheck">
                        <option value="yes">Yes</option>
                        <option value="no" selected>No</option>
                    </select>
                </div>
                <div id="bulkamountcontainer" style="display: none">
                    <label for="bulkamount">Bulk Number of Promocodes</label>
                    <input type="number" id="bulkamount" name="bulkamount" max="2000" class="form-control"/>
                    <label for="bulkcharlength">Number of Characters</label>
                    <input type="number" id="bulkcharlength" name="bulkcharlength" max="32" class="form-control"/>
                </div>
            </fieldset>
            <input type="submit" value="Create Promocode">
        </div>
    </form>
</div>
</body>


<script>

    <c:if test="${not empty success}">
    $(document).ready(function () {
        <c:choose>
        <c:when test="${not empty bulkGeneratedListOfPromocodes}">

        var promocodes = [<c:forEach var="promocode" varStatus="loop" items="${bulkGeneratedListOfPromocodes}">"${promocode}"${not loop.last ? ',' : ''}</c:forEach>];

        var csvContent = generatePromocodeCsvString(promocodes);
        var encodedUri = encodeURI(csvContent);
        var link = document.createElement("a");
        link.setAttribute("href", encodedUri);
        link.setAttribute("download", "bulk_promocodes.csv");
        link.click();
        </c:when>
        <c:otherwise>
        alert("success!");
        </c:otherwise>
        </c:choose>
    });
    </c:if>

    $('#codetype').change(function () {
        if ($('#codetype option:selected').val() == "blanket") {
            $('#blanketdiscountcategorycontainer').show();
            $('#itemdiscountcategorycontainer').hide();
        }
        else {
            $('#itemdiscountcategorycontainer').show();
            $('#blanketdiscountcategorycontainer').hide();
        }
    });

    $('#itemdiscountcategory').change(function () {
        if ($('#itemdiscountcategory option:selected').val() == "material") {
            $('#materialcontainer').show();
            $('#materialfinishcontainer').hide();
            $('#itemcategorycontainer').hide();

        } else if ($('#itemdiscountcategory option:selected').val() == "materialfinish") {
            $('#materialcontainer').hide();
            $('#materialfinishcontainer').show();
            $('#itemcategorycontainer').hide();

        } else if ($('#itemdiscountcategory option:selected').val() == "category") {
            $('#materialcontainer').hide();
            $('#materialfinishcontainer').hide();
            $('#itemcategorycontainer').show();
        } else {
            $('#materialcontainer').hide();
            $('#materialfinishcontainer').hide();
            $('#itemcategorycontainer').hide();
        }
    });

    $('#unlimited').change(function () {
        if ($('#unlimited option:selected').val() == "yes") {
            $('#numusescontainer').show();
        } else {
            $('#numusescontainer').hide();
        }
    });

    $('#expires').change(function () {
        if ($('#expires option:selected').val() == "yes") {
            $('#expiresdatecontainer').show();
        } else {
            $('#expiresdatecontainer').hide();
        }
    });

    $('#bulkcheck').change(function () {
        if ($('#bulkcheck option:selected').val() == "yes") {
            $('#bulkamountcontainer').show();
        } else {
            $('#bulkamountcontainer').hide();
        }
    });

    function generatePromocodeCsvString(csvArray) {
        var rval = "data:text/csv;charset=utf-8,Promo Codes\n";

        for (var i = 0; i < csvArray.length; ++i) {
            rval += "=\"";
            rval += csvArray[i];
            rval += "\"\n";
        }

        return rval;
    }
</script>

</html>