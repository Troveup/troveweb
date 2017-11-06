<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="trove" uri="http://troveup.com/jsp/tlds/itemimage" %>
<html>
<head>
    <title>Trove: Product Description Page</title>
    <c:import url="../fragments/headers/commonHead.jsp"/>
    <link rel="stylesheet" href="/resources/stylesheets/main.css">
    <link rel="stylesheet" href="/resources/stylesheets/browse.css">
    <!-- inline form editing -->
    <link href="//cdnjs.cloudflare.com/ajax/libs/x-editable/1.5.0/bootstrap3-editable/css/bootstrap-editable.css"
          rel="stylesheet"/>
    <script src="//cdnjs.cloudflare.com/ajax/libs/x-editable/1.5.0/bootstrap3-editable/js/bootstrap-editable.min.js"></script>
    <c:import url="../fragments/analytics/all.jsp"/>
    <link rel="stylesheet" href="/resources/stylesheets/cards.css">
<!--     <link rel="stylesheet" href="/resources/stylesheets/auth.css"> -->
    <script type="text/javascript" src="/resources/js/vendor/head.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/jquery.slick/1.5.9/slick.css">
  <link rel="stylesheet" href="/resources/stylesheets/featured.css">


    <!-- for Google -->
    <meta name="description" content="Trove product description page."/>
    <meta name="keywords" content="Trove, Jewelry, Product Page"/>
    <meta name="copyright" content="Trove"/>
    <meta name="application-name" content="Trove Application"/>
    <meta name="author" content="${item.itemOwner.firstName}"/>

    <!-- for Facebook -->
    <meta property="fb:app_id" content="420400984811981"/>
    <meta property="og:title" content="Designed On Trove"/>
    <meta property="og:description"
          content="Trove is a place where you can customize, try-on, and buy jewelry that is made by you and your friends. Come check it out!"/>
    <meta property="og:author" content="${item.itemOwner.firstName}"/>
    <!--meta property="og:image" content="https://storage.googleapis.com/troveup-qa-cdn/horizontal-bar-ring-web.jpg" /-->
    <meta property="og:image" content="<trove:itemimage item="${item}" itemNumber="0" materials="${materials}"/>"/>
    <meta property="og:url" content="{landingUrl}"/>

    <link href="/resources/stylesheets/fancybox.css" rel="stylesheet">
    <script type="text/javascript" src="/resources/js/vendor/fancybox.js"></script>

    <!-- for Twitter -->
    <meta name="twitter:card" content="summary"/>
    <meta name="twitter:title" content="${item.itemName}"/>
    <meta name="twitter:description" content="${item.itemDescription}"/>
    <meta name="twitter:image" content="<trove:itemimage item="${item}" itemNumber="0" materials="${materials}"/>"/>
    <link href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <script>TROVE.setToken("${_csrf.token}");</script>
    <style>
        .material-selector select, .quantity-selector select, .modal-collection-selector select {
            width: 200px;
        }

        .noUi-horizontal {
            height: 8px;
            margin-top: 21px;
            margin-bottom: 30px;
        }

        .slideinput {
            float: right;
            width: 70px;
            height: 32px;
            border-radius: 6px;
            border: 1px solid #ccc;
            padding: 7px;
        }

        .item-price select {
            position: relative;
            display: inline-block;
            font-size: 16px;
            border: 1px solid #d0d0d0;
            border-radius: 0px;
            padding: 10px;
            -webkit-transition: all 0.150s ease-in-out;
            -moz-transition: all 0.15s ease-in-out;
            -ms-transition: all 0.15s ease-in-out;
            -o-transition: all 0.15s ease-in-out;
        }

        .item-size-container {
            height: inherit;
            display: inline-block;
            margin: 0 10px 0;
        }

        .item-size select {
            position: relative;
            display: inline-block;
            font-size: 16px;
            border: 1px solid #d0d0d0;
            border-radius: 0px;
            padding: 10px;
            -webkit-transition: all 0.150s ease-in-out;
            -moz-transition: all 0.15s ease-in-out;
            -ms-transition: all 0.15s ease-in-out;
            -o-transition: all 0.15s ease-in-out;
        }

        .item-size {
            padding: 0px;
        }

        .item-size {
            font-size: 1.2em;
            position: relative;
            display: inline-block;
        }

        .selector-title {
            display: block;
            padding-top: 10px;
        }

        .ownerimg {
            width: 30%;
            max-height: 70px;
        }

        .ownerimgholder {
            display: block;
            margin-top: 25px;
            padding: 7px;
            text-align: center;
            margin-bottom: 13px;
        }

        .profile-pic-img {
            /*background-image: url('/resources/img/profpic2.png');
            background-size: 60px 60px;*/
            max-height: 60px;
            max-width: 60px;
            border-radius: 50%;
        }

        .overlaid {
            position: relative;
            background-color: rgba(242, 241, 242, 0.8);
            pointer-events: none;
            z-index: 3;
            opacity: 0.0;
            -webkit-transition: all 0.5s ease-in-out;
            -moz-transition: all 0.5s ease-in-out;
            -ms-transition: all 0.5s ease-in-out;
            -o-transition: all 0.5s ease-in-out;
            transition: all 0.5s ease-in-out;
        }

        .overlaid.opaa {
            opacity: 1.0;
        }

        .overlaid .message {
            margin-top: 0;
            margin-left: 0;
            left: 0;
            position: absolute;
            text-align: center;
            padding: 200px 20px;
            border-radius: 2px;
            color: #797979;
            font-size: 18px;
            width: 100%;
            height: 754px;
            background-color: #F0F0F0;
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

        .editableform {
            width: 50%;
            height: 35px;
            float: left;
        }

        input.form-control.input-sm {
            font-size: 20px;
            color: #f26868;
        }

        .btn-edit {
            float: right;
            font-size: 20px;
            padding: 10px;
            border: 0px solid transparent;
            background-color: transparent;
            cursor: pointer;
            color: #ABABAB;
            text-decoration: none;
            border-radius: 2px;
            opacity: 1;
            -webkit-transition: all 0.30s ease-in-out;
            -moz-transition: all 0.30s ease-in-out;
            -ms-transition: all 0.30s ease-in-out;
            -o-transition: all 0.30s ease-in-out;
        }

        .editable-input .form-control {
            border: 0px solid transparent;
        }

        .editable-input .form-control:focus {
            box-shadow: none;
        }

        .editable-clear-x {
            display: none;

        }

        .btn-edit:hover {
            color: #333;
        }

        .btn.btn-primary.btn-sm.editable-submit {
            color: #fff;
            background-color: #f26868;
            line-height: 1.5;
            border-radius: 50%;
            font-size: 14px;
            padding: 10px 10px;
            border: 0px solid transparent;
        }

        .btn.btn-primary.btn-sm.editable-submit:hover {
            color: #FFF;
            background-color: #dd2435;
            border: 0px solid transparent;
        }

        .btn.btn-default.btn-sm.editable-cancel {
            background: transparent;
            color: #ABABAB;
            line-height: 1.5;
            border-radius: 2px;
            font-size: 14px;
            padding: 10px 10px;
            border: 0px solid transparent;

        }

        .btn.btn-default.btn-sm.editable-cancel:hover {
            color: 333;
            border: 0px solid transparent;
            background: transparent;
        }

        .item-price select {
            position: relative;
            display: inline-block;
            font-size: 16px;
            border: 1px solid #d0d0d0;
            border-radius: 2px;
            padding: 10px;
            -webkit-transition: all 0.150s ease-in-out;
            -moz-transition: all 0.15s ease-in-out;
            -ms-transition: all 0.15s ease-in-out;
            -o-transition: all 0.15s ease-in-out;
        }

        .item-price {
            padding: 8px 0 0;
        }

        .selector-title {
            display: block;
        }

        .ownerimg {
            width: 30%;
        }

        .ownerimgholder {
            display: block;
            margin-top: 25px;
            padding: 7px;
            text-align: center;
            margin-bottom: 13px;
        }

        .profile-pic-img {
            /*background-image: url('/resources/img/profpic2.png');
            background-size: 60px 60px;*/
            max-height: 60px;
            max-width: 60px;
            border-radius: 50%;
        }

        .buyModal.in ~ .modal-backdrop.in {
            background-color: #333;
            opacity: 0.5;
            filter: alpha(opacity=50);
        }

        .modal-content {
            border: 1px solid #e5e5e5;
            border-radius: 2px;
        }

        .modal-header {
            box-shadow: none;
            -webkit-box-shadow: none;
        }

        .sizing-chart-cell {
            padding: 5px;
        }

        .panel-size {
          margin-bottom: 20px;
          background-color: #fff;
          border-top: 1px solid #f0f0f0;
          border-radius: 0px;
          -webkit-box-shadow: none;
          box-shadow: none;
          text-align: left;
          text-decoration: none;
          padding: 10px 0px;
        }

        .panel-size-title {
          margin-top: 0;
          margin-bottom: 0;
          font-size: 13px;
          margin-left:15px;
          color: inherit;
          font-weight: 600;
          text-transform: uppercase;
          text-decoration: underline;
        }

        .panel-size-heading{
            margin-top:10px;
        }

        .panel-size-title i {
          float: right;
          font-weight: 800;
          font-size: 16px;
          margin-top: -3px;
          margin-right: -6px;
        }

        .panel-size-body{
            padding:0 15;
        }


        @media (min-width: 768px) {
            .modal-dialog {
                width: 510px;
                margin: 30px auto;
            }
        }

        .try-on-hand {
            float: left;
            display: block;
            width: 30%;
            padding-left: 25px;
        }

        .try-on-text {
            float: left;
            display: block;
            width: 68%
        }

        @media (max-width: 767px) {
            .try-on-hand {
                display: none;
            }

            .try-on-text {
                width: 100%;
                text-align: center;
            }
        }

        @media (max-width: 440px) {
            .try-on-description {
                display: none;
            }
        }

        .gold_polished,
        .platinum_polished,
        .red_gold_polished,
        .white_gold_polished,
        .palladium_polished {
            display: none;
        }

        .gold_polished.product-image,
        .platinum_polished.product-image,
        .red_gold_polished.product-image,
        .white_gold_polished_.product-image,
        .palladium_polished.product-image {
            height: 400px;
            display: block;
            margin: 0px auto;
        }

        .btn {
            border-radius: 2px;
        }

        .additional-remakes-container {

        }

        .additional-remakes-img-wrapper {
            text-align: left;
            overflow: hidden;
            width: 140px;
        }

        .additional-remakes-img {
            width: 140px;
            height: 140px;
        }

        .additional-remakes-title-wrapper {
            display: block;
        }

        .card_product_name {
            font-size: 13px;
            max-width: 240px;
            text-align: left;
        }

        .card_username {
            font-size: 10px;
            text-align: left;
            white-space: nowrap;
        }

        .additional-remakes-container {
            text-align: center;
        }

        .card_user_info {
            font-size: 13px;
            color: #232323;
            max-width: 100%;
        }

        .additional-remakes-img-wrapper {
            display: inline-block;
            margin-left: 0px;
            padding: 10px 22px 10px 20px;
            width: 158px;
        }

        .remade-by-product-img.ds img {
            width: 60px;
            height: 60px;
        }

        .remade-by-pic.ds img {
            width: 25px;
            height: 25px;
        }

        .cardy.ds {
            margin-top: 30px;
            padding-bottom: 20px;
        }

        body {
            background: #FFFFFF;
        }

        .made-by-user-info {
            margin-bottom: 30px;
        }

        .btn {
            border-radius: 2px;
        }

        .carousel-control.left, .carousel-control.right {
            background: none;
            top: 50%;
        }

        .carousel-inner > .item > img {
            margin-left: 0px;
        }

        @media (min-width: 768px) {
            .carousel-inner > .item > img {
                margin-left: 60px;
            }
        }

        .product-title-bar {
            padding-top: 30px;
            padding-right: 6px;
            padding-left: 20px;
            margin-bottom: 35px;
        }

        .chain-container {
            padding-bottom: 30px;
        }

        .special-container {
            display: none;
        }

        #thumbnail {
            max-width: 100%;
            padding: 40px;
        }

        .tnails {
            display: none;
            max-width: 100%;
            padding: 40px;
        }

        .tnails.show {
            display: block;
        }

        .fancybox-skin {
            padding: 0px !important;
            border: 1px solid #DEDEDE;
        }

    </style>
    <c:if test="${not isAuthenticated}"><link rel="stylesheet" href="/resources/stylesheets/authmod.css"></c:if>
