package com.troveup.brooklyn.model;

import com.troveup.brooklyn.orm.user.model.User;

/**
 * Created by tim on 2/11/16.
 */
public class UserAuthResponse extends GenericAjaxResponse
{
    private User user;

    public UserAuthResponse(Boolean isSuccesful, String message) {
        super.setIsSuccess(isSuccesful);
        super.setErrorMessage(message);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
