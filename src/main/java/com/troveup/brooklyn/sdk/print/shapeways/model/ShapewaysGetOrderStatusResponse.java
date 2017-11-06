package com.troveup.brooklyn.sdk.print.shapeways.model;

import com.troveup.brooklyn.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tim on 7/22/15.
 */
public class ShapewaysGetOrderStatusResponse
{
    private String result;
    private ShapewaysOrderCount ordersCount;
    private List<ShapewaysOrderStatus> ordersStatus;

    public ShapewaysGetOrderStatusResponse()
    {

    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ShapewaysOrderCount getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(ShapewaysOrderCount shapewaysOrderCount) {
        this.ordersCount = shapewaysOrderCount;
    }

    public List<ShapewaysOrderStatus> getOrdersStatus() {
        return ordersStatus;
    }

    public void setOrdersStatus(List<ShapewaysOrderStatus> shapewaysOrderStatus) {
        this.ordersStatus = shapewaysOrderStatus;
    }

    //TODO:  Convert this into a proper deserializer.  See RealityServerPostbackResponseDeserializer.java for examples of how.
    /**
     * This requires some explanation.
     *
     * Shapeways, in their infinite wisdom, returns json strings with an "objects of objects" mentality.  This means
     * that, rather than returning an array of objects with identifier fields on the inside of the object, they return
     * a set of identifier objects that then contain a bunch of data.  For example, rather than returning:
     * {"arrayOfObjects":[{"id":1,"data":"blah"},{"id":2,"data":"blah"}]}, as a standard parseable array,
     * they return this ridiculous, seemingly unparseable thing:
     * {"arrayOfObjects":{"1":{"data":"blah"},"2":{"data":"blah"}}}.  Which cannot be parsed in a logical way by
     * Google's Gson library, nor any other, to my knowledge.
     *
     * It will first go through and identify all of the identifiers that need to be moved.  Then, it will create that
     * identifier within its proper location, and delete the old identifier.  Finally, in true array form, it will
     * replace curly braces ("{}") with brackets ("[]").
     *
     * This method is built to correct this specifically for the ShapewaysGetOrderStatus call.  A more generalized
     * version will be built later, if the need arises.
     *
     * @param shapewaysResponse The raw JSON response from Shapeways.
     * @return The corrected JSON response that should be parseable by any standard library.
     */
    public static String fixShapewaysOrderResponse(String shapewaysResponse)
    {

        /**
         * Sample Shapeways response, around which this method was built.  A few assumptions were made:
         * 1.  Everything that needs to be fixed is between the ordersStatus field and the ordersInfo field.
         * 2.  All of the identifiers that need fixing are surrounded with quotes.  All other numbers are not.
         * 3.  All identifiers are unique
         */
        /*String sampleShapewaysResponse = "{\"result\":\"success\",\"ordersCount\":{\"total\":2,\"placed\":0," +
                "\"in_production\":1,\"cancelled\":1,\"unknown\":0,\"shipped\":0},\"ordersStatus\":{\"1022998\":" +
                "{\"status\":\"cancelled\",\"items\":{\"2137383\":{\"title\":\"Warrior-ring\",\"quantity\":1," +
                "\"status\":{\"processing\":0,\"in_production\":0,\"complete\":0,\"cancelled\":1}}}},\"1023053\":" +
                "{\"status\":\"in_production\",\"items\":{\"2137458\":{\"title\":\"Warrior-ring\",\"quantity\":1," +
                "\"status\":{\"processing\":1,\"in_production\":0,\"complete\":0,\"cancelled\":0}}}}},\"ordersInfo\":" +
                "[{\"orderId\":\"1022998\",\"targetDeliveryDate\":\"2015-08-03 00:00:00\",\"targetShipDate\":" +
                "\"2015-07-30 00:00:00\",\"shipments\":null},{\"orderId\":\"1023053\",\"targetDeliveryDate\":" +
                "\"2015-08-03 00:00:00\",\"targetShipDate\":\"2015-07-30 00:00:00\",\"shipments\":null}]," +
                "\"nextActionSuggestions\":[]}";*/

        //Bound the search to identifying numbers between orderStatus and ordersInfo fields
        int startOfOrderInfo = shapewaysResponse.indexOf("\"ordersStatus\"");
        int endOfOrderInfo = shapewaysResponse.indexOf("\"ordersInfo\"");

        //Identify everything that has a set of quotes around it with a starting point and an ending point
        Map<Integer, Integer> possibleOrderIdentifiers = new HashMap<>();
        for (int i = startOfOrderInfo; i < endOfOrderInfo; ++i)
        {
            if (shapewaysResponse.charAt(i) == '"')
            {
                for (int j = i + 1; j < endOfOrderInfo; j++)
                {
                    if (shapewaysResponse.charAt(j) == '"')
                    {
                        possibleOrderIdentifiers.put(i + 1, j);
                        i = j;
                        break;
                    }
                }
            }
        }

        //Convert these bounds into actual strings
        Map<Integer, String> possibleOrderStrings = new HashMap<>();
        for (Integer beginningQuote : possibleOrderIdentifiers.keySet())
        {
            possibleOrderStrings.put(beginningQuote, shapewaysResponse.substring(beginningQuote,
                    possibleOrderIdentifiers.get(beginningQuote)));
        }

        //Check if any of these strings is actually a number.  If it is, we've found an identifier.
        Map<Integer, String> orderStrings = new HashMap<>();
        for (Integer possibleOrderString : possibleOrderStrings.keySet())
        {
            if (StringUtils.isNumeric(possibleOrderStrings.get(possibleOrderString)))
                orderStrings.put(possibleOrderString, possibleOrderStrings.get(possibleOrderString));
        }

        StringBuilder builder = new StringBuilder(shapewaysResponse);

        //For each of the identifiers, carry out the string manipulation
        for (Integer startPoint : orderStrings.keySet())
        {
            //First, identify the starting point of the identifying word
            int startPointForWord = builder.indexOf(orderStrings.get(startPoint));

            //Formulate there replacement string that goes after the subsequent curly brace
            String newString = "\"id\":" + orderStrings.get(startPoint) + ",";

            //Insert it after the curly brace
            builder.insert(startPointForWord + orderStrings.get(startPoint).length() + 3, newString);

            //The string just changed due to an insertion, find the startpoint again
            startPointForWord = builder.indexOf(orderStrings.get(startPoint));

            //Delete it
            builder.delete(startPointForWord - 1,  startPointForWord +
                    orderStrings.get(startPoint).length() + 2);

            //If this was the beginning of an array, we see a double curly brace.  Replace
            //one of them with an open bracket
            int openBracket = builder.indexOf("{{");
            if (openBracket > -1)
                builder.replace(openBracket, openBracket + 1, "[");

            //If this is the end of an array, we see a quadruple curly brace (this is specific to this return
            //JSON schema).  Replace the last curly brace with a close bracket
            int closeBracket = builder.indexOf("}}}}");
            if (closeBracket > -1)
                builder.replace(closeBracket + 2, closeBracket + 3, "]");
        }

        //After all has been done, there is one final closing bracket to put in place.  Do that.
        int finalCloseBracket = builder.indexOf("}}]}}");
        if (finalCloseBracket > -1)
            builder.replace(finalCloseBracket + 4, finalCloseBracket + 5, "]");

        return builder.toString();
    }
}
