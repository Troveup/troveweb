<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="trove" uri="http://troveup.com/jsp/tlds/itemimage" %>
<html>
<head>
  <title>Trove: 3D Printed Jewelry Customized for You</title>
  <c:import url="../fragments/headers/commonHead.jsp"/>
  <c:import url="../fragments/headers/feedsHead.jsp"/>
  <c:import url="../fragments/analytics/all.jsp"/>

  <style>
    .chain-container {
      padding-bottom: 30px;
    }
  </style>

  <style>

    body {
      padding-top: 100px;
    }

    @media (min-width: 767px) {
      body {
        padding-top: 140px;
      }
    }

    @media (max-width: 575px) {
      body {
        padding-top: 130px;
      }

      /* .btn.btn_specialsignup {
         margin-top: 5px;
       }*/
    }

    .special-container p {
      margin-bottom: 5px;
    }

  </style>
</head>

<body>
<script>TROVE.setToken("${_csrf.token}");</script>
<c:import url="../fragments/modals/authModal.jsp"/>
<c:import url="../fragments/nav/topNavBar.jsp"/>
<c:import url="../fragments/handlebars/mainFeedItemCards.jsp"/>
<c:import url="../fragments/forms/addToCartForm.jsp"/>
<div class="container-fluid">
  <div class="row items"></div>
</div>
<c:import url="../fragments/vendor/npro.jsp"/>

