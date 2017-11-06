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
        <form class="new-collection form-horizontal" action="/admin/modelspost?${_csrf.parameterName}=${_csrf.token}" method='POST' enctype="multipart/form-data">
            <div class="form newCollection">
                <fieldset>
                    <div class="col-xs-12">
                        <label for="jsonfile">Json File</label>
                        <input type="file" name="jsonfile" id="jsonfile" class="form-control" placeholder="c:\MODELFILELOCATION" />
                    </div>
                    <div>
                      <label for="highresavailable">Is higher resolution json file available?</label>
                      <select name="highresavailable" id="highresavailable">
                        <option value="yes">Yes</option>
                        <option value="no" selected>No</option>
                      </select>
                    </div>
                    <div class="col-xs-12" id="highresfilecontainer" style="display: none;">
                      <label for="highresjsonfile">High Resolution Json File</label>
                      <input type="file" name="highresjsonfile" id="highresjsonfile" class="form-control" placeholder="c:\MODELFILELOCATION" />
                    </div>
                    <div class="col-xs-12">
                      <label for="livephotoavailable">Live Photo Available</label>
                      <input type="checkbox" id="livephotoavailable" name="livephotoavailable"/>
                    </div>
                    <div class="col-xs-12" id="livephotouploadcontainer" style="display: none">
                      <label for="livephotoupload">Live Photo File Path</label>
                      <input type="file" name="livephotoupload" id="livephotoupload" class="form-control" placeholder="c:\MODELFILELOCATION" />
                    </div>
                    <div class="col-xs-12">
                        <label for="categoryselect">CATEGORY</label>
                        <select id="categoryselect" name="categoryselect">
                          <c:forEach var="category" items="${categories}">
                            <option value="${category}">${category}</option>
                          </c:forEach>
                        </select>
                    </div>
                    <div>
                      <label for="itemname">Item Name</label>
                      <input type="text" id="itemname" name="itemname" maxlength="30" class="form-control" placeholder="A Cool Filename" />
                    </div>
                    <div>
                      <label for="itemdescription">Item Description</label>
                      <input type="textarea" name="itemdescription" id="itemdescription" maxlength="255" class="form-control" placeholder="A Cool File Description" />
                    </div>
                    <div>
                      <label for="materialmenu">Default Material</label>
                      <select name="materialmenu" id="materialmenu">
                        <c:forEach var="material" items="${materials}">
                          <c:forEach var="finish" items="${material.finishList}">
                            <option value="${material.materialId} ${finish.finishId}">${material.name} - ${finish.name}</option>
                          </c:forEach>
                        </c:forEach>
                      </select>
                    </div>
                    <div>
                      <label for="engravable">Engravable</label>
                      <select name="engravable" id="engravable">
                        <option value="yes">Yes</option>
                        <option value="no" selected>No</option>
                      </select>
                    </div>
                    <div>
                      <label for="renderscene">Render Scene</label>
                        <select name="renderscene" id="renderscene">
                          <option value="trove_scene_mirror-no-hole-ring">trove_scene_mirror-no-hole-ring</option>
                          <option value="trove_scene_mirror-ring_v1">trove_scene_mirror-ring_v1</option>
                          <option value="trove_scene_parted-ring" selected>trove_scene_parted-ring</option>
                          <option value="trove_scene1">trove_scene1</option>
                          <option value="trove_scene_parted-ring_v1">trove_scene_parted-ring_v1</option>
                          <option value="trove_scene_bar-ring">trove_scene_bar-ring</option>
                          <option value="trove_scene_warrior-ring">trove_scene_warrior-ring</option>
                          <option value="trove_scene_general">trove_scene_general</option>
                        </select>
                    </div>
                  <div>
                    <label for="ismobile">Mobile Customizer Available</label>
                    <select name="ismobile" id="ismobile">
                      <option value="yes">Yes</option>
                      <option value="no" selected>No</option>
                    </select>
                  </div>
                </fieldset>
              <input type="submit" value="Create Model">
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

    $('#livephotoavailable').change(function() {
      if ($('#livephotoavailable').prop('checked')) {
        $('#livephotouploadcontainer').show();
      } else {
        $('#livephotouploadcontainer').hide();
      }
    });
  </script>

</html>