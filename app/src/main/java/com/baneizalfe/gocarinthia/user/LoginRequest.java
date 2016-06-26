package com.baneizalfe.gocarinthia.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by baneizalfe on 6/25/16.
 */
public class LoginRequest {

    @Expose
    @SerializedName("Email")
    public String email;

    @Expose
    @SerializedName("Password")
    public String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
