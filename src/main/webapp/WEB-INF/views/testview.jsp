<%--
  Created by IntelliJ IDEA.
  User: tim
  Date: 5/20/15
  Time: 8:55 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head lang="en">
  <meta charset="UTF-8">
  <title></title>
  <script src="https://code.jquery.com/jquery-1.11.3.min.js"></script>
</head>
<body>

<div><button id="demo" onclick="clickRetrieveModel()">Click me to upload some stuff.</button></div>
<div><button id="demo1" onclick="clickCallUploadModel()">Click me to make to worker do some work.</button></div>
<div><button id="demo2" onclick="prepareImatCart()">Click me to make test imatcartcreation.</button></div>
<div><button id="demo3" onclick="submitOrder()">Click me submit an order.</button></div>

<c:if test="${not empty user.shoppingCart}">
  <c:forEach var="cartItem" items="${user.shoppingCart.cartItems}">
      <div>Available cart item with ID: ${cartItem.cartItemId}</div>
  </c:forEach>
</c:if>

<div>
  <div>
    <label for="cartItemIdToPrototype">Cart Item Id to Prototype:</label>
    <input type="number" id="cartItemIdToPrototype" />
  </div>
</div>

<div><button onclick="pushPrototypeId(document.getElementById('cartItemIdToPrototype').value)">Add itemId to prototype list</button></div>

<div>
  <div>
    <label for="orderId">OrderId:</label>
    <input type="number" id="orderId" />
  </div>
</div>

<div>
  <div>
    <label for="promoCode">PromoCode:</label>
    <input type="text" id="promoCode" />
  </div>
</div>

<div>
    <div>
        <label for="firstName">First Name:</label>
        <input type="text" id="firstName" />
    </div>
</div>

<div>
    <div>
        <label for="lastName">Last Name:</label>
        <input type="text" id="lastName" />
    </div>
</div>

<div>
    <div>
        <label for="password">Password:</label>
        <input type="password" id="password" />
    </div>
</div>

<div>
    <div>
        <label for="followfield">Follow User Id:</label>
        <input type="number" id="followfield" />
    </div>
</div>

<button onclick="cancelOrder()">Cancel Specified Order</button>
<button onclick="applyPromoCode()">Apply Promo Code</button>
<button onclick="removePromoCode()">Remove Promo Code</button>
<div><button id="addfromcard" onclick="addFromCard()">Click to sample add from card</button></div>
<div><button id="testfileget" onclick="testFileGet()">Click to test getting a file</button></div>
<div><button id="teststatusget" onclick="testGetStatus()">Click to test getting a set of cart item statuses</button></div>
<div><button onclick="testUpdateProfilePhoto()">Test update profile image</button></div>
<div><button onclick="optout(true)">Opt user out of e-mail</button></div>
<div><button onclick="optout(false)">Opt user in to e-mail</button></div>
<div><button onclick="updateUserFirstLastName()">Update username from fields.</button></div>
<div><button onclick="updatePassword()">Update password from field.</button></div>
<div><button onclick="followUser()">Follow user with userid from field.</button></div>
<div><button onclick="unfollowUser()">Unfollow user with userid from field.</button></div>
<div><button onclick="ftuisubmit()">Submit Sample FTUI</button></div>
<div><button onclick="triggerSomeShapewaysCallbackAction()">SHAPEWAYSYEAAAHHH</button></div>

<div>
    <div>
        <label for="itemIdForPricing">Item ID for pricing:</label>
        <input type="number" id="itemIdForPricing" />
    </div>
</div>

<div>
    <div>
        <label for="materialIdForPricing">Material ID for pricing:</label>
        <input type="number" id="materialIdForPricing" />
    </div>
</div>

<div>
    <div>
        <label for="finishIdForPricing">Finish ID for pricing:</label>
        <input type="number" id="finishIdForPricing" />
    </div>
</div>

<div><button onclick="sendSamplePricingRequest()">Try a pricing sample</button></div>
<div><button onclick="testCurateEmail()">Curate FTUE EMAILS</button></div>

<div>
    <div>
        <label for="ftueLandingId">Ftue Landing Id</label>
        <input type="text" id="ftueLandingId" />
    </div>
</div>

<div><button onclick="testFtueCartCreate()">Test creating a FTUE cart</button></div>

<div>
    <div>
        <label for="ftueCheckoutId">Ftue Checkout Id</label>
        <input type="text" id="ftueCheckoutId" value="" />
    </div>
</div>

<div><button onclick="testFtueUsePromo()">Test using a promo code</button></div>
<div><button onclick="testFtueFinalizeCheckout()">Test final FTUE checkout</button></div>
<div><hr></div>
<button onclick="testCreateCollection()">Create Collection</button>

<div>
    <div>
        <label for="collectionId">Collection ID for updating:</label>
        <input type="number" id="collectionId" />
    </div>
</div>

<button onclick="testUpdateCollection()">Update Collection</button>

<div>
    <div>
        <label for="itemId">Item ID for adding to or removing from collection:</label>
        <input type="number" id="itemId" />
    </div>
</div>

<button onclick="testAddItemToCollection()">Add Collection Item</button>
<button onclick="testRemoveItemFromCollection()">Remove Collection Item</button>

<div>
    <div>
        <label for="userId">User ID:</label>
        <input type="number" id="userId" />
    </div>
</div>

<div>
    <div>
        <label for="pageNumber">Scrolling page number:</label>
        <input type="number" id="pageNumber" />
    </div>
