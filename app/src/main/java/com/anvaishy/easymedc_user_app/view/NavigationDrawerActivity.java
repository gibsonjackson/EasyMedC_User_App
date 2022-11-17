package com.anvaishy.easymedc_user_app.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.anvaishy.easymedc_user_app.R;

public class NavigationDrawerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        String[] pages = {"Doctor List", "Upload Documents", "Medical Pass Requests", "My Profile"};
        ListView listView = findViewById(R.id.navigation_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, pages);
        listView.setAdapter(arrayAdapter);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Intent intent = new Intent(NavigationDrawerActivity.this, DoctorTimingActivity.class);
                    startActivity(intent);
                }

                else if (i == 1) {
                    Intent intent = new Intent(NavigationDrawerActivity.this, DocumentUploadList.class);
                    startActivity(intent);
                }

                else if (i == 2) {
                    Intent intent = new Intent(NavigationDrawerActivity.this, MedicalPassRequestListActivity.class);
                    startActivity(intent);
                }

                else {
                    Intent intent = new Intent(NavigationDrawerActivity.this, StudentProfile.class);
                    startActivity(intent);
                }
            }
        };
        listView.setOnItemClickListener(itemClickListener);
    }

    public void closeDrawer (View view) {

        finish();
        overridePendingTransition(R.anim.slide_left, 0);
    }
}