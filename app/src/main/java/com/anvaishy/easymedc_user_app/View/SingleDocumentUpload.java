package com.anvaishy.easymedc_user_app.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.anvaishy.easymedc_user_app.R;

import java.util.HashMap;
import java.util.Map;


public class SingleDocumentUpload extends AppCompatActivity {

    Button upload;
    Uri imageuri = null;
    EditText ed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_document_upload);
        upload = findViewById(R.id.selectorFile);
        ed = findViewById(R.id.documentname);
        // After Clicking on this we will be
        // redirected to choose pdf
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                // We will be redirected to choose pdf
                galleryIntent.setType("application/pdf");
                startActivityForResult(galleryIntent, 1);
            }
        });
    }

    ProgressDialog dialog;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            imageuri = data.getData();
        }
    }

    public void Upload(View view) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading");

        // this will show message uploading
        // while pdf is uploading
        dialog.show();

        final String timestamp = "" + System.currentTimeMillis();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final String messagePushID = timestamp;
        Toast.makeText(this, imageuri.toString(), Toast.LENGTH_SHORT).show();

        // Here we are uploading the pdf in firebase storage with the name of current time
        final StorageReference filepath = storageReference.child(messagePushID + "." + "pdf");
        Toast.makeText(this, filepath.getName(), Toast.LENGTH_SHORT).show();
        filepath.putFile(imageuri).continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return filepath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // After uploading is done it progress
                    // dialog box will be dismissed
                    dialog.dismiss();
                    Uri uri = task.getResult();
                    String myurl;
                    myurl = uri.toString();
                    FirebaseAuth firebaseAuth;
                    firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                    String emailID = firebaseUser.getEmail();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference docRef = db.collection("Users").document(emailID)
                            .collection("Document");
                    Map<String, Object> data = new HashMap<>();
                    data.put("name",ed.getText().toString());
                    data.put("link",myurl);
                    docRef.document().set(data);

                    Toast.makeText(SingleDocumentUpload.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    Toast.makeText(SingleDocumentUpload.this, "UploadedFailed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}