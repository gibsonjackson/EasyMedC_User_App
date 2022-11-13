package com.anvaishy.easymedc_user_app.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.anvaishy.easymedc_user_app.R;

public class DocumentUploadList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload_list);
        Toolbar toolbar = findViewById(R.id.simple_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Medical History");

    }

    public void goToSelectFile(View view) {
        Intent intent = new Intent(this,SingleDocumentUpload.class);
        startActivity(intent);
    }
}