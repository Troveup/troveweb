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
    <script src="/resources/js/vendor/handlebars.js"></script>
    <style>

        .orderrow {
            padding-top: 10px;
            background-color: white;
            transition: background-color 0.5s ease;
        }

        .special-container {
            display: none;
        }

        span.bold {
            font-weight: bold;
        }

        span.searchheading {
            text-decoration: underline;
        }

        span.resultheading {
            font-weight: bold;
            text-decoration: underline;
            text-align: center;
            display: block;
            margin-bottom: 20px;
        }

        button.detailsbutton {
            float: right;
            border-radius: 10px;
        }

        button.detailsbutton:hover {
            background-color: lightpink;
            transition: background-color 0.5s ease;
        }

        .datarow {
            overflow: hidden;
        }

        span.bold {
            font-weight: bold;
        }

        .rowspace {
            padding-bottom: 10px;
        }

        img.itemimg {
            width: 150px;
            height: 150px;
            vertical-align: baseline;
        }

        .row-well {
            display: inline-block;
        }

        .img-container {
            display: inline-block;
            vertical-align: top;
        }

        .data-container {
            min-width: 768px;
            display: inline-block;
        }

        .notes-label {
            padding-right: 5px;
        }

        .notes-container {
            max-width: 700px;
            padding-left:20px;
        }

        .new-note-label {
            padding-left: 20px;
        }

        .new-note-textarea-container {
            max-width: 700px;
            padding-left: 20px;
        }

        textarea.new-note-textarea {
            width: 100%;
        }

        button.action-button {
            float: right;
            border-radius: 10px;
        }

    </style>
</head>

<body>
<c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
<c:import url="../fragments/nav/topNavBar.jsp"/>
<div class="container-fluid" style="margin-top: 50px;">
    <div class="container">
        <div id="error-container"></div>
        <div id="filter-select-container">
        </div>
        <div id="results-container"></div>
    </div>
</div>
</body>

<script type="text/javascript" src="/resources/js/trove/AJAX.js"></script>
<script>

    var resultsContainer = $('#results-container');
    var errorContainer = $('#error-container');
    var ajaxHelper = new AJAXHelper("${_csrf.token}");
    var orderRowHtmlTemplate = Handlebars.compile($('#manufacturerorderrow').html());
    var pageLimit = 10;
    var pageNumber = 0;
    var atLastPage = false;
    var processingAjaxRequest = false;

    Handlebars.registerHelper('exists', function(variable, options) {
       if (variable != null) {
           return options.fn(this);
       } else {
           return options.inverse(this);
       }
    });

    Handlebars.registerHelper('evalbool', function(x) {
        return (x === void 0) ? 'undefined' : x.toString();
    });

    $(document).ready(function () {
        getAllOrders();

        $(window).scroll(function() {
            if (!atLastPage && !processingAjaxRequest && ($(window).height() - $(window).scrollTop() <= ($(document).height()) * 0.50)) {
                getAllOrders();
            }
        })
    });

    function getAllOrders() {

        processingAjaxRequest = true;

        var dataObject = ajaxHelper.createOrAppendGetDataObject("pagelimit", pageLimit, null);
        dataObject = ajaxHelper.createOrAppendGetDataObject("pagenumber", pageNumber, dataObject);

        ajaxHelper.performGetRequest(dataObject, "/dashboard/ajax/ordersall", getSuccessAppendCallback(), ajaxHelper.createGenericFailureCallback(errorContainer));
    }

    function getSuccessAppendCallback() {
        return function(data) {
            var rowHtml = orderRowHtmlTemplate(data.items);
            resultsContainer.append(rowHtml);
            processingAjaxRequest = false;
            pageNumber += 1;

            if (data.items.length < pageLimit) {
                atLastPage = true;
            }
        }
    }

    function getEmptySuccessCallback() {
        return function(data) {
            //Do nothing with a successful callback
        }
    }

    function getSuccessAddNoteCallback() {
        return function(data) {

            //Set the text in the note area and unhide it
            var parentContainer = $(data.extraCallbackData["noteArea"]).parent();
            $(data.extraCallbackData["noteArea"]).html(data.extraCallbackData["latestText"]);
            $(parentContainer).show(300);

            //Grab the grand parent level and update the title
            var grandparentContainer = $(data.extraCallbackData["noteArea"]).parent().parent();
            var title = $(grandparentContainer).find('span.notes-label')[0];
            $(title).html('Latest Note by You:');

            //Unhide the label container if it's hidden
            $($(grandparentContainer).find('div.notes-label-container')[0]).show(300);
        }
    }

    function updateManufacturerOrderCompletionDate(context) {
        var dateContext = $(context)[0];

        var dataObject = ajaxHelper.createOrAppendPostDataObject("itemId", $(dateContext).attr('data-itemid'));
        dataObject = ajaxHelper.createOrAppendPostDataObject("completionDate", $(dateContext).val(), dataObject);

        ajaxHelper.performPostRequest(dataObject, "/dashboard/ajax/updatecompletiondate", getEmptySuccessCallback(), ajaxHelper.createGenericFailureCallback(errorContainer));
    }

    function updateManufacturerStatus(context) {
        var statusContext = $(context)[0];

        var dataObject = ajaxHelper.createOrAppendPostDataObject("itemId", $(statusContext).attr('data-itemid'));
        dataObject = ajaxHelper.createOrAppendPostDataObject("status", $(statusContext).val(), dataObject);

        ajaxHelper.performPostRequest(dataObject, "/dashboard/ajax/updatemanufacturerstatus", getEmptySuccessCallback(), ajaxHelper.createGenericFailureCallback(errorContainer));
    }

    function updateManufacturerPrice(context) {
        var priceContext = $(context)[0];

        var dataObject = ajaxHelper.createOrAppendPostDataObject("itemId", $(priceContext).attr('data-itemid'));
        dataObject = ajaxHelper.createOrAppendPostDataObject("price", $(priceContext).val(), dataObject);

        ajaxHelper.performPostRequest(dataObject, "/dashboard/ajax/updatemanufacturerprice", getEmptySuccessCallback(), ajaxHelper.createGenericFailureCallback(errorContainer));
    }

    function addNote(context)
    {
        var buttonContext = $(context)[0];

        var textArea = $(buttonContext).parent().find('textarea.new-note-textarea')[0];
        var noteArea = $(buttonContext).parent().find('.notes-container').children()[0];

        var extraCallbackData = [];
        extraCallbackData["noteArea"] = noteArea;
        extraCallbackData["latestText"] = $(textArea).val();

        var dataObject = ajaxHelper.createOrAppendPostDataObject("itemId", $(textArea).attr('data-itemid'));
        dataObject = ajaxHelper.createOrAppendPostDataObject("noteText", $(textArea).val(), dataObject);

        ajaxHelper.performPostRequest(dataObject, "/dashboard/ajax/addnote", getSuccessAddNoteCallback(), ajaxHelper.createGenericFailureCallback(errorContainer), extraCallbackData);
    }

</script>
</html>