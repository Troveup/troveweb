package com.troveup.brooklyn.orm.order.model;

import javax.jdo.annotations.*;

/**
 * Created by tim on 7/27/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class SupplierModelIdAndName
{
    private Long primaryKey;
    private String supplierModelId;
    private String supplierModelName;

    public SupplierModelIdAndName()
    {

    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Long primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Persistent
    public String getSupplierModelId() {
        return supplierModelId;
    }

    public void setSupplierModelId(String supplierModelId) {
        this.supplierModelId = supplierModelId;
    }

    @Persistent
    public String getSupplierModelName() {
        return supplierModelName;
    }

    public void setSupplierModelName(String supplierModelName) {
        this.supplierModelName = supplierModelName;
    }
}
