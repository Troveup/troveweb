<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title> Trove: Legal - Privacy Policy </title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href='https://fonts.googleapis.com/css?family=Quicksand:300,400,700' rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Raleway:400,200,300,500,600,700' rel='stylesheet' type='text/css'>
    <link rel="icon" href="/resources/img/favicon.png?v=2">
    <script src="https://code.jquery.com/jquery-2.1.3.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.13.1/jquery.validate.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.13.1/additional-methods.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/resources/stylesheets/main.css">
    <c:import url="../fragments/analytics/all.jsp"/>
    <c:import url="../fragments/headers/commonHead.jsp"/>
    <c:import url="../fragments/headers/legalHead.jsp"/>
  </head>

  <body>
    <c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
    <c:import url="../fragments/nav/topNavBar.jsp"/>
    <div class="container cards_product_wrapper2">
      <div class="row">

        <div class="col-sm-12">
          <div class="cardy about"> 
            <h3>Legal</h3>

            <ul class="nav bs-docs-sidenav">
              <li class="sidelink"><a href="/privacy">Privacy Policy</a> </li>
              <li class="sidelink"><a href="/terms">Terms of Service</a> </li>
              <li class="sidelink"><a href="/copyright">Copyright Policy</a> </li>
              <li class="sidelink"><a href="/sales">Sales Policy</a> </li>
            </ul>

          </div>
          <br></br>
          <br></br>
          <br></br>
          <br></br>
        </div>

      </div>
    </div>

   <script>
      $(document).ready(function() {

        var count = parseInt($('.tallyho-cart').html());
        if ( count >= 1 ) {
          $('.tallyho-cart').addClass('showitnow');
        }
       
        $('#filters').on( 'click', 'a', function( event ) {
          $('a').removeClass('active');
          $(this).addClass('active');
        });

        $('li').on( 'click', 'a', function( event ) {
          $('a').removeClass('active');
          $(this).addClass('active');
        });

        $('.jam').on('mouseover', function() {
          $('.dropdown-container.jam').addClass('showtime');
        });

        $('.jam').on('mouseleave', function() {
          $('.dropdown-container.jam').removeClass('showtime');
        });

        $('.pro').on('mouseover', function() {
          $('.dropdown-container.pro').addClass('showtime');
        });

        $('.pro').on('mouseleave', function() {
          $('.dropdown-container.pro').removeClass('showtime');
        });

        $('.modal').on('shown.bs.modal', function() {
          $(this).find('[autofocus]').focus();
        });

      });
    </script>

  </body>
</html>