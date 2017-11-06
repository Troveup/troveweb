<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Trove: Welcome to Trove</title>
  <c:import url="../fragments/headers/commonHead.jsp"/>
  <c:import url="../fragments/headers/legalHead.jsp"/>
  <c:import url="../fragments/analytics/all.jsp"/>
  <link rel="stylesheet" href="/resources/stylesheets/featured.css">
  <link rel="stylesheet" href="/resources/stylesheets/spinner.css">
  <style>
    .ftui-nav {
      z-index: 999;
      background: rgb(225, 228, 233);
      border-bottom: 1px solid rgb(208, 211, 216);
      height: 60px;
      width: 100%;
      text-align: center;
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
    }

    .container-fluid {
      padding-top: 30px;
    }

    .input-section {
      max-width: 500px;
      margin: auto;
      margin-top: 30px;
      text-align: center;
    }

    button.remove-asset-button {
      display: inline-block;
      margin-top: 10px;
      margin-bottom: 10px;
    }

    input.extra-asset-input {
      display: inline-block;
    }

    button.dropdown-button {
      display: inline-block;
      margin: 10px;
    }

    img.select-value-asset-img {
      height: 30px;
      width: 30px;
      border-radius: 2em;
      border: 2px solid #787878;
      margin: auto;
    }

    .bottom-row {
      padding-bottom: 30px;
      padding-top: 30px;
      text-align: center;
    }

    button.progression-button {
      padding: 10px;
    }
  </style>
</head>

