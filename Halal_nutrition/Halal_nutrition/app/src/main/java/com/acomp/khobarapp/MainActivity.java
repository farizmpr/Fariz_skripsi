package com.acomp.khobarapp;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    RecyclerView mList1,mList2;
    List<App> appList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        mList1 = findViewById(R.id.list1);
        appList = new ArrayList<>();

        appList.add(new App(R.drawable.halal_info_news,"youtube",80));
        appList.add(new App(R.drawable.nambah_produk_home_ps,"haha",40));
        appList.add(new App(R.drawable.slide_berita_ps,"hihi",40));

        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        mList1.setLayoutManager(manager1);

        LinearLayoutManager manager2 = new LinearLayoutManager(this);
        manager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        mList1.setLayoutManager(manager2);

        CustomAdaptor adaptor1 = new CustomAdaptor(this,appList);
        mList1.setAdapter(adaptor1);
    }
}
