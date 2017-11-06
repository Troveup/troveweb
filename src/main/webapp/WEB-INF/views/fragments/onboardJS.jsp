<script>
	// applies dynamic vertical centering to elements with the .centrafuge class
  function jqUpdateSize(){
    var height = $('.centrafuge').innerHeight();
    $('.centrafuge').css("margin-top", String(-1 * (height / 2)) + "px");
  };  

  function upDateMaxHeight(){
    var bestheight = $(window).height() - $('.welcomehero').height() - $('.footer').height() - $('.card_remake_title').height() - 155;
    var container = $('#controls');
    container.css("max-height", String(bestheight) + "px");
    //$('.shapeholder').css("margin-top", String( ($(window).height() - 400) / 2 ) + "px");
  }; 
  // changes the href of the next link depending on which element you select
  function changeLinkRef(img,link) {
    $('#itemtooltip').tooltip('destroy');
    $('.selected-item').removeClass('selected-item');
    $('.disabled').removeClass('disabled');
    $(img).addClass('selected-item');
    $('a.nextlink').attr('href', link )
  };
  // simulate http redirect
  function reDirect(link) {
    window.location.replace(link);
  };
  // validates the form before allowing the submit button to be disabled
  function reVal(button) {
    var needsFeedback = $('.has-feedback').length;
    var hasSuccess = $('.has-success').length;
    if (hasSuccess >= needsFeedback) {
      $(button).removeClass('disabled');
      $(button).addClass('readytosend');
      $('#itemtooltip').attr('title','');
    } else {
      $(button).addClass('disabled');
      $(button).removeClass('readytosend');
      $('#itemtooltip').attr('title','Please fill out all required info');
    }
  };
  function ftuisubmit() {
      var data = new FormData();
      data.append("parentModelId", "${itemId}");
      data.append("firstName", document.getElementById('sFName').value);
      data.append("lastName", document.getElementById('sLName').value);
      data.append("email", document.getElementById('sEmail').value);
      data.append("address1", document.getElementById('sAddressOne').value);
      data.append("address2", document.getElementById('sAddressTwo').value);
      data.append("city", document.getElementById('sCit').value);
      var selectedState = document.getElementById('sStat');
      data.append("state", selectedState.options[selectedState.selectedIndex].text);
      data.append("zip", document.getElementById('sZ').value);
      data.append("country", "USA");
      data.append("size", document.getElementById('sRingsize').value);
      //Customizer provided values

      FORGE.Page.addMeshData(data);
      FORGE.Page.addExportHash(data, document.getElementById('sRingsize').value);
      jQuery.ajax({
          url: '/ftuisubmit',
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
              if (data.isSuccess) {
                reDirect('/public/thankyou/' + data.referralCode);
              } else {
                $('.spinner.shipping').hide();
                $('.shippingstuff').fadeIn();
                document.getElementById('error').innerHTML = data.errorMessage;
              }  
          }
      });
  };
      
  // DOM READY
  $(document).ready(function() {
    // Things to do when the page first loads
    jqUpdateSize();
    upDateMaxHeight();
    $('.spinner.shipping').hide();
    $('form.names').validate({ 
      ignore: ".ignore",   
      rules: {
          sFName: {
              minlength: 1,
              maxlength: 20,
              required: true
          },
          sLName: {
              minlength: 1,
              maxlength: 20,
              required: true
          },
          sEmail: {
              required: true
          },
          sAddressOne: {
              required: true
          },
          sCit: {
              required: true
          },
          sStat: {
              required: true
          },
          sZ: {
              digits: true,
              required: true
          },
          sRingsize: {
              required: true
          }
      },
      highlight: function(element) {
          var id_attr = "#" + $( element ).attr("id") + "1";
          $(element).closest('.col').removeClass('has-success').addClass('has-error');
          $(id_attr).removeClass('glyphicon-ok').addClass('glyphicon-remove');         
      },
      unhighlight: function(element) {
          var id_attr = "#" + $( element ).attr("id") + "1";
          $(element).closest('.col').removeClass('has-error').addClass('has-success');
          $(id_attr).removeClass('glyphicon-remove').addClass('glyphicon-ok');         
      },
      errorElement: 'span',
          errorClass: 'help-block',
          errorPlacement: function(error, element) {
              if(element.length) {
                  error.insertAfter(element);
              } else {
              error.insertAfter(element);
              }
          } 
    });
    // form event handlers
    // make sure all fields are valid before submitting the button
    $(document).on('focusout', 'input, select', function() {
      reVal('#sendmemyring');
    });

      // click event handlers
      // images on onboarding choose item page
      $("#img1").click(function () {
          changeLinkRef('#img1','/public/obcustomize/4');
          $("#img1").addClass('selected-item');
      });
      $("#img1").click(function () {
          changeLinkRef('#img2','/public/obcustomize/12');
          $("#img2").addClass('selected-item');
      });
      $("#img3").click(function () {
          changeLinkRef('#img3','/public/obcustomize/8');
          $("#img3").addClass('selected-item');
      });

    // ajax posts 
    // product hunt onboarding order submission
    $('#sendmemyring').on('click', function(e) {
      e.preventDefault();
      if ($('.readytosend').length) {
        $('.spinner.shipping').fadeIn();
        $('.shippingstuff').hide();
        ftuisubmit();
      } else {
        alert('Please Fix Errors');
      }
    });

    $(function () {
      $('[data-toggle="tooltip"]').tooltip()
    })

    $('input').on('change', function() {
      $(this).valid(); 
      reVal('#sendmemyring');
    });

    $('select').on('change', function() {
      $(this).valid(); 
      reVal('#sendmemyring');
    });




  });

  $(window).on('resize', function() {
    jqUpdateSize();
    upDateMaxHeight();
  });
</script>
