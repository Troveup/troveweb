<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>${pageTitle}</title>
    <meta name="description" content="${pageDescription}">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href='https://fonts.googleapis.com/css?family=Quicksand:300,400,700' rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Raleway:400,200,300,500,600,700' rel='stylesheet' type='text/css'>
    <link rel="icon" href="/resources/img/favicon.png?v=2">
    <script src="https://code.jquery.com/jquery-2.1.3.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.13.1/jquery.validate.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.13.1/additional-methods.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/resources/stylesheets/onboard.css">
    <c:import url="../fragments/analytics/all.jsp"/>
    <style>
      body {
        margin:0;
        background:#FFF
      }

      video {
        position:fixed;
        top:50%;
        left:50%;
        min-width:100%;
        min-height:100%;
        width:auto;
        height:auto;
        z-index:-100;
        -webkit-transform:translateX(-50%) translateY(-50%);
        transform:translateX(-50%) translateY(-50%);
        background:url(https://storage.googleapis.com/troveup-imagestore/assets/video/trove-vid.jpg) no-repeat;
        background-size:cover;
        -webkit-transition: all 1s ease-in-out;
        transition: all 1s ease-in-out;
          -webkit-filter: blur(0px);
             -moz-filter: blur(0px);
               -o-filter: blur(0px);
              -ms-filter: blur(0px);
                  filter: blur(0px);
      }

      .overlay {
        background:rgba(0,0,0,0.3);
        width:100%;
        height: 100%;
        position: absolute;
        top: 0;
        left: 0;
        z-index:-99;
      }
      body.onboard .ob-logo {
        width: 170px;
        padding-top: 5px;
        padding-bottom: 15px;
      }
      body.onboard h2 {
        font-family: "Raleway", sans-serif;
        margin-bottom: 50px;
        font-weight: 400;
        color: rgba(255,255,255,0.8);
        font-size: 18px;
        letter-spacing: .15em;
        margin-top: -8px;
        text-shadow: 1px 2px 10px rgba(0,0,0,0.3);
          line-height: 1.75em;
      }

      body.onboard h3 {
        margin-top: 0px;
        color: rgba(255,255,255,0.85);
        text-shadow: 1px 2px 10px rgba(0,0,0,0.3);
        margin-bottom: 30px;
      }

      body.onboard h4 {
        font-weight: 500;
        font-size: 20px;
      }
      
      .holder {
        background-color: rgba(255,255,255,0.8);
        padding: 38px;
        border-radius: 0px;
        border: 6px solid #ccc;
        width: 700px;
        position: relative;
        left: 50%;
        margin-left: -350px;
      }

      body.onboard h5 {
        font-family: "Raleway", sans-serif;
        font-size: 15px;
        font-weight: 300;
        color: #333;
        margin-bottom: 10px;
        line-height: 2em;
        padding: 15px;
      }

      body.onboard .btn-primary {
        background-color: #EC2121;
        position: relative;
        font-size: 16px;
        width: 600px;
        margin-top: 10px;
        margin-bottom: 30px;
        padding-bottom: 10px;
      }



      @media screen and (max-width: 768px) {

        body.onboard .btn-primary {
          position: inherit;
          font-size: 18px;
          width: 100%;
          display: inline-block;
          white-space: pre-line;
          margin-top: 0px;
        }
        body.onboard h2 {
          margin-bottom: 5px;
        }
        body.onboard h3 {
          margin-top: 0px;
        }

        .holder {
            background-color: rgba(255,255,255,0.8);
            padding: 40px 20px 20px 20px;
            border-radius: 8px;
            width: 100%;
            position: relative;
            left: 0px;
            margin-left: 0px;
          }

        body.onboard h5 {
          margin-bottom: 7px;
          line-height: 1.5em;
          padding: 10px;
          white-space: pre-line;
        }

        .chrome {
          width: 500px;
          margin-top: -20px;
        }

        body.onboard h4 {
          text-transform: uppercase;
          color: #EC2121;
          margin-bottom: -20px;
          margin-top: 0px;
        }

        body.onboard .centrafuge {
          margin-top: 0px !important;
          top: 0px !important;
        }
      }

    </style>
  </head>
  <body class="${bodyClass}">
    <div class="centrafuge"> 
      
      <div class="holder">
        <h4>SORRY TO SEE YOU GO!</h4>
        <h5>Let us know why you're leaving?</h5>
        <br><br><br>
        <a href="#">Why?</a>
      </div>
    </div>

    <c:import url="../fragments/phonboardJS.jsp"/>
    <script type="text/javascript">
      function landed() {
        mixpanel.track("landthankyouu");
      };

      $(document).ready(function() {
          landed();
      });
    </script>
  </body>
</html>
