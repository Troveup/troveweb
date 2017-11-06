<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title> Trove: Account Settings </title>
  <%-- <c:import url="../fragments/baseHead.jsp"/>
  <c:import url="../fragments/staticHeader.jsp"/> --%>
  <c:import url="../fragments/analytics/all.jsp"/>
  <c:import url="../fragments/headers/commonHead.jsp"/>

  <%--TODO:  Replace these with commonHead--%>
  <link rel="stylesheet" href="/resources/stylesheets/main.css">
  <link rel="stylesheet" href="/resources/stylesheets/browse.css">
  <link href="/resources/stylesheets/styles.css" rel="stylesheet">
  <style>
    .modal-body.photo {
      padding-bottom: 0px;
    }

    .containers {
      position: relative;
      top: 10%;
      left: 50%;
      right: 0;
      bottom: 0;
      margin-left: -200px;
    }

    .action {
      width: 400px;
      height: 30px;
      margin: 10px 0;
    }

    .cropped > img {
      margin-right: 10px;
      border-radius: 50%;
    }

    form.form-horizontal {
      margin-bottom: 20px;
      margin-top: 60px;
    }

    .form-horizontal .form-group {
      margin-right: -15px;
      margin-left: -15px;
      padding: 10px;
    }

    input.form-control {
      border-radius: 5px;
    }

    input:-webkit-autofill {
      -webkit-box-shadow: 0 0 0px 1000px white inset;
    }

    input:focus:-webkit-autofill {
      -webkit-box-shadow: 0 0 0px 1000px white inset;
    }

    #profbutton {
      display: inline-block;
      padding: 6px 15px;
      border-radius: 2px;
      background-color: #8e8b8b;
      color: #FFF;
    }

    #profbutton.hover {
      background-color: #b7b0b0;
    }

    .profpic {
      float: left;
      width: 100px;
      height: 100px;
      border-radius: 50%;
      overflow: hidden;
    }

    .profpicholder {
      height: 100px;
      width: 100px;
      position: relative;
      margin: 0 auto 20px;
    }

    .account-settings-header.checkout {
      margin: 0px -15px 40px;
    }

    .profpic img {
      width: 100px;
      height: 100px;
    }

    input::-webkit-input-placeholder,
    input:-moz-placeholder,
    input::-moz-placeholder,
    input:-ms-input-placeholder {
      color: #666;
    }

    .redeemresponse {
      display: none;
    }

    .special-container {
      display: none;
    }

    #redeem2 {
      background: #DEDEDE;
      color: #666;
      line-height: 30px;
      padding: 5px;
      font-size: 20px;
    }

  </style>

  <c:if test="${not empty storeCredit}">
    <style>
      #redeem2 {
        display: block;
      }
    </style>
  </c:if>
</head>