<script type="text/javascript" src="/resources/js/trove/Auth.js"></script>
<script type="text/javascript" src="/resources/js/trove/Category.js"></script>
<script>

  var authHelper = new AuthHelper("${_csrf.token}", ${isAuthenticated});
  var categoryHelper = new CategoryHelper();

  function updateId(itemid) {
    document.getElementById("itemId").value = itemid;
    alert(itemid);
  }

  function updateSize(itemid) {

    var sizeDropdown = $('#sizee-' + String(itemid));

    if (sizeDropdown && sizeDropdown.val()) {
      $('#size').val(sizeDropdown.val());
    }
  }

  function updateMaterial(itemid) {
    var e = document.getElementById("dismenu-" + String(itemid));
    var mater = e.options[e.selectedIndex].value.split(" ");
    document.getElementById("materialId").value = mater[0];
    document.getElementById("finishId").value = mater[1];
  }

  function updateChain(itemId) {
    $("#chainId").val($("#chain-" + itemId).val());
  }

  var cardData;
  var pageNum = 0;
  var itemsPerPage = 12;
  var processing;
  var canContinueScrolling = true;

  function getFeedItems() {
    if (!processing) {
      processing = true;
      var data = Object.create(null);
      data['pageNumber'] = pageNum;
      // data['objectId-0'] = 0;
      // data['objectId-1'] = 3;
      data['pageLimit'] = itemsPerPage;
      data['duplicateBuffer'] = 3;
      jQuery.ajax({
        url: '/pagefeed',
        data: jQuery.param(data),
        cache: false,
        contentType: false,
        processData: false,
        headers: {
          'Access-Control-Allow-Origin': '*',
          "X-CSRF-TOKEN": "${_csrf.token}"
        },
        type: 'GET',
        success: function (data) {
          if (data.itemList.length == 0) {
            cardData = data.itemList;
          } else {
            cardData = data.itemList;
            pageNum = pageNum + 1;
          }

          if (data.itemList.length < itemsPerPage) {
            canContinueScrolling = false;
          }

          processing = false;
        }
      }).done(function (data) {
        layoutBars();

        for (var i = 0; i < data.itemList.length; ++i) {
          $('#engravehint-' + data.itemList[i].itemId).popover({trigger: "hover click"});
        }
      });
    }
  }

  // getCategoryItems('BRACELET');
  // getCategoryItems('RING');
  function getCategoryItems(category) {
    var data = Object.create(null);
    data['category'] = category;
    data['pageNumber'] = pageNum;
    // data['objectId-0'] = 0;
    // data['objectId-1'] = 3;
    data['pageLimit'] = itemsPerPage;
    data['duplicateBuffer'] = 3;
    jQuery.ajax({
      url: '/pagecategory',
      data: jQuery.param(data),
      cache: false,
      contentType: false,
      processData: false,
      headers: {
        'Access-Control-Allow-Origin': '*',
        "X-CSRF-TOKEN": "${_csrf.token}"
      },
      type: 'GET',
      success: function (data) {
        cardData = data.itemList;
        pageNum = pageNum + 1;
        processing = false;
      }
    }).done(function () {
      layoutBars();
    });
  }

  function layoutBars() {
    var t = Handlebars.compile($('#itemcards').html());
    Handlebars.registerHelper('unless_blank', function (item, block) {
      return (item && String(item).replace(/\s/g, "").length) ? block.fn(this) : block.inverse(this);
    });
    Handlebars.registerHelper('decimal', function (number) {
      return parseFloat(number).toFixed(0);
    });
    Handlebars.registerHelper('split', function(name) {
      return name.split(' ')[0];
    });
    $('.row.items').append(t(cardData));
    setupCards('.card');
  }

  var colCount = 0;
  var colWidth = 250;
  var margin = 30;
  var spaceLeft = 0;
  var windowWidth = 0;
  var blocks = [];

  function setupCards(selector) {
    windowWidth = $(window).width();
    blocks = [];
    colCount = Math.floor(Math.min(windowWidth / (colWidth + margin * 2), 4));
    spaceLeft = (windowWidth - ((colWidth * colCount) + (margin * (colCount - 1)))) / 2 + 15;
    for (var i = 0; i < colCount; i++) {
      blocks.push(margin);
    }

    positionCards(selector);
  }

  function positionCards(selector) {
    var itemsProcessed = 0;
    $(selector).each(function (i) {
      if ($(window).width() > 468 || ($(window).width() <= 468 && $(this).hasClass('hidemobile') == false)) {
        itemsProcessed += 1;
        var min = Array.min(blocks);
        var index = $.inArray(min, blocks);
        var leftPos = margin + (index * (colWidth + margin));
        $(this).css({
          'left': (leftPos + spaceLeft) - 40 + 'px',
          'top': min + $(".collection-container").height() + $(".banner").height() + $(".navbar").height() + $(".special-container").height() + 20 + 'px',
          //'display':'inherit',
          'opacity': 1
        });
        blocks[index] = min + $(this).outerHeight() + margin;
      }
    });

    if (itemsProcessed <= 3 && canContinueScrolling) {
      getFeedItems();
    }
  }

  function hideFade() {
    $('.faderr').css({
      'opacity': 0
    });
  }

  function showFade() {
    $('.faderr').css({
      'opacity': 1
    });
  }

  Array.min = function (array) {
    return Math.min.apply(Math, array);
  };

  $(window).load(function () {
    NProgress.inc(0.1);
  });
  $(document).ready(function () {
    NProgress.done();

  });  // start if not started otherwise inc
  $(document).ajaxStart(function () {
    showFade();
  });
  $(document).ajaxStop(function () {
    hideFade();
  });

  getFeedItems();
  $('[data-toggle="popover"]').popover();
  head.ready(document, function () {


    $('.cardbuybutton').on('click', function (event) {
      var arr = $(this).attr('id').split("-");
      updateId(arr[1]);
      updateSize(arr[1]);
      updateMaterial(arr[1]);
      updateChain(arr[1]);
    });

    $(document).scroll(function (e) {
      if (!canContinueScrolling)
        return false;
      if ($(window).scrollTop() >= ($(document).height() - $(window).height()) * 0.65) {
        getFeedItems();
      }
    });
    $(window).on('resize', function () {
      setupCards('.card');
    });


  });

  $(document).ready(function () {
    categoryHelper.landedOnFeedEvent(authHelper.getIsAuthenticated());
  });
</script>
</body>
</html>
