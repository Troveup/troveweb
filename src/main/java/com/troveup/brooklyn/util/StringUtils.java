package com.troveup.brooklyn.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * Created by tim on 4/20/15.
 */
public class StringUtils
{
    public static String join(List<String> stringCollection, String separator)
    {
        String rval;
        if (stringCollection != null && stringCollection.size() > 0) {
            rval = stringCollection.get(0);

            for (String string : stringCollection) {
                rval += separator;
                rval += string;
            }
        }
        else
            throw new IllegalArgumentException("Collection cannot be empty!");

        return rval;
    }

    public static String converStreamToString(InputStream is) throws IOException
    {
        String rval = "";

        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedInputStreamReader = new BufferedReader(reader);
        String readline = bufferedInputStreamReader.readLine();

        while(readline != null)
        {
            rval += readline;
            readline = bufferedInputStreamReader.readLine();
        }

        return rval;
    }

    public static Boolean isNumeric(String possibleNumber)
    {
        try
        {
            Long parsedNumber = Long.parseLong(possibleNumber);
        }
        catch (NumberFormatException e)
        {
            return false;
        }

        return true;
    }

    public static String generateRandomUppercaseString(Integer stringLength)
    {
        return UUID.randomUUID().toString().toUpperCase().replace("-", "").substring(0, stringLength);
    }

    public static Boolean isNullOrEmpty(String string)
    {
        return string == null || string.length() == 0;
    }

    public static String surroundWithQuotes(String string)
    {
        return "\"" + string + "\"";
    }
    public static String escapeStringQuotes(String string)
    {
        return string.replace("\"", "\\\"");
    }
}