<body>
<c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
<c:import url="../fragments/nav/topNavBar.jsp"/>
<div class="container-fluid">
  <div class="ftui-nav">
    <div class="steps">
      <div id="progress-step-1" class="step active"><span><img id="progress-step-1-img" class="gemIcon"
                                                               src="https://storage.googleapis.com/troveup-imagestore/assets/sectioned-gem-red.svg"></span><br>Step
        1: General Info
      </div>
      <div id="progress-step-2" class="step"><span><img id="progress-step-2-img" class="gemIcon"
                                                        src="https://storage.googleapis.com/troveup-imagestore/assets/sectioned-gem-white.svg"></span><br>Step
        2: Dropdowns
      </div>
      <div id="progress-step-3" class="step"><span><img id="progress-step-3-img" class="gemIcon"
                                                        src="https://storage.googleapis.com/troveup-imagestore/assets/sectioned-gem-white.svg"></span><br>Step
        3: Preview
      </div>
    </div>
  </div>
  <div id="step-one-container">
    <div style="text-align: center;">
      <h2>General Info</h2>
    </div>
    <div class="row">
      <div class="col-md-6">
        <div class="well input-section">
          <label for="camera-angle-count">Number of Camera Angles</label><br>
          <select onchange="updateCameraAssetInputs();" id="camera-angle-count">
            <c:forEach var="i" begin="1" end="10">
              <option value="${i}">${i}</option>
            </c:forEach>
          </select>
        </div>
      </div>
      <div class="col-md-6">
        <div class="well input-section">
          <label for="item-name">Item Name</label><br>
          <input type="text" style="min-width: 300px;" id="item-name">
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-md-6">
        <div class="well input-section">
          <label for="item-description">Item Description (Top of Page)</label><br>
          <textarea style="min-width: 300px;" id="item-description"></textarea>
        </div>
      </div>
      <div class="col-md-6">
        <div class="well input-section">
          <label for="secondary-item-description-title">Bottom Page Description Title</label><br>
          <textarea style="min-width: 300px;" id="secondary-item-description-title"></textarea>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-md-6">
        <div class="well input-section">
          <label for="secondary-item-description-text">Bottom Page Description Text</label><br>
          <textarea style="min-width: 300px;" id="secondary-item-description-text"></textarea>
        </div>
      </div>
      <div class="col-md-6">
        <div class="well input-section">
          <label for="default-image">Default Item Image</label><br>
          <input type="file" name="default-image" id="default-image" class="form-control"
                 placeholder="c:\IMAGEFILELOCATION"/>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-md-6">
        <div class="well input-section">
          <label for="alt-hover-photo">Alternative Hover Photo</label><br>
          <input type="file" name="alt-hover-photo" id="alt-hover-photo" class="form-control"
                 placeholder="c:\IMAGEFILELOCATION">
        </div>
      </div>
      <div class="col-md-6">
        <div class="well input-section">
          <label for="item-price">Item Price</label><br>
          $<input id="item-price" type="number" value="0.00" min="0.00" step="0.01">
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-md-6">
        <div class="well input-section">
          <label for="influencer-owner">Influencer Owner (will populate this item on their page, otherwise goes to
            Ready to Wear flow)</label><br>
          <select id="influencer-owner">
            <option value="none">None</option>
            <c:forEach items="${influencers}" var="influencer">
              <option data-profile-username="${influencer.username}"
                      data-profile-img="${influencer.profileImageThumbnailPath}"
                      value="${influencer.userId}">${influencer.firstName}</option>
            </c:forEach>
          </select>
        </div>
      </div>
      <div class="col-md-6">
        <div class="well input-section">
          <input id="active-immediately" type="checkbox"><label for="active-immediately">Active Immediately (will go
          live on
          submission)</label>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-md-6">
        <div class="well input-section">
          <div id="extra-assets">
            <div>Extra Assets (Live Photos)</div>
            <br>
            <div id="extra-assets-input-container"></div>
            <button id="new-extra-asset-input-button" onclick="addNewAssetInput();">New Asset</button>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-md-6"></div>
      <div class="col-md-6 bottom-row">
        <button id="step-two-button" class="progression-button" onclick="showStepTwo();">Next Step ></button>
      </div>
    </div>
  </div>
  <div style="display: none;" id="step-two-container">
    <div style="text-align: center;">
      <h2>Add Dropdowns</h2>
    </div>
    <div id="dropdown-editing-container" class="dropdowns">
    </div>
    <div class="row">
      <div class="col-md-6">
        <div style="float: right; padding-right: 10px;" id="template-dropdown">
          <label style="margin-top: 0px;" for="template-select">From Template</label>
          <select id="template-select">
            <option value="material">Material</option>
            <option value="crystal">Crystal</option>
          </select>
          <button onclick="createFromTemplate();" id="template-select-create-button">Create from Template</button>
        </div>
      </div>
      <div class="col-md-6">
        <button id="create-new-dropdown-button" onclick="createBareDropdown();">Create New</button>
      </div>
    </div>
    <div class="row bottom-row">
      <div style="padding-left: 20px; text-align: center;" class="col-md-6">
        <button class="progression-button" onclick="showStepOne();">< Back</button>
      </div>
      <div class="col-md-6">
        <button class="progression-button" onclick="showStepThree();">Preview ></button>
      </div>
    </div>
  </div>
  <div style="display: none;" id="step-three-container">
    <div class="container-fluid centered">
      <div class="col-sm-7 col-sm-pull-1 col-md-6 col-md-push-1">
        <div class="img-container">
          <img id="mainimage" width="400" height="400" crossOrigin="anonymous" class="mainimage">
          <img id="staticimage" width="400" height="400" class="mainimage" style="display: none;">
          <canvas id="myCanvas" style="visibility: hidden; display: none;" width="500" height="500"></canvas>
        </div>
        <div id="cameras">
          <div class="cameraOptions chosen"></div>
        </div>
      </div>
      <div class="col-sm-5 col-sm-pull-1 col-md-4 col-md-push-1">
        <div class="controlpanel">
          <h2 id="display-item-name"></h2>
          <h3 id="display-item-price"></h3>
          <div class="thumb-holder"><img id="owner-profile-img" class="facepic-thumb" src=""></div>
          <b>Designed by <a href="/featured/christendominique" id="user-creator-name"
                            style="font-weight: 700;text-decoration: underline;"></a></b>
          <p class="item-description" id="display-item-description"></p>
          <div id="step-three-dropdowns-container" class="dropdowns"><p class="errorMessage"></p></div>
          <a id="buttonaddtobag" class="btn btn-blue btn-action">Add to Bag<i
              class="icon ion-android-arrow-forward"></i></a>
          <div class="panel">
            <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
              <div class="panel-heading">
                <b class="panel-title">SIZE & MATERIAL<i class="icon ion-plus"></i></b>
              </div>
            </a>
            <div id="collapseOne" class="panel-collapse collapse">
              <div class="panel-body">
                <p><b>Material: </b> 14k gold plated metal, 14k rose gold plated metal, silver plated metal</p>
                <p><b>Dimensions: </b> 3" Long</p>
                <br>
                <a href="#sizinghelp" class="ui-link"><span
                    style="font-weight: 500; color: #ee2435;text-decoration:underline;">View Size Guide</span></a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="container-fluid">
      <div class="row" id="bottom-description-container"
           style="text-align: center;background: #DEDEDE; padding: 34px; padding: 60px 30px 50px;">
        <h3 id="display-secondary-item-description-title" style="font-weight: 500;">THE PROFESSIONAL DREAMER
          COLLECTION</h3>
        <p>Designed by <span id="display-secondary-item-description-user"
                             style="font-style: italic;">Christen Dominique</span></p><br><br>
      </div>
    </div>

    <div class="row bottom-row">
      <div class="col-md-6">
        <button class="progression-button" onclick="showStepTwo();">< Back</button>
      </div>
      <div class="col-md-6">
        <button id="finalize-button" style="width: 80px; height: 36px;" class="progression-button"
                onclick="uploadItem();"><span id="finalize-button-text">Finalize</span><span
            id="finalize-button-spinner" class="button-spinner centered"></span></button>
      </div>
    </div>
  </div>
