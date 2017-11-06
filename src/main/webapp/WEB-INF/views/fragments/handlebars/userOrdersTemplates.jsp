<script id="itemcards" type="text/x-handlebars">
    {{#each this}}
      <div class="row orderContainer">
        <%--Order Header--%>
        <div class="orderHeader">
          <div class="orderDetails">
            <div class="orderNum"><b>Order Number </b><span> {{troveOrderNumber}}</span></div><br>
            <div class="orderDate"><b>Date </b><span>{{userFriendlyOrderDate}} EST</span></div>
            <div class="orderTotal"><b>Total </b><span>$ {{moneydecimalfix grandTotal}}</span></div>
          </div>
          <ul class="orderLinks">
            <!--<li><a href="#"><span class="hidesmaal">Order </span> Details</a></li> | <li><a href="#">Track <span class="hidesmaal">My Order</span></a></li>-->
          </ul>
        </div>
        {{#each orderItems}}
          <div id="order-item-row-{{cartItemId}}" class="orderItem">
            <div class="col-md-6 col-xs-12 orderItemLeft">
              <div class="orderItemStatus">Status: {{userFriendlyStatus}}</div>
              <div class="orderItemDetails">
                <img class="orderItemPhoto" src="{{cartItemReference.defaultCardImageUrl}}">
                <div class="orderItemName"><b>{{frozenItemName}}</b><span></div><br>
                <div class="orderItemMaterial">{{finishName}}</div><br>
                <div class="orderItemSize">Size: <span> {{size}}</span></div><br>
                <div class="orderItemPrice">$ {{moneydecimalfix actualPrice}}</div>
                {{#if cancelable}}
                  <br><div id="cancel-button-container-{{cartItemId}}" class="cancel-button-container"><div id="cancel-order-btn-text-{{cartItemId}}" onclick="cancelOrderLineItem({{cartItemId}})" class="cancel-text">X Cancel</div><span id="cancel-button-spinner-{{cartItemId}}" class="button-spinner centered"></span></div>
                {{else}}
                {{/if}}
              </div>
            </div>
            <div class="col-md-6 col-xs-12 orderItemRight">
              <span style="display: none" class="spinner centered"></span>
              <div class="order-line-item-right-side">
              {{#if prototypeRequested}}
                {{#if_str cartItemStatus 'PENDING_USER_DECISION'}}
                  <div class="tryOnStatus">CONFIRM
                  {{#if canCustomizeAgain}}
                    OR MODIFY YOUR ORDER</div>
                    <button onclick="completeOrderLineItem({{cartItemId}})" class="btn longButton purch">Send Me My Final Jewelry</button>
                    <a id="mdfy" href="/private/customizesample/{{cartItemId}}"><button class="btn longButton two">Modify Design</button></a>
                  {{else}}
                    YOUR ORDER</div>
                    <button onclick="completeOrderLineItem({{cartItemId}})" class="btn longButton purch"><span>Send Me My Final Jewelry</span></button>
                  {{/if}}
                {{else}}
                  <div class="tryOnStatus">{{ordersPageStatusTitleAndDescription.title}}</div>
                  <div class="tryOnNum">{{ordersPageStatusTitleAndDescription.description}}</div>
                {{/if_str}}
              {{else}}
                <div class="tryOnStatus">{{ordersPageStatusTitleAndDescription.title}}</div>
                <div class="tryOnNum">{{ordersPageStatusTitleAndDescription.description}}</div>
              {{/if}}
              </div>
            </div>     
          </div>
        {{/each}}


        {{#each genericItemsList}}
          <div class="orderItem">
            <div class="col-md-6 col-xs-12 orderItemLeft">
              <div class="orderItemDetails">
                {{#if_not_null userFriendlyItemStatus}}
                  <div class="orderItemStatus">Status: {{userFriendlyItemStatus}}</div>
                {{/if_not_null}}
                <img class="orderItemPhoto" src="{{bagLineItemImage}}">
                <div class="orderItemName"><b>{{cartDisplayName}}</b><span></div><br>
                <div class="orderItemPrice">$ {{moneydecimalfix price}}</div>
                {{#if cancelable}}
                  <div style="display:block; margin-top:10px"><a class="orderItemCancel" onclick="cancelOrderLineItem({{cartItemId}})" href="#" style="">X Cancel</a></div>
                {{else}}
                {{/if}}
              </div>
            </div>
            <div class="col-md-6 col-xs-12 orderItemRight">
              <div class="tryOnStatus">Details</div>
              <div class="tryOnNum">
                {{#each bagDisplayAttributes}}
                  {{this.key}}: {{this.value}}<br>
                {{/each}}
              </div>
            </div>     
          </div>
        {{/each}}

      </div>
    {{/each}}

  </script>