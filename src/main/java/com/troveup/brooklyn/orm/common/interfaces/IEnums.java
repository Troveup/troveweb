package com.troveup.brooklyn.orm.common.interfaces;

/**
 * Created by tim on 4/21/15.
 */
public interface IEnums
{
    enum SEEK_MODE{
        FULL,
        QUICK,
        //Equivalent to QUICK, only additionally retrieves a count of the number of items in the user's bag
        USER_BAG_COUNT,
        //Cart and its immediately necessary details
        USER_CART_QUICK,
        //User with their cart and a surface level seek of cart items
        USER_CART_CARTITEMS,
        //Full cart, all of its cart items, and all of the items associated with those cart items
        USER_CART_FULL,
        //User with cart and its payment details
        USER_CART_PAYMENT_DETAILS,
        //Address and cart items
        USER_CART_PREPARE,
        //Troved items and all details about those items
        USER_TROVED,
        //All details pertianing to the user's Trove page
        USER_TROVE,
        //Social connections and all details about those connections
        USER_SOCIAL_CONNECTIONS,
        //List of orders, surface details about those orders, and a shipping address reference
        USER_ORDERS_SLIM,
        //List of orders, surface details about those orders, the order item references
        USER_ORDERS_QUICK,
        //List of orders, all of the cart items associated with the order, all of the items associated with the
        // cart items, and surface details about the cart
        USER_ORDERS_MEDIUM,
        //List of orders, all of the cart items associated with the order, all of the items associated with the
        // cart items, and the full cart, all of its cart items, and all of the items associated with the cart items
        USER_ORDERS_FULL,
        //Includes the PII field of whether or not the user is opted in to mailers from Trove
        USER_SETTINGS,
        //Pulls only the followedUsers field
        USER_FOLLOWED_USERS,
        //Pulls the user plus their e-mail.  PII is contained in this fetch mode.
        USER_BAG_COUNT_EMAIL,
        //Retrieves only the user's addresses
        USER_ADDRESSES,
        //Item with full list of attributes
        ITEM_ATTRIBUTES,
        //All necessary details to display an item on the feed
        ITEM_FEED,
        //Fields necessary for the design story.  Item parent, base item, images, and owners
        ITEM_DESIGN_STORY,
        //All fields from the anonymous item object DB reference
        ANONYMOUS_ITEM_FULL,
        //Order details for details page
        ORDER_DETAILED,
        //Cart with its payment details
        ORDER_QUICK,
        ORDER_FULL,
        //Fields necessary for uploading models to a manufacturer
        ORDER_UPLOAD,
        //Address fields of an order
        ORDER_ADDRESS,
        //Shipping address and order items
        ORDER_SETTLE,
        ORDER_LINE_ITEMS,
        //Promo codes and generic items
        ORDER_INFLUENCER_DASHBOARD,
        //Pull full promo code details
        PROMO_FULL,
        //Cart with access to all promo codes and financial values
        CART_PROMO,
        //Pertinent item details for the customizer
        ITEM_CUSTOMIZER,
        //Only base-level customizer elements
        ITEM_CUSTOMIZER_FTUE,
        //Sample manufacturer and sample manufacturer material ID
        ITEM_FTUE_REQUEST,
        //Full render object
        RENDER_FULL,
        //Only enough information as necessary to submit for render
        FTUE_IMAGE_SUBMIT,
        //FTUE object with address data
        FTUE_ADDRESSES,
        //FTUE object with all data necessary for the landing page
        FTUE_LANDING_ACTION,
        //Ftue object with all materials and their prices
        FTUE_MATERIAL_PRICES,
        //Address data plus images for the outbound e-mail
        FTUE_POST_PROTOTYPE_EMAIL,
        //Material weights plus request address
        FTUE_PROTOTYPE,
        //Ftue object with subtotal, tax, shipping, and grandtotal
        FTUECHECKOUT_CART_PRICES,
        //Collection Name, description, images, counts, and surface data on followers
        COLLECTION_LIGHT,
        //Full FTUE Checkout object
        FTUECHECKOUT_FULL,
        USER_FOLLOWS,
        //A cart item and all of its ItemCustomization properties
        CART_ITEM_UPLOAD,
        //A cart item and all of the details necessary for submission (reference item, for now)
        CART_ITEM_ORDER_SUBMIT,
        //A cart item with only its status as an extra field
        CART_ITEM_STATUS,
        GENERIC_ITEM_FULL,
        ITEM_PRICING, //A cart and its shipping and billing address
        CART_ADDRESSES,
        ITEM_ORDER,
        SIMPLEITEM_DASHBOARD_ITEM_ORDER,
        ORDERNOTE_USER,
        ASSETS,
        STORE_FRONT_VIEW_DATA //data pulled for displaying user's storefront to customers
    }

    enum UPDATE_MODE
    {
        REPLACE,
        APPEND
    }

    enum COUNTRY_REQUEST_TYPE
    {
        COUNTRY_NAME("name"),
        ALPHA_2("isoAlpha2Code"),
        ALPHA_3("isoAlpha3Code"),
        NUMERIC("isoNumericCode");

        private final String text;

        COUNTRY_REQUEST_TYPE(String type) {
            this.text = type;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    enum SUBDIVISION_REQUEST_TYPE
    {
        CODE("code"),
        NAME("name"),
        CATEGORY("category"),
        COUNTRY("country");

        private final String text;

        SUBDIVISION_REQUEST_TYPE(String type) {
            this.text = type;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    enum ORDER_REQUEST_TYPE
    {
        VENDOR_CART_ID("vendorCartId"),
        DOLLAR_DISCOUNT("additionalDollarDiscount"),
        PERCENT_DISCOUNT("additionalPercentDiscount"),
        FREE_SHIPPING("freeShipping"),
        VENDOR_ORDER_NUMBER("vendorOrderNumber"),
        TRACKING_NUMBER("trackingNumber"),
        SHIPPER("shipper");

        private final String text;

        ORDER_REQUEST_TYPE(String type) {
            this.text = type;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
