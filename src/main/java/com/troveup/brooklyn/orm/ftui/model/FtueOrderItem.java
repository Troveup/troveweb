package com.troveup.brooklyn.orm.ftui.model;

import javax.jdo.annotations.*;

/**
 * Created by tim on 6/16/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class FtueOrderItem
{
    private Long ftueOrderItemId;
    private Long ftueResponseItemId;
    private int material;
    private int quantity;
    private String file_url;
    private String units;

    public FtueOrderItem()
    {

    }

    public FtueOrderItem(int material, int quantity, String file_url, String units)
    {
        this.material = material;
        this.quantity = quantity;
        this.file_url = file_url;
        this.units = units;
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getFtueOrderItemId() {
        return ftueOrderItemId;
    }

    public void setFtueOrderItemId(Long ftueOrderItemId) {
        this.ftueOrderItemId = ftueOrderItemId;
    }

    @Persistent
    @Column(name = "response_item_id", allowsNull = "true")
    public Long getFtueResponseItemId() {
        return ftueResponseItemId;
    }

    public void setFtueResponseItemId(Long ftueResponseItemId) {
        this.ftueResponseItemId = ftueResponseItemId;
    }

    @Persistent
    @Column(name = "material_id", allowsNull = "true")
    public int getMaterial() {
        return material;
    }

    public void setMaterial(int material) {
        this.material = material;
    }

    @Persistent
    @Column(name = "quantity", allowsNull = "true")
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Persistent
    @Column(name = "file_url", allowsNull = "true")
    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    @Persistent
    @Column(name = "units", allowsNull = "true")
    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
