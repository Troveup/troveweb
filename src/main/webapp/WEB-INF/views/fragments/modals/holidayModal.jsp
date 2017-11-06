<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="modal fade holidayModal" id="holidayModal" tabindex="-1" role="dialog" aria-labelledby="holidayModalALabel" aria-hidden="true">
  <div class="modal-dialog holiday">
    <div class="modal-content holiday" id="holidayModalContent2">
      <div class="modal-header">
        <a href="#" data-dismiss="modal" class="closeModal"><i class="ion-android-close"></i></a>
      </div>
      <img id="holidayimg" src="https://storage.googleapis.com/troveup-imagestore/assets/img/holiday-deadline-modal.jpg" class="img-responsive">
      <img id="holidayimgmobile" src="https://storage.googleapis.com/troveup-imagestore/assets/img/holiday-deadline-modal-mobile.jpg" class="img-responsive">
     
    </div>
  </div>
</div>

<script type="text/javascript">

  function centerModal(){
    var wh = Math.min($(window).height(), window.innerHeight);
    var mh = Math.max(( wh - 613 ) / 2, 0);
     $('#holidayModalContent2').css("margin-top", String(mh) + "px");
  } 

  function triggerGiftRequest(){
    $('#holidayModal').modal('show');  
    centerModal();
  }
  
  $(window).on('resize', function() {
    centerModal();
  });
</script>




