<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="trove" uri="http://troveup.com/jsp/tlds/itemimage" %>

<html>
<head>

  <c:import url="../fragments/headers/commonHead.jsp"/>
  <link href="/resources/stylesheets/fancybox.css" rel="stylesheet">
  <link href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">

  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=0">
  <link rel="icon" href="/resources/img/favicon.png?v=2">
  <link href="/resources/vendor/nouislider/nouislider.min.css" rel="stylesheet">
  <script type="text/javascript" src="/resources/vendor/nouislider/nouislider.min.js"></script>
  <script type="text/javascript" src="https://code.jquery.com/jquery-1.11.3.min.js"></script>
  <link href="https://fonts.googleapis.com/css?family=Raleway:400,200,300,500,600,700" rel="stylesheet" type="text/css">
  <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
  <script type="text/javascript" src="/resources/js/vendor/head.js"></script>
  <link href="//cdnjs.cloudflare.com/ajax/libs/x-editable/1.5.0/bootstrap3-editable/css/bootstrap-editable.css"
        rel="stylesheet"/>
  <script
      src="//cdnjs.cloudflare.com/ajax/libs/x-editable/1.5.0/bootstrap3-editable/js/bootstrap-editable.min.js"></script>
  <script type="text/javascript" src="/resources/js/namespace.js"></script>
  <script type="text/javascript" src="/resources/js/TROVE.js"></script>
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.3/handlebars.min.js"></script>
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
  <script type="text/javascript" src="/resources/js/FORGEv2.js"></script>
  <script type="text/javascript" src="/resources/js/TROVE.js"></script>
  <script type="text/javascript" src="/resources/js/vendor/fancybox.js"></script>
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.13.1/jquery.validate.js"></script>
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.13.1/additional-methods.js"></script>
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>
  <c:import url="../fragments/analytics/all.jsp"/>
  <script>TROVE.setToken("${_csrf.token}");</script>

  <!-- WARNING: copied nav from src/main/webapp/WEB-INF/views/ftui/choose.jsp -->
  
  <style>
/* start main.css */
a {
    color: #232323;
}
/* end main.css */

@media screen and (max-width: 767px) {
    /* is this one necessary? */
    .modal-dialog {
        margin: 0;
    }
    .modal-footer #addressSubmit {
        font-size: 14px;
    }
    h4.modalHeadline {
        white-space: normal;
        margin: -55px 0 10px;
    }
    select.form-control {
        height: 40px;
    }
    .form-stuff select {
        align: center;
    }
    #shippingStateId {
        margin-left: 0;
    }
}
.modal-header .closeModal {
    position: absolute;
    font-size: 24px;
    padding: 5px;
    right: 8px;
    top: 8px;
    font-family: 'Ionicons';
}

/* start old choose page */
.modal-body {
    padding-bottom: 9px;
}
/* end old choose page */


/* start cards.css */
.modal-header .closeModal i {
    color: rgba(0, 0, 0, 0.3);
}
/* end cards.css */

.modal-header {
    border-bottom: 0px solid #dedede;
    padding: 20px;
}
.modal-content {
    padding: 35px;
}

.formstuff {
    opacity: 0.0;
}

.formstuff.show {
    opacity: 1.0;
}
h4.modalHeadline {
    font-size: 17px;
    line-height: 1.25em;
    margin-top: -55px;
    max-width: 500px;
    display: inline-block;
}
#addressSubmit {
    width: 100%;
    margin-left: -15px;
    margin-top: 10px;
    height: 50px;
    background: #DD2435;
    border: 1px solid #DD2435;
    color: #FFF;
    letter-spacing: 0.15em;
    font-size: 16px;
    -webkit-font-smoothing: antialiased;
    font-weight: 600;
}

#addressSubmit:hover {
    background: #F26868;
    border: 1px solid #F26868;
}

