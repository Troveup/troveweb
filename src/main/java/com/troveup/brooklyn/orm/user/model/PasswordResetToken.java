package com.troveup.brooklyn.orm.user.model;

import javax.jdo.annotations.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by tim on 4/20/15.
 */
@Unique(name="UNIQUEPASSTOKEN", members={"token"})
@PersistenceCapable
@Cacheable(value = "false")
public class PasswordResetToken
{
    private static final int EXPIRATION = 60 * 24;

    private Long tokenId;
    private String token;
    private User user;
    private Date expiryDate;
    private boolean valid;

    public PasswordResetToken(){
        this.expiryDate = calculateExpiryDate(EXPIRATION);
        this.valid = true;
    }

    public PasswordResetToken(String token)
    {
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
        this.valid = true;
    }

    public PasswordResetToken(String token, User user)
    {
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
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
