package com.acomp.khobarapp.model;

import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("address")
    private String address;
    @SerializedName("attachmentId")
    private Integer attachmentId;
    @SerializedName("attachmentUrl")
    private String attachmentUrl;

    public UserModel(Integer id, String name, String email, String address,Integer attachmentId,String attachmentUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.attachmentId = attachmentId;
        this.attachmentUrl = attachmentUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }
}
