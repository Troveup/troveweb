<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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

<div class="modal fade material-modal" id="material-modal" tabindex="-1" role="dialog" aria-labelledby="materialModalALabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content" id="authModalContent">
      <div class="modal-header">
        <span class="modalttoptext">Material</span>
        <a href="#" data-dismiss="modal" class="closeModal"><i class="ion-android-close"></i></a>
      </div>
      <div class="modal-body">
        <ul class="nav navbar-nav cust-material">
          <c:if test="${not empty materials}">
            <c:forEach var="material" items="${materials}">
              <c:forEach var="finish" items="${material.finishList}">
                <li><a href="#" class="fakedownmat" id="${material.materialId}_${finish.finishId}" onclick="matit('${material.materialId} ${finish.finishId}');">${finish.name}</a></li>
              </c:forEach>
            </c:forEach>
          </c:if>
        </ul>
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
          <c:if test="${not empty size}">
            <c:forEach var="individualsize" items="${size}">
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
          <button id="sizebuton" data-role="none" type="button" class="navbar-toggle cont size" data-toggle="modal" data-target="#size-modal">
            <i class='ion-arrow-up-b'></i>
          </button>
          <a class="navbar-brand size" href="#">SIZE</a>
        </div>
      </div>
    </nav>

    <nav id="materialnav" class="navbar navbar-inverse navbar-fixed-bottom material" role="navigation">
      <div class="container-fluid">
        <div class="navbar-header">
          <button data-role="none" type="button" class="navbar-toggle cont material" data-toggle="modal" data-target="#material-modal">
            <i class='ion-arrow-up-b'></i>
          </button>
          <a class="navbar-brand material" href="#">MATERIAL</a>
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



<script type="text/javascript">
  function triggerSizeRequest(){
    $('#size-modal').modal('show');  
    centerModal();
  }
</script>