<body>
<c:import url="../fragments/nav/topNavBar.jsp"/>
<div class="container cards_product_wrapper2">
  <div class="row">
    <div class="col-sm-12">
      <div class="cardy about profile">
        <div class="row account-settings-header checkout"><a class="topper"><span style="color: #2e2626">Account Settings</span></a>
        </div>
        <form class="form-horizontal">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          <div class="form-group">
            <label for="inputEmail3" class="col-sm-4 control-label">Profile Picture</label>
            <div class="col-sm-6">
              <div class="profpicholder">
                <div class="profpic"><img id="profpicimg" src="${authUser.fullProfileImagePath}"></div>
              </div>
              <a id="profbutton" data-toggle="modal" href="" data-target="#myModal">Change Profile Photo</a>
            </div>
          </div>
          <div class="row account-settings-header checkout"><a class="topper"></a></div>
          <div class="form-group">
            <label for="inputEmail3" class="col-sm-4 control-label">Name</label>
            <div class="col-sm-6">
              <input type="text" class="form-control" id="firstname" placeholder="${authUser.firstName}">
            </div>
          </div>
          <div class="form-group">
            <div class="col-sm-offset-4 col-sm-6" style="">
              <button id="firstandlast" type="submit" class="btn btn--red">Update Name</button>
            </div>
          </div>

          <div class="row account-settings-header checkout"><a class="topper"></a></div>
          <div class="form-group">
            <label for="inputEmail3" class="col-sm-4 control-label">Gift Card Redemption</label>
            <div class="col-sm-6">
              <input type="text" class="form-control" id="giftcardstring" placeholder="Enter your gift code">
            </div>
          </div>
          <div class="form-group">
            <div class="col-sm-offset-4 col-sm-6" style="">
              <a onclick="redeemGiftCard(); return false" href="#" class="btn btn--red"
                 style="width:inherit; font-size:16px;">REDEEM</a>
              <h5 class="redeemresponse" id="redeem1" style="padding-top:20px;"></h5>
              <h5 class="redeemresponse" id="redeem2" style="padding-top:0px;">Store Credit: $ ${storeCredit}</h5>
            </div>
          </div>


          <div class="row account-settings-header checkout"><a class="topper"></a></div>
          <div class="form-group">
            <label for="inputEmail3" class="col-sm-4 control-label">Password</label>
            <div class="col-sm-6">
              <input type="password" class="form-control" id="password1" placeholder="New Password">
            </div>
          </div>
          <div class="form-group">
            <label for="inputPassword3" class="col-sm-4 control-label">Confirm Password</label>
            <div class="col-sm-6">
              <input type="password" class="form-control" id="password2" placeholder="Confirm New Password">
            </div>
          </div>
          <div class="form-group">
            <div class="col-sm-offset-4 col-sm-6" style="">
              <button id="passwordy" type="submit" class="btn btn--red">Change Password</button>
            </div>
          </div>
          <div class="row account-settings-header checkout"><a class="topper"></a></div>
          <div class="form-group">
            <label for="inputPassword3" class="col-sm-4 control-label"></label>
            <div id="optin" lass="col-sm-6" style="text-align: left;">
              <div class="checkbox">
                <label>
                  <c:choose>
                    <c:when test="${optIn}">
                      <input id="optincheckbox" type="checkbox" checked> Keep me updated with emails about Trove
                    </c:when>
                    <c:otherwise>
                      <input id="optincheckbox" type="checkbox"> Keep me updated with emails about Trove
                    </c:otherwise>
                  </c:choose>
                </label>
              </div>
            </div>
          </div>
          <div class="form-group">
            <div class="col-sm-offset-4 col-sm-6" style="">
              <button id="privacy" type="submit" class="btn btn--red">Update Email Settings</button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>
</div>

<!-- <div class="col-sm-3">
  <div class="cardy sidemenu">
    <ul class="nav bs-docs-sidenav">
      <li class="sidelink"><a class="active" href="#about">Profile</a> </li> -->
<!-- <li class="sidelink"><a href="#team">Privacy</a> </li>
<li class="sidelink"><a href="#mission">Shipping</a> </li>
<li class="sidelink"><a href="#press">Billing</a> </li> -->
<!--   </ul>
</div>
</div> -->

<!-- <div class="col-sm-9">
  <div class="cardy about profile">
    <div class="row account-settings-header checkout"><a class="topper"><span style="color: #2e2626">Profile Settings</span></a></div> -->

<!-- <form class="form-horizontal"> -->

<!--  <div class="form-group form-row">
   <label for="profilePhoto" class="col-sm-4 account-control-label">Profile Photo</label>
   <div class="col-sm-6"><a data-toggle="modal" href="http://fiddle.jshell.net/bHmRB/51/show/" data-target="#myModal">Change Profile Photo</a></div>
 </div> -->

<!--  <div class="form-row row">
   <div class="col-sm-2 col-md-1"></div>
   <div class="form-group col-md-11 col-sm-10">
     <label for="firstName" class="account-control-label">Profile Photo</label>
     <div class="cropped"></div>
     <a class="profilelink" data-toggle="modal" href="http://fiddle.jshell.net/bHmRB/51/show/" data-target="#myModal">Change Profile Photo</a>
   </div>
   <div class="col-sm-2 col-md-1"></div>
 </div>

 <div class="form-row row">
   <div class="col-sm-2 col-md-1"></div>
   <div class="form-group has-feedback col-md-6 col-sm-5">
     <label for="firstName" class="account-control-label">First Name</label>
     <input id="sFirstName" name="shippingFirstName" type="text" placeholder="First" class="account-text-form">
     <span class="glyphicon form-control-feedback" id="shippingFirstName1"></span>
   </div>
   <div class="form-group col-md-6 col-sm-5">
     <label for="firstName" class="account-control-label">Last Name</label>
     <input id="sLastName" name="shippingLastName" type="text" placeholder="Last" class="account-text-form">
   </div>
   <div class="col-sm-2 col-md-1"></div>
 </div>
