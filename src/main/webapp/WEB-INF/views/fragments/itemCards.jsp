<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="trove" uri="http://troveup.com/jsp/tlds/itemimage" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <!-- item cards -->
    <div class="row items">
      <c:if test="${not empty items}">
        <c:forEach var="item" items="${items}">
          <div class="block card_mask item" style="display: none;">
            <div id="trovedAlert-${item.itemId}" class="overlaid"><div class="message"><h2>Saved to<br></br>Your Trove</h2><img class="add_image" src="/resources/img/plus.png"></div></div>
            <div id="buyAlert-${item.itemId}" class="overlaid"><div class="message"><h2>Added to<br></br>Your Bag</h2><img class="add_image" src="/resources/img/plus.png"></div></div>
            <div class="card_column_single">
              <div class="overlay">
                <a href="/public/productdescription/${item.itemId}"><div class="card_darken" style="position: absolute"></div></a>
                <div class="card_content_img"><img class="card_image" src="<trove:itemimage item="${item}" itemNumber="0" materials="${materials}"/>"></div>
                
                <!-- Buttons -->
                <div class="grid-cta">
                  <button id="troveItem-${item.itemId}" onclick="TROVE.troveItem(${item.itemId})" class="btn btn--red btn--small btn--card--overlay">Trove</button>
                  <a href="/public/customize/webgl/${item.itemId}" alt="Customize this design"><button class="btn btn--red btn--small btn--card--overlay">Customize</button></a>
                  <button id="buyItem-${item.itemId}" class="btn btn--lightgray btn--small btn--card--overlay cardbuybutton" data-toggle="modal" data-target="#modal-${item.itemId}">Buy</button>
                </div>

              </div>
              <div class="card_content_a"><div class="card_product_name"><a href="/public/productdescription">${item.itemName}</a></div></div>
              <div class="card_content_b product-collection-name">
                <div class="card_user_info"><a href="/private/user/${item.itemOwner.userId}"><img src="${item.itemOwner.profileImageThumbnailPath}" class="card_avatar_small"><span class="card_username">${item.itemOwner.firstName}</span></a></div>
              </div>

              <!-- Modal -->
              <div class="modal fade modal-${item.itemId}" id="modal-${item.itemId}" tabindex="-1" role="dialog" aria-labelledby="buyModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                  <div class="modal-content">
                    <div class="modal-header"><h4 class="modal-title" id="buyModalLabel">Buy Item</h4></div>
                    <div class="modal-body">
                      <span class="spinner addtocartfromcard-${item.itemId}"></span>
                      <div class="addtocartfromcardstuff-${item.itemId}">
                        <div class="material-selector-container">
                          <span class="selector-title">Material</span>
                          <div class="material-selector">
                            <select name="dismenu" id="dismenu-${item.itemId}" onchange="updateMaterial(${item.itemId});">
                              <c:if test="${not empty materials}">
                                <c:forEach var="material" items="${materials}">
                                  <c:forEach var="finish" items="${material.finishList}">
                                    <option value="${material.materialId} ${finish.finishId}">${material.name} - ${finish.name}</option>
                                  </c:forEach>
                                </c:forEach>
                              </c:if>
                            </select>
                          </div>
                        </div>
                        <div class="material-selector-container" style="margin:0 10px 15px;">
                          <span class="selector-title">Size</span>
                          <div class="material-selector">
                            <select name="sizee" id="sizee-${item.itemId}" onchange="updateSize(${item.itemId});">
                              <c:forEach var="size" items="${item.availableSizes}">
                                <option value="${size}">${size}</option>
                              </c:forEach>
                            </select>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="modal-footer">
                      <button type="button" class="btn btn--lightGray" data-dismiss="modal" style="float: left;">Close</button>
                      <button id="addToCart" type="button" onclick="TROVE.buyItem(${item.itemId})" class="btn btn--red addToCart">Add to Bag</button>
                    </div>
                  </div>
                </div>
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