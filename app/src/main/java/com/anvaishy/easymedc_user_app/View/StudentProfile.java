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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.Document;

import org.w3c.dom.Text;

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
        Button Document = findViewById(R.id.view_medical_history);
        Document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentProfile.this,com.anvaishy.easymedc_user_app.View.DocumentUploadList.class );
                startActivity(intent);
            }
        });
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

    public void goToEditProfile(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }
}