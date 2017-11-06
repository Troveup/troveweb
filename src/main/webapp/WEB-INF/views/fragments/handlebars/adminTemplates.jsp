<script id="adminorderrow" type="text/x-handlebars">
    {{#each this}}
      <div class="well orderrow"">
        <button class="detailsbutton" onclick="orderClickRedirect({{orderId}})">Order Details ></button>
        <div class="row datarow">
          <div class="col-md-4">
            <span class="bold">Date: </span>{{userFriendlyOrderDate}}
          </div>
          <div class="col-md-4">
            <span class="bold">Order ID: </span> {{orderId}}
          </div>
          <div class="col-md-4">
            <span class="bold">Order Total: </span>$ {{grandTotal}}
          </div>
        </div>
        <div class="row datarow">
          <div class="col-md-4">
            <span class="bold">Username: </span> {{purchaser.username}}
          </div>
          <div class="col-md-4">
            <span class="bold">Checkout Email: </span> {{checkoutEmail}}
          </div>
          <div class="col-md-4">
            <span class="bold">Order Status: </span> {{orderStatus}}
          </div>
        </div>
        <div class="row datarow">
            <div class="col-md-3" style="margin-right:25px">
                <span class="bold">FTUE Order: </span> {{#if originFtue}}true{{else}}false{{/if}}
            </div>
            <div class="col-md-4">
                <span class="bold">Order Number: </span> {{troveOrderNumber}}
            </div>
        </div>
        <div class="well">
        {{#each orderItems}}
          <div class="row">
            <div class="col-md-4">
              {{frozenItemName}}
            </div>
            <div class="col-md-4">
              $ {{actualPrice}}
            </div>
          </div>
        {{/each}}
        {{#each genericItemsList}}
          <div class="row">
            <div class="col-md-4">
              {{cartDisplayName}}
            </div>
            <div class="col-md-4">
              $ {{price}}
            </div>
          </div>
        {{/each}}
        </div>
        {{#if hasNote}}
          <div class="well">
            <div class="row">
              <div class="col-md-4"><span class="bold">Latest Note: </span>
                {{latestNote.note}}
              </div>
              <div class="col-md-4"><span class="bold">Commenter: </span>
                {{latestNote.userCreator.firstName}}
              </div>
              <div class="col-md-4"><span class="bold">Date Created: </span>
                {{latestNote.userFriendlyOrderNoteDate}}
              </div>
            </div>
          </div>
        {{/if}}
      </div>
    {{/each}}
</script>
