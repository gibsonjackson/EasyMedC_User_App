package com.anvaishy.easymedc_user_app.view;

import static java.lang.String.valueOf;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.anvaishy.easymedc_user_app.R;
import com.anvaishy.easymedc_user_app.model.MedicalPassRequestGlobal;
import com.anvaishy.easymedc_user_app.model.MedicalPassRequestUser;
import com.anvaishy.easymedc_user_app.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class NewRequestActivity extends AppCompatActivity {

    EditText description;
    EditText arrivalTime;
    EditText departTime;

    Timestamp arrivalTimestamp;
    Timestamp departTimestamp;

    FirebaseFirestore db;
    String emailID;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);

        Toolbar toolbar = findViewById(R.id.simple_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create New Pass");

        db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        emailID = firebaseUser.getEmail();

        description = findViewById(R.id.description_fill);
        arrivalTime = findViewById(R.id.arrival_time_fill);
        departTime = findViewById(R.id.depart_time_fill);

        arrivalTime.setOnClickListener(new View.OnClickListener() {
            Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Calcutta"));
            LocalDateTime localDateTime = LocalDateTime.now();
            final int year = localDateTime.getYear();
            final int month = localDateTime.getMonthValue();
            final int date = localDateTime.getDayOfMonth();
            final int hours = localDateTime.getHour();
            final int minutes = localDateTime.getMinute();
            int sYear = year;
            int sMonth = month;
            int sDate = date;
            int sHour = hours;
            int sMinute = minutes;
            @Override
            public void onClick(View view) {
                arrivalTime.setText("");
                TimePickerDialog timePickerDialog = new TimePickerDialog(NewRequestActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        sHour = i;
                        sMinute = i1;
                        calendar.set(sYear, sMonth, sDate, sHour, sMinute);
                        Date date = calendar.getTime();
                        Timestamp timestamp = new Timestamp(date);
                        arrivalTimestamp = timestamp;
                        arrivalTime.setText(timestamp.toDate().toString());
                    }
                }, sHour, sMinute, false);
                timePickerDialog.show();
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewRequestActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        sYear = i;
                        sMonth = i1;
                        sDate = i2;
                    }
                }, sYear, sMonth - 1, sDate);
                datePickerDialog.show();
            }
        });

        departTime.setOnClickListener(new View.OnClickListener() {
            Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Calcutta"));
            LocalDateTime localDateTime = LocalDateTime.now();
            final int year = localDateTime.getYear();
            final int month = localDateTime.getMonthValue();
            final int date = localDateTime.getDayOfMonth();
            final int hours = localDateTime.getHour();
            final int minutes = localDateTime.getMinute();
            int sYear = year;
            int sMonth = month;
            int sDate = date;
            int sHour = hours;
            int sMinute = minutes;
            @Override
            public void onClick(View view) {
                departTime.setText("");
                TimePickerDialog timePickerDialog = new TimePickerDialog(NewRequestActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        sHour = i;
                        sMinute = i1;
                        calendar.set(sYear, sMonth, sDate, sHour, sMinute);
                        Date date = calendar.getTime();
                        Timestamp timestamp = new Timestamp(date);
                        departTimestamp = timestamp;
                        StringBuilder timeSt = new StringBuilder(timestamp.toDate().toString());
                        timeSt.setCharAt(20, 'I');
                        timeSt.setCharAt(21, 'S');
                        timeSt.setCharAt(22, 'T');
                        departTime.setText(timeSt.toString());
                    }
                }, sHour, sMinute, false);
                timePickerDialog.show();
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewRequestActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        sYear = i;
                        sMonth = i1;
                        sDate = i2;
                    }
                }, sYear, sMonth - 1, sDate);
                datePickerDialog.show();
            }
        });
    }

    public void createRequest(View view) {
        Log.e("Desc: ",valueOf(description.getText().length()));
        Log.e("Desc: ",valueOf(departTime.getText().length()));
        Log.e("Desc: ",valueOf(arrivalTime.getText().length()));
        if (description.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter description", Toast.LENGTH_SHORT).show();
            description.requestFocus();
        }
        else if (departTime.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter departure time", Toast.LENGTH_SHORT).show();
            departTime.requestFocus();
        }
        else if (arrivalTime.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter arrival time", Toast.LENGTH_SHORT).show();
            arrivalTime.requestFocus();
        }

        else {
            Date date = new Date(System.currentTimeMillis());
            Timestamp currentTime = new Timestamp(date);
            if (departTimestamp.compareTo(currentTime) < 0) {
                Toast.makeText(this, "Please enter valid departure time", Toast.LENGTH_SHORT).show();
            }

            else if (arrivalTimestamp.compareTo(departTimestamp) < 0) {
                Toast.makeText(this, "Please enter valid arrival time", Toast.LENGTH_SHORT).show();
            }

            else {
                DocumentReference docRef = db.collection("Users").document(emailID);
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        MedicalPassRequestUser userRequest = documentSnapshot.toObject(MedicalPassRequestUser.class);
                        userRequest.setDescription(description.getText().toString());
                        userRequest.setArrival(arrivalTimestamp);
                        userRequest.setDepart(departTimestamp);
                        docRef.collection("Medical Pass Requests").add(userRequest);
                        Toast.makeText(NewRequestActivity.this, "Request Created Successfully!", Toast.LENGTH_SHORT).show();
                    }
                });

                DocumentReference docRef1 = db.collection("Users").document(emailID);
                docRef1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User currentUser = documentSnapshot.toObject(User.class);
                        MedicalPassRequestGlobal globalRequest = new MedicalPassRequestGlobal();
                        globalRequest.setName(currentUser.getName());
                        globalRequest.setUid(currentUser.getStudentID());
                        globalRequest.setPhoneNo(currentUser.getStudentPhoneNo());
                        globalRequest.setDescription(description.getText().toString());
                        globalRequest.setStatus(0);
                        globalRequest.setArrival(arrivalTimestamp);
                        globalRequest.setDepart(departTimestamp);
                        db.collection("Medical Pass Requests").add(globalRequest);
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MedicalPassRequestListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.house_button:
                Intent intent = new Intent(this, HomePageActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}