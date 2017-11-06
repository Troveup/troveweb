package com.troveup.brooklyn.util;

import com.troveup.brooklyn.orm.item.model.Item;
import com.troveup.brooklyn.orm.item.model.PriceFilter;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by tim on 6/15/15.
 */
public class MoneyUtil
{
    //Flat fee for packaging
    public static String MARKUP_PACKAGING = "3.15";

    //Total amount being charged for shipping
    public static String MARKUP_SHIPPING = "4.00";

    //Total amount being charged for the prototype
    public static String MARKUP_PROTOTYPE = "7.50";

    //Overall percentage markup
    public static Double MARKUP_PERCENTAGE = 1.65;

    public static Money bigDecimalToMoney(String currency, BigDecimal value)
    {
        CurrencyUnit currencyUnit = CurrencyUnit.USD;
        if (currency != null) {
            if (currency.toLowerCase().equals("aud")) {
                currencyUnit = CurrencyUnit.AUD;
            } else if (currency.toLowerCase().equals("cad")) {
                currencyUnit = CurrencyUnit.CAD;
            } else if (currency.toLowerCase().equals("chf")) {
                currencyUnit = CurrencyUnit.CHF;
            } else if (currency.toLowerCase().equals("eur")) {
                currencyUnit = CurrencyUnit.EUR;
            } else if (currency.toLowerCase().equals("gbp")) {
                currencyUnit = CurrencyUnit.GBP;
            } else if (currency.toLowerCase().equals("JPY")) {
                currencyUnit = CurrencyUnit.JPY;
            }
        }

        return Money.of(currencyUnit, toProperScale(value, currency));
    }

    public static BigDecimal floatToBigDecimal(Float floatVal, String currencyUnit)
    {
        BigDecimal rval = new BigDecimal(String.valueOf(floatVal));
        //TODO:  When we start going to more than the euro or usd, modify this so that the values scale properly
        if (currencyUnit != null)
        {
            if (currencyUnit.toLowerCase().equals("eur") || currencyUnit.toLowerCase().equals("usd"))
            {
                return rval.setScale(2, RoundingMode.HALF_EVEN);
            }
            else
            {
                return rval.setScale(2, RoundingMode.HALF_EVEN);
            }
        }
        else
        {
            rval = rval.setScale(2, RoundingMode.HALF_EVEN);
        }


        return rval;
    }

    public static BigDecimal toProperScale(BigDecimal valueToScaleProperly, String currencyUnit)
    {
        BigDecimal rval = Money.zero(CurrencyUnit.USD).getAmount();
        //TODO:  When we start going to more than the euro or usd, modify this so that the values scale properly
        if (valueToScaleProperly != null) {
            if (currencyUnit != null) {
                if (currencyUnit.toLowerCase().equals("eur") || currencyUnit.toLowerCase().equals("usd")) {
                    rval = valueToScaleProperly.setScale(2, RoundingMode.HALF_EVEN);
                } else {
                    rval = valueToScaleProperly.setScale(2, RoundingMode.HALF_EVEN);
                }
            } else {
                rval = valueToScaleProperly.setScale(2, RoundingMode.HALF_EVEN);
            }
        }

        return rval;
    }

    public static CurrencyUnit toCurrencyUnit(String currencyUnitString)
    {
        CurrencyUnit currencyUnit = CurrencyUnit.USD;
        if (currencyUnitString.toLowerCase().equals("aud"))
        {
            currencyUnit = CurrencyUnit.AUD;
        }
        else if (currencyUnitString.toLowerCase().equals("cad"))
        {
            currencyUnit = CurrencyUnit.CAD;
        }
        else if (currencyUnitString.toLowerCase().equals("chf"))
        {
            currencyUnit = CurrencyUnit.CHF;
        }
        else if (currencyUnitString.toLowerCase().equals("eur"))
        {
            currencyUnit = CurrencyUnit.EUR;
        }
        else if (currencyUnitString.toLowerCase().equals("gbp"))
        {
            currencyUnit = CurrencyUnit.GBP;
        }
        else if (currencyUnitString.toLowerCase().equals("JPY"))
        {
            currencyUnit = CurrencyUnit.JPY;
        }

        return currencyUnit;
    }

