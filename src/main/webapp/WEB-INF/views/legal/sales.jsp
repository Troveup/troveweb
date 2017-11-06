<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Trove: Legal - ${pageTitle}</title>
  <c:import url="../fragments/headers/commonHead.jsp"/>
  <c:import url="../fragments/headers/legalHead.jsp"/>
  <c:import url="../fragments/analytics/all.jsp"/>

  <style>

    .special-container p {
     margin-bottom: 5px;
    }

  </style>
</head>

<body>
  <c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
  <c:import url="../fragments/nav/topNavBar.jsp"/>
    <div class="container cards_product_wrapper2">
      <div class="row">
        <c:import url="../fragments/nav/legalNav.jsp"/>

        <div class="col-sm-9">
          <div class="cardy privacy"> 
            <h3>Sales Policy</h3>
            <p>Last Updated: September 30th, 2015</p>
            <br></br>
            <p>Thanks for being a part of Trove’s online community of individuals who collect, design, and 3-D print jewelry customized to their lives. As with any shopping experience, there are terms and conditions that apply to your purchases from Trove. By placing an order or making a purchase from Trove, you agree to the terms and conditions of this Sales Policy, along with Trove’s Terms of Service and Privacy Policy.</p>

            <b>Product Display</b>
            <p>Trove attempts to display images of jewelry and jewelry designs as accurately as possible but cannot guarantee that the product you see on troveup.com will match exactly the product delivered to you. Due to differences in production (such as the composition of raw materials and print orientation), there may be minor differences between products printed from the same jewelry design. In addition, the color display may vary according to the computer monitor you use to view the website.</p>

            <b>Prototyping</b>
            <p>Trove offers complimentary prototyping services to each user at check out. Users who opt into this feature will receive a plastic version of the jewelry piece approximately 7 days after placing the order. Changes to your design are limited solely to the size of the item. Your jewelry piece will begin manufacture only after you either confirm or change your order. The user may only receive 1 prototype per item before the printing begins.</p>

            <b>U.S. Sales Only</b>
            <p>At this time, products may only be purchased from Trove if you have a billing address in the U.S. or its territories. Products will only be shipped to shipping addresses in the U.S. and its territories.</p>

            <b>Pricing</b>
            <p>Price and availability are subject to change at any time without notice. Trove reserves the right at any time to correct any pricing errors that inadvertently occur, to change or revoke any limited-time offer, and to correct any errors, inaccuracies, or omissions on the troveup.com website, including after your order has been submitted or confirmed or your credit card charged.</p>

            <b>Payment</b>
            <p>By submitting an order through the troveup.com website, you agree to pay in advance the applicable, one-time, non-refundable amount, plus any applicable taxes and other fees, for the purchased items. All fees and applicable taxes, if any, are non-transferable and payable in U.S. dollars. In accordance with state and local law, all purchases are taxed with the sales tax or seller’s use tax rate for your shipping address.</p>
            <p>Trove accepts the following credit cards and debit cards associated with billing addresses within the 50 United States and United States territories: American Express, Discover, MasterCard, and Visa. When you place an order, you will be required to provide customary billing information, such as name, billing address, and credit card number, and you agree to provide accurate, current, and complete information. Upon receipt of your order, Trove will verify authorization for the purchase amount with your credit card company. To prevent any unnecessary delays to the processing of your order, please ensure that the billing address on your order matches the information in your credit card account. Trove will charge your credit card at the time of purchase. The payment terms and any fees payable by you to your credit card company are based on the agreement between you and your credit card company. Trove is not a party to any agreement between you and your credit card company. You acknowledge and agree that you (and not Trove) are responsible for complying with the terms and conditions governing your relationship with such third party payment service provider.</p>

            <b>Email Notifications</b>
            <p>After receipt of your order, Trove will provide an email notice that confirms Trove’s receipt of the order but does not indicate Trove’s acceptance of your order. Trove reserves the right at any time and for any reason after receipt of your order to decline or cancel your order and, subject to availability of materials, to limit the quantity of your order. Trove sells and ships products only to individual customers and reserves the right to refuse, limit, or cancel orders of products intended for resale by dealers or other resellers. Upon shipment of your product, you will be able to find the UPS tracking number in the Orders tab on the Trove website. You can use this tracking number on the UPS website to see the shipping progress of your order.</p>

            <b>Cancellation</b>
            <p>You may cancel your order before manufacture of the product has begun. You can track the progress of your item as it proceeds from order acceptance to manufacture and shipment by checking the Order page on the troveup.com website. <b> After printing of your product has begun, you have no right to cancel your order or receive a refund.</b></p>
            <p>If Trove cancels all or any part of your order after your credit card has been charged, Trove will refund the billed amount as a credit to your credit card account. Your credit card company will determine when the amount is credited to your account.</p>

            <b>No Returns or Refunds</b>
            <p>Returns are only permitted for defective products. If you feel that your product is defective or was misprinted, please e-mail Trove with pictures of the misprinted item at hello@troveup.com within 7 days of receipt of the product to request a reprint of your order. Other than the return of defective or misprinted orders, products will not be accepted for return or refund. If you are concerned about the fit of your jewelry piece, please request a prototype of your item as explained above.</p>
            <br></br>

            <u>SHIPPING AND DELIVERY POLICY</u>
            <p>By placing an order or making a purchase from Trove, you agree to the terms and conditions of this Shipping and Delivery Policy, along with Trove’s Terms of Service and Privacy Policy.</p>
            <b>Shipping Cost and Carrier</b>
            <p>The cost of shipping is free for all domestic orders. Trove uses United States Postal Service (USPS) to ship all orders.</p>

            <b>No Delivery to a P.O. Box</b>
            <p>USPS will only accept shipments to a street address, which means that Trove cannot deliver to a P.O. Box.</p>

            <b>Tracking Your Package</b>
            <p>Upon shipment of your order, Trove will send you an email notice with the UPS tracking number and expected delivery date. You can click on the tracking number to track your package on the USPS website. In addition, you can track the progress of your order by logging onto the Trove website and checking the Orders page in your profile.</p>

            <b>Delivery Time</b>
            <p>Except for orders that include delivery of a prototype, the estimated shipping time for all orders is approximately 2.5 weeks from the date that you place your order to the date that it arrives on your doorstep. This 2.5-week period includes time to print your jewelry product and time to ship the finished product to you. The time required to print your product will depend on the complexity of your design, the material, and the finish of your item. Depending on your location, the transit time for shipment will take between 1-5 days.</p>

            <p>Delivery times for your purchase will be slightly longer if you request a prototype because manufacture of the actual item will not begin until you approve the prototype. The standard delivery time for a prototype is approximately 1 week from the date that you request the prototype.</p>

          </div>
          <br></br>
          <br></br>
          <br></br>
          <br></br>
        </div>
      </div>
    </div>
    <script>
      function bodyPadding(){
        var navHeight = $('.navbar').height();
        var bannerHeight = $('.special-container').outerHeight();
        var totalHeight = navHeight + bannerHeight;
        $('body').css("padding-top", totalHeight + "px");
      };

      $(document).ready(function() {
        bodyPadding();
        window.addEventListener('resize', bodyPadding);
      });
    </script>
  </body>
</html>