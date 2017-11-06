<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="trove" uri="http://troveup.com/jsp/tlds/itemimage" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <!-- item cards -->
    <div class="row items">
      <c:if test="${not empty items}">
        <c:forEach var="item" items="${items}">
          <div class="block card_mask item" style="display: none;">
           
            <div class="card_column_single">
              <div class="overlay">
                <a href="#" data-toggle="modal" data-target="#modal-ph"><div class="card_darken" style="position: absolute"></div></a>
                <div class="card_content_img"><img class="card_image" src="<trove:itemimage item="${item}" itemNumber="0" materials="${materials}"/>"></div>
                
                <!-- Buttons -->
                <div class="grid-cta">
                  <button class="btn btn--red btn--small btn--card--overlay" data-toggle="modal" data-target="#modal-ph">Trove</button>
                  <button class="btn btn--red btn--small btn--card--overlay" data-toggle="modal" data-target="#modal-ph">Customize</button>
                  <button class="btn btn--lightgray btn--small btn--card--overlay" data-toggle="modal" data-target="#modal-ph">Buy</button>
                </div>

              </div>
              <div class="card_content_a"><div class="card_product_name"><a href="#" data-toggle="modal" data-target="#modal-ph">${item.itemName}</a></div></div>
              <div class="card_content_b product-collection-name">
                <div class="card_user_info"><a href="#" data-toggle="modal" data-target="#modal-ph"><img src="${item.itemOwner.profileImageThumbnailPath}" class="card_avatar_small"><span class="card_username">${item.itemOwner.firstName}</span></a></div>
              </div>

              <!-- <div class="card_content_b"><div class="card_collection_name"><a href="/public/collections-inside">Collection Name</a></div></div> -->
              <div class="card_content_count">
                <div class="card_social_count">
                  <img src="/resources/img/trove-icon.svg" class="icon-small" title="Troves" alt="Troves">
                  <span class="trovedCount-${item.itemId}">${item.trovedCount}</span>
                  <img src="/resources/img/remix-icon.svg" class="icon-small" title="Remakes" alt="Remakes">
                  <span class="remadeCount-${item.itemId}">${item.remadeCount}</span>
                </div>
              </div>
            </div>
          </div>
        </c:forEach>
      </c:if>
    </div>