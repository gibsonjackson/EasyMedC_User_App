package com.anvaishy.easymedc_user_app.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.anvaishy.easymedc_user_app.model.User;
import com.anvaishy.easymedc_user_app.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
        actionBar.setHomeAsUpIndicator(R.drawable.outline_menu_24);
        actionBar.setDisplayHomeAsUpEnabled(true);

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

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(emailID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User currentUser = documentSnapshot.toObject(User.class);
                TextView name = findViewById(R.id.name);
                name.append(currentUser.getName());

                TextView email = findViewById(R.id.email);
                email.append(currentUser.getEmail());

                TextView studID = findViewById(R.id.student_id);
                studID.append(currentUser.getStudentID());

                TextView studHostel = findViewById(R.id.hostel);
                if (currentUser.getHostel() != null) studHostel.append(currentUser.getHostel());

                TextView studRoomNo = findViewById(R.id.room_no);
                if (currentUser.getRoomNo() != null) studRoomNo.append(currentUser.getRoomNo());

                TextView studPhoneNo = findViewById(R.id.student_phone);
                if (currentUser.getStudentPhoneNo() != null) studPhoneNo.append(currentUser.getStudentPhoneNo());

                TextView studGuardPhoneNo = findViewById(R.id.guardian_phone);
                if (currentUser.getGuardianPhoneNo() != null) studGuardPhoneNo.append(currentUser.getGuardianPhoneNo());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public void goToEditProfile(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, NavigationDrawerActivity.class);
                startActivity(intent);
                overridePendingTransition(0, R.anim.slide_right);
                return true;
            case R.id.house_button:
                Intent intent1 = new Intent(this, HomePageActivity.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}