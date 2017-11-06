<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Trove: Gift Cards</title>
  <c:import url="../fragments/headers/commonHead.jsp"/>
  <c:import url="../fragments/headers/legalHead.jsp"/>
  <c:import url="../fragments/analytics/all.jsp"/>
  <link rel="stylesheet" href="https://code.jquery.com/ui/1.11.4/themes/ui-lightness/jquery-ui.css">
  <script type="text/javascript" src="https://code.jquery.com/ui/1.9.2/jquery-ui.min.js"></script>
  <style>

    .thumbnail > img {
      width: 130px;
      padding: 10px;
    }

    .thumbnail {
      text-align: center;
      border: 0px solid #FFF;
    }

    .cards_product_wrapper2 {
      text-align: center;
    }

    input.refarea {
      margin: 0 5px;
      text-transform: none;
      background-color: #FFFFFF;
      font-size: 16px;
      border: 2px solid #DEDEDE;
      display: inline-block;
      max-width: 400px;
      width: 100%;
      text-align: center;
      white-space: nowrap;
      overflow-x: auto;
      border-top-left-radius: 2px;
      border-bottom-left-radius: 2px;
      height: auto;

    }

    @media (max-width: 320px) {
      input.refarea {
        max-width: 320px;
      }
    }

    h3 {

      background-color: #8C8C8C;
      width: 50px;
      height: 50px;
      line-height: 46px;
      border: 2px solid #FFF;
      border-radius: 2em;
      color: #FFF;
      margin: 20px auto;
      font-size: 20px;
    }

    h6 {
      font-size: 18px;
      text-align: center;
      margin-bottom: 40px;
      line-height: 2em;
      padding: 0px 20px;
      font-weight: 300;
    }

    h5 {
      padding: 0px 40px;
    }

    h1 {
      font-style: italic;
    }

    @media (min-width: 768px) {
      .nav-justified > li {
        width: auto;
      }
    }

    /*.cards_product_wrapper2{ margin-top:50px}*/

    .bigbreaker {
      min-height: 2px;
      width: 100%;
      border-bottom: 2px solid #DEDEDE;
      margin: 30px 0px 30px 0px;
    }

    #copyme {
      display: inline-block;
      height: 41px;
      font-size: 15px;
      font-weight: bold;
      color: #FFF;
      background-color: rgb(222, 222, 222);
      border: 2px solid #DEDEDE;
      vertical-align: top;
      padding: 8px 20px;
      margin-left: -7px;
      border-top-right-radius: 2px;
      border-bottom-right-radius: 2px;
    }

    #copyme:hover {
      background-color: #8C8C8C;
    }

    #shareRow {
      text-align: center;
      margin-top: 15px;
    }

    #myRow, #myRow1, #myRow2, #myRow3, #myRow4 {
      margin-bottom: 0px;
    }

    .social-icon {
      display: inline-block;
      width: 30px;
      height: 30px;
      overflow: hidden;
      float: none;
      padding-top: 2px;
      opacity: .4;
    }

    .row.invitestate {
      margin-right: 15px;
      margin-left: 15px;
    }

    .row.preinvite {
      margin-right: 15px;
      margin-left: 15px;
    }

    .progress {
      position: relative;
      top: 50px;
      height: 8px;
      overflow: hidden;
      background-color: #DEDEDE;
      border-radius: 4px;
      -webkit-box-shadow: none;
      box-shadow: none;
      width: 80%;
      margin-left: 10%;
      margin-bottom: 0px;
    }

    .progress-bar {
      float: left;
      width: 0;
      height: 100%;
      font-size: 12px;
      line-height: 20px;
      color: #fff;
      text-align: center;
      background-color: #F26868;
      -webkit-box-shadow: none;
      box-shadow: none;
      -webkit-transition: width .6s ease;
      -o-transition: width .6s ease;
      transition: width .6s ease;
    }

    @media (max-width: 768px) {
      .hidmob {
        display: none;
      }
    }

    @media (min-width: 768px) {
      .showmob {
        display: none;
      }
    }

    .marge {
      margin-top: 24px;
      padding: 10px;
    }

    #numInvites, #numLeft {
      color: #DD2435;
      font-weight: 500;
      font-size: 120%;
    }

    #beforeWhat {
      color: #DD2435;
      font-weight: 500;
    }

    #promo {
      color: #DD2435;
      font-weight: 600;
      font-size: 130%;
    }

    /*.cards_product_wrapper2 {
        margin-top: 30px;
    }*/
    .giftcard-container {
      text-align: center;
      width: 100%;
      height: 154px;
      padding: 13px;
    }

    #giftcard_link {

    }

    .giftcard {
      height: 160px;
      max-width: 256px;
      background-color: #b7b7d2;
      border-radius: 8px;
      margin-left: calc(50% - 128px);
      text-align: center;
    }

    .giftcard-logo {
      margin-top: 60px;
      width: 151px;
    }

    .nav-pills > li {
      text-align: center;
      margin: 0px;
      padding: 0px;
    }

    .nav-pills > li > a {
      padding: 3px 25px;
      margin: 2px;
      border: 2px solid #DEDEDE;
    }

    .nav-pills > li.active > a, .nav-pills > li.active > a:focus, .nav-pills > li.active > a:hover {
      color: #2E2626;
      background-color: #FFF;
      margin: 2px;
      border: 2px solid rgba(221, 36, 53, 0.7);
    }

    .nav-pills > li.moneys {
      text-align: center;
      margin: 10px 0px;
      width: 20%;
    }

    .nav-pills > li.moneys > a {
      padding: 3px 0px;
      margin: 2px;
      border: 2px solid #DEDEDE;
    }

    .nav-pills > li.moneys.active > a, .nav-pills > li.moneys.active > a:focus, .nav-pills > li.moneys.active > a:hover {
      color: #2E2626;
      background-color: #FFF;
      margin: 2px;
      border: 2px solid rgba(221, 36, 53, 0.7);
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
    }

    #myRow1, #myRow2, #myRow3, #tofieldEmail {
      display: none;
    }

    .ui-widget-header {
      border: 1px solid #DEDEDE;
      background: #DEDEDE;
      color: #ffffff;
      font-weight: bold;
    }

    .ui-corner-all {
      border-radius: 0px;
    }

    .ui-widget-content {
      border: 1px solid #DEDEDE;
      background: #FFF;
      color: #333333;
      border-radius: 0px;
    }

    .ui-state-default, .ui-widget-content .ui-state-default, .ui-widget-header .ui-state-default {
      border: 1px solid #DEDEDE;
      background: #DEDEDE;
      font-weight: bold;
      color: #8C8C8C;
      text-align: center;
    }

    .ui-state-hover,
    .ui-widget-content .ui-state-hover,
    .ui-widget-header .ui-state-hover,
    .ui-state-focus,
    .ui-widget-content .ui-state-focus,
    .ui-widget-header .ui-state-focus {
      border: 1px solid #999;
      background: #999;
      font-weight: bold;
      color: #FFF;
    }

    .ui-icon {
      width: 16px;
      height: 16px;
    }

    .ui-icon,
    .ui-widget-content .ui-icon {
      background-image: url("https://code.jquery.com/ui/1.11.4/themes/ui-lightness/images/ui-icons_222222_256x240.png");
    }

    .ui-widget-header .ui-icon {
      background-image: url("https://code.jquery.com/ui/1.11.4/themes/ui-lightness/images/ui-icons_ffffff_256x240.png");
    }

    .ui-state-default .ui-icon {
      background-image: url("https://code.jquery.com/ui/1.11.4/themes/ui-lightness/images/ui-icons_ffffff_256x240.png");
    }

    .ui-state-hover .ui-icon,
    .ui-state-focus .ui-icon {
      background-image: url("https://code.jquery.com/ui/1.11.4/themes/ui-lightness/images/ui-icons_ffffff_256x240.png");
    }

    .ui-state-active .ui-icon {
      background-image: url("https://code.jquery.com/ui/1.11.4/themes/ui-lightness/images/ui-icons_ffffff_256x240.png");
    }

    .ui-state-highlight .ui-icon {
      background-image: url("https://code.jquery.com/ui/1.11.4/themes/ui-lightness/images/ui-icons_ffffff_256x240.png");
    }

    .ui-state-error .ui-icon,
    .ui-state-error-text .ui-icon {
      background-image: url("https://code.jquery.com/ui/1.11.4/themes/ui-lightness/images/ui-icons_ffffff_256x240.png");
    }

    h6.senddate {
      display: none;
    }

    #myRow4 {
      display: none;
    }

    input.refareaa {
      margin: 5px 5px;
    }

    .input-group {
      position: relative;
      display: table;
      border-collapse: separate;
      max-width: 300px;
      margin: auto;
    }

    /*.cards_product_wrapper2 {
      margin-top: 20px;
    }*/


  </style>

  <style>

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
    }*/

    @media (max-width: 419px) {
      body {
        padding-top: 145px;
      }

      /*.btn.btn_specialsignup {
        margin-top: 5px;
      }*/
    }

    .cardaddresponse {
      display: none;
    }

    #otherAmt {
      margin: 0px;
      width: calc(100% - 40px);
      margin-left: -4px;
      border: 2px solid #DEDEDE;
      padding: 0px 0px 0px 14px;
      border-radius 5px;
    }

    .megladon {
      max-width: 1400px;
    }

    .oAmt {
      display: none;
      width: 200%;
    }

    .input-group-addon.amt {
      width: 30px;
      display: inline-block;
      border: 2px solid #FFF;
      background: #FFF;
      line-height: 17px;
      font-weight: bold;
    }

    #toEmail-error {
      color: #DD2435;
    }

    .special-container p {
      margin-bottom: 5px;
    }

    .cards_product_wrapper2 {
      margin-top: 30px;
    }


  </style>

