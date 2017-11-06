package com.troveup.brooklyn.tests.orm;

import com.troveup.brooklyn.orm.materials.interfaces.IMaterialFilterAccessor;
import com.troveup.brooklyn.orm.materials.model.Material;
import com.troveup.brooklyn.sdk.print.imaterialise.model.iMatMaterial;
import com.troveup.brooklyn.sdk.print.imaterialise.model.iMatMaterialsResponse;
import com.troveup.brooklyn.sdk.print.interfaces.IPrintSupplier;
import com.troveup.config.PersistenceConfig;
import com.troveup.config.SDKConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 6/15/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, SDKConfig.class})
public class MaterialAccessorTest
{
    @Autowired
    IMaterialFilterAccessor materialFilterAccessor;

    @Autowired
    IPrintSupplier printSupplier;

    @Before
    public void setUp()
    {
        List<Material> filteredMaterials = materialFilterAccessor.getAllMaterials();

        if (filteredMaterials == null || filteredMaterials.size() == 0)
            materialFilterAccessor.persistMaterials(populateFilteredMaterials());
    }

    @Test
    public void emptyTest()
    {
        //empty test so that the countries will be populated in a one-time run for the ORM
    }

    public List<Material> populateFilteredMaterials()
    {
        iMatMaterialsResponse response = (iMatMaterialsResponse) printSupplier.getSupplierMaterials();

        List<Material> rval = new ArrayList<>();

        for (iMatMaterial material : response.getMaterials())
        {
            rval.add(Material.fromiMatMaterial(material));
        }

        return rval;
    }
}
