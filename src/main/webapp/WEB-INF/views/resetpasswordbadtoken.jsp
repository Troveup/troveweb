<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Trove: Reset Password Faliure</title>
    <meta name="description" content="Confirm Email Page Post Registration">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href='https://fonts.googleapis.com/css?family=Quicksand:300,400,700' rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Raleway:400,200,300,500,600,700' rel='stylesheet' type='text/css'>
    <link rel="icon" href="/resources/img/favicon.png?v=2">
    <link rel="stylesheet" href="/resources/stylesheets/reset.css">
    <script src="https://code.jquery.com/jquery-2.1.3.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
    <c:import url="fragments/analytics/all.jsp"/>
  </head>
  <body class="reset">
    <div class="centrafuge"> 
      <a href="/"><img alt="Trovelogo" class="ob-logo" src="/resources/img/troveLogo2.svg"></a>
      <h3>RESET PASSWORD FALIURE</h3>
      <h5>There was a problem with the URL that we sent you to reset your password.  Please request another!</h5>
      <a href="/password/forgotpassword"><button id="button" class="btn btn-lg">Request Agian</button></a>
    </div>
    <script>
      function jqUpdateSize(contain){
        var height = $('.centrafuge').innerHeight();
        $(contain).css("margin-top", String(-1 * (height / 2)) + "px");
      };  
      $(document).ready(function() {
      	jqUpdateSize('.centrafuge');
      });
      $(window).on('resize', function() {
        jqUpdateSize('.centrafuge');
      });
    </script>
  </body>
</html>