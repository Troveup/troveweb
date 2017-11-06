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

  .profile-img{
    max-width:100%;
    position:relative;
    margin:0 auto 10;
  }

  .profile-img.small{
    -webkit-transition: all 0.30s ease-in-out;
    -moz-transition: all 0.30s ease-in-out;
    -ms-transition: all 0.30s ease-in-out;
    -o-transition: all 0.30s ease-in-out;
  }

  .profile-img.small:hover{
    opacity:.8;
  }

  .profile.title{
    text-align:center;
  }

  .question{
    font-style:italic;
    margin-bottom:10px;
    position: relative;
    display: block;
  }

  .profile.text{
    margin:15px;
  }

    .text-explanation{
    margin:40px 5% 20px;
    padding: 30px 2% 30px;
    text-align:center;
  }

  .interview-text{
    margin-bottom:40px;
  }

  .mobileStuff{
    display:none;
  }


  </style>
</head>

<body>
  <c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
  <c:import url="../fragments/nav/topNavBar.jsp"/>
    <div class="container cards_product_wrapper2">

      <div class="banner">
        <div class="row bannerheight">
          <img class="megladon" src="https://storage.googleapis.com/troveup-imagestore/assets/img/rings-banner-sm.png">
        </div>
      </div>

      <div class="row">
        <div class="col-xs-12">
         <div class="text-explanation">
          <span>We asked some of our most fashionable friends to tell us about their favorite Trove jewelry and how it fits into their style. Here's what they had to say.</span>
        </div>
       </div>
      </div>


      <!--Four small profile images-->
      <div class="row desktopStuff" style="text-align:center;margin:25 auto 100;">
        <div class="col-xs-6 col-md-3">
          <div>   
               <a href="https://www.troveup.com/public/customize/webgl/678"><img src="https://console.developers.google.com/m/cloudstorage/b/troveup-imagestore/o/assets/img/menswear-dog.jpg" class="img-responsive profile-img small"></a>
               <p><a href="https://www.troveup.com/public/customize/webgl/678" style="font-weight:400;">See Gabby's Picks ></a></p>
          </div>
        </div>
        <div class="col-xs-6 col-md-3">
          <div>   
               <a href="https://www.troveup.com/public/customize/webgl/679"><img src="https://console.developers.google.com/m/cloudstorage/b/troveup-imagestore/o/assets/img/menswear-dog.jpg" class="img-responsive profile-img small"></a>
               <p><a href="https://www.troveup.com/public/customize/webgl/679" style="font-weight:400;">See XXXXXXXXX's Picks ></a></p>
          </div>
        </div>
        <div class="col-xs-6 col-md-3">
          <div>   
               <a href="https://www.troveup.com/public/customize/webgl/681"><img src="https://console.developers.google.com/m/cloudstorage/b/troveup-imagestore/o/assets/img/menswear-dog.jpg" class="img-responsive profile-img small"></a>
               <p><a href="https://www.troveup.com/public/customize/webgl/681" style="font-weight:400;">See XXXXXXXXX's Picks ></a></p>
          </div>
        </div>
        <div class="col-xs-6 col-md-3">
          <div>   
               <a href="https://www.troveup.com/public/customize/webgl/681"><img src="https://console.developers.google.com/m/cloudstorage/b/troveup-imagestore/o/assets/img/menswear-dog.jpg" class="img-responsive profile-img small"></a>
               <p><a href="https://www.troveup.com/public/customize/webgl/681" style="font-weight:400;">See XXXXXXXXX's Picks ></a></p>
          </div>
        </div>
      </div>
      <!--End Four small profile images-->

      <!--End First Profile-->
      <div class="row">
        <div class="col-xs-12 col-md-6">
          <div class="profile-img large">
            <img src="https://console.developers.google.com/m/cloudstorage/b/troveup-imagestore/o/assets/img/menswear-dog.jpg" class="img-responsive profile-img">
          </div>
          <div class="row">
            <div class="col-xs-4 col-md-4" style="padding-right:5px;">
              <img src="https://console.developers.google.com/m/cloudstorage/b/troveup-imagestore/o/assets/img/menswear-dog.jpg" class="img-responsive profile-img">
            </div>
            <div class="col-xs-4 col-md-4" style="padding-right:10px;padding-left:10px;">
              <img src="https://console.developers.google.com/m/cloudstorage/b/troveup-imagestore/o/assets/img/menswear-dog.jpg" class="img-responsive profile-img">
            </div>
            <div class="col-xs-4 col-md-4" style="padding-left:5px;">
              <img src="https://console.developers.google.com/m/cloudstorage/b/troveup-imagestore/o/assets/img/menswear-dog.jpg" class="img-responsive profile-img">
            </div>
          </div>
        </div>
        <div class="col-xs-12 col-md-6 interview-text">
          <div class="profile title">
            <h6>Style Description</h6>
            <h3 style="font-family:'Vollkorn','Arial',serif;">Gabby Leverette</h3><br>
          </div>
          <div class="profile text">
            <p>
              <span class="question">Could you briefly describe your style? Classic, minimalist, bohemian, or something else?</span>
              <span>I would say that I am a minimalist with a heavy lean towards classic staple pieces. I love a good set of dark jeans with a white button up and heels, perfect for work or drinks afterwords. </span>
              <br><br>
              <span class="question">As far as jewelry color goes, do you wear more gold, silver, or rose gold?</span>
              <span>I think it's a mix of all types of jewelry, the most important thing to me is the story and who gave me the piece. I wear a gold necklace that my boyfriend gave me and my mother's first wedding band (which my father had to replace for her). </span>
              <br><br>
              <span class="question">Do you find yourself wearing more necklaces, rings, or bracelets?</span>
              <span>I am more of a ring person then anything else. I ALWAYS have to have my rings but i tend to wear the same necklace, earrings, and rings daily. </span>
              <br><br>
              <span class="question">What’s your ideal date night?</span>
              <span>Anything planned by my man because honestly, isn't the effort what we're really looking for ladies? </span>
              <br><br>
              <span class="question">Do you prefer single statement pieces or stacking multiple pieces?</span>
              <span>I prefer small delicate pieces like a single pearl necklace or a thin gold ring with meaning. I tend to go for stacking rather then bold statement pieces for looks.</span>
              <br><br>
              <span class="question">What’s your favorite book and/or movie?</span>
              <span>Embarrassingly enough my favorite movie is Morning Glory with Rachel McAdams and Harrison Ford because COME ONE how could it not be?</span>
              <br>
            </p>
          </div>
        </div>
      </div>
      <!--End First Profile-->




    </div>
  </body>
</html>