<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Trove: Free 3D-Printed Jewelry</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=0">
    <c:import url="../fragments/headers/commonHead.jsp"/>
    <c:import url="../fragments/analytics/all.jsp"/>
    <style>
      body { 
        margin: 0; 
        /*background: url("https://storage.googleapis.com/troveup-imagestore/assets/ftue-banner-short.jpg");*/
        background: #FFF;
        background-size: auto Calc(100% - 350px);
        background-position-x: center;
        background-position-y: 10px;
        background-repeat: no-repeat;
        text-align: center;
        font-family: "Raleway", Helvetica, Arial, sans-serif;
        letter-spacing: 1px;
        font-weight: 300;
        width: 100%;
        max-height: 100%;
        overflow: auto;
        max-width: 100%;
        overflow-x: hidden;
      }
      button, a, * {
        outline: 0;
      }

      .vidstuff {
        opacity: 0.0;
        width: 75%;
        -webkit-transition: 1s opacity; 
                transition: 1s opacity;
        padding-bottom: 100px;
      }

      video { 
        position: fixed;
        top: 50%;
        left: 50%;
        min-width: 100%;
        min-height: 100%;
        width: auto;
        height: auto;
        z-index: -100;
           -moz-transform: translateX(-50%) translateY(-50%);
            -ms-transform: translateX(-50%) translateY(-50%);
        -webkit-transform: translateX(-50%) translateY(-50%);
                transform: translateX(-50%) translateY(-50%);
        background: url('https://storage.googleapis.com/troveup-imagestore/assets/img/trove-vid.jpg') no-repeat; 
        background-size: cover;  
        -webkit-transition: 1s opacity; 
                transition: 1s opacity; 
        opacity: 1.0;
      }
      
      .stopfade { 
        opacity: .4; 
      }

      .btn.btn-pause { 
        display: block;
        width: 60px;
        height: 60px;
        padding: .4rem;
        border: none; 
        font-size: 1.3rem;
        background: rgba(255,255,255,0.0);
        color: rgba(255,255,255,0.0);
        border-radius: 3px; 
        cursor: pointer;
        -webkit-transition: .3s all;
                transition: .3s all;
      }

      .btn.btn-bottomnext { 
        display: block;
        width: 340px;
        max-width: 100%;
        padding: .6em;
        line-height: 1.4em;
        margin: 20px auto 0px auto;
        border: none;
        background: rgba(221, 36, 53,1.0);
        text-transform: uppercase;
        color: rgba(255,255,255,1.0);
        border-radius: 0px;
        font-size: 18px;
        letter-spacing: .08em;
        font-weight: 500;
        -webkit-font-smoothing: antialiased;
        outline: 0;
        cursor: pointer;
        -webkit-transition: .3s all;
        transition: .3s all;
        z-index: 99;
      }

      .btn.btn-signin {
        outline: 0;
        position: fixed;
        right: 16px;
        top: 16px;
        background: transparent;
        color: #BBC2CD;
        border: 1px solid #BBC2CD;
        border-radius: 0px;
        padding: 10px 40px;
        font-size: 16px;
        font-family: "Raleway", Helvetica, Arial, sans-serif;
        letter-spacing: 0.1em;
        -webkit-transition: .3s all;
        transition: .3s all;
        opacity: 0.6;
      }

      .btn.btn-bottomnextmod { 
        display: block;
        width: Calc(100% - 30px);
        text-transform: uppercase;
        max-width: 500px;
        padding: .6em;
        line-height: 1.4em;
        margin: 60px auto 0px auto;
        border: none;
        background: rgba(221, 36, 53,1.0);
        color: rgba(255,255,255,1.0);
        border-radius: 0px;
        font-size: 18px;
        letter-spacing: .08em;
        font-weight: 500;
        -webkit-font-smoothing: antialiased;
        outline: 0;
        cursor: pointer;
        -webkit-transition: .3s all;
        transition: .3s all;
      }

      .btn.btn-signin:hover {
        opacity: 0.9;
      }

      .btn.btn-bottomnextmod:hover {
        background: #F26868;
      }
      .btn.btn-bottomnext:hover {
        background: #F26868;
      }

      body.stopfader .btn.btn-pause {
        background: rgba(255,255,255,0.0);
        color: rgba(255,255,255,0.0);
      }
      body.stopfader-mobile .btn.btn-pause {
        background: rgba(0,0,0,0.5);
        color: rgba(255,255,255,1.0);
      }

      .no-mobile .btn.btn-pause {
        pointer-events: none;
        display: none;
      }

      body.stopfader.stopfader-mobile .btn.btn-pause {
        background: rgba(0,0,0,0.5);
        color: rgba(255,255,255,1.0);
      }

      

      body:hover .btn.btn-pause { 
        background: rgba(0,0,0,0.5);
        color: rgba(255,255,255,1.0);
      }
      body:hover .btn.btn-pause:hover  { 
        background: rgba(0,0,0,0.5);
        color: rgba(255,255,255,1.0);
      }
      .icon-button {
        font-size: 2.4em;
      }

      .nav-logo {
        width: 160px;
        padding-top: 0px;
        padding-bottom: 0px;
        margin-top: 10px;
        /*margin-bottom: -20px;
        margin-right: -25%;
        margin-left: 80px;*/
        margin-top: -5px;
        position: absolute;
        left: 50%;
        margin-left: -80px;
      }

      h2 {
        text-align: center;
        font-family: "Raleway", Helvetica, Arial, sans-serif;
        letter-spacing: 1px;
        font-weight: 300;
        width: 100%;
        font-size: 18px;
        text-transform: uppercase;
      }

      h3 {
        font-family: 'vollkorn';
        color: #100E0D;
        font-weight: 500;
        letter-spacing: .08em;
        font-size: 40px;
        padding: 110px 5% 10px 5%;
        pointer-events: none;
      }
      h5 {
        color: #383A3D;
        padding: 20px 10px 0px 10px;
        max-width: 320px;
        margin: auto;
        font-weight: 300;
        font-size: 20px;
        line-height: 1.5em;
      }

      h5::after {
        content: "";
        position: absolute;
        width: 70px;
        height: 3px;
        background: #F26868;
        left: 37%;
        margin-left: -35px;
        margin-top: -80px;
      }

      .nav-logo {
      /*  margin-right: -45%;
        margin-left: -80px;*/
      }

      @media screen and (max-width: 800px) { 
        .btn.btn-bottomnext {
          /*margin: 15px;
          width: Calc(100% - 30px);
          padding: 1em;
          position: fixed;
          bottom: 220px;
          max-width: none;*/
        }

        h3 {
          /*padding: 15px 15px 100px 15px;
          line-height: 1.2em;*/
        }

        h5 {
         /* position: fixed;
          bottom: 318px;
          left: 50%;
          margin-left: -146px;
          font-size: 18px;*/
        }

        .nav-logo {
       /*   margin-right: 0;
          margin-left: 0;*/
        }


        .vidstuff {
          width: 100%;
        }
        .lefty {
          display: none;
        }
        .righty {
          display: none;
        }

        .img-responsive {
          padding-bottom: 25px;
        }

        h5::after {
          left: 50%;
          margin-left: -35px;
        }

      }

      @media all and (max-device-width: 800px) {
        
        body { 
/*          background: url("") rgb(222, 222, 222) no-repeat center center fixed; 
          background-size: cover; */
        }

        #bgvid { 
          display: none;
        }

      }

      @media screen and (max-width: 500px) { 

        h3 {
          bottom: 374px;
          width: 100%;
          font-size: 32px;
          top: 20px;
        }
        .nav-logo {
          width: 115px;
          position: fixed;
          left: 20px;
          top: 20px;
          margin-right: 0;
          margin-left: 0;
        }
        .btn.btn-signin {
          /*top: 14px;*/
        }

        
      }

      @-webkit-keyframes spin {
        to {
          -webkit-transform: rotate(360deg);
          transform: rotate(360deg);
        }
      }
      @-moz-keyframes spin {
        to {
          -webkit-transform: rotate(360deg);
          transform: rotate(360deg);
        }
      }
      @-ms-keyframes spin {
        to {
          -webkit-transform: rotate(360deg);
          transform: rotate(360deg);
        }
      }
      @-o-keyframes spin {
        to {
          -webkit-transform: rotate(360deg);
          transform: rotate(360deg);
        }
      }
      @keyframes spin {
        to {
          -webkit-transform: rotate(360deg);
          transform: rotate(360deg);
        }
      }

      #livePhotoLeft {
        -webkit-transition: 1s opacity; 
                transition: 1s opacity; 
      }

      #livePhotoLeft.show {
        opacity: 0.0;
      }

      .spinstuff {
        opacity: 0.0;
        -webkit-transition: 1s opacity; 
                transition: 1s opacity; 
        pointer-events: none;
      }

      .modstuff {
        opacity: 0.0;
        -webkit-transition: 1s opacity; 
                transition: 1s opacity; 
      }

      .modspinstuff{ 
        opacity: 0.0;
        -webkit-transition: 1s opacity; 
                transition: 1s opacity; 
        pointer-events: none;
      }

      .vidstuff.show {
        opacity: 1.0;
      }

      .spinstuff.show {
        opacity: 1.0;
      }

      .modstuff.show {
        opacity: 1.0;
      }

      .modspinstuff.show {
        opacity: 1.0;
      }

      .bottomHolder {
        width: 100%;
        background: transparent;
        height: 340px;
        position: fixed;
        bottom: 0px;
        left: 0px;
        right: 0px;
        padding: 60px 30px;
        text-align: center;
        z-index: 9;
      }

      .bottomHolder.hidden {
        display: none;
      }

      h5 {
        color: #100E0D;
      }

      .modal-header {
        text-align: right;
        border-bottom: 1px solid #FFFFFF;
      }

      .closeModal {
        font-size: 2em;
        color: #4A4646;
      }
      .modal-l {
        overflow: hidden;
        width: 50%;
        height: 475px;
        background: #FFF;
        display: inline-block;
        border-radius: 6px;
        float: left;
        border-top-right-radius: 0px;
        border-bottom-right-radius: 0px;
      }
      .modal-r {
        width: 50%;
        display: inline-block;
        height: 475px;
      }

      #livePhotoLeft {
        width: auto;
        height: 100%;
      }

      @media (max-width: 767px) {
        .modal-l {
          height: auto;
          width: 100%;
          border-top-right-radius: 6px;
          border-bottom-left-radius: 0px;
        }
        .modal-r {
          width: 100%;
          display: inline-block;
          height: 475px;
        }
        .closeModal {
          position: absolute;
          right: 20px;
          top: 20px;
        }
        #livePhotoLeft {
          width: 100%;
          height: auto;
        }
      }

      .modal-footer {
        border-top: 1px solid #FFFFFF;
      }

      h4 {
        font-size: 17px;
        line-height: 1.2;
        margin-top: 0px;
        margin-bottom: 30px;
        font-family: 'vollkorn';
      }

      @media (min-width: 768px) {
        .modal-dialog {
          width: 725px;
        }
      }

      .carousel-control {
        width: 0%;
      }

      .carousel-inner .active.left  { left: -20%;             }
      .carousel-inner .active.right { left: 20%;              }
      .carousel-inner .next         { left: 20%;               }
      .carousel-inner .prev         { left: -20%;              }
      .carousel-control.left        { background-image: none; }
      .carousel-control.right       { background-image: none; }
      .carousel-inner .item         { background: white;      }

      .slick-slide img {
        outline: 0 !important;
        padding: 30px;
      }
      .slick-slider {
        max-width: 1200px;
        margin: auto;
      }

      #prevButton {
        position: absolute;
        left: 12px;
        top: 160px;
        outline: 0;
        border: 0px solid #DDD;
        background: transparent;
        font-size: 33px;
      }

      #nextButton {
        position: absolute;
        right: 12px;
        top: 160px;
        outline: 0;
        border: 0px solid #DDD;
        background: transparent;
        font-size: 33px;
      }

      

      #livePhoto {
        width: 200px;
        height: 200px;
      }

      .slick-slide {
        opacity: 0.8;
        -webkit-transition: 1s opacity; 
              transition: 1s opacity; 
      }

      .bottomHolder {
        -webkit-transition: all 0.3s ease-in-out; 
              transition: all 0.3s ease-in-out; 
      }

      .slick-slide:hover {
        opacity: 1.0;
      }

      h2 {
          margin-top: 0px;
          margin-bottom: 30px;
      }

 

      @media (max-width: 480px) {
        .bottomHolder {
          height: 180px;
          padding: 5px 30px;
        }
        .slick-slide img {
          padding: 30px;
        }
        .slick-slide {
          opacity: 1.0;
        }
        #prevButton {
          display: none;
        }
        #nextButton {
          display: none;
        }

        .btn.btn-bottomnextmod {
          margin: margin: 83px auto 0px auto;
          padding: 1em .6em;
        }

        .slick-slider {
          width: 100%;
        }

        .btn.btn-bottomnext {
          bottom: 180px;
        }

        h5 {
          bottom: 265px;
          margin-left: -146px;
          font-size: 18px;
        }

        h3 {
          bottom: 310px;
          width: 100%;
          font-size: 32px;
        }
      }

      @media (min-width: 480px) {
        .bottomHolder {
          height: 218px;
          padding: 30px 30px;
        }
        #prevButton {
          top: 95px;
        }
        #nextButton {
          top: 95px;
        }
      }

      @media (min-width: 768px) {
        #livePhotoLeft {
          margin-left: -50px;
        }
        .bottomHolder {
          padding: 30px 60px;
        }
      }

      @media (min-width: 930px) {
        .bottomHolder {
          height: 250px;
          padding: 40px 60px;
        }
        .modal-dialog {
          width: 900px;
        }
        #livePhotoLeft {
          margin-left: 0px;
        }

        #prevButton {
          top: 116px;
        }
        #nextButton {
          top: 116px;
        }
      }

      @media (min-width: 1200px) {
        .bottomHolder {
          height: 340px;
          padding: 60px;
        }
        .modal-dialog {
          width: 900px;
        }
        #livePhotoLeft {
          margin-left: 0px;
        }

        #prevButton {
          top: 160px;
        }
        #nextButton {
          top: 160px;
        }
      }

      h5 span {
        font-size: 80%;
        line-height: 4em;
      }

      @media (min-width: 1360px) {
        #prevButton {
          left: 60px;
        }
        #nextButton {
          right: 60px;
        }
      }

      
      /* iphone 5s specific */
      

      .lefty {
        opacity: 1.0;
        height: 420px;
        width: Calc(60%);
        pointer-events: none;
        position: absolute;
        left: 0px;
        z-index: -9;
        top: 75px;
        background: #FFF0E3;
        background-size: 420px auto;
        background-position: right;
        background-repeat: no-repeat;
      }

      .righty {
      	opacity: 1.0;
  	    height: 420px;
  	    width: Calc(40%);
  	    pointer-events: none;
  	    position: absolute;
  	    right: 0px;
  	    z-index: -9;
  	    top: 75px;
  	    background: url('https://storage.googleapis.com/troveup-imagestore/assets/img/split-bg-img.jpg') no-repeat #A9B2C1;
  	    background-size: 420px auto;
  	    background-position: left;
  	    background-repeat: no-repeat;
      }
      

      @media (max-width: 768px) {
        /*.nav-logo {
          margin-left: 0px;
          margin-right: 0px;
        }*/

        .vidstuff {
          width: 100%;
        }

        h5::after {
          left: 50%;
        }
        .closeModal {
          font-size: 3em;
          color: #FFFFFF;
        }

      }


     /* body.biggy .lefty {
        height: 550px;
      }

      body.biggy .righty {
        height: 550px;
        background: url('https://storage.googleapis.com/troveup-imagestore/assets/img/split-bg-img.jpg') no-repeat #A9B2C1;
        background-size: 550px auto;
      }

      body.biggy h3 {
        padding-top: 30px;
        background: url('https://storage.googleapis.com/troveup-imagestore/assets/img/split-bg-img.jpg') no-repeat #A9B2C1;
      }*/
      h6 {
        display: none;
      }

      @media (max-width: 480px) {
        h3 {
          padding-top: 25px;
          position: relative;
          height: 360px;
          background: url('https://storage.googleapis.com/troveup-imagestore/assets/img/mobile-split-ring.jpg') no-repeat transparent;
          background-size: 100%;
          background-position: 0px 122px;
          left: 0px;
        }
        h5 {
          display: none;
          position: relative;
          margin-top: 20px;
          margin-left: 0px;
          width: 100%;
          bottom: auto;
          max-width: 100%;
          left: 0px;
        }
        h6 {
          display: block;
        }
        .btn.btn-bottomnext {
          bottom: 0px;
          position: relative;
          margin-top: 50px;margin-top: 50px;
        }

        .btn.btn-signin {
          position: absolute;
          margin-top: 50px;
        }

        .nav-logo {
          width: 80px;
          position: absolute;
          left: 20px;
          top: 20px;
          margin-right: 0;
          margin-left: 0;
        }
      }

      .bottomItems.hidden {
        display: none;
      }


      @media screen and (device-aspect-ratio: 40/71) {
        h3 {
          bottom: 210px;
          width: 100%;
          font-size: 24px;
          z-index: 997;
          height: 270px;
          background-position: 0px 92px;
        }
        h4 {
          margin-top: -20px;
        }
        h5 {
          bottom: 160px;
          margin-left: 0px;
          left: 0px;
          width: 100%;
          font-size: 16px;
          z-index: 998;
        }
        h5 span {
          line-height: 3em;
        }
        .bottomHolder {
          height: 190px;
        }
        .modal-l {
          height: 296px;
        }
        .modal-r {
          height: 420px;
        }
        .btn.btn-bottomnext {
          bottom: 0px;
          z-index: 999;
          padding: 13px;
        }
        .btn.btn-bottomnextmod {
          margin: 30px auto 0px;
          font-size: 14px;
        }
            
      }

      .downarrow {
        margin-top: 15px;
      }

      .downarrow.hidden {
        display: none;
      }
      #scrollbutt {
        font-size: 20px;
        color: #666;
      }
      #scrollbutt:hover {
        font-size: 20px;
        color: #666;
        text-decoration: none;
      }

      #bottomnextbtnmob {
         display: none;
      }

      #mobhide {
         display: none;
      }

      @media (min-width: 767px) {
        .downarrow {
          display: none;
        }
        #bottomnextbtn {
           display: none;
        }

        #bottomnextbtnmob {
           display: block;
        }

        #mobhide {
         display: block;
        }

        #deskhide {
           display: none;
        }

        body.biggy .righty {
          opacity: 1.0;
          height: 550px;
          width: Calc(40%);
          pointer-events: none;
          position: absolute;
          right: 0px;
          z-index: -9;
          top: 75px;
          background: url('https://storage.googleapis.com/troveup-imagestore/assets/img/split-bg-img.jpg') no-repeat #A9B2C1;
          background-size: 550px auto;
          background-position: left;
          background-repeat: no-repeat;
        }

        body.biggy .lefty {
          opacity: 1.0;
          height: 550px;
          width: Calc(60%);
          pointer-events: none;
          position: absolute;
          left: 0px;
          z-index: -9;
          top: 75px;
          background: #FFF0E3;
          background-size: 420px auto;
          background-position: right;
          background-repeat: no-repeat;
        }

        body.biggy h3 {
          font-family: 'vollkorn';
          color: #100E0D;
          font-weight: 500;
          letter-spacing: .08em;
          font-size: 40px;
          padding: 170px 5% 10px 5%;
          pointer-events: none;
        }


      }

      .link-hover-effect {
        -webkit-transition: all 0.3s ease-in-out; 
              transition: all 0.3s ease-in-out; 
      }

      .link-hover-effect:hover {
        cursor: pointer;
        opacity: 0.8;
      }



    </style>

  </head>
  <body class="biggy">

  <div class="modal fade itemModal" id="itemModal" tabindex="-1" role="dialog" aria-labelledby="itemModalALabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content" id="itemModalContent">
        <div class="modal-l">
         <img id="livePhotoLeft" data-src="holder.js/180x180" class="" alt="200x200" src="data:image/svg+xml;charset=UTF-8,%3Csvg%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20width%3D%22200%22%20height%3D%22200%22%20viewBox%3D%220%200%20200%20200%22%20preserveAspectRatio%3D%22none%22%3E%3C!--%0ASource%20URL%3A%20holder.js%2F200x200%0ACreated%20with%20Holder.js%202.8.2.%0ALearn%20more%20at%20http%3A%2F%2Fholderjs.com%0A(c)%202012-2015%20Ivan%20Malopinsky%20-%20http%3A%2F%2Fimsky.co%0A--%3E%3Cdefs%3E%3Cstyle%20type%3D%22text%2Fcss%22%3E%3C!%5BCDATA%5B%23holder_152cc48d099%20text%20%7B%20fill%3Argba(255%2C255%2C255%2C.75)%3Bfont-weight%3Anormal%3Bfont-family%3AHelvetica%2C%20monospace%3Bfont-size%3A10pt%20%7D%20%5D%5D%3E%3C%2Fstyle%3E%3C%2Fdefs%3E%3Cg%20id%3D%22holder_152cc48d099%22%3E%3Crect%20width%3D%22200%22%20height%3D%22200%22%20fill%3D%22%23777%22%2F%3E%3Cg%3E%3Ctext%20x%3D%2274.4296875%22%20y%3D%22104.65%22%3E200x200%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fsvg%3E" data-holder-rendered="true">
        </div>
        <div class="modal-r">
          <div class="modspinstuff">
            <span class="spinner page-fade-spinner"></span>
          </div>
          <div class="modstuff show">
            <div class="modal-header">
              <a href="#" data-dismiss="modal" class="closeModal"><i class="ion-android-close"></i></a>
            </div>
            <div class="modal-body">
              <h4 class="modalHeadline" id="nana">Personalize this item and we'll send you a 3D-printed sample of it for free.</h4>
              <img id="livePhoto" data-src="holder.js/180x180" class="" alt="200x200" src="data:image/svg+xml;charset=UTF-8,%3Csvg%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20width%3D%22200%22%20height%3D%22200%22%20viewBox%3D%220%200%20200%20200%22%20preserveAspectRatio%3D%22none%22%3E%3C!--%0ASource%20URL%3A%20holder.js%2F200x200%0ACreated%20with%20Holder.js%202.8.2.%0ALearn%20more%20at%20http%3A%2F%2Fholderjs.com%0A(c)%202012-2015%20Ivan%20Malopinsky%20-%20http%3A%2F%2Fimsky.co%0A--%3E%3Cdefs%3E%3Cstyle%20type%3D%22text%2Fcss%22%3E%3C!%5BCDATA%5B%23holder_152cc48d099%20text%20%7B%20fill%3Argba(255%2C255%2C255%2C.75)%3Bfont-weight%3Anormal%3Bfont-family%3AHelvetica%2C%20monospace%3Bfont-size%3A10pt%20%7D%20%5D%5D%3E%3C%2Fstyle%3E%3C%2Fdefs%3E%3Cg%20id%3D%22holder_152cc48d099%22%3E%3Crect%20width%3D%22200%22%20height%3D%22200%22%20fill%3D%22%23777%22%2F%3E%3Cg%3E%3Ctext%20x%3D%2274.4296875%22%20y%3D%22104.65%22%3E200x200%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fsvg%3E" data-holder-rendered="true">
              <button id="bottomnextmodbtn" class="btn btn-bottomnextmod">Customize this Design</button>
            </div>
            <div class="modal-footer">
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="spinstuff"><span class="spinner page-fade-spinner"></span></div>
  <div class="vidstuff show">
    <div class="lefty"></div>
    <div class="righty"></div>
    <!-- <img alt="Trovelogo" class="nav-logo" src="/resources/img/troveLogo2.svg"> -->
    <img alt="Trovelogo" class="nav-logo" src="https://storage.googleapis.com/troveup-imagestore/assets/img/logo-text.png">
    <!--VIDEO STUFF--><!-- <img alt="Trovelogo" class="nav-logo" src="https://storage.googleapis.com/troveup-imagestore/assets/img/troveLogo-white.svg"> --><!-- <video class="vignette" autoplay loop poster="https://storage.googleapis.com/troveup-imagestore/assets/img/trove-vid.jpg" id="bgvid"><source src="https://storage.googleapis.com/troveup-imagestore/assets/video/trove-vid.webm" type="video/webm"><source src="https://storage.googleapis.com/troveup-imagestore/assets/video/trove-vid.mp4" type="video/mp4"></video>  --><!-- <button id="pausebtn" class="btn btn-pause centered"><span class="icon-button ion-ios-pause"></span></button> -->
    <!-- <button id="signinbtn" class="btn btn-signin">Login</button> -->
    <h3><span style="font-style:italic;font-size:85%;">free trial</span><br>CUSTOM JEWELRY</h3>
    <h5>Customize jewelry.<br>Get a free 3D-printed sample.<br><span style="font-style:italic;font-size:80%;">No credit card or sign-up required.</span></h5>
    <button id="bottomnextbtn" class="btn btn-bottomnext">Get a 3D-Printed Sample</button>
    <button id="bottomnextbtnmob" class="btn btn-bottomnext">Start Customizing Jewelry</button>
    <h6>No credit card or sign-up required.</h6>
    <div class="downarrow hidden"><a id="scrollbutt"><span class="icon ion-chevron-down"></div>
    <!-- <div class="bottomHolder hidden">
      <h2>See what others have customized</h2>
      <button id="prevButton" class="icon ion-ios-arrow-back"></button>
      <button id="nextButton" class="icon ion-ios-arrow-forward"></button>
      <div class="slider-nav">
        <!--DONE 7. Chevron Ring -->            <!--<div class="a"><a href="#" onclick="triggerItemRequest('https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/chevron-2.jpg', 'https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/chevron-ring-silver.jpg','/onboard/customize/625');">                  <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/chevron-ring-silver.jpg" class="img-responsive">    </a></div>-->
        <!--DONE 2. Heartbeat Ring -->          <!--<div class="a"><a href="#" onclick="triggerItemRequest('https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/open-arrow-carousel.jpg', 'https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/heartbeat-ring.jpg','/onboard/customize/698');">             <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/heartbeat-ring.jpg" class="img-responsive">         </a></div>-->
        <!--DONE 5. Double Warrior Ring -->     <!--<div class="a"><a href="#" onclick="triggerItemRequest('https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/double-warrior-ring.jpg', 'https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/double-warrior.jpg','/onboard/customize/910');">             <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/double-warrior.jpg" class="img-responsive">         </a></div>-->
        <!--DONE 9. Eye Ring -->                <!--<div class="a"><a href="#" onclick="triggerItemRequest('https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/parted-ring-carousel.jpg', 'https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/eye-ring-silver.jpg','/onboard/customize/695');">           <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/eye-ring-silver.jpg" class="img-responsive">        </a></div> -->
        <!--DONE 3. Rumpus Ring -->             <!--<div class="a"><a href="#" onclick="triggerItemRequest('https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/rumpus-ring.jpg', 'https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/rumpus-ring.jpg','/onboard/customize/679');">                        <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/rumpus-ring.jpg" class="img-responsive">            </a></div>-->
        <!--DONE 4. Twin Atom Ring -->          <!--<div class="a"><a href="#" onclick="triggerItemRequest('https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/double-warrior-carousel.jpg', 'https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/twin-atom-silver.jpg','/onboard/customize/47');">        <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/twin-atom-silver.jpg" class="img-responsive">       </a></div>-->
        
        <!--DONE 1. Waterdrop Ring -->          <!--<div class="sli-nav"><a href="#" onclick="triggerItemRequest('https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/waterdrop-ring-carousel.jpg', 'https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/waterdrop.jpg','/onboard/customize/167');">              <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/waterdrop.jpg" class="img-responsive">              </a></div>-->
        <!--DONE 6. Pearl Ring -->              <!--<div class="sli-nav"><a href="#" onclick="triggerItemRequest('https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/pearl-ring.jpg', 'https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/pearl-ring.jpg','/onboard/customize/132');">                          <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/pearl-ring.jpg" class="img-responsive">             </a></div>-->
        <!--DONE 8. Open Arrow Ring -->         <!--<div class="sli-nav"><a href="#" onclick="triggerItemRequest('https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/arrow-ring-twist.jpg', 'https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/open-arrow.jpg','/onboard/customize/769');">                    <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/open-arrow.jpg" class="img-responsive">             </a></div>-->
        <!--DONE 1. Monogram Pendant -->        <!--<div class="sli-nav"><a href="#" onclick="triggerItemRequest('https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/monogram-pendant.jpg', 'https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/monogram-pendant.jpg','/onboard/customize/772');">              <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/monogram-pendant.jpg" class="img-responsive">       </a></div>-->
        <!--DONE 2. Open Rectangle Necklace --> <!--<div class="sli-nav"><a href="#" onclick="triggerItemRequest('https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/open-rectangle-pendant.jpg', 'https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/open-rectangle-silver.jpg','/onboard/customize/781');">   <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/open-rectangle-silver.jpg" class="img-responsive">  </a></div>-->
        <!--DONE 3. Arrow Necklace -->          <!--<div class="sli-nav"><a href="#" onclick="triggerItemRequest('https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/warrior-ring-2-carousel.jpg', 'https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/arrow-necklace.jpg','/onboard/customize/783');">         <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/arrow-necklace.jpg" class="img-responsive">         </a></div>-->
      <!--</div>
    </div> -->
    
  </div>

  <div class="container bottomItems hidden">
    <h2>GET INSPIRED AND SEE WHAT OTHERS HAVE CREATED</h2>
    <div class="row">
      <!-- 1. Chevron Rin-->   <div class="col-sm-2 col-xs-6 sli-nav link-hover-effect" onclick="triggerItemRequest('https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/chevron-2.jpg', 'https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/chevron-ring-silver.jpg','/onboard/customize/625');"><img src="https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/chevron-2.jpg" class="img-responsive"></div>
    <!-- 2. Openarrow (nortb--><div id="mobhide" class="col-sm-2 col-xs-6 sli-nav link-hover-effect" onclick="triggerItemRequest('https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/open-arrow-carousel.jpg', 'https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/open-arrow.jpg','/onboard/customize/769');"><img src="https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/open-arrow-carousel.jpg" class="img-responsive"></div>
                               <div id="deskhide" class="col-sm-2 col-xs-6 sli-nav link-hover-effect" onclick="triggerItemRequest('https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/open-arrow-carousel.jpg', 'https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/heartbeat-ring.jpg','/onboard/customize/698');"><img src="https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/open-arrow-carousel.jpg" class="img-responsive"></div>
    <!-- 7. Double WarRing --> <div class="col-sm-2 col-xs-6 sli-nav link-hover-effect" onclick="triggerItemRequest('https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/double-warrior-ring.jpg', 'https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/double-warrior.jpg','/onboard/customize/910');"><img src="https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/double-warrior-ring.jpg" class="img-responsive"></div>
    <!-- 8. Eye Ring -->       <div class="col-sm-2 col-xs-6 sli-nav link-hover-effect" onclick="triggerItemRequest('https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/parted-ring-carousel.jpg', 'https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/eye-ring-silver.jpg','/onboard/customize/695');"><img src="https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/parted-ring-carousel.jpg" class="img-responsive"></div>
    <!-- 4. Rumpus Ring -->    <div class="col-sm-2 col-xs-6 sli-nav link-hover-effect" onclick="triggerItemRequest('https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/rumpus-ring.jpg', 'https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/rumpus-ring.jpg','/onboard/customize/679');"><img src="https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/rumpus-ring.jpg" class="img-responsive"></div>
    <!-- 5. Twin Atom Ring --> <div class="col-sm-2 col-xs-6 sli-nav link-hover-effect" onclick="triggerItemRequest('https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/double-warrior-carousel.jpg', 'https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/twin-atom-silver.jpg','/onboard/customize/47');"><img src="https://storage.googleapis.com/troveup-imagestore/assets/img/ftui-jewelry/carousel/double-warrior-carousel.jpg" class="img-responsive"></div>
    </div>
  </div>
    <script>

      $(".sli-nav a").click(function(e) {
         e.stopPropagation();
      });

      

      // function calcBottom() {
      //   if ( window.innerHeight >= 600 ) {
      //     var botVar = Math.min(0, (window.innerHeight - ( $('.bottomHolder').innerHeight() + $('.vidstuff').innerHeight() ) ) );
      //     if ( botVar >= -120 ) {
      //       $('.bottomHolder').css("bottom", botVar / 2);
      //     } else {
      //       $('.bottomHolder').css("bottom", botVar);
      //     }
      //   } else {
      //     $('.bottomHolder').css("bottom", -340);
      //   }
      // }

      function triggerItemRequest(image, imageb, link) {
        $('#livePhoto').attr("src", imageb);
        $('#livePhotoLeft').attr("src", image);
        $('#itemModal').modal('show'); 
        $('#bottomnextmodbtn').attr("href", link);
        mixpanel.track("onboard_click_modal", {
          "itemLink": link
        });
      }



      function getUrlVars() {
        var vars = [], hash;
        var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
        for(var i = 0; i < hashes.length; i++) {
            hash = hashes[i].split('=');
            vars.push(hash[0]);
            vars[hash[0]] = hash[1];
        }
        return vars;
      }

      function superProp(ref) {
        mixpanel.register({
          "refBucket": ref
        });
      };

      function land() {
        mixpanel.track("land_onboard_welcome");
      };

      $("#scrollbutt").click(function(e) {
        $("html, body").animate({ scrollTop: 500 }, "slow");
        return false;
      });

      $(document).ready(function() {
        
        var ref = getUrlVars()["ref"];
        land();
        if (ref) {
          superProp(ref);
          if (ref == "B" || ref == "b") {
           $('.bottomItems').removeClass('hidden');
           $('.downarrow').removeClass('hidden');
           $('body').removeClass('biggy');
          }
        }

        var sStuff = document.querySelector(".spinstuff");
        var vStuff = document.querySelector(".vidstuff");
        var mStuff = document.querySelector(".modstuff");
        var msStuff = document.querySelector(".modspinstuff");
        var lpl = document.getElementById("livePhotoLeft");
        var nextButton = document.getElementById("bottomnextbtn");
        var nextButtonMob = document.getElementById("bottomnextbtnmob");
        // var signinButton = document.getElementById("signinbtn");
        var modnextButton = document.getElementById("bottomnextmodbtn");
        var bod = document.querySelector("body");
        // var video = document.getElementById("bgvid");
        // var pauseButton = document.querySelector(".btn");

        
        nextButton.addEventListener("click", function() {
          sStuff.classList.toggle("show");
          vStuff.classList.toggle("show");
          setTimeout(function(){ window.location.href = "/onboard/choose"; }, 300);
        }, false);

        nextButtonMob.addEventListener("click", function() {
          sStuff.classList.toggle("show");
          vStuff.classList.toggle("show");
          setTimeout(function(){ window.location.href = "/onboard/choose"; }, 300);
        }, false);

        // signinButton.addEventListener("click", function() {
        //   window.location.href = "/signin"; 
        // }, false);

        modnextButton.addEventListener("click", function() {
          var destlink = $('#bottomnextmodbtn').attr("href");
          mStuff.classList.toggle("show");
          msStuff.classList.toggle("show");
          lpl.classList.toggle("show");
          mixpanel.track("onboard_next_modal");
          setTimeout(function(){ window.location.href = destlink; }, 300);
        }, false);


        // if (head.mobile) {
        //   $('.icon-button').removeClass('ion-ios-pause').addClass('ion-ios-play');
        //   bod.classList.add("stopfader-mobile");
        // }
        // function vidFade() {
        //   video.classList.add("stopfade");
        //   bod.classList.add("stopfader");
        // }
        // video.addEventListener('ended', function() {
        //   video.pause();
        //   vidFade();
        // }, false); 
        // pauseButton.addEventListener("click", function() {
        //   video.classList.toggle("stopfade");
        //   bod.classList.toggle("stopfader");
        //   if (video.paused) {
        //     video.play();
        //     if (!head.mobile) {
        //       pauseButton.innerHTML = "<span class='icon-button ion-ios-pause'></span>";
        //     }
        //   } else {
        //     video.pause();
        //     pauseButton.innerHTML = "<span class='icon-button ion-ios-play'></span>";
        //   }
        // }, false);

        // video.addEventListener('touchstart', function(e) {
        //   e.preventDefault();
        //   video.play();
        // });

      //   $('.slider-nav').slick({
      //     slidesToShow: 5,
      //     infinite: true,
      //     slidesToScroll: 1,
      //     prevArrow: '#prevButton',
      //     nextArrow: '#nextButton',
      //     centerMode: true,
      //     centerPadding: '0px',
      //     focusOnSelect: true,
      //     autoplay: false,
      //     autoplaySpeed: 2000,
      //     cssEase: 'ease-in-out',
      //     responsive: [
      //       {
      //         breakpoint: 768,
      //         settings: {
      //           arrows: false,
      //           infinite: true,
      //           centerMode: true,
      //           centerPadding: '0px',
      //           slidesToShow: 3
      //         }
      //       },
      //       {
      //         breakpoint: 480,
      //         settings: {
      //           arrows: false,
      //           centerMode: false,
      //           infinite: true,
      //           centerPadding: '0px',
      //           slidesToShow: 2
      //         }
      //       }
      //     ]
      //   });
      });

      // $(window).on('resize', function() {
      //   if ( window.innerWidth >= 800 ) {
      //     calcBottom();
      //   } else {
      //     $('.bottomHolder').css("bottom", 0);
      //   }
      // });
    </script>

  </body>
</html>