#addressSubmit.disabled {
    pointer-events: none;
    background: #CCC;
    color: #666;
    border: 1px solid #CCC;
    cursor: not-allowed;
}
.labyo {
    text-align: left;
    font-size: 12px;
}
input.form-control {
    border-radius: 4px;
    border: 1px solid #DEDEDE;
    margin-bottom: 6px;
}




      .ftui-nav {
        z-index: 999;
        background: rgb(225, 228, 233);
        border-bottom: 1px solid rgb(208, 211, 216);
        height: 60px;
        width: 100%;
        text-align: center;
        position: fixed;
        top: 0px;
        left: 0px;
        right: 0px;
      }

      .steps {
        height: 40px;
        line-height: 16px;
        padding: 0px;
        margin-top: 10px;
        color: #FFF;
        display: none;
        text-align: center;
        width: auto;
      }

      .step {
        display: inline-block;
        font-size: 13px;
        min-width: 180px;
        font-weight: 500;
        color: #656565;
        letter-spacing: 0.06em;
      }

      .step span {
        font-size: 25px;
        color: #FFF;
      }

      .step.active {
        color: #F26968;
      }

      .step.active span {
        color: #F26968;
      }
      .gemIcon {
        width: 30px;
      }
      .currentStep {
        z-index: 1000;
        position: fixed;
        top: 12px;
        right: 10px;
        padding: 8px 20px;
        background: transparent;
        color: #656565;
        letter-spacing: 0.15em;
        font-size: 15px;
        -webkit-font-smoothing: antialiased;
        font-weight: 500;
        -webkit-transition: .3s all;
                transition: .3s all;
        display: none;
        font-family: Helvetica, Arial, sans-serif;
      }

      @media screen and (min-width: 768px) {
        .steps { 
          display: inline-block;
        }
      }

      @media screen and (max-width: 767px) {
        .currentStep {
          display: inline-block;
        }
        .ftui-nav, .currentStep {
          display: none;
        }
        .ui-page.ui-page-active {
          margin-top: 0px;
        }
      }

      .ui-page {
          margin-top: 100px;
      }

      .spinstuff {
        opacity: 0.0;
        -webkit-transition: 1s opacity; 
                transition: 1s opacity;
        pointer-events: none; 
      }
      .choosestuff {
        opacity: 0.0;
        -webkit-transition: 1s opacity; 
                transition: 1s opacity; 
      }

      .choosestuff.show {
        display: block;
        opacity: 1.0;
      }

      .spinstuff.show {
        display: block;
        opacity: 1.0;
      }

  </style>
   
</head>
<body class="custom">

<div class="ftui-nav">
  <div class="steps">
    <div class="step"><span><img class="gemIcon" src="https://storage.googleapis.com/troveup-imagestore/assets/sectioned-gem-red.svg"></span><br>Step 1: Choose</div>
    <div class="step active"><span><img class="gemIcon" src="https://storage.googleapis.com/troveup-imagestore/assets/sectioned-gem-red.svg"></span><br>Step 2: Customize</div>
    <div class="step"><span><img class="gemIcon" src="https://storage.googleapis.com/troveup-imagestore/assets/sectioned-gem-white.svg"></span><br>Step 3: Free Jewelry</div>
  </div>
</div>
<div class="currentStep">Step 2 of 3</div>
<div class="spinstuff show">
    <span class="spinner page-fade-spinner"></span>
</div>
<div data-role="page" class="ui-page ui-page-active choosestuff">

  <c:import url="../fragments/modals/addressModal.jsp"/>
  <c:import url="../fragments/cust/canvasParams.jsp"/>
  <input class="ignore" id="csrfToken" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

  <div id="customizer-container"></div>

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
        var cStuff = document.querySelector(".choosestuff");
        sStuff.classList.toggle("show");
        cStuff.classList.toggle("show");
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
        backLink: '/onboard/choose'
    });

    UI.driver.registerListener('controlsRendered', function() {
        toggleSpinner();
        var formSpinner = document.querySelector(".modal-body .spinstuff");
        formSpinner.style.display = 'none';

        UI.registerSlideHandler(function(event, slick, currentSlide, stepList) {
            var reachedStep = stepList[currentSlide];
            var humanIndex = currentSlide+1;
            invokeMixpanel("ftue_customizer_reached_step_"+currentSlide, {
                "stepType": reachedStep.trackingType,
                "stepLabel": reachedStep.rootSection.sectionName
            });
        }, true);

        $("#button_addToBag").click(function () {
            invokeMixpanel('ftue_customizer_buy');
        });

        var firstClick = new Date().getTime();
        $('#forgecanvas').mousedown(function(e) {
            firstClick = new Date().getTime();
        });

        $('#forgecanvas').mouseup(function(e) {
            var now, duration;
            now = new Date().getTime();
            duration = now - firstClick;
            invokeMixpanel("ftue_customizer_rotate", {
                "duration": duration
            });
        });

        // engage slider
        $('.sliderWrapper').click(function (e) {
            invokeMixpanel("ftue_customizer_engage_slider");
        });


        $('.size_param select').change(function(){
            var currOpt = this.options[this.selectedIndex];
            invokeMixpanel("ftue_customizer_edit_size", {
                "size": currOpt.value
            });
        });

        $('.material_render_select .selectButton').click(function(){
            var renderKey = this.getAttribute('data-key');
            invokeMixpanel("ftue_customizer_edit_material", {
                "material": renderKey
            });
        });
    });

    UI.driver.registerListener('buttonPress_addToBag', triggerAddressRequest);

    UI.driver.registerListener('itemUpdatePostDelay', function(driver, scene) {
        var volume = scene.getModelVolume();

        var submission = new FormData();
        submission.append("parentItemId", parseInt(scene.getConfig("modelParentID"), 10));
        submission.append("volume", parseFloat(volume));
        submission.append("physicalMaterialId", scene.activePhysicalMaterial);

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
    });

</script>
</body>
</html>

