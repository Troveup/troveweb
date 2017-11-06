<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Trove: ${pageTitle}</title>
  <c:import url="../fragments/headers/commonHead.jsp"/>
  <c:import url="../fragments/headers/aboutHead.jsp"/>
  <c:import url="../fragments/analytics/all.jsp"/>

  <style>
    .sizing-chart-cell{
      padding: 5px;
    }

    .special-container p {
     margin-bottom: 5px;
    }

  </style>

  <style>

    

  </style>
</head>

<body>
  <c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
  <c:import url="../fragments/nav/topNavBar.jsp"/>
    <div class="container cards_product_wrapper2">
      <div class="row">
        <c:import url="../fragments/nav/aboutNav.jsp"/>
        <div class="col-sm-9">
          <div class="cardy about"> 
            <h3>FAQ</h3>
            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
              <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingOne">
                  <h4 class="panel-title">
                    <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-controls="collapseOne" aria-expanded="true" class="collapsed">
                      What is Trove?
                    </a>
                  </h4>
                </div>
                <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true">
                  <div class="panel-body">
                    Trove is a place where anyone can 3D print jewelry - no printer or experience required. You can take any design on our site use our in-browser customizer and personalize it to your style. Collect these designs in your Trove and share your favorites with friends and followers. Once you’ve perfected your design, we will 3D print your jewelry to order in your choice of materials ranging from 18k gold to sterling silver.
                  </div>
                </div>
              </div>

              <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingFour">
                  <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseFour" aria-expanded="false" aria-controls="collapseFour">
                      How do I determine my ring and bracelet size?
                    </a>
                  </h4>
                </div>
                <div id="collapseFour" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingFour" aria-expanded="false">
                  <div class="panel-body">
                    You can measure your ring size using <a href="https://storage.googleapis.com/troveup-imagestore/assets/ring-sizer-final.pdf" target="_blank" alt="Jewelry Sizing Chart" style="color:#f26868;">this chart.</a><br><br>
                    Our bracelet sizes are sized according to their inside diameter at the widest point. Use a ruler or tape measure to see how wide your wrist is and find the closest larger size.<br><br>
                    <table>
                      <tr style="border-bottom:1px solid #b2b8b8">
                        <td class="sizing-chart-cell">Bracelet Size</td>
                        <td class="sizing-chart-cell">Inner Diameter (mm)</td>
                      </tr>
                      <tr>
                        <td class="sizing-chart-cell">XS</td>
                        <td class="sizing-chart-cell">55</td>
                      </tr>
                      <tr>
                        <td class="sizing-chart-cell">S</td>
                        <td class="sizing-chart-cell">60</td>
                      </tr>
                      <tr>
                        <td class="sizing-chart-cell">M</td>
                        <td class="sizing-chart-cell">65</td>
                      </tr>
                      <tr>
                        <td class="sizing-chart-cell">L</td>
                        <td class="sizing-chart-cell">70</td>
                      </tr>
                      <tr>
                        <td class="sizing-chart-cell">XL</td>
                        <td class="sizing-chart-cell">75</td>
                      </tr>
                    </table>

                  </div>
                </div>
              </div>

              <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingThree">
                  <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
                      What materials can you print in?
                    </a>
                  </h4>
                </div>
                <div id="collapseThree" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingThree" aria-expanded="false">
                  <div class="panel-body">
                    Materials range from gold, sterling silver, gold-plated brass, and bronze.
                  </div>
                </div>
              </div>

              

              <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingFive">
                  <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseFive" aria-expanded="false" aria-controls="collapseFive">
                      When will I receive my custom item?
                    </a>
                  </h4>
                </div>
                <div id="collapseFive" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingFive" aria-expanded="false">
                  <div class="panel-body">
                    We always strive to ship items as quickly as possible. Given the customized nature of our products, you will receive your order in 2-3 weeks. You will receive an email confirmation once each of your orders are ready for shipment. Don’t worry, it is well worth the wait!
                  </div>
                </div>
              </div>

              <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingSix">
                  <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseSix" aria-expanded="false" aria-controls="collapseSix">
                      What is the cost of domestic shipping?
                    </a>
                  </h4>
                </div>
                <div id="collapseSix" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingSix" aria-expanded="false">
                  <div class="panel-body">
                    We charge a flat $4.99 for shipping on all of our products.
                  </div>
                </div>
              </div>

              <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingSeven">
                  <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseSeven" aria-expanded="false" aria-controls="collapseSeven">
                      What is your return policy?
                    </a>
                  </h4>
                </div>
                <div id="collapseSeven" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingSeven" aria-expanded="false">
                  <div class="panel-body">
                    Since all of our orders are custom-made, we only allow cancellations to be requested up to 12 hours after you submit your purchase. Cancellations can be requested via email to <a href="mailto:hello@troveup.com?Subject=Order%20Cancellation%20Request">hello@troveup.com</a>. We appreciate your understanding and want to ensure you are satisfied with your order. Please reach out to us if you have any specific questions, and we would be more than happy to help.
                  </div>
                </div>
              </div>

              <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingEight">
                  <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseEight" aria-expanded="false" aria-controls="collapseEight">
                      What types of payment do you accept?
                    </a>
                  </h4>
                </div>
                <div id="collapseEight" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingEight" aria-expanded="false">
                  <div class="panel-body">
                    We accept Paypal and most credit and debit cards, including Visa, MasterCard, American Express, and Discover.
                  </div>
                </div>
              </div>

              <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingNine">
                  <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseNine" aria-expanded="false" aria-controls="collapseNine">
                      How soon will I be charged for my transaction? 
                    </a>
                  </h4>
                </div>
                <div id="collapseNine" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingNine" aria-expanded="false">
                  <div class="panel-body">
                    When ordering without a Try-On model, you will be charged once your final item ships. When ordering with a Try-On model, you will be charged upon confirmation of your final design.
                  </div>
                </div>
              </div>

              <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingTen">
                  <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseTen" aria-expanded="false" aria-controls="collapseTen">
                      Where does Trove ship to?
                    </a>
                  </h4>
                </div>
                <div id="collapseTen" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTen" aria-expanded="false">
                  <div class="panel-body">
                    Currently, Trove ships anywhere in the United States - including Hawaii and Alaska! We do plan on expanding internationally in the near future. So, please, keep checking back!
                  </div>
                </div>
              </div>

              <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingTenhalf">
                  <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseTenhalf" aria-expanded="false" aria-controls="collapseEleven">
                      How do I track my order?
                    </a>
                  </h4>
                </div>
                <div id="collapseTenhalf" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTenhalf" aria-expanded="false">
                  <div class="panel-body">
                    You can view your order status under the “Orders” tab in your Trove. You will also receive email notifications with tracking information once your packages have been shipped. If you have any additional questions about your order, don’t hesitate to email us at <a href="mailto:shipping@troveup.com?Subject=Shipping%20Help">shipping@troveup.com</a>.
                  </div>
                </div>
              </div>

              <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingEleven">
                  <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseEleven" aria-expanded="false" aria-controls="collapseEleven">
                      What is a Try-on Model?
                    </a>
                  </h4>
                </div>
                <div id="collapseEleven" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingEleven" aria-expanded="false">
                  <div class="panel-body">
                    A Try-On Model is a representation of your custom jewelry piece, 3D-printed in white plastic. We believe that your jewelry should be exactly what you imagined and want to give you the opportunity to try it on before you make your final purchase.
                  </div>
                </div>
              </div>




              <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingTwelve">
                  <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseTwelve" aria-expanded="false" aria-controls="collapseTwelve">
                      How do I get a Try-On Model?
                    </a>
                  </h4>
                </div>
                <div id="collapseTwelve" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwelve" aria-expanded="false">
                  <div class="panel-body">
                   You have the option of adding a free Try-On model at checkout when you purchase an item. Once that purchase has been confirmed, your Try-On model will be shipped to you and will give you an opportunity to test out your design before the final product arrives. After you receive the model, you will be able to make any adjustments or changes to your final design before we create it and ship it to you. If you have any additional questions about the process, please email us at <a href="mailto:hello@troveup.com?Subject=Try-On%20Model%20Question">hello@troveup.com</a>.
                  </div>
                </div>
              </div>

              <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingFourteen">
                  <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseFourteen" aria-expanded="false" aria-controls="collapseFourteen">
                      How long will it take for my Preview Model to arrive?
                    </a>
                  </h4>
                </div>
                <div id="collapseFourteen" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingFourteen" aria-expanded="false">
                  <div class="panel-body">
                    You will receive your custom Preview Model in approximately one week.
                  </div>
                </div>
              </div>

              <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingSixteen">
                  <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseSixteen" aria-expanded="false" aria-controls="collapseSixteen">
                      Does the customizer work on mobile?
                    </a>
                  </h4>
                </div>
                <div id="collapseSixteen" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingSixteen" aria-expanded="false">
                  <div class="panel-body">
                    Yes, our customizer is optimized for iPhone and Android mobile, as well as for iPad and Windows tablet.
                  </div>
                </div>
              </div>

              <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingSixteenn">
                  <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseSixteenn" aria-expanded="false" aria-controls="collapseSixteen">
                     Does Trove offer personal shopper and design services?
                    </a>
                  </h4>
                </div>
                <div id="collapseSixteenn" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingSixteenn" aria-expanded="false">
                  <div class="panel-body">
                    Yes, Trove will provide you with personal assistance in customizing jewelry that is already on Trove or finding the right jewelry piece to give as a gift. Please email us at <a href="mailto:hello@troveup.com?Subject=Design%20Help!">hello@troveup.com</a> if you would like to set an appointment time or have questions that you would like answered. 
                  </div>
                </div>
              </div>

               <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingTwo">
                  <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                      What is 3D printing?
                    </a>
                  </h4>
                </div>
                <div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo" aria-expanded="false">
                  <div class="panel-body">
                    Magic. At least, that’s how we feel when we see things being 3D-printed. In its simplest form, 3D printing builds up layer after layer of material to build a physical object from the ground up. 3D printing allows you to take digital files and turn them into physical products with the exact specifications that you designed with.
                  </div>
                </div>
              </div>

              <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingSeventeen">
                  <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseSeventeen" aria-expanded="false" aria-controls="collapseSeventeen">
                      Where does the 3D printing take place?
                    </a>
                  </h4>
                </div>
                <div id="collapseSeventeen" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingSeventeen" aria-expanded="false">
                  <div class="panel-body">
                    We create all of the jewelry and ship it directly to you so that you do not need any equipment to create your own jewelry.
                  </div>
                </div>
              </div>
              <br><br>
             <p>Have additional questions? Please email us at <a href="mailto:hello@troveup.com?Subject=Hello%20there!">hello@troveup.com</a> or reach out to us on <a href="https://twitter.com/troveup">Twitter</a> </p>
            </div>
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