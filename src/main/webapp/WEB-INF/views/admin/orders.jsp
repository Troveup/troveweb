<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Trove: Welcome to Trove</title>
    <c:import url="../fragments/headers/commonHead.jsp"/>
    <c:import url="../fragments/headers/legalHead.jsp"/>
    <c:import url="../fragments/analytics/all.jsp"/>
    <c:import url="../fragments/handlebars/adminTemplates.jsp"/>
    <link rel="stylesheet" href="/resources/stylesheets/orders.css">
    <script src="/resources/js/vendor/handlebars.js"></script>
</head>

<body>
<c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
<c:import url="../fragments/nav/topNavBar.jsp"/>
<div class="container-fluid" style="margin-top: 50px;">
    <div class="container">
        <div id="filterselectcontainer">
            <div class="well">
                <span class="resultheading">Result Filter/Search</span>
                <div class="row">
                    <div class="col-md-4">
                        <div>
                            <span class="searchheading">Result Type</span><br>
                            <label for="filterselect">Result Filter</label>
                            <select id="filterselect" class="filterinput" name="filterselect">
                                <option value="filtered" selected>Filtered</option>
                                <option value="search">Search</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="filtertypes">
                            <span class="searchheading">Filter Type</span><br>
                            <label for="filtertypeselect">Select Filter Type</label>
                            <select id="filtertypeselect" class="filterinput">
                                <option value="all" selected>All</option>
                                <option value="orderstatus">Order Status</option>
                                <option value="daterange">Date Range</option>
                                <option value="amountrange">Amount Range</option>
                                <option value="cartitemstatus">Cart Item Status</option>
                                <option value="cartitemcategory">Cart Item Category</option>
                            </select>
                        </div>
                        <div class="searchtypes" style="display: none;">
                            <span class="searchheading">Search Type</span><br>
                            <label for="searchtypeselect">Select Search Type</label>
                            <select id="searchtypeselect" class="filterinput">
                                <option value="ordernumber">Order Number</option>
                                <option value="orderid">Order ID</option>
                                <option value="userid">User ID</option>
                                <option value="username">Username</option>
                                <option value="useremail">User Email</option>
                                <option value="name">Name</option>
                                <option value="promocode">Promo Code</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="filters" style="display: none;">
                            <span class="searchheading">Filter Parameters</span><br>
                            <div id="orderstatustypeselector">
                                <label for="orderstatustypedropdown">Order Status Filter</label>
                                <select id="orderstatustypedropdown" class="filterinput">
                                    <c:forEach var="orderstatustype" items="${orderstatustypes}">
                                        <option value="${orderstatustype}">${orderstatustype}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div id="daterangeselector">
                                <label for="begindaterange">Begin Date</label>
                                <input type="date" id="begindaterange" class="filterinput"/>
                                <label for="enddaterange">End Date</label>
                                <input type="date" id="enddaterange" class="filterinput"/>
                            </div>
                            <div id="amountrangeselector">
                                <label for="beginamountrange">Low Amount</label>
                                <input id="beginamountrange" type="number" step=".01" min="0" class="filterinput">
                                <label for="endamountrange">High Amount</label>
                                <input id="endamountrange" type="number" step=".01" min="0" class="filterinput">
                            </div>
                            <div id="cartitemstatusselector">
                                <label for="cartitemstatustypedropdown">Cart Item Status Filter</label>
                                <select id="cartitemstatustypedropdown" class="filterinput">
                                    <c:forEach var="cartitemstatustype" items="${cartitemstatustypes}">
                                        <option value="${cartitemstatustype}">${cartitemstatustype}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div id="cartitemcategoryselector">
                                <label for="cartitemcategorydropdown">Cart Item Category</label>
                                <select id="cartitemcategorydropdown" class="filterinput">
                                    <c:forEach var="cartitemcategorytype" items="${cartitemcategorytypes}">
                                        <option value="${cartitemcategorytype}">${cartitemcategorytype}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="searches" style="display: none;">
                            <span id="searchtypeselectheading" class="searchheading" style="padding-bottom: 20px;">Search Parameters</span><br>
                            <span id="ordernumberheading">Order Number</span>
                            <span id="orderidheading">Order ID</span>
                            <span id="useridheading">User ID</span>
                            <span id="usernameheading">Username</span>
                            <span id="useremailheading">User Email</span>
                            <span id="nameheading">Name</span>
                            <span id="promocodeheading">Promo Code</span>
                            <input type="text" id="searchinput" class="filterinput">
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-4"></div>
                    <div class="col-md-4">
                        <button id="applybutton" onclick="handleApplyButtonAction()">Apply Search</button>
                    </div>
                </div>
            </div>
        </div>
        <div id="resultscontainer"></div>
    </div>
