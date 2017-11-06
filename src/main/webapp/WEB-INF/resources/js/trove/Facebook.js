/** Helper JS file for making FB tracking calls.  Must already have the Facebook Pixel code on the page **/

/**
 * Sends a tracking event to Facebook when a user views the content of a page
 *
 * @param itemId String value representing the item
 * @param price Float value representing the item's price
 */
function eventViewContent(itemId, price) {
    fbq('track', 'ViewContent', {
        content_type: 'product',
        content_ids: [itemId],
        value: price,
        currency: 'USD'
    });
}

/**
 * Sends a tracking event upon addition of an item to the user's cart.
 *
 * @param itemId String value representing the item
 * @param price Float value representing the item's price
 */
function eventAddToCart(itemId, price) {
    fbq('track', 'AddToCart', {
        content_ids: [itemId],
        content_type: 'product',
        value: price,
        currency: 'USD'
    });
}

/**
 * Sends a tracking event upon completion of the purchase flow.
 *
 * @param itemIds Array of strings representing all items in the bag
 * @param subtotal Total of all of the line items used to check out.
 */
function eventPurchase(itemIds, subtotal) {
    fbq('track', 'Purchase', {
        content_type: 'product',
        content_ids: itemIds,
        value: subtotal,
        currency: 'USD'
    });
}

/**
 * Sends a tracking event upon initiating the checkout process
 *
 * @param itemIds Array of strings representing all items in the bag
 * @param subtotal Total amount from the line items in the bag
 */
function eventInitiateCheckout(itemIds, subtotal) {
    fbq('track', 'InitiateCheckout', {
        content_type: 'product',
       content_ids: itemIds,
       value: subtotal,
       currency: 'USD'
    });
}

/**
 * Sends a tracking event upon adding payment info during the checkout process
 *
 * @param itemIds
 * @param subtotal
 */
function eventAddPaymentInfo(itemIds, subtotal) {
    fbq('track', 'AddPaymentInfo', {
        content_type: 'product',
       content_ids: itemIds,
        value: subtotal,
        currency: 'USD'
    });
}




