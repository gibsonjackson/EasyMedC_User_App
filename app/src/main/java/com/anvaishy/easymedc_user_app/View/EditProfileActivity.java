package com.anvaishy.easymedc_user_app.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.anvaishy.easymedc_user_app.R;

import java.util.List;

public class EditProfileActivity extends AppCompatActivity {

    // Just for example, data to be fetched from database
    String[] array = {"Select Hostel", "Vyas", "Valmiki", "Shankar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = findViewById(R.id.simple_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Profile");
        Spinner spinner = findViewById(R.id.hostel_options);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
        spinner.setAdapter(adapter);
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, StudentProfile.class);
        startActivity(intent);
    }
}