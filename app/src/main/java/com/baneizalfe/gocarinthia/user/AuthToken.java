package com.baneizalfe.gocarinthia.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by baneizalfe on 6/25/16.
 */
public class AuthToken {

    @SerializedName("Id")
    public long id;

    @SerializedName("AuthToken")
    public String authToken;

    @SerializedName("Type")
    public String type;

}
