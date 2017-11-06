var objectData;
var dropdownsContainer = $('#dropdown-container');
var mainImgSelect = $('#mainimage');
var staticImgSelect = $('#staticimage');
var cameraContainerSelect = $('#cameras');
var mobileCarouselContainerSelect = $('#mobilepics');
var bottomCarouselContainerSelect = $('#otheritemspics');
var assetData = [];
var activeCamera = 0;
var cameraCount = 0;
var dropdownCounter = 0;
var optionCounter = 0;
var canvasInUse = false;
var canvasQueue = [];
var currentRenderSet = 0;
var ATTR_OPTION_NUMBER = "option-number";
var ATTR_ACTIVE_OPTION = "active-option";
var addToBagButton = $('#buttonaddtobag');
var pageInitStateControl = {
    initializationRenderSet: 0,
    initRenderSetCompletedCount: 0,
    initRenderSetTotalCount: 0
};

function initializeFeaturedEngine(dropdownsContainerJqueryReference,
                                  mainImgSelectJqueryReference, staticImgSelectJqueryReference,
                                  cameraContainerSelectJqueryReference, mobileCarouselContainerSelectJqueryReference,
                                  bottomCarouselContainerSelectJqueryReference) {
    dropdownsContainer = dropdownsContainerJqueryReference;
    mainImgSelect = mainImgSelectJqueryReference;
    staticImgSelect = staticImgSelectJqueryReference;
    cameraContainerSelect = cameraContainerSelectJqueryReference;
    mobileCarouselContainerSelect = mobileCarouselContainerSelectJqueryReference;
    bottomCarouselContainerSelect = bottomCarouselContainerSelectJqueryReference;
}

/**
 * Generates a dropdown given a SimpleItemControl JS Object
 *
 * @param dropdownData SimpleItemControl JS Object
 * @returns An HTML container with inner HTML representing the dropdown with options and attached asset changes
 **/
function generateDropdown(dropdownData) {
    var dropdownContainer = $('<div></div>')
        .addClass('dropdown')
        .attr('name', 'dropdown-' + dropdownCounter++)
        .attr('dropdown-id', dropdownData.controlId);

    var titleElement = $('<p></p>')
        .addClass('select-title')
        .attr('original-title', dropdownData.controlName)
        .attr('selected-title', dropdownData.controlSelectedName)
        .html(dropdownData.controlName)
        .appendTo(dropdownContainer);

    //Initially hidden select options section element
    var sectionElement = $('<section></section>')
        .addClass('select-options')
        .css('display', 'none');


    for (var i = 0; i < dropdownData.controlOptions.length; ++i) {
        var option = $('<figure></figure>')
            .attr(ATTR_OPTION_NUMBER, optionCounter++)
            .attr('value', dropdownData.controlOptions[i].optionValue)
            .attr('display-name', dropdownData.controlOptions[i].optionDisplayName)
            .attr('option-id', dropdownData.controlOptions[i].controlOptionId);

        option.html(dropdownData.controlOptions[i].optionDisplayName);

        //Add whatever assets need to change based on selection changes here
        if (dropdownData.interactionType == "ASSET_CHANGE") {
            //Parse the data assets
            $.each(dropdownData.controlOptions[i].controlOptionAssets, function () {
                var asset = {
                    camera: this.camera,
                    assetUrl: this.controlOptionAssetUrl,
                    optionNumber: option.attr(ATTR_OPTION_NUMBER)
                };

                assetData.push(asset);
            });
        }

        //First option of the bunch, make it active asset-wise
        if (i == 0) {
            option.attr(ATTR_ACTIVE_OPTION, 'true');
        }

        //Add a sample swatch to the dropdown option if available
        if (dropdownData.controlOptions[i].optionAssetUrl != null) {
            option.addClass('select-value');
            $('<div></div>')
                .addClass('select-value-asset')
                .css('background-image', 'url(' + dropdownData.controlOptions[i].optionAssetUrl + ')')
                .appendTo(option);
        } else {
            option.addClass('select-value-no-asset');
        }

        option.attr('value', dropdownData.controlOptions[i].optionValue);
        option.appendTo(sectionElement);

        addDropdownOptionClickAndHoverEvents(option);
    }

    sectionElement.appendTo(dropdownContainer);

    addDropdownClickEvent(dropdownContainer);

    return dropdownContainer;
}

