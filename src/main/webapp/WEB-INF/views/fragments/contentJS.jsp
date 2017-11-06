<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
  <script src="../resources/js/vendor/jquery.pjax.js"></script>
  <script src="../resources/js/custom.js"></script>
  <script>
    function performAddItemRequest(itemId) {
        //Grab the base URL
        if (typeof location.origin === 'undefined')
            location.origin = location.protocol + '//' + location.host;

        //Make an AJAX request to the addtotrove Action, including the csrf token
        $.ajax({
            url: location.origin + "/addtotrove",
            type: 'post',
            data: {
                itemId: itemId
            },
            headers: {
                "X-CSRF-TOKEN": "${_csrf.token}"
            },
            dataType: 'json',
            success: updateCard(itemId)
        });
    }

    function updateCard(cardId) {
        var newcount = parseInt($('.tallyho-profile').html()) + 1;
        $('#card-'+ cardId +' span').text('Troved');
        document.getElementById('card-' + cardId).onclick = null;
        $('#bump'+ cardId).addClass('opa');
        $('#buy-'+ cardId).addClass('hideitnow');
        $('#cust-'+ cardId).addClass('hideitnow');
        $('.tallyho-profile').html(String(newcount));
        $('.tallyho-profile').addClass('showitnow');
        setTimeout(function() {
            $('#bump'+ cardId).removeClass('opa');
            $('#buy-'+ cardId).removeClass('hideitnow');
            $('#cust-'+ cardId).removeClass('hideitnow');
            $('.tallyho-profile').removeClass('showitnow');
            $('.tallyho-profile').addClass('hideitlater');
        }, 2000);
    }
  </script>

  <script>
    $(document).ready(function() {
      setupBlocksFeatured();
     
      go();
      window.addEventListener('resize', go);

      function go(){
        setupBlocksFeatured();
        // document.querySelector('.width').innerText = document.documentElement.clientWidth;
        // document.querySelector('.height').innerText = document.documentElement.clientHeight;
      }

    });
  </script>