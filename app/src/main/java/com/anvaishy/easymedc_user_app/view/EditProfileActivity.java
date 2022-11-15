package com.anvaishy.easymedc_user_app.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.anvaishy.easymedc_user_app.model.User;
import com.anvaishy.easymedc_user_app.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfileActivity extends AppCompatActivity {

    // Just for example, data to be fetched from database
    String[] array = {"Select Hostel", "Vyas", "Valmiki", "Shankar"};
    Spinner spinner;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    StringBuilder studentID;
    User currentUser;
    String emailID;
    EditText room_number;
    EditText student_phone;
    EditText guardian_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = findViewById(R.id.simple_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Profile");
        actionBar.setDisplayHomeAsUpEnabled(true);

        // UI variable initializations
        spinner = findViewById(R.id.hostel_options);
        room_number = findViewById(R.id.room_no_edit);
        student_phone = findViewById(R.id.student_phone_edit);
        guardian_phone = findViewById(R.id.guardian_phone_edit);
        spinner.setFocusable(true);
        spinner.setFocusableInTouchMode(true);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
        spinner.setAdapter(adapter);

        // Firebase Variable Initializations
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        emailID = firebaseUser.getEmail();
        studentID = new StringBuilder();
        for (int i = 0; i < 9; i++)
        {
            studentID.append(emailID.charAt(i));
        }
        DocumentReference docRef = db.collection("Users").document(emailID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentUser = documentSnapshot.toObject(User.class);

                // Populating UI with values
                if (currentUser.getHostel() != null) {
                    int spinnerPosition = adapter.getPosition(currentUser.getHostel());
                    spinner.setSelection(spinnerPosition);
                }
                if (currentUser.getRoomNo() != null) room_number.setText(currentUser.getRoomNo());
                if (currentUser.getStudentPhoneNo() != null) student_phone.setText(currentUser.getStudentPhoneNo());
                if (currentUser.getGuardianPhoneNo() != null) guardian_phone.setText(currentUser.getGuardianPhoneNo());
            }
        });
    }

    public void updateProfile(View view) {
        // Handling empty fields
        if (spinner.getSelectedItem().toString().equals("Select Hostel")) {
            Toast.makeText(this, "Please select a hostel!", Toast.LENGTH_SHORT).show();
            spinner.requestFocus();
        }

        else if (room_number.getText().toString().length() == 0) {
            Toast.makeText(this, "Please enter a valid room number!", Toast.LENGTH_SHORT).show();
            room_number.requestFocus();
        }

        else if (student_phone.getText().toString().length() == 0) {
            Toast.makeText(this, "Please enter valid Student Phone Number!", Toast.LENGTH_SHORT).show();
            student_phone.requestFocus();
        }

        else if (guardian_phone.getText().toString().length() == 0) {
            Toast.makeText(this, "Please enter valid Guardian Phone Number!", Toast.LENGTH_SHORT).show();
            guardian_phone.requestFocus();
        }

        // Handling erroneous inputs
        else if (!validateRoomNo(room_number.getText().toString())) {
            Toast.makeText(this, "Please enter a valid room number!", Toast.LENGTH_SHORT).show();
            room_number.requestFocus();
        }

        else if (!validatePhoneNo(student_phone.getText().toString())) {
            Toast.makeText(this, "Please enter valid Student Phone Number!", Toast.LENGTH_SHORT).show();
            student_phone.requestFocus();
        }

        else if (!validatePhoneNo(guardian_phone.getText().toString())) {
            Toast.makeText(this, "Please enter valid Guardian Phone Number!", Toast.LENGTH_SHORT).show();
            guardian_phone.requestFocus();
        }

        else {


            DocumentReference docRef = db.collection("Users").document(emailID);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User currentUser = documentSnapshot.toObject(User.class);
                    String studHostel = spinner.getSelectedItem().toString();
                    currentUser.setHostel(studHostel);

                    String studRoomNo = room_number.getText().toString();
                    currentUser.setRoomNo(studRoomNo);

                    String studPhoneNo = student_phone.getText().toString();
                    currentUser.setStudentPhoneNo(studPhoneNo);

                    String studGuardPhoneNo = guardian_phone.getText().toString();
                    currentUser.setGuardianPhoneNo(studGuardPhoneNo);

                    docRef.set(currentUser);
                    Toast.makeText(EditProfileActivity.this, "Profile Successfully Updated!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Needs some more work in hostel room code checking
    public boolean validateRoomNo (String str) {
        if (str.length() == 4) {
            return Character.isUpperCase(str.charAt(0)) && Character.isDigit(str.charAt(1)) && Character.isDigit(str.charAt(2)) && Character.isDigit(str.charAt(3));
        }
        else if (str.length() == 5) {
            return Character.isUpperCase(str.charAt(0)) && Character.isUpperCase(str.charAt(1)) && Character.isDigit(str.charAt(2)) && Character.isDigit(str.charAt(3)) && Character.isDigit(str.charAt(4));
        }
        return false;
    }

    public boolean validatePhoneNo (String str) {
        if (str.length() == 10) {
            try {
                long val = Long.parseLong(str);
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, StudentProfile.class);
                startActivity(intent);
                finish();
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