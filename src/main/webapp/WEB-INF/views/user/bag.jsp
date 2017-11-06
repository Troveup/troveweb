<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="trove" uri="http://troveup.com/jsp/tlds/itemimage" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Trove: 3D Printed Jewelry Customized for You</title>
    <c:import url="../fragments/headers/commonHead.jsp"/>

    <link rel="stylesheet" href="/resources/stylesheets/main.css">
    <link rel="stylesheet" href="/resources/stylesheets/browse.css">
    <c:import url="../fragments/analytics/all.jsp"/>
    <script src="https://js.braintreegateway.com/v2/braintree.js"></script>
    <style>
        .modal-dialog {
            margin: 0px 0px 0px 0px;
            width: 100%;
        }

        button.remove:hover {
            background-color: #8e8b8b;
        }

        .cart-item-name {
            font-weight: 500;
        }

        input.form-control {
            outline: 0;
            font-family: 'Raleway', sans-serif;
            display: inline-block;
            height: 34px;
            padding: 6px 12px;
            font-size: 14px;
            line-height: 1.42857143;
            color: #2E2626;
            background-color: #fff;
            background-image: none;
            -webkit-border-radius: 4px;
            border-radius: 4px;
            background-clip: padding-box;
            -webkit-box-shadow: none;
            box-shadow: none;
            margin-bottom: 15px;
        }

        form.shipping {
            text-align: left;
        }

        .row.form-input {
            margin-left: 15px;
            margin-right: 15px;
        }

        .labyo {
            text-transform: uppercase;
        }

        .modal-footer.back-next {
            margin: 0 15px 0;
        }

        .form-horizontal .has-feedback .form-control-feedback {
            right: 15px;
            top: 20px;
        }

        input:-webkit-autofill {
            -webkit-box-shadow: 0 0 0px 1000px white inset;
        }

        input:focus:-webkit-autofill {
            -webkit-box-shadow: 0 0 0px 1000px white inset;
        }

        select:-webkit-autofill {
            -webkit-box-shadow: 0 0 0px 1000px white inset;
        }

        select:focus:-webkit-autofill {
            -webkit-box-shadow: 0 0 0px 1000px white inset;
        }

        .billingstuff {
            padding: 0px 40px 0px;
        }

        .promostuff {
            padding: 0px 20px 0px 20px;
            text-align: right;

        }

        #promo-button {
            margin-top: -22px;
            margin-left: 20px;
        }

        .promod {
            margin-bottom: 20px;
        }

        .cartitem {
            border-bottom: 2px solid #ffffff;
        }

        .cartitembottom {
            border-bottom: 2px solid #f0f0f0;
        }

        .billingerror {
            padding: 0px 0px 24px 0px;
            color: #F26868;
        }

        h4 {
            text-transform: uppercase;
        }

        .qty {
            display: none;
        }

        .cart_img {
            margin-left: 0px;
        }

        .holder {
            margin: 0px;
            padding: 10px 5px 5px 0px;
        }

        #block1 {
            padding: 10px 0px 0px 0px;
        }

        #block2 {
            display: none;
            background: #FFF;
            padding: 0px;
            text-align: left;
            padding-left: 30px;
        }

        input[type="radio"], input[type="checkbox"] {
            margin: 15px 15px 5px 0px;
        }

        .checkbox input[type=checkbox] {
            position: absolute;
            margin-left: -25px;
        }

        #block3 {
            background: #FFF;
            padding: 5px;
            text-align: left;
            padding-left: 0px;
        }

        .block-left {
            width: 50%;
        }

        textarea {
            margin: 10px 0px;
            height: 100px;
            width: 238px;
            padding: 12px;
            line-height: 1.3em;
            border: 2px solid #DEDEDE;
            resize: none;
            font-size: 14px;
            width: 100%;
        }

        .char {
            color: #CCC;
            text-align: right;
        }

        textarea:focus {
            outline: none;
        }

        @media (max-width: 476px) {
            .cart_img {
                margin-left: 0px;
                height: 100px;
            }

            .cart-item-name {
                font-size: 12px;
            }

            .cart-item-material {
                font-size: 11px;
            }

            .cart-item-size {
                font-size: 11px;
            }

            .quantity {
                display: none;
            }

            .qty {
                display: inline-block;
            }

            .row.form-input {
                margin-left: 0px;
                margin-right: 0px;
            }

            .shippingstuff {
                padding: 20px 5px;
            }

            .col-xs-3, .col-xs-4 {
                padding-right: 5px;
                padding-left: 5px;
            }

            .has-feedback .form-control {
                padding-right: 15.5px;
            }

            .radio label {
                font-weight: 700;
                cursor: pointer;
                font-size: 11px;
                line-height: 20px;
            }

            #special-card {
              padding-left: 10px;
              padding-right: 10px;
            }
        }

        a.lmore {
            text-decoration: underline;
        }

        form {
            margin-bottom: 15px;
        }

        .modal-backdrop {
            background-color: #000;
        }

        .modal-dialog.learn {
            margin: 50px 0px 0px 0px;
            width: 600px;
            left: 50%;
            margin-left: -300px;
        }

        h5 {
            padding: 10px 40px;
        }

        h1 span {
            color: #FFF;
            background: #E10A0A;
            border-radius: 2em;
            display: block;
            width: 1.5em;
            height: 1.5em;
            line-height: 0.8em;
            font-size: 73%;
            padding: 6px;
            margin-left: auto;
            margin-right: auto;
        }

        .modal-content {
            border-radius: 0px;
            border: 0px solid #FFF;
        }

        .checkout-chart-cell {
            padding: 10px;
        }

        .checkout-chart-cell.number {
            text-align: right;
        }

        .main-content {
            display: block;
        }

        .cart-content {
            display: none;
            width: 100%;
            overflow-x: hidden;
        }

        #back0 {
            float: left;
        }

        .labyo {
            text-transform: uppercase;
            font-weight: 700;
            font-size: 10px;
            line-height: 20px;
        }

        .giftwrapcontainer {
            border-top: 2px solid #F0F0F0;
        }

        .lii {
            float: left;
            margin-left: 10px;
        }

        .holder {
            margin-top: 0px;
        }

        .holder .rii {
            font-weight: 600;
        }

        .topmarg {
            margin-top: 5px;
        }

        .giftwrap, .promowrap {
            display: none;
        }

        #chxoutbottom {
            width: 100%;
            padding: 10px;
            font-size: 115%;
        }

        #finalprice {
            font-weight: bold;
        }

        #proo {
            margin-top: 10px;
            width: 100%;
            padding: 10px;
        }

        .subtotal {
            padding: 10px 0px 10px 0px;
        }

        @media (max-width: 992px) {

            .rowmarg {
                padding-bottom: 20px;
            }

            .bottomarg {
                margin-bottom: 16px;
            }

            .topmarg.chec {
                padding-top: 20px;
                padding-bottom: 20px;
                border-top: 2px solid #F0F0F0;
            }
        }

        body {
            padding-top: 67px;
        }

        @media (min-width: 767px) {
            body {
                padding-top: 110px;
            }
        }

        .topmarg.chec {
            padding-bottom: 20px;
        }

        .special-container {
          display: none;
        }



    </style>

    <style>

    body {
      padding-top: 100px;
    }


    @media (min-width: 767px) {
      body {
          padding-top: 140px;
      }
    }

    @media (max-width: 575px) {
      body {
          padding-top: 130px;
      }
      .btn.btn_specialsignup {
        margin-top: 5px;
      }
    }


      


    .checkbox input[type=checkbox] {
        margin-top: 5px;
    }


    .cart-content {
      background: #DEDEDE;
    }

    .cart-nav {
      z-index: 999;
      background: rgb(255, 255, 255);
      width: 100%;
      text-align: center;
      position: fixed;
      top: 0px;
      left: 0px;
      right: 0px;
    }

    .logo-holder img {
      width: 100px;
      padding-top: 15px;
      opacity: 0.6;
    }

    .steps {
      line-height: 16px;
      padding: 15px 0px 0px;
      color: #FFF;
      text-align: center;
      width: auto;
    }

    .step {
      display: inline-block;
      font-size: 13px;
      padding: 10px;
      font-weight: 500;
      color: #656565;
      letter-spacing: 0.06em;
      border-bottom: 3px solid rgb(255, 255, 255);
    }

    .step.active {
      color: #000;
      border-bottom: 3px solid #000;
    }

    #cart-container {
      padding-top: 92px;
      padding-bottom: 100px;
    }

    .cardy.checkout {
      margin-top: 20px;
      border: 1px solid #dedede;
      -webkit-box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
         -moz-box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
              box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
      background: #FFF;
      padding-bottom: 30px;
    }

    #summarybox {
      margin-left: 30px;
      margin-right: -30px;
    }

    .topper {
      text-align: left;
    }

    .account-settings-header {
      padding: 20px 50px 10px;
      border-bottom: 0px solid #d0d0d0;
      font-size: 1.1em;
    }

    .has-feedback .form-control {
      padding-right: 30px;
    }

    .labyo {
      font-weight: 400;
    }

    .shippingstuff {
      padding-left: 5px;
      padding-right: 5px;
    }



    input.form-control {
      font-family: "Roboto", "Helvetica Neue", Helvetica, Arial, sans-serif;
      height: 30px;
      -webkit-border-radius: 0px;
      border-radius: 0px;
    }

    select.form-control {
      font-family: "Roboto", "Helvetica Neue", Helvetica, Arial, sans-serif;
      height: 30px;
      border-radius: 0px;
      -webkit-appearance: none;
      -webkit-border-radius: 0px;
    }

    .form-horizontal .has-feedback .form-control-feedback {
      font-size: 80%;
    }

    .modal-footer {
      text-align: center;
    }

    .btn.big-bottom {
      width: Calc(100% - 10px);
      padding: 15px 20px;
    }

    .btn.big-back {
      width: Calc(100% - 10px);
      padding: 15px 20px;
    }

    #back0 {
      background-color: transparent;
      color: #3E3E3E;
      text-transform: capitalize;
      text-decoration: underline;
      font-style: italic;
    }

    #back0:hover {
      background-color: transparent;
      color: #3E3E3E;
    }

    .btn--red.disabled {
        background-color: #f26868;
    }

    .has-success .form-control {
      -webkit-box-shadow: inset 0 60px 60px rgba(255,255,255,1) !important;
              box-shadow: inset 0 60px 60px rgba(255,255,255,1) !important;
    }

    .has-failure .form-control {
      -webkit-box-shadow: inset 0 60px 60px rgba(255,255,255,1) !important;
              box-shadow: inset 0 60px 60px rgba(255,255,255,1) !important;
    }

    .btn--red.disabled {
      pointer-events: none;
    }

    .row.sidebox {
      padding-top: 20px;
      padding-bottom: 20px;
      border-bottom: 2px solid #DEDEDE;
    }
    .row.sidetotal {
      padding-top: 20px;
    }

    h5 {
      font-size: 15px;
      color: #8e8b8b;
      padding: 40px;
      text-align: left;
      line-height: 1.3em;
      padding: 10px 10px 0px;
    }

    h6 {
      font-size: 1.1em;
      padding: 0px 10px 10px;
      margin-top: 0px;
      margin-bottom: 10px;
    }

    #promo-button {
      margin-top: 0px;
      margin-left: -1;
      height: 30px;
      padding: 0px 40px;
      background: #232323;
    }

    span.labyo {
      font-weight: 400;
      text-align: left;
      display: block;
    }

    #result {
      line-height: 1.5em;
      text-align: left;
      padding: 15px 20px 5px;
    }

    .other-total {
      display: none;
    }


    @media (max-width: 768px) {
      .other-total {
        display: block;
        padding-top: 20px;
        padding-bottom: 20px;
        border-top: 2px solid #DEDEDE;
        padding-left: 25px;
        padding-right: 25px;
      }
      .cart-nav {
        position: relative;
      }
      #cart-container {
        padding: 0px;
      }
      #summarybox {
        display: none;
      }
      .cardy.checkout {
        padding: 0px;
        margin: 0px;
      }
      .account-settings-header.checkout {
        margin: 10px -15px 5px;
        padding: 20px 35px 5px;
      }
      .steps {
        padding: 15px 0px 15px;
      }
      .step {
        padding: 5px;
        margin: 0px 15px;
      }

      #step-one {
        float: left;
      }

      #step-three {
        float: right;
      }

      .shippingstuff {
        padding: 20px 5px;
      }

    }

    @media (min-width: 767px) {
      .step {
        min-width: 180px;
      }

      #special-state-two, #special-state {
        padding: 0px;
      }

      .form-horizontal #special-state-two.has-feedback .form-control-feedback,
      .form-horizontal #special-state.has-feedback .form-control-feedback {
        right: 0px;
      }

    }

    @media (max-width: 476px) {
      .form-horizontal #special-mm.has-feedback .form-control-feedback,
      .form-horizontal #special-yr.has-feedback .form-control-feedback,
      .form-horizontal #special-cvc.has-feedback .form-control-feedback {
        right: 0px;
      }

      .modal-footer.back-next {
          margin: 0px;
      }

      .step {
          padding: 5px 5px;
          margin: 0px 2px;
          font-size: 11px;
      }

    }

    #step-one, #step-two {
      pointer-events: none;
    }

    input::-webkit-outer-spin-button,
    input::-webkit-inner-spin-button {
      -webkit-appearance: none;
      margin: 0; 
    }
    input[type=number] {
      -moz-appearance:textfield;
    }

    select.form-control {
      -webkit-appearance: none;
      -moz-appearance: window;
      background-image: url("https://storage.googleapis.com/troveup-imagestore/assets/img/br_down.png");
      background-repeat: no-repeat;
      background-position: right 10px center;
      background-size: 10px;
    }

    .has-success select.form-control  {
      background-image: none;
    }
    .has-error select.form-control {
      background-image: none;
    }
  
    @-moz-document url-prefix() {
      .css-select-moz {
        background-image: url("https://storage.googleapis.com/troveup-imagestore/assets/img/br_down.png");
        background-repeat: no-repeat;
        background-position: right 10px center;
        position: absolute;
        width: Calc(100% - 10px);
        border: 1px solid #ccc;
        height: 30px;
        background-size: 10px;
      }
      .has-success .css-select-moz  {
        border-color: #3c763d;
        background-image: none;
      }
      .has-error .css-select-moz {
        border-color: #a94442;
      }

      @media (min-width: 768px) {
        #special-state .css-select-moz {
          width: 100%;
        }
      }
    }

    .empty-select-option {
      display: none;
    }



  </style>
