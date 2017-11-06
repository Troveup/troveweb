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

        .thumbnail > img {
            width: 130px;
            padding: 10px;
        }

        .thumbnail {
            text-align: center;
            border: 0px solid #FFF;
        }

        .four-points {
            padding: 30px 0px 0px;
        }

        .four-headers {
            font-family: 'Vollkorn', serif;
            font-style: italic;
        }

        .cta-header {
            font-family: 'Vollkorn', serif;
        }

        .welcome-banner-container {
            /*      width: 100%;
                  display:inline-block;
                  background-size:100%;
                  background-repeat: no-repeat;*/
        }

        .try-text {
            position: absolute;
            padding: 20px 20px 40px;
            background-color: rgba(255, 255, 255, .75);
            margin-left: 6%;
        }

        .try-photo-container {
            display: block;
            margin: 0 auto;
        }

        .how-header {
            position: absolute;
            background-color: #ffffff;
            font-size: 28px;
            left: 50%;
            margin-left: -153px;
            top: 30px;
            padding: 15px;
        }

        .try-container {
            display: block;
            margin: 0 auto;
            max-width: 1000px;
        }

        .how-body {
            margin: 60px 20% 30px;
            padding: 30px 5% 30px;
            text-align: center;
            border: 2px solid #dedede;
        }

        @media screen and (min-width: 769px) {
            .welcome-banner-container {
                /*      background-image: url("http://i.imgur.com/TD6CN88.png");
                */
            }
        }

        @media screen and (max-width: 768px) {
            .how-body {
                margin: 60px 10% 30px;
            }

            .welcome-banner-container {
                /*      background-image: url("http://i.imgur.com/F267GJT.png");
                */
            }

            .try-container {
                margin: 30px 5% 30px;
            }

            .try-text {
                position: relative;
                width: 100%;
                text-align: center;
                top: -40px;
                margin-left: 0;
                padding: 0px;

            }
        }

        @media screen and (max-width: 440px) {
            .how-body {
                margin: 60px 0 30px;
            }

            .cta-container {
                left: 0;
                margin-left: 0px;
                width: 100%;
            }
        }

        .how-header-text {
            color: #000;
        }

        .btn-open {
            background-color: transparent;
            color: #000;
            border: 2px solid #8e8b8b;
        }

        .cta-container {
            background-color: rgba(255, 255, 255, 0.75);
            width: 480px;
            z-index: 1;
            position: absolute;
            left: 50%;
            margin-left: -240px;
            text-align: center;
            padding: 50px 0;
        }

        .special-container {
            display: none;
        }

        .heading-underline {
            text-decoration: underline;
        }

        input.variablebox {
            width: 100px;
        }

        .variable-row-style {
            padding-bottom: 10px;
        }

        .section-header {
            text-align: center;
            padding-bottom: 20px;
            padding-top: 10px;
            text-decoration: underline;
            font-weight: bold;
        }

        button.item-level-button {
            display: none;
        }

        .item-level {
            display: none;
        }

        .category-level {
            display: none;
        }

        .initial-hide {
            display: none;
        }

    </style>
</head>