</div>
<script type="text/javascript" src="/resources/js/trove/Featured.js"></script>
<script type="text/javascript" src="/resources/js/trove/AJAX.js"></script>
<script type="text/javascript" src="/resources/js/trove/ImageInput.js"></script>
<script>
  var imageInputHelper = new ImageInputHelper();
  var ajaxHelper = new AJAXHelper("${_csrf.token}");
  var stepThreeBareHtml;
  var extraAssetsInputCounter = 0;
  var extraAssetsInputContainer = $('#extra-assets-input-container');

  var stepOneContainer = $("#step-one-container");
  var stepTwoContainer = $('#step-two-container');
  var stepThreeContainer = $('#step-three-container');
  var progressBarStep1 = $('#progress-step-1');
  var progressBarStep1Image = $('#progress-step-1-img');
  var progressBarStep2 = $('#progress-step-2');
  var progressBarStep2Image = $('#progress-step-2-img');
  var progressBarStep3 = $('#progress-step-3');
  var progressBarStep3Image = $('#progress-step-3-img');

  var dropdownEditingContainer = $('#dropdown-editing-container');
  var dropdownCounter = 0;
  var dropdownOptionCounter = 0;

  function addNewAssetInput() {
    var uploadInput = $('<input type="file" id="data-extra-asset-input-' + extraAssetsInputCounter + '" data-input-count=' + extraAssetsInputCounter + '  class="extra-asset-input">');
    var uploadInputDeleteButton = $('<button data-input-count=' + extraAssetsInputCounter + ' onclick="removeNewAssetInput(' + extraAssetsInputCounter + ')" class="remove-asset-button">Remove</button>');
    extraAssetsInputContainer.append(uploadInput);
    extraAssetsInputContainer.append(uploadInputDeleteButton);
    extraAssetsInputCounter++;
  }

  function removeNewAssetInput(counterIdToRemove) {
    $("input[data-input-count='" + counterIdToRemove + "']").remove();
    $("button[data-input-count='" + counterIdToRemove + "']").remove();
  }

  function showStepTwo() {
    $(stepThreeContainer).hide(500);
    $(stepOneContainer).hide(500);
    $(stepTwoContainer).show(500);
    setProgressBarStep2();
  }

  function showStepOne() {
    $(stepOneContainer).show(500);
    $(stepTwoContainer).hide(500);
    setProgressBarStep1();
  }

  function showStepThree() {
    createItemSettingsObject()
        .then(function (result) {
          setProgressBarStep3();
          resetStepThree();
          resetVars();
          objectData = result;

          if ($('#influencer-owner').val() != "none") {
            $('#user-creator-name').text($('#user-creator-name').text() + $('#influencer-owner').find('option:selected').text());
            $('#display-secondary-item-description-user').text($('#influencer-owner').find('option:selected').text());
            $('#user-creator-name').attr('href', '/' + $('#influencer-owner').find('option:selected').attr('data-profile-username'));
            $('#owner-profile-img').attr('src', $('#influencer-owner').find('option:selected').attr('data-profile-img'));
          } else {
            $('#user-creator-name').text($('#user-creator-name').text() + 'You!');
            $('#display-secondary-item-description-user').text('You!');
            $('#user-creator-name').attr('href', '#');
            $('#owner-profile-img').attr('src', 'https://storage.googleapis.com/troveup-imagestore/assets/img/default-user-icon-thumb.jpg');
          }

          $('#display-item-name').text(objectData.itemName);
          $('#display-item-price').text(objectData.itemPrice);
          $('#display-item-description').text(objectData.itemDescription);
          $('#display-secondary-item-description-title').text(objectData.bottomDescriptionTitle);
          processBottomDescriptionText($('#bottom-description-container'));


          mainImgSelect.attr('src', objectData.primaryDisplayImageUrl);

          $.each(objectData.simpleItemControls, function () {
            var dropdown = generateDropdown(this);
            dropdown.appendTo($('#step-three-dropdowns-container'));
          });

          updateCameraMaxCountFromCurrentAssets();
          initCameraTags();
          initStaticAssets();
          updatePageAssets(true);
          $(stepTwoContainer).hide(500);
          $(stepThreeContainer).show(500);
        });
  }

  function setProgressBarStep1() {
    progressBarStep2.removeClass('active');
    progressBarStep2Image.attr('src', 'https://storage.googleapis.com/troveup-imagestore/assets/sectioned-gem-white.svg');
    progressBarStep3.removeClass('active');
    progressBarStep3Image.attr('src', 'https://storage.googleapis.com/troveup-imagestore/assets/sectioned-gem-white.svg');

    progressBarStep1.addClass('active');
    progressBarStep1Image.attr('src', 'https://storage.googleapis.com/troveup-imagestore/assets/sectioned-gem-red.svg');
  }

  function setProgressBarStep2() {
    progressBarStep1.removeClass('active');
    progressBarStep1Image.attr('src', 'https://storage.googleapis.com/troveup-imagestore/assets/sectioned-gem-white.svg');
    progressBarStep3.removeClass('active');
    progressBarStep3Image.attr('src', 'https://storage.googleapis.com/troveup-imagestore/assets/sectioned-gem-white.svg');

    progressBarStep2.addClass('active');
    progressBarStep2Image.attr('src', 'https://storage.googleapis.com/troveup-imagestore/assets/sectioned-gem-red.svg');
  }

  function setProgressBarStep3() {
    progressBarStep1.removeClass('active');
    progressBarStep1Image.attr('src', 'https://storage.googleapis.com/troveup-imagestore/assets/sectioned-gem-white.svg');
    progressBarStep2.removeClass('active');
    progressBarStep2Image.attr('src', 'https://storage.googleapis.com/troveup-imagestore/assets/sectioned-gem-white.svg');

    progressBarStep3.addClass('active');
    progressBarStep3Image.attr('src', 'https://storage.googleapis.com/troveup-imagestore/assets/sectioned-gem-red.svg');
  }

  function createBareDropdown(title) {

    var displayedTitle = title == null ? "Set Dropdown Title" : title;

    var dropdownContainer = $('<div id="dropdown-container-' + dropdownCounter + '" style="text-align: center;">');
    var dropdown = $('<div data-dropdown-id="' + dropdownCounter + '" onclick="hideShowDropdownIfNotEditing(' + dropdownCounter + ');" id="dropdown-' + dropdownCounter + '" style="display: inline-block" class="custom-dropdown"></div>');
    var removeButton = $('<button id="dropdown-remove-button-' + dropdownCounter + '" class="dropdown-button" onclick="removeDropdown(' + dropdownCounter + ')" class="remove-asset-button">Remove</button>');
    var checkboxContainer = $('<div id="checkbox-container-' + dropdownCounter + '"></div>');
    var swatchCheckboxLabel = $('<label style="margin-right: 10px;" for="enable-swatch-checkbox-' + dropdownCounter + '">Option Swatches</label>');
    var swatchCheckbox = $('<input onclick="enableDisableSwatchInputs(' + dropdownCounter + ')" id="enable-swatch-checkbox-' + dropdownCounter + '" type="checkbox">');
    var optionAssetCheckboxLabel = $('<label for="enable-assets-checkbox-' + dropdownCounter + '">Option Assets</label>');
    var optionAssetCheckbox = $('<input onclick="enableDisableAssetInputs(' + dropdownCounter + ')" id="enable-assets-checkbox-' + dropdownCounter + '" type="checkbox">');
    var dropdownTitle = $('<p id="dropdown-title-' + dropdownCounter + '" class="select-title">' + displayedTitle + '</p>');
    var dropdownTitleEditField = $('<input id="dropdown-title-edit-field-' + dropdownCounter + '" style="display: none;" type="text" data-active="false" value="' + displayedTitle + '">');
    var dropdownTitleEditButton = $('<button id="dropdown-title-edit-button-' + dropdownCounter + '" onclick="enableDisableEditTitleDropdownField(' + dropdownCounter + ')" class="dropdown-button">Edit Title</button>');
    var dropdownAddOptionButtonContainer = $('<div id="dropdown-add-option-button-container-' + dropdownCounter + '"></div>');
    var dropdownAddOptionButton = $('<button class="dropdown-button" onclick="addDropdownOption(' + dropdownCounter + ')" style="display: inline-block;" id="dropdown-add-option-button-' + dropdownCounter + '">Add Option</button>');
    var optionContainer = $('<div class="dropdown-option-container" data-dropdown-id="' + dropdownCounter + '" id="dropdown-option-container-' + dropdownCounter + '"></div>');


    dropdownAddOptionButtonContainer.append(dropdownAddOptionButton);
    checkboxContainer.append(swatchCheckbox);
    checkboxContainer.append(swatchCheckboxLabel);
    checkboxContainer.append(optionAssetCheckbox);
    checkboxContainer.append(optionAssetCheckboxLabel);
    dropdown.append(dropdownTitle);
    dropdown.append(dropdownTitleEditField);
    dropdownContainer.append(checkboxContainer);
    dropdownContainer.append(dropdownTitleEditButton);
    dropdownContainer.append(dropdown);
    dropdownContainer.append(removeButton);
    dropdownContainer.append(optionContainer);
    dropdownContainer.append(dropdownAddOptionButtonContainer);
    dropdownEditingContainer.append(dropdownContainer);

    return dropdownCounter++;
  }

  function removeDropdown(dropdownCounter) {
    $("#dropdown-container-" + dropdownCounter).remove();
    $("#dropdown-" + dropdownCounter).remove();
    $("#dropdown-remove-button-" + dropdownCounter).remove();
    $('#dropdown-title-edit-button-' + dropdownCounter).remove();
    $('#dropdown-add-option-button-' + dropdownCounter).remove();

  }

  function enableDisableEditTitleDropdownField(dropdownCounter) {
    var editField = $('#dropdown-title-edit-field-' + dropdownCounter);
    var titleText = $("#dropdown-title-" + dropdownCounter);
    var editButton = $('#dropdown-title-edit-button-' + dropdownCounter);
    if (editField.attr('data-active') == "false") {
      titleText.hide();
      editField.show();
      editField.attr('data-active', "true");
      editButton.html('Set Title');

    } else {
      titleText.html(editField.val());
      editField.attr('data-active', "false");
      editField.hide();
      titleText.show();
      editButton.html('Edit Title');

    }
  }

  function enableDisableEditTitleDropdownOptionField(optionCounter) {
    var editField = $('#dropdown-option-title-edit-field-' + optionCounter);
    var titleText = $("#option-figure-" + optionCounter);
    var editButton = $('#dropdown-option-title-edit-button-' + optionCounter);
    if (editField.attr('data-active') == "false") {
      titleText.hide();
      editField.show();
      editField.attr('data-active', "true");
      editButton.html('Set Option Title');

    } else {
      clearDivHtmlTextButLeaveChildElements('option-figure-' + optionCounter);
      titleText.prepend(editField.val());
      editField.attr('data-active', "false");
      editField.hide();
      titleText.show();
      editButton.html('Edit Option Title');

    }
  }

  function addDropdownOption(dropdownCounter, optionTitle, swatchSrc) {

    var displayedTitle = optionTitle == null ? "Set Option Title" : optionTitle;

    var container = $('#dropdown-option-container-' + dropdownCounter);
    var secondaryContainer = $('<div class="secondary-option-container" data-option-id="' + dropdownOptionCounter + '" id="option-container-' + dropdownOptionCounter + '"</div>');
    var optionTitleEditButton = $('<button onclick="enableDisableEditTitleDropdownOptionField(' + dropdownOptionCounter + ')" class="dropdown-button" id="dropdown-option-title-edit-button-' + dropdownOptionCounter + '">Edit Option Title</button>');
    var optionSection = $('<section style="top: 0px;" class="custom-select-options"></section>"');
    var option = $('<figure class="select-value-no-asset dropdown-option" id="option-figure-' + dropdownOptionCounter + '">' + displayedTitle + '</figure>');
    var optionEditTextField = $('<input id="dropdown-option-title-edit-field-' + dropdownOptionCounter + '" style="display: none;" type="text" data-active="false" value="' + displayedTitle + '">');
    var optionRemoveButton = $('<button class="dropdown-button" onclick="removeDropdownOption(' + dropdownOptionCounter + ')" id="dropdown-option-remove-button-' + dropdownOptionCounter + '">Remove Option</button>');

    optionSection.append(option);
    optionSection.append(optionEditTextField);
    secondaryContainer.append(optionTitleEditButton);
    secondaryContainer.append(optionSection);
    secondaryContainer.append(optionRemoveButton);
    container.append(secondaryContainer);

    if ($('#enable-swatch-checkbox-' + dropdownCounter).is(":checked")) {
      addSwatchInput(dropdownOptionCounter);
    }

    if ($('#enable-assets-checkbox-' + dropdownCounter).is(":checked")) {
      var cameraCount = parseInt($('#camera-angle-count').val());
      for (var i = 1; i <= cameraCount; ++i) {
        addOptionAssetInput(dropdownOptionCounter, i);
      }
    }

    if (!container.is(":visible")) {
      container.show();
    }

    if (swatchSrc != null) {
      updateSwatch(dropdownOptionCounter, swatchSrc);
    }

    return dropdownOptionCounter++;
  }

  function enableDisableSwatchInputs(dropdownCounter) {

    var dropdownOptionIdsToModify = getDropdownOptionIdsByDropdown(dropdownCounter);

    //Enable inputs
    if ($('#enable-swatch-checkbox-' + dropdownCounter).is(":checked")) {
      $.each(dropdownOptionIdsToModify, function () {
        addSwatchInput(this);
      });
    }
    //Disable inputs
    else {
      $.each(dropdownOptionIdsToModify, function () {
        $('#swatch-input-' + this).remove();
        $('#swatch-input-label-' + this).remove();

        //Modify the class on the option to accomodate the new swatch
        var option = $('#option-figure-' + this);
        option.removeClass('select-value');
        option.addClass('select-value-no-asset');

        //Remove image sample swatch div
        $('#option-figure-swatch-' + this).remove();
      });
    }
  }

  function getDropdownOptionIdsByDropdown(dropdownCounter) {
    var dropdowns = $('#dropdown-option-container-' + dropdownCounter).children();
    var dropdownOptionIds = [];

    $.each(dropdowns, function () {
      var dropdownId = $(this).attr('data-option-id');
      dropdownOptionIds.push(dropdownId);
    });

    return dropdownOptionIds;
  }

  function addSwatchInput(dropdownOptionCounter) {
    //Append the input to the option
    var fileInputContainer = verifyOrCreateFileInputContainer(dropdownOptionCounter);
    var containingDiv = $('<div id="swatch-input-container-' + dropdownOptionCounter + '"></div>');
    var swatchInputLabel = $('<label style="padding-right: 10px;" id="swatch-input-label-' + dropdownOptionCounter + '" for="swatch-input-' + dropdownOptionCounter + '">Select Swatch</label>');
    var swatchInput = $('<input onchange="updateSwatch(' + dropdownOptionCounter + ')" style="display: inline-block;" id="swatch-input-' + dropdownOptionCounter + '" type="file">');

    containingDiv.append(swatchInputLabel);
    containingDiv.append(swatchInput);
    fileInputContainer.prepend(containingDiv);
  }

  function enableDisableAssetInputs(dropdownCounter) {
    var cameraCount = parseInt($('#camera-angle-count').val());
    var dropdownOptionIdsToModify = getDropdownOptionIdsByDropdown(dropdownCounter);

    //Enable inputs
    if ($('#enable-assets-checkbox-' + dropdownCounter).is(":checked")) {
      $.each(dropdownOptionIdsToModify, function () {
        for (var i = 1; i <= cameraCount; ++i) {
          addOptionAssetInput(this, i);
        }
      });
    }
    //Disable inputs
    else {
      $.each(dropdownOptionIdsToModify, function () {
        for (var i = 1; i <= cameraCount; ++i) {
          $('#asset-input-container-' + this + '-camera-' + i).remove();
        }
      });
    }
  }

  function updateSwatch(dropdownOptionCounter, swatchSrc) {

    if (swatchSrc != null) {
      addSwatchAsset(dropdownOptionCounter, swatchSrc);
    } else {
      imageInputHelper.getRawSource('swatch-input-' + dropdownOptionCounter, dropdownOptionCounter)
          .then(function (result) {
            addSwatchAsset(result.args, result.result);
          });
    }

    //Modify the class on the option to accomodate the new swatch
    var option = $('#option-figure-' + dropdownOptionCounter);
    if (option.hasClass('select-value-no-asset')) {
      option.removeClass('select-value-no-asset');
      option.addClass('select-value');
    }
  }

  function addSwatchAsset(dropdownOptionCounter, src) {
    var container = $('#option-figure-' + dropdownOptionCounter);
    var swatchImg = $('<img data-option-id="' + dropdownOptionCounter + '" id="option-figure-swatch-' + dropdownOptionCounter + '" class="select-value-asset-img" src="' + src + '">');

    //Clear any prior swatch divs
    var swatchDiv = $('#option-figure-swatch-container-' + dropdownOptionCounter);
    if (swatchDiv.length) {
      swatchDiv.remove();
    }

    swatchDiv = $('<div style="border: 0;" id="option-figure-swatch-container-' + dropdownOptionCounter + '" class="select-value-asset"></div>');
    swatchDiv.append(swatchImg);
    container.append(swatchDiv);
  }

  function addOptionAssetInput(dropdownOptionCounter, cameraNumber) {
    //Append the input to the option
    var fileInputContainer = verifyOrCreateFileInputContainer(dropdownOptionCounter);
    var containingDiv = $('<div data-option-id=' + dropdownOptionCounter + ' class="camera-input-container" id="asset-input-container-' + dropdownOptionCounter + '-camera-' + cameraNumber + '"></div>');
    var assetInputLabel = $('<label style="padding-right: 10px;" id="asset-input-label-' + dropdownOptionCounter + '-camera-' + cameraNumber + '" for="asset-input-' + dropdownOptionCounter + '-camera-' + cameraNumber + '">Select Camera ' + cameraNumber + ' Asset</label>');
    var assetInput = $('<input class="dropdown-option-asset-input" data-option-id=' + dropdownOptionCounter + ' data-camera-number=' + cameraNumber + ' style="display: inline-block;" id="asset-input-' + dropdownOptionCounter + '-camera-' + cameraNumber + '" type="file">');

    containingDiv.append(assetInputLabel);
    containingDiv.append(assetInput);
    fileInputContainer.append(containingDiv);
  }

  function verifyOrCreateFileInputContainer(dropdownOptionCounter) {
    var fileInputContainer = $('#option-file-input-container-' + dropdownOptionCounter);

    if (!$(fileInputContainer).length) {
      var optionContainer = $('#option-container-' + dropdownOptionCounter);
      fileInputContainer = $('<div data-option-id=' + dropdownOptionCounter + ' class="option-file-input-container" style="padding: 10px;" id="option-file-input-container-' + dropdownOptionCounter + '"></div>');

      optionContainer.append(fileInputContainer);
    }

    return fileInputContainer;
  }


  function clearDivHtmlTextButLeaveChildElements(elementId) {
    $('#' + elementId).contents().filter(function () {
      return (this.nodeType == 3);
    }).remove();
  }

  function getTextPortionOfDivHtml(elementId) {
    return $('#' + elementId).contents().filter(function () {
      return (this.nodeType == 3);
    })[0].data;
  }

  function removeDropdownOption(dropdownOptionCounter) {
    $('#option-container-' + dropdownOptionCounter).remove();
  }

  function updateCameraAssetInputs() {
    var cameraCount = parseInt($('#camera-angle-count').val());

    var fileInputContainers = $('.option-file-input-container');

    if (fileInputContainers.length) {
      var priorCameraCount = 0;
      var cameraDelta = 0;

      //Retrieve the prior camera count
      $.each(fileInputContainers, function () {

        var cameraContainers = $(this).find('.camera-input-container');
        var optionId = $(this).attr('data-option-id');

        //Determine the initial asset input deltas
        if (cameraContainers.length) {

          if (cameraDelta == 0) {
            priorCameraCount = cameraContainers.length;
            cameraDelta = cameraCount - priorCameraCount;
          }

          //Remove asset inputs if the number of cameras selected decreased
          if (cameraDelta < 0) {
            for (var i = priorCameraCount; i > priorCameraCount - (cameraDelta * -1); --i) {
              $('#asset-input-container-' + optionId + '-camera-' + i).remove();
            }
          }
          //Otherwise, add extra asset inputs
          else {
            for (i = priorCameraCount; i < priorCameraCount + cameraDelta; ++i) {
              addOptionAssetInput(optionId, i + 1);
            }
          }
        }
      });
    }
  }

  function addMaterialDropdownFromTemplate() {
    var dropdownId = createBareDropdown("Color");

    //Check the option swatches and option assets checkboxes
    $('#enable-swatch-checkbox-' + dropdownId).attr('checked', true);
    $('#enable-assets-checkbox-' + dropdownId).attr('checked', true);

    addDropdownOption(dropdownId, "Gold", "https://storage.googleapis.com/troveup-imagestore/assets/img/swat/gold.png");
    addDropdownOption(dropdownId, "Silver", "https://storage.googleapis.com/troveup-imagestore/assets/img/swat/silver.png");
    addDropdownOption(dropdownId, "Rose Gold", "https://storage.googleapis.com/troveup-imagestore/assets/img/swat/rose.png");
  }

  function addGemstoneDropdownFromTemplate() {
    var dropdownId = createBareDropdown("Crystal");

    //Check the option swatches and option assets checkboxes
    $('#enable-swatch-checkbox-' + dropdownId).attr('checked', true);
    $('#enable-assets-checkbox-' + dropdownId).attr('checked', true);

    addDropdownOption(dropdownId, "White", "https://storage.googleapis.com/troveup-imagestore/assets/img/swat/clear.png");
    addDropdownOption(dropdownId, "Peridot", "https://storage.googleapis.com/troveup-imagestore/assets/img/swat/green.png");
    addDropdownOption(dropdownId, "Siam", "https://storage.googleapis.com/troveup-imagestore/assets/img/swat/red.png");
    addDropdownOption(dropdownId, "Sapphire", "https://storage.googleapis.com/troveup-imagestore/assets/img/swat/blue.png");
  }

  function createFromTemplate() {
    var selectedTemplate = $('#template-select').val();

    if (selectedTemplate == 'material') {
      addMaterialDropdownFromTemplate();
    } else {
      addGemstoneDropdownFromTemplate();
    }
  }

  function hideShowDropdownIfNotEditing(dropdownId) {
    if ($('#dropdown-title-edit-field-' + dropdownId).attr('data-active') == "false") {
      $('#dropdown-option-container-' + dropdownId).toggle();
    }
  }

  function createItemSettingsObject() {
    return new Promise(function (resolve, reject) {
      getAssetSources()
          .then(function (result) {

            var sources = result;

            var dataObject = {};
            dataObject.itemName = $('#item-name').val();
            dataObject.itemDescription = $('#item-description').val();
            dataObject.userCreatorName = $('#influencer-owner option:selected').text();

            var influencerId = $('#influencer-owner').val();
            if (influencerId != 'none') {
              dataObject.influencerUserAccountId = influencerId;
            }

            dataObject.itemPrice = $('#item-price').val();
            dataObject.primaryDisplayImageUrl = sources.defaultImage;

            if (sources.altHoverImage != null) {
              dataObject.hoverImageUrl = sources.altHoverImage;
            }

            dataObject.simpleItemControls = compileDropdownsAndAssets(sources);
            dataObject.staticAssets = [];
            dataObject.bottomDescriptionTitle = $('#secondary-item-description-title').val();
            dataObject.bottomDescriptionText = $('#secondary-item-description-text').val();
            dataObject.active = $('#active-immediately').prop('checked');

            if (sources.extraAssetInputs != null) {
              for (var i = 0; i < Object.keys(sources.extraAssetInputs).length; ++i) {
                var asset = {};
                asset.assetUrl = sources.extraAssetInputs[i];
                dataObject.staticAssets.push(asset);
              }
            }

            resolve(dataObject);
          });
    });
  }

  function getAssetSources() {

    return new Promise(function (resolve, reject) {
      var sources = {};

      imageInputHelper.getRawSource('default-image')
          .then(function (result) {
            sources.defaultImage = result.result;

            return imageInputHelper.getRawSource('alt-hover-photo');
          })
          .then(function (result) {

            sources.altHoverImage = result.result;
            return getDynamicInputListByClassName('.extra-asset-input');
          })
          .then(function (result) {

            sources.extraAssetInputs = result;
            return getDropdownOptionSwatches();
          })
          .then(function (result) {
            sources.swatches = result;
            return getDropdownOptionAssets();
          })
          .then(function (result) {
            sources.optionAssets = result;

            resolve(sources);
          });
    });
  }

  function getDynamicInputListByClassName(className) {
    return new Promise(function (resolve, reject) {

      var inputs = $(className);
      var rval = [];

      if (inputs.length > 0) {
        for (var i = 0; i < inputs.length; ++i) {
          imageInputHelper.getRawSource($(inputs[i]).attr('id'))
              .then(function (result) {
                rval.push(result.result);

                if (rval.length == inputs.length) {
                  resolve(rval);
                }
              });
        }
      } else {
        resolve(null);
      }
    });
  }

  function getDropdownOptionSwatches() {
    return new Promise(function (resolve, reject) {
      var dropdowns = $('.dropdown-option-container');
      var rval = {};

      if ($('.select-value-asset-img').length > 0) {

        for (var i = 0; i < dropdowns.length; ++i) {
          var thisDropdown = $(dropdowns[i]);
          if (thisDropdown.find('.select-value-asset-img').length > 0) {

            var dropdownId = thisDropdown.attr('data-dropdown-id');
            var dropdownOptionAssets = thisDropdown.find('.select-value-asset-img');

            for (var j = 0; j < dropdownOptionAssets.length; ++j) {
              var optionId = $(dropdownOptionAssets[j]).attr('data-option-id');

              var index = "dropdown-" + dropdownId + "-option-" + optionId;

              rval[index] = $(dropdownOptionAssets[j]).attr('src');
            }
          }
        }
        resolve(rval);

      } else {
        resolve(null);
      }
    });
  }

  function getDropdownOptionAssets() {
    return new Promise(function (resolve, reject) {

      var rval = {};
      var inputs = $('.dropdown-option-asset-input');

      if (inputs.length > 0) {
        for (var i = 0; i < inputs.length; ++i) {
          var dropdownId = $(inputs[i]).parent().parent().parent().parent().attr('data-dropdown-id');
          var optionId = $(inputs[i]).attr('data-option-id');
          var cameraId = $(inputs[i]).attr('data-camera-number');

          var index = "dropdown-" + dropdownId + "-option-" + optionId + "-camera-" + cameraId;

          imageInputHelper.getRawSource($(inputs[i]).attr('id'), index)
              .then(function (result) {
                rval[result.args] = result.result;

                if (Object.keys(rval).length == inputs.length) {
                  resolve(rval);
                }
              })
        }
      } else {
        resolve(rval);
      }
    });
  }

  function countTotalSwatches() {
    return $('.select-value-asset-img').length;
  }

  function getDropdownTitleString(dropdownElementId) {
    var dropdownId = $('#' + dropdownElementId).attr('data-dropdown-id');
    return $('#dropdown-title-' + dropdownId).html();
  }

  function compileDropdownsAndAssets(assetSources) {
    var rval = [];
    var dropdowns = $('.custom-dropdown');

    $.each(dropdowns, function () {
      var dropdown = {};
      var cameraCount = parseInt($('#camera-angle-count').val());
      var dropdownId = $(this).attr('data-dropdown-id');
      var dropdownTitle = $('#dropdown-title-' + dropdownId).html();

      var options = $('#dropdown-option-container-' + dropdownId).find('.secondary-option-container');

      dropdown.controlName = 'Select ' + dropdownTitle;
      dropdown.controlSelectedName = dropdownTitle;
      dropdown.controlOptions = [];

      for (var i = 0; i < options.length; ++i) {
        var option = {};
        var optionId = $(options[i]).attr('data-option-id');
        option.controlOptionId = optionId;
        option.optionDisplayName = getTextPortionOfDivHtml('option-figure-' + optionId);
        option.optionValue = option.optionDisplayName.toLowerCase().replace(" ", "");

        //Check for a swatch for this option
        if (assetSources.swatches != null) {
          option.optionAssetUrl = assetSources.swatches["dropdown-" + dropdownId + "-option-" + optionId];
        }

        //Check option assets
        if (assetSources.optionAssets['dropdown-' + dropdownId + '-option-' + optionId + '-camera-1'] != null) {
          dropdown.interactionType = "ASSET_CHANGE";
          option.controlOptionAssets = [];
          for (var j = 0; j < cameraCount; ++j) {
            var index = "dropdown-" + dropdownId + "-option-" + optionId + '-camera-' + (j + 1);
            var asset = {};
            asset.controlOptionAssetUrl = assetSources.optionAssets[index];
            asset.camera = j;

            option.controlOptionAssets.push(asset);
          }
        }

        dropdown.controlOptions.push(option);
      }

      rval.push(dropdown);
    });

    return rval;
  }

  function resetStepThree() {
    $('#step-three-container').empty();
    $('#step-three-container').html(stepThreeBareHtml);
  }

  $(document).ready(function () {
    stepThreeBareHtml = $('#step-three-container').html();
    dropdownsContainer = $('#step-three-dropdowns-container');
  });

  function uploadItem() {

    disableFinalizeButtonAndShowSpinner();

    createItemSettingsObject()
        .then(function (result) {
          var dataObject = ajaxHelper.createOrAppendPostDataObject("simpleItemData", JSON.stringify(result));
          ajaxHelper.performPostRequest(dataObject, "/admin/ajax/simpleitem", getUploadItemSuccessCallback(), getUploadItemFailureCallback());
        });
  }

  function getUploadItemSuccessCallback() {
    return function (data) {
      alert("Success!");
      enableFinalizeButtonAndHideSpinner();
    }
  }

  function getUploadItemFailureCallback() {
    return function (data) {
      alert("Failed!");
      enableFinalizeButtonAndHideSpinner();
    }
  }

  function disableFinalizeButtonAndShowSpinner() {
    $('#finalize-button').prop('disabled', true);
    $('#finalize-button-text').hide(200);
    $('#finalize-button-spinner').show(200);
  }

  function enableFinalizeButtonAndHideSpinner() {
    $('#finalize-button').prop('disabled', false);
    $('#finalize-button-spinner').hide(200);
    $('#finalize-button-text').show(200);
  }
</script>
</body>
</html>