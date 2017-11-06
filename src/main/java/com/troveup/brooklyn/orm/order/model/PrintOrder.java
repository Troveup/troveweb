package com.troveup.brooklyn.orm.order.model;

import com.troveup.brooklyn.orm.cart.model.CartItem;
import com.troveup.brooklyn.orm.ftui.model.FtueCheckout;
import com.troveup.brooklyn.orm.ftui.model.FtuePersistedRecord;
import com.troveup.brooklyn.util.DateUtils;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 7/26/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class PrintOrder
{
    //Amount of delay time between order status update requests to the manufacturer
    public static int UPDATE_HOUR_DELAY = 24;

    public enum PRINT_SUPPLIER
    {
        SHAPEWAYS,
        IMATERIALISE
    }

    public enum ORDER_SYSTEM
    {
        FTUE,
        CART,
        FTUE_PROTOTYPE
    }

    public enum ORDER_STATUS
    {
        ON_HOLD,
        UPLOADING,
        UPLOADED,
        SUBMITTED,
        PROCESSING,
        IN_PRODUCTION,
        CANCELLED,
        CANCELLATION_ERROR,
        COMPLETE,
        ERROR
    }

    private long printOrderPrimaryKeyId;
    private PRINT_SUPPLIER printSupplier;
    private ORDER_SYSTEM orderSystem;
    private ORDER_STATUS status;
    private String manufacturerStatus;

    private Order orderReference;
    private CartItem cartItemReference;
    private FtueCheckout checkoutReference;
    private FtuePersistedRecord persistedRecordReference;
    private List<EasyPostPostageLabel> printLabels;
    private Date orderDate;
    private Date lastStatusUpdateRequest;
    private Date nextStatusUpdateRequest;
    private String manufacturerOrderId;
    private String modelUrl;
    private Integer orderSubmitAttemptCount;
    private Boolean isPrototype;

    private List<SupplierModelIdAndName> supplierModelIdAndNames;

    public PrintOrder()
    {
        orderDate = new Date();
        lastStatusUpdateRequest = new Date();
        nextStatusUpdateRequest = DateUtils.getDateXHoursFrom(new Date(), UPDATE_HOUR_DELAY);
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public long getPrintOrderPrimaryKeyId() {
        return printOrderPrimaryKeyId;
    }

    public void setPrintOrderPrimaryKeyId(long printOrderPrimaryKeyId) {
        this.printOrderPrimaryKeyId = printOrderPrimaryKeyId;
    }

    @Persistent
    public PRINT_SUPPLIER getPrintSupplier() {
        return printSupplier;
    }

    public void setPrintSupplier(PRINT_SUPPLIER printSupplier) {
        this.printSupplier = printSupplier;
    }

    @Persistent
    public ORDER_SYSTEM getOrderSystem() {
        return orderSystem;
    }

    public void setOrderSystem(ORDER_SYSTEM orderSystem) {
        this.orderSystem = orderSystem;
    }

    @Persistent
    public ORDER_STATUS getStatus() {
        return status;
    }

    public void setStatus(ORDER_STATUS status) {
        this.status = status;
    }

    @Persistent
    public Order getOrderReference() {
        return orderReference;
    }

    public void setOrderReference(Order orderReference) {
        this.orderReference = orderReference;
    }

    @Persistent
    public FtueCheckout getCheckoutReference() {
        return checkoutReference;
    }

    public void setCheckoutReference(FtueCheckout checkoutReference) {
        this.checkoutReference = checkoutReference;
    }

    @Persistent
    public List<SupplierModelIdAndName> getSupplierModelIdAndNames() {
        return supplierModelIdAndNames;
    }

    public void setSupplierModelIdAndNames(List<SupplierModelIdAndName> supplierModelIdAndNames) {
        this.supplierModelIdAndNames = supplierModelIdAndNames;
    }

    @Persistent
    public String getManufacturerStatus() {
        return manufacturerStatus;
    }

    public void setManufacturerStatus(String manufacturerStatus) {
        this.manufacturerStatus = manufacturerStatus;
    }

    @Persistent
    public FtuePersistedRecord getPersistedRecordReference() {
        return persistedRecordReference;
    }

    public void setPersistedRecordReference(FtuePersistedRecord persistedRecordReference) {
        this.persistedRecordReference = persistedRecordReference;
    }

    @Persistent
    public List<EasyPostPostageLabel> getPrintLabels() {
        return printLabels;
    }

    @Persistent
    public String getModelUrl() {
        return modelUrl;
    }

    public void setModelUrl(String modelUrl) {
        this.modelUrl = modelUrl;
    }

    public void setPrintLabels(List<EasyPostPostageLabel> printLabels) {
        this.printLabels = printLabels;
    }

    @Persistent
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Persistent
    public CartItem getCartItemReference() {
        return cartItemReference;
    }

    public void setCartItemReference(CartItem cartItemReference) {
        this.cartItemReference = cartItemReference;
    }

    @Persistent
    public Date getLastStatusUpdateRequest() {
        return lastStatusUpdateRequest;
    }

    public void setLastStatusUpdateRequest(Date lastStatusUpdateRequest) {
        this.lastStatusUpdateRequest = lastStatusUpdateRequest;
    }

    @Persistent
    public Date getNextStatusUpdateRequest() {
        return nextStatusUpdateRequest;
    }

    public void setNextStatusUpdateRequest(Date nextStatusUpdateRequest) {
        this.nextStatusUpdateRequest = nextStatusUpdateRequest;
    }

    @Persistent
    public String getManufacturerOrderId() {
        return manufacturerOrderId;
    }

    public void setManufacturerOrderId(String manufacturerOrderId) {
        this.manufacturerOrderId = manufacturerOrderId;
    }

    @Persistent
    public Integer getOrderSubmitAttemptCount() {
        return orderSubmitAttemptCount;
    }

    public void setOrderSubmitAttemptCount(Integer orderSubmitAttemptCount) {
        this.orderSubmitAttemptCount = orderSubmitAttemptCount;
    }

    @Persistent
    public Boolean getIsPrototype() {
        return isPrototype;
    }

    public void setIsPrototype(Boolean isPrototype) {
        this.isPrototype = isPrototype;
    }

    public static List<PrintOrder.ORDER_STATUS> getStatusesThatCanBeCancelled()
    {
        List<PrintOrder.ORDER_STATUS> rval = new ArrayList<>();
        rval.add(ORDER_STATUS.ON_HOLD);
        rval.add(ORDER_STATUS.UPLOADING);
        rval.add(ORDER_STATUS.UPLOADED);

        return rval;
    }

    public static List<String> getPrintOrderFullFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("printOrderPrimaryKeyId");
        rval.add("printSupplier");
        rval.add("orderSystem");
        rval.add("status");
        rval.add("orderReference");
        rval.add("checkoutReference");
        rval.add("supplierModelIdAndNames");
        rval.add("persistedRecordReference");
        rval.add("printLabels");

        return rval;
    }
}
