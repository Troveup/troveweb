package com.troveup.brooklyn.sdk.mail.business;

import com.easypost.EasyPost;
import com.easypost.exception.EasyPostException;
import com.easypost.model.Address;
import com.easypost.model.Rate;
import com.easypost.model.Shipment;
import com.google.gson.Gson;
import com.troveup.brooklyn.orm.ftui.model.FtueRequestAddress;
import com.troveup.brooklyn.orm.order.model.EasyPostPostageLabel;
import com.troveup.brooklyn.sdk.mail.model.EasyPostError;
import com.troveup.brooklyn.sdk.mail.model.EasyPostResponseError;
import com.troveup.brooklyn.sdk.mail.model.TroveBoxDimensions;

import javax.print.attribute.HashPrintJobAttributeSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tim on 7/4/15.
 */
public class EasyPostProvider
{
    public static final String API_SANDBOX_KEY = "A0BEkL3MUBd7apApD163eg";
    public static final String OK_RESPONSE = "OK";
    private final Gson gson;

    public EasyPostProvider(String apiKey, Gson gson)
    {
        EasyPost.apiKey = apiKey;
        this.gson = gson;

    }

    public String verifyAddress(String name, String street1, String street2, String city, String state, String zip,
                                String email) {

        String rval = OK_RESPONSE;
        Map<String, Object> addressMap = new HashMap<>();
        addressMap.put("name", name);
        addressMap.put("street1", street1);
        addressMap.put("street2", street2);
        addressMap.put("city", city);
        addressMap.put("state", state);
        addressMap.put("zip", zip);

        try {
            Address address = Address.createAndVerify(addressMap);
        }
        catch (EasyPostException e)
        {
            int jsonStartPoint = e.getMessage().indexOf("{");
            String jsonString = e.getMessage().substring(jsonStartPoint);
            EasyPostResponseError error = gson.fromJson(jsonString, EasyPostResponseError.class);
            rval = error.getError().getMessage();
        }

        return rval;
    }

    public EasyPostPostageLabel purchaseLabel(com.troveup.brooklyn.orm.user.model.Address toAddress,
                                              com.troveup.brooklyn.orm.user.model.Address fromAddress,
                                              TroveBoxDimensions boxDimensions, Integer advancePurchaseDayCount) throws EasyPostException
    {
        Map<String, Object> toAddressMap = new HashMap<>();
        Map<String, Object> fromAddressMap = new HashMap<>();
        Map<String, Object> parcelMap = new HashMap<>();
        Map<String, Object> shipmentMap = new HashMap<>();

        //To Address
        toAddressMap.put("street1", toAddress.getAddressLine1());
        if (toAddress.getAddressLine2() != null && toAddress.getAddressLine2().length() > 0)
            toAddressMap.put("street2", toAddress.getAddressLine2());

        toAddressMap.put("city", toAddress.getCity());
        toAddressMap.put("state", toAddress.getSubdivision().getCode().replace("US-", ""));

        toAddressMap.put("zip", toAddress.getPostalCode());
        toAddressMap.put("country", "US");

        if (toAddress.getLastName() != null && toAddress.getLastName().length() > 0)
            toAddressMap.put("name", toAddress.getFirstName() + " " + toAddress.getLastName());
        else
            toAddressMap.put("name", toAddress.getFirstName());

        toAddressMap.put("phone", toAddress.getPhone());

        //From Address
        fromAddressMap.put("street1", fromAddress.getAddressLine1());

        if (fromAddress.getAddressLine2() != null && fromAddress.getAddressLine2().length() > 0)
            fromAddressMap.put("street2", fromAddress.getAddressLine2());

        fromAddressMap.put("city", fromAddress.getCity());
        fromAddressMap.put("state", fromAddress.getSubdivision().getCode().replace("US-", ""));

        fromAddressMap.put("zip", fromAddress.getPostalCode());
        fromAddressMap.put("country", "US");

        String companyName = "";
        if (fromAddress.getFirstName() != null && fromAddress.getFirstName().length() > 0)
        {
            companyName = fromAddress.getFirstName();
        }
        if (fromAddress.getLastName() != null && fromAddress.getLastName().length() > 0)
        {
            companyName += " ";
            companyName += fromAddress.getLastName();
        }

        fromAddressMap.put("company", companyName);
        fromAddressMap.put("phone", fromAddress.getPhone());

        //Inches and ounces
        parcelMap.put("height", boxDimensions.getHeight());
        parcelMap.put("width", boxDimensions.getWidth());
        parcelMap.put("length", boxDimensions.getLength());
        parcelMap.put("weight", boxDimensions.getWeight());

        shipmentMap.put("to_address", toAddressMap);
        shipmentMap.put("from_address", fromAddressMap);
        shipmentMap.put("parcel", parcelMap);

        Shipment shipment = Shipment.create(shipmentMap);

        if (advancePurchaseDayCount != null && advancePurchaseDayCount > 0)
        {
            Map<String, String> optionMap = new HashMap<>();
            optionMap.put("date_advance", advancePurchaseDayCount.toString());
            shipment.setOptions(optionMap);
        }

        List<String> buyCarriers = new ArrayList<>();
        buyCarriers.add("USPS");

        List<String> buyServices = new ArrayList<>();

        for (Rate rate : shipment.getRates())
        {
            if (rate.getCarrier().equals("USPS"))
                buyServices.add(rate.getService());
        }

        shipment = shipment.buy(shipment.lowestRate(buyCarriers, buyServices));

        return EasyPostPostageLabel.fromEasyPostLabel(shipment.getPostageLabel(), shipment.getTrackingCode());
    }

