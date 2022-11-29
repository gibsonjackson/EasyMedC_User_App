package com.anvaishy.easymedc_user_app.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.toolbox.Volley;
import com.anvaishy.easymedc_user_app.R;
import com.anvaishy.easymedc_user_app.model.MedicalPassRequestUser;
import com.anvaishy.easymedc_user_app.model.User;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.concurrent.TimeUnit;
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

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anvaishy.easymedc_user_app.R;
import com.anvaishy.easymedc_user_app.model.Doctor;
import com.anvaishy.easymedc_user_app.model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
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


public class MedicalPassRequestListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore db;
    MedicalPassAdapter adapter;
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
        setContentView(R.layout.activity_medical_pass_request_list);

        Toolbar toolbar = findViewById(R.id.simple_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("My Medical Pass Requests");
        actionBar.setHomeAsUpIndicator(R.drawable.outline_menu_24);
        actionBar.setDisplayHomeAsUpEnabled(true);
        queue = Volley.newRequestQueue(MedicalPassRequestListActivity.this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(
                MedicalPassRequestListActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                MedicalPassRequestListActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] array = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(
                    MedicalPassRequestListActivity.this, array, 99
            );
        }

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

                SOSDialog dialog = new SOSDialog(MedicalPassRequestListActivity.this);
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
                                            .addOnSuccessListener(MedicalPassRequestListActivity.this, new OnSuccessListener<Location>() {
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
                                            .addOnFailureListener(MedicalPassRequestListActivity.this, new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("y u do dis", e.toString());
                                                }
                                            });
                                }

                                catch (SecurityException e) {
                                    Toast.makeText(MedicalPassRequestListActivity.this, "Location permission not granted, sending with default location", Toast.LENGTH_SHORT).show();
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
        recyclerView = findViewById(R.id.medical_pass_request_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(MedicalPassRequestListActivity.this));
        Query query = db.collection("Users").document(emailID).collection("Medical Pass Requests").orderBy("status");
        FirestoreRecyclerOptions<MedicalPassRequestUser> options = new FirestoreRecyclerOptions.Builder<MedicalPassRequestUser>().setQuery(query, MedicalPassRequestUser.class).build();
        adapter = new MedicalPassAdapter(options);
        recyclerView.setAdapter(adapter);
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

    public void goToNewRequest(View view) {
        Intent intent = new Intent(this, NewRequestActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
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