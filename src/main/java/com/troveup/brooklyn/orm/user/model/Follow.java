package com.troveup.brooklyn.orm.user.model;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 8/6/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class Follow
{
    //Object that represents the action of a user following another user or collection.  The "userFollowOwner" is the
    //user that is doing the following.  The userFollower is the user being followed.

    //I must apologize in advance, I accidentally reversed the logic without thinking, and now it's all built and
    //we're just about to launch, so it'll change if time ever permits (hah).  In the case of following a collection, the collection
    //owns all of the follows, and the userFollower field becomes the field that represents the user that is following
    //that collection.  So, if you wanted to know who all followed a collection, you get the List of Follows objects
    //from that collection, and analyze their userFollower fields.  If you wanted to see who a given user followed,
    //You get the List of all of their Follow objects, then analyze the userFollower field for all of the people that
    //the originating user followed.

    private Long followId;
    private Date followedDate;


    //User being followed, in the context of user-on-user, otherwise the user who is following a given collection
    private User userFollower;

    //User doing the following, (object representing the action of the person who clicked "follow")
    //Associated in the List of the owner's "followedUsers" field
    private User userFollowOwner;

    //Collection being followed, logic reverses here
    //The collection becomes the follow owner, and the userFollower is the user doing the following
    private Collection collectionFollowOwner;

    public Follow()
    {

    }

    public Follow(User followOwner, User userFollower)
    {
        this.userFollowOwner = followOwner;
        this.userFollower = userFollower;
        followedDate = new Date();
    }

    public Follow(Collection followOwner, User userFollower)
    {
        this.collectionFollowOwner = followOwner;
        this.userFollower = userFollower;
        followedDate = new Date();
    }

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    public Long getFollowId() {
        return followId;
    }

    public void setFollowId(Long followId) {
        this.followId = followId;
    }

    @Persistent
    public Date getFollowedDate() {
        return followedDate;
    }

    public void setFollowedDate(Date followedDate) {
        this.followedDate = followedDate;
    }

    @Persistent
    public User getUserFollower() {
        return userFollower;
    }

    public void setUserFollower(User userFollower) {
        this.userFollower = userFollower;
    }

    @Persistent
    public User getUserFollowOwner() {
        return userFollowOwner;
    }

    public void setUserFollowOwner(User userFollowOwner) {
        this.userFollowOwner = userFollowOwner;
    }

    @Persistent
    public Collection getCollectionFollowOwner() {
        return collectionFollowOwner;
    }

    public void setCollectionFollowOwner(Collection collectionFollowOwner) {
        this.collectionFollowOwner = collectionFollowOwner;
    }

    public static List<User> followToUserList(List<Follow> follows)
    {
        List<User> rval = new ArrayList<>();

        for (Follow follow : follows)
        {
            rval.add(follow.getUserFollower());
        }

        return rval;

    }

    public static List<User> ownerToUserList(List<Follow> follows)
    {
        List<User> rval = new ArrayList<>();

        for (Follow follow : follows)
        {
            rval.add(follow.getUserFollowOwner());
        }

        return rval;

    }

    public static List<String> getFollowFetchGroups()
    {
        List<String> rval = new ArrayList<>();

        rval.add("userFollower");

        return rval;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Follow)
        {
            return ((Follow) obj).getFollowId().equals(this.getFollowId());
        }
        else if (obj instanceof User)
        {
            return userFollower.getUserId().equals(((User) obj).getUserId());
        }
        else
            return false;
    }
}
