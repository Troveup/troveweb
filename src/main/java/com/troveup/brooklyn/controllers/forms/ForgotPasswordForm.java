package com.troveup.brooklyn.controllers.forms;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by tim on 4/20/15.
 */
public class ForgotPasswordForm
{
    @NotEmpty
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
