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

        .thumbnail > img {
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
            font-family: 'Vollkorn', serif;
            font-style: italic;
        }

        .cta-header {
            font-family: 'Vollkorn', serif;
        }

        .welcome-banner-container {
            /*      width: 100%;
                  display:inline-block;
                  background-size:100%;
                  background-repeat: no-repeat;*/
        }

        .try-text {
            position: absolute;
            padding: 20px 20px 40px;
            background-color: rgba(255, 255, 255, .75);
            margin-left: 6%;
        }

        .try-photo-container {
            display: block;
            margin: 0 auto;
        }

        .how-header {
            position: absolute;
            background-color: #ffffff;
            font-size: 28px;
            left: 50%;
            margin-left: -153px;
            top: 30px;
            padding: 15px;
        }

        .try-container {
            display: block;
            margin: 0 auto;
            max-width: 1000px;
        }

        .how-body {
            margin: 60px 20% 30px;
            padding: 30px 5% 30px;
            text-align: center;
            border: 2px solid #dedede;
        }

        @media screen and (min-width: 769px) {
            .welcome-banner-container {
                /*      background-image: url("http://i.imgur.com/TD6CN88.png");
                */
            }
        }

        @media screen and (max-width: 768px) {
            .how-body {
                margin: 60px 10% 30px;
            }

            .welcome-banner-container {
                /*      background-image: url("http://i.imgur.com/F267GJT.png");
                */
            }

            .try-container {
                margin: 30px 5% 30px;
            }

            .try-text {
                position: relative;
                width: 100%;
                text-align: center;
                top: -40px;
                margin-left: 0;
                padding: 0px;

            }
        }

        @media screen and (max-width: 440px) {
            .how-body {
                margin: 60px 0 30px;
            }

            .cta-container {
                left: 0;
                margin-left: 0px;
                width: 100%;
            }
        }

        .how-header-text {
            color: #000;
        }

        .btn-open {
            background-color: transparent;
            color: #000;
            border: 2px solid #8e8b8b;
        }

        .cta-container {
            background-color: rgba(255, 255, 255, 0.75);
            width: 480px;
            z-index: 1;
            position: absolute;
            left: 50%;
            margin-left: -240px;
            text-align: center;
            padding: 50px 0;
        }

        .special-container {
            display: none;
        }

        .mobile-wrapper {
            display: none;
        }

        .button-wrapper {
            display: none;
        }

        input.input-style {
            width: 500px;
        }


    </style>
</head>

<body>
<c:if test="${not isAuthenticated}"><c:import url="../fragments/modals/authModal.jsp"/></c:if>
<c:import url="../fragments/nav/topNavBar.jsp"/>
<div class="container-fluid">
    <form class="new-collection form-horizontal" style="padding-top: 50px;" action="/admin/alertbannerpost?${_csrf.parameterName}=${_csrf.token}"
          method='POST' enctype="multipart/form-data">
        <div class="form newCollection">
            <fieldset>
                <div>
                    <label for="bannerenabled">Banner Enabled</label>
                    <input type="checkbox" name="bannerenabled" id="bannerenabled" checked/>
                </div>
                <div class="banner-wrapper">
                    <div>
                        <label for="bannertext">Banner Text</label>
                        <input class="input-style" type="text" name="bannertext" id="bannertext"/>
                    </div>
                    <div>
                        <label for="mobiletextenabled">Different Mobile Text</label>
                        <input type="checkbox" name="mobiletextenabled" id="mobiletextenabled">
                    </div>
                    <div class="mobile-wrapper">
                        <label for="mobilebannertext">Mobile Banner Text</label>
                        <input type="text" class="input-style" name="mobilebannertext" id="mobilebannertext"/>
                    </div>
                    <div>
                        <label for="buttonenabled">Enable Button</label>
                        <input type="checkbox" name="buttonenabled" id="buttonenabled"/>
                    </div>
                    <div class="button-wrapper">
                        <div>
                            <label for="buttontext">Button Text</label>
                            <input type="text" class="input-style" name="buttontext" id="buttontext"/>
                        </div>
                        <div>
                            <label for="buttonurl">Button URL (ex: /giftcard)</label>
                            <input type="text" class="input-style" name="buttonurl" id="buttonurl"/>
                        </div>
                    </div>
                </div>
            </fieldset>
            <button type="button" id="showmebutton">Show me!</button>
            <input type="submit" style="margin-left: 500px" value="Save Banner Changes">
        </div>
    </form>
</div>
<c:choose>
    <c:when test="${not empty success and success}">
        <script>alert("Success!");</script>
    </c:when>
    <c:when test="${not empty success and not success}">
        <script>alert("Oops!  There was a problem.  Please check your inputs and try again.");</script>
    </c:when>
</c:choose>

<script>
    $("#bannerenabled").change(function () {
        if ($("#bannerenabled").prop("checked")) {
            $(".banner-wrapper").show(500);
        }
        else {
            $(".banner-wrapper").hide(500);
        }
    });

    $("#mobiletextenabled").change(function () {
        if ($("#mobiletextenabled").prop("checked")) {
            $(".mobile-wrapper").show(500);
        }
        else {
            $(".mobile-wrapper").hide(500);
        }
    });

    $("#buttonenabled").change(function () {
        if ($("#buttonenabled").prop("checked")) {
            $(".button-wrapper").show(500);
        }
        else {
            $(".button-wrapper").hide(500);
        }
    });

    $("#showmebutton").click(function () {

        var adminBanner = $('#adminbanner');
        var bannerText = $('#bannertext').val();
        var mobileText = $('#mobilebannertext').val();
        var buttonText = $('#buttontext').val();

        if ($('#bannerenabled').prop("checked")) {

            if (bannerText && bannerText.length > 0) {
                $('#adminbannermessage').text(bannerText);
            }

            if ($('#mobiletextenabled').prop("checked") && mobileText && mobileText.length > 0) {
                $('#adminmobilebannermessage').text(mobileText);
            } else {
                $('#adminmobilebannermessage').text(bannerText);
            }

            if ($('#buttonenabled').prop("checked") && buttonText && buttonText.length > 0) {
                var button = $('#adminbannerbutton');
                button.text(buttonText);
                button.click(function() {
                    window.location.href = $('#buttonurl').val();
                });

                button.show(500);
            }
            else {
                $('#adminbannerbutton').hide(500);
            }

            adminBanner.show(500);

        } else {
            adminBanner.hide(500);
        }

    });
</script>
</body>
</html>