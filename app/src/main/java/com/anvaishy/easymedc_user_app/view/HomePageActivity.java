package com.anvaishy.easymedc_user_app.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.anvaishy.easymedc_user_app.R;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.simple_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.outline_menu_24);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, NavigationDrawerActivity.class);
                startActivity(intent);
                overridePendingTransition(0, R.anim.slide_right);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public void GoToDoctorList(View view) {
        Intent intent = new Intent(HomePageActivity.this,DoctorTimingActivity.class);
        startActivity(intent);
    }

    public void MedicalHistory(View view) {
        Intent intent = new Intent(HomePageActivity.this,DocumentUploadList.class);
        startActivity(intent);
    }

    public void MedicalPass(View view) {
        Intent intent = new Intent(HomePageActivity.this,MedicalPassRequestListActivity.class);
        startActivity(intent);
    }

    public void Profile(View view) {
        Intent intent = new Intent(HomePageActivity.this,StudentProfile.class);
        startActivity(intent);
    }
}