</head>

<body>
<c:import url="../fragments/nav/topNavBar.jsp"/>
<c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
<%-- <c:import url="fragments/navBar.jsp"/> --%>
<div class="container cards_product_wrapper">
    <div class="row">

        <div class="col-sm-8">
            <div class="cardy">

                <div id="bump" class="overlaid">
                    <div class="message">
                        <h2>Saved to <br></br>Your Trove</h2>
                        <img class="add_image" src="/resources/img/plus.png">
                    </div>
                </div>

                <div id="bump2" class="overlaid">
                    <div class="message">
                        <h2>Added to <br></br>Your Bag</h2>
                        <img class="add_image" src="/resources/img/plus.png">
                    </div>
                </div>

                <div class="product-title-bar">
                    <!-- <button type="button" class="btn-edit" id="controller" style="float: right; margin-top: -15px; margin-right: 8px;"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span></button> -->
                    <!-- <a href="#" id="model" data-value="${item.itemName}"><div id="view" style="float: left;"></div></a>  -->
                    <div id="title"
                         style="white-space:nowrap;text-overflow:ellipsis;overflow:hidden;margin-bottom:5px">${item.itemName}</div>
                    <c:if test="${empty stillRendering}">
                        <div>
                            <div class="social-icon"><a id="pinshare"
                                                        href="javascript:window.open('http://pinterest.com/pin/create/bookmarklet/?media=${map[0].imageUrls[0]}&url=${landingUrl}&is_video=false&description=I%20Customized%20this%20on%20Trove.%20Trove%20lets%20you%20customize%20jewelry%20and%203D-prints%20it%20for%20you%20in%20precious%20metals', 'Share on Pinterest', 'width=550,height=300');"><img
                                    src="https://storage.googleapis.com/trove-demo-cdn/img/pinterest-badge-2.png"
                                    alt="Share on Pinterest" class="inline-icon"></a></div>
                            <div class="social-icon"><a id="twshare"
                                                        href="javascript:window.open('http://twitter.com/intent/tweet?status=Trove%20lets%20you%20customize%20jewelry%20and%203D-prints%20it%20for%20you%20in%20precious%20metals+${landingUrl}', 'Share on Twitter', 'width=550,height=300');"><img
                                    src="https://storage.googleapis.com/trove-demo-cdn/img/twitter-badge-2.png"
                                    alt="Share on Twitter" class="inline-icon"></a></div>
                            <div class="social-icon"><a id="fbshare"
                                                        href="javascript:window.open('http://www.facebook.com/sharer/sharer.php?u=${landingUrl}', 'Share on Facebook', 'width=550,height=300');"><img
                                    src="https://storage.googleapis.com/trove-demo-cdn/img/facebook-badge-2.png"
                                    alt="Share on Facebook" class="inline-icon"></a></div>
                        </div>
                    </c:if>
                </div>

                <!-- <div class="product-title-bar">
              <span>${item.itemName}</span> -->
                <!-- <div class="social-icon"><a href=""><img src="http://storage.googleapis.com/trove-demo-cdn/img/facebook-badge-2.png" alt="Share on Facebook" class="inline-icon"></a></div>
                <div class="social-icon"><a href=""><img src="http://storage.googleapis.com/trove-demo-cdn/img/twitter-badge-2.png" alt="Share on Twitter" class="inline-icon"></a></div>
                <div class="social-icon"><a href=""><img src="http://storage.googleapis.com/trove-demo-cdn/img/pinterest-badge-2.png" alt="Share on Instagram" class="inline-icon"></a></div> -->
                <!-- </div> -->
                <!-- <div class="product-secondary-bar">
                  <div class="edit-icon"></div>
                </div> -->

                <c:choose>
                    <c:when test="${not empty stillRendering}">
                        <div class="product-description-image">
                                <%--c:forEach var="image" items="${item.images}" varStatus="outer">
                                  <img class='${image.material}' id='${image.material}-${outer.index}' src='${image.largeImageUrlPath}'>
                                </c:forEach--%>
                            <img class="product-image" src="${item.defaultCardImageUrl}">
                        </div>
                    </c:when>

                    <c:otherwise>
                        <div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
                            <ol class="carousel-indicators">
                                <c:forEach begin="0" end="${fn:length(map[0].imageUrls) - 1}" varStatus="loop">
                                    <c:choose>
                                        <c:when test="${loop.index == 0}">
                                            <li onclick="fireMixpanelEvent('Click_Image${loop.index + 1}');"
                                                data-target="#carousel-example-generic" data-slide-to="${loop.index}"
                                                class="active" style="border: 1px solid #8e8b8b;"></li>
                                        </c:when>
                                        <c:otherwise>
                                            <li onclick="fireMixpanelEvent('Click_Image${loop.index + 1}');"
                                                data-target="#carousel-example-generic" data-slide-to="${loop.index}"
                                                style="border: 1px solid #8e8b8b;" class=""></li>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </ol>
                            <div class="carousel-inner" role="listbox">
                                <c:forEach var="image" items="${map[defaultMaterialIndex].imageUrls}" varStatus="loop">
                                    <c:choose>
                                        <c:when test="${loop.index == 0}">
                                            <div class="item active"><img id="img${loop.index}" src="${image}"></div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="item"><img id="img${loop.index}" src="${image}"></div>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </div>
                            <a class="left carousel-control" href="#carousel-example-generic" role="button"
                               data-slide="prev">
                                <span class="ion-chevron-left" aria-hidden="true"></span>
                                <span class="sr-only">Previous</span>
                            </a>
                            <a class="right carousel-control" href="#carousel-example-generic" role="button"
                               data-slide="next">
                                <span class="ion-chevron-right" aria-hidden="true"></span>
                                <span class="sr-only">Next</span>
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>


                <div class="item-options">
                    <div class="material-selector-container">
                        <span class="selector-title">Color</span>

                        <div class="material-selector">
                            <c:choose>
                                <c:when test="${not empty stillRendering}">
                                    <c:if test="${not empty materials}">
                                        <select name="materialmenu" id="materialmenu">
                                            <c:forEach var="material" items="${materials}">
                                                <c:forEach var="finish" items="${material.finishList}">
                                                    <option value="${material.materialId} ${finish.finishId}">${finish.name}</option>
                                                </c:forEach>
                                            </c:forEach>
                                        </select>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <select name="materialmenu" id="materialmenu" class="ignore">
                                        <c:if test="${not empty map}">
                                            <c:forEach var="materialmap" items="${map}">
                                                <option value="${materialmap.material.materialId} ${materialmap.finish.finishId}"
                                                        data-price="${materialmap.price}"
                                                        <c:forEach var="image" items="${materialmap.imageUrls}"
                                                                   varStatus="loop">
                                                            data-f${loop.index}="${image}"
                                                        </c:forEach>
                                                        data-size="${fn:length(materialmap.imageUrls)}"
                                                >${materialmap.finish.name}</option>
                                            </c:forEach>
                                        </c:if>
                                    </select>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <c:if test="${item.sizeDropdownAvailable}">
                        <div class="item-size-container">
                            <span class="selector-title" id="sizinghelpanchor">Size <!-- <a href="#sizinghelp"><span
                                    style="color: #ee2435;text-decoration:underline;">(Sizing Help)</span></a> --></span>

                            <div class="item-size">
                                <select name="size" id="size">
                                    <c:if test="${not empty size}">
                                        <c:forEach var="individualsize" items="${size}">
                                            <option value="${individualsize['key']}">${individualsize['value']}</option>
                                        </c:forEach>
                                    </c:if>
                                </select>
                            </div>
                        </div>
                    </c:if>

                    <div class="item-price-container">
                        <span class="selector-title">Price</span>

                        <div class="item-price">-</div>
                    </div>

                    <c:if test="${not empty availableChains}">
                        <div class="chain-container">
                            <span class="selector-title">Chain</span>

                            <div class="material-selector">
                                <select name="chainDropdown" id="chainDropdown">
                                    <c:forEach var="chain" items="${availableChains}">
                                        <option value="${chain.chainId}" selected>${chain.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </c:if>

                </div>
                <!--Item Options End-->

                <div class="product-cta-nav">
                    <ul class="product-cta-btns">
                        <li>
                            <button type="button" id="card-${item.itemId}"
                                    onclick="saveButtonClick('${item.itemId}')"
                                    class="btn btn--red  btn--product--description"><img
                                    src="https://storage.googleapis.com/trove-demo-cdn/img/trove-icon-white.svg"
                                    class="icon-btn" alt="Save to Trove"><span
                                    style="color: #fff"> Save</span></button>
                        </li>
                        <li><a href="/public/customize/webgl/${item.itemId}"
                               class="btn btn--red btn--product--description customize" role="button"
                               onclick="custb()"><img
                                src="https://storage.googleapis.com/trove-demo-cdn/img/remix-icon-white.svg"
                                class="icon-btn" alt="Remakes"> Customize</a></li>
                        <li>
                            <button class="btn btn--darkgray  btn--product--description buy cardbuybutton"
                                    onclick="buyButtonClick()"><img
                                    src="https://storage.googleapis.com/troveup-imagestore/assets/img/shopping-bag-lightweight-2-white.svg"
                                    class="icon-btn" alt="Add to Shopping Bag"><span style="color: #fff"> Buy</span>
                            </button>
                        </li>
                    </ul>
                </div>
                <!--Product CTA Nav Ends-->

                <!--Try-on Model Banner-->
<!--                 <div style="background-color:#f0f0f0; padding: 15px;margin:0 15px 15px;height:150px;">
                    <div class="try-on-hand">
                        <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/hand-w-ring-outline.svg"
                             style="height:100px;margin-top:10px">
                    </div>
                    <div class="try-on-text">
                        <h4 style="margin-top:0 px;">Get a Free Jewelry Sample</h4>
                        <span class="try-on-description" style="font-style:italic;font-size:13px;margin-bottom:10px;">Add this item to your Bag and select your free jewelry sample at checkout.</span><br>
                        <a href="/try" target="_blank" class="btn btn-open" style="margin-top:10px;">Learn More</a>
                    </div> 
                </div>-->
                <!--End Try-on Model Banner-->

                <div class="panel-size">
                  <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                    <div class="panel-size-heading">
                      <b class="panel-size-title">SIZE & MATERIAL<i class="icon ion-plus" style="margin-right:15px;"></i></b>
                    </div>
                  </a>
                  <div id="collapseOne" class="panel-collapse collapse in">
                    <div class="panel-size-body">
                      <p class="size-and-material item-description"><b>Materials: </b><br>Each piece is made of cast brass and plated with either 18K gold, rose gold, or rhodium for durability and shine.</p>
                      <p class="size-and-material item-description"><b>Sizing Help: </b><br>
                        You can measure your ring size using <a
                            href="https://storage.googleapis.com/troveup-imagestore/assets/ring-sizer-final.pdf"
                            target="_blank" alt="Jewelry Sizing Chart" style="color:#f26868">this chart.</a><br><br>
                        Our bracelet sizes are sized according to their inside diameter at the widest point. Use a ruler
                        or tape measure to see how wide your wrist is and find the closest larger size.<br><br>
                    <table style="font-size: 13px;">
                        <tr style="border-bottom:1px solid #b2b8b8">
                            <td class="sizing-chart-cell">Bracelet Size</td>
                            <td class="sizing-chart-cell">Inner Width (cm)</td>
                        </tr>
                        <tr>
                            <td class="sizing-chart-cell">XS</td>
                            <td class="sizing-chart-cell">55</td>
                        </tr>
                        <tr>
                            <td class="sizing-chart-cell">S</td>
                            <td class="sizing-chart-cell">60</td>
                        </tr>
                        <tr>
                            <td class="sizing-chart-cell">M</td>
                            <td class="sizing-chart-cell">65</td>
                        </tr>
                        <tr>
                            <td class="sizing-chart-cell">L</td>
                            <td class="sizing-chart-cell">70</td>
                        </tr>
                        <tr>
                            <td class="sizing-chart-cell">XL</td>
                            <td class="sizing-chart-cell">75</td>
                        </tr>
                    </table>
                    

                      </p>
                      <br>
                      <!--a href="https://storage.googleapis.com/troveup-imagestore/assets/ring-sizer-final.pdf" target="_blank" class="ui-link"><span style="font-weight: 500; color: #ee2435;text-decoration:underline;">View Size Guide</span></a-->
                    </div>
                  </div>
                </div>

                <div class="additional-remakes-container">
                    <div class="additional-remakes-title-wrapper"> Popular Customizations</div>
                    <c:if test="${not empty relatedItems}">
                        <c:forEach var="item" items="${relatedItems}">
                            <div class="additional-remakes-img-wrapper">
                                <a href="/public/productdescription/${item.itemId}"><img class="additional-remakes-img"
                                                                                         src="<trove:itemimage item="${item}" itemNumber="0" materials="${materials}"/>"></a>
                                    <%-- <img class="additional-remakes-img" src="${item.mediumImageUrlPath}" class=""> --%>
                                <div class="card_product_name"><a
                                        href="/public/productdescription/${item.itemId}">${item.itemName}</a></div>
                                <div class="card_user_info"><a href="/private/user/${item.itemOwner.userId}"><img
                                        src="${item.itemOwner.profileImageThumbnailPath}"
                                        class="card_avatar_small"><span
                                        class="card_username">${item.itemOwner.firstName}</span></a></div>
                            </div>
                        </c:forEach>
                    </c:if>
                    <!--  <img class="additional-remakes-img" src="https://storage.googleapis.com/troveup-imagestore/assets/img/pyramid-ring.jpg">
                     <img class="additional-remakes-img" src="https://storage.googleapis.com/troveup-imagestore/assets/img/wrap-silver-ring.jpg">
                     <img class="additional-remakes-img" src="https://storage.googleapis.com/troveup-imagestore/assets/img/dual-bracelet.jpg">
                     <img class="additional-remakes-img last" src="https://storage.googleapis.com/troveup-imagestore/assets/img/art-deco-cuff.jpg"> -->
                </div>

                <div class="product-about"><u>About this design</u><br>
                    <span style="font-size: 13px;">${item.itemDescription}</span><br><br>

                    <!-- <button type="button" class="btn-edit" id="controller2" style="float: right; margin-top: -15px; margin-right: 8px;"></button>
            <a href="#" id="model2" data-value="${item.itemDescription}"><div id="view2" style="float: left;"></div></a> -->

                    
                </div>

            </div>
        </div>


        <div class="col-sm-4">
            <div class="cardy">
                <div class="user_card_content_a">
                    <div class="card_remake_title"> Made By</div>
                </div>
                <div class="collection_card_content_row1">
                    <div class="made-by-user-info">
                        <div class="made-by-pic"><a onclick="madepicb(${item.itemOwner.userId})"
                                                    href="/private/user/${item.itemOwner.userId}"
                                                    alt="view this user's profile"><img
                                src="${item.itemOwner.fullProfileImagePath}" alt="user profile image"
                                class="profile-pic-img"></a></div>
                        <div class="made-by-username"><a href="/private/user/${item.itemOwner.userId}"
                                                         onclick="madenameb(${item.itemOwner.userId})"
                                                         alt="view this user's profile"><span>${item.itemOwner.firstName}</span></a>
                        </div>
                        <br>

                        <div class="product-follow-btn">
                            <c:choose>
                                <c:when test="${item.itemOwner.userId == authUser.userId}"></c:when>
                                <c:when test="${item.itemOwner.followed}">
                                    <button onclick="TROVE.unfollowUser('${item.itemOwner.userId}', 'productdescription')"
                                            id="ufolwbutton" class="btn ufolbtn${item.itemOwner.userId} btn--lightgray">
                                        <span style="color: #fff">Unfollow</span></button>
                                </c:when>
                                <c:otherwise>
                                    <button onclick="TROVE.followUser('${item.itemOwner.userId}', 'productdescription')"
                                            id="folwbutton" class="btn folbtn${item.itemOwner.userId} btn--red"><span
                                            style="color: #fff">Follow</span></button>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
                <%--<div class="collection_card_content_row1">
                  <div class="ownerimgholder">
                    <%--c:forEach begin="0" end="5" varStatus="loop" var="ownedItem" items="${item.itemOwner.ownedItems}">
                      <a href="/public/productdescription/${ownedItem.itemId}" onclick="madeitemb(${ownedItem.itemId})"><img class="ownerimg" src="<trove:itemimage item="${ownedItem}" itemNumber="0" materials="${materials}"/>"></a>
                    </c:forEach>
                  </div>
                </div--%>
            </div>

            <%------livephoto-----%>
            <c:if test="${not empty item.livePhotoUrl}">
                <div class="cardy live">
                    <div class="user_card_content_a">
                        <div class="card_remake_title"> Live View</div>
                    </div>
                    <div class="live-holder">
                        <a id="example1"
                           href="${item.livePhotoUrl}">
                            <img id="thumbnail" alt="example1"
                                 src="${item.livePhotoUrl}"/>
                        </a>
                    </div>
                </div>
            </c:if>

            <div class="cardy ds">
                <div class="user_card_content_a">
                    <div class="card_remake_title"> Design Story</div>
                </div>
                <div class="remakes_card_content_row1">
                    <div class="remade-by-item"><a onclick="dsbaseb('replace')"
                                                   href="/public/productdescription/${itemParent.itemId}"><img
                            class="remade-by-product-img ds" src="${itemParent.defaultCardImageUrl}"></a></div>
                    <div class="made-by-username"><span style="font-size: .8em">Remade From</span></div>
                    <div class="made-by-username">
                        <div class="remade-by-pic ds"><a onclick="dsbasenameb('replace')"
                                                         href="/private/user/${itemParent.itemOwner.userId}"
                                                         alt="view this user's profile"><img
                                src="${itemParent.itemOwner.fullProfileImagePath}" alt="user profile image"
                                class="profile-pic-img"></a></div>
                        <a onclick="dsbasenameb('replace')" href="/private/user/${itemParent.itemOwner.userId}"
                           alt="view this user's profile"><span
                                style="font-size: 1em;">${itemParent.itemOwner.username}</span></a>
                    </div>
                </div>
                <div class="remakes_card_content_row1">
                    <div class="remade-by-item"><a onclick="dsremadeb('replace')"
                                                   href="/public/productdescription/${itemBase.itemId}"><img
                            class="remade-by-product-img ds" src="${itemBase.defaultCardImageUrl}"></a></div>
                    <div class="made-by-username"><span style="font-size: .8em">Original Design</span></div>
                    <div class="made-by-username">
                        <div class="remade-by-pic ds"><a onclick="dsremadenameb('replace')"
                                                         href="/private/user/${itemBase.itemOwner.userId}"
                                                         alt="view this user's profile"><img
                                src="${itemBase.itemOwner.fullProfileImagePath}" alt="user profile image"
                                class="profile-pic-img"></a></div>
                        <a onclick="dsremadenameb('replace')" href="/private/user/${itemBase.itemOwner.userId}"
                           alt="view this user's profile"><span
                                style="font-size: 1em;">${itemBase.itemOwner.username}</span></a>
                    </div>
                </div>
            </div>
        </div>


    </div>
</div>
</div>

<form action="" id="add_to_cart_form" class="form-horizontal">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input id="itemId" type="hidden" name="itemId" value="${item.itemId}"/>
    <input id="materialId" type="hidden" name="materialId" value="${item.materialId}"/>
    <input id="finishId" type="hidden" name="finishId" value="${item.finishId}"/>
    <input id="sizeholder" type="hidden" name="size"/>
    <input id="chain" type="hidden" name="chain" value="0"/>
</form>

<script type="text/javascript" src="/resources/js/trove/Auth.js"></script>
<script>

    var authHelper = new AuthHelper("${_csrf.token}", ${isAuthenticated});

    function saveButtonClick(itemId) {
        if (authHelper.getIsAuthenticated()) {
            performAddItemRequest(itemId);
        } else {
            authHelper.setActionToPerformPostAuth(saveButtonClick);
            authHelper.setArgsForPostAuthAction([itemId]);
            triggerAuthModal();
        }
    }

    function buyButtonClick() {
        updateSize(false);
        updateMaterial(false);
        updateChain(false);
        buyb();
        submitToCart();
    }

    $("#example1").click(function () {
        mixpanel.track("productdescription_livephoto");
    });

    function custb() {
        mixpanel.track("productdescription_customize", {
            "item": '${item.itemId}',
            "itemname": '${item.itemName}'
        });
    }
    ;
    function buyb() {
        mixpanel.track("productdescription_buy", {
            "item": '${item.itemId}',
            "itemname": '${item.itemName}'
        });
    }
    ;

    function madenameb(userId) {
        mixpanel.track("madeby_username", {
            "user": userId
        });
    }
    ;
    function madefollowb(userId) {
        mixpanel.track("madeby_follow", {
            "user": userId
        });
    }
    ;
    function madeitemb(itemId) {
        mixpanel.track("madeby_item", {
            "item": '${item.itemId}',
            "itemclicked": itemId
        });
    }
    ;
    function madepicb(itemId) {
        mixpanel.track("madeby_pic", {
            "item": '${item.itemId}',
            "itemclicked": itemId
        });
    }
    ;
    function dsbasenameb(userId) {
        mixpanel.track("designstory_base_username", {
            "user": userId
        });
    }
    ;
    function dsbaseb(itemId) {
        mixpanel.track("designstory_base_design", {
            "item": itemId
        });
    }
    ;
    function dsremadenameb(userId) {
        mixpanel.track("designstory_remade_username", {
            "user": userId
        });
    }
    ;
    function dsremadeb(itemId) {
        mixpanel.track("designstory_remade_design", {
            "item": itemId
        });
    }
    ;

    var e = document.getElementById("materialmenu");
    if (e) {
        var materialID = $("#materialId").val();
        var finishID = $("#finishId").val();
        var defaultMaterial = materialID + " " + finishID;

        var materialIndex = 0;
        for (var i = 0; i < e.options.length; i++) {
            if (e.options[i].value == defaultMaterial) {
                materialIndex = i;
            }
        }
        e.selectedIndex = materialIndex;
    }

    function updatePriceEstimate(newmat, newfin) {
        var e = document.getElementById("materialmenu");
        var mat = e.options[e.selectedIndex].value.split(" ");
        var data = new FormData();
        data.append("itemId", '${item.itemId}');
        data.append("materialId", newmat);
        data.append("finishId", newfin);
        $('.item-price').html('updating...');
        jQuery.ajax({
            url: '/cardpriceestimate',
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
                $('.item-price').html(data.estimate);
            }
        });
    }

    function requestPriceEstimate() {
        var data = new FormData();
        data.append("itemId", '${item.itemId}');
        data.append("materialId", document.getElementById("materialId").value);
        data.append("finishId", document.getElementById("finishId").value);
        $('.item-price').html('updating...');
        jQuery.ajax({
            url: '/cardpriceestimate',
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
                $('.item-price').html(data.estimate);
            }
        });
    }


    function submitToCart() {
        // e.preventDefault();
        // $('.spinner.addtocartfromcard-'+String(itemid)).fadeIn();
        // $('.addtocartfromcardstuff-'+String(itemid)).hide();
        var $form = $('#add_to_cart_form');
        var data = $form.serialize();
        $.ajax({
            type: 'POST',
            data: data,
            url: '/noncustaddtobag',
            success: updateCart
        });

    }

    var firstsize = true;
    var firstmaterial = true;

    function updateSize(track) {

        var sizeDropdown = $('#size');

        if (sizeDropdown && sizeDropdown.val()) {
            $('#sizeholder').val(sizeDropdown.val());
            if (firstsize) {
                firstsize = false;
            } else {
                if (track) {
                    mixpanel.track("productdescription_size", {
                        "item": '${item.itemId}',
                        "itemname": '${item.itemName}',
                        "size": document.getElementById("size").value
                    });
                } else {
                }
            }
        }
    }

    function updateMaterial(track) {
        var e = document.getElementById("materialmenu");
        var mater = e.options[e.selectedIndex].value.split(" ");
        document.getElementById("materialId").value = mater[0];
        document.getElementById("finishId").value = mater[1];
        if (firstmaterial) {
            firstmaterial = false;
        } else {
            if (track) {
                mixpanel.track("productdescription_material", {
                    "item": '${item.itemId}',
                    "itemname": '${item.itemName}',
                    "material": mater[0],
                    "finish": mater[1]
                });
            } else {

            }
        }
    }

    function updateChain(track) {
        var chainId = $('#chainDropdown').val();
        $('#chain').val(chainId);

        if (firstmaterial) {
            firstmaterial = false;
        } else {
            if (track) {
                mixpanel.track("productdescription_chain", {
                    "item": '${item.itemId}',
                    "itemname": '${item.itemName}',
                    "chainId": chainId
                });
            }
        }
    }

    function land() {
        mixpanel.track("land_productdescription");
    }
    ;

    function performAddItemRequest(itemId) {
        mixpanel.track("productdescription_trove", {
            "item": itemId,
            "itemname": '${item.itemName}',
        });

        //Grab the base URL
        if (typeof location.origin === 'undefined')
            location.origin = location.protocol + '//' + location.host;
        $.ajax({
            url: location.origin + "/addtotrove",
            type: 'post',
            data: {
                itemId: itemId
            },
            headers: {
                "X-CSRF-TOKEN": "${_csrf.token}"
            },
            dataType: 'json',
            success: updateCard(itemId)
        });
    }
    ;

    function updateCard(cardId) {
        var newcount = parseInt($('.tallyho-profile').html()) + 1;
        $('#card-' + cardId + ' span').text(' Troved');
        document.getElementById('card-' + cardId).onclick = null;
        $('#bump').addClass('opa');
        $('.tallyho-profile').html(String(newcount));
        $('.tallyho-profile').addClass('showitnow');
        setTimeout(function () {
            $('#bump').removeClass('opa');
        }, 2000);
    }
    ;

    function updateCart() {
        $('#bump2').addClass('opa');
        $('.cardbuybutton span').text(' Added');

        //Declared in topNavBar
        navHelper.incrementBagItemCounter();

        setTimeout(function () {
            $('#bump2').removeClass('opa');
        }, 2000);
    }
