<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="trove" uri="http://troveup.com/jsp/tlds/itemimage" %>

<html>
<head>
  <title>Trove: Jewelry Made Just For You</title>
  <c:import url="../fragments/headers/commonHead.jsp"/>
  <c:import url="../fragments/headers/legalHead.jsp"/>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/jquery.slick/1.5.9/slick.css">
  <link href='https://fonts.googleapis.com/css?family=Raleway:400,200,300,500,600,700' rel='stylesheet' type='text/css'>
    <link href="https://fonts.googleapis.com/css?family=Vollkorn:400,400italic,700,700italic" rel="stylesheet" type="text/css">
  <link rel="stylesheet" href="/resources/stylesheets/featured.css">
  <c:import url="../fragments/analytics/all.jsp"/>
  <style>

  p .size-and-material{
    font-size: 13px;
  }
    @media (max-width: 500px) {
      .controlpanel {
        width: 100%;
      }

      .container-fluid {
        padding-right: 0px;
        padding-left: 0px;
      }
      .row {
        margin-right: 0px;
        margin-left: 0px;
      }

      .select-options {
        margin: 0 auto;
        top: -12px;
      }
    }

    .bold-element {
      margin-bottom: 15px;
      display: block;
    }

    #mobilepics {
      margin-bottom: 20px;
      margin-top: 20px;
    }
  </style>

