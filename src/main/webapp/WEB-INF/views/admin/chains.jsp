<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Trove: Welcome to Trove</title>
  <c:import url="../fragments/headers/commonHead.jsp"/>
  <c:import url="../fragments/headers/legalHead.jsp"/>
  <c:import url="../fragments/analytics/all.jsp"/>
  <style>

  .thumbnail>img {
    width: 130px;
    padding: 10px;
  }

  .thumbnail {
    text-align: center;
    border: 0px solid #FFF;
  }

  .four-points {
    padding: 30px 0px 0px;
  }

  .four-headers {
    font-family:'Vollkorn', serif; 
    font-style: italic;
  }

  .cta-header{
    font-family:'Vollkorn', serif; 
  }

  .welcome-banner-container{
/*      width: 100%;
      display:inline-block;
      background-size:100%;
      background-repeat: no-repeat;*/
  }

  .try-text{
    position:absolute;
    padding:20px 20px 40px;
    background-color: rgba(255,255,255,.75);
    margin-left:6%;
  }

  .try-photo-container{
    display:block;
    margin:0 auto;
  }

  .how-header{
    position: absolute;
    background-color: #ffffff;
    font-size: 28px;
    left: 50%;
    margin-left: -153px;
    top: 30px;
    padding:15px;
  }

  .try-container{
    display:block;
    margin:0 auto;
    max-width:1000px;
      }

  .how-body{
    margin:60px 20% 30px;
    padding: 30px 5% 30px;
    text-align:center;
    border: 2px solid #dedede;
  }

  @media screen and (min-width: 769px) { 
    .welcome-banner-container{
/*      background-image: url("http://i.imgur.com/TD6CN88.png");
*/    }
  }

    @media screen and (max-width: 768px) { 
    .how-body{
      margin:60px 10% 30px;
    }

    .welcome-banner-container{
/*      background-image: url("http://i.imgur.com/F267GJT.png");
*/    }

    .try-container{
      margin:30px 5% 30px;
    }

    .try-text{
      position:relative;
      width: 100%;
      text-align:center;
      top: -40px;
      margin-left:0;
      padding:0px;

    }
  }

    @media screen and (max-width: 440px) { 
    .how-body{
      margin:60px 0 30px;
    }

    .cta-container{
      left: 0;
      margin-left: 0px;
      width: 100%;
    }
  }
  

  .how-header-text{
    color: #000;
  }

  .btn-open{
    background-color: transparent;
    color:#000;
    border:2px solid #8e8b8b;
  }

  .cta-container{
    background-color: rgba(255,255,255,0.75);
    width: 480px;
    z-index:1;
    position:absolute;
    left:50%;
    margin-left:-240px;
    text-align:center;
    padding:50px 0;
  }

  .special-container {
    display: none;
  }


  </style>
</head>

<body>
  <c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
  <c:import url="../fragments/nav/topNavBar.jsp"/>
    <div class="container-fluid">
        <form class="new-collection form-horizontal" action="/admin/chainspost?${_csrf.parameterName}=${_csrf.token}" method='POST'>
            <div class="form newCollection">
                <fieldset>
                    <div>
                      <label for="chainname">Chain Name (This will show in the dropdown)</label>
                      <input type="text" id="chainname" name="chainname" class="form-control" placeholder="Three 16&quot; 18k Gold Chainz" />
                    </div>
                    <div>
                      <label for="description">Description (This isn't shown anywhere at the moment)</label>
                      <input type="text" id="description" name="description" maxlength="30" class="form-control" placeholder="TOO many chainz" />
                    </div>
                    <div>
                      <label for="price">Price</label>
                      <input type="number" step=".01" id="price" name="price" maxlength="30" class="form-control"/>
                    </div>
                </fieldset>
              <input type="submit" value="Add Chain">
            </div>
        </form>
    </div>
    <c:if test="${not empty success}">
      <script>alert("success!");</script>
    </c:if>

  </body>


  <script>
    $('#highresavailable').change(function() {
      if ($('#highresavailable option:selected').val() == "yes") {
        $('#highresfilecontainer').show();
      }
      else {
        $('#highresfilecontainer').hide();
      }
    });
  </script>

</html>