package com.troveup.brooklyn.sdk.print.shapeways.api;

import com.troveup.brooklyn.orm.materials.model.Finish;
import com.troveup.brooklyn.orm.materials.model.FinishPriceMap;
import com.troveup.brooklyn.orm.materials.model.Material;
import com.troveup.brooklyn.sdk.realityserver.api.RealityServer;
import com.troveup.brooklyn.sdk.realityserver.business.RealityServerManager;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 7/3/15.
 */
public class ShapewaysPriceApi
{

    //Direct mapping, because Shapeways' API doesn't know how to JSON.
    //Numbers are "Material" identifiers on the shapeways side.  Setting these up under a Material - Finish
    //structure.  Gold, Silver, Precious Plated, Brass, Bronze, and Try-On are materials.  Numerically marked items beneath
    //them are finishes, and the "Material" identifiers on the shapeways side are the numerical pieces.
    /*Materials:
    Gold
    18k Gold —98
        14k Gold — 91
        14k Rose Gold — 96
        14k White Gold — 97

    Silver
    Premium — 81

    Precious Plated
    18k Gold Plated — 112
        14k Gold Plated — 110
        14k Rose Gold Plated — 111
    Rhodium Plated — 113

    Brass
    Polished — 85

    Bronze
    Polished — 87*/

    // gold-plated brass, rose gold-plated brass, and rhodium-plated brass
    public List<Material> getShapewaysMaterials()
    {
        List<Material> rval = new ArrayList<>();

        rval.add(new Material("1", "Gold", getFinishes("1"), null, "Shapeways", null, true));
        rval.add(new Material("2", "Silver", getFinishes("2"), null, "Shapeways", null, true));
        rval.add(new Material("3", "Precious Plated", getFinishes("3"), null, "Shapeways", null, true));
        rval.add(new Material("4", "Brass", getFinishes("4"), null, "Shapeways", null, false));
        rval.add(new Material("5", "Bronze", getFinishes("5"), null, "Shapeways", null, true));
        rval.add(new Material(getTryOnMaterialId(), "Try-On", getFinishes(getTryOnMaterialId()), null, "Shapeways", null, true));

        return rval;
    }

    public List<Material> getActiveShapewaysMaterials()
    {
        List<Material> allMaterials = getShapewaysMaterials();
        List<Material> rval = new ArrayList<>();

        for (Material material : allMaterials)
        {
            if (material.isActive())
                rval.add(material);
        }

        return rval;
    }

    public List<Finish> getFinishes(String materialId)
    {
        List<Finish> rval = new ArrayList<>();

        //Arbitrarily assigned number system for materials, as the real identifiers are attached to
        //finishes.

        /**Available reality server mappings:
         * gold_polished
         * platinum_polished
         * white_gold_polished
         * palladium_polished
         * red_gold_polished
         */




        //Gold
        if (materialId.equals("1"))
        {
            rval.add(new Finish("98", "18k Gold", "Shapeways", RealityServerManager.GOLD_POLISHED_MATERIAL_MAPPING, "gold", getFinishPriceMap("98"), "18k", true));
            rval.add(new Finish("91", "14k Gold", "Shapeways", RealityServerManager.GOLD_POLISHED_MATERIAL_MAPPING, "gold", getFinishPriceMap("91"), "14k", true));
            rval.add(new Finish("96", "14k Rose Gold", "Shapeways", RealityServerManager.RED_GOLD_POLISHED_MATERIAL_MAPPING, "rosegold", getFinishPriceMap("96"), "14k", true));
            rval.add(new Finish("97", "14k White Gold", "Shapeways", RealityServerManager.WHITE_GOLD_POLISHED_MATERIAL_MAPPING, "silver", getFinishPriceMap("97"), "14k", true));
        }
        //Silver
        else if (materialId.equals("2"))
        {
            rval.add(new Finish("81", "Premium", "Shapeways", RealityServerManager.PLATINUM_POLISHED_MATERIAL_MAPPING, "silver", getFinishPriceMap("81"), "SLV", true));
        }
        //Precious Plated
        else if(materialId.equals("3"))
        {
            rval.add(new Finish("112", "18k Gold Plated", "Shapeways", RealityServerManager.GOLD_POLISHED_MATERIAL_MAPPING, "gold", getFinishPriceMap("112"), "PL", true));
            rval.add(new Finish("110", "14k Gold Plated", "Shapeways", RealityServerManager.GOLD_POLISHED_MATERIAL_MAPPING, "gold", getFinishPriceMap("110"), "PL", true));
            rval.add(new Finish("111", "14k Rose Gold Plated", "Shapeways", RealityServerManager.RED_GOLD_POLISHED_MATERIAL_MAPPING, "rosegold", getFinishPriceMap("111"), "PL", true));
            rval.add(new Finish("113", "Rhodium Plated", "Shapeways", RealityServerManager.PLATINUM_POLISHED_MATERIAL_MAPPING, "silver", getFinishPriceMap("113"), "PL", true));
        }
        //Brass
        else if (materialId.equals("4"))
        {
            rval.add(new Finish("85", "Polished", "Shapeways", RealityServerManager.GOLD_POLISHED_MATERIAL_MAPPING, "gold", getFinishPriceMap("85"), "BRS",true));
        }
        //Bronze
        else if (materialId.equals("5"))
        {
            rval.add(new Finish("87", "Polished", "Shapeways", RealityServerManager.RED_GOLD_POLISHED_MATERIAL_MAPPING, "rosegold", getFinishPriceMap("87"), "BRZ", true));
        }
        //Try-On
        else if (materialId.equals(getTryOnMaterialId()))
        {
            rval.add(new Finish("6", "White Strong & Flexible", "Shapeways", RealityServerManager.PLATINUM_POLISHED_MATERIAL_MAPPING, "silver", getFinishPriceMap("6"), "TRY", true));
        }

        return rval;
    }

