package com.troveup.brooklyn.tests.sdk;

/*import com.troveup.brooklyn.sdk.cdn.gcs.GoogleCloudStorage;
import com.troveup.brooklyn.sdk.cdn.model.CloudParam;
import com.troveup.config.PersistenceConfig;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by tim on 5/20/15.
 */
/*@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceConfig.class)*/

/**
 * Won't work until I get stubs for GAE devserver for junit.  Maybe someday.
 */
public class GoogleCloudStorageTests
{
    /*@Test
    public void testCloudUploadFile()
    {
        String testFile = "Some test text";
        GoogleCloudStorage storage = new GoogleCloudStorage();

        List<CloudParam> params = new ArrayList<>();
        CloudParam param = new CloudParam();
        param.setParamName(GoogleCloudStorage.KEY_BUCKET_NAME);
        param.setParamValue("trove-qa-teststore");

        params.add(param);
        Assert.assertTrue(storage.writeFile(testFile, UUID.randomUUID().toString() + ".txt", params));
    }*/
}
