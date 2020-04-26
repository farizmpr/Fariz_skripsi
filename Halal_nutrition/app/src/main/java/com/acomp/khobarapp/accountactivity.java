package com.acomp.khobarapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class accountactivity extends AppCompatActivity {
    ImageView img;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        img = findViewById(R.id.GoBackIcon);

        TextView textView = (TextView) findViewById(R.id.textView);

        String text = "<font color=##C7C7C7>Don't have an account?</font><font color=#000000>Create Account</font>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            textView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
        }else{
            textView.setText(Html.fromHtml(text));
        }

    }
    public void gotoNext(View view){
        Intent dsp = new Intent(accountactivity.this, LoginActivity.class);
        startActivity(dsp);
    }
    public void gotoForward(View view){
        Intent dp = new Intent(accountactivity.this, RegisterActivity.class);
        startActivity(dp);
    }
    public void goback(View view){
        Intent ic = new Intent(accountactivity.this,MainActivity.class);
        startActivity(ic);
    }
}
