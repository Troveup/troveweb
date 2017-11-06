package com.troveup.brooklyn.model;

/**
 * Created by tim on 6/19/15.
 */
public class UserProfileImageUpdateResponse
{
    private String profileImageFullUrl;
    private String profileImageThumbnailUrl;

    public UserProfileImageUpdateResponse()
    {

    }

    public String getProfileImageFullUrl() {
        return profileImageFullUrl;
    }

    public void setProfileImageFullUrl(String profileImageFullUrl) {
        this.profileImageFullUrl = profileImageFullUrl;
    }

    public String getProfileImageThumbnailUrl() {
        return profileImageThumbnailUrl;
    }

    public void setProfileImageThumbnailUrl(String profileImageThumbnailUrl) {
        this.profileImageThumbnailUrl = profileImageThumbnailUrl;
    }
}
