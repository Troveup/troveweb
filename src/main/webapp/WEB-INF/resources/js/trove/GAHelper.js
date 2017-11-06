/** Helper JS functions to fire various Enhanced Google Analytics ecommerce transactions/events.
 Requires that analytics.js already be present and active on the page.  **/

function GAHelper() {
    ga('require', 'ec');
}

GAHelper.prototype.itemView = function (itemId, itemName, itemPrice) {
    performEcAddProductFunction(itemId, itemName, itemPrice);
    ga('ec:setAction', 'detail');

    ga('send', 'event', {
        'eventCategory': 'Item Interaction',
        'eventAction': 'View Item',
        'eventLabel': itemName
    });
};

GAHelper.prototype.addToBag = function (itemId, itemName, itemPrice) {
    performEcAddProductFunction(itemId, itemName, itemPrice);
    ga('ec:setAction', 'add');

    ga('send', 'event', {
        'eventCategory': 'Item Interaction',
        'eventAction': 'Add to Bag',
        'eventLabel': itemName
    });
};

GAHelper.prototype.landCheckout = function(items, cartId) {
    $.each(this, function() {
       performEcAddProductFunction(this.itemId, this.itemName, this.itemPrice);
    });

    ga('ec:setAction', 'checkout', {
       'id': cartId
    });

    ga('send', 'event', {
        'eventCategory': 'Bag Interaction',
        'eventAction': 'Land Bag'
    });
};

GAHelper.prototype.purchase = function(items, cartId) {
    var totalPrice = 0.00;
    $.each(items, function() {
        performEcAddProductFunction(this.itemId, this.itemName, this.itemPrice);
        totalPrice += parseFloat(this.itemPrice);
    });

    ga('ec:setAction', 'purchase', {
        'id': cartId,
        'revenue': totalPrice.toFixed(2)
    });

    ga('send', 'transaction', {
        'id': cartId,
        'revenue': totalPrice.toFixed(2)
    });
};

function performEcAddProductFunction(itemId, itemName, itemPrice) {
    ga('ec:addProduct', {
        'id': itemId,
        'name': itemName,
        'price': itemPrice
    });
}
