<script>
  // applies dynamic vertical centering to elements with the .centrafuge class
  function jqUpdateSize(){
    var wh = $(window).height();
    var ch = $('.centrafuge').innerHeight();

    if (isWelcome)  {
        var oldheight = (-1 * (ch / 2 ));
        $('.centrafuge').css("margin-top", String(oldheight) + "px");
    } else {
        var magicheight = Math.max(( wh - (ch + 20) ) / 2, 0);
        $('.centrafuge').css("margin-top", String(magicheight) + "px");
    }

  };  

  function upDateMaxHeight(){
    var bestheight = $(window).height() - $('.welcomehero').height() - $('.footer').height() - $('.card_remake_title').height() - 155;
    var container = $('#controls');
    container.css("max-height", String(bestheight) + "px");
  }; 

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
        var testver = getUrlVars()["testversion"];
        if (testver) {
            var evntname = testver + "-successfulringsize";
            mixpanel.track(evntname);
        } else {
            mixpanel.track("successfulringsize");
        }
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
              var testversion = getUrlVars()["testversion"];
              if (testversion) {
                var newlink = "/public/thankyou/" + data.referralCode + "?testversion=" + testversion;
                reDirect(newlink);
              } else {
                reDirect('/public/thankyou/' + data.referralCode);
              }
            } else {
              $('.spinner.shipping').hide();
              $('.shippingstuff').fadeIn();
                $('#backy').fadeIn();
                $('#itemtooltip').fadeIn();
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

    function getUrlVars() {
      var vars = [], hash;
      var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
      for(var i = 0; i < hashes.length; i++) {
          hash = hashes[i].split('=');
          vars.push(hash[0]);
          vars[hash[0]] = hash[1];
      }
      return vars;
    };


    $("#itemtooltip1").click(function () {
      var testversion = getUrlVars()["testversion"];
      if (testversion) {
        var eventname = testversion + "-chooseitem";
        mixpanel.track(eventname, {
          "item": "1"
        });
      } else {
        mixpanel.track("chooseitem", {
          "item": "1"
        });
      }
      $('.selected-item').removeClass('selected-item');
      $("#img1").addClass('selected-item');
    });

    $("#itemtooltip2").click(function () {
      var testversion = getUrlVars()["testversion"];
      if (testversion) {
        var eventname = testversion + "-chooseitem";
        mixpanel.track(eventname, {
          "item": "2"
        });
      } else {
        mixpanel.track("chooseitem", {
          "item": "2"
        });
      }
      $('.selected-item').removeClass('selected-item');
      $("#img2").addClass('selected-item');
    });

    $("#itemtooltip3").click(function () {
      var testversion = getUrlVars()["testversion"];
      if (testversion) {
        var eventname = testversion + "-chooseitem";
        mixpanel.track(eventname, {
          "item": "3"
        });
      } else {
        mixpanel.track("chooseitem", {
          "item": "3"
        });
      }
      $('.selected-item').removeClass('selected-item');
      $("#img3").addClass('selected-item');
    });

    $("#itemtooltip4").click(function () {
      var testversion = getUrlVars()["testversion"];
      if (testversion) {
        var eventname = testversion + "-chooseitem";
        mixpanel.track(eventname, {
          "item": "4"
        });
      } else {
        mixpanel.track("chooseitem", {
          "item": "4"
        });
      }
      $('.selected-item').removeClass('selected-item');
      $("#img4").addClass('selected-item');
    });

    $("#itemtooltip5").click(function () {
      var testversion = getUrlVars()["testversion"];
      if (testversion) {
        var eventname = testversion + "-chooseitem";
        mixpanel.track(eventname, {
          "item": "5"
        });
      } else {
        mixpanel.track("chooseitem", {
          "item": "5"
        });
      }
      $('.selected-item').removeClass('selected-item');
      $("#img5").addClass('selected-item');
    });

    $("#itemtooltip6").click(function () {
      var testversion = getUrlVars()["testversion"];
      if (testversion) {
        var eventname = testversion + "-chooseitem";
        mixpanel.track(eventname, {
          "item": "6"
        });
      } else {
        mixpanel.track("chooseitem", {
          "item": "6"
        });
      }
      $('.selected-item').removeClass('selected-item');
      $("#img6").addClass('selected-item');
    });

    // ajax posts 
    // product hunt onboarding order submission
    $('#sendmemyring').on('click', function(e) {
      e.preventDefault();
      if ($('.readytosend').length) {
        $('.spinner.shipping').fadeIn();
        $('.shippingstuff').hide();
        $('#backy').hide();
        $('#itemtooltip').hide();
        ftuisubmit();
      } else {
        alert('Please Fix Errors');
      }
    });


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
