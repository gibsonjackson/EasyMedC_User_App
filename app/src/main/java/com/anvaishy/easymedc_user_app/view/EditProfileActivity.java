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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anvaishy.easymedc_user_app.model.User;
import com.anvaishy.easymedc_user_app.R;
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

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

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
        queue = Volley.newRequestQueue(EditProfileActivity.this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(
                EditProfileActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                EditProfileActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] array = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(
                    EditProfileActivity.this, array, 99
            );
        }

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

        // To go in StudentProfileVM
      
        FloatingActionButton sos = findViewById(R.id.sos_button);

        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SOSDialog dialog = new SOSDialog(EditProfileActivity.this);
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
                                            .addOnSuccessListener(EditProfileActivity.this, new OnSuccessListener<Location>() {
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
                                            .addOnFailureListener(EditProfileActivity.this, new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("y u do dis", e.toString());
                                                }
                                            });
                                }

                                catch (SecurityException e) {
                                    Toast.makeText(EditProfileActivity.this, "Location permission not granted, sending with default location", Toast.LENGTH_SHORT).show();
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
        if (str.length() == 3) {
            return Character.isDigit(str.charAt(0)) && Character.isDigit(str.charAt(1)) && Character.isDigit(str.charAt(2));
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