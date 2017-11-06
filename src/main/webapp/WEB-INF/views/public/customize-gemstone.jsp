<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="trove" uri="http://troveup.com/jsp/tlds/itemimage" %>

<html>
<head>
    <c:import url="../fragments/headers/commonHead.jsp"/>
    <%--<c:import url="../fragments/headers/legalHead.jsp"/>--%>
    <%--<link rel="stylesheet" href="/resources/stylesheets/theme-custom.css" rel="stylesheet">--%>

    <script>
        // allow jquery mobile to ignore restyling elements with 'data-enchance' attribute set to false
        $(document).on("mobileinit", function () {
            // $.mobile.ignoreContentEnabled=true;
            $.mobile.ajaxEnabled = false;
            $.mobile.linkBindingEnabled = false;
        });

        $(document).bind("mobileinit", function () {
            $.mobile.page.prototype.options.keepNative = "a, button, input.slideinput, ul, li, input.form-control";
            // $.extend(  $.mobile , {
            //   autoInitializePage: false
            // });
        });
    </script>

    <%--TODO:  evaluate which of these includes are still necessary, already removed those explicitly in commonHead --%>
    <link href="/resources/stylesheets/fancybox.css" rel="stylesheet">
    <link href="/resources/stylesheets/sliders/base.css" rel="stylesheet">
    <link href="/resources/stylesheets/main.css" rel="stylesheet">
    <link href="/resources/stylesheets/trovecustomizer.css" rel="stylesheet">
    <script type="text/javascript" src="/resources/js/FORGEv2.js"></script>
    <script type="text/javascript" src="/resources/js/vendor/fancybox.js"></script>
    <script type="text/javascript" src="/resources/js/trove/AJAX.js"></script>
    <c:import url="../fragments/modals/addressModal.jsp"/>
    <c:import url="../fragments/cust/canvasParams.jsp"/>
    <c:import url="../fragments/analytics/all.jsp"/>
    <c:if test="${not isAuthenticated}"><link rel="stylesheet" href="/resources/stylesheets/authmod.css"></c:if>
    <link rel="stylesheet" href="/resources/stylesheets/browse.css">
  
    <script>
        var ajaxHelper = new AJAXHelper("${_csrf.token}");
        <%-- TROVE.setToken("${_csrf.token}"); --%>
    </script>

    <style type="text/css">
      .spinstuff {
        opacity: 0.0;
        -webkit-transition: 1s opacity; 
                transition: 1s opacity;
        pointer-events: none; 
      }
      @media screen and (max-width: 767px) {
        .noUi-horizontal .noUi-handle {
          width: 50px;
          height: 50px;
          left: -20px;
          top: -22px;
        }
      }

      .ui-mobile label, .ui-controlgroup-label {
        background: #FFF;
      }


      /* ? port to forge ? */
      .panelRoot .stepHeader .panelSubHeader {
        margin: 3px 0px 0px 0px;
      }
      #saveButton span {
        margin-top: 0px;
      }
      h1 {
        color: #2e2626;
      }
      @media (max-width: 767px) {
        a span.topnav-text {
          font-size: 10px;
          letter-spacing: 1px;
          text-transform: uppercase;
          position: relative;
          top: 5px;
          text-decoration: none !important;
        }
        #actions #saveButton {
          right: 2px;
          top: 60px;
          border-right: 0px solid #DEDEDE;
        }
        #actions #homeButton {
          top: 5px;
        }
        .box.cart.special {
          right: 25px;
        }

        .noUi-horizontal {
          top: -8px;
        }
      }

      #actions {
        margin-bottom: 50px;
      }

      #actions #saveButton {
        border-right: 0px solid #DEDEDE;
      }
      .selectButton.silver {
        background: url(https://storage.googleapis.com/troveup-imagestore/assets/img/material-swatch-silver.png);
        background-size: contain;
      }
      .selectButton.gold {
        background: url(https://storage.googleapis.com/troveup-imagestore/assets/img/material-swatch-gold.png);
        background-size: contain;
      }
      .selectButton.rosegold {
        background: url(https://storage.googleapis.com/troveup-imagestore/assets/img/material-swatch-rose.png);
        background-size: contain;
      }
      body {
        overflow: scroll;
      }
    </style>
  </head>
  <body class="custom">
    <div class="spinstuff show">
        <span class="spinner page-fade-spinner"></span>
    </div>

    <div data-role="page" class="ui-page ui-page-active">
      <c:import url="../fragments/nav/topNavBar.jsp"/>
      <!--BEGIN AUTH MODAL-->
      <c:import url="../fragments/modals/authModal.jsp"/>
      <!--END AUTH MODAL-->
      <input id="csrfToken" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <div id="customizer-container"></div>
    </div>


    <c:if test="${not isAuthenticated}">
    <script>
        $("#button_addToTrove").click(function () {
            mixpanel.track("customizer_trove_ANON", {
                "itemName": "${item.itemName}"
            });
        });
        $("#button_addToBag").click(function () {
            mixpanel.track("customizer_buy_ANON", {
                "itemName": "${item.itemName}"
            });
        });
        $("#button_addToTroveMob").click(function () {
            mixpanel.track("customizer_trove_ANON", {
                "itemName": "${item.itemName}"
            });
        });
        $("#button_addToBagMob").click(function () {
            mixpanel.track("customizer_buy_ANON", {
                "itemName": "${item.itemName}"
            });
        });
    </script>
    </c:if>
    <c:if test="${isAuthenticated}">
    <script>
        $("#button_addToTrove").click(function () {
            mixpanel.track("customizer_trove", {
                "itemName": "${item.itemName}"
            });
        });
        $("#button_addToBag").click(function () {
            mixpanel.track("customizer_buy", {
                "itemName": "${item.itemName}"
            });
        });
        $("#button_addToTroveMob").click(function () {
            mixpanel.track("customizer_trove", {
                "itemName": "${item.itemName}"
            });
        });
        $("#button_addToBagMob").click(function () {
            mixpanel.track("customizer_buy", {
                "itemName": "${item.itemName}"
            });
        });
    </script>

    </c:if>
    <script>

        TROVE.setToken("${_csrf.token}");
        UI.setCSRFToken("${_csrf.token}");

        function attemptToParse(cssSelector) {
            $elem = $(cssSelector);
            if (!$elem.length) {
                return null;
            }

            var contents = null;
            try {
                var contentString = $elem.html();
                contents = JSON.parse(contentString);
            } catch (e) {
                console.warn("Error parsing json for element ", cssSelector, ": ", e);
            }
            return contents;
        }

        function toggleSpinner() {
            var sStuff = document.querySelector(".spinstuff");
            sStuff.classList.toggle("show");
        }

        function invokeMixpanel(eventName, eventData) {
            eventData = eventData || {};
            eventData.itemId = modelParentID;
            eventData.modelFilename = filename;

            mixpanel.track(eventName, eventData);
        }

        var initialParameters = attemptToParse("#initialParameters");
        var materialOptions = attemptToParse("#materialOptions");

        var chainData = attemptToParse("#chainOptions");
        chainOptions = null;
        if (chainData) {
            chainOptions = chainData.map(function(datum){
                return { label: datum.name, value: datum.chainId };
            });
        }

        var sizeHash = attemptToParse("#sizeOptions");
        var sizeOptions = [];
        Object.keys(sizeHash).map(function(sizeParam) {
            sizeOptions.push({
                label: sizeHash[sizeParam],
                value: sizeParam
            });
        });

        var filename = $("#modelFilename").html();
        var storageRoot = $("#modelPath").html();
        var modelParentID = $("#modelParentID").html();
        var displayName = $("#itemDisplayName").html();

        UI.start({
            activeFilename: filename,
            storageRoot: storageRoot, 
            modelParentID: modelParentID,
            displayName: displayName,
            itemHash: initialParameters,
            containerElement: document.getElementById("customizer-container"),
            sizeOptions: sizeOptions,
            chainOptions: chainOptions,
            materialOptions: materialOptions,
            backLink: '/onboard/choose',
            showPrice: true
        });

        UI.driver.registerListener('controlsRendered', function() {
            toggleSpinner();

            UI.registerSlideHandler(function(event, slick, currentSlide, stepList) {
                var reachedStep = stepList[currentSlide];
                var humanIndex = currentSlide+1;
                invokeMixpanel("customizer_reached_step_"+currentSlide, {
                    "stepType": reachedStep.trackingType,
                    "stepLabel": reachedStep.rootSection.sectionName
                });
            }, true);

            $("#addButton").click(function () {
              invokeMixpanel('customizer_buy');
            });

            $("#saveButton").click(function () {
              invokeMixpanel('customizer_save');
            });

            $("#resetButton").click(function () {
              invokeMixpanel('customizer_reset');
            });

            $("#homeButton").click(function () {
              invokeMixpanel('customizer_home');
            });

            $(".sizehelp").click(function () {
              invokeMixpanel('customizer_sizehelp');
            });

            var firstClick = new Date().getTime();
            $('#forgecanvas').mousedown(function(e) {
                firstClick = new Date().getTime();
            });

            $('#forgecanvas').mouseup(function(e) {
                var now, duration;
                now = new Date().getTime();
                duration = now - firstClick;
                invokeMixpanel("customizer_rotate", {
                    "duration": duration
                });
            });

            // engage slider
            $('.sliderWrapper').click(function (e) {
                invokeMixpanel("customizer_engage_slider");
            });


            $('.size_param select').change(function(){
                var currOpt = this.options[this.selectedIndex];
                invokeMixpanel("customizer_edit_size", {
                    "size": currOpt.value
                });
            });

            $('.material_render_select .selectButton').click(function(){
                var renderKey = this.getAttribute('data-key');
                invokeMixpanel("customizer_edit_material", {
                    "material": renderKey
                });
            });
        });

        function addPersistenceParameters(postData, scene) {

            UI.packExportDataFTUE(postData);

            var parentModelId = scene.cfg.modelParentID;
            var modelName = $(".displayText.itemName").first().text();
            var modelDescription = "hardcoded description";
            var isPrivate = true; // not really used anymore, remove param when endpoint is updated
            var modelExtension = '.json';
            var imageExtension = '.png';
            UI.appendImageData(postData);

            // materials and finishes defined on troveweb in file ShapewaysPriceApi.java
            var modelMaterial = "3"; // "Precious Plated" material
            var modelMaterialFinish = scene.shapewaysFinish;

            ajaxHelper.createOrAppendPostDataObject("parentModelId", parentModelId, postData);
            ajaxHelper.createOrAppendPostDataObject("shouldAddToCart", true, postData);
            ajaxHelper.createOrAppendPostDataObject("modelName", modelName, postData);
            ajaxHelper.createOrAppendPostDataObject("modelDescription", modelDescription, postData);
            ajaxHelper.createOrAppendPostDataObject("isPrivate", isPrivate, postData);
            ajaxHelper.createOrAppendPostDataObject("modelExtension", modelExtension, postData);
            ajaxHelper.createOrAppendPostDataObject("imageExtension", imageExtension, postData);
            ajaxHelper.createOrAppendPostDataObject("modelMaterial", modelMaterial, postData);
            ajaxHelper.createOrAppendPostDataObject("modelMaterialFinish", modelMaterialFinish, postData);

            // console.warn("add engraving data to export"); // will fix this later
            //ajaxHelper.createOrAppendPostDataObject(value = "engraveText", required = false) final String engraveText,

            // TODO: optional arguments
            //ajaxHelper.createOrAppendPostDataObject(value = "addToCollectionId", required = false) final Long addToCollectionId,
            //ajaxHelper.createOrAppendPostDataObject(value = "defaultCollectionId", required = false) final Long defaultCollectionId,
        }

        function saveModel(driver, scene) {

          if (!authHelper.getIsAuthenticated())
          {
            authHelper.setActionToPerformPostAuth(function() {$('#saveButton').click();});
            authModal.modal('show');
          } else {
            var saveButtonLabel = $("#saveButton span");
            saveButtonLabel.text("SAVING...");

            var postData = ajaxHelper.createOrAppendPostDataObject("shouldAddToCart", false);
            addPersistenceParameters(postData, scene);

            ajaxHelper.performPostRequest(postData, "/savenewmodel", saveModelSuccess, ajaxHelper.createGenericFailureCallback('#errorMessage'))
          }
        }

        function addToBag(driver, scene) {
            var addToBagButton = $('#addButton');
            addToBagButton.text("Adding to Bag...");

            var postData = ajaxHelper.createOrAppendPostDataObject("shouldAddToCart", true);
            addPersistenceParameters(postData, scene);

            ajaxHelper.performPostRequest(postData, "/savenewmodel", addToBagSuccess, ajaxHelper.createGenericFailureCallback("#errorMessage"));
        }

        function saveModelSuccess() {
            var saveButtonLabel = $("#saveButton span");
            saveButtonLabel.text("Saved");
        }

        function addToBagSuccess() {
            var addToBagButton = $('#addButton'); // this line is redundant  but helpful for testing purposes

            //Page handling for successful add-to-bag here
            addToBagButton.text("Item added!");

            //JS lib declared in the navbar import code
            navHelper.incrementBagItemCounter();

            $('.addtobagpop').fadeIn();
            setTimeout(function(){ $('.addtobagpop').fadeOut('slow'); }, 5000);
        }

        UI.driver.registerListener('buttonPress_addToBag', addToBag);
        UI.driver.registerListener('buttonPress_saveModel', saveModel);

        function sendPriceRequest(driver, scene) {
            var volume = scene.getModelVolume();

            var submission = new FormData();
            submission.append("parentItemId", parseInt(scene.getConfig("modelParentID"), 10));
            submission.append("volume", parseFloat(volume));
            submission.append("physicalMaterialId", scene.activeFinishID);
        
            jQuery.ajax({
                url: '/newcustomizerpriceestimate',
                data: submission,
                contentType: false,
                processData: false,
                headers: {
                    'Access-Control-Allow-Origin': '*',
                    "X-CSRF-TOKEN": $("#csrfToken").val()
                },
                type: 'POST',
                success: function(data) {
                    UI.updatePricePreview(data.estimate);
                },
                error: function(xhr, errorString) {
                    console.log("FORGE Error:", errorString);
                }
            });
        }

        UI.driver.registerListener('materialUpdate', sendPriceRequest);
        UI.driver.registerListener('itemUpdatePostDelay', sendPriceRequest);
    </script>
    <script id="initialParameters" type="application/json">
    ${customizerInput}
    </script>

    <script>
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

        $(document).ready(function () {
            if (isIphone5()) {
                $('body').css("overflow", "hidden");
                $('#main').css("margin-top", "-100px");
            }
        });

    </script>
</body>
</html>

