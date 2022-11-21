package com.anvaishy.easymedc_user_app.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.anvaishy.easymedc_user_app.R;
import com.google.firebase.Timestamp;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class NewRequestActivity extends AppCompatActivity {

    EditText description;
    EditText arrivalTime;
    EditText departTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);

        Toolbar toolbar = findViewById(R.id.simple_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create New Pass");

        description = findViewById(R.id.description_fill);
        arrivalTime = findViewById(R.id.arrival_time_fill);
        departTime = findViewById(R.id.depart_time_fill);

        arrivalTime.setOnClickListener(new View.OnClickListener() {
            Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.MONTH);
            final int date = calendar.get(Calendar.DATE);
            final int hours = calendar.get(Calendar.HOUR);
            final int minutes = calendar.get(Calendar.MINUTE);
            int sYear = year;
            int sMonth = month;
            int sDate = date;
            int sHour = hours;
            int sMinute = minutes;
            boolean dateSet = false;
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
                        arrivalTime.setText(timestamp.toDate().toString());
                    }
                }, hours, minutes, false);
                timePickerDialog.show();
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewRequestActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        sYear = i;
                        sMonth = i1;
                        sDate = i2;
                    }
                }, year, month, date);
                datePickerDialog.show();
            }
        });

        departTime.setOnClickListener(new View.OnClickListener() {
            Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.MONTH);
            final int date = calendar.get(Calendar.DATE);
            final int hours = calendar.get(Calendar.HOUR);
            final int minutes = calendar.get(Calendar.MINUTE);
            int sYear, sMonth, sDate, sHour, sMinute;
            boolean dateSet = false;
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
                        departTime.setText(timestamp.toDate().toString());
                    }
                }, hours, minutes, false);
                timePickerDialog.show();
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewRequestActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        sYear = i;
                        sMonth = i1;
                        sDate = i2;
                        dateSet = true;
                    }
                }, year, month, date);
                datePickerDialog.show();
            }
        });
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