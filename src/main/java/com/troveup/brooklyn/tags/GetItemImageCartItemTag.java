package com.troveup.brooklyn.tags;

import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.item.model.ItemImage;
import com.troveup.brooklyn.orm.materials.model.Finish;
import com.troveup.brooklyn.orm.materials.model.Material;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tim on 6/28/15.
 */
public class GetItemImageCartItemTag extends SimpleTagSupport
{
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private Item item;
    private int itemNumber;
    private String selectedMaterialId;
    private String selectedFinishId;
    private List<Material> materials;

    public void setItem(Item item) {
        this.item = item;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }

    public void setSelectedMaterialId(String selectedMaterialId) {
        this.selectedMaterialId = selectedMaterialId;
    }

    public void setSelectedFinishId(String selectedFinishId) {
        this.selectedFinishId = selectedFinishId;
    }

    @Override
    public void doTag() throws JspException, IOException
    {
        Material itemMaterial = null;
        Finish itemFinish = null;
        if (selectedMaterialId != null)
            itemMaterial = Material.getMaterialByMaterialId(selectedMaterialId, materials);

        if (selectedFinishId != null && itemMaterial != null)
            itemFinish = Finish.getFinishByFinishId(selectedFinishId, itemMaterial.getFinishList());

        String realityMaterialMapping = null;

        if (itemFinish != null && itemFinish.getRealityServerMapping() != null)
            realityMaterialMapping = itemFinish.getRealityServerMapping();
        else if (itemMaterial != null)
            realityMaterialMapping = itemMaterial.getRealityServerMapping();


        List<ItemImage> images = item.getImages();
        try {
            if (images != null) {
                Collections.sort(images);

                List<ItemImage> imagesOfPertinentMaterial = new ArrayList<>();
                if (realityMaterialMapping != null)
                {
                    for (ItemImage image : images)
                    {
                        if (image.getMaterial() != null && image.getMaterial().equals(realityMaterialMapping)) {
                            imagesOfPertinentMaterial.add(image);
                        }
                    }
                }

                if (imagesOfPertinentMaterial.size() > 0)
                    getJspContext().getOut().write(imagesOfPertinentMaterial.get(itemNumber).getLargeImageUrlPath());
                else {
                    getJspContext().getOut().write(images.get(itemNumber).getLargeImageUrlPath());
                }
            }
        } catch (Exception e)
        {
            logger.error("Encountered an item without any images attached.  Safely placing an image in its place, but" +
                    "please get it fixed!  Item ID: " + item.getItemId());
            getJspContext().getOut().write("https://storage.googleapis.com/troveup-imagestore/default-image-icon-1.jpg");
        }
    }
}