</div>

<button onclick="testGetCollections()">Get Collections</button>
<button onclick="testDeleteCollection()">Delete Collection</button>
<div><button onclick="testGetCollectionItems()">Get Collection Items</button></div>
<button onclick="testGetCollectionFollowers()">Get Collection Followers</button>
<div></div>
<button onclick="testFollowCollection()">Follow Collection</button>
<button onclick="testUnfollowCollection()">Unfollow Collection</button>
<div></div>
<button onclick="testFireRemakeIncrement()">Test Fire Remake Counter Increment</button>
<button onclick="testFireTroveIncrement()">Test Fire Trove Counter Increment</button>
<div></div>
<button onclick="setItemDefaultCollection()">Set Item Default Collection</button>
<button onclick="clearItemDefaultCollection()">Clear Item Default Collection</button>
<div><hr></div>
<button onclick="testPagedFeedItems()">Get Paged Feed Items</button>
<button onclick="testPagedCategoryItems()">Paged Category Items</button>
<button onclick="testPagedTroveItems()">Get Paged Trove Items</button>
<div><hr></div>
<button onclick="testGetUserFollowersPaged()">Get User Followers</button>
<button onclick="testGetUserFollowedPaged()">Get Followed Users</button>
<button onclick="removeFromTrove()">Remove Item from Trove</button>
<button onclick="testGetOpenOrders()">Get Paginated Orders</button>

<div><hr></div>

<div>
  <div>
    <label for="orderItemId">Order Cart Item Id:</label>
    <input type="number" id="orderItemId" />
  </div>
</div>
<button onclick="cancelLineItem()">Cancel order item</button>
<button onclick="completeOrderLineItem()">Complete order item</button>

<div><hr></div>
Gift Cards

<div>
  <div>
  <label for="digital">Digital Item?</label>
  <select id="digital" name="digital">
    <option value="true">true</option>
    <option value="false">false</option>
  </select>
  </div>
  <div>
    <label for="amount">Amount</label>
    <input id="amount" name="amount" type="number" value="50.00">
  </div>
  <div>
    <label for="toName">Recipient Name</label>
    <input id="toName" name="toName" type="text" value="To Name">
  </div>
  <div>
    <label for="toEmail">Recipient Email</label>
    <input id="toEmail" name="toEmail" type="text" value="Email">
  </div>
  <div>
    <label for="fromName">Sender Name</label>
    <input id="fromName" name="fromName" type="text" value="From Name">
  </div>
  <div>
    <label for="fromEmail">Sender Email</label>
    <input id="fromEmail" name="fromEmail" type="text" value="From Email">
  </div>
  <div>
    <label for="date">Send Date</label>
    <input id="date" name="date" type="text" value="Send Date">
  </div>

  <button onclick="addGiftCard()">Add Gift Card to Cart</button>
  <div style="padding-top:10px">
    <div>
      <label for="giftcardstring">Gift Card String</label>
      <input id="giftcardstring" name="giftcardstring" type="text">
    </div>
    <button onclick="redeemGiftCard()">Redeem Gift Card</button>
  </div>
</div>


</body>

