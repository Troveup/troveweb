<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="trove" uri="http://troveup.com/jsp/tlds/itemimage" %>


<script id="itemcards" type="text/x-handlebars">
      {{#each this}}
        {{#if isMobileCompatible}}
        <div class="card" id="card-{{itemId}}" style="opacity:0;">
        {{else}}
        <div class="card hidemobile" id="card-{{itemId}}" style="opacity:0;">
        {{/if}}
          <div id="trovedAlert-{{itemId}}" class="overlaid"><div class="message"><h2>Saved to<br></br>Your Trove</h2><img class="add_image" src="/resources/img/plus.png"></div></div>
          <div id="buyAlert-{{itemId}}" class="overlaid"><div class="message"><h2>Added to<br></br>Your Bag</h2><img class="add_image" src="/resources/img/plus.png"></div></div>
          <div class="card-content" id="item-{{itemId}}">
            <div class="overlay">
              <a class="imgLink" id="imgLink-{{itemId}}" href="/public/productdescription/{{itemId}}" onclick="categoryHelper.imageButtonClick(authHelper.getIsAuthenticated(), {{itemId}})"><div class="card_darken"></div></a>
              <div class="card_content_img"><img class="card_image" src="{{defaultCardImageUrl}}"></div>
              <div class="grid-cta">
                <button id="troveItem-{{itemId}}" onclick="categoryHelper.saveButtonClick({{itemId}}, '${loco}')" class="btn btn_trove">Save</button>
                <a class="cusLink" id="cusLink-{{itemId}}" href="/public/customize/webgl/{{itemId}}" alt="Customize this design"><button id="customizeItem-{{itemId}}" onclick="categoryHelper.customizeButtonClick(authHelper.getIsAuthenticated(), {{itemId}})" class="btn btn_customize">Customize</button></a>
              </div>
            </div>
            <div class="modal fade modal-{{itemId}}" id="modalbuyItem-{{itemId}}" tabindex="-1" role="dialog" aria-labelledby="buyModalLabel" aria-hidden="true">
              <div class="modal-dialog">
                <div class="modal-content" style="border:2px solid #dedede;">
                  <div class="modal-header" style="border-bottom:1px solid #dedede;"><h4 class="modal-title" id="buyModalLabel">Buy Item</h4></div>
                  <div class="modal-body">
                  <div class="card_product_name" style="width:200px;margin:0 auto;"><span style="font-weight:400;">{{itemName}}</span></div>
                  <img class="card_image" src="{{defaultCardImageUrl}}" style="margin:0 auto 20px;">
                    <div class="addtocartfromcardstuff-{{itemId}}">
                      <div class="material-selector-container">
                        <span class="selector-title">Material</span>
                        <div class="material-selector">
                          <select name="dismenu" id="dismenu-{{itemId}}" onchange="updateMaterial({{itemId}});" style="max-width:250px;">
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
                      {{#if sizeDropdownAvailable}}
                      <div class="material-selector-container">
                        <span class="selector-title">Size</span>
                        <div class="material-selector">
                          <select name="sizee" id="sizee-{{itemId}}" onchange="updateSize({{itemId}});">
                            {{#each availableSizes}}
                              <option value="{{@key}}">{{this}}</option>
                            {{/each}}
                          </select>
                        </div>
                      </div>
                      {{/if}}
                      {{#if shouldIncludeChainDropdown}}
                      <c:if test="${not empty availableChains}">
    <div class="chain-container">
    <span class="selector-title">Chain</span>
    <div class="material-selector">
    <select name="chain" id="chain-{{itemId}}" onchange="updateChain({{itemId}});">
    <c:forEach var="chain" items="${availableChains}">
        <option value="${chain.chainId}" selected>${chain.name} - ${chain.price}</option>
    </c:forEach>
    </select>
    </div>
    </div>
</c:if>
                      {{/if}}
                      {{#if isEngravable}}
                      <div class="material-selector-container">
                        <span class="selector-title">Engraving</span>
                        <div class="material-selector">
                          <input type="text" id="engravetext-{{itemId}}" maxlength="16" class="form-control engravebox" placeholder="Your Custom Engraving">
                          <label class="engravingfineprint">Maximum 16 Characters</label><div id="engravehint-{{itemId}}" class="engravehintstyle" data-placement="right" data-content="Text will be engraved in the inside center of our jewelry. Try-on Models do not come with engraved text.">?</div>
                        </div>
                      </div>
                      {{/if}}
                    </div>
                  </div>
                  <div class="modal-footer" style="border-top: 1px solid #dedede; padding:20px 30px 20px">
                    <button type="button" class="btn btn--lightGray" data-dismiss="modal" style="float: left;">Close</button>
                    <button id="addToCart" type="button"  data-dismiss="modal" onclick="TROVE.addFromCard({{itemId}})" class="btn btn--red addToCart" style="float:right;">Add to Cart</button>
                  </div>
                </div>
              </div>
            </div>
            <div class="card_content_a"><div class="card_product_name"><a href="/public/productdescription/{{itemId}}">{{itemName}}</a></div></div>
            <div class="cardprice">Starting at $ {{decimal startingPrice}}</div>
            {{#unless_blank defaultCollection}}
              <div class="card_content_b product-collection-name"><div class="card_user_info"><a onclick="categoryHelper.nameButtonClick(authHelper.getIsAuthenticated(), {{itemId}})" href="/private/user/{{itemOwner.userId}}"><img src="{{itemOwner.profileImageThumbnailPath}}" class="card_avatar_small"><span class="card_username">{{split itemOwner.firstName}}</span></a></div></div>
              <div class="card_content_b"><div class="card_collection_name"><a onclick="categoryHelper.collectionButtonClick(authHelper.getIsAuthenticated(), {{itemOwner.userId}})" href="/private/collection/{{defaultCollection.collectionId}}">{{defaultCollection.collectionName}}</a></div></div>
            {{else}}
              <div class="card_content_b product-collection-name"><div class="card_user_info"><a onclick="categoryHelper.nameButtonClick(authHelper.getIsAuthenticated(), {{itemId}})" href="/private/user/{{itemOwner.userId}}"><img src="{{itemOwner.profileImageThumbnailPath}}" class="card_avatar_small"><span class="card_username">{{split itemOwner.firstName}}</span></a></div></div>
              <div class="card_content_b"><div class="card_collection_name"><a onclick="categoryHelper.collectionButtonClick(authHelper.getIsAuthenticated(), {{itemOwner.userId}})" href="/private/user/{{itemOwner.userId}}">{{split itemOwner.firstName}}'s Trove</a></div></div>
            {{/unless_blank}}
            <div class="social_counts item">
              <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/trove-icon.svg" class="icon-small" title="Troves" alt="Troves"><span id="trovedCount-{{itemId}}">{{#if trovedCount}} {{trovedCount}} {{else}} 0 {{/if}}</span>
              <img src="https://storage.googleapis.com/troveup-imagestore/assets/img/remix-icon.svg" class="icon-small" style="padding-left: 8px" title="Remixes" alt="Remixes"><span id="remadeCount-{{itemId}}">{{#if remadeCount}} {{remadeCount}} {{else}} 0 {{/if}}</span>
            </div>
          </div>
        </div>
      {{/each}}
</script>




