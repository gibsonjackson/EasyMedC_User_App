package com.anvaishy.easymedc_user_app.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.anvaishy.easymedc_user_app.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anvaishy.easymedc_user_app.model.User;
import com.anvaishy.easymedc_user_app.R;
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

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomePageActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.simple_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.outline_menu_24);
        actionBar.setDisplayHomeAsUpEnabled(true);
        queue = Volley.newRequestQueue(HomePageActivity.this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(
                HomePageActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                HomePageActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] array = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(
                    HomePageActivity.this, array, 99
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

                SOSDialog dialog = new SOSDialog(HomePageActivity.this);
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
                                            .addOnSuccessListener(HomePageActivity.this, new OnSuccessListener<Location>() {
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
                                            .addOnFailureListener(HomePageActivity.this, new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("y u do dis", e.toString());
                                                }
                                            });
                                }

                                catch (SecurityException e) {
                                    Toast.makeText(HomePageActivity.this, "Location permission not granted, sending with default location", Toast.LENGTH_SHORT).show();
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
    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, NavigationDrawerActivity.class);
                startActivity(intent);
                overridePendingTransition(0, R.anim.slide_right);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public void GoToDoctorList(View view) {
        Intent intent = new Intent(HomePageActivity.this,DoctorTimingActivity.class);
        startActivity(intent);
    }

    public void MedicalHistory(View view) {
        Intent intent = new Intent(HomePageActivity.this,DocumentUploadList.class);
        startActivity(intent);
    }

    public void MedicalPass(View view) {
        Intent intent = new Intent(HomePageActivity.this,MedicalPassRequestListActivity.class);
        startActivity(intent);
    }

    public void Profile(View view) {
        Intent intent = new Intent(HomePageActivity.this,StudentProfile.class);
        startActivity(intent);
    }
}