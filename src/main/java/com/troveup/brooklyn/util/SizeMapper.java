package com.troveup.brooklyn.util;

import java.util.*;

/**
 * Created by tim on 6/22/15.
 */
public class SizeMapper
{
    public static final String CATEGORY_BRACELET = "BRACELET";
    public static final String CATEGORY_NECKLACE = "NECKLACE";
    public static final String CATEGORY_RING = "RING";

    private static final String BRACELET_EXTRA_SMALL = "Extra Small";
    private static final String BRACELET_SMALL = "Small";
    private static final String BRACELET_MEDIUM = "Medium";
    private static final String BRACELET_LARGE = "Large";
    private static final String BRACELET_EXTRA_LARGE = "Extra Large";

    public static final String PENDANT_SMALL = "Small";
    public static final String PENDANT_MEDIUM = "Medium";
    public static final String PENDANT_LARGE = "Large";

    public static List<String> getRingSizes()
    {
        List<String> rval = new ArrayList<>();

        for (float i = 4; i < 12.0; i+=.25)
        {
            rval.add(Float.toString(i));
        }

        return rval;
    }

    public static List<String> getBraceletSizes()
    {
        List<String> rval = new ArrayList<>();

        rval.add(BRACELET_EXTRA_SMALL);
        rval.add(BRACELET_SMALL);
        rval.add(BRACELET_MEDIUM);
        rval.add(BRACELET_LARGE);
        rval.add(BRACELET_EXTRA_LARGE);

        return rval;
    }

    public static List<String> getPendantSizes()
    {
        List<String> rval = new ArrayList<>();

        rval.add(PENDANT_SMALL);
        rval.add(PENDANT_MEDIUM);
        rval.add(PENDANT_LARGE);

        return rval;
    }

    public static List<String> getItemSizes(String category)
    {
        List<String> rval;

        if (category.toUpperCase().equals(CATEGORY_BRACELET))
            rval = getBraceletSizes();
        else if (category.toUpperCase().equals(CATEGORY_NECKLACE))
            rval = getPendantSizes();
        else
            rval = getRingSizes();

        return rval;
    }

    public static String getRingSizeExporterValue(String ringSize)
    {
        String rval;

        //Account for new ring size scheme
        if (ringSize.contains("_"))
            rval = ringSize;
        else
            rval = "ring_" + ringSize;

        return rval;
    }

    public static String getBraceletSizeExporterValue(String braceletSize)
    {
        String rval = "bracelet_1";
        String BRACELET_PREFIX = "bracelet_";

        //Size mappings have been changed a few times.  There is some data out on production that uses several formats.
        //This will account for them all.
        List<String> defaultBraceletSizeMappings = getBraceletSizes();
        List<String> customizerMappingBugBraceletSizes = new ArrayList<>();

        for (String mapping : defaultBraceletSizeMappings)
        {
            customizerMappingBugBraceletSizes.add(mapping.toLowerCase().replace(" ", "-"));
        }

        //Default, working case
        if (defaultBraceletSizeMappings.contains(braceletSize))
        {
            if (braceletSize.toLowerCase().equals(BRACELET_EXTRA_SMALL.toLowerCase()))
                rval = BRACELET_PREFIX + "1";
            else if (braceletSize.toLowerCase().equals(BRACELET_SMALL.toLowerCase()))
                rval = BRACELET_PREFIX + "2";
            else if (braceletSize.toLowerCase().equals(BRACELET_MEDIUM.toLowerCase()))
                rval = BRACELET_PREFIX + "3";
            else if (braceletSize.toLowerCase().equals(BRACELET_LARGE.toLowerCase()))
                rval = BRACELET_PREFIX + "4";
            else
                rval = BRACELET_PREFIX + "5";
        }
        //Bug case where the customizer returned sizes in all lowercase with dashes instead of spaces
        else if (customizerMappingBugBraceletSizes.contains(braceletSize))
        {
            if (braceletSize.toLowerCase().replace(" ", "-").equals(BRACELET_EXTRA_SMALL.toLowerCase().replace(" ", "-")))
                rval = BRACELET_PREFIX + "1";
            else if (braceletSize.toLowerCase().replace(" ", "-").equals(BRACELET_SMALL.toLowerCase().replace(" ", "-")))
                rval = BRACELET_PREFIX + "2";
            else if (braceletSize.toLowerCase().replace(" ", "-").equals(BRACELET_MEDIUM.toLowerCase().replace(" ", "-")))
                rval = BRACELET_PREFIX + "3";
            else if (braceletSize.toLowerCase().replace(" ", "-").equals(BRACELET_LARGE.toLowerCase().replace(" ", "-")))
                rval = BRACELET_PREFIX + "4";
            else
                rval = BRACELET_PREFIX + "5";
        }
        //Case where a bracelet size that was already an exporter size was passed in for conversion.  Pass it right back through.
        else if (braceletSize.contains("_"))
            rval = braceletSize;

        return rval;

    }

