package com.acomp.khobarapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    ImageView img;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        img = findViewById(R.id.GoBackIcon);
    }
    public void gotoNext(View view){
        Intent dsp = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(dsp);
    }
    public void goback(View view){
        Intent ic = new Intent(LoginActivity.this,accountactivity.class);
        startActivity(ic);
    }
}