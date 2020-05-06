package com.acomp.khobarapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainFoodDetil extends AppCompatActivity {
    RecyclerView mList1,mList2;
    List<fooddetil> appList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detil_venue);

        getSupportActionBar().hide();

        mList1 = findViewById(R.id.feature_recycler);
        appList = new ArrayList<>();

        appList.add(new fooddetil(R.drawable.waffle_background,"youtube",80));
        appList.add(new fooddetil(R.drawable.waffle_background,"haha",40));
        appList.add(new fooddetil(R.drawable.waffle_background,"hihi",40));

        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        mList1.setLayoutManager(manager1);

        LinearLayoutManager manager2 = new LinearLayoutManager(this);
        manager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        mList1.setLayoutManager(manager2);

        FoodDetilCustomAdaptor adaptor1 = new FoodDetilCustomAdaptor(this,appList);
        mList1.setAdapter(adaptor1);

    }
}
