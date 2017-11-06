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
      <div style="padding-top: 50px;">
      <c:if test="${not empty success}">
        <script>alert("success!");</script>
        <b>Your EasyPost Download URL (Note: Navigating away will lose this URL!): </b>
        <br>
        ${easyPostLabelUrl}
      </c:if>
      <c:if test="${not empty error}">
        <script>alert("Something went wrong!");</script>
        <b>There was an error generating the easypost label.  Send this to Tim:</b>
        <br>
        ${easyPostLabelError}
      </c:if>
        <div>
        <form class="new-collection form-horizontal" action="/admin/labelspost?${_csrf.parameterName}=${_csrf.token}" method='POST' enctype="multipart/form-data">
            <div class="form newCollection">
              <label for="toaddress">To Address</label>
              <fieldset name="toaddress" id="toaddress">
                <div>
                  <label for="toname">Name</label>
                  <input type="text" name="toname" id="toname" maxlength="255" class="form-control" placeholder="Company Name" />
                </div>
                <div>
                  <label for="toaddress1">Address Line 1</label>
                  <input type="text" name="toaddress1" id="toaddress1" maxlength="255" class="form-control" placeholder="Address Line One" />
                </div>
                <div>
                  <label for="toaddress2">Address Line 2</label>
                  <input type="text" name="toaddress2" id="toaddress2" maxlength="255" class="form-control" placeholder="Address Line Two" />
                </div>
                <div>
                  <label for="tocity">City</label>
                  <input type="text" name="tocity" id="tocity" maxlength="255" class="form-control" placeholder="City" />
                </div>
                <div>
                  <label for="tostatemenu">State</label>
                  <select name="tostatemenu" id="tostatemenu">
                    <c:forEach var="state" items="${states}">
                      <option value="${state.code}">${state.name}</option>
                    </c:forEach>
                  </select>
                </div>
                <div>
                  <label for="tozip">Postal Code</label>
                  <input type="number" name="tozip" id="tozip" maxlength="255" class="form-control" placeholder="Zipcode" />
                </div>
                <div>
                  <label for="usedefault">Use 48 Wall Street as From Address?</label>
                  <select name="usedefault" id="usedefault">
                    <option value="yes" selected>Yes</option>
                    <option value="no">No</option>
                  </select>
                </div>
              </fieldset>
              <label id="labelfrom" for="fromaddress" style="display: none;">From Address</label>
              <fieldset name="fromaddress" id="fromaddress" style="display: none;">
                <div>
                  <label for="fromname">Name (Company)</label>
                  <input type="text" name="fromname" id="fromname" maxlength="255" class="form-control" placeholder="Company Name" />
                </div>
                <div>
                  <label for="fromaddress1">Address Line 1</label>
                  <input type="text" name="fromaddress1" id="fromaddress1" maxlength="255" class="form-control" placeholder="Address Line One" />
                </div>
                <div>
                  <label for="fromaddress2">Address Line 2</label>
                  <input type="text" name="fromaddress2" id="fromaddress2" maxlength="255" class="form-control" placeholder="Address Line Two" />
                </div>
                <div>
                  <label for="fromcity">City</label>
                  <input type="text" name="fromcity" id="fromcity" maxlength="255" class="form-control" placeholder="City" />
                </div>
                <div>
                  <label for="fromstatemenu">State</label>
                  <select name="fromstatemenu" id="fromstatemenu">
                    <c:forEach var="state" items="${states}">
                      <option value="${state.code}">${state.name}</option>
                    </c:forEach>
                  </select>
                </div>
                <div>
                  <label for="fromzip">Postal Code</label>
                  <input type="number" name="fromzip" id="fromzip" maxlength="255" class="form-control" placeholder="Zipcode" />
                </div>
              </fieldset>
              <input type="submit" value="Create Label">
            </div>
        </form>
    </div>
  </body>


  <script>
    $('#usedefault').change(function() {
      if ($('#usedefault option:selected').val() == "no")
      {
        $('#fromaddress').show();
        $('#labelfrom').show();
      } else {
        $('#fromaddress').hide();
        $('#labelfrom').hide();
      }
    });
  </script>

</html>