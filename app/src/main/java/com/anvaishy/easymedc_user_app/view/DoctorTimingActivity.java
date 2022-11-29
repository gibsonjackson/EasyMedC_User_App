package com.anvaishy.easymedc_user_app.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anvaishy.easymedc_user_app.R;
import com.anvaishy.easymedc_user_app.model.Doctor;
import com.anvaishy.easymedc_user_app.model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorTimingActivity extends AppCompatActivity {

    ListView DocumentListView;
    ArrayList<Doctor> dataModalArrayList;
    FirebaseFirestore db;
    private RequestQueue queue;
    private Boolean locGrant = false;
    private FusedLocationProviderClient fusedLocationClient;
    private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    private String serverKey = "key=AAAAAIwf2j0:APA91bE2zUb4Lj1o7p4yYlbgaKZwPjtuI0v5oEef6HOJPui5BMYslwTYeu-a1_265v3uK7Nw_mQvFZIlIIig5HyBJ8bUiDLAbHt4EF94d85485o_qWPqKIQgbc0S9Qq33v8BvyN7ol-0";
    private String contentType = "application/json";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED) locGrant = true;
    }
    
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
        queue = Volley.newRequestQueue(DoctorTimingActivity.this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(
                DoctorTimingActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                DoctorTimingActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] array = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(
                    DoctorTimingActivity.this, array, 99
            );
        }
        db = FirebaseFirestore.getInstance();
        DocumentListView = findViewById(R.id.DocList);
        dataModalArrayList = new ArrayList<>();

        // initializing our variable for firebase
        // firestore and getting its instance.
        db = FirebaseFirestore.getInstance();
        // To go in StudentProfileVM
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        String emailID = firebaseUser.getEmail();
        StringBuilder studentID = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            studentID.append(emailID.charAt(i));
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FloatingActionButton sos = findViewById(R.id.sos_button);
        DocumentReference docRef = db.collection("Users").document(emailID);

        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SOSDialog dialog = new SOSDialog(DoctorTimingActivity.this);
                dialog.setListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> request = new HashMap<>();
                        TextView desc = dialog.findViewById(R.id.description);

                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                User currentUser = documentSnapshot.toObject(User.class);

                                request.put("Name", currentUser.getName());
                                request.put("UID", currentUser.getStudentID());
                                request.put("Status", false);
                                if (currentUser.getStudentPhoneNo() != null)
                                    request.put("Phone No", currentUser.getStudentPhoneNo());
                                else request.put("Phone No", "N/A");
                                request.put("time", new Timestamp(new Date()));
                                request.put("Description", desc.getText().toString());
                                request
                                        .put("location", new GeoPoint(
                                                17.545052463780358,
                                                78.57185945507129)
                                        );

                                try {
                                    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                                                @NonNull
                                                @Override
                                                public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                                                    return null;
                                                }

                                                @Override
                                                public boolean isCancellationRequested() {
                                                    return false;
                                                }
                                            })
                                            .addOnSuccessListener(DoctorTimingActivity.this, new OnSuccessListener<Location>() {
                                                @Override
                                                public void onSuccess(Location location) {
                                                    Log.d("ok", "yay");
                                                    if (location != null) request
                                                            .put("location", new GeoPoint(
                                                                    location.getLatitude(),
                                                                    location.getLongitude())
                                                            );
                                                    db.collection("Ambulance Requests").add(request);
                                                }
                                            })
                                            .addOnFailureListener(DoctorTimingActivity.this, new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("y u do dis", e.toString());
                                                }
                                            });
                                }

                                catch (SecurityException e) {
                                    Toast.makeText(DoctorTimingActivity.this, "Location permission not granted, sending with default location", Toast.LENGTH_SHORT).show();
                                    db.collection("Ambulance Requests").add(request);
                                }


                                sendNotif(request);
                            }
                        });

                        dialog.dismiss();

                    }
                });

                dialog.show();
            }
        });

        // here we are calling a method
        // to load data in our list view.
        loadDatainListview();
    }

    private void sendNotif(Map<String, Object> request) {
        String topic = "/topics/sos";

        JSONObject notif = new JSONObject();
        JSONObject notifBody = new JSONObject();

        try {
            notifBody.put("title", "SOS Request");
            notifBody.put("message", request.get("Description"));
            notif.put("to", topic);
            notif.put("data", notifBody);
        }

        catch (Exception e) {
            Log.i("TAG", "error: " + e.toString());
        }

        JsonObjectRequest req = new JsonObjectRequest(FCM_API, notif, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("TAG", "onResponse: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("TAG", "onError: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", serverKey);
                headers.put("Content-Type", contentType);
                return headers;
            }
        };

        queue.add(req);
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
                        Doctor dataModal = new Doctor(docname, docurl,start,end, d.getId());
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

