<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Trove: Item Print Orders</title>
    <c:import url="../fragments/headers/commonHead.jsp"/>
    <c:import url="../fragments/headers/legalHead.jsp"/>
    <c:import url="../fragments/analytics/all.jsp"/>
    <c:import url="../dashboard/notes.jsp"/>
    <link rel="stylesheet" href="/resources/stylesheets/orders.css">
    <style>
        span.link-text {

        }
    </style>
</head>
<body>
    <c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
    <c:import url="../fragments/nav/topNavBar.jsp"/>
    <div class="container-fluid">
        <div class="container">
            <div class="well">
                <span class="resultheading">Cart Item ID: ${cartItem.cartItemId}</span><br>
                <img class="orderitemimage" src="${cartItem.cartItemReference.defaultCardImageUrl}">
                <div class="orderitems">
                    <span class="itemdataheading">Item Name:</span> <span
                        class="itemdata">${cartItem.frozenItemName}</span><br>
                    <span class="itemdataheading">Item Price:</span> <span
                        class="itemdata">$${cartItem.actualPrice}</span><br>
                    <c:if test="${not empty cartItem.engraveText}"><span
                            class="itemdataheading">Engraving Text:</span> <span
                            class="itemdata">${cartItem.engraveText}</span><br></c:if>
                    <span class="itemdataheading">Item Status:</span> <span
                        class="itemdata">${cartItem.cartItemStatus}</span><br>
                    <span class="itemdataheading">Prototype Requested:</span> <span
                        class="itemdata">${cartItem.prototypeRequested}</span><br>
                    <span class="itemdataheading">Material:</span> <span
                        class="itemdata">${cartItem.materialName}</span><br>
                    <span class="itemdataheading">Finish:</span> <span
                        class="itemdata">${cartItem.finishName}</span><br>
                    <c:if test="${not empty cartItem.chain}"><span class="itemdataheading">Necklace Length: </span>
                        <span class="itemdata">${cartItem.chain.name}</span><br>
                    </c:if>
                    <c:if test="${not empty cartItem.size}"><span class="itemdataheading">Size: </span>
                        <span class="itemdata">${cartItem.size}</span><br>
                    </c:if>
                    <c:if test="${not empty cartItem.cartItemReference.shortLink}">
                        <span class="itemdataheading">Short URL:</span>
                                    <span class="itemdata">
                                        <a href="${baseSiteUrl}s/${cartItem.cartItemReference.shortLink.shortLinkTag}">
                                        ${baseSiteUrl}s/${cartItem.cartItemReference.shortLink.shortLinkTag}</a></span>
                    </c:if>
                </div>
                <c:if test="${empty cartItem.manufacturerOrders}">
                    <span class="resultheading">No Print Orders</span>
                </c:if>
                <c:if test="${not empty cartItem.manufacturerOrders}">
                    <span class="resultheading">Print Orders:</span>
                    <c:set var="printOrderCount" value="0"/>
                    <c:forEach var="printOrder" items="${cartItem.manufacturerOrders}">
                        <c:set var="printOrderCount" value="${printOrderCount + 1}"/>
                        <div class="well note-well">
                            <div class="row rowspace" id="printorder-container">
                                <div class="col-md-6">
                                    <span class="note-text">Order Date:</span> ${printOrder.orderDate}
                                </div>
                                <div class="col-md-6">
                                    <span class="note-text">Order Status:</span>  ${printOrder.status}
                                </div>
                                <div class="col-md-6">
                                    <span class="note-text">Print Supplier:</span> ${printOrder.printSupplier}
                                </div>
                                <div class="col-md-6">
                                    <span class="note-text">${printOrder.printSupplier} Order ID:</span>  ${printOrder.manufacturerOrderId}
                                </div>
                                <div class="col-md-6">
                                    <span class="note-text">Last Status Update Request:</span>  ${printOrder.lastStatusUpdateRequest}
                                </div>
                                <div class="col-md-6">
                                    <span class="note-text">Prototype:</span>  ${printOrder.isPrototype}
                                </div>
                                <div class="col-md-6">
                                    <a href="${printOrder.modelUrl}">Model URL</a>
                                </div>
                                <c:if test="${not empty cartItem.customizations}">
                                    <c:forEach var="customization" items="${cartItem.customizations}">
                                        <c:if test="${not empty customization.size && customization.setDate <= printOrder.orderDate && printOrderCount == customization.setNumber}">
                                            <div class="col-md-6">
                                                <span class="note-text">Size:</span> ${customization.size}
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                </c:if>
            </div>
        </div>
    </div>
</body>
<script>
    var printOrders = $('.note-well');
    $(document).ready(function() {
        for (i = 0; i < printOrders.length; i++) {
            var printOrder = printOrders[i];
            var printDate = printOrder.getAttribute('print-date');
            var numPrintOrder = printOrder.getAttribute('count');
            var customizations = $('.customization');
            for (j = 0; j < customizations.length; j++) {
                var customization = customizations[j];
                var customizationDate = customization.getAttribute('date');
                var customizationNum = customization.getAttribute('setnumber');
                if (numPrintOrder <= customizationNum) {
                    customization.style.display = 'block'; // show the correct customization size
                    break;
                }
            }
        }

    });
</script>
</html>