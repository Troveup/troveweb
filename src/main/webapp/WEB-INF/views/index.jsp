<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <c:import url="fragments/staticHeader.jsp"/>
  </head>

  <body>

  <div class="container">
    <c:import url="fragments/commonNav.jsp"/>
    <hr>
    <!-- Main component for a primary marketing message or call to action -->
    <div class="jumbotron">
      <h3 class="text-center">Welcome to +rove</h3>
      <p class="text-center" style="margin-top: 100px">Helping you make cool things.</p>
        <button type="button" class="btn btn-default center-block" aria-label="Center Align" style="margin-top: 100px">Start Customizing Right Meow</button>
      <hr>

      <button type="button" class="btn btn-default" aria-label="Left Align">
        <span class="glyphicon glyphicon-align-left" aria-hidden="true"></span>
      </button>
      
      <p class="text-center">How it works</p>

      <!-- Three columns of text below the carousel -->
      <div class="container">
        <div class="row">
          <div class="col-md-3">
            <img class="img-circle center-block" src="data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==" alt="Generic placeholder image" style="width: 140px; height: 140px;">
            <p class="text-center">Choose</p>
          </div><!-- /.col-lg-4 -->
          <div class="col-md-3">
            <img class="img-circle center-block" src="data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==" alt="Generic placeholder image" style="width: 140px; height: 140px;">
            <p class="text-center">Customize</p>
          </div><!-- /.col-lg-4 -->
          <div class="col-md-3">
            <img class="img-circle center-block" src="data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==" alt="Generic placeholder image" style="width: 140px; height: 140px;">
            <p class="text-center">Save/Trove</p>
          </div><!-- /.col-lg-4 -->
          <div class="col-md-3">
            <img class="img-circle center-block" src="data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==" alt="Generic placeholder image" style="width: 140px; height: 140px;">
            <p class="text-center">Print</p>
          </div><!-- /.col-lg-4 -->
        </div>
    </div>
      
      <h2>Browse</h2>
      <div class="container">
        <div class="row">
          <div class="col-sm-6 col-md-4">
            <div style="top: 0px; right: 0px; width: 100%; height: 200px; position: absolute;">
              <img src="/resources/img/plus.png" style="position:absolute; top:0px; right:0px; width:30px; height:30px;" />
              <img src="/resources/img/ring_2.png" style="width: 100%; height: 100%" />
            </div>
          </div>
          <div class="col-sm-6 col-md-4">
            <div style="top: 0px; right: 0px; width: 100%; height: 200px; position: absolute;">
              <img src="/resources/img/plus.png" style="position:absolute; top:0px; right:0px; width:30px; height:30px;" />
              <img src="/resources/img/ring_2.png" style="width: 100%; height: 100%" />
            </div>
          </div>
          <div class="col-sm-6 col-md-4">
            <div style="top: 0px; right: 0px; width: 100%; height: 200px; position: absolute;">
              <img src="/resources/img/plus.png" style="position:absolute; top:0px; right:0px; width:30px; height:30px;" />
              <img src="/resources/img/ring_2.png" style="width: 100%; height: 100%" />
            </div>
          </div>
        </div>
            </div>
          </div>
        </div>
      

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <c:import url="fragments/commonJSSources.jsp"/>
  </body>
</html>
