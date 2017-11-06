<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="modal fade giftModal" id="giftModal" tabindex="-1" role="dialog" aria-labelledby="giftModalALabel" aria-hidden="true">
  <div class="modal-dialog gift">
    <div class="modal-content gift moby" id="giftModalContent1">
      <img src="https://storage.googleapis.com/troveup-qa-cdn/get-gift-img-2.jpg" class="img-responsive">
    </div>
    <div class="modal-content gift" id="giftModalContent2">
      <div class="modal-header">
        <a href="#" data-dismiss="modal" class="closeModal"><i class="ion-android-close"></i></a>
      </div>
      <div class="modal-body gift">
        <h4 class="modalHeadline gift" id="nana" style="font-family:'vollkorn';font-style:italic;">GIVE A GIFT.<br> GET A FREE NECKLACE.</h4><br>

        <ol class="list-group">
          <li class="list-group-item">
            <p>At checkout, select "This order is a gift" then  "Gift wrap this order" (with an order value of $50 or more).</p>
          </li>
          <li class="list-group-item">
            <p>We will send you an exclusive surprise necklace that has not yet been released.</p>
          </li>
          <li class="list-group-item">
            <p>Celebrate! You just earned yourself some free jewelry.</p>
          </li>
        </ol>

      </div>
      <div class="modal-footer gift">
        <p class="modalText" style="font-style: italic;">One necklace per user. While supplies last.</p>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  function centerModal(){
    var wh = Math.min($(window).height(), window.innerHeight);
    var mh = Math.max(( wh - (540) ) / 2, 0);
    $('#giftModalContent1').css("margin-top", String(mh) + "px");
     $('#giftModalContent2').css("margin-top", String(mh) + "px");
  } 

  function triggerGiftRequest(){
    $('#giftModal').modal('show');  
    centerModal();
  }
  
  $(window).on('resize', function() {
    centerModal();
  });
</script>




