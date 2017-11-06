package com.troveup.brooklyn.tests.sdk;

import com.easypost.exception.EasyPostException;
import com.google.gson.Gson;
import com.troveup.brooklyn.orm.ftui.model.FtueRequestAddress;
import com.troveup.brooklyn.orm.order.model.EasyPostPostageLabel;
import com.troveup.brooklyn.orm.user.model.Address;
import com.troveup.brooklyn.sdk.mail.business.EasyPostProvider;
import com.troveup.brooklyn.sdk.mail.model.EasyPostTrackingUpdate;
import com.troveup.brooklyn.sdk.mail.model.TroveBoxDimensions;
import com.troveup.config.PersistenceConfig;
import com.troveup.config.SDKConfig;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by tim on 7/4/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, SDKConfig.class})
public class EasyPostTests
{
    @Autowired
    EasyPostProvider easyPostProvider;

    @Test
    public void testFailVerification() throws EasyPostException {
        String easyPostReply =
                easyPostProvider.verifyAddress("Test User", "20 Exchange Pl", null, "New York", "NY", "10005", null);

        Assert.assertFalse(easyPostReply.equals(EasyPostProvider.OK_RESPONSE));
    }

    @Test
    public void testSuccessVerification()
    {
        String easyPostReply = easyPostProvider.verifyAddress("Test User", "20 Exchange Pl", "Apt 1604", "New York", "NY", "10005", null);

        Assert.assertTrue(easyPostReply.equals(EasyPostProvider.OK_RESPONSE));
    }

    @Test
    public void testPurchaseShippingLabel() throws EasyPostException {
        FtueRequestAddress toAddress = new FtueRequestAddress();
        toAddress.setStreet1("20 Exchange Place");
        toAddress.setStreet2("Apt 1604");
        toAddress.setCity("New York");
        toAddress.setState("NY");
        toAddress.setZip("10005");
        toAddress.setName("Tim Growney");

        FtueRequestAddress fromAddress = new FtueRequestAddress();
        fromAddress.setStreet1("20 Exchange Place");
        fromAddress.setStreet2("Apt 1604");
        fromAddress.setCity("New York");
        fromAddress.setState("NY");
        fromAddress.setZip("10005");
        fromAddress.setName("Trove, Inc.");

        EasyPostPostageLabel label = easyPostProvider.purchaseLabel(toAddress, fromAddress, "5555555555", "5555555555",
                TroveBoxDimensions.getTroveBoxDimensions(), 0);

        Address regularFromaddress = Address.getTroveFromAddress();
        Address regularToAddress = Address.getTroveFromAddress();

        EasyPostPostageLabel regularlabel = easyPostProvider.purchaseLabel(regularToAddress, regularFromaddress,
                TroveBoxDimensions.getTroveBoxDimensions(), 0);

        Assert.assertNotNull(label);
        Assert.assertNotNull(regularlabel);
    }

    @Test
    public void testParseWebhookData()
    {
        Gson gson = new Gson();

        String webhookData = "{\"completed_urls\":[],\"created_at\":\"2015-08-26T01:06:35Z\"," +
                "\"description\":\"tracker.updated\",\"mode\":\"test\",\"pending_urls\"" +
                ":[\"https://project-troveup-dev.appspot.com/worker/easypostwebhook\"],\"previous_attributes\":{" +
                "\"status\":\"unknown\"},\"updated_at\":\"2015-08-26T01:06:35Z\",\"result\":{\"id\":" +
                "\"trk_701364759b2046aa859d13b1da1725f7\",\"object\":\"Tracker\",\"mode\":\"test\",\"tracking_code\":" +
                "\"9499907123456123456781\",\"status\":\"in_transit\",\"created_at\":\"2015-08-26T01:05:35Z\"," +
                "\"updated_at\":\"2015-08-26T01:06:35Z\",\"signed_by\":null,\"weight\":null,\"est_delivery_date\":" +
                "\"2014-08-27T00:00:00Z\",\"shipment_id\":\"shp_54bb633b45424eac839614fd9ec6ed58\",\"carrier\":\"USPS\"," +
                "\"tracking_details\":[{\"object\":\"TrackingDetail\",\"message\":\"August 21 Pre-Shipment Info " +
                "Sent to USPS\",\"status\":\"pre_transit\",\"datetime\":\"2015-07-14T17:27:54Z\",\"tracking_location\":" +
                "{\"object\":\"TrackingLocation\",\"city\":null,\"state\":null,\"country\":null,\"zip\":null}}," +
                "{\"object\":\"TrackingDetail\",\"message\":\"August 21 12:37 pm Shipping Label Created in HOUSTON, TX\"," +
                "\"status\":\"pre_transit\",\"datetime\":\"2015-07-15T06:04:54Z\",\"tracking_location\":" +
                "{\"object\":\"TrackingLocation\",\"city\":\"HOUSTON\",\"state\":\"TX\",\"country\":null,\"zip\":" +
                "\"77063\"}},{\"object\":\"TrackingDetail\",\"message\":\"August 21 10:42 pm Arrived at USPS Origin " +
                "Facility in NORTH HOUSTON, TX\",\"status\":\"in_transit\",\"datetime\":\"2015-07-15T16:09:54Z\"," +
                "\"tracking_location\":{\"object\":\"TrackingLocation\",\"city\":\"NORTH HOUSTON\",\"state\":\"TX\"," +
                "\"country\":null,\"zip\":\"77315\"}},{\"object\":\"TrackingDetail\",\"message\":\"August 23 12:18 am" +
                " Arrived at USPS Facility in COLUMBIA, SC\",\"status\":\"in_transit\",\"datetime\":\"2015-07-16T17:45:" +
                "54Z\",\"tracking_location\":{\"object\":\"TrackingLocation\",\"city\":\"COLUMBIA\",\"state\":\"SC\"," +
                "\"country\":null,\"zip\":\"29201\"}},{\"object\":\"TrackingDetail\",\"message\":\"August 23 3:09 am " +
                "Arrived at Post Office in CHARLESTON, SC\",\"status\":\"in_transit\",\"datetime\":\"2015-07-16T20:" +
                "36:54Z\",\"tracking_location\":{\"object\":\"TrackingLocation\",\"city\":\"CHARLESTON\",\"state\":" +
                "\"SC\",\"country\":null,\"zip\":\"29407\"}},{\"object\":\"TrackingDetail\",\"message\":\"August 23 " +
                "8:49 am Sorting Complete in CHARLESTON, SC\",\"status\":\"in_transit\",\"datetime\":\"2015-07-17T02:" +
                "16:54Z\",\"tracking_location\":{\"object\":\"TrackingLocation\",\"city\":\"CHARLESTON\",\"state\":\"SC" +
                "\",\"country\":null,\"zip\":\"29407\"}}],\"carrier_detail\":{\"object\":\"CarrierDetail\",\"service\"" +
                ":null,\"container_type\":null},\"fees\":[]},\"id\":\"evt_1626aac2cf574255949a4fb1eff90518\",\"object\"" +
                ":\"Event\"}";

        EasyPostTrackingUpdate update = gson.fromJson(webhookData, EasyPostTrackingUpdate.class);

        Assert.assertNotNull(update);
        Assert.assertNotNull(update.getResult());
        Assert.assertNotNull(update.getResult().getTracking_code());
        Assert.assertNotNull(update.getResult().getStatus());
    }
}
