<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="modal fade addressModal" id="addressModal" tabindex="-1" role="dialog" aria-labelledby="authModalALabel"
     aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content" id="addressModalContent">
      <div class="modal-header">
        <div class="errorstuff"></div>
        <a href="#" data-dismiss="modal" class="closeModal"><i class="ion-android-close"></i></a>
      </div>
      <div class="modal-body">
        <div class="spinstuff">
          <span class="spinner centered"></span>
        </div>
        <div class="formstuff show">
          <h4 class="modalHeadline" id="nana">Tell us where to send your free sample jewelry.</h4>
          <form id="shippingForm" class="shipping" action="/" method="POST" onsubmit="return false;" role="form">
            <input class="forme" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div class="row form-group">
              <div class="col-xs-6 colpadder col has-feedback">
                <span class="labyo">First Name</span>
                <input type="text" id="shippingFirstName" name="shippingFirstName" class="forme form-control"
                       placeholder="First"/>
                <span class="glyphicon form-control-feedback" id="shippingFirstName1"></span>
              </div>
              <div class="col-xs-6 colpadder col has-feedback">
                <span class="labyo">Last Name</span>
                <input type="text" name="shippingLastName" id="shippingLastName" class="forme form-control"
                       placeholder="Last"/>
                <span class="glyphicon form-control-feedback" id="shippingLastName1"></span>
              </div>
            </div>
            <div class="row form-group">
              <div class="col-md-12 col has-feedback">
                <span class="labyo">Email</span>
                <input type="email" name="shippingEmail" id="shippingEmail" class="forme form-control"
                       placeholder="Email"/>
                <span class="glyphicon form-control-feedback" id="shippingEmail1"></span>
              </div>
            </div>
            <div class="row form-group">
              <div class="col-md-12 col has-feedback">
                <span class="labyo">Phone</span>
                <input type="text" name="shippingPhone" id="shippingPhone" class="forme form-control"
                       placeholder="Phone"/>
                <span class="glyphicon form-control-feedback" id="shippingPhone1"></span>
              </div>
            </div>
            <div class="row form-group">
              <div class="col-md-12 col has-feedback">
                <span class="labyo">Address</span>
                <input type="text" name="shippingAddressLine1" id="shippingAddressLine1" class="forme form-control"
                       placeholder="Address"/>
                <span class="glyphicon form-control-feedback" id="shippingAddressLine11"></span>
              </div>
            </div>
            <div class="row form-group">
              <div class="col-md-12 col">
                <span class="labyo">Address Line 2</span>
                <input type="text" name="shippingAddressLine2" id="shippingAddressLine2"
                       class="forme form-control ignore" placeholder="Address"/>
              </div>
            </div>
            <c:if test="${not empty country}">
              <input id="shippingCountryId" name="shippingCountryId" type="hidden" class="forme form-control ignore"
                     value="1"/>
            </c:if>
            <div class="row form-group">
              <div class="col-sm-5 col has-feedback">
                <span class="labyo">City</span>
                <input type="text" name="shippingCity" id="shippingCity" class="forme form-control" placeholder="City"/>
                <span class="glyphicon form-control-feedback" id="shippingCity1"></span>
              </div>
              <div class="col-xs-6 col-sm-4 colpadder col has-feedback">
                <span class="labyo">State</span>
                <select id="shippingStateId" name="shippingStateId" class="forme form-control">
                  <option value="">Choose a State</option>
                  <c:forEach var="state" items="${states}">
                    <option value="${state.primaryKeyId}">${state.code}</option>
                  </c:forEach>
                </select>
                <span class="glyphicon form-control-feedback select" id="shippingStateId1"></span>
              </div>
              <div class="col-xs-6 col-sm-3 colpadder col has-feedback">
                <span class="labyo">Zip</span>
                <input type="text" name="shippingZip" id="shippingZip" class="forme form-control" placeholder="Zip"/>
                <span class="glyphicon form-control-feedback" id="shippingZip1"></span>
              </div>
            </div>
          </form>
        </div>
      </div>
      <div class="modal-footer">
        <button id="addressSubmit" type="submit" class="btn btn-si">Send My Trial Jewelry</button>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">

  var addressForm = $('#shippingForm');
  var $shipButton = $('#addressSubmit');

  function centerModal() {
    var wh = Math.min($(window).height(), window.innerHeight);
    var mh = Math.max(( wh - (612) ) / 2, 0);
    $('#addressModalContent').css("margin-top", String(mh) + "px");
  }

    // FIXME: manually copied this code to views/public/customize.jsp
  function triggerAddressRequest() {
      var modalOpen = $('#addressModal').hasClass('in');

      // FIXME: add any desired behavior for when button is clicked before last step
      var buttonDisabled = $('#addButton').hasClass('disabled');
      if (buttonDisabled) { }

      if (!modalOpen) {
          mixpanel.track("onboard_customize_click_shipping_modal");
          $('#addressModal').modal('show');
      }
  }

    function packPageFormData(formData) {
        formData.append('shouldAddToCart', true);

        // make sure all these parameters are accounted for
        //("shouldAddToCart") final Boolean shouldAddToCart,
        //("modelName") final String modelName,
        //("modelDescription") final String modelDescription,
        //("isPrivate") final Boolean isPrivate,
        //("modelExtension") final String modelExtension,
        //("imageExtension") final String imageExtension,
        //("modelMaterial") final String modelMaterial,
        //("modelMaterialFinish") final String modelMaterialFinish,
        //(value = "addToCollectionId", required = false) final Long addToCollectionId,
        //(value = "defaultCollectionId", required = false) final Long defaultCollectionId,
        //(value = "exportHash", required = true) final String exportHash,
        //(value = "engraveText", required = false) final String engraveText,
        //(value = "size", required = false) final String size,
        //(value = "chain", required = false) final Integer chain,
    
        // formData.append('firstName', $('#shippingFirstName').val());
        // formData.append('lastName', $('#shippingLastName').val());
        // formData.append('email', $('#shippingEmail').val());
        // formData.append('address1', $('#shippingAddressLine1').val());
        // formData.append('address2', $('#shippingAddressLine2').val());
        // formData.append('city', $('#shippingCity').val());
        // formData.append('state', parseInt($('#shippingStateId').val(), 10));
        // formData.append('zip', $('#shippingZip').val());
    }


  $(document).ready(function (){
    addressForm.validate({
      ignore: ".ignore",
      rules: {
        shippingFirstName: {
          required: true
        },
        shippingLastName: {
          required: true
        },
        shippingEmail: {
          required: true
        },
        shippingPhone: {
          minlength: 10,
          required: true
        },
        shippingAddressLine1: {
          required: true
        },
        shippingCity: {
          required: true
        },
        shippingStateId: {
          required: true
        },
        shippingZip: {
          digits: true,
          required: true
        }
      },
      highlight: function (element) {
        var id_attr = "#" + $(element).attr("id") + "1";
        $(element).closest('.col').removeClass('has-success').addClass('has-error');
      },
      unhighlight: function (element) {
        var id_attr = "#" + $(element).attr("id") + "1";
        $(element).closest('.col').removeClass('has-error').addClass('has-success');
      },
      errorElement: 'span',
      errorClass: 'help-block',
      errorPlacement: function (error, element) {
        if (element.length) {
          error.insertAfter(element);
        } else {
          error.insertAfter(element);
        }
      }
    });
  });

    function debugFormData(fd) {
        var xhr = new XMLHttpRequest;
        xhr.open('POST', '/blahblahblah', true);
        xhr.send(fd);
    }


  $shipButton.on('click', function () {

    if (addressForm.valid()) {

      var postData = new FormData();
      packPageFormData(postData);
      UI.packExportDataFTUE(postData);

      var sStuff = document.querySelector(".spinstuff");
      var fStuff = document.querySelector(".formstuff");
      sStuff.classList.toggle("show");
      fStuff.classList.toggle("show");
      mixpanel.track("onboard_item_order");

      $shipButton.button('loading');

      jQuery.ajax({
        url: '/savenewmodel',
        data: postData,
        cache: false,
        contentType: false,
        processData: false,
        headers: {
          'Access-Control-Allow-Origin': '*',
          "X-CSRF-TOKEN": '${_csrf.token}'
        },
        type: 'POST',
        success: function (data) {
          if (data.success == true) {
            mixpanel.track("onboard_item_order_success");
            window.location.href = "/onboard/confirm";
          }
          else {
            sStuff.classList.toggle("show");
            fStuff.classList.toggle("show");
            mixpanel.track("onboard_item_order_fail1");
            $('.errorstuff').html(data.message);
            $('.errorstuff').show();
          }
        },
        failure: function (data) {
          mixpanel.track("onboard_item_order_fail2");
          sStuff.classList.toggle("show");
          fStuff.classList.toggle("show");
          $('.errorstuff').html("Error -500.  Oops!  We're experiencing some technical difficulties. Please try submitting again in a few minutes.  Otherwise, contact us at hello@troveup.com.");
          $('.errorstuff').show();
        },
        complete: function (data) {
            $shipButton.button('reset');
        }
      });
    }
  });
</script>