<body>
<c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
<c:import url="../fragments/nav/topNavBar.jsp"/>
<div class="container-fluid">
    <form class="new-collection form-horizontal" onsubmit="return false;" action='/' method='post'>
        <div class="form newCollection">
            <fieldset>
                <div class="section-header">Scope</div>
                <div>
                    <label for="applevel">Application Level</label>
                    <select id="applevel" name="applevel">
                        <option value="global">Global</option>
                        <option value="category">Category</option>
                        <option value="item">Item</option>
                    </select>
                </div>
                <div id="categorywrapper" class="category-level">
                    <div class="col-xs-12">
                        <label for="categoryselect">Category</label>
                        <select id="categoryselect" name="categoryselect">
                            <c:forEach var="category" items="${categories}">
                                <option value="${category}">${category}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div id="itemidwrapper" class="item-level">
                    <label for="itemid">Item ID</label>
                    <input type="number" id="itemid" name="itemid"/>

                    <div>
                        <label for="applyrelatives">Apply to Relatives</label>
                        <input type="checkbox" id="applyrelatives" name="applyrelatives"/>
                    </div>
                </div>

                <div>
                    <label for="applymaterial">Apply to All Materials</label>
                    <input type="checkbox" id="applymaterial" name="applymaterial" checked/>
                </div>

                <div id="appliedmaterialwrapper" class="initial-hide">
                    <label for="appliedmaterial">Material</label>
                    <select id="appliedmaterial" name="appliedmaterial" class="variable-row-style">
                        <c:forEach var="material" items="${materials}">
                            <c:forEach var="finish" items="${material.finishList}">
                                <option value="${material.materialId} ${finish.finishId}">${material.name}
                                    - ${finish.name}</option>
                            </c:forEach>
                        </c:forEach>
                    </select>
                </div>
            </fieldset>
            <hr>

            <div class="section-header">Variables</div>
            <div class="container">
                <div class="row">
                    <div class="col-md-4"></div>
                    <div class="col-md-4">
                        <label for="vartype">Pricing Type</label>
                        <select id="vartype" name="vartype">
                            <option value="fixed">Fixed</option>
                            <option value="formula">Formula</option>
                        </select>
                    </div>
                </div>
                <div class="row variablecontainer initial-hide">
                    <div class="col-md-4"></div>
                    <div class="col-md-4" style="text-align: center; padding-bottom: 20px; padding-top: 20px;">Formula:
                        ((Packaging + Manufacturing Cost) * Markup) + Shipping + Prototype
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-4 heading-underline">Variable</div>
                    <div class="col-md-4 heading-underline">Current</div>
                    <div class="col-md-4 heading-underline">New</div>
                </div>
            </div>
            <div class="fixedcontainer">
                <fieldset>
                    <div class="container">
                        <div class="row variable-row-style">
                            <div class="col-md-4">Fixed Price (Dollars)</div>
                            <div id="fixedpricecurrent" class="col-md-4 markupval">0.00</div>
                            <div class="col-md-4">
                                <input id="fixedpricenew" type="number" step=".01" min="0" class="variablebox">
                            </div>
                        </div>
                    </div>
                </fieldset>
            </div>
            <div class="variablecontainer initial-hide">
                <fieldset>
                    <div class="container">
                        <div class="row variable-row-style">
                            <div class="col-md-4">Markup (0-1)</div>
                            <div id="markupcurrent" class="col-md-4 markupval">.00</div>
                            <div class="col-md-4">
                                <input id="markupnew" type="number" step=".01" max="1" min="0" class="variablebox">
                            </div>
                        </div>
                        <div class="row variable-row-style">
                            <div class="col-md-4">Packaging (dollar amount)</div>
                            <div class="col-md-4 dollarval" id="packagingcurrent">$0.00</div>
                            <div class="col-md-4">
                                <input id="packagingnew" class="variablebox" type="number" step=".01" min="0">
                            </div>
                        </div>
                        <div class="row variable-row-style">
                            <div class="col-md-4">Shipping (dollar amount)</div>
                            <div class="col-md-4 dollarval" id="shippingcurrent">$0.00</div>
                            <div class="col-md-4">
                                <input id="shippingnew" class="variablebox" type="number" step=".01" min="0">
                            </div>
                        </div>
                        <div class="row variable-row-style">
                            <div class="col-md-4">Prototyping (dollar amount)</div>
                            <div class="col-md-4 dollarval" id="prototypingcurrent">$0.00</div>
                            <div class="col-md-4">
                                <input id="prototypingnew" class="variablebox" type="number" step=".01" min="0">
                            </div>
                        </div>
                    </div>
                </fieldset>
            </div>
            <div class="container">
                <div class="row variable-row-style">
                    <div class="col-md-4"></div>
                    <div class="col-md-4">
                        <button id="getcurrent">Get Current Variables</button>
                    </div>
                </div>
            </div>
            <hr>
            <div class="sample-price-container initial-hide">
                <div class="section-header">Sample Price</div>
                <div class="container">
                    <div class="row">
                        <div class="col-md-4"></div>
                        <div class="col-md-4">
                            <div id="newprice" class="variable-row-style">$0.00</div>
                            <label for="material">Material</label>
                            <select id="material" name="material" class="variable-row-style">
                                <c:forEach var="material" items="${materials}">
                                    <c:forEach var="finish" items="${material.finishList}">
                                        <option value="${material.materialId} ${finish.finishId}">${material.name}
                                            - ${finish.name}</option>
                                    </c:forEach>
                                </c:forEach>
                            </select>
                            <button id="getprice">Get Price</button>
                        </div>
                    </div>
                </div>
            </div>
            <input type="submit" id="savebutton" name="savebutton" value="Commit Variables">
        </div>
    </form>
