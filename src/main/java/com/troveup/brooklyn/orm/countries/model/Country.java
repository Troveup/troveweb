package com.troveup.brooklyn.orm.countries.model;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 5/12/15.
 */

/**
 * Model for containing ISO 3166-1 compliant data country lists
 */
@PersistenceCapable
@Unique(name="CUNIQUE", members={"isoNumericCode"})
public class Country
{
    Long primaryKeyId;
    String name;
    String isoAlpha2Code;
    String isoAlpha3Code;
    String isoNumericCode;
    List<Subdivision> subdivisions;
    boolean zipRequired;

    public Country()
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
    @Column(name = "name", allowsNull = "true")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Persistent
    @Column(name = "alpha_2_code", allowsNull = "true")
    public String getIsoAlpha2Code() {
        return isoAlpha2Code;
    }

    public void setIsoAlpha2Code(String isoAlpha2Code) {
        this.isoAlpha2Code = isoAlpha2Code;
    }

    @Persistent
    @Column(name = "alpha_3_code", allowsNull = "true")
    public String getIsoAlpha3Code() {
        return isoAlpha3Code;
    }

    public void setIsoAlpha3Code(String isoAlpha3Code) {
        this.isoAlpha3Code = isoAlpha3Code;
    }

    @Persistent
    @Column(name = "numeric", allowsNull = "true")
    public String getIsoNumericCode() {
        return isoNumericCode;
    }

    public void setIsoNumericCode(String isoNumericCode) {
        this.isoNumericCode = isoNumericCode;
    }

    @Persistent(mappedBy = "country")
    public List<Subdivision> getSubdivisions() {
        return subdivisions;
    }

    public void setSubdivisions(List<Subdivision> subdivisions) {
        this.subdivisions = subdivisions;
    }

    @Persistent
    @Column(name = "zip_required", allowsNull = "false")
    public boolean isZipRequired() {
        return zipRequired;
    }

    public void setZipRequired(boolean zipRequired) {
        this.zipRequired = zipRequired;
    }

    public static List<String> getCountryFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();

        rval.add("subdivisions");

        return rval;
    }

    public Subdivision getSubdivisionByCode(String code)
    {
        Subdivision rval = null;

        for (Subdivision subdivision : getSubdivisions())
        {
            if (code.toUpperCase().equals(subdivision.getCode().toUpperCase().replace("US-", ""))) {
                rval = subdivision;
                break;
            }
        }

        return rval;
    }
}
