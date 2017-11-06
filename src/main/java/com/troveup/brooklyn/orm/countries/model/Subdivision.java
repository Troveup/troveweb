package com.troveup.brooklyn.orm.countries.model;

/**
 * Created by tim on 5/12/15.
 */

import javax.jdo.annotations.*;

/**
 * Model for containing ISO 3166-2 compliant data country subdivision lists
 */
@PersistenceCapable
public class Subdivision
{
    Long primaryKeyId;
    String code;
    String name;
    String category;
    Country country;

    public Subdivision()
    {

    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getPrimaryKeyId() {
        return primaryKeyId;
    }

    public void setPrimaryKeyId(Long primaryKeyId) {
        this.primaryKeyId = primaryKeyId;
    }

    @Persistent
    @Column(name = "code", allowsNull = "true")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Persistent
    @Column(name = "name", allowsNull = "true")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Persistent
    @Column(name = "category", allowsNull = "true")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Subdivision)
            return ((Subdivision) obj).getPrimaryKeyId().equals(this.getPrimaryKeyId());
        else
            return false;
    }

    public static String getAbbreviatedSubdivisionCode(String subdivision)
    {
        String[] split = subdivision.split("-");
        if (split != null && split.length > 1)
            return split[1];
        else
            return subdivision;
    }
}