</head>

<body>
<c:import url="../fragments/analytics/adwordstag.jsp"/>
<!-- Modal -->
<div class="modal fade" id="learnModal" tabindex="-1" role="dialog" aria-labelledby="learnModalLabel"
     aria-hidden="true">
    <div class="modal-dialog learn">
        <div class="modal-content" style="width: 100%;">
            <div class="modal-body learn">
                <h3>TRY-ON MODELS</h3>

                <h2>How it works</h2>

                <h1><span>1</span></h1>
                <h5>When placing an order, select a free Try-on Model. We'll send a plastic version of your jewelry for
                    you to test out.</h5>

                <h1><span>2</span></h1>
                <h5>Use your Try-On Model to test for fit and design.</h5>

                <h1><span>3</span></h1>
                <h5>Once you're happy with your jewelry, we'll ship your final item. Only pay for the Try-On Model if
                    you decide to cancel your order after receiving the model - $7.49.</h5>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn--lightgray" data-dismiss="modal"
                        style="display:block;margin:0 auto;">Close
                </button>
                <!-- <button type="button"  id="btnCrop" class="btn btn--darkgray" data-dismiss="modal">Update Photo</button> -->
                <!-- <input type="button" id="btnCrop" value="Crop" style="float: right">  -->
            </div>
        </div>
    </div>
</div>

<div class="main-content">
    <c:import url="../fragments/nav/topNavBar.jsp"/>
    <div class="container" id="cart-cont">

        <c:if test="${empty cart.cartItems and empty cart.genericItems}">
            <div class="row">
                <div class="col-md-12"><h4>Shopping Bag</h4></div>
            </div>
            <h5>Customize jewelry to add to your cart!</h5>
        </c:if>

        <!-- cart items  -->
        <c:if test="${not empty cart.cartItems or not empty cart.genericItems}">
            <div class="row">
                <div class="col-xs-6"><h4>Shopping Bag</h4></div>
                <div class="col-xs-6 rii">
                    <div class="subtotal" id="${cart.subTotal}">Subtotal: $${cart.subTotal}</div>
                </div>
            </div>

            <div class="row cartheader">
                <div class="col-xs-3"></div>
                <div class="col-xs-4">Name</div>
                <div class="col-xs-2 quantity">Quantity</div>
                <div class="col-xs-2 qty">Qty</div>
                <div class="col-xs-3" style="text-align: right;">Price</div>
            </div>

            <c:if test="${not empty cart.cartItems}">
                <c:forEach var="cartItem" items="${cart.cartItems}">
                    <div class="row cartitem" id="cartitem-${cartItem.cartItemId}">
                        <div class="col-xs-3">
                            <div class="cart_img_holder">
                                <c:if test="${not empty cartItem.cartItemReference.images}">
                                    <img class="cart_img" src="<trove:itemimagecartitem item="${cartItem.cartItemReference}"
                      itemNumber="0" materials="${materials}" selectedMaterialId="${cartItem.materialId}" selectedFinishId="${cartItem.finishId}"/>">
                                </c:if>
                            </div>
                        </div>
                        <div class="col-xs-4 deets">
                            <div class="cart-item-name">${cartItem.cartItemReference.itemName}</div>
                            <div class="cart-item-material">${cartItem.finishName}</div>
                            <c:if test="${not empty cartItem.engraveText}">
                                <div class="cart-item-size">Custom Engraving: ${cartItem.engraveText}</div>
                            </c:if>
                            <c:if test="${not empty cartItem.chain}">
                                <div class="cart-item-size">Chain: ${cartItem.chain.name}</div>
                            </c:if>
                            <c:if test="${not empty cartItem.size}">
                                <div class="cart-item-size">Size: ${cartItem.size}</div>
                            </c:if>
                            <form id="cartitemform-${cartItem.cartItemId}">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <input type="hidden" name="itemId" value="${cartItem.cartItemId}"/>
                                <input type="hidden" name="generic" value="false"/>
                            </form>
                            <button class="btn btn--gray remove" id="${cartItem.cartItemId}"><span>X Remove</span>
                            </button>
                        </div>
                        <div class="col-xs-2 quant">${cartItem.quantity}</div>
                        <div class="col-xs-3 price" id="cartitemprice-${cartItem.cartItemId}"
                             style="text-align: right;">$${cartItem.actualPrice}</div>
                        <c:if test="${not empty cartItem.chain}">
                            <div class="price" id="cartitemchainprice-${cartItem.cartItemId}"
                                 style="text-align: right;">+ $${cartItem.chain.price}</div>
                        </c:if>
                    </div>

                    <c:if test="${cartItem.canPrototype}">
                        <div id="formItem-${cartItem.cartItemId}" class="row cartitembottom" id="cartitem-${cartItem.cartItemId}">
                            <div class="col-sm-8 col-xs-12">
                                <form class="form-inline protocheck">
                                    <div class="checkbox">
                                        <label>
                                            <input onclick="pushOrPopPrototypeId(${cartItem.cartItemId})" type="checkbox"> Receive a free plastic Try-on Model to check your size and design before we send your final jewelry.
                                            <a class="lrnmore" id="lrnmore" data-toggle="modal" data-target="#learnModal" class="lmore" href="#">Learn More</a>
                                        </label>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </c:if>

                </c:forEach>
            </c:if>
            <c:if test="${not empty cart.genericItems}">
                <c:forEach var="genericItem" items="${cart.genericItems}">
                  <c:choose>
                    <c:when test="${not empty genericItem.itemReferenceId}">
                      <div class="row genericitem" id="genericitem-${genericItem.genericItemId}" data-fbitemid="${genericItem.itemReferenceId}" data-fbitemprice="${genericItem.price}" data-fbitemname="${genericItem.cartDisplayName}">
                    </c:when>
                    <c:otherwise>
                      <div class="row genericitem" id="genericitem-${genericItem.genericItemId}">
                    </c:otherwise>
                  </c:choose>
                    <div class="col-xs-3">
                        <div class="cart_img_holder">
                            <c:if test="${not empty genericItem.bagLineItemImage}">
                                <img class="cart_img" src="${genericItem.bagLineItemImage}">
                            </c:if>
                        </div>
                    </div>
                    <div class="col-xs-4 deets">
                        <div class="cart-item-name">${genericItem.cartDisplayName}</div>
                        <c:forEach var="displayAttribute" items="${genericItem.bagDisplayAttributes}">
                            <div class="cart-item-size">${displayAttribute.key}: ${displayAttribute.value}</div>
                        </c:forEach>
                        <form id="genericitemform-${genericItem.genericItemId}">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <input type="hidden" name="itemId" value="${genericItem.genericItemId}"/>
                            <input type="hidden" name="generic" value="true"/>
                        </form>
                        <button class="btn btn--gray remove" data="generic" id="${genericItem.genericItemId}"><span>X Remove</span>
                        </button>
                    </div>
                    <div class="col-xs-2 quant">1</div>
                    <div class="col-xs-3 price" id="genericitemprice-${genericItem.genericItemId}" style="text-align: right;">$${genericItem.price}</div>
                </div>

                <div id="formItem-${genericItem.genericItemId}" class="row cartitembottom">
                    <div class="col-sm-8 col-xs-12">
                    </div>
                </div>
                </c:forEach>
            </c:if>


            <div class="row rowmarg">
                <div class="col-md-8 bottomarg">
                    <div id="block1"><input type="checkbox" id="cbxGiftOrderShow"><label for="cbxGiftOrderShow"> This
                        order is a gift</label></div>
                    <div id="block2">
                        <input type="checkbox" id="cbxGiftWrap"><label for="cbxGiftWrap"> Gift wrap this order -
                        $4.99</label><br>
                        <input type="checkbox" id="cbxPersonalizedMessage"><label for="cbxPersonalizedMessage"> Include
                        a personalized gift message.</label><br>

                        <div id="block3">
                            <textarea placeholder="Leave blank for a blank card" type="textarea" row="3"
                                      id="txtAreaPersonalMessage"></textarea>

                            <div class="char"><span class="charcount">240</span> Characters left</div>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 rii topmarg chec">
                    <div class="holder">
                        <div class="lii">Subtotal:</div>
                        <div id="bottomSubtotal" class="rii">$ ${cart.subTotal}</div>
                    </div>
                    <div class="holder giftwrap">
                        <div class="lii">Gift Wrap:</div>
                        <div class="rii">$ 4.99</div>
                    </div>
                    <div class="holder">
                        <div class="lii">Shipping:</div>
                        <div id="landing-bag-shipping" class="rii">$ ${cart.shipping}</div>
                    </div>
                    <div class="holder promowrap">
                        <div class="lii">Promo:</div>
                        <div class="rii">-$ 15.00</div>
                    </div>
                    <div style="display: none;" class="holder creditwrap">
                        <div class="lii">Store Credit:</div>
                        <div id="store-credit-mainbag" class="rii">- $ -</div>
                    </div>
                    <br>
                    <button id="chxoutbottom" class="btn btn--red checkout"><span style="color: #fff">Checkout $ </span><span
                            id="finalprice" style="color: #fff">${cart.grandTotal}</span></button>
                        <%-- <a id="proo" class="btn btn--lightgray promod" role="button" data-toggle="collapse" href="#collapseExample2" aria-expanded="false" aria-controls="collapseExample">Enter Promo Code or Gift Card</a>
                          <div class="collapse" id="collapseExample2">
                            <div class="well">
                              <form class="promo-form form-horizontal" onkeypress="return event.keyCode != 13;" enctype="multipart/form-data">
                                <div class="form names">
                                  <fieldset>
                                    <div class="row">
                                      <div class="col-md-12">
                                        <div id="error"></div>
                                        <div class="form-group">
                                          <div class="col-md-12 col has-feedback">
                                            <span class="labyo">Promo Code</span>
                                            <input type="hidden" name="_csrf" value="15b84e6a-06a1-431e-91ae-ec5d90880ec3">
                                            <input type="text" name="promoCode" id="promoCode" class="form-control" placeholder="Promo Code">
                                            <span class="glyphicon form-control-feedback" id="promoCode1"></span>
                                          </div>
                                        </div>
                                      </div>
                                    </div>
                                  </fieldset>
                                </div>
                              </form>
                              <button class="btn btn--lightgray" id="promo-button"><span>Submit Promo Code</span></button>
                            </div>
                          </div> --%>
                </div>
            </div>


        </c:if>
    </div>
