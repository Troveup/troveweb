<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Trove: Invite Friends</title>
  <c:import url="../fragments/headers/commonHead.jsp"/>
  <c:import url="../fragments/headers/legalHead.jsp"/>
  <c:import url="../fragments/analytics/all.jsp"/>
  <style>

    .thumbnail>img {
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

    @media(max-width: 320px)
    {
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

    /*.cards_product_wrapper2{ margin-top:50px}*/

    .bigbreaker {
      min-height: 2px;
      width: 100%;
      border-bottom: 2px solid #DEDEDE;
      margin: 30px 0px 40px 0px;
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

    #myRow {
      margin-bottom: 20px;
      overflow-y: hidden;
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
      margin-top: 75px;
  }*/

   .special-container p {
     margin-bottom: 5px;
    }

  </style>

  

  
</head>

<body>
  <c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
  <c:import url="../fragments/nav/topNavBar.jsp"/>
    <div class="container cards_product_wrapper2">
      <div class="row">
        <div class="col-sm-12">
          <div style="text-align: center;"> 
            <!-- <h3>TRY-ON MODELS</h3> -->
            <div class="breaker"><span></span></div>
            <h1 style="font-family:'vollkorn';">Invite friends. Earn jewelry.</h1>
            <h5 style="padding-top:20px;">Tell friends about Trove to earn free customizable jewelry!</h5>
            <br></br>
          </div>
        </div>
      </div>

      <%-- ONLY SHOWN TO AUTHENTICATED USER --%>
      <c:if test="${isAuthenticated}">
        <div id="myRow" class="row">
          <input type="text" class="refarea" id="selectable" value="${refLink}">
          <%--<button id="copyme" onclick="copyToClipboard('#selectable')">Copy</button>--%>
        </div>
        
        <div id="shareRow" class="row">
          <span style="font-size: 110%;font-weight: 500; line-height: 30px; vertical-align: top; margin-right: 5px;">Share: </span>
          <div class="social-icon"><a id="twshare" href="javascript:window.open('https://twitter.com/home?status=Sign%20up%20for%20Trove%20and%20customize%20jewelry%203D-printed%20in%20precious%20metals!%20${refLink}', 'Share on Twitter', 'width=550,height=300');"><img src="https://storage.googleapis.com/troveup-imagestore/assets/img/twitter-badge-2.png" alt="Share on Twitter" class="inline-icon"></a></div>
          <div class="social-icon"><a id="fbshare" href="javascript:window.open('http://www.facebook.com/sharer/sharer.php?u=${refLink}', 'Share on Facebook', 'width=550,height=300');"><img src="https://storage.googleapis.com/troveup-imagestore/assets/img/facebook-badge-2.png" alt="Share on Facebook" class="inline-icon"></a></div>
        </div>

        <div class="bigbreaker"></div>
        <h6 id="numMessage"><span id="numInvites">${numInvites} </span> friends have signed up. <br> Only <span id="numLeft"><%--${numLeft}--%> </span> more until you receive <span id="beforeWhat"><%--${beforeWhat}--%> </span></h6>
        <div class="row invitestate">
          <div class="progress">
            <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>
          </div>
          <div class="col-xs-3">
            <div class="caption">
              <h3 class="first">0</h3>
              <p class="hidmob"><span style="text-decoration: underline;font-weight: 600;line-height: 1.25em;font-size: 110%;">0 Referrals</span><br><span style="font-style:italic;">Start inviting friends!</span></p>
            </div>
          </div>
          <div class="col-xs-3">
            <div class="caption">
              <h3 class="second">5</h3>
              <p class="hidmob"><span style="text-decoration: underline;font-weight: 600;line-height: 1.25em;font-size: 110%;">5 Referrals</span><br><span style="font-style:italic;">One customized try-on ring in 3D-printed nylon</span></p>
            </div>
          </div>
          <div class="col-xs-3">
            <div class="caption">
              <h3 class="third">10</h3>
              <p class="hidmob"><span style="text-decoration: underline;font-weight: 600;line-height: 1.25em;font-size: 110%;">10 Referrals</span><br><span style="font-style:italic;">Three customized try-on rings in 3D-printed nylon</span></p>
            </div>
          </div>
          <div class="col-xs-3">
            <div class="caption">
              <h3 class="final">25</h3>
              <p class="hidmob"><span style="text-decoration: underline;font-weight: 600;line-height: 1.25em;font-size: 110%;">25 Referrals</span><br><span style="font-style:italic;">One customized ring in polished bronze</span></p>
            </div>
          </div>
        </div>
        <div class="row showmob">
          <div class="col-xs-12">
            <div class="caption">
              <p class="marge"><span style="text-decoration: underline;font-weight: 600;line-height: 1.25em;font-size: 110%;">5 Referrals</span><br><span style="font-style:italic;">One customized try-on ring in 3D-printed nylon</span></p>
            </div>
          </div>
          <div class="col-xs-12">
            <div class="caption">
              <p class="marge"><span style="text-decoration: underline;font-weight: 600;line-height: 1.25em;font-size: 110%;">10 Referrals</span><br><span style="font-style:italic;">Three customized try-on rings in 3D-printed nylon</span></p>
            </div>
          </div>
          <div class="col-xs-12">
            <div class="caption">
              <p class="marge"><span style="text-decoration: underline;font-weight: 600;line-height: 1.25em;font-size: 110%;">25 Referrals</span><br><span style="font-style:italic;">One customized ring in polished bronze</span></p>
            </div>
          </div>
        </div>
      </c:if>
      <%-- END --%>
      <%-- ANONYMOUS USER --%>
      <c:if test="${not isAuthenticated}">
        <button onclick="signUpButtonClick()" class="btn btn_big btn_big_red" style="width:inherit; font-size:16px; max-width: 400px;">Sign Up to Get Your Link</button>
        <div class="bigbreaker"></div>
        <h6>You must first sign up to receive your unique referral link.</h6>
        <div class="row preinvite">
          <div class="progress"></div>
          <div class="col-xs-3">
            <div class="caption">
              <h3>0</h3>
              <p class="hidmob"><span style="text-decoration: underline;font-weight: 600;line-height: 1.25em;font-size: 110%;">0 Referrals</span><br><span style="font-style:italic;">Start inviting friends!</span></p>
            </div>
          </div>
          <div class="col-xs-3">
            <div class="caption">
              <h3>5</h3>
              <p class="hidmob"><span style="text-decoration: underline;font-weight: 600;line-height: 1.25em;font-size: 110%;">5 Referrals</span><br><span style="font-style:italic;">One customized try-on ring in 3D-printed nylon</span></p>
            </div>
          </div>
          <div class="col-xs-3">
            <div class="caption">
              <h3>10</h3>
              <p class="hidmob"><span style="text-decoration: underline;font-weight: 600;line-height: 1.25em;font-size: 110%;">10 Referrals</span><br><span style="font-style:italic;">Three customized try-on rings in 3D-printed nylon</span></p>
            </div>
          </div>
          <div class="col-xs-3">
            <div class="caption">
              <h3>25</h3>
              <p class="hidmob"><span style="text-decoration: underline;font-weight: 600;line-height: 1.25em;font-size: 110%;">25 Referrals</span><br><span style="font-style:italic;">One customized ring in polished bronze</span></p>
            </div>
          </div>
        </div>
        <div class="row showmob">
          <div class="col-xs-12">
            <div class="caption">
              <p class="marge"><span style="text-decoration: underline;font-weight: 600;line-height: 1.25em;font-size: 110%;">5 Referrals</span><br><span style="font-style:italic;">One customized try-on ring in 3D-printed nylon</span></p>
            </div>
          </div>
          <div class="col-xs-12">
            <div class="caption">
              <p class="marge"><span style="text-decoration: underline;font-weight: 600;line-height: 1.25em;font-size: 110%;">10 Referrals</span><br><span style="font-style:italic;">Three customized try-on rings in 3D-printed nylon</span></p>
            </div>
          </div>
          <div class="col-xs-12">
            <div class="caption">
              <p class="marge"><span style="text-decoration: underline;font-weight: 600;line-height: 1.25em;font-size: 110%;">25 Referrals</span><br><span style="font-style:italic;">One customized ring in polished bronze</span></p>
            </div>
          </div>
        </div>
      </c:if>
      <%-- END --%>
    </button>

    <script>
      function copyToClipboard(element) {
        var $temp = $("<input />");
        $("body").append($temp);
        $temp.val($(element).text()).select();
        var result = false;
        try {
          result = document.execCommand("copy");
        } catch (err) {
          console.log("Copy error: " + err);
        }
        $temp.remove();
        $('#selectable').css("background-color", "rgba(245, 104, 102, 0.3)");
        $('#selectable').css("border", "2px solid #F56866");
        $('#copyme').html('Copied');
        $('#copyme').css("border", "2px solid #F56866");
        $('#copyme').css("background-color", "#F56866");
        return result;
      };

      function progressBarSetup(numinvites) {
        var numInv = Number(numinvites);
        var numStr = "0.00%";
        if (numInv == 0) {
          $('h3.first').css("background-color", "#F26868");
          $('h3.second').css("background-color", "#8C8C8C");
          $('h3.third').css("background-color", "#8C8C8C");
          $('h3.final').css("background-color", "#8C8C8C");
          $('.progress-bar').css("width", "0%");
          $('#numInvites').html('0 ');
          $('#numLeft').html('5 ');
          $('#beforeWhat').html('one customized try-on ring.');
        } else if (numInv == 5) {
          $('h3.first').css("background-color", "#F26868");
          $('h3.second').css("background-color", "#F26868");
          $('h3.third').css("background-color", "#8C8C8C");
          $('h3.final').css("background-color", "#8C8C8C");
          $('.progress-bar').css("width", "32.5%");
          $('#numInvites').html('5 ');
          $('#numLeft').html('5 ');
          $('#beforeWhat').html('three customized try-on rings.');
        } else if (numInv == 10) {
          $('h3.first').css("background-color", "#F26868");
          $('h3.second').css("background-color", "#F26868");
          $('h3.third').css("background-color", "#F26868");
          $('h3.final').css("background-color", "#8C8C8C");
          $('.progress-bar').css("width", "65%");
          $('#numInvites').html('10 ');
          $('#numLeft').html('15 ');
          $('#beforeWhat').html('a free customized ring in polished bronze.');
        } else if (numInv >= 25) {
          $('h3.first').css("background-color", "#F26868");
          $('h3.second').css("background-color", "#F26868");
          $('h3.third').css("background-color", "#F26868");
          $('h3.final').css("background-color", "#F26868");
          $('.progress-bar').css("width", "100%");
          $('#numMessage').html("Congrats! Check your e-mail for a special promo code for your free ring.");
          // $('#numMessage').html('Congrats! you invited 25 friends and can redeem your free ring with code:1234532 at checkout!');
        } else if (numInv <= 4) {
          $('h3.first').css("background-color", "#F26868");
          $('h3.second').css("background-color", "#8C8C8C");
          $('h3.third').css("background-color", "#8C8C8C");
          $('h3.final').css("background-color", "#8C8C8C");
          numStr = String( 15.0 + ( 3.0 * (numInv - 1) ) ) + "%";
          $('.progress-bar').css("width", numStr);
          var nInv = String(numInv) + " ";
          $('#numInvites').html(nInv);
          var left = String(5 - numInv) + " ";
          $('#numLeft').html(left);
          $('#beforeWhat').html('one customized try-on ring.');
        } else if (numInv <= 9) {
          $('h3.first').css("background-color", "#F26868");
          $('h3.second').css("background-color", "#F26868");
          $('h3.third').css("background-color", "#8C8C8C");
          $('h3.final').css("background-color", "#8C8C8C");
          numStr = String( 47.5 + ( 2.5 * (numInv - 6) ) ) + "%";
          $('.progress-bar').css("width", numStr);
          var nInv = String(numInv) + " ";
          $('#numInvites').html(nInv);
          var left = String(10 - numInv) + " ";
          $('#numLeft').html(left);
          $('#beforeWhat').html('three customized try-on rings.');
        } else {
          $('h3.first').css("background-color", "#F26868");
          $('h3.second').css("background-color", "#F26868");
          $('h3.third').css("background-color", "#F26868")
          $('h3.final').css("background-color", "#8C8C8C");;
          numStr = String( 65 + ( 1.7 * (numInv - 10) ) ) + "%";
          $('.progress-bar').css("width", numStr);
          var nInv = String(numInv) + " ";
          $('#numInvites').html(nInv);
          var left = String(25 - numInv) + " ";
          $('#numLeft').html(left);
          $('#beforeWhat').html('a free customized ring in polished bronze.');
        } 
      };


      function landed() {
        mixpanel.track("landfreering");
      };

      $(document).ready(function() {
        landed();
        progressBarSetup(${numInvites});
        //$("#selectable").focus(function() { $(this).select(); } );
        $("#selectable").mouseup(function() { $(this).select(); } );
      });



    </script>

    <script>
      function bodyPadding(){
          var navHeight = $('.navbar').height();
          var bannerHeight = $('.special-container').outerHeight();
          var totalHeight = navHeight + bannerHeight;
          $('body').css("padding-top", totalHeight + "px");
        };

        $(document).ready(function() {
          bodyPadding();
          window.addEventListener('resize', bodyPadding);
        });

      function signUpButtonClick() {
          authHelper.setActionToPerformPostAuth(function() {
            window.location.reload(true);
          });
          triggerAuthModal();
      }
    </script>
  </body>
</html>