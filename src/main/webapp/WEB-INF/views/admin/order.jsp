<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Trove: Welcome to Trove</title>
    <c:import url="../fragments/headers/commonHead.jsp"/>
    <c:import url="../fragments/headers/legalHead.jsp"/>
    <c:import url="../fragments/analytics/all.jsp"/>
    <link rel="stylesheet" href="/resources/stylesheets/orders.css">
</head>

<body>
<c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
<c:import url="../fragments/nav/topNavBar.jsp"/>
<div class="container-fluid" style="margin-top: 50px;">
    <div class="container">
        <div class="row">
            <div class="col-md-4"></div>
            <div class="col-md-4 selectorcontainer">
                <div class="selector checkoutselector selectactive"><span
                        class="selectortext">Checkout Details</span></div>
                <div class="selector orderitemselector"><span class="selectortext">Order Item Details</span></div>
            </div>
        </div>
        <div id="checkoutdetailscontainer">
            <div class="row">
                <div class="col-md-6">
                    <div class="well">
                        <span class="resultheading">Billing Address</span>
                        <div class="data">
                            ${order.billingAddress.firstName} <c:if
                                test="${not empty order.billingAddress.lastName}">${order.billingAddress.lastName}</c:if><br>
                            <c:if test="${not empty order.billingAddress.email}">${order.billingAddress.email}
                                <br></c:if>
                            <c:if test="${not empty order.billingAddress.phone}">${order.billingAddress.phone}
                                <br></c:if>
                            ${order.billingAddress.addressLine1}<br>
                            <c:if test="${not empty order.billingAddress.addressLine2}">${order.billingAddress.addressLine2}
                                <br></c:if>
                            ${order.billingAddress.city}, ${order.billingAddress.subdivision.code} ${order.billingAddress.postalCode}
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="well">
                        <span class="resultheading">Shipping Address</span>
                        <div class="data">
                            ${order.shippingAddress.firstName} <c:if
                                test="${not empty order.shippingAddress.lastName}">${order.shippingAddress.lastName}</c:if><br>
                            <c:if test="${not empty order.shippingAddress.email}">${order.shippingAddress.email}
                                <br></c:if>
                            <c:if test="${not empty order.shippingAddress.phone}">${order.shippingAddress.phone}
                                <br></c:if>
                            ${order.shippingAddress.addressLine1}<br>
                            <c:if test="${not empty order.shippingAddress.addressLine2}">${order.shippingAddress.addressLine2}
                                <br></c:if>
                            ${order.shippingAddress.city}, ${order.shippingAddress.subdivision.code} ${order.shippingAddress.postalCode}
                        </div>
                        <div class="data">
                            <span class="resultheading">Shipping Labels</span>
                            <span class="dataheading">Number of labels available:</span><span class="data">${labelCount}</span><br>
                            <button class="labelsbutton" onclick="window.open('/admin/orderlabel/${order.orderId}')">View Labels</button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="well">
                        <span class="resultheading">Order Details</span><br>
                        <span class="dataheading">Order Date:</span> <span class="data"> ${order.orderDate}</span><br>
                        <span class="dataheading">Order ID:</span> <span class="data"> ${order.orderId}</span><br>
                        <span class="dataheading">Order Number:</span> <span
                            class="data"> ${order.troveOrderNumber}</span><br>
                        <span class="dataheading">Order Status:</span> <span
                            class="data"> ${order.orderStatus}</span><br>
                        <span class="dataheading">Username:</span> <span
                            class="data"> ${order.purchaser.username}</span><br>
                        <span class="dataheading">User Full Name:</span> <span
                            class="data">${order.purchaser.firstName}</span><br>
                        <span class="dataheading">Should Be Gift Wrapped:</span> <span
                            class="data">${order.shouldBeGiftWrapped}</span><br>
                        <span class="dataheading">Include Gift Message Card:</span> <span
                            class="data">${order.includeGiftMessageCard}</span><br>
                        <c:if test="${not empty order.giftMessageCardText}">
                            <span class="dataheading">Gift Card Message</span> <span
                                class="data">${order.giftMessageCardText}</span><br>
                        </c:if>
                        <c:if test="${empty order.giftMessageCardText and order.includeGiftMessageCard}">
                            <span class="dataheading">Gift Card Message</span> <span
                                class="data">Leave Card Blank</span><br>
                        </c:if>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="well">
                        <span class="resultheading">Pricing Details</span>
                        <span class="dataheading">Subtotal:</span> <span class="data">${order.subTotal}</span><br>
                        <span class="dataheading">Tax:</span> <span class="data">${order.tax}</span><br>
                        <span class="dataheading">Shipping:</span> <span class="data">${order.shipping}</span><br>
                        <span class="dataheading">Grand Total:</span> <span class="data">${order.grandTotal}</span><br>
                        <c:if test="${not empty order.storeBalanceOffset}"><span
                                class="dataheading">Store Credit To Be Applied:</span> <span
                                class="data">${order.storeBalanceOffset}</span><br>
                        </c:if>
                        <c:if test="${not empty order.finalSettlementAmount}">
                            <span class="dataheading">Final Settlement Amount:</span> <span
                                class="data">${order.finalSettlementAmount}</span><br>
                        </c:if>
                        <c:if test="${not empty usedPromoCodes}">
                            <span class="resultheading" style="padding-top: 10px;">Applied PromoCodes</span>
                            <c:forEach var="promocode" items="${usedPromoCodes}">
                                <div class="promocode">
                                    <span class="dataheading">Promocode:</span> <span
                                        class="data">${promocode.promoCode}</span><br>
                                    <span class="dataheading">Amount Discounted:</span> <span
                                        class="data">${promocode.priceDelta}</span><br>
                                </div>
                            </c:forEach>
                        </c:if>
                    </div>
                </div>
                <div class="col-md-12">
                    <div class="well row">
                        <span class="resultheading">Order Notes</span>
                        <div class="rowspace">
                            <div class="row rowspace new-note-label">
                                <span class="bold">New Note:</span>
                            </div>
                            <div class="row rowspace new-note-textarea-container">
                                <textarea class="new-note-textarea"></textarea>
                                <button class="action-button" user-id="${userId}" onclick="addNote(this)">Add Note</button>
                            </div>
                        </div>
                        <div id="existing-notes">
                            <c:if test="${not empty order.orderNotes}">
                                <c:forEach var="note" items="${orderNotes}">
                                    <div class="well note-well">
                                        <div class="row rowspace">
                                            <div class="col-md-6">
                                                <span class="note-text">Commenter:</span> ${note.userCreator.firstName}
                                            </div>
                                            <div class="col-md-6">
                                                <span class="note-text">Note Created:</span>  ${note.creationDate}
                                            </div>
                                        </div>
                                        <div class="well note-text-well">
                                            ${note.note}
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="orderitemcontainer" style="display: none;">
            <span class="resultheading">Order Items</span>
            <c:if test="${not empty order.orderItems}">
                <c:forEach var="orderItem" items="${order.orderItems}">
                    <div class="well">
                        <div class="row">
                            <div class="col-md-6">
                                <img class="orderitemimage" src="${orderItem.cartItemReference.defaultCardImageUrl}">
                                <div class="orderitems">
                                    <span class="itemdataheading">Cart Item ID:</span> <span
                                        class="itemdata">${orderItem.cartItemId}</span><br>
                                    <span class="itemdataheading">Item Name:</span> <span
                                        class="itemdata">${orderItem.frozenItemName}</span><br>
                                    <span class="itemdataheading">Item Price:</span> <span
                                        class="itemdata">$${orderItem.actualPrice}</span><br>
                                    <c:if test="${not empty orderItem.engraveText}"><span
                                            class="itemdataheading">Engraving Text:</span> <span
                                            class="itemdata">${orderItem.engraveText}</span><br></c:if>
                                    <span class="itemdataheading">Item Status:</span> <span
                                        class="itemdata">${orderItem.cartItemStatus}</span><br>
                                    <span class="itemdataheading">Prototype Requested:</span> <span
                                        class="itemdata">${orderItem.prototypeRequested}</span><br>
                                    <span class="itemdataheading">Material:</span> <span
                                        class="itemdata">${orderItem.materialName}</span><br>
                                    <span class="itemdataheading">Finish:</span> <span
                                        class="itemdata">${orderItem.finishName}</span><br>
                                    <c:if test="${not empty orderItem.chain}"><span class="itemdataheading">Necklace Length: </span>
                                        <span class="itemdata">${orderItem.chain.name}</span><br>
                                    </c:if>
                                    <c:if test="${not empty orderItem.size}"><span class="itemdataheading">Size: </span>
                                        <span class="itemdata">${orderItem.size}</span><br>
                                    </c:if>
                                    <c:if test="${not empty orderItem.cartItemReference.shortLink}">
                                        <span class="itemdataheading">Short URL:</span>
                                        <span class="itemdata">
                                            <a href="${baseSiteUrl}s/${orderItem.cartItemReference.shortLink.shortLinkTag}">
                                            ${baseSiteUrl}s/${orderItem.cartItemReference.shortLink.shortLinkTag}</a></span>
                                    </c:if>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="row">
                                    <button class="detailsbutton" onclick="window.open('/admin/printorders/${orderItem.cartItemId}')" style="margin-right: 15px">View All Print Orders ></button>
                                </div>
                                <div class="well shippinglabel-well">
                                    <span class="resultheading">Shipping Label: </span>
                                    <div>
                                        <label for="shippinglabels-select">Tracking Number: </label>
                                        <select item-id="${orderItem.cartItemId}" item-type="cart">
                                            <c:choose>
                                                <c:when test="${not empty orderItem.shippingLabel}">
                                                    <option value="${orderItem.shippingLabel.id}" label-date="${orderItem.shippingLabel.creationDate}" label-status="${orderItem.shippingLabel.status}" selected>${orderItem.shippingLabel.trackingNumber}</option>
                                                    <c:forEach var="label" items="${order.shippingLabels}">
                                                        <c:if test="${label.easyPostPostageLabelPrimaryKey ne orderItem.shippingLabel.easyPostPostageLabelPrimaryKey}">
                                                            <option value="${label.id}" label-date="${label.creationDate}" label-status="${label.status}">${label.trackingNumber}</option>
                                                        </c:if>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="" id="empty-shippinglabels-option-${orderItem.cartItemId}"></option>
                                                    <c:forEach var="label" items="${order.shippingLabels}">
                                                        <option value="${label.id}" label-date="${label.creationDate}" label-status="${label.status}">${label.trackingNumber}</option>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                        </select>
                                    </div>
                                    <div id="shippinglabel-desc-${orderItem.cartItemId}">
                                        <c:if test="${not empty orderItem.shippingLabel}">
                                            <div>
                                                <span class="dataheading">Date Created:</span>
                                                <span class="itemdata" id="shippinglabel-date-area-${orderItem.cartItemId}"> ${orderItem.shippingLabel.creationDate}</span>
                                            </div>
                                            <div >
                                                <span class="dataheading">Shipping Label Status:</span>
                                                <span class="itemdata" id="shippinglabel-status-area-${orderItem.cartItemId}"> ${orderItem.shippingLabel.status}</span>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:if>
            <c:if test="${not empty order.genericItemsList}">
                <c:forEach var="genericItem" items="${order.genericItemsList}">
                    <div class="well">
                        <div class="row">
                            <div class="col-md-6">
                                <img class="orderitemimage" src="${genericItem.bagLineItemImage}">
                                <div class="orderitems">
                                  <span class="dataheading">Item Name:</span> <span
                                      class="data">${genericItem.cartDisplayName}</span><br>
                                  <span class="dataheading">Item Price:</span> <span class="data">$${genericItem.price}</span><br>
                                  <span class="dataheading">Attributes:</span><br>
                                  <c:forEach var="bagDisplayAttribute" items="${genericItem.bagDisplayAttributes}">
                                      ${bagDisplayAttribute.key}:  ${bagDisplayAttribute.value}<br>
                                  </c:forEach>
                                </div>
                            </div>
                            <div class="col-md-6 shippinglabel-well">
                                <span class="resultheading">Shipping Label: </span>
                                <div>
                                    <label for="shippinglabels-select">Tracking Number: </label>
                                    <select id="shippinglabels-select" item-id="${genericItem.genericItemId}" item-type="generic">
                                        <c:choose>
                                            <c:when test="${not empty genericItem.shippingLabel}">
                                                <option value="${genericItem.shippingLabel.id}" label-date="${genericItem.shippingLabel.creationDate}" label-status="${genericItem.shippingLabel.status}" selected>${genericItem.shippingLabel.trackingNumber}</option>
                                                <c:forEach var="label" items="${order.shippingLabels}">
                                                    <c:if test="${label.easyPostPostageLabelPrimaryKey ne genericItem.shippingLabel.easyPostPostageLabelPrimaryKey}">
                                                        <option value="${label.id}" label-date="${label.creationDate}" label-status="${label.status}">${label.trackingNumber}</option>
                                                    </c:if>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="" id="empty-shippinglabels-option-${genericItem.genericItemId}"></option>
                                                <c:forEach var="label" items="${order.shippingLabels}">
                                                        <option value="${label.id}" label-date="${label.creationDate}" label-status="${label.status}">${label.trackingNumber}</option>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </select>
                                </div>
                                <div id="shippinglabel-desc-${genericItem.genericItemId}">
                                    <c:if test="${not empty genericItem.shippingLabel}">
                                        <div>
                                            <span class="dataheading">Date Created:</span>
                                                <span class="itemdata" id="shippinglabel-date-area-${genericItem.genericItemId}"> ${genericItem.shippingLabel.creationDate}</span>
                                        </div>
                                        <div >
                                            <span class="dataheading">Shipping Label Status:</span>
                                            <span class="itemdata" id="shippinglabel-status-area-${genericItem.genericItemId}"> ${genericItem.shippingLabel.status}</span>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:if>
        </div>
    </div>