</div>

<div class="cart-content">


  <div class="cart-nav">
    <div class="logo-holder">
      <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/logo-lt-gray.svg">
    </div>
    <div class="steps">
      <div id="step-one" class="step active">1: SHIPPING</div>
      <div id="step-two" class="step">2: PAYMENT</div>
      <div id="step-three" class="step">3: REVIEW</div>
    </div>
  </div>


  <div class="container" id="cart-container">
    <div class="col-md-6 col-md-offset-1 col-sm-7 cardy checkout">
        <div role="tabpanel">
            <div class="tab-content">

                <!-- SHIPPING INFORMATION -->
                <div role="tabpanel" class="tab-pane active" id="shipping">
                    <div class="row account-settings-header checkout"><a class="topper"><span style="color: #2e2626">Shipping to</span></a>
                    </div>
                    <form class="shipping form-horizontal" action='/' method='post'>
                        <div class="form names">
                            <fieldset>
                                <div class="row  form-input">
                                    <div class="col-md-12">
                                        <span class="spinner shipping"></span>

                                        <div class="shippingstuff">
                                            <div id="shippingerror"></div>
                                            <div class="form-group">
                                                <div class="col-xs-6 colpadder col has-feedback">
                                                    <span class="labyo">First Name</span>
                                                    <input type="text" id="shippingFirstName" name="shippingFirstName"
                                                           class="form-control checkout-validate" placeholder="First"/>
                                                    <span class="glyphicon form-control-feedback"
                                                          id="shippingFirstName1"></span>
                                                </div>
                                                <div class="col-xs-6 colpadder col has-feedback">
                                                    <span class="labyo">Last Name</span>
                                                    <input type="text" name="shippingLastName" id="shippingLastName"
                                                           class="form-control checkout-validate" placeholder="Last"/>
                                                    <span class="glyphicon form-control-feedback"
                                                          id="shippingLastName1"></span>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <div class="col-md-12 col has-feedback">
                                                    <span class="labyo">Email</span>
                                                    <input type="email" name="shippingEmail" id="shippingEmail"
                                                           class="form-control checkout-validate" placeholder="Email"/>
                                                    <span class="glyphicon form-control-feedback"
                                                          id="shippingEmail1"></span>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <div class="col-md-12 col has-feedback">
                                                    <span class="labyo">Phone</span>
                                                    <input type="tel" name="shippingPhone" id="shippingPhone"
                                                           class="form-control checkout-validate" placeholder="Phone"/>
                                                    <span class="glyphicon form-control-feedback"
                                                          id="shippingPhone1"></span>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <div class="col-md-12 col has-feedback">
                                                    <span class="labyo">Address</span>
                                                    <input type="text" name="shippingAddressLine1"
                                                           id="shippingAddressLine1"
                                                           class="form-control checkout-validate"
                                                           placeholder="Address"/>
                                                    <span class="glyphicon form-control-feedback"
                                                          id="shippingAddressLine11"></span>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <div class="col-md-12 col">
                                                    <span class="labyo">Address Line 2</span>
                                                    <input type="text" name="shippingAddressLine2"
                                                           id="shippingAddressLine2"
                                                           class="form-control ignore checkout-validate"
                                                           placeholder="Address"/>
                                                </div>
                                            </div>
                                            <c:if test="${not empty country}">
                                                <input id="shippingCountryId" name="shippingCountryId" type="hidden"
                                                       class="form-control ignore" value="1"/>
                                            </c:if>
                                            <div class="form-group">
                                                <div class="col-sm-5 col has-feedback">
                                                    <span class="labyo">City</span>
                                                    <input type="text" name="shippingCity" id="shippingCity"
                                                           class="form-control checkout-validate" placeholder="City"/>
                                                    <span class="glyphicon form-control-feedback"
                                                          id="shippingCity1"></span>
                                                </div>
                                                <div id="special-state" class="col-xs-6 col-sm-3 colpadder col has-feedback">
                                                    <span class="labyo">State</span>
                                                    <span class="css-select-moz">
                                                    <select id="shippingStateId" name="shippingStateId"
                                                            class="form-control checkout-validate">
                                                        <optgroup id="" label="State">
                                                        <c:if test="${not empty country}">
                                                            <option class="empty-select-option" value="" selected="true"></option>
                                                            <c:forEach var="state" items="${states}">
                                                                <option value="${state.primaryKeyId}">${state.code}</option>
                                                            </c:forEach>
                                                        </c:if>
                                                        </optgroup>
                                                    </select>
                                                    </span>
                                                    <span class="glyphicon form-control-feedback select"
                                                          id="shippingStateId1"></span>
                                                </div>
                                                <div class="col-xs-6 col-sm-4 colpadder col has-feedback">
                                                    <span class="labyo">Zip</span>
                                                    <input type="number" name="shippingZip" id="shippingZip"
                                                           class="form-control checkout-validate" placeholder="Zip"/>
                                                    <span class="glyphicon form-control-feedback"
                                                          id="shippingZip1"></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </fieldset>
                        </div>
                    </form>
                    <!-- hidden form elements -->
                    <form action="" id="first_form" class="form-horizontal">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input id="sFirstName" name="shippingFirstName" type="hidden" placeholder="First Name"
                               class="form-control"/>
                        <input id="sLastName" name="shippingLastName" type="hidden" placeholder="Last Name"
                               class="form-control"/>
                        <input id="sAddress1" name="shippingAddressLine1" type="hidden" placeholder="Address"
                               class="form-control"/>
                        <input id="sAddress2" name="shippingAddressLine2" type="hidden" placeholder="Address"
                               class="form-control"/>
                        <input id="sCity" name="shippingCity" type="hidden" placeholder="City" class="form-control"/>
                        <input id="sState" name="shippingStateId" type="hidden" placeholder="State"
                               class="form-control"/>
                        <input id="sCountry" name="shippingCountryId" type="hidden" placeholder="Country"
                               class="form-control" value="1"/>
                        <input id="sZip" name="shippingZip" type="hidden" placeholder="Zip" class="form-control"/>
                        <input id="sPhone" name="shippingPhone" type="hidden" placeholder="phone" class="form-control"/>
                        <input id="bFirstName" name="billingFirstName" type="hidden" placeholder="First Name"
                               class="form-control"/>
                        <input id="bLastName" name="billingLastName" type="hidden" placeholder="Last Name"
                               class="form-control"/>
                        <input id="bAddress1" name="billingAddressLine1" type="hidden" placeholder="Address"
                               class="form-control"/>
                        <input id="cartEmail" name="cartEmail" type="hidden" placeholder="Email" class="form-control"
                               value="${user.email}"/>
                        <input id="bAddress2" name="billingAddressLine2" type="hidden" placeholder="Address"
                               class="form-control"/>
                        <input id="bCity" name="billingCity" type="hidden" placeholder="City" class="form-control"/>
                        <input id="bState" name="billingStateId" type="hidden" placeholder="State"
                               class="form-control"/>
                        <input id="bCountry" name="billingCountryId" type="hidden" placeholder="Country"
                               class="form-control" value="1"/>
                        <input id="bZip" name="billingZip" type="hidden" placeholder="Zip" class="form-control"/>
                        <input id="bPhone" name="billingPhone" type="hidden" placeholder="phone" class="form-control"/>
                        <input id="nonce" name="paymentNonce" type="hidden" placeholder="Nonce" class="form-control"/>
                    </form>
                </div>

                <!-- BILLING INFORMATION -->
                <div role="tabpanel" class="tab-pane" id="billing">
                    <div class="row account-settings-header checkout"><a class="topper"><span style="color: #2e2626">Enter your payment information</span></a></div>
                    <span class="spinner shipping"></span>
                    <div class="shippingstuff">
                        <form class="billing form-horizontal" action='/' method='post'>
                            <div class="form names">
                                <fieldset>
                                    <div class="row form-input">
                                        <div class="col-md-12">
                                            <div class="">
                                                <div id="ccerror"></div>
                                                <div class="form-group">
                                                    <div class="col-md-12 col has-feedback">
                                                        <span class="labyo">Card Number</span>
                                                        <input type="number" name="creditCard" id="creditCard"
                                                               class="form-control checkout-validate"
                                                               placeholder="Credit Card Number"/>
                                                        <span class="glyphicon form-control-feedback select"
                                                              id="creditCard1"></span>
                                                    </div>
                                                </div>
                                                <div class="form-group" id="special-card">
                                                    <div id="special-mm" class="col-xs-4 col-sm-4 colpadder col has-feedback">
                                                        <span class="labyo">EXP MM</span>
                                                        <span class="css-select-moz">
                                                        <select id="expmnth" name="expmnth"
                                                                class="form-control checkout-validate">
                                                            <optgroup id="" label="Month">
                                                              <option class="empty-select-option" value="" selected="true">MM</option>
                                                              <option value="01">01</option>
                                                              <option value="02">02</option>
                                                              <option value="03">03</option>
                                                              <option value="04">04</option>
                                                              <option value="05">05</option>
                                                              <option value="06">06</option>
                                                              <option value="07">07</option>
                                                              <option value="08">08</option>
                                                              <option value="09">09</option>
                                                              <option value="10">10</option>
                                                              <option value="11">11</option>
                                                              <option value="12">12</option>
                                                            </optgroup>
                                                        </select>
                                                        </span>
                                                        <span class="glyphicon form-control-feedback select"
                                                              id="expmnth1"></span>
                                                    </div>
                                                    <input id="expiration" type="hidden"
                                                           placeholder="Expiration (MM/YY)" class="account-text-form"
                                                           value="12/18"/>

                                                    <div id="special-yr" class="col-xs-4 col-sm-4 colpadder col has-feedback">
                                                        <span class="labyo">EXP YR</span>
                                                        <span class="css-select-moz">
                                                        <select id="expyr" name="expyr"
                                                                class="form-control checkout-validate">
                                                            <optgroup id="" label="Year">
                                                              <option class="empty-select-option" value="" selected="true">YYYY</option>
                                                              <c:forEach var="year" items="${ccExpirationList}">
                                                                  <option value="${year - 2000}">${year}</option>
                                                              </c:forEach>
                                                            </optgroup>
                                                        </select>
                                                        </span>
                                                        <span class="glyphicon form-control-feedback select"
                                                              id="expyr1"></span>
                                                    </div>
                                                    <div id="special-cvc" class="col-xs-4 col-sm-4 colpadder col has-feedback">
                                                        <span class="labyo">CVC</span>
                                                        <input type="number" name="cvc" id="cvc"
                                                               class="form-control checkout-validate"
                                                               placeholder="cvc"/>
                                                        <span class="glyphicon form-control-feedback " id="cvc1"></span>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <div class="col-md-12">
                                                        <div class="checkbox">
                                                            <label>
                                                                <input class="ignore" type="checkbox" id="checkAll"
                                                                       checked="checked"
                                                                       onclick="SetBilling(this.checked);"> USE SAME
                                                                ADDRESS FOR BILLING
                                                            </label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </fieldset>
                            </div>
                        </form>
                        <br><!-- <br> -->
                        <!-- hidden until checkmark unchecked -->
                        <div class="tohider">
                            <form class="hbilling form-horizontal" action='/' method='post'>
                                <div class="form names">
                                    <fieldset>
                                        <div class="row form-input">
                                            <div class="col-md-12">
                                                <div class="">
                                                    <div id="error"></div>
                                                    <div class="form-group">
                                                        <div class="col-xs-6 colpadder col has-feedback">
                                                            <span class="labyo">First Name</span>
                                                            <input type="text" id="hFirstName" name="hFirstName"
                                                                   class="form-control checkout-validate"
                                                                   placeholder="First"/>
                                                            <span class="glyphicon form-control-feedback"
                                                                  id="hFirstName1"></span>
                                                        </div>
                                                        <div class="col-xs-6 colpadder col has-feedback">
                                                            <span class="labyo">Last Name</span>
                                                            <input type="text" name="hLastName" id="hLastName"
                                                                   class="form-control checkout-validate"
                                                                   placeholder="Last"/>
                                                            <span class="glyphicon form-control-feedback"
                                                                  id="hLastName1"></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <div class="col-md-12 col has-feedback">
                                                            <span class="labyo">Email</span>
                                                            <input type="email" name="hEmail" id="hEmail"
                                                                   class="form-control checkout-validate"
                                                                   placeholder="Email"/>
                                                            <span class="glyphicon form-control-feedback"
                                                                  id="hEmail1"></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <div class="col-md-12 col has-feedback">
                                                            <span class="labyo">Phone</span>
                                                            <input type="tel" name="hPhone" id="hPhone"
                                                                   class="form-control checkout-validate"
                                                                   placeholder="Phone"/>
                                                            <span class="glyphicon form-control-feedback"
                                                                  id="hPhone1"></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <div class="col-md-12 col has-feedback">
                                                            <span class="labyo">Address</span>
                                                            <input type="text" name="hAddressLine1" id="hAddressLine1"
                                                                   class="form-control checkout-validate"
                                                                   placeholder="Address"/>
                                                            <span class="glyphicon form-control-feedback"
                                                                  id="hAddressLine11"></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <div class="col-md-12 col">
                                                            <span class="labyo">Address Line 2</span>
                                                            <input type="text" name="hAddressLine2" id="hAddressLine2"
                                                                   class="form-control ignore" placeholder="Address"/>
                                                            <!-- <span class="glyphicon form-control-feedback" id="sAddressTwo1"></span> -->
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <div class="col-xs-12 col-sm-5 colpadder col has-feedback">
                                                            <span class="labyo">City</span>
                                                            <input type="text" name="hCity" id="hCity"
                                                                   class="form-control checkout-validate"
                                                                   placeholder="City"/>
                                                            <span class="glyphicon form-control-feedback"
                                                                  id="hCity1"></span>
                                                        </div>
                                                        <div id="special-state-two" class="col-xs-6 col-sm-3 colpadder col has-feedback">
                                                            <span class="labyo">State</span>
                                                            <span class="css-select-moz">
                                                            <select id="hStateId" name="hStateId"
                                                                    class="form-control checkout-validate">
                                                                <optgroup id="" label="State">
                                                                  <c:if test="${not empty country}">
                                                                      <option class="empty-select-option" value="" selected="true"></option>
                                                                      <c:forEach var="state" items="${states}">
                                                                          <option value="${state.primaryKeyId}">${state.code}</option>
                                                                      </c:forEach>
                                                                  </c:if>
                                                                </optgroup>
                                                            </select>
                                                            </span>
                                                            <span class="glyphicon form-control-feedback select"
                                                                  id="hStateId1"></span>
                                                        </div>
                                                        <div class="col-xs-6 col-sm-4 colpadder col has-feedback">
                                                            <span class="labyo">Zip</span>
                                                            <input type="number" name="hZip" id="hZip"
                                                                   class="form-control checkout-validate"
                                                                   placeholder="Zip"/>
                                                            <span class="glyphicon form-control-feedback"
                                                                  id="hZip1"></span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </fieldset>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <!-- REVIEW INFORMATION -->

                <div role="tabpanel" class="tab-pane" id="review">
                    <div class="row account-settings-header checkout"><a class="topper"><span style="color: #2e2626">Review your order</span></a></div>
                    <span class="spinner billing"></span>
                    <div class="billingstuff">
                        <div class="row">
                          <h5>Shipping Address</h5>
                          <h6 id="ship-address-review"></h6>
                          <h5>Billing Address</h5>
                          <h6 id="bill-address-review"></h6>
                        </div>
                        <span id="stuff"></span>
                        <form action="" id="third_form" class="form-horizontal">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        </form>
                    </div>
                    <div class="other-total">
                      <table style="position:relative;margin:0 auto;width:100%;">
                          <tbody><tr style="">
                              <td id="num-item-count" class="checkout-chart-cell">Items</td>
                              <td class="checkout-chart-cell number"><span id="review-step-subtotal">$${cart.subTotal}</span></td>
                          </tr>
                          <tr style="">
                              <td class="checkout-chart-cell">Shipping</td>
                              <td class="checkout-chart-cell number"><span id="review-step-shipping">${cart.shipping}</span></td>
                          </tr>
                          <tr>
                              <td class="checkout-chart-cell">Tax</td>
                              <td class="checkout-chart-cell number"><span id="review-step-tax">-</span></td>
                          </tr>
                          <tr style="display: none;">
                              <td class="checkout-chart-cell">Gift Wrap</td>
                              <td class="checkout-chart-cell number"><span id="review-step-giftwrap">-</span></td>
                          </tr>
                          <c:if test="${not empty user.storeBalance}">
                          <tr>
                              <td class="checkout-chart-cell">Store Credit</td>
                              <td class="checkout-chart-cell number"><span id="review-step-storecredit">- ${user.storeBalance}</span></td>
                          </tr>
                          </c:if>
                      </tbody></table>
                    </div>
                    <div class="other-total">
                      <table style="position:relative;margin:0 auto;width:100%;">
                        <tbody>
                          <tr style="">
                            <td class="checkout-chart-cell"><span style="font-weight:500; font-size:20px;">Total</span></td>
                            <td class="checkout-chart-cell number"><span id="review-step-grandtotal" style="font-weight:500;font-size:20px;">-</span></td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                    <div class="promostuff">
                      <form class="promo-form form-horizontal" onkeypress="return event.keyCode != 13;" enctype="multipart/form-data">
                        <div class="form names">
                          <fieldset>
                            <div class="">
                              <div class="col-md-12">
                                <div id="error"></div>
                                <div class="col has-feedback">
                                  <span class="labyo">Apply a Promo Code</span>
                                  <div class="input-group">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <input type="text" name="promoCode" id="promoCode" class="form-control" placeholder="Promo Code"/>
                                    <div class="input-group-btn">
                                      <button class="btn btn btn--lightgray" id="promo-button" type="button" style="border: 0px;">APPLY</button>
                                    </div>
                                  </div>
                                  <span class="glyphicon form-control-feedback" id="promoCode1"></span>
                                </div>
                              </div>
                            </div>
                          </fieldset>
                        </div>
                        <span id="result" style="display: block;text-align: center;"></span>
                      </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal-footer back-next">
          <button id="checkout1" type="submit" class="btn big-bottom btn--red disabled">CONTINUE TO PAYMENT</button>
          <button id="back0" type="submit" class="btn big-back btn--lightgray">Back</button>
          <button id="checkout2" type="submit" class="btn big-bottom btn--red disabled">REVIEW YOUR ORDER</button>
          <button id="checkout3" type="submit" class="btn big-bottom btn--red">PLACE YOUR ORDER</button>
          <!-- <button id="back1" type="submit" class="btn big-back btn--lightgray">< BACK</button>
          <button id="back2" type="submit" class="btn big-back btn--lightgray">< BACK</button> -->
        </div>
    </div>

    <div class="col-md-4 col-sm-5 cardy checkout" id="summarybox">
      <div class="row sidebox">
        <table style="position:relative;margin:0 auto;width:75%;">
            <tbody><tr style="">
                <td id="num-item-count" class="checkout-chart-cell">Items</td>
                <td class="checkout-chart-cell number"><span id="first-steps-subtotal">${cart.subTotal}</span></td>
            </tr>
            <tr style="">
                <td class="checkout-chart-cell">Shipping</td>
                <td class="checkout-chart-cell number"><span id="first-steps-shipping">${cart.shipping}</span></td>
            </tr>
            <tr>
                <td class="checkout-chart-cell">Tax</td>
                <td class="checkout-chart-cell number"><span id="first-steps-tax">-</span></td>
            </tr>
            <tr style="display: none;">
              <td class="checkout-chart-cell">Gift Wrap</td>
              <td class="checkout-chart-cell number"><span id="first-steps-giftwrap">-</span></td>
            </tr>
            <c:if test="${not empty user.storeBalance}">
            <tr>
                <td class="checkout-chart-cell">Store Credit</td>
                <td class="checkout-chart-cell number"><span id="first-steps-storecredit">- ${user.storeBalance}</span></td>
            </tr>
            </c:if>
        </tbody></table>
      </div>
      <div class="row sidetotal">
        <table style="position:relative;margin:0 auto;width:75%;">
            <tbody>
            <tr style="">
                <td class="checkout-chart-cell"><span style="font-weight:500; font-size:20px;">Total</span></td>
                <td class="checkout-chart-cell number"><span id="right-block-grandtotal" style="font-weight:500;font-size:20px;">-</span>
                </td>
            </tr>
        </tbody></table>
      </div>
    </div>

  </div>
