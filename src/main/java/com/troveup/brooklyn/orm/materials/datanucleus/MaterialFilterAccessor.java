package com.troveup.brooklyn.orm.materials.datanucleus;

import com.troveup.brooklyn.orm.common.datanucleus.ObjectAccessor;
import com.troveup.brooklyn.orm.materials.interfaces.IMaterialFilterAccessor;
import com.troveup.brooklyn.orm.materials.model.Finish;
import com.troveup.brooklyn.orm.materials.model.Material;

import javax.jdo.*;
import java.util.List;

/**
 * Created by tim on 6/15/15.
 */
public class MaterialFilterAccessor extends ObjectAccessor implements IMaterialFilterAccessor
{

    public MaterialFilterAccessor(PersistenceManagerFactory pmfProxy)
    {
        super(pmfProxy);
    }

    @Override
    public Boolean persistMaterials(List<Material> materials)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (materials == null || materials.size() == 0)
                throw new NullPointerException("Arguments cannot be null");

            pm.setDetachAllOnCommit(true);
            rval = pm.makePersistentAll(materials).size() > 0;


        } catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public Boolean persistMaterial(Material material)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Boolean rval = false;

        try
        {
            if (material == null)
                throw new NullPointerException("Argument cannot be null");

            pm.setDetachAllOnCommit(true);
            pm.makePersistent(material);

            rval = true;
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }


        return rval;
    }

    @Override
    public List<Material> getAllMaterials()
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Material> rval = null;

        try
        {
            configureFetchGroups(pm);

            Query query = pm.newQuery(Material.class, "1 == 1");

            rval = (List<Material>) query.execute();

            rval = (List<Material>) pm.detachCopyAll(rval);
        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    @Override
    public List<Material> getAllActiveMaterials()
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        List<Material> rval = null;

        try
        {
            configureFetchGroups(pm);

            Query query = pm.newQuery(Material.class, "active == true");

            rval = (List<Material>) query.execute();

            rval = (List<Material>) pm.detachCopyAll(rval);

        }
        catch (Exception e)
        {
            logError(e);
        }
        finally
        {
            pm.close();
        }

        return rval;
    }

    public void configureFetchGroups(PersistenceManager pm)
    {
        FetchGroup fetchGroup = pm.getFetchGroup(Material.class, "materialFetch");
        List<String> fetchGroupFields = Material.fullMaterialFetchGroupFields();
        for (String field : fetchGroupFields)
        {
            fetchGroup.addMember(field);
        }

        fetchGroup = pm.getFetchGroup(Finish.class, "finishFetch");
        fetchGroupFields = Finish.fullFetchGroupFields();

        for (String field : fetchGroupFields)
        {
            fetchGroup.addMember(field);
        }

        FetchPlan fetchPlan = pm.getFetchPlan();
        fetchPlan.addGroup("materialFetch");
        fetchPlan.addGroup("finishFetch");
        fetchPlan.setMaxFetchDepth(2);
    }
}