-->
<!-- <div class="form-row row">
  <div class="col-sm-2 col-md-1"></div>
  <div class="form-group col-md-11 col-sm-10">
    <label for="firstName" class="account-control-label">Email</label>
    <input id="sAddress1" name="shippingAddressLine1" type="text" placeholder="Email Address" class="account-text-form full">
  </div>
  <div class="col-sm-2 col-md-1"></div>
</div> -->

<!-- <div class="form-row row">
  <div class="col-sm-2 col-md-1"></div>
  <div class="form-group has-feedback col-md-6 col-sm-5">
    <label for="firstName" class="account-control-label">Password</label>
    <input id="sFirstName" name="shippingFirstName" type="text" placeholder="First" class="account-text-form">
    <span class="glyphicon form-control-feedback" id="shippingFirstName1"></span>
  </div>
  <div class="form-group col-md-6 col-sm-5">
    <label for="firstName" class="account-control-label">Confirm Password</label>
    <input id="sLastName" name="shippingLastName" type="text" placeholder="Last" class="account-text-form">
  </div>
  <div class="col-sm-2 col-md-1"></div>
</div> -->


<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content" style="width: 480px;">
      <div class="modal-body photo">
        <!-- photo upload -->
        <div class="containers">
          <div class="imageBox">
            <div class="thumbBox"></div>
            <div class="spinner" style="display: none">Loading...</div>
          </div>
          <div class="action">
            <input type="file" id="file" style="float:left; width: 250px">
            <input type="button" id="btnZoomIn" value="+" style="float: right">
            <input type="button" id="btnZoomOut" value="-" style="float: right">
          </div>
        </div>
        <script type="text/javascript">
          $(window).load(function () {
            var options =
            {
              thumbBox: '.thumbBox',
              spinner: '.spinner',
              imgSrc: 'avatar.png'
            }
            var cropper;
            $('#file').on('change', function () {
              var reader = new FileReader();
              reader.onload = function (e) {
                options.imgSrc = e.target.result;
                cropper = $('.imageBox').cropbox(options);
              }
              reader.readAsDataURL(this.files[0]);
              this.files = [];
            })
            $('#btnCrop').on('click', function () {
              var img = cropper.getDataURL();
              var imgblob = cropper.getBlob();
              // $('.cropped').append('<img src="'+img+'">');
              var formData = new FormData();
              // formData.append("timImage", this.response);
              formData.append("blob", imgblob);
              // formData.append('file',$('input[type=file]')[0].files[0]);

              jQuery.ajax({
                //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
                //url: 'http://localhost:8080/worker/uploadimatmodel',
                url: '/updateprofileimage',
                //url: 'http://localhost:4444/',
                data: formData,
                cache: false,
                contentType: false,
                processData: false,
                headers: {
                  'Access-Control-Allow-Origin': '*',
                  "X-CSRF-TOKEN": "${_csrf.token}"
                },
                type: 'POST',
                success: function (data) {
                  //Do something with this

                  // $('.profpic').append('<img src="'+data.profileImageThumbnailUrl+'">');
                  $('#profpicimg').attr('src', data.profileImageFullUrl);
                  $('#profilethumbnail').attr('src', data.profileImageThumbnailUrl)
                  // alert("Response: " + data.profileImageFullUrl + " " + data.profileImageThumbnailUrl);
                }
              });

            })
            $('#btnZoomIn').on('click', function () {
              cropper.zoomIn();
            })
            $('#btnZoomOut').on('click', function () {
              cropper.zoomOut();
            })
          });
        </script>
        <!-- end photo upload -->
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn--lightgray" data-dismiss="modal">Close</button>
        <button type="button" id="btnCrop" class="btn btn--red" data-dismiss="modal">Update Photo</button>
        <!-- <input type="button" id="btnCrop" value="Crop" style="float: right">  -->
      </div>
    </div>
  </div>
