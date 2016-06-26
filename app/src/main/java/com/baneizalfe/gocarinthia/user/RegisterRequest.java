package com.baneizalfe.gocarinthia.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by baneizalfe on 6/25/16.
 */
public class RegisterRequest {

    @Expose
    @SerializedName("Email")
    public String email;

    @Expose
    @SerializedName("Name")
    public String name;

    @Expose
    @SerializedName("BirthYear")
    public String birthYear;

    @Expose
    @SerializedName("Password")
    public String password;

    public RegisterRequest(String name, String birthYear, String email, String password) {
        this.name = name;
        this.birthYear = birthYear;
        this.email = email;
        this.password = password;
    }
}
