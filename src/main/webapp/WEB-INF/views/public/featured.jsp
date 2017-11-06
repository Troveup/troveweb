<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="trove" uri="http://troveup.com/jsp/tlds/itemimage" %>
<html>
<head>
  <title>Trove: ${pageTitle}</title>
  <c:import url="../fragments/headers/commonHead.jsp"/>
  <c:import url="../fragments/headers/feedsHead.jsp"/>
  <c:import url="../fragments/analytics/all.jsp"/>
</head>

<body>
  <script>TROVE.setToken("${_csrf.token}");</script>
  <c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
  <c:import url="../fragments/nav/topNavBar.jsp"/>
  <c:import url="../fragments/handlebars/mainFeedItemCards.jsp"/>  
  <c:import url="../fragments/forms/addToCartForm.jsp"/>

  <c:if test="${not empty category}">
    <c:choose>
      <c:when test="${category == 'BRACELET'}"><c:import url="../fragments/banners/braceletBanner.jsp"/></c:when>
      <c:when test="${category == 'RING'}"><c:import url="../fragments/banners/ringBanner.jsp"/></c:when>
      <c:when test="${category == 'NECKLACES'}"><c:import url="../fragments/banners/necklacesBanner.jsp"/></c:when>
      <c:when test="${category == 'FEATURED'}"><c:import url="../fragments/banners/featuredBanner.jsp"/></c:when>
      <c:otherwise>    </c:otherwise>
    </c:choose>
  </c:if>

  <div class="container-fluid"><div class="row items"></div></div>
  <c:import url="../fragments/vendor/npro.jsp"/>
  <c:choose>
    <c:when test="${isAuthenticated}"> 
      <script>
        var authUserId = ${authUser.userId};
        function troveb(itemid) {
          mixpanel.track("productcategory_trove", {
            "item": itemid
          });
        };
        function buyb(itemid) {
          document.getElementById("itemId").value = itemid;
          var s = document.getElementById("sizee-"+String(itemid));
          document.getElementById("size").value = s.options[s.selectedIndex].text;
          var m = document.getElementById("dismenu-"+String(itemid));
          var mater = m.options[m.selectedIndex].value.split(" ");
          document.getElementById("materialId").value = mater[0];
          document.getElementById("finishId").value = mater[1];
          mixpanel.track("productcategory_buy", {
            "item": itemid
          });
        };
        function nameb(itemid) {
          mixpanel.track("productcategory_username", {
            "item": itemid
          });
        };
        function collectionb(userid) {
          mixpanel.track("productcategory_collection", {
           "owner": userid
          });
        };
        function imgb(itemid) {
          mixpanel.track("productcategory_image", {
            "item": itemid
          });
        };
        function custb(itemid) {
          mixpanel.track("productcategory_customize", {
            "item": itemid
          });
        };
        function landed() {
          mixpanel.track("regularfeed");
        }
      </script>  
    </c:when>
    <c:otherwise>
      <script>
        function trovebANON(itemid) {
          mixpanel.track("productcard_customize_ANON", {
            "item": itemid
          });
        };
        function buybANON(itemid) {
          mixpanel.track("productcard_buy_ANON", {
            "item": itemid
          });
        };
        function namebANON(itemid) {
          mixpanel.track("productcard_username_ANON", {
            "item": itemid
          });
        };
        function collectionbANON(itemid) {
          mixpanel.track("productcard_customize_ANON", {
            "item": itemid
          });
        };
        function imgbANON(itemid) {
          mixpanel.track("productcard_image_ANON", {
            "item": itemid
          });
        };
        function custbANON(itemid) {
          mixpanel.track("productcard_customize_ANON", {
            "item": itemid
          });
        };
        function landed() {
          mixpanel.track("regularfeed_ANON");
        }
      </script>                              
    </c:otherwise>
  </c:choose>
  
  <script>
    function updateId(itemid) {
      document.getElementById("itemId").value = itemid;
      alert(itemid);
    }

    function updateSize(itemid) {
      var e = document.getElementById("sizee-"+String(itemid));
      document.getElementById("size").value = e.options[e.selectedIndex].text;
    }

    function updateMaterial(itemid) {
      var e = document.getElementById("dismenu-"+String(itemid));
      var mater = e.options[e.selectedIndex].value.split(" ");
      document.getElementById("materialId").value = mater[0];
      document.getElementById("finishId").value = mater[1];
    }

    function featureditemClick(featuredlink) {
      mixpanel.track("featured_click", {
        "featuredlinkid": featuredlink
      });
    };

    var cardData;
    var pageNum = 0;
    var itemsPerPage = 12;
    var processing;

    function getFeedItems() {
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
            success: function(data) {
              if ( data.itemList.length == 0 ) {
                $('.row.items').css({
                  'height': $(document).height()+100+'px'
                });
                cardData = data.itemList;
              } else {
                cardData = data.itemList;
                pageNum = pageNum + 1;
                processing = false;
              }
            }
        }).done( function() {
          layoutBars();
        });
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
            success: function(data) {
              cardData = data.itemList;
              pageNum = pageNum + 1;
              processing = false;
            }
        }).done( function() {
          layoutBars();
        });
    }
    
    function layoutBars() {
      var t = Handlebars.compile($('#itemcards').html());
      Handlebars.registerHelper('unless_blank', function(item, block) {
        return (item && String(item).replace(/\s/g,"").length) ? block.fn(this) : block.inverse(this);
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
      colCount = Math.floor(Math.min(windowWidth/(colWidth+margin*2), 4));
      spaceLeft = (windowWidth - ((colWidth*colCount)+(margin*(colCount-1)))) / 2 + 15;
      for(var i=0;i<colCount;i++){
        blocks.push(margin);
      }
      positionCards(selector);
    }

    function positionCards(selector) {
      $(selector).each(function(i){
        var min = Array.min(blocks);
        var index = $.inArray(min, blocks);
        var leftPos = margin+(index*(colWidth+margin));
        $(this).css({
          'left':(leftPos+spaceLeft)-40+'px',
          'top':min+$(".collection-container").height()+$(".banner").height()+$(".navbar").height()+20+'px',
          'display':'inherit',
          'opacity':1
        });
        blocks[index] = min+$(this).outerHeight()+margin;
      }); 
    }

    function hideFade() {
      $('.faderr').css({
          'opacity':0
      });
    }

    function showFade() {
      $('.faderr').css({
          'opacity':1
      });
    }
    
    Array.min = function(array) {
        return Math.min.apply(Math, array);
    };

    $(window).load(function() {          NProgress.inc(0.1);   });
    $(document).ready(function() {       NProgress.done();     });  // start if not started otherwise inc
    $(document).ajaxStart(function() {   showFade();           });  
    $(document).ajaxStop(function() {    hideFade();           });


  </script>

  <%-- determin if we are on the feed or a category page, and if so which one --%>

  <c:if test="${not empty category}">
    <c:choose>
      <c:when test="${category == 'BRACELET'}"> 
        <script>
          getCategoryItems('BRACELET');
          head.ready(document, function () {
            $('.cardbuybutton').on( 'click', function( event ) {
              var arr = $(this).attr('id').split("-");
              updateId(arr[1]);
              updateSize(arr[1]);
              updateMaterial(arr[1]);
            });
            $(document).scroll(function(e){
              if (processing)
                return false;
              if ($(window).scrollTop() >= ($(document).height() - $(window).height())*0.65){
                processing = true;
                getCategoryItems('BRACELET');
              }
            });
            $(window).on('resize', function() {
              setupCards('.card');
            });  
          });
        </script>  
      </c:when>
      <c:when test="${category == 'RING'}"> 
        <script>
          getCategoryItems('RING');
          head.ready(document, function () {
            $('.cardbuybutton').on( 'click', function( event ) {
              var arr = $(this).attr('id').split("-");
              updateId(arr[1]);
              updateSize(arr[1]);
              updateMaterial(arr[1]);
            });
            $(document).scroll(function(e){
              if (processing)
                return false;
              if ($(window).scrollTop() >= ($(document).height() - $(window).height())*0.65){
                processing = true;
                getCategoryItems('RING');
              }
            });
            $(window).on('resize', function() {
              setupCards('.card');
            });  
          });
        </script>  
      </c:when>
      <c:when test="${category == 'NECKLACES'}"> 
        <script>
          getCategoryItems('NECKLACES');
          head.ready(document, function () {
            $('.cardbuybutton').on( 'click', function( event ) {
              var arr = $(this).attr('id').split("-");
              updateId(arr[1]);
              updateSize(arr[1]);
              updateMaterial(arr[1]);
            });
            $(document).scroll(function(e){
              if (processing)
                return false;
              if ($(window).scrollTop() >= ($(document).height() - $(window).height())*0.65){
                processing = true;
                getCategoryItems('NECKLACES');
              }
            });
            $(window).on('resize', function() {
              setupCards('.card');
            });  
          });
        </script>  
      </c:when>
      <c:when test="${category == 'FEATURED'}"> 
        <script>
          getCategoryItems('FEATURED');
          head.ready(document, function () {
            $('.cardbuybutton').on( 'click', function( event ) {
              var arr = $(this).attr('id').split("-");
              updateId(arr[1]);
              updateSize(arr[1]);
              updateMaterial(arr[1]);
            });
            $(document).scroll(function(e){
              if (processing)
                return false;
              if ($(window).scrollTop() >= ($(document).height() - $(window).height())*0.65){
                processing = true;
                getCategoryItems('FEATURED');
              }
            });
            $(window).on('resize', function() {
              setupCards('.card');
            });  
          });
        </script>
      </c:when>
      <c:otherwise>
        <script>
          getFeedItems();
          head.ready(document, function () {
            $('.cardbuybutton').on( 'click', function( event ) {
              var arr = $(this).attr('id').split("-");
              updateId(arr[1]);
              updateSize(arr[1]);
              updateMaterial(arr[1]);
            });
            $(document).scroll(function(e){
              if (processing)
                return false;
              if ($(window).scrollTop() >= ($(document).height() - $(window).height())*0.65){
                processing = true;
                getFeedItems();
              }
            });
            $(window).on('resize', function() {
              setupCards('.card');
            });  
          });
        </script>                             
      </c:otherwise>
    </c:choose>
  </c:if>

</body>
</html>