</div>

<!-- <div class="form-group form-row">
                <label for="firstName" class="col-sm-4 account-control-label">First Name</label>
                <div class="col-sm-6"><input type="email" class="account-text-form" id="inputEmail3" placeholder="${authUser.firstName}"></div>
              </div>
              <div class="form-group form-row">
                <label for="lastName" class="col-sm-4 account-control-label">Last Name</label>
                <div class="col-sm-6"><input type="email" class="account-text-form" id="inputEmail3" placeholder="${authUser.lastName}"></div>
              </div>
              <div class="form-group form-row">
                <label for="inputEmail3" class="col-sm-4 account-control-label">Email</label>
                <div class="col-sm-6">
                  <input type="email" class="account-text-form" id="inputEmail3" placeholder="Email">
                </div>
              </div>
              <div class="form-group form-row">
                <label for="inputPassword3" class="col-sm-4 account-control-label">Password</label>
                <div class="col-sm-6">
                  <input type="password" class="account-text-form" id="inputPassword3" placeholder="Password">
                </div>
              </div>
              <div class="form-group form-row">
                <label for="inputPassword3" class="col-sm-4 account-control-label">Confirm Password</label>
                <div class="col-sm-6">
                  <input type="password" class="account-text-form" id="inputPassword3" placeholder="Password">
                </div>
              </div> -->
<!-- <div class="form-group form-row">
  <div class="col-sm-offset-4 col-sm-6">
    <button id="firstandlast" class="btn btn--red btn-md submitter">Change Name</button> -->
<!--  <input class="btn btn--red btn-md submitter" type="submit" value="Save" style="width: 100px;"> -->
<!-- </div>
</div>
<br><br> -->


<!-- <div class="form-row row">
  <div class="col-sm-2 col-md-1"></div>
  <div class="form-group has-feedback col-md-6 col-sm-5">
    <label for="password" class="account-control-label">ACCOUNT PASSWORD</label>
    <input id="password" name="shippingFirstName" type="password" placeholder="New Password" class="account-text-form">
    <span class="glyphicon form-control-feedback" id="shippingFirstName1"></span>
  </div>
  <div class="form-group col-md-6 col-sm-5">
    <button id="passwordy" class="btn btn--red btn-md submitter">Change Password</button>
  </div>
  <div class="col-sm-2 col-md-1"></div>
</div>

<br><br>

<div class="form-row row">
  <div class="col-sm-2 col-md-1"></div>
  <div class="form-group has-feedback col-md-6 col-sm-5">
    <label for="password" class="account-control-label">Privacy Opt Out</label>
    <span class="glyphicon form-control-feedback" id="shippingFirstName1"></span>
  </div>
  <div class="form-group col-md-6 col-sm-5">
    <button id="privacy" class="btn btn--red btn-md submitter">Opt out of emails</button>
  </div>
  <div class="col-sm-2 col-md-1"></div>
</div>



</form>   -->

<!-- <div class="row account-settings-header"><a name="privacy"><span style="color: #2e2626">Privacy Settings</span></a></div>
<form class="form-horizontal">
  <div class="form-group form-row">
    <div class="col-sm-4 account-control-label">
      <input type="checkbox" name="email-notification" value="Yes" class="">
    </div>
    <div class="col-sm-6 form-normal-label"><span>Receive notification emails from Trove</span></div>
  </div>
  <div class="form-group form-row">
    <div class="col-sm-4 account-control-label">
      <input id="email-option" type="checkbox" name="email-notification" value="Yes" class="">
      <label for="email-option"></label>
    </div>
    <div class="col-sm-6 form-normal-label">
      <span>Receive news from Trove</span>
    </div>
  </div>
  <div class="form-group form-row">
    <div class="col-sm-offset-4 col-sm-6">
      <input class="btn btn--red btn-md" type="submit" value="Save" style="width: 100px;">
    </div>
  </div>
</form>