    public List<FinishPriceMap> getFinishPriceMap(String finishId)
    {
        List<FinishPriceMap> rval = new ArrayList<>();

        //18k Gold
        if (finishId.equals("98"))
        {
            rval.add(new FinishPriceMap(false, null, null, new BigDecimal("100.00"), new BigDecimal("800.00")));
        }
        //14k Gold
        else if (finishId.equals("91"))
        {
            rval.add(new FinishPriceMap(false, null, null, new BigDecimal("50.00"), new BigDecimal("600.00")));
        }
        //14k Rose Gold
        else if (finishId.equals("96"))
        {
            rval.add(new FinishPriceMap(false, null, null, new BigDecimal("50.00"), new BigDecimal("600.00")));
        }
        //14k White Gold
        else if (finishId.equals("97"))
        {
            rval.add(new FinishPriceMap(false, null, null, new BigDecimal("50.00"), new BigDecimal("600.00")));
        }
        //Premium Silver
        else if (finishId.equals("81"))
        {
            rval.add(new FinishPriceMap(true, 0f, 1.7f, new BigDecimal("45.00"), new BigDecimal("28.00")));
            rval.add(new FinishPriceMap(true, 1.7f, 3.4f, new BigDecimal("78.00"), new BigDecimal("28.00")));
            rval.add(new FinishPriceMap(true, 3.4f, null, new BigDecimal("0.00"), new BigDecimal("75.00")));
        }
        //18k Gold Plated
        else if (finishId.equals("112"))
        {
            rval.add(new FinishPriceMap(false, null, null, new BigDecimal("30.00"), new BigDecimal("22.00")));
        }
        //14k Gold Plated
        else if (finishId.equals("110"))
        {
            rval.add(new FinishPriceMap(false, null, null, new BigDecimal("30.00"), new BigDecimal("22.00")));
        }
        //14k Rose Gold Plated
        else if (finishId.equals("111"))
        {
            rval.add(new FinishPriceMap(false, null, null, new BigDecimal("30.00"), new BigDecimal("22.00")));
        }
        //Rhodium Plated
        else if (finishId.equals("113"))
        {
            rval.add(new FinishPriceMap(false, null, null, new BigDecimal("30.00"), new BigDecimal("22.00")));
        }
        //Polished Brass
        else if (finishId.equals("85"))
        {
            rval.add(new FinishPriceMap(false, null, null, new BigDecimal("20.00"), new BigDecimal("18.00")));
        }
        //Polished Bronze
        else if (finishId.equals("87"))
        {
            rval.add(new FinishPriceMap(false, null, null, new BigDecimal("20.00"), new BigDecimal("18.00")));
        }
        //Try-On
        else if (finishId.equals("6"))
        {
            //rval.add(new FinishPriceMap(false, null, null, getFlatPrototypePrice(), new BigDecimal("0.00")));

            //Updated from hard coded value to actual pricing scheme so that pricing admin panel may override it
            rval.add(new FinishPriceMap(false, null, null, new BigDecimal("1.50"), new BigDecimal("0.49")));
        }

        return rval;
    }

    public static BigDecimal getFlatPrototypePrice()
    {
        return new BigDecimal(7.49);
    }

    public static String getTryOnMaterialId()
    {
        return "6";
    }

    public static AbstractMap.SimpleEntry<String, String> getPolishedBronzeMaterialFinish()
    {
        return new AbstractMap.SimpleEntry<String, String>("5", "87");
    }
}
