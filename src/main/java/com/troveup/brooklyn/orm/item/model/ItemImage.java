package com.troveup.brooklyn.orm.item.model;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 4/24/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class ItemImage implements Comparable<ItemImage>
{
    private Long imageId;

    private String largeImageUrlPath;
    private String mediumImageUrlPath;
    private String smallImageUrlPath;
    private String imageName;
    private String material;
    private Item ownerItem;
    private Integer imageHash;
    private boolean customizerImage;
    private int order;

    public ItemImage()
    {

    }

    public ItemImage(ItemImage image)
    {
        this.largeImageUrlPath = image.getLargeImageUrlPath();
        this.mediumImageUrlPath = image.getMediumImageUrlPath();
        this.smallImageUrlPath = image.getSmallImageUrlPath();
        this.imageName = image.getImageName();
        this.material = image.getMaterial();
        this.ownerItem = image.getOwnerItem();
        this.imageHash = image.getImageHash();
        this.imageHash = image.getImageHash();
        this.customizerImage = image.customizerImage;
        this.order = image.getOrder();
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    @Persistent
    public String getLargeImageUrlPath() {
        return largeImageUrlPath;
    }

    public void setLargeImageUrlPath(String largeImageUrlPath) {
        this.largeImageUrlPath = largeImageUrlPath;
    }

    @Persistent
    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Item getOwnerItem() {
        return ownerItem;
    }

    public void setOwnerItem(Item ownerItem) {
        this.ownerItem = ownerItem;
    }

    /**
     * Gets the localized order of this image. (e.g. Should it show up first, second, or third in the list of images
     * that are returned to the view with reference to the object that owns it).
     * @return int image order.
     */
    @Persistent
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Persistent
    public String getMediumImageUrlPath() {
        return mediumImageUrlPath;
    }

    public void setMediumImageUrlPath(String mediumImageUrlPath) {
        this.mediumImageUrlPath = mediumImageUrlPath;
    }

    @Persistent
    public String getSmallImageUrlPath() {
        return smallImageUrlPath;
    }

    public void setSmallImageUrlPath(String smallImageUrlPath) {
        this.smallImageUrlPath = smallImageUrlPath;
    }

    @Persistent
    public boolean isCustomizerImage() {
        return customizerImage;
    }

    @Persistent
    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setCustomizerImage(boolean customizerImage) {
        this.customizerImage = customizerImage;
    }

    @Persistent
    public Integer getImageHash() {
        return imageHash;
    }

    public void setImageHash(Integer imageHash) {
        this.imageHash = imageHash;
    }

    @Override
    public int compareTo(ItemImage o) {
        return this.order - o.order;
    }

    public static List<ItemImage> getItemImageListSubsetByMaterial(List<ItemImage> fullListOfImages, String material)
    {
        List<ItemImage> rval = new ArrayList<>();

        for (ItemImage image : fullListOfImages)
        {
            if (image.getMaterial().equals(material))
            {
                rval.add(image);
            }
        }

        return rval;
    }

}
