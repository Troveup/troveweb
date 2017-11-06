package com.troveup.brooklyn.sdk.print.interfaces;

import com.troveup.brooklyn.orm.materials.model.Material;
import com.troveup.brooklyn.orm.order.model.Order;
import com.troveup.brooklyn.orm.user.model.Address;
import com.troveup.brooklyn.sdk.print.model.OrderStatus;
import com.troveup.brooklyn.sdk.print.model.OrderSubmissionDetails;
import com.troveup.brooklyn.sdk.print.model.PriceRequest;
import com.troveup.brooklyn.sdk.print.model.PriceResponse;
import com.troveup.brooklyn.sdk.print.shapeways.model.ShapewaysItem;
import org.joda.money.Money;

import java.util.List;

/**
 * Created by tim on 5/22/15.
 */
public interface IPrintSupplier
{
    //TODO:  Build this out more so that it's standardized across print suppliers.

    /**
     * Upload a model to a given supplier.
     *
     * @param troveItemId Persistent Item identifier pointing to the object stored with Trove that contains the
     *                    appropriate model information to use in the upload process.
     * @return Implementation specific request response object.
     */
    Object uploadModel(Long troveItemId);

    /**
     * Upload a model to a given supplier.
     *
     * @param modelCdnFilepath Name of the file stored on the Content Delivery Network.
     * @return Implementation specific request response object.
     */
    Object uploadModel(String modelCdnFilepath);

    /**
     * Upload a model to a given supplier.
     *
     * @param model Raw model data.
     * @param modelName Model's name.
     * @return Implementation specific request response object.
     */
    Object uploadModel(byte[] model, String modelName);

    /**
     * Get an estimated price based on volume, surface area, and dimensions.
     *
     * @param request Object containing the necessary details for the request.
     * @return List of response objects containing pricing data.
     */
    Object getInstantPrice(PriceRequest request);

    public Money getInstantPrice(String materialId, String finishId, Float volume);

    /**
     * Get an estimated price based on an already-uploaded model.  Uses the persisted Trove item to gather the
     * required information for making this request (e.g. Trove Item model contains the supplier's ModelId to use
     * in making the request).
     *
     * @param troveItemId Persistent Item identifier pointing to the object stored with Trove that contains the
     *                    appropriate model information to use in the model price request.
     * @param quantity Quantity of objects that need pricing.
     * @param scale The model scale.
     * @param currency Currency of the price to be requested.
     * @param shippingAddress
     * @return List of response objects containing pricing data.
     */
    Object getModelBasedPrice(Long troveItemId, int quantity, Float scale, String currency, Address shippingAddress);

    /**
     * Get an estimated price based on an already-uplaoded model.  Uses an iMaterialise model identifier to gather
     * the required information for making this request.
     *
     * @param imaterialiseModelId Unique model identifier supplied by the upload request to iMaterialise.
     * @param quantity Quantity of objects that need pricing.
     * @param scale The model scale.
     * @param currency Currency of the price to be requested.
     * @param shippingAddress Shipping address for this item, if available.
     * @param materialId
     *@param finishId @return List of response objects containing pricing data.
     */
    Object getModelBasedPrice(String imaterialiseModelId, int quantity, Float scale, String currency,
                              Address shippingAddress, String materialId, String finishId);

    /**
     * Adds a model to the pending supplier order.  Uses the persisted Trove item to gather the required information
     * for making this request.
     *
     * @param troveItemId Persistent Item identifier pointing to the object stored with Trove that contains the
     *                    appropriate model information to use in the model price request.
     * @param troveCartId Persistent Cart identifier pointing to the Cart stored with Trove that contains the
     *                    appropriate cart information to use in the cart creation process with a supplier.
     * @return Boolean success state of the operation.
     */
    Boolean addModelToOrder(Long troveItemId, Long troveCartId);

    /**
     * Removes a model from the pending supplier order.  Uses the persisted Trove item to gather the required
     * information for making this request.
     *
     * @param troveUserId Persisted Item identifier pointing to the object stored with Trove that contains the
     *                    appropriate model information.
     * @param troveCartId Persistent Cart identifier pointing to the Cart stored with Trove that contains the
     *                    appropriate cart information to use in the cart creation process with a supplier.
     * @return Boolean success state of the operation.
     */
    Boolean removeModelFromOrder(Long troveUserId, Long troveCartId);

    /**
     * Retrieves the final price of the pending supplier order, including shipping, if applicable.  Uses the persisted
     * Trove item to gather the required information for making this request.
     *
     * @param troveOrderId Persisted Item identifier pointing to the object stored with Trove that contains the
     *                     appropriate model information.
     * @return Status object containing order pricing information.
     */
    Object getFinalOrderPrice(Long troveOrderId);

    /**
     * Submits an order to the supplier.
     *
     * @param troveCartId Supplier's identifier that references the in-progress order on their system.
     * @return Status object containing details about the order.
     */
    Object submitOrder(Long troveCartId);

    /**
     * Cancels a given order.
     *
     * @param supplierOrderId Supplier's identifier that references the in-progress order on their system.
     * @return Status object containing details about the cancellation.
     */
    Object cancelOrder(String supplierOrderId);

    /**
     * Cancels a given order.  Uses the persisted Trove cart to gather the required information for making this
     * request.
     *
     * @param troveCartId Persisted Cart identifier pointing to the Cart stored with Trove that contains
     *                     the appropriate order information.
     * @return Status object containing details about the cancellation.
     */
    Object cancelOrder(Long troveCartId);

    /**
     * Gets the status of an order.
     *
     * @param supplierOrderId Supplier's identifier that references the order on their system.
     * @return Status object containing details about the order.
     */
    Object getOrderStatus(String supplierOrderId);

    /**
     * Gets the status of an order.  Uses the persisted Trove item to gather the required information for making
     * this request.
     *
     * @param troveOrderId Persisted Item identifier pointing to the object stored with Trove that contains the
     *                     appropriate model information.
     * @return Status object containing details about the order.
     */
    OrderStatus getOrderStatus(Long troveOrderId);

    /**
     * Gets a list of order statuses from the supplier.
     *
     * @param supplierOrderId List of supplier order identifiers
     * @return Status object containing details about the order.
     */
    Object getOrderStatuses(List<String> supplierOrderId);


    /**
     * Gets a list of order statuses from the supplier given a list of Trove Order objects.
     *
     * @param troveUserOrders List of Trove Order objects.
     * @return Status object containing details about the orders.
     */
    Object getOrderStatusesByTroveOrder(List<Order> troveUserOrders);

    /**
     * Gets all materials and finishes from the implemented supplier.
     *
     * @return
     */
    List<Material> getSupplierMaterials();

    Object checkUploadStatus(Integer modelId);

    Object submitOrder(String userFirstName, String userLastName, List<ShapewaysItem> items);

}