</div>
</body>


<script>
    $('#applevel').change(function () {

        var selectedVal = $('#applevel option:selected').val();

        if (selectedVal == "category") {
            configurePageForCategoryScope(null, true);
            hideSamplePriceContainer();
        }
        else if (selectedVal == "global") {
            configurePageForGlobalScope(true);
            hideSamplePriceContainer();
        }
        else {

            if ($('#vartype option:selected').val() != "fixed") {
                configurePageForItemScope(true);
            } else {
                configurePageForItemScope(false);
            }
        }
    });

    $('#applymaterial').change(function () {

        if ($('#applymaterial').prop('checked')) {
            hideMaterialChooserElement();
        }
        else {
            showMaterialChooserElement();
        }
    });

    $('#vartype').change(function () {

        var selectedVal = $('#vartype option:selected').val();

        if (selectedVal == "fixed") {
            configurePageVariablesFixed();
            hideSamplePriceContainer();
        }
        else {

            if ($('#applevel option:selected').val() == "item") {
                showSamplePriceContainer();
            } else {
                hideSamplePriceContainer();
            }

            configurePageVariablesFormula();
        }
    });

    function showCategoryElements() {
        $('.category-level').show(500);
    }

    function hideCategoryElements() {
        $('.category-level').hide(500);
    }

    function showItemElements() {
        $('.item-level').show(500);
        $('.item-level-button').show(500);
    }

    function hideItemElements() {
        $('.item-level').hide(500);
        $('.item-level-button').hide(500);
    }

    function showMaterialChooserElement() {
        $('#appliedmaterialwrapper').show(500);
    }

    function hideMaterialChooserElement() {
        $('#appliedmaterialwrapper').hide(500);
    }

    function showFixedPricingElements() {
        $('.fixedcontainer').show(500);
    }

    function hideFixedPricingElements() {
        $('.fixedcontainer').hide(500);
    }

    function showFormulaPricingElements() {
        $('.variablecontainer').show(500);
    }

    function hideFormulaPricingElements() {
        $('.variablecontainer').hide(500);
    }

    function showSamplePriceContainer() {
        $('.sample-price-container').show(500);
    }

    function hideSamplePriceContainer() {
        $('.sample-price-container').hide(500);
    }

    function resetItemValues() {
        setItemValues(".00", "0.00", "0.00", "0.00");
    }

    function setItemValues(markupVal, packagingVal, shippingVal, prototypeVal) {
        $('#markupcurrent').text(markupVal);
        $('#packagingcurrent').text("$" + packagingVal);
        $('#shippingcurrent').text("$" + shippingVal);
        $('#prototypingcurrent').text("$" + prototypeVal);
    }

    function setSamplePrice(samplePrice) {
        $('#newprice').text('$' + samplePrice);
    }

    function getCurrentVariables() {

        var data = Object.create(null);
        var appLevel = $('#applevel option:selected').val();
        var itemId = $('#itemid').val();

        if (appLevel == "global" || appLevel == "category" || (appLevel == "item" && itemId && itemId.length > 0)) {

            data['appLevel'] = appLevel;

            var includeMaterials = !$('#applymaterial').prop('checked');

            data['includeMaterials'] = includeMaterials;

            if (includeMaterials) {
                data['materialFinish'] = $('#appliedmaterial option:selected').val();
            }

            if (appLevel == "category") {
                data['category'] = $('#categoryselect option:selected').val();
            }

            if (appLevel == "item") {
                data['item'] = itemId;
            }


            jQuery.ajax({
                url: '/admin/variables',
                data: jQuery.param(data),
                cache: false,
                contentType: false,
                processData: false,
                headers: {
                    'Access-Control-Allow-Origin': '*',
                    "X-CSRF-TOKEN": "${_csrf.token}"
                },
                type: 'GET',
                success: function (data) {

                    if (data) {
                        if (data.materialId && data.finishId) {
                            enableMaterialSelection(data.materialId, data.finishId);
                        }
                        else {
                            disableMaterialSelection();
                        }

                        if (data.priceType == "STATIC") {
                            configureStaticVariables(data.flatPrice);
                        }
                        else {
                            configureFormulaicVariables(data.markupPercentage, data.shippingMarkup, data.prototypeMarkup, data.packagingMarkup);
                            var showSamplePriceIfItemLevel = true;
                        }

                        if (data.applicationLevel == "GLOBAL") {
                            configurePageForGlobalScope(false);
                        }
                        else if (data.applicationLevel == "CATEGORY") {
                            configurePageForCategoryScope(data.category, false);
                        }
                        else {
                            configurePageForItemScope(showSamplePriceIfItemLevel);
                        }
                    }
                    else {
                        alert("No price change data is available for that configuration.");
                    }
                }
            });
        }
        else {
            alert("Please provide an itemId.");
        }

    }

    $('#savebutton').on('click', function () {
        var confirmation = false;
        var appLevel = $('#applevel option:selected').val();
        var category = $('#categoryselect option:selected').val();
        var itemId = $('#itemid').val();
        var applyRelatives = $('#applyrelatives').prop('checked');
        var markupNew = $('#markupnew').val();
        var packagingNew = $('#packagingnew').val();
        var shippingNew = $('#shippingnew').val();
        var prototypingNew = $('#prototypingnew').val();
        var appliedMaterial = $('#appliedmaterial option:selected').val();
        var includeMaterial = !$('#applymaterial').prop('checked');
        var variableType = $('#vartype option:selected').val();
        var fixedPriceNew = $('#fixedpricenew').val();

        if (appLevel == "global") {
            confirmation = confirm("This will overwrite pricing for ALL items.  Would you like to continue?");
        }
        else if (appLevel == "category") {
            confirmation = confirm("This will overwrite pricing for ALL items for category " + category + ".  Would you like to continue?");
        }
        else if (appLevel == "item" && applyRelatives) {
            confirmation = confirm("This will overwrite pricing for ALL items that are siblings of item " + itemId + ".  Would you like to continue?");
        }
        else {
            confirmation = true;
        }

        if (confirmation) {

            if (appLevel == "global" || appLevel == "category" || (appLevel == "item" && itemId && itemId.length > 0)) {
                var data = new FormData();

                data.append('applevel', appLevel);
                data.append('applyrelatives', applyRelatives);

                if (appliedMaterial && appliedMaterial.length > 0) {
                    data.append('appliedMaterial', appliedMaterial);
                }

                data.append('includeMaterial', includeMaterial);

                if (variableType && variableType.length > 0) {
                    data.append('variableType', variableType);
                }

                if (fixedPriceNew && fixedPriceNew.length > 0) {
                    data.append('fixedPrice', fixedPriceNew);
                }

                if (category && category.length > 0) {
                    data.append('category', category);
                }

                if (itemId && itemId.length > 0) {
                    data.append('itemid', itemId);
                }

                if (markupNew && markupNew.length > 0) {
                    data.append('markupnew', markupNew);
                }

                if (packagingNew && packagingNew.length > 0) {
                    data.append('packagingnew', packagingNew);
                }

                if (shippingNew && shippingNew.length > 0) {
                    data.append('shippingnew', shippingNew);
                }

                if (prototypingNew && prototypingNew.length > 0) {
                    data.append('prototypingnew', prototypingNew);
                }


                jQuery.ajax({
                    url: '/admin/pricingpost',
                    data: data,
                    cache: false,
                    contentType: false,
                    processData: false,
                    headers: {
                        'Access-Control-Allow-Origin': '*',
                        "X-CSRF-TOKEN": "${_csrf.token}"
                    },
                    type: 'POST',
                    success: function (data) {
                        if (data.success) {
                            alert("Successful update!  Record count updated: " + data.updateCount);
                        }
                        else {
                            alert("Failure to update!");
                        }
                    }
                });
            }
            else {
                alert("Please make sure the itemId field has something in it.");
            }
        }
    });

    function getSamplePrice() {

        var itemId = $('#itemid').val();
        var markupNew = $('#markupnew').val();
        var packagingNew = $('#packagingnew').val();
        var shippingNew = $('#shippingnew').val();
        var prototypingNew = $('#prototypingnew').val();
        var materialfinish = $('#material option:selected').val();

        if (itemId && itemId.length > 0) {

            var data = Object.create(null);

            if (itemId && itemId.length > 0) {
                data['itemid'] = itemId;
            }

            if (markupNew && markupNew.length > 0) {
                data['markupnew'] = markupNew;
            }

            if (packagingNew && packagingNew.length > 0) {
                data['packagingnew'] = packagingNew;
            }

            if (shippingNew && shippingNew.length > 0) {
                data['shippingnew'] = shippingNew;
            }

            if (prototypingNew && prototypingNew.length > 0) {
                data['prototypingnew'] = prototypingNew;
            }

            data['materialfinish'] = materialfinish;

            $('#newprice').text('Calculating...');

            jQuery.ajax({
                //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
                //url: 'http://localhost:8080/worker/uploadimatmodel',
                url: '/admin/sampleprice',
                //url: 'http://localhost:4444/',
                data: jQuery.param(data),
                cache: false,
                contentType: false,
                processData: false,
                headers: {
                    'Access-Control-Allow-Origin': '*',
                    "X-CSRF-TOKEN": "${_csrf.token}"
                },
                type: 'GET',
                success: function (data) {
                    setSamplePrice(data.price);
                }
            });
        } else {
            alert("Please provide an item ID to fetch the current values.");
        }
    }

    $('#getcurrent').on('click', function () {
        getCurrentVariables();
    });

    $('#getprice').on('click', function () {
        getSamplePrice();
    });

    function configurePageForGlobalScope(shouldResetVariables) {
        $('#applevel').val("global");
        hideCategoryElements();
        hideItemElements();

        if (shouldResetVariables) {
            resetItemValues();
        }

        hideSamplePriceContainer();
    }

    function configurePageForCategoryScope(category, shouldResetVariables) {
        $('#applevel').val("category");
        showCategoryElements();
        hideItemElements();

        if (shouldResetVariables) {
            resetItemValues();
        }
        hideSamplePriceContainer();

        if (category) {
            $("#categoryselect").val(category);
        }
    }

    function configurePageForItemScope(shouldShowSamplePrice) {
        $('#applevel').val("item");
        showItemElements();
        hideCategoryElements();

        if (shouldShowSamplePrice) {
            showSamplePriceContainer();
        }
        else {
            hideSamplePriceContainer();
        }
    }

    function configurePageVariablesFixed() {
        showFixedPricingElements();
        hideFormulaPricingElements();
    }

    function configurePageVariablesFormula() {
        hideFixedPricingElements();
        showFormulaPricingElements();
    }

    function enableMaterialSelection(material, finish) {
        $('#applymaterial').prop('checked', false);
        showMaterialChooserElement();

        if (material && finish) {
            $('#appliedmaterial').val(material + " " + finish);
        }
    }

    function disableMaterialSelection() {
        $('#applymaterial').prop('checked', true);
        hideMaterialChooserElement();
    }

    function configureStaticVariables(price) {
        $('#vartype').val("fixed");
        $('#fixedpricecurrent').text(parseFloat(price).toFixed(2));
        configurePageVariablesFixed();
    }

    function configureFormulaicVariables(markup, shipping, prototype, packaging) {
        $('#vartype').val("formula");
        configurePageVariablesFormula();


        if (markup) {
            $('#markupcurrent').text(parseFloat(markup).toFixed(2));
        } else {
            $('#markupcurrent').text('default');
        }

        if (shipping) {
            $('#shippingcurrent').text(parseFloat(shipping).toFixed(2));
        } else {
            $('#shippingcurrent').text('default');
        }

        if (prototype) {
            $('#prototypingcurrent').text(parseFloat(prototype).toFixed(2));
        } else {
            $('#prototypingcurrent').text('default');
        }

        if (packaging) {
            $('#packagingcurrent').text(parseFloat(packaging).toFixed(2));
        } else {
            $('#packagingcurrent').text('default');
        }
    }

</script>

</html>