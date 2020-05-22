package com.acomp.khobarapp.model;

import java.math.BigInteger;
import java.util.ArrayList;

public class VenuesModel {
    private BigInteger id;
    private String code = "";
    private String name = "";
    private String foodType = "";
    private String restaurantStatusId = "";
    private String address = "";
    private String longitude = "";
    private String latitude = "";
    private String status;
    private String phoneNumber = "";
    private String restaurantStatus = "";
    private Double votes;
    private ArrayList<AttachmentModel> attachmentModels = new ArrayList<AttachmentModel>();

    public Double getVotes() {
        return votes;
    }

    public void setVotes(Double votes) {
        this.votes = votes;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getRestaurantStatusId() {
        return restaurantStatusId;
    }

    public void setRestaurantStatusId(String restaurantStatusId) {
        this.restaurantStatusId = restaurantStatusId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRestaurantStatus() {
        return restaurantStatus;
    }

    public void setRestaurantStatus(String restaurantStatus) {
        this.restaurantStatus = restaurantStatus;
    }

    public ArrayList<AttachmentModel> getAttachmentModels() {
        return attachmentModels;
    }

    public void setAttachmentModels(ArrayList<AttachmentModel> attachmentModels) {
        this.attachmentModels = attachmentModels;
    }
}
