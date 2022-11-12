package com.anvaishy.easymedc_user_app.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.anvaishy.easymedc_user_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class EditProfileActivity extends AppCompatActivity {

    // Just for example, data to be fetched from database
    String[] array = {"Select Hostel", "Vyas", "Valmiki", "Shankar"};
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = findViewById(R.id.simple_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Profile");
        spinner = findViewById(R.id.hostel_options);
        spinner.setFocusable(true);
        spinner.setFocusableInTouchMode(true);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
        spinner.setAdapter(adapter);
    }

    public void updateProfile(View view) {
        EditText room_number = findViewById(R.id.room_no_edit);
        EditText student_phone = findViewById(R.id.student_phone_edit);
        EditText guardian_phone = findViewById(R.id.guardian_phone_edit);

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

    public void goBack(View view) {
        Intent intent = new Intent(this, StudentProfile.class);
        intent.setAction("From Edit Profile");
        startActivity(intent);
        finish();
    }
}