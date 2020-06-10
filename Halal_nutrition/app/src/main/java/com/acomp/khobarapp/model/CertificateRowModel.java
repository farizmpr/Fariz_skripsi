package com.acomp.khobarapp.model;

public class CertificateRowModel {
    private String title = "";
    private String type = "";
    private Integer typeId;
    private Integer statusId;
    private String expiredDate;
    private String code;
    private String halalStatus = "";

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public String getHalalStatus() {
        return halalStatus;
    }

    public void setHalalStatus(String halalStatus) {
        this.halalStatus = halalStatus;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
