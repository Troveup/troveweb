package com.troveup.brooklyn.orm.user.model;

import org.springframework.social.connect.ConnectionData;

import javax.jdo.annotations.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by tim on 4/16/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class SocialConnection implements Serializable
{
    private Long connectionId;

    private User user;

    private String accessToken;

    private String displayName;

    private Long expireTime;

    private String imageUrl;

    private String profileUrl;

    private String providerId;

    private String providerUserId;

    private String refreshToken;

    private String secret;

    private Date created;

    public SocialConnection(){

    }

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    public Long getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(Long connectionId) {
        this.connectionId = connectionId;
    }

    @Persistent
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Persistent
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Persistent
    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }
    @Persistent
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Persistent
    public String getProfileUrl() {
        return profileUrl;
    }


    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    @Persistent
    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    @Persistent
    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    @Persistent
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Persistent
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Persistent
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public static Builder getBuilder()
    {
        return new Builder();
    }

    public static Builder getBuilder(ConnectionData connectionData)
    {
        return new Builder(connectionData);
    }

    public static Builder getBuilder(SocialConnection connection)
    {
        return new Builder(connection);
    }

    @Override
    public boolean equals(Object obj) {
        SocialConnection compareTo = (SocialConnection) obj;

        if (compareTo.connectionId != null && this.connectionId != null)
            return this.connectionId.equals(compareTo.connectionId);
        else if (compareTo.providerId != null && this.providerId != null)
            return this.providerId.equals(compareTo.providerId);
        else if (compareTo.providerUserId != null && this.providerUserId != null)
            return this.providerUserId.equals(compareTo.providerUserId);
        else
            return false;
    }

    public static class Builder
    {
        public Builder(){

        }

        public Builder(ConnectionData connectionData){

            this.accessToken = connectionData.getAccessToken();
            this.displayName = connectionData.getDisplayName();
            this.expireTime = connectionData.getExpireTime();
            this.imageUrl = connectionData.getImageUrl();
            this.profileUrl = connectionData.getProfileUrl();
            this.providerId = connectionData.getProviderId();
            this.providerUserId = connectionData.getProviderUserId();
            this.refreshToken = connectionData.getRefreshToken();
            this.secret = connectionData.getSecret();
            this.created = created;
        }

        public Builder(SocialConnection connection)
        {
            this.connectionId = connection.getConnectionId();
            this.accessToken = connection.getAccessToken();
            this.displayName = connection.getDisplayName();
            this.expireTime = connection.getExpireTime();
            this.imageUrl = connection.getImageUrl();
            this.profileUrl = connection.getProfileUrl();
            this.providerId = connection.getProviderId();
            this.providerUserId = connection.getProviderUserId();
            this.refreshToken = connection.getRefreshToken();
            this.secret = connection.getSecret();
            this.created = created;
        }

        private Long connectionId;

        private String accessToken;

        private String displayName;

        private Long expireTime;

        private String imageUrl;

        private String profileUrl;

        private String providerId;

        private String providerUserId;

        private String refreshToken;

        private String secret;

        private Date created;

        public Builder connectionId(Long connectionId) {
            this.connectionId = connectionId;

            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;

            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;

            return this;
        }

        public Builder expireTime(Long expireTime) {
            this.expireTime = expireTime;

            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;

            return this;
        }

        public Builder profileUrl(String profileUrl) {
            this.profileUrl = profileUrl;

            return this;
        }

        public Builder providerId(String providerId) {
            this.providerId = providerId;

            return this;
        }

        public Builder providerUserId(String providerUserId) {
            this.providerUserId = providerUserId;

            return this;
        }

        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;

            return this;
        }

        public Builder secret(String secret) {
            this.secret = secret;

            return this;
        }

        public Builder created(Date created){
            this.created = created;

            return this;
        }

        public SocialConnection buildSocialConnection()
        {
            SocialConnection connection = new SocialConnection();

            connection.connectionId = this.connectionId;

            connection.accessToken = this.accessToken;

            connection.displayName = this.displayName;

            connection.expireTime = this.expireTime;

            connection.imageUrl = this.imageUrl;

            connection.profileUrl = this.profileUrl;

            connection.providerId = this.providerId;

            connection.providerUserId = this.providerUserId;

            connection.refreshToken = this.refreshToken;

            connection.secret = this.secret;

            connection.created = this.created;

            return connection;
        }

        public ConnectionData buildConnectionData()
        {
            return new ConnectionData(this.providerId, this.providerUserId, this.displayName, this.profileUrl,
                    this.imageUrl, this.accessToken, this.secret, this.refreshToken, this.expireTime);
        }
    }
}