</head>
<body>
  <c:import url="../fragments/analytics/adwordstag.jsp"/>
  <c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
  <c:import url="../fragments/nav/topNavBar.jsp"/>

  <div class="container-fluid centered">
    <div class="col-sm-7 col-sm-pull-1 col-md-6 col-md-push-1">
      <div class="img-container">
        <img id="mainimage" width="400" height="400" crossOrigin="anonymous" class="mainimage">
        <img id="staticimage" width="400" height="400" class="mainimage" style="display: none;">
        <canvas id="myCanvas" style="visibility: hidden; display: none;" width="500" height="500"></canvas>
      </div>
      <div id="cameras"><div class="cameraOptions chosen"></div></div>
    </div>
    <div class="col-sm-5 col-sm-pull-1 col-md-4 col-md-push-1">
      <div class="controlpanel">
        <h2 id="item-name"></h2>
        <h3 id="item-price"></h3>
        <div class="thumb-holder"><img class="facepic-thumb" src="${influencer.fullProfileImagePath}"></div>
        <b class="bold-element">Designed by <a href="/jaleesamoses" id="user-creator-name" style="font-weight: 700;text-decoration: underline;"></a></b>

        <div id="mobilepics" class="mobilepics"></div>
        <div id="dropdown-container" class="dropdowns"><p class="errorMessage"></p></div>
        <a id="buttonaddtobag" class="btn btn-blue btn-action">Add to Bag<i class="icon ion-android-arrow-forward"></i></a>
        <p class="item-description" id="item-description"></p>

        <div class="panel">
          <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
            <div class="panel-heading">
              <b class="panel-title">SIZE & MATERIAL<i class="icon ion-plus"></i></b>
            </div>
          </a>
          <div id="collapseOne" class="panel-collapse collapse">
            <div class="panel-body">
              <p class="size-and-material item-description"><b>Materials: </b><br>Each piece is made of .925 sterling silver and plated with either gold, rose gold, or rhodium for durability and shine. A Swarovski crystal on each necklace adds a touch of color and brilliance.<br><br>Our jewelry is made in New York alongside some of the best brands in the world out of sterling silver, gold, and top quality crystals. </p>
              <p class="size-and-material item-description"><b>Dimensions: </b><br>3/4” tall pendant; 18” chain</p>
              <br>
              <!--a href="https://storage.googleapis.com/troveup-imagestore/assets/ring-sizer-final.pdf" target="_blank" class="ui-link"><span style="font-weight: 500; color: #ee2435;text-decoration:underline;">View Size Guide</span></a-->
            </div>
          </div>
        </div>

      </div>
    </div>
  </div>

  <div class="container-fluid">
    <div id="bottom-description-container" class="row" style="background: #f6f6f6; padding: 34px; padding: 60px 30px 50px;">
      <h3 id="secondary-item-description-title" class="" style="font-weight: 400;text-transform: uppercase;
    letter-spacing: 1px;font-family: 'Vollkorn', 'Times New Roman',serif;"></h3>
      <p class="" style="text-align:center;">Designed by <span id="secondary-item-description-user" style="font-style: italic;"></span></p><br>
      <p id="secondary-item-description-text" class="lockup"></p>
    </div>
  </div>
  <c:if test="${not empty relatedItems}">
  <div class="container">
    <div class="row" style="padding: 50px 25px;">
      <h3 class="">CHOSEN JUST FOR YOU</h3>
      <div id="otheritemspics">
        <c:forEach var="item" items="${relatedItems}">
          <a href="/featured/item/${item.simpleItemId}" class="other-item"><img src="${item.troveDisplayImages[0].largeImageUrlPath}" class="img-responsive"></a>
        </c:forEach>
      </div>
    </div>
  </div>
  </c:if>

  <c:import url="../fragments/footers/footer.jsp"/>
  <script type="text/javascript" src="https://cdn.jsdelivr.net/jquery.slick/1.5.9/slick.min.js"></script>
  <script type="text/javascript" src="/resources/js/trove/AJAX.js"></script>
  <script type="text/javascript" src="/resources/js/trove/Facebook.js"></script>
  <script type="text/javascript" src="/resources/js/trove/GAHelper.js"></script>
  <script type="text/javascript" src="/resources/js/trove/Featured.js"></script>
  <script>
    var gaeHelper = new GAHelper();
    var ajaxHelper = new AJAXHelper("${_csrf.token}");
    var addToBagButton = $('#buttonaddtobag');

    function pageAssetInitializationCallback(renderSet) {
      if (++pageInitStateControl.initRenderSetCompletedCount >= pageInitStateControl.initRenderSetTotalCount) {
        initializeSlickCarouselLibrary();
      }
    }

    function initializeSlickCarouselLibrary() {
      mobileCarouselContainerSelect.slick({
        dots: true,
        slidesToShow: 1,
        infinite: false,
        slidesToScroll: 1,
        prevArrow: '#prevButton',
        nextArrow: '#nextButton',
        centerMode: true,
        centerPadding: '0px',
        focusOnSelect: true,
        autoplay: false,
        cssEase: 'ease-in-out'
      });

      bottomCarouselContainerSelect.slick({
        slidesToShow: 4,
        infinite: true,
        slidesToScroll: 1,
        prevArrow: '#prev2Button',
        nextArrow: '#next2Button',
        centerMode: false,
        centerPadding: '0px',
        focusOnSelect: false,
        autoplay: true,
        autoplaySpeed: 2000,
        cssEase: 'ease-in-out',
        responsive: [
          {
            breakpoint: 768,
            settings: {
              arrows: false,
              infinite: true,
              centerMode: true,
              centerPadding: '0px',
              slidesToShow: 3
            }
          },
          {
            breakpoint: 480,
            settings: {
              arrows: false,
              centerMode: false,
              infinite: true,
              centerPadding: '0px',
              slidesToShow: 2
            }
          }
        ]
      });
    }

    function addToBagSuccess() {
      //Page handling for successful add-to-bag here
      addToBagButton.text("Item added!");

      mixpanel.track("featured_add_bag", {
        "influencer": objectData.userCreatorName,
        "item": objectData.itemName
      });

      //JS lib declared in the navbar import code
      navHelper.incrementBagItemCounter();

      //Facebook add event
      eventAddToCart(objectData.simpleItemId.toString(), objectData.itemPrice);

      //GAHelper add event
      gaeHelper.addToBag(objectData.simpleItemId.toString(), objectData.itemName, objectData.itemPrice);

      $('.addtobagpop').fadeIn();
      setTimeout(function(){ $('.addtobagpop').fadeOut('slow'); }, 5000);
    }

    function addToBag() {
      $(".dropdown").each(function() {
        if ( $(this).attr('data-user-selected') == 'true' ) {

        } else {
          $(this).addClass('forgot');
          return;
        }
      });

      if ( $(".forgot").length ) {
        return;
      } else {
        var generateDisplayImageCallback = function () {
          var selectedOptions = JSON.stringify(getDropdownsWithSelectedOptions());
          var dataObject = ajaxHelper.createOrAppendPostDataObject("selectedOptions", selectedOptions);
          dataObject = ajaxHelper.createOrAppendPostDataObject("referenceItemId", objectData.simpleItemId, dataObject);
          dataObject = ajaxHelper.createOrAppendPostDataObject("displayImage", dummyImg.attr('src'), dataObject);
          ajaxHelper.performPostRequest(dataObject, "/addsimpleitemtobag", addToBagSuccess, ajaxHelper.createGenericFailureCallback('#errorMessage'))
        };
        addToBagButton.text("Adding to Bag...");
        var pertinentAssets = retrievePertinentAssets(0);
        var dummyImg = $('<img>');

        if (pertinentAssets.length > 0) {
          mergeImagesAndSetImgSrcAttribute(pertinentAssets, dummyImg, currentRenderSet + 500, generateDisplayImageCallback);
        } else {
          dummyImg.attr('src', $('#mainimage').attr('src'));
          generateDisplayImageCallback();
        }

      }
    }

    function processBottomDescriptionText() {
      var bottomDescriptionContainer = $('#bottom-description-container');

      var textSplit = objectData.bottomDescriptionText.split("\n");

      for (var i = 0; i < textSplit.length; ++i)
      {
        $('<p></p><br>')
            .addClass('lockup')
            .html(textSplit[i])
            .appendTo(bottomDescriptionContainer);
      }
    }

    $(document).ready(function () {
      objectData = JSON.parse('${itemAttributes}');
      $('#item-name').text(objectData.itemName);
      $('#user-creator-name').text($('#user-creator-name').text() + objectData.userCreatorName);
      <c:choose>
      <c:when test="${not empty storefrontUrl}">
      $('#user-creator-name').attr('href', '${storefrontUrl}');
      </c:when>
      <c:otherwise>
      $('#user-creator-name').attr('href', '/${influencer.username}');
      </c:otherwise>
      </c:choose>
      $('#item-description').text(objectData.itemDescription);
      $('#item-price').text('$' + objectData.itemPrice);
      $('#secondary-item-description-title').text(objectData.bottomDescriptionTitle);
      $('#secondary-item-description-user').text(objectData.userCreatorName);
      processBottomDescriptionText($('#bottom-description-container'));

      //Set the main image source from SimpleItem
      mainImgSelect.attr('src', objectData.primaryDisplayImageUrl);

      //Populate the dropdowns and image assets
      $.each(objectData.simpleItemControls, function () {
        if (!this.hidden) {
          generateDropdown(this).appendTo(dropdownsContainer);
        }
      });

      //Updates the camera counter to reflect the number of cameras possible for the screen
      updateCameraMaxCountFromCurrentAssets();

      //Create the camera tags for assets to be populated
      initCameraTags();

      //Initialize any additional static assets
      initStaticAssets();

      //Update the on-page assets
      updatePageAssets(true);

      addToBagButton.on('click', addToBag);

      //Track the viewing of this page with Facebook
      eventViewContent(objectData.simpleItemId.toString(), objectData.itemPrice);

      //Track the viewing of this page with Google Analytics
      gaeHelper.itemView(objectData.simpleItemId.toString(), objectData.itemName, objectData.itemPrice);

      $(document).click(function (e) {
        //Hide dropdowns if anywhere else on the page is clicked
        $('.select-options').css('display', 'none');
      });

      $("section").find("[active-option='true']").each(function() {
        $(this).click();
        $(document).click();
      });


    });



  </script>

</body>