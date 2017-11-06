package com.troveup.brooklyn.orm.user.model;

import com.troveup.brooklyn.orm.user.model.User;

import javax.jdo.annotations.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by tim on 4/20/15.
 * Shamelessly borrowed from http://www.baeldung.com/registration-verify-user-by-email
 */
@PersistenceCapable
@Unique(name="UNIQUETOKEN", members={"token"})
@Cacheable(value = "false")
public class VerificationToken
{
    private static final int EXPIRATION = 60 * 24;

    private Long tokenId;
    private String token;
    private User user;
    private Date expiryDate;
    private boolean verified;
    private boolean valid;

    public VerificationToken(){
        this.expiryDate = calculateExpiryDate(EXPIRATION);
        this.verified = false;
        this.valid = true;
    }

    public VerificationToken(String token)
    {
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
        this.verified = false;
        this.valid = true;
    }

    public VerificationToken(String token, User user)
    {
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
        this.verified = false;
        this.valid = true;
    }

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    @Persistent
    @Column(name = "token", allowsNull = "false")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Persistent
    @Column(name="expiration", allowsNull = "false")
    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Persistent
    @Column(name="verified", allowsNull = "false")
    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Persistent
    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }
}
