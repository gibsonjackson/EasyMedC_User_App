package com.anvaishy.easymedc_user_app.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.anvaishy.easymedc_user_app.model.User;
import com.anvaishy.easymedc_user_app.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class GoogleSignin extends AppCompatActivity {
    SignInButton signInButton;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_signin);

        signInButton = findViewById(R.id.bt_sign_in);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("2350897725-kttff7vupbvvs75m7hrif1hhnrdoc3gp.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(GoogleSignin.this, googleSignInOptions);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, 100);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            startActivity(new Intent(this, HomePageActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100)
        {
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (signInAccountTask.isSuccessful())
            {
                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    if (googleSignInAccount != null)
                    {
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        firebaseAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful())
                                        {
                                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                            String emailID = firebaseUser.getEmail();
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                                            // IMP: NEED TO CHECK IF USER ALREADY EXISTS OR NOT
                                            StringBuilder studentID = new StringBuilder();
                                            for (int i = 0; i < 9; i++)
                                            {
                                                studentID.append(emailID.charAt(i));
                                            }

                                            CollectionReference collectionReference = db.collection("Users");
                                            DocumentReference docRef = collectionReference.document(emailID);
                                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot documentSnapshot = task.getResult();
                                                        if (!documentSnapshot.exists()) {
                                                            User user = new User();
                                                            user.setName(firebaseUser.getDisplayName());
                                                            user.setEmail(firebaseUser.getEmail());
                                                            user.setStudentID(studentID.toString());
                                                            db.collection("Users").document(emailID).set(user);
                                                        }
                                                    }
                                                }
                                            });
                                            
                                            try {
                                                TimeUnit.SECONDS.sleep(3);
                                            }
                                            catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            startActivity(new Intent(GoogleSignin.this, StudentProfile.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                            finish();
                                        }
                                        else
                                        {
                                            displayToast("Authentication failed!");
                                        }
                                    }
                                });
                    }
                }
                catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}