/**
 * Adds default click events to the given dropdown
 *
 * @param dropdown The dropdown jQuery object to which click events are added
 **/
function addDropdownClickEvent(dropdown) {
    dropdown.on('click', function (e) {
        e.stopPropagation();
        $(this).removeClass('forgot');
        var dropdownName = $(this).attr("name");
        var dropdownElement = $('.dropdown[name=' + dropdownName + ']');
        var dropdownOptions = dropdownElement.children('section.select-options');
        if ($(dropdownOptions).css('display') == 'none') {

            //Make sure all other select options and titles are hidden
            $('.select-options').css('display', 'none');
            $('.select-title').removeClass('select-title-active');

            //Unhide only the children of this dropdown
            $(dropdownOptions).addClass('select-title-active');
            $(dropdownOptions).css('display', 'block');
        } else {
            $(dropdownOptions).removeClass('select-title-active');
            $(dropdownOptions).css('display', 'none');
        }
    });
}

/**
 * Update dropdown option items so that click/hover events trigger a change in assets if necessary
 *
 * @param dropdownOption SimpleItemControlOption object to add the event to
 **/
function addDropdownOptionClickAndHoverEvents(dropdownOption) {
    dropdownOption.on('click', function (e) {
        optionClickHoverEvent(this);
    });

    dropdownOption.on('mouseover', function (e) {
        optionClickHoverEvent(this);
    });
}

/**
 * Adds common default SimpleItemControlOption click/hover event to a given dropdownOption
 *
 * @param dropdownOption SimpleItemControlOption object to add the event to
 **/
function optionClickHoverEvent(dropdownOption) {
    //Change the dropdown display text based on the currenly hovered-over/clicked option
    var dropdownTitleSelect = getSelectOptionTitle(dropdownOption);

    if ($(dropdownTitleSelect).attr('selected-title') != null) {
        $(dropdownTitleSelect).text($(dropdownTitleSelect).attr('selected-title') + ': ' + $(dropdownOption).text());
    } else {
        $(dropdownTitleSelect).text($(dropdownOption).text());
    }
    $(dropdownOption).parent().parent().attr('data-user-selected', 'true');
    //Update active option html attribute so that asset updates are made correctly
    $(dropdownOption).parent().children("figure[" + ATTR_ACTIVE_OPTION + "='true']").removeAttrs(ATTR_ACTIVE_OPTION);
    $(dropdownOption).attr(ATTR_ACTIVE_OPTION, 'true');

    updatePageAssets();
}

/**
 * Updates the assets on the page using the pertinent active dropdown options and cameras
 **/
function updatePageAssets(isFirstRun) {
    ++currentRenderSet;
    var pageInitCallback = null;

    if (isFirstRun != null && isFirstRun) {

        if (typeof pageAssetInitializationCallback != 'undefined' && pageAssetInitializationCallback != null) {
            pageInitCallback = pageAssetInitializationCallback;
        }
        pageInitStateControl.initializationRenderSet = currentRenderSet;
        pageInitStateControl.initRenderSetTotalCount = cameraCount;
    }

    //Set the main image
    var assetsToUpdate = retrievePertinentAssets(activeCamera);

    if (assetsToUpdate.length > 0) {
        mergeImagesAndSetImgSrcAttribute(assetsToUpdate, mainImgSelect, pageInitCallback);
    } else if (pageInitCallback != null) {
        pageInitCallback();
    }

    //Set the camera previews
    for (var i = 0; i < cameraCount; ++i) {
        assetsToUpdate = retrievePertinentAssets(i);

        var imgTagArray = [];

        var cameraImageTag = $('button[data-camera=' + i + ']').children()[0];
        var mobileCarouselImageTag = $('img[data-camera=' + i + ']')[0];

        imgTagArray.push(cameraImageTag);
        imgTagArray.push(mobileCarouselImageTag);

        mergeImagesAndSetImgSrcAttribute(assetsToUpdate, imgTagArray, currentRenderSet, pageInitCallback);
    }
}

/**
 * Retrieves all asset objects that are pertinent to the current page context based on the provided/active camera and
 * the active dropdown options
 *
 * @param pertinentCamera
 **/
