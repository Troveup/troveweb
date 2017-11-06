package com.troveup.brooklyn.orm.item.model;

import com.troveup.brooklyn.model.CustomizerWeight;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 8/22/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class ItemCustomization
{
    private Long cartItemModelWeightId;

    //Tracks what set of model weights this is.  If it's set 1, it was inherited from the original Item customization,
    //If it was more than that, then it was tweaked
    private Integer setNumber;
    private String id;
    private String value;
    private String visibleMesh;
    private String size;
    private Date setDate;

    public ItemCustomization(String id, String value)
    {
        setDate = new Date();
        setNumber = 1;
        this.id = id;
        this.value = value;
    }

    public ItemCustomization(String visibleMesh)
    {
        setDate = new Date();
        setNumber = 1;

        this.visibleMesh = visibleMesh;
    }

    public ItemCustomization()
    {
        setDate = new Date();
        setNumber = 1;
    }


    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getCartItemModelWeightId() {
        return cartItemModelWeightId;
    }

    public void setCartItemModelWeightId(Long cartItemModelWeightId) {
        this.cartItemModelWeightId = cartItemModelWeightId;
    }

    @Persistent
    public Integer getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(Integer setNumber) {
        this.setNumber = setNumber;
    }

    @Persistent
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Persistent
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Persistent
    public String getVisibleMesh() {
        return visibleMesh;
    }

    public void setVisibleMesh(String visibleMesh) {
        this.visibleMesh = visibleMesh;
    }

    @Persistent
    public Date getSetDate() {
        return setDate;
    }

    public void setSetDate(Date setDate) {
        this.setDate = setDate;
    }

    @Persistent
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public static List<CustomizerWeight> toCustomizerWeights(List<ItemCustomization> customizations)
    {
        return toCustomizerWeights(customizations, 1);
    }

    public static List<CustomizerWeight> toCustomizerWeights(List<ItemCustomization> customizations, Integer setNumber)
    {
        List<CustomizerWeight> rval = new ArrayList<>();

        for (ItemCustomization customization : customizations)
        {
            if (customization.getId() != null && customization.getId().length() > 0 && customization.getSetNumber().equals(setNumber))
            {
                rval.add(new CustomizerWeight(customization.getId(), customization.getValue()));
            }
        }

        return rval;
    }

    public static List<String> toVisibleMeshes(List<ItemCustomization> customizations)
    {
        return toVisibleMeshes(customizations, 1);
    }

    public static List<String> toVisibleMeshes(List<ItemCustomization> customizations, Integer setNumber)
    {
        List<String> rval = new ArrayList<>();

        for (ItemCustomization customization : customizations)
        {
            if (customization.getVisibleMesh() != null && customization.getVisibleMesh().length() > 0 && customization.getSetNumber().equals(setNumber))
            {
                rval.add(customization.getVisibleMesh());
            }
        }

        return rval;
    }

    public static Integer getLatestSetNumber(List<ItemCustomization> customizations)
    {
        Integer rval = 0;

        if (customizations.size() > 0) {
            List<Integer> availableSetNumbers = new ArrayList<>();

            for (ItemCustomization customization : customizations) {
                if (!availableSetNumbers.contains(customization.getSetNumber())) {
                    availableSetNumbers.add(customization.getSetNumber());
                }
            }

            Collections.sort(availableSetNumbers);

            rval = availableSetNumbers.get(availableSetNumbers.size() - 1);
        }

        return rval;
    }

    public static String toSize(List<ItemCustomization> customizations, Integer setNumber, String defaultSize)
    {
        String rval = defaultSize;

        for (ItemCustomization customization : customizations)
        {
            if (customization.getSize() != null && customization.getSize().length() > 0 && customization.setNumber.equals(setNumber))
            {
                rval = customization.getSize();
            }
        }

        return rval;
    }
}
