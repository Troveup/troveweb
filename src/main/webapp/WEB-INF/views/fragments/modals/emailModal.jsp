<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="modal fade emailModal" id="emailModal" tabindex="-1" role="dialog" aria-labelledby="emailModalALabel" aria-hidden="true">
  <div class="modal-dialog">
    <div id="emailModalContent">
      <div class="auth-content">
        <div class="auth-content-inner">
          <div class="auth-form freehead">

            <div id="pre-submit-container">
              <h3 class="free-heading">FREE SHIPPING</h3>
              <div class="block-spacer"></div>
              <p class="email-tag-line">Enter your email for free shipping off your first order</p>
              <input id="user-email-freeship" name="username" type="text" placeholder="Enter you Email" class="form-control"/>
              <button id="free-shipping-button" type="submit" class="btn btn-si">I WANT FREE SHIPPING</button>
              <a class="free-ship-close" href="#" data-dismiss="modal" class="closeModal">No thanks, I'm not interested in free shipping</a>
            </div>

            <div id="post-submit-container">
              <h3 class="free-heading">THANK YOU!</h3>
              <div class="block-spacer"></div>
              <h4 class="free-code-h4">Use code</h4>
              <h5 class="free-code-h5">FREESHIP</h5>
              <h4 class="free-code-h4">for free shipping</h4>
              <a class="free-ship-close" href="#" data-dismiss="modal" class="closeModal">Close</a>
            </div>
           
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript" src="/resources/js/trove/AJAX.js"></script>
<script type="text/javascript">
  var emailModal = $('#emailModal');
  var emailModalAjaxHelper = new AJAXHelper("${_csrf.token}");

  //Controlled in the CommonController as part of the getModelAndView method
  var pageVisitCounter = "${showFreeShippingModal}";
  var authenticated = "${isAuthenticated}";
  function centerEmailModal() {
    var wh = Math.min($(window).height(), window.innerHeight);
    var mh = Math.max(( wh - (540) ) / 2, 0);
    $('#emailModalContent').css("margin-top", String(mh) + "px");
  }
  function triggerEmailModal() {
    emailModal.modal('show');
    centerEmailModal();
  }
  $(window).on('resize', function () {
    centerEmailModal();
  });

  if ((pageVisitCounter.length > 0 && pageVisitCounter == "true") && (authenticated.length == 0 || authenticated == "false")) {
    triggerEmailModal();
  }

  $('#free-shipping-button').on('click', function () {

    var email = $('#user-email-freeship').val();

    if (email.length > 0)
    {
      var dataObject = emailModalAjaxHelper.createOrAppendPostDataObject("email", email, null);
      dataObject = emailModalAjaxHelper.createOrAppendPostDataObject("type", "FREE_SHIPPING_MODAL", dataObject);
      emailModalAjaxHelper.performPostRequest(dataObject, "/genericsignup", function(){}, function(){}, null);
      $('#pre-submit-container').hide();
      $('#post-submit-container').show();

      mixpanel.register({"session_email": email});
      mixpanel.track("email_modal_submit");
    }
  });

</script>