    public static String getPendantSizeExporterValue(String pendantSize)
    {
        String PENDANT_PREFIX = CATEGORY_NECKLACE.toLowerCase() + "_";
        String rval = PENDANT_PREFIX + "0";

        if (pendantSize != null) {
            if (pendantSize.contains("_"))
                rval = pendantSize;
            else if (pendantSize.toLowerCase().equals(PENDANT_SMALL.toLowerCase()))
                rval = PENDANT_PREFIX + "1";
            else if (pendantSize.toLowerCase().equals(PENDANT_MEDIUM.toLowerCase()))
                rval = PENDANT_PREFIX + "2";
            else if (pendantSize.toLowerCase().equals(PENDANT_LARGE.toLowerCase()))
                rval = PENDANT_PREFIX + "3";
        }

        return rval;
    }

    public static String getStandardizedSize(String size)
    {
        String rval;

        if (size != null) {
            if (size.contains("_")) {
                String[] split = size.split("_");

                //Assume ring, then override if it's not
                rval = split[1];

                if (split[0].toLowerCase().contains(CATEGORY_BRACELET.toLowerCase())) {
                    if (split[1].equals("1"))
                        rval = BRACELET_EXTRA_SMALL;
                    else if (split[1].equals("2"))
                        rval = BRACELET_SMALL;
                    else if (split[1].equals("3"))
                        rval = BRACELET_MEDIUM;
                    else if (split[1].equals("4"))
                        rval = BRACELET_LARGE;
                    else if (split[1].equals("5"))
                        rval = BRACELET_EXTRA_LARGE;
                } else if (split[0].toLowerCase().contains(CATEGORY_NECKLACE.toLowerCase())) {
                    if (split[1].equals("1"))
                        rval = PENDANT_SMALL;
                    else if (split[1].equals("2"))
                        rval = PENDANT_MEDIUM;
                    else if (split[1].equals("3"))
                        rval = PENDANT_LARGE;
                }
            } else
                rval = size;
        }
        //Special case where the size didn't exist.  Put filler text in to be sure that it doesn't crash.
        else
        {
            rval = "No Size Available";
        }

        return rval;
    }

    public static String getExporterSize(String category, String size)
    {
        if (category.toLowerCase().equals(CATEGORY_BRACELET.toLowerCase()))
            return getBraceletSizeExporterValue(size);
        else if (category.toLowerCase().equals(CATEGORY_NECKLACE.toLowerCase()))
            return getPendantSizeExporterValue(size);
        else
            return getRingSizeExporterValue(size);
    }

    public static String getMediumExportSize(String category)
    {
        if (category.toLowerCase().equals(CATEGORY_BRACELET.toLowerCase()))
            return getBraceletSizeExporterValue(BRACELET_MEDIUM);
        else if (category.toLowerCase().equals(CATEGORY_NECKLACE.toLowerCase()))
            return getPendantSizeExporterValue(PENDANT_MEDIUM);
        else
            return getRingSizeExporterValue("7.0");
    }

    public static List<String> getAvailableCategories()
    {
        List<String> rval = new ArrayList<>();

        rval.add(CATEGORY_BRACELET);
        rval.add(CATEGORY_RING);
        rval.add(CATEGORY_NECKLACE);

        return rval;
    }

    public static Map<String, String> getAvailableSizesMap(String category)
    {
        Map<String, String> rval = new LinkedHashMap<>();

        String prefix = category.toLowerCase() + "_";

        List<String> sizes;

        if (category.equals(CATEGORY_RING))
        {
            sizes = getRingSizes();

            for (String size : sizes)
            {
                rval.put(prefix + size, size);
            }
        }
        else if (category.equals(CATEGORY_BRACELET))
        {
            sizes = getBraceletSizes();

            for (int i = 0; i < sizes.size(); ++i)
            {
                rval.put(prefix + (i + 1), sizes.get(i));
            }
        }
        else if (category.equals(CATEGORY_NECKLACE))
        {
            sizes = getPendantSizes();
            for (int i = 0; i < sizes.size(); ++i)
            {
                rval.put(prefix + (i + 1), sizes.get(i));
            }
        }

        return rval;
    }
}
