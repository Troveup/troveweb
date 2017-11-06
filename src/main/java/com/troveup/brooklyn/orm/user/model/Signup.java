package com.troveup.brooklyn.orm.user.model;

import javax.jdo.annotations.*;
import java.util.Date;

/**
 * Created by tim on 5/9/16.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class Signup
{
    public enum SIGNUP_TYPE {
        SELLER_LAUNCH_PAGE,
        KIMBERLY_SMITH_SIGNUP,
        FREE_SHIPPING_MODAL
    }

    private Long signupId;
    private String email;
    private SIGNUP_TYPE type;
    private Date date;

    public Signup()
    {
        date = new Date();
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getSignupId() {
        return signupId;
    }

    public void setSignupId(Long signupId) {
        this.signupId = signupId;
    }

    @Persistent
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Persistent
    public SIGNUP_TYPE getType() {
        return type;
    }

    public void setType(SIGNUP_TYPE type) {
        this.type = type;
    }

    @Persistent
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static SIGNUP_TYPE getSignupTypeFromStringType(String type)
    {
        SIGNUP_TYPE rval = null;

        for (SIGNUP_TYPE potentialType : SIGNUP_TYPE.values())
        {
            if (potentialType.name().toUpperCase().equals(type.toUpperCase()))
            {
                rval = potentialType;
            }
        }

        return rval;
    }
}