</head>

<body>
<c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
<c:import url="../fragments/nav/topNavBar.jsp"/>
<c:import url="../fragments/banners/giftcardsbuyBanner.jsp"/>
<div class="container cards_product_wrapper2">
  <div class="row">
    <div class="col-sm-12">
      <div style="text-align: center;">
        <!-- <h3>TRY-ON MODELS</h3> -->
        <div class="breaker"><span></span></div>
        <h1 style="font-family:'vollkorn';">Trove Gift Cards</h1>
        <h5 style="padding-top:10px;">You choose the amount. They design exactly what they want.</h5>
      </div>
    </div>
  </div>

    <div id="myRow" class="row">
      <h4 style="padding-bottom:10px;">Choose a Card Type</h4>
      <ul id="myTabs" class="nav nav-pills nav-justified">
        <li class=""><a href="#physical" id="physical-tab" role="tab" data-toggle="tab" aria-controls="physical">Physical</a>
        </li>
        <li class=""><a href="#digital" id="digital-tab" role="tab" data-toggle="tab" aria-controls="digital">E-Gift
          Card</a></li>
      </ul>
      <div class="bigbreaker"></div>
    </div>

    <div id="myRow1" class="row">
      <h4 style="padding-bottom:10px;">Choose an Amount</h4>
      <ul id="myAmount" class="nav nav-pills nav-justified">
        <li class="moneys"><a href="#xx" data-price="25" role="tab" data-toggle="tab" id="xx-tab">$ 25</a></li>
        <li class="moneys"><a href="#aa" data-price="50" role="tab" data-toggle="tab" id="aa-tab">$ 50</a></li>
        <li class="moneys"><a href="#bb" data-price="75" role="tab" data-toggle="tab" id="bb-tab">$ 75</a></li>
        <li class="moneys"><a href="#cc" data-price="100" role="tab" data-toggle="tab" id="cc-tab">$ 100</a></li>
        <br>
        <li class="moneys" style="margin-left: -4px"><a href="#dd" data-price="150" role="tab" data-toggle="tab"
                                                        id="dd-tab">$ 150</a></li>
        <li class="moneys"><a data-price="200" href="#aaa" role="tab" data-toggle="tab" id="aaa-tab">$ 200</a></li>
        <li class="moneys"><a href="#bbb" data-price="250" role="tab" data-toggle="tab" id="bbb-tab">$ 250</a></li>
        <li class="moneys"><a href="#ccc" data-price="300" role="tab" data-toggle="tab" id="ccc-tab">$ 300</a></li>
        <br>
        <li class="moneys"><a href="#eee" data-price="other" role="tab" data-toggle="tab" id="eee-tab">Other</a></li>
        <li class="moneys">
          <div class="input-group oAmt" style="margin-top:10px;">
            <span class="input-group-addon amt">$</span>
            <input type="text" class="form-input" id="otherAmt" placeholder="Choose amount">
          </div>
        </li>
      </ul>
      <div class="bigbreaker"></div>
    </div>

    <div id="myRow2" class="row">
      <h4 style="padding-bottom:10px;">Select a Send Date</h4>
      <div class="date-form">
        <div class="form-horizontal">
          <div class="control-group">
            <div class="controls">
              <div class="input-group">
                <input id="date-picker-2" type="text" class="date-picker form-control"/>
                <label for="date-picker-2" class="input-group-addon btn"><span style="color: #fff"
                                                                               class="glyphicon glyphicon-calendar"></span></label>
              </div>
            </div>
          </div>
        </div>
      </div>
      <h6 class="senddate">Your E-Gift Card will be sent on: <span>Choose</span></h6>
      <div class="bigbreaker"></div>
      <br><br>
    </div>

    <div id="myRow3" class="row">
      <h4 style="padding-bottom:10px;">From</h4>
      <input type="text" class="refareaa" id="fromName" placeholder="Your Name">
      <br><br>
      <h4 style="padding-bottom:10px;">To</h4>
      <input type="text" class="refareaa" id="toName" placeholder="Their Name">
      <form class="people" action='/' method='post'>
        <div class="has-feedback">
          <input type="email" name="toEmail" id="toEmail" class="refareaa checkout-validate" placeholder="Their Email"/>
        </div>
      </form>
      <!-- <input type="email" class="refareaa" id="toEmail" placeholder="Their Email"> -->
      <br><br>
      <div class="bigbreaker"></div>
    </div>

    <div id="myRow4">
      <div class="row">
        <h4 style="padding-bottom:10px;">Add a custom message to your gift at checkout</h4>
        <a id="addCardToBag" href="#" class="btn btn_big btn_big_red" onclick="addGiftCard(); return false"
           style="width:inherit; font-size:16px; max-width: 400px; padding-left: 40px;padding-right: 40px;"><img
            src="https://storage.googleapis.com/trove-demo-cdn/img/trove-icon-white.svg" class="icon-btn"
            alt="Add to Bag"> Add to Bag</a>
        <h5 class="cardaddresponse" id="redeem3" style="padding-top:20px;"></h5>
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
                Trove Gift Cards are available to residents of the United States and do not have expiration dates or
                service fees. Upon redemption of Gift Cards at troveup.com, the indicated amount will be added as store
                credit to the user account of the person who redeemed the Gift Card. Trove Gift Cards do not have cash
                or resale value except when required by law. To report a Gift Card as stolen or to request a
                replacement, email hello@troveup.com.
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
</div>

