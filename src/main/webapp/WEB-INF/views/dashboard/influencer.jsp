<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="trove" uri="http://troveup.com/jsp/tlds/itemimage" %>

<html>
<head>
  <title>Trove: Featured Item</title>
  <c:import url="../fragments/headers/commonHead.jsp"/>
  <c:import url="../fragments/headers/legalHead.jsp"/>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/jquery.slick/1.5.9/slick.css">
  <link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Roboto:300,400,500,700,400italic' type='text/css'>
  <link rel="stylesheet" href="/resources/stylesheets/featured.css">
  <script src="https://d3js.org/d3.v3.min.js"></script>
  <c:import url="../fragments/analytics/all.jsp"/>
  <style>

    body {
      width: 100%;
      overflow-x: hidden; 
    }
    
    .topnav a {
      border-top: 3px solid #ffffff;
    }
    .topnav a.current-page {
      color: #f26868;
      border-top: 3px solid #f26868;
    }
    .profile-container {
      height: 300px;
    }
    .profile-pic-holder {
      left: 50%;
      position: relative;
      height: 100px;
      width: 100px;
      border: 2px solid #fff;
      border-radius: 50%;
      overflow: hidden;
    }

    .username-container {
      margin-top: 50px;
    }
    .card_avatar_small {
      margin-right: 10px;
    }
    .btn--edit--profile {
      margin-top: 20px;
    }
    .follow-pic-holder {
      width: 100px;
      height: 100px;
      border-radius: 50%;
      overflow: hidden;
      margin: 30px 74px;
    }  

    #folwbuttonprofile{
      width: 108px;
      margin: 30px 18px 10px 18px;
      border-radius: 2px;
    }
    #folwbutton {
      width: 108px;
      margin: 10px 18px 10px 18px;
      border-radius: 2px;
    }
    #ufolwbutton {
      width: 108px;
      margin: 10px 18px 10px 18px;
      border-radius: 2px;
    }
    
    .card_darken.person {
      height: 194px;
    }
    .card_username {
      display: block;
      font-size: 16px;
      padding-top: 7px;
      margin-bottom: 3px;
    }
    .card_avatar_small {
      margin-right: 10px;
    }
    .card_username {
      display: block;
      font-size: 16px;
      padding-top: 7px;
      margin-bottom: 3px;
    }
    .overlaid {
      position: relative;
      background-color: rgba(242, 241, 242, 0.8);
      pointer-events: none;
      z-index: 6;
      opacity: 0.0;
      -webkit-transition: all 0.5s ease-in-out;
      -moz-transition: all 0.5s ease-in-out;
      -ms-transition: all 0.5s ease-in-out;
      -o-transition: all 0.5s ease-in-out;
      transition: all 0.5s ease-in-out;
    }
    .overlaid .message {
      margin-top: 0;
      margin-left: 0;
      left: 0;
      position: absolute;
      text-align: center;
      padding: 100px 20px;
      border-radius: 2px;
      color: #797979;
      font-size: 18px;
      width: 250px;
      height: 356px;
      background-color: #DEDEDE;
      &.action {
        z-index: 100;
      }
    }
    .btn--small {
      padding: 5px 12px;
    }
    .overlaid .message img {
      opacity: 0.2;
      width: 44px;
    }
    .overlaid .message h2 {
      color: #797979;
      font-size: 23px;
      margin-bottom: 40px;
      line-height: .7em;
    }
    .hideitnow {
      opacity: 0.2;
    }
    .grid-cta {
      z-index: 4;
    }
    .tallyho-profile, .tallyho-cart {
      opacity: 0.0;
      position: absolute;
      z-index: 9999999;
      right: 6px;
      top: -4px;
      background-color: #f26868;
      color: #FFF;
      padding: 0px 5px 0px 6px;
      height: 18px;
      border-radius: 9px;
      font-size: 75%;
      font-weight: 500;
      border: 1px solid #FFF;
    }
    .tallyho-profile.hideitlater, .tallyho-cart.hideitlater {
      opacity: 1.0;
      display: block !important;
    }
    .tallyho-profile.showitnow, .tallyho-cart.showitnow {
      opacity: 1.0;
      display: block !important;
    }
    .buyModal.in ~ .modal-backdrop.in { 
      background-color: #333;   
      opacity: 0.5;
      filter: alpha(opacity=50); 
    }

    .modal-content {
      border: 1px solid #dedede;
      border-radius: 2px;
    }
    .modal-header {
      box-shadow: none;
      -webkit-box-shadow: none;
      margin: 0 0 30px;
      border-bottom: 1px solid #dedede;
    }

    .modal-body {
      padding-bottom: 30px;
      padding-top: 30px;
      text-align: center;
      border-bottom: 1px solid #dedede;
    }
    .material-selector-container{
      margin:0 10px 20px;
    }

    #ufolwbutton.btn--edit--profile {
      margin-top: 15px !important;
    }
    #folwbutton.btn--edit--profile {
      margin-top: 15px !important;
    }
    .card_user_info {
      padding-left: 10px;
    }

    .btn--edit--profile {
      margin-top: 15px;
      margin-right: 15px;
    }

    @media (min-width: 768px) {
      .btn--edit--profile {
        margin-top: 15px;
        margin-right: 15px;
      }
      #covbutton {
        margin-top: 15px;
      }
      .modal-dialog {
        width: 510px;
        margin: 30px auto;
      }
    }

    @media (max-width: 480px){
      .material-selector-container{
      max-width:90%;
      }

      .user-info-count.category{
        font-size:10px;
      }
    }

    .modal-body.photo {
      padding-bottom: 0px;
    }
    .containers {
        position: relative;
        top: 10%; left: 50%; right: 0; bottom: 0;
        margin-left: -200px;
    }
    .action {
        width: 400px;
        height: 30px;
        margin: 5px 0;
    }
    .cropped>img {
        margin-right: 10px;
        border-radius: 50%;
    }
    form.form-horizontal {
      margin-bottom: 20px;
      margin-top: 30px;
    }
    .form-horizontal .form-group {
      margin-right: -15px;
      margin-left: -15px;          padding: 10px;
    }
    input.form-control {
      border-radius: 0px;
    }
    input:-webkit-autofill {
        -webkit-box-shadow: 0 0 0px 1000px white inset;
    }
    input:focus:-webkit-autofill {
        -webkit-box-shadow: 0 0 0px 1000px white inset;
    }
    #profbutton {
        display: inline-block;
        padding: 6px 15px;
        border-radius: 2px;
        background-color: #8e8b8b;
        color: #FFF;
    }
    #profbutton.hover{
      background-color: #b7b0b0;
    }
    .profpic {
      float: left;
      width: 100px;
      height: 100px;
      border-radius: 50%;
      margin-top: -30px;
      overflow: hidden;
    }
    .profpicholder{
      height: 100px;
      width: 100px;
      position: relative;
      margin: 0 auto;
    }

    .account-settings-header.checkout {
      margin: 0px -15px 40px;
    }
    .profpic img { 
      width: 100px;
      height: 100px;
    }

    .imageBox {
      position: relative;
      height: 340px;
      width: 100%;
      border: 1px solid #aaa;
      background: #fff;
      overflow: hidden;
      background-repeat: no-repeat;
      cursor: move;
    }

    .imageBox .thumbBox {
      position: absolute;
      top: 0;
      left: 0;
      width: 101%;
      height: 309px;
      margin-top: 30px;
      margin-left: -1px;
      margin-right: -1px;
      border: 1px solid rgb(102, 102, 102);
      border-radius: 0px;
      box-shadow: 0 0 0 1000px rgba(0, 0, 0, 0.5);
      background: none repeat scroll 0% 0% transparent;
    }

    .imageBox .spinner {
        position: absolute;
        top: 0;
        left: 0;
        bottom: 0;
        right: 0;
        text-align: center;
        line-height: 400px;
        background: rgba(0,0,0,0.7);
    }

    .containers {
      position: fixed;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      margin-left: 0px;
      height: 200px;
    }

    .action {
      display: block;
      width: 260px;
      height: 30px;
      float: right;
      /*margin: 16px 246px 10px 19px;*/
    }

    .modal-body.photo {
      padding-bottom: 0px;
      min-height: 335px;
    }

    #btnZoomIn, #btnZoomOut {
      float: left;
      background-color: #B7B0B0;
      border: 0px solid transparent;
      border-radius: 2px;
      font-size: 18px;
      padding: 1px 8px;
      font-weight: bold; 
      font-family: helvetica;
      color: #FFF;
      margin-right: 3px;
      margin-top: -3px;
    }

    #btnZoomOut {
      padding: 1px 11px;
    }

    label.myLabel input[type="file"] {
      position: fixed;
      top: -1000px;
    }

    /***** Example custom styling *****/
    .myLabel {
      border: 1px solid #F26868;
      border-radius: 2px;
      padding: 4px 16px;
      margin: -5px 0px 0px -10px;
      background: #F26868;
      display: inline-block;
    }

    .myLabel :valid {
      background-color: #b7b0b0;
    }

    .myLabel + span {
      color: #666;
    }

    .myLabel:hover {
      cursor: pointer;
      background: #DD2435;
    }

    .btn.btn--darkgray {
      background-color: rgba(0, 0, 0, 0.7);
      color: #fff;
    }
    .btn.btn--darkgray:hover {
      background-color: rgba(0, 0, 0, 0.8);
      color: #fff;
    }

    .myLabel:active {
    }
    .myLabel :invalid + span {
      color: #F26868;
    }
    .myLabel :valid + span {
      color: #FFF;
    }

    #covimg {
      width: 100%;
      position: absolute;
      margin-top: 0px;
      left: 0px;
    }

    .profile-container {
      height: 245px;
    }

    .cover-photo-container {
      width: 100%;
      height: 130px;
    }

    @media (min-width: 768px) {
      .modal-dialog.coverphoto {
          width: 100%;
          margin: 50px auto;
      }
    }

    .profile-container {
      -webkit-box-shadow: none;
         -moz-box-shadow: none;
              box-shadow: none;
    }

     @media (max-width: 767px) {
      .cover-photo-container {
        height: 75px;
        margin-top: -10px;
      }
      .profile-container {
        height: 190px;
      }
    }

    .hold {
      padding: 5px;
    }

    .cont {
      border: 2px solid #EAEAEA;
      min-height: 140px;
    }
    .holda {
      padding: 0px 0px 10px 0px;
    }
    .holdb {
      padding: 0px;
    }

    blockquote {
      padding: 18px;
      margin: 0px;
      font-size: 17.5px;
      border-left: 0px;
      /*background: #EAEAEA;*/
      /*background-image: url('https://storage.googleapis.com/trove-qa-teststore/quotes-lighter.svg');*/
      /*background-repeat: no-repeat;*/
      /*background-position: center;*/
      /*background-size: 80px;*/
      font-weight: 400;
      line-height: 1.4em;
      text-align: center;
    }
    .breaker {
      content: "";
      position: absolute;
      height: 3px;
      width: 60px;
      background: #2E2626;
      margin-left: -30px;
      margin-top: 8px;
    }

    #sales-pie-chart-container {
      background-color: #ffffff;
      font: 10px sans-serif;
      height: auto;
      text-shadow: none;
      text-align: center;
    }

    #sales-pie-chart .total{
      font-size: 30px;
      font-weight: bold;
    }
    #sales-pie-chart .units{
      fill: gray;
      font-size: 14px;
    }

    #sales-pie-chart-legend {
      max-width: 350px;
      margin: 20px auto;
    }

    #sales-pie-chart-legend .label {
      display: block;
      width: 100%;
      font-size: 16px;
      text-align: left;
      padding: 5px;
      cursor: pointer;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }

    #pie-chart-legend .label {
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }

    li.label:before {
      content: "";
      width: 20px;
      height: 20px;
      margin-right: 10px;
      margin-left: 10px;
      background: #CCC;
      float: left;
      display: block;
    }

    path.slice {
      transition: fill-opacity .4s ease;
      cursor: pointer;
    }

    .label {
      font-weight: 500;
      color: #555;
    }


    h5 {
      font-size: 13px;
      font-weight: 600;
      text-transform: uppercase;
      color: #2E2626;
      padding: 15px 10px 15px;
      text-align: center;
      line-height: 1.3em;
      -webkit-font-smoothing: antialiased;
    }
    h4 {
      font-size: 32px;
      text-align: center;
      color: #23B473;
      letter-spacing: -0.03em;
      font-weight: 400;
      -webkit-font-smoothing: antialiased;
      font-family: "Roboto", "Helvetica Neue", Helvetica, Arial, sans-serif;
    }

    h3 {
      padding: 15px 20px;
      margin-top: 0;
      font-size: 1.15em;
      font-weight: 500;
      text-align: left;
      text-transform: uppercase;
      color: #8C8C8C;
      -webkit-font-smoothing: antialiased;
      opacity: 0.6;
    }

    @media (min-width: 992px) {
      .container.dashboard {
        width: 840px;
      }
    }

    .slick-dots {
      position: absolute;
      bottom: 0px;
      display: block;
      width: 100%;
      padding: 0;
      list-style: none;
      text-align: center;
      opacity: 0.5;
    }

    #quote-machine {
      height: 190px;
      background: #EAEAEA;
      background-image: url('https://storage.googleapis.com/trove-qa-teststore/quotes-lighter.svg');
      background-repeat: no-repeat;
      background-position: 50% 36%;
      background-size: 80px;
      padding: 20px 0px;
    }

    #sold-items {
      display: none;
    }

    h1 {
      text-align: center;
      font-size: 114%;
      margin-bottom: 0px;
      margin-top: 10px;
    }

    #pie-change {
      display: none;
    }

    #top-selling-image {
      max-width: 200px;
      height: auto;
      margin: 0px auto;
    }

  </style>
  <link rel="stylesheet" href="/resources/stylesheets/pie.css">
  <style>
    #pie-chart-legend {
      margin: 30px auto 20px 10px;
    }

    @media (min-width: 768px) {
      #pie-chart-legend {
        margin: 30px 10px 20px 40px;
        height: 182px;
        overflow: scroll;
      }
    }
  </style>