</div>
</body>

<script type="text/javascript" src="/resources/js/trove/AJAX.js"></script>
<script>

    var checkoutDetailContainer = $('#checkoutdetailscontainer');
    var itemDetailContainer = $('#orderitemcontainer');

    var errorContainer = $('#error-container');
    var ajaxHelper = new AJAXHelper("${_csrf.token}");

    var selectors = $('.selector');
    var checkoutSelector = $('.checkoutselector');
    var orderItemSelector = $('.orderitemselector');
    var userId = ${authUser.userId};
    var userFullName = "${authUser.firstName}";
    var orderId = ${order.orderId};

    $(document).ready(function () {
        $('select').change(function() {
            var labelId = $(this).find(':selected').val();
            var itemId = $(this).attr('item-id');
            $('#empty-shippinglabels-option-' + itemId).hide();
            var itemType = $(this).attr('item-type');
            var extraCallbackData = [];
            extraCallbackData["labelId"] = labelId;
            extraCallbackData["labelDate"] = $(this).find(':selected').attr('label-date');
            extraCallbackData["labelStatus"] = $(this).find(':selected').attr('label-status');
            extraCallbackData["itemId"] = itemId;
            extraCallbackData["shippingLabelDateArea"] = $('#shippinglabel-date-area-' + itemId);
            extraCallbackData["shippingLabelStatusArea"] = $('#shippinglabel-status-area-' + itemId);


            var dataObject = ajaxHelper.createOrAppendPostDataObject("labelId", labelId);
            dataObject = ajaxHelper.createOrAppendPostDataObject("itemId", itemId, dataObject);
            dataObject = ajaxHelper.createOrAppendPostDataObject("itemType", itemType, dataObject);

            ajaxHelper.performPostRequest(dataObject, "/admin/ajax/associatelabel", getSuccessAssociateLabelWithItemCallback(), ajaxHelper.createGenericFailureCallback(errorContainer), extraCallbackData);
        });
    });

    function getSuccessAssociateLabelWithItemCallback() {
        return function(data) {
            var labelContainer = $('#shippinglabel-desc-' + data.extraCallbackData["itemId"]);
            if (labelContainer.children().length == 0){ // this is a new label, we need to create/display all html elements
                var dateHeadingSpan = $('<span></span>')
                        .addClass('dataheading')
                        .html('Date Created:');
                var dateSpan = $('<span></span>')
                        .addClass('itemdata')
                        .html(data.extraCallbackData["labelDate"]);
                var dateDiv = $('<div></div>');
                dateDiv.append(dateHeadingSpan);
                dateDiv.append(dateSpan);
                var statusHeadingSpan = $('<span></span>')
                        .addClass('dataheading')
                        .html('Shipping Label Status:');
                var statusSpan = $('<span></span>')
                        .addClass('itemdata')
                        .html(data.extraCallbackData["labelStatus"]);
                var statusDiv = $('<div></div>');
                statusDiv.append(statusHeadingSpan);
                statusDiv.append(statusSpan);
                labelContainer.append(dateDiv);
                labelContainer.append(statusDiv);
            } else {
                var labelDateContainer = data.extraCallbackData["shippingLabelDateArea"];
                labelDateContainer.html(data.extraCallbackData["labelDate"]);
                var labelPrimaryKeyContainer = data.extraCallbackData["shippingLabelStatusArea"];
                labelPrimaryKeyContainer.html(data.extraCallbackData["labelStatus"]);

            }
        }
    }

    function addNote(note) {
        var buttonContext = $(note)[0];

        var textArea = $(buttonContext).parent().find('textarea.new-note-textarea')[0]; //search by css classes
        var noteArea = $('#notes')[0];

        var extraCallbackData = [];
        var noteText = $(textArea).val();
        $(textArea).val("");
        extraCallbackData["noteText"] = noteText;
        extraCallbackData["creatorName"] = userFullName;
        extraCallbackData["timeCreated"] = new Date();

        var dataObject = ajaxHelper.createOrAppendPostDataObject("orderId", orderId);
        dataObject = ajaxHelper.createOrAppendPostDataObject("noteText", noteText, dataObject);
        dataObject = ajaxHelper.createOrAppendPostDataObject("timeCreated", extraCallbackData["timeCreated"], dataObject);
        dataObject = ajaxHelper.createOrAppendPostDataObject("creatorUserId", userId, dataObject);

        ajaxHelper.performPostRequest(dataObject, "/admin/ajax/addnote", getSuccessAddNoteCallback(), ajaxHelper.createGenericFailureCallback(errorContainer), extraCallbackData);
    }

    function getSuccessAddNoteCallback() {
        return function(data) {
            //  recreating the note element. starting with the most inside element.
            // commenter, creation time elements
            var commenterSpan = $('<span></span>')
                    .addClass('note-text')
                    .html('Commenter:');
            var commenterCol = $('<div></div>')
                    .addClass('col-md-6')
                    .html(commenterSpan);
            commenterCol.append(" " + data.extraCallbackData["creatorName"]);
            var noteCreatedSpan = $('<span></span>')
                    .addClass('note-text')
                    .html('Note Created:');
            var noteCreatedCol = $('<div></div>')
                    .addClass('col-md-6')
                    .html(noteCreatedSpan);
            noteCreatedCol.append(" " + data.extraCallbackData["timeCreated"]);
            var infoRow = $('<div></div>')
                    .addClass('row rowspace');
            commenterCol.appendTo(infoRow);
            noteCreatedCol.appendTo(infoRow);
            // create the actual note element
            var noteText = $('<div></div>')
                    .addClass('well note-text-well')
                    .html(" " + data.extraCallbackData["noteText"]);
            // create a well for the whole note and add elements
            var noteWell = $('<div></div>')
                    .addClass('well note-well');
            infoRow.appendTo(noteWell);
            noteText.appendTo(noteWell);
            //Set the text in the note area and unhide it
            var notesContainer = $('#existing-notes');
            noteWell.prependTo(notesContainer);
        }
    }

    function printOrderClickRedirect(itemId) {
        savePageState();
        window.open('/admin/printorders/' + itemId);
    }

    function showCheckoutDetailContainer() {
        itemDetailContainer.hide(500);
        checkoutDetailContainer.show(500);
    }

    function showItemDetailContainer() {
        checkoutDetailContainer.hide(500);
        itemDetailContainer.show(500);
    }

    function highlightCheckoutSelector() {
        selectors.removeClass('selectactive');
        checkoutSelector.addClass('selectactive');
    }

    function highlightOrderItemSelector() {
        selectors.removeClass('selectactive');
        orderItemSelector.addClass('selectactive');
    }

    checkoutSelector.on('click', function () {
        showCheckoutDetailContainer();
        highlightCheckoutSelector();
    });

    orderItemSelector.on('click', function () {
        showItemDetailContainer();
        highlightOrderItemSelector();
    });


</script>
</html>