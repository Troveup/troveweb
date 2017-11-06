
(function(){
    namespace("FORGE.Page", updateTrove, updateCart, updateCustPriceEstimate, requestCustPriceEstimate, isMobile, setMaterialSelection, readMaterialSelection, fetchPriceDelayed, restartPriceFetch, setupMobile );

    function updateTrove() {
        FORGE.exportModel(false);

        var newcount = parseInt($('.tallyho-profile').html()) + 1;
        $('#button_addToTrove span').text(' Troved');
        $('#bump').addClass('opaa');
        setTimeout(function() {
            $('#bump').removeClass('opaa');
        }, 2000);
    }

    function setupMobile() {
        $(".sliderWrapper").each(function(){
            var temptitle = $(this).attr('id');
            var title = temptitle.substring(13,temptitle.length - 1);
            alert(title);
        });
    }

    function updateCart() {
        FORGE.exportModel(true);
        // document.getElementById('#button_addToBag span').onclick = null;
        var newcount = parseInt($('.tallyho-profile').html()) + 1;
        $('#button_addToBag span').text(' Added');
        $('#bump2').addClass('opaa');
        setTimeout(function() {
            $('#bump2').removeClass('opaa');
        }, 2000);
    }



    var timerVar;
    var responseVar;

    // waits 2 seconds then requests the updated price 
    function fetchPriceDelayed() {
        if ( responseVar == false ) {
            return;
        } else {
            timerVar = setTimeout(function(){ 
                updateCustPriceEstimate();
            }, 2000);
        }        
    }

    // gets fired everytime the material dropdown value changes and when any slider value slides 1 unit
    // cancels the stale fetchPriceDelayed call if its inside of the 2 second window and sends a fresh priceEstimate request
    function restartPriceFetch() {
        $('.item-price').html('updating...');
        clearTimeout(timerVar);
        fetchPriceDelayed();
    }


    function updateCustPriceEstimate() {
        responseVar = false;
        readMaterialSelection();
        if (FORGE.getSetting("isFTUE")) {
            return; // FIXME: total hack, should compartmentalize logic better
        }
        var e = document.getElementById("webmenu");
        var mat = e.options[e.selectedIndex].value.split(" ");
        var data = new FormData();
        data.append("parentItemId", document.getElementById("modelParentID").innerHTML);
        data.append("materialId", mat[0]);
        data.append("finishId", mat[1]);
        // $('.item-price').html('updating...');

        jQuery.ajax({
            url: '/customizerpriceestimate',
            data: data,
            cache: false,
            contentType: false,
            processData: false,
            headers: {
                'Access-Control-Allow-Origin': '*',
                "X-CSRF-TOKEN": $("#csrfToken").val()
            },
            type: 'POST',
            success: function(data) {
                responseVar = true;
                $('.item-price').html(data);
            }
        }).fail(function(err){
            FORGE.Log.error("Failed to update graphical customized price estimate");
        });
    }

    // parameter is of form:  materialID +" "+finishID
    function setMaterialSelection(materialFinish) {
        var $material = $("#"+FORGE.getSetting("materialSelectID"));
        if (!$material.length) {
            FORGE.Log.error("Cannot find HTML element for material selector.");
            FORGE.Material.changeMaterial("gold");
            return;
        }
        $material.val(materialFinish);

        readMaterialSelection();
    }

    function readMaterialSelection(){
        var shapewaysMaterialFinish = $("#"+FORGE.getSetting("materialSelectID")).val();
        var forgeMaterial = $("#"+FORGE.getSetting("materialSelectID")+" option:selected").data("forge");
        if (!forgeMaterial) {
            forgeMaterial = "gold";
        }
        FORGE.Material.changeMaterial(forgeMaterial);
    }

    function requestCustPriceEstimate() {
        var mat = document.getElementById("defaultMaterial").innerHTML.split(" ");
        var data = new FormData();
        data.append("parentItemId", document.getElementById("modelParentID").innerHTML);
        data.append("materialId", mat[0]);
        data.append("finishId", mat[1]);
        $('.item-price').html('updating...');

        jQuery.ajax({
            url: '/customizerpriceestimate',
            data: data,
            cache: false,
            contentType: false,
            processData: false,
            headers: {
                'Access-Control-Allow-Origin': '*',
                "X-CSRF-TOKEN": $("#csrfToken").val()
            },
            type: 'POST',
            success: function(data) {
                $('.item-price').html(data);
            }
        }).fail(function(err){
            FORGE.Log.error("Failed to request customized price estimate");
        });
    }

    function isMobile(){
        return /(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|ipad|iris|kindle|Android|Silk|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino/i.test(navigator.userAgent) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(navigator.userAgent.substr(0,4));
    }

})();

