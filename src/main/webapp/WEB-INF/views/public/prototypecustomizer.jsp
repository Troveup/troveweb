<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="trove" uri="http://troveup.com/jsp/tlds/itemimage" %>

<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=0">
    <link rel="icon" href="/resources/img/favicon.png?v=2">
    <link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Raleway:400,200,300,500,600,700' type='text/css'>
    <link href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="/resources/stylesheets/jquery-ui.min.css" rel="stylesheet">
    <link href="/resources/stylesheets/jquery-ui.structure.min.css" rel="stylesheet">
    <c:import url="../fragments/headers/customizer/jqueryMobileHead.jsp"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
    <link href="/resources/stylesheets/sliders/base.css" rel="stylesheet">
    <script type="text/javascript" src="/resources/js/vendor/head.js"></script>
    <script type="text/javascript" src="/resources/js/namespace.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.3/handlebars.min.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
    <link href="/resources/vendor/nouislider/nouislider.min.css" rel="stylesheet">
    <script type="text/javascript" src="/resources/vendor/nouislider/nouislider.min.js"></script>
    <link href="/resources/stylesheets/main.css" rel="stylesheet">
    <script type="text/javascript" src="/resources/js/FORGE.js"></script> 
    <script type="text/javascript" src="/resources/js/TROVE.js"></script>
    <c:import url="../fragments/analytics/all.jsp"/>
    <script>TROVE.setToken("${_csrf.token}");</script>
    <link href="/resources/stylesheets/trovecustomizer.css" rel="stylesheet">

    <style>
      * {
        outline: none !important;
        font-family: "Raleway", Helvetica, Arial, sans-serif;
        letter-spacing: 1px;
      }

      .btn {
        background-color: #8E8B8B;
        display: block;
        padding: 10px 0px;
        margin: 5px 0px;
        font-size: 14px;
        font-weight: normal;
        border: 0px solid #FFF;
        border-radius: 2px;
        width: 100%;
        -webkit-transition: all 0.3s ease-in-out;
           -moz-transition: all 0.3s ease-in-out;
            -ms-transition: all 0.3s ease-in-out;
             -o-transition: all 0.3s ease-in-out;
                transition: all 0.3s ease-in-out;
      }

      .btn:hover {
        background-color: #B7B0B0;
      }

      .btn.btn-red {
        background-color: #DD2435;
      }

      .btn.btn-red:hover {
        background-color: #F26868;
        color: #fff;
      }

      .btn img {
        position: relative;
        height: 14px;
        width: 14px;
        top: -2px;
        margin-right: 2px;
      }

      .form-control {
        display: block;
        width: 100%;
        height: 3em;
        padding: 6px 12px;
        margin: 6px 5px;
        font-size: 14px;
        border: 1px solid #DEDEDE;
        border-radius: 0px;
        -webkit-box-shadow: none;
        box-shadow: none;
        -webkit-transition: none;
             -o-transition: none;
                transition: none;
      }

      .renderChannel {
        border-top: 0px solid #F0F0F0;  
      }

      .card_remake_title {
        font-size: 18px;
        padding: 0px;
        border: 0px;
        text-align: center;
        color: #3B3A3A;
        font-weight: 400;
        margin-bottom: 40px;
        margin-top: 55px;
      }

      .card_remake_title.less {
        font-size: 16px;
        margin-top: 60px;
        margin-bottom: 35px;;
      }

      .card_remake_title.more {
        font-size: 22px;
        margin-top: 10px;
        margin-bottom: 20px
      }

      .ui-page {
        margin-top: 50px;
      }

      #materialFaker {
        display: none;
      }

      .ui-mini {
        margin: .446em 0px;
      }

      .item-size-container {
        width: 100%;
        height: inherit;
        display: block;
        margin: 0px;
      }

      @media screen and (max-width: 468px) {
        .acceptButton {
          position: fixed;
          width: 100%;
          left: 0px;
          bottom: 0px;
          z-index: 999;
          border-radius: 0px;
          height: 60px;
          font-size: 18px;
          background-color: #f16868;
          margin-bottom: 0px;
        }
      }

      .navbar.navbar-trove.navbar-fixed-top {
        border-bottom: 2px solid #DEDEDE;
      }

      .cent-label {
        text-align: center;
        display: block;
        text-transform: uppercase;
        font-size: 90%;
        margin-top: 30px;
      }

      .old-size-label {
        text-align: center;
        display: block;
        padding: 9px;
        font-size: 90%;
        border: 1px solid #DEDEDE;
        margin-top: 10px;
        margin-bottom: 20px;
        color: #444343;
        margin-left: 40px;
        margin-right: 40px;
      }

      .product-cta-nav.hidemob {
        margin-top: 40px;
      }

      .ui-select.ui-mini {
        margin: .446em 0px;
        margin-left: 40px;
        margin-right: 40px;
        width: calc(100% - 80px);
      }

      .free-shipping-text {
        display: none;
      }

      #backButton {
        position: fixed;
        top: 10px;
        left: 90px;
        z-index: 999999;
        max-width: 168px;
        background-color: #DEDEDE;
        color: #8C8C8C;
        font-size: 80%;
        opacity: 0.7;
        padding-left: 10px;
      }

      #backButton:hover {
        opacity: 1.0;
      }

      #backButton:after {
        content: "\f3d2";
        color: #8C8C8C;
        background-color: transparent;
        background-position: center center;
        background-repeat: no-repeat;
        -webkit-border-radius: 1em;
        border-radius: 1em;
        background-image: none;
        position: absolute;
        display: block;
        width: 22px;
        height: 22px;
        text-indent: 4px;
        font-size: 15px;
        line-height: 24px;
        font-family: Ionicons;
        text-shadow: none;
        top: 6px;
        left: 5px;
      }


      @media screen and (max-width: 768px) {
        .nav-center {
          margin: 0px;
          float: none;
          border-bottom: 0px solid;
        }
        .product-cta-nav.hidemob {
          display: none;
        }
        #backButton {
          display: none;
        }
      }

      .tallyho-cart {
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

      .tallyho-cart.showitnow {
        opacity: 1.0;
        display: block !important;
      }

      @media screen and (min-width: 767px) {
        .can-hold {
          border: 2px solid #DEDEDE;
        }
        #nav-col {
          display: none !important;
        }   
        .nav-center {
          padding-bottom: 10px;
        }    
      }

    </style>
    
  </head>
  <body>

    <div data-role="page" class="ui-page ui-page-active">
      <c:import url="../fragments/nav/topNavBar.jsp"/>
      <input id="csrfToken" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

      <a href="/private/orders" data-role="none" id="backButton" type="button" class="btn btn-back-text"> Back to Your Orders</a>
      <div class="modal fade group-modal" id="group-modal" tabindex="-1" role="dialog" aria-labelledby="groupModalALabel" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content" id="authModalContent">
            <div class="modal-header">
              <span class="modalttoptext">Customization Group</span>
              <a href="#" data-dismiss="modal" class="closeModal"><i class="ion-android-close"></i></a>
            </div>
            <div class="modal-body">
              <ul class="nav navbar-nav cust-group-options"></ul>
            </div>
            <div class="modal-footer"></div>
          </div>
        </div>
      </div>

      <div class="modal fade size-modal" id="size-modal" tabindex="-1" role="dialog" aria-labelledby="sizeModalALabel" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content" id="authModalContent">
            <div class="modal-header">
              <span class="modalttoptext">Size</span>
              <a href="#" data-dismiss="modal" class="closeModal"><i class="ion-android-close"></i></a>
            </div>
            <div class="modal-body">
              <ul class="nav navbar-nav cust-size">
                <c:if test="${not empty sizes}">
                  <c:forEach var="individualsize" items="${sizes}">
                    <li class="sizefor sizeformodal" id="${individualsize['key']}"><a href="#" class="fakedownsize" id="${individualsize['key']}" onclick="sizeit('${individualsize['key']}','${individualsize['value']}');">${individualsize['value']}</a></li>
                  </c:forEach>
                </c:if>
              </ul>
            </div>
            <div class="modal-footer"></div>
          </div>
        </div>
      </div>


      <div class="container-fluid mobile">
        <div class="row">

          <nav id="sizenav" class="navbar navbar-inverse navbar-fixed-bottom size" role="navigation">
            <div class="container-fluid">
              <div class="navbar-header">
                <button data-role="none" type="button" class="navbar-toggle cont size" data-toggle="modal" data-target="#size-modal">
                  <i class='ion-arrow-up-b'></i>
                </button>
                <a class="navbar-brand size" href="#">SIZE</a>
              </div>
            </div>
          </nav>

          <nav id="groupcustnav" class="navbar navbar-inverse navbar-fixed-bottom" role="navigation">
            <div class="container-fluid">
              <div class="navbar-header">
                <button data-role="none" type="button" class="navbar-toggle cont custom spec" data-toggle="modal" data-target="#group-modal">
                  <i class='ion-cube'></i>
                </button>
              </div>
            </div>
          </nav>

          <nav id="samplecustnav" class="navbar navbar-inverse navbar-fixed-bottom" role="navigation">
            <div class="container-fluid">
              <div class="navbar-header topper">
                <button data-role="none" type="button" class="navbar-toggle cont custom ization" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                  <i class='ion-chevron-up'></i>
                </button>
                <a class="navbar-brand custom" href="#">Customization 1</a>
              </div>
              <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav cust-options"></ul>
              </div>
            </div>
          </nav>

        </div>
      </div>



      <div class="container cards_product_wrapper customizer">

      
        <div class="col-sm-8 col-sm-push-4 col-md-6 col-md-push-3">
          <div class="hidden-xs card_remake_title more customizer hidemob">Try-on Model Editor</div>
          <div class="can-hold">
            <c:import url="../fragments/cust/canvas.jsp"/>
          </div>
        </div>
        <div class="col-sm-4 col-sm-pull-8 col-md-3 col-md-pull-6">
          <div class="card_remake_title less customizer hidemob">Customizations</div>
          <c:import url="../fragments/cust/controls.jsp"/>
        </div>
        <div class="col-sm-12 col-md-3">
          <div class="hidden-xs card_remake_title customizer hidemob">${item.itemName}</div>
          <div id="materialFaker" class="hidden-xs material-selector-container">   
            <span class="selector-title">Material</span>
            <div class="material-selector">
              <div class="ui-select ui-mini">
                <div id="webmenu-button" class="ui-btn ui-icon-carat-d ui-btn-icon-right ui-corner-all ui-shadow">
                  <span>Precious Plated - 18k Gold Plated</span>
                  <select data-role="none" data-mini="true" name="webmenu" id="webmenu">
                    <option selected="true" value="1 98" data-forge="gold">Gold - 18k Gold</option>
                    <option value="1 91" data-forge="gold">Gold - 14k Gold</option>
                    <option value="1 96" data-forge="rosegold">Gold - 14k Rose Gold</option>
                    <option value="1 97" data-forge="silver">Gold - 14k White Gold</option>
                    <option value="2 81" data-forge="silver">Silver - Premium</option>
                    <option value="3 112" data-forge="gold">Precious Plated - 18k Gold Plated</option>
                    <option value="3 110" data-forge="gold">Precious Plated - 14k Gold Plated</option>
                    <option value="3 111" data-forge="rosegold">Precious Plated - 14k Rose Gold Plated</option>
                    <option value="3 113" data-forge="silver">Precious Plated - Rhodium Plated</option>
                    <option value="5 87" data-forge="rosegold">Bronze - Polished</option>
                    <option value="6 6" data-forge="silver">Try-On - White Strong &amp; Flexible</option>
                  </select>
                </div>
              </div>
            </div>
          </div>

          


          <div class="hidden-xs item-size-container">
            <!-- need this here (temporarily) as a hook for Page module to add the toggle -->

            <span class="cent-label">Original Size</span>
            <span class="old-size-label">${size}</span>
            <span class="cent-label">New Size</span>
            <div data-mini="true"class="item-size">
              <select data-mini="true" name="size" id="size">
                <c:if test="${not empty sizes}">
                  <c:forEach var="individualsize" items="${sizes}">
                    <option value="${individualsize['key']}">${individualsize['value']}</option>
                  </c:forEach>
                </c:if>
              </select>
            </div>
            <c:if test="${not empty availableChains}">
              <div class="chain-container">
                <span class="selector-title">Chain</span>
                <div class="material-selector">
                  <select data-mini="true" name="chain" id="chain">
                    <c:forEach var="chain" items="${availableChains}">
                      <option value="${chain.chainId}" selected>${chain.name} - ${chain.price}</option>
                    </c:forEach>
                  </select>
                </div>
              </div>
            </c:if>
          </div>

          <div class="holdprice hidden-xs " style="display:none;">
            <div class="item-price-container">
              <span class="selector-title price">Price</span>
              <div class="item-price">-</div>
            </div>
          </div>

          <div class="product-cta-nav hidemob">
            <c:if test="${canRequestPrototype}">
              <button data-role="none" id="sendPrototype" type="button" class="btn btn-red acceptButton">Send Another Try-On</button>
            </c:if>
            <button data-role="none" id="sendFinal" type="button" class="btn btn-red acceptButton">Send Final Jewelry</button>
          </div>
        </div>
      </div>
    </div>

      <script>

        $("#webmenu").click(function () {
          mixpanel.track("customizer_material", {
            "itemName": "${item.itemName}"
          });
        });

        $("#size").click(function () {
          mixpanel.track("customizer_size", {
            "itemName": "${item.itemName}",
            "size": this.value
          });
        });

      </script>



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
        $('#engravehint').popover({trigger: "hover click"});

        function setUpMobileUI() {
          $('#controls').trigger('create');

          var first = true;
          var firstt = true;
          $(".groupContainer").each(function(){
            var gro = $(this).attr('id');
            var groptitle = gro.replace(" ", "_").replace(" ", "_").replace(" ", "_").replace(".", "_").replace(".", "_");
            $(this).attr('id', groptitle);
            var grouptitle = groptitle.substring(10,groptitle.length);
            var group = grouptitle.substring(2,grouptitle.length);
            group = group.replace("_", " ").replace("_", " ").replace("_", " ").replace(".", " ").replace(".", " ");
            if (firstt) {
              $("ul.cust-group-options").append('<li><a class="fakeup activee" href="#" id="' + grouptitle +'" onclick="fakeitt(\'' + String(grouptitle) + '\');">' + group + '</a></li>');
            } else {
              $("ul.cust-group-options").append('<li><a class="fakeup" href="#" id="' + grouptitle + '" onclick="fakeitt(\'' + String(grouptitle) + '\');">' + group + '</a></li>');
            }
            $(this).children(".sliderWrapper").each(function(){
              var temptit = $(this).attr('id');
              var temptitle = temptit.replace(" ", "_").replace(" ", "_").replace(" ", "_").replace(".", "_").replace(".", "_");
              $(this).attr('id', temptitle);
              var nametit = $(this).children('.subHeading').html();
              var title = temptitle.substring(12,temptitle.length);
              var tit = title.substring(2,title.length);
              tit = tit.replace("_", " ").replace("_", " ").replace("_", " ").replace(".", " ").replace(".", " ");
              if (firstt) {
                if (first) {
                  $(this).addClass('activated');
                  $(".navbar-brand.custom").html(nametit);
                  $("ul.cust-options").append('<li class="slideopt ' + grouptitle + ' showme"><a class="fakedown activ" href="#" id="' + title +'" onclick="fakeit(\'' + String(title) + '\');">' + nametit + '</a></li>');
                  first = false;
                } else {
                  $("ul.cust-options").append('<li class="slideopt ' + grouptitle + ' showme"><a class="fakedown" href="#" id="' + title + '" onclick="fakeit(\'' + String(title) + '\');">' + nametit + '</a></li>');
                }
              } else {
                $("ul.cust-options").append('<li class="slideopt ' + grouptitle + '"><a class="fakedown" href="#" id="' + title + '" onclick="fakeit(\'' + String(title) + '\');">' + nametit + '</a></li>');
              }

            });
            firstt = false;

            // handle custom ring size modal case
            var sizenum = $('.sizefor').size();
            if (sizenum <= 10) {
              $('.sizefor').removeClass('sizeformodal');
            }

            $('.sizefor').each(function(){
              var temptit = $(this).attr('id');
              var temptitle = temptit.replace(" ", "_").replace(" ", "_").replace(" ", "_").replace(".", "_").replace(".", "_");
              $(this).attr('id', temptitle);
            });

          });

          var defmat = $('#webmenu').val();
          var defsiz = $('#size').val();
          // matit(defmat);
          // sizeit(defsiz);
          // $(".navbar-toggle.size").click();
          // $(".navbar-toggle.material").click();
          defmat = '#' + defmat.replace(" ", "_");

          if (defsiz) {
            defsiz = '#' + defsiz.replace(" ", "_").replace(" ", "_").replace(" ", "_").replace(".", "_").replace(".", "_");
            $(defsiz).children('a').addClass('activeeee');
          }
          $(defmat).addClass('activeee');


          // $(".sliderWrapper").each(function(){
          //   var temptit = $(this).attr('id');
          //   var temptitle = temptit.replace(" ", "_").replace(" ", "_").replace(" ", "_").replace(".", "_").replace(".", "_");
          //   $(this).attr('id', temptitle);
          //   var title = temptitle.substring(12,temptitle.length);
          //   var tit = title.substring(2,title.length);
          //   tit = tit.replace("_", " ").replace("_", " ").replace("_", " ").replace(".", " ").replace(".", " ");
          //   if (first) {
          //     $(this).addClass('activated');
          //     $(".navbar-brand.custom").html(tit);
          //     $("ul.cust-options").append('<li><a class="fakedown active" href="#" id="' + title +'" onclick="fakeit(\'' + String(title) + '\');">' + tit + '</a></li>');
          //     first = false;
          //   } else {
          //     $("ul.cust-options").append('<li><a class="fakedown" href="#" id="' + title + '" onclick="fakeit(\'' + String(title) + '\');">' + tit + '</a></li>');
          //   }
          // });
        };

        $('.navbar .navbar-collapse').on('show.bs.collapse', function(e) {
          $('.navbar .navbar-collapse').not(this).collapse('hide');
        });

        function sizeit(ide,val) {
          $('#sRingsize').val(String(ide)).trigger('change');
          $('#size').val(String(ide)).trigger('change');
          $(".navbar-brand.size").html(String(val));
          var sizid = String(ide);

          sizid = '#' + sizid.replace(" ", "_").replace(" ", "_").replace(" ", "_").replace(".", "_").replace(".", "_");
          $('.activeeee').removeClass('activeeee');
          $(sizid).children('a').addClass('activeeee');
          $(".navbar-toggle.size").click();
          mixpanel.track("customizer_size", {
            "itemName": "${item.itemName}",
            "size": String(ide)
          });
        };

        function matit(ide) {
          $('#webmenu').val(String(ide)).trigger('change');
          var t = '#' + String(ide);
          t = t.replace(" ", "_");
          var text = $(t).html();
          text = text.split(" - ")
          if ( text[0] == "Gold" || text[0] == "Precious Plated" ) {
            $(".navbar-brand.material").html(text[1]);
          } else {
            $(".navbar-brand.material").html(text[1] + " " + text[0]);
          }
          $('.activeee').removeClass('activeee');
          $(t).addClass('activeee');
          $(".navbar-toggle.material").click();
          var matswitch = text[1] + " " + text[0]
          mixpanel.track("customizer_material", {
            "itemName": "${item.itemName}",
            "material": matswitch

          });
        };

        function fakeit(ide) {
          var ti = '#' + String(ide);
          var newid = '#sliderWrappe' + String(ide);
          var t = ide.substring(2,ide.length).split("_");
          $('.activ').removeClass('activ');
          $('.activated').removeClass('activated');
          $(newid).addClass('activated');
          $(ti).addClass('activ');
          $(".navbar-brand.custom").html(t[0] + " " + t[1]);
          $(".navbar-toggle.custom.ization").click();
        };

        function fakeitt(ide) {
          var ti = '.slideopt.' + String(ide);
          var tid = '#' + String(ide);
          $('.showme').removeClass('showme');
          $('.activee').removeClass('activee');
          $(ti).addClass('showme');
          $(tid).addClass('activee');
          $('.activ').removeClass('activ');
          $('.activated').removeClass('activated');
          $(".navbar-brand.custom").html("Choose One");
          $(".navbar-toggle.custom.spec").click();
          $(".navbar-toggle.custom.ization").click();
        };

        function land() {
          mixpanel.track("land_customizer");
        }

        function updatePrototypeOrder(cartItemId) {
          updateOrder(cartItemId, 'PROTOTYPE');
        }

        function updateFinalOrder(cartItemId) {
          updateOrder(cartItemId, 'REAL');
        }

        function updateOrder(cartItemId, nextOrderType) {
          var data = new FormData();
          var selectedSize = $('#size option:selected');
          var sizeValue = selectedSize.val();
          FORGE.Page.addExportHash(data, sizeValue, null)
          data.append('cartItemId', cartItemId);
          data.append('nextOrderType', nextOrderType);
          data.append('size', sizeValue);
          jQuery.ajax({
            url: '/updateprototypeorder',
            data: data,
            cache: false,
            contentType: false,
            processData: false,
            headers: {
              'Access-Control-Allow-Origin': '*',
              "X-CSRF-TOKEN": "${_csrf.token}"
            },
            type: 'POST',
            success: function(data) {
              window.location = "/private/orders";
            }
          });
        }


        $(document).ready(function() {
          land();

          $(function() {
            $('[data-toggle="popover"]').popover()
          });

          var count = parseInt($('.tallyho-cart').html());
          if ( count >= 1 ) {
            $('.tallyho-cart').addClass('showitnow');
          }

          var origsize = "${size}";

          $('#size option').filter(function () { return $.trim($(this).text()) == origsize; }).attr('selected', true).change();

          // $.fn.editable.defaults.mode = 'inline';
          // var firstthingsfirst = true;
          // $('#model').editable({
          //     send: 'never',  
          //     title: 'Enter username',
          //     placement: 'right',
          //     toggle: 'manual',
          //     validate: function(value) {
          //       var regex = new RegExp(/^[a-zA-Z0-9 ]+$/);
          //       if (!(value.length < 31))
          //         return "Please shorten your item\'s name to 30 characters or less.";
          //       if (!(regex.test(value)))
          //         return "There are illegal characters in your item\'s name.";
          //       else
          //         return null;
          //     },
          //     display: function(value) {
          //         $('#view').text(value);
          //         if (firstthingsfirst) {
          //           firstthingsfirst = false;
          //         } else {
          //           mixpanel.track("customizer_editname", {
          //             "itemName": "${item.itemName}",
          //             "newName": value
          //           });
          //         }
          //         // TROVE.renameItem( '${item.itemId}', value );
          //     }
          // });

          // $('#controller').click(function(e){
          //     e.stopPropagation();
          //     $('#model').editable('toggle');
          // });

          // var firstthingsfirstt = true;
          // $('#model2').editable({
          //     send: 'never',  
          //     title: 'Enter username',
          //     placement: 'right',
          //     toggle: 'manual',
          //     display: function(value) {
          //         $('#view2').text(value);
          //         // TROVE.updateItemDescription( '${item.itemId}', value );
          //         if (firstthingsfirstt) {
          //           firstthingsfirstt = false;
          //         } else {
          //           mixpanel.track("customizer_editabout", {
          //             "itemName": "${item.itemName}",
          //             "newDescrip": value
          //           });
          //         }
          //     }
          // });

          // $('#controller2').click(function(e){
          //     e.stopPropagation();
          //     $('#model2').editable('toggle');
          // });

          // $("#button_addToBag").on('click', FORGE.Page.updateCart);
          // $("#button_addToTrove").on('click', FORGE.Page.updateTrove);

          $('#filters').on( 'click', 'a', function( event ) {
            $('a').removeClass('active');
            $(this).addClass('active');
          });
          $('li').on( 'click', 'a', function( event ) {
            $('a').removeClass('active');
            $(this).addClass('active');
          });
          $('.jam').on('mouseover', function() {
            $('.dropdown-container.jam').addClass('showtime');
          });
          $('.jam').on('mouseleave', function() {
            $('.dropdown-container.jam').removeClass('showtime');
          });
          $('.pro').on('mouseover', function() {
            $('.dropdown-container.pro').addClass('showtime');
          });
          $('.pro').on('mouseleave', function() {
            $('.dropdown-container.pro').removeClass('showtime');
          });

        });

        $("#sendFinal").click( function () {
          var cartItemId = ${cartItem.cartItemId};
          updateFinalOrder(cartItemId);
        });

        $("#sendPrototype").click( function () {
          var cartItemId = ${cartItem.cartItemId};
          updatePrototypeOrder(cartItemId);
        })

        $('.fakedown').click(function() {
          return false;
        });

        $("#walkthroughclose").click(function () {
          $('#walkthroughbanner').slideUp("fast");
        });

        // $("#walkthroughopen").click(function () {
        //   $('#walkthroughbanner').slideDown("fast");
        // });

        
        TROVE.setToken("${_csrf.token}");
        FORGE.Page.setCSRFToken("${_csrf.token}");

        FORGE.Page.init({
            selectors: {                                 
                materialSelect: "#webmenu",
                modelParentID: ${item.itemId},
                initialParamsBlock: "#initialParameters",
                canvas: $("#canvas")
            },
            admin: true,
            defaultMaterial: "${item.materialId} ${item.finishId}",
            storageRoot: $("#modelPath").html(),
            activeFilename: $("#modelFilename").html(),
            debug: true,
            isFTUE: false
        }, setUpMobileUI );

      </script>
      <script id="initialParameters" type="application/json">
      ${customizerInput}
      </script>
  </body>
</html>

