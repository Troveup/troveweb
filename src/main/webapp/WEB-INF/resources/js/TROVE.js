(function(){
    namespace("TROVE", followUser, unfollowUser, setToken, bumpCount, troveItem, updateItemCard, renameItem, updateItemDescription, buyItem, setupCards, positionCards, removeItem, addFromCard );

    // make csrf token available to TROVE
    var csrfToken;
    function setToken(token) {
        csrfToken = token;
    }

    // USER
    // Follow a user
    function followUser(userid, fromview) {
        var fromv = fromview;
        if ( fromv == 'productdescription') {
            mixpanel.track("madeby_follow", {
                "user": userid
            });
        }

        if ( fromv == 'trovefollowing') {
            mixpanel.track("following_follow", {
                "user": userid
            });
        }


        if ( fromv == 'trovefollowers') {
            mixpanel.track("followers_follow", {
                "user": userid
            });
        }

        var f = "folbtn" + String(userid);
        var fs = ".folbtn" + String(userid);
        var fsf = "TROVE.followUser('" + String(userid) + "','" + String(fromview) + "');";
        var u = "ufolbtn" + String(userid);
        var us = ".ufolbtn" + String(userid)
        var usf = "TROVE.unfollowUser('" + String(userid) + "','" + String(fromview) + "');";

        var data = new FormData();
        data.append("userId", userid);
        jQuery.ajax({
            url: '/followuser',
            data: data,
            cache: false,
            contentType: false,
            processData: false,
            headers: {
                'Access-Control-Allow-Origin': '*',
                "X-CSRF-TOKEN": csrfToken
            },
            type: 'POST',
            success: function(data) {
                $(fs).html('Unfollow');
                $(fs).attr( "onclick", usf );
                $(fs).attr( "id", "ufolwbutton" );
                $(fs).addClass('btn--lightgray');
                $(fs).addClass(u);
                $(fs).removeClass('btn--red');
                $(fs).removeClass(f);
                bumpCount('.folwng','up');
            }
        });
    }

    // unfollow a user
    function unfollowUser(userid, fromview) {
        var fromv = fromview;
        if (fromview == 'productdescription') {
            mixpanel.track("madeby_unfollow", {
                "user": userid
            });
        }

        if ( fromv == 'trovefollowing') {
            mixpanel.track("following_unfollow", {
                "user": userid
            });
        }


        if ( fromv == 'trovefollowers') {
            mixpanel.track("followers_unfollow", {
                "user": userid
            });
        }

        var f = "folbtn" + String(userid);
        var fs = ".folbtn" + String(userid);
        var fsf = "TROVE.followUser('" + String(userid) + "','" + String(fromview) + "');";
        var u = "ufolbtn" + String(userid);
        var us = ".ufolbtn" + String(userid)
        var usf = "TROVE.unfollowUser('" + String(userid) + "','" + String(fromview) + "');";
       
        var data = new FormData();
        data.append("userId", userid);
        jQuery.ajax({
            url: '/unfollowuser',
            data: data,
            cache: false,
            contentType: false,
            processData: false,
            headers: {
                'Access-Control-Allow-Origin': '*',
                "X-CSRF-TOKEN": csrfToken
            },
            type: 'POST',
            success: function(data) {
                $(us).html('Follow');
                $(us).attr( "onclick", fsf );
                $(us).attr( "id", "folwbutton" );
                $(us).addClass('btn--red');
                $(us).addClass(f);
                $(us).removeClass('btn--lightgray');
                $(us).removeClass(u);
                bumpCount('.folwng','down');
            }
        });
    }




    // ITEM 
    // rename an item
    function renameItem( itemid, newname ) {
        mixpanel.track("productdescription_editname", {
            "item": itemid,
            "itemname": newname
        });
        if (typeof location.origin === 'undefined') { location.origin = location.protocol + '//' + location.host; }
        $.ajax({
            url: location.origin + "/updateitemname",
            type: 'post',
            data: {
                itemId: itemid,
                itemDescription: newname
            },
            headers: {
                "X-CSRF-TOKEN": csrfToken
            },
            dataType: 'json',
            success: updateItemCard( itemid, 'itemDescriptionUpdate' )
        });
    }

    var firstDescrip = true;
    // update an item's description
    function updateItemDescription( itemid, description ) {

        if ( firstDescrip ) {
            firstDescrip = false;
        } else {
            mixpanel.track("productdescription_editabout", {
                "item": itemid
            });
        }

        if (typeof location.origin === 'undefined') { location.origin = location.protocol + '//' + location.host; }
        $.ajax({
            url: location.origin + "/updateitemdescription",
            type: 'post',
            data: {
                itemId: itemid,
                itemDescription: description
            },
            headers: {
                "X-CSRF-TOKEN": csrfToken
            },
            dataType: 'json',
            success: updateItemCard( itemid, 'itemDescriptionUpdate' )
        });
    }

    // add an item to the current user's trove from the item card
    function troveItem(itemid, fromwhere) {

        mixpanel.track("productcard_trove", {
            "item": itemid,
            "fromwhere": fromwhere
        });

        if (typeof location.origin === 'undefined') { location.origin = location.protocol + '//' + location.host; }
        $.ajax({
            url: location.origin + "/addtotrove",
            type: 'post',
            data: {
                itemId: itemid
            },
            headers: {
                "X-CSRF-TOKEN": csrfToken
            },
            dataType: 'json',
            success: updateItemCard( itemid, 'troveItem' )
        });
    }

    function removeItem(itemid) {
        if (typeof location.origin === 'undefined') { location.origin = location.protocol + '//' + location.host; }
        $.ajax({
            url: location.origin + "/removefromtrove",
            type: 'post',
            data: {
                itemId: itemid
            },
            headers: {
                "X-CSRF-TOKEN": csrfToken
            },
            dataType: 'json',
            success: updateItemCard( itemid, 'removeItem' )
        });
    }

    function buyItem(itemid) {
        mixpanel.track("productcard_buy", {
            "item": itemid
        });

        $('.spinner.addtocartfromcard-'+String(itemid)).fadeIn();
        $('.addtocartfromcardstuff-'+String(itemid)).hide();
        var $form = $('#add_to_cart_form');
        var data = $form.serialize();

        var promoCode = document.getElementById("promoCode").value;
        var ftueCheckoutId = document.getElementById("ftueCheckoutId").value;
        var data = new FormData();
        data.append("promoCode", promoCode);
        data.append("ftueCheckoutId", ftueCheckoutId);


        $.ajax({
            type: 'POST',
            data: data,
            url:  '/noncustaddtobag',
            success:function(data) {
              $('.modal-'+String(itemid)).removeClass("in");
              $('.modal-backdrop').removeClass("in");
              $('.spinner.addtocartfromcard-'+String(itemid)).hide();
              $('.addtocartfromcardstuff-'+String(itemid)).fadeIn();
              updateItemCard( itemid, 'buyItem');
            }
        });
    }

    function addFromCard(itemid) {
      var materialId = document.getElementById("materialId").value;
      var finishId = document.getElementById("finishId").value;
      var size = document.getElementById("size").value;
      var chain = $('#chainId').val();

      if (document.getElementById("engravetext-" + itemid))
        var engraveText = document.getElementById("engravetext-" + itemid).value;
      else
        engraveText = "";

      var data = new FormData();
      data.append("itemId", itemid);
      data.append("materialId", materialId);
      data.append("finishId", finishId);
      data.append("size", size);
      data.append("engraveText", engraveText);
      data.append("chain", chain);
      jQuery.ajax({
          url: '/noncustaddtobag',
          data: data,
          cache: false,
          contentType: false,
          processData: false,
          headers: {
              'Access-Control-Allow-Origin': '*',
              "X-CSRF-TOKEN": csrfToken
          },
          type: 'POST',
          success: function() {
              $('.addtocartfromcardstuff-'+String(itemid)).fadeIn();
              updateItemCard( itemid, 'buyItem');

              if (document.getElementById("engravetext-" + itemid)) {
                  document.getElementById("engravetext-" + itemid).value = "";
              }
          }
      });
    }

    // updates the item card in the view depending on which action is passed
    function updateItemCard( itemid, action ) {

        switch (action) {

            // trove an item from an item card
            case 'troveItem':
                document.getElementById('troveItem-' + itemid).onclick = null;
                $('#troveItem-'+itemid).text('Troved');
                $('#trovedAlert-'+itemid).addClass('opa');
                $('.tallyho-profile').addClass('showitnow');
                bumpCount('.tallyho-profile','up');
                bumpCount('.trovedCount-'+itemid ,'up');
                setTimeout(function() {
                    $('#trovedAlert-'+itemid).removeClass('opa');
                }, 2000);
                break;

            case 'updateTrovedItem':
                $('#updateAlert-'+itemid).addClass('opa');
                setTimeout(function() {
                    $('#updateAlert-'+itemid).removeClass('opa');
                }, 2000);
                break;

            case 'remove':
                $('#removeAlert-'+itemid).addClass('opa');
                break;
            // remove an item from an item card
            case 'removeItem':
                bumpCount('.item-count.value','down');
                $('#removeAlert-'+itemid).removeClass('opa');
                $('.item-'+itemid).fadeOut();
                $('.modal2-'+String(itemid)).removeClass("in");
                $('.modal-backdrop').remove();
                $('body').removeClass("modal-open");
                $('.item-'+itemid).remove();
                setupCards('profile');
                break;
                // remove an item from an item card
            case 'cancelRemoveItem':
                $('#removeAlert-'+itemid).removeClass('opa');
                break;
            // buy an item from an item card
            case 'buyItem':
                $('#buyAlert-'+itemid).addClass('opa');
                $('.tallyho-cart').addClass('showitnow');
                bumpCount('.tallyho-cart','up');
                setTimeout(function() {
                    $('#buyAlert-'+itemid).removeClass('opa');
                }, 2000);
                break;
            default:
                console.log(action);
                break;
        }
    }

    // CARDS


    var colCount = 0;
    var colWidth = 250;
    var margin = 30;
    var spaceLeft = 0;
    var windowWidth = 0;
    var blocks = [];

    function setupCards(view) {
        windowWidth = $(window).width();
        blocks = [];
        colCount = Math.floor(windowWidth/(colWidth+margin*2));
        spaceLeft = (windowWidth - ((colWidth*colCount)+(margin*(colCount-1)))) / 2 + 15;
        for(var i=0;i<colCount;i++){
            blocks.push(margin);
        }
        positionCards(view);
    }

    function positionCards(view) {

        switch (view) {

            case 'feed':
                $('.block').each(function(i){
                    var min = Array.min(blocks);
                    var index = $.inArray(min, blocks);
                    var leftPos = margin+(index*(colWidth+margin));
                    $(this).css({
                        'left':(leftPos+spaceLeft)-40+'px',
                        'top':min+$(".banner-container").height()+$(".navbar").height()+10+'px',
                        'display':'inherit'
                    });
                    blocks[index] = min+$(this).outerHeight()+margin;
                }); 
                break;

            case 'banner':
                $('.block').each(function(i){
                    var min = Array.min(blocks);
                    var index = $.inArray(min, blocks);
                    var leftPos = margin+(index*(colWidth+margin));
                    $(this).css({
                        'left':(leftPos+spaceLeft)-40+'px',
                        'top':min+$(".browse-container").height()+$(".navbar").height()+$(".banner").height()+'px',
                        'display':'inherit'
                    });
                    blocks[index] = min+$(this).outerHeight()+margin;
                });
                break;

            case 'profile':
                $('.block').each(function(i){
                    var min = Array.min(blocks);
                    var index = $.inArray(min, blocks);
                    var leftPos = margin+(index*(colWidth+margin));
                    $(this).css({
                      'left':(leftPos+spaceLeft)-40+'px',
                      'top':min+$(".profile-container").height()+$(".navbar").height()+10+'px',
                      'display':'inherit'
                    });
                    blocks[index] = min+$(this).outerHeight()+margin;
                }); 
                break;
        }

    }


    // MISC

    // Function to get the Min value in Array
    Array.min = function(array) {
        return Math.min.apply(Math, array);
    }
        

    // Function to get increment a counter 
    function bumpCount(selector, direction) {
        var elm = $(selector);
        var count = parseInt(elm.html());

        switch (direction) {
            case 'up':
                elm.html(String(count + 1));
                break;
            case 'down':
                elm.html(String(count - 1));
                break;
        }
    }



    


})();
