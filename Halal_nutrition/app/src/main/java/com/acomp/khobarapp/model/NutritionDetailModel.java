package com.acomp.khobarapp.model;

import java.util.ArrayList;

public class NutritionDetailModel {
    private String code = null;
    private String name = null;
    private String type = null;
    private Double value = null;
    private String unitCode = null;
    private Double percentage = null;
    private Integer order = null;
    private Integer urutChild = 1;
    private ArrayList<NutritionDetailModel> child = new ArrayList<NutritionDetailModel>();

    public Integer getUrutChild() {
        return urutChild;
    }

    public void setUrutChild(Integer urutChild) {
        this.urutChild = urutChild;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public ArrayList<NutritionDetailModel> getChild() {
        return child;
    }

    public void setChild(ArrayList<NutritionDetailModel> child) {
        this.child = child;
    }
}
