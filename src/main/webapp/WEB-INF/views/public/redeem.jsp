<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Trove: Gift Cards</title>
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

    input.refareaa {
      margin: 0 5px;
      text-transform: none;
      background-color: #FFFFFF;
      font-size: 16px;
      border: 2px solid #DEDEDE;
      display: inline-block;
      max-width: 320px;
      width: 100%;
      text-align: center;
      white-space: nowrap;
      overflow-x: auto;
      border-top-left-radius: 2px;
      border-bottom-left-radius: 2px;
      height: auto;
      margin-top: 20px;
      margin-bottom: 49px;
    }

    h5 {
      font-size: 17px;
      color: #8e8b8b;
      padding: 20px;
    }

    h3 {
      margin-top: 30px;
    }

    .redeemresponse {
      display: none;
    }

    .orimg {
      position: absolute;
      margin-left: -29px;
      margin-top: -16px;
    }

    .cards_product_wrapper2 {
      margin-top: 0px;
      width: 100%;
      max-width: 1000px;
    }

    .bigbreaker {
      display: none;
    }

    .fineprint {
      font-size: 11px;
      font-style: italic;
      padding-top: 5px;
    }

    span.jslink {
      text-decoration: underline;
      font-size: 11px;
      font-style: italic;
      padding-top: 5px;
    }

    span.jslink:hover {
      cursor: pointer;
    }

    @media (max-width: 768px) {
      .orimg {
        display: none;
      }

      .bigbreaker {
        display: block;
        min-height: 2px;
        width: 100%;
        border-bottom: 2px solid #DEDEDE;
        margin: 30px 0px 30px 0px;
      }

      h3 {
        margin-top: 50px;
      }
    }

    .megladon {
      max-width: 1400px;
    }

    @media screen and (max-width: 450px) {
      .container-fluid.banner {
        background-image: url("https://storage.googleapis.com/troveup-imagestore/assets/img/giftcard-banner-mobile.jpg");
        background-size: 100%;
        background-repeat: no-repeat;
      }

      .megladon {
        opacity: 0.0;
      }
    }

    .special-container p {
      margin-bottom: 5px;
    }


  </style>

  <c:if test="${isAuthenticated}">
    <style>
      .cards_product_wrapper2 {
        margin-top: 30px;
      }
    </style>
  </c:if>

  <c:if test="${not isAuthenticated}">
    <style>
      /* .cards_product_wrapper2 {
           margin-top: 75px;
       }*/
    </style>
  </c:if>

  <style>

    .img-text-overlay {
      position: absolute;
      top: 45%;
      transform: translateY(-50%);
      -moz-transform: translateY(-50%);
      -o-transform: translateY(-50%);
      -ms-transform: translateY(-50%);
      -webkit-transform: translateY(-50%);
    }

    .img-text-overlay.center {
      left: 0;
      right: 0;
    }

    /*body {
      padding-top: 100px;
    }


    @media (min-width: 767px) {
      body {
          padding-top: 144px;
      }
    }

     @media (max-width: 567px) {
      body {
          padding-top: 120px;
      }
    }

    @media (max-width: 419px) {
      body {
          padding-top: 145px;
      }
      .btn.btn_specialsignup {
        margin-top: 5px;
      }
    }*/

    .cards_product_wrapper2 {
      margin-top: 30px;
    }

  </style>


</head>