    public static Money applyMarkup(Money manufacturerPrice, String currencyUnit, Item item)
    {
        if (currencyUnit == null)
            currencyUnit = "USD";

        //Flat fee for packaging
        Money packaging = Money.parse(currencyUnit + " " + MARKUP_PACKAGING);

        //Total amount being charged for shipping
        Money shippingTotal = Money.parse(currencyUnit + " " + MARKUP_SHIPPING);

        //Total amount being charged for the prototype
        Money prototypeTotal = Money.parse(currencyUnit + " " + MARKUP_PROTOTYPE);

        double percentageMarkup = MARKUP_PERCENTAGE;

        if (item != null)
        {
            if (item.getPackaging() != null)
                packaging = MoneyUtil.bigDecimalToMoney(null, item.getPackaging());

            if (item.getShippingTotal() != null)
                shippingTotal = MoneyUtil.bigDecimalToMoney(null, item.getShippingTotal());

            if (item.getPrototypeTotal() != null)
                prototypeTotal = MoneyUtil.bigDecimalToMoney(null, item.getPrototypeTotal());

            if (item.getPercentageMarkup() != null)
                percentageMarkup = item.getPercentageMarkup().floatValue();
        }

        //Flat fee for iMaterialise services
        //Money printServices = Money.parse(currencyUnit + " 2.50");

        //Formula is ((3.15 + Manufacturing Cost) * 1.65) + 4.00 + 7.50
        manufacturerPrice = manufacturerPrice.plus(packaging);
        //manufacturerPrice = manufacturerPrice.plus(printServices);
        manufacturerPrice = manufacturerPrice.multipliedBy(percentageMarkup, RoundingMode.UP);
        manufacturerPrice = manufacturerPrice.plus(shippingTotal);
        manufacturerPrice = manufacturerPrice.plus(prototypeTotal);

        return manufacturerPrice;
    }

    public static Money applyMarkup(Money manufacturerPrice, String currencyUnit, PriceFilter priceFilter)
    {
        Boolean bypassPricing = false;

        if (currencyUnit == null)
            currencyUnit = "USD";

        //Flat fee for packaging
        Money packaging = Money.parse(currencyUnit + " " + MARKUP_PACKAGING);

        //Total amount being charged for shipping
        Money shippingTotal = Money.parse(currencyUnit + " " + MARKUP_SHIPPING);

        //Total amount being charged for the prototype
        Money prototypeTotal = Money.parse(currencyUnit + " " + MARKUP_PROTOTYPE);

        double percentageMarkup = MARKUP_PERCENTAGE;

        //If a price filter was supplied, apply it accordingly
        if (priceFilter != null)
        {
            //If there is a simple flat price for this item, apply it
            if (priceFilter.getFlatPrice() != null)
            {
                manufacturerPrice = MoneyUtil.bigDecimalToMoney(currencyUnit, priceFilter.getFlatPrice());
                bypassPricing = true;
            }
            //Otherwise, apply any different markups applied by the price filter
            else
            {
                if (priceFilter.getPackagingMarkup() != null)
                {
                    packaging = MoneyUtil.bigDecimalToMoney(currencyUnit, priceFilter.getPackagingMarkup());
                }

                if (priceFilter.getShippingMarkup() != null)
                {
                    shippingTotal = MoneyUtil.bigDecimalToMoney(currencyUnit, priceFilter.getShippingMarkup());
                }

                if (priceFilter.getPrototypeMarkup() != null)
                {
                    prototypeTotal = MoneyUtil.bigDecimalToMoney(currencyUnit, priceFilter.getPrototypeMarkup());
                }

                if (priceFilter.getMarkupPercentage() != null)
                {
                    percentageMarkup = priceFilter.getMarkupPercentage().floatValue();
                }
            }
        }

        if (!bypassPricing)
        {
            //Flat fee for iMaterialise services
            //Money printServices = Money.parse(currencyUnit + " 2.50");

            //Formula is ((3.15 + Manufacturing Cost) * 1.65) + 4.00 + 7.50
            manufacturerPrice = manufacturerPrice.plus(packaging);
            //manufacturerPrice = manufacturerPrice.plus(printServices);
            manufacturerPrice = manufacturerPrice.multipliedBy(percentageMarkup, RoundingMode.UP);
            manufacturerPrice = manufacturerPrice.plus(shippingTotal);
            manufacturerPrice = manufacturerPrice.plus(prototypeTotal);
        }

        return manufacturerPrice;
    }
}
