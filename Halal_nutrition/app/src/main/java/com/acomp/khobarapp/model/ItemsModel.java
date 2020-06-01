package com.acomp.khobarapp.model;

import java.util.ArrayList;

public class ItemsModel {
    private String code = "";
    private String name = "";
    private String manufacture = "";
    private String ingredient = "";
    private String status;
    private String organization = "";
    private ArrayList<AttachmentModel> attachmentModels = new ArrayList<AttachmentModel>();
    private ArrayList<CertificateRowModel> certificateRowModels = new ArrayList<CertificateRowModel>();
    private NutritionModel nutrition = new NutritionModel();

    public NutritionModel getNutrition() {
        return nutrition;
    }

    public void setNutrition(NutritionModel nutrition) {
        this.nutrition = nutrition;
    }

    public ArrayList<CertificateRowModel> getCertificateRowModels() {
        return certificateRowModels;
    }

    public void setCertificateRowModels(ArrayList<CertificateRowModel> certificateRowModels) {
        this.certificateRowModels = certificateRowModels;
    }

    public ArrayList<AttachmentModel> getAttachmentModels() {
        return attachmentModels;
    }

    public void setAttachmentModels(ArrayList<AttachmentModel> attachmentModels) {
        this.attachmentModels = attachmentModels;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