</div>

<%--<c:import url="../fragments/contentJS.jsp"/>
<c:import url="../fragments/commonJSSources.jsp"/>
<script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.11.1/jquery.validate.min.js"></script>--%>
<script type="text/javascript" src="/resources/js/trove/Facebook.js"></script>
<script type="text/javascript" src="/resources/js/trove/GAHelper.js"></script>
<script>
    var gaeHelper = new GAHelper();

    <c:choose>

    <c:when test="${not empty cart.shouldBeGiftWrapped}">
    var giftWrapInCart = ${cart.shouldBeGiftWrapped};

        <c:choose>
            <c:when test="${cart.shouldBeGiftWrapped}">
    var currentGrandtotal = ${cart.grandTotal} - 4.99;
            </c:when>
            <c:otherwise>
    var currentGrandtotal = ${cart.grandTotal};
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
    var giftWrapInCart = false;
    var currentGrandtotal = ${cart.grandTotal};
    </c:otherwise>
    </c:choose>
    <c:if test="${not empty cart.tax}">
    currentGrandtotal -= ${cart.tax};
    </c:if>
    var cbxGiftWrap = $('#cbxGiftWrap');
    var cbxGiftOrderShow = $('#cbxGiftOrderShow');
    var currentSubtotal = ${cart.subTotal};

    <c:choose>
    <c:when  test="${not empty user.storeBalance}">
    var storeBalance = ${user.storeBalance};
    </c:when>
    <c:otherwise>
    var storeBalance = 0.00;
    </c:otherwise>
    </c:choose>

    var currentShipping = ${cart.shipping};
    var appliedStoreBalance = 0.00;
    var currentTax = 0.00;
    var currentGiftWrap = 0.00;

    cbxGiftOrderShow.click(function () {

        var cbxPersonalizedMessage = $('#cbxPersonalizedMessage');

        this.checked ? $('.protocheck').hide(500) : $('.protocheck').show(500);
        this.checked ? $('#block2').show(500) : $('#block2').hide(500);

        //If the Gift Order Show checkbox is being checked, auto check the gift message box and show the
        //textarea accordingly.  This will prevent a hidden box from being shown as "checked" when we send the
        //cart preparation message off to TroveWeb.
        if (this.checked) {
            cbxPersonalizedMessage.prop('checked', true);
            $('#block3').show(500);
        }
        else {
            cbxPersonalizedMessage.prop('checked', false);
            $('#block3').hide(500);
        }

        //Automatically uncheck the giftwrap checkbox if the outer gift order show checkbox is unchecked
        if (!this.checked && cbxGiftWrap.prop('checked')){
            cbxGiftWrap.click();
        }
    });
    $('#cbxPersonalizedMessage').click(function () {
        this.checked ? $('#block3').show(500) : $('#block3').hide(500);
    });
    $('#cbxShowHide3').click(function () {
        this.checked ? $('#block3').hide(500) : $('#block3').show(500);
    });
    cbxGiftWrap.click(function () {
        if (this.checked) {
          currentGiftWrap = 4.99;
        } else {
          currentGiftWrap = 0.00;
        }

      recalculateAndDisplayCurrentBagTotals();
    });

    $("#txtAreaPersonalMessage").keyup(function () {
        el = $(this);
        if (el.val().length >= 240) {
            el.val(el.val().substr(0, 240));
        } else {
            $(".charcount").text(240 - el.val().length);
        }
    });

    var prototypeIds = [];

    function pushOrPopPrototypeId(id) {
        mixpanel.track("bag_prototype");
        var i = prototypeIds.indexOf(id);
        if (i == -1) {
            prototypeIds.push(id);
        } else {
            prototypeIds.splice(i, 1);
        }

        if (prototypeIds.length == 0) {
            $('#block1').show(500);
        } else {
            $('#block1').hide(500);
        }
    }

    // $(".checkout").click(function () {  mixpanel.track("bag_checkout");  });

    $(".remove").click(function () {
        mixpanel.track("bag_remove");
    });

    $(".lrnmore").click(function () {
        mixpanel.track("bag_learnmore");
    });
    // $("#promo-button").click(function () {  mixpanel.track("bag_promo");  });

    $("#chxouttop").click(function () {
        showCheckout();
        mixpanel.track("bag_checkout");

    });
    $("#chxoutbottom").click(function () {
        showCheckout()
        mixpanel.track("bag_checkout");
    });


    function showCheckout() {
        $('.cart-content').css({
            'display': 'block'
        });
        $('.main-content').css({
            'display': 'none'
        });
        $('body').css({
            'padding-top': '0px',
            'background': '#DEDEDE'
        });
    }


    function hideCheckout() {
        $('.main-content').css({
            'display': 'block'
        });
        $('.cart-content').css({
            'display': 'none'
        });
        var newpad = $('.navbar.navbar-trove').height() + 10 + 'px';

        $('body').css({
            'padding-top': newpad,
            'background': '#FFFFFF'
        });
    }


    function land() {
        mixpanel.track("land_bag");
    }

    function shouldGiftWrap()
    {
      return cbxGiftWrap.prop('checked');
    }

    $(document).ready(function () {

        $('#promoCode').bind('keypress keydown keyup', function (e) {
            if (e.keyCode == 13) {
                e.preventDefault();
            }
        });

        $('form.shipping').validate({
            ignore: ".ignore",
            rules: {
                shippingFirstName: {
                    minlength: 1,
                    maxlength: 20,
                    required: true
                },
                shippingLastName: {
                    minlength: 1,
                    maxlength: 20,
                    required: true
                },
                shippingEmail: {
                    required: true
                },
                shippingPhone: {
                    required: true
                },
                shippingAddressLine1: {
                    required: true
                },
                shippingCity: {
                    required: true
                },
                shippingStateId: {
                    required: true
                },
                shippingZip: {
                    digits: true,
                    required: true
                }
            },
            highlight: function (element) {
                var id_attr = "#" + $(element).attr("id") + "1";
                $(element).closest('.col').removeClass('has-success').addClass('has-error');
                $(id_attr).removeClass('glyphicon-ok').addClass('glyphicon-remove');
            },
            unhighlight: function (element) {
                var id_attr = "#" + $(element).attr("id") + "1";
                $(element).closest('.col').removeClass('has-error').addClass('has-success');
                $(id_attr).removeClass('glyphicon-remove').addClass('glyphicon-ok');
            },
            errorElement: 'span',
            errorClass: 'help-block',
            errorPlacement: function (error, element) {
                if (element.length) {
                    error.insertAfter(element);
                } else {
                    error.insertAfter(element);
                }
            }
        });

        $('form.billing').validate({
            ignore: ".ignore",
            rules: {
                creditCard: {
                    digits: true,
                    required: true
                },
                /*expmnth: {
                 required: true
                 },
                 expyr: {
                 required: true
                 },*/
                cvc: {
                    digits: true,
                    required: true
                }
            },
            highlight: function (element) {
                var id_attr = "#" + $(element).attr("id") + "1";
                $(element).closest('.col').removeClass('has-success').addClass('has-error');
                $(id_attr).removeClass('glyphicon-ok').addClass('glyphicon-remove');
            },
            unhighlight: function (element) {
                var id_attr = "#" + $(element).attr("id") + "1";
                $(element).closest('.col').removeClass('has-error').addClass('has-success');
                $(id_attr).removeClass('glyphicon-remove').addClass('glyphicon-ok');
            },
            errorElement: 'span',
            errorClass: 'help-block',
            errorPlacement: function (error, element) {
                if (element.length) {
                    error.insertAfter(element);
                } else {
                    error.insertAfter(element);
                }
            }
        });

        $('form.hbilling').validate({
            ignore: ".ignore",
            rules: {
                hFirstName: {
                    minlength: 1,
                    maxlength: 20,
                    required: true
                },
                hLastName: {
                    minlength: 1,
                    maxlength: 20,
                    required: true
                },
                hEmail: {
                    required: true
                },
                hPhone: {
                    required: true
                },
                hAddressLine1: {
                    required: true
                },
                hCity: {
                    required: true
                },
                hStateId: {
                    required: true
                },
                hZip: {
                    digits: true,
                    required: true
                }
            },
            highlight: function (element) {
                var id_attr = "#" + $(element).attr("id") + "1";
                $(element).closest('.col').removeClass('has-success').addClass('has-error');
                $(id_attr).removeClass('glyphicon-ok').addClass('glyphicon-remove');
            },
            unhighlight: function (element) {
                var id_attr = "#" + $(element).attr("id") + "1";
                $(element).closest('.col').removeClass('has-error').addClass('has-success');
                $(id_attr).removeClass('glyphicon-remove').addClass('glyphicon-ok');
            },
            errorElement: 'span',
            errorClass: 'help-block',
            errorPlacement: function (error, element) {
                if (element.length) {
                    error.insertAfter(element);
                } else {
                    error.insertAfter(element);
                }
            }
        });

        $('#checkAll').change(function () {

        });


        $('.checkout-validate').on('change', function () {
            $(this).valid();
            reVal('#checkout1');
            if ($('input[id="checkAll"]:checked').length > 0) {
                rereVal('#checkout2');
            } else {
                rerereVal('#checkout2');
            }
        });

        $("#shippingZip").on("change keyup paste", function(){
          $('form.shipping').valid();
          reVal('#checkout1');
        });

        $("#cvc").on("change keyup paste", function(){
          $('form.billing').valid();
          if ($('input[id="checkAll"]:checked').length > 0) {
            rereVal('#checkout2');
          } else {
            rerereVal('#checkout2');
          }
        });

        $("#hZip1").on("change keyup paste", function(){
          $('form.billing').valid();
          if ($('input[id="checkAll"]:checked').length > 0) {
            rereVal('#checkout2');
          } else {
            rerereVal('#checkout2');
          }
        });

        

        /*$('select').on('change', function() {
         $(this).valid();
         reVal('#checkout1');
         if ($('input[id="checkAll"]:checked').length > 0) {
         rereVal('#checkout2');
         } else {
         rerereVal('#checkout2');
         }
         });*/








    });
