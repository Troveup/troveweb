package com.troveup.brooklyn.model;

import com.troveup.brooklyn.orm.materials.model.Finish;
import com.troveup.brooklyn.orm.materials.model.Material;

import java.util.List;

/**
 * Created by tim on 7/15/15.
 */
public class LandingPageMaterialMap
{
    private Material material;
    private Finish finish;
    private List<String> imageUrls;
    private String price;

    public LandingPageMaterialMap(Material material, Finish finish, List<String> imageUrls, String price)
    {
        this.material = material;
        this.finish = finish;
        this.imageUrls = imageUrls;
        this.price = price;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Finish getFinish() {
        return finish;
    }

    public void setFinish(Finish finish) {
        this.finish = finish;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