<script src="https://code.jquery.com/jquery-1.11.3.min.js"></script>
<script src="https://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
<script>

  /**
  *   Fully functional script template for uploading post-customizer-submit page data.  Required identifiers are
   *   specified in the comments.
   */
  var imageBlob;
  var modelBlob;
  var blob;

  var prototypeIds = [];

  function ajaxUpload()
  {
    var data = new FormData();

    //Slider weight data, expecting prefix "modelWeight-" for parsing purposes
    data.append("modelWeight-prong1", 25.62);
    data.append("modelWeight-prong2", 26.63);

    //The parent model from which this model originated, expecting this exact identifier
    data.append("parentModelId", 2);

    //Indicate whether they are just saving this to their trove or if they should have this added to their
    //shopping cart, expecting this exact identifier
    data.append("shouldAddToCart", true);

    //Other data, expecting these exact identifiers
    data.append("modelName", "Sample Model Name");
    data.append("modelDescription", "Sample Model Description");
    data.append("isPrivate", false);
    data.append("modelEstimatedPrice", 25.26);
    //Gold material ID
    data.append("modelMaterial", "d01a95ab-aaba-44f0-a4b6-8f72b66655b1");
    //14kt Rose Gold Finish
    data.append("modelMaterialFinish", "a9962d5c-04d7-4f29-93d1-56dc43c8d766");
    data.append("cartQuantity", 1);

    //Model binary OBJ data to save to the cloud, expecting this field identifier
    //data.append("model", new Blob(['modelbinarydata'], ['application/octet-stream']));
    data.append("model", blob);

    //File extension of the model, expecting this field identifier
    data.append("modelExtension", ".stl");

    //Unity rendered images for initial item creation, expecting prefix "image-" for parsing purposes
    data.append("image-0", new Blob(['imagebinarydata'], ['image/png']));
    data.append("image-1", new Blob(['imagebinarydata'], ['image/png']));
    data.append("image-2", new Blob(['imagebinarydata'], ['image/png']));

    //File extensions of the images, expecting this field identifier
    data.append("imageExtension", ".png");

    jQuery.ajax({
      url: 'https://project-troveup-qa.appspot.com/savenewmodel',
      //url: 'http://localhost:8080/savenewmodel',
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
      success: function(data){
        //Do something with this
        alert("Upload success!");
      }
    });
  }

  function clickRetrieveModel()
  {
    var xhf = new XMLHttpRequest();
    xhf.open('GET', "https://storage.googleapis.com/trove-qa-teststore/bar_necklace.stl", true);
    xhf.responseType = 'blob';

    xhf.onload = function(e)
    {
      blob = this.response;
      ajaxUpload();
    }

    xhf.send();

  }

  function clickCallUploadModel()
  {
    var data = new FormData();
    data.append("itemId", 27);

    jQuery.ajax({
      //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
      url: 'http://localhost:8080/worker/uploadimatmodel',
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
      success: function(data){
        //Do something with this
        alert("Upload success!");
      }
    });
  }

  function prepareImatCart()
  {
    var data = new FormData();
    var addressFirstName = "Test";
    var addressLastName = "User";
    var addressLine1 = "20 Exchange Pl";
    var addressLine2 = "";
    var addressCity = "New York";
    var addressStateId = 25;
    var countryId = 1;
    var postalCode = "10005";
    var paymentNonce = "fake-valid-nonce";
    var cartEmail = "tuser001@mailinator.com";

    //Set up the billing address data
    data.append('billingFirstName', addressFirstName);
    data.append('billingLastName', addressLastName);
    data.append('billingAddressLine1', addressLine1);
    data.append('billingAddressLine2', addressLine2);
    data.append('billingCity', addressCity);
    data.append('billingZip', postalCode);
    data.append('billingStateId', addressStateId);
    data.append('billingCountryId', countryId);


    //Set up the shipping address data (can be the same)
    data.append('shippingFirstName', addressFirstName);
    data.append('shippingLastName', addressLastName);
    data.append('shippingAddressLine1', addressLine1);
    data.append('shippingAddressLine2', addressLine2);
    data.append('shippingCity', addressCity);
    data.append('shippingZip', postalCode);
    data.append('shippingStateId', addressStateId);
    data.append('shippingCountryId', countryId);
    data.append('cartEmail', cartEmail);

    //Set up the braintree payment nonce after credit card information has been taken
    data.append('paymentNonce', paymentNonce);

      //Add cart items that should be prototyped
    for (var i = 0; i < prototypeIds.length; ++i) {
      data.append('prototypeItem-' + i, prototypeIds[i]);
    }

    jQuery.ajax({
      //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
      //url: 'http://localhost:8080/worker/uploadimatmodel',
      url: '/preparecart',
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
      success: function(data){
        //Do something with this
        alert("Response: " + data);
      }
    });
  }

  function submitOrder()
  {
    var data = new FormData();
    jQuery.ajax({
      //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
      //url: 'http://localhost:8080/worker/uploadimatmodel',
      url: '/completecartorder',
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
      success: function(data){
        //Do something with this
        alert("Response: " + data);
      }
    });
  }

  function cancelOrder()
  {
    var orderId = document.getElementById("orderId").value;

    var data = new FormData();
    data.append("orderId", orderId);

    jQuery.ajax({
      //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
      //url: 'http://localhost:8080/worker/uploadimatmodel',
      url: 'http://localhost:8080/cancelorder',
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
      statusCode:
      {
        304: function()
        {
          alert("Refund not possible at this time due to order status.");
        },
        402: function()
        {
          alert("Print order was cancelled, but refund couldn't be processed.  Contact us.");
        },
        200: function()
        {
          alert("Refund processed properly.");
        }
      }
    });
  }

  function applyPromoCode()
  {
    var promoCode = document.getElementById("promoCode").value;

    var data = new FormData();
    data.append("promoCode-0", promoCode);

    jQuery.ajax({
      //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
      //url: 'http://localhost:8080/worker/uploadimatmodel',
      url: 'http://localhost:8080/addpromocodes',
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
      success: function()
      {
        //Do something with this
        alert("Response: " + data.getJson());
      }
    });
  }

  function removePromoCode()
  {
    var promoCode = document.getElementById("promoCode").value;

    var data = new FormData();
    data.append("promoCode-0", promoCode);

    jQuery.ajax({
      //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
      //url: 'http://localhost:8080/worker/uploadimatmodel',
      url: 'http://localhost:8080/removepromocodes',
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
      success: function()
      {
        //Do something with this
        alert("Response: " + data.getJson());
      }
    });
  }

  function addFromCard()
  {
      var data = new FormData();
      data.append("itemId", 104);
      data.append("materialId", "d01a95ab-aaba-44f0-a4b6-8f72b66655b1");
      data.append("finishId", "8f1cc7fa-8422-43e3-abf7-a7a2f8f63b8f");
      data.append("size", 3);
      data.append("estimatedPrice", 25.75);

      jQuery.ajax({
          //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
          //url: 'http://localhost:8080/worker/uploadimatmodel',
          url: '/noncustaddtobag',
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
          success: function()
          {
              //Do something with this
              alert("Response: " + data);
          }
      });
  }

    function testFileGet()
    {
        var data = new FormData();

        jQuery.ajax({
            //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
            //url: 'http://localhost:8080/worker/uploadimatmodel',
            url: '/testgooglecloud',
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
            success: function()
            {
                //Do something with this
                alert("Response: " + data);
            }
        });
    }

    function testUpdateProfilePhoto()
    {

        var xhf = new XMLHttpRequest();
        xhf.open('GET', "https://storage.googleapis.com/trove-qa-teststore/tim-headshot-base64", true);
        xhf.responseType = 'blob';

        xhf.onload = function(e)
        {
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
                success: function(data)
                {
                    //Do something with this
                    alert("Response: " + data.profileImageFullUrl + " " + data.profileImageThumbnailUrl);
                }
            });
        }

        xhf.send();

    }
    function carryOutPhotoUpload(data)
    {
        alert("Data size is " + data.get("timImage").length);

    }

    function optout(status)
    {
        var data = new FormData();
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
            success: function(data)
            {
                //Do something with this
                alert("Completed successfully!");
            }
        });
    }

    function updateUserFirstLastName()
    {
        var data = new FormData();

        var firstName = document.getElementById("firstName").value;
        var lastName = document.getElementById("lastName").value;

        data.append("firstName", firstName);
        data.append("lastName", lastName);

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
            success: function(data)
            {
                //Do something with this
                alert("Completed successfully!");
            }
        });
    }

    function updatePassword()
    {
        var data = new FormData();

        var password = document.getElementById("password").value;

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
            success: function(data)
            {
                //Do something with this
                alert("Completed successfully!");
            }
        });
    }

    function followUser()
    {
        var data = new FormData();

        data.append("userId", document.getElementById("followfield").value);

        jQuery.ajax({
            //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
            //url: 'http://localhost:8080/worker/uploadimatmodel',
            url: '/followuser',
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
            success: function(data)
            {
                //Do something with this
                alert("Completed successfully!");
            }
        });
    }

    function unfollowUser()
    {
        var data = new FormData();

        data.append("userId", document.getElementById("followfield").value);

        jQuery.ajax({
            //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
            //url: 'http://localhost:8080/worker/uploadimatmodel',
            url: '/unfollowuser',
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
            success: function(data)
            {
                //Do something with this
                alert("Completed successfully!");
            }
        });
    }

  function ftuisubmit()
  {
      var data = new FormData();

      data.append("parentModelId", 104);
      data.append("firstName", "test");
      data.append("lastName", "user");
      data.append("email", "tuser001@mailinator.com");
      data.append("address1", "20 Exchange Pl");
      data.append("address2", "Apt 1604");
      data.append("city", "New York");
      data.append("state", "NY");
      data.append("zip", "10005");
      data.append("country", "USA");
      data.append("size", 7.5);

      //Customizer provided values
      //data.append("modelWeight-Inner",.89);
      //data.append("vmesh-0", "Cube.Inner");

      jQuery.ajax({
          //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
          //url: 'http://localhost:8080/worker/uploadimatmodel',
          url: '/ftuisubmit',
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
          success: function(data)
          {
              //Do something with this
              alert("Success boolean: " + data.isSuccess + " and errorMessage " + data.errorMessage);
          }
      });
  }

    function triggerSomeShapewaysCallbackAction()
    {
        var data = new FormData();

        jQuery.ajax({
            //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
            //url: 'http://localhost:8080/worker/uploadimatmodel',
            url: '/requestshapewaysaccess',
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
            success: function(data)
            {
                //Do something with this
                alert("Success!");
            }
        });
    }

    function sendSamplePricingRequest()
    {
        var data = new FormData();

        data.append("itemId", document.getElementById("itemIdForPricing").value);
        data.append("materialId", document.getElementById("materialIdForPricing").value);
        data.append("finishId", document.getElementById("finishIdForPricing").value);

        jQuery.ajax({
            //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
            //url: 'http://localhost:8080/worker/uploadimatmodel',
            url: '/cardpriceestimate',
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
            success: function(data)
            {
                //Do something with this
                alert("Success!  String that came back: " + data);
            }
        });
    }

  function testCurateEmail()
  {
      var data = new FormData();

      jQuery.ajax({
          //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
          //url: 'http://localhost:8080/worker/uploadimatmodel',
          url: '/curateemailreadyfreeftuerequests',
          //url: 'http://localhost:4444/',
          data: data,
          cache: false,
          contentType: false,
          processData: false,
          headers: {
              'Access-Control-Allow-Origin': '*',
              "X-CSRF-TOKEN": "${_csrf.token}"
          },
          type: 'GET',
          success: function(data)
          {
              //Do something with this
              alert("Success!  String that came back: " + data);
          }
      });
  }

  function testFtueCartCreate()
  {
      var data = new FormData();
      var addressFirstName = "Test";
      var addressLastName = "User";
      var addressLine1 = "20 Exchange Pl";
      var addressLine2 = "";
      var addressCity = "New York";
      var addressStateId = 25;
      var countryId = 1;
      var postalCode = "10005";
      var paymentNonce = "fake-valid-nonce";

      //Set up the billing address data
      data.append('billingFirstName', addressFirstName);
      data.append('billingLastName', addressLastName);
      data.append('billingAddressLine1', addressLine1);
      data.append('billingAddressLine2', addressLine2);
      data.append('billingCity', addressCity);
      data.append('billingZip', postalCode);
      data.append('billingStateId', addressStateId);
      data.append('billingCountryId', countryId);


      //Set up the shipping address data (can be the same)
      data.append('shippingFirstName', addressFirstName);
      data.append('shippingLastName', addressLastName);
      data.append('shippingAddressLine1', addressLine1);
      data.append('shippingAddressLine2', addressLine2);
      data.append('shippingCity', addressCity);
      data.append('shippingZip', postalCode);
      data.append('shippingStateId', addressStateId);
      data.append('shippingCountryId', countryId);

      //Set up the braintree payment nonce after credit card information has been taken
      data.append('paymentNonce', "fake-valid-nonce");
      data.append('materialId', 1);
      data.append('finishId', 98);
      data.append('size', 7.5);
      data.append('landingUuid', document.getElementById("ftueLandingId").value);
      data.append('cartEmail', 'tuser@mailinator.com');

      jQuery.ajax({
          //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
          //url: 'http://localhost:8080/worker/uploadimatmodel',
          url: '/initftuecheckout',
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
          success: function(data)
          {
              //Do something with this
              alert("Success!  Data that came back: " + data.subTotal + " " + data.tax + " " + data.shipping + " " +
                      data.grandTotal + " " + data.checkoutReference);
          }
      });
  }

  function testFtueUsePromo()
  {
      var data = new FormData();
      data.append('promoCode', "kargo20");
      data.append('ftueCheckoutId', document.getElementById("ftueCheckoutId").value);

      jQuery.ajax({
          //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
          //url: 'http://localhost:8080/worker/uploadimatmodel',
          url: '/applyftuepromocode',
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
          success: function(data)
          {
              //Do something with this
              alert("Success!  Data that came back: " + data.subTotal + " " + data.tax + " " + data.shipping + " " +
                      data.grandTotal + " " + data.appliedCodes[0].promoCode);
          }
      });
  }

  function testFtueFinalizeCheckout()
  {
      var data = new FormData();
      data.append('ftueCheckoutId', document.getElementById("ftueCheckoutId").value);

      jQuery.ajax({
          //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
          //url: 'http://localhost:8080/worker/uploadimatmodel',
          url: '/submitftuecheckout',
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
          success: function(data)
          {
              //Do something with this
              alert("Success!  String that came back: " + data);
          }
      });
  }

    function testCreateCollection()
    {
        var data = new FormData();
        data.append('collectionName', 'A new collection!');
        data.append('collectionDescription', 'A new collection description!');
        data.append('isPrivate', false);

        jQuery.ajax({
            //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
            //url: 'http://localhost:8080/worker/uploadimatmodel',
            url: '/createcollection',
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
            success: function(data)
            {
                //Do something with this
                alert("Success!  Collection ID that came back: " + data.collectionList[0].collectionId);
            }
        });
    }
    function testUpdateCollection()
    {
        var data = new FormData();
        data.append('collectionId', document.getElementById("collectionId").value)
        data.append('collectionName', 'updated test collection');
        data.append('collectionDescription', 'updated test description');
        data.append('isPrivate', false);

        jQuery.ajax({
            //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
            //url: 'http://localhost:8080/worker/uploadimatmodel',
            url: '/updatecollection',
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
            success: function(data)
            {
                //Do something with this
                alert("Success!  String that came back: " + data);
            }
        });
    }
    function testAddItemToCollection()
    {
        var data = new FormData();
        data.append('collectionId', document.getElementById("collectionId").value)
        data.append('itemId', document.getElementById("itemId").value);

        jQuery.ajax({
            //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
            //url: 'http://localhost:8080/worker/uploadimatmodel',
            url: '/addcollectionitem',
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
            success: function(data)
            {
                //Do something with this
                alert("Success!  String that came back: " + data);
            }
        });
    }
    function testRemoveItemFromCollection()
    {
        var data = new FormData();
        data.append('collectionId', document.getElementById("collectionId").value)
        data.append('itemId', document.getElementById("itemId").value);

        jQuery.ajax({
            //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
            //url: 'http://localhost:8080/worker/uploadimatmodel',
            url: '/removecollectionitem',
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
            success: function(data)
            {
                //Do something with this
                alert("Success!  String that came back: " + data);
            }
        });
    }
    function testGetCollections()
    {
        var data = Object.create(null);
        //User identifier for collections to pull
        data['userId'] = document.getElementById("userId").value;

        //Page number of the page to load
        data['pageNumber'] = document.getElementById("pageNumber").value;

        //Object identifiers from the last page load.  This is used to make sure that there is no duplicate
        //data for the next page load
        data['objectId-0'] = 0;
        data['objectId-1'] = 3;

        //Maximum number of items that are allowed to be returned by the server
        data['pageLimit'] = 12;

        //The number of extra items beyond the pageLimit that the back-end will load.  If there are overlapping items
        //specified by the collectionObject param that need to be eliminated from the returned list (as there may
        //be new items that have been populated), these extras will replace the
        data['duplicateBuffer'] = 3;

        jQuery.ajax({
            //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
            //url: 'http://localhost:8080/worker/uploadimatmodel',
            url: '/collections',
            //url: 'http://localhost:4444/',
            data: jQuery.param(data),
            cache: false,
            contentType: false,
            processData: false,
            headers: {
                'Access-Control-Allow-Origin': '*',
                "X-CSRF-TOKEN": "${_csrf.token}"
            },
            type: 'GET',
            success: function(data)
            {
                //Do something with this
                alert("Success!  Array size that came back: " + data.collectionList.length);
            }
        });
    }
    function testDeleteCollection()
    {
        var data = new FormData();
        data.append('collectionId', document.getElementById("collectionId").value);

        jQuery.ajax({
            //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
            //url: 'http://localhost:8080/worker/uploadimatmodel',
            url: '/deletecollection',
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
            success: function(data)
            {
                //Do something with this
                alert("Success!  String that came back: " + data);
            }
        });
    }
    function testGetCollectionItems()
    {
        var data = Object.create(null);
        //User identifier for collections to pull
        data['collectionId'] = document.getElementById("collectionId").value;

        //Page number of the page to load
        data['pageNumber'] = document.getElementById("pageNumber").value;

        //Object identifiers from the last page load.  This is used to make sure that there is no duplicate
        //data for the next page load
        data['objectId-0'] = 0;
        data['objectId-1'] = 3;

        //Maximum number of items that are allowed to be returned by the server
        data['pageLimit'] = 5;

        //The number of extra items beyond the pageLimit that the back-end will load.  If there are overlapping items
        //specified by the collectionObject param that need to be eliminated from the returned list (as there may
        //be new items that have been populated), these extras will replace them
        data['duplicateBuffer'] = 3;

        jQuery.ajax({
            //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
            //url: 'http://localhost:8080/worker/uploadimatmodel',
            url: '/collectionitems',
            //url: 'http://localhost:4444/',
            data: jQuery.param(data),
            cache: false,
            contentType: false,
            processData: false,
            headers: {
                'Access-Control-Allow-Origin': '*',
                "X-CSRF-TOKEN": "${_csrf.token}"
            },
            type: 'GET',
            success: function(data)
            {
                //Do something with this
                alert("Success!  Array size that came back: " + data.collectionItems.length);
            }
        });
    }

  function testFollowCollection()
  {
      var data = new FormData();
      //User identifier for collections to pull
      data.append('collectionId', document.getElementById("collectionId").value);

      jQuery.ajax({
          //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
          //url: 'http://localhost:8080/worker/uploadimatmodel',
          url: '/followcollection',
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
          success: function(data)
          {
              //Do something with this
              alert("Successful follow!");
          }
      });
  }
  function testUnfollowCollection()
  {
      var data = new FormData();
      //User identifier for collections to pull
      data.append('collectionId', document.getElementById("collectionId").value);

      jQuery.ajax({
          //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
          //url: 'http://localhost:8080/worker/uploadimatmodel',
          url: '/unfollowcollection',
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
          success: function(data)
          {
              //Do something with this
              alert("Successful unfollow!");
          }
      });
  }

  function testFireTroveIncrement()
  {
      var data = new FormData();
      //User identifier for collections to pull
      data.append('itemId', document.getElementById("itemId").value);

      jQuery.ajax({
          //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
          //url: 'http://localhost:8080/worker/uploadimatmodel',
          url: '/testfiretroveincrement',
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
          success: function(data)
          {
              //Do something with this
              alert("Successful testfire!");
          }
      });
  }
  function testFireRemakeIncrement()
  {
      var data = new FormData();
      //User identifier for collections to pull
      data.append('itemId', document.getElementById("itemId").value);

      jQuery.ajax({
          //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
          //url: 'http://localhost:8080/worker/uploadimatmodel',
          url: '/testfireremakeincrement',
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
          success: function(data)
          {
              //Do something with this
              alert("Successful testfire!");
          }
      });
  }
    function setItemDefaultCollection()
    {
        var data = new FormData();
        //User identifier for collections to pull
        data.append('itemId', document.getElementById("itemId").value);
        data.append('collectionId', document.getElementById("collectionId").value);

        jQuery.ajax({
            //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
            //url: 'http://localhost:8080/worker/uploadimatmodel',
            url: '/setitemdefaultcollection',
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
            success: function(data)
            {
                //Do something with this
                alert("Successful default set!");
            }
        });
    }
    function clearItemDefaultCollection()
    {
        var data = new FormData();
        //User identifier for collections to pull
        data.append('itemId', document.getElementById("itemId").value);

        jQuery.ajax({
            //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
            //url: 'http://localhost:8080/worker/uploadimatmodel',
            url: '/clearitemdefaultcollection',
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
            success: function(data)
            {
                //Do something with this
                alert("Successful default clear!");
            }
        });
    }
    function testPagedFeedItems()
    {
        var data = Object.create(null);

        //Page number of the page to load
        data['pageNumber'] = document.getElementById("pageNumber").value;

        //Object identifiers from the last page load.  This is used to make sure that there is no duplicate
        //data for the next page load
        data['objectId-0'] = 0;
        data['objectId-1'] = 3;

        //Maximum number of items that are allowed to be returned by the server
        data['pageLimit'] = 10;

        //The number of extra items beyond the pageLimit that the back-end will load.  If there are overlapping items
        //specified by the collectionObject param that need to be eliminated from the returned list (as there may
        //be new items that have been populated), these extras will replace the
        data['duplicateBuffer'] = 3;

        jQuery.ajax({
            //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
            //url: 'http://localhost:8080/worker/uploadimatmodel',
            url: '/pagefeed',
            //url: 'http://localhost:4444/',
            data: jQuery.param(data),
            cache: false,
            contentType: false,
            processData: false,
            headers: {
                'Access-Control-Allow-Origin': '*',
                "X-CSRF-TOKEN": "${_csrf.token}"
            },
            type: 'GET',
            success: function(data)
            {
                //Do something with this
                alert("Success!  Array size that came back: " + data.itemList.length);
            }
        });
    }
    function testPagedCategoryItems()
    {
        var data = Object.create(null);

        //Category types that exist:  BRACELET, RING, FEATURED
        data['category'] = 'RING';

        //Page number of the page to load
        data['pageNumber'] = document.getElementById("pageNumber").value;

        //Object identifiers from the last page load.  This is used to make sure that there is no duplicate
        //data for the next page load
        data['objectId-0'] = 0;
        data['objectId-1'] = 3;

        //Maximum number of items that are allowed to be returned by the server
        data['pageLimit'] = 10;

        //The number of extra items beyond the pageLimit that the back-end will load.  If there are overlapping items
        //specified by the collectionObject param that need to be eliminated from the returned list (as there may
        //be new items that have been populated), these extras will replace the
        data['duplicateBuffer'] = 3;

        jQuery.ajax({
            //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
            //url: 'http://localhost:8080/worker/uploadimatmodel',
            url: '/pagecategory',
            //url: 'http://localhost:4444/',
            data: jQuery.param(data),
            cache: false,
            contentType: false,
            processData: false,
            headers: {
                'Access-Control-Allow-Origin': '*',
                "X-CSRF-TOKEN": "${_csrf.token}"
            },
            type: 'GET',
            success: function(data)
            {
                //Do something with this
                alert("Success!  Array size that came back: " + data.itemList.length);
            }
        });
    }
    function testPagedTroveItems()
    {
        var data = Object.create(null);

        //User id of the user whose trove we are interrogating
        data['userId'] = document.getElementById("userId").value;

        //Page number of the page to load
        data['pageNumber'] = document.getElementById("pageNumber").value;

        //Object identifiers from the last page load.  This is used to make sure that there is no duplicate
        //data for the next page load
        data['objectId-0'] = 0;
        data['objectId-1'] = 3;

        //Maximum number of items that are allowed to be returned by the server
        data['pageLimit'] = 10;

        //The number of extra items beyond the pageLimit that the back-end will load.  If there are overlapping items
        //specified by the collectionObject param that need to be eliminated from the returned list (as there may
        //be new items that have been populated), these extras will replace the
        data['duplicateBuffer'] = 3;

        jQuery.ajax({
            //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
            //url: 'http://localhost:8080/worker/uploadimatmodel',
            url: '/troveitems',
            //url: 'http://localhost:4444/',
            data: jQuery.param(data),
            cache: false,
            contentType: false,
            processData: false,
            headers: {
                'Access-Control-Allow-Origin': '*',
                "X-CSRF-TOKEN": "${_csrf.token}"
            },
            type: 'GET',
            success: function(data)
            {
                //Do something with this
                alert("Success!  Array size that came back: " + data.itemList.length);
            }
        });
    }
    function testGetUserFollowersPaged()
    {
        var data = Object.create(null);

        //User id of the user whose trove we are interrogating
        data['userId'] = document.getElementById("userId").value;

        //Page number of the page to load
        data['pageNumber'] = document.getElementById("pageNumber").value;

        //Object identifiers from the last page load.  This is used to make sure that there is no duplicate
        //data for the next page load
        data['objectId-0'] = 0;

        //Maximum number of items that are allowed to be returned by the server
        data['pageLimit'] = 10;

        //The number of extra items beyond the pageLimit that the back-end will load.  If there are overlapping items
        //specified by the objectId param that need to be eliminated from the returned list (as there may
        //be new items that have been populated), these extras will replace them
        data['duplicateBuffer'] = 3;

        jQuery.ajax({
            //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
            //url: 'http://localhost:8080/worker/uploadimatmodel',
            url: '/followers',
            //url: 'http://localhost:4444/',
            data: jQuery.param(data),
            cache: false,
            contentType: false,
            processData: false,
            headers: {
                'Access-Control-Allow-Origin': '*',
                "X-CSRF-TOKEN": "${_csrf.token}"
            },
            type: 'GET',
            success: function(data)
            {
                //Do something with this
                alert("Success!  Array size that came back: " + data.userList.length);
            }
        });
    }
        function testGetUserFollowedPaged()
        {
            var data = Object.create(null);

            //User id of the user whose trove we are interrogating
            data['userId'] = document.getElementById("userId").value;

            //Page number of the page to load
            data['pageNumber'] = document.getElementById("pageNumber").value;

            //Object identifiers from the last page load.  This is used to make sure that there is no duplicate
            //data for the next page load
            data['objectId-0'] = 0;

            //Maximum number of items that are allowed to be returned by the server
            data['pageLimit'] = 10;

            //The number of extra items beyond the pageLimit that the back-end will load.  If there are overlapping items
            //specified by the objectId param that need to be eliminated from the returned list (as there may
            //be new items that have been populated), these extras will replace them
            data['duplicateBuffer'] = 3;

            jQuery.ajax({
                //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
                //url: 'http://localhost:8080/worker/uploadimatmodel',
                url: '/followed',
                //url: 'http://localhost:4444/',
                data: jQuery.param(data),
                cache: false,
                contentType: false,
                processData: false,
                headers: {
                    'Access-Control-Allow-Origin': '*',
                    "X-CSRF-TOKEN": "${_csrf.token}"
                },
                type: 'GET',
                success: function(data)
                {
                    //Do something with this
                    alert("Success!  Array size that came back: " + data.userList.length);
                }
            });
        }

    function testGetCollectionFollowers()
    {
        var data = Object.create(null);

        //User id of the user whose trove we are interrogating
        data['collectionId'] = document.getElementById("collectionId").value;

        //Page number of the page to load
        data['pageNumber'] = document.getElementById("pageNumber").value;

        //Object identifiers from the last page load.  This is used to make sure that there is no duplicate
        //data for the next page load
        data['objectId-0'] = 0;

        //Maximum number of items that are allowed to be returned by the server
        data['pageLimit'] = 10;

        //The number of extra items beyond the pageLimit that the back-end will load.  If there are overlapping items
        //specified by the objectId param that need to be eliminated from the returned list (as there may
        //be new items that have been populated), these extras will replace them
        data['duplicateBuffer'] = 3;

        jQuery.ajax({
            //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
            //url: 'http://localhost:8080/worker/uploadimatmodel',
            url: '/collectionfollowers',
            //url: 'http://localhost:4444/',
            data: jQuery.param(data),
            cache: false,
            contentType: false,
            processData: false,
            headers: {
                'Access-Control-Allow-Origin': '*',
                "X-CSRF-TOKEN": "${_csrf.token}"
            },
            type: 'GET',
            success: function(data)
            {
                //Do something with this
                alert("Success!  Array size that came back: " + data.userList.length);
            }
        });
    }

  function testGetOpenOrders()
  {
    var data = Object.create(null);

    //User id of the user whose trove we are interrogating
    data['userId'] = document.getElementById("userId").value;

    //Status of the orders that should be retrieved, complete or open
    data['orderStatus'] = "open";

    //Page number of the page to load
    data['pageNumber'] = document.getElementById("pageNumber").value;

    //Maximum number of items that are allowed to be returned by the server
    data['pageLimit'] = 10;

    //The number of extra items beyond the pageLimit that the back-end will load.  If there are overlapping items
    //specified by the objectId param that need to be eliminated from the returned list (as there may
    //be new items that have been populated), these extras will replace them
    data['duplicateBuffer'] = 3;

    jQuery.ajax({
      //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
      //url: 'http://localhost:8080/worker/uploadimatmodel',
      url: '/orders',
      //url: 'http://localhost:4444/',
      data: jQuery.param(data),
      cache: false,
      contentType: false,
      processData: false,
      headers: {
        'Access-Control-Allow-Origin': '*',
        "X-CSRF-TOKEN": "${_csrf.token}"
      },
      type: 'GET',
      success: function(data)
      {
        //Do something with this
        alert("Success!  Array size that came back: " + data.orders.length);
      }
    });
  }

  function removeFromTrove()
  {
      var data = new FormData();
      //User identifier for collections to pull
      data.append('itemId', document.getElementById("itemId").value);

      jQuery.ajax({
          //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
          //url: 'http://localhost:8080/worker/uploadimatmodel',
          url: '/removefromtrove',
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
          success: function(data)
          {
              //Do something with this
              alert("Successful removal!");
          }
      });
  }

  function cancelLineItem()
  {
    var data = new FormData();
    //User identifier for collections to pull
    data.append('orderItemId', document.getElementById("orderItemId").value);

    jQuery.ajax({
      //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
      //url: 'http://localhost:8080/worker/uploadimatmodel',
      url: '/cancelorderlineitem',
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
      success: function(data)
      {
        //Do something with this
        alert("Successful cancellation!");
      }
    });
  }

  function completeOrderLineItem()
  {
    var data = new FormData();
    //User identifier for collections to pull
    data.append('orderItemId', document.getElementById("orderItemId").value);

    jQuery.ajax({
      //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
      //url: 'http://localhost:8080/worker/uploadimatmodel',
      url: '/completeorderlineitem',
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
      success: function(data)
      {
        //Do something with this
        alert("Successful cancellation!");
      }
    });
  }

  function updatePrototypeOrder()
  {
    var data = new FormData();

    //exportHash from the FORGE based customizer
    data.append('exportHash', customizerExportHash);

    //Cart item identifier that should be updated
    data.append('cartItemId', cartItemId);

    //The type of the next order, either PROTOTYPE or REAL
    data.append('nextOrderType', "PROTOTYPE");

    jQuery.ajax({
      //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
      //url: 'http://localhost:8080/worker/uploadimatmodel',
      url: '/updateprototypeorder',
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
      success: function(data)
      {
        //Do something with this
        alert("Successful cancellation!");
      }
    });
  }

  function addGiftCard()
  {
    var data = new FormData();

    //Required values
    data.append('digital', $('#digital option:selected').text());
    data.append('amount', $('#amount').val());

    //Optional values
    data.append('date', $('#date').val());
    data.append('toName', $('#toName').val());
    data.append('fromName', $('#fromName').val());
    data.append('toEmail', $('#toEmail').val());
    data.append('fromEmail', $('#fromEmail').val());

    jQuery.ajax({
      //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
      //url: 'http://localhost:8080/worker/uploadimatmodel',
      url: '/addgiftcard',
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
      success: function(data)
      {
        //Do something with this
        alert("Successful addition!");
      }
    });
  }

  function redeemGiftCard()
  {
    var data = new FormData();

    data.append('giftCardString', $('#giftcardstring').val());

    jQuery.ajax({
      //url: 'http://project-troveup-qa.appspot.com/savenewmodel',
      //url: 'http://localhost:8080/worker/uploadimatmodel',
      url: '/redeemgiftcard',
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
      success: function(data)
      {
        //Do something with this
        if (data.success) {
          alert("Successful redemption!  Card Amount: " + data.giftCardAmount + ". Total in user's account: " + data.storeCreditBalance);
        }
        else
          alert("Failure!  Error message: " + data.redemptionError);
      }
    });
  }

  function pushPrototypeId(id)
  {
    prototypeIds.push(id);
  }


</script>
</html>