</head>
<body>
  <c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
  <c:import url="../fragments/nav/topNavBar.jsp"/>
  <div class="profile-container">
    <div class="cover-photo-container"><img id="covimg" src="${influencer.coverPhotoImagePath}"></div>
    <div class="profile-pic-container"><div class="profile-pic-holder"><img src="${influencer.fullProfileImagePath}" class="profile-pic-img"></div></div>
    <div class="username-container"><div class="username"><span>Your Dashboard</span><br></div></div>
  </div>
  <div class="container dashboard">
    
    <div class="row">
      <div class="hold col-xs-6">
        <div class="cont">
          <h5>You've earned</h5>
          <h4>$${totalMoneyMade}</h4>
        </div>
      </div>
      <div class="hold col-xs-6">
        <div class="cont">
          <h5>Items sold</h5>
          <h4>${totalItemsSold}</h4>
        </div>
      </div>
    </div>
    
    <div class="row">
      <div class="hold col-sm-6 col-md-6">
        <div class="holda col-md-12">
          <div id="quote-machine" class="cont">
            <blockquote>Life is tough,<br> my darling,<br> but so are you.<br><span class="breaker"></span><br>Stephanie Bennett-Henry</blockquote>
            <blockquote>Ask yourself if what you’re doing today is getting you closer to where you want to be tomorrow.<br><span class="breaker"></span><br>Anonymous</blockquote>
            <blockquote>If you haven’t found it yet,<br> keep looking.<br><span class="breaker"></span><br>Steve Jobs</blockquote>
            <blockquote>If you obey all the rules,<br> you miss all the fun.<br><span class="breaker"></span><br>Katharine Hepburn</blockquote>
            <blockquote>You never realize how strong you are, until being strong is the only choice you have.<br><span class="breaker"></span><br>Anonymous</blockquote>
            <blockquote>My mission in life is not merely to survive, but to thrive, and to do so with some passion, some compassion, some humor, and some style.<br><span class="breaker"></span><br>Maya Angelou</blockquote>
            <blockquote>You are braver than you believe, stronger than you seem, smarter than you think, and loved more than you’ll ever know.<br><span class="breaker"></span><br>A. A. Milne</blockquote>
            <blockquote>I didn’t get there by wishing for it<br> or hoping for it,<br> but by working for it.<br><span class="breaker"></span><br>Estee Lauder</blockquote>
            <blockquote>The road to success <br>is always under construction.<br><span class="breaker"></span><br>Lily Tomlin</blockquote>
            <blockquote>And those who were dancing were thought to be insane by those who could not hear the music.<br><span class="breaker"></span><br>Friedrich Nietzsche</blockquote>
          </div>
        </div>
        <div class="holdb col-md-12">
          <div class="cont" style="min-height: 452px;">
            <h3>Top selling item</h3>
            <div class="col-xs-12" style="padding: 0px 80px 10px;">
              <a id="top-selling-url" href="https://www.troveup.com/public/customize/webgl/679"><img id="top-selling-image" src="https://storage.googleapis.com/troveup-imagestore/assets/img/rumpus-ring-sml.jpg" class="img-responsive favorites-img"></a>
            </div>
            <div class="col-xs-12">
              <h1 id="top-item-name">IEM NAME</h1><br>
              <h1 id="top-item-sale">88 sales</h1>
              <h1 id="top-item-earned">$23.00 earned</h1>
            </div>
            <div id="sold-items">
              <c:forEach var="thisItem" items="${simpleItems}">
                <div class="sold-item" data-image="${thisItem.primaryDisplayImageUrl}" data-url="/featured/item/${thisItem.simpleItemId}" data-name="${thisItem.itemName}" data-sold="${thisItem.numberSold}" data-amount="${thisItem.moneyEarnedFromThisItem}">Item</div>
              </c:forEach>
            </div>
          </div>
        </div>
      </div>
      <div class="hold col-sm-6 col-md-6">
        <div class="cont">
          <h3>Most Popular</h3>

          <div id="pie-container">
            <div id="pie-center">
              <div id="pie-total">1</div>
              <div id="pie-units">ITEMS SOLD</div>
              <div id="pie-change">+ 1 TODAY</div>
            </div>
          </div>
          <ul id="pie-chart-legend"></ul>
        </div>
      </div>
    </div>
    
  </div>
  <br><br>
  <br><br>
  <br><br>
  

  <script type="text/javascript" src="https://cdn.jsdelivr.net/jquery.slick/1.5.9/slick.min.js"></script>
  
  <script>

    // check for special iphone 5 case
    function isIphone5(){
      function iOSVersion(){
        var agent = window.navigator.userAgent,
        start = agent.indexOf( 'OS ' );
        if( (agent.indexOf( 'iPhone' ) > -1) && start > -1)
          return window.Number( agent.substr( start + 3, 3 ).replace( '_', '.' ) );
        else return 0;
      }
      return iOSVersion() >= 6 && window.devicePixelRatio >= 2 && screen.availHeight==548 ? true : false;
    } 

    var soldItemsArray = [];
    var itemWithMostSales = 0;
    var amountOfItemsSold = [];
    var amountArray = [];
    var itemNames = [];

    $(".sold-item").each(function(i) {
      amountOfItemsSold.push(parseInt($(this).attr("data-sold")));
      amountArray.push( parseFloat($(this).attr("data-amount")));
      itemNames.push($(this).attr("data-name"));
    });

    // alert(amountOfItemsSold + amountArray + itemNames);

    // data for item starting values, counts, and amounts
    var data = {
      start: [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1000],
      solditems: amountOfItemsSold,
      soldamounts: amountArray,
      itemnames: itemNames
    };

    var totalCount = ${totalItemsSold};
    var totalAmount = ${totalMoneyMade};
    var subCount = "ITEMS SOLD";
    var subAmount = "TOTAL EARNINGS";
    //$.each(data.solditems,function() { totalCount += this; });
    //$.each(data.soldamounts,function() { totalAmount += this; });

    // variable to toggle between price and count mode
    var pricemode = false;
    var zoom = false;
    var oldi = "";
    var strokeVal = 4;

    // d3 donut-pie-chart setup
    if ( isIphone5() ) {
      var w = 300;
      var h = 300;
    } else {
      var w = 360;
      var h = 360;
    }
     
    var r = Math.min(w, h) / 2;

    var tooltip = d3.select("body").append("div").attr("class", "toolTip");
    var arc = d3.svg.arc().innerRadius(r - 50).outerRadius(r-20);
    var pie = d3.layout.pie().value(function(d){ return d; }).sort(null);

    var container = d3.select("#pie-container").append("svg:svg").attr("width", w).attr("height", h);
    var svg = container.append("g").attr("transform", "translate(" + w / 2 + "," + h / 2 + ")");

    // seed starting data and store _current
    svg.datum(data.start).selectAll("path").data(pie).enter().append("path").attr("fill", function(d,i){ return col(i); }).attr("d", arc).style('stroke', 'white').style('stroke-width', strokeVal).attr("class","slice").each(function(d){ this._current = d; })
        .on("mouseover",function(e,i){
           
        })
        .on("mousemove", function(d,i){
            var tamont = (Math.round(((d.value * 100 / totalAmount) * 1000)/10)/100).toFixed(2);
            var dval = numberWithCommas(d.value.toString());
            var tcount = (Math.round(((d.value * 100 / totalCount ) * 1000)/10)/100).toFixed(2);
            var printed = !pricemode ? "$" + dval + " ( " + tamont + "% )" : d.value + " Sold " + " ( " + tcount + "% )";
            tooltip.style("left", d3.event.pageX-34+"px");
            tooltip.style("top", d3.event.pageY-66+"px");
            tooltip.style("opacity", "1.0");
            tooltip.html("<div class='tip-name'>" + data.itemnames[i] + "</div>" + printed);
        }).on("mouseout", function(d,i){
            tooltip.style("opacity", "0.0");
            tooltip.transition().duration(1000).attrTween("d", arcTween);  
            $(".label.active").removeClass('active');
        }).on('click', function(d,i) {
          if (zoom) {
            if ( i == oldi) {
              svg.selectAll("path").attr("class","slice").transition().ease('cubic-out').duration('600').attr('fill', function(d,i){ return col(i); });
              d3.select(this).attr("class","slice").transition().ease('cubic-out').duration('600').attr('fill', col(i));
              zoom = !zoom;
            } else {
              svg.selectAll("path").attr("class","slice zoom").transition().ease('cubic-out').duration('600').attr('fill', '#DEDEDE');
              d3.select(this).attr("class","slice zoom active").transition().ease('cubic-out').duration('600').attr('fill', col(i));
            }
          } else {
            svg.selectAll("path").attr("class","slice zoom").transition().ease('cubic-out').duration('600').attr('fill', '#DEDEDE');
            d3.select(this).attr("class","slice zoom active").transition().ease('cubic-out').duration('600').attr('fill', col(i));
            zoom = !zoom;
          }
          oldi = i;
        });

    function render(){
      var ndata = pricemode ? data.soldamounts : data.solditems;
      var npath = svg.datum(ndata).selectAll("path").data(pie);
      npath.transition().duration(1000).attrTween("d", arcTween);  
      npath.enter().append("path").attr("fill", function(d,i) { return col(i); }).attr("d", arc).style('stroke', 'white').style('stroke-width', strokeVal).each(function(d) { this._current = d; });
      npath.exit().remove();
      toggleMode();
    }

    // Store the displayed angles in _current. Then, interpolate from _current to the new angles. During the transition, _current is updated in-place by d3.interpolate.
    function arcTween(a) {
      var i = d3.interpolate(this._current, a);
      this._current = i(0);
      return function(t) {
        return arc(i(t));
      };
    }

    function toggleMode() {
      //totalAmount = Math.round(totalAmount*100)/100;
      var priceTextSplit = totalAmount.toFixed(2).toString().split('.');
      var tot = pricemode ? numberWithCommas(priceTextSplit[0]) : totalCount;
      var su = pricemode ? subAmount : subCount;
      if (pricemode) {
        $('#pie-total').addClass('amount');
        var str = "." + priceTextSplit[1];
        $('<style>.amount#pie-total:after{content:"'+str+'"}</style>').appendTo('head');
        $('#pie-change').html('+ $214.02 TODAY&nbsp;');
      } else {
        $('#pie-total').removeClass('amount');
        $('#pie-change').html('+ 3 ITEMS TODAY&nbsp;');
      }
      $('#pie-total').html(tot);
      $('#pie-units').html(su);
      pricemode = !pricemode;
    }

    function numberWithCommas(x) {
        return x.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }

    function col(n) {
      var col = ["#26D7AA", "#F6D349", "#70B5EF", "#9AABDC", "#F8A786", "#CCCCCC", "#FF9C9C", "#FFEE9C", "#E29CFF", "#9CFFE4", "#D9FF9C", "#BE9CFF"];
      return col[n % col.length];
    }

    $(document).ready(function () {
      render();
      $("#pie-center").on('click', function(e) {
        svg.selectAll("path").attr("class","slice");
        d3.selectAll("path").attr("class","slice").attr("fill", function(d,i) { return col(i); });
        render();
      });

      var quoteMachineHolder = $('#quote-machine');

      quoteMachineHolder.slick({
        dots: true,
        slidesToShow: 1,
        infinite: false,
        slidesToScroll: 1,
        prevArrow: '#prevButton',
        nextArrow: '#nextButton',
        centerMode: true,
        centerPadding: '0px',
        focusOnSelect: true,
        autoplay: true,
        cssEase: 'ease-in-out'
      });

      //Create an array of the items that were sold
      $(".sold-item").each(function(i) {
        soldItemsArray.push(this);
      });

      //Find the best-selling item
      var salesCount = -1;
      var currentTopSales = -1;
      for (var i = 0; i < soldItemsArray.length; ++i) {
        if (parseInt($(soldItemsArray[i]).attr('data-sold')) > salesCount) {
          salesCount = parseInt($(soldItemsArray[i]).attr('data-sold'));
          currentTopSales = i;
        }
      }
      itemWithMostSales = currentTopSales;

      //Replace the generic slot data with provided data
      $("#top-item-name").html($(soldItemsArray[itemWithMostSales]).attr('data-name'));
      $("#top-item-sale").html($(soldItemsArray[itemWithMostSales]).attr('data-sold') + " sales");
      $("#top-item-earned").html("$" + $(soldItemsArray[itemWithMostSales]).attr('data-amount') + " earned");
      $("#top-selling-url").attr('href', $(soldItemsArray[itemWithMostSales]).attr('data-url'));
      $("#top-selling-image").attr('src', $(soldItemsArray[itemWithMostSales]).attr('data-image'));


      if ( isIphone5() ) {
        $("#pie-center").css("top","140px");
      } else {
      }

    });

    $.each(data.itemnames,function(d, i) { 
      $("#pie-chart-legend").append("<li class='label' id='label-" + d + "'>" + this + "</li>");
      $('head').append("<style>#label-" + d + ":before { background:" + col(d) + ";}</style>");
    });
  </script>

</body>