<div class="row account-settings-header"><a name="shipping"><span style="color: #2e2626">Shipping Address</span></a></div>
<form class="form-horizontal">
  <div class="form-group form-row">
    <div class="col-sm-4 account-control-label">
    </div>
    <div class="col-sm-4 form-normal-label">
      <span>Andrew Hong<br>123 Alphabet Lane<br>New York, NY 10005</span><br>
      <input data-toggle="modal" data-target="#edit-address-modal" class="btn btn--lightgray btn-md" type="button" value="Edit" style="width: 70px; margin-top: 10px">
      <input class="btn btn--lightgray btn-md" type="button" value="Delete" style="width: 90px; margin-top: 10px">
    </div>
    <div class="col-sm-4 form-normal-label">
      <span>Andrew Hong<br>123 Alphabet Lane<br>New York, NY 10005</span><br>
      <input data-toggle="modal" data-target="#edit-address-modal" class="btn btn--lightgray btn-md" type="button" value="Edit" style="width: 70px; margin-top: 10px">
      <input class="btn btn--lightgray btn-md" type="button" value="Delete" style="width: 90px; margin-top: 10px">
    </div>
  </div>
  <div class="form-group form-row">
    <div class="col-sm-4 account-control-label"></div>
    <div class="col-sm-4 form-normal-label"><input data-toggle="modal" data-target="#edit-address-modal" class="btn btn--red btn-md" type="button" value=" + Add Address" style="margin-top: 10px"></div>
  </div>
</form>

<div class="row account-settings-header"><a name="billing"><span style="color: #2e2626">Billing Address</span></a></div>
<form class="form-horizontal">
  <div class="form-group form-row">
    <div class="col-sm-4 account-control-label"></div>
    <div class="col-sm-4 form-normal-label">
      <span>Andrew Hong<br>123 Alphabet Lane<br>New York, NY 10005</span><br>
      <input data-toggle="modal" data-target="#edit-address-modal" class="btn btn--lightgray btn-md" type="button" value="Edit" style="width: 70px; margin-top: 10px">
      <input class="btn btn--lightgray btn-md" type="button" value="Delete" style="width: 90px; margin-top: 10px">
    </div>
    <div class="col-sm-4 form-normal-label">
      <span>Andrew Hong<br>123 Alphabet Lane<br>New York, NY 10005</span><br>
      <input data-toggle="modal" data-target="#edit-address-modal" class="btn btn--lightgray btn-md" type="button" value="Edit" style="width: 70px; margin-top: 10px">
      <input class="btn btn--lightgray btn-md" type="button" value="Delete" style="width: 90px; margin-top: 10px">
    </div>
  </div>
  <div class="form-group form-row">
    <div class="col-sm-4 account-control-label"></div>
    <div class="col-sm-4 form-normal-label"><input data-toggle="modal" data-target="#edit-address-modal" class="btn btn--red btn-md" type="button" value=" + Add Address" style="margin-top: 10px"></div>
    <div class="col-sm-4 form-normal-label"></div>
  </div>
</form>

<div class="row account-settings-header"><a name="orders"><span style="color: #2e2626">Orders</span></a></div>
<form class="form-horizontal">
  <div class="form-group form-row">
    <div class="col-sm-4 account-control-label"></div>
    <div class="col-sm-6 form-normal-label"><span>Order list here</span></div>
  </div>
</form>

</div>
<br></br>
<br></br>
<br></br>
<br></br>
</div>-->


<!-- <c:import url="../fragments/contentJS.jsp"/>
    <c:import url="../fragments/commonJSSources.jsp"/> -->
<script src="/resources/js/vendor/cropbox.js"></script>