</div>
</body>

<script>

    var ajaxUrlPrefix = '/admin/ajax/';
    var pageNumber = 0;
    var pagelimit = 10;
    var enterKeyMapping = 13;

    var orderContainer = $('#resultscontainer');
    var orderRowHtmlTemplate = Handlebars.compile($('#adminorderrow').html());
    var processingAjaxRequest = false;
    var atLastPage = false;
    var scrollAjaxFunction = getAllOrders;

    var filterTypeHandle = $('.filtertypes');
    var searchTypeHandle = $('.searchtypes');
    var filtersHandle = $('.filters');
    var searchesHandle = $('.searches');
    var orderStatusTypeSelectorHandle = $('#orderstatustypeselector');
    var dateRangeSelectorHandle = $('#daterangeselector');
    var amountRangeSelectorHandle = $('#amountrangeselector');
    var cartItemStatusSelectorHandle = $('#cartitemstatusselector');
    var cartItemCategorySelectorHandle = $('#cartitemcategoryselector');

    var orderNumberHeading = $('#ordernumberheading');
    var orderIdHeading = $('#orderidheading');
    var userIdHeading = $('#useridheading');
    var userNameHeading = $('#usernameheading');
    var userEmailHeading = $('#useremailheading');
    var nameHeading = $('#nameheading');
    var promoCodeHeading = $('#promocodeheading');

    var filterSelectDropdown = $('#filterselect');
    var filterTypeSelectDropdown = $('#filtertypeselect');
    var searchTypeSelectDropdown = $('#searchtypeselect');

    var statusDropdown = $('#orderstatustypedropdown');
    var beginDateInput = $('#begindaterange');
    var endDateInput = $('#enddaterange');
    var lowAmountInput = $('#beginamountrange');
    var highAmountInput = $('#endamountrange');
    var cartItemStatusDropdown = $('#cartitemstatustypedropdown');
    var cartItemCategoryDropdown = $('#cartitemcategorydropdown');

    var searchInputBox = $('#searchinput');
    var searchButton = $('#applybutton');

    var allInputs = $('.filterinput');


    $(document).ready(function () {

        $(window).scroll(function () {
            if (!atLastPage && !processingAjaxRequest && ($(window).height() - $(window).scrollTop() <= ($(document).height()) * 0.50)) {
                scrollAjaxFunction(pageNumber);
            }
        });

        filterSelectDropdown.val('filtered');

        filterSelectDropdown.change(function () {
            if (filterSelectDropdown.find(':selected').val() == "filtered") {
                resetDefaultFilterMode();
            }
            else {
                resetDefaultSearchMode();
            }
        });

        filterTypeSelectDropdown.change(function () {
            var selectionValue = filterTypeSelectDropdown.find(':selected').val();

            hideAllFilters();

            if (selectionValue == "all") {
                disableFiltersAndSearchesSections();
            } else {

                enableFiltersSection();

                if (selectionValue == "orderstatus") {
                    showOrderStatusTypeSelectorHandle();
                } else if (selectionValue == "daterange") {
                    showDateRangeSelectorHandle();
                } else if (selectionValue == "amountrange") {
                    showAmountRangeSelectorHandle();
                } else if (selectionValue == "cartitemstatus") {
                    showCartItemStatusSelectorHandle();
                } else if (selectionValue == "cartitemcategory") {
                    showCartItemCategorySelectorHandle();
                }
            }
        });

        searchTypeSelectDropdown.change(function () {
            var selectionValue = searchTypeSelectDropdown.find(':selected').val();

            hideAllSearches();

            enableSearchesSection();

            if (selectionValue == "ordernumber") {
                showSearchOrderNumberHeading();
            } else if (selectionValue == "orderid") {
                showSearchOrderIdHeading();
            } else if (selectionValue == "userid") {
                showSearchUserIdHeading();
            } else if (selectionValue == "username") {
                showSearchUsernameHeading();
            } else if (selectionValue == "useremail") {
                showSearchUserEmailHeading();
            } else if (selectionValue == "name") {
                showSearchNameHeading();
            } else if (selectionValue == "promocode") {
                showSearchPromoCodeHeading();
            }
        });

        <c:choose>
        <c:when test="${not empty state}">
        restorePageState("${state}");
        </c:when>
        <c:otherwise>
        getAllOrders(pageNumber);
        </c:otherwise>
        </c:choose>

    });

    function getAllOrders(pagenumber) {
        var data = initializeDataObjectWithPageVariables(pagenumber);
        performAjaxGet(appendOrderHtmlData, ajaxUrlPrefix + 'ordersall', data);
    }

    function getOrdersByStatus(pagenumber) {
        var data = initializeDataObjectWithPageVariables(pagenumber);
        data['status'] = statusDropdown.find(':selected').val();

        performAjaxGet(appendOrderHtmlData, ajaxUrlPrefix + 'ordersbystatus', data);
    }

    function getOrdersByDateRange(pagenumber) {
        var data = initializeDataObjectWithPageVariables(pagenumber);
        data['begindate'] = beginDateInput.val();
        data['enddate'] = endDateInput.val();

        performAjaxGet(appendOrderHtmlData, ajaxUrlPrefix + 'ordersbydaterange', data);
    }

    function getOrdersByAmountRange(pagenumber) {
        var data = initializeDataObjectWithPageVariables(pagenumber);
        data['beginamount'] = lowAmountInput.val();
        data['endamount'] = highAmountInput.val();

        performAjaxGet(appendOrderHtmlData, ajaxUrlPrefix + 'ordersbyamountrange', data);
    }

    function getOrdersByCartItemStatus(pagenumber) {
        var data = initializeDataObjectWithPageVariables(pagenumber);
        data['status'] = cartItemStatusDropdown.find(':selected').val();

        performAjaxGet(appendOrderHtmlData, ajaxUrlPrefix + 'ordersbycartitemstatus', data);
    }

    function getOrdersByCartItemCategory(pagenumber) {
        var data = initializeDataObjectWithPageVariables(pagenumber);
        data['category'] = cartItemCategoryDropdown.find(':selected').val();

        performAjaxGet(appendOrderHtmlData, ajaxUrlPrefix + 'ordersbycartitemcategory', data);
    }

    function getOrdersBySearchOrderNumber(pagenumber) {
        getOrdersBySearchInput(pagenumber, 'ordernumber', 'ordersearchbyordernumber');
    }

    function getOrdersBySearchOrderId(pagenumber) {
        getOrdersBySearchInput(pagenumber, 'orderid', 'ordersearchbyorderid');
    }

    function getOrdersBySearchUserId(pagenumber) {
        getOrdersBySearchInput(pagenumber, 'userid', 'ordersearchbyuserid');
    }

    function getOrdersBySearchUsername(pagenumber) {
        getOrdersBySearchInput(pagenumber, 'username', 'ordersearchbyusername');
    }

    function getOrdersBySearchUserEmail(pagenumber) {
        getOrdersBySearchInput(pagenumber, 'email', 'ordersearchbyemail');
    }

    function getOrdersBySearchName(pagenumber) {
        getOrdersBySearchInput(pagenumber, 'name', 'ordersearchbyname');
    }

    function getOrdersBySearchPromoCode(pagenumber) {
        getOrdersBySearchInput(pagenumber, 'promocode', 'ordersearchbypromocode');
    }

    function getOrdersBySearchInput(pagenumber, requestparamindex, endpointurl) {
        var data = initializeDataObjectWithPageVariables(pagenumber);
        data[requestparamindex] = searchInputBox.val();

        performAjaxGet(appendOrderHtmlData, ajaxUrlPrefix + endpointurl, data);
    }

    function handleApplyButtonAction() {

        clearSearchResults();
        pageNumber = 0;
        atLastPage = false;

        scrollAjaxFunction = getSeekFunction();

        scrollAjaxFunction(pageNumber);
    }

    function getSeekFunction() {
        var rval = getAllOrders;

        var filterSelectDropdownVal = filterSelectDropdown.find(':selected').val();

        if (filterSelectDropdownVal == 'filtered') {

            var filterTypeDropdownVal = filterTypeSelectDropdown.find(':selected').val();

            if (filterTypeDropdownVal == 'orderstatus') {
                rval = getOrdersByStatus;
            } else if (filterTypeDropdownVal == 'daterange') {
                rval = getOrdersByDateRange;
            } else if (filterTypeDropdownVal == 'amountrange') {
                rval = getOrdersByAmountRange;
            } else if (filterTypeDropdownVal == 'cartitemstatus') {
                rval = getOrdersByCartItemStatus;
            } else if (filterTypeDropdownVal == 'cartitemcategory') {
                rval = getOrdersByCartItemCategory;
            }

        } else if (filterSelectDropdownVal == 'search') {

            var searchTypeDropdownVal = searchTypeSelectDropdown.find(':selected').val();

            if (searchTypeDropdownVal == 'ordernumber') {
                rval = getOrdersBySearchOrderNumber;
            } else if (searchTypeDropdownVal == 'orderid') {
                rval = getOrdersBySearchOrderId;
            } else if (searchTypeDropdownVal == 'userid') {
                rval = getOrdersBySearchUserId;
            } else if (searchTypeDropdownVal == 'username') {
                rval = getOrdersBySearchUsername;
            } else if (searchTypeDropdownVal == 'useremail') {
                rval = getOrdersBySearchUserEmail;
            } else if (searchTypeDropdownVal == 'name') {
                rval = getOrdersBySearchName;
            } else if (searchTypeDropdownVal == 'promocode') {
                rval = getOrdersBySearchPromoCode;
            }

        }

        return rval;

    }

    function clearSearchResults() {
        orderContainer.empty();
    }

    function performAjaxGet(callback, ajaxUrl, getData) {
        processingAjaxRequest = true;
        jQuery.ajax({
            url: ajaxUrl,
            data: jQuery.param(getData),
            cache: false,
            contentType: false,
            processData: false,
            headers: {
                'Access-Control-Allow-Origin': '*',
                "X-CSRF-TOKEN": "${_csrf.token}"
            },
            type: 'GET',
            success: function (returnData) {
                callback(returnData.orders);
                processingAjaxRequest = false;
                pageNumber = pageNumber + 1;

                if (returnData.orders.length < pagelimit) {
                    atLastPage = true;
                }

            }
        });
    }

    function savePageState() {
        var pageData = getAllInputValues();
        pageData = JSON.stringify(pageData);

        var formData = new FormData();
        formData.append("pageStateString", pageData);

        //Fire and forget
        jQuery.ajax({
            url: '/admin/ajax/preserveordersmenustate',
            data: formData,
            cache: false,
            contentType: false,
            processData: false,
            headers: {
                'Access-Control-Allow-Origin': '*',
                "X-CSRF-TOKEN": "${_csrf.token}"
            },
            type: 'POST',
            success: function () {
            }
        });
    }

    function orderClickRedirect(orderId) {
        savePageState();
        window.open('/admin/order/' + orderId);
    }

    function initializeDataObjectWithPageVariables(pagenumber) {

        var dataobject = Object.create(null);

        dataobject['pagenumber'] = pagenumber;
        dataobject['pagelimit'] = pagelimit;

        return dataobject;
    }

    function appendOrderHtmlData(data) {
        var html = orderRowHtmlTemplate(data);
        orderContainer.append(html);
    }

    function showFilterTypes() {
        searchTypeHandle.hide(500);
        filterTypeHandle.show(500);
    }

    function showSearchTypes() {
        filterTypeHandle.hide(500);
        searchTypeHandle.show(500);
    }

    function enableFiltersSection() {
        searchesHandle.hide(500);
        filtersHandle.show(500);
    }

    function enableSearchesSection() {
        filtersHandle.hide(500);
        searchesHandle.show(500);
    }

    function disableFiltersAndSearchesSections() {
        filtersHandle.hide(500);
        searchesHandle.hide(500);
    }

    function hideAllFilters() {
        orderStatusTypeSelectorHandle.hide(500);
        dateRangeSelectorHandle.hide(500);
        amountRangeSelectorHandle.hide(500);
        cartItemStatusSelectorHandle.hide(500);
        cartItemCategorySelectorHandle.hide(500);
    }

    function hideAllSearches() {
        orderNumberHeading.hide(500);
        orderIdHeading.hide(500);
        userIdHeading.hide(500);
        userNameHeading.hide(500);
        userEmailHeading.hide(500);
        nameHeading.hide(500);
        promoCodeHeading.hide(500);
    }

    function showOrderStatusTypeSelectorHandle() {
        orderStatusTypeSelectorHandle.show(500);
    }

    function showDateRangeSelectorHandle() {
        dateRangeSelectorHandle.show(500);
    }

    function showAmountRangeSelectorHandle() {
        amountRangeSelectorHandle.show(500);
    }

    function showCartItemStatusSelectorHandle() {
        cartItemStatusSelectorHandle.show(500);
    }

    function showCartItemCategorySelectorHandle() {
        cartItemCategorySelectorHandle.show(500);
    }

    function showSearchOrderNumberHeading() {
        orderNumberHeading.show(500);
    }

    function showSearchOrderIdHeading() {
        orderIdHeading.show(500);
    }

    function showSearchUserIdHeading() {
        userIdHeading.show(500);
    }

    function showSearchUsernameHeading() {
        userNameHeading.show(500);
    }

    function showSearchUserEmailHeading() {
        userEmailHeading.show(500);
    }

    function showSearchNameHeading() {
        nameHeading.show(500);
    }

    function showSearchPromoCodeHeading() {
        promoCodeHeading.show(500);
    }

    function resetDefaultFilterMode() {
        disableFiltersAndSearchesSections();
        showFilterTypes();
        //filterTypeSelectDropdown.val('all');
    }

    function resetDefaultSearchMode() {
        disableFiltersAndSearchesSections();
        hideAllSearches();
        enableSearchesSection();
        showSearchOrderNumberHeading();
        showSearchTypes();
        //searchTypeSelectDropdown.val('ordernumber');
    }

    function getAllInputValues() {

        var inputNameValues = {};

        for(var i = 0; i < allInputs.length; ++i) {
            inputNameValues[allInputs[i].id] = allInputs[i].value;
        }

        return inputNameValues;
    }

    function restorePageState(pageStateString) {
        var stateObject = JSON.parse(pageStateString);

        for (var key in stateObject) {

            if (!stateObject.hasOwnProperty(key)) continue;
            $("#" + key).val(stateObject[key]);
        }

        filterSelectDropdown.trigger('change');

        if (filterSelectDropdown.find(':selected').val() == "filtered") {
            filterTypeSelectDropdown.trigger('change');
        } else {
            searchTypeSelectDropdown.trigger('change');
        }

        searchButton.trigger('click');
    }

    $(document).keydown(function(event) {
       if (event.which === enterKeyMapping) {
           searchButton.click();
       }
    });

</script>

</html>