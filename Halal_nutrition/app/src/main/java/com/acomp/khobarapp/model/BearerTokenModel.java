package com.acomp.khobarapp.model;

import com.google.gson.annotations.SerializedName;

public class BearerTokenModel {
    @SerializedName("token")
    private String token;
    public BearerTokenModel(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