<script>

  function testUpdateProfilePhoto() {
    var xhf = new XMLHttpRequest();
    xhf.open('GET', "https://storage.googleapis.com/trove-qa-teststore/tim-headshot-base64", true);
    xhf.responseType = 'blob';
    xhf.onload = function (e) {
      var formData = new FormData();
      formData.append("timImage", this.response);
      jQuery.ajax({
        //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
        //url: 'http://localhost:8080/worker/uploadimatmodel',
        url: '/updateprofileimage',
        //url: 'http://localhost:4444/',
        data: formData,
        cache: false,
        contentType: false,
        processData: false,
        headers: {
          'Access-Control-Allow-Origin': '*',
          "X-CSRF-TOKEN": "${_csrf.token}"
        },
        type: 'POST',
        success: function (data) {
          //Do something with this
          alert("Response: " + data.profileImageFullUrl + " " + data.profileImageThumbnailUrl);
        }
      });
    }
    xhf.send();
  }

  function updateauthUserFirstLastName() {
    var data = new FormData();
    var firstName = document.getElementById("firstname").value;
    // var lastName = document.getElementById("sLastName").value;

    data.append("firstName", firstName);
    data.append("lastName", "");

    jQuery.ajax({
      //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
      //url: 'http://localhost:8080/worker/uploadimatmodel',
      url: '/updatefirstlastname',
      //url: 'http://localhost:4444/',
      data: data,
      cache: false,
      contentType: false,
      processData: false,
      headers: {
        'Access-Control-Allow-Origin': '*',
        "X-CSRF-TOKEN": "${_csrf.token}"
      },
      type: 'POST',
      success: function (data) {
        //Do something with this
        alert("Completed successfully!");
      }
    });
  }

  function updatePassword() {
    var data = new FormData();
    if (document.getElementById("password1").value = document.getElementById("password2").value) {
      var password = document.getElementById("password1").value;
      data.append("password", password);
      jQuery.ajax({
        //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
        //url: 'http://localhost:8080/worker/uploadimatmodel',
        url: '/updatepassword',
        //url: 'http://localhost:4444/',
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        headers: {
          'Access-Control-Allow-Origin': '*',
          "X-CSRF-TOKEN": "${_csrf.token}"
        },
        type: 'POST',
        success: function (data) {
          //Do something with this
          alert("Completed successfully!");
        }
      });
    } else {
      alert("Passwords don't match! Try again");
    }

  }

  function optout() {
    var status;
    var data = new FormData();
    status = !document.getElementById("optincheckbox").checked;

    data.append("optOutStatus", status);

    jQuery.ajax({
      //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
      //url: 'http://localhost:8080/worker/uploadimatmodel',
      url: '/optout',
      //url: 'http://localhost:4444/',
      data: data,
      cache: false,
      contentType: false,
      processData: false,
      headers: {
        'Access-Control-Allow-Origin': '*',
        "X-CSRF-TOKEN": "${_csrf.token}"
      },
      type: 'POST',
      success: function (data) {
        //Do something with this
        alert("Completed successfully!");
      }
    });
  }
</script>

<script>
  $(document).ready(function () {
    var count = parseInt($('.tallyho-cart').html());
    if (count >= 1) {
      $('.tallyho-cart').addClass('showitnow');
    }

    $('#firstandlast').on('click', function (e) {
      e.preventDefault();
      updateauthUserFirstLastName();
    });
    $('#passwordy').on('click', function (e) {
      e.preventDefault();
      updatePassword();
    });
    $('#privacy').on('click', function (e) {
      e.preventDefault();
      optout();
    });
  });
</script>

<script>

  function redeemGiftCard() {
    $('#redeem1').slideUp();
    var data = new FormData();
    data.append('giftCardString', $('#giftcardstring').val());
    jQuery.ajax({
      url: '/redeemgiftcard',
      data: data,
      cache: false,
      contentType: false,
      processData: false,
      headers: {
        'Access-Control-Allow-Origin': '*',
        "X-CSRF-TOKEN": "${_csrf.token}"
      },
      type: 'POST',
      success: function (data) {
        //Do something with this
        if (data.success) {
          $('#redeem1').text("$ " + data.giftCardAmount + " credit has been added to your account.");
          $('#redeem2').text("You have $ " + data.storeCreditBalance + " credit in your account");
          $('#redeem1').slideDown();
          $('#redeem2').slideDown();
          $('#giftcardstring').val("");
        }
        else
        // $('#redeem1').text("Sorry!  Error message: " + data.redemptionError);
          $('#redeem1').text("Sorry, that code is invalid.");
        $('#redeem1').slideDown();
        $('#giftcardstring').val("");
      }
    });
  }

</script>


</body>
</html>