<body>
<c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
<c:import url="../fragments/nav/topNavBar.jsp"/>
<c:import url="../fragments/banners/giftcardsBanner.jsp"/>
<div class="container cards_product_wrapper2">
  <div class="row" style="min-height: 300px">
    <div class="col-sm-6">
      <div style="text-align: center;">
        <h3>GIVE THE PERFECT GIFT</h3>
        <div class="breaker"><span></span></div>
        <h5 style="padding-top:20px;">You choose the amount.<br> They design exactly what they want.</h5>
        <br>
        <a id="giftcard_order" href="/giftcard/buy" class="btn btn_big btn_big_gray"
           style="width:inherit; font-size:16px;">ORDER GIFT CARD</a>
        </br>
      </div>
    </div>
    <img class="orimg" src="https://storage.googleapis.com/trove-qa-teststore/orthing.jpg">
    <div class="bigbreaker"></div>
    <div class="col-sm-6">
      <div style="text-align: center;">
        <h3>RECEIVED A GIFT CARD?</h3>
        <div class="breaker"><span></span></div>

        <input type="text" class="refareaa" id="giftcardstring" placeholder="Enter your gift code">
        <br>
        <a onclick="redeemGiftCard(); return false" href="#" class="btn btn_big btn_big_gray"
           style="width:inherit; font-size:16px;">REDEEM</a>
        <c:if test="${not isAuthenticated}">
        <div class="fineprint">Note: <span class="jslink" onclick="signUpButtonClick()">Please Log In</span> to save your card balance.</div>
        </c:if>
        <h5 class="redeemresponse" id="redeem1" style="padding-top:20px;"></h5>
        <h5 class="redeemresponse" id="redeem2" style="padding-top:0px;"></h5>
        </br>
      </div>
    </div>
  </div>
  <div class="row">
    <div class="col-sm-12">
      <div class="">
        <div class="" role="tab" id="headingThree">
          <h4 style="text-align: center; text-decoration: underline; margin-top: 60px; ">
            <a style="font-style: italic;" class="collapsed" role="button" data-toggle="collapse"
               data-parent="#accordion" href="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
              Gift card terms and conditions
            </a>
          </h4>
        </div>
        <div id="collapseThree" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingThree"
             aria-expanded="false">
          <div style="text-align: center;" class="panel-body">
            Trove Gift Cards are available to residents of the United States and do not have expiration dates or service
            fees. Upon redemption of Gift Cards at troveup.com, the indicated amount will be added as store credit to
            the user account of the person who redeemed the Gift Card. Trove Gift Cards do not have cash or resale value
            except when required by law. To report a Gift Card as stolen or to request a replacement, email
            hello@troveup.com.
          </div>
        </div>
      </div>
    </div>
  </div>
</div>


<script>
  function land() {
    mixpanel.track("land_giftcard");
  }
  land();

</script>

<script>

  function redeemGiftCard() {

    $('#redeem1').slideUp();
    $('#redeem2').slideUp();
    mixpanel.track("giftcard_redeem");
    var data = new FormData();
    data.append('giftCardString', $('#giftcardstring').val());
    jQuery.ajax({
      url: '/redeemgiftcard',
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
        //Do something with this
        if (data.success) {
          $('#redeem1').text("$ " + data.giftCardAmount + " credit has been added to your account.");
          $('#redeem2').text("You have $ " + data.storeCreditBalance + " credit in your account");
          $('#redeem1').slideDown();
          $('#redeem2').slideDown();
          $('#giftcardstring').val("");
          mixpanel.track("giftcard_redeem_success");
        }
        else
        // $('#redeem1').text("Sorry!  Error message: " + data.redemptionError);
          $('#redeem1').text("Sorry, that code is invalid.");
        $('#redeem1').slideDown();
        $('#giftcardstring').val("");
        mixpanel.track("giftcard_redeem_faliure");
      }
    });
  }


  // mixpanel stuff

  $("#giftcard_order").click(function () {
    mixpanel.track("giftcard_order");
  });

</script>

<script>
  function bodyPadding() {
    var navHeight = $('.navbar').height();
    var bannerHeight = $('.special-container').outerHeight();
    var totalHeight = navHeight + bannerHeight;
    $('body').css("padding-top", totalHeight + "px");
  }
  ;

  $(document).ready(function () {
    bodyPadding();
    window.addEventListener('resize', bodyPadding);
  });

  function signUpButtonClick() {
    authHelper.setActionToPerformPostAuth(function () {
      window.location.reload(true);
    });
    triggerAuthModal();
  }
</script>
</body>
</html>


