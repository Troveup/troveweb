package com.troveup.brooklyn.model;

import com.troveup.brooklyn.orm.user.model.User;

import java.util.List;

/**
 * Created by tim on 8/6/15.
 */
public class UserResponse
{
    private List<User> userList;

    public UserResponse()
    {

    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