<input type="hidden" id="digital" name="digital" value="">
<input type="hidden" id="amount" name="amount" value="50.00">

<c:choose>
  <c:when test="${isAuthenticated}">
    <script>
      function land() {
        mixpanel.track("land_giftcard_buy");
      }
      land();

    </script>
  </c:when>
  <c:otherwise>
    <script>
      function land() {
        mixpanel.track("land_giftcard_buy_ANON");
      }
      land();
    </script>
  </c:otherwise>
</c:choose>

<script>
  function setAmount(value) {
    $('#amount').val(value);
  }

  function setDigital(yeaorne) {
    $('#digital').val(yeaorne);
  }


  function addGiftCard() {
    $("h5.cardaddresponse").slideUp();
    var data = new FormData();
    data.append('digital', $('#digital').val());
    data.append('amount', $('#amount').val());
    data.append('date', $(".date-picker").val());
    data.append('toName', $('#toName').val());
    data.append('fromName', $('#fromName').val());
    data.append('toEmail', $('#toEmail').val());
    mixpanel.track("giftcard_add", {
      "type": context
    });
    jQuery.ajax({
      url: '/addgiftcard',
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
        // alert("Successful addition!");
        $("h5.cardaddresponse").text("Successfully added!");
        $("h5.cardaddresponse").slideDown();

        //Declared in topNavBar
        navHelper.incrementBagItemCounter();
        window.location = "/private/bag";
      }
    });
  }

  var hasType = false;
  var hasPrice = false;
  var hasDate = false;
  var hasToName = false;
  var hasToEmail = false;
  var context = "";

  $(document).ready(function () {

    $('#myTabs a').click(function (e) {
      e.preventDefault();
      $(this).tab('show');
      hasType = true;
      $('#myRow1').slideDown();
      if ($(this).attr("id") == "digital-tab") {
        context = "digital";
        setDigital("true");
        $('form.people').slideDown();
        if (hasPrice == true) {
          $('#myRow2').slideDown();
          // $('#myRow3').slideUp();
        }
      } else {
        context = "physical";
        setDigital("false");
        $('form.people').slideUp();
        if (hasPrice == true) {
          $('#myRow2').slideUp();
          $('#myRow3').slideDown();
          $('#myRow4').slideDown();
        }
      }

      // mixpanel.track("giftcard_type_click", {
      //   "type": context
      // });
    })

    $(".date-picker").datepicker();
    $(".date-picker").datepicker("setDate", new Date());

    $('#myAmount a').click(function (e) {
      if ($(this).attr("id") == "eee-tab") {
        $('.oAmt').fadeIn();
      } else {
        $('.oAmt').fadeOut();
        var amount = $(this).data('price');
        setAmount(amount);
      }
      e.preventDefault();
      $(this).tab('show');
      hasPrice = true;
      if (context == "digital") {
        $('#myRow2').slideDown();
        // $('#myRow3').slideUp();

      } else {
        $('#myRow2').slideUp();
        $('#myRow3').slideDown();
        $('#myRow4').slideDown();
      }
    })

    $('#otherAmt').on("change", function () {
      var amt = $('#otherAmt').val();
      setAmount(amt);
    });


    $(".date-picker").on("change", function () {
      $('#myRow3').slideDown();
      $('h6.senddate').slideDown();
      var month = $(".ui-datepicker-month").text();
      var year = $(".ui-datepicker-year").text();
      var id = $(this).val();
      $("h6.senddate span").text(id);
    });


    // $("#toName").on("change", function () {
    //   $('#myRow4').slideDown();
    // });

    $("#toName").keydown(function () {
      $('#myRow4').slideDown();
    });


    $('form.people').validate({
      ignore: ".ignore",
      rules: {
        toEmail: {
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

  });

</script>

<script>
  function bodyPadding() {
    var navHeight = $('.navbar').height();
    var bannerHeight = $('.special-container').outerHeight();
    var totalHeight = navHeight + bannerHeight;
    $('body').css("padding-top", totalHeight + "px");
  }


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