<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="trove" uri="http://troveup.com/jsp/tlds/itemimage" %>
<html>
<head>
  <title>Trove: 3D Printed Jewelry Customized for You</title>
  <c:import url="fragments/baseHead.jsp"/>
  <c:import url="fragments/staticHeader.jsp"/>
</head>

<body onload="">

  <c:import url="fragments/navBar.jsp"/>


  <div>
    <c:if test="${not empty order}">
      <div class="container cards_product_wrapper2">
        <div class="row">

          <!-- <div class="col-sm-3">
            <div class="cardy sidemenu">
              <ul class="nav bs-docs-sidenav">
                <li class="sidelink"><a href="/account/settings">Account Settings</a> </li>
                <li class="sidelink"><a class="active" href="/private/orders">My Orders</a> </li>
                <li class="sidelink"><a href="/help">Help</a> </li>
              </ul>
            </div>
          </div> -->

          <div class="col-sm-12">
            <div class="cardy order">
              <h3>Order <b>${order.vendorOrderNumber}</b></h3>
              <input id="orderId" name="orderId" type="hidden" value="${order.orderId}"/>
              <button id="cancelOrder" data-toggle="modal" data-target="#edit-address-modal" class="btn btn--red btn-md" type="button">Cancel Order</button>
              <a href="/contact"><button class="btn btn--lightgray btn-md" type="button">Contact Us</button></a>
              <!-- ${order.shippingAddress} --><!-- Last Updated: ${order.orderStatus.lastUpdated} -->
              <br><br>
              <div class="row cartheader">
                <div class="col-xs-6 lef">Shipping Address</div>
                <div class="col-xs-6 lef">Billing Address</div>
              </div>
              <div class="row cartitem lef">
                <div class="col-xs-6 form-normal-label">
                  <span>${order.shippingAddress.firstName} ${order.shippingAddress.lastName}
                    <br>${order.shippingAddress.addressLine1}
                    <c:if test="${not empty order.shippingAddress.addressLine2}">
                      <br>${order.shippingAddress.addressLine2}
                    </c:if>
                    <br>${order.shippingAddress.city}, ${order.shippingAddress.subdivision.code} ${order.shippingAddress.postalCode}</span><br>
                </div>
                <div class="col-xs-6 form-normal-label">
                  <span>${order.billingAddress.firstName} ${order.billingAddress.lastName}
                    <br>${order.billingAddress.addressLine1}
                    <c:if test="${not empty order.billingAddress.addressLine2}">
                      <br>${order.billingAddress.addressLine2}
                    </c:if>
                    <br>${order.billingAddress.city}, ${order.billingAddress.subdivision.code} ${order.billingAddress.postalCode}</span><br>
                </div>
              </div>
              <br><br>
              <div class="row cartheader">
                <div class="col-xs-4 lef">Item</div>
                <div class="col-xs-4 lef">Status</div>
                <div class="col-xs-4 rii">Subtotal</div>
              </div>
              <c:forEach var="item" items="${order.orderItems}">
                <div class="row cartitem ">
                  <div class="col-xs-4 lef form-normal-label">
                    <span><b>${item.cartItemReference.itemName}</b>
                      <br>${item.materialName}
                      <br>${item.finishName}</span><br>
                  </div>
                  <div class="col-xs-4 lef form-normal-label">
                    <span class="label label-primary" id="${order.orderStatus.statusName}">${order.orderStatus.statusName}</span>
                  </div>
                  <div class="col-xs-4 form-normal-label rii">
                    ${item.actualPrice}
                  </div>
                </div>
              </c:forEach>
              <div class="row cartitem ">
                <div class="col-xs-4 lef form-normal-label">
                </div>
                <div class="col-xs-4 rii form-normal-label">
                Subtotal: <br>
                Tax: <br>
                Shipping: <br>
                <br>
                Total:
                </div>
                <div class="col-xs-4 form-normal-label rii">
                  ${subtotal}<br>
                  ${tax}<br>
                  ${shipping}<br>
                  <br>
                  <b>${grandtotal}</b>
                </div>
              </div>
              <br><br>
            </div>
          </div>
        </div>
      </div>





        <!-- <div>Subtotal: </div>
        <div></div>
        <div></div>
        <div>Grand Total: </div>
        <div>Last Updated: ${order.orderStatus.lastUpdated}</div>
        <div>Order Shipping Type: ${order.trackingInfo.carrier}</div>
        <div>Cancel boolean: ${isCancelable}</div> -->
    </c:if>
  </div>

 


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->

    <c:import url="fragments/commonJSSources.jsp"/>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
  <script src="../resources/js/vendor/jquery.pjax.js"></script>
  <script src="../resources/js/custom.js"></script>
  

  <script>
    if (${isCancelable}) {
    } else {
      $('#orderId').hide();
    }

    function cancelOrder() {
      var orderId = document.getElementById("orderId").value;
      var data = new FormData();
      data.append("orderId", orderId);
      jQuery.ajax({
        //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
        //url: 'http://localhost:8080/worker/uploadimatmodel',
        url: '/cancelorder',
        //url: 'http://localhost:4444/',
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        headers: {
          'Access-Control-Allow-Origin': '*',
          "X-CSRF-TOKEN": "${_csrf.token}"
        },
        type: 'POST',
        statusCode: {
          304: function() {
            alert("Refund not possible at this time due to order status.");
          },
          402: function() {
            alert("Print order was cancelled, but refund couldn't be processed.  Contact us.");
          },
          200: function() {
            alert("Refund processed properly.");
          }
        }
      });
    }


    $(document).ready(function() {
      setupBlocksProfile();
     
      go();
      window.addEventListener('resize', go);

      function go(){
        setupBlocksProfile();
        // document.querySelector('.width').innerText = document.documentElement.clientWidth;
        // document.querySelector('.height').innerText = document.documentElement.clientHeight;
      }

      $("#cancelOrder").click(function (e) {
        e.preventDefault();
        cancelOrder();
      });



    });
  </script>


  </body>
  </html>