function retrievePertinentAssets(pertinentCamera) {
    var cameraToUse = pertinentCamera == null ? activeCamera : pertinentCamera;
    var rval = [];
    var activeOptionsArray = [];

    var activeOptions = getActiveOptions();
    $.each(activeOptions, function () {
        activeOptionsArray.push($(this).attr(ATTR_OPTION_NUMBER));
    });

    $.each(assetData, function () {
        if (this.camera == cameraToUse && $.inArray(this.optionNumber, activeOptionsArray) > -1) {
            rval.push(this);
        }
    });

    return rval;
}

/**
 *  Merges a set of images into a single image using an HTML5 canvas, then sets the image source of the given
 *  img tag to the output
 *
 *  @param images Array of Objects, each of which contain an assetUrl descriptor with the URL for the given asset to
 *  be combined into the rest of the image
 *  @param imgTags The jQuery img selector or Array of img selectors to set the output src to
 *  @param renderSet Set number of the current batch of renders.  Further queued items will be ignored if the global
 *  variable currentRenderSet does not match the current renderSet, as this indicates that there more recent items
 *  to render.
 *  @param renderFinishedCallback Nullable function called when a render on a single image has been completed.
 *  @note Input images must be no larger than 500x500px!
 **/
function mergeImagesAndSetImgSrcAttribute(images, imgTags, renderSet, renderFinishedCallback) {
    //If the canvas isn't busy, proceed
    if (!canvasInUse) {
        canvasInUse = true;

        var canvas = document.getElementById('myCanvas');
        var ctx = canvas.getContext('2d');

        var completedImageRenders = [];

        for (var i = 0; i < images.length; ++i) {
            var image = new Image();
            image.setAttribute('crossOrigin', 'anonymous');
            image.i = i;
            image.imagesLength = images.length;
            image.src = images[i].assetUrl;
            image.renderFinishedCallback = renderFinishedCallback;
            image.onload = function () {
                ctx.drawImage(this, 0, 0);
                completedImageRenders.push(this.i);

                if (completedImageRenders.length == this.imagesLength) {

                    if (!$.isArray(imgTags)) {
                        $(imgTags).attr('src', canvas.toDataURL());
                    } else {
                        $.each(imgTags, function () {
                            $(this).attr('src', canvas.toDataURL());
                        });
                    }

                    //Render is completed, call the callback function if necessary
                    if (this.renderFinishedCallback != null) {
                        this.renderFinishedCallback(renderSet);
                    }

                    canvasInUse = false;

                    //Check the render queue to make sure there's nothing else to render
                    var nextRender = canvasQueue.shift();

                    if (nextRender != null) {
                        mergeImagesAndSetImgSrcAttribute(nextRender.images, nextRender.tag, nextRender.renderSet, nextRender.renderFinishedCallback);
                    }
                }

            };
        }
    }
    //Canvas was busy, push the work onto the stack to be re-visited later
    else {
        var canvasRenderData = {
            images: images,
            tag: imgTags,
            renderSet: renderSet,
            renderFinishedCallback: renderFinishedCallback
        };
        canvasQueue.push(canvasRenderData);
    }
}

/**
 * Searches through the current assetData to retrieve the maximum camera count, then sets the cameraCount variable
 * to reflect the result.
 **/
function updateCameraMaxCountFromCurrentAssets() {
    $.each(assetData, function () {
        if (this.camera > cameraCount) {
            cameraCount = this.camera;
        }
    });

    if (assetData.length > 0) {
        //Cameras start at 0
        cameraCount += 1;
    }
}

function getActiveOptions() {
    return $("figure[" + ATTR_ACTIVE_OPTION + "='true']");
}

function getSelectOptionTitle(selectOption) {
    return $(selectOption).parent().parent().children('.select-title')[0];
}

/**
 * Function for populating the cameras div with empty button and image tags for different "camera" perspectives
 *
 * @note These tags are uninitialized and should be set with a subsequent updatePageAssets() call.
 **/
function initCameraTags() {
    for (var i = 0; i < cameraCount; i++) {
        //Desktop camera tags
        var camera = $('<button></button>')
            .addClass('btn')
            .addClass('cameraOption')
            .attr('data-camera', i);

        if (i == 0) {
            camera.addClass('active');
        }

        var cameraImage = $('<img>')
            .addClass('cameraPhoto');

        cameraImage.appendTo(camera);

        camera.appendTo(cameraContainerSelect);

        camera.on('click', function () {
            $(this).addClass('active').siblings().removeClass('active');
            activeCamera = $(this).attr('data-camera');
            mainImgSelect.show();
            staticImgSelect.hide();
            updatePageAssets();
        });

        //Mobile carousel camera tags
        camera = $('<img>')
            .attr('data-camera', i);

        camera.on('click', function () {
            activeCamera = $(this).attr('data-camera');
            updatePageAssets();
        });

        camera.appendTo(mobileCarouselContainerSelect);
    }
}

