package com.troveup.brooklyn.controllers.forms;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * Created by tim on 4/21/15.
 */
public class ForgotPasswordReplacementForm
{
    @NotEmpty
    @Size(min=6, message = "Must be at least 6 characters")
    private String password;

    @NotEmpty
    @Size(min=6, message = "Must be at least 6 characters")
    private String confirmPassword;

    @NotEmpty
    private String token;

    public ForgotPasswordReplacementForm()
    {

    }

    public ForgotPasswordReplacementForm(String token)
    {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