</script>


<script>

    function reVal(button) {
        var hasSuccess = $('.has-success').length;
        if (hasSuccess >= 8) {
            $(button).removeClass('disabled');
        } else {
            $(button).addClass('disabled');
        }
    }
    ;

    function rereVal(button) {
        var hasSuccess = $('.has-success').length;
        if (hasSuccess >= 12) {
            $(button).removeClass('disabled');
        } else {
            $(button).addClass('disabled');
        }
    }
    ;

    function rerereVal(button) {
        var hasSuccess = $('.has-success').length;
        if (hasSuccess >= 18) {
            $(button).removeClass('disabled');
        } else {
            $(button).addClass('disabled');
        }
    }
    ;


    // handle shipping and billing information form stuff
    $('.tohider').hide();
    function SetBilling(checked) {
        if (checked) {
            $('.tohider').fadeOut();
            ShippingToBilling();
        } else {
            $('.tohider').fadeIn();
            BillingDiff();
        }
    }

    function BillingDiff() {
        document.getElementById('hFirstName').value = "";
        document.getElementById('hLastName').value = "";
        document.getElementById('hAddressLine1').value = "";
        document.getElementById('hAddressLine2').value = "";
        document.getElementById('hCity').value = "";
        document.getElementById('hStateId').value = "";
        document.getElementById('hZip').value = "";
        document.getElementById('hPhone').value = "";
        document.getElementById('hEmail').value = "";
    }

    function ShippingToBilling() {
        document.getElementById('sFirstName').value = document.getElementById('shippingFirstName').value;
        document.getElementById('sLastName').value = document.getElementById('shippingLastName').value;
        document.getElementById('sAddress1').value = document.getElementById('shippingAddressLine1').value;
        document.getElementById('sAddress2').value = document.getElementById('shippingAddressLine2').value;
        document.getElementById('sCity').value = document.getElementById('shippingCity').value;
        document.getElementById('sState').value = document.getElementById('shippingStateId').value;
        document.getElementById('sZip').value = document.getElementById('shippingZip').value;
        document.getElementById('sPhone').value = document.getElementById('shippingPhone').value;
        document.getElementById('bFirstName').value = document.getElementById('shippingFirstName').value;
        document.getElementById('bLastName').value = document.getElementById('shippingLastName').value;
        document.getElementById('bAddress1').value = document.getElementById('shippingAddressLine1').value;
        document.getElementById('bAddress2').value = document.getElementById('shippingAddressLine2').value;
        document.getElementById('bCity').value = document.getElementById('shippingCity').value;
        document.getElementById('bState').value = document.getElementById('shippingStateId').value;
        document.getElementById('bZip').value = document.getElementById('shippingZip').value;
        document.getElementById('bPhone').value = document.getElementById('shippingPhone').value;
        document.getElementById('cartEmail').value = document.getElementById('shippingEmail').value;
    }

    function ShippingToHidden() {
        document.getElementById('hFirstName').value = document.getElementById('shippingFirstName').value;
        document.getElementById('hLastName').value = document.getElementById('shippingLastName').value;
        document.getElementById('hAddressLine1').value = document.getElementById('shippingAddressLine1').value;
        document.getElementById('hAddressLine2').value = document.getElementById('shippingAddressLine2').value;
        document.getElementById('hCity').value = document.getElementById('shippingCity').value;
        document.getElementById('hStateId').value = document.getElementById('shippingStateId').value;
        document.getElementById('hZip').value = document.getElementById('shippingZip').value;
        document.getElementById('hPhone').value = document.getElementById('shippingPhone').value;
        document.getElementById('hEmail').value = document.getElementById('shippingEmail').value;
    }

    function BillingFromHidden() {
        document.getElementById('bFirstName').value = document.getElementById('hFirstName').value;
        document.getElementById('bLastName').value = document.getElementById('hLastName').value;
        document.getElementById('bAddress1').value = document.getElementById('hAddressLine1').value;
        document.getElementById('bAddress2').value = document.getElementById('hAddressLine2').value;
        document.getElementById('bCity').value = document.getElementById('hCity').value;
        document.getElementById('bState').value = document.getElementById('hStateId').value;
        document.getElementById('bZip').value = document.getElementById('hZip').value;
        document.getElementById('bPhone').value = document.getElementById('hPhone').value;
    }

    function applyPromoCode() {
        var promoCode = document.getElementById("promoCode").value;

        var data = new FormData();
        data.append("promoCode-0", promoCode);

        mixpanel.track("bag_promo", {
            "code": promoCode
        });

        jQuery.ajax({
            //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
            //url: 'http://localhost:8080/worker/uploadimatmodel',
            url: '/addpromocodes',
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
            success: function (data) {

                var responseSubtotal = parseFloat(data.subTotal);
                if (data.giftwrapIncludedInSubtotal != null && data.giftwrapIncludedInSubtotal == true) {
                  currentSubtotal = responseSubtotal - 4.99;
                } else {
                  currentSubtotal = responseSubtotal;
                }

                currentTax = parseFloat(data.tax);
                currentShipping = parseFloat(data.shipping);

                recalculateAndDisplayCurrentBagTotals();

                $('.spinner.billing').hide();
                $('.billingstuff').fadeIn();
                $('.promostuff').fadeIn();
                if (data.appliedCodes.length > 0) {
                  $("#promo-button").html('APPLIED!');
                  $("#promo-button").css({
                    'background-color': '#36C16E'
                  });
                  $("#promoCode").css({
                    'border': '1px solid #36C16E'
                  });
                  $("#result").css({
                    'color': '#232323'
                  });
                  document.getElementById('result').innerHTML = '';
                }
                // document.getElementById('result').innerHTML = 'Promo Code Successfully Applied!';
                else {
                  $("#promo-button").css({
                    'background-color': '#DD2435'
                  });
                  $("#promoCode").css({
                    'border': '1px solid #DD2435'
                  });
                  $("#promoCode").val("");
                  document.getElementById('result').innerHTML = 'That Promo Code was invalid. Please check to make sure it was entered correctly.';
                  $("#result").css({
                    'color': '#DD2435'
                  });
                }  
            }
        });
    }


    $("#checkout1").click(function (e) {
        e.preventDefault();
        mixpanel.track("checkout_shipping");
        $('#shipping').removeClass('active');
        $('#shippingtab').removeClass('active');
        $('#step-one').removeClass('active');
        $('#billing').addClass('active');
        $('#billingtab').addClass('active');
        $('#step-two').addClass('active');
        $('#step-one').css({
            'pointer-events': 'all',
            'cursor': 'pointer'
        });
        document.getElementById('back0').style.display = "none";
        document.getElementById('checkout1').style.display = "none";
        document.getElementById('checkout2').style.display = "inline-block";
        ShippingToBilling();
    });

    // button click handling
    $("#checkout2").click(function (e) {
        mixpanel.track("checkout_billing");
        e.preventDefault();
        if ($('input[id="checkAll"]:checked').length > 0) {
            ShippingToBilling();
        } else {
            BillingFromHidden();
        }

        $('.spinner.shipping').fadeIn();
        $('.shippingstuff').hide();
        document.getElementById('checkout2').style.display = "none";
        // document.getElementById('back1').style.display = "none";

        // braintree cc details
        var cc = document.getElementById('creditCard').value;
        var expiration = $('#expmnth').val() + '/' + $('#expyr').val();
        var billingFirstName = $('#bFirstName');
        var billingLastName = $('#bLastName');

        if (billingLastName && billingLastName.val())
            var billingName = billingFirstName.val() + ' ' + billingLastName.val();
        else
            billingName = billingFirstName;

        var cvvVal = $('#cvc').val();
        var billingZip = $('#bZip').val();
        var paymentNonce = "";
        var tokenyo = "${braintreeClientToken}";
        var brainTreeclient = new braintree.api.Client({clientToken: tokenyo});
        brainTreeclient.tokenizeCard({
                    number: cc,
                    expirationDate: expiration,
                    cvv: cvvVal,
                    cardholderName: billingName,
                    billingAddress: {
                        postalCode: billingZip
                    }
                },
                function (err, nonce) {

                    //Register the simple items with Facebook's event tracking
                    var fbItems = getFbDataItems();

                    if (fbItems != null && fbItems.itemIdArray != null && fbItems.itemIdArray.length > 0)
                    {
                      eventAddPaymentInfo(fbItems.itemIdArray, fbItems.subtotal);
                    }

                    document.getElementById('nonce').value = nonce;

                    var $form = $('#first_form');
                    var data = $form.serializeArray();
                    //Add cart items that should be prototyped
                    for (var i = 0; i < prototypeIds.length; ++i) {
                        data.push({
                            name: 'prototypeItem-' + i,
                            value: prototypeIds[i]
                        });
                    }

                    var giftWrap = $('#cbxGiftWrap');
                    if (giftWrap && giftWrap.prop('checked')) {
                        addDataToForm('giftWrap', true, data);
                    }
                    else {
                        addDataToForm('giftWrap', false, data);
                    }

                    var giftMessageCard = $('#cbxPersonalizedMessage');
                    if (giftMessageCard && giftMessageCard.prop('checked')) {
                        addDataToForm('giftMessageCard', true, data);
                    }
                    else {
                        addDataToForm('giftMessageCard', false, data);
                    }

                    var txtAreaPersonalMessage = $('#txtAreaPersonalMessage');
                    if (txtAreaPersonalMessage && txtAreaPersonalMessage.val().length > 0) {
                        addDataToForm('giftMessageCardText', txtAreaPersonalMessage.val(), data);
                    }

                    $.ajax({
                        type: 'POST',
                        data: $.param(data),
                        url: '/preparecart',
                        success: function (data) {
                            if (data.success == true) {
                                mixpanel.track("checkout_billing_success");
                                $('#billing').removeClass('active');
                                $('#billingtab').removeClass('active');
                                $('#review').addClass('active');
                                $('#reviewtab').addClass('active');
                                $('#step-two').removeClass('active');
                                $('#step-three').addClass('active');


                                var subtotalResponse = parseFloat(data.subTotal);
                                if (data.giftwrapIncludedInSubtotal != null && data.giftwrapIncludedInSubtotal == true) {
                                  currentSubtotal = subtotalResponse - 4.99;
                                } else {
                                  currentSubtotal = subtotalResponse;
                                }

                                currentTax = parseFloat(data.tax);
                                currentShipping = parseFloat(data.shipping);

                                recalculateAndDisplayCurrentBagTotals();

                                setMainReviewTabData();
                                document.getElementById('checkout3').style.display = "inline-block";
                                $('#step-two').css({
                                    'pointer-events': 'all',
                                    'cursor': 'pointer'
                                });
                                $('.billingstuff').show();
                                $('.promostuff').show();
                            }
                            else {
                                mixpanel.track("checkout_billing_failure");
                                $('.spinner.shipping').hide();
                                $('#billing').removeClass('active');
                                $('#billingtab').removeClass('active');
                                $('#shipping').addClass('active');
                                $('#shippingtab').addClass('active');
                                $('#step-two').removeClass('active');
                                $('#step-one').addClass('active');
                                $('#step-one').css({
                                    'pointer-events': 'none',
                                    'cursor': 'default'
                                });
                                $('#step-two').css({
                                    'pointer-events': 'none',
                                    'cursor': 'default'
                                });
                                // document.getElementById('back1').style.display = "none";
                                document.getElementById('checkout2').style.display = "none";
                                document.getElementById('checkout1').style.display = "inline-block";
                                $('.shippingstuff').show();
                                document.getElementById('shippingerror').innerHTML = data.message;
                                $('#shippingerror').addClass('billingerror');
                            }
                        }
                    });
                });
    });




    $("#checkout3").click(function (e) {
        e.preventDefault();
        mixpanel.track("checkout_submit");
        $('.spinner.billing').fadeIn();
        document.getElementById('result').innerHTML = 'Completing Purchase...';
        $('.billingstuff').hide();
        $('.promostuff').hide();
        // document.getElementById('back2').style.display = "none";
        document.getElementById('checkout3').style.display = "none";

        var $form = $('#third_form');
        var data = $form.serialize();

        $.ajax({
            type: 'POST',
            data: data,
            url: '/completecartorder',
            success: function (data) {
                if (data.success == true) {

                    //Register the simple items with Facebook's event tracking
                    var fbItems = getFbDataItems();

                    if (fbItems != null && fbItems.itemIdArray != null && fbItems.itemIdArray.length > 0)
                    {
                      eventPurchase(fbItems.itemIdArray, fbItems.subtotal);
                      gaeHelper.purchase(fbItems.itemArray, ${cart.cartId});
                    }

                    mixpanel.track("checkout_success");
                    $('.spinner.billing').hide();
                    document.getElementById('result').innerHTML = 'Thanks for your purchase!';
                    // document.location.reload(true);
                    window.location = "/confirm";
                }
                else {
                    mixpanel.track("checkout_faliure");
                    //Return the user to the CC tab
                    //TODO:  Rewrite this so that it's easier to work with.
                    $('#review').removeClass('active');
                    $('#reviewtab').removeClass('active');
                    $('#billing').addClass('active');
                    $('#billingtab').addClass('active');
                    $('.spinner.billing').hide();
                    $('.spinner.shipping').hide();
                    document.getElementById('result').innerHTML = '';
                    $('.shippingstuff').show();
                    // document.getElementById('back1').style.display = "inline-block";
                    document.getElementById('checkout2').style.display = "inline-block";
                    document.getElementById('ccerror').innerHTML = data.message;
                    $('#ccerror').addClass('billingerror');
                }
            },
            statusCode: {
                500: function () {
                    $('.spinner.billing').hide();
                    $('#shipping').removeClass('active');
                    $('#shippingtab').removeClass('active');
                    $('#billing').addClass('active');
                    $('#billingtab').addClass('active');
                    // document.getElementById('back1').style.display = "inline-block";
                    document.getElementById('checkout1').style.display = "none";
                    document.getElementById('checkout2').style.display = "inline-block";
                    document.getElementById('ccerror').innerHTML =
                            "Error -500.  Oops!  We're experiencing some technical difficulties.  " +
                            "Please try submitting again in a few minutes.  Otherwise, contact us at hello@troveup.com.";
                    $('#ccerror').addClass('billingerror');
                }
            }
        });
    });

    $(".remove").click(function (e) {
        e.preventDefault();
        var cartitemidentifier = this.id;

        if ($(this).attr("data") == "generic"){
            var genericItem = true;
        }
        else{
            genericItem = false;
        }

        if (!genericItem){
            var price = $('#cartitemprice-' + cartitemidentifier).html();
            var itemprice = parseFloat(price.substring(1));
            var formstring = '#cartitemform-' + cartitemidentifier;
        }
        else {
            price = $('#genericitemprice-' + cartitemidentifier).html();
            itemprice = parseFloat(price.substring(1));
            formstring = '#genericitemform-' + cartitemidentifier;
        }

        itemprice = currentSubtotal - itemprice;

        setDisplayedSubtotal(itemprice.toFixed(2));

        var $form = $(formstring);
        var data = $form.serialize();
        $.ajax({
            type: 'POST',
            data: data,
            url: '/removeitem',
            success: function (data) {

                if (genericItem) {
                    var removeItem = '#genericitem-' + cartitemidentifier;
                }
                else
                    removeItem = '#cartitem-' + cartitemidentifier;

                var cartSubtotal = parseFloat(data.estimate);
                currentShipping = parseFloat(data.shipping);

              if (data.giftwrapIncludedInEstimate != null && data.giftwrapIncludedInEstimate == true) {
                currentSubtotal = cartSubtotal - 4.99;
              } else {
                currentSubtotal = cartSubtotal;
              }

                recalculateAndDisplayCurrentBagTotals();

                $(removeItem).hide();


                <%-- TODO:  Fix the mixing of jQuery references with JS references --%>
                $('#cartitem-' + cartitemidentifier).hide();
                $('#formItem-' + cartitemidentifier).hide();

                var itemCounter = document.getElementById('bagcounter');

                if (parseInt(itemCounter.innerHTML) > 1) {
                    itemCounter.innerHTML = parseInt(itemCounter.innerHTML) - 1;
                }
                else {
                    $('#bagcounter').removeClass('showitnow');
                    $('#bagcounter').hide();
                }
            }
        });
    });

    $("#promo-button").click(function (e) {
        e.preventDefault();
        $('.spinner.billing').fadeIn();
        $('.billingstuff').hide();
        $('.promostuff').hide();
        document.getElementById('result').innerHTML = 'Applying promo code...';
        applyPromoCode();
        // var $form = $('.promo-form');
        // var data = $form.serialize();
        // $.ajax({
        //   type:'POST',
        //   data:data,
        //   url:'/addpromocodes',
        //   success:function(data) {
        //     document.getElementById('subTotal').innerHTML = (Math.round(parseFloat(data.subTotal) * 100) / 100).toString();
        //     document.getElementById('tax').innerHTML = (Math.round(parseFloat(data.tax) * 100) / 100).toString();
        //     document.getElementById('shippingg').innerHTML = (Math.round(parseFloat(data.shipping) * 100) / 100).toString();
        //     document.getElementById('grandTotal').innerHTML = (Math.round(parseFloat(data.grandTotal) * 100) / 100).toString();
        //     $('.spinner.billing').hide();
        //     $('.billingstuff').fadeIn();
        //     document.getElementById('result').innerHTML = 'Promo Code Successfully Applied!';
        //   }
        // });
    });


    $('.spinner.billing').fadeIn();

    $("#back0").click(function (e) {
        e.preventDefault();
        hideCheckout();
    });

    $("#step-one").click(function (e) {
        e.preventDefault();
        $('#billing').removeClass('active');
        $('#billingtab').removeClass('active');
        $('#review').removeClass('active');
        $('#reviewtab').removeClass('active');
        $('#shipping').addClass('active');
        $('#shippingtab').addClass('active');
        $('#step-two').removeClass('active');
        $('#step-three').removeClass('active');
        $('#step-one').addClass('active');
        $('.spinner.shipping').hide();
        $('.shippingstuff').fadeIn();
        $('#step-one').css({
            'pointer-events': 'none',
            'cursor': 'default'
        });
        $('#step-two').css({
            'pointer-events': 'none',
            'cursor': 'default'
        });
        document.getElementById('back0').style.display = "block";
        document.getElementById('checkout3').style.display = "none";
        document.getElementById('checkout2').style.display = "none";
        document.getElementById('checkout1').style.display = "inline-block";
    });


    $("#step-two").click(function (e) {
        e.preventDefault();
        $('#review').removeClass('active');
        $('#reviewtab').removeClass('active');
        $('#billing').addClass('active');
        $('#billingtab').addClass('active');
        $('.spinner.shipping').hide();
        $('.shippingstuff').fadeIn();
        $('#step-three').removeClass('active');
        $('#step-one').removeClass('active');
        $('#step-two').addClass('active');
        $('#step-two').css({
            'pointer-events': 'none',
            'cursor': 'default'
        });
        document.getElementById('checkout3').style.display = "none";
        document.getElementById('checkout2').style.display = "inline-block";
    });