    public EasyPostPostageLabel purchaseLabel(FtueRequestAddress toAddress,
                                              FtueRequestAddress fromAddress,
                                              String toAddressPhoneNumber,
                                              String fromAddressPhoneNumber,
                                              TroveBoxDimensions boxDimensions, Integer advancePurchaseDayCount) throws EasyPostException {
        Map<String, Object> toAddressMap = new HashMap<>();
        Map<String, Object> fromAddressMap = new HashMap<>();
        Map<String, Object> parcelMap = new HashMap<>();
        Map<String, Object> shipmentMap = new HashMap<>();

        //To Address
        toAddressMap.put("street1", toAddress.getStreet1());
        if (toAddress.getStreet2() != null && toAddress.getStreet2().length() > 0)
            toAddressMap.put("street2", toAddress.getStreet2());

        toAddressMap.put("city", toAddress.getCity());
        toAddressMap.put("state", toAddress.getState());

        toAddressMap.put("zip", toAddress.getZip());
        toAddressMap.put("country", "US");
        toAddressMap.put("name", toAddress.getName());
        toAddressMap.put("phone", toAddressPhoneNumber);

        //From Address
        fromAddressMap.put("street1", fromAddress.getStreet1());

        if (fromAddress.getStreet2() != null && fromAddress.getStreet2().length() > 0)
            fromAddressMap.put("street2", fromAddress.getStreet2());

        fromAddressMap.put("city", fromAddress.getCity());
        fromAddressMap.put("state", fromAddress.getState());

        fromAddressMap.put("zip", fromAddress.getZip());
        fromAddressMap.put("country", "US");

        fromAddressMap.put("company", fromAddress.getName());
        fromAddressMap.put("phone", fromAddressPhoneNumber);

        //Inches and ounces
        parcelMap.put("height", boxDimensions.getHeight());
        parcelMap.put("width", boxDimensions.getWidth());
        parcelMap.put("length", boxDimensions.getLength());
        parcelMap.put("weight", boxDimensions.getWeight());

        shipmentMap.put("to_address", toAddressMap);
        shipmentMap.put("from_address", fromAddressMap);
        shipmentMap.put("parcel", parcelMap);

        Shipment shipment = Shipment.create(shipmentMap);

        List<String> buyCarriers = new ArrayList<>();
        buyCarriers.add("USPS");

        List<String> buyServices = new ArrayList<>();

        for (Rate rate : shipment.getRates())
        {
            if (rate.getCarrier().equals("USPS"))
                buyServices.add(rate.getService());
        }

        if (advancePurchaseDayCount != null && advancePurchaseDayCount > 0)
        {
            Map<String, String> optionMap = new HashMap<>();
            optionMap.put("date_advance", advancePurchaseDayCount.toString());
            shipment.setOptions(optionMap);
        }

        shipment = shipment.buy(shipment.lowestRate(buyCarriers, buyServices));

        return EasyPostPostageLabel.fromEasyPostLabel(shipment.getPostageLabel(), shipment.getTrackingCode());
    }


}
