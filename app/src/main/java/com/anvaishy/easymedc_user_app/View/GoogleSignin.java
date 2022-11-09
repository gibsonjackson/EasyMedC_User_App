package com.anvaishy.easymedc_user_app.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.anvaishy.easymedc_user_app.R;

public class GoogleSignin extends AppCompatActivity {
//This is going to be just the SplashScreen hence no ViewModel and Model for this page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_signin);

    }
}