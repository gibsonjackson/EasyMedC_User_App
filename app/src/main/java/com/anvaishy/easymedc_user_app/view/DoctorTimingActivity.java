package com.anvaishy.easymedc_user_app.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.anvaishy.easymedc_user_app.R;
import com.anvaishy.easymedc_user_app.model.Doctor;
import com.anvaishy.easymedc_user_app.model.Document;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DoctorTimingActivity extends AppCompatActivity {

    ListView DocumentListView;
    ArrayList<Doctor> dataModalArrayList;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_timing);
        Log.e("N","Entered Timing Activity");
        Toolbar toolbar = findViewById(R.id.simple_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Doctor Timings");
        actionBar.setHomeAsUpIndicator(R.drawable.outline_menu_24);
        actionBar.setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
        DocumentListView = findViewById(R.id.DocList);
        dataModalArrayList = new ArrayList<>();

        // initializing our variable for firebase
        // firestore and getting its instance.
        db = FirebaseFirestore.getInstance();

        // here we are calling a method
        // to load data in our list view.
        loadDatainListview();
    }

    private void loadDatainListview() {
        db.collection("Doctor").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // after getting the data we are calling on success method
                // and inside this method we are checking if the received
                // query snapshot is empty or not.
                if (!queryDocumentSnapshots.isEmpty()) {
                    // if the snapshot is not empty we are hiding
                    // our progress bar and adding our data in a list.
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        // after getting this list we are passing
                        // that list to our object class.

                        String docname = (String) d.get("name");
                        String docurl = (String) d.get("specialisation");
                        String start = (String) d.get("startTime");
                        String end = (String) d.get("endTime");
                        Doctor dataModal = new Doctor(docname, docurl,start,end);
                        // after getting data from Firebase we are
                        // storing that data in our array list
                        dataModalArrayList.add(dataModal);
                    }
                    // after that we are passing our array list to our adapter class.
                    DoctorTimingAdapter doctorTimingAdapter = new DoctorTimingAdapter(DoctorTimingActivity.this,dataModalArrayList);

                    // after passing this array list to our adapter
                    // class we are setting our adapter to our list view.
                    Log.e("URL",dataModalArrayList.get(0).getSpecialisation()+"--");
                    DocumentListView.setAdapter(doctorTimingAdapter);


                } else {
                    // if the snapshot is empty we are displaying a toast message.
                    Toast.makeText(DoctorTimingActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(e -> {
            // we are displaying a toast message
            // when we get any error from Firebase.
            Toast.makeText(DoctorTimingActivity.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem) {
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

