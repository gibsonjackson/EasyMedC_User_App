package com.anvaishy.easymedc_user_app.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.anvaishy.easymedc_user_app.Model.User;
import com.anvaishy.easymedc_user_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        Toolbar toolbar = findViewById(R.id.simple_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Student Profile");

        // To go in StudentProfileVM
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        String emailID = firebaseUser.getEmail();
        StringBuilder studentID = new StringBuilder();
        for (int i = 0; i < 9; i++)
        {
            studentID.append(emailID.charAt(i));
        }

        TextView name = findViewById(R.id.name);
        name.append(firebaseUser.getDisplayName());

        TextView email = findViewById(R.id.email);
        email.append(firebaseUser.getEmail());

        TextView studID = findViewById(R.id.student_id);
        studID.append(studentID.toString());
    }

    public void goToEditProfile(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }
}