</script>

<script>
    $(document).ready(function () {
        land();
        $('.spinner.shipping').hide();
        $('.spinner.billing').hide();

        //Total hack.  Gift wrapping is accounted for in the subtotal for the moment, because anonymous cart items haven't
        //been coded yet (this will be done in the near future).  For now, account for gift wrapping by making sure that
        //the subtotal displayed to the user has the gift wrapping properly subtracted in the event that the cart has
        //been submitted and they return to the bag.
        if (giftWrapInCart) {
            cbxGiftOrderShow.click();
            cbxGiftWrap.click();

            currentSubtotal -= currentGiftWrap;
        }

        recalculateAndDisplayCurrentBagTotals();

        var count = parseInt($('.tallyho-cart').html());
        if (count >= 1) {
            $('.tallyho-cart').addClass('showitnow');
        }

        $('.spinner').hide();
        $('.spinner.addtocartfromcard').hide();

        $('#filters').on('click', 'a', function (event) {
            $('a').removeClass('active');
            $(this).addClass('active');
        });

        $('li').on('click', 'a', function (event) {
            $('a').removeClass('active');
            $(this).addClass('active');
        });

        $('.jam').on('mouseover', function () {
            $('.dropdown-container.jam').addClass('showtime');
        });

        $('.jam').on('mouseleave', function () {
            $('.dropdown-container.jam').removeClass('showtime');
        });

        $('.pro').on('mouseover', function () {
            $('.dropdown-container.pro').addClass('showtime');
        });

        $('.pro').on('mouseleave', function () {
            $('.dropdown-container.pro').removeClass('showtime');
        });

        $('.modal').on('shown.bs.modal', function () {
            $(this).find('[autofocus]').focus();
        });

        $('.cardbuybutton').on('click', function (event) {
            var arr = $(this).attr('id').split("-");
            document.getElementById("itemId").value = arr[1];
            updateSize(arr[1]);
            updateMaterial(arr[1]);
        });

          //Register the simple items with Facebook's event tracking
          var fbItems = getFbDataItems();

          if (fbItems != null && fbItems.itemIdArray != null && fbItems.itemIdArray.length > 0)
          {
            eventInitiateCheckout(fbItems.itemIdArray, fbItems.subtotal);
            gaeHelper.landCheckout(fbItems.itemArray, ${cart.cartId});
          }

    });

    function getFbDataItems() {
      var rval = {};
      rval.itemArray = [];
      rval.itemIdArray = [];
      rval.subtotal = 0.00;
      var simpleItemElements = $('[data-fbitemid]');
      if (simpleItemElements.size() > 0) {
        $.each(simpleItemElements, function () {
          var itemElement = $(this);
          var itemObject = {};
          itemObject.itemId = itemElement.attr('data-fbitemid');
          itemObject.itemName = itemElement.attr('data-fbitemname');
          itemObject.itemPrice = itemElement.attr('data-fbitemprice');
          rval.itemArray.push(itemObject);
          rval.itemIdArray.push(itemElement.attr('data-fbitemid'));
          rval.subtotal += parseFloat(itemElement.attr('data-fbitemprice'));

        });
      }

        return rval;
    }

    function addDataToForm(name, value, formvar) {
        formvar.push({
            name: name,
            value: value
        });
    }

    /**
     * Updates the displayed subtotals across the bag view.  Takes a String and parses it to a Float to keep track of
     * the running total for on-page manipulation.  Note:  Does NOT update the grand total.
     *
     * @param subtotal String param that all subtotals should be updated to.
     */
    function setDisplayedSubtotal(subtotal) {
        currentSubtotal = parseFloat(subtotal);

        //Subtotal at the top of the bag
        var newsubstring = 'Subtotal: $' + subtotal;
        $('.subtotal').html(newsubstring);

        //Subtotal at the bottom of the bag
        $('#bottomSubtotal').html('$ ' + subtotal);

      //Subtotal in the right block of the review process
      $('#review-step-subtotal').html(subtotal);
      $('#first-steps-subtotal').html(subtotal);

    }

    function setDisplayedStoreCredit(storeCredit) {
      var storeCreditText = storeCredit;
      $('#store-credit-mainbag').parent().show();
      $('#store-credit-mainbag').html('- $ ' + storeCreditText).show();
      $('#review-step-storecredit').html('- ' + storeCreditText).parent().parent().show();
      $('#first-steps-storecredit').html('- ' + storeCreditText).parent().parent().show();
    }

    function setDisplayedTax(tax) {

      //Tax in the right block of the review process
      $('#first-steps-tax').html(tax);
      $('#review-step-tax').html(tax);
    }

    function setDisplayedShipping(shipping) {
      $('#landing-bag-shipping').html('$ ' + shipping);
      $('#first-steps-shipping').html(shipping);
      $('#review-step-shipping').html(shipping);
    }

    function setDisplayedGiftwrap(giftWrap) {
      $('.giftwrap').show();
      $('#block2').show();
      var giftwrapDisplayElement = $('#review-step-giftwrap');
      giftwrapDisplayElement.html(giftWrap);
      $(giftwrapDisplayElement.parent().parent()[0]).show();

      giftwrapDisplayElement = $('#first-steps-giftwrap');
      giftwrapDisplayElement.html(giftWrap);
      $(giftwrapDisplayElement.parent().parent()[0]).show();
    }

    function clearDisplayedGiftWrap() {
      $('.giftwrap').hide();
      var giftwrapDisplayElement = $('#review-step-giftwrap');
      $(giftwrapDisplayElement.parent().parent()[0]).hide();
      giftwrapDisplayElement = $('#first-steps-giftwrap');
      $(giftwrapDisplayElement.parent().parent()[0]).hide();
    }

    function setMainReviewTabData() {

      if ($('input[id="checkAll"]:checked').length > 0) {
        var newsubstring1 = document.getElementById('sFirstName').value + " " + document.getElementById('sLastName').value + "<br>" + document.getElementById('sAddress1').value + " " + document.getElementById('sAddress2').value + "<br>" + document.getElementById('sCity').value + " " + $( "#shippingStateId option:selected" ).text() + " " + document.getElementById('sZip').value;
        var newsubstring2 = document.getElementById('bFirstName').value + " " + document.getElementById('bLastName').value + "<br>" + document.getElementById('bAddress1').value + " " + document.getElementById('bAddress2').value + "<br>" + document.getElementById('bCity').value + " " + $( "#shippingStateId option:selected" ).text() + " " + document.getElementById('bZip').value;
      } else {
        var newsubstring1 = document.getElementById('sFirstName').value + " " + document.getElementById('sLastName').value + "<br>" + document.getElementById('sAddress1').value + " " + document.getElementById('sAddress2').value + "<br>" + document.getElementById('sCity').value + " " + $( "#shippingStateId option:selected" ).text() + " " + document.getElementById('sZip').value;
        var newsubstring2 = document.getElementById('bFirstName').value + " " + document.getElementById('bLastName').value + "<br>" + document.getElementById('bAddress1').value + " " + document.getElementById('bAddress2').value + "<br>" + document.getElementById('bCity').value + " " + $( "#special-state-two option:selected" ).text() + " " + document.getElementById('bZip').value;
      }


        
        $('#ship-address-review').html(newsubstring1);
        $('#bill-address-review').html(newsubstring2);
    }


    /**
     * Updates the displayed grand total across the bag view.  Takes a String and parses it to a Float to keep track
     * of the running total for on-page manipulation.  Note:  Does NOT do any other calculation.  Just simply sets the
     * value in a dumb way.
     *
     * @param grandtotal String param that grand total should be updated to.
     */
    function setDisplayedGrandtotal(grandtotal) {
        $('#finalprice').html(grandtotal);
        $('#right-block-grandtotal').html(grandtotal);
        $('#review-step-grandtotal').html(grandtotal);
    }

    function applyStoreBalance(amountToApplyStoreBalanceTo)
    {
        var rval = amountToApplyStoreBalanceTo;
        if (storeBalance != null)
        {
            var storeBalanceFloat = parseFloat(storeBalance);

            if (rval > storeBalanceFloat) {
                rval -= storeBalanceFloat;
                appliedStoreBalance = storeBalanceFloat;
            } else {
                rval = 0.00;
                appliedStoreBalance = amountToApplyStoreBalanceTo;
            }

            setDisplayedStoreCredit(appliedStoreBalance.toFixed(2));
        }

        return rval;
    }

    function clearDisplayedStoreCredit() {
      $('#store-credit-mainbag').parent().hide();
      $('#store-credit-mainbag').html('-').hide();
      $('#review-step-storecredit').html('-').parent().parent().hide();
      $('#first-steps-storecredit').html('-').parent().parent().hide();
    }

    function recalculateAndDisplayCurrentBagTotals()
    {

      var continueShowingGiftWrapLineItem = false;

      //Account for the edge case where a promo code was applied to the subtotal that brought it lower than 4.99
      //while still carrying a gift card.  Set the giftwrap to zero, because that's what happens on the back end, as
      //the subtotal isn't allowed to drop below 0.
      if (currentSubtotal <= 0 && (currentGiftWrap > 0 || shouldGiftWrap())) {
        currentGiftWrap = 0;
        currentSubtotal = 0;
        continueShowingGiftWrapLineItem = true;
      }

      var tempGrandtotal = currentSubtotal + currentShipping + currentTax + currentGiftWrap;

      if (storeBalance >= tempGrandtotal)
      {
        appliedStoreBalance = tempGrandtotal;
        tempGrandtotal = 0;
      } else {
        appliedStoreBalance = storeBalance;
        tempGrandtotal -= appliedStoreBalance;
      }

      currentGrandtotal = tempGrandtotal;

      //Set the displays
      if (currentGiftWrap > 0 || continueShowingGiftWrapLineItem) {
        setDisplayedGiftwrap(currentGiftWrap.toFixed(2));
      } else {
        clearDisplayedGiftWrap();
      }

      if (currentGrandtotal > 0) {
        setDisplayedGrandtotal(currentGrandtotal.toFixed(2));
      } else {
        setDisplayedGrandtotal("0.00");
      }

      if (currentSubtotal > 0) {
        setDisplayedSubtotal(currentSubtotal.toFixed(2));
      } else {
        setDisplayedSubtotal("0.00");
      }

      if (appliedStoreBalance > 0) {
        setDisplayedStoreCredit(appliedStoreBalance.toFixed(2));
      } else {
        clearDisplayedStoreCredit();
      }

      if (currentShipping > 0) {
        setDisplayedShipping(currentShipping.toFixed(2));
      } else {
        setDisplayedShipping("0.00");
      }

      if (currentTax > 0) {
        setDisplayedTax(currentTax.toFixed(2));
      } else {
        setDisplayedTax("-");
      }

    }
</script>

</body>
</html>