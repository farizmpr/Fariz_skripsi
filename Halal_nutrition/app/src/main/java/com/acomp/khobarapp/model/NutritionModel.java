package com.acomp.khobarapp.model;

import java.util.ArrayList;

public class NutritionModel {

    private ArrayList<NutritionDetailModel> serving = new ArrayList<NutritionDetailModel>();
    private ArrayList<NutritionDetailModel> dailyValue = new ArrayList<NutritionDetailModel>();

    public ArrayList<NutritionDetailModel> getServing() {
        return serving;
    }

    public void setServing(ArrayList<NutritionDetailModel> serving) {
        this.serving = serving;
    }

    public ArrayList<NutritionDetailModel> getDailyValue() {
        return dailyValue;
    }

    public void setDailyValue(ArrayList<NutritionDetailModel> dailyValue) {
        this.dailyValue = dailyValue;
    }
}
