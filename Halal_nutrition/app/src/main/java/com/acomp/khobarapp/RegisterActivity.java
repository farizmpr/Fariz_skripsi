package com.acomp.khobarapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    Button btn;
    ImageView img;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        img = findViewById(R.id.GoBackIcon);
        btn = findViewById(R.id.joinus);
    }

    public void goback(View view){
        Intent ic = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(ic);
    }
}
