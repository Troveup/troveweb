package com.troveup.brooklyn.spring.social.model;

import com.troveup.brooklyn.orm.user.model.SocialConnection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.social.security.SocialUser;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tim on 4/13/15.
 */
public class UserDetails extends SocialUser
{
    public enum Role
    {
        ROLE_USER
    }

    private Long id;

    //Implement these as-needed
    /*private List<UserDetails> following;

    private List<UserAttribute> userAttributes;

    private List<ItemDetails> userTrovedItems;

    private List<CollectionDetails> collections;*/


    private String firstName;

    private String lastName;

    private Role role;

    private List<SocialConnection> socialConnections;

    private String email;

    private String thumbnailProfileImage;

    private String profileBannerBucket;

    private String profileBannerFileName;

    private String profileImageBucket;

    private String profileImageFileName;

    private Date created;

    private Date lastModified;

    public UserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, java.util.Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public UserDetails(String username, String password, java.util.Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileBannerBucket() {
        return profileBannerBucket;
    }

    public void setProfileBannerBucket(String profileBannerBucket) {
        this.profileBannerBucket = profileBannerBucket;
    }

    public String getProfileBannerFileName() {
        return profileBannerFileName;
    }

    public void setProfileBannerFileName(String profileBannerFileName) {
        this.profileBannerFileName = profileBannerFileName;
    }

    public String getProfileImageBucket() {
        return profileImageBucket;
    }

    public void setProfileImageBucket(String profileImageBucket) {
        this.profileImageBucket = profileImageBucket;
    }

    public String getProfileImageFileName() {
        return profileImageFileName;
    }

    public void setProfileImageFileName(String profileImageFileName) {
        this.profileImageFileName = profileImageFileName;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public List<SocialConnection> getSocialConnections() {
        return socialConnections;
    }

    public void setSocialConnections(List<SocialConnection> socialConnections) {
        this.socialConnections = socialConnections;
    }

    public String getThumbnailProfileImage() {
        return thumbnailProfileImage;
    }

    public void setThumbnailProfileImage(String thumbnailProfileImage) {
        this.thumbnailProfileImage = thumbnailProfileImage;
    }

    public static Builder getBuilder()
    {
        return new Builder();
    }

    public static class Builder
    {
        private Long id;

        private String username;

        private String firstName;

        private String lastName;

        private String password;

        private String email;

        private String thumbnailProfileImage;

        private Role role;

        private List<SocialConnection> socialConnections;

        private Set<GrantedAuthority> authorities;

        private boolean enabledStatus;

        private boolean accountNonExpired;

        private boolean credentialsNonExpired;

        private boolean accountNonLocked;

        public Builder()
        {
            this.authorities = new HashSet<>();
        }

        public Builder firstName(String firstName)
        {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName)
        {
            this.lastName = lastName;
            return this;
        }

        public Builder id(Long id)
        {
            this.id = id;
            return this;
        }

        public Builder password(String password) {
            if (password == null)
                password = "SocialUser";

            this.password = password;

            return this;
        }

        public Builder accountNonExpired(boolean accountNonExpired)
        {
            this.accountNonExpired = accountNonExpired;

            return this;
        }

        public Builder credentialsNonExpired(boolean credentialsNonExpired)
        {
            this.credentialsNonExpired = credentialsNonExpired;

            return this;
        }

        public Builder accountNonLocked(boolean accountNonLocked)
        {
            this.accountNonLocked = accountNonLocked;
            return this;
        }


        public Builder role(Role role)
        {
            this.role = role;

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.toString());
            this.authorities.add(authority);

            return this;
        }

        public Builder socialConnections(List<SocialConnection> socialSignInProvider)
        {
            this.socialConnections = socialSignInProvider;
            return this;
        }

        public Builder username(String username)
        {
            this.username = username;
            return this;
        }

        public Builder email(String email)
        {
            this.email = email;
            return this;
        }

        public Builder enabledStatus(boolean enabled)
        {
            this.enabledStatus = enabled;

            return this;
        }

        public Builder thumbnailProfileImage(String thumbnailProfileImage)
        {
            this.thumbnailProfileImage = thumbnailProfileImage;

            return this;
        }

        public UserDetails build()
        {
            UserDetails user = new UserDetails(username, password, enabledStatus, accountNonExpired,
                    credentialsNonExpired, accountNonLocked, authorities);

            user.id = id;
            user.email = email;
            user.firstName = firstName;
            user.lastName = lastName;
            user.thumbnailProfileImage = thumbnailProfileImage;
            user.role = role;
            user.socialConnections = socialConnections;

            return user;

        }

    }
}