function initStaticAssets() {

    if (objectData.staticAssets != null && objectData.staticAssets.length > 0) {
        $.each(objectData.staticAssets, function () {
            //Desktop static image tags
            var staticAssetContainer = generateStaticAssetElement(this.assetUrl);

            //Append to the camera container
            staticAssetContainer.appendTo(cameraContainerSelect);

            //Also append to the carousel
            var staticAsset = $('<img>')
                .attr('src', this.assetUrl);
            staticAsset.appendTo(mobileCarouselContainerSelect);
        });
    }

    //Special case where there are no dropdown assets/cameras
    //but there are static assets.  Need to be able to back-select
    //to the default image for the item.
    if (cameraCount == 0) {
        var staticAssetContainer = generateStaticAssetElement(objectData.primaryDisplayImageUrl)
            .addClass('active');
        staticAssetContainer.appendTo(cameraContainerSelect);

        //Also append to the carousel
        var staticAsset = $('<img>')
            .attr('src', objectData.primaryDisplayImageUrl);
        staticAsset.appendTo(mobileCarouselContainerSelect);
    }
}

function generateStaticAssetElement(assetUrl) {
    var staticAssetContainer = $('<button></button>')
        .addClass('btn')
        .addClass('cameraOption');

    var staticAsset = $('<img>')
        .attr('src', assetUrl);

    staticAsset.appendTo(staticAssetContainer);

    staticAssetContainer.on('click', function () {
        var thisSelect = $(this);
        thisSelect.addClass('active').siblings().removeClass('active');
        staticImgSelect.attr('src', $(thisSelect.children()[0]).attr('src'));
        mainImgSelect.hide();
        staticImgSelect.show();
    });

    return staticAssetContainer;
}

function getDropdownsWithSelectedOptions() {

    var rval = [];
    var activeOptions = getActiveOptions();

    $.each(activeOptions, function () {
        var thisSelect = $(this);
        var activeOptionDropdownTitle = getSelectOptionTitle(this);
        var dropdownContainer = thisSelect.parent().parent();

        var selectedOption = {
            simpleItemControlId: dropdownContainer.attr('dropdown-id'),
            simpleItemControlOptionId: thisSelect.attr('option-id'),
            simpleItemControlTitle: getDropdownSelectedTitle(dropdownContainer.attr('dropdown-id')),
            simpleItemControlOptionLabel: thisSelect.attr('display-name')
        };

        rval.push(selectedOption);
    });

    return rval;
}

function processBottomDescriptionText(bottomDescriptionTextContainer) {
    var textSplit = objectData.bottomDescriptionText.split("\n");

    for (var i = 0; i < textSplit.length; ++i) {
        $('<p></p>')
            .addClass('lockup')
            .html(textSplit[i])
            .appendTo(bottomDescriptionTextContainer);
    }
}

function getDropdownSelectedTitle(dropdownId) {

    var rval = null;

    $.each(objectData.simpleItemControls, function() {
        if (parseInt(this.controlId) == parseInt(dropdownId)) {
            rval = this.controlSelectedName;
        }
    });

    return rval;
}

function resetVars() {
    objectData = null;
    assetData = [];
    activeCamera = 0;
    cameraCount = 0;
    dropdownsContainer = $('#dropdown-container');
    dropdownCounter = 0;
    optionCounter = 0;
    mainImgSelect = $('#mainimage');
    staticImgSelect = $('#staticimage');
    cameraContainerSelect = $('#cameras');
    canvasInUse = false;
    canvasQueue = [];
    currentRenderSet = 0;
    ATTR_OPTION_NUMBER = "option-number";
    ATTR_ACTIVE_OPTION = "active-option";
    mobileCarouselContainerSelect = $('#mobilepics');
    bottomCarouselContainerSelect = $('#otheritemspics');
    addToBagButton = $('#buttonaddtobag');
    pageInitStateControl = {
        initializationRenderSet: 0,
        initRenderSetCompletedCount: 0,
        initRenderSetTotalCount: 0
    }
}