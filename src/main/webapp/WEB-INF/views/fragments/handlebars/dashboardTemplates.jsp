<script id="manufacturerorderrow" type="text/x-handlebars">
    {{#each this}}
    <div class="well row-well">
      <div class="img-container">
        <img class="itemimg" src="{{primaryDisplayImageUrl}}">
      </div>
      <div class="data-container">
        <div class="row rowspace">
          <div class="col-md-4">
            <span class="bold">Order Id:</span> {{associatedOrder.orderId}}
          </div>
          <div class="col-md-4">
            <span class="bold">Item Id:</span> {{simpleItemId}}
          </div>
          <div class="col-md-4">
            <span class="bold">Order Date:</span> {{associatedOrder.userFriendlyOrderDate}}
          </div>
        </div>
        <div class="row rowspace">
          <div class="col-md-4">
            <span class="bold">Color:</span> {{color}}
          </div>
          <div class="col-md-4">
            <span class="bold">Rush:</span> {{evalbool rushOrder}}
          </div>
          <div class="col-md-4">
            <span class="bold">Mold Serial #:</span> {{moldSerialNumber}}
          </div>
        </div>
        <div class="row rowspace">
          <div class="col-md-4">
            <span class="bold">Qty:</span> {{quantity}}
          </div>
          <div class="col-md-4">
            <span class="bold">Size:</span> {{size}}
          </div>
          {{#exists engraving}}
          <div class="col-md-4">
            <span class="bold">Engraving:</span> {{engraving}}
          </div>
          {{/exists}}
        </div>
        <div class="row rowspace">
          <div class="col-md-4">
            <span class="bold">Estimated Completion Date:</span>
          </div>
          <div class="col-md-4">
          {{#if lastSetManufacturerCompletionDate}}
            <input data-itemid="{{simpleItemId}}" type="date" onchange="updateManufacturerOrderCompletionDate(this);" value="{{lastSetManufacturerCompletionDate}}">
          {{else}}
            <input data-itemid="{{simpleItemId}}" type="date" onchange="updateManufacturerOrderCompletionDate(this);">
          {{/if}}
          </div>
        </div>
        <div class="row rowspace">
          <div class="col-md-4">
            <span class="bold">Order Status:</span>
          </div>
          <div class="col-md-4">
            <select data-itemid="{{simpleItemId}}" onchange="updateManufacturerStatus(this);">
              {{#each availableStatuses}}
              {{#if selected}}
                <option value="{{this.stringValue}}" selected>{{this.stringStatusHumanReadable}}</option>
              {{else}}
                <option value="{{this.stringValue}}">{{this.stringStatusHumanReadable}}</option>
              {{/if}}
              {{/each}}
            </select>
          </div>
        </div>
        <div class="row rowspace">
          <div class="col-md-4">
            <span class="bold">Manufacturing Price:</span>
          </div>
          <div class="col-md-4">
          {{#if manufacturingPrice}}
            <input data-itemid="{{simpleItemId}}" onblur="updateManufacturerPrice(this);" type="number" min="0.01" step="0.01" value="{{manufacturingPrice}}">
          {{else}}
            <input data-itemid="{{simpleItemId}}" onblur="updateManufacturerPrice(this);" type="number" min="0.01" step="0.01" placeholder="{{manufacturerPriceText}}">
          {{/if}}
          </div>
        </div>
        {{#if latestNote}}
        <div class="row rowspace">
        {{else}}
        <div class="row rowspace notes-label-container" style="display:none;">
        {{/if}}
          <div class="col-md-4">
            <span class="bold notes-label">Latest Note (Out of {{notesCount}}) by {{latestNote.userCreator.firstName}}:
          </div>
          <div class="col-md-4"></div>
          <div class="col-md-4">
            <button class="action-button" onclick="window.open('/dashboard/manufacturer/notes/{{simpleItemId}}')">View All Notes</button>
          </div>
        </div>
        {{#if latestNote}}
        <div class="row rowspace notes-container">
        {{else}}
        <div class="row rowspace notes-container" style="display:none;">
        {{/if}}
          <div class="well">
            {{latestNote.note}}
          </div>
        </div>
        <div class="row rowspace new-note-label">
          <span class="bold">New Note:</span>
        </div>
        <div class="row rowspace new-note-textarea-container">
          <textarea class="new-note-textarea" data-itemid="{{simpleItemId}}"></textarea>
        </div>
        <button class="action-button" onclick="addNote(this);">Add Note</button>
      </div>
    </div>
{{/each}}

</script>