</script>

<script>

</script>

<script>


    function fireMixpanelEvent(event) {
        switch (event) {
            //
            // Fires when users land on the FTUE Buy Page
            // fireMixpanelEvent('FTUE_Purchase');
            case 'FTUE_Purchase':
                mixpanel.track("FTUE_Purchase");
                break;
            //
            // Fires when users click Buy
            // fireMixpanelEvent('Click_Buy');
            case 'Click_Buy':
                mixpanel.track("Click_Buy");
                break;
            //
            // Fires when user selects the material dropdown and records what the user switched to
            // fireMixpanelEvent('Click_Material');
            case 'Click_Material':
                var material = $("#materialmenu").find('option:selected').text();
                mixpanel.track("Click_Material", {
                    "material": material
                });
                break;
            //
            // Fires when user shares on Facebook
            // fireMixpanelEvent('Click_Share_FB');
            case 'Click_Share_FB':
                mixpanel.track("Click_Share_FB");
                break;
            //
            // Fires when user shares on Twitter
            // fireMixpanelEvent('Click_Share_TW');
            case 'Click_Share_TW':
                mixpanel.track("Click_Share_TW");
                break;
            //
            // Fires when user shares on Pinterest
            // fireMixpanelEvent('Click_Share_PIN');
            case 'Click_Share_PIN':
                mixpanel.track("Click_Share_PIN");
                break;
            //
            // Fires when user navigates to Image 1 either via bubbles or arrows
            // fireMixpanelEvent('Click_Image1');
            case 'Click_Image1':
                mixpanel.track("Click_Image1");
                break;
            //
            // Fires when user navigates to Image 2 either via bubbles or arrows
            // fireMixpanelEvent('Click_Image2');
            case 'Click_Image2':
                mixpanel.track("Click_Image2");
                break;
            //
            // Fires when user navigates to Image 3 either via bubbles or arrows
            // fireMixpanelEvent('Click_Image3');
            case 'Click_Image3':
                mixpanel.track("Click_Image3");
                break;
            //
            // Fires when user navigates to Image 4 either via bubbles or arrows
            // fireMixpanelEvent('Click_Image4');
            case 'Click_Image4':
                mixpanel.track("Click_Image4");
                break;
            //
            // Fires when users click next on shipping
            // fireMixpanelEvent('Checkout_Shipping');
            case 'Checkout_Shipping':
                mixpanel.track("Checkout_Shipping");
                break;
            //
            // Fires when users click next on billing
            // fireMixpanelEvent('Checkout_Billing');
            case 'Checkout_Billing':
                mixpanel.track("Checkout_Billing");
                break;
            //
            // Fires when users click submit button on promo code
            // fireMixpanelEvent('Checkout_Promo');
            case 'Checkout_Promo':
                // var promocode = ;
                mixpanel.track("Checkout_Promo", {
                    // "promo-code": promocode
                });
                break;
            //
            // Fires when users land on the Verify Page in Checkout
            // fireMixpanelEvent('Checkout_Error');
            case 'Checkout_Error':
                mixpanel.track("Checkout_Error");
                break;
            //
            // Fires when users click submit on the verify page
            // fireMixpanelEvent('Click_Submit');
            case 'Click_Submit':
                mixpanel.track("Click_Submit");
                break;
            //
            // Fires when users hit the last page and transaction is successfully processed
            // fireMixpanelEvent('Payment_Success');
            case 'Payment_Success':
                mixpanel.track("Payment_Success");
                break;
        }
    }
    ;

    $(document).ready(function () {
        land();
        updateSize(false);
        updateMaterial(false);
        updateChain(false);
        requestPriceEstimate();

        // $("#materialmenu").change(function(){
        //   var e = document.getElementById("materialmenu");
        //   var mater = e.options[e.selectedIndex].text;
        //   var valer = e.options[e.selectedIndex].value.split(" ");
        //   document.getElementById("materialId").value = valer[0];
        //   document.getElementById("finishId").value = valer[1];
        //   updatePriceEstimate(valer[0],valer[1]);
        //   if (mater == "Gold - 18k Gold" || mater == "Gold - 14k Gold" || mater == "Precious Plated - 18k Gold Plated" || mater == "Precious Plated - 14k Gold Plated" || mater == "Brass - Polished" || mater == "Bronze - Polished") {
        //       $('.gold_polished').addClass('product-image');   // show gold_polished
        //       $('.platinum_polished').removeClass('product-image');
        //       $('.red_gold_polished').removeClass('product-image');
        //   } else if (mater == "Gold - 14k White Gold" || mater == "Silver - Premium" || mater == "Precious Plated - Rhodium Plated") {
        //       $('.platinum_polished').addClass('product-image'); // show platinum_polished
        //       $('.gold_polished').removeClass('product-image');
        //       $('.red_gold_polished').removeClass('product-image'); 
        //   } else {
        //       $('.red_gold_polished').addClass('product-image'); // show red_gold_polished
        //       $('.platinum_polished').removeClass('product-image'); 
        //       $('.gold_polished').removeClass('product-image');
        //   }
        // });


        $("#materialmenu").change(function () {
            // alert("changing materials");
            var selected = $("#materialmenu").find('option:selected');
            // fireMixpanelEvent('Click_Material');
            updateMaterial(true);
            // $('#text').html(selected.text());
            // $('#value').html(selected.val());
            $('.pricee').html(selected.data('price'));
            var numberOfImages = selected.data('size');

            var mat = selected.val().split(" ");
            document.getElementById('materialId').value = mat[0];
            document.getElementById('finishId').value = mat[1];
            for (var i = 0; i < numberOfImages; ++i) {
                $('#img' + i).attr('src', selected.data('f' + i));
            }
            updatePriceEstimate(mat[0], mat[1]);
        });

        $("#size").change(function () {
            updateSize(true);
        });


        $.fn.editable.defaults.mode = 'inline';
        $('#model').editable({
            send: 'never',
            title: 'Enter username',
            placement: 'right',
            toggle: 'manual',
            display: function (value) {
                $('#view').text(value);
                TROVE.renameItem('${item.itemId}', value);
            }
        });

        $('#controller').click(function (e) {
            e.stopPropagation();
            $('#model').editable('toggle');
        });

        $('#model2').editable({
            send: 'never',
            title: 'Enter username',
            placement: 'right',
            toggle: 'manual',
            display: function (value) {
                $('#view2').text(value);
                TROVE.updateItemDescription('${item.itemId}', value);
            }
        });

        $('#controller2').click(function (e) {
            e.stopPropagation();
            $('#model2').editable('toggle');
        });


        var count = parseInt($('.tallyho-cart').html());
        if (count >= 1) {
            $('.tallyho-cart').addClass('showitnow');
        }

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

        $('#example1').fancybox({
            openEffect: 'elastic',
            closeEffect: 'elastic',
            helpers: {
                title: {
                    type: 'inside'
                }
            }
        });

    });

</script>

</body>
</html>
